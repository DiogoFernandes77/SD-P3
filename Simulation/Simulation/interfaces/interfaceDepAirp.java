package Simulation.interfaces;

import java.rmi.*;

public interface interfaceDepAirp extends Remote{

    //pilot
    public void informPlaneReadyForBoarding()throws RemoteException ;
    public void waitForAllInBoarding()throws RemoteException ;
    public void parkAtTransferGate()throws RemoteException ;
    
    //hostess
    public void prepareForPassBoarding()throws RemoteException ;
    public void checkDocuments()throws RemoteException;
    public void waitForNextPassenger()throws RemoteException;
    public void informPlaneReadyToTakeOff()throws RemoteException;
    public void waitForNextFlight()throws RemoteException;

    //pilot
    public void enterQueue(int person)throws RemoteException;
    public void waitInQueue(int person)throws RemoteException;
    public void showDocuments(int person)throws RemoteException;

    //get/set
    public int getPassenger_left()throws RemoteException;
    public int getBoardingMin()throws RemoteException;
    public int getBoardingMax()throws RemoteException ;
    public int getCurrent_capacity()throws RemoteException ;
    public boolean getIsQueueEmpty()throws RemoteException ;
    public boolean stillPassenger()throws RemoteException ;



}