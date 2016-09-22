import java.awt.event.*;
import javax.swing.*;
import java.awt.*;
import java.awt.Toolkit;
import java.awt.Dimension;

class Splash extends JWindow implements ActionListener
{
	public boolean begin_game;
	private	JLabel image;
	private	JPanel pane= new JPanel();
	private JButton play= new JButton( "JUGAR" );
	private JButton exit= new JButton( "SALIR" );
	private Sound sonidoJuego = new Sound("../sonidos/menu.mid");
	private JButton instructions= new JButton( "INSTRUCCIONES" );
	private String error = "";

	public Splash( String filename, Frame frame, Dimension screenSize )
	{
		super( frame );
		
		image = new JLabel( new ImageIcon(filename) );
		Dimension labelSize = image.getPreferredSize();

		play.addActionListener( this );
		instructions.addActionListener( this );
		exit.addActionListener( this );

		pane.add( play, BorderLayout.WEST );
		pane.add( instructions, BorderLayout.WEST );
		pane.add( exit, BorderLayout.EAST );

		getContentPane().add( image, BorderLayout.CENTER );
		getContentPane().add( pane, BorderLayout.SOUTH );
		pack();

		setLocation( screenSize.width/2 - (labelSize.width/2),	screenSize.height/2 - (labelSize.height/2) );
		setVisible(true);
		
		if(sonidoJuego!= null){ sonidoJuego.task("loop");}
		else{ error = "no se pudo cargar el sonido";}
		
		while( begin_game == false )
		{System.out.println( error );
		}

	    this.setVisible( false );
		sonidoJuego.task("stop");
	}
	
	public void actionPerformed( ActionEvent e )
	{
		if( e.getSource() == play )
		begin_game= true;

		if( e.getSource() == instructions )
		JOptionPane.showMessageDialog( null, "=> :Mover Derecha\n<= :Mover Izquierda\nm :Saltar\nespacio :Correr", "Instrucciones", 1 );
		
		if( e.getSource() == exit )
		new Thread(new Runnable()	{	public void run(){ System.exit(0); }}).start();
	}
}