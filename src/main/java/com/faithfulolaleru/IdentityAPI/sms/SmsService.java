package com.faithfulolaleru.IdentityAPI.sms;

import com.faithfulolaleru.IdentityAPI.config.TwilioConfig;
import com.faithfulolaleru.IdentityAPI.exception.ErrorResponse;
import com.faithfulolaleru.IdentityAPI.exception.GeneralException;
import com.faithfulolaleru.IdentityAPI.utils.Utils;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.net.URI;

@Slf4j
@AllArgsConstructor
@Service
public class SmsService implements SmsSender {

    private final TwilioConfig twilioConfig;


    @Override
    public void send(String receiver, String message) {

        Twilio.init(twilioConfig.getAccountSid(), twilioConfig.getAuthToken());

        String formatNumber = Utils.toMsisdn(receiver);
        Message response;
        try {
            response = Message.creator(new PhoneNumber(formatNumber),
                    new PhoneNumber(twilioConfig.getSender()), message)
                    .create();

            log.info("Response from twilio sms --> {} ", response);

            // return response;

        } catch (Exception e) {
            e.printStackTrace();
            throw new GeneralException(HttpStatus.BAD_REQUEST, ErrorResponse.ERROR_SMS,
                    "Failed to send Sms");
        }
    }
}
