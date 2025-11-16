package com.aiqueen.service;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class TwilioService {
  @Value("${twilio.account-sid:}")
  private String sid;
  @Value("${twilio.auth-token:}")
  private String token;
  @Value("${twilio.whatsapp-number:}")
  private String whatsappNumber;

  public void init() {
    if (sid!=null && !sid.isBlank() && token!=null && !token.isBlank()) Twilio.init(sid, token);
  }

  public void sendWhatsApp(String to, String body) {
    init();
    Message.creator(new PhoneNumber("whatsapp:"+to), new PhoneNumber(whatsappNumber), body).create();
  }
}
