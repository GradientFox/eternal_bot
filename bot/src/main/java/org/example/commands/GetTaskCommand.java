package org.example.commands;

import org.example.user.User;

import java.util.List;
import java.util.Map;

public class GetTaskCommand extends Command {
    User user;
    public GetTaskCommand(User user) {
        this.user = user;
        triggerCommand = "Дела";
        currentState = 0;
        answerByState = Map.of(
                0, "У тебя пока нет задач, если хочешь их добавить напиши 'Ежедневник'"
        );
    }


    @Override
    public String getAnswer(){
        String response = "";
        List<String> temp = user.getUserTasks();
        if (temp.isEmpty())
            response = answerByState.get(currentState);
        else
            response = String.format("Твои задачи \n%s", String.join("\n", temp));
        return response;
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
