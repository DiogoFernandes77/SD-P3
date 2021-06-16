/**
 *  Plane Class
 *  @author António Ramos e Diogo Fernandes
 */

package Simulation.server.Plane;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import Simulation.States.Hostess_State;
import Simulation.States.Passenger_State;
import Simulation.States.Pilot_State;
import Simulation.interfaces.interfaceLog;
import Simulation.interfaces.interfacePlane;

import java.util.Random;

/**
 * Plane class - implement methods
 */
public class Plane implements interfacePlane {
    // static variable single_instance of type Singleton
    private static Plane plane_instance = null;
    private Hostess_State hostess_state;
    private Pilot_State pilot_state;
    private Passenger_State passenger_state;

    private interfaceLog log_int = null;
    private ArrayList<Integer> plane;
    private Condition flying;
    private Condition hostess;
    private Condition cd_deboarding;
    private int flight_id = 0;
    private final Lock lock;
    private boolean enter = false;
    private boolean plane_flying = true;

    Random gen = new Random();

    /**
     * Constructor Plane
     */
    public Plane(interfaceLog i_log){
        plane = new ArrayList<Integer>();
        lock = new ReentrantLock();
        flying = lock.newCondition();
        hostess = lock.newCondition();
        cd_deboarding = lock.newCondition();
        this.log_int = i_log;
    }


    //---------------------------------------------------/Pilot methods/-----------------------------------------------------//

    /**
     * Pilot flies to destination
     */
    public void flyToDestinationPoint()throws RemoteException{
        lock.lock();
        int delay = gen.nextInt(10);
        try{
            flying.await(delay, TimeUnit.MILLISECONDS);
            pilot_state = Pilot_State.FLYING_FORWARD;
            synchronized (interfaceLog.class){
                log_int.setST_Pilot(pilot_state);
                log_int.log_write("Pilot is flying forward");
            }
        }catch(Exception e){
             System.out.println("Interrupter Exception Error - " + e);
            e.printStackTrace();
         }finally{
            lock.unlock();
         }
    }

    /**
     * Set of flight id
     * @param id
     */
    public void setFlightId(int id)throws RemoteException{
        lock.lock();
        try{
            flight_id = id;
        }catch(Exception e){
            System.out.println("Interrupter Exception Error - " + e);
            e.printStackTrace();
         }finally{
            lock.unlock();
         }
    }

    /**
     * Pilot announce arrival
     */
    public void announceArrival(int id_to_set)throws RemoteException{
        lock.lock();
        try{
            plane_flying = false;
            flying.signalAll();
            while(!plane.isEmpty()){
                cd_deboarding.await();
            }
            pilot_state = Pilot_State.DEBOARDING;
            synchronized (interfaceLog.class) {
                log_int.setST_Pilot(pilot_state);
                log_int.board_start("\nFlight " + id_to_set + ": arrived.\n");
            }
            System.out.println("PILOT: deboarding complete \n");
        }catch(Exception e){
            System.out.println("Interrupter Exception Error - " + e);
            e.printStackTrace();
         }finally{
            lock.unlock();
         }
    }

    /**
     * Pilot flies to departure point
     */
    public void flyToDeparturePoint(int id_to_set)throws RemoteException{
        lock.lock();
        int delay = gen.nextInt(10);
        try{
            System.out.println("PILOT: Flying back \n");
            plane_flying = true;
            pilot_state = Pilot_State.FLYING_BACK;
            synchronized (interfaceLog.class) {
                log_int.setST_Pilot(pilot_state);
                log_int.board_start("\nFlight " + id_to_set + ": returning.\n");
            }
            flying.await(delay, TimeUnit.MILLISECONDS);
        }catch(Exception e){
            System.out.println("Interrupter Exception Error - " + e);
            e.printStackTrace();
         }finally{
            lock.unlock();
         }
    }

    //---------------------------------------------------/Hostess methods/-----------------------------------------------------//
    /**
     * Waiting for boarding
     */
    public void waitBoarding()throws RemoteException{
        lock.lock();
        try{
            while(!enter){
                hostess.await();
            }
            enter = false;
         }catch(Exception e){
            System.out.println("Interrupter Exception Error - " + e);
            e.printStackTrace();
        }finally{
            lock.unlock();
         }
    }

    //---------------------------------------------------/Passenger methods/-----------------------------------------------------//

    /**
     * Passenger of person enters on plane
     * @param person
     */
    public void boardThePlane(int person)throws RemoteException{
        lock.lock();
        try{
            plane.add(person);
            enter = true;
            hostess.signal();
            System.out.printf("passenger %d boarding plane \n", person);
            synchronized (interfaceLog.class) {
                log_int.setIN_F(plane);
            }
         }catch(Exception e){
            System.out.println("Interrupter Exception Error - " + e);
            e.printStackTrace();
        }finally{
             lock.unlock();
         }
    }
    /**
     * Passenger is in flight, wait for its end
     */
    public void waitForEndOfFlight(int id_passenger)throws RemoteException{
        lock.lock();
        try{
            while(plane_flying){
               flying.await(); 
            }
            passenger_state = Passenger_State.IN_FLIGHT;
            synchronized (interfaceLog.class) {
                log_int.setST_Passenger(id_passenger, passenger_state);
                log_int.log_write("Passenger " + id_passenger + " is in flight");
            }
        }catch(Exception e){
            System.out.println("Interrupter Exception Error - " + e);
            e.printStackTrace();
        }finally{
            lock.unlock();
        }
    }
    /**
     * Passenger person_id is leaving plane
     * @param person
     */
    public void leaveThePlane(int person)throws RemoteException{
        lock.lock();
        try{
            plane.remove(Integer.valueOf(person));
            cd_deboarding.signal();
            System.out.printf("Passenger %d leaving the plane \n", person);
            synchronized (interfaceLog.class) {
                log_int.setIN_F(plane);
            }
        }catch(Exception e){
            System.out.println("Interrupter Exception Error - " + e);
            e.printStackTrace();
        }finally{
            lock.unlock();
        }
    }

    //---------------------------------------------------/getters/setters/-----------------------------------------------------//

    /**
     * Get capacity of plane
     * @return plane.size()
     */
    public int getCapacity()throws RemoteException{
        return plane.size();
    }
}