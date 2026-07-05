package datenhaltung;

import database.DatenbankVerbindung;
import model.BestellPosition;
import model.Bestellung;

import java.sql.*;

/**
 * Datenbankzugriffe fuer die Tabellen Bestellung und BestellPosition.
 * Beide Tabellen gehoeren fachlich zusammen: Eine Bestellung besteht immer
 * aus Bestellung + ihren Positionen, daher werden sie hier gemeinsam gespeichert.
 */
public class BestellungRepository {

    /**
     * Speichert eine Bestellung inklusive aller Positionen.
     * Die BestellNr wird von der Datenbank vergeben (AUTO_INCREMENT)
     * und anschliessend im Bestellung-Objekt gesetzt.
     */
    public void speichereBestellung(Bestellung bestellung) throws SQLException {
        String sqlBestellung = "INSERT INTO Bestellung (KundenID, Bestelldatum, Gesamtpreis, Versandtermin, Status) "
                + "VALUES (?, ?, ?, ?, ?)";
        String sqlPosition = "INSERT INTO BestellPosition (BestellNr, ArtikelNr, Menge, Einzelpreis) "
                + "VALUES (?, ?, ?, ?)";

        try (Connection verbindung = DatenbankVerbindung.getVerbindung()) {

            // 1. Bestellung speichern und BestellNr auslesen
            try (PreparedStatement anweisung = verbindung.prepareStatement(sqlBestellung, Statement.RETURN_GENERATED_KEYS)) {
                anweisung.setInt(1, bestellung.getKundenID());
                anweisung.setDate(2, Date.valueOf(bestellung.getBestelldatum()));
                anweisung.setDouble(3, bestellung.getGesamtpreis());
                anweisung.setDate(4, Date.valueOf(bestellung.getVersandtermin()));
                anweisung.setString(5, bestellung.getStatus());
                anweisung.executeUpdate();

                try (ResultSet schluessel = anweisung.getGeneratedKeys()) {
                    if (schluessel.next()) {
                        bestellung.setBestellNr(schluessel.getInt(1));
                    }
                }
            }

            // 2. Alle Positionen mit der neuen BestellNr speichern
            try (PreparedStatement anweisung = verbindung.prepareStatement(sqlPosition)) {
                for (BestellPosition position : bestellung.getPositionen()) {
                    anweisung.setInt(1, bestellung.getBestellNr());
                    anweisung.setInt(2, position.getArtikelNr());
                    anweisung.setInt(3, position.getMenge());
                    anweisung.setDouble(4, position.getEinzelpreis());
                    anweisung.executeUpdate();
                }
            }
        }
    }

    /**
     * Aendert den Status einer Bestellung (z.B. auf "Storniert" oder "Versendet").
     */
    public void aendereStatus(int bestellNr, String neuerStatus) throws SQLException {
        String sql = "UPDATE Bestellung SET Status = ? WHERE BestellNr = ?";

        try (Connection verbindung = DatenbankVerbindung.getVerbindung();
             PreparedStatement anweisung = verbindung.prepareStatement(sql)) {

            anweisung.setString(1, neuerStatus);
            anweisung.setInt(2, bestellNr);
            anweisung.executeUpdate();
        }
    }
}
