'''
Created on 27 Nov 2010

@author: anj
'''

import hmac
import os, os.path, fnmatch, sys, time, mimetypes, xmlrpclib, pprint, base64
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
        parser = objectify.makeparser(remove_blank_text=True)
        doc = objectify.parse( source, parser )
        ff = doc.getroot().report_format_detail.FileFormat;
        
        timestamp = str(int(time.time()))
        #timestamp = str(int(time.mktime()))
        
        node = {
          'type': 'format',
          'status': 1,
          'promote': 1,
          'nid': 383,
          'uid': self.user['uid'],
          'name': self.user['name'],
          'changed': timestamp,
          'created': timestamp,
          'revision_timestamp': timestamp,
          
          'title': ff.FormatName.text.strip(),
          'body': ff.FormatDescription.text.strip(),
          'field_shortname' : [ {'value': ff.FormatName.text.strip()}, ],
          'field_version': [{'value': ff.FormatVersion.text.strip()}],
          'field_fullname': [{'value': ff.FormatName.text.strip()}],
          'field_aliases': [{'value': ff.FormatAliases.text.strip()}],
    # FormatFamilies
          'field_release_date': [{'value': { 'date': ff.ReleaseDate.text.strip() }}],
          'field_withdrawn_date': [{'value': { 'date': ff.WithdrawnDate.text.strip()  }}],
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
            node['field_is1_name'] = [{"value": ff.InternalSignature[0].SignatureName.text.strip()}]
            node['field_is1_desc'] = [{"value": ff.InternalSignature[0].SignatureNote.text.strip()}]
            node['field_regex'] = [{'value': fido.prepare.convert_to_regex(ff.InternalSignature[0].ByteSequence[0].ByteSequenceValue.text.strip()) }];

        #Split FormatTypes and add.
        #      'field_type': [{'value': ''}],
        if( hasattr(ff, 'FormatTypes') ):
            node['field_type'] = []
            for type in ff.FormatTypes.text.split(','):
                node['field_type'].append( { 'value': self.add_taxonomy_term(self.type_vid, type.strip())} )
        
        # ByteOrders (Big-endian|Little-endian (Intel)|Little-endian (Intel) and Big-endian)

        
        pp = pprint.PrettyPrinter()    
        #pp.pprint(node);
        
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
    dfr.push_pronom('pronom/xml/puid.fmt.10.xml')
    
    #for file in os.listdir('pronom/xml'):
    #    if fnmatch.fnmatch(file, 'puid.fmt.*.xml'):
    #        print file
    #        dfr.push_pronom('pronom/xml/'+file)
        
'''
 'field_doc_link': [{'attributes': [],
                     'title': 'Adobe Photoshop: TIFF Technical Notes',
                     'url': 'http://partners.adobe.com/public/developer/en/tiff/TIFFphotoshop.pdf'},
                    {'attributes': [],
                     'title': 'Adobe PageMaker 6.0: TIFF Technical Notes',
                     'url': 'http://partners.adobe.com/public/developer/en/tiff/TIFFPM6.pdf'}],
 'field_doc_title': [{'value': 'Adobe Systems Incorporated, 2002, Adobe Photoshop: TIFF Technical Notes'},
                     {'value': 'Adobe Systems Incorporated, 1995, Adobe PageMaker 6.0: TIFF Technical Notes'}],

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
