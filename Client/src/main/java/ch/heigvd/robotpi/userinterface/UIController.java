/*
 * @File UIController.java
 * @Authors : David González León
 * @Date 24 mars 2021
 */
package ch.heigvd.robotpi.userinterface;

import ch.heigvd.robotpi.communication.Client;
import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * The controller of the main window of the client's app
 */
public class UIController {
   private Scene scene;
   private Client client;
   private ConnectedWorker worker;

   /**
    * Boolean to know when a key is pressed
    */
   private Boolean upPressed = false;
   private boolean rightPressed = false;
   private boolean leftPressed = false;
   private boolean downPressed = false;

   @FXML private Button BFrontLeft;
   @FXML private Button BFront;
   @FXML private Button BFrontRight;
   @FXML private Button BLeft;
   @FXML private Button BRight;
   @FXML private Button BBackwardsLeft;
   @FXML private Button BBackwards;
   @FXML private Button BBackwardsRight;


   @FXML private Label LConnectionStatus;
   @FXML private TextField TFConnectionAddress;

   /**
    * Sets the scene linked to this controller
    *
    * @param scene the scene
    */
   public void setScene(Scene scene) {
      this.scene = scene;
      scene.addEventFilter(KeyEvent.KEY_PRESSED, keyEvent -> {
         switch (keyEvent.getCode()) {
            case LEFT:
               leftPressed = true;
               break;
            case RIGHT:
               rightPressed = true;
               break;
            case DOWN:
               downPressed = true;
               break;
            case UP:
               upPressed = true;
               break;
            default:
               break;
         }
      });
      scene.addEventFilter(KeyEvent.KEY_RELEASED, keyEvent -> {
         switch (keyEvent.getCode()) {
            case LEFT:
               leftPressed = false;
               break;
            case RIGHT:
               rightPressed = false;
               break;
            case DOWN:
               downPressed = false;
               break;
            case UP:
               upPressed = false;
               break;
            default:
               break;
         }
      });
      scene.getRoot().requestFocus();

   }

   /**
    * Loads onto the stage the scene, and executes the basic setup for the ui
    *
    * @param primaryStage the primary stage
    */
   public void load(Stage primaryStage) {
      client = new Client();
      worker = new ConnectedWorker();
      primaryStage.setScene(scene);
      primaryStage.showingProperty().addListener(((observableValue, oldValue, showing) -> {
         if (showing) {
            primaryStage.setMinHeight(primaryStage.getHeight());
            primaryStage.setMinWidth(primaryStage.getWidth());
         }
      }));
      primaryStage.setTitle("Robot PI HEIG");
      //handles key pressing
      AnimationTimer timer = new AnimationTimer() {
         @Override
         public void handle(long l) {
            try {
               if (worker.connected) {
                  if (upPressed) {
                     if (leftPressed) {
                        client.goFrontLeft();
                     } else if (rightPressed) {
                        client.goFrontRight();
                     } else if (!downPressed) {
                        client.goForward();
                     }
                  } else if (downPressed) {
                     if (leftPressed) {
                        client.goBackwardsLeft();
                     } else if (rightPressed) {
                        client.goBackwardsRight();
                     } else {
                        client.goBackward();
                     }
                  } else if (leftPressed) {
                     if (!rightPressed) {
                        client.goLeft();
                     }
                  } else if (rightPressed) {
                     client.goRight();
                  }
               }
            } catch (IOException e) {
               e.printStackTrace();
            } catch (Client.RobotException e) {
               e.printStackTrace();
            }
         }
      };
      timer.start();
   }

   /**
    * Closes the ui
    */
   public void close() {
      worker.signalShutdown();
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
            worker.setConnected();
         }
      } else {
         Util.createAlertFrame(Alert.AlertType.ERROR, "Not an ip adress", "Not an ip adress",
                               "The adress you provided is not a valid ip adress. Please try again.");
      }

   }

   /**
    * The worker used to keep the connected RadioButton up to date
    */
   class ConnectedWorker implements Runnable {
      private boolean connected;
      private boolean running = true;

      /**
       * SSignals to the worker that the UI is being closed, and that it needs to stop running
       */
      public void signalShutdown() {
         this.running = false;
      }

      /**
       * Informs of the status of the connection to the robot
       *
       * @return true if the Client is connected to a robot, false otherwise
       */
      public boolean isConnected() {
         return connected;
      }


      /**
       * Informs the worker that a connection has been made
       */
      public void setConnected() {
         this.connected = true;
         this.notify();
      }

      @Override
      public void run() {
         while (running) {
            if (!connected) {
               try {
                  this.wait();
               } catch (InterruptedException e) {
                  e.printStackTrace();
               }
            }
            LConnectionStatus.setText("Connected");
            while (connected) {
               if (!client.isConnected()) {
                  connected = false;
                  LConnectionStatus.setText("Disconnected");
               }
               try {
                  Thread.sleep(20000);
               } catch (InterruptedException e) {
                  e.printStackTrace();
               }
            }
         }

      }
   }
}
