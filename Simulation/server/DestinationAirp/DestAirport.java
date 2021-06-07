/**
 * Destination Airport
 *
 * @author António Ramos e Diogo Fernandes
 */

package Simulation.server.DestinationAirp;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import Simulation.States.Passenger_State;
import Simulation.interfaces.interfaceDestAirp;
import Simulation.interfaces.interfaceLog;

/**
 * Destination Airport
 */
public class DestAirport implements interfaceDestAirp {
    private final Lock lock;
    private static final DestAirport destArp_instance = null;
    private static ArrayList<Integer> passenger_arrived;
    private interfaceLog i_log = null;
    private Passenger_State passenger_state;

    /**
     * Create new ArrayList and new ReentrantLock
     */
    public DestAirport(interfaceLog i_log) {
        passenger_arrived = new ArrayList<Integer>();
        lock = new ReentrantLock();
        this.i_log =i_log;
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
            passenger_state = Passenger_State.AT_DESTINATION;
            synchronized (interfaceLog.class) {
                i_log.setATL(passenger_arrived);
                i_log.setST_Passenger(person, passenger_state);
                i_log.log_write("Passenger " + person + " is at destination");
            }
        } catch (Exception e) {
            System.out.println("Interrupter Exception Error - " + e);
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }
}