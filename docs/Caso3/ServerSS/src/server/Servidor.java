//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Servidor
{
	private static final int TIME_OUT = 10000;

	public static final int N_THREADS = 2;

	private static ServerSocket elSocket;

	private static Servidor elServidor;

	private ExecutorService executor = Executors.newFixedThreadPool( 2 );

	public Servidor( )
	{
	}

	public static void main( String[] args )
	{
		elServidor = new Servidor( );
		elServidor.runServidor( );
	}

	private void runServidor( )
	{
		int num = 0;

		try
		{
			System.out.print( "Puerto: " );
			BufferedReader br = new BufferedReader( new InputStreamReader( System.in ) );
			int puerto = Integer.parseInt( br.readLine( ) );
			elSocket = new ServerSocket( puerto );
			System.out.println( "Servidor escuchando en puerto: " + puerto );

			while( true )
			{
				Socket sThread = null;
				sThread = elSocket.accept( );
				sThread.setSoTimeout( 10000 );
				System.out.println( "Thread " + num + " recibe a un cliente." );
				this.executor.submit( new Worker( num, sThread ) );
				++num;
			}
		}
		catch( Exception var5 )
		{
			var5.printStackTrace( );
		}
	}
}
