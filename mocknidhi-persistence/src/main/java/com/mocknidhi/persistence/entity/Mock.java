package com.mocknidhi.persistence.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by anilkumartalla on 2/5/17.
 */
@Data
public class Mock implements Serializable {

    @Id
    private UUID id;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
    private Integer status;
    private String statusText;
    private String httpVersion;
    private Map<String, String> headers = new LinkedHashMap<String, String>();
    private MockContent content;
    private String redirectURL;
    private Integer headersSize;
    private Integer bodySize;

}
