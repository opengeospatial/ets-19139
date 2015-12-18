#!/bin/sh
echo "testing"
##http://hydro10.sdsc.edu/metadata/Raquel_Files/37E28B7A-0406-449B-8A45-3988AE675368.xml
# file
# schematron
# level
iut=http://54.209.245.204/teamengine/test-arg-validXMLFile.xml

level=3
schematron=http://54.209.245.204/teamengine/test-arg-ISOStyle.sch
echo iut $iut
name_w_ext=`echo "${iut##*/}"`
name_noext="${name_w_ext%.*}"
outputname=$name_noext-$level-pass

echo "Testing Level $level with a wrong schematron"
echo "------------------------------------------------"
echo "Testing \`this URL: <http://cite-dev-03.opengeospatial.org/teamengine/rest/suites/iso19139/1.0/run?iut=$iut&ics=$level&sch=$schematron>\`_":
echo ""
echo " - iut: $iut"
echo " - ics (level): $level"
echo " - sch: $schematron"
echo ""
echo "Results:"
echo ""
echo " - In HTML: \`$name_noext-$level.html <$outputname.html>\`_"
echo " - In XML: \`$name_noext-$level.xml <$outputname.xml>\`_"

curl -s "http://cite-dev-03.opengeospatial.org/teamengine/rest/suites/iso19139/1.0/run?iut=$iut&ics=$level&sch=$schematron" > $outputname.xml


java -jar /Applications/oxygen/lib/saxon.jar -o $outputname.html $outputname.xml tabular.xsl
