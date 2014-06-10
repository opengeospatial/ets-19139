/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.opengis.cite.iso19139;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import javax.xml.parsers.DocumentBuilderFactory;
import org.xml.sax.InputSource;

/**
 *
 * @author tanvi Input validator class is used to check whether a given file
 * actually exists and also to check if the file's extension
 */
public class InputValidator {

    private boolean validate;

    public boolean pathValid(String pathSent) throws IOException {

        if (pathSent.startsWith("http:")) {
            URL urlValidator = new URL(pathSent);
            HttpURLConnection http;
            http = (HttpURLConnection) urlValidator.openConnection();
            int statusCode = http.getResponseCode();
            if (statusCode == 200) {
                return true;
            } else {
                return false;
            }

        } else if (pathSent.startsWith("file:")) {
            File path = new File(pathSent);
            URL fileUrl;
            try {
                fileUrl = new URL(pathSent);
                File completePath = new File(fileUrl.getPath());
                return completePath.exists();

            } catch (MalformedURLException ex) {
                Logger.getLogger(InputValidator.class.getName()).log(Level.SEVERE, null, ex);
                return false;
            }
        } else {
            try {
                File completePath = new File(pathSent);
                return completePath.exists();

            } catch (Exception ex) {
                Logger.getLogger(InputValidator.class.getName()).log(Level.SEVERE, null, ex);
                return false;
            }
        }
    }

    public boolean extension(String pathSent, String extensionWanted) {

        String extensionWantedLower = extensionWanted.toLowerCase();
        String extensionWantedUpper = extensionWanted.toUpperCase();
        if (pathSent.endsWith(extensionWantedLower) || pathSent.endsWith(extensionWantedUpper)) {
            return true;
        } else {
            // Check whether the file contents are that of an XML file even if the extension doesn't say so
            // Create a new factory to create parsers that will
            // be aware of namespaces and will validate or
            // not according to the flag setting.
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setValidating(validate);
            dbf.setNamespaceAware(true);

            // Use the factory to create a parser (builder) and use
            // it to parse the document.
            try {
                DocumentBuilder builder = dbf.newDocumentBuilder();
                //builder.setErrorHandler(new MyErrorHandler());
                InputSource is = new InputSource(pathSent);
                Document doc = builder.parse(is);
                return true;
            } catch (Exception e) {
                return false;

            }
        }
    }
}
