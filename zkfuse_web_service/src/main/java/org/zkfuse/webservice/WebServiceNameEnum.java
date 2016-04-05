package org.zkfuse.webservice;

/**
 * Created with IntelliJ IDEA.
 * User: Sam
 * Date: 9/09/13
 */
public enum WebServiceNameEnum {

   USERNAME_TOKEN_X509_SYMMETRIC_WS( "usernametoken_x509_symmetric/doubleit", "ut-x509-sym" ),
   USERNAME_TOKEN_X509_ASYMMETRIC_WS( "usernametoken_x509_asymmetric/doubleit", "ut-x509-asym" ),
   USERNAME_TOKEN_HASHED_PASSWORD_WS( "usernametoken_hashed_password/doubleit", "ut-hash" ),
   X509_ASYMMETRIC_WS("", "x509-asym");

   private String webServiceName;
   private String webServiceId;

   private WebServiceNameEnum(String webServiceName, String webServiceId) {
       this.webServiceName = webServiceName;
       this.webServiceId = webServiceId;
   }

   public String value() {
      return webServiceName;
   }

   public String getId() {
      return webServiceId;
   }

}
