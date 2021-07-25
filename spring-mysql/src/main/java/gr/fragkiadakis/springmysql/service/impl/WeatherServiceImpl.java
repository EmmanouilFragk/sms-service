package gr.fragkiadakis.springmysql.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import gr.fragkiadakis.springmysql.service.WeatherService;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class WeatherServiceImpl implements WeatherService {

    // add your api here
    private final String API_KEY = "";

    @Override
    public String getCityTemperature(String cityName) throws IOException, InterruptedException {
        try {
            ObjectMapper mapper = new ObjectMapper();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://api.openweathermap.org/data/2.5/weather?q="+cityName+"&appid=" + API_KEY +"&units=metric"))
                    .method("GET", HttpRequest.BodyPublishers.noBody())
                    .build();
            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());


            JsonNode rootNode = mapper.readTree(response.body());
            JsonNode temperature = mapper.readTree(String.valueOf(rootNode.get("main")));
            System.out.println(temperature.get("temp"));
            return temperature.get("temp").asText();
        } catch (IOException ioException) {
            System.out.println(ioException.getMessage());
        } catch (InterruptedException interruptedException) {
            System.out.println(interruptedException.getMessage());
        }
        return "";
    }
}
