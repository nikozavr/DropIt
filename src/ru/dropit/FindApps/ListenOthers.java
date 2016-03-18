package ru.dropit.FindApps;

import ru.dropit.FindApps.GotAsClient;
import ru.dropit.Main;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeoutException;
import java.util.logging.Logger;

/**
 * Created by nikit on 15.03.2016.
 */
public class ListenOthers implements Runnable {
    Logger logger = Logger.getLogger("DropItLog");

    @Override
    public void run(){
        try{
            DatagramSocket socket = new DatagramSocket(4000, InetAddress.getByName("0.0.0.0"));
            socket.setBroadcast(true);
            logger.info("--Listen is started--");
            logger.info("--On " + socket.getInetAddress().getLocalHost().toString() + "--");
            byte[] buf = new byte[256];
            socket.setSoTimeout(5000);
            while(!Thread.interrupted()) {
                try {
                    Arrays.fill(buf, (byte) 0);
                    DatagramPacket packet = new DatagramPacket(buf, buf.length);
                    socket.receive(packet);
                    InetAddress send_addr = packet.getAddress();
                    System.out.println("--" + send_addr + "--");
                    System.out.println("--" + new String(packet.getData()) + "--");
                    if(new String(packet.getData()) == Main.SIGNAL) {
                        System.out.println("Test");
                        ExecutorService executor = Executors.newFixedThreadPool(1);
                        executor.submit(new GotAsClient(packet.getAddress()));
                    }
                } catch (SocketTimeoutException e){
                    System.out.println("--Timout--");
                    continue;
                } catch (Exception e){
                    System.out.println("--Error: Socket errro--");
                    break;
                }
            }
            socket.close();
            System.out.println("--Listen is over--");
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
