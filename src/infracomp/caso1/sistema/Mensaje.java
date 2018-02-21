package infracomp.caso1.sistema;

import java.time.ZonedDateTime;

public class Mensaje
{
	private String content;

	private Productor.TOPICS topic;

	private String response;

	private ZonedDateTime date;

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

	public ZonedDateTime getDate( )
	{
		return date;
	}

	public void setDate( ZonedDateTime date )
	{
		this.date = date;
	}

	public Productor.TOPICS getTopic( )
	{
		return topic;
	}

	public void setTopic( Productor.TOPICS topic )
	{
		this.topic = topic;
	}
}
