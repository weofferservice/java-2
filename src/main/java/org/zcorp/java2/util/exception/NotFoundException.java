package org.zcorp.java2.util.exception;

import org.springframework.lang.NonNull;

public class NotFoundException extends Java2Exception {
    public NotFoundException(@NonNull String message) {
        super(message);
    }
}