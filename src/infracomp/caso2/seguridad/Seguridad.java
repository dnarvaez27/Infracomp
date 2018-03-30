package infracomp.caso2.seguridad;

import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

public abstract class Seguridad
{
	public abstract static class KeyAlgorithm
	{
		public enum Single
		{
			AES( "AES", 128 ),
			DES( "DES", 56 ),
			DESede( "DESede", 168 ),
			HmacSHA1( "HmacSHA1", -1 ),
			HmacSHA256( "HmacSHA256", -1 );

			private String value;

			private int length;

			Single( String value, int length )
			{
				this.value = value;
				this.length = length;
			}
		}

		public enum Pair
		{
			RSA_1024( "RSA", 1024 ),
			RSA_2048( "RSA", 2048 ),
			DiffieHellman( "DiffieHellman", 1024 ),
			DSA( "DSA", 1024 );

			private String value;

			private int length;

			Pair( String value, int length )
			{
				this.value = value;
				this.length = length;
			}

			public String getValue( )
			{
				return value;
			}

			public int getLength( )
			{
				return length;
			}
		}
	}

	public static KeyPair getKeyPair( KeyAlgorithm.Pair algoritmo ) throws NoSuchAlgorithmException
	{
		KeyPairGenerator generator = KeyPairGenerator.getInstance( algoritmo.value );
		if( algoritmo.length != -1 )
		{
			generator.initialize( algoritmo.length );
		}
		return generator.generateKeyPair( );
	}

	public static SecretKey getKey( KeyAlgorithm.Single algoritmo ) throws NoSuchAlgorithmException
	{
		KeyGenerator generator = KeyGenerator.getInstance( algoritmo.value );
		if( algoritmo.length != -1 )
		{
			generator.init( algoritmo.length );
		}
		return generator.generateKey( );
	}
}
