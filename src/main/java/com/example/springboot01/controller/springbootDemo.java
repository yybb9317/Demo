package com.example.springboot01.controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class springbootDemo {
    public static void main(String[] args) {
        new springbootDemo().getConnect();
    }

    public Connection getConnect() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            String url = "jdbc:mysql://localhost/student?useUnicode=true&characterEncoding=utf-8";
            String user="root";
            String password = "123456";
            Connection conn = DriverManager.getConnection(url, user, password);

            Statement stat = conn.createStatement();
            String command = "select * from information";
            ResultSet result =  stat.executeQuery(command);
            while(result.next()) {
                //使用访问器方法获取信息
                System.out.println(result.getString(1) + " " + result.getString(2));
            }
            result.close();
            return conn;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
