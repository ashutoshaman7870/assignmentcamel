package com.personal.assignment.passwors;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Random;

@Configuration
public class generator {

    @Bean
    public  String generatePass() {

        String upper ="QWERTYUIOPLKJHGFDSAZXCVBNM";
        String lower ="qwertyuioplkjhgfdsazxcvbnm";
        String nums ="1234567890";
        String specialch="~!@#$%^&*(){}[]_+-=?><";

        String combination=upper+lower+nums+specialch;

        int len=8;
        char[] password=new char[len];
        Random r=new Random();

        for (int i = 0; i < len; i++) {

            password[i]=combination.charAt(r.nextInt(combination.length()));

        }
        return new String(password);
    }
}
