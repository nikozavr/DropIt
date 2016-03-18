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
import java.util.logging.Level;
import java.util.logging.Logger;


public class GetClients implements Callable<Map<InetAddress, String>> {
    Logger logger = Logger.getLogger("DropItLog");

    public GetClients(){

    }

    private void send() {
        try{
            logger.info("====Send is started====");
            DatagramSocket socket = new DatagramSocket(4001);
            socket.setBroadcast(true);
            byte[] buf = Main.SIGNAL.getBytes();
            InetAddress address = InetAddress.getByName("192.168.255.255");
            DatagramPacket data = new DatagramPacket(buf, buf.length, address, 4000);
            socket.send(data);
            socket.close();
            logger.info("====Send is over====");
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

            logger.info("==Search is started==");
            logger.info("==On " + socket.getInetAddress().getLocalHost().toString() + "==");
            byte[] buf = new byte[256];
            while(!Thread.interrupted()) {
                try {
                    Arrays.fill(buf, (byte) 0);
                    DatagramPacket packet = new DatagramPacket(buf, buf.length);
                    socket.receive(packet);
                    InetAddress send_addr = packet.getAddress();
                    logger.info("?/" + send_addr + "--");
                    logger.info("?/" + packet.getData().toString() + "--");
                    client.put(packet.getAddress(), packet.getData().toString());
                } catch (SocketTimeoutException e){
                    logger.log(Level.SEVERE, "Error: Socket timeout");
                    break;
                }
            }
            socket.close();
            logger.info("==Search is over==");
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
