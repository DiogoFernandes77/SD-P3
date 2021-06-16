/**
 *  Class Pilot
 *  @author António Ramos e Diogo Fernandes
 */
package Simulation.client;
import Simulation.interfaces.*;

import java.rmi.RemoteException;

import Simulation.States.Pilot_State;

/**
 * Class Pilot
 */
public class Pilot extends Thread{
    private Pilot_State pilot_state;
    private interfaceDepAirp dep_int = null;
    private interfacePlane plane_int = null;

    private int flight_passanger_number;
    public int id_to_set = 0;
    private Pilot_State get_State(){
        return pilot_state;
    }
    /**
     * Constructor Pilot, faz set ao estado do pilot
     */
    public Pilot(interfaceDepAirp dep_int , interfacePlane plane_int){
        pilot_state = Pilot_State.AT_TRANSFER_GATE;
        this.dep_int = dep_int;
        this.plane_int = plane_int;
    }

    /**
     * Implementação do médodo run() que estabiliza o operar da thread
     * */    @Override
    public void run(){
        try{
            do{
                setFlightID();
                informPlaneReadyForBoarding();
                waitForAllInBoarding();
                System.out.print("PILOT: GOING TO FLY \n" );
                flyToDestinationPoint();
                System.out.print("PILOT: Arrived \n" );
                announceArrival();
                flyToDeparturePoint();
                parkAtTransferGate();
            }while(stillPassenger());
            System.out.println("PILOT RUNS ENDED \n");
            sleep(1);
            dep_int.summary();
        }catch(RemoteException e){
            System.out.print(e);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Manda uma mensagem pelo o plane_int para fazer set do numero de voo
     */
    private void setFlightID()throws RemoteException{
    	id_to_set++;
        plane_int.setFlightId(id_to_set);
    }

    /**
     * Piloto informa que o aviao já se encontra pronto para o embarque
     */
    private void informPlaneReadyForBoarding()throws RemoteException{
        pilot_state = Pilot_State.READY_FOR_BOARDING;
        System.out.println("Pilot " + pilot_state);
        dep_int.informPlaneReadyForBoarding(id_to_set);
    }

    /**
     * Piloto aguarda que todos os passageiros entrem no aviao
     */
    private void waitForAllInBoarding()throws RemoteException{
        pilot_state = Pilot_State.WAIT_FOR_BOARDING;
        dep_int.waitForAllInBoarding();
    }

    /**
     * Piloto vai para o aeroporto de destino
     */
    private void flyToDestinationPoint()throws RemoteException{
        pilot_state = Pilot_State.FLYING_FORWARD;
        plane_int.flyToDestinationPoint();
    }

    /**
     * Piloto informa que chegou ao destino
     */
    private void announceArrival()throws RemoteException{
        pilot_state = Pilot_State.DEBOARDING;
        plane_int.announceArrival(id_to_set);
    }

    /**
     * Piloto volta que chegou ao departure
     */
    private void flyToDeparturePoint()throws RemoteException{
        pilot_state = Pilot_State.FLYING_BACK;
        plane_int.flyToDeparturePoint(id_to_set);
    }

    /**
     * Piloto estaciona no transfer gate
     */
    private void parkAtTransferGate()throws RemoteException{
        pilot_state = Pilot_State.AT_TRANSFER_GATE;
        dep_int.parkAtTransferGate();
    }

    /**
     * Verificar se existe passageiros na fila à espera para entrar no avião
     * @return <li>true if exist <li>false if not
     */
    public boolean stillPassenger()throws RemoteException{
        return dep_int.stillPassenger();
    }

    /**
     * Obtain Flight id
     * @return int flight_passanger_number
     */
    public int getFlight_passanger_number() {
        return this.flight_passanger_number;
    }

    /**
     * Set flight id
     * @param flight_passanger_number
     */
    public void setFlight_passanger_number(int flight_passanger_number) { this.flight_passanger_number = flight_passanger_number; }
}
