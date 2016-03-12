package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;

class CallClients extends Thread {
    @Override
    public void run(){
        System.out.println("Thread is started");

        try{
            DatagramSocket socket = new DatagramSocket(4001);
            socket.setBroadcast(true);
            byte[] buf = new byte[256];
            String hello = "hello";
            buf = hello.getBytes();
            InetAddress address = InetAddress.getByName("255.255.255.255");
            DatagramPacket data = new DatagramPacket(buf, buf.length, address, 4000);
            System.out.println(buf);
            socket.send(data);
            socket.close();
        } catch (Exception e){
            e.printStackTrace();
        }
        System.out.println("Thread is closed");
    }
}

class HearClients extends Thread {
    @Override
    public void run() {
        System.out.println("Hear is started");

        try {
            DatagramSocket socket = new DatagramSocket(4000,InetAddress.getByName("0.0.0.0"));
            socket.setBroadcast(true);
            byte[] buf = new byte[256];
            DatagramPacket packet = new DatagramPacket(buf, buf.length);
            socket.receive(packet);
            System.out.println(new String(packet.getData()));
            socket.receive(packet);
            System.out.println(new String(packet.getData()));
            socket.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}

public class Main {

 /*   @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }*/

    static CallClients callAll;
    static HearClients hearAll;

     public static void main(String[] args) {
        System.out.println("Program starts");

         hearAll = new HearClients();
         hearAll.start();

         callAll = new CallClients();
         callAll.start();



         System.out.println("Program ends");
     }

     public static String getSubnet(String currentIP) {
         int firstSeparator = currentIP.lastIndexOf("/");
         int lastSeparator = currentIP.lastIndexOf(".");
         return currentIP.substring(firstSeparator+1, lastSeparator+1);
     }

}
