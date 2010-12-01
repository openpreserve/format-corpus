foreg
=====

* doc.Author.AuthorCompoundName not always present, see fmt/121
* Regexs are borked because other parameters (EOF etc) not being used.
* WAVE Format GUID
* PDF 1.6 (fmt/20) is a good example of the problems with relationships as implemented here.


Some complex signatures require escaping in ways not 100% clear to me.
Perhaps curly brackets are the issue?
Also, need to get the regex escaping right in general.

Note that most formats have very few internal signatures, but 

* puid.fmt.134.xml:8
* puid.x-fmt.384.xml:7
* puid.x-fmt.387.xml:4
* puid.x-fmt.388.xml:4
* puid.x-fmt.399.xml:4
* puid.fmt.12.xml:3
* puid.fmt.1.xml:2
* puid.fmt.10.xml:2
* puid.fmt.122.xml:2
* puid.fmt.123.xml:2
* puid.fmt.124.xml:2
* puid.fmt.2.xml:2
* puid.fmt.278.xml:2
* puid.fmt.7.xml:2
* puid.fmt.8.xml:2
* puid.fmt.9.xml:2
* puid.x-fmt.390.xml:2
* puid.x-fmt.391.xml:2
* puid.x-fmt.398.xml:2


Due to issues with the multigroup module, you have to set the number of repeats to be larger than 8 when syncing.
Looks ugly, but works fine.
Nope, it doesn't really work, because nesting repeated items does not work.
And the XMLRPC interface is pretty nasty.
Looked at using full nodes, but that makes things more complex on the code end and looks ugly.
So, trying flexifield...


Sync
----
* XMLRPC Services

Visualisation
-------------

Drupal modules for possible visualisation

* http://drupal.org/project/visualize
* http://drupal.org/project/timelinemap
* http://drupal.org/project/timeline

Issues
------

* The date field type appears to insert a default value over xmlrpc (at least under flexifields) and leads to document publication dates like 0010-01-01.
* 
 
