package Simulation.interfaces;

import java.rmi.*;

public interface interfaceDestAirp extends Remote {
     void Passenger_death(int person) throws RemoteException;
    
}
