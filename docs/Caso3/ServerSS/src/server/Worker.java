//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package server;

import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.jcajce.JcaPEMWriter;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.Socket;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.security.cert.X509Certificate;
import java.util.Arrays;

public class Worker implements Runnable
{
	public static final String RC4 = "RC4";

	public static final String BLOWFISH = "BLOWFISH";

	public static final String AES = "AES";

	public static final String DES = "DES";

	public static final String RSA = "RSA";

	public static final String HMACMD5 = "HMACMD5";

	public static final String HMACSHA1 = "HMACSHA1";

	public static final String HMACSHA256 = "HMACSHA256";

	public static final String CERTSRV = "CERTSRV";

	public static final String SEPARADOR = ":";

	public static final String OK = "OK";

	public static final String HOLA = "HOLA";

	public static final String INICIO = "INICIO";

	public static final String RTA = "RTA";

	public static final String INFO = "INFO";

	public static final String ERROR = "ERROR";

	public static final String ESTADO = "ESTADO";

	public static final String ERROR_FORMATO = "Error en el formato. Cerrando conexion";

	public static final String CERTCLNT = "CERTCLNT";

	public static final String CERTSVR = "CERTSVR";

	private int id;

	private Socket ss;

	private KeyPair keyPair;

	public Worker( int pId, Socket pSocket )
	{
		this.id = pId;
		this.ss = pSocket;
		Security.addProvider( new BouncyCastleProvider( ) );
	}

	private void printError( Exception e )
	{
		System.out.println( e.getMessage( ) );
		e.printStackTrace( );
	}

	private String read( BufferedReader reader ) throws IOException
	{
		String linea = reader.readLine( );
		System.out.println( "Thread " + this.id + "<<CLNT: (recibe):" + linea );
		return linea;
	}

	private void write( PrintWriter writer, String msg )
	{
		writer.println( msg );
		System.out.println( "Srv " + this.id + ">>SERV (envia):" + msg );
	}

	public void run( )
	{
		try
		{
			PrintWriter writer = new PrintWriter( this.ss.getOutputStream( ), true );
			BufferedReader reader = new BufferedReader( new InputStreamReader( this.ss.getInputStream( ) ) );
			String linea = this.read( reader );
			if( !linea.split( ":" )[ 0 ].equals( "HOLA" ) )
			{
				this.write( writer, "Error en el formato. Cerrando conexion" );
				throw new FontFormatException( linea );
			}
			getStatus( linea );

			this.write( writer, "INICIO" );
			linea = this.read( reader );
			if( !linea.contains( ":" ) || !linea.split( ":" )[ 0 ].equals( "ALGORITMOS" ) )
			{
				this.write( writer, "Error en el formato. Se espera que la cadena empieze con la palabra ALGORITMOS. Cerrando conexion" );
				throw new FontFormatException( linea );
			}

			String[] algoritmos = linea.split( ":" );
			if( !algoritmos[ 1 ].equals( "BLOWFISH" ) && !algoritmos[ 1 ].equals( "AES" ) && !algoritmos[ 1 ].equals( "DES" ) && !algoritmos[ 1 ].equals( "RC4" ) )
			{
				this.write( writer, "ERROR: Algoritmo no soportado o no reconocido: " + algoritmos[ 1 ] + ". Cerrando conexion" );
				throw new NoSuchAlgorithmException( );
			}

			if( !algoritmos[ 2 ].equals( "RSA" ) )
			{
				this.write( writer, "ERROR: Algoritmo no soportado o no reconocido: " + algoritmos[ 1 ] + ". Cerrando conexion" );
				throw new NoSuchAlgorithmException( );
			}

			if( !algoritmos[ 3 ].equals( "HMACMD5" ) && !algoritmos[ 3 ].equals( "HMACSHA1" ) && !algoritmos[ 3 ].equals( "HMACSHA256" ) )
			{
				this.write( writer, "ERROR: Algoritmo no soportado o no reconocido: " + algoritmos[ 3 ] + " . Cerrando conexion" );
				throw new NoSuchAlgorithmException( );
			}

			this.write( writer, "ESTADO:OK" );
			linea = this.read( reader );
			if( !linea.equals( "CERTCLNT" ) )
			{
				this.write( writer, "Error en el formato. Se espera que la cadena fuera CERTCLNT. Cerrando conexion" );
				throw new FontFormatException( linea );
			}

			try
			{
				linea = this.read( reader );
				String strToDecode = "";

				// TODO: Fue necesario cambiar a !line.isEmpty() que es cuando termina de enviar el Certificado por PEM
				for( strToDecode = strToDecode + linea; !linea.isEmpty( ); linea = this.read( reader ) )
				{
					strToDecode = strToDecode + linea + "\n";
				}

				StringReader rea = new StringReader( strToDecode );
				PemReader pr = new PemReader( rea );
				PemObject pemcertificadoPuntoAtencion = pr.readPemObject( );
				X509CertificateHolder certHolder = new X509CertificateHolder( pemcertificadoPuntoAtencion.getContent( ) );
				X509Certificate certificadoCliente = ( new JcaX509CertificateConverter( ) ).getCertificate( certHolder );
				pr.close( );
			}
			catch( Exception var45 )
			{
				this.write( writer, "ESTADO:ERROR" );
				var45.printStackTrace( );
				throw new FontFormatException( "Error en el certificado recibido, no se puede decodificar" );
			}

			this.write( writer, "ESTADO:OK" );
			this.write( writer, "CERTSRV" );

			try
			{
				KeyPairGenerator keyGen = KeyPairGenerator.getInstance( "RSA", "BC" );
				this.keyPair = keyGen.generateKeyPair( );
				Security.addProvider( new BouncyCastleProvider( ) );
				keyGen.initialize( 1024 );
				X509Certificate cert = Seguridad.generateV3Certificate( this.keyPair );
				StringWriter wr = new StringWriter( );
				JcaPEMWriter pemWriter = new JcaPEMWriter( wr );
				pemWriter.writeObject( cert );
				pemWriter.flush( );
				pemWriter.close( );
				String certStr = wr.toString( );
				this.write( writer, certStr );
			}
			catch( Exception var44 )
			{
				var44.printStackTrace( );
			}

			linea = this.read( reader );
			if( linea.equals( "ESTADO:OK" ) )
			{
				this.write( writer, "INICIO" );
				this.read( reader );
				this.read( reader );
				this.write( writer, "ESTADO:OK" );

				write( writer, String.valueOf( getCPUUsage( ) ) );

				System.out.println( "Thread " + this.id + "Terminando\n" );
				return;
			}

			System.out.println( "Error de confirmación, cerrando conexion: " + linea );
		}
		catch( NullPointerException var46 )
		{
			this.printError( var46 );

			try
			{
				this.ss.close( );
			}
			catch( Exception var43 )
			{
				;
			}

			return;
		}
		catch( IOException var47 )
		{
			this.printError( var47 );

			try
			{
				this.ss.close( );
			}
			catch( Exception var42 )
			{
				;
			}

			return;
		}
		catch( FontFormatException var48 )
		{
			this.printError( var48 );

			try
			{
				this.ss.close( );
			}
			catch( Exception var41 )
			{
				;
			}

			return;
		}
		catch( NoSuchAlgorithmException var49 )
		{
			this.printError( var49 );

			try
			{
				this.ss.close( );
			}
			catch( Exception var40 )
			{
				;
			}

			return;
		}
		catch( IllegalStateException var50 )
		{
			this.printError( var50 );

			try
			{
				this.ss.close( );
			}
			catch( Exception var39 )
			{
				;
			}

			return;
		}
		catch( Exception var51 )
		{
			var51.printStackTrace( );

			try
			{
				this.ss.close( );
			}
			catch( Exception var38 )
			{
				;
			}

			return;
		}
		finally
		{
			try
			{
				this.ss.close( );
			}
			catch( Exception var37 )
			{
				;
			}

		}

	}

	private void getStatus( String linea )
	{
		Integer[] datos = Arrays.stream( linea.substring( linea.indexOf( ":" ) + 1 ).split( ":" ) ).map( Integer::parseInt ).toArray( Integer[]::new );
		int numThread = datos[ 0 ];
		int load = datos[ 1 ];
		int iter = datos[ 2 ];
	}

	public static double getCPUUsage( )
	{
		// "grep 'cpu ' /proc/stat | awk '{usage=($2+$4)*100/($2+$4+$5)} END {print usage \"%\"}'"
		try
		{
			ProcessBuilder builder = new ProcessBuilder( "bash", "-c", "cat /proc/stat " );
			Process p = builder.start( );
			BufferedReader in = new BufferedReader( new InputStreamReader( p.getInputStream( ) ) );

			String x = in.readLine( );
			Integer[] data = Arrays.stream( x.substring( 4 ).trim( ).split( " " ) ).map( Integer::parseInt ).toArray( Integer[]::new );
			return ( data[ 0 ] + data[ 2 ] ) * 100.0 / ( data[ 0 ] + data[ 2 ] + data[ 3 ] );
		}
		catch( IOException e )
		{
			e.printStackTrace( );
		}
		return -1;
	}
}
