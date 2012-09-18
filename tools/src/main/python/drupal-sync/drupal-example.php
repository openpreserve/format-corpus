<?php
require_once('/Users/anj/Sites/drupals/drupal-6/includes/xmlrpc.inc');

// Get anon session. system.connect is the only service that does
// not require a sessionid even when you have 'Use Sess ID' checked.
$xmlrpc_url = 'http://localhost.domd/~anj/drupals/drupal-6/services/xmlrpc';
$anon_session = xmlrpc($xmlrpc_url, 'system.connect');

// Use anon session id to login with authentication
$user = 'admin';
$password = '';
$authenticated_session = xmlrpc($xmlrpc_url, 'user.login', $anon_session['sessid'], $user, $password);

// Now we have an anuthenticated session, and when this ID is passed to services, it will run under that user's permissions
$xmlrpc_result = xmlrpc($xmlrpc_url, 'user.get', $authenticated_session['sessid'], 0);
if ($xmlrpc_result === FALSE) {
  print '<pre>' . print_r(xmlrpc_error(), TRUE) . '<pre>';
}
else {
  print '<pre>' . print_r($xmlrpc_result, TRUE) . '<pre>';
}
?>