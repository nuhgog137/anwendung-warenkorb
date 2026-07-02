package anwendung;

import java.util.List;

import datenhaltung.BestellRepository;
import modell.Position;

// Kümmert sich nur um eine Sache: Bestellungen anlegen und stornieren/verschicken.
public class BestellService
{
    private BestellRepository bestellRepository = new BestellRepository();

    // Legt eine neue Bestellung an und speichert alle Positionen dazu.
    // Preis und Versandtermin werden von außen übergeben, weil sie von
    // PreisService bzw. VersandService berechnet wurden.
    public int erstelleBestellung(int kundenId, List<Position> warenkorb, double gesamtpreis, String versandtermin)
    {
        int bestellNr = bestellRepository.naechsteBestellNr();
        bestellRepository.speichereBestellung(bestellNr, kundenId, gesamtpreis, versandtermin, "offen");

        for (Position p : warenkorb)
        {
            bestellRepository.speicherePosition(bestellNr, p.getArtikelNr(), p.getMenge());
        }

        return bestellNr;
    }

    public void storniereBestellung(int bestellNr)
    {
        bestellRepository.aktualisiereStatus(bestellNr, "storniert");
    }

    public void versendeBestellung(int bestellNr)
    {
        bestellRepository.aktualisiereStatus(bestellNr, "verschickt");
    }
}
