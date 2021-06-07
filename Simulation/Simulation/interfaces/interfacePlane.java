package Simulation.interfaces;

import java.rmi.*;

public interface interfacePlane extends Remote{
    
    void flyToDestinationPoint()throws RemoteException;
    void setFlightId(int id)throws RemoteException;
    void announceArrival(int id_to_set)throws RemoteException;
    void flyToDeparturePoint(int id_to_set)throws RemoteException;
    void waitBoarding()throws RemoteException;
    void boardThePlane(int person)throws RemoteException;
    void waitForEndOfFlight(int person)throws RemoteException;
    void leaveThePlane(int person)throws RemoteException;
    int getCapacity()throws RemoteException;




}
