import java.awt.Rectangle;

public class Bullet_Bill  extends Entidad
{
	private int opcion;
	private int sprite;
	private int velocidad= 4;
	
	public Bullet_Bill( int x, int ancho, int alto, int tipo )
	{
		this.x= x;
		this.ancho= ancho;
		this.alto = alto;
		this.tipo = tipo;

		opcion= x;
		y= ((int)(Math.random()*22)+1)*25;
	}	

	//Movimiento de Bullet Bill
	public void comportamiento( )
	{
		if( x>0 )
		x-=velocidad;
		
		if( x<=0 )
		{
			x= opcion;
			y= ((int)(Math.random()*22)+1)*25;	
		}
	}
		
	public int get_X()
	{	return x; }
		
	public int get_Y()
	{	return y; }

	public int get_velocidad()
	{	return velocidad; }

	public Rectangle get_area()
	{	return new Rectangle( x, y, ancho, alto ); }
}