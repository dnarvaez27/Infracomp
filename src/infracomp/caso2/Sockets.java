package infracomp.caso2;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;

public class Sockets extends Thread
{
	private Socket socket;

	public Sockets( Socket socket )
	{
		this.socket = socket;
	}

	@Override
	public void run( )
	{
		try
		{
			BufferedReader br = new BufferedReader( new InputStreamReader( socket.getInputStream( ) ) );
			//OutputStream out = socket.getOutputStream();

			while( ( br.readLine( ) ) != null )
			{
				System.out.println( "The client sent : " + br.readLine( ) );
			}

		}
		catch( Exception e )
		{
			e.printStackTrace( );
		}
	}
}
