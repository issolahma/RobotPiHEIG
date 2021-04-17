package ch.heigvd.robotpi.communication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class implements a single-threaded TCP server.
 */
class Server implements Runnable{

    final static Logger LOG = Logger.getLogger(Server.class.getName());
    private ServerSocket serverSocket;
    private Socket clientSocket = null;
    private BufferedReader in = null;
    private PrintWriter out = null;
    private int port;
    private String serverType;

    @Override
    public void run() {
        try {
            this.start();
            this.serveClients();
            this.stop();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Constructor
     *
     * @param port the port to listen on
     */
    public Server(int port, String serverType) {
        this.port = port;
        this.serverType = serverType;
    }

    public void start() throws IOException {
        LOG.info("Start server...");
        serverSocket = new ServerSocket(port);
    }

    public void stop() throws IOException {
        LOG.info("Cleaning up resources...");
        clientSocket.close();
        in.close();
        out.close();
    }

    /**
     * This method initiates the process.
     */
    public void serveClients() throws IOException {

        while (true) {
            LOG.log(Level.INFO, "Waiting (blocking) for a new client on port {0}", port);
            clientSocket = serverSocket.accept();
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new PrintWriter(clientSocket.getOutputStream());
            String line;
            boolean shouldRun = true;

            LOG.info("Reading until client send quit or closes the connection...\n");
            while ((shouldRun) && (line = in.readLine()) != null) {
                switch (line) {
                    case "PING":
                        if (serverType.equals("good")) {
                            out.println("PING");
                            LOG.info("PING");
                        } else {
                            out.println("PINGG");
                            LOG.info("PINGG");
                        }
                        break;
                    case "FWD":
                        if (serverType.equals("good")) {
                            out.println("FWD_OK");
                            LOG.info("PIFWD_OKNG");
                        } else {
                            out.println("FWD_KO");
                            LOG.info("FWD_KO");
                        }
                        break;
                    case "BKWD":
                        if (serverType.equals("good")) {
                            out.println("BKWD_OK");
                            LOG.info("BKWD_OK");
                        } else {
                            out.println("BKWD_KO");
                            LOG.info("BKWD_KO");
                        }
                        break;
                    case "ROTATE_LEFT":
                        if (serverType.equals("good")) {
                            out.println("ROTATE_LEFT_OK");
                            LOG.info("ROTATE_LEFT_OK");
                        }  else {
                            out.println("ROTATE_LEFT_KO");
                            LOG.info("ROTATE_LEFT_KO");
                        }
                        break;
                    case "ROTATE_RIGHT":
                        if (serverType.equals("good")) {
                            out.println("ROTATE_RIGHT_OK");
                            LOG.info("ROTATE_RIGHT_OK");
                        } else {
                            out.println("ROTATE_RIGHT_KO");
                            LOG.info("ROTATE_RIGHT_KO");
                        }
                        break;
                    case "STOP":
                        if (serverType.equals("good")) {
                            out.println("STOP");
                            LOG.info("STOP");
                        } else {
                            out.println("STOPP");
                            LOG.info("STOPP");
                        }
                        break;
                    case "quit":
                        shouldRun = false;
                }

                out.flush();
            }

        }
    }


}

