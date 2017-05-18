package com.cielo.ex1;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Created by 63289 on 2017/5/17.
 */
public class MyClient {
    private Integer port;
    public MyClient(Integer port){
        this.port=port;
    }
    public void start() throws IOException{
        File file=new File("data/fileReceive.txt");
        FileOutputStream fileOutputStream=new FileOutputStream(file);
        Socket socket=new Socket("127.0.0.1",port);
        int length=socket.getInputStream().available();
        if(length==0) length=16384;
        byte[] bytes=new byte[length];
        socket.getInputStream().read(bytes);
        socket.close();
        fileOutputStream.write(bytes);
        fileOutputStream.close();
    }
    public static void main(String[] args) throws IOException{
        new MyClient(8080).start();
    }
}
