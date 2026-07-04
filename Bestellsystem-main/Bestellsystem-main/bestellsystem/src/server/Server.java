package server;

import rmi.BestelldienstImpl;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * Startet den RMI-Server:
 * 1. RMI-Registry auf Port 1099 erzeugen
 * 2. Den Bestelldienst unter dem Namen "Bestelldienst" registrieren
 * Danach koennen Clients den Dienst per Naming.lookup finden.
 */
public class Server {

    public static void main(String[] args) throws Exception {
        // Terminal auf UTF-8 umstellen, damit Umlaute (ä, ö, ü) korrekt angezeigt werden
        umlauteAktivieren();

        Registry registry = LocateRegistry.createRegistry(1099);
        registry.rebind("Bestelldienst", new BestelldienstImpl());

        System.out.println("Server gestartet. Bestelldienst auf Port 1099 registriert.");
    }

    /**
     * Stellt die Konsole auf UTF-8 um (Windows-Konsolen nutzen sonst eine andere
     * Codepage, wodurch Umlaute wie ae/oe/ue statt ä/ö/ü angezeigt werden).
     */
    private static void umlauteAktivieren() {
        try {
            if (System.getProperty("os.name").toLowerCase().contains("win")) {
                new ProcessBuilder("cmd", "/c", "chcp 65001 >nul").inheritIO().start().waitFor();
            }
            System.setOut(new PrintStream(System.out, true, StandardCharsets.UTF_8));
            System.setErr(new PrintStream(System.err, true, StandardCharsets.UTF_8));
        } catch (Exception e) {
            // Falls die Konsole nicht umgestellt werden kann, laeuft das Programm trotzdem weiter
        }
    }
}
