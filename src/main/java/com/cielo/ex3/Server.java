package com.cielo.ex3;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by 63289 on 2017/5/17.
 */
public class Server {
    private Integer port;
    public Server(Integer port) {
        this.port = port;
    }
    public void start() throws IOException {
        ServerSocket serverSocket = new ServerSocket(port);//create,bind,listen
        while (true) {
            Socket socket = serverSocket.accept();//accept
            new Thread(new SocketHandler(socket, new File("data"))).start();
        }
    }
    public static void main(String[] args) throws IOException {
        new Server(8080).start();
    }
}


