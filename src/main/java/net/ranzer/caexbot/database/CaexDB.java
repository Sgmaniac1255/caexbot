package net.ranzer.caexbot.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import net.ranzer.caexbot.config.CaexConfiguration;
import net.ranzer.caexbot.util.Logging;


public class CaexDB {

	private static Connection connection;
	
	public static Connection getConnection(){
		CaexConfiguration config = CaexConfiguration.getInstance();
		try {
			if (connection == null || connection.isClosed()){
				String DBMS = config.getDatabaseManagementSystem();
				String host = config.getDatabaseHostname();
				Integer port = config.getDatabasePort();
				String user = config.getDatabaseUsername();
				String pw = config.getDatabasePassword();
				
				String DB;
				if (config.isDebug()) {
					DB = config.getTestDatabaseName();
				} else {
					DB = config.getDatabaseName();					
				}
				 
				connection = DriverManager.getConnection(String.format("jdbc:%s://%s:%d/%s?useSSL=false",DBMS,host,port,DB), user, pw);
			}
			
			return connection;
		} catch (SQLException e) {
			Logging.error(e.getMessage());
			Logging.log(e);
			return null;
		}
	}
}
