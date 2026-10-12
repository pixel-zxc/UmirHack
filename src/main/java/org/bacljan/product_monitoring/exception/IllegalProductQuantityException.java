package org.bacljan.product_monitoring.exception;

public class IllegalProductQuantityException extends RuntimeException{
    public IllegalProductQuantityException(String s) {
        super(s);
    }
}
