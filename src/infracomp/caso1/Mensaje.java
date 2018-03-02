package infracomp.caso1;

/**
 * Mensaje que viaja en el {@link Buffer} en el esquema {@link Productor} {@link Consumidor}<br>
 *
 * @author David Narvaez - (d.narvaez11@uniandes.edu.co)
 * @author Daniela Jaimes - (d.jaimes@uniandes.edu.co)
 */
public class Mensaje
{
	/**
	 * Contenido del mensaje
	 */
	private String content;

	/**
	 * Topic del mensaje
	 */
	private Consumidor.TOPICS topic;

	/**
	 * Respuesta al mensaje
	 */
	private String response;

	/**
	 * Retorna el contenido del mensaje
	 *
	 * @return Contenido del mensaje
	 */
	public String getContent( )
	{
		return content;
	}

	/**
	 * Actualiza el contenido del Contenido
	 *
	 * @param content nuevo Contenido del mensaje
	 */
	public void setContent( String content )
	{
		this.content = content;
	}

	/**
	 * Retorna la respuesta del mensaje
	 *
	 * @return Respuesta del mensaje
	 */
	public String getResponse( )
	{
		return response;
	}

	/**
	 * Actualiza la respuesta del mensaje
	 *
	 * @param response nueva Respuesta del mensaje
	 */
	public void setResponse( String response )
	{
		this.response = response;
	}

	/**
	 * Retorna el topico del mensaje
	 *
	 * @return Topico del mensaje
	 */
	public Consumidor.TOPICS getTopic( )
	{
		return topic;
	}

	/**
	 * Actualiza el topico del mensaje
	 *
	 * @param topic Nuevo topico del mensaje
	 */
	public void setTopic( Consumidor.TOPICS topic )
	{
		this.topic = topic;
	}
}
