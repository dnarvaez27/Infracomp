package infracomp.caso2;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.security.Security;

public class Servidor
{
	private static Cliente client;

	public static void main( String[] args ) throws IOException, NoSuchAlgorithmException
	{

		Security.addProvider( new BouncyCastleProvider( ) );

		ServerSocket ss = new ServerSocket( client.PUERTO );
		System.out.println( "El servidor se encuentra corriendo y esperando en este momento." );

		while( true )
		{
			Socket s = ss.accept( );
			new Sockets( s ).start( );
		}

		//Certificado intento con Bouncy castle
		//Usar bouncy castle para generar el certificado tipo X509
		//TODO: ver porque dice unrechable statement hhhmmm
		// MessageDigest message = MessageDigest.getInstance("X509");
		// //leer el certificado
		//byte[] buffer = new byte[1000]; //Verificar el tamaño del byte
		// //Crear un fileInputStream
		//FileInputStream in = new FileInputStream(args[0]);
		////leerlo
		//int read = in.read(buffer, 0, 1000);
		//while (read != -1) {
		//  message.update(buffer);
		//read = in.read(buffer, 0, 1000);
		//}
		// //cierro fileInputStream
		// in.close();

		//Estructural el resume
		// byte[] resumen = message.digest();

		// Mostrar certificado
		//System.out.println("RESUMEN:");
		//System.out.write(resumen, 0, resumen.length);
		//System.out.println();
	}
}

