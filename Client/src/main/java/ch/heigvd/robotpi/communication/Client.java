package ch.heigvd.robotpi.communication;

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

    public void ping() throws IOException, CommException {
        out.println("PING");
        if (!in.readLine().equals("PING")) {
            throw new CommException();
        }
    }

    //lancer des exception dans le cas ou serveur ne reagit pas comme prevu

    public void goForward() throws CommException, IOException {
            out.println("FWD");
            if (!in.readLine().equals("FWD_OK")) {
                throw new CommException();
            }
    }

    public void goBackward() throws IOException, CommException {
        out.println("BKWD");
        if (!in.readLine().equals("BKWD_OK")) {
            throw new CommException();
        }
    }
    public void goLeft() throws IOException, CommException {
        out.println("ROTATE_LEFT");
        if (!in.readLine().equals("ROTATE_LEFT_OK")) {
            throw new CommException();
        }
    }
    public void goRight() throws IOException, CommException {
        out.println("ROTATE_RIGHT");
        if (!in.readLine().equals("ROTATE_RIGHT_OK")) {
            throw new CommException();
        }
    }

    //TODO : a voir avec le protocole pour ces méthodes et la classe interne d'erreur
    public void goFrontLeft() {}
    public void goFrontRight() {}
    public void goBackwardsRight() {}
    public void goBackwardsLeft() {}

    public class CommException extends Exception {

    }

}
