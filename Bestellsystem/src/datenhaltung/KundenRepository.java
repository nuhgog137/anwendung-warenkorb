package datenhaltung;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;

import modell.Kunde;

// Diese Klasse übernimmt genau eine Aufgabe: den Zugriff auf die Tabelle KUNDE.
// Passend zum Datenmodell entspricht das der Klasse KundenRepository.
public class KundenRepository
{
    private Connection con;

    public KundenRepository()
    {
        try
        {
            Class.forName("org.mariadb.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mariadb://localhost/shop", "root", "");
        }
        catch (Exception e)
        {
            System.out.println("Fehler beim Verbinden mit der Datenbank: " + e.getMessage());
        }
    }

    // Sucht einen Kunden anhand seiner Email. Gibt null zurück, wenn keiner gefunden wurde.
    public Kunde findByEmail(String email)
    {
        try
        {
            String sql = "SELECT * FROM kunde WHERE email = '" + email + "'";
            Statement stat = con.createStatement();
            ResultSet res = stat.executeQuery(sql);
            if (res.next())
            {
                return new Kunde(
                        res.getInt("kundenid"),
                        res.getString("name"),
                        res.getString("email"),
                        res.getString("telefon"),
                        res.getString("adresse"));
            }
        }
        catch (SQLException e)
        {
            System.out.println("Fehler bei der Abfrage: " + e.getMessage());
        }
        return null;
    }

    // Ermittelt die nächste freie KundenID (höchste vorhandene + 1).
    public int naechsteKundenId()
    {
        try
        {
            String sql = "SELECT MAX(kundenid) AS hoechste FROM kunde";
            Statement stat = con.createStatement();
            ResultSet res = stat.executeQuery(sql);
            if (res.next())
            {
                return res.getInt("hoechste") + 1;
            }
        }
        catch (SQLException e)
        {
            System.out.println("Fehler bei der Abfrage: " + e.getMessage());
        }
        return 1;
    }

    // Speichert einen neuen Kunden in der Datenbank.
    public void speichereKunde(Kunde kunde)
    {
        try
        {
            String sql = "INSERT INTO kunde(kundenid, name, email, telefon, adresse) VALUES ("
                    + kunde.getKundenId() + ", '" + kunde.getName() + "', '" + kunde.getEmail() + "', '"
                    + kunde.getTelefon() + "', '" + kunde.getAdresse() + "')";
            Statement stat = con.createStatement();
            stat.executeUpdate(sql);
        }
        catch (SQLException e)
        {
            System.out.println("Fehler beim Speichern des Kunden: " + e.getMessage());
        }
    }
}
