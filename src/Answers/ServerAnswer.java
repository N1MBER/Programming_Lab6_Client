package Answers;

import java.io.Serializable;

public class ServerAnswer implements Serializable {

    private String answer;
    private final long serialVersionUID = 1L;
    private String command;

    public ServerAnswer(String answers, String command){
        this.answer = answers;
        this.command = command;
    }

    public String getAnswer(){
        return answer;
    }

    public String getCommand() {
        return command;
    }
}
