package ch.heigvd.robotpi.communication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Server {
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

    public void disconnect() throws IOException {
        try {
            in.close();
            out.close();
            clientSocket.close();
        } catch (IOException e) {

        }
    }

    public void goForward() {}
    public void goBackward() {}
    public void goLeft() {}
    public void goRight() {}
    public void goFrontLeft() {}
    public void goFrontRight() {}
    public void goBackwardsRight() {}
    public void goBackwardsLeft() {}
}
