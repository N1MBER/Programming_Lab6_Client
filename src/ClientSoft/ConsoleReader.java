package ClientSoft;

import Answers.*;
import Parser.*;
import PlantsInfo.Plants;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.channels.DatagramChannel;
import java.util.Scanner;

public class ConsoleReader {

    private int PORT_CLIENT = 3300;
    private ClientSendingAndReceiving sendingAndReceiving;
    private boolean workable = false;
    private Scanner scanner;
    private Reciever reciever;
    private boolean serverDisconnect = false;
    private DatagramChannel channel;
    private boolean loaded = false;
    private SocketAddress socketAddress;
    private boolean exit = true;
    private SocketAddress address;
    private int time = 0;
    private boolean portSet = false;
    private boolean connect = false;
    private boolean work = true;

    public ConsoleReader(){
        setWorkable(true);
        scanner = new Scanner(System.in);
        socketAddress = new InetSocketAddress("localhost", PORT_CLIENT);
    }

    public void setServerDisconnect(boolean serverDisconnect) {
        this.serverDisconnect = serverDisconnect;
    }

    public void getPath(){
        String path = scanner.nextLine();
        send(new ClientAnswer(path,"LOAD_PATH"));
    }

    protected void setPort(){
        portSet = false;
        System.out.println("----\nУкажите порт для подключения к серверу\n----");
        while (!portSet) {
            String numb = scanner.nextLine();
            if (numb.matches("[0-9]+")) {
                if (Integer.parseInt(numb) < 65535) {
                    address = new InetSocketAddress("localhost", Integer.parseInt(numb));
                    portSet = true;
                } else {
                    System.out.println("----\nНедопустимый номер порта, введите снова\n----");
                    continue;
                }
            } else {
                System.out.println("----\nНедопустимый номер порта, введите снова\n----");
                continue;
            }
        }
    }


    public void setWorkable(boolean workable){
        this.workable = workable;
    }

    public void setConnect(boolean var){
        this.connect = var;
    }

    public boolean getConnect(){
        return connect;
    }

    public void checkMode(){
        while (work) {
            System.out.println("----\nРабота ведется в онлайн режиме с подключением клиента\n" +
                    "Для начала работы введите \"start\", для завершения введите \"exit\".\n---- ");
            String answer = scanner.nextLine();
            switch (answer.trim()){
                case "exit":
                    setWorkable(false);
                    work = false;
                    System.out.println("----\nЗавершение работы...\n----");

                    break;
                case "start":
                    work = true;

                    try {
                        channel = DatagramChannel.open();
                        setPort();
                        reciever = new Reciever(channel,this,address,socketAddress);
                        reciever.setDaemon(true);
                        reciever.start();
                        sendingAndReceiving = new ClientSendingAndReceiving(channel,this,address,socketAddress);
//                        sendingAndReceiving.start();
                        sendingAndReceiving.sendMessage(new ClientAnswer("CONNECT"));
                        if (!getConnect()) {
                            while ((time < 10000) && !getConnect()) {
                                Thread.sleep(1000);
                                System.out.println("----\nОжидание...\n----");
                                time += 1000;
                            }
                        }
                        Thread.sleep(2000);
                        if (workable && getConnect()) {
                            System.out.println("----\nСоединение установлено\n----");
                            afterConnect();
                        } else {
                            portSet = false;
                            setConnect(false);
                            System.out.println("----\nОтвет от сервера не получен. \nВозможно ответ придёт позже. \nПопробуйте повторить попытку соединенияё\n----");
                        }
                    }catch (IOException | InterruptedException e){
                        System.out.println("----\nВозникла ошибка:");
                        e.printStackTrace();
                    }
                    break;
                default:
                    System.out.println("----\nНераспознаный ответ, введите снова\n----");
                    continue;
            }
        }
    }

    public void printMessage(ServerAnswer serverAnswer){
        System.out.println(serverAnswer.getAnswer());
    }


    private String getFilePath(){
        String path = System.getenv("PATH_CSV");
        if (path == null){
            System.out.println("----\nПуть через переменную окружения PATH_CSV не указан\nНапишите адрес вручную(в консоль)\n----");
            path =  scanner.nextLine();
        }
        while (workable){
            System.out.println("----\nВведенный путь:" + path + "\nЕсли путь верный введите \"да\", если нет то \"нет\" и введите его снова: ");
            switch (scanner.nextLine()){
                case "да":
                    return path;
                case "нет":
                    path = scanner.nextLine();
                    break;
                    default:
                        System.out.println("----\nНеизвестный ответ\n----");
                        break;
            }
        }
        return "";
    }

    public void send(ClientAnswer clientAnswer){
        sendingAndReceiving.sendMessage(clientAnswer);
    }

    private void shootDownHook(){
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (!exit) {
                send(new ClientAnswer("DISCONNECT"));
                System.out.println("----\nЗавершение работы\n----");
            }

        }));
    }

    public void work(){
        setWorkable(true);

        System.out.println("----\nНачало работы\n----");
        checkMode();
    }

    public void afterConnect(){
        shootDownHook();
        while (work){
            if (workable & connect) {
                scanAndExit();
            }
            else{
                checkMode();
            }
        }
        if (serverDisconnect) {
            System.out.println("----\nСервер отключился, для продолжения работы перезапустите клиент.\n" +
                    "Для завершения введите \"exit\".\n----");
            while (exit){
                if (scanner.nextLine().equals("exit")){
                    exit =false;
                }
                else {
                    System.out.println("----\nНераспознаный ответ\n----");
                }
            }
        }
    }

    public void setLoaded(boolean loaded) {
        this.loaded = loaded;
    }

    public void scanAndExit(){
        try {
            String command[] = new String[50];
            if (!loaded) {
                command[0] = scanner.nextLine();
                String helpcom[] = command[0].trim().split(" ", 2);
                switch (helpcom[0].trim()) {
                    case "load":
                        loaded = true;
                        if (helpcom.length > 1) {
                            if (helpcom[1].matches(" +") | helpcom[1].matches("")) {
                                System.out.println("----\nДанная команда не должна содержать аргументов.\n----");
                            }
                        } else {
                            send(new ClientAnswer("LOAD"));
                        }
                        break;
                    case "save":
                        if (helpcom.length > 1) {
                            if (helpcom[1].matches(" +") | helpcom[1].matches("")) {
                                System.out.println("----\nДанная команда не должна содержать аргументов.\n----");
                            }
                        } else {
                           send(new ClientAnswer("SAVE"));
                        }
                        break;
                    case "info":
                        if (helpcom.length > 1) {
                            if (helpcom[1].matches(" +") | helpcom[1].matches("")) {
                                System.out.println("----\nДанная команда не должна содержать аргументов.\n----");
                            }
                        } else {
                            send(new ClientAnswer("INFO"));
                        }
                        break;
                    case "show":
                        if (workable) {
                            if (helpcom.length > 1) {
                                System.out.println("----\nВ данной команде не должно быть аргументов\n----");
                            } else
                                send(new ClientAnswer("SHOW"));
                        }
                        break;
                    case "add":
                        if (!helpcom.equals(null)) {
                            try {
                                if (getBracket(helpcom[1], '}') == 2) {
                                    send(new ClientAnswer(getElements(helpcom[1]), "ADD"));
                                } else {
                                    Plants plants = getElements(scanElements(helpcom[1]));
                                    send(new ClientAnswer(plants, "ADD"));
                                }
                                System.out.println("\n----");
                            } catch (JSONException e) {
                                System.out.println("----\nОбнаружена ошибка при парсинге элемента: " + e.getMessage() + "\n----");
                            }
                        } else
                            System.out.println("----\nОшибка ввода элемента.\n----");
                        break;
                    case "add_if_max":
                        if (!helpcom[1].equals(null)) {
                            try {
                                if (getBracket(helpcom[1], '}') == 2) {
                                    send(new ClientAnswer(getElements(helpcom[1]), "ADD_IF_MAX"));
                                } else {
                                    Plants plants = getElements(scanElements(helpcom[1]));
                                    send(new ClientAnswer(plants, "ADD_IF_MAX"));
                                }
                                System.out.println("\n----");
                            } catch (JSONException e) {
                                System.out.println("----\nОбнаружена ошибка при парсинге элемента: " + e.getMessage() + "\n----");
                            }
                        } else
                            System.out.println("----\nОшибка ввода элемента.\n----");
                        break;
                    case "remove":
                        try {
                            if (getBracket(helpcom[1], '}') == 2) {
                                send(new ClientAnswer(getElements(helpcom[1]), "REMOVE"));
                            } else {
                                Plants plants = getElements(scanElements(helpcom[1]));
                                send(new ClientAnswer(plants, "REMOVE"));
                            }
                            System.out.println("\n----");
                        } catch (JSONException e) {
                            System.out.println("----\nОбнаружена ошибка при парсинге элемента: " + e.getMessage() + "\n----");
                        }
                        break;
                    case "remove_greater":
                        try {
                            if (getBracket(helpcom[1], '}') == 2) {
                                send(new ClientAnswer(getElements(helpcom[1]), "REMOVE_GREATER"));
                            } else {
                                Plants plants = getElements(scanElements(helpcom[1]));
                                send(new ClientAnswer(plants, "REMOVE_GREATER"));
                            }
                            System.out.println("\n----");
                        } catch (JSONException e) {
                            System.out.println("----\nОбнаружена ошибка при парсинге элемента: " + e.getMessage() + "\n----");
                        }
                        break;
                    case "remove_lower":
                        try {
                            if (getBracket(helpcom[1], '}') == 2) {
                                send(new ClientAnswer(getElements(helpcom[1]), "REMOVE_LOWER"));
                            } else {
                                Plants plants = getElements(scanElements(helpcom[1]));
                                send(new ClientAnswer(plants, "REMOVE_LOWER"));
                            }
                            System.out.println("\n----");
                        } catch (JSONException e) {
                            System.out.println("----\nОбнаружена ошибка при парсинге элемента: " + e.getMessage() + "\n----");
                        }
                        break;
                    case "exit":
                        if (workable)
//                            this.sendingAndReceiving.sendMessage(new ClientAnswer("EXIT"));
                        work = false;
                        workable = false;
                        break;
                    case "import":
                        if (helpcom.length > 1) {
                            if (helpcom[1].matches(" +") | helpcom[1].matches("")) {
                                System.out.println("----\nДанная команда не должна содержать аргументов.\n----");
                            }
                        } else {
                            if (workable) {
                                Loader loader = new Loader();
                                if (loader.getOK()) {
                                    send(new ClientAnswer(loader.getLinkHSPlants(), "IMPORT"));
                                    break;
                                }
                            }
                        }
                        break;
                    case "help":
                        if (workable)
                            send(new ClientAnswer("HELP"));
                        break;
                    default:
                        if (workable)
                            System.out.println("----\nНеизвестная команда.\n----");
                        break;
                }
            }
        }catch (ArrayIndexOutOfBoundsException e){
            System.out.println("----\nОшибка ввода элемента.\n----");
        }
    }

    private String scanElements(String helpcom){
        int rightbrecket = 0;
        int leftbrecket = 0;
        String command[] = new String[15];
        int k = 0;
        String plz = helpcom;
        while(leftbrecket != rightbrecket ) {
            command[k] = scanner.nextLine();
            command[k].trim();
            rightbrecket += getBracket(command[k],'}');
            leftbrecket += getBracket(command[k],'{');
            plz += command[k];
            k++;
            if (k == 11){
                System.out.println("----\nОшибка ввода элемента.\n----");
                break;
            }
        }
        return plz;
    }


    private Plants getElements(String txt){
        int countright = getBracket(txt,'}');
        int countleft = getBracket(txt, '{');
        while (!(countleft == countright)){
            String str = scanner.nextLine();
            countright += getBracket(str , '}');
            countleft += getBracket(str, '{');
            txt += str;
        }
        txt = txt.trim();
        JSONParser jsonParser = new JSONParser();
        return jsonParser.objParse(txt);
    }


    private int getBracket(String str,char bracket){
        int count = 0;
        for(char c : str.toCharArray()){
            if (c == bracket)
                count++;
        }
        return count;
    }
}
