package Simulation.server.Plane;

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
 * Plane server
 */
public class Plane_server {
    public static boolean waitConnection;

    /**
     * Doesn't have args
     * @param args
     */
    public static void main(String[] args){
        
        /* get location of the registry service */
        String rmiRegHostName = Parameters.REGISTRY_HOSTNAME;
        int rmiRegPortNumb = Parameters.REGISTRY_PORT;
        
        String nameEntryBase = Parameters.REGISTRY_NAME_ENTRY;
        String nameEntryObject = Parameters.PLANE_NAME_ENTRY;
        
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
        Plane plane = new Plane();
        interfacePlane plane_stub = null;
        
        try
        { plane_stub = (interfacePlane) UnicastRemoteObject.exportObject (plane, Parameters.PLANE_PORT);
        }
        catch (RemoteException e)
        { System.out.printf("Plane stub generation exception: " + e.getMessage ());
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
        { registerInt.bind (nameEntryObject, plane_stub);
        }
        catch (RemoteException e)
        { System.out.printf ("Plane registration exception: " + e.getMessage ());
          e.printStackTrace ();
          System.exit (1);
        }
        catch (AlreadyBoundException e)
        { System.out.printf ("Plane already bound exception: " + e.getMessage ());
          e.printStackTrace ();
          System.exit (1);
        }
        System.out.printf ("Plane object was registered!");
        
        
        
        

        

        
    }
}