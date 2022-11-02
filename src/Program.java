import java.sql.*;
import java.io.*;
import java.util.*;


public class Program
{
	public static void main(String[] args)
	{
		try
		{
			//Initial Variables
			boolean runAgain = true;
			Scanner sc = new Scanner(System.in);
			String name, password, validate;
			
			//Take user login information
			System.out.println("Hello user! Please enter your username: ");
			name = sc.nextLine();
			System.out.println("Hello " + name + ", please enter your password: ");
			password = sc.nextLine();
			System.out.println("You have entered:");
			System.out.println("Name: " + name);
			System.out.println("Password: " + password);
			
			System.out.println("Please enter Y/n: ");
			validate = sc.next();
			
			//Validate Information
			if(validate.equals("n") || validate.equals("N"))
			{
				//User entered wrong information, exit
				sc.close();
				System.exit(1);
			}
			sc.close();
			
			
			do 
			{
				runAgain = false;
				System.out.println("Test Success");
			}
			while(runAgain);
			//Good Exit
			System.exit(0);
		}
		catch(Exception e)
		{
			System.out.println("Error: " + e.toString());
			System.exit(-1);
		}
	}
}