package com.cielo.ex2.remote;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * Created by 63289 on 2017/5/17.
 */
public class StudentSystemImpl extends UnicastRemoteObject implements StudentSystem {
    public StudentSystemImpl() throws RemoteException{
    }
    public Integer getScore(String name) throws RemoteException{
        String score=RedisUtil.REDIS_UTIL.getJedis().get(name);
        return (score==null)?-1:Integer.parseInt(score);
    }
    public void addScore(String name,Integer score) throws RemoteException{
        RedisUtil.REDIS_UTIL.getJedis().set(name,score.toString());
    }
}
