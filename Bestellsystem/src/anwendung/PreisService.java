package anwendung;

import java.util.List;

import datenhaltung.ArtikelRepository;
import modell.Artikel;
import modell.Position;

// Kümmert sich nur um eine Sache: die Berechnung des Gesamtpreises.
public class PreisService
{
    private ArtikelRepository artikelRepository = new ArtikelRepository();

    // Rechnet für jede Position den Preisanteil (Einzelpreis * Menge) zusammen.
    public double berechneGesamtpreis(List<Position> warenkorb)
    {
        double gesamtpreis = 0;
        for (Position p : warenkorb)
        {
            Artikel a = artikelRepository.findeArtikelById(p.getArtikelNr());
            gesamtpreis = gesamtpreis + a.getEinzelpreis() * p.getMenge();
        }
        return gesamtpreis;
    }
}
