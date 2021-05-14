package ch.heigvd.robotpi.communication;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import javax.net.ssl.*;

public class Client {
    private SSLSocket clientSocket = null;
    private static final String[] protocols = new String[] {"TLSv1.3"};
    private static final String[] cipher_suites = new String[] {"TLS_ECDHE_RSA_WITH_AES_256_GCM_SHA384"}; // TLS_AES_128_GCM_SHA256
    private PrintWriter out;
    private BufferedReader in;
    private boolean isConnected;
    public final int PORT = 2025;

    public void connect(String ip) throws CantConnectException, IOException, IncorrectDeviceException {
        //clientSocket = new Socket(ip, PORT);

        try (SSLSocket socket = createSocket(ip, PORT)) {
            this.clientSocket = socket;
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            printSocketInfo(clientSocket);
            clientSocket.startHandshake();


            isConnected = true;

            String message = in.readLine(); // welcom msg
            System.out.println("recu: " + message); // REMOVE

            out.println("CONN");
            out.flush();
            //while(!in.ready()) {
            message = in.readLine();
            System.out.println("recu: " + message); // REMOVE
            //}


            if (message.equals("CONN_ERR")) {
                clientSocket.close();
                throw new CantConnectException();
            } else if (!message.equals("CONN_OK")) {
                clientSocket.close();
                throw new IncorrectDeviceException();
            }
        } catch (IOException e) {
            System.err.println(e.toString());
        }
    }

    public static SSLSocket createSocket(String host, int port) throws IOException {
        SSLSocket socket = (SSLSocket) SSLSocketFactory.getDefault()
                .createSocket(host, port);
        socket.setEnabledProtocols(protocols);
        socket.setEnabledCipherSuites(socket.getSupportedCipherSuites());

        // REMOVE
        for (String c : socket.getSupportedProtocols())
        System.out.println(c);
        return socket;
    }

    // TO REMOVE
    private static void printSocketInfo(SSLSocket s) {
        System.out.println("Socket class: "+s.getClass());
        System.out.println("   Remote address = "
                +s.getInetAddress().toString());
        System.out.println("   Remote port = "+s.getPort());
        System.out.println("   Local socket address = "
                +s.getLocalSocketAddress().toString());
        System.out.println("   Local address = "
                +s.getLocalAddress().toString());
        System.out.println("   Local port = "+s.getLocalPort());
        System.out.println("   Need client authentication = "
                +s.getNeedClientAuth());
        SSLSession ss = s.getSession();
        System.out.println("   Cipher suite = "+ss.getCipherSuite());
        System.out.println("   Protocol = "+ss.getProtocol());
    }

    public boolean isConnected() {
        return isConnected;
    }

    public void disconnect() throws IOException {
        int count = 1;
        String message;
        do {
            out.println("DISCONN");
            message = in.readLine();
        } while (!message.equals("DISCONN_OK") && count++ != 5);
        in.close();
        out.close();
        clientSocket.close();
        if (message.equals("DISCONN_OK")) {
            isConnected = false;
        }

    }

    //TODO catch les ioException et throw les bonnes exc

    public void ping() throws IOException, LostConnectionException {
        out.println("PING");
        if (!in.readLine().equals("PING")) {
            throw new LostConnectionException();
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
    public void goFrontLeft() throws IOException, RobotException {
        out.println("FRONT_L");
        if (!in.readLine().equals("FRONT_L_OK")) {
            throw new RobotException();
        }
        isMoving = true;
    }

    public void goFrontRight() throws RobotException, IOException {
        out.println("RIGHT_R");
        if (!in.readLine().equals("RIGHT_R_OK")) {
            throw new RobotException();
        }
        isMoving = true;
    }

    public void goBackwardsRight() throws IOException, RobotException {
        out.println("BCK_R");
        if (!in.readLine().equals("BCK_R_OK")) {
            throw new RobotException();
        }
        isMoving = true;
    }

    public void goBackwardsLeft() throws IOException, RobotException {
        out.println("BCK_L");
        if (!in.readLine().equals("BCK_L_OK")) {
            throw new RobotException();
        }
        isMoving = true;
    }

    public class CommException extends Exception {
    }

    public class CantConnectException extends CommException {
        // pb connexion en general
    }

    public class IncorrectDeviceException extends CommException {
        // qqn avec IP mais pas Robot pi
    }

    public class LostConnectionException extends CommException {
        //pb ping connexion
    }

    public class RobotException extends CommException {
        // par ex si robot envoi mauvaise reponse, pb cote robot en general
    }

    public boolean isMoving() {
        return isMoving;
    }

    private boolean isMoving = false;

}

/*
TLS_AES_128_GCM_SHA256
TLS_AES_256_GCM_SHA384
    TLS_ECDHE_ECDSA_WITH_AES_256_GCM_SHA384
TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256
    TLS_ECDHE_RSA_WITH_AES_256_GCM_SHA384
TLS_RSA_WITH_AES_256_GCM_SHA384
TLS_DHE_RSA_WITH_AES_256_GCM_SHA384
TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256
TLS_RSA_WITH_AES_128_GCM_SHA256
TLS_DHE_RSA_WITH_AES_128_GCM_SHA256
    TLS_ECDHE_ECDSA_WITH_AES_256_CBC_SHA384
    TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA384
TLS_RSA_WITH_AES_256_CBC_SHA256
TLS_DHE_RSA_WITH_AES_256_CBC_SHA256
    TLS_ECDHE_ECDSA_WITH_AES_256_CBC_SHA
    TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA
TLS_RSA_WITH_AES_256_CBC_SHA
TLS_DHE_RSA_WITH_AES_256_CBC_SHA
TLS_ECDHE_ECDSA_WITH_AES_128_CBC_SHA256
TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA256
TLS_RSA_WITH_AES_128_CBC_SHA256
TLS_DHE_RSA_WITH_AES_128_CBC_SHA256
TLS_ECDHE_ECDSA_WITH_AES_128_CBC_SHA
TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA
TLS_RSA_WITH_AES_128_CBC_SHA
TLS_DHE_RSA_WITH_AES_128_CBC_SHA
TLS_EMPTY_RENEGOTIATION_INFO_SCSV
TLS_RSA_WITH_NULL_SHA256
TLS_ECDHE_ECDSA_WITH_NULL_SHA
TLS_ECDHE_RSA_WITH_NULL_SHA
SSL_RSA_WITH_NULL_SHA

TLSv1.3
TLSv1.2
TLSv1.1
TLSv1
SSLv3
SSLv2Hello
 */