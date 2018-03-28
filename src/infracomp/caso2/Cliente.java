package infracomp.caso2;

import org.bouncycastle.jce.X509Principal;
import org.bouncycastle.x509.X509V3CertificateGenerator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.Date;

public class Cliente
{
	public static int KEY_LENGHT = 1024;

	public final static int PUERTO = 8080;

	public final static String ip = "localhost";

	private String asimetrico;

	private String simetrico;

	private String hashMac;

	private boolean ejecutar = false;

	public void ejecutar( ) throws IOException
	{
		//Conection

		Socket sock = new Socket( ip, PUERTO );

		PrintWriter pw = new PrintWriter( sock.getOutputStream( ), true );
		BufferedReader br = new BufferedReader( new InputStreamReader( System.in ) );
		BufferedReader stdIn = new BufferedReader( new InputStreamReader( System.in ) );
		String fromServer = null;
		String fromUser;
		int contador = 0;

		System.out.print( "Escriba el mensaje para enviar:" );
		fromUser = "HOLA";
		pw.println( fromUser );
		fromServer = br.readLine( );
		if( fromServer.equals( "INICIO" ) )
		{
			fromUser = "ALGORITMOS:AES:RSA:HMACMD5";
			if( fromUser != null && !fromUser.equals( "-1" ) )
			{
				System.out.println( "Cliente: " + fromUser );
				if( fromUser.contains( "ALGORITMOS" ) )
				{
					String algoritmos = fromUser;
					String[] algs = algoritmos.split( ":" );
					simetrico = algs[ 1 ];
					asimetrico = algs[ 2 ];
					hashMac = algs[ 3 ];
				}
			}
			fromServer = br.readLine( );
			if( fromServer.equalsIgnoreCase( "OK" ) )
			{
				try
				{
					X509Certificate certificado = generateCertificate( );
					pw.println( certificado );
				}
				catch( Exception e )
				{
					e.printStackTrace( );
				}
				// aqui deberia LEER EL CERTIFICADO DEL SERVIDOR .... como?
				//TODO: TERMINAR
				if( "OK" == "OK" )
				{

					fromUser = "OK";
				}
				else
				{
					fromUser = "ERROR";
				}
				//TODO: enviar ACT1 y act2
			}
			else if( fromServer.equalsIgnoreCase( "ERROR" ) )
			{
				pw.println( "error" );
			}

		}
	}

	//prueba de coneccion ente Servidor y cliente
	//   String msg;
	// while ((msg = br.readLine()) != null) {
	// System.out.println("Sending "+msg);
	//   pw.println(msg);
	//}
	// metodo para generar el certificado
	private X509Certificate generateCertificate( ) throws Exception
	{

		// fecha inicial
		Date validityBeginDate = new Date( System.currentTimeMillis( ) - 24 * 60 * 60 * 1000 );
		// fecha final
		Date validityEndDate = new Date( System.currentTimeMillis( ) + 2 * 365 * 24 * 60 * 60 * 1000 );

		// Generar clave publica y privada
		KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance( "RSA" );
		keyPairGenerator.initialize( 1024, new SecureRandom( ) );

		KeyPair keyPair = keyPairGenerator.generateKeyPair( );
		KeyPair duplicado = keyPair;

		// Generar certificado tipo X509
		X509V3CertificateGenerator certificado = new X509V3CertificateGenerator( );
		X509Principal dnName = new X509Principal( "CN=John DoeS" );

		certificado.setSerialNumber( java.math.BigInteger.valueOf( System.currentTimeMillis( ) ) );
		certificado.setSubjectDN( dnName );
		certificado.setIssuerDN( dnName );
		certificado.setNotBefore( validityBeginDate );
		certificado.setNotAfter( validityEndDate );
		certificado.setPublicKey( keyPair.getPublic( ) );
		certificado.setSignatureAlgorithm( "SHA1withRSA" );

		java.security.cert.X509Certificate cert = certificado.generate( keyPair.getPrivate( ) );
		return cert;
	}

}
