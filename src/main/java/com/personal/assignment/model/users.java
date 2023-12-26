package com.personal.assignment.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//lombock
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor


public class users {


    private String id;

    private String userName;

    private String userPassword;

    private String isDeleted;
}
