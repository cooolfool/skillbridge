package com.skillbridge.exception;

import java.util.Map;

public class FieldLevelException extends  RuntimeException{
    private final Map<String,Object> fieldLevelErrors;

    public FieldLevelException(String message, Map<String, Object> warningMap){
        super(message);
        this.fieldLevelErrors = warningMap;
    }

    public Map<String, Object> getErrorMap() {
        return fieldLevelErrors;
    }
}
