package system.persistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import utility.log.Log;

public class DBConnection {
	private final static class SingletonHolder {
		private final static DBConnection INSTANCE = new DBConnection ();
	}
	
	private Connection connection;
	
	private DBConnection () {
		connection = null;
	}
	
	public static final Connection getConnection () {	
		if (SingletonHolder.INSTANCE.connection == null) {
			SingletonHolder.INSTANCE.connection = createConnection();
		}
		
		return SingletonHolder.INSTANCE.connection;
	}

	private static final Connection createConnection() {
		Log log = system.core.Context.getSharedInstance().getLog();
		log.printBlock("Connecting Database");
		try {
			Class.forName("org.sqlite.JDBC");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			log.warning("SQLite JDBC-Driver not found - maybe sqlitejdbc missing in ./jar ?");
		}

		Connection con = null;

		try {
			con = DriverManager.getConnection("jdbc:sqlite:TSXBOT.db");
		} catch (SQLException e) {
			log.warning("SQLite Database not found - " +
					"review Config Database_filename int .\\conf\\systemconfig.txt");
		}

		return con;
	}

}
