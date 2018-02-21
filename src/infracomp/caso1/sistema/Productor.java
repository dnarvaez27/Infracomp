package infracomp.caso1.sistema;

public class Productor implements Runnable
{
	public enum TOPICS
	{
		RASTREAR_PKG( "RCV_PKG" ),
		DESPACHAR_PKG( "DES_PKG" ),
		REGISTRAR_PKG( "REG_PKG" ),
		FACTURAR_PKG( "FAC_PKG" ),
		ADMIN_PROC_CONTABLE( "ADMIN_CONTABLE" ),
		ADMIN_RECS( "ADMIN_RECS" ),
		ADMIN_CLIENTES( "ADMIN_CLIENTES" );

		TOPICS( String value )
		{
			this.value = value;
		}

		private String value;
	}

	private final Buffer buffer;

	private static int msgRecibidos = 0;

	private final int cantConsumers;

	public Productor( Buffer buffer, int cantConsumers )
	{
		this.buffer = buffer;
		this.cantConsumers = cantConsumers;
	}

	@Override
	public void run( )
	{
		out:
		while( cantConsumers > msgRecibidos )
		{
			Mensaje msg;
			while( ( msg = buffer.remove( ) ) == null )
			{
				synchronized( buffer )
				{
					try
					{
						buffer.wait( );
						if( cantConsumers <= msgRecibidos )
						{
							break out;
						}
					}
					catch( InterruptedException e )
					{
						e.printStackTrace( );
					}
				}
			}
			responder( msg );
		}
		synchronized( buffer )
		{
			buffer.notifyAll( );
		}
	}

	private static void responder( Mensaje mensaje )
	{
		System.out.println( "Proc_" + mensaje.getContent( ) );
		if( mensaje.getTopic( ).equals( TOPICS.DESPACHAR_PKG ) )
		{
			mensaje.setResponse( "mensaje enviado" );
		}
		else if( mensaje.getTopic( ).equals( TOPICS.REGISTRAR_PKG ) )
		{
			mensaje.setResponse( "mensaje recibido" );
		}
		else if( mensaje.getTopic( ).equals( TOPICS.RASTREAR_PKG ) )
		{
			mensaje.setResponse( "El mensaje se encuentra en " );
		}
		else if( mensaje.getTopic( ).equals( TOPICS.ADMIN_CLIENTES ) )
		{
			mensaje.setResponse( "Atenderemos pronto su pregunta :)" );
		}
		else if( mensaje.getTopic( ).equals( TOPICS.ADMIN_PROC_CONTABLE ) )
		{
			mensaje.setResponse( "Estas en cuentas por cobrar" );
		}
		else if( mensaje.getTopic( ).equals( TOPICS.ADMIN_RECS ) )
		{
			mensaje.setResponse( "Debitado en inventario" );
		}
		else if( mensaje.getTopic( ).equals( TOPICS.FACTURAR_PKG ) )
		{
			mensaje.setResponse( "Anotando lo que vendiii :D" );
		}

		synchronized( mensaje )
		{
			mensaje.notifyAll( );
		}

		synchronized( Productor.class )
		{
			msgRecibidos++;
		}
	}

	public static int getMsgRecibidos( )
	{
		return msgRecibidos;
	}
}
