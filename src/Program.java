import java.sql.*;
//import java.io.*;
import java.util.*;


public class Program
{
	public static void main(String[] args)
	{
		try
		{
			//Initial Variables
			boolean runAgain = true, login = true;
			Scanner sc = new Scanner(System.in);
			String name, password, validate;
			
			//Take user login information
			do
			{
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
				if(validate.equals("Y") || validate.equals("y"))
				{
					//User entered wrong information
					login = false;
				}
			} while (login);
			

			//Open Connection to DB
			try
			{
				Class.forName("com.mysql.cj.jdbc.Driver");
				try(Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/library", name, password);)
				{
					System.out.println("Connection Successfull");
					System.out.println();

					//Main Menu
					do 
					{
						System.out.println("Main Menu:");
						System.out.println();
						System.out.println("1. Retrieve ISBN, title, genre name, publication date, name of the publisher, edition number and description of each book.");
						System.out.println("2. Retrieve for a book first, middle and last names of its authors.");
						System.out.println("3. Retrieve ISBN, title and barcode of every book copy.");
						System.out.println("4. Retrieve card number, first, middle and last name of every member.");
						System.out.println("5. Retieve info (ISBN, title, barcode, date borrowed and number of renewals) of every loan that was not finalized for a chosen member.");
						System.out.println("6. Register in the system the return of a book a chosen member borrowed.");
						System.out.println("7. Borrow a book copy to a chosen member.");
						System.out.println("8. Renew a loan of a book copy to a chosen member.");
						System.out.println("9. Retrieve how much money a chosen member owns to the library.");
						System.out.println("10. Retrieve for a member ISBN, title, barcode, date borrowed, date returned and fee for every book he owns money for to the library.");
						System.out.println("11. Register in the system, for a member, a payment for a loan of a book copy that was overdue.");
						System.out.println("12. Exit");
						
						//Take Input
						validate = sc.next();
						
						//Menu Logic
						switch(validate)
						{
							case "1":
								//Open Statement
								try (Statement stmt = conn.createStatement();)
								{
									try
									{
										//Execute Query
										ResultSet rset = stmt.executeQuery("select BOOK.ISBN, BOOK.title, GENRE.name, BOOK.date_published, BOOK.publisher, BOOK.edition, BOOK.description from BOOK natural join GENRE order by BOOK.title asc;");
										
										//Print Query Results
										System.out.println("ISBN | Title | Genre | Date Published | Publisher | Edition Number | Description");
										while(rset.next())
										{
											System.out.println(rset.getString(1) + " | " + rset.getString(2) + " | " + rset.getString(3) + " | " + rset.getDate(4) + " | " + rset.getString(5) + " | " + rset.getInt(6) + " | " + rset.getString(7));
										}
										System.out.println();
										System.out.println();
										//Close Results
										rset.close();
									}
									catch(SQLException e)
									{
										System.out.println("Error Retrieving Row: " + e.toString());
									}
									//Close Statement
									stmt.close();
								}
								catch(SQLException e)
								{
									e.printStackTrace();
								}
							break;
							case "2":
								//Open PreparedStatement
								try(PreparedStatement pStmt = conn.prepareStatement("select BOOK.title, AUTHOR.first_name, AUTHOR.middle_name, AUTHOR.last_name from AUTHOR natural join BOOK_AUTHOR natural join BOOK where BOOK.title = ?"))
								{
									try 
									{
										boolean run = true;
										String helper;
										do
										{
											//Take Input
											System.out.println("What is the name of the book you would like to look up?");
											sc.nextLine();
											validate = sc.nextLine();
											
											//Validate
											System.out.println("Book Name: " + validate);
											System.out.println("Is this correct? (Y/n)");
											helper = sc.next();
											
											if(helper.equals("Y") || helper.equals("y"))
											{
												run = false;
											}
										} while (run);
										
										//Execute Query
										pStmt.setString(1, validate);
										ResultSet rset = pStmt.executeQuery();
										
										//List Results
										System.out.println("Title | Author First Name | Author Last Name");
										while(rset.next())
										{
											System.out.println(rset.getString(1) + " | " + rset.getString(2) + " | " + rset.getString(3) + " | " + rset.getString(4));
										}
										System.out.println();
										System.out.println();
										rset.close();
									}
									catch(SQLException e)
									{
										System.out.println("Error Retrieving Row: " + e.toString());
									}
									pStmt.close();
								}
								catch(SQLException e)
								{
									e.printStackTrace();
								}
							break;
							case "3":
								//Open Statement
								try (Statement stmt = conn.createStatement();)
								{
									try
									{
										//Execute Query
										ResultSet rset = stmt.executeQuery("select COPY.ISBN, BOOK.title, COPY.barcode from COPY natural join BOOK order by COPY.barcode asc");
										//Print Query Results
										System.out.println("ISBN | Title | Barcode");
										while(rset.next())
										{
											System.out.println(rset.getString(1) + " | " + rset.getString(2) + " | " + rset.getString(3));
										}
										//Close Results
										rset.close();
									}
									catch(SQLException e)
									{
										System.out.println("Error Retrieving Row: " + e.toString());
									}
									System.out.println();
									System.out.println();
									//Close Statement
									stmt.close();
								}
								catch(SQLException e)
								{
									e.printStackTrace();
								}
							break;
							case "4":
								//Open Statement
								try (Statement stmt = conn.createStatement();)
								{
									try
									{
										//Execute Query
										ResultSet rset = stmt.executeQuery("select MEMBER.card_no, MEMBER.first_name, MEMBER.middle_name, MEMBER.last_name from MEMBER order by MEMBER.card_no asc");
										//Print Results
										System.out.println("Card Number | First Name | Middle Name | Last Name");
										while(rset.next())
										{
											System.out.println(rset.getString(1) + " | " + rset.getString(2) + " | " + rset.getString(3) + " | " + rset.getString(4));
										}
										System.out.println();
										System.out.println();
										//Close Results
										rset.close();
									}
									catch(SQLException e)
									{
										System.out.println("Error: " + e.toString());
									}
									//Close Statement
									stmt.close();
								}
								catch(SQLException e)
								{
									e.printStackTrace();
								}
							break;
							case "5":
								//Open PreparedStatement
								try (PreparedStatement pStmt = conn.prepareStatement("select sel.ISBN, sel.title, sel.barcode, sel.date_borrowed, sel.renewals_no from ((select BOOK.ISBN, BOOK.title, BORROW.barcode, BORROW.date_borrowed, BORROW.renewals_no from BOOK natural join COPY natural join BORROW natural join MEMBER where MEMBER.card_no = ? and BORROW.date_returned is null and BORROW.renewals_no = 0 and datediff(NOW(), BORROW.date_borrowed) > 14) union (select BOOK.ISBN, BOOK.title, BORROW.barcode, BORROW.date_borrowed, BORROW.renewals_no from BOOK natural join COPY natural join BORROW natural join MEMBER where MEMBER.card_no = ? and BORROW.date_returned is null and BORROW.renewals_no = 1 and datediff(NOW(), BORROW.date_borrowed) > 28) union (select BOOK.ISBN, BOOK.title, BORROW.barcode, BORROW.date_borrowed, BORROW.renewals_no from BOOK natural join COPY natural join BORROW natural join MEMBER where MEMBER.card_no = ? and BORROW.date_returned is null and BORROW.renewals_no = 2 and datediff(NOW(), BORROW.date_borrowed) > 42)) sel");)
								{
									try
									{
										boolean run = true;
										String helper;
										do
										{
											//Take Input
											System.out.println("Please Enter the Member Card ID you would like to search");
											validate = sc.next();
											
											//Validate
											System.out.println("Member ID: " + validate);
											System.out.println("Is this correct? (Y/n)");
											helper = sc.next();
											
											if(helper.equals("Y") || helper.equals("y"))
											{
												run = false;
											}
										} while(run);

										pStmt.setString(1, validate);
										pStmt.setString(2, validate);
										pStmt.setString(3, validate);
										//Execute Query
										ResultSet rset = pStmt.executeQuery();
										//Print Results
										System.out.println("ISBN | Title | Barcode | Date Borrowed | Renewals");
										while(rset.next()) 
										{
											System.out.println(rset.getString(1) + " | " + rset.getString(2) + " | " + rset.getString(3) + " | " + rset.getDate(4) + " | " + rset.getInt(5));
										}
										System.out.println();
										System.out.println();
										//Close Results
										rset.close();
									}
									catch(SQLException e)
									{
										System.out.println("Error Retrieving Row: " + e.toString());
									}
									//Close PreparedStatement
									pStmt.close();
								}
								catch(SQLException e)
								{
									e.printStackTrace();
								}
							break;
							case "6":
								//Open PreparedStatement
								try (PreparedStatement pStmt = conn.prepareStatement("update BORROW set date_returned = NOW() where barcode = ? and date_borrowed in (select tOut from (select max(date_borrowed) as tOut from BORROW where card_no = ?) tRes)");)
								{
									try
									{
										boolean run = false;
										String helper, helper2;
										do
										{
											//Take Input
											System.out.println("What is the Card Number of the Member?:");
											helper = sc.next();
											System.out.println("What is the Barcode of the Book to be updated?:");
											validate = sc.next();
											
											//Validate
											System.out.println("Member ID: " + helper);
											System.out.println("Barcode Number: " + validate);
											System.out.println("Is this correct? (Y/n)");
											helper2 = sc.next();
											
											if(helper2.equals("Y") || helper2.equals("y"))
											{
												run = false;
											}
										} while (run);

										//Execute Update
										pStmt.setString(1, validate);
										pStmt.setString(2, helper);
										pStmt.executeUpdate();
										System.out.println("Update Sent, Row will be Udpated if Information Matches");
										System.out.println();
										System.out.println();
									}
									catch(SQLException e)
									{
										System.out.println("Error Updating Row: " + e.toString());
									}
									//Close PreparedStatement
									pStmt.close();
								}
								catch(SQLException e)
								{
									e.printStackTrace();
								}
							break;
							case "7":
								//Open PreparedStatement
								try (PreparedStatement pStmt = conn.prepareStatement("insert into BORROW values(?,?,NOW(),null,0, 0)");)
								{
									try 
									{
										boolean run = true;
										String helper, helper2;
										do
										{
											//Take input
											System.out.println("What is the Card Number of the Member?");
											validate = sc.next();
											System.out.println("What is the Barcode of the Book being Checked Out?");
											helper = sc.next();
											
											//Validate
											System.out.println("Member ID: " + validate);
											System.out.println("Barcode Number: " + helper);
											System.out.println("Is this correct? (Y/n)");
											helper2 = sc.next();
											
											if(helper2.equals("Y") || helper2.equals("y"))
											{
												run = false;
											}
										} while(run);

										//Execute Insert
										pStmt.setString(1, validate);
										pStmt.setString(2, helper);
										pStmt.executeUpdate();
										System.out.println("Row Inserted");

									}
									catch(SQLException e)
									{
										System.out.println("Error Inserting Row: " + e.toString());
									}
									System.out.println();
									System.out.println();
									//Close PreparedStatement
									pStmt.close();
								}
								catch(SQLException e)
								{
									e.printStackTrace();
								}
							break;
							case "8":
								//Open PreparedStatement
								try(PreparedStatement pStmt = conn.prepareStatement("update BORROW set renewals_no = renewals_no + 1 where barcode = ? and date_borrowed in (select tOut from (select max(date_borrowed) as tOut from BORROW where card_no = ?) tRes)");)
								{
									try 
									{
										boolean run = true;
										String helper, helper2;
										do
										{
											//Take Input
											System.out.println("What is the Card Number of the Member?");
											validate = sc.next();
											System.out.println("What is the Barcode of the Book being Renewed?");
											helper = sc.next();	
											
											//Validate
											System.out.println("Member ID: " + validate);
											System.out.println("Barcode Number: " + helper);
											System.out.println("Is this correct? (Y/n)");
											helper2 = sc.next();
											
											if(helper2.equals("Y") || helper2.equals("y"))
											{
												run = false;
											}
										} while (run);

										//Execute Update
										pStmt.setString(1, validate);
										pStmt.setString(2, helper);
										pStmt.executeUpdate();
										System.out.println("Update Sent, Row will be Udpated if Information Matches");
									}
									catch(SQLException e)
									{
										System.out.println("Error: " + e.toString());
									}
									System.out.println();
									System.out.println();
									//Close PreparedStatement
									pStmt.close();
								}
								catch(SQLException e)
								{
									e.printStackTrace();
								}
							break;
							case "9":
								//Open PreparedStatement
								try(PreparedStatement pStmt = conn.prepareStatement("select sel.card_no, sum(sel.MoneyOwed) as MoneyOwed from ((select MEMBER.card_no, (datediff(NOW(), BORROW.date_borrowed) - 14) * 0.25 as MoneyOwed from BOOK natural join COPY natural join BORROW natural join MEMBER where MEMBER.card_no = ? and (BORROW.paid = 0 or BORROW.paid is null) and BORROW.date_returned is null and BORROW.renewals_no = 0 and datediff(NOW(), BORROW.date_borrowed) > 14) union (select MEMBER.card_no, (datediff(NOW(), BORROW.date_borrowed) - 28) * 0.25 as MoneyOwed from BOOK natural join COPY natural join BORROW natural join MEMBER where MEMBER.card_no = ? and (BORROW.paid = 0 or BORROW.paid is null) and BORROW.date_returned is null and BORROW.renewals_no = 1 and datediff(NOW(), BORROW.date_borrowed) > 28) union (select MEMBER.card_no, (datediff(NOW(), BORROW.date_borrowed) - 42) * 0.25 as MoneyOwed from BOOK natural join COPY natural join BORROW natural join MEMBER where MEMBER.card_no = ? and (BORROW.paid = 0 or BORROW.paid is null) and BORROW.date_returned is null and BORROW.renewals_no = 2 and datediff(NOW(), BORROW.date_borrowed) > 42)) sel group by sel.card_no");)
								{
									try
									{
										//Take Input
										boolean run = true;
										String helper1;
										do
										{
											System.out.println("What is the Member ID?");
											validate = sc.next();
											
											//Validate
											System.out.println("Member ID: " + validate);
											System.out.println("Is this correct? (Y/n)");
											helper1 = sc.next();
											
											if(helper1.equals("Y") || helper1.equals("y"))
											{
												run = false;
											}
										} while (run);
										
										//Execute Query
										pStmt.setString(1, validate);
										pStmt.setString(2, validate);
										pStmt.setString(3, validate);
										ResultSet rset = pStmt.executeQuery();
										
										//Print Results
										System.out.println("Member ID | Total Owed to Library");
										while(rset.next())
										{
											System.out.println(rset.getString(1)+ " | " + rset.getString(2));
										}
										//Close ResultSet
										rset.close();
									}
									catch(SQLException e)
									{
										System.out.println("Error Retrieving Row: " + e.toString());
									}
									System.out.println();
									System.out.println();
									//Close PreparedStatement
									pStmt.close();
								}
								catch(SQLException e)
								{
									e.printStackTrace();
								}
							break;
							case "10":
								//Open PreparedStatement
								try(PreparedStatement pStmt = conn.prepareStatement("select sel.ISBN, sel.title, sel.barcode, sel.date_borrowed, sel.date_returned, sel.MoneyOwed from ((select BOOK.ISBN, BOOK.title, BORROW.barcode, BORROW.date_borrowed, BORROW.date_returned, (datediff(NOW(), BORROW.date_borrowed) - 14) * 0.25 as MoneyOwed from BOOK natural join COPY natural join BORROW natural join MEMBER where MEMBER.card_no = ? and (BORROW.paid = 0 or BORROW.paid is null) and BORROW.date_returned is null and BORROW.renewals_no = 0 and datediff(NOW(), BORROW.date_borrowed) > 14) union (select BOOK.ISBN, BOOK.title, BORROW.barcode, BORROW.date_borrowed, BORROW.date_returned, (datediff(NOW(), BORROW.date_borrowed) - 28) * 0.25 as MoneyOwed from BOOK natural join COPY natural join BORROW natural join MEMBER where MEMBER.card_no = ? and (BORROW.paid = 0 or BORROW.paid is null) and BORROW.date_returned is null and BORROW.renewals_no = 1 and datediff(NOW(), BORROW.date_borrowed) > 28) union (select BOOK.ISBN, BOOK.title, BORROW.barcode, BORROW.date_borrowed, BORROW.date_returned, (datediff(NOW(), BORROW.date_borrowed) - 42) * 0.25 as MoneyOwed from BOOK natural join COPY natural join BORROW natural join MEMBER where MEMBER.card_no = ? and (BORROW.paid = 0 or BORROW.paid is null) and BORROW.date_returned is null and BORROW.renewals_no = 2 and datediff(NOW(), BORROW.date_borrowed) > 42)) sel");)
								{
									try
									{
										boolean run = true;
										String helper;
										do
										{
											//Take Input
											System.out.println("What is the card number of the Member?");
											validate = sc.next();
											System.out.println("Member ID: " + validate);
											System.out.println("Is this correct? (Y/n)");
											helper = sc.next();
											
											//Validate
											if(helper.equals("Y") || helper.equals("y"))
											{
												run = false;
											}
										} while (run);
										
										//Execute Query
										pStmt.setString(1, validate);
										pStmt.setString(2, validate);
										pStmt.setString(3, validate);
										ResultSet rset = pStmt.executeQuery();
										
										//Print Results
										System.out.println("ISBN | Title | Barcode | Date Borrowed | Renewals | Money Owed");
										while(rset.next())
										{
											System.out.println(rset.getString(1) + " | " + rset.getString(2) + " | " + rset.getString(3) + " | " + rset.getDate(4) + " | " + rset.getString(5) + " | " + rset.getString(6));
										}
										System.out.println();
										System.out.println();
										//Close Results
										rset.close();
									}
									catch(SQLException e)
									{
										
									}
									System.out.println();
									System.out.println();
									//Close PreparedStatement
									pStmt.close();
								}
								catch(SQLException e)
								{
									e.printStackTrace();
								}
							break;
							case "11":
								//Open PreparedStatement
								try(PreparedStatement pStmt = conn.prepareStatement("update BORROW set paid = 1 where barcode = ? and (paid = 0 or paid is null) and date_borrowed in (select tOut from (select max(date_borrowed) as tOut from BORROW where card_no = ?) tRes)");)
								{
									try
									{
										boolean run = true;
										String helper, helper2;
										//Take Input
										do
										{
											System.out.println("What is the Barcode of the Book?");
											validate = sc.next();
											System.out.println("What is the ID of the Member?");
											helper = sc.next();
											
											//Verify
											System.out.println("Barcode: " + validate);
											System.out.println("Member ID: " + helper);
											System.out.println("Is this correct? (Y/n)");
											helper2 = sc.next();
											
											if(helper2.equals("Y") || helper2.equals("y"))
											{
												run = false;
											}
										} while (run);
										
										//Execute Update
										pStmt.setString(1, validate);
										pStmt.setString(2, helper);
										pStmt.executeUpdate();
										
										System.out.println("Row Updated");
									}
									catch(Exception e)
									{
										System.out.println("Error Updating Row: " + e.toString());
									}
									//Close PreparedStatement
									System.out.println();
									System.out.println();
									pStmt.close();
								}
								catch(SQLException e)
								{
									e.printStackTrace();
								}
							break;
							case "12":
								System.out.println("Exiting Program");
								runAgain = false;
							break;
							default:
								System.out.println("Not a valid input, please select again");
								System.out.println();
								System.out.println();
							break;
						}
					} while(runAgain);
					//Close Scanner
					sc.close();
					//Close Connection
					conn.close();
				}
				catch(SQLException e)
				{
					//Catch Connection Error
					e.printStackTrace();
				}
			}
			catch(ClassNotFoundException e)
			{
				//Catch Missing Driver Error
				e.printStackTrace();
				System.out.println("If you see this error please check if mysql jdbc driver is being compiled");
			}
			
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