import java.util.Timer;
import java.util.TimerTask;

public class TimeExample  {
  Timer timer;

  public TimeExample ( int seconds )   {
    timer = new Timer (  ) ;
    timer.schedule ( new ToDoTask (  ) , seconds*1000 ) ;
  }


  class ToDoTask extends TimerTask  {
    public void run (  )   {
      System.out.println ( "OK, It's time to do something!" ) ;
      timer.cancel (  ) ; //Terminate the thread
    }
  }


  public static void main ( String args [  ]  )   {
    System.out.println ( "Schedule something to do in 5 seconds." ) ;
    new TimeExample ( 5 ) ;
    System.out.println ( "Waiting." ) ;
  }
}
