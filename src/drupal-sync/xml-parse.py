import json, lxml, pprint, fido.prepare
from lxml import objectify

class objectJSONEncoder(json.JSONEncoder):
    """A specialized JSON encoder that can handle simple lxml objectify types
       >>> from lxml import objectify
       >>> obj = objectify.fromstring("<Book><price>1.50</price><author>W. Shakespeare</author></Book>")
       >>> objectJSONEncoder().encode(obj)
       '{"price": 1.5, "author": "W. Shakespeare"}'       
    """
    def default(self,o):
        if isinstance(o, lxml.objectify.IntElement):
            return int(o)
        if isinstance(o, lxml.objectify.NumberElement) or isinstance(o, lxml.objectify.FloatElement):
            return float(o)
        if isinstance(o, lxml.objectify.ObjectifiedDataElement):
            return str(o)
        if hasattr(o, '__dict__'):
            #For objects with a __dict__, return the encoding of the __dict__
            return o.__dict__
        return json.JSONEncoder.default(self, o)
        
#obj = objectify.fromstring("<Book><price>1.50</price><author>W. Shakespeare</author></Book>")
#print objectJSONEncoder().encode(obj)

obj2 = objectify.parse("pronom/xml/puid.fmt.10.xml", )
print objectJSONEncoder().encode(obj2.getroot())

ff = obj2.getroot().report_format_detail.FileFormat;

print ff.FormatName;
print len(ff.InternalSignature);
print ff.InternalSignature[0].ByteSequence[0].ByteSequenceValue;

print fido.prepare.convert_to_regex("49492A00")

# 
# Identifiers (PUID, MIME, Apple UID)
# Developers
# Document*
# - ...
# ExternalSignature
# InternalSignature (ByteSequence*
# RelatedFormat* (Type, ID, Name, Version?)
# - Has lower priority than
# - Is subsequent version of
# - Is supertype of 
# CompressionType*
# FormatProperty*

