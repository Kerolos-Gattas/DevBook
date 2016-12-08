package persistence;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.Query;
import org.hibernate.Session;

import encryption.Encryption;
import models.Account;
import models.Project;
import models.Requests;

public class HibernateDatabaseRequestManager extends AbstractHibernateDatabaseManager{

	private static String REQUEST_TABLE_NAME = "REQUEST";
	private static String REQUEST_CLASS_NAME = "Requests";
	
	private static final String DROP_TABLE_SQL = "drop table "
			+ REQUEST_TABLE_NAME + ";";
	private static String CREATE_TABLE_SQL = "create table IF NOT EXISTS REQUEST(id int not null, "
			+ "admin varchar(100) not null, " + "userName varchar(100) not null);";
	
	private static String SELECT_REQUESTS_BY_ADMIN = "from "
			+ REQUEST_CLASS_NAME + " as project where project.admin = :admin";
	
	private static HibernateDatabaseRequestManager manager;
	private static HibernateDatabaseProjectManager projectManager;

	
	HibernateDatabaseRequestManager() {
		super();
		projectManager = HibernateDatabaseProjectManager.getDefault();

	}
	
	public synchronized static HibernateDatabaseRequestManager getDefault() {
		
		if (manager == null) {
			manager = new HibernateDatabaseRequestManager();
		}
		return manager;
	}
	
	public String[] getProjectRequests(String admin){
		
		Session session = null;
		String[] error = null;
		try {

			session = HibernateUtil.getCurrentSession();
			Query query = session.createQuery(SELECT_REQUESTS_BY_ADMIN);
			query.setParameter("admin", admin);
			
			List<Requests> projectsIDS = query.list();
			List<Project> projectNames = new ArrayList();
			for(int i = 0; i < projectsIDS.size(); i++){
				projectNames.add(projectManager.selectProjectsbyId(projectsIDS.get(i).getId()));
			}
			
			String[] projectsStr = new String[projectsIDS.size()];

			for(int i = 0; i < projectsIDS.size(); i++){
				Encryption encrypter = Encryption.getDefaultEncrypter();
				String admintemp = "";
				try {
					admintemp = encrypter.decrypt(projectsIDS.get(i).getUserName());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				projectsStr[i] = admintemp + " has requested to join " + projectNames.get(i).getProjectName() 
						+ " click here for more Info.";
			}
			
			
			return projectsStr;
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
	
	public List<Requests> getRequestsByAdmin(String admin){
		Session session = null;
		List<Requests> error = null;
		try {

			session = HibernateUtil.getCurrentSession();
			Query query = session.createQuery(SELECT_REQUESTS_BY_ADMIN);
			query.setParameter("admin", admin);
			
			List<Requests> requests = query.list();			
			
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
	
	public boolean setupTable() {
		//HibernateUtil.executeSQLQuery(DROP_TABLE_SQL);
		return HibernateUtil.executeSQLQuery(CREATE_TABLE_SQL);
	}

	@Override
	public String getClassName() {
		// TODO Auto-generated method stub
		return REQUEST_CLASS_NAME;
	}

	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		return REQUEST_TABLE_NAME;
	}

}
