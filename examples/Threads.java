public class Threads {
	public static void main(String[] args) {
		Blinker b = new Blinker();
		System.out.println("is alive: "+b.isAlive());
		
		try{Thread.sleep(2000);} catch (Exception e) {};
		
		b.quit();
		
		for (int i=0;i<100;i++ ) {
			// System.out.println("is alive: "+b.isAlive());
			try{Thread.sleep(100);} catch (Exception e) {};
		}
		
		System.out.println("woke up");
		// System.out.println("is alive: "+b.isAlive());
		
		try{Thread.sleep(2000);} catch (Exception e) {};
		
		System.out.println("done");
	}
}