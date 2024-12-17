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
                0, Map.of(
                        0, "Выберите дату текущего месяца:",
                        1, Markup.Calendar
                ),
                1, Map.of(
                        0, "Введи время в формате ХХ.ХХ",
                        1, Markup.None
                ),
                2, Map.of(
                        0,"Оки, напиши задачу",
                        1, Markup.None
                ),
                3, Map.of(
                        0, "Задача добавлена✅",
                        1, Markup.Menu
                )
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
