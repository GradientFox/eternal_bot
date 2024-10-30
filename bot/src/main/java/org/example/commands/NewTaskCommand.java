package org.example.commands;

import java.lang.ref.Cleaner;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NewTaskCommand extends Command {
    private Map<String, String> data;

    public NewTaskCommand() {
        triggerCommand = "Ежедневник";
        currentState = 0;
        answerByState = Map.of(
                0, "Введи дату в формате ДД.ММ.ГГГГ",
                1, "Введи время в формате ХХ.ХХ",
                2, "Оки, напиши задачу",
                3, "Задача добавлена✅"
        );
        data = new HashMap<>(Map.of(
                "date", "",
                "time", "",
                "task", ""
        ));
    }

    @Override
    public void addData(String userText) {
        switch (currentState) {
            case (1):
                data.put("date", userText);
                break;
            case (2):
                data.put("time", userText);
                break;
            case (3):
                data.put("task", userText);
                break;
        }
    }

    @Override
    public Map<String, String> getData() {
        return data;
    }

}
