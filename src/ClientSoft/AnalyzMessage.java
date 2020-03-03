package ClientSoft;

import Answers.ServerAnswer;

public class AnalyzMessage {

    private ConsoleReader reader;
    private ServerAnswer answer;

    public AnalyzMessage(ConsoleReader consoleReader, ServerAnswer serverAnswer){
        this.answer = serverAnswer;
        this.reader = consoleReader;
        analyz(answer);

    }

    private void analyz(ServerAnswer serverAnswer){
        switch (serverAnswer.getCommand()){
            case "CONNECT":
                reader.setConnect(true);
                break;
//            case "DISCONNECT":
//                reader.setConnect(false);
//                reader.setServerDisconnect(true);
//                reader.setWork(false);
//                break;
            case "LOAD_ERROR":
                reader.printMessage(serverAnswer);
                reader.getPath();
                break;
            case "LOAD_TRUE":
                reader.printMessage(serverAnswer);
                reader.setLoaded(false);
                break;
            case "EXIT":
            case "LOAD":
            case "HELP":
            case "INFO":
            case "ADD":
            case "ADD_IF_MAX":
            case "REMOVE":
            case "SHOW":
            case "SAVE":
            case "IMPORT":
            case "REMOVE_LOWER":
            case "REMOVE_GREATER":
                reader.printMessage(serverAnswer);
                break;
                default:
//                    System.out.println("----\nНеизвестная команда от сервера:\n" + serverAnswer.getCommand() + "\n----");
        }
    }
}
