package infracomp.caso1;

/**
 * Representa un Productor dentro del esquema {@link Productor} {@link Consumidor}<br>
 *
 * @author David Narvaez - (d.narvaez11@uniandes.edu.co)
 * @author Daniela Jaimes - (d.jaimes@uniandes.edu.co)
 */
public class Productor extends Thread
{
	/**
	 * Instancia del Buffer para publicar mensajes
	 */
	private final Buffer buffer;

	/**
	 * Arreglo de Mensajes que el productor enviará
	 */
	private final Mensaje[] mensajes;

	/**
	 * Crea una instancia un Productor pasandole el Buffer como parámetro y la cantidad de mensajes que va a enviar
	 *
	 * @param buffer       Buffer que maneja los mensajes del sistema
	 * @param cantMensajes Cantidad de mensajes que va a enviar
	 * @see #run()
	 */
	public Productor( Buffer buffer, int cantMensajes )
	{
		this.buffer = buffer;
		this.mensajes = new Mensaje[ cantMensajes + 1 ];

		int i = 0;
		for( ; i < cantMensajes; i++ )
		{
			this.mensajes[ i ] = new Mensaje( );
		}

		Mensaje end = new Mensaje( );
		end.setTopic( Consumidor.TOPICS.TERMINACION );
		this.mensajes[ i ] = end;
	}

	/**
	 * Para cada mensaje del productor, se intenta agregarlo al Buffer. <br>
	 * Si se pudo agregar al Buffer, el Productor notifica al Buffer para que consumidores dormidos se despierten y se queda esperando una respuesta.
	 * De lo contrario, intenta constantemente, liberando el procesador (yield), hasta lograrlo
	 */
	@Override
	public void run( )
	{
		for( Mensaje mensaje : mensajes )
		{
			synchronized( mensaje )
			{
				// Espera activa
				while( !buffer.add( mensaje ) )
				{
					synchronized( this )
					{
						try
						{
							this.wait( 1 );
						}
						catch( InterruptedException e )
						{
							e.printStackTrace( );
						}
					}
				}

				// Despierta a los productores
				synchronized( buffer )
				{
					buffer.notifyAll( );
				}

				// Duerme sobre el mensaje
				try
				{
					mensaje.wait( );
				}
				catch( InterruptedException e )
				{
					e.printStackTrace( );
				}
			}
		}
	}
}
