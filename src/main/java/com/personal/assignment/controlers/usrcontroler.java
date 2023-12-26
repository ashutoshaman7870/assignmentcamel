package com.personal.assignment.controlers;

import com.personal.assignment.model.users;
import org.apache.camel.ProducerTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
public class usrcontroler {


    @Autowired
    ProducerTemplate producerTemplate;

    //register user
    @RequestMapping(value = "/reguser",consumes = "application/json",method = RequestMethod.POST)
    public boolean resisterUser(@RequestBody users usr){

        users usersifexist = producerTemplate.requestBody("direct:getone", usr.getUserName(), users.class);
        if (usersifexist!=null){
            producerTemplate.requestBody("direct:insert",usr);

            return true;
        }
        else return false;


    }


    //delete users
    @RequestMapping(value = "/deluser",consumes = "application/json",method = RequestMethod.POST)
    public String deleteUser(@RequestBody String uid){
        producerTemplate.requestBody("direct:delete",uid);

        return "deleted user "+uid;

    }


    @RequestMapping(value = "/getall",consumes = "application/json",method = RequestMethod.GET)
    public List<users> getall(){
        List<users> user = producerTemplate.requestBody("direct:select", null, List.class);
        return user;

    }







}
