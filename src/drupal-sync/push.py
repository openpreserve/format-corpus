'''
Created on 27 Nov 2010

@author: anj
'''

import hmac
import os.path, sys, time, mimetypes, xmlrpclib, pprint, base64
import json, lxml, pprint, fido.prepare
from lxml import objectify

pp = pprint.PrettyPrinter()

#def push_pronom(source='pronom/xml/puid.fmt.10.xml'):


if __name__ == "__main__":
    from config import config
    
    # Make initial connection to service, then login as developer
    server = xmlrpclib.Server(config['url'], allow_none=True);
    connection = server.system.connect();
    
    # hash_hmac('sha256', $timestamp .';'.$domain .';'. $nonce .';'.'user.get', 'remote_api_key');
    #h = hmac.new(key, data, digest_module)
    #result = h.hexdigest()
    
    #session = server.user.login( config['username'], config['password']);
    #session = server.user.login(config['key'], 'localhost.domd', '', 'C7nW83nDw', connection['sessid'], config['username'], config['password']);
    session = server.user.login(connection['sessid'],config['username'], config['password']);
    sessid = session['sessid'];
    user = session['user'];
    
    timestamp = str(int(time.time()))
    
    source='pronom/xml/puid.fmt.10.xml'
    doc = objectify.parse( source )
    ff = doc.getroot().report_format_detail.FileFormat;
    
    node = {
      'type': 'format',
      'status': 1,
      'promote': 1,
      'nid': 8,
      'uid': user['uid'],
      'name': user['name'],
      'changed': timestamp,
      
      'title': ff.FormatName.text,
      'body': ff.FormatDescription.text,
      'field_shortname' : [ {'value': ff.FormatName.text}, ],
      'field_version': [{'value': ff.FormatVersion.text}],
      'field_fullname': [{'value': ff.FormatName.text}],
      'field_aliases': [{'value': ff.FormatAliases.text}],
      'field_creator': [{'value': 'Adobe'}],
      'field_puid': [{'value': 'fmt/10'}],
#      'field_apple_uid': [{'value': ''}],
     
# Taxonomy-based ones:
# Use this to list the extensions taxonomy:
#    pp.pprint( server.taxonomy.getTree(sessid, "1")[0] )   
# if the given extension is not present, use this to add it.
# server.taxonomy.saveTerm(sessid, {"vid":"1","name":"tif"})
      'field_extensions': [{'value': '12' }],
#      'field_mimetype': [{'value': '4'}],
#      'field_type': [{'value': ''}],


#      'field_conforms_to': [{'nid': ''}],
#      'field_encapsulates': [],
#      'field_hasformat': [{'nid': ''}],
    
      'field_regex': [{'value': fido.prepare.convert_to_regex(ff.InternalSignature[0].ByteSequence[0].ByteSequenceValue.text) }],

    }
    
    pprint.pprint(node);
    
    try:
        n = server.node.save(sessid, node)
        print n, node
        nn = server.node.get(sessid,n,{})  # DEBUG - get the final node - not needed now that we know it works
    
    except xmlrpclib.Fault, err:
        print "A fault occurred"
        print "Fault code: %d" % err.faultCode
        print "Fault string: %s" % err.faultString
    
    else:
        pp.pprint(n) # DEBUG
        pp.pprint(nn) # DEBUG - dump the final node - not needed now that we know it works
        
