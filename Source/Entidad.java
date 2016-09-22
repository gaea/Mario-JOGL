import java.awt.Rectangle;

public class Entidad
{
	protected int x, y, ancho, alto, tipo;
	
	public Rectangle get_area()
	{	return new Rectangle( x, y, ancho, alto ); }
}