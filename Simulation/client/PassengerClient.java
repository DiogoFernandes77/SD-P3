package Simulation.client;

import Simulation.interfaces.*;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
/**
 * PassengerClient is the class that instantiates a passenger thread
 */
public class PassengerClient{
    
    public static void main(String[] args)
    {
        interfaceDepAirp dep_int = null;
        interfacePlane plane_int = null;
        interfaceDestAirp dest_int = null;

        /* get location of the generic registry service */
        String rmiRegHostName = Parameters.REGISTRY_HOSTNAME;
        int rmiRegPortNumb = Parameters.REGISTRY_PORT;
        
        
        /* look for the remote object by name in the remote host registry */
        
        Registry registry = null;
        try
        {
            registry = LocateRegistry.getRegistry (rmiRegHostName, rmiRegPortNumb);
        }
        catch (RemoteException e)
        { System.out.printf("RMI registry creation exception: " + e.getMessage ());
          e.printStackTrace ();
          System.exit (1);
        }      
        /* Look for the other entities in the registry */
        
        //depart airport
        try
        {
            dep_int = (interfaceDepAirp) registry.lookup (Parameters.DEPART_AIRPORT_NAME_ENTRY);
        }
        catch (NotBoundException ex) {
            System.out.println("Racing Track is not registered: " + ex.getMessage () );
            ex.printStackTrace ();
            System.exit(1);
        } catch (RemoteException ex) {
            System.out.println("Exception thrown while locating Racing Track: " + ex.getMessage () );
            ex.printStackTrace ();
            System.exit (1);
        }
        
        //plane
        try
        {
            plane_int = (interfacePlane) registry.lookup (Parameters.PLANE_NAME_ENTRY);
        }
        catch (NotBoundException ex) {
            System.out.println("Racing Track is not registered: " + ex.getMessage () );
            ex.printStackTrace ();
            System.exit(1);
        } catch (RemoteException ex) {
            System.out.println("Exception thrown while locating Racing Track: " + ex.getMessage () );
            ex.printStackTrace ();
            System.exit (1);
        }
        
        //destination airport
        try
        {
            dest_int = (interfaceDestAirp) registry.lookup (Parameters.DESTINATION_AIRPORT_NAME_ENTRY);
        }
        catch (NotBoundException ex) {
            System.out.println("Racing Track is not registered: " + ex.getMessage () );
            ex.printStackTrace ();
            System.exit(1);
        } catch (RemoteException ex) {
            System.out.println("Exception thrown while locating Racing Track: " + ex.getMessage () );
            ex.printStackTrace ();
            System.exit (1);
        }
        
        
        Passenger passenger = new Passenger(Integer.parseInt(args[0]),dep_int,plane_int,dest_int);
        System.out.println("Starting Passenger " + args[0] + " Thread");
        passenger.start();
        try
        {
            passenger.join();
        }
        catch (InterruptedException ex)
        {
            System.out.println("Interrupter Exception Error - " + ex.toString());
        }
        System.out.println("Passenger Thread Ended");
    }
}
