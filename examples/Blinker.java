public class Blinker extends Thread {
	private boolean keepRunning = true;
	
	public Blinker(){	
		this.start();
	}	
	public void run(){
		try{
			while(keepRunning){
				System.out.print("@");
				Thread.sleep(100);
			}
		}catch (Exception e){}
	}
	
	public void quit(){
		System.out.println("quit called");
		keepRunning = false;
	}
	
	public static void main(String[] args) {
		new Blinker();
	}
}