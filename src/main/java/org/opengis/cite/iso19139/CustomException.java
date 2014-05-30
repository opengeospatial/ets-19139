/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.opengis.cite.iso19139;

/**
 *
 * @author tanvishah
 * To customized the exceptions thrown when the Input File is invalid or when the Input File does not exist
 */
public class CustomException extends Exception {

    public CustomException(String message) {
         System.out.println(message);
    }

    public CustomException(String message, Throwable throwable) {
        super(message, throwable);
    }

}
