package encryptionTesting;

import static org.junit.Assert.*;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;

import org.glassfish.jersey.client.ClientProperties;
import org.junit.Test;

import encryption.Encryption;

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
    public static final String username1 = "Kerolos";
    public static final String password1 = "1234"; 
    public static final String email1 = "frenchkento_10@hotmail.com";
    public static final String username2 = "Kento";
    public static final String password2 = "5678"; 
    public static final String email2 = "kerolos_gattas@hotmail.com";
    
    //storing responses
    String createdUserResponse;
    String encryptedResponse1;
    String encryptedResponse2;
    String decryptionResponse;
    String userLoggedOut;
    String validateStr;
	
	private String issueToken(String username) throws Exception {
		// Issue a token (can be a random String persisted to a database or a JWT token)
		// The issued token must be associated to a user
		// Return the issued token	
		try {
			Encryption encrypter = Encryption.getDefaultEncrypter();
			return encrypter.encrypt(username);

		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("Failed to create encrypted token");
		}
	}
    
    //commented tests are tests that would make the junit tests fail, 
    //they are only to see if the code performs well in case of failures and returns the correct output.
    //they are expected to fail.
	@Test
	public void test() throws Exception {
		Client client = null;
		
			String firstUsername = issueToken(username1);
			String firstPassword = issueToken(password1);
			String firstEmail = issueToken(email1);
			String secondUsername = issueToken(username2);
			String secondPassword = issueToken(password2);
			String secondEmail = issueToken(email2);
			//testing creating a user, 
			//if tests are ran twice in a row without reseting the server the create user tests will fail since they
			//are already created and the server will refuse duplicate usernames
            Form userInfo1 = new Form();
            userInfo1.param("userName", firstUsername);
            userInfo1.param("password", firstPassword);
            userInfo1.param("email", firstEmail);
            
   			client = ClientBuilder.newClient();
   			createdUserResponse = client
                    .property(ClientProperties.CONNECT_TIMEOUT, TEN_SECONDS)
                    .property(ClientProperties.READ_TIMEOUT, TEN_SECONDS)
                    .target(SERVER_URL)
                    .path(SERVER_WEB_APP_NAME + CREATE_REQUEST)
                    .request()
                    .post(Entity.entity(userInfo1,MediaType.APPLICATION_FORM_URLENCODED_TYPE), String.class);
            client.close();
            assertEquals("Created Success", createdUserResponse);
            
            Form userInfo2 = new Form();
            userInfo2.param("userName", secondUsername);
            userInfo2.param("password", secondPassword);
            userInfo2.param("email", secondEmail);
            
   			client = ClientBuilder.newClient();
   			createdUserResponse = client
                    .property(ClientProperties.CONNECT_TIMEOUT, TEN_SECONDS)
                    .property(ClientProperties.READ_TIMEOUT, TEN_SECONDS)
                    .target(SERVER_URL)
                    .path(SERVER_WEB_APP_NAME + CREATE_REQUEST)
                    .request()
                    .post(Entity.entity(userInfo2,MediaType.APPLICATION_FORM_URLENCODED_TYPE), String.class);
            client.close();
            assertEquals("Created Success", createdUserResponse);

            
            //testing the login path
            Form loginInfo1 = new Form();
            loginInfo1.param("userName", firstUsername);
            loginInfo1.param("password", firstPassword);

            client = ClientBuilder.newClient();
            encryptedResponse1 = client
                    .property(ClientProperties.CONNECT_TIMEOUT, TEN_SECONDS)
                    .property(ClientProperties.READ_TIMEOUT, TEN_SECONDS)
                    .target(SERVER_URL)
                    .path(SERVER_WEB_APP_NAME + LOGIN_REQUEST)
                    .request()
                    .post(Entity.entity(loginInfo1,MediaType.APPLICATION_FORM_URLENCODED_TYPE), String.class); 
            client.close();
            
            Form loginInfo2 = new Form();
            loginInfo2.param("userName", secondUsername);
            loginInfo2.param("password", secondPassword);
            client = ClientBuilder.newClient();
            encryptedResponse2 = client
                    .property(ClientProperties.CONNECT_TIMEOUT, TEN_SECONDS)
                    .property(ClientProperties.READ_TIMEOUT, TEN_SECONDS)
                    .target(SERVER_URL)
                    .path(SERVER_WEB_APP_NAME + LOGIN_REQUEST)
                    .request()
                    .post(Entity.entity(loginInfo2,MediaType.APPLICATION_FORM_URLENCODED_TYPE), String.class); 
            client.close();

			
            //testing the validate path
            Form validate = new Form();
            validate.param("credentials", encryptedResponse1);
            validate.param("userName", firstUsername);
            
            client = ClientBuilder.newClient();
            validateStr = client
                    .property(ClientProperties.CONNECT_TIMEOUT, TEN_SECONDS)
                    .property(ClientProperties.READ_TIMEOUT, TEN_SECONDS)
                    .target(SERVER_URL)
                    .path(SERVER_WEB_APP_NAME + VALIDATE_REQUEST)
                    .request()
                    .post(Entity.entity(validate,MediaType.APPLICATION_FORM_URLENCODED_TYPE), String.class); 
            client.close();
            assertEquals("Validate Success", validateStr);
            
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
            Form logout1 = new Form();
            logout1.param("logoutCredentials", encryptedResponse1);
            logout1.param("userName", firstUsername);
            
            client = ClientBuilder.newClient();
            userLoggedOut = client
                    .property(ClientProperties.CONNECT_TIMEOUT, TEN_SECONDS)
                    .property(ClientProperties.READ_TIMEOUT, TEN_SECONDS)
                    .target(SERVER_URL)
                    .path(SERVER_WEB_APP_NAME + LOGOUT_REQUEST)
                    .request()
                    .post(Entity.entity(logout1,MediaType.APPLICATION_FORM_URLENCODED_TYPE), String.class); 
            client.close();
            assertEquals("Logout success", userLoggedOut);
			
            client = ClientBuilder.newClient();
            encryptedResponse1 = client
                    .property(ClientProperties.CONNECT_TIMEOUT, TEN_SECONDS)
                    .property(ClientProperties.READ_TIMEOUT, TEN_SECONDS)
                    .target(SERVER_URL)
                    .path(SERVER_WEB_APP_NAME + LOGIN_REQUEST)
                    .request()
                    .post(Entity.entity(loginInfo1,MediaType.APPLICATION_FORM_URLENCODED_TYPE), String.class); 
            client.close();
			
            Form logout2 = new Form();
            logout2.param("logoutCredentials", encryptedResponse2);
            logout2.param("userName", secondUsername);

            client = ClientBuilder.newClient();
            userLoggedOut = client
                    .property(ClientProperties.CONNECT_TIMEOUT, TEN_SECONDS)
                    .property(ClientProperties.READ_TIMEOUT, TEN_SECONDS)
                    .target(SERVER_URL)
                    .path(SERVER_WEB_APP_NAME + LOGOUT_REQUEST)
                    .request()
                    .post(Entity.entity(logout2,MediaType.APPLICATION_FORM_URLENCODED_TYPE), String.class); 
            client.close();
            assertEquals("Logout success", userLoggedOut);
			
            client = ClientBuilder.newClient();
            encryptedResponse2 = client
                    .property(ClientProperties.CONNECT_TIMEOUT, TEN_SECONDS)
                    .property(ClientProperties.READ_TIMEOUT, TEN_SECONDS)
                    .target(SERVER_URL)
                    .path(SERVER_WEB_APP_NAME + LOGIN_REQUEST)
                    .request()
                    .post(Entity.entity(loginInfo2,MediaType.APPLICATION_FORM_URLENCODED_TYPE), String.class); 
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

}
