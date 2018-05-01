package infracomp.caso2;

import javax.xml.bind.DatatypeConverter;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

/**
 * Clase de utilidades para el intercambio de información en el protocolo
 *
 * @author David Narvaez - 201516897
 * @author Daniela Jaimes - 201531521
 */
class Utils
{
	/**
	 * Une los items dados por parámetro con el appender proveido
	 *
	 * @param appender Token por el que unirá los items
	 * @param items    Items a unir
	 * @return String con los items unidos por el token proveido
	 */
	static String join( String appender, String... items )
	{
		StringBuilder sBuilder = new StringBuilder( );
		for( String s : items )
		{
			sBuilder.append( s ).append( appender );
		}
		sBuilder.deleteCharAt( sBuilder.length( ) - 1 );
		return sBuilder.toString( );
	}

	/**
	 * Separa el string por el token dados por parámetro
	 *
	 * @param token Token por el cual separará el string
	 * @param str   String a separar
	 * @return Arreglo de String con los items separados por el token del string
	 */
	static String[] split( String token, String str )
	{
		StringTokenizer tokenizer = new StringTokenizer( str );
		String[] data = new String[ tokenizer.countTokens( ) + 1 ];
		for( int i = 0; i < data.length && tokenizer.hasMoreElements( ); i++ )
		{
			data[ i ] = tokenizer.nextToken( token );
		}
		return data;
	}

	/**
	 * Convierte un arreglo de bytes en un String
	 *
	 * @param array Arreglo de bytes a transformar
	 * @return String que contiene la representación léxica de xds:hexBinary
	 */
	static String arrayToHexString( byte[] array )
	{
		return DatatypeConverter.printHexBinary( array ).toUpperCase( );
	}

	/**
	 * Convierte un Strign con representación léxica de xds:hexBinary a un arreglo de bytes
	 *
	 * @param str String a convertir
	 * @return Arreglo de bytes con la información del String dado por parámetro
	 */
	static byte[] hexToByteArray( String str )
	{
		return DatatypeConverter.parseHexBinary( str );
	}

	public static class Registro<T>
	{
		private HashMap<T, Long> reg;

		public Registro( )
		{
			reg = new HashMap<>( );
		}

		public void start( T titulo )
		{
			reg.put( titulo, System.currentTimeMillis( ) );
		}

		public void stop( T titulo )
		{
			reg.put( titulo, System.currentTimeMillis( ) - reg.get( titulo ) );
		}

		public Set<Map.Entry<T, Long>> getReg( )
		{
			return reg.entrySet( );
		}
	}
}
