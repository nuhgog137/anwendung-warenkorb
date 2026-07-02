package anwendung;

import datenhaltung.KundenRepository;
import modell.Kunde;

// Kümmert sich nur um eine Sache: die Registrierung von Kunden.
// Passend zum Architekturdiagramm entspricht das der Klasse KundenService.
public class KundenService
{
    private KundenRepository kundenRepository = new KundenRepository();

    // Prüft, ob zu einer Email bereits ein Kunde registriert ist.
    public boolean pruefeRegistrierung(String email)
    {
        return kundenRepository.findByEmail(email) != null;
    }

    // Holt einen bereits registrierten Kunden anhand seiner Email.
    public Kunde holeKunde(String email)
    {
        return kundenRepository.findByEmail(email);
    }

    // Legt einen neuen Kunden an und gibt ihn zurück.
    public Kunde legeKundeAn(String name, String email, String telefon, String adresse)
    {
        int neueId = kundenRepository.naechsteKundenId();
        Kunde kunde = new Kunde(neueId, name, email, telefon, adresse);
        kundenRepository.speichereKunde(kunde);
        return kunde;
    }
}
