package client;
import java.io.*;
import java.util.*;

import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

class BattleShipsClient {
	public static void main(String[] args) {
		String player1 = "_o____s__dd__b_____b_o___b__x__b_o__";
		String player2 = "____so_xo____x_o_______x__bbbbo_____";
		drawGrid(player1,player2);

//		System.out.println("-h\tDisplay help");
//
//		// System.exit(1);
//		// Scanner stdin = new Scanner(System.in);
//		// Scanner stdin = new Scanner(System.in);
//
//		// System.out.println(stdin.next());
//
//		// 
//		// // System.out.println("rrrrrrrruuuuuuuuunnnnnnniiiiiiinnnnnnnnn");
//		// // System.out.flush();
//		// // System.exit(0);
//		// 
//		// for (int i = 0; i < 5;i++ ){
//		// 		try {
//		// 			String str = stdin.next();
//		// 			System.out.println(str);
//		// 		} catch (Exception e) {
//		// 			System.out.println(e.getMessage());
//		// 			System.exit(1);
//		// 		}
//		// 	}
		Comunicator comunicator = new Comunicator();
		comunicator.startClient();
		//Scanner scanner = new Scanner(System.in);
		while (true){
//			System.out.println("Enter a Command: ");
//			String msg = scanner.nextLine();
//			comunicator.sendMessage(msg);
			String msgFromServer = comunicator.receiveMesssage();
			System.out.println("Command Received from Server: "+msgFromServer);
			String response = msgFromServer + "&Reply"; 
			comunicator.sendMessage(response);
			
		}
	}
	public static void drawGrid(String player1,String player2){
		player1 = player1.replace('_',' ');
		player2 = player2.replace('_',' ');
		char letter = 'A';
		String printString="   ";
		for (int j =0;j<2;j++){
			for (int i = 1; i<7;i++){
				printString+=("  "+i+" ");
			}
			printString+=("\t\t   ");
		}
		printString+=("\n   ");
		for (int i =0; i<50;i++){
			printString+=("-");
			if (i==24){
				printString+=("\t\t   ");
			}
		}
		printString+=("\n" + letter + "  ");
		for (int i=0;i<player1.length();i++){
			printString+="| "+ player1.substring(i,i+1) + " ";
			if (i>0&&(i+1)%6==0){
				printString+="|\t\t"+letter + "  ";
				for (int j=i-5;j<=i;j++){
					printString+="| "+ player2.substring(j,j+1) + " ";
				}
				letter++;
				printString+="|\n"+(i<player1.length()-1 ? letter:" ") + "  ";
				
			}
		}

		for (int i =0; i<50;i++){
			printString+=("-");
			if (i==24){
				printString+=("\t\t   ");
			}
		}
		System.out.println(printString);
	}
}