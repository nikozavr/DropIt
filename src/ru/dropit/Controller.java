package ru.dropit;


import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ProgressIndicator;
import ru.dropit.FindApps.GetClients;
import ru.dropit.FindApps.ListenOthers;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.*;
import java.util.logging.Logger;

public class Controller {
    public ListView lst_devices;
    public Label lbl_Hostname;
    public Label lbl_Local_ip;
    public Label lblNoneDevices;
    public ProgressIndicator pgiSearch;
    ExecutorService executor;
    ListenOthers listenOthers;
    Thread thListenOthers;
    Logger logger = Logger.getLogger("DropItLog");

    ObservableList<String> data = FXCollections.observableArrayList();

    public void searchConnections(ActionEvent actionEvent){
        lblNoneDevices.setVisible(false);
        pgiSearch.setStyle(" -fx-progress-color: red;");
        // changing size without css
        pgiSearch.setMinWidth(50);
        pgiSearch.setMinHeight(50);
     //   final FutureTask<Map<InetAddress, String>> query = new FutureTask(getClients);
        Task<ObservableList<String>> task = new GetClients();
        pgiSearch.setVisible(true);
        pgiSearch.setProgress(-1);
        lst_devices.itemsProperty().bind(task.valueProperty());
        task.stateProperty().addListener(new ChangeListener<Worker.State>() {
            @Override
            public void changed(ObservableValue<? extends Worker.State> observableValue, Worker.State oldState, Worker.State newState) {
                if (newState == Worker.State.SUCCEEDED) {
                    pgiSearch.setVisible(false);
                    if(task.valueProperty().get().isEmpty()){
                        lblNoneDevices.setVisible(true);
                    }
                }
            }
        });

        new Thread(task).start();
   //     task.stateProperty().
        //Future<Map<InetAddress, String>> futureList = executor.submit(getClients);
    /*    try {
            Map<InetAddress, String> list = query.get();
            if(!list.isEmpty()){
                data.addAll(list.values());
                lst_devices.setItems(data);
            } else{
                lblNoneDevices.setVisible(true);
            }
        } catch (Exception e){
            e.printStackTrace();
        }*/

    }

    public void handleWindowShownEvent(){
        executor = Executors.newFixedThreadPool(5);
        thListenOthers = new Thread(new ListenOthers());
        thListenOthers.setDaemon(true);
        executor.execute(thListenOthers);
        try{
            lbl_Hostname.setText(lbl_Hostname.getText() + InetAddress.getLocalHost().getHostName());
        } catch (UnknownHostException e){
            e.printStackTrace();
        }
        lbl_Local_ip.setText(lbl_Local_ip.getText() + IpAddress.getIpAddress().toString());
    }

    public void handleWindowCloseEvent(){
        logger.info("Program closed");
        executor.shutdownNow();
    }
}
