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

/**
 * Clase abstracta que representa a los diferentes tipos de encriptación de información
 *
 * @author David Narvaez - 201516897
 * @author Daniela Jaimes - 201531521
 */
public abstract class Seguridad
{
	/**
	 * Clase que alberga las enumeraciones que proveen los algoritmos para la obtención de las llaves
	 */
	public abstract static class KeyAlgorithm
	{
		/**
		 * Enumeración que contiene los algoritmos soportados para la generación de una {@link SecretKey}
		 */
		public enum Single
		{
			AES( "AES", 128 ),
			DES( "DES", 56 ),
			DESede( "DESede", 168 ),
			HmacSHA1( "HmacSHA1", -1 ),
			HmacSHA256( "HmacSHA256", -1 );

			/**
			 * Cadena de texto que representa el algoritmo
			 */
			private String value;

			/**
			 * Longitud de la llave del algoritmo. -1 en caso que no se provea
			 */
			private int length;

			Single( String value, int length )
			{
				this.value = value;
				this.length = length;
			}
		}

		/**
		 * Enumeración que contiene los algoritmos soportados para la generación de un {@link KeyPair}
		 */
		public enum Pair
		{
			RSA_1024( "RSA", 1024 ),
			RSA_2048( "RSA", 2048 ),
			DiffieHellman( "DiffieHellman", 1024 ),
			DSA( "DSA", 1024 );

			/**
			 * Cadena de texto que representa el algoritmo
			 */
			private String value;

			/**
			 * Longitud de la llave del algoritmo. -1 en caso que no se provea
			 */
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

	/**
	 * Genera un {@link KeyPair} (Llave pública y privada) con el algoritmo dado por parámetro
	 *
	 * @param algoritmo Algoritmo para la generación de las llaves {@link KeyAlgorithm.Pair}
	 * @return KeyPair generado a partir del algoritmo proporcionado
	 * @throws NoSuchAlgorithmException En caso que no exista el algoritmo proporcionado
	 */
	public static KeyPair getKeyPair( KeyAlgorithm.Pair algoritmo ) throws NoSuchAlgorithmException
	{
		KeyPairGenerator generator = KeyPairGenerator.getInstance( algoritmo.value );
		if( algoritmo.length != -1 )
		{
			generator.initialize( algoritmo.length );
		}
		return generator.generateKeyPair( );
	}

	/**
	 * Genera un {@link SecretKey} con el algoritmo dado por parámetro
	 *
	 * @param algoritmo Algoritmo para la generación de la llave {@link KeyAlgorithm.Single}
	 * @return SecretKey generado a partir del algoritmo proporcionado
	 * @throws NoSuchAlgorithmException En caso que no exista el algoritmo proprocionado
	 */
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
