package Simulation.server.DestinationAirp;



import java.net.SocketTimeoutException;

import java.net.SocketTimeoutException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import Simulation.Parameters;
import Simulation.interfaces.*;
import java.net.SocketTimeoutException;


/**
 * Destination Server
 */
public class DestAirp_server {

    public static boolean waitConnection;

    /**
     * doesn't have args
     * @param args
     */
    public static void main(String[] args){
        
       
      /* get location of the registry service */
      String rmiRegHostName = Parameters.REGISTRY_HOSTNAME;
      int rmiRegPortNumb = Parameters.REGISTRY_PORT;
      
      String nameEntryBase = Parameters.REGISTRY_NAME_ENTRY;
      String nameEntryObject = Parameters.DESTINATION_AIRPORT_NAME_ENTRY;
      
      Registry registry = null;
      Register registerInt = null;
      
      /* create and install the security manager */
      if (System.getSecurityManager () == null){
          
          System.setSecurityManager (new SecurityManager ());
      }
      
      
      try
      { registry = LocateRegistry.getRegistry (rmiRegHostName, rmiRegPortNumb);
      }
      catch (RemoteException e)
      { System.out.printf("RMI registry locate exception: " + e.getMessage ());
        e.printStackTrace ();
        System.exit (1);
      }    
      
      /* Localize the logger in the RMI server by its name */
      //TODO
      
      
      /* Initialize the shared region */
      DestAirport destAirp = new DestAirport();
      interfaceDestAirp dest_stub = null;
      
      try
      { dest_stub = (interfaceDestAirp) UnicastRemoteObject.exportObject (destAirp, Parameters.DESTINATION_AIRPORT_PORT);
      }
      catch (RemoteException e)
      { System.out.printf("DestAirp stub generation exception: " + e.getMessage ());
        e.printStackTrace ();
        System.exit (1);
      } 
      
      
      /* register it with the general registry service */
      try
      { registerInt = (Register) registry.lookup (nameEntryBase);
      }
      catch (RemoteException e)
      { System.out.printf ("Register lookup exception: " + e.getMessage ());
      e.printStackTrace ();
      System.exit (1);
      }
      catch (NotBoundException e)
      { System.out.printf ("Register not bound exception: " + e.getMessage ());
      e.printStackTrace ();
      System.exit (1);
      }
      
      try
      { registerInt.bind (nameEntryObject, dest_stub);
      }
      catch (RemoteException e)
      { System.out.printf ("DestAirp registration exception: " + e.getMessage ());
        e.printStackTrace ();
        System.exit (1);
      }
      catch (AlreadyBoundException e)
      { System.out.printf ("DestAirp already bound exception: " + e.getMessage ());
        e.printStackTrace ();
        System.exit (1);
      }
      
      System.out.printf ("DestAirp object was registered!");
        
        
        
        

       
    }
}