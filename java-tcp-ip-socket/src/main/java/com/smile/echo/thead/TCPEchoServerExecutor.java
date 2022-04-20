package com.smile.echo.thead;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author zhanglei19@corp.netease.com
 * @date 2022-03-09 10:50 上午
 */
public class TCPEchoServerExecutor {
    public static void main(String[] args) throws IOException {
        int serverPort = 8888;
        // Create a server socket to accept client connection requests
        ServerSocket serverSocket = new ServerSocket(serverPort);
        // Run forever,accepting and servicing connection
        ExecutorService executorService = Executors.newCachedThreadPool();
        while(true) {
            Socket clientSocket = serverSocket.accept();
            executorService.execute(new EchoProtocol(clientSocket));
        }
    }
}
