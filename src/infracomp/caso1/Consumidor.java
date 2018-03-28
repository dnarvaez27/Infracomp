package infracomp.caso1;

/**
 * Representa un Consumidor dentro del esquema {@link Productor} {@link Consumidor}<br>
 *
 * @author David Narvaez - (d.narvaez11@uniandes.edu.co)
 * @author Daniela Jaimes - (d.jaimes@uniandes.edu.co)
 */
public class Consumidor implements Runnable
{
	/**
	 * Topicos para los diferentes mensajes que pueden ser enviados por el Productor
	 */
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

	/**
	 * Instancia del Buffer del cual extraerá los mensajes de los Productores
	 */
	private final Buffer buffer;

	/**
	 * Cantidad de productores atendidos por todos los consumidores
	 */
	private static int productoresAtendidos = 0;

	/**
	 * Cantidad de productores totales en el sistema
	 */
	private final int cantProductores;

	/**
	 * Crea una instancia de un consumidor pasandole el Buffer y la cantidad de productores por parámetro
	 *
	 * @param buffer          Buffer que maneja los mensajes del sistema
	 * @param cantProductores Cantidad total de productores en el sistema}
	 * @see #run()
	 */
	public Consumidor( Buffer buffer, int cantProductores )
	{
		this.buffer = buffer;
		this.cantProductores = cantProductores;
	}

	/**
	 * Mientras aún existan Productores por atender, entonces constantemente intenta sacar mensajes del Buffer<br>
	 * Cuando un mensaje es extraido del Buffer, se ejecuta el método {@link #responder(Mensaje)}.
	 * Si no hay ningún mensaje en el Buffer, el Consumidor se queda esperando sobre el Buffer hasta que un {@link Productor} agrege u mensaje
	 */
	@Override
	public void run( )
	{
		// Se ejecuta mientras aún existan productores por atender
		out:
		while( Consumidor.productoresAtendidos < cantProductores )
		{
			// Intenta extraer el mensaje del Buffer
			Mensaje msg;
			while( ( msg = buffer.remove( ) ) == null )
			{
				// El Buffer está vacío por lo que entra a una espera pasiva sobre el buffer
				synchronized( buffer )
				{
					try
					{
						buffer.wait( );

						// Si al despertarse, todos los productores fueron ya atendidos, acaba su ejecución
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
			System.out.println( "Va a responder" );
			responder( msg );
		}

		// Despierta a los productores, que puedan estar dormidos, para que terminen
		synchronized( buffer )
		{
			buffer.notifyAll( );
		}
	}

	/**
	 * Responde al mensaje filtrandolo por mensaje por Topico <br>
	 * Si el mensaje tiene como Topico {@link TOPICS#TERMINACION}, entonces se entenderá que el consumidor habrá terminado sus peticiones
	 *
	 * @param mensaje Mensaje a ser procesado
	 */
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
