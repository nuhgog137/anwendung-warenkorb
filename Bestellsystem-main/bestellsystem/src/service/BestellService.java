package service;

import datenhaltung.ArtikelRepository;
import datenhaltung.BestellungRepository;
import datenhaltung.KundeRepository;
import model.Artikel;
import model.BestellPosition;
import model.Bestellung;
import model.Kunde;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Geschaeftslogik der Anwendung (Schicht "Service" in der Architektur).
 * Greift ausschliesslich ueber die DAOs auf die Datenbank zu
 * und wird ausschliesslich von der RMI-Schicht aufgerufen.
 */
public class BestellService {

    private KundeRepository kundeDAO = new KundeRepository();
    private ArtikelRepository artikelDAO = new ArtikelRepository();
    private BestellungRepository bestellungDAO = new BestellungRepository();

    /**
     * Prueft, ob ein Kunde mit dieser E-Mail bereits registriert ist.
     */
    public boolean istKundeRegistriert(String email) throws SQLException {
        return kundeDAO.findeKundePerEmail(email) != null;
    }

    /**
     * Liefert den Kunden zu einer E-Mail (oder null, wenn nicht vorhanden).
     */
    public Kunde findeKunde(String email) throws SQLException {
        return kundeDAO.findeKundePerEmail(email);
    }

    /**
     * Registriert einen neuen Kunden.
     */
    public Kunde registriereKunde(Kunde kunde) throws SQLException {
        kundeDAO.legeKundeAn(kunde);
        return kunde;
    }

    /**
     * Liefert alle Artikel fuer die Anzeige beim Kunden.
     */
    public List<Artikel> holeAlleArtikel() throws SQLException {
        return artikelDAO.findeAlleArtikel();
    }

    /**
     * Loest die Bestellung aus. Ablauf laut Aufgabenstellung:
     * 1. Verfuegbarkeit aller Artikel pruefen
     * 2. Gesamtpreis berechnen
     * 3. Versandtermin berechnen (Bestelldatum + laengste Lieferzeit)
     * 4. Bestellung mit Positionen speichern (BestellNr vergibt die Datenbank)
     *
     * Der Warenkorb ist eine Map: ArtikelNr -> Menge.
     */
    public Bestellung loeseBestellungAus(int kundenID, Map<Integer, Integer> warenkorb)
            throws SQLException, ArtikelNichtVerfuegbarException {

        double gesamtpreis = 0;
        int laengsteLieferzeit = 0;
        List<BestellPosition> positionen = new ArrayList<>();

        // Schritt 1: Warenkorb durchgehen - Verfuegbarkeit pruefen,
        //            Gesamtpreis und laengste Lieferzeit berechnen
        for (Map.Entry<Integer, Integer> eintrag : warenkorb.entrySet()) {
            int artikelNr = eintrag.getKey();
            int menge = eintrag.getValue();

            Artikel artikel = artikelDAO.findeArtikelPerNr(artikelNr);

            // Verfuegbarkeit pruefen -> sonst eigene checked Exception
            if (artikel == null || artikel.getLagerbestand() < menge) {
                throw new ArtikelNichtVerfuegbarException(
                        "Artikel Nr. " + artikelNr + " ist nicht in ausreichender Menge verfügbar!");
            }

            gesamtpreis = gesamtpreis + (artikel.getPreis() * menge);

            // Der Artikel mit der laengsten Lieferzeit bestimmt den Versandtermin
            if (artikel.getLieferzeitTage() > laengsteLieferzeit) {
                laengsteLieferzeit = artikel.getLieferzeitTage();
            }

            // Position merken (BestellNr wird beim Speichern von der Datenbank vergeben)
            positionen.add(new BestellPosition(0, artikelNr, menge, artikel.getPreis()));
        }

        // Schritt 1b: Erst wenn alle Artikel verfuegbar sind, den Lagerbestand reduzieren
        for (Map.Entry<Integer, Integer> eintrag : warenkorb.entrySet()) {
            artikelDAO.reduziereLagerbestand(eintrag.getKey(), eintrag.getValue());
        }

        // Schritt 2: Bestellung mit den berechneten Werten anlegen und speichern
        LocalDate versandtermin = LocalDate.now().plusDays(laengsteLieferzeit);
        Bestellung bestellung = new Bestellung(0, kundenID, LocalDate.now(),
                gesamtpreis, versandtermin, "Offen");
        bestellung.getPositionen().addAll(positionen);

        bestellungDAO.speichereBestellung(bestellung);

        return bestellung;
    }

    /**
     * Storniert eine Bestellung. Erlaubt ist das nur bis zum Versandtermin.
     */
    public boolean storniereBestellung(Bestellung bestellung) throws SQLException {
        if (LocalDate.now().isAfter(bestellung.getVersandtermin())) {
            return false; // Frist abgelaufen, Stornierung nicht mehr moeglich
        }
        bestellungDAO.aendereStatus(bestellung.getBestellNr(), "Storniert");
        bestellung.setStatus("Storniert");
        return true;
    }

    /**
     * Markiert eine Bestellung als versendet.
     */
    public void versendeBestellung(Bestellung bestellung) throws SQLException {
        bestellungDAO.aendereStatus(bestellung.getBestellNr(), "Versendet");
        bestellung.setStatus("Versendet");
    }
}
