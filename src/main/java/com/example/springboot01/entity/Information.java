package com.example.springboot01.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Information implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;

    private String name;
}
