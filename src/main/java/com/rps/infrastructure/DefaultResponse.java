package com.rps.infrastructure;

public class DefaultResponse implements Response {

    private String reply;
    private String state;
    private int playersReady;

    public DefaultResponse(String reply, String state) {
        this.reply = reply;
        this.state = state;
    }

    public String getReply() {
        return reply;
    }

    public int getPlayersReady() {
        return playersReady;
    }

    @Override
    public String getState() {
        return this.state;
    }

    @Override
    public String toString() {
        return this.state;
    }
}
