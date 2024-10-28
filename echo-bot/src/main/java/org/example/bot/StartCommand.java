package org.example.bot;

public class StartCommand extends Command {
    private String startAnswer = "Привет";
    @Override
    public String getAnswer(String text) {
        return startAnswer;
    }


}
