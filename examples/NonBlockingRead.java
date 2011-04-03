import java.io.*;

class NonBlockingRead {
	public static void main(String[] args) {

		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		
		try{
			while(true){
				System.out.println("Waiting");
				if(in.ready()){
					System.out.println("Read line: " + in.readLine());
					System.exit(0);
				}
				Thread.sleep(1000);
			}
		}catch(Exception e){}	
	}
}