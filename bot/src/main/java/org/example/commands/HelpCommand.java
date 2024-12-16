package org.example.commands;

import java.util.Map;

public class HelpCommand extends Command {
    public HelpCommand(){
        this.triggerCommand = "/help";
        this.currentState = 0;
        this.answerByState = Map.of(
                0, Map.of(
                        0, "Вот список моих комманд: '/start', '/help', '/clear', 'Ежедневник', 'Дела', 'Погода'",
                        1, Markup.Menu
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
