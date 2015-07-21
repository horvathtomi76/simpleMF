package utilities;
import java.sql.SQLException;
import org.springframework.jdbc.core.JdbcTemplate;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

/**
 * Class for managing the input of results into the MySQL database
 * @author Tomas Horvath
 *
 */
public class DatabaseManager {
	
	MysqlDataSource ds;
	JdbcTemplate jt;
	
	public DatabaseManager(String server, String database, String user, String password) throws SQLException {
		ds = new MysqlDataSource();
		ds.setUser(user);
		ds.setPassword(password);
		ds.setServerName(server);
		ds.setDatabaseName(database);
		
		ds.setConnectTimeout(60000);
		ds.setAutoReconnect(true);
		
		jt = new JdbcTemplate(ds);
	}
	
	public void writeResultsIntoDB(String dataset, String technique, String hyperparameters, Float train_error, Float test_error, int time) {
		String query = "INSERT INTO hp_search(dataset,technique,hyperparameters,train_error,test_error,time) " +
				"VALUES ('" + dataset + "','" + technique + "','" + hyperparameters + "',";

		if (Float.isNaN(train_error) || Float.isInfinite(train_error))
			query += "NULL" + ",";  
		else
			query += train_error + ",";

		if (Float.isNaN(test_error) || Float.isInfinite(test_error))
			query += "NULL" + "," + time + ")";  
		else
			query += test_error + "," + time + ")";
		
		jt.update(query);		
	}

}
