package org.example.commands;

import java.util.Map;

public class HelpCommand extends Command {
    public HelpCommand(){
        this.triggerCommand = "/help";
        this.currentState = 0;
        this.answerByState = Map.of(
                0, "Вот список моих комманд: '/start', '/help', '/add_task'"
        );
    }
    @Override
    public String getAnswer() {
        return answerByState.get(currentState);
    }
}