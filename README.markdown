A Prototype Editable Format Registry Build On Drupal
====================================================

To Do
-----
* Variable-location regexes are not handled correctly.
* When adding taxonomy terms, also add a description 'Files with extension xxx', 'MIME Type [link to IANA page e.g. http://www.iana.org/assignments/media-types/application/ and http://www.iana.org/assignments/media-types/application/pdf]'.
* Used domd/format/[title-raw] for format aliases, but switch to domd/pronom/[field_puid-raw] for demonstration purposes. Note that needed to allow slashes to remain in order for this to work as expected.

Issues
------ 
* Most importantly, having explored the data, there are clear problems with the current data model (e.g. URL and the implied http:// scheme) and also a lot of duplication of data, and the scope is too large.
* Not all of the data in the PRONOM Reports has been included, but the missing fields should not be added without first considering if the overall data model is appropriate (see above).
* The RDF mappings are a hack for demonstration purposes and should be reviewed at a later date.
* Not 100% clear how to ensure old data is 'cleared'. When updating a node, it would perhaps be better to download all node contents and then update it locally before pushing it back again.
* Regexs are still not quite right - the escaping is rather clumsy at present.
* Node References are currently a bit of a hack, largely because the script processes files one-by-one. A full download/sync/upload would work better:
** The nodes are looked up using the Title and Version fields, instead of a more solid identifier, because the XML uses IDs instead of PUIDs for cross-references and this makes resolving refs hard unless you load all the reports into memory.
** That look-up actually searches the live site, and is a big part of the reason why things are so slow at present.
** References to other nodes are only filled in in one direction, so any formats that complete (two-way) connections may be missed if only the back-link is present.
** Actually, worse than that, you should really wait for the search indexes to have been fully re-build!
** Because the push operation currently resolves node relationships by a remote search, the push has to be run twice to catch all node references.
** Furthermore, a third push may be required in order to ensure that all Node Referrer 'back-references' are resolved!
* What on earth to do with the "WAVE Format GUID" IdentifierType?
* The 'Unique Field' module does not cope well when one of the fields is left empty (i.e. Version). Just Title and PUID should be sufficient for now.
* We are using Flexifield to compound entities. This is rather ugly, but sorting the actual data model is more important.
* Most formats have very few internal signatures, some have a lot, up to 8 (fmt/134 http://beta.domd.info/domd/format/mpeg-12-audio-layer-3).
* Trickiest part is relations and mapping Drupal Nodes to records. Embedding Drupal Node ID in the XML would be best.
* The date field type appears to insert a default value (at least under flexifields) and can lead to displayed errors and document publication dates like 0010-01-01. Must be careful to pass 'empty' values. However, it is not clear that fixing this is worthwhile given that we probably need to sort out the data model.
* Title and PUID are required to be unique (in combination, so be aware that multiple identical titles with no PUID may be allowed).


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

 
