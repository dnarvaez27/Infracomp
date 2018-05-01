package infracomp.caso3;

import infracomp.caso2.Cliente;
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
public class Caso3SinSeg
{
	static class State
	{
		private Integer numPool;

		private Integer carga;

		private Integer gap;

		private Integer iter;

		private Long actualizacion;

		//private Long llaveSimetrica;

		private Double cpuUsage;

		private Integer i;

		private Integer j;

		State( Integer i, Integer j, Integer numPool, Integer carga, Integer gap, Integer iter, Long actualizacion, Double cpuUsage )
		{
			this.i = i;
			this.j = j;
			this.numPool = numPool;
			this.carga = carga;
			this.gap = gap;
			this.iter = iter;
			this.actualizacion = actualizacion;
			this.cpuUsage = cpuUsage;
		}

		@Override
		public String toString( )
		{
			return String.format( "%s;%s;%s;%s;%s;%s;%s", numPool, carga, gap, iter, actualizacion,  String.valueOf( cpuUsage ).replace( ".", "," ) );
		}
	}

	public static void main( String... args )
	{
		final AtomicInteger count = new AtomicInteger( 0 );
		ConcurrentLinkedQueue<State> leftStates = new ConcurrentLinkedQueue<>( );

		int iter = 10;

		Integer[] numPools = new Integer[]{ 1, 2, 8 };
		Integer[] load = new Integer[]{ 400, 200, 80 };
		Integer[] gap = new Integer[]{ 20, 40, 100 };

		for( int j = 0; j < load.length; j++ )
		{
			for( Integer numPool : numPools )
			{
				System.out.println( "-------------------------------" );
				System.out.println( String.format( "Pool: %s\nLoad: %s\nGap: %s", numPool, load[ j ], gap[ j ] ) );
				for( int i = 0; i < iter; i++ )
				{
					System.out.println( "Created: " + numPool + "-" + load[ j ] );
					count.incrementAndGet( );
					State currentState = new State( i, j, numPool, load[ j ], gap[ j ], i, -1L, -1D );
					leftStates.add( currentState );
				}
			}
		}

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
								Cliente cliente = new Cliente( false, new Integer[]{ s.numPool, s.j, s.i } );
								Thread thread = new Thread( cliente );
								thread.run( );

								Object[] statistics = cliente.getStatistics( );
								Set<Map.Entry<Cliente.Registros, Long>> logs = ( Set<Map.Entry<Cliente.Registros, Long>> ) statistics[ 0 ];
								Double cpuUsage = ( Double ) statistics[ 1 ];

								if( !logs.isEmpty( ) )
								{
									Long actualizacion = 0L;
									//Long llave = 0L;
									for( Map.Entry<Cliente.Registros, Long> s : logs )
									{
										if( s.getKey( ) == Cliente.Registros.ACTUALIZACION )
										{
											actualizacion = s.getValue( );
										}
									}
									s.actualizacion = actualizacion;
									//s.llaveSimetrica = llave;
									s.cpuUsage = cpuUsage;
								}
							}
							catch( IOException e )
							{
								e.printStackTrace( );
							}

							try( BufferedWriter out = new BufferedWriter( new FileWriter( "docs/Caso3/data/Carga_" + s.carga, true ) ) )
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

					LoadGenerator generator = new LoadGenerator( "Client Server Test", s.numPool, load[ s.j ], task, gap[ s.j ] );
					generator.generate( );
				}
			}
		}
	}
}
