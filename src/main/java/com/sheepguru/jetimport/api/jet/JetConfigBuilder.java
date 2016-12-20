
package com.sheepguru.jetimport.api.jet;

import com.sheepguru.jetimport.api.APILog;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * A builder for creating a jet api configuration object.
 * @author John Quinn
 */
public class JetConfigBuilder 
{
  /**
   * Jet API Hostname
   */
  protected String host = "";

  /**
   * Jet API Username
   */
  protected String user = "";

  /**
   * Jet API Password (secret)
   */
  protected String pass = "";

  /**
   * Merchant Id
   */
  protected String merchantId = "";

  /**
   * URI for authenticating username/password and retrieving an auth token
   */
  protected String uriToken = "";

  /**
   * URI for testing the token retrieved during the authentication process
   */
  protected String uriAuthTest = "";

  /**
   * URI for archiving a sku 
   */
  protected String uriArchiveSku = "";
  
  /**
   * URI For adding a new product.
   * This API provides information about all methods that affect merchant SKU
   * set up, price, inventory, and shipping exceptions. Explanations about the
   * flow of information between methods in this API can be found in the API
   * Explanations tab under the Products section.
   *
   * replace {sku} with the merchant sku
   */
  protected String uriAddProduct = "";

  /**
   * URI for adding a product image url
   */
  protected String uriAddProductImage = "";

  /**
   * URI for adding a product price
   */
  protected String uriAddProductPrice = "";

  /**
   * URI for setting a product's inventory
   */
  protected String uriAddProductInventory = "";

  /**
   * URI for adding a product's shipping exceptions
   */
  protected String uriAddProductShipException = "";

  /**
   * URI for retrieving product data
   */
  protected String uriGetProduct = "";

  /**
   * URI for retrieving product price data
   */
  protected String uriGetProductPrice = "";

  /**
   * URI for adding a product variation group 
   */
  protected String uriAddProductVariation = "";
  
  /**
   * URI for adding a returns exception 
   */
  protected String uriReturnsException = "";

  /**
   * URL to retrieve product inventory 
   */
  protected String uriGetProductInventory = "";
  
  /**
   * URL to retrieve product variations
   */
  protected String uriGetProductVariation = "";
  
  /**
   * URL to retrieve product shipping exception s
   */
  protected String uriGetProductShippingException = "";
  
  /**
   * URL to retrieve product returns exceptions
   */
  protected String uriGetProductReturnsException = "";
  
  /**
   * URL to retrieve the list of product skus 
   */
  protected String uriGetSkuList = "";
  
  /**
   * URL to retrieve product sales data 
   */
  protected String uriGetProductSalesData = "";
   
  
  /**
   * Read timeout 
   */
  private long readTimeout = 10000L;
  
  /**
   * The request accept header value 
   */
  private String acceptHeaderValue = "application/json,text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8";
  
  /**
   * The request accept language header value 
   */
  private String acceptLanguageHeaderValue = "en-US,en;q=0.5";
  
  /**
   * If untrusted SSL is allowed
   */
  private boolean allowUntrustedSSL = false;
  
  /**
   * lock host 
   */
  private boolean lockHost = true;
  
  /**
   * Max download size
   */
  private long maxDownloadSize = 1024 * 2048;
  
        
  /**
   * Url to retrieve a bulk file upload status by file id 
   */
  private String uriGetBulkJetFileId = "";
  
  /**
   * Url for posting additional files to a batch or to start processing 
   */
  private String uriPostBulkUploadedFiles = "";
  
  /**
   * Url for retrieving a token for uploading a bulk file
   */
  private String uriGetBulkUploadToken = "";  
  
  
  /**
   * URL for retrieving a list of orders
   */
  private String uriGetOrders = "";
  
  /**
   * Uri for getting a list of order url's 
   */
  private String uriGetDirectedCancel = "";
  
  /**
   * Uri for retrieving details for an order
   */
  private String uriGetOrderDetail = "";  
  
  /**
   * Uri for acknowledging an order
   */
  private String uriPutOrderAck = "";
  
  /**
   * Uri for telling jet an order has shipped
   */
  private String uriPutOrderShipped = "";  
  
  
  /**
   * Get the log 
   */
  private static final Log LOG = LogFactory.getLog( JetConfigBuilder.class );
  
  
  /**
   * Set the hostname 
   * @param host the host to set
   * @return this
   */
  public JetConfigBuilder setHost( final String host) 
  {
    this.host = host;
    APILog.debug( LOG, "Using Host: ", host );
    return this;
  }

  
  /**
   * Set the auth username 
   * @param user the user to set
   * @return this
   */
  public JetConfigBuilder setUser( final String user ) 
  {
    this.user = user;
    APILog.debug( LOG, "Using User: ", user );
    return this;
  }

  
  /**
   * Set the auth password
   * @param pass the pass to set
   * @return this
   */
  public JetConfigBuilder setPass( final String pass ) 
  {
    this.pass = pass;
    APILog.debug( LOG, "Using Pass: *****" );
    return this;
  }
  
  
  /**
   * Set lock host
   * @param on on 
   * @return this
   */
  public JetConfigBuilder setLockHost( final boolean on )
  {
    lockHost = on;
    APILog.debug( LOG, "lockHost set to ", ( lockHost ) ? " true " : " false" );
    return this;
  }
  
  
  /**
   * Set the max download size
   * @param size size 
   * @return this 
   */
  public JetConfigBuilder setMaxDownloadSize( final long size )
  {
    maxDownloadSize = size;
    APILog.debug( LOG, "maxDownloadSize set to", String.valueOf( size ));
    return this;
  }

  
  /**
   * Set the Jet.com merchant id 
   * @param merchantId the merchantId to set
   * @return this
   */
  public JetConfigBuilder setMerchantId( final String merchantId ) 
  {
    this.merchantId = merchantId;
    APILog.debug( LOG, "Using merchant Id: ", merchantId );
    return this;
  }
  
  
  /**
   * Set URI for authenticating username/password and retrieving an auth token
   * @param uriToken the uriToken to set
   * @return this
   */
  public JetConfigBuilder setUriToken( final String uriToken ) 
  {
    this.uriToken = uriToken;
    
    APILog.debug( LOG, "Authentication URI set to: ", uriToken );
    return this;
  }

  
  /**
   * Set URI for testing the token retrieved during the authentication process
   * @param uriAuthTest the uriAuthTest to set
   * @return this
   */
  public JetConfigBuilder setUriAuthTest( final String uriAuthTest ) 
  {
    this.uriAuthTest = uriAuthTest;
    APILog.debug( LOG, "Authentication test URI set to: ", uriAuthTest );
    return this;
  }
  

  /**
   * Set URI For adding a new product.
   * This API provides information about all methods that affect merchant SKU
   * set up, price, inventory, and shipping exceptions. Explanations about the
   * flow of information between methods in this API can be found in the API
   * Explanations tab under the Products section.
   * @param uriAddProduct the uriAddProduct to set
   * @return this
   */
  public JetConfigBuilder setUriAddProduct( final String uriAddProduct ) 
  {
    this.uriAddProduct = uriAddProduct;
    APILog.debug( LOG, "Send product SKU URI set to: ", uriAddProduct );
    return this;
  }
  
  
  /**
   * Set the uri for archiving a sku.
   * Archiving a SKU allows the retailer to "deactivate" a SKU from the catalog. 
   * At any point in time, a retailer may decide to "reactivate" the SKU
   * @param uriArchiveSku sku to archive 
   * @return this 
   */
  public JetConfigBuilder setUriArchiveSku( final String uriArchiveSku )
  {
    this.uriArchiveSku = uriArchiveSku;
    APILog.debug( LOG, "Send archive sku URI set to: ", uriArchiveSku );
    return this;    
  }

  
  /**
   * Set uri for adding a product image 
   * @param uriAddProductImage the uriAddProductImage to set
   * @return this
   */
  public JetConfigBuilder setUriAddProductImage( final String uriAddProductImage ) 
  {
    this.uriAddProductImage = uriAddProductImage;
    APILog.debug( LOG, "Send product image URI set to", uriAddProductImage );
    return this;
  }

  
  /**
   * Set uri for adding a product price 
   * @param uriAddProductPrice the uriAddProductPrice to set
   * @return this
   */
  public JetConfigBuilder setUriAddProductPrice( final String uriAddProductPrice ) 
  {
    this.uriAddProductPrice = uriAddProductPrice;
    APILog.debug( LOG, "Send product price URI set to: ", uriAddProductPrice );
    return this;
  }

  
  /**
   * Set uri for adding product inventory 
   * @param uriAddProductInventory the uriAddProductInventory to set
   * @return this
   */
  public JetConfigBuilder setUriAddProductInventory( final String uriAddProductInventory ) 
  {
    this.uriAddProductInventory = uriAddProductInventory;
    APILog.debug( LOG, "Send product inventory URI set to: ", uriAddProductInventory );
    return this;
  }

  
  /**
   * Set uri for adding a shipping exception 
   * @param uriAddProductShipException the uriAddProductShipException to set
   * @return this
   */
  public JetConfigBuilder setUriAddProductShipException( final String uriAddProductShipException ) 
  {
    this.uriAddProductShipException = uriAddProductShipException;
    APILog.debug( LOG, "Send product shipping exceptions URI set to: ", uriAddProductShipException );
    return this;
  }

  
  /**
   * Set the uri for retrieving a product 
   * @param uriGetProduct the uriGetProduct to set
   * @return this
   */
  public JetConfigBuilder setUriGetProduct( final String uriGetProduct ) 
  {
    this.uriGetProduct = uriGetProduct;
    APILog.debug( LOG, "Get product URI set to ", uriGetProduct );
    return this;
  }

  
  /**
   * Set uri for retrieving a product price 
   * @param uriGetProductPrice the uriGetProductPrice to set
   * @return this
   */
  public JetConfigBuilder setUriGetProductPrice( final String uriGetProductPrice ) 
  {
    this.uriGetProductPrice = uriGetProductPrice;
    APILog.debug( LOG, "Get product price uri set to: ", uriGetProductPrice );
    return this;
  }
  
  
  /**
   * Set uri for adding a product variation group 
   * @param uriAddProductVariation uri 
   * @return builder 
   */
  public JetConfigBuilder setUriAddProductVariation( final String uriAddProductVariation )
  {
    this.uriAddProductVariation = uriAddProductVariation;
    APILog.debug( LOG, "Add product variation uri set to:", uriAddProductVariation );
    return this;
  }
  
  
  /**
   * Set uri for adding a return exception 
   * @param uriReturnException Exception uri 
   * @return builder
   */
  public JetConfigBuilder setUriAddProductReturnException( final String uriReturnException )
  {
    this.uriReturnsException = uriReturnException;
    APILog.debug( LOG, "Add product return exception uri set to: ", uriReturnException );
    return this;
  }
  
  

  /**
   * Set the url for retrieving product inventory
   * @param uriGetProductInventory url 
   * @return this
   */
  public JetConfigBuilder setUriGetProductInventory( final String uriGetProductInventory )
  {
    this.uriGetProductInventory = uriGetProductInventory;
    APILog.debug( LOG, "Get product inventory uri set to: ", uriGetProductInventory );
    return this;
  }
  
  
  /**
   * Set the url for retrieving product variations
   * @param uriGetProductVariation url 
   * @return this
   */
  public JetConfigBuilder setUriGetProductVariation( final String uriGetProductVariation )
  {
    this.uriGetProductVariation = uriGetProductVariation;
    APILog.debug( LOG, "Get product variation uri set to: ", uriGetProductVariation );
    return this;
  }
  
  
  /**
   * Set the url for retrieving product shipping exceptions
   * @param uriGetProductShippingException url 
   * @return this
   */
  public JetConfigBuilder setUriGetShippingException( final String uriGetProductShippingException )
  {
    this.uriGetProductShippingException = uriGetProductShippingException;
    APILog.debug( LOG, "Get product shipping exceptions uri set to: ", uriGetProductShippingException );
    return this;
  }
  
  
  /**
   * Set the url for retrieving product returns exceptions 
   * @param uriGetProductReturnsException url 
   * @return this
   */
  public JetConfigBuilder setUriGetReturnsException( final String uriGetProductReturnsException )
  {
    this.uriGetProductReturnsException = uriGetProductReturnsException;
    APILog.debug( LOG, "Get product returns exceptions uri set to: ", uriGetProductReturnsException );
    return this;
  }
  
  
  /**
   * Set the url for retrieving product inventory
   * @param uriGetSkuList the url 
   * @return this
   */
  public JetConfigBuilder setUriGetSkuList( final String uriGetSkuList )
  {
    this.uriGetSkuList = uriGetSkuList;
    APILog.debug( LOG, "Get product sku list uri set to: ", uriGetSkuList );
    return this;
  }

  
  /**
   * Set the url for retrieving product sales data
   * @param uriGetProductSalesData url 
   * @return this
   */
  public JetConfigBuilder setUriGetSalesDataBySku( final String uriGetProductSalesData )
  {
    this.uriGetProductSalesData = uriGetProductSalesData;
    APILog.debug( LOG, "Get product sales data uri set to: ", uriGetProductSalesData );
    return this;
  }  
  

  /**
   * Set the socket read timeout in milliseconds
   * @param timeout millis
   * @return builder
   */
  public JetConfigBuilder setReadTimeout( final long timeout )
  {
    readTimeout = timeout;
    APILog.debug( LOG, "Read timeout set to: ", String.valueOf( timeout ));
    return this;
  }
  
  
  /**
   * Set the default accept header value for requests
   * @param value value 
   * @return builder
   * @throws IllegalArgumentException if value is null or empty 
   */
  public JetConfigBuilder setAcceptHeader( final String value ) 
    throws IllegalArgumentException
  {
    if ( value == null || value.isEmpty())
      throw new IllegalArgumentException( "value cannot be empty" );
    
    acceptHeaderValue = value;
    APILog.debug( LOG, "Accept header set to: ", value );
    return this;
  }
  
  
  /**
   * Set the default accept language header value for requests
   * @param value value 
   * @return builder
   * @throws IllegalArgumentException if value is null or empty 
   */
  public JetConfigBuilder setAcceptLanguageHeader( final String value ) 
    throws IllegalArgumentException
  {
    if ( value == null || value.isEmpty())
      throw new IllegalArgumentException( "value cannot be empty" );
    
    acceptLanguageHeaderValue = value;
    APILog.debug( LOG, "Accept-Language header set to: ", value );
    return this;
  }

  
  /**
   * Set allow untrusted ssl (default false)
   * @param allow toggle
   * @return builder
   */
  public JetConfigBuilder setAllowUntrustedSSL( final boolean allow )
  {
    allowUntrustedSSL = allow;
    
    if ( allow )
      APILog.debug( LOG, "Allow Untrusted SSL is enabled" );
    return this;
  }
  
  

  /**
   * URL for the endpoint for accessing the first 1000 orders in a certain status.
   * 
   * 
   * 'created' - The order has just been placed. Jet.com allows a half hour for 
   * fraud check and customer cancellation. We ask that retailers NOT fulfill 
   * orders that are created.
   * 'ready' - The order is ready to be fulfilled by the retailer
   * 'acknowledged' - The order has been accepted by the retailer and is 
   * awaiting fulfillment
   * 'inprogress' - The order is partially shipped
   * 'complete' - The order is completely shipped or cancelled. All units have 
   * been accounted for
   * 
   * @param uri The uri
   * @return builder
   */
  public JetConfigBuilder setGetOrdersUrl( final String uri )
  {
    this.uriGetOrders = uri;
    return this;
  }
  
  
  /**
   * This provides a list of order url's that can be used to retrieve order
   * details I think.  
   * @param uri The uri
   * @return builder
   */
  public JetConfigBuilder setGetOrderDirectCancelUrl( final String uri )
  {
    this.uriGetDirectedCancel = uri;
    return this;
  }
  
  
  /**
   * This endpoint will provide you with requisite fulfillment information for 
   * the order denoted by the Jet Defined Order ID.
   * @param jetDefinedOrderId The jet order id defined by jet.com 
   * @param uri The uri
   * @this.builder
   */
  public JetConfigBuilder setGetOrderDetailUrl( final String uri )
  {
    this.uriGetOrderDetail = uri;
    return this;
  }
  
  
  /**
   * The order acknowledge call is utilized to allow a retailer to accept or 
   * reject an order. If there are any skus in the order that cannot be 
   * fulfilled then you will reject the order.
   * @param uri The uri
   * @this.builder
   */
  public JetConfigBuilder setPutOrderAcknowledgeUrl( final String uri )
  {
    this.uriPutOrderAck = uri;
    return this;
  }
  
  
  /**
   * The order shipped call is utilized to provide Jet with the SKUs that have 
   * been shipped or cancelled in an order, the tracking information, carrier 
   * information and any additional returns information for the order.
   * @param uri The uri
   * @this.builder
   */
  public JetConfigBuilder setPutOrderShipNotificationUrl( final String uri )
  {
    this.uriPutOrderShipped = uri;
    return this;
  }  
  
  
  /**
   * Build a configuration object
   * @return config
   */
  public JetConfig build()
  {
    return new DefaultJetConfig(
      merchantId,
      host,
      user, 
      pass, 
      lockHost,
      maxDownloadSize,
      readTimeout,
      acceptHeaderValue,
      acceptLanguageHeaderValue,
      allowUntrustedSSL,
      uriToken, 
      uriAuthTest, 
      uriAddProduct,
      uriAddProductImage, 
      uriAddProductPrice, 
      uriAddProductInventory, 
      uriAddProductShipException, 
      uriGetProduct, 
      uriGetProductPrice,
      uriAddProductVariation,
      uriArchiveSku,
      uriReturnsException,
      uriGetProductInventory,
      uriGetProductVariation,
      uriGetProductShippingException,
      uriGetProductReturnsException,
      uriGetSkuList,
      uriGetProductSalesData,
      uriGetBulkUploadToken,
      uriGetBulkJetFileId,
      uriPostBulkUploadedFiles,
      uriGetOrders,
      uriGetDirectedCancel,
      uriGetOrderDetail,
      uriPutOrderAck,
      uriPutOrderShipped
    );
  }

  /**
   * @param uriGetBulkJetFileId the uriGetBulkJetFileId to set
   */
  public JetConfigBuilder setUriGetBulkJetFileId( final String uriGetBulkJetFileId ) 
  {
    APILog.debug( LOG, "Bulk product file id URI set to:", uriGetBulkJetFileId );
    this.uriGetBulkJetFileId = uriGetBulkJetFileId;
    return this;
  }

  /**
   * @param uriPostBulkUploadedFiles the uriPostBulkUploadedFiles to set
   */
  public JetConfigBuilder setUriPostBulkUploadedFiles(String uriPostBulkUploadedFiles) 
  {
    APILog.debug( LOG, "Post bulk file upload URI set to:", uriPostBulkUploadedFiles );
    this.uriPostBulkUploadedFiles = uriPostBulkUploadedFiles;
    return this;
  }

  /**
   * @param uriGetBulkUploadToken the uriGetBulkUploadToken to set
   */
  public JetConfigBuilder setUriGetBulkUploadToken(String uriGetBulkUploadToken) {
    APILog.debug( LOG, "Get bulk file upload token URI set to:", uriGetBulkUploadToken );
    this.uriGetBulkUploadToken = uriGetBulkUploadToken;
    return this;
  }
}