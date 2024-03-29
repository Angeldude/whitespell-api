package main.com.whitespell.peak.logic.sql;

import main.com.whitespell.peak.logic.config.Config;
import main.com.whitespell.peak.logic.logging.Logging;
import org.apache.commons.dbcp2.cpdsadapter.DriverAdapterCPDS;
import org.apache.commons.dbcp2.datasources.SharedPoolDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Pool {
    private static DataSource ds;
    static DriverAdapterCPDS cpds = null;
    static SharedPoolDataSource tds = null;

    static {
        Pool.initializePool();
    }

    public static Connection getConnection() {
        try {
            return ds.getConnection();
        } catch (SQLException e) {
            Logging.log("High", e);
        }
        return null;
    }

    public static void initializePool() {

        if(tds != null ) {
            try {
                tds.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        cpds = new DriverAdapterCPDS();

        try {
            cpds.setDriver("org.gjt.mm.mysql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        cpds.setUrl("jdbc:mysql://"+Config.DB_HOST+":"+Config.DB_PORT+"/"+Config.DB+"?autoreconnect=true");
        cpds.setUser(Config.DB_USER);
        cpds.setPassword(Config.DB_PASS);
        tds = new SharedPoolDataSource();
        tds.setConnectionPoolDataSource(cpds);
        tds.setMaxTotal(250);
        tds.setDefaultMaxWaitMillis(15000);
        tds.setValidationQuery("SELECT 1");
        tds.setDefaultMaxIdle(10);
        tds.setDefaultTestWhileIdle(true);
        tds.setDefaultMinIdle(0);
        ds = tds;
        try {
            ds.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
