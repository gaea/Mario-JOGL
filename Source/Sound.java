import java.applet.Applet; 
import java.applet.AudioClip; 
import java.net.URL; 

public class Sound extends Thread
{ 
	AudioClip audioClip;
	URL url;
	String action;

	public Sound(String file )
	{
		try { 
			url = new URL ( "file:"+file ) ; 
			audioClip = Applet.newAudioClip ( url ) ;
		    }

		catch ( Exception e ) {	System.out.println ( e.toString() ); }
	}

	public void task( String action )
	{
		this.action=action;
		run();
	}

	public void run( )
	{
		if(action.equals("play")) {audioClip.play();}
		if(action.equals("stop")) {audioClip.stop();}
		if(action.equals("loop")) {audioClip.loop();}
	}
}
