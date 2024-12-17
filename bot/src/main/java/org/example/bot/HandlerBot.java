package org.example.bot;

import org.example.commands.Command;
import org.example.db.DataBase;
import org.example.user.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;


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
        } catch (SQLException e) {
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

    public Command getCurrentCommand(String userText, String chatId) {
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
            if (Objects.equals(currentCommand.triggerCommand, "Ежедневник") && state != 0 && currentCommand.answerByState.size() - 1 == state) {
                Map<String, String> temp = currentCommand.getData();
                user.addTask(temp.get("date"), temp.get("time"), temp.get("task"));
            } else if (Objects.equals(currentCommand.triggerCommand, "/clear")) {
                user.clearTable();
            }
//            currentCommand.updateState();
        }
        return response;
    }

    public InlineKeyboardMarkup getInlineMarkup(String userText, String chatId, int month) {
        Command currentCommand = getCurrentCommand(userText, chatId);
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> temp = new ArrayList<>();
        markupInline.setKeyboard(temp);
        if (currentCommand == null) {
            return markupInline;
        }
        if (currentCommand.getMarkup() == Command.Markup.Calendar) {
            markupInline = getCalendar(month);
        }
        return markupInline;
    }

    public InlineKeyboardMarkup getCalendar(int month) {
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> dateLine = new ArrayList<>();
        InlineKeyboardButton date = new InlineKeyboardButton();
        int shiftYear = (month - 1) / 12;

        int yyear = LocalDate.now().getYear() + shiftYear;
        int mmonth = (LocalDate.now().getMonth().getValue() + month - 1) % 12 + 1;
        int dday = 1;
        LocalDate firstDay = LocalDate.of(yyear, mmonth, dday);
        date.setText(String.format("%d - %s", firstDay.getYear(), firstDay.getMonth().toString()));
        date.setCallbackData("-");
        dateLine.add(date);
        rowsInline.add(dateLine);
        int dayNum = 1;
        while (dayNum <= firstDay.getMonth().maxLength()) {
            List<InlineKeyboardButton> rowInline = new ArrayList<>();
            for (int i = 1; i < 8; i++) {
                InlineKeyboardButton button = new InlineKeyboardButton();
                button.setText("-");
                button.setCallbackData("-");
                if (dayNum <= firstDay.getMonth().maxLength()) {
                    LocalDate day = firstDay.withDayOfMonth(dayNum);
                    if (day.getDayOfWeek().getValue() == i) {
                        button.setText(String.valueOf(dayNum));
                        button.setCallbackData(day.toString());
                        dayNum++;
                    }
                }
                rowInline.add(button);
            }
            rowsInline.add(rowInline);
        }
        List<InlineKeyboardButton> navig = new ArrayList<>();
        InlineKeyboardButton nextYear = new InlineKeyboardButton();
        nextYear.setText(">>>");
        nextYear.setCallbackData("NEXT_YEAR");
        InlineKeyboardButton prevYear = new InlineKeyboardButton();
        prevYear.setText("<<<");
        prevYear.setCallbackData("PREV_YEAR");
        InlineKeyboardButton nextMonth = new InlineKeyboardButton();
        nextMonth.setText(">");
        nextMonth.setCallbackData("NEXT_MONTH");
        InlineKeyboardButton prevMonth = new InlineKeyboardButton();
        prevMonth.setText("<");
        prevMonth.setCallbackData("PREV_MONTH");
        navig.add(prevYear);
        navig.add(prevMonth);
        navig.add(nextMonth);
        navig.add(nextYear);
        rowsInline.add(navig);
        markupInline.setKeyboard(rowsInline);
        return markupInline;
    }

    public ReplyKeyboardMarkup getReplyMarkup(String userText, String chatId) {
        Command currentCommand = getCurrentCommand(userText, chatId);
        ReplyKeyboardMarkup markupReply = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboard = new ArrayList<>();
        if (currentCommand == null){
            markupReply.setKeyboard(keyboard);
            return markupReply;
        }
        if (currentCommand.getMarkup() == Command.Markup.Menu){
            KeyboardRow task = new KeyboardRow();
            task.add("Ежедневник");
            task.add("Дела");
            KeyboardRow weather = new KeyboardRow();
            weather.add("Погода");
            keyboard.add(task);
            keyboard.add(weather);
            markupReply.setKeyboard(keyboard);
        }
        currentCommand.updateState();
        return markupReply;
    }
}
