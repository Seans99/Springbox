package com.project.springbox.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class FileDTO {
    private String fileName;
    private String url;
    private String contentType;
    private long size;
}
