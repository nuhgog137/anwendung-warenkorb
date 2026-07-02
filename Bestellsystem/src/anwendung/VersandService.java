package anwendung;

import java.time.LocalDate;
import java.util.List;

import datenhaltung.ArtikelRepository;
import modell.Artikel;
import modell.Position;

// Kümmert sich nur um eine Sache: zu prüfen, wann eine Bestellung verschickt werden kann.
public class VersandService
{
    private ArtikelRepository artikelRepository = new ArtikelRepository();

    // Prüft für jeden Artikel im Warenkorb die Verfügbarkeit und liefert den
    // spätesten Versandtermin über alle Positionen zurück.
    public String pruefeVerfuegbarkeit(List<Position> warenkorb)
    {
        int maxTage = 0;
        for (Position p : warenkorb)
        {
            Artikel a = artikelRepository.findeArtikelById(p.getArtikelNr());

            int tage;
            if (a.getLagerbestand() >= p.getMenge())
            {
                tage = 0; // genug auf Lager -> sofort lieferbar
            }
            else
            {
                tage = a.getLieferzeit(); // muss erst beschafft werden
            }

            if (tage > maxTage)
            {
                maxTage = tage;
            }
        }

        LocalDate versand = LocalDate.now().plusDays(maxTage);
        return versand.toString();
    }

    // Verringert den Lagerbestand aller Artikel im Warenkorb nach der Bestellung.
    public void verringereBestaende(List<Position> warenkorb)
    {
        for (Position p : warenkorb)
        {
            artikelRepository.aktualisiereBestand(p.getArtikelNr(), p.getMenge());
        }
    }
}
