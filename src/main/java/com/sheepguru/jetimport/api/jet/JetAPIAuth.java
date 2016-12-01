
package com.sheepguru.jetimport.api.jet;

import com.sheepguru.jetimport.api.APIException;
import com.sheepguru.jetimport.api.APIHttpClient;
import javax.json.Json;
import javax.json.JsonObject;


/**
 * The Jet API Authentication Request 
 * @author John Quinn
 */
public class JetAPIAuth extends JetAPI
{
  /**
   * The auth test response from jet 
   */
  public static final String AUTH_TEST_RESPONSE = 
    "\"This message is authorized.\"";
  
  /**
   * Create a new API instance
   * @param client The built APIHttpClient instance 
   * @param conf The Jet Configuration object
   */
  public JetAPIAuth( final APIHttpClient client, final JetConfig conf )
  {
    super( client, conf );
  }
    

  /**
   * Attempt to log in to the Jet API, and retrieve a token
   * @return If the user is now logged in and the token has been acquired
   * @throws APIException if something goes wrong
   * @throws JetException if there are errors in the API response body
   * @throws JetAuthException if there is a problem with the authentication
   * data in the configuration object after setting it from the login response.
   */
  public boolean login()
    throws APIException, JetException, JetAuthException
  {
    //..Send the authorization request and attempt to set the response data in 
    //  the config cache.
    setConfigurationDataFromLogin( post(
      config.getAuthenticationURL(),
      getLoginPayload().toString(),
      JetHeaderBuilder.getJSONHeaderBuilder( 
        config.getAuthorizationHeaderValue()).build()
    ));
    
    //..Test the new configuration data from the response 
    config.testConfigurationData();    

    //..Perform a live authorization test
    if ( !authTest())
      config.clearAuthenticationData();

    //..Return the auth state
    return config.isAuthenticated();
  }
  
  
  /**
   * Retrieve the payload for the login/authentication request.
   * This creates an object with "user" and "pass" properties with values 
   * from the current JetConfig object.
   * @return The built JSON
   */
  private JsonObject getLoginPayload()
  {
    return Json.createObjectBuilder()
      .add( "user", config.getUsername())
      .add( "pass", config.getPassword())
    .build();    
  }  
  
  
  /**
   * Sets the configuration data from an authentication request response
   * @param response Response from login()
   * @throws JetException if the response does not contain 
   * id_token, token_type or expires_on 
   * @see JetAPI#login() 
   */
  private void setConfigurationDataFromLogin( final JetAPIResponse response )
    throws JetException
  {
    //..Turn it into JSON
    final JsonObject res = response.fromJSON();

    try {
      //..Set the authentication data
      config.setAuthenticationData(
        res.getString( "id_token" ),
        res.getString( "token_type" ),
        res.getString( "expires_on" )
      );
    } catch( NullPointerException e ) {
      throw new JetException( 
        "Authentication response is missing id_token, token_type or "
        + "expires_on. Check authentication response", e );      
    }    
  }


  /**
   *
   * @return If the authorization test was successful
   * @throws APIException if there's a problem
   */
  private boolean authTest() throws APIException
  {    
    return get( config.getAuthTestURL(), getPlainHeaderBuilder().build())
      .getResponseContent().equals( AUTH_TEST_RESPONSE );    
  }  
}