package com.company.shiva;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
	// write your code here
        Scanner scan=new Scanner(System.in);
        int opt;
        System.out.println("---****Welcome to Jukebox****---");
        System.out.println("Please Select your Choice:\n1.New User-Sign Up \n2.Existing User-Sign In \n3.Exit");
        opt = scan.nextInt();
        switch (opt){
            case 1:NewUser nu = new NewUser();
                nu.newUserRegistration();
                break;
            case 2:ExistingUser eu=new ExistingUser();
                eu.existingUser();
                break;
            case 3: break;
        }
    }
}
