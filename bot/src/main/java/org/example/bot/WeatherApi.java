package org.example.bot;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.json.JSONObject;

public class WeatherApi {
    public String apiKey;
    public String city;
    public String units;
    public String url;
    public HttpClient httpClient;
    public HttpRequest request;

    public WeatherApi() {
        apiKey = "36d30612dc211678ddce6c90b396bf38"; // Мой ключ
        city = "Ekaterinburg";
        units = "metric";


        url = String.format(
                "https://api.openweathermap.org/data/2.5/weather?q=%s&units=%s&appid=%s",
                city, units, apiKey
        );
        httpClient = HttpClient.newHttpClient();
        request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();


    }

    public String getTemperature() {
        String resp = null;
        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());


            if (response.statusCode() == 200) {

                JSONObject jsonResponse = new JSONObject(response.body());
                JSONObject main = jsonResponse.getJSONObject("main");
                double temperature = main.getDouble("temp");

                resp = "Температура в " + city + ": " + temperature + " °C";
            } else {
                resp = "Ошибка: " + response.statusCode() + " " + response.body();
            }
        } catch (IOException | InterruptedException e) {
            resp = "Ошибка выполнения запроса: " + e.getMessage();

        }
        return resp;
    }
}
