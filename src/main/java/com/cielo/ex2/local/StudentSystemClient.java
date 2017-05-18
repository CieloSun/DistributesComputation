package com.cielo.ex2.local;
import com.cielo.ex2.remote.StudentSystem;

import java.rmi.Naming;

/**
 * Created by 63289 on 2017/5/17.
 */
public class StudentSystemClient {
    private final static Integer port=8080;
    public static void main(String[] args) throws Exception{
        StudentSystem studentSystem= (StudentSystem) Naming.lookup("//127.0.0.1:"+port+"/SS");
        studentSystem.addScore("Cielo",100);
        System.out.println(studentSystem.getScore("Cielo"));
    }
}
