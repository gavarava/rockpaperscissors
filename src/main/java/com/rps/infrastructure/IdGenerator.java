package com.rps.infrastructure;

public class IdGenerator {

  public static String randomId() {
    return String.valueOf(System.currentTimeMillis());
  }

}
