/*
 * @File UIController.java
 * @Authors : David González León
 * @Date 24 mars 2021
 */
package ch.heigvd.robotpi.userinterface;

import ch.heigvd.robotpi.communication.Client;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class UIController {
   private Scene scene;
   private Client client;
   private ConnectedWorker worker;

   @FXML private RadioButton RBConnectedStatus;
   @FXML private TextField TFConnectionAddress;

   public void setScene(Scene scene) {
      this.scene = scene;

   }

   public void load(Stage primaryStage) {
      client = new Client();
      RBConnectedStatus.fire();
      primaryStage.setScene(scene);
      primaryStage.showingProperty().addListener(((observableValue, oldValue, showing) -> {
         if (showing) {
            primaryStage.setMinHeight(primaryStage.getHeight());
            primaryStage.setMinWidth(primaryStage.getWidth());
         }
      }));
      primaryStage.setTitle("Robot PI HEIG");
   }

   @FXML
   private void connectButtonPressed(ActionEvent event) {
      if (TFConnectionAddress.getText().isEmpty()) {
         Util.createAlertFrame(Alert.AlertType.WARNING, "No ip adress", "No ip adress",
                               "Please write the ip adress of the targeted robot before pressing connect.");
      }
      String ipAdress = TFConnectionAddress.getText();
      if (ipAdress.matches("(?<!\\d|\\d\\.)(?:[01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.(?:[01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                           "(?:[01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.(?:[01]?\\d\\d?|2[0-4]\\d|25[0-5])(?!\\d|\\.\\d)")) {
         if (!client.connect(ipAdress)) {
            Util.createAlertFrame(Alert.AlertType.ERROR, "Wrong ip adress", "Wrong ip adress",
                                  "The ip adress you wrote does not coincide with that of a robot. Please check the " +
                                  "ip adress of the robot and try again.");
         } else {
            worker.setConnected(true);
            worker.notify();
         }
      } else {
         Util.createAlertFrame(Alert.AlertType.ERROR, "Not an ip adress", "Not an ip adress",
                               "The adress you provided is not a valid ip adress. Please try again.");
      }

   }

   class ConnectedWorker implements Runnable {
      private boolean connected;
      private boolean running = true;

      public void signalShutdown() {
         this.running = false;
      }

      public boolean isConnected() {
         return connected;
      }

      public void setConnected(boolean connected) {
         this.connected = connected;
      }

      @Override
      public void run() {
         while (true) {
            if (!connected) {
               try {
                  this.wait();
               } catch (InterruptedException e) {
                  e.printStackTrace();
               }
            }
            RBConnectedStatus.fire();
            while (connected) {
               if (!client.isConnected()) {
                  connected = false;
                  RBConnectedStatus.fire();
               }
               try {
                  Thread.sleep(20000);
               } catch (InterruptedException e) {
                  e.printStackTrace();
               }
            }
            if (!running) {
               return;
            }
         }

      }
   }

}
