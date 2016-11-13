package models;

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
	
	public Account (String username, String password){
		this.username = username;
		this.password = password;
	}
	
	public Account(){
		
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
}
