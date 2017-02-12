package com.mocknidhi.persistence.entity;


import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class OperationDefinitionResponse {

    private Integer status;
    private String statusText;
    private ResponseBody content;
    private List<RequestHeader> headers = new ArrayList<>();
    private String comment;

}
