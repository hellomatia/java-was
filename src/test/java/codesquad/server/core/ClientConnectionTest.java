package codesquad.server.core;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

class ClientConnectionTest {
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private ClientConnection clientConnection;

    @BeforeEach
    void 테스트_준비() throws IOException {
        serverSocket = new ServerSocket(0);
        clientSocket = new Socket("localhost", serverSocket.getLocalPort());
        clientConnection = new ClientConnection(clientSocket);
    }

    @AfterEach
    void 테스트_정리() throws IOException {
        clientConnection.close();
        clientSocket.close();
        serverSocket.close();
    }

    @Test
    void 입력_스트림_가져오기_테스트() {
        assertNotNull(clientConnection.getInputStream());
    }

    @Test
    void 출력_스트림_가져오기_테스트() {
        assertNotNull(clientConnection.getOutputStream());
    }

    @Test
    void 연결_닫기_테스트() throws IOException {
        clientConnection.close();
        assertTrue(clientSocket.isClosed());
    }
}
