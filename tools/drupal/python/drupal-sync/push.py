'''
Created on 27 Nov 2010

When against D7 and attempting to add to empty taxonomies, got an odd error:
http://drupal.org/node/1028230
But now, even when taxonomy insert is fine, referencing from nodes is not stored.

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
    proxyurl = None
    session_name = None
    mysessid = None
    
    def set_proxy(self, proxy):
        self.proxyurl = proxy
        
    def set_sessid(self, session_name, sessid):
        self.session_name = session_name
        self.mysessid = sessid
                
    def request(self, host, handler, request_body, verbose=0):
        if not self.proxyurl is None:
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
        else:
            urlopener = urllib.FancyURLopener()
            
        if not self.mysessid is None:
            urlopener.addheaders.append( ("Cookie", "%s=%s" % (self.session_name,self.mysessid) ) )
            
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
        self.server = xmlrpclib.Server(config['url'], allow_none=True, transport=p);

        # Make initial connection to service, then login as developer
        self.connection = self.server.system.connect();
        
        # hash_hmac('sha256', $timestamp .';'.$domain .';'. $nonce .';'.'user.get', 'remote_api_key');
        #h = hmac.new(key, data, digest_module)
        #result = h.hexdigest()
        
        self.session = self.server.user.login( config['username'], config['password']);
        #session = server.user.login(config['key'], 'localhost.domd', '', 'C7nW83nDw', connection['sessid'], config['username'], config['password']);
        #self.session = self.server.user.login( self.connection['sessid'],config['username'], config['password']);
        self.sessid = self.session['sessid'];
        self.session_name = self.session['session_name'];
        p.set_sessid(self.session_name, self.sessid)
        self.user = self.session['user'];
        #self.user = config['username'];
        # Taxnonomy Vocabulary IDs
        #self.ext_vid = 1
        #self.mime_vid = 2
        #self.type_vid = 3
        #
        self.ext_vid = 2;
        self.mime_vid = 3;
        self.type_vid = 5;
        #
        try:
            nn = self.server.node.retrieve(1)
            pprint.pprint(nn)
        except:
            print "A fault occurred"
            #print "Fault code: %d" % err.faultCode
            #print "Fault string: %s" % err.faultString
    
    def add_taxonomy_term(self, vid, term):
        # Use this to list the taxonomy:
        tid = self.find_taxonomy_term(vid, term)
        # if the given term is not present, use this to add it.
        if( tid == -1 ):
            self.server.taxonomy_term.create({"vid":vid, "name":term})
            tid = self.find_taxonomy_term(vid, term)
        
        return tid
    
    def find_taxonomy_term(self, vid, term):
        tid = -1
        for t in  self.server.taxonomy_vocabulary.getTree(vid):
            if( t['name'] == term ):
                 tid = t['tid']
        return tid
    
    def find_node_for_puid(self, puid):
        try:
            found = self.server.node.index( '"'+puid+'"', "true" )#, "field_puid" )
        except xmlrpclib.Fault, err:
            print "Fault code: %d" % err.faultCode
            print "Fault string: %s" % err.faultString
            return -1;
        else :
            print "Found "+str(len(found))+" PUID matches!"
            #pprint.pprint(found)
            # Need to peel through matches and locate the matching PUID?
            if len(found) != 1:
                return -1
            return found[0]['node']

    def find_node_for_format(self, title, version):
        node_title = title
        if( version != "" ):
            node_title = title + " " + version
        print "Searching for "+node_title+"..."
        try:
            found = self.server.node.index( 0, "*", [{'title': '"'+node_title+'"' }] )
        except xmlrpclib.Fault, err:
            print "Fault code: %d" % err.faultCode
            print "Fault string: %s" % err.faultString
            return -1;
        else :
            print "Found "+str(len(found))+" Title Version matches!"
            #pprint.pprint(found)
            for f in found:
                if( f['title'].strip() == node_title.strip() ):
                    return f
            print "No matching title found!"
            return -1
    

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
          'uid': self.user['uid'],
          'name': self.user['name'],
          'changed': timestamp,
#          'created': timestamp,
#          'revision_timestamp': timestamp,
          
          'title': ff.FormatName.text.strip()+" "+ ff.FormatVersion.text.strip(),
          'body': {'und': [{'value': ff.FormatDescription.text.strip() }] },
          'field_shortname' : {'und': [ {'value': ff.FormatName.text.strip()}, ] },
          'field_version': {'und': [{'value': ff.FormatVersion.text.strip()}] },
#          'field_fullname': [{'value': ff.FormatName.text.strip()}],
          'field_aliases': {'und': [{'value': ff.FormatAliases.text.strip()}] },
    # FormatFamilies
          'field_release_date': {'und': [{'value': { 'date': ff.ReleaseDate.text.strip() }}] },
          'field_withdrawn_date': {'und': [{'value': { 'date': ff.WithdrawnDate.text.strip()  }}] },
        }
        

        node['field_creator'] = [{'value': '' }]
        if( hasattr(ff, "Developers") ):
            node['field_creator'] = [{'value': ff.Developers.DeveloperCompoundName.text.strip()}];
        
        # Loop through FileFormatIdentifier[]
        node['field_puid'] = {'und': [{'value': '' }] }
        node['field_apple_uid'] = {'und': [{'value': '' }] }
        node['field_mimetypes'] = {'und': "" }
        if( hasattr(ff, "FileFormatIdentifier") ):
            for ffid in ff.FileFormatIdentifier:
                if( ffid.IdentifierType.text == "PUID"):
                    node['field_puid'] = {'und': [{'value': ffid.Identifier.text.strip()}] }
                if( ffid.IdentifierType.text == "Apple Uniform Type Identifier"):
                    node['field_apple_uid'] = {'und': [{'value': ffid.Identifier.text.strip()}] }
                if( ffid.IdentifierType.text == "MIME"):
                    #tid = self.add_taxonomy_term(self.mime_vid, ffid.Identifier.text.strip());
                    #print "---MIME Type"
                    #pprint.pprint(tid)
                    #print "---"
                    #node['field_mimetypes'] = {'und': [{'tid': '23'}]}
                    #node['field_mimetypes'] = {'und': { '1': tid } }
                    node['field_mimetypes']['und'] += ffid.Identifier.text.strip()+","
         
         
        # Loop through ExternalSignature[]
        node['field_extensions'] = [] #{'und': [{'value': '' }] }
        if( hasattr(ff, "ExternalSignature") ):
            node['field_extensions'] = {'und': "" }
            #{'und': [{'tid': '25'}, {'tid': '24'}]}
            for es in ff.ExternalSignature:
                if( es.SignatureType.text == "File extension" ):
                    #tid = self.add_taxonomy_term(self.ext_vid, es.Signature.text.strip())
                    #print "---Ext"
                    #pprint.pprint(tid)
                    #idx = len(node['field_extensions']['und'])+1
                    #print "---"
                    node['field_extensions']['und'] += es.Signature.text.strip()+","
	#,{ '1' : 25, '2' : 24 }
        #node['field_extensions'] = {'und': [{'tid': '25'}, {'tid': '24'}]}
                    
                
        #Split FormatTypes and add.
#        node['taxonomy'] = {}
        node['field_type'] = []
        if( hasattr(ff, 'FormatTypes') ):
            node['field_type'] = {'und': "" }
#            node['taxonomy'] = []
            for type in ff.FormatTypes.text.split(','):
                #tid = self.add_taxonomy_term(self.type_vid, type.strip())
                #idx = len(node['field_type']['und'])+1
                node['field_type']['und'] += type.strip()+","
#                node['taxonomy'] = { 'tags' : { str(self.type_vid) : type.strip() } }
        
        # ByteOrders (Big-endian|Little-endian (Intel)|Little-endian (Intel) and Big-endian)

        # Internal Signatures
        node['field_int_sigs'] = [{'value': {} }]
        if( hasattr(ff,"InternalSignature") ):
             node['field_int_sigs'] = []
             for isg in ff.InternalSignature:
                 intsig = {  
                                     'field_title' : [{ 'value': isg.SignatureName.text.strip() }], 
                                     'field_note' : [{ 'value': isg.SignatureNote.text.strip() }], 
                                     'field_regex' : [],
                                 }
                 for bs in isg.ByteSequence:
                     regex =  bs.ByteSequenceValue.text.strip()
                     endianness = bs.Endianness.text.strip()
                     pos = 'BOF'
                     if( bs.PositionType.text.strip() == "Absolute from EOF" ):
                         pos = 'EOF'
                     offset = bs.Offset.text.strip()
                     maxoffset = bs.MaxOffset.text.strip()
                     regex = fido.prepare.convert_to_regex( regex, endianness, pos, offset, maxoffset)
                     intsig['field_regex'].append({ 'value': repr(regex) })
                 node['field_int_sigs'].append( { 'value': intsig } )

        # Documents
        node['field_documents'] = [{'value': {} }]
        if( hasattr(ff, "Document") ):
             node['field_documents'] = []
             for doc in ff.Document:
                 content = {
                                     'field_title' : [{"value": doc.DisplayText.text.strip() }],
                                     'field_doc_type' : [{"value": doc.DocumentType.text.strip() }],
                                     'field_doc_avail' : [{"value": doc.AvailabilityDescription.text.strip() }],
                                     'field_doc_avail_note' : [{"value": doc.AvailabilityNote.text.strip() }],
                                     'field_doc_pub_date' : [{ 'date': doc.PublicationDate.text.strip() } ],
                                     'field_doc_ipr' : [{"value": doc.DocumentIPR.text.strip() }],
                                     'field_doc_note' : [{"value": doc.DocumentNote.text.strip() }],
                            }
                 if( hasattr(doc, 'Author')):
                     content['field_doc_author'] = [{"value": doc.Author.AuthorCompoundName.text.strip() }];
                 if( hasattr(doc, 'Publisher')):
                     content['field_doc_publisher'] = [{"value": doc.Publisher.PublisherCompoundName.text.strip() }];
                 if( hasattr(doc, "DocumentIdentifier")):
                     content['field_doc_link'] = []
                     for doci in doc.DocumentIdentifier:
                         if( doci.IdentifierType.text == "URL" ):
                             content['field_doc_link'].append( { 'attributes': [],
                                                   'title': doc.TitleText.text.strip(),
                                                    'url': 'http://'+doci.Identifier.text.strip() })
                         if( doci.IdentifierType.text == "ISBN" ):
                             content['field_doc_link'].append( { 'attributes': [],
                                                   'title': "ISBN "+doci.Identifier.text.strip(),
                                                    'url': 'http://www.worldcat.org/search?q=bn:'+doci.Identifier.text.strip() })
                 node['field_documents'].append( { 'value': content } )
        
        # Relationships
        # This works for plain lists, but not autocomplete: node['field_same_as'] = {'und': {'nid': '43'} }
	# This works for autocomplete lists: node['field_same_as'] = {'und': [{'nid': '[nid:43]'}] }
        node['field_same_as'] = {'und': [] }
        node['field_lower_priority_than'] = {'und': [] }
        node['field_subsequent_version'] = {'und': [] }
        node['field_conforms_to'] = {'und': [] }
        if( hasattr(ff, 'RelatedFormat')):
            for rf in ff.RelatedFormat:
                rf_node_id = self.find_node_for_format(rf.RelatedFormatName.text.strip(), rf.RelatedFormatVersion.text.strip())
                if( rf_node_id != -1 ):
                    rf_node_id = rf_node_id['nid']
                    rf_node_id = rf.RelatedFormatName.text.strip()+" "+rf.RelatedFormatVersion.text.strip()+" [nid:"+str(rf_node_id)+"]"
                    print rf_node_id
                    if( rf.RelationshipType.text == "Equivalent to" ):
                        node['field_same_as']['und'].append( { 'nid' : rf_node_id} )
                    if( rf.RelationshipType.text == "Has lower priority than" ):
                        node['field_lower_priority_than']['und'].append( { 'nid' : rf_node_id} )
                    if( rf.RelationshipType.text == "Is subsequent version of" ):
                        node['field_subsequent_version']['und'].append( { 'nid' :rf_node_id} )
                    if( rf.RelationshipType.text == "Is subtype of" ):
                        node['field_conforms_to']['und'].append( { 'nid' : rf_node_id } )

        # If empty, ensure empty:
#        if len(node['field_equivalent_to']) == 0:
#            node['field_equivalent_to'] =  [{'nid': '' }]
#        if len(node['field_lower_priority_than']) == 0:
#            node['field_lower_priority_than'] =  [{'nid': ''}]
#        if len(node['field_subsequent_version']) == 0:
#            node['field_subsequent_version'] =  [{'nid': ''}]
#        if len(node['field_conforms_to']) == 0:
#            node['field_conforms_to'] =  [{'nid': ''}]

        # Check if this record is already there, and if so, update instead of add:
        node_match = dfr.find_node_for_format(node['title'],'');
        do_update = False
        if node_match != -1:
            do_update = True
            node['nid'] = node_match['nid'];

        # DEBUG
        pp = pprint.PrettyPrinter()
        pp.pprint(node);
        pp.pprint(node['field_version']['und'][0]['value'])
        print "Submitting to the site... Update =", do_update
        
        try:
            # knocked out .update, as it does not seem to change anything!
            if do_update and False:
                n = self.server.node.update(node)
            else:
                n = self.server.node.create(node)
            nn = self.server.node.retrieve(n,{})  # DEBUG - get the final node - not needed now that we know it works
        
        except xmlrpclib.Fault, err:
            print "A fault occurred"
            print "Fault code: %d" % err.faultCode
            print "Fault string: %s" % err.faultString
        
        print n, node['title']
        #pp.pprint(nn['title'])
        #pp.pprint(n) # DEBUG
        pp.pprint(nn) # DEBUG - dump the final node - not needed now that we know it works

# NOTE Drupal taxonomy services create did not work when taxonomies were empty?
# Strange error...

if __name__ == "__main__":
    from config import config
    
    
    dfr = DrupalFormatRegistry(config)
    
    dfr.push_pronom('data/pronom/xml/puid.fmt.10.xml')
    #dfr.push_pronom('data/pronom/xml/puid.fmt.101.xml')
    #sys.exit(0)
    
    for file in os.listdir('data/pronom/xml'):
	#if fnmatch.fnmatch(file, 'puid.*fmt.*.xml'):
        if fnmatch.fnmatch(file, 'puid.fmt.?.xml'):
            print file
            dfr.push_pronom('data/pronom/xml/'+file)
            
    print "DONE!"
        
