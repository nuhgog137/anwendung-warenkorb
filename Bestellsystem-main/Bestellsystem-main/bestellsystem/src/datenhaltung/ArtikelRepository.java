package datenhaltung;

import database.DatenbankVerbindung;
import model.Artikel;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Datenbankzugriffe fuer die Tabelle Artikel.
 */
public class ArtikelRepository {

    /**
     * Laedt alle Artikel aus der Datenbank.
     * Rueckgabe als typisierte Liste (Generics, siehe Vorlesung Collections).
     */
    public List<Artikel> findeAlleArtikel() throws SQLException {
        String sql = "SELECT * FROM Artikel";
        List<Artikel> artikelListe = new ArrayList<>();

        try (Connection verbindung = DatenbankVerbindung.getVerbindung();
             PreparedStatement anweisung = verbindung.prepareStatement(sql);
             ResultSet ergebnis = anweisung.executeQuery()) {

            while (ergebnis.next()) {
                artikelListe.add(new Artikel(
                        ergebnis.getInt("ArtikelNr"),
                        ergebnis.getString("Bezeichnung"),
                        ergebnis.getDouble("Preis"),
                        ergebnis.getInt("Lagerbestand"),
                        ergebnis.getInt("Lieferzeit_Tage")
                ));
            }
        }
        return artikelListe;
    }

    /**
     * Laedt einen einzelnen Artikel anhand seiner Artikelnummer.
     * Gibt null zurueck, wenn der Artikel nicht existiert.
     */
    public Artikel findeArtikelPerNr(int artikelNr) throws SQLException {
        String sql = "SELECT * FROM Artikel WHERE ArtikelNr = ?";

        try (Connection verbindung = DatenbankVerbindung.getVerbindung();
             PreparedStatement anweisung = verbindung.prepareStatement(sql)) {

            anweisung.setInt(1, artikelNr);

            try (ResultSet ergebnis = anweisung.executeQuery()) {
                if (ergebnis.next()) {
                    return new Artikel(
                            ergebnis.getInt("ArtikelNr"),
                            ergebnis.getString("Bezeichnung"),
                            ergebnis.getDouble("Preis"),
                            ergebnis.getInt("Lagerbestand"),
                            ergebnis.getInt("Lieferzeit_Tage")
                    );
                }
                return null;
            }
        }
    }

    /**
     * Reduziert den Lagerbestand eines Artikels um die bestellte Menge.
     */
    public void reduziereLagerbestand(int artikelNr, int menge) throws SQLException {
        String sql = "UPDATE Artikel SET Lagerbestand = Lagerbestand - ? WHERE ArtikelNr = ?";

        try (Connection verbindung = DatenbankVerbindung.getVerbindung();
             PreparedStatement anweisung = verbindung.prepareStatement(sql)) {

            anweisung.setInt(1, menge);
            anweisung.setInt(2, artikelNr);
            anweisung.executeUpdate();
        }
    }
}
