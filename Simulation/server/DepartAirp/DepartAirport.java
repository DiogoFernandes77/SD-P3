/**
 *  Log Class to produce log file each initiation
 *  @author António Ramos e Diogo Fernandes
 */

package Simulation.server.DepartAirp;


import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.rmi.*;

import Simulation.interfaces.*;
import Simulation.States.*;

/**
 * DepartAirport
 */
public class DepartAirport implements interfaceDepAirp{
    private static DepartAirport depArp_instance = null;

    private Hostess_State hostess_state;
    private Pilot_State pilot_state;
    private Passenger_State passenger_state;


    private interfaceLog log_int = null;

    private int nPassenger, boardMin, boardMax;
    private int current_capacity = 0;
    private int passenger_left;

    private final Lock lock;
    private final Condition waitingPlane, waitingPassenger,waitingCheck,waitingFly,waitingShow;
    private final Queue<Integer> queue ;
    
    private boolean plane_rdy = false;
    private boolean showing = false;
    private boolean rdyCheck = false;
    private boolean boardingComplete = false;
    private boolean block_state2 = false;

    /**
     * Construct for the departure airport, know passenger, plane capacity, min and max of boarding
     * @param nPassenger
     * @param boardMin
     * @param boardMax
     */
    public DepartAirport(int nPassenger, int boardMin, int boardMax, interfaceLog log_int){
        lock = new ReentrantLock();
        queue = new LinkedList<>();
        waitingPlane = lock.newCondition();
        waitingPassenger = lock.newCondition();
        waitingCheck = lock.newCondition();
        waitingShow = lock.newCondition();
        waitingFly = lock.newCondition();

        this.log_int = log_int;

        this.nPassenger = nPassenger;
        this.boardMin = boardMin;
        this.boardMax = boardMax;
        passenger_left = nPassenger;
        synchronized (interfaceLog.class){
            try {
                log_int.setQ(queue);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    //---------------------------------------------------/Pilot methods/-----------------------------------------------------//

    /**
     * Pilot inform Hostess that plane is ready to board
     */
    public void informPlaneReadyForBoarding(int id_to_set)throws RemoteException{
        lock.lock();
        try{
            current_capacity = 0;
            plane_rdy = true; 
            waitingPlane.signal();
            pilot_state = Pilot_State.READY_FOR_BOARDING;
            synchronized (interfaceLog.class) {
                log_int.setFN(id_to_set);
                log_int.setST_Pilot(pilot_state);
                log_int.board_start("\nFlight " + id_to_set + ": boarding started.\n");
            }
            System.out.print("Plane is Ready \n");
        }catch(Exception e){
            System.out.println("Interrupter Exception Error - " + e);
            e.printStackTrace();
        }finally{
            lock.unlock();
        }
    }

    /**
     * Waits for the passenger enter in the plane until hostess gives the signal
     */
    public void waitForAllInBoarding( )throws RemoteException{
        lock.lock();
        try{
            while(!boardingComplete){
                waitingFly.await();   
           }
            pilot_state = Pilot_State.WAIT_FOR_BOARDING;
            synchronized (interfaceLog.class) {
                log_int.setST_Pilot(pilot_state);
                log_int.log_write("Pilot is waiting for boarding");
            }
           boardingComplete = false;
        }catch(Exception e){
            System.out.println("Interrupter Exception Error - " + e);
            e.printStackTrace();
        }finally{
            lock.unlock();
        }
    }

    /**
     * Pilot inform that he is in transfer gate
     */
    public void parkAtTransferGate()throws RemoteException{
        lock.lock();
        try{
            System.out.println("PILOT: parking plane \n");
            pilot_state = Pilot_State.AT_TRANSFER_GATE;
            synchronized (interfaceLog.class) {
                log_int.setST_Pilot(pilot_state);
                log_int.log_write("Pilot is at transfer gate");
            }
            System.out.printf("PILOT: passenger left = %d \n", passenger_left);
        }catch(Exception e){
            System.out.println("Interrupter Exception Error - " + e);
            e.printStackTrace();
        }finally{
            lock.unlock();
         }
    }

    /**
     *  Pilot inform to write summary on file
     */

    public void summary() throws RemoteException{
        synchronized(interfaceLog.class){
            log_int.summary();
        }
    }

    //---------------------------------------------------/Hostess methods/-----------------------------------------------------//

    /**
     * Hostess gets ready and waits until first passenger
     */
    public void prepareForPassBoarding()throws RemoteException{
        lock.lock();
        try{
            System.out.print("Prepare for Boarding! \n");
            while(queue.isEmpty()){
                waitingPassenger.await();
            }
            hostess_state = Hostess_State.WAIT_FOR_PASSENGER;
            synchronized (interfaceLog.class) {
                log_int.setST_Hostess(hostess_state);
                log_int.log_write("Hostess is waiting for passenger");
            }
        }catch(Exception e){
            System.out.println("Interrupter Exception Error - " + e);
            e.printStackTrace();
        }finally{
            lock.unlock();
        }
    }

    /**
     * Hostess check documents of the passenger in queue
     */
    public void checkDocuments()throws RemoteException{
        lock.lock();
        try{
            int person_id = queue.peek();

            rdyCheck = true;
            waitingCheck.signalAll();
            
            System.out.println("Hostess is waiting to documents to be shown \n");
            while(!showing){
                waitingShow.await();  
            }
            queue.remove();
            hostess_state = Hostess_State.CHECK_PASSENGER;
            synchronized (interfaceLog.class) {
                log_int.setST_Hostess(hostess_state);
                log_int.log_write("Hostess is checking documents of passengers");
                log_int.setQ(queue);
            }

            showing = false;
            rdyCheck = false;
            waitingPassenger.signal();
            current_capacity++;
            passenger_left--;

        }catch(Exception e){
            System.out.println("Interrupter Exception Error - " + e);
            e.printStackTrace();
        }finally{
            lock.unlock();
        }
    }

    /**
     * Hostess waits for the passengers
     */
    public void waitForNextPassenger()throws RemoteException{
        lock.lock();
        try{
            block_state2 = true;
            waitingPassenger.signal();
            System.out.print("Hostess waiting passenger \n");
            while(queue.isEmpty()){
                waitingPassenger.await(); 
            }
            hostess_state = Hostess_State.WAIT_FOR_PASSENGER;
            synchronized (interfaceLog.class) {
                log_int.setST_Hostess(hostess_state);
                log_int.log_write("Hostess is waiting for next passenger");
            }
        }catch(Exception e){
            System.out.println("Interrupter Exception Error - " + e);
            e.printStackTrace();
        }finally{
            lock.unlock();
        }
    }

    /**
     * Hostess signals pilot that he can fly
     */
    public void informPlaneReadyToTakeOff() throws RemoteException{
        lock.lock();
        try{
            boardingComplete = true;
            waitingFly.signal();
            hostess_state = Hostess_State.READY_TO_FLY;
            synchronized (interfaceLog.class) {
                log_int.setST_Hostess(hostess_state);
                log_int.log_write("Hostess tell pilot that he can fly");
                log_int.departed(current_capacity);
            }
        }catch(Exception e){
            System.out.println("Interrupter Exception Error - " + e);
            e.printStackTrace();
        }finally{
            lock.unlock();
        }
    }

    /**
     * Hostess waits until next flight
     */
    public void waitForNextFlight()throws RemoteException{
        lock.lock();
        try{
            while(!plane_rdy){
                waitingPlane.await();
            }
            plane_rdy = false;
            hostess_state = Hostess_State.WAIT_FOR_NEXT_FLIGHT;
            synchronized (interfaceLog.class) {
                log_int.setST_Hostess(hostess_state);
                log_int.setQ(queue);
                log_int.log_write("Hostess is waiting for next flight");
            }
        }catch(Exception e){
            System.out.println("Interrupter Exception Error - " + e);
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
    }
    
    //---------------------------------------------------/Passenger methods/-----------------------------------------------------//

    /**
     * Passenger person enters in queue
     * @param person - id passenger
     */
    public void enterQueue(int person)throws RemoteException{
        lock.lock();
        try{
            System.out.printf("passenger %d enters queue \n", person);
            queue.add(person);
          //coment and try
            passenger_state = Passenger_State.IN_QUEUE;
            synchronized (interfaceLog.class) {
                log_int.setST_Passenger(person, passenger_state);
                log_int.setQ(queue);
                log_int.log_write("Passenger " + person + " is entering in queue");
            }
        }catch(Exception e){
            System.out.println("Interrupter Exception Error - " + e);
            e.printStackTrace();
        }finally{
            lock.unlock();
        }
    }

    /**
    * Passenger waits in the queue before showing docs
     * @param person - id passenger
     */
    public void waitInQueue(int person)throws RemoteException{   
        lock.lock();
        try{
            waitingPassenger.signal();
            System.out.printf("passenger %d wait for check \n", person);
            passenger_state = Passenger_State.IN_QUEUE;
            synchronized (interfaceLog.class) {
                log_int.setST_Passenger(person, passenger_state);
                log_int.setQ(queue);
                log_int.log_write("Passenger " + person + " is wait in queue");
            }
            while(!(rdyCheck && (queue.peek() == person))){// each thread see if hostess is ready and if is their turn
                waitingCheck.await();
            }

        }catch(Exception e){
            System.out.println("Interrupter Exception Error - " + e);
            e.printStackTrace();
        }finally{
            lock.unlock();
        }
    }

    /**
     * Passenger shows documents
     * @param person - id passenger
     */
    public void showDocuments(int person)throws RemoteException{
        lock.lock();
        try{
            showing = true;
            waitingShow.signal();
            System.out.printf("passenger %d  show documents \n", person);
            //block state 2
            synchronized (interfaceLog.class) {
                log_int.pass_check(": passenger " + person + " checked.\n");
            }
            while(!block_state2){
                waitingPassenger.await(); 
            }
            block_state2 = false;
        }catch(Exception e){
            System.out.println("Interrupter Exception Error - " + e);
            e.printStackTrace();
        }finally{
            lock.unlock();
        }
    }
 //---------------------------------------------------/getters/setters/-----------------------------------------------------//

    /**
     * get passenger left
     * @return passenger_left
     */
    public int getPassenger_left()throws RemoteException{
        return this.passenger_left;
    }
    /**
     * getBoardingMin
     * @return boardMin
     */
    public int getBoardingMin()throws RemoteException{
        return boardMin;
    }
    /**
     * getBoardingMax
     * @return boardMax
     */
    public int getBoardingMax()throws RemoteException{
        return boardMax;
    }
    /**
     * getCurrent_capacity
     * @return current_capacity
     */
    public int getCurrent_capacity()throws RemoteException{
        return current_capacity;
    }
    /**
     * getIsQueueEmpty
     * @return <li>True if is empty <li> False if not
     */
    public boolean getIsQueueEmpty()throws RemoteException{
        return queue.isEmpty();
    }
    /**
     * stillPassenger
     * @return <li>True if is empty <li> False if not
     */
    public boolean stillPassenger()throws RemoteException{ return (passenger_left != 0); }
}
