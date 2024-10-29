package org.example.bot;

import org.example.commands.Command;
import org.example.user.User;


import java.io.Console;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HandlerBot {
    private List<User> usersData;

    public HandlerBot(){
        usersData = new ArrayList<User>();
    }

    public void addUser(String chatId){
        usersData.add(new User(chatId));
    }

    public User getUserByChatId(String chatId){
        if (!usersData.isEmpty()){
            for (User user: usersData) {
                if (user.getUserChatId() == chatId)
                    return user;
            }
        }
        addUser(chatId);
        return usersData.get(usersData.size()-1);
    }
    public String getResponse(String userText, String chatId){
        User user = getUserByChatId(chatId);
        for (Command command : user.commandsData){
            if (Objects.equals(userText, command.triggerCommand) || command.currentState != 0){
                return command.getAnswer();
            }
        }
        return "Я не понимаю такой запрос, напишите '/help' чтобы узнать что я могу делать.";
    }
}
