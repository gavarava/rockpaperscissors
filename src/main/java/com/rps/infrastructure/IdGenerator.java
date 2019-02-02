package com.rps.infrastructure;

public class IdGenerator {

    private IdGenerator() {
        //Disabling Instantiation
    }

    public static String randomId() {
        return String.valueOf(System.currentTimeMillis());
    }

}
