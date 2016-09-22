
public class Boo extends Entidad
{
	private int direccion;
	private int sprite;
	private int velocidad= 3;
	private long intervalo;
	
	public Boo( int x, int y, int ancho, int alto, int tipo )
	{
		this.x= x;
		this.y= y;
		this.ancho= ancho;
		this.alto = alto;
		this.tipo = tipo;
		
		intervalo= System.currentTimeMillis();
	}	

	//Cambia la direccion en diagonales aleatoriamente cada 1,5 segundos
	public void comportamiento( )
	{
		if( (System.currentTimeMillis()-intervalo) >= 2000 )
		{
			direccion=(int)(Math.random()*4);
			intervalo= System.currentTimeMillis();
		}

		if( direccion==0 && x+velocidad>0 && x+velocidad<6000-65 && y+velocidad>0 && y+velocidad<600-55 )
		{
			x+=velocidad;
			y+=velocidad;
		}

			
		if( direccion==1 && x-velocidad>0 && x-velocidad<6000-65 && y+velocidad>0 && y+velocidad<600-55 )
		{
			x-=velocidad;
			y+=velocidad;
		}
		
		if( direccion==2 && x-velocidad>0 && x-velocidad<6000-65 && y-velocidad>0 && y-velocidad<600-55 )
		{
			x-=velocidad;
			y-=velocidad;
		}
			
		if( direccion==3 && x+velocidad>0 && x+velocidad<6000-65 && y-velocidad>0 && y-velocidad<600-55 )
		{
			x+=velocidad;
			y-=velocidad;
		}
		
		animar();	
	}	


	public void animar( )
	{
		if( direccion==0 || direccion==3 )
		sprite= 0;

		if( direccion==1 || direccion==2 )
		sprite= 1;
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