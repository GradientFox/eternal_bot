package org.example.bot;

import org.example.commands.Command;
import org.example.db.DataBase;
import org.example.user.User;


import java.io.Console;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class HandlerBot {
    private List<User> usersData;
    private DataBase dataBase;

    public HandlerBot() {
        usersData = new ArrayList<User>();
        try {
            dataBase = new DataBase();
        } catch (SQLException e){
            System.out.println("Create DB class error");
        }
    }

    public void addUser(String chatId) {
        usersData.add(new User(chatId, dataBase));
    }

    public User getUserByChatId(String chatId) {
        if (!usersData.isEmpty()) {
            for (User user : usersData) {
                if (Objects.equals(user.getUserChatId(), chatId))
                    return user;
            }
        }
        addUser(chatId);
        return usersData.get(usersData.size() - 1);
    }

    public String getResponse(String userText, String chatId) {
        User user = getUserByChatId(chatId);
        Command currentCommand = null;
        String response = null;
        for (Command command : user.commandsData) {
            if (Objects.equals(userText, command.triggerCommand)) {
                currentCommand = command;
            }
        }
        for (Command command : user.commandsData) {
            if (command.currentState != 0) {
                if (currentCommand != null)
                    command.resetState();
                else
                    currentCommand = command;
            }
        }
        if (currentCommand == null)
            response = "Я не понимаю такой запрос, напишите '/help' чтобы узнать что я могу делать.";
        else {
            response = currentCommand.getAnswer();
            Integer state = currentCommand.currentState;
            currentCommand.addData(userText);
            if (Objects.equals(currentCommand.triggerCommand, "Ежедневник") && state != 0 && currentCommand.answerByState.size() - 1 == state)
            {
                Map<String, String> temp = currentCommand.getData();
                user.addTask(temp.get("date"), temp.get("time"), temp.get("task"));
            }
            else if (Objects.equals(currentCommand.triggerCommand, "/clear"))
            {
                user.clearTable();
            }
            currentCommand.updateState();
        }
        return response;
    }

}
