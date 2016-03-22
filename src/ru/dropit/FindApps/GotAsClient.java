package ru.dropit.FindApps;

import ru.dropit.LocalIpAddress.IpAddress;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.logging.Logger;

/**
 * Created by nikit on 15.03.2016.
 */
public class GotAsClient implements Runnable {
    InetAddress server;
    Logger logger = Logger.getLogger("DropItLog");
    Logger logger_send = Logger.getLogger("DropItSend");

    GotAsClient(InetAddress address){
        server = address;
    }

    @Override
    public void run() {
        try{
            logger.info("======Send is started====");
            DatagramSocket socket = new DatagramSocket(4001);
            socket.setBroadcast(true);
            IpAddress ipAddress = new IpAddress();
            byte[] buf = ipAddress.getIpAddress().toString().getBytes();
            DatagramPacket data = new DatagramPacket(buf, buf.length, server, 4000);
            socket.send(data);
            logger_send.info(ipAddress.getIpAddress().toString());
            socket.close();
            logger.info("======Send is over====");
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
