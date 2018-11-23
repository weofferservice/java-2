package org.zcorp.java2.util.exception;

import static org.springframework.http.HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS;
import static org.zcorp.java2.util.exception.ErrorType.APP_ERROR;

public class ModificationRestrictionException extends ApplicationException {
    public static final String EXCEPTION_MODIFICATION_RESTRICTION = "exception.userModificationRestriction";

    public ModificationRestrictionException(String userName) {
        super(UNAVAILABLE_FOR_LEGAL_REASONS, // 451
                APP_ERROR,
                EXCEPTION_MODIFICATION_RESTRICTION,
                userName);
    }
}