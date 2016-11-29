package models;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="ACCOUNT")
public class Account {

private static long ZERO = 0;
	
	@Id
	@Column(name="USERNAME")
	private String username;
	
	@Column(name="PASSWORD")
	private String password;
	
	@Column(name="EMAIL")
	private String email;
	
	@Column(name="LOGGEDIN")
	private int loggedIn;
	
	@Column(name="LASTLOGINTIME")
	private String lastLoginTime;
	
	@Column(name="BIO")
	private String bio;
	
	@Column(name="CITY")
	private String city;
	
	@Column(name="COUNTRY")
	private String country;
	
	public Account(){
		
	}
	
	public Account (String username, String password, String email, String city, String country){
		this.username = username;
		this.password = password;
		this.email = email;
		this.city = city;
		this.country = country;
		loggedIn = 0;
	}
	
	/*public Counter copy(Counter counter){
		Counter newCounter = new Counter(getName(),getCounter() );
		newCounter.setCounterPrimaryKey(counter.getCounterPrimaryKey());
		newCounter.setResetTime(getResetTime());
		return newCounter;
	}

	public void setResetTime(Timestamp resetTime) {
		this.resetTime = resetTime;
	}

	public Timestamp getResetTime() {
		return resetTime;
	}

	public void reset() {
		Calendar calendar = Calendar.getInstance();
		setCounter(0);
		setResetTime(new Timestamp(calendar.getTimeInMillis()));	
	}*/
	
	public String toString() {
		return username + ":" + password;
	}

	public int getLoggedIn() {
		return loggedIn;
	}
	
	public String getPassword(){
		return password;
	}
	
	public void setLoggedIn(int value){
		loggedIn = value;
	}
	
	public void setLastLoginTime(String time){
		lastLoginTime = time;
	}
	
	public String getLastLoginTime(){
		return lastLoginTime;
	}
}
