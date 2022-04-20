package com.smile.echo.thead;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.logging.Logger;

/**
 * @author zhanglei19@corp.netease.com
 * @date 2022-03-09 10:39 上午
 */
public class TCPEchoServerThread {

    public static void main(String[] args) throws IOException {
        int serverPort = 8888;
        // Create a server socket to accept client connection requests
        ServerSocket serverSocket = new ServerSocket(serverPort);
        // Run forever,accepting and servicing connection
        while (true) {
            // get client connection
            Socket clientSocket = serverSocket.accept();
            Thread thread = new Thread(new EchoProtocol(clientSocket));
            thread.start();

            System.out.println("Create and started thread" + thread.getName());
        }
    }
}
