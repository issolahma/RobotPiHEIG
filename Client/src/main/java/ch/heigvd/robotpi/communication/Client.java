package ch.heigvd.robotpi.communication;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    private boolean isConnected;
    public final int PORT = 2025;

    public boolean connect(String ip) {
        try {
            clientSocket = new Socket(ip, PORT);
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            isConnected = true;
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public boolean isConnected() {
        return isConnected;
    }

    public void disconnect() {
        try {
            in.close();
            out.close();
            clientSocket.close();
        } catch (IOException e) {

        }
    }

    //TODO catch les ioException et throw les bonnes exc

    public void ping() throws IOException, RobotException {
        out.println("PING");
        if (!in.readLine().equals("PING")) {
            throw new RobotException();
        }
    }

    //lancer des exception dans le cas ou serveur ne reagit pas comme prevu

    public void goForward() throws RobotException, IOException {
        out.println("FWD");
        if (!in.readLine().equals("FWD_OK")) {
            throw new RobotException();
        }
        isMoving = true;
    }

    public void goBackward() throws IOException, RobotException {
        out.println("BKWD");
        if (!in.readLine().equals("BKWD_OK")) {
            throw new RobotException();
        }
        isMoving = true;
    }

    public void goLeft() throws IOException, RobotException {
        out.println("ROTATE_LEFT");
        if (!in.readLine().equals("ROTATE_LEFT_OK")) {
            throw new RobotException();
        }
        isMoving = true;
    }

    public void goRight() throws IOException, RobotException {
        out.println("ROTATE_RIGHT");
        if (!in.readLine().equals("ROTATE_RIGHT_OK")) {
            throw new RobotException();
        }
        isMoving = true;
    }

    public void stop() throws IOException, RobotException {
        out.println("STOP");
        if (!in.readLine().equals("STOP")) {
            throw new RobotException();
        }
        isMoving = false;
    }

    //TODO : a voir avec le protocole pour ces méthodes et la classe interne d'erreur
    public void goFrontLeft() {
    }

    public void goFrontRight() {
    }

    public void goBackwardsRight() {
    }

    public void goBackwardsLeft() {
    }

    public class CommException extends Exception {
    }

    public class CantConnectException extends Exception {
        // pb connexion en general
    }

    public class IncorrectDeviceException extends Exception {
        // qqn avec IP mais Robot pi
    }

    public class LostConnectionException extends Exception {
        //pb ping connexion
    }

    public class RobotException extends Exception {
        // par ex si robot envoi mauvaise reponse, pb cote robot en general
    }

    public boolean isMoving() {
        return isMoving;
    }

    private boolean isMoving = false;

}
