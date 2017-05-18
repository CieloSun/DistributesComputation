package com.cielo.ex2.remote;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

/**
 * Created by 63289 on 2017/5/17.
 */
public class RemoteServer {
    private static final Integer port=8080;
    public static void main(String args[]) throws Exception{
        StudentSystem studentSystem=new StudentSystemImpl();
        LocateRegistry.createRegistry(port);
        Naming.bind("//127.0.0.1:"+port+"/SS",studentSystem);
    }
}
