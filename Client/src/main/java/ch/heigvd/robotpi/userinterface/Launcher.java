/*
 * @File Launcher.java
 * @Authors : David González León
 * @Date 17 mars 2021
 */
package ch.heigvd.robotpi.userinterface;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Launcher extends Application {
   public static void main(String[] args) {
      launch();
   }

   @Override
   public void start(Stage stage) throws Exception {
      FXMLLoader uiLoader = new FXMLLoader();
      uiLoader.setLocation(getClass().getClassLoader().getResource("mainView.fxml"));
      Scene loginScene = new Scene(uiLoader.load());
      UIController uiController = uiLoader.getController();
      uiController.setScene(loginScene);

      uiController.load(stage);
      stage.show();
      stage.setMaximized(true);
   }
}
