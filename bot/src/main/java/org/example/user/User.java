package org.example.user;

import org.example.commands.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class User {
    private final String userChatId;
    public List<Command> commandsData;
    private Map<String, Map<String, String>> userTasks;

    public User(String chatId) {
        userChatId = chatId;
        commandsData = List.of(
                new StartCommand(),
                new HelpCommand(),
                new NewTaskCommand(),
                new GetTaskCommand(this)
        );
        userTasks = new HashMap<String, Map<String, String>>();
    }

    public void addTask(String date, String time, String task) {
        userTasks.put(date, Map.of(time, task));
    }

    public List<String> getUserTasks() {
        List<String> temp = new ArrayList<>();
        for (String date : userTasks.keySet()) {
            for (String time: userTasks.get(date).keySet()){
                String task = userTasks.get(date).get(time);
                temp.add(String.format("%s %s - %s", date, time, task));
            }
        }
        return temp;
    }

    public String getUserChatId() {
        return userChatId;
    }


}
