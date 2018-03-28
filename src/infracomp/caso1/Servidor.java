package infracomp.caso1;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;
import java.util.Random;

/**
 * Clase que representa el servidor
 *
 * @author David Narvaez - (d.narvaez11@uniandes.edu.co)
 * @author Daniela Jaimes - (d.jaimes@uniandes.edu.co)
 */
public class Servidor
{
	/**
	 * Crea una instancia del Servidor<br>
	 * Carga la configuración desde el archivo dado por parámetro y corre los Threads de Consumidor y Productor
	 *
	 * @param confPath Ruta del archivo de configuración
	 */
	public Servidor( String confPath )
	{
		Properties p = loadConfig( confPath );

		Buffer buffer = new Buffer( Integer.parseInt( p.getProperty( "caso1.buffer_size" ) ) );
		Consumidor[] consumidores = new Consumidor[ Integer.parseInt( p.getProperty( "caso1.cant_consumidores" ) ) ];
		Productor[] productores = new Productor[ Integer.parseInt( p.getProperty( "caso1.cant_productores" ) ) ];
		Integer[] cantMensajes = Arrays.stream( p.getProperty( "caso1.cant_mensajes" ).split( "," ) )
				.map( Integer::parseInt )
				.toArray( Integer[]::new );

		for( int i = 0; i < consumidores.length; i++ )
		{
			consumidores[ i ] = new Consumidor( buffer, productores.length );
			new Thread( consumidores[ i ], "Consumidor_" + i ).start( );
		}

		for( int i = 0; i < productores.length; i++ )
		{
			productores[ i ] = new Productor( buffer, cantMensajes[ i ] );
			new Thread( productores[ i ], "Productor_" + i ).start( );
		}
	}

	/**
	 * Carga el archivo de configuración a un Objeto Properties
	 *
	 * @param confPath Ruta del archivo a cargar
	 * @return Objeto de Properties con la configuración del archivo
	 */
	private Properties loadConfig( String confPath )
	{
		Properties p = new Properties( );
		try( FileInputStream inStream = new FileInputStream( confPath ) )
		{
			p.load( inStream );
		}
		catch( IOException e )
		{
			e.printStackTrace( );
		}
		return p;
	}

	/**
	 * Corre el servidor con la configuración del archivo /conf/caso1_conf.properties
	 *
	 * @param args None
	 */
	public static void main( String... args )
	{
		new Servidor( "./conf/caso1_conf.properties" );
	}
}
