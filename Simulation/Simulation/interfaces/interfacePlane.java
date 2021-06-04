package Simulation.interfaces;

import java.rmi.*;

public interface interfacePlane extends Remote{
    
    public void flyToDestinationPoint()throws RemoteException;
    public void setFlightId(int id)throws RemoteException;
    public void announceArrival()throws RemoteException;
    public void flyToDeparturePoint()throws RemoteException;
    public void waitBoarding()throws RemoteException;
    public void boardThePlane(int person)throws RemoteException;
    public void waitForEndOfFlight()throws RemoteException;
    public void leaveThePlane(int person)throws RemoteException;
    public int getCapacity()throws RemoteException;




}
