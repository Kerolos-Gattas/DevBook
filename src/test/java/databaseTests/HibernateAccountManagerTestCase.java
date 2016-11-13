package databaseTests;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Test;

import common.LoggerManager;
import models.Account;
import persistence.HibernateDatabaseAccountManager;
import persistence.HibernateUtil;

public class HibernateAccountManagerTestCase {

	private static final String DROP_COUNTER_TABLE_SQL = "drop table counter;";
	private static String CREATE_COUNTER_TABLE_SQL = "create table COUNTER(COUNTER_PRIMARY_KEY char(36) primary key not null, "
			+ "NAME varchar(16), COUNTER integer, RESET_TIME timestamp);";
	@Test
	public void testCreatDropTable() {
		LoggerManager.current().info(new Account(), "test1", "test2", null);
		
		assertTrue(new File("/Users/Kento/desktop/mylog.log").isFile());
		
		HibernateUtil.getCurrentSession();
		
		//assertTrue(HibernateUtil.executeSQLQuery(DROP_COUNTER_TABLE_SQL));
		//assertTrue(HibernateUtil.executeSQLQuery(CREATE_COUNTER_TABLE_SQL));
		
		assertTrue(HibernateDatabaseAccountManager.getDefault().setupTable());
		
		//assertTrue(HibernateDatabaseAccountManager.getDefault().getAllCounters().isEmpty());
	}
}
