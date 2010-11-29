'''
Created on 27 Nov 2010

@author: anj
'''

import hmac
import os, os.path, fnmatch, sys, time, mimetypes, xmlrpclib, pprint, base64
import json,lxml, pprint, fido.prepare
from lxml import objectify


import base64
import urllib
from urllib import unquote, splittype, splithost
import xmlrpclib

class UrllibTransport(xmlrpclib.Transport):
    def set_proxy(self, proxy):
        self.proxyurl = proxy
                
    def request(self, host, handler, request_body, verbose=0):
        type, r_type = splittype(self.proxyurl)
        phost, XXX = splithost(r_type)

        puser_pass = None
        if '@' in phost:
            user_pass, phost = phost.split('@', 1)
            if ':' in user_pass:
                user, password = user_pass.split(':', 1)
                puser_pass = base64.encodestring('%s:%s' % (unquote(user),
                                                unquote(password))).strip()
        
        urlopener = urllib.FancyURLopener({'http':'http://%s'%phost})
        if not puser_pass:
            urlopener.addheaders = [('User-agent', self.user_agent)]
        else:
            urlopener.addheaders = [('User-agent', self.user_agent),
                                    ('Proxy-authorization', 'Basic ' + puser_pass) ]

        host = unquote(host)
        f = urlopener.open("http://%s%s"%(host,handler), request_body)

        self.verbose = verbose 
        return self.parse_response(f)

class DrupalFormatRegistry():
    
    def __init__(self, config):
        # Proxy:
        p = UrllibTransport()
        if( config.has_key('proxy') ):
            p.set_proxy(config['proxy'])
    
        # Make initial connection to service, then login as developer
        self.server = xmlrpclib.Server(config['url'], allow_none=True, transport=p);
        self.connection = self.server.system.connect();
        
        # hash_hmac('sha256', $timestamp .';'.$domain .';'. $nonce .';'.'user.get', 'remote_api_key');
        #h = hmac.new(key, data, digest_module)
        #result = h.hexdigest()
        
        #session = server.user.login( config['username'], config['password']);
        #session = server.user.login(config['key'], 'localhost.domd', '', 'C7nW83nDw', connection['sessid'], config['username'], config['password']);
        self.session = self.server.user.login( self.connection['sessid'],config['username'], config['password']);
        self.sessid = self.session['sessid'];
        self.user = self.session['user'];
        # Taxnonomy Vocabulary IDs
        self.ext_vid = 1
        self.mime_vid = 2
        self.type_vid = 3
        #
        self.ext_vid = 3
        self.mime_vid = 4
        self.type_vid = 5
        
    
    def add_taxonomy_term(self, vid, term):
        # Use this to list the taxonomy:
        tid = self.find_taxonomy_term(vid, term)
        # if the given term is not present, use this to add it.
        if( tid == -1 ):
            self.server.taxonomy.saveTerm(self.sessid, {"vid":vid,"name":term})
            tid = self.find_taxonomy_term(vid, term)
        
        return tid
    
    def find_taxonomy_term(self, vid, term):
        tid = -1
        for t in  self.server.taxonomy.getTree(self.sessid, vid):
            if( t['name'] == term ):
                 tid = t['tid']
        return tid

    def push_pronom(self, source ):
        parser = objectify.makeparser(remove_blank_text=True)
        doc = objectify.parse( source, parser )
        ff = doc.getroot().report_format_detail.FileFormat;
        
        timestamp = str(int(time.time()))
        #timestamp = str(int(time.mktime()))
        
        node = {
          'type': 'format',
          'status': 1,
          'promote': 1,
          'nid': 45,
          'uid': self.user['uid'],
          'name': self.user['name'],
          'changed': timestamp,
#          'created': timestamp,
#          'revision_timestamp': timestamp,
          
          'title': ff.FormatName.text.strip(),
          'body': ff.FormatDescription.text.strip(),
          'field_shortname' : [ {'value': ff.FormatName.text.strip()}, ],
          'field_version': [{'value': ff.FormatVersion.text.strip()}],
          'field_fullname': [{'value': ff.FormatName.text.strip()}],
          'field_aliases': [{'value': ff.FormatAliases.text.strip()}],
    # FormatFamilies
          'field_release_date': [{'value': { 'date': ff.ReleaseDate.text.strip() }}],
          'field_withdrawn_date': [{'value': { 'date': ff.WithdrawnDate.text.strip()  }}],
    
    #      'field_conforms_to': [{'nid': ''}],
    #      'field_encapsulates': [],
    #      'field_hasformat': [{'nid': ''}],
    
        }
        
        if( hasattr(ff, "Developers") ):
            node['field_creator'] = [{'value': ff.Developers.DeveloperCompoundName.text.strip()}];
        
        # Loop through FileFormatIdentifier[]
        if( hasattr(ff, "FileFormatIdentifier") ):
            for ffid in ff.FileFormatIdentifier:
                if( ffid.IdentifierType.text == "PUID"):
                    node['field_puid'] = [{'value': ffid.Identifier.text.strip()}]
                if( ffid.IdentifierType.text == "Apple Uniform Type Identifier"):
                    node['field_apple_uid'] = [{'value': ffid.Identifier.text.strip()}]
                if( ffid.IdentifierType.text == "MIME"):
                    node['field_mimetype'] = [{'value': self.add_taxonomy_term(self.mime_vid, ffid.Identifier.text.strip()) }]
         
         
        # Loop through ExternalSignature[]
        if( hasattr(ff, "ExternalSignature") ):
            node['field_extensions'] = []
            for es in ff.ExternalSignature:
                if( es.SignatureType.text == "File extension" ):
                    node['field_extensions'].append( 
                            { 'value': self.add_taxonomy_term(self.ext_vid, es.Signature.text.strip()) } )
                    
        # Internal Signatures
        if( hasattr(ff,"InternalSignature") ):
            node['field_int_sigs'] = []
            for isg in ff.InternalSignature:
                node['field_int_sigs'].append( { 
                               'value': { 
                                    'field_title' : [{ 'value': isg.SignatureName.text.strip() }], 
                                    'field_note' : [{ 'value': isg.SignatureNote.text.strip() }], 
                                    'field_regex' : [{ 'value': fido.prepare.convert_to_regex(isg.ByteSequence[0].ByteSequenceValue.text.strip()) }],
                                }
                                })

        # Documents
        if( hasattr(ff, "Document") ):
            node['field_documents'] = []
            for doc in ff.Document:
                content = {
                                    'field_title' : [{"value": doc.DisplayText.text.strip() }],
                                    'field_doc_type' : [{"value": doc.DocumentType.text.strip() }],
                                    'field_doc_avail' : [{"value": doc.AvailabilityDescription.text.strip() }],
                                    'field_doc_avail_note' : [{"value": doc.AvailabilityNote.text.strip() }],
                                    'field_doc_pub_date' : [{"value": { 'date': doc.PublicationDate.text.strip() } } ],
                                    'field_doc_ipr' : [{"value": doc.DocumentIPR.text.strip() }],
                                    'field_doc_note' : [{"value": doc.DocumentNote.text.strip() }],
                                    'field_doc_author' : [{"value": doc.Author.AuthorCompoundName.text.strip() }],
                                    'field_doc_publisher' : [{"value": doc.Publisher.PublisherCompoundName.text.strip() }],
                           }
                if( hasattr(doc, "DocumentIdentifier")):
                    content['field_doc_link'] = [{'attributes': [],
                                                  'title': doc.TitleText.text.strip(),
                                                   'url': 'http://'+doc.DocumentIdentifier.Identifier.text.strip() } ]
                node['field_documents'].append( { 'value': content } )
                
        #Split FormatTypes and add.
        #      'field_type': [{'value': ''}],
        if( hasattr(ff, 'FormatTypes') ):
            node['field_type'] = []
            node['taxonomy'] = []
            for type in ff.FormatTypes.text.split(','):
                tid = self.add_taxonomy_term(self.type_vid, type.strip())
#                node['field_type'].append( { 'value': tid } )
                node['taxonomy'] = { 'tags' : { str(self.type_vid) : type.strip() } }
        
        # ByteOrders (Big-endian|Little-endian (Intel)|Little-endian (Intel) and Big-endian)

        pp = pprint.PrettyPrinter()    
        pp.pprint(node);
        
        try:
            n = self.server.node.save( self.sessid, node)
            print n, node['title']
            nn = self.server.node.get( self.sessid,n,{})  # DEBUG - get the final node - not needed now that we know it works
        
        except xmlrpclib.Fault, err:
            print "A fault occurred"
            print "Fault code: %d" % err.faultCode
            print "Fault string: %s" % err.faultString
        
        else:
            pass
            #pp.pprint(nn['title'])
            #pp.pprint(n) # DEBUG
            pp.pprint(nn) # DEBUG - dump the final node - not needed now that we know it works



if __name__ == "__main__":
    from config import config
    
    
    dfr = DrupalFormatRegistry(config)
    #dfr.push_pronom('pronom/xml/puid.fmt.10.xml')
    
    for file in os.listdir('pronom/xml'):
        if fnmatch.fnmatch(file, 'puid.fmt.2.xml'):
            print file
            dfr.push_pronom('pronom/xml/'+file)
        
'''
      <RelatedFormat>
        <RelationshipType>Has lower priority than</RelationshipType>
        <RelatedFormatID>672</RelatedFormatID>
        <RelatedFormatName>Exchangeable Image File Format (Uncompressed)</RelatedFormatName>
        <RelatedFormatVersion>2.2</RelatedFormatVersion>

        <RelationshipType>Equivalent to</RelationshipType>
        <RelationshipType>Has lower priority than</RelationshipType>
        <RelationshipType>Has priority over</RelationshipType>
        <RelationshipType>Is previous version of</RelationshipType>
        <RelationshipType>Is subsequent version of</RelationshipType>
        <RelationshipType>Is subtype of</RelationshipType>
        <RelationshipType>Is supertype of</RelationshipType>
        
'''
