package com.smile.echo.thead;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * @author zhanglei19@corp.netease.com
 * @date 2022-03-09 10:26 上午
 */
public class TimelimitEchoProtocol implements Runnable {
    // Size(in bytes) od I/O buffer
    private static final int BUF_SIZE = 32;
    private static final String TIME_LIMIT = "10000";
    // Socket connect to client
    private Socket socket;
    private static int timeLimit;

    public TimelimitEchoProtocol(Socket socket) {
        this.socket = socket;
        timeLimit = Integer.parseInt(TIME_LIMIT);
    }

    public static void handleEchoClient(Socket socket) throws IOException {
        InputStream in = socket.getInputStream();
        OutputStream out = socket.getOutputStream();

        // Size of received message
        int receivedMessageSize;
        int totalReceiveBytes = 0;
        // Receive buffer
        byte[] receiveBuf = new byte[BUF_SIZE];
        long endTime = System.currentTimeMillis() + timeLimit;
        int timeBoundMillis = timeLimit;

        socket.setSoTimeout(timeBoundMillis);

        // Receive until client closes connection,indicated by -1 return
        while (timeBoundMillis > 0 && (receivedMessageSize = in.read(receiveBuf)) != -1) {
            out.write(receiveBuf, 0, receivedMessageSize);
            totalReceiveBytes += receivedMessageSize;
            timeBoundMillis = (int)(endTime - System.currentTimeMillis());
            socket.setSoTimeout(timeBoundMillis);
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
