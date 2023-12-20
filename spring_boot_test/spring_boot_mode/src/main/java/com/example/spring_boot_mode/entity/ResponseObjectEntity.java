package com.example.spring_boot_mode.entity;

import io.swagger.models.auth.In;

public class ResponseObjectEntity {
    private Integer code;
    private Boolean successful;
    private Object resultValue;

    public ResponseObjectEntity() {
    }
    public ResponseObjectEntity(Integer code, Boolean successful, Object resultValue) {
        this.code = code;
        this.successful = successful;
        this.resultValue = resultValue;
    }


    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Boolean getSuccessful() {
        return successful;
    }

    public void setSuccessful(Boolean successful) {
        this.successful = successful;
    }

    public Object getResultValue() {
        return resultValue;
    }

    public void setResultValue(Object resultValue) {
        this.resultValue = resultValue;
    }

}
