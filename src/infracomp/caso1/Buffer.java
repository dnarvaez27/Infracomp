package infracomp.caso1;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Buffer que almacena los mensajes del esquema {@link Productor } - {@link Consumidor}<br>
 * Este objeto tiene un tamaño limitado que es pasado por parámetro al buffer.<br>
 * El comportamiento del buffer depende de la capacidad disponible del buffer<br><br>
 * <b>El Buffer está implementado como un Monitor, por lo que el acceso a sus métodos se realiza por un Thread a la vez</b>
 *
 * @author David Narvaez (d.narvaez11@uniandes.edu.co)
 * @author Daniela Jaimes (d.jaimes@uniandes.edu.co)
 * @see #add(Mensaje)
 * @see #remove()
 */
public class Buffer
{
	/**
	 * Cola de mensajes en el Buffer
	 */
	private Queue<Mensaje> mensajes;

	/**
	 * Tamaño máximo del Buffer
	 */
	private final int size;

	/**
	 * Construye una instancia del Buffer con un tamaño fijo dado
	 *
	 * @param size Tamaño máximo del buffer
	 */
	public Buffer( int size )
	{
		this.size = size;
		mensajes = new LinkedList<>( );
	}

	/**
	 * Agrega un mensaje al Buffer siempre y cuando no exceda el tamaño máximo de este
	 *
	 * @param m Mensaje a agregar al Buffer
	 * @return True si el mensaje fue agregado exitosamente, False si el Buffer ya está lleno
	 * @see Queue#add(Object)
	 */
	public synchronized boolean add( Mensaje m )
	{
		if( mensajes.size( ) < size )
		{
			mensajes.add( m );
			return true;
		}
		return false;
	}

	/**
	 * Remueve y retorna el mensaje que está en la cabeza de la cola del Buffer
	 *
	 * @return El mensaje que está en la cabeza de la cola del Buffer o null si está vacía
	 * @see Queue#poll()
	 */
	public synchronized Mensaje remove( )
	{
		return mensajes.poll( );
	}
}
