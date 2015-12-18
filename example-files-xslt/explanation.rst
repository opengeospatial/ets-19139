Using the OGC TEAM Engine validator to test metadata instances
================================================================

Introduction
---------------
The validator test three options:

- level 1: schema test
- level 2: schematron A.2.1 By-Value or By-Reference or gco:nilReason, and A.2.2 Co-Constraints
- level 3: user provided schematron


Example files
--------------

- Schematron Example: `ISO Best Practices <http://54.209.245.204/teamengine/test-arg-ISOStyle.sch>`_.
- `File that passes level 1, 2 and not 3 <http://54.209.245.204/teamengine/test-arg-inValidXMLFileCase2.xml>`_.


Testing Level 1 - Passes, no schematron provided
------------------------------------------------------
Testing `this URL: <http://cite-dev-03.opengeospatial.org/teamengine/rest/suites/iso19139/1.0/run?iut=http://54.209.245.204/teamengine/test-arg-inValidXMLFileCase2.xml&ics=1>`_:

 - iut: http://54.209.245.204/teamengine/test-arg-inValidXMLFileCase2.xml
 - ics (level): 1

Results:

 - In HTML: `test-arg-inValidXMLFileCase2-1.html <test-arg-inValidXMLFileCase2-1.html>`_
 - In XML: `test-arg-inValidXMLFileCase2-1.xml <test-arg-inValidXMLFileCase2-1.xml>`_


Testing Level 2 - Passes, no schematron provided
----------------------------------------------------
Testing `this URL: <http://cite-dev-03.opengeospatial.org/teamengine/rest/suites/iso19139/1.0/run?iut=http://54.209.245.204/teamengine/test-arg-inValidXMLFileCase2.xml&ics=2>`_:

 - iut: http://54.209.245.204/teamengine/test-arg-inValidXMLFileCase2.xml
 - ics (level): 2


Results:

 - In HTML: `test-arg-inValidXMLFileCase2-2.html <test-arg-inValidXMLFileCase2-2.html>`_
 - In XML: `test-arg-inValidXMLFileCase2-2.xml <test-arg-inValidXMLFileCase2-2.xml>`_


Testing Level 3 - Invalid XML fail due to provided schematron
-----------------------------------------------------------------
Testing `this URL: <http://cite-dev-03.opengeospatial.org/teamengine/rest/suites/iso19139/1.0/run?iut=http://54.209.245.204/teamengine/test-arg-inValidXMLFileCase2.xml&ics=3&sch=http://54.209.245.204/teamengine/test-arg-ISOStyle.sch>`_:

 - iut: http://54.209.245.204/teamengine/test-arg-inValidXMLFileCase2.xml
 - ics (level): 3
 - sch: http://54.209.245.204/teamengine/test-arg-ISOStyle.sch

Results:

 - In HTML: `test-arg-inValidXMLFileCase2-3.html <test-arg-inValidXMLFileCase2-3.html>`_
 - In XML: `test-arg-inValidXMLFileCase2-3.xml <test-arg-inValidXMLFileCase2-3.xml>`_

Testing Level 3 with a wrong schematron
------------------------------------------------
Testing `this URL: <http://cite-dev-03.opengeospatial.org/teamengine/rest/suites/iso19139/1.0/run?iut=http://54.209.245.204/teamengine/test-arg-inValidXMLFileCase2.xml&ics=3&sch=werwerwe.sch>`_:

 - iut: http://54.209.245.204/teamengine/test-arg-inValidXMLFileCase2.xml
 - ics (level): 3
 - sch: werwerwe.sch

Results:

 - In HTML: `test-arg-inValidXMLFileCase2-3.html <test-arg-inValidXMLFileCase2-3-no-sc.html>`_
 - In XML: `test-arg-inValidXMLFileCase2-3.xml <test-arg-inValidXMLFileCase2-3-no-sc.xml>`_

Testing Level 3 with correct instance 
----------------------------------------

Testing `this URL: <http://cite-dev-03.opengeospatial.org/teamengine/rest/suites/iso19139/1.0/run?iut=http://54.209.245.204/teamengine/test-arg-validXMLFile.xml&ics=3&sch=http://54.209.245.204/teamengine/test-arg-ISOStyle.sch>`_:

 - iut: http://54.209.245.204/teamengine/test-arg-validXMLFile.xml
 - ics (level): 3
 - sch: http://54.209.245.204/teamengine/test-arg-ISOStyle.sch

Results:

 - In HTML: `test-arg-validXMLFile-3.html <test-arg-validXMLFile-3-pass.html>`_
 - In XML: `test-arg-validXMLFile-3.xml <test-arg-validXMLFile-3-pass.xml>`_





