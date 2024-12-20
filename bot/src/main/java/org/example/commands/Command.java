package org.example.commands;

import java.util.List;
import java.util.Map;

public abstract class Command {
    public enum Markup {
        None,
        Calendar,
        Menu
    }

    public Map<Integer, Map<Integer, Object>> answerByState;
    public Integer currentState;
    public String triggerCommand;

    public String getAnswer() {
        return answerByState.get(currentState).get(0).toString();
    }

    public Markup getMarkup() {
        return (Markup) answerByState.get(currentState).get(1);
    }

    public void updateState() {
        if (answerByState.size() == 1) {
            return;
        }
        if (answerByState.size() - 1 == currentState) {
            resetState();
            return;
        }
        currentState += 1;
    }

    public void resetState() {
        currentState = 0;
    }

    public abstract Map<String, String> getData();

    public abstract void addData(String userText);
}
