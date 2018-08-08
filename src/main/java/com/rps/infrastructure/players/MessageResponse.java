package com.rps.infrastructure.players;

public class MessageResponse implements PlayerResponse {

    private String message;

    public MessageResponse(String responseMessage) {
        this.message = responseMessage;
    }

    public String getMessage() {
        return message;
    }
}
