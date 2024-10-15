package org.example.bot;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class TelegramBot extends TelegramLongPollingBot {
    @Override
    public void onUpdateReceived(Update update) {
        String chatId = update.getMessage().getChatId().toString();
        String text = update.getMessage().getText();

        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        switch (text) {
            case ("/start"):
                message.setText("Привет, человек!\nЯ эхо-бот Этернал!");
                break;
            case ("/help"):
                message.setText("Все что я могу на данный момент, это отвечать тебе твоим же текстом.\nА так же имею две уникальные команды '/start', '/help'.");
                break;
            default:
                message.setText(text);
        }
        try {
            this.execute(message);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getBotUsername() {
        return "eternal_bot";
    }

    @Override
    public String getBotToken() {
        return "7938055245:AAFIbaLJpgosuBNSG9NDidwVBydjHo3ufFk";
    }
}
