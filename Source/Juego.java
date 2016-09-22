import java.io.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.Vector;
import java.awt.Rectangle;

import javax.media.opengl.*;
import javax.media.opengl.glu.*;
import com.sun.opengl.util.GLUT;
import com.sun.opengl.util.texture.*;

public class Juego implements GLEventListener
{
	private static GL gl;
	private static GLU glu;
	private static GLUT glut;
	private Sound sonido_nivel, sonido_coin, sonido_muere, sonido_gana, sonido_life, sonido_alerta, sonido_gameover, sonido_aplastar;
	
	private int lado_colision;
	private int viewport_x, viewport_y;
	private int score, coins, lives, level, time;
	
	private Nivel nivel_actual;
	private Personaje personaje;
	private boolean gana = false;
	private int	fin = 0;

	private Texture []mario_texturas;
	private Texture []enemigos_texturas;
	private Texture bloque_textura, gameover_textura, fondo1_textura, fondo2_textura, fondo3_textura, coin_textura;
	private Vector enemigos, monedas, bloques;
	private Manejador_Teclado manejador_teclado;
	private long timer;

	public Juego( )
	{
		lives = 3;
		time = 100;
		level = 1;
		nivel_actual= new Nivel( "../Niveles/nivel1.txt" );
		personaje= new Personaje( 90, 80, 70, 90 );
		monedas = nivel_actual.get_monedas();
		enemigos = nivel_actual.get_enemigos();
		bloques = nivel_actual.get_bloques();
		manejador_teclado= new Manejador_Teclado();
		timer= System.currentTimeMillis();
	}

	public Manejador_Teclado retornar_manejador_teclado( )
	{ return manejador_teclado; }

	public void displayChanged( GLAutoDrawable drawable, boolean modeChanged, boolean deviceChanged ){}
	
	public void init( GLAutoDrawable drawable )
    {
		gl = drawable.getGL();
		gl.glEnable(GL.GL_TEXTURE_2D);
		gl.glEnable (GL.GL_BLEND);		
		gl.glBlendFunc (GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
		gl.glTexEnvi(GL.GL_TEXTURE_ENV, GL.GL_TEXTURE_ENV_MODE, GL.GL_REPLACE);
	
		gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		
		cargar_texturas();
		cargar_sonidos();
		sonido_nivel.task("loop");
    }

	public void reshape( GLAutoDrawable drawable, int x, int y, int width, int height )
    {
		gl.glMatrixMode( GL.GL_PROJECTION );  
		gl.glLoadIdentity(); 

		glu= new GLU();
		glut= new GLUT();

		glu.gluOrtho2D( 0.0, 6020, 0.0, 600 );
    }

    public void display(GLAutoDrawable drawable)
    {	
    	if(fin == 0){
    	
			mover_camara( personaje.get_velocidad() );
			gl.glViewport(viewport_x, viewport_y, 6020, 600 );
			gl.glClear(GL.GL_COLOR_BUFFER_BIT);
			pintar_fondo();
			gl.glColor3f(0.8f, 0.3f, 0.0f);
	
			/*for( int i=0; i<bloques.size(); i++ )
			{
				dibujar_rectangulo( (int)(( Rectangle )bloques.get(i)).x, (int)(( Rectangle )bloques.get(i)).y, (int)(( Rectangle )bloques.get(i)).width, (int)(( Rectangle )bloques.get(i)).height );
			}*/
			
			if( personaje.get_vitalidad() && !gana )
			{
				if(System.currentTimeMillis() - timer > 1000){
					time--;
					timer=System.currentTimeMillis();
				}
					
				personaje.desplazar( bloques ); 
				personaje.saltar( bloques );
				
				if(time == 50){
					sonido_alerta.task("play");
				}
				
				if(personaje.get_X() > 5670 && !gana){
					//gano!!!!!!!!!!!!!!!!!!
					sonido_gana.task("play");
					sonido_nivel.task("stop");
					gana = true;
				}
				
				if(personaje.get_Y() < 0 || time < 1){
					personaje.set_vitalidad(false);
					sonido_muere.task("play");
					sonido_nivel.task("stop");
					personaje.set_sprite( 5 );
					personaje.animacion_muere();
					reiniciar();
				}
	
				colision_coins();
				colision_enemys();			
			}
			else
			{
				if(!gana){
					personaje.set_sprite( 5 );
					personaje.animacion_muere();
					reiniciar();
				}
			}
	
			dibujar_textura( personaje.get_X(), personaje.get_Y(), mario_texturas[personaje.get_sprite()], personaje.get_direccion() );
			gl.glColor3f(0.4f, 0.4f, 0.8f);
			dibujar_panel();

    	}
    	else{
    		gl.glViewport(0, 0, 6020, 600 );
    		dibujar_textura(0, 0, gameover_textura, true);
			gl.glColor3f(1.0f, 1.0f, 1.0f);
			try{
				Thread.sleep(3000);
			}
			catch(Exception e){
				
			}
    		dibujar_texto("Perdiste !!!!!!! ", 320, 40);
    		//sonido_gameover.task("play");
    	}
		
		gl.glFlush();
    }

	public void mover_camara( int velocidad )
	{
		double distancia= Math.abs(viewport_x-personaje.get_X());
		
		if(  !personaje.get_direccion() && viewport_x < 0 && personaje.get_desplazar() && 
				personaje.get_X() < 5700 && personaje.get_vitalidad() && 
					personaje.validar_movimiento( bloques, personaje.get_X() - velocidad, personaje.get_Y() )){
			viewport_x+= velocidad;
		}
		if( personaje.get_direccion() && personaje.get_desplazar() && personaje.get_X() > 300 && 
				personaje.get_X() < 5700 && personaje.get_vitalidad() && 
					personaje.validar_movimiento( bloques, personaje.get_X() + velocidad, personaje.get_Y() )){
			viewport_x-= velocidad;
		}
	}

	public void colision_coins( )
	{
		for( int i=0; i<monedas.size(); i++ )
		{
			dibujar_textura( ((Entidad)monedas.get(i)).x, ((Entidad)monedas.get(i)).y, coin_textura, true );

			if( (personaje.get_area_personaje()).intersects( ((Entidad)monedas.get(i)).get_area() ) )
			{
				monedas.removeElementAt( i );
				sonido_coin.task("play");
				score+=100;
				coins++;
				if(coins%100 == 0){
					lives++;
					coins=0;
					sonido_life.task("play");
				}
			}
		}
	}
	
	public void colision_enemys( )
	{
		for( int i=0; i<enemigos.size(); i++ )
		{
			
			int tipo= ((Entidad)enemigos.get(i)).tipo;
			
			switch( tipo )
			{
				case 1:
						((Goomba)enemigos.get(i)).comportamiento( bloques );
						dibujar_textura( ((Entidad)enemigos.get(i)).x, ((Entidad)enemigos.get(i)).y, enemigos_texturas[0], ((Goomba)enemigos.get(i)).get_direccion() );
	
						if( (personaje.get_area_personaje()).intersects( ((Entidad)enemigos.get(i)).get_area() ) )
						{
							lado_colision= tipo_colision( (personaje.get_area_personaje()).intersection( ((Entidad)enemigos.get(i)).get_area() ) );
			
							if( lado_colision==1 )
							{	enemigos.removeElementAt( i ); 
								sonido_aplastar.task("play");
							}
							
							if( lado_colision==-1 )
							{
								personaje.set_vitalidad( false );
								sonido_nivel.task("stop");
								sonido_muere.task("play");
							}
						}
				break;
				
				case 2:					
						((Shell)enemigos.get(i)).comportamiento( bloques );
						dibujar_textura( ((Entidad)enemigos.get(i)).x, ((Entidad)enemigos.get(i)).y, enemigos_texturas[1], ((Shell)enemigos.get(i)).get_direccion() );
	
						if( (personaje.get_area_personaje()).intersects( ((Entidad)enemigos.get(i)).get_area() ) )
						{
								personaje.set_vitalidad( false );
								sonido_nivel.task("stop");
								sonido_muere.task("play");
						}
				break;
				
				case 3:
						((Bullet_Bill)enemigos.get(i)).comportamiento();
						dibujar_textura( ((Entidad)enemigos.get(i)).x, ((Entidad)enemigos.get(i)).y, enemigos_texturas[2], true );
	
	
						if( (personaje.get_area_personaje()).intersects( ((Entidad)enemigos.get(i)).get_area() ) )
						{
							lado_colision= tipo_colision( ((Entidad)enemigos.get(i)).get_area() );
			
							if( lado_colision==1 )
							{	
								enemigos.removeElementAt( i );
								enemigos.add( new Bullet_Bill( 6000, 65, 40, 3 ) );
								sonido_aplastar.task("play");
							}
								
							if( lado_colision==-1 )
							{
								personaje.set_vitalidad( false );
								sonido_nivel.task("stop");
								sonido_muere.task("play");
							}
						}
				break;
				
				case 4:
						((Boo)enemigos.get(i)).comportamiento();
						dibujar_textura( ((Entidad)enemigos.get(i)).x, ((Entidad)enemigos.get(i)).y, enemigos_texturas[3], ((Boo)enemigos.get(i)).get_direccion() );
						
						if( (personaje.get_area_personaje()).intersects( ((Entidad)enemigos.get(i)).get_area() ) )
						{
							personaje.set_vitalidad( false );
							sonido_nivel.task("stop");
							sonido_muere.task("play");					
						}
				break;
			}
		}
	}

	public int tipo_colision( Rectangle objeto )
	{
		Rectangle interseccion= (personaje.get_area_personaje()).intersection( objeto );
		int col_hor_ver= ((interseccion.width>= interseccion.height)? 1 : -1 );
		
		if( Math.abs((personaje.get_Y()+personaje.get_alto())- objeto.y)< Math.abs(personaje.get_Y()- (objeto.y+objeto.height) )  )
			col_hor_ver= -1;
			
		return col_hor_ver;
	}	

	public void dibujar_textura( float pos_x, float pos_y, Texture texture, boolean voltear_horizontal )
	{
		texture.enable();
		texture.bind();
		TextureCoords coords = texture.getImageTexCoords();
		
		gl.glBegin(GL.GL_QUADS);
		{
			if( voltear_horizontal )
			{
				gl.glTexCoord2f(coords.bottom(), coords.right());
				gl.glVertex2f( pos_x+texture.getWidth(), pos_y);
				gl.glTexCoord2f(coords.bottom(), coords.left());
				gl.glVertex2f( pos_x+texture.getWidth(), pos_y+texture.getHeight());
				gl.glTexCoord2f( coords.top(), coords.left());
				gl.glVertex2f( pos_x, pos_y+texture.getHeight());
				gl.glTexCoord2f( coords.top(), coords.right());
				gl.glVertex2f( pos_x, pos_y);				
			}
			
			else
			{
				gl.glTexCoord2f( coords.top(), coords.right());
				gl.glVertex2f( pos_x+texture.getWidth(), pos_y);
				gl.glTexCoord2f( coords.top(), coords.left());
				gl.glVertex2f( pos_x+texture.getWidth(), pos_y+texture.getHeight());
				gl.glTexCoord2f(coords.bottom(), coords.left());
				gl.glVertex2f( pos_x, pos_y+texture.getHeight());
				gl.glTexCoord2f(coords.bottom(), coords.right());
				gl.glVertex2f( pos_x, pos_y);				
			}
		}
		gl.glEnd();
		texture.disable();
	}

	public void dibujar_texto( String cadena, int x, int y ) 
	{
		    gl.glWindowPos2i( x, y );
		    glut.glutBitmapString( 5, cadena );
	}

	public void pintar_fondo()
	{
		dibujar_textura(0, 0, fondo1_textura, true);
		dibujar_textura(2000, 0, fondo2_textura, true);
		dibujar_textura(4000, 0, fondo3_textura, true);
	}

	public void dibujar_rectangulo( int x, int y, int ancho, int alto )
	{
		gl.glColor3f(0.0f, 0.0f, 0.0f);
        gl.glRecti(x, y, x+ancho, y+alto);
        gl.glColor3f(0.8f, 0.3f, 0.0f);
        gl.glRecti(x+2, y+2, x+ancho-2, y+alto-2);
	}

	public void cargar_texturas()
	{
	    try
	    {
		    mario_texturas= new Texture[6];
		    mario_texturas[0]= TextureIO.newTexture( new File("../Imagenes/Mario/Mario Camina1.gif"), true);
		    mario_texturas[1]= TextureIO.newTexture( new File("../Imagenes/Mario/Mario Camina2.gif"), true);
		    mario_texturas[2]= TextureIO.newTexture( new File("../Imagenes/Mario/Mario Camina3.gif"), true);
		    mario_texturas[3]= TextureIO.newTexture( new File("../Imagenes/Mario/Mario Salta.gif"), true);
		    mario_texturas[4]= TextureIO.newTexture( new File("../Imagenes/Mario/Mario Cunclillas.gif"), true);
		    mario_texturas[5]= TextureIO.newTexture( new File("../Imagenes/Mario/Mario Muere.gif"), true);
		    
		    enemigos_texturas= new Texture[6];		    
		    enemigos_texturas[0]= TextureIO.newTexture( new File("../Imagenes/Enemigos/Goomba.gif"), true);
		    enemigos_texturas[1]= TextureIO.newTexture( new File("../Imagenes/Enemigos/Shell.gif"), true);
		    enemigos_texturas[2]= TextureIO.newTexture( new File("../Imagenes/Enemigos/Bullet Bill.gif"), true);
		    enemigos_texturas[3]= TextureIO.newTexture( new File("../Imagenes/Enemigos/Boo.gif"), true);
		    enemigos_texturas[4]= TextureIO.newTexture( new File("../Imagenes/Enemigos/Goomba Muerto.gif"), true);
		    enemigos_texturas[5]= TextureIO.newTexture( new File("../Imagenes/Enemigos/Bullet Bill Muerto.gif"), true);
		    
		    coin_textura= TextureIO.newTexture( new File("../Imagenes/Juego/Coin.gif"), true);
		    //bloque_textura= TextureIO.newTexture( new File("../Imagenes/Juego/bloque.gif"), true);
		    fondo1_textura= TextureIO.newTexture( new File("../Imagenes/Juego/Fondo1.1.gif"), true);
		    fondo2_textura= TextureIO.newTexture( new File("../Imagenes/Juego/Fondo1.2.gif"), true);
		    fondo3_textura= TextureIO.newTexture( new File("../Imagenes/Juego/Fondo1.3.gif"), true);
		    gameover_textura= TextureIO.newTexture( new File("../Imagenes/Juego/Game Over.gif"), true);
	    }
	    catch (Exception e)
	    { JOptionPane.showMessageDialog(null, e.toString(), "Error Cargando Texturas", JOptionPane.ERROR_MESSAGE); System.exit(1); }
	}

	public void cargar_sonidos()
	{
	    try
	    {
		    sonido_nivel = new Sound("../Sonidos/game.mid");
		    sonido_coin = new Sound("../Sonidos/coin.wav");
		    sonido_muere = new Sound("../Sonidos/over.wav");
		    sonido_gana = new Sound("../Sonidos/win.wav");
		    sonido_life = new Sound("../Sonidos/life.wav");
		    sonido_alerta = new Sound("../Sonidos/warning.wav");
		    sonido_gameover = new Sound("../Sonidos/gameover.wav");
		    sonido_aplastar = new Sound("../Sonidos/trample.wav");
	    }
	    catch (Exception e)
	    { JOptionPane.showMessageDialog(null, e.toString(), "Error Cargando Sonidos", JOptionPane.ERROR_MESSAGE); System.exit(1); }
	}
	
	public void reiniciar(){
		if(lives>0){
			time = 100;
			lives--;
			
			try{
				Thread.sleep(3000);
			}
			catch(Exception e){
				
			}
						
			personaje.set_vitalidad(true);
			viewport_x= 0;
			viewport_y= 0;
			sonido_nivel.task("play");
			nivel_actual= new Nivel( "../Niveles/nivel1.txt" );
			personaje= new Personaje( 90, 70, 70, 90 );
			monedas = nivel_actual.get_monedas();
			enemigos = nivel_actual.get_enemigos();
			bloques = nivel_actual.get_bloques();
		}
		else{
			fin ++;
			personaje.set_vitalidad(false);
			
			if(fin == 1){
				sonido_muere.task("stop");
				
				sonido_gameover.task("play");
				
			}
		}
	}

	public void dibujar_panel()
	{
		  int mas=40;
	      dibujar_texto("Score: ", 20+mas, 525);
	      dibujar_texto(score+"", 90+mas, 525);
	      
	      dibujar_texto("Coins: ", 190+mas, 525);
	      dibujar_texto(coins+"", 255+mas, 525);
	      
	      dibujar_texto("Lives: ", 340+mas, 525);
	      dibujar_texto(lives+"", 400+mas, 525);
	      
	      dibujar_texto("Level: ", 460+mas, 525);
	      dibujar_texto(level+"", 520+mas, 525);
	      
	      dibujar_texto("Time: ", 580+mas, 525);
	      dibujar_texto(time+"", 640+mas, 525);
	}

	public class Manejador_Teclado extends KeyAdapter
	{
		public void keyPressed(KeyEvent e)
		{
			if (e.getKeyCode() == KeyEvent.VK_LEFT)
			{
				personaje.set_direccion( 2 );
				personaje.set_desplazar( true );
			}
			
			if (e.getKeyCode() == KeyEvent.VK_RIGHT)
			{
				personaje.set_direccion( 1 );
				personaje.set_desplazar( true );
			}
			
			if (e.getKeyCode() == KeyEvent.VK_DOWN)
			{ personaje.set_abajo( true ); }
		
			if (e.getKeyCode() == KeyEvent.VK_M)
			{ personaje.set_salto( true ); }
		
			if (e.getKeyCode() == KeyEvent.VK_SPACE)
			{ 
				personaje.set_velocidad( 4 ); 
				personaje.set_correr(40);
			}
		}

		public void keyReleased(KeyEvent e)
		{
			if (e.getKeyCode() == KeyEvent.VK_LEFT)
			{ 
				if( !personaje.get_direccion() )
				personaje.set_desplazar( false );
			}
			
			if (e.getKeyCode() == KeyEvent.VK_RIGHT)
			{ 
				if( personaje.get_direccion() )
				personaje.set_desplazar( false );
			}
	
			if (e.getKeyCode() == KeyEvent.VK_DOWN)
			{ personaje.set_abajo( false ); }
	
			if (e.getKeyCode() == KeyEvent.VK_M)
			{ personaje.set_salto( false ); }
						
			if (e.getKeyCode() == KeyEvent.VK_SPACE)
			{ 
				personaje.set_velocidad( 2 );
				personaje.set_correr(100);
			}
		}
	}

}