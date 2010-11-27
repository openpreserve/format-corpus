'''
Created on 27 Nov 2010

@author: anj
'''


'''
'''

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
    
    doc = objectify.parse( source )
    ff = doc.getroot().report_format_detail.FileFormat;
    
    node = {
      'type': 'format',
      'status': 1,
      'promote': 1,
#      'nid': 3,
      'uid': user['uid'],
      'name': user['name'],
      'changed': timestamp,
      'title': ff.FormatName,
      'body': ff.FormatDescription,
      'field_shortname' : [{'value': ff.FormatName},],
     'field_aliases': [{'value': ff.FormatAliases}],
     'field_apple_uid': [{'value': ''}],
     'field_conforms_to': [{'nid': ''}],
     'field_creator': [{'value': 'Adobe'}],
     'field_encapsulates': [],
     'field_extensions': [{'value': '1'}, {'value': '2'}],
     'field_fullname': [{'value': 'Portable Document Format'}],
     'field_hasformat': [{'nid': ''}],
     'field_mimetype': [{'value': '4'}],
     'field_puid': [{'value': 'fmt/11'}],
     'field_regex': [{'value': '^/%%PDF/'}],
     'field_shortname': [{'value': 'PDF'}],
     'field_type': [{'value': ''}],
     'field_version': [{'value': ''}],

    }
