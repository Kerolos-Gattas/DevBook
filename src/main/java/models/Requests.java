package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="REQUEST")
public class Requests {

	@Id
	@Column(name="id")
	private int id;
	
	@Column(name="admin")
	private String admin;
	
	@Column(name="userName")
	private String userName;
	
	public Requests(){
		
	}
	
	public Requests(int id, String admin, String userName){
		this.id = id;
		this.admin = admin;
		this.userName = userName;
	}

	public int getId() {
		return id;
	}

	public String getAdmin() {
		return admin;
	}

	public String getUserName() {
		return userName;
	}
	
}
