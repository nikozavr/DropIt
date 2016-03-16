package ru.dropit;


import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import ru.dropit.FindApps.ListenOthers;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Controller {
    public ListView lst_devices;
    public Label lbl_Hostname;
    public Label lbl_Local_ip;
    ExecutorService executor;

    ObservableList<String> data = FXCollections.observableArrayList();

    public void searchConnections(ActionEvent actionEvent){
        Map<InetAddress, String> list = FindOtherApps.listClients();
        data.addAll(list.values());
        lst_devices.setItems(data);
    }

    public void handleWindowShownEvent(){
        executor = Executors.newFixedThreadPool(1);
        executor.execute(new ListenOthers());
        try{
            lbl_Hostname.setText(lbl_Hostname.getText() + InetAddress.getLocalHost().getHostName());
        } catch (UnknownHostException e){
            e.printStackTrace();
        }
        lbl_Local_ip.setText(lbl_Local_ip.getText() + IpAddress.getIpAddress().toString());
    }

    public void handleWindowCloseEvent(){
        System.out.println("Program closed");
        Thread.currentThread().interrupt();
    }
}
