package ch.heigvd.robotpi.communication;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.io.IOException;

class ClientBadServerTest {
    Client cli = new Client();

    @BeforeAll
    static void beforeAll() {
        Thread srvThread = new Thread(new ClientGoodServerTest.Server(2025, "bad"));
        srvThread.start();
    }

    @AfterEach
    void teardown() {
        try {
            cli.disconnect();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    void pingTrowsException() {
        assertThrows(Client.LostConnectionException.class,
                ()->{
                    cli.connect("127.0.0.1");
                    cli.ping();
                });
    }

    @Test
    void goForwardTrowsException() {
        assertThrows(Client.RobotException.class,
                ()->{
                    cli.connect("127.0.0.1");
                    cli.goForward();
                });
    }

    @Test
    void goBackwardTrowsException() {
        assertThrows(Client.RobotException.class,
                ()->{
                    cli.connect("127.0.0.1");
                    cli.goBackward();
                });
    }

    @Test
    void goLeftTrowsException() {
        assertThrows(Client.RobotException.class,
                ()->{
                    cli.connect("127.0.0.1");
                    cli.goLeft();
                });
    }

    @Test
    void goRightTrowsException() {
        assertThrows(Client.RobotException.class,
                ()->{
                    cli.connect("127.0.0.1");
                    cli.goRight();
                });
    }

    @Test
    void stopTrowsException() {
        assertThrows(Client.RobotException.class,
                ()->{
                    cli.connect("127.0.0.1");
                    cli.stop();
                });
    }

    // TODO goFrontLeft and next
}
