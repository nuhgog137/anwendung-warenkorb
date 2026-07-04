package datenhaltung;

import database.DatenbankVerbindung;
import model.Kunde;

import java.sql.*;
import java.time.LocalDate;

/**
 * Datenbankzugriffe fuer die Tabelle Kunde.
 * Verwendet PreparedStatement (siehe Vorlesung JDBC), um SQL-Injection zu vermeiden.
 */
public class KundeRepository {

    /**
     * Sucht einen Kunden anhand seiner E-Mail-Adresse.
     * Gibt null zurueck, wenn kein Kunde gefunden wurde.
     */
    public Kunde findeKundePerEmail(String email) throws SQLException {
        String sql = "SELECT * FROM Kunde WHERE Email = ?";

        try (Connection verbindung = DatenbankVerbindung.getVerbindung();
             PreparedStatement anweisung = verbindung.prepareStatement(sql)) {

            anweisung.setString(1, email);

            try (ResultSet ergebnis = anweisung.executeQuery()) {
                if (ergebnis.next()) {
                    return new Kunde(
                            ergebnis.getInt("KundenID"),
                            ergebnis.getString("Name"),
                            ergebnis.getString("Vorname"),
                            ergebnis.getString("Email"),
                            ergebnis.getString("Telefon"),
                            ergebnis.getString("Strasse"),
                            ergebnis.getString("PLZ"),
                            ergebnis.getString("Ort"),
                            ergebnis.getDate("Registrierdatum").toLocalDate()
                    );
                }
                return null;
            }
        }
    }

    /**
     * Legt einen neuen Kunden in der Datenbank an.
     * Die KundenID wird von der Datenbank vergeben (AUTO_INCREMENT)
     * und anschliessend im Kunde-Objekt gesetzt.
     */
    public void legeKundeAn(Kunde kunde) throws SQLException {
        String sql = "INSERT INTO Kunde (Name, Vorname, Email, Telefon, Strasse, PLZ, Ort, Registrierdatum) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection verbindung = DatenbankVerbindung.getVerbindung();
             PreparedStatement anweisung = verbindung.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            anweisung.setString(1, kunde.getName());
            anweisung.setString(2, kunde.getVorname());
            anweisung.setString(3, kunde.getEmail());
            anweisung.setString(4, kunde.getTelefon());
            anweisung.setString(5, kunde.getStrasse());
            anweisung.setString(6, kunde.getPlz());
            anweisung.setString(7, kunde.getOrt());
            anweisung.setDate(8, Date.valueOf(LocalDate.now()));

            anweisung.executeUpdate();

            // Von der Datenbank erzeugte KundenID auslesen
            try (ResultSet schluessel = anweisung.getGeneratedKeys()) {
                if (schluessel.next()) {
                    kunde.setKundenID(schluessel.getInt(1));
                }
            }
        }
    }
}
