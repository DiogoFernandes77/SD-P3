package Simulation.interfaces;

import java.rmi.*;

public interface interfaceDepAirp extends Remote{

    //pilot
    public void informPlaneReadyForBoarding() ;
    public void waitForAllInBoarding() ;
    public void parkAtTransferGate() ;
    
    //hostess
    public void prepareForPassBoarding() ;
    public void checkDocuments();
    public void waitForNextPassenger();
    public void informPlaneReadyToTakeOff();
    public void waitForNextFlight();

    //pilot
    public void enterQueue(int person);
    public void waitInQueue(int person);
    public void showDocuments(int person);

    //get/set
    public int getPassenger_left();
    public int getBoardingMin();
    public int getBoardingMax() ;
    public int getCurrent_capacity() ;
    public boolean getIsQueueEmpty() ;
    public boolean stillPassenger() ;



}