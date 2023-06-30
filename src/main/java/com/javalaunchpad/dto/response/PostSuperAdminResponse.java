package com.javalaunchpad.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class PostSuperAdminResponse {
    private String title ;
    private String author ;
    private String categories ;
    private  String tags ;
    private LocalDateTime  lastUpdate ;
    private String status ;
    private Integer commentCount;
    private Integer views;

}
