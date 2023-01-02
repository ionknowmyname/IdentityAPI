package com.faithfulolaleru.IdentityAPI.sms;

public interface SmsSender {

    void send(String receiver, String message);
}
