package ru.dropit.FindApps;

import com.sun.org.apache.bcel.internal.generic.SIPUSH;
import ru.dropit.Main;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;


public class GetClients implements Callable<Map<InetAddress, String>> {
    public GetClients(){

    }

    private void send() {
        try{
            System.out.println("====Send is started====");
            DatagramSocket socket = new DatagramSocket(4001);
            socket.setBroadcast(true);
            byte[] buf = Main.SIGNAL.getBytes();
            System.out.println(Main.SIGNAL);
            System.out.println(Main.SIGNAL.getBytes());
            InetAddress address = InetAddress.getByName("192.168.255.255");
            DatagramPacket data = new DatagramPacket(buf, buf.length, address, 4000);
            socket.send(data);
            socket.close();
            System.out.println("====Send is over====");
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private Map<InetAddress, String> listen(){
        Map<InetAddress, String> client = new HashMap<InetAddress, String>();

        try{
            DatagramSocket socket = new DatagramSocket(4002,InetAddress.getByName("0.0.0.0"));
            socket.setBroadcast(true);
            socket.setSoTimeout(5000);

            System.out.println("==Search is started==");
            System.out.println("==On " + socket.getInetAddress().getLocalHost().toString() + "==");
            byte[] buf = new byte[256];
            while(!Thread.interrupted()) {
                try {
                    Arrays.fill(buf, (byte) 0);
                    DatagramPacket packet = new DatagramPacket(buf, buf.length);
                    socket.receive(packet);
                    InetAddress send_addr = packet.getAddress();
                    System.out.println("?/" + send_addr + "--");
                    System.out.println("?/" + packet.getData().toString() + "--");
                    client.put(packet.getAddress(), packet.getData().toString());
                } catch (SocketTimeoutException e){
                    System.out.println("Error: Socket timeout");
                    break;
                }
            }
            socket.close();
            System.out.println("==Search is over==");
        } catch (Exception e){
            e.printStackTrace();
        }
        return client;
    }

    @Override
    public Map<InetAddress, String> call() {
        send();
        return listen();
    }
}
