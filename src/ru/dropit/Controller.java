package ru.dropit;


import javafx.event.ActionEvent;
import javafx.scene.control.ListView;

public class Controller {
    public ListView lstListView;

    public void searchConnections(ActionEvent actionEvent){
        Connection.connect();
    }
}
