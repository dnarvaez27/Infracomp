package infracomp.caso1;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;
import java.util.Random;

public class Servidor
{
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

	public static void main( String... args )
	{
		new Servidor( "./conf/conf.properties" );
	}
}
