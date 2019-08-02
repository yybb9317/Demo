package com.example.springboot01.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Activity implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;

    private String activityName;

    private Date startTime;

    private Date endTime;

    private String province;

    private String city;

    private String district;

    private String location;

    private String cover;

    private String detail;

    private Integer costPoint;

    private Date createdOn;

    private Date updatedOn;

    private Byte state;

    private String createdBy;

    private String rejectReason;
}