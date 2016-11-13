package encryptionTesting;

import static org.junit.Assert.*;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.*;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;

import org.glassfish.jersey.client.ClientProperties;
import org.junit.Test;

public class EncryptionTesting {

    private static String RAW_STRING = "this is the string to encrypt";
    public static final int TEN_SECONDS = 10000;
    public static final String SERVER_URL = "http://localhost:8080";
    public static final String SERVER_WEB_APP_NAME = "DevBook/encryption";
    public static final String ENCRYPTION_REQUEST = "/encrypt-p";
    public static final String DECRYPTION_REQUEST = "/decrypt-p";
    public static final String DOGG = "dogg";
    public static final String CATT = "catt";  
    
    
    
    @Test
	public void testPost() {	
		Client client = null;
        try {
            Form firstForm = new Form();
            firstForm.param("first", DOGG);
            firstForm.param("second", CATT);
            
            /* Another approach
            client = ClientBuilder.newClient();            
            WebTarget webTarget = client.target("http://localhost:8080/gettingstartedwithrest/encryption").path("encrypt-p");
            Response response = webTarget.request().post(Entity.entity(firstForm, MediaType.APPLICATION_FORM_URLENCODED));
            System.out.println("HI " + response.readEntity(String.class));
            String encryptionResponse = response.readEntity(String.class);
            */
 
   			client = ClientBuilder.newClient();
            String encryptionResponse = client
                    .property(ClientProperties.CONNECT_TIMEOUT, TEN_SECONDS)
                    .property(ClientProperties.READ_TIMEOUT, TEN_SECONDS)
                    .target(SERVER_URL)
                    .path(SERVER_WEB_APP_NAME + ENCRYPTION_REQUEST)
                    .request()
                    .post(Entity.entity(firstForm,MediaType.APPLICATION_FORM_URLENCODED_TYPE), String.class);
            client.close();
            
            Form secondForm = new Form();
            secondForm.param("first", encryptionResponse);
            client = ClientBuilder.newClient();
            String decryptionResponse = client
                    .property(ClientProperties.CONNECT_TIMEOUT, TEN_SECONDS)
                    .property(ClientProperties.READ_TIMEOUT, TEN_SECONDS)
                    .target(SERVER_URL)
                    .path(SERVER_WEB_APP_NAME + DECRYPTION_REQUEST)
                    .request()
                    //.post(Entity.entity(form,MediaType.APPLICATION_FORM_URLENCODED_TYPE), String.class);
                    .post(Entity.entity(secondForm,MediaType.APPLICATION_FORM_URLENCODED_TYPE), String.class); 
            
            client.close();
            assertEquals(DOGG + "/" + CATT, decryptionResponse);
        }
        catch (Exception exception) {
            client.close();
            fail();
        }
	}
}
