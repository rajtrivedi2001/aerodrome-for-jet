/**
 * This file is part of the Aerodrome package, and is subject to the 
 * terms and conditions defined in file 'LICENSE', which is part 
 * of this source code package.
 *
 * Copyright (c) 2016 All Rights Reserved, John T. Quinn III,
 * <johnquinn3@gmail.com>
 *
 * THIS CODE AND INFORMATION ARE PROVIDED "AS IS" WITHOUT WARRANTY OF ANY
 * KIND, EITHER EXPRESSED OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND/OR FITNESS FOR A
 * PARTICULAR PURPOSE.
 */

package com.sheepguru.aerodrome;

/**
 * A series of exit/return values for the Aerodrome application
 * @author John Quinn
 */
public interface ExitCodes
{
  /**
   * A success code
   */
  public static final int E_SUCCESS = 0;

  /**
   * Failed to extract some resource from META-INF or from the jar file
   */
  public static final int E_JAR_EXTRACT_FAILURE = 1;

  /**
   * aerodrome.conf.xml was not found
   */
  public static final int E_CONFIG_NOT_FOUND = 2;

  /**
   * Some sort of configuration or formatting error occurred in the
   * xml configuration file.
   */
  public static final int E_CONFIG_FAILURE = 3;

  /**
   * Jet-specific configuration object failure
   */
  public static final int E_JET_CONFIG_FAILURE = 4;

  /**
   * A file used for interacting with Jet was not found.
   * This is commonly thrown from the run mode configuration
   */
  public static final int E_FILE_NOT_FOUND = 5;

  /**
   * Missing run mode
   */
  public static final int E_NO_RUN_MODE = 6;

  /**
   * If the /resources directory containing config files, etc is missing
   * within the jar file.
   */
  public static final int E_MISSING_RESOURCES = 7;

  /**
   * Jet API Authentication failure (token not received)
   */
  public static final int E_AUTH_FAILURE = 8;

  /**
   * A generic API failure code 
   */
  public static final int E_API_FAILURE = 9;
  
  /**
   * Some command line argument failure 
   */
  public static final int E_CLI_FAILURE = 10;
}