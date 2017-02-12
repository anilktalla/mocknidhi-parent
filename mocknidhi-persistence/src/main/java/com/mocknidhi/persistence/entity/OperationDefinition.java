package com.mocknidhi.persistence.entity;

import lombok.Data;

import java.util.UUID;

/**
 * @author Anil.Talla
 */
@Data
public class OperationDefinition {

    private UUID id;
    private String description;
    private String displayName;
    private String resourcePath;
    private String verb;
    private Integer responseDelayInSeconds;
    private Boolean active;
    private OperationDefinitionRequest request;
    private OperationDefinitionResponse response;
}
