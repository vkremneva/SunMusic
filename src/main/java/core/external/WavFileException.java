package main.java.core.external;

/**
 * Exception for WavFile Class
 * http://www.labbookpages.co.uk
 *
 * @author A.Greensted
 */

public class WavFileException extends Exception
{
	public WavFileException()
	{
		super();
	}

	public WavFileException(String message)
	{
		super(message);
	}

	public WavFileException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public WavFileException(Throwable cause) 
	{
		super(cause);
	}
}
