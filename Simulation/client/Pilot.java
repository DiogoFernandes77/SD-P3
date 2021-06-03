/**
 *  Log Class to produce log file each initiation
 *  @author António Ramos e Diogo Fernandes
 */
package Simulation.client;
import Simulation.interfaces.*;
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
     * Manda uma mensagem pelo o //Logger_stub que o estado mudou
     */
    public Pilot(interfaceDepAirp dep_int , interfacePlane plane_int){
        pilot_state = Pilot_State.AT_TRANSFER_GATE;
        this.dep_int = dep_int;
        this.plane_int = plane_int;

        
        
        ////Logger_stub.pil_state(pilot_state);
    }

    /**
     * Implementação do médodo run() que estabiliza o operar da thread
     * @return SHUT to //Logger_stub if work done
     * */    @Override
    public void run(){
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
        ////Logger_stub.shutdown();

        System.out.println("PILOT RUNS ENDED \n");
    }

    /**
     * Manda uma mensagem pelo o plane_int para fazer set do numero de voo
     */
    private void setFlightID(){
    	id_to_set++;
        plane_int.setFlightId(id_to_set);
    }

    /**
     * Piloto informa que o aviao já se encontra pronto para o embarque
     * Manda uma mensagem pelo o //Logger_stub para informar que o voo x teve inicio e escreve a informação no ficheiro
     * Manda uma mensagem pelo o dep_int para informar que o aviao esta pronto para o embarque
     */
    private void informPlaneReadyForBoarding(){
        pilot_state = Pilot_State.READY_FOR_BOARDING;
        //Logger_stub.pil_board_start(pilot_state, id_to_set);//\nFlight " + id_to_set + ": boarding started.\n"
        System.out.println("Pilot " + pilot_state);
        dep_int.informPlaneReadyForBoarding();
    }

    /**
     * Piloto aguarda que todos os passeigeiros entrem no aviao
     * Manda uma mensagem pelo o //Logger_stub para informar que o estado mudou e escreve no ficheiro
     * Manda uma mensagem pelo o dep_int para informar que o piloto esta a espera que os passageiros embarquem
     */
    private void waitForAllInBoarding(){
        pilot_state = Pilot_State.WAIT_FOR_BOARDING;
        //Logger_stub.pil_state_log(pilot_state,"Pilot is waiting for boarding");
        dep_int.waitForAllInBoarding();
    }

    /**
     * Piloto vai para o aeroporto de destino
     * Manda uma mensagem pelo o //Logger_stub para informar que o estado mudou e escreve no ficheiro
     * Manda uma mensagem pelo o plane_int para informar que o piloto voou para o destino
     */
    private void flyToDestinationPoint(){
        pilot_state = Pilot_State.FLYING_FORWARD;
        //Logger_stub.pil_state_log(pilot_state, "Pilot is flying forward");
        plane_int.flyToDestinationPoint();
    }

    /**
     * Piloto informa que chegou ao destino
     * Manda uma mensagem pelo o //Logger_stub para informar que o estado mudou e escreve no ficheiro que o voo x chegou
     * Manda uma mensagem pelo o plane_int para informar que o piloto voou para o destino
     */
    private void announceArrival(){
        pilot_state = Pilot_State.DEBOARDING;
        //Logger_stub.pil_board_start(pilot_state, id_to_set);//\nFlight " + id_to_set + ": arrived.\n"
        plane_int.announceArrival();
    }

    /**
     * Piloto volta que chegou ao departure
     * Manda uma mensagem pelo o //Logger_stub para informar que o estado mudou e escreve no ficheiro que o voo x voltou
     * Manda uma mensagem pelo o plane_int para informar que o piloto voou para o departure
     */
    private void flyToDeparturePoint(){
        pilot_state = Pilot_State.FLYING_BACK;
        //Logger_stub.pil_board_start(pilot_state, id_to_set);//\nFlight " + id_to_set + ": returning.\n"
        plane_int.flyToDeparturePoint();
    }

    /**
     * Piloto estacion no transfer gate
     * Manda uma mensagem pelo o //Logger_stub para informar que o estado mudou e escreve no ficheiro que o voo x voltou
     * Manda uma mensagem pelo o plane_int para informar que o piloto estacionou no transfer gate
     */
    private void parkAtTransferGate(){
        pilot_state = Pilot_State.AT_TRANSFER_GATE;
        //Logger_stub.pil_state_log(pilot_state,"Pilot is at transfer gate");
        dep_int.parkAtTransferGate();
    }

    /**
     * Verificar se existe passageiros na fila à espera para entrar no avião
     * @return <li>true if exist <li>false if not
     */
    public boolean stillPassenger(){
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
