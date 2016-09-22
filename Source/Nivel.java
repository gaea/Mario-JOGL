import java.io.*;
import java.awt.*;
import java.util.*;
import javax.swing.*;


public class Nivel
{
	private Vector enemigos, monedas, bloques;
	private int tiempo, ancho, alto;
	
	public Nivel( String archivo )
    {
    	monedas= new Vector();
		monedas.add( new Coin(400, 400, 40, 40, 5 ));
		monedas.add( new Coin(440, 400, 40, 40, 5 ));
		monedas.add( new Coin(480, 400, 40, 40, 5 ));
		monedas.add( new Coin(520, 400, 40, 40, 5 ));
		monedas.add( new Coin(560, 400, 30, 40, 5 ));

		monedas.add( new Coin(400, 360, 40, 40, 5 ));
		monedas.add( new Coin(400, 320, 40, 40, 5 ));
		monedas.add( new Coin(400, 280, 40, 40, 5 ));
		
		monedas.add( new Coin(440, 360, 40, 40, 5 ));
		monedas.add( new Coin(440, 320, 40, 40, 5 ));
		monedas.add( new Coin(440, 280, 40, 40, 5 ));

		monedas.add( new Coin(480, 360, 40, 40, 5 ));
		monedas.add( new Coin(480, 320, 40, 40, 5 ));
		monedas.add( new Coin(480, 280, 40, 40, 5 ));
		

		monedas.add( new Coin(520, 360, 40, 40, 5 ));
		monedas.add( new Coin(520, 320, 40, 40, 5 ));
		monedas.add( new Coin(520, 280, 40, 40, 5 ));
		
		
		monedas.add( new Coin(560, 360, 40, 40, 5 ));
		monedas.add( new Coin(560, 320, 40, 40, 5 ));
		monedas.add( new Coin(560, 280, 40, 40, 5 ));

		for(int i=0; i<20; i++){
			monedas.add( new Coin(1200+i*40, 180, 40, 40, 5 ));
			monedas.add( new Coin(1200+i*40, 230, 40, 40, 5 ));
			
		}
		
		for(int i=0; i<5; i++){
			monedas.add( new Coin(2470, 250+i*40, 40, 40, 5 ));
			monedas.add( new Coin(2510, 250+i*40, 40, 40, 5 ));
			monedas.add( new Coin(2550, 250+i*40, 40, 40, 5 ));
			
			monedas.add( new Coin(3560, 300+i*40, 40, 40, 5 ));
			monedas.add( new Coin(3600, 300+i*40, 40, 40, 5 ));
			monedas.add( new Coin(3640, 300+i*40, 40, 40, 5 ));
		}
		
		for(int i=0; i<5; i++){
			monedas.add( new Coin(4300+i*40, 300, 40, 40, 5 ));
			monedas.add( new Coin(4820+i*40, 340, 40, 40, 5 ));
			monedas.add( new Coin(2850+i*40, 80, 40, 40, 5 ));
			monedas.add( new Coin(5230+i*40, 80, 40, 40, 5 ));
			monedas.add( new Coin(5230+i*40, 130, 40, 40, 5 ));
		}
		
		bloques= new Vector();
		bloques.add( new Rectangle( 0, 0, 20, 800 ) );
		bloques.add( new Rectangle( 0, 0, 4000+280, 70 ) );
		bloques.add( new Rectangle( 4000+395, 0, 6000-(4000+395), 70 ) );
		bloques.add( new Rectangle( 0, 600, 6000, 20 ) );
		bloques.add( new Rectangle( 6000, 0, 20, 800 ) );

		bloques.add( new Rectangle( 358, 164, 300, 14 ) );
		bloques.add( new Rectangle( 828, 200, 131, 14 ) );
		
		bloques.add( new Rectangle( 2200, 70, 92, 34 ) );
		
				
		bloques.add( new Rectangle( 294+2000, 600-500, 74, 42 ) );
		bloques.add( new Rectangle( 369+2000, 600-454, 59, 32 ) );
		bloques.add( new Rectangle( 425+2000, 600-532, 260, 145 ) );
		
		bloques.add( new Rectangle( 1553+2000, 270, 147, 14 ) );
		bloques.add( new Rectangle( 1515+2000, 163, 85, 14 ) );
		bloques.add( new Rectangle( 1670+2000, 163, 105, 14 ) );
		
		bloques.add( new Rectangle( 1176+2000, 70, 34, 107 ) );
		bloques.add( new Rectangle( 1215+2000, 70, 34, 143 ) );
		
		bloques.add( new Rectangle( 1987+2000, 600-364, 180, 14 ) );
		bloques.add( new Rectangle( 97+4000, 600-535, 185, 77 ) );
		
		
		bloques.add( new Rectangle( 547+4000, 70, 36, 71 ) );
		bloques.add( new Rectangle( 754+4000, 70, 36, 107 ) );
		bloques.add( new Rectangle( 1074+4000, 70, 36, 71 ) );
		bloques.add( new Rectangle( 1129+4000, 70, 36, 107 ) );
		
		
		enemigos= new Vector();
		enemigos.add( new Goomba( 3370, 70, 53, 65, 1 ) );
		enemigos.add( new Goomba( 1800, 70, 53, 65, 1 ) );
		enemigos.add( new Goomba( 800, 70, 53, 65, 1 ) );
		enemigos.add( new Shell( 2840, 70, 69, 65, 2 ) );
		enemigos.add( new Shell( 4880, 70, 69, 65, 2 ) );
		enemigos.add( new Bullet_Bill( 6000, 65, 40, 3 ) );
		enemigos.add( new Bullet_Bill( 5000, 65, 40, 3 ) );
		enemigos.add( new Bullet_Bill( 4000, 65, 40, 3 ) );
		enemigos.add( new Bullet_Bill( 3000, 65, 40, 3 ) );
		enemigos.add( new Boo( 4000, 60, 65, 55, 4 ) );
		enemigos.add( new Boo( 2000, 60, 65, 55, 4 ) );
		
	}
	
	public Vector get_enemigos(){
		return enemigos;
	}
	
	public Vector get_monedas(){
		return monedas;
	}
	
	public Vector get_bloques(){
		return bloques;
	}
}