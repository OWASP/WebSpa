/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.seleucus.wsp.crypto.fwknop;

/**
 *
 * @author pgolen
 */
public class FwknopException extends Exception {
    
    public FwknopException(String message) {
        super(message);
    }
    
    public FwknopException(String message, Throwable throwable) {
        super(message, throwable);
    }
    
}
