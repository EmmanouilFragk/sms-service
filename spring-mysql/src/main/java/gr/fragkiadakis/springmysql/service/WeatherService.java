package gr.fragkiadakis.springmysql.service;

import java.io.IOException;

public interface WeatherService {
    String getCityTemperature(String cityName) throws IOException, InterruptedException;
}
