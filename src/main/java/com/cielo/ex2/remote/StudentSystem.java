package com.cielo.ex2.remote;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by 63289 on 2017/5/17.
 */
public interface StudentSystem extends Remote {
    public Integer getScore(String name) throws RemoteException;
    public void addScore(String name,Integer score) throws RemoteException;
}
