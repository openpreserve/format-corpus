A Prototype Editable Format Registry Build On Drupal
====================================================

To Do
-----
* Variable-location regexes are not handled correctly.
* Check RDF mappings.
* Check for missing fields.
* When adding taxonomy terms, also add a description 'Files with extension xxx'.
* Check multiple relationships of the same nature are pushed correctly. It does.

Issues
------ 
* Most importantly, having explored the data, there are clear problems with the current data model (e.g. URL and the implied http:// scheme) and also a lot of duplication of data, and the scope is too large.
* Not 100% clear how to ensure old data is 'cleared'. When updating a node, it would perhaps be better to download all node contents and then update it locally before pushing it back again.
* Regexs are still not quite right - the escaping is rather clumsy at present.
* References to other nodes are only filled in in one direction, so any formats that complete (two-way) connections may be missed if only the back-link is present.
* Actually, worse than that, you should really wait for the search indexes to have been fully re-build!
* Because the push operation currently resolves node relationships by a remote search, the push has to be run twice to catch all references.
* What on earth to do with the "WAVE Format GUID" IdentifierType?
* The 'Unique Field' module does not cope well when one of the fields is left empty (i.e. Version). Just Title and PUID should be sufficient for now.
* We are using Flexifield to compound entities. This is rather ugly, but sorting the actual data model is more important.
* Most formats have very few internal signatures, some have a lot, up to 8 (fmt/134 http://beta.domd.info/domd/format/mpeg-12-audio-layer-3).
* Trickiest part is relations and mapping Drupal Nodes to records. Embedding Drupal Node ID in the XML would be best.
* The date field type appears to insert a default value over xmlrpc (at least under flexifields) and can lead to document publication dates like 0010-01-01. Must be careful to pass 'empty' values.


Sync
----
* XMLRPC Services
* Python scripts to map from Drupal to/from XML.

Visualisation
-------------

Drupal modules for possible visualisation

* http://drupal.org/project/visualize
* http://drupal.org/project/timelinemap
* http://drupal.org/project/timeline

 
