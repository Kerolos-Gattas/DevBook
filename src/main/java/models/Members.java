package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="MEMBER")
public class Members {

	@Id
	@Column(name="id")
	private int id;
	
	@Column(name="userName")
	private String userName;
	
	public Members(){
		
	}
	
	public Members(int id, String userName){
		this.id = id;
		this.userName = userName;
	}

	public int getId() {
		return id;
	}

	public String getUserName() {
		return userName;
	}
}
