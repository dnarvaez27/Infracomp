package infracomp.caso1;

public class Consumidor implements Runnable
{
	public enum TOPICS
	{
		RASTREAR_PKG( "RCV_PKG" ),
		DESPACHAR_PKG( "DES_PKG" ),
		REGISTRAR_PKG( "REG_PKG" ),
		FACTURAR_PKG( "FAC_PKG" ),
		ADMIN_PROC_CONTABLE( "ADMIN_CONTABLE" ),
		ADMIN_RECS( "ADMIN_RECS" ),
		ADMIN_CLIENTES( "ADMIN_CLIENTES" ), TERMINACION( "TERMINACION" );

		TOPICS( String value )
		{
			this.value = value;
		}

		private String value;
	}

	private final Buffer buffer;

	private static int productoresAtendidos = 0;

	private final int cantProductores;

	public Consumidor( Buffer buffer, int cantProductores )
	{
		this.buffer = buffer;
		this.cantProductores = cantProductores;
	}

	@Override
	public void run( )
	{
		// Empieza a atendera los
		out:
		while( Consumidor.productoresAtendidos < cantProductores )
		{
			Mensaje msg;
			while( ( msg = buffer.remove( ) ) == null )
			{
				synchronized( buffer )
				{
					try
					{
						buffer.wait( );
						if( cantProductores == Consumidor.productoresAtendidos )
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

		// Despierta a los productores, que puedan estar dormidos, para que terminen
		synchronized( buffer )
		{
			buffer.notifyAll( );
		}
	}

	private static void responder( Mensaje mensaje )
	{
		// System.out.println( "Proc_" + mensaje.getContent( ) );
		// if( mensaje.getTopic( ).equals( TOPICS.DESPACHAR_PKG ) )
		// {
		// 	mensaje.setResponse( "mensaje enviado" );
		// }
		// else if( mensaje.getTopic( ).equals( TOPICS.REGISTRAR_PKG ) )
		// {
		// 	mensaje.setResponse( "mensaje recibido" );
		// }
		// else if( mensaje.getTopic( ).equals( TOPICS.RASTREAR_PKG ) )
		// {
		// 	mensaje.setResponse( "El mensaje se encuentra en " );
		// }
		// else if( mensaje.getTopic( ).equals( TOPICS.ADMIN_CLIENTES ) )
		// {
		// 	mensaje.setResponse( "Atenderemos pronto su pregunta :)" );
		// }
		// else if( mensaje.getTopic( ).equals( TOPICS.ADMIN_PROC_CONTABLE ) )
		// {
		// 	mensaje.setResponse( "Estas en cuentas por cobrar" );
		// }
		// else if( mensaje.getTopic( ).equals( TOPICS.ADMIN_RECS ) )
		// {
		// 	mensaje.setResponse( "Debitado en inventario" );
		// }
		// else if( mensaje.getTopic( ).equals( TOPICS.FACTURAR_PKG ) )
		// {
		// 	mensaje.setResponse( "Anotando lo que vendiii :D" );
		// }

		if( mensaje.getTopic( ) == TOPICS.TERMINACION )
		{
			synchronized( Consumidor.class )
			{
				productoresAtendidos++;
			}
		}

		mensaje.setResponse( "Respondido: " + mensaje.getContent( ) );

		synchronized( mensaje )
		{
			mensaje.notifyAll( );
		}
	}

}
