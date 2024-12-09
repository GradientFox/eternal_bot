package org.example.commands;

import org.example.user.User;

import java.util.Map;

public class RemoveTaskCommand extends Command {
    User user;

    public RemoveTaskCommand(User usr){
        user = usr;
        triggerCommand = "/clear";
        currentState = 0;
        answerByState = Map.of(
                0, "Готово."
        );
    }



    @Override
    public Map<String, String> getData() {
        return Map.of();
    }

    @Override
    public void addData(String userText) {

    }
}
