// http://download.oracle.com/javase/tutorial/networking/sockets/examples/KnockKnockServer.java
import java.net.*;
import java.util.Scanner;
import java.io.*;

public class KnockKnockServer {
	public static void main(String[] args) throws IOException {
		
		int port = 54321;	

		ServerSocket serverSocket = null;
		
		try {
			serverSocket = new ServerSocket(port);
			System.out.println("Listenin to port:" +port);
		} catch (IOException e) {
			System.err.println("Could not start listening on port: " + port);
			System.exit(1);
		}

		Socket clientSocket = null;
		try {
			clientSocket = serverSocket.accept();
		} catch (IOException e) {
			System.err.println("Accept failed.");
			System.exit(1);
		}

		PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
		BufferedReader in = new BufferedReader(
			new InputStreamReader(
			clientSocket.getInputStream()));
		String inputLine, outputLine;
		KnockKnockProtocol kkp = new KnockKnockProtocol();

		outputLine = kkp.processInput(null);
		out.println(outputLine);
		Scanner scanner = new Scanner(System.in);
		while (true){
			System.out.println("Insert Command");
			String command = scanner.nextLine();
			out.println(command);
		}
		
//		while ((inputLine = in.readLine()) != null) {
//			System.out.println("Received: "+inputLine); 
//			outputLine = kkp.processInput(inputLine);
//			out.println(outputLine);
//			System.out.println("Sent: "+outputLine);
//			if (outputLine.equals("Bye."))
//				break;
//		}
//		out.close();
//		in.close();
//		clientSocket.close();
//		serverSocket.close();
	}
}