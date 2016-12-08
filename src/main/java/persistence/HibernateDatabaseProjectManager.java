package persistence;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.Query;
import org.hibernate.Session;

import common.LoggerManager;
import common.Messages;
import models.Account;
import models.Project;

public class HibernateDatabaseProjectManager extends AbstractHibernateDatabaseManager{
	
	private static String PROJECT_TABLE_NAME = "PROJECT";
	private static String PROJECT_CLASS_NAME = "Project";
	
	private static final String DROP_TABLE_SQL = "drop table "
			+ PROJECT_TABLE_NAME + ";";
	private static String CREATE_TABLE_SQL = "create table IF NOT EXISTS PROJECT(id int not null AUTO_INCREMENT primary key, "
			+ "admin varchar(100) not null, " + "name varchar(50) not null, " + "CITY varchar(50) not null, " 
			+ "COUNTRY varchar(50) not null, " + "description text not null, "+ "tools text, " 
			+ "languages text, " + "messages text);";
	
	private static String SELECT_PROJECTS_NEAR_USER = "from "
			+ PROJECT_CLASS_NAME + " as project where project.city = :city and project.country = :country";

	private static String SELECT_PROJECTS_BY_ID = "from "
			+ PROJECT_CLASS_NAME + " as project where project.id = :id";
		
	private static String SELECT_PROJECTS_BY_ADMIN_AND_TITLE = "from "
			+ PROJECT_CLASS_NAME + " as project where project.admin = :admin and project.projectName = :name";
	
	private static HibernateDatabaseProjectManager manager;
	private static HibernateDatabaseAccountManager accountManager;

	
	HibernateDatabaseProjectManager() {
		super();
		accountManager = HibernateDatabaseAccountManager.getDefault();
	}
	
	/**
	 * Returns default instance.
	 * 
	 * @return
	 */
	public synchronized static HibernateDatabaseProjectManager getDefault() {
		
		if (manager == null) {
			manager = new HibernateDatabaseProjectManager();
		}
		return manager;
	}
	
	public synchronized List<Project> selectProjectsNearUser(String userName) {
		
		Session session = null;
		List<Project> error = null;
		try {
			Account account = accountManager.getAccountByUserName(userName);

			session = HibernateUtil.getCurrentSession();
			Query query = session.createQuery(SELECT_PROJECTS_NEAR_USER);
			query.setParameter("city", account.getCity());
			query.setParameter("country", account.getCountry());
			List<Project> projects = query.list();
			return projects;
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
	
	public synchronized Project selectProjectsbyAdminandTitle(String admin, String name){
		Session session = null;
		Project error = null;
		

		try {
			session = HibernateUtil.getCurrentSession();
			Query query = session.createQuery(SELECT_PROJECTS_BY_ADMIN_AND_TITLE);
			query.setParameter("admin", admin);
			query.setParameter("name", name);
			System.err.println(query.getQueryString());

			Project project = (Project) query.uniqueResult();
			return project;
		} catch (ObjectNotFoundException exception) {
			System.err.println(exception.getMessage());
			return error;
		} catch (HibernateException exception) {
			System.err.println(exception.getMessage());
			return error;
		} catch (RuntimeException exception) {
			System.err.println(exception.getMessage());
			return error;
		} finally {
			closeSession();
		}
	}
	
	public synchronized Project selectProjectsbyId(int id){
		Session session = null;
		Project error = null;
		
		try {
			session = HibernateUtil.getCurrentSession();
			Query query = session.createQuery(SELECT_PROJECTS_BY_ID);
			query.setParameter("id", id);
			Project project = (Project) query.uniqueResult();
			return project;
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
	
	public boolean setupTable() {
		//HibernateUtil.executeSQLQuery(DROP_TABLE_SQL);
		return HibernateUtil.executeSQLQuery(CREATE_TABLE_SQL);
	}

	@Override
	public String getClassName() {
		// TODO Auto-generated method stub
		return PROJECT_CLASS_NAME;
	}

	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		return PROJECT_TABLE_NAME;
	}
}
