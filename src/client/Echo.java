package client;
import java.io.*;
import java.util.*;
class Echo {
	public static void main(String[] args) {
		// Scanner stdin = new Scanner(System.in);
		// Scanner stdin = new Scanner(new InputStreamReader(System.in));
		// System.out.println(stdin.next());		
		try{
			BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
			System.out.println(stdin.readLine());
			System.out.println("somethhhhhing");

		} catch (Exception e) {
			
		}
	}
}