'''
Created on 27 Nov 2010

@author: anj
'''

import hmac
import os.path, sys, time, mimetypes, xmlrpclib, pprint, base64
import json, lxml, pprint, fido.prepare
from lxml import objectify


class DrupalFormatRegistry():
    
    def __init__(self, config):
        # Make initial connection to service, then login as developer
        self.server = xmlrpclib.Server(config['url'], allow_none=True);
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
        doc = objectify.parse( source )
        ff = doc.getroot().report_format_detail.FileFormat;
        
        timestamp = str(int(time.time()))
        #timestamp = str(int(time.mktime()))
        
        node = {
          'type': 'format',
          'status': 1,
          'promote': 1,
          'nid': 11,
          'uid': self.user['uid'],
          'name': self.user['name'],
          'changed': timestamp,
          'created': timestamp,
          'revision_timestamp': timestamp,
          
          'title': ff.FormatName.text,
          'body': ff.FormatDescription.text,
          'field_shortname' : [ {'value': ff.FormatName.text}, ],
          'field_version': [{'value': ff.FormatVersion.text}],
          'field_fullname': [{'value': ff.FormatName.text}],
          'field_aliases': [{'value': ff.FormatAliases.text}],
          'field_creator': [{'value': ff.Developers.DeveloperCompoundName.text}],
    # FormatFamilies
          'field_release_date': [{'value': { 'date': ff.ReleaseDate.text }}],
    #      'field_release_date': [{'value': { 'date': time.strftime("%d %b %Y", time.strptime(ff.ReleaseDate.text, "%d %b %Y"))}}],
    # 'field_release_date': [{
    #                         'date_type': 'date',
    #                         'timezone': 'UTC',
   #                          'value': time.strftime("%Y-%m-%dT00:00:00", time.strptime(ff.ReleaseDate.text, "%d %b %Y")) }],
    #                         'timezone_db': 'UTC',
    #                         'value': '1992-06-03T00:00:00'}],
    # WithdrawnDate
    
    #      'field_conforms_to': [{'nid': ''}],
    #      'field_encapsulates': [],
    #      'field_hasformat': [{'nid': ''}],
          'field_regex': [{'value': fido.prepare.convert_to_regex(ff.InternalSignature[0].ByteSequence[0].ByteSequenceValue.text) }],
    
        }
        
        
        
        # Loop through FileFormatIdentifier[]
        for ffid in ff.FileFormatIdentifier:
            if( ffid.IdentifierType.text == "PUID"):
                node['field_puid'] = [{'value': ffid.Identifier.text}]
            if( ffid.IdentifierType.text == "Apple Uniform Type Identifier"):
                node['field_apple_uid'] = [{'value': ffid.Identifier.text}]
            if( ffid.IdentifierType.text == "MIME"):
                node['field_mimetype'] = [{'value': self.add_taxonomy_term(self.mime_vid, ffid.Identifier.text) }]
         
         
        # Loop through ExternalSignature[]
        node['field_extensions'] = []
        for es in ff.ExternalSignature:
            if( es.SignatureType.text == "File extension" ):
                node['field_extensions'].append( 
                        { 'value': self.add_taxonomy_term(self.ext_vid, es.Signature.text) } )
        
        
        #Split FormatTypes and add.
        #      'field_type': [{'value': ''}],
        
        # ByteOrders (Big-endian|Little-endian (Intel)|Little-endian (Intel) and Big-endian)

        
        pp = pprint.PrettyPrinter()    
        pp.pprint(node);
        
        try:
            n = self.server.node.save( self.sessid, node)
            print n, node
            nn = self.server.node.get( self.sessid,n,{})  # DEBUG - get the final node - not needed now that we know it works
        
        except xmlrpclib.Fault, err:
            print "A fault occurred"
            print "Fault code: %d" % err.faultCode
            print "Fault string: %s" % err.faultString
        
        else:
            pp.pprint(n) # DEBUG
            pp.pprint(nn) # DEBUG - dump the final node - not needed now that we know it works



if __name__ == "__main__":
    from config import config
    
    
    dfr = DrupalFormatRegistry(config)
    dfr.push_pronom('pronom/xml/puid.fmt.101.xml')
        
'''
     <Document>
        <DocumentID>13</DocumentID>
        <DisplayText>European Broadcasting Union, 2001, Technical Specification 3285: BWF - a format for audio data files in broadcasting, Version 1</DisplayText>
        <DocumentType>Authoritative</DocumentType>
        <AvailabilityDescription>Public</AvailabilityDescription>
        <AvailabilityNote>
        </AvailabilityNote>
        <PublicationDate>01 Jul 2001</PublicationDate>
        <TitleText>Technical Specification 3285: BWF - a format for audio data files in broadcasting, Version 1</TitleText>
        <DocumentIPR>
        </DocumentIPR>
        <DocumentNote>
        </DocumentNote>
        <DocumentIdentifier>
          <Identifier>www.ebu.ch/CMSimages/en/tec_doc_t3285_tcm6-10544.pdf</Identifier>
          <IdentifierType>URL</IdentifierType>
        </DocumentIdentifier>
        <Author>
          <AuthorID>104</AuthorID>
          <AuthorName>
          </AuthorName>
          <OrganisationName>European Broadcasting Union</OrganisationName>
          <AuthorCompoundName>European Broadcasting Union</AuthorCompoundName>
        </Author>
        <Publisher>
          <PublisherID>104</PublisherID>
          <PublisherName>
          </PublisherName>
          <OrganisationName>European Broadcasting Union</OrganisationName>
          <PublisherCompoundName>European Broadcasting Union</PublisherCompoundName>
        </Publisher>
      </Document>
'''
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
