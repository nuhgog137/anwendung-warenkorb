package datenhaltung;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;

// Diese Klasse übernimmt genau eine Aufgabe: den Zugriff auf die Tabelle BESTELLUNG
// (und die zugehörige Warenkorb-Tabelle BESTELLUNG_ARTIKEL).
public class BestellRepository
{
    private Connection con;

    public BestellRepository()
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

    // Ermittelt die nächste freie Bestellnummer (höchste vorhandene + 1).
    public int naechsteBestellNr()
    {
        try
        {
            String sql = "SELECT MAX(bestellnr) AS hoechste FROM bestellung";
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

    // Speichert eine neue Bestellung mit dem Bestelldatum von heute.
    public void speichereBestellung(int bestellNr, int kundenId, double gesamtpreis, String versandtermin, String status)
    {
        try
        {
            String sql = "INSERT INTO bestellung(bestellnr, kundenid, bestelldatum, gesamtpreis, versandtermin, status) "
                    + "VALUES (" + bestellNr + ", " + kundenId + ", CURDATE(), " + gesamtpreis
                    + ", '" + versandtermin + "', '" + status + "')";
            Statement stat = con.createStatement();
            stat.executeUpdate(sql);
        }
        catch (SQLException e)
        {
            System.out.println("Fehler beim Speichern der Bestellung: " + e.getMessage());
        }
    }

    // Speichert eine einzelne Warenkorb-Zeile zu einer Bestellung.
    public void speicherePosition(int bestellNr, int artikelNr, int menge)
    {
        try
        {
            String sql = "INSERT INTO bestellung_artikel(bestellnr, artikelnr, menge) VALUES ("
                    + bestellNr + ", " + artikelNr + ", " + menge + ")";
            Statement stat = con.createStatement();
            stat.executeUpdate(sql);
        }
        catch (SQLException e)
        {
            System.out.println("Fehler beim Speichern der Position: " + e.getMessage());
        }
    }

    // Ändert den Status einer Bestellung (z. B. "storniert" oder "verschickt").
    public void aktualisiereStatus(int bestellNr, String status)
    {
        try
        {
            String sql = "UPDATE bestellung SET status = '" + status + "' WHERE bestellnr = " + bestellNr;
            Statement stat = con.createStatement();
            stat.executeUpdate(sql);
        }
        catch (SQLException e)
        {
            System.out.println("Fehler beim Ändern des Status: " + e.getMessage());
        }
    }
}
