import java.awt.Rectangle;
import java.util.Vector;

public class Personaje
{
	private long intervalo;
	public Rectangle area_personaje;
	private Sound jump = new Sound("../Sonidos/jump.wav");
	private boolean desplazar, abajo, salto, saltando, vitalidad, animado;
	private int velocidad, x, y, ancho, alto, direccion, sprite, correr, x_frame, y_salto;
	
	public Personaje( int x, int y, int ancho, int alto )
	{
		this.x= x;
		this.y= y;
		this.ancho= ancho;
		this.alto = alto;
		
		velocidad= 2;
		direccion= 1;
		correr= 200;
		animado= true;
		vitalidad= true;
		intervalo= System.currentTimeMillis();
	}	

	public void desplazar( Vector bloques )
	{
		if( direccion==1 && desplazar && validar_movimiento( bloques, x+velocidad, y ) )
		{
			x+=velocidad;
			
			if( Math.abs( x_frame-x )>600 && x_frame<5600 )
			x_frame+=velocidad;
		}
		
		if( direccion==2 && desplazar && validar_movimiento( bloques, x-velocidad, y ) )
		{
			x-=velocidad;
			
			if( Math.abs( x_frame-x )<200 && x_frame>0 )
			x_frame-=velocidad;
		}
		//System.out.println("X_"+this.x+",Y_"+this.y);
		animar();
	}	

	public void saltar( Vector bloques )
	{
		if( salto )
		{
			if( y<y_salto && validar_movimiento( bloques, x, y+(velocidad*2) ) ){
				y+= (velocidad*2);
			}
			else
			{
				y_salto= 0;
				salto= false;
			}
		}

		else
		{
			if( validar_movimiento( bloques, x, y-(velocidad*2) ) )
			y-=(velocidad*2);
			
			else
			saltando= false;
		}
	}

	public void animar( )
	{
		if( (sprite<=2 && (System.currentTimeMillis()-intervalo)>=correr && desplazar )  )
		{
			sprite++;
			intervalo= System.currentTimeMillis();		
		}
		
		if( (sprite>2 && saltando==false ) || (desplazar==false && abajo==false) )
		sprite= 0;

		if( saltando )
		sprite= 3;
			
		if( abajo && desplazar==false )
		sprite= 4;
	}

	public boolean validar_movimiento( Vector bloques, int x_new, int y_new )
	{
		area_personaje= new Rectangle( x_new, y_new, ancho, alto );
		
		for( int i=0; i<bloques.size(); i++ )
		{
			if( area_personaje.intersects( (Rectangle)bloques.get(i) ) )
			{ return false; }
		}
		
		return true;
	}

	public void animacion_muere( )
	{ 
		if( y<=400 && animado )
		y+=10;
		
		if( y>400 )		
		animado= false;
		
		if( y>0 && !animado )
		y-=10;
	}

	public int get_correr(){
		return correr;
	}

	public void set_correr(int vel){
		correr = vel;
	}

	public void set_velocidad( int vel )
	{ velocidad= vel; }

	public int get_velocidad()
	{	return velocidad; }

	public void set_direccion( int dir )
	{ 
		if( vitalidad )
		direccion= dir;
	}
	
	public boolean get_direccion( )
	{	return ((direccion==1)? true : false );	}
	
	public void set_desplazar( boolean desp )
	{	desplazar= desp; }

	public boolean get_desplazar(){
		return desplazar;
	}
	
	public void set_abajo( boolean abj )
	{	
		abajo= abj;	
		alto = ((abajo)? 65: 90 );
	}

	public void set_salto( boolean sal )
	{
		if( sal && saltando==false )
		{
			saltando= true;
			y_salto= y+200;
			salto= sal;
			jump.task("play");
		}
		
		if( !sal )
		salto= sal;
	}

	public void set_vitalidad( boolean vit )
	{ vitalidad= vit; }

	public void set_sprite( int spr )
	{ sprite= spr; }

	public boolean get_vitalidad()
	{ return vitalidad; }
	
	public int get_X()
	{	return x; }
		
	public int get_Y()
	{	return y; }

	public int get_x_frame()
	{	return x_frame; }
	
	public int get_sprite()
	{	return sprite; }
	
	public Rectangle get_area_personaje()
	{	return new Rectangle( x, y, ancho, alto ); }
	
	public int get_alto()
	{ return alto; }
	
	
}