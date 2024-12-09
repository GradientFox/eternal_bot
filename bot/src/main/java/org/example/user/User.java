package org.example.user;

import org.example.commands.*;
import org.example.db.DataBase;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class User {
    private final String userChatId;
    public List<Command> commandsData;
    DataBase dataBase;


    public User(String chatId, DataBase db) {
        dataBase = db;
        userChatId = chatId;
        commandsData = List.of(
                new StartCommand(),
                new HelpCommand(),
                new NewTaskCommand(),
                new GetTaskCommand(this),
                new RemoveTaskCommand(this)g
        );
    }

    public void addTask(String date, String time, String task) {
        dataBase.updateUser(userChatId, task, date, time);
    }

    public void clearTable() {
        dataBase.clearTable(userChatId);
    }

    public List<String> getUserTasks() {
        List<String> temp = new ArrayList<>();
        Map<String, Map<String, String>> userTasks = dataBase.getUserTask(userChatId);
        for (String task : userTasks.keySet()) {
            for (String date : userTasks.get(task).keySet()) {
                String time = userTasks.get(task).get(date);
                temp.add(String.format("%s / %s - %s", task, date, time));
            }
        }
        return temp;
    }

    public String getUserChatId() {
        return userChatId;
    }


}
