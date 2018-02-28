package infracomp.caso1;

public class Mensaje
{
	private String content;

	private Consumidor.TOPICS topic;

	private String response;

	public String getContent( )
	{
		return content;
	}

	public void setContent( String content )
	{
		this.content = content;
	}

	public String getResponse( )
	{
		return response;
	}

	public void setResponse( String response )
	{
		this.response = response;
	}

	public Consumidor.TOPICS getTopic( )
	{
		return topic;
	}

	public void setTopic( Consumidor.TOPICS topic )
	{
		this.topic = topic;
	}
}
