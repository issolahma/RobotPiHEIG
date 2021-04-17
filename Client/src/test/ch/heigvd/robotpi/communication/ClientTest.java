package ch.heigvd.robotpi.communication;

import org.junit.jupiter.api.*;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class ClientTest {
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
    void teardown(){

    }

    @Test
    void returnFalseWhenNotConnecting() {
        boolean result = cli.connect("aaa.333.333.333");
        boolean expected = false;
        assertEquals(expected, result);
    }

    @Test
    void returnTrueWhenConnecting() {
        boolean result = cli.connect("127.0.0.1");
        cli.disconnect();
        boolean expected = true;
        assertEquals(expected, result);
    }


    @Test
    void isConnectedReturnTrueWhenConnected() {
        cli.connect("127.0.0.1");
        boolean result = cli.isConnected();
        cli.disconnect();
        boolean expected = true;
        assertEquals(expected, result);
    }

    @Test
    void isConnectedReturnFalseWhenNotConnected() {
        cli.connect("aaa.333.333.333");
        boolean result = cli.isConnected();
        boolean expected = false;
        assertEquals(expected, result);
    }

    @Test
    void disconnectWorks() {
        boolean result = false;
        boolean expected = true;

        cli.connect("127.0.0.1");
        if (cli.isConnected()){
            cli.disconnect();
            if (!cli.isConnected())
                result = true;
        }

        assertEquals(expected, result);
    }

    @Test
    void pingCmdIsReceived() {
        cli.connect("127.0.0.1");
        try {
            cli.ping();
        } catch (Client.RobotException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // TODO ...
    }

}