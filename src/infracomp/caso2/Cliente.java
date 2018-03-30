package infracomp.caso2;

import infracomp.caso2.seguridad.CertificadoDigital;
import infracomp.caso2.seguridad.Cifrado;
import infracomp.caso2.seguridad.Seguridad;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.cert.X509Certificate;
import java.util.Arrays;

import static infracomp.caso2.Utils.hexToByteArray;
import static infracomp.caso2.Utils.join;
import static infracomp.caso2.Utils.split;
import static infracomp.caso2.Utils.arrayToHexString;

public class Cliente implements Runnable
{
	enum Protocolo
	{
		HOLA( "HOLA" ),
		INICIO( "INICIO" ),
		ALGORITMOS( "ALGORITMOS" ),
		CERTCLNT( "CERTCLNT" ),
		CERTSRV( "CERTSRV" ),
		ACT1( "ACT1" ),
		ACT2( "ACT2" ),
		ESTADO( "ESTADO" ),
		OK( "OK" ),
		ERROR( "ERROR" ),
		SEP( ":" );

		private String value;

		Protocolo( String value )
		{
			this.value = value;
		}
	}

	public final static int PUERTO = 8080;

	private final static String IP = "localhost";

	private final static Cifrado.CipherAlgorithm algoritmoSimetrico = Cifrado.CipherAlgorithm.AES;

	private final static Cifrado.CipherAlgorithm algoritmoAsimetrico = Cifrado.CipherAlgorithm.RSA;

	private final static Seguridad.KeyAlgorithm.Pair algoritmoCertificado = Seguridad.KeyAlgorithm.Pair.RSA_1024;

	private final static Cifrado.MacAlgorithm algoritmoHMAC = Cifrado.MacAlgorithm.HmacSHA256;

	private PrintWriter out;

	private OutputStream outStream;

	private InputStream inStream;

	private BufferedReader in;

	public Cliente( ) throws IOException
	{
		Socket sock = new Socket( IP, PUERTO );

		outStream = sock.getOutputStream( );
		inStream = sock.getInputStream( );
		out = new PrintWriter( outStream, true );
		in = new BufferedReader( new InputStreamReader( inStream ) );
	}

	@Override
	public void run( )
	{
		sendState( new Double[]{ 41.0, 24.2028 },
				   new Double[]{ 2.0, 10.4418 } );
	}

	private void sendState( Double[] lat, Double[] lon )
	{
		try
		{
			KeyPair keyPair = Seguridad.getKeyPair( algoritmoCertificado );
			X509Certificate clientCertificate;
			X509Certificate serverCertificate;
			byte[][] stage4;

			// ETAPA 1
			System.out.println( "Inicio Etapa 1" );
			stage1( );
			System.out.println( "Fin Etapa 1" );

			// ETAPA 2
			System.out.println( "Inicio Etapa 2" );
			clientCertificate = stage2( keyPair );
			System.out.println( String.format( "Certificado Cliente:\n%s", clientCertificate ) );
			System.out.println( "Fin Etapa 2" );

			// ETAPA 3:
			System.out.println( "Inicio Etapa 3" );
			serverCertificate = stage3( );
			System.out.println( String.format( "Certificado Servidor:\n%s", serverCertificate ) );
			System.out.println( "Fin Etapa 3" );

			// ETAPA 4:
			System.out.println( "Inicio Etapa 4" );
			stage4 = stage4( lat, lon, keyPair, serverCertificate );
			System.out.println( Arrays.toString( Arrays.stream( stage4 ).map( Arrays::toString ).toArray( String[]::new ) ) );
			System.out.println( "Fin Etapa 4" );
		}
		catch( Exception e )
		{
			e.printStackTrace( );
			out.println( join( Protocolo.SEP.value, Protocolo.ESTADO.value, Protocolo.ERROR.value ) );
		}
	}

	private void stage1( ) throws Exception
	{
		String line;
		// INICIO DE SESION
		out.println( Protocolo.HOLA.value );
		line = in.readLine( );
		if( !line.equals( Protocolo.INICIO.value ) )
		{
			throw new Exception( line );
		}

		// SELECCION DE ALGORITMOS
		out.println( join( Protocolo.SEP.value,
						   Protocolo.ALGORITMOS.value,
						   algoritmoSimetrico.getValue( ),
						   algoritmoAsimetrico.getValue( ),
						   algoritmoHMAC.getValue( ) ) );

		readResponseState( in );
	}

	private X509Certificate stage2( KeyPair keyPair ) throws Exception
	{
		// ENVIAR CERTIFICADO
		out.println( Protocolo.CERTCLNT.value );

		X509Certificate certificate = CertificadoDigital.generateCertificate( keyPair );
		byte[] certificateBytes = certificate.getEncoded( );

		outStream.write( certificateBytes );
		outStream.flush( );

		readResponseState( in );
		return certificate;
	}

	private X509Certificate stage3( ) throws Exception
	{
		String line = in.readLine( );
		if( !line.equals( Protocolo.CERTSRV.value ) )
		{
			throw new Exception( line );
		}
		// RECIBIR CERTIFICADO
		X509Certificate certificateServer = CertificadoDigital.loadCertificate( inStream );
		out.println( join( Protocolo.SEP.value, Protocolo.ESTADO.value, Protocolo.OK.value ) );
		return certificateServer;
	}

	private byte[][] stage4( Double[] lat, Double[] lon, KeyPair keyPair, X509Certificate serverCertificate ) throws Exception
	{
		// OBTENCION DE LLAVE SIMÉTRICA DEL SERVIDOR
		String[] data = split( Protocolo.SEP.value, in.readLine( ) );
		if( !data[ 0 ].equals( Protocolo.INICIO.value ) )
		{
			throw new Exception( data[ 0 ] );
		}

		// Etapa 4.1
		// ENCRIPTAR POSICION CON LLAVE SIMETRICA ENVIADA
		// La llave está codificada asimetricamente con la llave pública, toca pasar de Hexa a String
		byte[] cipheredText = hexToByteArray( data[ 1 ] );
		byte[] llaveSimetricaDescifrada = Cifrado.Asimetrico.descifrar( cipheredText, keyPair.getPrivate( ), algoritmoAsimetrico );
		// Valid: System.out.println( new String( llaveSimetricaDescifrada, StandardCharsets.ISO_8859_1 ).getBytes( StandardCharsets.ISO_8859_1 ).length );

		SecretKey key = new SecretKeySpec( llaveSimetricaDescifrada, 0, llaveSimetricaDescifrada.length, algoritmoSimetrico.getValue( ) );

		String pos = join( ",",
						   join( " ", Arrays.stream( lat ).map( String::valueOf ).toArray( String[]::new ) ),
						   join( " ", Arrays.stream( lon ).map( String::valueOf ).toArray( String[]::new ) ) );
		byte[] positionCifradaLlaveSimetrica = Cifrado.Simetrico.cifrar( pos, key, algoritmoSimetrico );

		// ENVIO DE POSICION ENCRIPTADO CON LLAVE SIMETRICA
		out.println( join( Protocolo.SEP.value, Protocolo.ACT1.value, arrayToHexString( positionCifradaLlaveSimetrica ) ) );

		// Etapa 4.2
		// ENCRIPTAR POSICION CON HMAC
		byte[] positionCifradoHMAC = Cifrado.HMAC.mac( pos, key, algoritmoHMAC );
		// ENCRIPTAR RESULTADO DE HMAC CON LLAVE PUBLICA DEL SERVIDOR
		byte[] positionCifrada = Cifrado.Asimetrico.cifrar( positionCifradoHMAC, serverCertificate.getPublicKey( ), algoritmoAsimetrico );

		// ENVIO DE POSICION ENCRIPTADO CON LLAVE PÚBLICA DEL SERVIDOR Y HMAC
		out.println( join( Protocolo.SEP.value, Protocolo.ACT2.value, arrayToHexString( positionCifrada ) ) );

		readResponseState( in );

		return new byte[][]{ positionCifradaLlaveSimetrica, positionCifradoHMAC };
	}

	private void readResponseState( BufferedReader in ) throws Exception
	{
		String line = in.readLine( );
		if( line.equals( Protocolo.ERROR.value ) )
		{
			throw new Exception( line );
		}
	}
}
