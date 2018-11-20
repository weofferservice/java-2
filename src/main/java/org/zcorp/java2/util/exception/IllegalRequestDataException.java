package org.zcorp.java2.util.exception;

import org.springframework.lang.NonNull;

public class IllegalRequestDataException extends Java2Exception {
    public IllegalRequestDataException(@NonNull String msg) {
        super(msg);
    }
}