package ru.dropit;


import javafx.event.ActionEvent;
import javafx.scene.control.ListView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.net.InetAddress;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Controller {
    public ListView lst_devices;

    ObservableList<String> data = FXCollections.observableArrayList();

    public void searchConnections(ActionEvent actionEvent){
        Map<InetAddress, String> list = Connection.listClients();
        data.addAll(list.values());
        lst_devices.setItems(data);
    }

    public void handleWindowShownEvent(){
        Executor executor = Executors.newFixedThreadPool(1);
        executor.execute(new ListenOthers());
    }
}
