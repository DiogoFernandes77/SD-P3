package Simulation.server.LogPackage;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import Simulation.Parameters;
import Simulation.interfaces.*;

/**
 * Logger_Server is the class that instantiates the Logger server
 */
public class Logger_Server {
    public static boolean waitConnection;
    public static int nPassenger,boardMax,boardMin;
    /**
     * nPassenger, boarding min, boarding max
     * Default 21 8 3 if args.length == 0
     * @param args - nPassenger, boarding min, boarding max
     */
    public static void main(String[] args) throws RemoteException {
        if (args.length == 3) {//custom config
            try {
                nPassenger = Integer.parseInt(args[0]);
                boardMin = Integer.parseInt(args[1]);
                boardMax = Integer.parseInt(args[2]);
            } catch (Exception e) {
                System.out.print("Args must be numbers \n");
                System.exit(1);
            }
            if (nPassenger == 0) {
                System.out.print(" Nº passenger can't be 0 \n");
                System.exit(1);
            }
            if (boardMax < boardMin) {
                System.out.print(" Boarding max needs to be higher than boarding min \n");
                System.exit(1);
            }
            System.out.print("Config Ok \n");
        } else if (args.length == 0) {//default config
            nPassenger = 21;
            boardMax = 8;
            boardMin = 5;
            System.out.print("Config Ok \n");
        } else {
            System.out.print("Arguments missing/wrong \n");
            System.out.print("N_max_passengers boardMax boardMin\n");
            System.exit(1);
        }

        /* get location of the registry service */
        String rmiRegHostName = Parameters.REGISTRY_HOSTNAME;
        int rmiRegPortNumb = Parameters.REGISTRY_PORT;

        String nameEntryBase = Parameters.REGISTRY_NAME_ENTRY;
        String nameEntryObject = Parameters.LOGGER_NAME_ENTRY;

        Registry registry = null;
        Register registerInt = null;

        /* create and install the security manager */
        if (System.getSecurityManager () == null){
            System.setSecurityManager (new SecurityManager ());
        }

        try {
            registry = LocateRegistry.getRegistry (rmiRegHostName, rmiRegPortNumb);
        } catch (RemoteException e) {
            System.out.printf("RMI registry locate exception: " + e.getMessage ());
            e.printStackTrace ();
            System.exit (1);
        }

        /* Initialize the shared region */
        Logger_Class logger_class = new Logger_Class(nPassenger);
        interfaceLog log_stub = null;

        try {
            log_stub = (interfaceLog) UnicastRemoteObject.exportObject(logger_class, Parameters.LOGGER_PORT);
        }catch (RemoteException e) {
            System.out.printf("Logger stub generation exception: " + e.getMessage ());
            e.printStackTrace ();
            System.exit (1);
        }

        /* register it with the general registry service */
        try {
            registerInt = (Register) registry.lookup (nameEntryBase);
        } catch (RemoteException e) {
            System.out.printf ("Register lookup exception: " + e.getMessage ());
            e.printStackTrace ();
            System.exit (1);
        } catch (NotBoundException e) {
            System.out.printf ("Register not bound exception: " + e.getMessage ());
            e.printStackTrace ();
            System.exit (1);
        }

        try {
            registerInt.bind (nameEntryObject, log_stub);
        } catch (RemoteException e) {
            System.out.printf ("Logger registration exception: " + e.getMessage ());
            e.printStackTrace ();
            System.exit (1);
        } catch (AlreadyBoundException e) {
            System.out.printf ("Logger already bound exception: " + e.getMessage ());
            e.printStackTrace ();
            System.exit (1);
        }
        System.out.printf ("Logger object was registered!");
    }
}