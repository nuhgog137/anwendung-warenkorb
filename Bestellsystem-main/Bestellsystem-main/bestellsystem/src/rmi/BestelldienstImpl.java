package rmi;

import model.Artikel;
import model.Bestellung;
import model.Kunde;
import service.ArtikelNichtVerfuegbarException;
import service.BestellService;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Implementierung des Remote-Interface (erweitert UnicastRemoteObject, siehe Vorlesung RMI).
 * Nimmt die Aufrufe des Clients entgegen und reicht sie an den BestellService weiter.
 * Es wird also keine Schicht uebersprungen: Client -> RMI -> Service -> DAO -> Datenbank.
 * Datenbankfehler (SQLException) werden in RemoteException verpackt,
 * damit der Client davon erfaehrt.
 */
public class BestelldienstImpl extends UnicastRemoteObject implements Bestelldienst {

    private BestellService bestellService = new BestellService();

    public BestelldienstImpl() throws RemoteException {
        super();
    }

    @Override
    public boolean istKundeRegistriert(String email) throws RemoteException {
        try {
            return bestellService.istKundeRegistriert(email);
        } catch (SQLException e) {
            throw new RemoteException("Datenbankfehler bei istKundeRegistriert", e);
        }
    }

    @Override
    public Kunde findeKunde(String email) throws RemoteException {
        try {
            return bestellService.findeKunde(email);
        } catch (SQLException e) {
            throw new RemoteException("Datenbankfehler bei findeKunde", e);
        }
    }

    @Override
    public Kunde registriereKunde(Kunde kunde) throws RemoteException {
        try {
            return bestellService.registriereKunde(kunde);
        } catch (SQLException e) {
            throw new RemoteException("Datenbankfehler bei registriereKunde", e);
        }
    }

    @Override
    public List<Artikel> holeAlleArtikel() throws RemoteException {
        try {
            return bestellService.holeAlleArtikel();
        } catch (SQLException e) {
            throw new RemoteException("Datenbankfehler bei holeAlleArtikel", e);
        }
    }

    @Override
    public Bestellung loeseBestellungAus(int kundenID, Map<Integer, Integer> warenkorb)
            throws RemoteException, ArtikelNichtVerfuegbarException {
        try {
            return bestellService.loeseBestellungAus(kundenID, warenkorb);
        } catch (SQLException e) {
            throw new RemoteException("Datenbankfehler bei loeseBestellungAus", e);
        }
    }

    @Override
    public boolean storniereBestellung(Bestellung bestellung) throws RemoteException {
        try {
            return bestellService.storniereBestellung(bestellung);
        } catch (SQLException e) {
            throw new RemoteException("Datenbankfehler bei storniereBestellung", e);
        }
    }

    @Override
    public void versendeBestellung(Bestellung bestellung) throws RemoteException {
        try {
            bestellService.versendeBestellung(bestellung);
        } catch (SQLException e) {
            throw new RemoteException("Datenbankfehler bei versendeBestellung", e);
        }
    }
}
