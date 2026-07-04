package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Stellt die JDBC-Verbindung zur MariaDB-Datenbank her.
 * Alle Verbindungsdaten stehen zentral an dieser einen Stelle.
 */
public class DatenbankVerbindung {

    private static final String URL = "jdbc:mariadb://localhost:3306/bestellsystem";
    private static final String BENUTZER = "root";
    private static final String PASSWORT = "";

    public static Connection getVerbindung() throws SQLException {
        return DriverManager.getConnection(URL, BENUTZER, PASSWORT);
    }
}
