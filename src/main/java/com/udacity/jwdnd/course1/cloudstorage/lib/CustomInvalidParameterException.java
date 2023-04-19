package com.udacity.jwdnd.course1.cloudstorage.lib;

import java.security.InvalidParameterException;
import java.util.List;

public class CustomInvalidParameterException extends InvalidParameterException {

    private List<String> errorMessages;

    public CustomInvalidParameterException (List<String> errorMessages) {
        this.errorMessages = errorMessages;
    }

    public List<String> getErrorMessages() {
        return errorMessages;
    }
}
