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

/**
 * Clase que se encarga de cifrar o descifrar con diferentes tipos de cifrado
 * <ul>
 * <li> {@link Simetrico}</li>
 * <li> {@link Asimetrico}</li>
 * <li> {@link HMAC}</li>
 * </ul>
 *
 * @author David Narvaez - 201516897
 * @author Daniela Jaimes - 201531521
 */
public abstract class Cifrado extends Seguridad
{
	// ---------------------------------
	// Tipos de Cifrado
	// ---------------------------------

	/**
	 * Clase que se encarga de cifrar o descifrar la información con un algoritmo de encriptación simétrico
	 */
	public static class Simetrico extends Cifrado
	{
		/**
		 * Cifra un arreglo de bytes con la llave y el algoritmo proporionado
		 *
		 * @param cadena    Arreglo de bytes con la información a cifrar
		 * @param key       Llave con la que cifrará la información
		 * @param algorithm Algoritmo que se utilizará para cifrar. Ver {@link CipherAlgorithm}
		 * @return Arreglo de bytes con la información cifrada con la llave y el algoritmo proporcionado
		 * @throws IllegalBlockSizeException En caso que el bloque a cifrar y la llave no coincidan en tamaño
		 * @throws InvalidKeyException       En caso que haya un error con la llave
		 * @throws NoSuchPaddingException    En caso que haya un error con el padding a utilizar
		 * @throws NoSuchAlgorithmException  En caso que haya un error con el algoritmo a utilizar
		 * @throws BadPaddingException       En caso que haya un error con el padding a la hora de cifrar
		 */
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

		/**
		 * Cifra una cadena de texto (String) con la llave y el algoritmo proporionado
		 *
		 * @param cadena    Cadena de texto a cifrar
		 * @param key       Llave con la que cifrará la información
		 * @param algorithm Algoritmo que se utilizará para cifrar. Ver {@link CipherAlgorithm}
		 * @return Arreglo de bytes con la información cifrada con la llave y el algoritmo proporcionado
		 * @throws IllegalBlockSizeException En caso que el bloque a cifrar y la llave no coincidan en tamaño
		 * @throws InvalidKeyException       En caso que haya un error con la llave
		 * @throws NoSuchPaddingException    En caso que haya un error con el padding a utilizar
		 * @throws NoSuchAlgorithmException  En caso que haya un error con el algoritmo a utilizar
		 * @throws BadPaddingException       En caso que haya un error con el padding a la hora de cifrar
		 */
		public static byte[] cifrar( String cadena, SecretKey key, CipherAlgorithm algorithm ) throws IllegalBlockSizeException, InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException, BadPaddingException
		{
			return cifrar( cadena.getBytes( ), key, algorithm );
		}

		/**
		 * Descifra un arreglo de bytes con la llave y el algoritmo proporionado
		 *
		 * @param cipheredText Arreglo de bytes con la información a cifrar
		 * @param key          Llave con la que descifrará la información
		 * @param algorithm    Algoritmo que se utilizará para descifrar. Ver {@link CipherAlgorithm}
		 * @return Arreglo de bytes con la información descifrada con la llave y el algoritmo proporcionado
		 * @throws IllegalBlockSizeException En caso que el bloque a descifrar y la llave no coincidan en tamaño
		 * @throws InvalidKeyException       En caso que haya un error con la llave
		 * @throws NoSuchPaddingException    En caso que haya un error con el padding a utilizar
		 * @throws NoSuchAlgorithmException  En caso que haya un error con el algoritmo a utilizar
		 * @throws BadPaddingException       En caso que haya un error con el padding a la hora de descifrar
		 */
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

	/**
	 * Clase que se encarga de cifrar o descifrar la información con un algoritmo de encriptación asimétrico
	 */
	public static class Asimetrico extends Cifrado
	{
		/**
		 * Cifra una cadena de texto con la llave y el algoritmo proporionado
		 *
		 * @param cadena    Cadena de texto a cifrar
		 * @param key       Llave con la que cifrará la información
		 * @param algorithm Algoritmo que se utilizará para cifrar. Ver {@link CipherAlgorithm}
		 * @return Arreglo de bytes con la información cifrada con la llave y el algoritmo proporcionado
		 * @throws IllegalBlockSizeException En caso que el bloque a cifrar y la llave no coincidan en tamaño
		 * @throws InvalidKeyException       En caso que haya un error con la llave
		 * @throws NoSuchPaddingException    En caso que haya un error con el padding a utilizar
		 * @throws NoSuchAlgorithmException  En caso que haya un error con el algoritmo a utilizar
		 * @throws BadPaddingException       En caso que haya un error con el padding a la hora de cifrar
		 */
		public static byte[] cifrar( String cadena, Key key, CipherAlgorithm algorithm ) throws IllegalBlockSizeException, InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException, BadPaddingException
		{
			return cifrar( cadena.getBytes( ), key, algorithm );
		}

		/**
		 * Cifra un arreglo de bytes con la llave y el algoritmo proporionado
		 *
		 * @param cadena    Arreglo de bytes a cifrar
		 * @param key       Llave con la que cifrará la información
		 * @param algorithm Algoritmo que se utilizará para cifrar. Ver {@link CipherAlgorithm}
		 * @return Arreglo de bytes con la información cifrada con la llave y el algoritmo proporcionado
		 * @throws IllegalBlockSizeException En caso que el bloque a cifrar y la llave no coincidan en tamaño
		 * @throws InvalidKeyException       En caso que haya un error con la llave
		 * @throws NoSuchPaddingException    En caso que haya un error con el padding a utilizar
		 * @throws NoSuchAlgorithmException  En caso que haya un error con el algoritmo a utilizar
		 * @throws BadPaddingException       En caso que haya un error con el padding a la hora de cifrar
		 */
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

		/**
		 * Descifra un arreglo de bytes con la llave y el algoritmo proporionado
		 *
		 * @param cipheredText Arreglo de bytes a descifrar
		 * @param key          Llave con la que descifrará la información
		 * @param algorithm    Algoritmo que se utilizará para descifrar. Ver {@link CipherAlgorithm}
		 * @return Arreglo de bytes con la información descifrada con la llave y el algoritmo proporcionado
		 * @throws IllegalBlockSizeException En caso que el bloque a descifrar y la llave no coincidan en tamaño
		 * @throws InvalidKeyException       En caso que haya un error con la llave
		 * @throws NoSuchPaddingException    En caso que haya un error con el padding a utilizar
		 * @throws NoSuchAlgorithmException  En caso que haya un error con el algoritmo a utilizar
		 * @throws BadPaddingException       En caso que haya un error con el padding a la hora de descifrar
		 */
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

	/**
	 * Clase que se encarga de realizar la función de Hash MAC y las operaciones Digest
	 */
	public static class HMAC extends Cifrado
	{
		/**
		 * Cifra un arreglo de bytes con la función Hash MAC
		 *
		 * @param msg       Arreglo de bytes con la información a aplicarle la función HMAC
		 * @param key       Llave con la cual se realizará la función
		 * @param algorithm Algoritmo para la función de Hash MAC. Ver {@link MacAlgorithm}
		 * @return Arreglo de bytes encriptados con la función de HMAC y la llave proveida
		 * @throws NoSuchAlgorithmException En caso que el algoritmo proporcionado no exista
		 * @throws InvalidKeyException      En caso que la llave proporcionada sea incorrecta
		 */
		public static byte[] mac( byte[] msg, Key key, MacAlgorithm algorithm ) throws NoSuchAlgorithmException, InvalidKeyException
		{
			Mac mac = Mac.getInstance( algorithm.value );
			mac.init( key );
			return mac.doFinal( msg );
		}

		/**
		 * Calcula el digest de un mensage representado por un arreglo de bytes y un algoritmo dado
		 *
		 * @param msg       Arreglo de bytes con la información a la cual se calculará el Digest
		 * @param algorithm Algoritmo de Digest. Ver {@link DigestAlgorithm}
		 * @return Arreglo de Bytes con el Digest de la información dada
		 * @throws NoSuchAlgorithmException En caso que no exista el algoritmo proporcionado
		 */
		public byte[] calcularDigest( byte[] msg, DigestAlgorithm algorithm ) throws NoSuchAlgorithmException
		{
			try
			{
				MessageDigest md5 = MessageDigest.getInstance( algorithm.value );
				md5.update( msg );
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

	/**
	 * Enumeración que contiene los Algoritmos de Cifrado soportados por {@link Cipher}
	 */
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

		/**
		 * Cadena de texto que representa el algoritmo
		 */
		private String value;

		CipherAlgorithm( String value )
		{
			this.value = value;
		}

		/**
		 * Retorna la cadena de texto que representa el algoritmo
		 *
		 * @return Cadena de texto que representa el algoritmo
		 */
		public String getValue( )
		{
			return value;
		}
	}

	/**
	 * Enumeración que contiene los Algoritmos de cifrado soportado por {@link Mac}
	 */
	public enum MacAlgorithm
	{
		HmacMD5( "HMACMD5" ),
		HmacSHA1( "HMACSHA1" ),
		HmacSHA256( "HMACSHA256" );

		/**
		 * Cadena de texto que representa el algoritmo
		 */
		private String value;

		MacAlgorithm( String value )
		{
			this.value = value;
		}

		/**
		 * Retorna la cadena de texto que representa el algoritmo
		 *
		 * @return Cadena de texto que representa el algoritmo
		 */
		public String getValue( )
		{
			return value;
		}
	}

	/**
	 * Enumeración que contiene los Algoritmos para el cálculo del Digest de un Mensaje {@link MessageDigest}
	 */
	public enum DigestAlgorithm
	{
		MD5( "MD5" ),
		SHA1( "SHA-1" ),
		SHA256( "SHA-256" );

		/**
		 * Cadena de texto que representa el algoritmo
		 */
		private String value;

		DigestAlgorithm( String value )
		{
			this.value = value;
		}

		/**
		 * Retorna la cadena de texto que representa el algoritmo
		 *
		 * @return Cadena de texto que representa el algoritmo
		 */
		public String getValue( )
		{
			return value;
		}
	}

	/**
	 * Enumeración que contiene los modos de cifrado para {@link Cipher}
	 */
	private enum CipherMode
	{
		ENCRIPTAR( Cipher.ENCRYPT_MODE ),
		DESENCRIPTAR( Cipher.DECRYPT_MODE );

		/**
		 * Modo de cifrado soportado por el método {@link Cipher#init(int, Key)}
		 */
		private int value;

		CipherMode( int value )
		{
			this.value = value;
		}

		/**
		 * Retorna el modo de cifrado soportado por el método {@link Cipher#init(int, Key)}
		 *
		 * @return Modo de cifrado soportado por el método {@link Cipher#init(int, Key)}
		 */
		public int getValue( )
		{
			return value;
		}
	}

	// ---------------------------------
	// Metodos
	// ---------------------------------

	/**
	 * Realiza el cifrado con el modo, el algoritmo y la llave proporcionada al arreglo de bytes dado por parámetro
	 *
	 * @param toApply   Arreglo de bytes al cual se le aplicará el cifrado
	 * @param algorithm Algoritmo que se utilizará para realizar el cifrado
	 * @param mode      Modo de cifrado. Ver {@link CipherMode}
	 * @param key       Llave con la cual se va a realizar el cifrado
	 * @return Arreglo de bytes con la información cifrada con la llave, el algoritmo y el modo proporcionado
	 * @throws IllegalBlockSizeException En caso que el bloque a cifrar y la llave no coincidan en tamaño
	 * @throws InvalidKeyException       En caso que haya un error con la llave
	 * @throws NoSuchPaddingException    En caso que haya un error con el padding a utilizar
	 * @throws NoSuchAlgorithmException  En caso que haya un error con el algoritmo a utilizar
	 * @throws BadPaddingException       En caso que haya un error con el padding a la hora de cifrar
	 */
	static byte[] doCipher( byte[] toApply, String algorithm, CipherMode mode, Key key )
			throws BadPaddingException, IllegalBlockSizeException, NoSuchPaddingException,
			NoSuchAlgorithmException, InvalidKeyException
	{
		Cipher cipher = Cipher.getInstance( algorithm );
		cipher.init( mode.getValue( ), key );
		return cipher.doFinal( toApply );
	}
}
