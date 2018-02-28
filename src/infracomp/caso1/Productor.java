package infracomp.caso1;

public class Productor extends Thread
{
	private final Buffer buffer;

	private final Mensaje[] mensajes;

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
