// http://download.oracle.com/javase/tutorial/networking/sockets/examples/KnockKnockServer.java
import java.io.*;
import java.net.*;

public class KnockKnockClient {
	public static void main(String[] args) throws IOException {

		Socket kkSocket = null;
		PrintWriter out = null;
		BufferedReader in = null;
		int port = 54321;

		String host = (args.length > 0) ? args[0] : "localhost";

		try {
			kkSocket = new Socket(host, port);
			out = new PrintWriter(kkSocket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(kkSocket.getInputStream()));
			System.out.println("Connected to "+ host +" on port "+port);
		} catch (UnknownHostException e) {
			System.err.println("Don't know about host: " + host);
			System.exit(1);
		} catch (IOException e) {
			System.err.println("Couldn't get I/O for the connection to: " + host);
			System.exit(1);
		}

		BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
		String fromServer;
		String fromUser;

		while ((fromServer = in.readLine()) != null) {
			System.out.println("Server: " + fromServer);
			if (fromServer.equals("Bye."))
				break;

			fromUser = stdIn.readLine();
			if (fromUser != null) {
				System.out.println("Client: " + fromUser);
				out.println(fromUser);
			}
		}
		out.close();
		in.close();
		stdIn.close();
		kkSocket.close();
	}
}
