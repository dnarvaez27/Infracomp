package infracomp.caso2.seguridad;

import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.KeyPair;
import java.security.PublicKey;
import java.security.Security;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

/**
 * Clase que se encarga de los Certificados Digitales
 *
 * @author David Narvaez - 201516897
 * @author Daniela Jaimes - 201531521
 */
public class CertificadoDigital extends Seguridad
{
	/**
	 * Proveedor de Seguridad de BouncyCastle
	 */
	private static BouncyCastleProvider provider = new BouncyCastleProvider( );

	/**
	 * Genera un Certificado X509 v3 a partir de un {@link KeyPair} (Llave pública y privada)
	 *
	 * @param keyPair Keypair con el cual se generará el certificado
	 * @return Certificado X509 generado a partir del KeyPair dado por parámetro
	 * @throws OperatorCreationException En caso que haya un problema creando la firma para el certificado
	 * @throws CertificateException      En caso que haya un problema construyendo el certificado con las opciones dadas
	 */
	public static X509Certificate generateCertificate( final KeyPair keyPair ) throws OperatorCreationException, CertificateException
	{
		// Se establece el atributo de nombre para la entidad de X.500
		X500NameBuilder builder = new X500NameBuilder( );
		builder.addRDN( BCStyle.CN, "dd.infracomp.caso2" );
		builder.addRDN( BCStyle.C, "Colombia" );
		builder.addRDN( BCStyle.ST, "Bogota" );
		builder.addRDN( BCStyle.O, "Universidad de Los Andes" );
		builder.addRDN( BCStyle.OU, "Infraestructura Computacional" );
		final X500Name dnName = builder.build( );

		// Se generan las fechas de validéz del certificado
		Date[] range = getRange( Calendar.YEAR, 1 );
		final Date startDate = range[ 0 ];
		final Date endDate = range[ 1 ];

		// Se genera el numero serial del certificado
		final BigInteger certSerialNumber = new BigInteger( 128, new Random( ) );

		// Se establece el algoritmo para la firma del certificado
		// Se construye el ContentSigner
		ContentSigner contentSigner = new JcaContentSignerBuilder( "SHA256WithRSAEncryption" ).build( keyPair.getPrivate( ) );
		// Se genera el CertificateBuilder
		JcaX509v3CertificateBuilder certificateBuilder = new JcaX509v3CertificateBuilder(
				dnName,
				certSerialNumber,
				startDate,
				endDate,
				dnName,
				keyPair.getPublic( )
		);

		return new JcaX509CertificateConverter( )
				.setProvider( provider )
				.getCertificate( certificateBuilder.build( contentSigner ) );
	}

	/**
	 * Carga un Certificado X509 desde un Stream de datos
	 *
	 * @param stream Stream de datos de donde se leerá el certificado
	 * @return Certificado X509 v3 a partir de los datos leidos del Stream
	 * @throws CertificateException En caso que haya un problema generando el certificado a partir de la información recibida
	 * @throws IOException          En caso que haya un problema al leer la información del Stream
	 */
	public static X509Certificate loadCertificate( InputStream stream ) throws CertificateException, IOException
	{
		CertificateFactory certificateFactory = CertificateFactory.getInstance( "X.509" );
		byte[] certificadoBytes = new byte[ 5000 ];
		int read = stream.read( certificadoBytes );
		ByteArrayInputStream inputStream = new ByteArrayInputStream( certificadoBytes );
		return ( X509Certificate ) certificateFactory.generateCertificate( inputStream );
	}

	/**
	 * Verifica si el Certificado dado es válido
	 *
	 * @param cert      Certificado a validar
	 * @param publicKey Llave pública con la cual se envió el certificado
	 * @return True si el certificado es válido, False de lo contrario
	 */
	public static boolean isValid( Certificate cert, PublicKey publicKey )
	{
		try
		{
			if( cert instanceof X509Certificate )
			{
				( ( X509Certificate ) cert ).checkValidity( );
			}
			cert.verify( publicKey, provider );
			return true;
		}
		catch( Exception e )
		{
			System.err.println( e.getMessage( ) );
			return false;
		}
	}

	/**
	 * Obtiene un rango de fechas entre el instante del momento capturado por el sistema
	 * {@link System#currentTimeMillis()} y el tiempo dado por parámetro
	 *
	 * @param type   Tipo de unidad de tiempo a agregar. Ver {@link Calendar#YEAR}, {@link Calendar#MONTH}, ...
	 * @param amount Cantidad de unidades de tiempo a agregar
	 * @return Arreglo de dos posiciones, en el cual se encuentran los rangos de fechas generados
	 */
	private static Date[] getRange( int type, int amount )
	{
		// Se guarda la hora actual
		long now = System.currentTimeMillis( );
		final Date startDate = new Date( now );

		// Se guarda la hora final
		Calendar calendar = Calendar.getInstance( );
		calendar.setTime( startDate );
		calendar.add( type, amount );
		final Date endDate = calendar.getTime( );

		return new Date[]{ startDate, endDate };
	}

	static
	{
		Security.addProvider( provider ); // Se agrega el proveedor de seguridad al manejador de seguridad
	}
}
