package com.company.shiva;

import java.sql.*;
import java.util.Scanner;

public class ExistingUser {
    public static void existingUser()
    {
        Scanner scan=new Scanner(System.in);
        System.out.println("Please Enter your Login Credentials");
        System.out.println("Enter your USER ID");
        String user_id=scan.nextLine();
        System.out.println("Enter your Password");
        String password= scan.nextLine();
        try {
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/jukebox", "root", "password@123");
            Statement st= con.createStatement();
            ResultSet rs=st.executeQuery("select * from user where userid="+user_id+" and password='"+password+"'");
            if(rs.next())
            {
                System.out.println("Login Successful");
                System.out.println("Welcome "+rs.getString(2));
                AllSongsPodcasts ds=new AllSongsPodcasts();
                ds.displayAllSongs(user_id);
            }
            else
            {
                System.out.println("Invalid Credentials");
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

    }
}
