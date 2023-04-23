package com.example.demo.utils;

import java.util.Random;

import org.springframework.stereotype.Component;

@Component
public class Utils {
	public String generateRandomString() {
	    int maxLength = 7;
	    String caracteres = "abcdefghijklmnopqrstuvwxyz0123456789";
	    Random rnd = new Random();
	    StringBuilder sb = new StringBuilder(maxLength);
	    for (int i = 0; i < maxLength; i++) {
	        int index = rnd.nextInt(caracteres.length());
	        sb.append(caracteres.charAt(index));
	    }
	    return sb.toString();
	}
}
