package org.zcorp.java2;

import javax.validation.groups.Default;

public class ValidationGroup {
    // Validate only for form/AJAX/REST
    public interface Web extends Default {}

    // Validate only when DB save/update
    public interface Persist extends Default {}
}