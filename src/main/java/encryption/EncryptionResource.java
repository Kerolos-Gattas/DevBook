package encryption;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import javax.ws.rs.GET;
import javax.ws.rs.core.MediaType;

//   http://localhost:8080/Assignement1/encryption/encrypt?first=dogg&second=catt
//   http://localhost:8080/Assignement1/encryption/decrypt?first=XI3CKRf2uFlyaBlpuxtIZg==

// This invokes the @get if path encrypt, not the post
// https://jersey.java.net/documentation/latest/jaxrs-resources.html

@Path("/encryption")
public class EncryptionResource {

	private final String STATUS_RUNNING = "Encryption Running";
	private final String SEPERATOR = "/";


	/* see http://stackoverflow.com/questions/7776283/use-post-method-of-jersey-rest-and-gets-params-from-url
	 * for the difference between get and post with parameters
	 */

	// http://localhost:8080/gettingstartedwithrest/encryption/encrypt?first=dogg&second=catt

	@GET
	@Path("encrypt")
	@Produces("application/json")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	//@Consumes("application/x-www-form-urlencoded")
	public Response encryptUserQuery(@QueryParam("first") String firstParameter, 
			@QueryParam("second") String secondParameter) {
		try {
			String token = issueToken(firstParameter + SEPERATOR + secondParameter);
			return Response.ok(token).build();
		} catch (Exception exception) {
			//return Response.status(Response.Status.UNAUTHORIZED).build();
			return Response.ok(exception.getLocalizedMessage()).build();
		}      
	}

	/* see http://stackoverflow.com/questions/7776283/use-post-method-of-jersey-rest-and-gets-params-from-url
	 * for the difference between get and post with parameters
	 */


	@POST
	@Path("encrypt-p")
	@Produces("application/json")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	//@Consumes("application/x-www-form-urlencoded")
	public Response encryptUserForm(@FormParam("first") String firstParameter, 
			@FormParam("second") String secondParameter) {
		try {
			String token = issueToken(firstParameter + SEPERATOR + secondParameter);
			return Response.ok(token).build();
		} catch (Exception exception) {
			//return Response.status(Response.Status.UNAUTHORIZED).build();
			return Response.ok(exception.getLocalizedMessage()).build();
		}      
	}


	// http://localhost:8080/gettingstartedwithrest/encryption/decrypt?first=XI3CKRf2uFlyaBlpuxtIZg==
	// special url char http://www.w3schools.com/tags/ref_urlencode.asp
	@GET
	@Path("decrypt")
	@Produces("application/json")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	//@Consumes("application/x-www-form-urlencoded")
	public Response decryptUserQuery(@QueryParam("first") String firstParameter) {
		try {
			Encryption encrypter = Encryption.getDefaultEncrypter();
			String token = encrypter.decrypt(firstParameter);
			return Response.ok(token).build();
		} catch (Exception exception) {
			//return Response.status(Response.Status.UNAUTHORIZED).build();
			return Response.ok(exception.getLocalizedMessage()).build();
		}      
	}

	@POST
	@Path("decrypt-p")
	@Produces("application/json")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	//@Consumes("application/x-www-form-urlencoded")
	public Response decryptUserForm(@FormParam("first") String firstParameter) {
		try {
			Encryption encrypter = Encryption.getDefaultEncrypter();
			String token = encrypter.decrypt(firstParameter);
			return Response.ok(token).build();
		} catch (Exception exception) {
			//return Response.status(Response.Status.UNAUTHORIZED).build();
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

	// This method is called if TEXT_PLAIN is request
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String sayPlainTextHello() {
		return STATUS_RUNNING + " (TEXT)";
	}

	// This method is called if XML is request
	@GET
	@Produces(MediaType.TEXT_XML)
	public String xmlStatus() {
		return "<?xml version=\"1.0\"?>" + "<status> " + STATUS_RUNNING + " (XML) </status>";
	}

	// This method is called if JSON is request
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String jsonStatus() {
		return "{ \"status\" : \"" + STATUS_RUNNING + "\" }";
	}


	// This method is called if HTML is request
	@GET
	@Produces(MediaType.TEXT_HTML)
	public String htmlStatus() {
		return "<html> " + "<title>" + STATUS_RUNNING + "</title>"
				+ "<body><h1>" + STATUS_RUNNING  + " (HTML) </body></h1>" + "</html> ";
	}

}

