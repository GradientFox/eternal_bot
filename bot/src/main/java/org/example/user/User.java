package org.example.user;

import org.example.commands.Command;
import org.example.commands.HelpCommand;
import org.example.commands.StartCommand;

import java.util.List;

public class User {
    private final String userChatId;
    public List<Command> commandsData;

    public User(String chatId){
        this.userChatId = chatId;
        this.commandsData = List.of(
                new StartCommand(),
                new HelpCommand()
        );
    }

    public String getUserChatId(){
        return userChatId;
    }


}
