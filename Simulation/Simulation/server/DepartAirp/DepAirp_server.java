package Simulation.server.DepartAirp;


import Simulation.server.DepartAirp.DepartAirport;

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

/**
 * DepAirp_server is the class that instantiates the DepAirp server
 */
public class DepAirp_server {

    public static boolean waitConnection;
    public static int nPassenger,boardMax,boardMin;

    /**
     * @param args - nPassenger, boarding min, boarding max
     *  Default 21 8 3 if args.length == 0
     */
    public static void main(String[] args){
        if(args.length == 3){//custom config
            try{
                nPassenger = Integer.parseInt(args[0]);
                boardMin = Integer.parseInt(args[1]);
                boardMax = Integer.parseInt(args[2]);
            }catch(Exception e){
                System.out.print("Args must be numbers \n");
                System.exit(1);
            }
            if(nPassenger == 0){
                System.out.print(" Nº passenger can't be 0 \n");
                System.exit(1);
            }
            if(boardMax < boardMin){
                System.out.print(" Boarding max needs to be higher than boarding min \n");
                System.exit(1);
            }
            System.out.print("Config Ok \n");
        }else if(args.length == 0){//default config
            nPassenger = 21;
            boardMax = 8;
            boardMin = 5;
            System.out.print("Config Ok \n");
        }else{
            System.out.print("Arguments missing/wrong \n");
            System.out.print("N_max_passengers boardMax boardMin\n");
            System.exit(1);
        }
        
        /* get location of the registry service */
        String rmiRegHostName = Parameters.REGISTRY_HOSTNAME;
        int rmiRegPortNumb = Parameters.REGISTRY_PORT;
        
        String nameEntryBase = Parameters.REGISTRY_NAME_ENTRY;
        String nameEntryObject = Parameters.DEPART_AIRPORT_NAME_ENTRY;
        
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
        DepartAirport depAirp = new DepartAirport(nPassenger, boardMin, boardMax);
        interfaceDepAirp dep_stub = null;
        
        try
        { dep_stub = (interfaceDepAirp) UnicastRemoteObject.exportObject (depAirp, Parameters.DEPART_AIRPORT_PORT);
        }
        catch (RemoteException e)
        { System.out.printf("DepAirp stub generation exception: " + e.getMessage ());
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
        { registerInt.bind (nameEntryObject, dep_stub);
        }
        catch (RemoteException e)
        { System.out.printf ("DepAirp registration exception: " + e.getMessage ());
          e.printStackTrace ();
          System.exit (1);
        }
        catch (AlreadyBoundException e)
        { System.out.printf ("DepAirp already bound exception: " + e.getMessage ());
          e.printStackTrace ();
          System.exit (1);
        }
        System.out.printf ("DepAirp object was registered!");





        
        
       
    }
}