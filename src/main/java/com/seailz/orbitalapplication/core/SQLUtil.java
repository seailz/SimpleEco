package com.seailz.orbitalapplication.core;

import com.seailz.orbitalapplication.OrbitalApplication;
import org.bukkit.OfflinePlayer;

import java.sql.*;

public class SQLUtil {

    static final String SET_PREFERENCE = "insert into BALANCE (player, balance) values (?, ?)";
    static final String REMOVE_PLAYER = "DELETE FROM BALANCE WHERE player=";
    static final String CHECK_VALID_QUERY = "SELECT player, balance";

    public static String connection = "jdbc:mysql://" + OrbitalApplication.getInstance().getConfig().getString("mysql.ip") + ":" + OrbitalApplication.getInstance().getConfig().getString("mysql.port") +  "/"
            + OrbitalApplication.getInstance().getConfig().getString("mysql.db-name");

    public static boolean hasRecord(String id) throws SQLException {
        Connection conn = DriverManager.getConnection(connection, OrbitalApplication.getInstance().getConfig().getString("mysql.username"), OrbitalApplication.getInstance().getConfig().getString("mysql.password"));
        Statement statement = conn.createStatement();
        String stmt = "SELECT * FROM BALANCE WHERE player='" + id + "'";
        ResultSet rs = statement.executeQuery(stmt);

        return rs.next();
    }

    public static void setBalance(String balance, OfflinePlayer player) throws SQLException {

        Connection conn = DriverManager.getConnection(connection, OrbitalApplication.getInstance().getConfig().getString("mysql.username"), OrbitalApplication.getInstance().getConfig().getString("mysql.password"));

        DatabaseMetaData dbm = conn.getMetaData();
        // check if "employee" table is there
        ResultSet tables = dbm.getTables(null, null, "BALANCE", null);
        if (!tables.next()) {
            Statement createTable = conn.createStatement();
            String create = "CREATE TABLE BALANCE " +
                    "(player VARCHAR(255), " +
                    " balance VARCHAR(255), " +
                    " PRIMARY KEY ( player ))";

            createTable.executeUpdate(create);
        }

        // Extract data from result set

        if (hasRecord(player.getUniqueId().toString())) {
            PreparedStatement removePlayer = conn.prepareStatement(REMOVE_PLAYER + "'" + player.getUniqueId().toString() + "';");
            removePlayer.execute();
        }

        PreparedStatement preparedStmt = conn.prepareStatement(SET_PREFERENCE);
        preparedStmt.setString(1, player.getUniqueId().toString());
        preparedStmt.setString(2, balance);

        preparedStmt.execute();
    }

    public static String getBalance(OfflinePlayer player) throws SQLException {
        Connection conn = DriverManager.getConnection(connection, OrbitalApplication.getInstance().getConfig().getString("mysql.username"), OrbitalApplication.getInstance().getConfig().getString("mysql.password"));
        Statement preparedStmt = conn.createStatement();

        ResultSet set = preparedStmt.executeQuery("SELECT * FROM BALANCE");
        String preference = null;
        while (set.next()) {
            if (set.getString("player").equals(player.getUniqueId().toString())) {
                preference = set.getString("balance");
            }
        }
        preparedStmt.close();
        return preference;
    }


}
