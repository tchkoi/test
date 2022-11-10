package ge.ufc.webservices.dao;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseManager {

    private static final String PROJECT_TOMCAT_DS = "java:comp/env/jdbc/ServiceA";

    public static Connection getConnection() {
        try {
            DataSource ds = getDataSource();
            return ds.getConnection();
        } catch (NamingException | SQLException | RuntimeException e) {
            throw new InternalError();
        }
    }

    private static DataSource getDataSource() throws NamingException {
        Context context = new InitialContext();
        return (DataSource) context.lookup(DatabaseManager.PROJECT_TOMCAT_DS);
    }

    public static void closeConnection(Connection connection) {
        try {
            if (connection != null)
                connection.close();
        } catch (SQLException | RuntimeException e) {
            throw new InternalError();
        }
    }
}

