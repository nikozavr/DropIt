package ru.dropit;

import sun.rmi.runtime.Log;

import java.net.*;
import java.util.*;
import java.util.concurrent.*;


public class Connection {
    public static Map<InetAddress, String> listClients(){
        ExecutorService executor = Executors.newFixedThreadPool(1);
        GetClients getClients = new GetClients();
        Future<Map<InetAddress,String>> clients = executor.submit(getClients);
        Map<InetAddress, String> list = new HashMap<InetAddress, String>();
        try {
            list = clients.get();
        } catch (Exception e){
            e.printStackTrace();
        }
        clients.cancel(true);
        //this method will stop the running underlying task
        try {
            for (InetAddress key : list.keySet()) {
                System.out.println(key);
            }
        } catch (Exception e){
            System.out.println("Error");
        }
        return list;
    }

}
