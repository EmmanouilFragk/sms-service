package gr.fragkiadakis.springmysql.service;

import java.io.IOException;

public interface SMSService {
    String getAccessToken() throws IOException, InterruptedException;
    void sendSMS(String accessToken, String temperature) throws IOException;

}
