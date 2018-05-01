//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package server;

import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.X509Extension;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509ExtensionUtils;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;

import java.math.BigInteger;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.Date;

public class Seguridad
{
	private static final Date NOT_BEFORE = new Date( System.currentTimeMillis( ) - 31536000000L );

	private static final Date NOT_AFTER = new Date( System.currentTimeMillis( ) + 3153600000000L );

	public static final String RSA = "RSA";

	public static final String HMACMD5 = "HMACMD5";

	public static final String HMACSHA1 = "HMACSHA1";

	public static final String HMACSHA256 = "HMACSHA256";

	public static final String RC4 = "RC4";

	public static final String BLOWFISH = "BLOWFISH";

	public static final String AES = "AES";

	public static final String DES = "DES";

	public Seguridad( )
	{
	}

	public static X509Certificate generateV3Certificate( KeyPair pair ) throws Exception
	{
		PublicKey subPub = pair.getPublic( );
		PrivateKey issPriv = pair.getPrivate( );
		PublicKey issPub = pair.getPublic( );
		JcaX509ExtensionUtils extUtils = new JcaX509ExtensionUtils( );
		X509v3CertificateBuilder v3CertGen = new JcaX509v3CertificateBuilder( new X500Name( "CN=0.0.0.0, OU=None, O=None, L=None, C=None" ), new BigInteger( 128, new SecureRandom( ) ), new Date( System.currentTimeMillis( ) ), new Date( System.currentTimeMillis( ) + 8640000000L ), new X500Name( "CN=0.0.0.0, OU=None, O=None, L=None, C=None" ), subPub );
		v3CertGen.addExtension( X509Extension.subjectKeyIdentifier, false, extUtils.createSubjectKeyIdentifier( subPub ) );
		v3CertGen.addExtension( X509Extension.authorityKeyIdentifier, false, extUtils.createAuthorityKeyIdentifier( issPub ) );
		return ( new JcaX509CertificateConverter( ) ).setProvider( "BC" ).getCertificate( v3CertGen.build( ( new JcaContentSignerBuilder( "MD5withRSA" ) ).setProvider( "BC" ).build( issPriv ) ) );
	}
}
