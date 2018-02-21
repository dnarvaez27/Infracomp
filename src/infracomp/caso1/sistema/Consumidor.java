package infracomp.caso1.sistema;

public class Consumidor implements Runnable
{
	private final Buffer buffer;

	private final Mensaje mensaje;

	public Consumidor( Buffer buffer, Productor.TOPICS topic, String content )
	{
		this.buffer = buffer;
		this.mensaje = new Mensaje( );
		this.mensaje.setTopic( topic );
		this.mensaje.setContent( content );

//		System.out.println( topic );
	}

	@Override
	public void run( )
	{
		synchronized( this.mensaje )
		{
			synchronized( buffer )
			{
				buffer.notifyAll( );
			}

			while( !buffer.add( this.mensaje ) )
			{
				synchronized( this )
				{
					try
					{
						System.out.println( "Full_" + mensaje.getContent( ) );
						this.wait( 500 );
					}
					catch( InterruptedException e )
					{
						e.printStackTrace( );
					}
				}
			}

			synchronized( buffer )
			{
				buffer.notifyAll( );
			}

			try
			{
				System.out.println( "Wait_4A_" + mensaje.getContent( ) );
				this.mensaje.wait( );
//				System.out.println( this.mensaje.getResponse( ) );
				System.out.println( "Done_" + mensaje.getContent( ) );
			}
			catch( InterruptedException e )
			{
				e.printStackTrace( );
			}
		}
	}
}
