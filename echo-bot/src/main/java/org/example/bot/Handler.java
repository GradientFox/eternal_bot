package org.example.bot;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class Handler {
    private Map<String, Map<String, String>> commands;
    Map<String, String> data;
    boolean waitingReply;
    String last_reply;
    String last_recieve;

    public Handler() {
        commands = new HashMap<>();
        data = new HashMap<>();
        last_recieve = "";
        last_reply = "";
        waitingReply = false;
        commands.put("/start", new HashMap<>());
        commands.get("/start").put("text", "Привет, человек!\nЯ бот Этернал, я буду помогать тебе отслеживать твои повседневные задачи!\nЧтобы создать твою первую задачу напиши: '/new_task'.");
        commands.get("/start").put("waiting-reply", "false");
        commands.put("/help", new HashMap<>());
        commands.get("/help").put("text", "Все что я могу на данный момент, это отвечать тебе твоим же текстом.\nА так же имею две уникальные команды '/start', '/new_task, '/help'.");
        commands.get("/help").put("waiting-reply", "false");
        commands.put("/new_task", new HashMap<>());
        commands.get("/new_task").put("text", "Напишите дату вашего события:");
        commands.get("/new_task").put("waiting-reply", "true");
        commands.put("/get_tasks", new HashMap<>());
        commands.get("/get_tasks").put("text", "Вот список ваших задач");
        commands.get("/get_tasks").put("waiting-reply", "false");
    }


    public String getAnswer(String text) {
        String answer = "";

        if (waitingReply)
        {
            switch (last_reply){
                case ("Напишите дату вашего события:"):
                    answer = "Напишите вашу задачу:";
                    break;
                case ("Напишите вашу задачу:"):
                    answer = "Задача доабвлена";
                    data.put(last_recieve, text);
                    waitingReply = false;
                    break;
            }
        }
        else if (commands.containsKey(text))
        {
            answer = commands.get(text).get("text");
            if (commands.get(text).get("waiting-reply") == "true")
                waitingReply = true;
        }
        else
            answer = "Я не знаю такую команду, попробуй ввести: '/help'.";
        last_reply = answer;
        last_recieve = text;
        System.out.println(data.values());
        return answer;
    }
}
