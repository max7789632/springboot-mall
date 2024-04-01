package com.maxdeng.springbootmall.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.Date;

@Data
public class User {

    private Integer userId;

    //    @JsonProperty("e_mail")
    // 讓 springboot 在轉換 User object 時，自動將這個變數的 key 轉換成 e_mail
    private String email;

    @JsonIgnore
    // 讓 springboot 在轉換 User object 時，忽略這個變數，這樣就不會回傳到前端
    private String password;
    private Date createdDate;
    private Date lastModifiedDate;
}
