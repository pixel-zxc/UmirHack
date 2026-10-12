package org.bacljan.product_monitoring.exception;

public class ShipmentAlreadyExistsException extends RuntimeException {
    public ShipmentAlreadyExistsException(String s) {
        super(s);
    }
}
