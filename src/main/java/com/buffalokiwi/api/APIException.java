
package com.buffalokiwi.api;

import org.apache.commons.logging.Log;

/**
 * A generic exception for objects that interact with the Jet API
 *
 * @author John Quinn
 */
public class APIException extends Exception
{

  /**
   * The previous exception
   */
  private Exception previous = null;

  /**
   * Creates a new instance of <code>APIException</code> without detail message.
   */
  public APIException() {}


  /**
   * Constructs an instance of <code>APIException</code> with the specified
   * detail message.
   *
   * @param msg the detail message.
   */
  public APIException(String msg)
  {
    super(msg);
  }


  /**
   * An api exception with a cause
   * @param message the detail message
   * @param previous The previous exception
   */
  public APIException(String message, Exception previous )
  {
    super( message );
    this.previous = previous;
  }


  /**
   * Retrieve the previous exception if any
   * @return Previous
   */
  public Exception getPrevious()
  {
    return previous;
  }
  
  
  /**
   * Print this exception to the log 
   * @param log Log to print to
   */  
  public void printToLog( final Log log )
  {
    if ( log == null )
      throw new IllegalArgumentException( "log cannot be null" );
    
    log.error( getMessage());
    log.debug( this );
    if ( getCause() != null )
    {
      log.debug( getCause());
    }
  }
}
