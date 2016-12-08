package encryption;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import models.Account;
import persistence.HibernateDatabaseAccountManager;

@Path("/authentication")
public class Authentication {

	//stroing the lists of created users and loggedin users
	//private static Map<String,String> loginCredentials = new HashMap<String,String>();
	//private static ArrayList<String> loggedIn = new ArrayList<String>();
	
	private final String SEPERATOR = "/";

	private static HibernateDatabaseAccountManager manager;

	final int LOGIN_EXPIRY_TIME = 30000; //Secs
	DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@POST
	@Path("create-p")
	@Produces("application/json")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response create(@FormParam("userName") String userName, 
			@FormParam("password") String password, @FormParam("email") String email,
			@FormParam("city") String city, @FormParam("country") String country, @FormParam("bio") String bio) {
		try {
			//String userNameToken = issueToken(userName);
			//String passwordToken = issueToken(password);
			//String emailToken = issueToken(email);
			
			//check if user already exists, if so return an error to the user otherwise create the user 
			Account userExists = getAccount(userName);
			Account emailExists = getEmail(email);
			
			if(userExists == null && emailExists == null){
				Account account = new Account(userName, password, email, city, country, bio);
				manager.add(account);
				return Response.ok("Created Success").build();
			}
			//TODO add why it failed
			return Response.status(406).build(); 
		} 
		catch (Exception exception) {
			return Response.ok(exception.getLocalizedMessage()).build();
		}      
	}

	@POST
	@Path("login-p")
	@Produces("application/json")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response login(@FormParam("userName") String userName, 
			@FormParam("password") String password) {
		try {			
			
			Account account = getAccount(userName);

			//check if user is already logged in
			if(account == null || account.getLoggedIn() == 1){
				return Response.status(406).build();
			}

			//if user is not logged in and the login matches the one in the database then the user logs in successfully
			if(account.getPassword().equals(password)){
				Timestamp timestamp = new Timestamp(System.currentTimeMillis());
				String loginToken = issueToken(userName + SEPERATOR + timestamp.getTime());
				account.setLastLoginTime(loginToken);
				account.setLoggedIn(1);
				manager.update(account);
				return Response.ok(loginToken).build();
			}
			return Response.status(406).build();
		} catch (Exception exception) {
			return Response.ok(exception.getLocalizedMessage()).build();
		}      
	}

	@POST
	@Path("logout-p")
	@Produces("application/json")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response logout(@FormParam("logoutCredentials") String logoutCredentials, 
			@FormParam("userName") String userName) {
		try {		
			Account account = getAccount(userName);			
			
			//if user is logged in then remove them from the list and send a success message
			if(account.getLastLoginTime().equals(logoutCredentials)){
				account.setLoggedIn(0);
				account.setLastLoginTime(null);
				manager.update(account);
				return Response.ok("Logout success").build();
			}

			return Response.status(406).build();
		} catch (Exception exception) {
			return Response.ok(exception.getLocalizedMessage()).build();
		}      
	}

	@POST
	@Path("validate-p")
	@Produces("application/json")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response validate(@FormParam("credentials") String credentials, 
			@FormParam("userName") String userName) {
		try {
			//decrypt the token
			Encryption encrypter = Encryption.getDefaultEncrypter();
			String token = encrypter.decrypt(credentials);

			Account account = getAccount(userName);

			//check if the user is logged in
			if(account.getLoggedIn() == 0){
				return Response.status(406).build();
			}

			//add the specified time to the timestamp associated with the user
			Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			Long cuurentTime = timestamp.getTime();
			
			String orignalDate = token.substring(token.lastIndexOf(SEPERATOR) + 1);
			Long orignalTime = Long.parseLong(orignalDate) + LOGIN_EXPIRY_TIME;
			

			//compare dates and see if the user login is still valid
			if(cuurentTime < orignalTime){
				return Response.ok("Validate Success").build();
			}

			return Response.status(406).build();
		} catch (Exception exception) {
			return Response.ok(exception.getLocalizedMessage()).build();
		}      
	}

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

	//adds the specified time to the date
	/*private Date addTimeToDate(Date orignalTimeStamp){
		Calendar cal = Calendar.getInstance();
		cal.setTime(orignalTimeStamp);

		//change this to MINUTE instead of SECOND if you want to add minutes instead. As well as adjust the LOGIN_EXPIRY_TIME variable above.
		cal.add(Calendar.SECOND, LOGIN_EXPIRY_TIME); 
		Date newTime = cal.getTime();
		return newTime;
	}*/
	
	private Account getAccount(String userName){
		
		if(manager == null){
			manager = manager.getDefault();
			manager.setupTable();
		}		

		Account account = manager.getAccountByUserName(userName);
		return account;
	}
	
	private Account getEmail(String email){
		
		if(manager == null){
			manager = manager.getDefault();
			manager.setupTable();
		}		

		Account account = manager.getAccountByEmail(email);
		return account;
	}
}
