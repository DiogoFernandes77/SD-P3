package Simulation.registry;

import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import Simulation.interfaces.Register;
import Simulation.Parameters;
/**
 *   Instantiation and registering of a remote object that enables the registration of other remote objects
 *   located in the same or other processing nodes of a parallel machine in the local registry service.
 *
 *   Communication is based in Java RMI.
 */

public class ServerRegisterRemoteObject
{
  /**
   *  Main method.
   *
   *    @param args runtime arguments
   */

   public static void main(String[] args)
   {
    /* get location of the registering service */

     String rmiRegHostName;
     int rmiRegPortNumb;

     rmiRegHostName = Parameters.REGISTRY_HOSTNAME;
     rmiRegPortNumb = Parameters.REGISTRY_PORT;

    /* create and install the security manager */

     if (System.getSecurityManager () == null)
        System.setSecurityManager (new SecurityManager ());
     System.out.println("Security manager was installed!");

    /* instantiate a registration remote object and generate a stub for it */

     RegisterRemoteObject regEngine = new RegisterRemoteObject (rmiRegHostName, rmiRegPortNumb);
     Register regEngineStub = null;
     
     int listeningPort = Parameters.SERVER_REGISTRY_PORT;                                      /* it should be set accordingly in each case */

     try
     { regEngineStub = (Register) UnicastRemoteObject.exportObject (regEngine, listeningPort);
     }
     catch (RemoteException e)
     { System.out.printf("\n RegisterRemoteObject stub generation exception: " + e.getMessage ());
       System.exit (1);
     }
     System.out.println ("Stub was generated!");

    /* register it with the local registry service */

     String nameEntry = Parameters.REGISTRY_NAME_ENTRY;
     Registry registry = null;

     try
     { registry = LocateRegistry.getRegistry (rmiRegHostName, rmiRegPortNumb);
     }
     catch (RemoteException e)
     { System.out.printf("\n RMI registry creation exception: " + e.getMessage ());
       System.exit (1);
     }
     System.out.println ("RMI registry was created!");

     try
     { registry.rebind (nameEntry, regEngineStub);
     }
     catch (RemoteException e)
     { System.out.println ("RegisterRemoteObject remote exception on registration: " + e.getMessage ());
       System.exit (1);
     }
     System.out.println("RegisterRemoteObject object was registered!");
   }
}
