package org.example.commands;

import java.util.Map;

public abstract class Command {
    public Map<Integer, String> answerByState;
    public Integer currentState;
    public String triggerCommand;
    public abstract String getAnswer();
    public void updateState() {
        if (answerByState.size() == 1) {
            return;
        }
        if (answerByState.size() - 1 == currentState) {
            currentState = 0;
            return;
        }
        currentState++;
        return;
    }
}
