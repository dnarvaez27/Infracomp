package infracomp.caso2;

import javax.xml.bind.DatatypeConverter;
import java.util.StringTokenizer;

class Utils
{
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

	static String arrayToHexString( byte[] array )
	{
		return DatatypeConverter.printHexBinary( array );
	}

	static byte[] hexToByteArray( String str )
	{
		return DatatypeConverter.parseHexBinary( str );
	}
}
