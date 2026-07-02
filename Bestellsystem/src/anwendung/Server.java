package anwendung;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

// Startet die RMI-Registry und meldet den Bestelldienst dort an.
public class Server
{
    public static void main(String[] args)
    {
        try
        {
            LocateRegistry.createRegistry(1099);

            Bestelldienst dienst = new BestelldienstImpl();
            Naming.rebind("Bestelldienst", dienst);

            System.out.println("Server läuft und wartet auf Anfragen ...");
        }
        catch (Exception e)
        {
            System.out.println("Fehler beim Starten des Servers: " + e.getMessage());
        }
    }
}
