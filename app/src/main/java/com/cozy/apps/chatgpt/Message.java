package com.cozy.apps.chatgpt;

import java.util.Date;

 class Message {
    private String sender;
    private String content;
    private String timestamp;

    public Message(String sender, String content, String timestamp) {
        this.sender = sender;
        this.content = content;
        this.timestamp = timestamp;
    }

    public String getSender() {
        return sender;
    }

    public String getContent() {
        return content;
    }

    public String getTimestamp() {
        return timestamp;
    }
}