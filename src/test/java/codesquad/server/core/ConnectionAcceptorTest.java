package codesquad.server.core;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.Socket;

import static org.junit.jupiter.api.Assertions.*;

class ConnectionAcceptorTest {
    private ConnectionAcceptor connectionAcceptor;
    private Port port;

    @BeforeEach
    void 테스트_준비() throws IOException {
        port = new Port(1);
        connectionAcceptor = new ConnectionAcceptor(port);
    }

    @AfterEach
    void 테스트_정리() {
        connectionAcceptor.close();
    }

    @Test
    void 연결_수락_테스트() throws IOException {
        Thread clientThread = new Thread(() -> {
            try {
                new Socket("localhost", port.getValue());
            } catch (IOException e) {
                fail("클라이언트 연결 실패");
            }
        });
        clientThread.start();

        Socket acceptedSocket = connectionAcceptor.accept();
        assertNotNull(acceptedSocket);
        assertTrue(acceptedSocket.isConnected());
    }

    @Test
    void 서버소켓_닫기_테스트() throws IOException {
        connectionAcceptor.close();
        assertThrows(IOException.class, () -> connectionAcceptor.accept());
    }
}
