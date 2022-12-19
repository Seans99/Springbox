package com.project.springbox.message;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ResponseFile {
    private String fileName;
    private String url;
    private String type;
    private long size;
    private String username;
}
