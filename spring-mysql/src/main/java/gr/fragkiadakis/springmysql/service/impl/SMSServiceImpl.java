package gr.fragkiadakis.springmysql.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.okhttp.*;
import gr.fragkiadakis.springmysql.service.SMSService;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Service
public class SMSServiceImpl implements SMSService {
    private static String MESSAGE_HIGH_TEMPERATURE = "Your name and Temperature more than 20C. ";
    private static String MESSAGE_LOW_TEMPERATURE = "Your name and Temperature less than 20C. ";

    // add your application id and secret key here
    private static String APPLICATION_ID = "";
    private static String APPLICATION_SECRET = "";

    @Override
    public String getAccessToken () throws IOException, InterruptedException {
        try {
            // encode id and secret to get access token
            String toEncode = APPLICATION_ID + ":" +APPLICATION_SECRET;
            String encodedString = Base64.getEncoder().encodeToString(toEncode.getBytes(StandardCharsets.UTF_8));
            MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
            RequestBody body = RequestBody.create(mediaType, "grant_type=client_credentials");
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://auth.routee.net/oauth/token"))
                    .header("authorization", "Basic " + encodedString)
                    .header("content-type", "application/x-www-form-urlencoded")
                    .method("POST", HttpRequest.BodyPublishers.ofString("grant_type=client_credentials"))
                    .build();
            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            // object mapper to get access token from json
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonResponse = mapper.readTree(response.body());
            System.out.println(jsonResponse.get("access_token"));
            return jsonResponse.get("access_token").asText();
        } catch (IOException ioException) {
            System.out.println("IOException: " + ioException.getMessage());
        } catch (InterruptedException interruptedException) {
            System.out.println("IOException: " + interruptedException.getMessage());
        }
        // if something goes wrong return empty access token
        return "";

    }

    @Override
    public void sendSMS(String accessToken, String temperature) throws IOException {
        try {
            OkHttpClient client = new OkHttpClient();
            // format message based on temperature value
            String bodyMessage = bodyMessage(temperature);
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, "{ \"body\": \"" + bodyMessage + "\",\"to\" : \"+306922222222\",\"from\": \"amdTelecom\"}");
            Request request = new Request.Builder()
                    .url("https://connect.routee.net/sms")
                    .post(body)
                    .addHeader("authorization", "Bearer " + accessToken)
                    .addHeader("content-type", "application/json")
                    .build();

            Response response = client.newCall(request).execute();
            // if sms was sent successfully log this message
            System.out.println("SMS sent successfully");
        } catch (IOException ioException) {
            System.out.println(ioException.getMessage());
        }
    }

    public  String bodyMessage (String temperature) {
        float f = Float.parseFloat(temperature);
        if (f > 20.0) return MESSAGE_HIGH_TEMPERATURE + temperature;
        else return MESSAGE_LOW_TEMPERATURE + temperature;
    }
}
