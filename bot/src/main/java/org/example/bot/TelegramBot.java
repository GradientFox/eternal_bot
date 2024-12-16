package org.example.bot;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;


public class TelegramBot extends TelegramLongPollingBot {
    HandlerBot handler = new HandlerBot();

    @Override
    public String getBotUsername() {
        return "eternal_work_bot";
    }

    @Override
    public String getBotToken() {
        return System.getenv("TG_TOKEN");
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {

            String chatId = update.getMessage().getChatId().toString();
            String userText = update.getMessage().getText();
            System.out.println(String.format("Message - %s", chatId));
            this.sendMessage(chatId,
                        handler.getResponse(userText, chatId),
                        handler.getInlineMarkup(userText, chatId));

            return;
        }
        if (update.hasCallbackQuery()) {
            System.out.println(update.getCallbackQuery().getData());
            String userText = update.getCallbackQuery().getData();
            String chatId = update.getCallbackQuery().getMessage().getChatId().toString();
            this.sendMessage(chatId,
                    handler.getResponse(userText, chatId),
                    handler.getInlineMarkup(userText, chatId));
            return;
        }
    }

    public void sendMessage(String chatId, String msg, InlineKeyboardMarkup markupInline) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        System.out.println(1);
        message.setText(msg);
        if (!markupInline.getKeyboard().isEmpty()) {
            message.setReplyMarkup(markupInline);
        }
//        if (!markupReply.getKeyboard().isEmpty()) {
//            message.setReplyMarkup(markupReply);
//        }
        System.out.println(1);
        try {
            this.execute(message);
        } catch (TelegramApiException e) {
            System.out.println(2);
            throw new RuntimeException(e);
        }
    }

}
