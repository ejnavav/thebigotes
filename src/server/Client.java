package server;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
	Socket socket;
	PrintWriter out;
	BufferedReader in;
	private String type;

	public Board board;
	public boolean isReady= false;
	public boolean hasTurn = false;
	ClientListener clientListener;
	String name;
	
	public Client(Socket socket) {
		try {
			out = new PrintWriter(socket.getOutputStream(), true);
		} catch (Exception e) {

		}
		clientListener = new ClientListener(this,socket);
		clientListener.start();
		System.out.println("After Thread Start");
		
		this.board = new Board();
	}
	
	public String getName(){return name;}
	
	public void setName(String name){this.name=name;}
	
	public void sendCommand(String command) throws Exception {
		out.println(command);
	}
	public void addShip(String shipType,String orientation, String position )throws Exception{
		try{
			board.placeShip(shipType,orientation,position);
		}catch(Exception e){
			//TODO Create Proper exception
			e.printStackTrace();
			throw e;
		}
		
	}
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	public Socket getSocket(){
		return this.socket;
	}
	
	public void kill(){
		clientListener.kill();
		clientListener.interrupt();
		try {
			this.finalize();
		}catch(Throwable e){
			
		}
		
	}
}
