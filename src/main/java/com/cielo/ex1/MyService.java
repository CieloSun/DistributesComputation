package com.cielo.ex1;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by 63289 on 2017/5/10.
 */
public class MyService {
    private Integer port;
    public MyService(Integer port){
        this.port=port;
    }
    public void start() throws IOException{
        ServerSocket serverSocket=new ServerSocket(port);//create,bind,listen
        File file=new File("data/fileSend.txt");
        FileInputStream fileInputStream=new FileInputStream(file);
        int length=fileInputStream.available();
        if(length==0) length=16384;
        byte[] bytes=new byte[length];
        fileInputStream.read(bytes);
        fileInputStream.close();
        Socket socket=serverSocket.accept();//accept
        socket.getOutputStream().write(bytes);
        socket.close();
    }
    public static void main(String[] args) throws IOException{
        new MyService(8080).start();
    }
}
