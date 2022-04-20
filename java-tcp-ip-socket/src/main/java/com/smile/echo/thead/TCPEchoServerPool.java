package com.smile.echo.thead;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author zhanglei19@corp.netease.com
 * @date 2022-03-09 10:44 上午
 */
public class TCPEchoServerPool {
    public static void main(String[] args) throws IOException {
        int serverPort = 8888;
        // Create a server socket to accept client connection requests
        ServerSocket serverSocket = new ServerSocket(serverPort);
        // Run forever,accepting and servicing connection
        for (int i = 0; i < 10; i++) {
            Thread thread = new Thread(() -> {
                while (true) {
                    // get client connection
                    Socket clientSocket = null;
                    try {
                        clientSocket = serverSocket.accept();
                        EchoProtocol.handleEchoClient(clientSocket);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            thread.start();
            System.out.println("Create and started thread" + thread.getName());
        }
    }
}
