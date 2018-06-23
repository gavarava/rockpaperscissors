package com.rps.infrastructure.players;

public class NoPlayerCreatedResponse implements PlayerResponse {

    private String message;

    public NoPlayerCreatedResponse(String responseMessage) {
        this.message = responseMessage;
    }

    public String getMessage() {
        return message;
    }
}
