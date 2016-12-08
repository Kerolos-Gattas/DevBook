package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="PROJECT")
public class Project {

	@Id
	@Column(name="id")
	private int id;
	
	@Column(name="admin")
	private String admin;
	
	@Column(name="name")
	private String projectName;
	
	@Column(name="CITY")
	private String city;

	@Column(name="COUNTRY")
	private String country;
	
	@Column(name="description")
	private String description;
	
	@Column(name="tools")
	private String tools;
	
	@Column(name="languages")
	private String languages;
	
	@Column(name="messages")
	private String messages;
	
	public Project(){
		
	}
	
	public Project(String admin, String projectName, String city, String country, 
			String description, String tools, String languages){
		this.admin = admin;
		this.projectName = projectName;
		this.city = city;
		this.country = country;
		this.description = description;
		this.tools = tools;
		this.languages = languages;
		messages = "";
	}
	
	public void addMessages(String message){
		messages += message;
	}
	
	public int getId(){
		return id;
	}
	
	public String getAdmin(){
		return admin;
	}
	
	public String getName(){
		return projectName;
	}

	public String getProjectName() {
		return projectName;
	}

	public String getCity() {
		return city;
	}

	public String getCountry() {
		return country;
	}

	public String getDescription() {
		return description;
	}

	public String getTools() {
		return tools;
	}

	public String getLanguages() {
		return languages;
	}

	public String getMessages() {
		return messages;
	}
	
	
}
