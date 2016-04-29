# ISO 19139 Release Notes

## Revision r5 (20160429)
- moved information from readme to index.md under the site folder
- [Pull Request 11](https://github.com/opengeospatial/ets-19139/pull/11) - Includes NOAA schemas profile.


## Revision r4

- Changes made in TeamEngine-ServiceProvider for Conformance Level 3: Added REST API Handler in Teamengine Service Provider Module to handle POST requests where both Metadata and Schematron are sent as files in post body.

## Revision r3

   - Test Cases: Capabiloty 3 test to check whether the XML conforms to A.3 clause of ISO 19139.
   - Rest Suite for ISO 19139: Using rest suite of ISO 19139 to validate whether the XML conforms to A.3 clause of ISO 19139. User friendly error message is shown after the tests fail.
   - Error handling is done for the following cases: When user select level 1 or (level 1+level 2) at that time no conformance level A.3 rules not run. when user select (level 1+level 2+level 3) at that time if user not give schematron file or give wrong input path then result should be failed and give failure reason on final report.
   - JUnit Tests (both positive and negative scenarios) are done for the following test class: Capability3Tests

## Revision r2

   - Test Cases: Capability 2 test to check whether the XML conforms to A.2 clause of ISO 19139.
   - Rest Suite for ISO 19139: Using rest suite of ISO 19139 to validate whether the XML conforms to A.2 clause of ISO 19139,  User friendly error message is shown after the tests fail.
   - Error handling is done for the following cases: When user select level 1 at that time no conformance level A.2 rules not run.
   -  JUnit Tests (both positive and negative scenarios) are done for the following test class: Capability2Tests

## Revision r1
Changes made in Test Report:
   - Added summary of the result in "Result overview" page including pie chart. It includes following statistics: Test Name and Version, Session Id, Result, Input, Date and Time Performed
   - Added these Contents on Reporter Output Page: Total no of passed tests, Total no of skipped tests, Total no of failed tests, Final result of test.
   - Added description column in the Result page.
   - Added user friendly error messages in the Result page.
   - Added user friendly name of test in the Result page.
   - Changed the styling if the Result page


## Revision r0

- Test Cases: 
   - Capabilty 1 test to check whether the XML conforms to A.1 clause of ISO 19139.
   - Input Validation test to chcek whether the given input exists i.e the file path or URL actually to an existing resource.
   - Input Validation test to chcek whether the given input is an XML file.

- Rest Suite for ISO 19139:
   - Using rest suite of ISO 19139 to validate whether the XML conforms to A.1 clause of ISO 19139.
   - User friendly error message is shown after the tests fail.

- Error handling is done for the following cases:
   - When no XML file is passed as an argument to the test.
   - When path to the XML file is not valid.
   - When the given input XML file is corrupt.
-   JUnit Tests (both positive and negative scenarios) are done for the following test classes:
   - ValidateInputTests
   - Capability1Tests


