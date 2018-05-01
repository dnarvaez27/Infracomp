package infracomp.caso2;

import infracomp.caso2.seguridad.CertificadoDigital;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.openssl.jcajce.JcaPEMWriter;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.security.KeyPair;
import java.security.cert.X509Certificate;
import java.util.Arrays;

import static infracomp.caso2.Utils.join;
import static infracomp.caso2.Utils.split;

/**
 * Clase que se encargará del protocolo del cliente para la unidad de transporte
 *
 * @author David Narvaez - 201516897
 * @author Daniela Jaimes - 201531521
 */
public class ClienteNoSec extends Cliente implements Runnable
{
	/**
	 * Instancia un Cliente iniciando los canales de comunicación con el Socket
	 *
	 * @throws IOException En caso que no sea posible establecer la comunicación con el Servidor
	 */
	public ClienteNoSec( boolean logs, Integer[] status ) throws IOException
	{
		super( logs, status );
	}

	@Override
	protected X509Certificate stage2( KeyPair keyPair ) throws Exception
	{
		// ENVIAR CERTIFICADO
		out.println( Protocolo.CERTCLNT.value );

		X509Certificate certificate = CertificadoDigital.generateCertificate( keyPair );

		StringWriter wr = new StringWriter( );
		JcaPEMWriter pemWriter = new JcaPEMWriter( wr );
		pemWriter.writeObject( certificate );
		pemWriter.flush( );
		pemWriter.close( );
		out.println( wr.toString( ) );

		readResponseState( );
		return certificate;
	}

	@Override
	protected X509Certificate stage3( ) throws Exception
	{
		String linea = in.readLine( );
		StringBuilder strToDecode = new StringBuilder( );
		strToDecode.append( linea );
		while( !linea.isEmpty( ) )
		{
			strToDecode.append( linea ).append( "\n" );
			linea = in.readLine( );
		}

		StringReader rea = new StringReader( strToDecode.toString( ) );
		PemReader pr = new PemReader( rea );
		PemObject pemcertificadoPuntoAtencion = pr.readPemObject( );
		X509CertificateHolder certHolder = new X509CertificateHolder( pemcertificadoPuntoAtencion.getContent( ) );
		X509Certificate certificado = ( new JcaX509CertificateConverter( ) ).getCertificate( certHolder );
		pr.close( );

		out.println( join( Protocolo.SEP.value, Protocolo.ESTADO.value, Protocolo.OK.value ) );
		return null;
	}

	/**
	 * Etapa 4:
	 * <ul>
	 * <li>Se obtiene la llave simétrica enviada por el Servidor</li>
	 * <li>Se envían las coordenadas cifradas con la llave simétrica obtenida</li>
	 * <li>Se envían las coordenadas encriptadas con la llave pública del Servidor y la función HMAC establecida</li>
	 * </ul>
	 */
	@Override
	protected byte[][] stage4( Double[] lat, Double[] lon, KeyPair keyPair, X509Certificate serverCertificate ) throws Exception
	{
		String line = in.readLine( );
		System.out.println( line );
		String[] data = split( Protocolo.SEP.value, evaluateForError( line ) );
		if( !data[ 0 ].equals( Protocolo.INICIO.value ) )
		{
			throw new Exception( data[ 0 ] );
		}

		registro.start( Registros.ACTUALIZACION );
		String pos = join( ",",
						   join( " ", Arrays.stream( lat ).map( String::valueOf ).toArray( String[]::new ) ),
						   join( " ", Arrays.stream( lon ).map( String::valueOf ).toArray( String[]::new ) ) );

		out.println( join( Protocolo.SEP.value, Protocolo.ACT1.value, pos ) );
		out.println( join( Protocolo.SEP.value, Protocolo.ACT2.value, pos ) );

		readResponseState( );
		registro.stop( Registros.ACTUALIZACION );

		return new byte[][]{ String.valueOf( pos ).getBytes( ), String.valueOf( pos ).getBytes( ) };
	}
}
