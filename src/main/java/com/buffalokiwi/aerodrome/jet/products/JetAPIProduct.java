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

package com.buffalokiwi.aerodrome.jet.products;

import com.buffalokiwi.aerodrome.jet.JetAPI;
import com.buffalokiwi.api.APIException;
import com.buffalokiwi.api.APILog;
import com.buffalokiwi.api.IAPIHttpClient;
import com.buffalokiwi.aerodrome.jet.IJetAPIResponse;
import com.buffalokiwi.aerodrome.jet.JetConfig;
import com.buffalokiwi.aerodrome.jet.JetException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * Handles working with the Jet Products API
 *
 * @author John Quinn
 */
public class JetAPIProduct extends JetAPI implements IJetAPIProduct
{
  /**
   * The log 
   */
  private static final Log LOG = LogFactory.getLog( JetAPIProduct.class );
  
  
  /**
   * Create a new JetProduct instance
   * @param client The http client 
   * @param conf Configuration 
   */
  public JetAPIProduct( final IAPIHttpClient client, final JetConfig conf )
  {
    super( client, conf );
  }

  
  @Override
  public boolean addProductSku( final ProductRec product ) throws APIException, JetException, ValidateException
  {
    product.validate();
    sendPutProductSku( product );  
    return true;
  }
  

  /**
   * Add a product to the Jet catalog
   * @param product Product to add
   * @return Success
   * @throws JetException if there is an error from the jet api
   * @throws APIException if there is some sort of error with the api 
   * library itself. A network issue, etc.
   * @throws ValidateException if the product fails pre-submit validation
   */
  @Override
  public boolean addProduct( final ProductRec product ) throws APIException, JetException, ValidateException
  {
    product.validate();
    
    //..Add Sku    
    sendPutProductSku( product );
    
    //..Add an image
   // sendPutProductImage( product );

    //..Add the price
    sendPutProductPrice( product );

    //..Add some inventory
    sendPutProductInventory( product );
    
    sendPutProductShippingExceptions( product.getMerchantSku(), product.getShippingExceptionNodes());
    
    sendPutReturnsException( product.getMerchantSku(), product.getAllReturnLocationIds());
    
    //..pointless.
    return true;
  }


  /**
   * Adds a product sku.
   * Part of a multi-part operation.
   * This will call merchant-skus/{sku-id}
   *
   * @param product product data
   * @return success
   * @throws APIException
   * @throws JetException
   */
  @Override
  public IJetAPIResponse sendPutProductSku( final ProductRec product )
      throws APIException, JetException
  {    
    APILog.info( LOG, "Sending ", product.getMerchantSku());
    final IJetAPIResponse response = put(
      config.getAddProductURL( product.getMerchantSku()),
      product.toJSON().toString(),
      getJSONHeaderBuilder().build()
    );

    return response;
  }


  /**
   * Adds image url's
   * @param product product data
   * @return success
   * @throws APIException
   * @throws JetException
   * @deprecated Removed from Jet 
   */
  @Override
  public IJetAPIResponse sendPutProductImage( final ProductRec product )
      throws APIException, JetException
  {
    APILog.info( LOG, "Sending", product.getMerchantSku(), "image" );
    
    final IJetAPIResponse response = put(
      config.getAddProductImageUrl( product.getMerchantSku()),
      product.toImageJson().toString(),
      getJSONHeaderBuilder().build()
    );

    return response;
  }


  /**
   * Adds product price data
   * @param product
   * @return
   * @throws APIException
   * @throws JetException
   */
  @Override
  public IJetAPIResponse sendPutProductPrice( final ProductRec product )
      throws APIException, JetException
  {
    APILog.info( LOG, "Sending", product.getMerchantSku(), "price" );
    
    final IJetAPIResponse response = put(
      config.getAddProductPriceUrl( product.getMerchantSku()),
      product.toPriceJson().toString(),
      getJSONHeaderBuilder().build()
    );
    
    return response;
  }
  
  
  /**
   * Send product price data
   * @param sku merchant sku
   * @param price price data
   * @return response 
   * @throws APIException
   * @throws JetException 
   */
  @Override
  public IJetAPIResponse sendPutProductPrice( final String sku, final ProductPriceRec price )
    throws APIException, JetException
  {
    if ( sku == null || sku.isEmpty())
      throw new IllegalArgumentException( "sku can't be null or empty" );
    
    APILog.info( LOG, "Sending", sku, "price" );
    
    final IJetAPIResponse response = put(
      config.getAddProductPriceUrl( sku ),
      price.toJSON().toString(),
      getJSONHeaderBuilder().build()
    );
    
    return response;    
  }


  /**
   * Adds product quantity and inventory data
   * @param product product data
   * @return success
   * @throws APIException
   * @throws JetException
   */
  @Override
  public IJetAPIResponse sendPutProductInventory( final ProductRec product )
      throws APIException, JetException
  {
    APILog.info( LOG, "Sending", product.getMerchantSku(), "inventory" );
    
    final IJetAPIResponse response = put(
      config.getAddProductInventoryUrl( product.getMerchantSku()),
      product.toInventoryJson().toString(),
      getJSONHeaderBuilder().build()
    );

    return response;
  }
  
  
  /**
   * The variation request is used to create a variation-type relationship 
   * between several SKUs. To use this request, one must have already uploaded 
   * all the SKUs in question ; they should then choose one "parent" SKU and 
   * make the variation request to that SKU, adding as "children" any SKUs they 
   * want considered part of the relationship.
   * To denote the particular variation refinements, one must have uploaded one 
   * or more attributes in the product call for all the SKUs in question; 
   * finally, they are expected to list these attributes in the variation 
   * request.
   * 
   * @param group data to send 
   * @return response from jet 
   * @throws APIException if there's a problem 
   * @throws JetException 
   */
  @Override
  public IJetAPIResponse sendPutProductVariation( 
    final ProductVariationGroupRec group ) throws APIException, JetException
        
  {
    if ( group == null )
      throw new IllegalArgumentException( "group cannot be null" );
    
    APILog.info( LOG, "Sending", group.getParentSku(), "variations" );
    
    final IJetAPIResponse response = put(
      config.getAddProductVariationUrl( group.getParentSku()),
      group.toJSON().toString(),
      getJSONHeaderBuilder().build()
    );
    
    return response;
  }  
  
  
  /**
   * Send shipping exceptions to jet 
   * @param sku Sku 
   * @param nodes Filfillment nodes 
   * @return
   * @throws APIException
   * @throws JetException 
   */
  @Override
  public IJetAPIResponse sendPutProductShippingExceptions(
    final String sku,
    final List<FNodeShippingRec> nodes
  ) throws APIException, JetException
  {
    checkSku( sku );
    
    if ( nodes == null )
      throw new IllegalArgumentException( "nodes cannot be null" );
    
    APILog.info( LOG, "Sending", sku, "shipping exceptions" );
    
    
    final JsonArrayBuilder b = Json.createArrayBuilder();
    for ( final FNodeShippingRec node : nodes )
    {
      b.add( node.toJSON());
    }
    
    
    final JsonObjectBuilder o = Json.createObjectBuilder();
    o.add( "fulfillment_nodes", b );
    
    
    final IJetAPIResponse response = put(
      config.getAddProductShipExceptionUrl( sku ),
      o.build().toString(),
      getJSONHeaderBuilder().build()
    );
    
    return response;    
  }  
  
  
  /**
   * The returns exceptions call is used to set up specific methods that will 
   * overwrite your default settings on a fulfillment node level for returns. 
   * This exception will be used to determine how and to where a product is 
   * returned unless the merchant specifies otherwise in the Ship Order message. 
   * 
   * @param sku Product SKU to modify 
   * @param hashes A list of md5 hashes - Each hash is the ID of the returns 
   * node that was created on partner.jet.com under fulfillment settings.
   * 
   * Must be a valid return node ID set up by the merchant
   * 
   * @return response 
   * @throws APIException
   * @throws JetException 
   */
  @Override
  public IJetAPIResponse sendPutReturnsException( final String sku, 
    final List<String> hashes ) throws APIException, JetException
  {
    checkSku( sku );
    
    if ( hashes == null )
      throw new IllegalArgumentException( "hashes cannot be null" );
    
    final JsonArrayBuilder b = Json.createArrayBuilder();
    for ( final String s : hashes )
    {
      b.add( s );
    }
    
    
    
    APILog.info( LOG, "Sending", sku, "returns exceptions" );
    
    final IJetAPIResponse res = put( 
      config.getProductReturnsExceptionUrl( sku ),
      Json.createObjectBuilder().add(  "return_location_ids", b.build()).build().toString(),
      getJSONHeaderBuilder().build()
    );
    
    return res;
    
  }
  
  
  /**
   * Archive a product sku.
   * 
   * Archiving a SKU allows the retailer to "deactivate" a SKU from the catalog. 
   * At any point in time, a retailer may decide to "reactivate" the SKU
   * @param sku
   * @param isArchived Indicates whether the specified SKU is archived.
    'true' - SKU is inactive
    'false' - SKU is potentially sellable
   * @return
   * @throws APIException
   * @throws JetException 
   */
  @Override
  public IJetAPIResponse sendPutArchiveSku( final String sku, 
    final boolean isArchived ) throws APIException, JetException
  {
    checkSku( sku );
    
    APILog.info( LOG, "Sending archive sku:", sku );

    final IJetAPIResponse response = put(
      config.getArchiveSkuURL( sku ),
      Json.createObjectBuilder()
        .add( "is_archived", isArchived ).build().toString(),
      getJSONHeaderBuilder().build()
    );
    
    return response;    
  }
  
  
  /**
   * At Jet, the price the retailer sets is not the same as the price the 
   * customer pays. The price set for a SKU will be the price the retailer 
   * gets paid for selling the products. However, the price that is set will 
   * influence how competitive your product offer matches up compared to other 
   * product offers for the same SKU.
   * 
   * @param sku Product sku 
   * @return API response 
   * @throws APIException
   * @throws JetException 
   */
  @Override
  public IJetAPIResponse sendGetProductPrice( final String sku ) 
    throws APIException, JetException
  {
    checkSku( sku );
    
    APILog.info( LOG, "Sending GET product price for sku:", sku );
    
    final IJetAPIResponse response = get(
      config.getGetProductPriceURL( sku ),
      getJSONHeaderBuilder().build()
    );
    
    return response;
  }


  /**
   * At Jet, the price the retailer sets is not the same as the price the 
   * customer pays. The price set for a SKU will be the price the retailer 
   * gets paid for selling the products. However, the price that is set will 
   * influence how competitive your product offer matches up compared to other 
   * product offers for the same SKU.
   * 
   * @param sku Product sku 
   * @return API response 
   * @throws APIException
   * @throws JetException 
   */
  @Override
  public ProductPriceRec getProductPrice( final String sku )
    throws APIException, JetException
  {
    return ProductPriceRec.fromJSON( sendGetProductPrice( sku ).getJsonObject());
  }
  
  
  /**
   * Retrieve a single product by sku.
   * Any information about the SKU that was previously uploaded (price, 
   * inventory, shipping exception) will show up here
   * @param sku Product Sku
   * @return response 
   * @throws APIException
   * @throws JetException 
   */
  @Override
  public IJetAPIResponse sendGetProductSku( final String sku )
    throws APIException, JetException
  {
    checkSku( sku );
    
    APILog.info( LOG, "Retrieving ", sku );
    
    return get( config.getGetProductURL( sku ), getPlainHeaderBuilder().build());
  }
  

  /**
   * Retrieve product data
   * @param sku Sku to retrieve
   * @return jet product data
   * @throws APIException
   * @throws JetException
   */
  @Override
  public ProductRec getProduct( final String sku ) throws APIException, JetException
  {
    return ProductRec.fromJSON( sendGetProductSku( sku ).getJsonObject());
  }
  
  
  /**
   * Retrieve product data, pricing, variations, returns exceptions and 
   * shipping exceptions 
   * @param sku product sku 
   * @return Product data 
   * @throws APIException
   * @throws JetException 
   */
  @Override
  public ProductRec getFullProduct( final String sku ) throws APIException, JetException
  {
    final ProductRec.Builder b = getProduct( sku ).toBuilder();
    try {
      ProductPriceRec p = getProductPrice( sku );
      b.setfNodePrices( p.getFulfillmentNodes());
    } catch( Exception e ) {
      System.err.println( e );
    }
    
    b.setVariations( getProductVariations( sku ));
    b.getReturnsExceptions().add( getReturnsExceptions( sku ));
    b.setShippingExceptionNodes( getShippingExceptions( sku));

    return b.build();    
  }
  
  
  /**
   * Retrieve product inventory by sku.
   * The inventory returned from this endpoint represents the number in the 
   * feed, not the quantity that is currently sellable on Jet.com
   * 
   * @param sku Product sku
   * @return api response 
   * @throws APIException
   * @throws JetException 
   */
  @Override
  public IJetAPIResponse sendGetProductInventory( final String sku )
     throws APIException, JetException
  {
    checkSku( sku );
    
    APILog.info( LOG, "Sending GET product inventory for sku:", sku );
    
    final IJetAPIResponse response = get(
      config.getGetProductInventoryURL( sku ),
      getJSONHeaderBuilder().build()
    );
    
    return response;    
  }
  
  
  /**
   * Retrieve product inventory by sku.
   * The inventory returned from this endpoint represents the number in the 
   * feed, not the quantity that is currently sellable on Jet.com
   * 
   * @param sku Product sku
   * @return api response 
   * @throws APIException
   * @throws JetException 
   */  
  @Override
  public ProductInventoryRec getProductInventory( final String sku )
    throws APIException, JetException
  {
    try {
      return ProductInventoryRec.fromJSON( 
        sendGetProductInventory( sku ).getJsonObject());
    } catch( ParseException e ) {
      APILog.error( LOG, 
        "Failed to parse Jet Fulfillment Node lastUpdate Date:", e.getMessage());
      throw new JetException( "getProductPrice result was successful, but "
        + "Fulfillment node had an invalid lastUpdate date", e );
    }
  }

  
  /**
   * Retrieve product shipping exceptions by sku.
   * The shipping exceptions call is used to set up specific methods and costs 
   * for individual SKUs that will override your default settings, with the 
   * ability to drill down to the fulfillment node level.
   * 
   * @param sku Product sku 
   * @return api response 
   * @throws APIException
   * @throws JetException 
   */
  @Override
  public IJetAPIResponse sendGetProductShippingExceptions( final String sku )
    throws APIException, JetException 
  {
    checkSku( sku );
    
    APILog.info( LOG, "Sending GET product shipping exceptions for sku:", sku );
    
    final IJetAPIResponse response = get(
      config.getGetShippingExceptionURL( sku ),
      getJSONHeaderBuilder().build()
    );
    
    return response;    
  }
  
  
  /**
   * Retrieve product variations exceptions by sku.
   * 
   * @param sku Product sku 
   * @return api response 
   * @throws APIException
   * @throws JetException 
   */
  @Override
  public IJetAPIResponse sendGetProductVariations( final String sku )
    throws APIException, JetException 
  {
    checkSku( sku );
    
    APILog.info( LOG, "Sending GET product variations for sku:", sku );
    
    final IJetAPIResponse response = get(
      config.getGetProductVariationURL( sku ),
      getJSONHeaderBuilder().build()
    );
    
    return response;    
  }  
  
    
  /**
   * Retrieve product variations exceptions by sku.
   * 
   * @param sku Product sku 
   * @return api response 
   * @throws APIException
   * @throws JetException 
   */
  @Override
  public ProductVariationGroupRec getProductVariations( final String sku )
    throws APIException, JetException
  {
    checkSku( sku );
    
    try {
      return ProductVariationGroupRec.fromJSON( 
        sku, 
        sendGetProductVariations( sku ).getJsonObject()
      );
    } catch( ClassCastException e ) {
      APILog.error( LOG, 
        "Failed to convert variation_refinements or children_skus to a List" );
      throw new JetException( e.getMessage(), e );
    }
  }


  /**
   * Retrieve a set of product shipping exceptions.
   * @param sku Sku
   * @return exceptions 
   * @throws APIException 
   * @throws JetException
   */
  @Override
  public List<FNodeShippingRec> getShippingExceptions( final String sku )
    throws APIException, JetException
  {
    checkSku( sku );
    
    final JsonArray nodes = sendGetProductShippingExceptions( sku )
      .getJsonObject()
      .getJsonArray( "fulfillment_nodes" );    
    
    final List<FNodeShippingRec> out = new ArrayList<>();
    
    if ( nodes == null )
      return out;
        
    
    for ( int i = 0; i < nodes.size(); i++ )
    {
      out.add( FNodeShippingRec.fromJSON( nodes.getJsonObject( i )));
    }
    
    return out;
    
  }
  
  
  /**
   * Retrieve product returns exceptions by sku.
   * 
   * @param sku Product sku 
   * @return api response 
   * @throws APIException
   * @throws JetException 
   */
  @Override
  public IJetAPIResponse sendGetProductReturnsExceptions( final String sku )
    throws APIException, JetException 
  {
    checkSku( sku );
    
    APILog.info( LOG, "Sending GET product returns exceptions for sku:", sku );
    
    final IJetAPIResponse response = get(
      config.getGetReturnsExceptionURL( sku ),
      getJSONHeaderBuilder().build()
    );
    
    return response;    
  }  
  
  
  /**
   * Retrieve product returns exceptions by sku.
   * 
   * @param sku Product sku 
   * @return api response 
   * @throws APIException
   * @throws JetException 
   */
  @Override
  public ReturnsExceptionRec getReturnsExceptions( final String sku )
    throws APIException, JetException 
  {
    checkSku( sku );
    
    return ReturnsExceptionRec.fromJSON( 
      sendGetProductReturnsExceptions( sku ).getJsonObject());
  }
  
  
  /**
   * This call allows you visibility into the total number of SKUs you have 
   * uploaded. Alternatively, the Partner Portal allows you to download a 
   * CSV file of all SKUs.
   * @param offset The first SKU # you wish to appear in the return
   * @param limit The last SKU # you wish to appear in the return
   * @return api response 
   * @throws APIException
   * @throws JetException 
   */
  @Override
  public IJetAPIResponse sendGetSkuList( final int offset, final int limit )
    throws APIException, JetException 
  {
    if ( offset < 0 )
      throw new IllegalArgumentException( "offset cannot be less than zero" );
    else if ( limit < 1 )
      throw new IllegalArgumentException( "limit cannot be less than one" );
    
    APILog.info( LOG, "Sending GET sku list at (", String.valueOf( offset ), 
       ".", String.valueOf( limit ), ")" );
    
    return get( 
      config.getSkuListURL( offset, limit ),
      getJSONHeaderBuilder().build()
    );
  }
  
  
  /**
   * This call allows you visibility into the total number of SKUs you have 
   * uploaded. Alternatively, the Partner Portal allows you to download a 
   * CSV file of all SKUs.
   * @param offset The first SKU # you wish to appear in the return
   * @param limit The last SKU # you wish to appear in the return
   * @return api response 
   * @throws APIException
   * @throws JetException 
   */
  @Override
  public List<String> getSkuList( final int offset, final int limit )
    throws APIException, JetException 
  {
    //..This shouldn't be able to throw a NullPointerException, need to write tests.....
    return jsonArrayToTokenList( sendGetSkuList( offset, limit )
      .getJsonObject().getJsonArray( "sku_urls" ), false );
  }

  
  /**
   * Get sales data.
   *  
   * Analyze how your individual product price (item and shipping price) compares 
   * to the lowest individual product prices from the marketplace. These prices 
   * are only provided for SKUs that have the status “Available for Sale”. If a 
   * best price does not change, then the last_update time also will not change. 
   * If your inventory is zero, then these prices will not continue to be updated 
   * and will be stale. Note: It may take up to 24 hours to reflect any price 
   * updates from you and the marketplace.
   * 
   * Product pricing is one factor that Jet uses to determine which retailer wins 
   * a basket order. Jet determines what orders retailers will win based on the 
   * the product prices of all products in the order, base commission on those 
   * items as well as commission adjustments set via the Rules Engine. Commission 
   * adjustments set via the Rules Engine can be very effective in optimizing 
   * your win rate and profitability at the order level without having to have 
   * the absolute lowest item and shipping prices.
   * @param sku Product sku 
   * @return data 
   * @throws APIException
   * @throws JetException 
   */
  @Override
  public IJetAPIResponse sendGetSkuSalesData( final String sku )
    throws APIException, JetException
  {
    checkSku( sku );
    
    return get(
      config.getSalesDataBySkuURL( sku ),
      getJSONHeaderBuilder().build()          
    );
  }
  
  
  /**
   * Get sales data.
   *  
   * Analyze how your individual product price (item and shipping price) compares 
   * to the lowest individual product prices from the marketplace. These prices 
   * are only provided for SKUs that have the status “Available for Sale”. If a 
   * best price does not change, then the last_update time also will not change. 
   * If your inventory is zero, then these prices will not continue to be updated 
   * and will be stale. Note: It may take up to 24 hours to reflect any price 
   * updates from you and the marketplace.
   * 
   * Product pricing is one factor that Jet uses to determine which retailer wins 
   * a basket order. Jet determines what orders retailers will win based on the 
   * the product prices of all products in the order, base commission on those 
   * items as well as commission adjustments set via the Rules Engine. Commission 
   * adjustments set via the Rules Engine can be very effective in optimizing 
   * your win rate and profitability at the order level without having to have 
   * the absolute lowest item and shipping prices.
   * @param sku Product sku 
   * @return data 
   * @throws APIException
   * @throws JetException 
   */  
  @Override
  public SkuSalesDataRec getSkuSalesData( final String sku )
    throws APIException, JetException
  {
    return SkuSalesDataRec.fromJSON( sku, 
      sendGetSkuSalesData( sku ).getJsonObject());
  }
  
  

  /**
   * Archive a product sku.
   *
   * Archiving a SKU allows the retailer to "deactivate" a SKU from the catalog.
   * At any point in time, a retailer may decide to "reactivate" the SKU
   * @param sku
   * @param isArchived Indicates whether the specified SKU is archived.
  'true' - SKU is inactive
  'false' - SKU is potentially sellable
   * @return
   * @throws APIException
   * @throws JetException
   */
  @Override
  public boolean archiveSku(final String sku, 
    final boolean isArchived) throws APIException, JetException
  {
    return sendPutArchiveSku( sku, isArchived ).isSuccess();
  }

  /**
   * Adds image url's
   * @param product product data
   * @return success
   * @throws APIException
   * @throws JetException
   * @deprecated Removed from Jet 
   */
  @Override
  public boolean setProductImages(final ProductRec product) throws APIException, JetException
  {
    return sendPutProductImage( product ).isSuccess();
  }

  /**
   * Adds product quantity and inventory data
   * @param product product data
   * @return success
   * @throws APIException
   * @throws JetException
   */
  @Override
  public boolean setProductInventory(final ProductRec product) 
    throws APIException, JetException
  {
    return sendPutProductInventory( product ).isSuccess();
  }

  
  /**
   * Adds product price data
   * @param product
   * @return
   * @throws APIException
   * @throws JetException
   */
  @Override
  public boolean setProductPrice(final ProductRec product) 
    throws APIException, JetException
  {
    return sendPutProductPrice( product ).isSuccess();
  }

  /**
   * Send shipping exceptions to jet
   * @param sku Sku
   * @param nodes Filfillment nodes
   * @return
   * @throws APIException
   * @throws JetException
   */
  @Override
  public boolean setProductShippingExceptions(final String sku, 
    final List<FNodeShippingRec> nodes) throws APIException, JetException
  {
    return sendPutProductShippingExceptions( sku, nodes ).isSuccess();
  }

  /**
   * Adds a product sku.
   * Part of a multi-part operation.
   * This will call merchant-skus/{sku-id}
   *
   * @param product product data
   * @return success
   * @throws APIException
   * @throws JetException
   */
  @Override
  public boolean setProductSku(final ProductRec product) 
    throws APIException, JetException
  {
    return sendPutProductSku( product ).isSuccess();
  }

  /**
   * The variation request is used to create a variation-type relationship
   * between several SKUs. To use this request, one must have already uploaded
   * all the SKUs in question ; they should then choose one "parent" SKU and
   * make the variation request to that SKU, adding as "children" any SKUs they
   * want considered part of the relationship.
   * To denote the particular variation refinements, one must have uploaded one
   * or more attributes in the product call for all the SKUs in question;
   * finally, they are expected to list these attributes in the variation
   * request.
   *
   * @param group data to send
   * @return response from jet
   * @throws APIException if there's a problem
   * @throws JetException
   */
  @Override
  public boolean setProductVariations(
    final ProductVariationGroupRec group) throws APIException, JetException
  {
    return sendPutProductVariation( group ).isSuccess();
  }

  /**
   * The returns exceptions call is used to set up specific methods that will
   * overwrite your default settings on a fulfillment node level for returns.
   * This exception will be used to determine how and to where a product is
   * returned unless the merchant specifies otherwise in the Ship Order message.
   *
   * @param sku Product SKU to modify
   * @param hashes A list of md5 hashes - Each hash is the ID of the returns
   * node that was created on partner.jet.com under fulfillment settings.
   *
   * Must be a valid return node ID set up by the merchant
   *
   * @return response
   * @throws APIException
   * @throws JetException
   */
  @Override
  public boolean setReturnsException(final String sku, 
    final List<String> hashes) throws APIException, JetException
  {
    return sendPutReturnsException( sku, hashes ).isSuccess();
  }
  
  
  
  
  

  /**
   * Simply checks sku for null/empty.
   * If true, then throw an exception
   * @param sku Product sku
   * @throws IllegalArgumentException if sku is null/empty 
   */
  private void checkSku( final String sku ) throws IllegalArgumentException 
  {
    if ( sku == null || sku.isEmpty())
      throw new IllegalArgumentException( "sku cannot be null or empty" );    
  }
}