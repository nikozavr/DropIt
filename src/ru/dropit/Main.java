package ru.dropit;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Main extends Application {
    public static final String SIGNAL = "DropIt_is_running";


    @Override
    public void start(Stage primaryStage) throws Exception{
      //  Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        FXMLLoader loader = new FXMLLoader();
        Parent root = (Parent)loader.load(Controller.class.getResourceAsStream("sample.fxml"));
        final Controller controller = (Controller)loader.getController();
        primaryStage.addEventHandler(WindowEvent.WINDOW_SHOWN, new EventHandler<WindowEvent>()
        {
            @Override
            public void handle(WindowEvent window)
            {
                controller.handleWindowShownEvent();
            }
        });
        primaryStage.addEventHandler(WindowEvent.WINDOW_CLOSE_REQUEST, new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                controller.handleWindowCloseEvent();
            }
        });

        Logger logger = Logger.getLogger("DropItLog");
        Logger logger_recieve = Logger.getLogger("DropItRecieve");
        Logger logger_send = Logger.getLogger("DropItSend");

        FileHandler fh;
        FileHandler fh_recieve;
        FileHandler fh_send;

        try {

            // This block configure the logger with handler and formatter
            fh = new FileHandler("C:\\Users\\nikit\\IdeaProjects\\DropIt\\DropItLogFile.log");
            logger.addHandler(fh);
            SimpleFormatter formatter = new SimpleFormatter();
            fh.setFormatter(formatter);

            fh_recieve = new FileHandler("C:\\Users\\nikit\\IdeaProjects\\DropIt\\DropItRecieve.log");
            logger_recieve.addHandler(fh_recieve);
            fh_recieve.setFormatter(formatter);

            fh_send = new FileHandler("C:\\Users\\nikit\\IdeaProjects\\DropIt\\DropItSend.log");
            logger_send.addHandler(fh_send);
            fh_send.setFormatter(formatter);

        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        primaryStage.setTitle("DropIt");
        primaryStage.setScene(new Scene(root, 500, 275));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }

}
