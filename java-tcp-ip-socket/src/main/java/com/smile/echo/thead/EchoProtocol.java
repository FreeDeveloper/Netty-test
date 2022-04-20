package com.smile.echo.thead;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * @author zhanglei19@corp.netease.com
 * @date 2022-03-09 10:26 上午
 */
public class EchoProtocol implements Runnable {
    // Size(in bytes) od I/O buffer
    private static final int BUF_SIZE = 32;
    // Socket connect to client
    private Socket socket;

    public EchoProtocol(Socket socket) {
        this.socket = socket;
    }

    public static void handleEchoClient(Socket socket) throws IOException {
        InputStream in = socket.getInputStream();
        OutputStream out = socket.getOutputStream();

        // Size of received message
        int receivedMessageSize;
        int totalReceiveBytes = 0;
        // Receive buffer
        byte[] receiveBuf = new byte[BUF_SIZE];

        // Receive until client closes connection,indicated by -1 return
        while ((receivedMessageSize = in.read(receiveBuf)) != -1) {
            out.write(receiveBuf, 0, receivedMessageSize);
            totalReceiveBytes += receivedMessageSize;
        }
        System.out.println("Thread " + Thread.currentThread() + "Client " + socket.getRemoteSocketAddress() + ", echoed " + totalReceiveBytes + " bytes");
        // Close the socket.We are done with this client
        socket.close();
    }

    @Override
    public void run() {
        try {
            handleEchoClient(socket);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
