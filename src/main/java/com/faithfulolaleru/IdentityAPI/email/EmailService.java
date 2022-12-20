package com.faithfulolaleru.IdentityAPI.email;

import com.faithfulolaleru.IdentityAPI.exception.ErrorResponse;
import com.faithfulolaleru.IdentityAPI.exception.GeneralException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
@AllArgsConstructor
@Slf4j
public class EmailService implements EmailSender {

    private final JavaMailSender mailSender;


    @Override
    @Async    //we dont want this to block a client
    public void send(String to, String email) {

        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper =
                    new MimeMessageHelper(mimeMessage, true);  // multipart can be true // encoding can be "utf-8"
            helper.setText(email, "true");  // true for if we want it as html
            helper.setTo(to);
            helper.setSubject("Confirm your email");
            helper.setFrom("Testing456@gmail.com");

            mailSender.send(mimeMessage);
        } catch (MessagingException ex) {
            log.error("failed to send email", ex);
            throw new GeneralException(HttpStatus.BAD_REQUEST, ErrorResponse.ERROR_EMAIL,
                    "Failed to send email");
        }
    }
}
