package ch.heigvd.robotpi.communication;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class ClientTestGoodServer {

    Client cli = new Client();

    @BeforeAll
    static void beforeAll() {
        Thread srvThread = new Thread(new Server(2025, "good"));
        srvThread.start();
    }

    @BeforeEach
    void setUp() {

    }

    @AfterEach
    void teardown() {

    }

    @Test
    void isConnectedReturnTrueWhenConnected() {
        boolean result = false;
        try {
            cli.connect("127.0.0.1");
            result = cli.isConnected();
            cli.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }

        boolean expected = true;
        assertEquals(expected, result);
    }

    @Test
    void isConnectedReturnFalseWhenNotConnected() { // TODO A revoir
        // To get the default value FALSE
        boolean result = cli.isConnected();

        try {
            // throws timout exception because wrong ip
            cli.connect("192.22.22.22");
            // not updated here
            result = cli.isConnected();
        } catch (Exception e) {
            e.printStackTrace();
        }

        boolean expected = false;
        assertEquals(expected, result);
    }

    @Test
    void disconnectWorks() {
        boolean result = false;
        boolean expected = true;

        try {
            cli.connect("127.0.0.1");
            if (cli.isConnected()) {
                cli.disconnect();
                if (!cli.isConnected())
                    result = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals(expected, result);
    }
}

