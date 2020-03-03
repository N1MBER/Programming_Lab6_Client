package ClientSoft;


import Answers.ClientAnswer;
import Answers.ServerAnswer;
import PlantsInfo.Plants;


import java.awt.*;
import java.io.*;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AlreadyConnectedException;
import java.nio.channels.DatagramChannel;

public class ClientSendingAndReceiving<T> {

    private DatagramChannel datagramChannel;
    private SocketAddress client;
    private SocketAddress serv;
    private ConsoleReader reader;
    private ByteBuffer sendBuffer = ByteBuffer.allocate(16384);

    public ClientSendingAndReceiving(DatagramChannel channel,ConsoleReader consoleReader,SocketAddress serverAddress,SocketAddress clientAddress){
        this.reader = consoleReader;
        this.datagramChannel = channel;
        this.serv = serverAddress;
        this.client = clientAddress;
    }

    public void sendMessage(ClientAnswer clientAnswer){
        try {

                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
                objectOutputStream.writeObject(clientAnswer);
                sendBuffer.put(byteArrayOutputStream.toByteArray());
                objectOutputStream.flush();
                byteArrayOutputStream.flush();
                sendBuffer.flip();
                datagramChannel.send(sendBuffer, serv);
                System.out.println("----\nСообщение отправлено.\n----");
                objectOutputStream.close();
                byteArrayOutputStream.close();
                sendBuffer.clear();
        }catch (IOException e){
            System.out.println("----\nВозникла ошибка:\n");
            e.printStackTrace();
        }
    }
}
