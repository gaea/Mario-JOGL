import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import com.sun.opengl.util.FPSAnimator;
import javax.media.opengl.GLCanvas;
import com.sun.opengl.util.Animator;

public class Ventana extends JFrame
{
	public Ventana()
    {
    	super("Juego OpenGL");
	
    	Dimension dimensionPantalla = (Toolkit.getDefaultToolkit()).getScreenSize();

		//Splash splash= new Splash( "../Imagenes/Juego/Splash.gif", this, dimensionPantalla );	   	
		//splash= null;
		
		int altura = ((int)dimensionPantalla.getHeight())-100;
		int anchura = ((int)dimensionPantalla.getWidth())-100;
		
		Juego juego= new Juego( );
		
		GLCanvas canvas= new GLCanvas();
		canvas.addGLEventListener( juego );
		
		final FPSAnimator animator= new FPSAnimator( canvas, 120 );
		animator.start();
		
		this.addKeyListener( juego.retornar_manejador_teclado() );
		canvas.addKeyListener( juego.retornar_manejador_teclado() );
		
		JPanel panelDibujo = new JPanel( new BorderLayout() );
		panelDibujo.add( canvas, BorderLayout.CENTER );
		
		Container contenedor = getContentPane();
		contenedor.add( panelDibujo, BorderLayout.CENTER );
		
		
		this.addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent e)
	        {
				new Thread(new Runnable()	{	public void run(){ System.exit(0); }}).start();
			}
		});
		
		this.setSize( 800, 600 );
		this.setLocation( dimensionPantalla.width/2 - 400,	dimensionPantalla.height/2 - 300 );
		this.setResizable( false );
		this.setVisible( true ); 

	}
    	
	public static void main(String[] args)
    {	new Ventana();	}
}