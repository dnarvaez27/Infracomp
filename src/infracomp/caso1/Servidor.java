package infracomp.caso1;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;
import java.util.Random;

public class Servidor
{
	private Buffer buffer;

	private Consumidor[] consumidores;

	private Productor[] productores;

	public Servidor( String confPath )
	{
		Properties p = loadConfig( confPath );

		this.buffer = new Buffer( Integer.parseInt( p.getProperty( "servidor.buffer_size" ) ) );
		this.consumidores = new Consumidor[ Integer.parseInt( p.getProperty( "servidor.cant_consumidores" ) ) ];
		this.productores = new Productor[ Integer.parseInt( p.getProperty( "servidor.cant_productores" ) ) ];
		Integer[] cantMensajes = Arrays.stream( p.getProperty( "servidor.cant_mensajes" ).split( "," ) )
				.map( Integer::parseInt )
				.toArray( Integer[]::new );

		for( int i = 0; i < consumidores.length; i++ )
		{
			consumidores[ i ] = new Consumidor( buffer, productores.length );
			new Thread( consumidores[ i ], "Consumidor_" + i ).start( );
		}

		for( int i = 0; i < productores.length; i++ )
		{
			productores[ i ] = new Productor( this.buffer, cantMensajes[ i ] );
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
