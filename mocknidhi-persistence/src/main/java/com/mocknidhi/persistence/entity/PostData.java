package com.mocknidhi.persistence.entity;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PostData {

    private String mimeType;
    private String text;
    private List<RequestParam> params = new ArrayList<>();


}
