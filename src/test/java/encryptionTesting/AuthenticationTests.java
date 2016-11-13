package encryptionTesting;

import static org.junit.Assert.*;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;

import org.glassfish.jersey.client.ClientProperties;
import org.junit.Test;

public class AuthenticationTests {
	
    public static final int TEN_SECONDS = 10000;
    public static final String SERVER_URL = "http://localhost:8080";
    public static final String SERVER_WEB_APP_NAME = "DevBook/authentication";
    
    //paths
    public static final String CREATE_REQUEST = "/create-p";
    public static final String LOGIN_REQUEST = "/login-p";
    public static final String LOGOUT_REQUEST = "/logout-p";
    public static final String VALIDATE_REQUEST = "/validate-p";
    
    //usernames and passwords
    public static final String firstUsername = "Kerolos";
    public static final String firstPassword = "1234"; 
    public static final String secondUsername = "Kento";
    public static final String secondPassword = "5678"; 
    
    //storing responses
    String createdUserResponse;
    String encryptedResponse1;
    String encryptedResponse2;
    String decryptionResponse;
    String userLoggedOut;
    String validateStr;
	
    //commented tests are tests that would make the junit tests fail, 
    //they are only to see if the code performs well in case of failures and returns the correct output.
    //they are expected to fail.
	@Test
	public void test() {
		Client client = null;
		
		try{
			//testing creating a user, 
			//if tests are ran twice in a row without reseting the server the create user tests will fail since they
			//are already created and the server will refuse duplicate usernames
            Form userInfo1 = new Form();
            userInfo1.param("first", firstUsername);
            userInfo1.param("second", firstPassword);
            
   			client = ClientBuilder.newClient();
   			createdUserResponse = client
                    .property(ClientProperties.CONNECT_TIMEOUT, TEN_SECONDS)
                    .property(ClientProperties.READ_TIMEOUT, TEN_SECONDS)
                    .target(SERVER_URL)
                    .path(SERVER_WEB_APP_NAME + CREATE_REQUEST)
                    .request()
                    .post(Entity.entity(userInfo1,MediaType.APPLICATION_FORM_URLENCODED_TYPE), String.class);
            client.close();
            assertEquals("Created", createdUserResponse);
            
            Form userInfo2 = new Form();
            userInfo2.param("first", secondUsername);
            userInfo2.param("second", secondPassword);
            
   			client = ClientBuilder.newClient();
   			createdUserResponse = client
                    .property(ClientProperties.CONNECT_TIMEOUT, TEN_SECONDS)
                    .property(ClientProperties.READ_TIMEOUT, TEN_SECONDS)
                    .target(SERVER_URL)
                    .path(SERVER_WEB_APP_NAME + CREATE_REQUEST)
                    .request()
                    .post(Entity.entity(userInfo2,MediaType.APPLICATION_FORM_URLENCODED_TYPE), String.class);
            client.close();
            assertEquals("Created", createdUserResponse);

            
            //testing the login path
            client = ClientBuilder.newClient();
            encryptedResponse1 = client
                    .property(ClientProperties.CONNECT_TIMEOUT, TEN_SECONDS)
                    .property(ClientProperties.READ_TIMEOUT, TEN_SECONDS)
                    .target(SERVER_URL)
                    .path(SERVER_WEB_APP_NAME + LOGIN_REQUEST)
                    .request()
                    .post(Entity.entity(userInfo1,MediaType.APPLICATION_FORM_URLENCODED_TYPE), String.class); 
            client.close();
            
            client = ClientBuilder.newClient();
            encryptedResponse2 = client
                    .property(ClientProperties.CONNECT_TIMEOUT, TEN_SECONDS)
                    .property(ClientProperties.READ_TIMEOUT, TEN_SECONDS)
                    .target(SERVER_URL)
                    .path(SERVER_WEB_APP_NAME + LOGIN_REQUEST)
                    .request()
                    .post(Entity.entity(userInfo2,MediaType.APPLICATION_FORM_URLENCODED_TYPE), String.class); 
            client.close();

			
            //testing the validate path
            Form validate = new Form();
            validate.param("first", encryptedResponse1);
            
            client = ClientBuilder.newClient();
            validateStr = client
                    .property(ClientProperties.CONNECT_TIMEOUT, TEN_SECONDS)
                    .property(ClientProperties.READ_TIMEOUT, TEN_SECONDS)
                    .target(SERVER_URL)
                    .path(SERVER_WEB_APP_NAME + VALIDATE_REQUEST)
                    .request()
                    .post(Entity.entity(validate,MediaType.APPLICATION_FORM_URLENCODED_TYPE), String.class); 
            client.close();
            assertEquals("true", validateStr);
            
            /*
            //trying to login again.This test is expected to fail
            client = ClientBuilder.newClient();
            encryptedResponse = client
                    .property(ClientProperties.CONNECT_TIMEOUT, TEN_SECONDS)
                    .property(ClientProperties.READ_TIMEOUT, TEN_SECONDS)
                    .target(SERVER_URL)
                    .path(SERVER_WEB_APP_NAME + LOGIN_REQUEST)
                    .request()
                    .post(Entity.entity(userInfo,MediaType.APPLICATION_FORM_URLENCODED_TYPE), String.class); 
            client.close();*/
			
            //testing the logout path
            Form logout = new Form();
            logout.param("first", encryptedResponse1);
            
            client = ClientBuilder.newClient();
            userLoggedOut = client
                    .property(ClientProperties.CONNECT_TIMEOUT, TEN_SECONDS)
                    .property(ClientProperties.READ_TIMEOUT, TEN_SECONDS)
                    .target(SERVER_URL)
                    .path(SERVER_WEB_APP_NAME + LOGOUT_REQUEST)
                    .request()
                    .post(Entity.entity(logout,MediaType.APPLICATION_FORM_URLENCODED_TYPE), String.class); 
            client.close();
            assertEquals("User Logged out success", userLoggedOut);
			
            client = ClientBuilder.newClient();
            encryptedResponse1 = client
                    .property(ClientProperties.CONNECT_TIMEOUT, TEN_SECONDS)
                    .property(ClientProperties.READ_TIMEOUT, TEN_SECONDS)
                    .target(SERVER_URL)
                    .path(SERVER_WEB_APP_NAME + LOGIN_REQUEST)
                    .request()
                    .post(Entity.entity(userInfo1,MediaType.APPLICATION_FORM_URLENCODED_TYPE), String.class); 
            client.close();
			
            
            logout.param("first", encryptedResponse2);
            
            client = ClientBuilder.newClient();
            userLoggedOut = client
                    .property(ClientProperties.CONNECT_TIMEOUT, TEN_SECONDS)
                    .property(ClientProperties.READ_TIMEOUT, TEN_SECONDS)
                    .target(SERVER_URL)
                    .path(SERVER_WEB_APP_NAME + LOGOUT_REQUEST)
                    .request()
                    .post(Entity.entity(logout,MediaType.APPLICATION_FORM_URLENCODED_TYPE), String.class); 
            client.close();
            assertEquals("User Logged out success", userLoggedOut);
			
            client = ClientBuilder.newClient();
            encryptedResponse2 = client
                    .property(ClientProperties.CONNECT_TIMEOUT, TEN_SECONDS)
                    .property(ClientProperties.READ_TIMEOUT, TEN_SECONDS)
                    .target(SERVER_URL)
                    .path(SERVER_WEB_APP_NAME + LOGIN_REQUEST)
                    .request()
                    .post(Entity.entity(userInfo1,MediaType.APPLICATION_FORM_URLENCODED_TYPE), String.class); 
            client.close();
            
			/*
			//testing validate after the time expires. This test is expected to fail.
			
			//you will have to change this variable if you changed the LOGIN_EXPIRY_TIME variable in Authentication endpoint
			TimeUnit.SECONDS.sleep(35); 
			
            client = ClientBuilder.newClient();
            validateStr = client
                    .property(ClientProperties.CONNECT_TIMEOUT, TEN_SECONDS)
                    .property(ClientProperties.READ_TIMEOUT, TEN_SECONDS)
                    .target(SERVER_URL)
                    .path(SERVER_WEB_APP_NAME + VALIDATE_REQUEST)
                    .request()
                    .post(Entity.entity(validate,MediaType.APPLICATION_FORM_URLENCODED_TYPE), String.class); 
            client.close();*/
            
		}
        catch (Exception exception) {
            client.close();
            fail();
        }
	}

}
