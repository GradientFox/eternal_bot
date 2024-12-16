package org.example;

import org.example.bot.TelegramBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;

public class Main {
    public static void main(String[] args) throws TelegramApiException {
//        LocalDate date = LocalDate.of(2024, 10, 9);
//        Integer month = date.getMonth().getValue();
//        Integer year = date.getYear();
//        System.out.println(date.withDayOfMonth(1).getDayOfWeek());
        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
        botsApi.registerBot(new TelegramBot());
    }
}