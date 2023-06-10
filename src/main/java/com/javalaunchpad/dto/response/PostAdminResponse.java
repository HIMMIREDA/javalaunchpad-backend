package com.javalaunchpad.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostAdminResponse {
   private String title ;
   private String author ;
   private String categories ;
   private  String tags ;
   private LocalDateTime creationDate ;
   private LocalDateTime  lastUpdate ;
   private String status ;
}
