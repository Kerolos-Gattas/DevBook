package restCommunication;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import encryption.Encryption;
import models.Account;
import models.Members;
import models.Project;
import models.Requests;
import persistence.HibernateDatabaseAccountManager;
import persistence.HibernateDatabaseMembersManager;
import persistence.HibernateDatabaseProjectManager;
import persistence.HibernateDatabaseRequestManager;

@Path("/project")
public class ProjectCommunication {

	private final String SEPERATOR = "/";

	private static HibernateDatabaseProjectManager manager;
	private static HibernateDatabaseRequestManager requestManager;
	private static HibernateDatabaseMembersManager membersManager;
	private static HibernateDatabaseAccountManager AccountManager;

	@POST
	@Path("create-p")
	@Produces("application/json")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response create(@FormParam("admin") String admin, 
			@FormParam("projectName") String projectName, @FormParam("city") String city, 
			@FormParam("country") String country, @FormParam("description") String description,
			@FormParam("tools") String tools, @FormParam("languages") String languages) {
		try {
			
			intializeManager();
			Project project = new Project(admin, projectName, city, country, description, tools, languages);
			if(manager.add(project)){
				Project temp = manager.selectProjectsbyAdminandTitle(admin, projectName);
				Members member = new Members(temp.getId(), admin);
				if(membersManager.add(member))
					return Response.ok("Created Success").build();
			}
			
			return Response.status(406).build(); 
		} 
		catch (Exception exception) {
			return Response.ok(exception.getLocalizedMessage()).build();
		}      
	}
	
	@POST
	@Path("projectsNearBy-p")
	@Produces("application/json")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public JSONObject getProjectsNearby(@FormParam("userName") String userName){
		JSONObject error = null;

		try{
			intializeManager();
			List<Project> projects = manager.selectProjectsNearUser(userName);
			if(projects != null){
				JSONObject projectsObj = new JSONObject();
				JSONArray projectsList = new JSONArray();
				for(int i = 0; i < projects.size(); i++){
					projectsList.add(projects.get(i));
				}
				projectsObj.put("projects", projectsList);
				return projectsObj;
			}
			return error;
			
		}
		catch(Exception exception) {
			return error;
		}
	}
	
	@POST
	@Path("projectsRequests-p")
	@Produces("application/json")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public JSONObject getProjectsRequests(@FormParam("admin") String admin){
		JSONObject error = null;

		try{
			intializeManager();
			String[] projectRequestsStr = requestManager.getProjectRequests(admin);
			List<Requests> requestsIDS = requestManager.getRequestsByAdmin(admin);
			
			if(requestsIDS != null && projectRequestsStr != null){
				
				JSONObject projectsObj = new JSONObject();
				JSONArray jsonRequests = new JSONArray();
				JSONArray jsonProjectIds = new JSONArray();

				for(int i = 0; i < projectRequestsStr.length; i++){
					jsonRequests.add(projectRequestsStr[i]);
				}
				
				for(int i = 0; i < requestsIDS.size(); i++){
					jsonProjectIds.add(requestsIDS.get(i));
				}
				
				projectsObj.put("requestsStr", jsonRequests);
				projectsObj.put("requestsID", jsonProjectIds);

				return projectsObj;
			}
			return error;
			
		}
		catch(Exception exception) {
			return error;
		}
	}
	
	@POST
	@Path("userBio-p")
	@Produces("application/json")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public JSONObject getUserBio(@FormParam("userName") String userName){
		JSONObject error = null;

		try{
			intializeManager();
			Account account = AccountManager.getAccountByUserName(userName);
			if(account != null){
				Encryption encrypter = Encryption.getDefaultEncrypter();
				String userNameDecrypted = encrypter.decrypt(userName);
				
				JSONObject projectObj = new JSONObject();
				projectObj.put("userName", userNameDecrypted);
				projectObj.put("bio", account.getBio());
				return projectObj;
			}
			return error;
			
		}
		catch(Exception exception) {
			return error;
		}
	}
	
	@POST
	@Path("currentProjects-p")
	@Produces("application/json")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public JSONObject getCurrentProjects(@FormParam("userName") String userName){
		JSONObject error = null;

		try{
			intializeManager();
			List<Members> membersProjects = membersManager.getProjectsByUsername(userName);
			List<Project> projects = new ArrayList<Project>();
			String[] projectTitles = new String[membersProjects.size()];
			int[] projectIds = new int[membersProjects.size()];
			
			for(int i = 0; i < membersProjects.size(); i++){
				projects.add(manager.selectProjectsbyId(membersProjects.get(i).getId()));
				projectTitles[i] = projects.get(i).getProjectName();
				projectIds[i] = membersProjects.get(i).getId();
				System.err.println(projects.get(i).getProjectName());
			}
			
			if(membersProjects != null && projects != null){
				JSONObject projectsObj = new JSONObject();
				JSONArray jsonids = new JSONArray();
				JSONArray jsonTitles = new JSONArray();

				for(int i = 0; i < membersProjects.size(); i++){
					jsonids.add(projectIds[i]);			
					jsonTitles.add(projectTitles[i]);
				}
				
				projectsObj.put("ids", jsonids);
				projectsObj.put("titles", jsonTitles);

				return projectsObj;
			}
			return error;
			
		}
		catch(Exception exception) {
			return error;
		}
	}
	
	@POST
	@Path("acceptRequest-p")
	@Produces("application/json")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response acceptRequest(@FormParam("id") String id, @FormParam("admin") String admin, @FormParam("userName") String userName){

		try{
			
			Requests request = new Requests(Integer.parseInt(id), admin, userName);
			if(requestManager.delete(request)){
				Members member = new Members(Integer.parseInt(id), userName);
				if(membersManager.add(member))
					return Response.ok("Success").build();
			}
			
			return Response.status(406).build(); 
		}
		catch(Exception exception) {
			return Response.status(406).build();
		}
	}
	
	@POST
	@Path("refuseRequest-p")
	@Produces("application/json")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response refuseRequest(@FormParam("id") String id, @FormParam("admin") String admin, @FormParam("userName") String userName){

		try{
			
			Requests request = new Requests(Integer.parseInt(id), admin, userName);
			if(requestManager.delete(request)){
				return Response.ok("Success").build();
			}
			
			return Response.status(406).build(); 
		}
		catch(Exception exception) {
			return Response.status(406).build();
		}
	}
	
	@POST
	@Path("projectID-p")
	@Produces("application/json")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public JSONObject getProjectbyId(@FormParam("id") String idStr){
		JSONObject error = null;

		try{
			intializeManager();
			int id = Integer.parseInt(idStr);
			Project project = manager.selectProjectsbyId(id);
			if(project != null){
				JSONObject projectObj = new JSONObject();
				projectObj.put("project", project);
				return projectObj;
			}
			return error;
			
		}
		catch(Exception exception) {
			return error;
		}
	}
	
	@POST
	@Path("getMessages-p")
	@Produces("application/json")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public JSONObject getProjectMessages(@FormParam("id") String idStr){
		JSONObject error = null;

		try{
			intializeManager();
			int id = Integer.parseInt(idStr);
			Project project = manager.selectProjectsbyId(id);
			if(project != null){
				JSONObject projectObj = new JSONObject();
				projectObj.put("messages", project.getMessages());
				return projectObj;
			}
			return error;
			
		}
		catch(Exception exception) {
			return error;
		}
	}
	
	@POST
	@Path("addMessages-p")
	@Produces("application/json")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response addProjectMessages(@FormParam("id") String idStr, @FormParam("message") String message, @FormParam("userName") String userName){

		try{
			intializeManager();
			int id = Integer.parseInt(idStr);
			Project project = manager.selectProjectsbyId(id);
			if(project != null){
				Encryption encrypter = Encryption.getDefaultEncrypter();
				String userNameDecrypted = encrypter.decrypt(userName);
				project.addMessages(userNameDecrypted + ": " + message + "\n");
				
				if(manager.update(project))
					return Response.ok("Success").build();
			}
			return Response.status(406).build();
			
		}
		catch(Exception exception) {
			return Response.status(406).build();
		}
	}
	
	@POST
	@Path("addRequest-p")
	@Produces("application/json")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response addRequest(@FormParam("id") String idStr, @FormParam("admin") String admin, 
			@FormParam("userName") String userName){
		
		try{
			intializeManager();
			Requests request = new Requests(Integer.parseInt(idStr), admin, userName);
			if(requestManager.add(request)){
				return Response.ok("Success").build();
			}
			return Response.status(406).build(); 
		}
		catch(Exception exception) {
			return Response.status(406).build(); 
		}
	}
	
	@POST
	@Path("addMember-p")
	@Produces("application/json")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response addMember(@FormParam("id") String idStr, @FormParam("userName") String userName){
		
		try{
			intializeManager();
			Members request = new Members(Integer.parseInt(idStr), userName);
			if(membersManager.add(request)){
				return Response.ok("Member added").build();
			}
			return Response.status(406).build(); 
		}
		catch(Exception exception) {
			return Response.status(406).build(); 
		}
	}
	
	private void intializeManager(){
		if(manager == null){
			manager = manager.getDefault();
			manager.setupTable();
		}
		if(requestManager == null){
			requestManager = requestManager.getDefault();
			requestManager.setupTable();
		}
		if(membersManager == null){
			membersManager = membersManager.getDefault();
			membersManager.setupTable();
		}
		if(AccountManager == null){
			AccountManager = AccountManager.getDefault();
			AccountManager.setupTable();
		}
	}
}
