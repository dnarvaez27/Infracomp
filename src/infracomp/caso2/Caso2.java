package infracomp.caso2;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

/**
 * Clase para ejecutar una prueba del Caso 2
 */
public class Caso2
{
	public static void main( String... args )
	{
		try
		{
			// Instancia un cliente en un Thread por separado. El Thread no se inicia aún
			Thread cliente = new Thread( ( ) ->
										 {
											 try
											 {
												 new Thread( new Cliente( true, null ) ).start( );
											 }
											 catch( IOException e )
											 {
												 e.printStackTrace( );
											 }
										 } );

			// Ejecuta el jar del Servidor e inicia el Thread del cliente una vez se inicie el servidor
			new Thread( ( ) ->
						{
							try
							{
								ProcessBuilder builder = new ProcessBuilder( "java", "-jar", "Servidorcs.jar" );
								builder.directory( new File( "./libs" ) );
								Process p = builder.start( );

								PrintWriter out = new PrintWriter( p.getOutputStream( ), true );
								out.println( Cliente.PUERTO );

								cliente.start( );

								BufferedReader input = new BufferedReader( new InputStreamReader( p.getInputStream( ) ) );
								String line;
								while( ( line = input.readLine( ) ) != null )
								{
									System.out.println( line );
								}
								input.close( );
							}
							catch( Exception e )
							{
								e.printStackTrace( );
							}
						} ).start( );

		}
		catch( Exception e )
		{
			e.printStackTrace( );
		}
	}
}
