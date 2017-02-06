package com.mocknidhi.persistence.entity;

import lombok.Data;

/**
 * Created by anilkumartalla on 2/5/17.
 */
@Data
public class MockContent {

    private Integer size;
    private String mimeType;
    private Integer compression;
    private String text;
    private String comment;

}
