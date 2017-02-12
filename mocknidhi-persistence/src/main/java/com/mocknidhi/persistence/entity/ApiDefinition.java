package com.mocknidhi.persistence.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by anilkumartalla on 2/11/17.
 */
@Data
public class ApiDefinition implements Serializable {

    @Id
    private UUID id;
    private String name;
    private String version;
    private String description;
    private String license;
    private String licenseURL;
    private String contextPath;
    private String baseURI;
    private Integer port;
    private String httpVersion;
    private Boolean active;
    private Integer responseDelayInSeconds;
    private List<OperationDefinition> operationDefinitions = new ArrayList<>();
}
