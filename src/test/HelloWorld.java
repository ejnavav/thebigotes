package test;

public class HelloWorld {

	public static void main(String[] args) {
		HelloWorld hw = new HelloWorld();
		System.out.println(hw.say() + "I'm running");
	}
		
	public String say(){
		return "hello world";
	}
}