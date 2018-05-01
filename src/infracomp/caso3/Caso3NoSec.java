package infracomp.caso3;

import infracomp.caso2.Cliente;
import infracomp.caso2.ClienteNoSec;
import uniandes.gload.core.LoadGenerator;
import uniandes.gload.core.Task;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

@SuppressWarnings( "unchecked" )
public class Caso3NoSec extends Caso3
{
	public static void main( String... args )
	{
		final AtomicInteger count = new AtomicInteger( 0 );
		ConcurrentLinkedQueue<State> leftStates = new ConcurrentLinkedQueue<>( );

		loadStates( leftStates, count );

		System.out.println( "Init" );

		while( !leftStates.isEmpty( ) )
		{
			State s = leftStates.poll( );
			if( s != null )
			{
				synchronized( s )
				{
					Task task = new Task( )
					{
						@Override
						public void execute( )
						{
							System.out.println( "Executing(" + s.iter + "): " + s.numPool + "-" + s.carga );
							try
							{
									ClienteNoSec cliente = new ClienteNoSec( false, new Integer[]{ s.numPool, s.j, s.i } );
								Thread thread = new Thread( cliente );
								thread.run( );

								Object[] statistics = cliente.getStatistics( );
								Set<Map.Entry<Cliente.Registros, Long>> logs = ( Set<Map.Entry<Cliente.Registros, Long>> ) statistics[ 0 ];
								Double cpuUsage = ( Double ) statistics[ 1 ];

								if( !logs.isEmpty( ) )
								{
									Long actualizacion = 0L;
									for( Map.Entry<Cliente.Registros, Long> s : logs )
									{
										if( s.getKey( ) == Cliente.Registros.ACTUALIZACION )
										{
											actualizacion = s.getValue( );
										}
									}
									s.actualizacion = actualizacion;
									s.cpuUsage = cpuUsage;
								}
							}
							catch( IOException e )
							{
								e.printStackTrace( );
							}

							try( BufferedWriter out = new BufferedWriter( new FileWriter( "docs/Caso3/data/SS_Carga_" + s.carga, true ) ) )
							{
								out.write( s.toString( ) );
								out.newLine( );
							}
							catch( Exception e )
							{
								e.printStackTrace( );
							}

							count.decrementAndGet( );
						}

						@Override
						public void fail( )
						{
							System.out.println( Task.MENSAJE_FAIL );
						}

						@Override
						public void success( )
						{
							System.out.println( Task.OK_MESSAGE );
						}
					};

					LoadGenerator generator = new LoadGenerator( "Client [SS] Server Test", s.numPool, load[ s.j ], task, gap[ s.j ] );
					generator.generate( );
				}
			}
		}
	}
}
