package org.example.commands;

import org.example.bot.WeatherApi;

import java.util.Map;

public class GetTemperatureCommand extends Command {
    public WeatherApi weatherApi;
    public GetTemperatureCommand(){
        weatherApi = new WeatherApi();
        triggerCommand = "Погода";
        currentState = 0;
        answerByState = Map.of(
                0, ""
        );
    }

    @Override
    public String getAnswer(){
        String resp = weatherApi.getTemperature();
        return resp;
    }

    @Override
    public Map<String, String> getData() {
        return Map.of();
    }

    @Override
    public void addData(String userText) {

    }
}
