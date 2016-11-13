package encryption;

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

@Path("/authentication")
public class Authentication {
	
	//stroing the lists of created users and loggedin users
	private static Map<String,String> loginCredentials = new HashMap<String,String>();
	private static ArrayList<String> loggedIn = new ArrayList<String>();
	
	private final String SEPERATOR = "/";
	
	final int LOGIN_EXPIRY_TIME = 30; //Secs
	DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@POST
	@Path("create-p")
	@Produces("application/json")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response create(@FormParam("first") String userName, 
			@FormParam("second") String password) {
		try {
			String token = issueToken(userName + SEPERATOR + password);
			
			//check if user already exists, if so return an error to the user otherwise create the user 
			boolean userExists = loginCredentials.containsKey(userName);
			if(!userExists){
				loginCredentials.put(userName, password);
				return Response.ok("Created").build();
			}

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
	public Response login(@FormParam("first") String userName, 
			@FormParam("second") String password) {
		try {
			String retrievedPassword = loginCredentials.get(userName);
			boolean userLoggedIn = false;
			
			//check if user is already logged in
			for(int i = 0; i < loggedIn.size(); i++){
				if(loggedIn.get(i).contains(userName))
					userLoggedIn = true;
			}

			//if user is not logged in and the login matches the one in the database then the user logs in successfully
			if(retrievedPassword != null && !userLoggedIn){
				if(retrievedPassword.equals(password)){
					String date = format.format(new Date());
					String token = issueToken(userName + SEPERATOR + date);
					loggedIn.add(userName + SEPERATOR + date); //store the user in the list of logged in users
					return Response.ok(token).build();
				}
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
	public Response logout(@FormParam("first") String firstParameter) {
		try {
			//decrypt the token and compare it to the exsisting one
			Encryption encrypter = Encryption.getDefaultEncrypter();
			String token = encrypter.decrypt(firstParameter);
			
			//if user is logged in then remove them from the list and send a success message
			if(loggedIn.contains(token)){
				loggedIn.remove(token);
				return Response.ok("User Logged out success").build();
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
	public Response validate(@FormParam("first") String firstParameter) {
		try {
			//decrypt the token
			Encryption encrypter = Encryption.getDefaultEncrypter();
			String token = encrypter.decrypt(firstParameter);
			
			//check if the user is logged in
			if(!loggedIn.contains(token)){
				return Response.status(406).build();
			}

			//add the specified time to the timestamp associated with the user
			String orignalDate = token.substring(token.lastIndexOf(SEPERATOR) + 1);
			Date date = addTimeToDate(format.parse(orignalDate));
			//get current date
			Date currentDate = new Date();

			//compare dates and see if the user login is still valid
			if(date.after(currentDate)){
				return Response.ok("true").build();
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
	private Date addTimeToDate(Date orignalTimeStamp){
		Calendar cal = Calendar.getInstance();
		cal.setTime(orignalTimeStamp);
		
		//change this to MINUTE instead of SECOND if you want to add minutes instead. As well as adjust the LOGIN_EXPIRY_TIME variable above.
		cal.add(Calendar.SECOND, LOGIN_EXPIRY_TIME); 
		Date newTime = cal.getTime();
		return newTime;
	}
}
