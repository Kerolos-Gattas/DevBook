package persistence;

import org.hibernate.HibernateException;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.Query;
import org.hibernate.Session;

import common.LoggerManager;
import common.Messages;
import models.Account;

//Copied and adjusted from a class task
public class HibernateDatabaseAccountManager extends AbstractHibernateDatabaseManager{

	private static String ACCOUNT_TABLE_NAME = "ACCOUNT";
	private static String ACCOUNT_CLASS_NAME = "Account";

	/*private static String SELECT_ALL_Accounts = "from " + Account_CLASS_NAME
			+ " as counter";
	private static String SELECT_COUNTER_WITH_NAME = "from "
			+ Account_CLASS_NAME + " as counter where counter.name = ?";
	private static String METHOD_GET_OBJECT_WITH_NAME = "getObjectWithName";
	private static String METHOD_INCREMENT_HIT_COUNTER_BY_NAME_BY = "incrementHitCounterByNameBy";
	private static String METHOD_GET_ALL = "getAllCounters";
	private static String RESET_ALL = "resetAllCounters";*/

	private static String METHOD_GET_OBJECT_WITH_NAME = "getObjectWithName";
	private static String SELECT_ACCOUNT_WITH_USERNAME = "from "
			+ ACCOUNT_CLASS_NAME + " as account where account.username = ?";
	
	private static String INCOMMING_COUNTER_NAME = "incomming msgs";
	public static String OUTGOING_COUNTER_NAME = "outgoing msgs";

	private static final String DROP_TABLE_SQL = "drop table "
			+ ACCOUNT_TABLE_NAME + ";";
	private static String CREATE_TABLE_SQL = "create table IF NOT EXISTS ACCOUNT(USERNAME varchar(100) primary key not null, "
			+ "PASSWORD varchar(100) not null, " + "EMAIL varchar(100) not null, " + "LOGGEDIN boolean not null default 0, "
			+ "LASTLOGINTIME varchar(100));";

	private static HibernateDatabaseAccountManager manager;

	HibernateDatabaseAccountManager() {
		super();
	}

	/**
	 * Returns default instance.
	 * 
	 * @return
	 */
	public synchronized static HibernateDatabaseAccountManager getDefault() {
		
		if (manager == null) {
			manager = new HibernateDatabaseAccountManager();
		}
		return manager;
	}

	/**
	 * Returns counter from the database found for a given name.
	 * If not found returns null.
	 * 
	 * @param name
	 * @return
	 */
	public synchronized Account getAccountByUserName(String userName) {
		
		Session session = null;
		Account error = null;
		
		try {
			session = HibernateUtil.getCurrentSession();
			Query query = session.createQuery(SELECT_ACCOUNT_WITH_USERNAME);
			query.setParameter(0, userName);
			Account account = (Account) query.uniqueResult();
			return account;
		} catch (ObjectNotFoundException exception) {
			LoggerManager.current().error(this,
					METHOD_GET_OBJECT_WITH_NAME,
					Messages.OBJECT_NOT_FOUND_FAILED, exception);
			return error;
		} catch (HibernateException exception) {
			LoggerManager.current().error(this,
					METHOD_GET_OBJECT_WITH_NAME, Messages.HIBERNATE_FAILED,
					exception);
			return error;
		} catch (RuntimeException exception) {
			LoggerManager.current().error(this,
					METHOD_GET_OBJECT_WITH_NAME, Messages.GENERIC_FAILED,
					exception);
			return error;
		} finally {
			closeSession();
		}
	}

	/**
	 * Returns all counters from the database.
	 * Upon error returns null.
	 * 
	 * @return
	 */
	/*@SuppressWarnings("unchecked")
	public synchronized List<Counter> getAllCounters() {
		
		Session session = null;
		List<Counter> errorResult = null;

		try {
			session = HibernateUtil.getCurrentSession();
			Query query = session.createQuery(SELECT_ALL_COUNTERS);
			List<Counter> counters = query.list();
			return counters;
		} catch (ObjectNotFoundException exception) {
			LoggerManager.current().error(this, METHOD_GET_ALL,
					Messages.OBJECT_NOT_FOUND_FAILED, exception);
			return errorResult;
		} catch (HibernateException exception) {
			LoggerManager.current().error(this, METHOD_GET_ALL,
					Messages.HIBERNATE_FAILED, exception);
			return errorResult;
		} catch (RuntimeException exception) {
			LoggerManager.current().error(this, METHOD_GET_ALL,
					Messages.GENERIC_FAILED, exception);
			return errorResult;
		} finally {
			closeSession();
		}
	}*/

	/**
	 * Increments counter found for given name by 1. 
	 * 
	 * @param name
	 */
	/*public synchronized void incrementHitCounterByName(String name) {
		
		incrementHitCounterByNameBy(name, 1);
	}*/

	/**
	 * Increments counter found for given name by given count.
	 * 
	 * @param name
	 * @param count
	 */
	/*public synchronized void incrementHitCounterByNameBy(String name, int count) {
		
		Session session = null;
		Transaction transaction = null;

		try {
			session = HibernateUtil.getCurrentSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery(SELECT_COUNTER_WITH_NAME);
			query.setParameter(0, name);
			Counter counter = (Counter) query.uniqueResult();
			if (counter != null) {
				counter.setCounter(counter.getCounter() + count);
				session.update(counter);
				transaction.commit();
			} else {
				LoggerManager.current().error(this,
						METHOD_INCREMENT_HIT_COUNTER_BY_NAME_BY,
						Messages.OBJECT_NOT_FOUND_FAILED + ":" + name, null);
			}
		} catch (ObjectNotFoundException exception) {
			LoggerManager.current().error(this,
					METHOD_INCREMENT_HIT_COUNTER_BY_NAME_BY,
					Messages.OBJECT_NOT_FOUND_FAILED, exception);
		} catch (HibernateException exception) {
			LoggerManager.current().error(this,
					METHOD_INCREMENT_HIT_COUNTER_BY_NAME_BY,
					Messages.HIBERNATE_FAILED, exception);
		} catch (RuntimeException exception) {
			LoggerManager.current().error(this,
					METHOD_INCREMENT_HIT_COUNTER_BY_NAME_BY,
					Messages.GENERIC_FAILED, exception);
		} finally {
			closeSession();
		}
	}*/

	public String getTableName() {
		return ACCOUNT_TABLE_NAME;
	}

	/**
	 * Adds given counter (object) to the database.
	 * Sets counter's reset time to the current time.
	 *  
	 * @return
	 */
	/*public synchronized boolean add(Object object) {
		
		Calendar calendar = Calendar.getInstance();
		Counter counter = (Counter) object;
		counter.setResetTime(new Timestamp(calendar.getTimeInMillis()));
		return super.add(object);
	}*/

	/**
	 * Resets all counters in the database.
	 * 
	 * @return
	 */
	/*@SuppressWarnings("unchecked")
	public synchronized boolean resetAllCounters() {
		
		Session session = null;
		Transaction transaction = null;

		try {
			session = HibernateUtil.getCurrentSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery(SELECT_ALL_COUNTERS);
			List<Counter> counters = query.list();
			for (Iterator<Counter> iterator = counters.iterator(); iterator
					.hasNext();) {
				iterator.next().reset();
			}
			transaction.commit();
			return true;
		} catch (ObjectNotFoundException exception) {
			rollback(transaction);
			LoggerManager.current().error(this, RESET_ALL,
					Messages.OBJECT_NOT_FOUND_FAILED, exception);
			return false;
		} catch (HibernateException exception) {
			rollback(transaction);
			LoggerManager.current().error(this, RESET_ALL,
					Messages.HIBERNATE_FAILED, exception);
			return false;
		} catch (RuntimeException exception) {
			rollback(transaction);
			LoggerManager.current().error(this, RESET_ALL,
					Messages.GENERIC_FAILED, exception);
			return false;
		} finally {
			closeSession();
		}
	}*/

	public boolean setupTable() {
		HibernateUtil.executeSQLQuery(DROP_TABLE_SQL);
		return HibernateUtil.executeSQLQuery(CREATE_TABLE_SQL);
	}

	public static String getIncommingCounterName() {
		return INCOMMING_COUNTER_NAME;
	}

	public static String getOutgoingCounterName() {
		return OUTGOING_COUNTER_NAME;
	}

	public String getClassName() {
		return ACCOUNT_CLASS_NAME;
	}
}
