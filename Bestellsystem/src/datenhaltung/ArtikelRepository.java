package datenhaltung;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import modell.Artikel;

// Diese Klasse übernimmt genau eine Aufgabe: den Zugriff auf die Tabelle ARTIKEL.
public class ArtikelRepository
{
    private Connection con;

    public ArtikelRepository()
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

    // Sucht einen einzelnen Artikel anhand seiner Nummer.
    public Artikel findeArtikelById(int artikelNr)
    {
        try
        {
            String sql = "SELECT * FROM artikel WHERE artikelnr = " + artikelNr;
            Statement stat = con.createStatement();
            ResultSet res = stat.executeQuery(sql);
            if (res.next())
            {
                return new Artikel(
                        res.getInt("artikelnr"),
                        res.getString("bezeichnung"),
                        res.getDouble("einzelpreis"),
                        res.getInt("lagerbestand"),
                        res.getInt("lieferzeit"));
            }
        }
        catch (SQLException e)
        {
            System.out.println("Fehler bei der Abfrage: " + e.getMessage());
        }
        return null;
    }

    // Lädt alle Artikel, zum Beispiel für die Anzeige im Client.
    public List<Artikel> ladeAlleArtikel()
    {
        List<Artikel> liste = new ArrayList<Artikel>();
        try
        {
            String sql = "SELECT * FROM artikel";
            Statement stat = con.createStatement();
            ResultSet res = stat.executeQuery(sql);
            while (res.next())
            {
                liste.add(new Artikel(
                        res.getInt("artikelnr"),
                        res.getString("bezeichnung"),
                        res.getDouble("einzelpreis"),
                        res.getInt("lagerbestand"),
                        res.getInt("lieferzeit")));
            }
        }
        catch (SQLException e)
        {
            System.out.println("Fehler bei der Abfrage: " + e.getMessage());
        }
        return liste;
    }

    // Verringert den Lagerbestand eines Artikels um die bestellte Menge.
    // GREATEST sorgt dafür, dass der Bestand nie negativ wird.
    public void aktualisiereBestand(int artikelNr, int menge)
    {
        try
        {
            String sql = "UPDATE artikel SET lagerbestand = GREATEST(lagerbestand - " + menge + ", 0) "
                    + "WHERE artikelnr = " + artikelNr;
            Statement stat = con.createStatement();
            stat.executeUpdate(sql);
        }
        catch (SQLException e)
        {
            System.out.println("Fehler beim Aktualisieren des Bestands: " + e.getMessage());
        }
    }
}
