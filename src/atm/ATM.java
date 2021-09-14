package atm;
import java.util.Scanner;

public class ATM {

	public static void main(String[] args) {
		
		Scanner sc = new Scanner(System.in);
		
		Bank theBank = new Bank("HDFC");
		
		User aUser = theBank.addUser("charan", "Reddy", "9999");
		
		Account newAccount = new Account("Checking", aUser, theBank);
		aUser.addAccount(newAccount);
		theBank.addAccount(newAccount);
		
		
		User curUser;
		while(true) {
			
			curUser = ATM.mainMenuPromt(theBank, sc);
			
			ATM.printUserMenu(curUser, sc);
			
		}
	}
	
	public static User mainMenuPromt(Bank theBank, Scanner sc) {
		
		String userID;
		String pin;
		User authUser;
		do{
			System.out.printf("\n\n Welcome to %s \n\n",theBank.getName());
			System.out.print("Enter use ID:");
			userID = sc.nextLine();
			System.out.print("Enter pin: ");
			pin=sc.nextLine();
			
			authUser=theBank.userLogin(userID, pin);
			if(authUser==null) {
				System.out.println("Incorrect user ID/pin combination."+ "Please try again");
				
			}
			
		}while(authUser==null);
		
		return authUser;
	}
	
	public static void printUserMenu(User theUser,Scanner sc) {
		theUser.printAccountSummary();
		int choice;
		
		do {
			System.out.printf("Welcome %s\n",theUser.getFirstName());
			System.out.println("1. show transaction history");
			System.out.println("2. withdraw");
			System.out.println("3. deposit");
			System.out.println("4. transfer");
			System.out.println("5. exit");
			System.out.println();
			System.out.println("enter choice");
			choice = sc.nextInt();
			
			if(choice <1 || choice >5) {
				System.out.println("wrong choice. enter again");
			}
		}while(choice<1 || choice>5);
		
		switch(choice) {
		
		case 1:
			ATM.showTransHistory(theUser, sc);
			break;
		case 2:
			ATM.withdrawFunds(theUser, sc);
			break;
		case 3:
			ATM.depositFunds(theUser, sc);
			break;
		case 4:
			ATM.transferFunds(theUser, sc);
			break;
		}
		
		if(choice!=5) {
			ATM.printUserMenu(theUser, sc);
		}
	}
	
	public static void showTransHistory(User theUser, Scanner sc) {
		int theAcct;
		do {
			System.out.printf("enter the number(1-%d) of the acc"+ 
					"whose trans u want to see: ",theUser.numAccounts());
			theAcct = sc.nextInt()-1;
			if(theAcct < 0 || theAcct >= theUser.numAccounts()) {
				System.out.println("Invalid account try again");
			}
		}while(theAcct < 0 || theAcct >= theUser.numAccounts());
		theUser.printAcctTransHistory(theAcct);
	}
	public static void transferFunds(User theUser, Scanner sc) {
		
		int fromAcct;
		int toAcct;
		double amount;
		double acctBal;
		do {
			System.out.printf("enter the number (1-%d) of the accounts \n"+
								"to transfer from: ",theUser.numAccounts());
			fromAcct = sc.nextInt()-1;
			if(fromAcct < 0 || fromAcct >=theUser.numAccounts()) {
				System.out.println("invalid account try again");
			}
		}while(fromAcct < 0 || fromAcct >= theUser.numAccounts());
		acctBal = theUser.getAcctBalance(fromAcct);
		
		// get the acc to transfer to
		
		do {
			System.out.printf("enter the number (1-%d) of the accounts \n"+
								"to transfer to: ",theUser.numAccounts());
			toAcct = sc.nextInt()-1;
			if(toAcct < 0 || toAcct >=theUser.numAccounts()) {
				System.out.println("invalid account try again");
			}
		}while(toAcct < 0 || toAcct >= theUser.numAccounts());
		//get the amount transfer from
		do {
			System.out.printf("enter the amount to transfer (max rs%.02f): rs",acctBal);
			amount = sc.nextDouble();
			if(amount<0) {
				System.out.println("Amount must be greater than 0");
				
			}else if (amount>acctBal) {
				System.out.printf("amount must not be greater than\n"
						+ "balance of rs%.02f\n",acctBal);
			}
		}while(amount<0 || amount> acctBal);
		theUser.addAcctTransaction(fromAcct,-1*amount,String.format("Transfer to account %s", theUser.getAcctUUID(toAcct)));
		
		theUser.addAcctTransaction(toAcct,amount,String.format("Transfer to account %s", theUser.getAcctUUID(fromAcct)));

	}
	public static void withdrawFunds(User theUser, Scanner sc) {

		int fromAcct;
		double amount;
		double acctBal;
		String memo;
		do {
			System.out.printf("enter the number (1-%d) of the accounts \n"+
								"to withdraw from: ",theUser.numAccounts());
			fromAcct = sc.nextInt()-1;
			if(fromAcct < 0 || fromAcct >=theUser.numAccounts()) {
				System.out.println("invalid account try again");
			}
		}while(fromAcct < 0 || fromAcct >= theUser.numAccounts());
		acctBal = theUser.getAcctBalance(fromAcct);
		
		//-----------------------------
		
		do {
			System.out.printf("enter the amount to transfer (max rs%.02f): rs",acctBal);
			amount = sc.nextDouble();
			if(amount<0) {
				System.out.println("Amount must be greater than 0");
				
			}else if (amount>acctBal) {
				System.out.printf("amount must not be greater than\n"
						+ "balance of rs%.02f\n",acctBal);
			}
		}while(amount<0 || amount> acctBal);
		sc.nextLine();
		
		System.out.println("enter a memeo: ");
		memo = sc.nextLine();
		theUser.addAcctTransaction(fromAcct, -1*amount, memo);
	}
	
	public static void depositFunds(User theUser,Scanner sc) {
		
		int toAcct;
		double amount;
		double acctBal;
		String memo;
		do {
			System.out.printf("enter the number (1-%d) of the accounts \n"+
								"to deposit in: ",theUser.numAccounts());
			toAcct = sc.nextInt()-1;
			if(toAcct < 0 || toAcct >=theUser.numAccounts()) {
				System.out.println("invalid account try again");
			}
		}while(toAcct < 0 || toAcct >= theUser.numAccounts());
		acctBal = theUser.getAcctBalance(toAcct);
		
		//-----------------------------
		
		do {
			System.out.printf("enter the amount to transfer (max rs%.02f): rs",acctBal);
			amount = sc.nextDouble();
			if(amount<0) {
				System.out.println("Amount must be greater than 0");
				
			}
		}while(amount<0);
		sc.nextLine();
		
		System.out.println("enter a memeo: ");
		memo = sc.nextLine();
		theUser.addAcctTransaction(toAcct,amount, memo);
	}
	
}
