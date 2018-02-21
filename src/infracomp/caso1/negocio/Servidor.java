package infracomp.caso1.negocio;

import infracomp.caso1.sistema.Buffer;
import infracomp.caso1.sistema.Consumidor;
import infracomp.caso1.sistema.Productor;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.Random;

public class Servidor
{
	private Buffer buffer;

	private Productor[] productores;

	private Consumidor[] consumidores;

	public Servidor( String confPath )
	{
		Properties p = loadConfig( confPath );

		this.buffer = new Buffer( Integer.parseInt( p.getProperty( "servidor.buffer_size" ) ) );
		this.productores = new Productor[ Integer.parseInt( p.getProperty( "servidor.cant_productores" ) ) ];
		this.consumidores = new Consumidor[ Integer.parseInt( p.getProperty( "servidor.cant_consumidores" ) ) ];

		for( int i = 0; i < productores.length; i++ )
		{
			productores[ i ] = new Productor( this.buffer, consumidores.length );
			new Thread( productores[ i ], "Productor_" + i ).start( );
		}

		Random r = new Random( );
		for( int i = 0; i < consumidores.length; i++ )
		{
			consumidores[ i ] = new Consumidor( this.buffer, Productor.TOPICS.values( )[ r.nextInt( Productor.TOPICS.values( ).length ) ], String.valueOf( i ) );
			new Thread( consumidores[ i ], "Consumidor_" + i ).start( );
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
