package anwendung;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

import modell.Kunde;
import modell.Artikel;
import modell.Position;
import modell.Bestellbestaetigung;
import datenhaltung.ArtikelRepository;

// Diese Klasse ist der einzige Teil des Systems, der über RMI erreichbar ist.
// Sie enthält selbst kaum eigene Logik, sondern reicht jede Anfrage an genau
// den passenden Service weiter (KundenService, PreisService, VersandService,
// BestellService, BenachrichtigungsService). So bleibt jeder einzelne Service
// klein, und diese Klasse ist nur die "Schaltzentrale" dazwischen.
public class BestelldienstImpl extends UnicastRemoteObject implements Bestelldienst
{
    private KundenService kundenService = new KundenService();
    private PreisService preisService = new PreisService();
    private VersandService versandService = new VersandService();
    private BestellService bestellService = new BestellService();
    private BenachrichtigungsService benachrichtigungsService = new BenachrichtigungsService();
    private ArtikelRepository artikelRepository = new ArtikelRepository();

    public BestelldienstImpl() throws RemoteException
    {
        super();
    }

    public boolean pruefeRegistrierung(String email) throws RemoteException
    {
        return kundenService.pruefeRegistrierung(email);
    }

    public Kunde legeKundeAn(String name, String email, String telefon, String adresse) throws RemoteException
    {
        return kundenService.legeKundeAn(name, email, telefon, adresse);
    }

    public Kunde holeKunde(String email) throws RemoteException
    {
        return kundenService.holeKunde(email);
    }

    public List<Artikel> alleArtikel() throws RemoteException
    {
        return artikelRepository.ladeAlleArtikel();
    }

    public Bestellbestaetigung erstelleBestellung(int kundenId, String email, List<Position> warenkorb) throws RemoteException
    {
        // PreisService berechnet den Gesamtpreis.
        double gesamtpreis = preisService.berechneGesamtpreis(warenkorb);

        // VersandService prüft die Verfügbarkeit und liefert den Versandtermin.
        String versandtermin = versandService.pruefeVerfuegbarkeit(warenkorb);

        // BestellService legt die Bestellung mit den berechneten Werten an.
        int bestellNr = bestellService.erstelleBestellung(kundenId, warenkorb, gesamtpreis, versandtermin);

        // Nach dem Anlegen wird der Lagerbestand verringert.
        versandService.verringereBestaende(warenkorb);

        // BenachrichtigungsService informiert den Kunden (hier nur als Konsolenausgabe).
        benachrichtigungsService.sendeBestellbestaetigung(email, bestellNr);

        return new Bestellbestaetigung(bestellNr, gesamtpreis, versandtermin);
    }

    public void storniereBestellung(int bestellNr) throws RemoteException
    {
        bestellService.storniereBestellung(bestellNr);
    }

    public void versendeBestellung(int bestellNr) throws RemoteException
    {
        bestellService.versendeBestellung(bestellNr);
    }
}
