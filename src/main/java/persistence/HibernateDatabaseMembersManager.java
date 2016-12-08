package persistence;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.Query;
import org.hibernate.Session;

import models.Members;
import models.Requests;

public class HibernateDatabaseMembersManager extends AbstractHibernateDatabaseManager{

	private static String MEMBERS_TABLE_NAME = "MEMBER";
	private static String MEMBERS_CLASS_NAME = "Members";
	
	private static final String DROP_TABLE_SQL = "drop table "
			+ MEMBERS_TABLE_NAME + ";";
	private static String CREATE_TABLE_SQL = "create table IF NOT EXISTS MEMBER(id int not null, "
			+ "userName varchar(100) not null);";
	
	private static String SELECT_PROJECTS_BY_USERNAME = "from "
			+ MEMBERS_CLASS_NAME + " as members where members.userName = :userName";
	
	private static HibernateDatabaseMembersManager manager;
	
	HibernateDatabaseMembersManager() {
		super();
	}
	
	public List<Members> getProjectsByUsername(String userName){
		Session session = null;
		List<Members> error = null;
		try {

			session = HibernateUtil.getCurrentSession();
			Query query = session.createQuery(SELECT_PROJECTS_BY_USERNAME);
			query.setParameter("userName", userName);
			
			List<Members> requests = query.list();			
			
			return requests;		
		} catch (ObjectNotFoundException exception) {
			return error;
		} catch (HibernateException exception) {
			return error;
		} catch (RuntimeException exception) {
			return error;
		} finally {
			closeSession();
		}
	}
	
	public synchronized static HibernateDatabaseMembersManager getDefault() {
		
		if (manager == null) {
			manager = new HibernateDatabaseMembersManager();
		}
		return manager;
	}
	
	public boolean setupTable() {
		//HibernateUtil.executeSQLQuery(DROP_TABLE_SQL);
		return HibernateUtil.executeSQLQuery(CREATE_TABLE_SQL);
	}

	@Override
	public String getClassName() {
		// TODO Auto-generated method stub
		return MEMBERS_CLASS_NAME;
	}

	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		return MEMBERS_TABLE_NAME;
	}
}
