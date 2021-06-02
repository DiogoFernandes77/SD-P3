package Simulation.interfaces;

import java.rmi.*;

public interface interfacePlane extends Remote{
    
    public void flyToDestinationPoint();
    public void setFlightId(int id);
    public void announceArrival();
    public void flyToDeparturePoint();
    public void waitBoarding();
    public void boardThePlane(int person);
    public void waitForEndOfFlight();
    public void leaveThePlane(int person);
    public int getCapacity();




}
