/**
 *  Log Class to produce log file each initiation
 *  @author António Ramos e Diogo Fernandes
 */

package Simulation.server.Plane;

//import Simulation.stub.Logger_stub;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.Random;

/**
 * Plane class - implement methods
 */
public class Plane  {
    // static variable single_instance of type Singleton
    private static Plane plane_instance = null;
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
    public Plane(){
        plane = new ArrayList<Integer>();
        lock = new ReentrantLock();
        flying = lock.newCondition();
        hostess = lock.newCondition();
        cd_deboarding = lock.newCondition();
    }


    //---------------------------------------------------/Pilot methods/-----------------------------------------------------//

    /**
     * Pilot flies to destination
     */
    public void flyToDestinationPoint(){
        lock.lock();
        int delay = gen.nextInt(10);
        try{
            flying.await(delay, TimeUnit.MILLISECONDS);
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
    public void setFlightId(int id){
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
    public void announceArrival(){
        lock.lock();
        try{
            plane_flying = false;
            flying.signalAll();
            while(!plane.isEmpty()){
                cd_deboarding.await();
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
    public void flyToDeparturePoint(){
        lock.lock();
        int delay = gen.nextInt(10);
        try{
            System.out.println("PILOT: Flying back \n");
            plane_flying = true;
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
    public void waitBoarding(){
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
    public void boardThePlane(int person){
        lock.lock();
        try{
            plane.add(person);
            enter = true;
            hostess.signal();
            System.out.printf("passenger %d boarding plane \n", person);
            //Logger_stub.getInstance().pass_in_flight(plane);
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
    public void waitForEndOfFlight(){
        lock.lock();
        try{
            while(plane_flying){
               flying.await(); 
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
     * @param person_id
     */
    public void leaveThePlane(int person){
        lock.lock();
        try{
            plane.remove(Integer.valueOf(person));
            cd_deboarding.signal();
            System.out.printf("Passenger %d leaving the plane \n", person);
            //Logger_stub.getInstance().pass_in_flight(plane);
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
    public int getCapacity(){
        return plane.size();
    }
}