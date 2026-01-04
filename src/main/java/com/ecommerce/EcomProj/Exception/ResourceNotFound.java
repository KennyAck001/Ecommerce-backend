package com.ecommerce.EcomProj.Exception;

public class ResourceNotFound extends RuntimeException{
    String resourseName;
    String field;
    String fieldName;
    Long resourseId;

    public ResourceNotFound(){}

    public ResourceNotFound(String resourseName, String field, String fieldName) {
        super(String.format("%s not found with %s: %s", resourseName, field, fieldName));
        this.resourseName = resourseName;
        this.field = field;
        this.fieldName = fieldName;
    }

    public ResourceNotFound(String resourseName, String field, Long resourseId) {
        super(String.format("%s not found with %s: %d", resourseName, field, resourseId));
        this.resourseName = resourseName;
        this.field = field;
        this.resourseId = resourseId;
    }
}
