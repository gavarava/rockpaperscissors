package com.rps.infrastructure.players;

public class NoPlayerCreatedResponse implements PlayerCreationResponse {

    private String message;

    public NoPlayerCreatedResponse(String responseMessage) {
        this.message = responseMessage;
    }

    public String getMessage() {
        return message;
    }
}
