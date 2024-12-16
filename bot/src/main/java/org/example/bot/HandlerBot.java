package org.example.bot;

import org.example.commands.Command;
import org.example.db.DataBase;
import org.example.user.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;


import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class HandlerBot {
    private final List<User> usersData;
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
    private Command getCurrentCommand(String userText, String chatId)
    {
        User user = getUserByChatId(chatId);
        Command currentCommand = null;
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
        return currentCommand;
    }

    public String getResponse(String userText, String chatId) {
        User user = getUserByChatId(chatId);
        String response = null;
        Command currentCommand = getCurrentCommand(userText, chatId);
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
//            currentCommand.updateState();
        }
        return response;
    }

    public InlineKeyboardMarkup getInlineMarkup(String userText, String chatId){
        Command currentCommand = getCurrentCommand(userText, chatId);
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> temp = new ArrayList<>();
        markupInline.setKeyboard(temp);
        if (currentCommand == null)
        {
            return markupInline;
        }
        if (currentCommand.getMarkup() == Command.Markup.Calendar) {
            List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
            LocalDate now = LocalDate.now();
            int dayNum = 1;
            while (dayNum <= now.getMonth().maxLength())
            {
                List<InlineKeyboardButton> rowInline = new ArrayList<>();
                for (int i = 1; i < 8; i++) {
                    InlineKeyboardButton button = new InlineKeyboardButton();
                    button.setText("-");
                    button.setCallbackData("-");
                    if(dayNum<=now.getMonth().maxLength()){
                        LocalDate day = now.withDayOfMonth(dayNum);
                        if (day.getDayOfWeek().getValue() == i){
                            button.setText(String.valueOf(dayNum));
                            button.setCallbackData(day.toString());
                            dayNum++;
                        }
                    }
                    rowInline.add(button);
                }
                rowsInline.add(rowInline);
            }
            markupInline.setKeyboard(rowsInline);
        }
        currentCommand.updateState();
        return markupInline;
    }

    public ReplyKeyboardMarkup getReplyMarkup(String userText, String chatId) {

        ReplyKeyboardMarkup markupReply = new ReplyKeyboardMarkup();
        return markupReply;
    }
}
