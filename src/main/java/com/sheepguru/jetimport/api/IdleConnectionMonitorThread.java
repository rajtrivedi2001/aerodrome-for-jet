/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sheepguru.jetimport.api;

import java.util.concurrent.TimeUnit;
import org.apache.http.conn.HttpClientConnectionManager;

/**
 * The idle connection monitor thread
 * @author John Quinn
 */
public class IdleConnectionMonitorThread extends Thread
{
  /**
   * The connection manager
   */
  private final HttpClientConnectionManager connMgr;

  /**
   * If this has been shutdown or not
   */
  private volatile boolean shutdown = false;


  /**
   * Create a new instance
   * @param connMgr The connection manager to monitor
   */
  public IdleConnectionMonitorThread( final HttpClientConnectionManager connMgr )
  {
    super();
    this.connMgr = connMgr;
  }


  /**
   * Run.
   * Monitors the connection manager and closes idle and expired connections every
   * 5 seconds.
   * The idle connection closer will wait at maximum for 30 seconds.
   */
  @Override
  public void run()
  {
    try {
      while ( !shutdown )
      {
        synchronized( this )
        {
          wait( 5000L );
          // Close expired connections
          connMgr.closeExpiredConnections();
          // Optionally, close connections
          // that have been idle longer than 30 sec
          connMgr.closeIdleConnections( 30, TimeUnit.SECONDS );
        }
      }
    } catch (InterruptedException ex) {
      // terminate
    }
  }


  /**
   * Trigger shutdown of this thread
   */
  public void shutdown()
  {
    shutdown = true;
    synchronized( this )
    {
      //..Wake anyone up
      notifyAll();
    }
  }
}