package Simulation.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Queue;

import Simulation.States.*;

public interface interfaceLog extends Remote {
    String createFile() throws RemoteException;
    void init() throws RemoteException;
    void add_struct(String file_n, int n_passenger) throws RemoteException;
    void board_start(String text) throws RemoteException;
    void pass_check(String text) throws RemoteException;
    void departed(int capacity) throws RemoteException;
    void summary() throws RemoteException;
    void log_write(String type) throws RemoteException;
    void setFN(int FN) throws RemoteException;
    void setST_Passenger(int id, Passenger_State ST_Passenger) throws RemoteException;
    void setST_Pilot(Pilot_State ST_Pilot) throws RemoteException;
    void setST_Hostess(Hostess_State st) throws RemoteException;
    void setQ(Queue<Integer> q) throws RemoteException;
    void setIN_F(ArrayList<Integer> IN_F) throws RemoteException;
    void setATL(ArrayList<Integer> ATL) throws RemoteException;
}
