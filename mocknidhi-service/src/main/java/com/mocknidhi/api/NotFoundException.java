package com.mocknidhi.api;

/**
 * @author Anil.Talla
 */
public class NotFoundException extends RuntimeException{

    public NotFoundException(String message){
        super(message);
    }
}
