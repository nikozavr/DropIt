package ru.dropit;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * Created by nikit on 15.03.2016.
 */
public class GotAsClient implements Runnable {
    InetAddress server;

    GotAsClient(InetAddress address){
        server = address;
    }

    @Override
    public void run() {
        try{
            System.out.println("======Send is started====");
            DatagramSocket socket = new DatagramSocket(4001);
            socket.setBroadcast(true);
            byte[] buf = IpAddress.getIpAddress().toString().getBytes();
            DatagramPacket data = new DatagramPacket(buf, buf.length, server, 4000);
            socket.send(data);
            socket.close();
            System.out.println("======Send is over====");
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
