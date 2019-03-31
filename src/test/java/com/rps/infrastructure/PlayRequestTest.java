package com.rps.infrastructure;

import static org.junit.Assert.*;

import com.rps.application.RPSException;
import org.junit.Test;

public class PlayRequestTest {

  @Test
  public void shouldThrowExceptionWhenPlayRequestIsIncomplete() {
    try {
      PlayRequest playRequest = new PlayRequest(null,null,null);
      fail();
    } catch (Exception e) {

    }
  }

}