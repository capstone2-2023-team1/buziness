package com.example.nerfbiz.src.convert.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Target {
    private String targetID;
    private String inputVideoUrl;
    private String renderingUrl;
}
