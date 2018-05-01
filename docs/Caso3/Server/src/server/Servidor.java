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

	private static final int N_THREADS = 2;

	public Servidor( )
	{
	}

	private ExecutorService executor = Executors.newFixedThreadPool( N_THREADS );

	public static void main( String[] args )
	{
		Servidor elServidor = new Servidor( );
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
			ServerSocket elSocket = new ServerSocket( puerto );
			System.out.println( "Servidor escuchando en puerto: " + puerto );
			while( true )
			{
				Socket sThread;
				sThread = elSocket.accept( );
				sThread.setSoTimeout( TIME_OUT );
				System.out.println( "Thread " + num + " recibe a un cliente." );
				executor.submit( new Worker( num, sThread ) );
				num++;
			}
		}
		catch( Exception e )
		{
			e.printStackTrace( );
		}
	}
}
