package com.mocknidhi.persistence.entity;


import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class OperationDefinitionRequest {

    private String method;
    private List<RequestParam> queryParams = new ArrayList<>();
    private PostData postData = null;
    private List<RequestHeader> headers = new ArrayList<>();
    private String comment = null;
}
