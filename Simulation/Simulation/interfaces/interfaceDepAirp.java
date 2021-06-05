package Simulation.interfaces;

import java.rmi.*;

public interface interfaceDepAirp extends Remote{

    //pilot
    void informPlaneReadyForBoarding()throws RemoteException ;
    void waitForAllInBoarding()throws RemoteException ;
    void parkAtTransferGate()throws RemoteException ;
    
    //hostess
    void prepareForPassBoarding()throws RemoteException ;
    void checkDocuments()throws RemoteException;
    void waitForNextPassenger()throws RemoteException;
    void informPlaneReadyToTakeOff()throws RemoteException;
    void waitForNextFlight()throws RemoteException;

    //pilot
    void enterQueue(int person)throws RemoteException;
    void waitInQueue(int person)throws RemoteException;
    void showDocuments(int person)throws RemoteException;

    //get/set
    int getPassenger_left()throws RemoteException;
    int getBoardingMin()throws RemoteException;
    int getBoardingMax()throws RemoteException ;
    int getCurrent_capacity()throws RemoteException ;
    boolean getIsQueueEmpty()throws RemoteException ;
    boolean stillPassenger()throws RemoteException ;
}