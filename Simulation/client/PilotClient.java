package Simulation.client;

import Simulation.interfaces.*;
import Simulation.Parameters;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;


/**
 * PilotClients is the class that instantiates the pilot thread
 */
public class PilotClient {
    public static void main(String[] args) {
        interfaceDepAirp dep_int = null;
        interfacePlane plane_int = null;
        interfaceLog log_int = null;

        /* get location of the generic registry service */
        String rmiRegHostName = Parameters.REGISTRY_HOSTNAME;
        int rmiRegPortNumb = Parameters.REGISTRY_PORT;


        /* look for the remote object by name in the remote host registry */

        Registry registry = null;
        try {
            registry = LocateRegistry.getRegistry(rmiRegHostName, rmiRegPortNumb);
        } catch (RemoteException e) {
            System.out.printf("RMI registry creation exception: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
        /* Look for the other entities in the registry */

        //logger
        try{
            log_int = (interfaceLog) registry.lookup(Parameters.LOGGER_NAME_ENTRY);
        }catch (NotBoundException ex) {
            System.out.println("Racing Track is not registered: " + ex.getMessage());
            ex.printStackTrace();
            System.exit(1);
        } catch (RemoteException ex) {
            System.out.println("Exception thrown while locating Racing Track: " + ex.getMessage());
            ex.printStackTrace();
            System.exit(1);
        }

        //depart airport
        try {
            dep_int = (interfaceDepAirp) registry.lookup(Parameters.DEPART_AIRPORT_NAME_ENTRY);
        } catch (NotBoundException ex) {
            System.out.println("Racing Track is not registered: " + ex.getMessage());
            ex.printStackTrace();
            System.exit(1);
        } catch (RemoteException ex) {
            System.out.println("Exception thrown while locating Racing Track: " + ex.getMessage());
            ex.printStackTrace();
            System.exit(1);
        }

        //plane
        try {
            plane_int = (interfacePlane) registry.lookup(Parameters.PLANE_NAME_ENTRY);
        } catch (NotBoundException ex) {
            System.out.println("Racing Track is not registered: " + ex.getMessage());
            ex.printStackTrace();
            System.exit(1);
        } catch (RemoteException ex) {
            System.out.println("Exception thrown while locating Racing Track: " + ex.getMessage());
            ex.printStackTrace();
            System.exit(1);
        }

        System.out.println("Starting Pilot Thread");
        Pilot pil = new Pilot(dep_int, plane_int, log_int);
        pil.start();

        try {
            pil.join();
        } catch (InterruptedException ex) {
            System.out.println("Interrupter Exception Error - " + ex.toString());
        }
        System.out.println("Pilot Thread Ended");
    }
}
