package com.example.springboot01.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.example.springboot01.constant.DataBaseEnum.DocType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Doc implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;

    private DocType kind;

    private String uploadName;

    private String path;

    private Long size;

    private String md5;

    private String mimeType;

    private LocalDateTime uploadOn;
}