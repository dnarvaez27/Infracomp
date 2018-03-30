package infracomp.caso2.seguridad;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.Mac;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public abstract class Cifrado extends Seguridad
{
	// ---------------------------------
	// Tipos de Cifrado
	// ---------------------------------
	public static class Simetrico extends Cifrado
	{
		public static byte[] cifrar( byte[] cadena, SecretKey key, CipherAlgorithm algorithm ) throws IllegalBlockSizeException, InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException, BadPaddingException
		{
			try
			{
				return doCipher( cadena, algorithm.value, CipherMode.ENCRIPTAR, key );
			}
			catch( Exception e )
			{
				e.printStackTrace( );
				throw e;
			}
		}

		public static byte[] cifrar( String cadena, SecretKey key, CipherAlgorithm algorithm ) throws IllegalBlockSizeException, InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException, BadPaddingException
		{
			return cifrar( cadena.getBytes( ), key, algorithm );
		}

		public static byte[] descifrar( byte[] cipheredText, SecretKey key, CipherAlgorithm algorithm ) throws IllegalBlockSizeException, InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException, BadPaddingException
		{
			try
			{
				return doCipher( cipheredText, algorithm.value, CipherMode.DESENCRIPTAR, key );
			}
			catch( Exception e )
			{
				e.printStackTrace( );
				throw e;
			}
		}
	}

	public static class Asimetrico extends Cifrado
	{
		public static byte[] cifrar( String cadena, Key key, CipherAlgorithm algorithm ) throws IllegalBlockSizeException, InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException, BadPaddingException
		{
			return cifrar( cadena.getBytes( ), key, algorithm );
		}

		public static byte[] cifrar( byte[] cadena, Key key, CipherAlgorithm algorithm ) throws IllegalBlockSizeException, InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException, BadPaddingException
		{
			try
			{
				return doCipher( cadena, algorithm.value, CipherMode.ENCRIPTAR, key );
			}
			catch( Exception e )
			{
				e.printStackTrace( );
				throw e;
			}
		}

		public static byte[] descifrar( byte[] cipheredText, Key key, CipherAlgorithm algorithm ) throws IllegalBlockSizeException, InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException, BadPaddingException
		{
			try
			{
				return doCipher( cipheredText, algorithm.value, CipherMode.DESENCRIPTAR, key );
			}
			catch( Exception e )
			{
				e.printStackTrace( );
				throw e;
			}
		}
	}

	public static class HMAC extends Cifrado
	{
		public static byte[] mac( String msg, Key key, MacAlgorithm algorithm ) throws NoSuchAlgorithmException, InvalidKeyException
		{
			Mac mac = Mac.getInstance( algorithm.value );
			mac.init( key );
			return mac.doFinal( msg.getBytes( ) );
		}

		public byte[] calcularDigest( String str, DigestAlgorithm algorithm ) throws NoSuchAlgorithmException
		{
			try
			{
				MessageDigest md5 = MessageDigest.getInstance( algorithm.value );
				md5.update( str.getBytes( ) );
				return md5.digest( );
			}
			catch( Exception e )
			{
				System.out.println( "Excepcion: " + e.getMessage( ) );
				throw e;
			}
		}
	}

	// ---------------------------------
	// Algoritmos y Modo de Cifrado
	// ---------------------------------
	public enum CipherAlgorithm
	{
		AES( "AES" ),
		AES_CBC_NoPadding( "AES/CBC/NoPadding" ),
		AES_CBC_PKCS5Padding( "AES/CBC/PKCS5Padding" ),
		AES_ECB_NoPadding( "AES/ECB/NoPadding" ),
		AES_ECB_PKCS5Padding( "AES/ECB/PKCS5Padding" ),
		DES_CBC_NoPadding( "DES/CBC/NoPadding" ),
		DES_CBC_PKCS5Padding( "DES/CBC/PKCS5Padding" ),
		DES_ECB_NoPadding( "DES/ECB/NoPadding" ),
		DES_ECB_PKCS5Padding( "DES/ECB/PKCS5Padding" ),
		DESede_CBC_NoPadding( "DESede/CBC/NoPadding" ),
		DESede_CBC_PKCS5Padding( "DESede/CBC/PKCS5Padding" ),
		DESede_ECB_NoPadding( "DESede/ECB/NoPadding" ),
		DESede_ECB_PKCS5Padding( "DESede/ECB/PKCS5Padding" ),
		RSA_ECB_PKCS1Padding( "RSA/ECB/PKCS1Padding" ),
		RSA_ECB_OAEPWithSHA1AndMGF1Padding( "RSA/ECB/OAEPWithSHA-1AndMGF1Padding" ),
		RSA_ECB_OAEPWithSHA256AndMGF1Padding( "RSA/ECB/OAEPWithSHA-256AndMGF1Padding" ),
		RSA( "RSA" );

		private String value;

		CipherAlgorithm( String value )
		{
			this.value = value;
		}

		public String getValue( )
		{
			return value;
		}
	}

	public enum MacAlgorithm
	{
		HmacMD5( "HMACMD5" ),
		HmacSHA1( "HMACSHA1" ),
		HmacSHA256( "HMACSHA256" );

		private String value;

		MacAlgorithm( String value )
		{
			this.value = value;
		}

		public String getValue( )
		{
			return value;
		}
	}

	public enum DigestAlgorithm
	{
		MD5( "MD5" ),
		SHA1( "SHA-1" ),
		SHA256( "SHA-256" );

		private String value;

		DigestAlgorithm( String value )
		{
			this.value = value;
		}
	}

	private enum CipherMode
	{
		ENCRIPTAR( Cipher.ENCRYPT_MODE ),
		DESENCRIPTAR( Cipher.DECRYPT_MODE );

		private int value;

		CipherMode( int value )
		{
			this.value = value;
		}

		public int getValue( )
		{
			return value;
		}
	}

	// ---------------------------------
	// Metodos
	// ---------------------------------
	static byte[] doCipher( byte[] toApply, String algorithm, CipherMode mode, Key key )
			throws BadPaddingException, IllegalBlockSizeException, NoSuchPaddingException,
			NoSuchAlgorithmException, InvalidKeyException
	{
		Cipher cipher = Cipher.getInstance( algorithm );
		cipher.init( mode.getValue( ), key );
		return cipher.doFinal( toApply );
	}
}
