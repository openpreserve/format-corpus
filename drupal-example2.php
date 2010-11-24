$domain = 'my domain';
$timestamp = (string) time();
$nonce = user_password();
$hash = hash_hmac('sha256', $timestamp .';'.$domain .';'. $nonce .';'.'user.get', 'remote_api_key');
$xmlrpc_result = xmlrpc('http://remoteserver.com/services/xmlrpc', 'user.get', $hash, $domain, $timestamp, $nonce, 0);
if ($xmlrpc_result === FALSE) {
  print '<pre>' . print_r(xmlrpc_error(), TRUE) . '<pre>';
}
else {
  print '<pre>' . print_r($xmlrpc_result, TRUE) . '<pre>';
}

<?php
$domain = 'example.com';
$timestamp = (string) time();
$nonce = user_password();
$hash = hash_hmac('sha256', $timestamp .';'.$domain .';'. $nonce .';'.'user.get', 'remote_api_key', 0);
//Note that we are using domain_time_stamp not timestamp and domain_name not domain here
$request_url = $domain.'/method=user.get&uid=0&hash='.$hash.'&domain_name='.$domain.'&domain_time_stamp='.$timestamp.'&nonce='.$nonce;
$session = curl_init($request_url);
curl_setopt($session, CURLOPT_POST, 1); // Do a regular HTTP POST
curl_setopt($session, CURLOPT_FOLLOWLOCATION, true);
curl_setopt($session, CURLOPT_HEADER, FALSE);
curl_setopt($session, CURLOPT_RETURNTRANSFER, TRUE);
$result = curl_exec($session);
$error = curl_error($session);
curl_close($session);
if (!empty($error)) {
  print '<pre>'.$error.'</pre>';
}
else {
  print '<pre>'.$result.'</pre>';
}
?>
