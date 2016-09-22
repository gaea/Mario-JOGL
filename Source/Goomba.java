import java.awt.Rectangle;
import java.util.Vector;

public class Goomba extends Entidad
{
	private int direccion;
	private int sprite;
	private int velocidad= 2;
	private long intervalo1, intervalo2;
	
	public Goomba( int x, int y, int ancho, int alto, int tipo )
	{
		this.x= x;
		this.y= y;
		this.ancho= ancho;
		this.alto = alto;
		this.tipo = tipo;
		
		intervalo1= System.currentTimeMillis();
		intervalo2= System.currentTimeMillis();
	}	

	//Cambia la direccion aleatoriamente (<-, ->) cada 1,5 segundos
	public void comportamiento( Vector bloques )
	{
		if( (System.currentTimeMillis()-intervalo1) >= 1500 )
		{
			direccion=(int)(Math.random()*2);
			intervalo1= System.currentTimeMillis();
		}

		if( direccion==0   )
		{
			if( validar_movimiento( bloques, x+velocidad, y ) )
			x+=velocidad;
			
			else
			direccion= 1;
		}

			
		if( direccion==1   )
		{
			if( validar_movimiento( bloques, x-velocidad, y ) )
			x-=velocidad;
			
			else
			direccion= 0;
		}		
		animar();	
	}	


	public void animar( )
	{
		if( (System.currentTimeMillis()-intervalo2)>=300 )
		{
			sprite++;
			intervalo2= System.currentTimeMillis();
		}

		if( sprite==2 )
		sprite= 0;
	}
	
	
	public boolean validar_movimiento( Vector bloques, int x_new, int y_new )
	{
		Rectangle area_enemigo= new Rectangle( x_new, y_new, ancho, alto );
		
		for( int i=0; i<bloques.size(); i++ )
		{
			if( area_enemigo.intersects( (Rectangle)bloques.get(i) ) )
			{ return false; }
		}
		
		return true;
	}

	
	public boolean get_direccion( )
	{
		boolean dir= ((sprite==0)? true : false );
		return dir;
	}
		
	public int get_X()
	{	return x; }
		
	public int get_Y()
	{	return y; }

	public int get_velocidad()
	{	return velocidad; }
}