package com.company.shiva;

import java.sql.*;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NewUser {

    public static void newUserRegistration()
    {
        Scanner scan=new Scanner(System.in);
        try{
            Connection con= DriverManager.getConnection("jdbc:mysql://localhost:3306/jukebox","root","password@123");
            System.out.println("---Create your Account---");
            System.out.println("Enter your full name:");
            String full_name;
            boolean validOrNot4;
            do {
                full_name=scan.nextLine();
                validOrNot4 = isValid3(full_name);

                if(!validOrNot4)
                {
                    System.out.println("Invalid name format");
                    System.out.println("Please enter your valid name");
                }
            }while (!validOrNot4);

            boolean validOrNot2;
            System.out.println("Please enter your Mobile Number:");
            String number;
            do {
                number=scan.nextLine();
                validOrNot2 = isValid2(number);
                if(!validOrNot2)
                {
                    System.out.println("Invalid format");
                    System.out.println("Please enter your re-enter your Mobile Number");
                }
                Statement st= con.createStatement();
                ResultSet rs=st.executeQuery("select * from user where userid='"+number+"'");
                while (rs.next())
                {
                    validOrNot2=false;
                    System.out.println("Number already Exists");
                    System.out.println("Please enter your Mobile Number");
                }
            }while (!validOrNot2);

            System.out.println("Enter your email-id");
            boolean validOrNot;
            String email_id;
            do {
                email_id=scan.nextLine();
                validOrNot = isValid(email_id);
                if(validOrNot)
                {
                    System.out.println("Email Id Valid");
                }
                if(!validOrNot)
                {
                    System.out.println("Invalid format");
                    System.out.println("Please enter your valid email-id");
                }
            }while (!validOrNot);

            boolean validOrNot1;
            System.out.println("Enter your Password");
            String password;
            System.out.println("**Password should have atLeast one digit,one lower-case alphabet,one upper-case alphabet one symbol and atLeast 8 characters**");
            do {
                password=scan.nextLine();
                validOrNot1 = isValid1(password);
                if(validOrNot1)
                {
                    System.out.println("Password Valid");
                }
                if(!validOrNot1)
                {
                    System.out.println("Invalid format");
                    System.out.println("Please enter your password in valid format");
                }
            }while (!validOrNot1);

            PreparedStatement st= con.prepareStatement("insert into user values(?,?,?,?)");
            st.setString(1,number);
            st.setString(2,full_name);
            st.setString(3,email_id);
            st.setString(4,password);
            int i=st.executeUpdate();
            if(i>0)
            {
                System.out.println("Registration Successful");
                System.out.println("Your User Id is "+number);
                ExistingUser eu=new ExistingUser();
                eu.existingUser();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    public static boolean isValid(String email_id)
    {
        String regex="[A-Za-z0-9]+@(.+)$";
        Pattern pattern=Pattern.compile(regex);
        Matcher match=pattern.matcher(email_id);
        return match.matches();
    }
    public  static boolean isValid1(String password)
    {
        String regex="((?=.*[a-z])(?=.*d)(?=.*[@#$%])(?=.*[A-Z]).{6,16})";
        Pattern pattern=Pattern.compile(regex);
        Matcher match=pattern.matcher(password);
        return match.matches();
    }
    public  static boolean isValid2(String number)
    {
        String regex1="[7-9][0-9]{9}";
        Pattern pattern=Pattern.compile(regex1);
        Matcher match=pattern.matcher(number);
        return match.matches();
    }
    public  static boolean isValid3(String full_name)
    {
        String regex3="[A-za-z]+$";
        Pattern pattern=Pattern.compile(regex3);
        Matcher match=pattern.matcher(full_name);
        return match.matches();
    }
}
