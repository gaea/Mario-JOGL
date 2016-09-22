import java.awt.Rectangle;
import java.util.Vector;
   
public class Shell extends Entidad
{
	private int direccion;
	private int sprite;
	private int velocidad= 4;
	
	public Shell( int x, int y, int ancho, int alto, int tipo )
	{
		this.x= x;
		this.y= y;
		this.ancho= ancho;
		this.alto = alto;
		this.tipo = tipo;
	}	

	//Cambia la direccion (<-, ->) cuando colisiona
	public void comportamiento( Vector bloques )
	{
		if( direccion==0 )
		{
			if( validar_movimiento( bloques, x+velocidad, y ) )
			{
				x+=velocidad;
				sprite=0;				
			}
			else
			direccion=1;
		}
		
		if( direccion==1 )
		{
			if( validar_movimiento( bloques, x-velocidad, y ) )
			{
				x-=velocidad;
				sprite=1;				
			}
			else
			direccion=0;
		}
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