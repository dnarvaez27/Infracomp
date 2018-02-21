package infracomp.caso1.sistema;

import java.util.LinkedList;
import java.util.Queue;

public class Buffer
{
	private Queue<Mensaje> mensajes;

	private final int size;

	public Buffer( int size )
	{
		this.size = size;
		mensajes = new LinkedList<>( );
	}

	public synchronized boolean add( Mensaje m )
	{
		if( mensajes.size( ) < size )
		{
			mensajes.add( m );
			return true;
		}
		return false;
	}

	public synchronized Mensaje remove( )
	{
		return mensajes.poll( );
	}
}
