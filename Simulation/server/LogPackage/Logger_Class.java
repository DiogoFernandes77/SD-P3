/**
 * Log Class to produce log file each initiation
 *
 * @author António Ramos e Diogo Fernandes
 */

package Simulation.server.LogPackage;

import Simulation.interfaces.interfaceLog;
import Simulation.States.*;

import java.io.*;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Queue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

//Class responsible to write on file the state of program
public class Logger_Class implements interfaceLog {
    //implements singleton for repository information about what happens in the simulation, gives output file

    private static String file_name; // name file
    private final static String directory_file = "./Simulation/server/LogPackage/"; // where output files is stored
    private final static String default_name = "Logger_"; //default name
    private final static String extension_file = ".txt"; //extension file

    private int nPassenger;
    public ArrayList<String> Summary = new ArrayList<>(); // struct to save what happened in each file

    //auxiliar variables
    int FN; // number of flight
    public Passenger_State[] ST_Passenger; // State of the Passengers; save state of each passenger
    public Pilot_State ST_Pilot; // State of the Pilot
    public Hostess_State ST_Hostess; // State of the Hostess

    private ArrayList<Integer> Q; // State of the waiting queue
    private ArrayList<Integer> IN_F; // State of in flight
    private ArrayList<Integer> ATL; // State of number of passengers that have already arrived at their destination

    private static FileWriter fileWriter; // Write on file

    //pilot variables abbreviate
    public String[] Pilot_state = new String[]{"ATRG", "RDFB", "WTFB", "FLFW", "DRPP", "FLBK"};

    //hostess variables abbreviate
    public String[] Hostess_state = new String[]{"WTFL", "WTPS", "CKPS", "RDTF"};

    //passenger variables abbreviate
    public String[] Passenger_state = new String[]{"GTAP", "INQE", "INFL", "ATDS"};

    private final Lock lock;

    /**
     * Constructor of Logger
     * @param nPassenger
     */
    public Logger_Class(int nPassenger) throws RemoteException {
        this.nPassenger = nPassenger;
        ST_Passenger = new Passenger_State[nPassenger];

        ST_Pilot = Pilot_State.AT_TRANSFER_GATE;
        ST_Hostess = Hostess_State.WAIT_FOR_NEXT_FLIGHT;

        Q = new ArrayList<Integer>();
        IN_F = new ArrayList<Integer>();
        ATL = new ArrayList<Integer>();
        lock = new ReentrantLock();

        this.init();
    }


    /**
     * Creation of a empty file
     * @return file_name
     */
    public String createFile() throws RemoteException {
        file_name = directory_file + default_name + extension_file; //output file
        File dir = new File(file_name);
        try {
            fileWriter = new FileWriter(file_name);
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file_name;
    }

    /**
     * Start writing head of file
     */
    public void init() throws RemoteException {
        String file_name = createFile(); //creation of file
        add_struct(file_name, nPassenger);   //header file
    }

    /**
     * header file
     * @param file_n -- String
     * @param n_passenger -- int
     */
    public void add_struct(String file_n, int n_passenger) throws RemoteException {
        try {
            fileWriter = new FileWriter(file_n);
            fileWriter.write("PT \t HT  ");
            for (int i = 0; i < n_passenger; i++) {
                if (i < 10)
                    fileWriter.write(" P0" + i + " ");
                else
                    fileWriter.write(" P" + i + " ");
            }
            fileWriter.write("\tInQ InF PTAL\n");
            fileWriter.write(Pilot_state[ST_Pilot.ordinal()] + " " + Hostess_state[ST_Hostess.ordinal()] + " ");
            for (int i = 0; i < n_passenger; i++) {
                fileWriter.write(Passenger_state[0] + " ");
            }
            fileWriter.write("\t0\t0\t0\n");
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /** Used for writing Flight x: boarding started, returnig, arrived
     * x is the number of the flight
     */
    public void board_start(String text) throws RemoteException {
        lock.lock();
        try {
            try {
                System.out.println(text);
                fileWriter = new FileWriter(file_name, true);
                fileWriter.write(text);
                fileWriter.flush();
                fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            System.out.println("Interrupter Exception Error - " + e);
            e.printStackTrace();
        } finally {
            lock.unlock();
        }


    }

    /**
     * add struct Flight x: passenger y checked.
     * @param text -- String
     */
    public void pass_check(String text) throws RemoteException {
        lock.lock();
        try {
            try {
                fileWriter = new FileWriter(file_name, true);
                fileWriter.write("\nFlight " + FN + text);
                fileWriter.flush();
                fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            System.out.println("Interrupter Exception Error - " + e);
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    /**
     * Write struct of Flight x departed with y passengers
     * Add output on array, for using in final simulation, for writing a summary
     * @param capacity -int
     */
    public void departed(int capacity) throws RemoteException {
        lock.lock();
        try {
            try {
                fileWriter = new FileWriter(file_name, true);
                fileWriter.write("\nFlight " + FN + " departed with " + capacity + " passengers.\n");
                Summary.add("Flight " + FN + " departed with " + capacity + " passengers.\n");
                fileWriter.flush();
                fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            System.out.println("Interrupter Exception Error - " + e);
            e.printStackTrace();
        } finally {
            lock.unlock();
        }

    }

    /**
     * Summary of flights
     */
    public void summary() throws RemoteException {
        try {
            fileWriter = new FileWriter(file_name, true);
            fileWriter.write("\nAirlift sum up:\n");
            for (String s : Summary)
                fileWriter.write(s + "\n");
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Write main events on file
     * @param type - string
     */
    public void log_write(String type) throws RemoteException {
        lock.lock();
        try {
            try {
                System.out.println(type);
                fileWriter = new FileWriter(file_name, true);
                StringBuilder struct_string = new StringBuilder();
                struct_string.append(Pilot_state[ST_Pilot.ordinal()]).append(" ");
                struct_string.append(Hostess_state[ST_Hostess.ordinal()]).append(" ");
                for (int i = 0; i < nPassenger; i++) {
                    try {
                        struct_string.append(Passenger_state[ST_Passenger[i].ordinal()]).append(" ");
                    } catch (NullPointerException e) {
                        struct_string.append("GTAP ");
                    }
                }
                try {
                    struct_string.append("\t").append(Q.size()).append("\t").append(IN_F.size()).append("\t").append(ATL.size()).append("\n");
                } catch (NullPointerException e) {
                    //struct_string.append("");
                }
                fileWriter.write(struct_string.toString());
                fileWriter.flush();
                fileWriter.close();

            } catch (IOException e) {
                System.out.print(e);
                e.printStackTrace();
            }
        } catch (Exception e) {
            System.out.println("Interrupter Exception Error - " + e);
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    // -------------------------- SETTERS ------------------------- //

    /**
     * Set FLIGHT NUMBER
     * @param FN
     */
    public void setFN(int FN) throws RemoteException {
        this.FN = FN;
    }

    /**
     * Set PASSENGER STATE OF ID
     * @param id
     * @param ST_Passenger
     */
    public void setST_Passenger(int id, Passenger_State ST_Passenger) throws RemoteException {
        this.ST_Passenger[id] = ST_Passenger;
    }

    /**
     * Set pilot STATE
     * @param ST_Pilot
     */
    public void setST_Pilot(Pilot_State ST_Pilot) throws RemoteException {
        this.ST_Pilot = ST_Pilot;
    }

    /**
     * Set pilot STATE
     * @param st
     */
    public void setST_Hostess(Hostess_State st) throws RemoteException {
        this.ST_Hostess = st;
    }

    /**
     * Set ArrayList in queue
     * @param q
     */
    public void setQ(Queue<Integer> q) throws RemoteException {
        Q = new ArrayList<>(q);
    }

    /**
     * Set ArrayList in flight
     * @param IN_F
     */
    public void setIN_F(ArrayList<Integer> IN_F) throws RemoteException {
        this.IN_F = IN_F;
    }

    /**
     * Set ArrayList at destination
     * @param ATL
     */
    public void setATL(ArrayList<Integer> ATL) throws RemoteException {
        this.ATL = ATL;
    }
}
