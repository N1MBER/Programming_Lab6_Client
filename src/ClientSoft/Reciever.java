package ClientSoft;

import Answers.ServerAnswer;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.PortUnreachableException;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AlreadyConnectedException;
import java.nio.channels.DatagramChannel;

public class Reciever extends Thread {
    private DatagramChannel datagramChannel;
    private SocketAddress client;
    private boolean exit = false;
    private SocketAddress serv;
    private ConsoleReader reader;
    private ByteBuffer recievBuffer = ByteBuffer.allocate(16384);

    public Reciever(DatagramChannel channel,ConsoleReader consoleReader,SocketAddress serverAddress,SocketAddress clientAddress){
        this.reader = consoleReader;
        this.datagramChannel = channel;
        this.serv = serverAddress;
        this.client = clientAddress;
    }

    @Override
    public void run() {
        while (!isInterrupted() & !exit) {
            try {
                datagramChannel.connect(serv);
                datagramChannel.receive(recievBuffer);
                recievBuffer.flip();
                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(recievBuffer.array());
                ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
                ServerAnswer serverAnswer = (ServerAnswer) objectInputStream.readObject();
                new AnalyzMessage(reader,serverAnswer);
                //Код оповещающий о подключении
                objectInputStream.close();
                byteArrayInputStream.close();
                recievBuffer.clear();
                if (serverAnswer.getCommand().equals("DISCONNECT")) {
                    interrupt();
                    exit = true;
                }
                datagramChannel.disconnect();
            } catch (IOException | ClassNotFoundException | AlreadyConnectedException e) {
                System.out.println("----\nВозникла ошибка:\n");
                e.printStackTrace();
            }
        }
    }

}