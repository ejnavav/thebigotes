package test;

import org.junit.* ;
import static org.junit.Assert.* ;

public class HelloWorldTest {
	
	@Test
	public void hello_test() {
		HelloWorld hw = new HelloWorld();
		assertEquals("hello world", hw.say());
	}
}
