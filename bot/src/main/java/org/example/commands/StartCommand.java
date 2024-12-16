package org.example.commands;

import java.util.Map;

public class StartCommand extends Command {
    public StartCommand() {
        this.triggerCommand = "/start";
        this.currentState = 0;
        this.answerByState = Map.of(
                0, Map.of(
                        0, "Привет! Я бот Этернал.\nЯ твой помощник в ежедневной рутине\uD83D\uDC7E\nТы можешь записывать свои задачи, а я\nнапомню тебе о них. Также ты можешь\nпосмотреть прогноз погоды и прогноз звезд.\uD83C\uDF1F",
                        1, Markup.None
                )
        );
    }

    @Override
    public Map<String, String> getData() {
        return Map.of();
    }

    @Override
    public void addData(String userText) {
        return;
    }
}
