/**
 * Log Class to produce log file each initiation
 *
 * @author António Ramos e Diogo Fernandes
 */

package Simulation.server.DestinationAirp;

import java.rmi.RemoteException;

//import Simulation.stub.Logger_stub;

import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import Simulation.interfaces.interfaceDestAirp;

/**
 * Destination Airport
 */
public class DestAirport implements interfaceDestAirp {
    private final Lock lock;
    private static DestAirport destArp_instance = null;
    private static ArrayList<Integer> passenger_arrived;

    /**
     * Create new ArrayList and new ReentrantLock
     */
    public DestAirport() {
        passenger_arrived = new ArrayList<Integer>();
        lock = new ReentrantLock();
    }

    //---------------------------------------------------/Passenger methods/-----------------------------------------------------//

    /**
     * Adiciona um passageiro à lista de chegada ao destination
     * Manda uma Mensagem ao Logger_stub a informar o numero de pessoas que já sairam e faz set do array que se encontra no Logger_Class
     * @param person
     */
    public void Passenger_death(int person)throws RemoteException {
        lock.lock();
        try {
            passenger_arrived.add(person);
            //Logger_stub.getInstance().pass_leave_plane(passenger_arrived);
        } catch (Exception e) {
            System.out.println("Interrupter Exception Error - " + e);
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }
}