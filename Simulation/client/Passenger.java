/**
 *  Log Class to produce log file each initiation
 *  @author António Ramos e Diogo Fernandes
 */

package Simulation.client;

import java.util.Random;


import Simulation.stub.Logger_stub;
import Simulation.stub.Plane_stub;
import Simulation.States.Passenger_State;
import Simulation.stub.DepAirp_stub;
import Simulation.stub.DestAirp_stub;

/**
 * Passenger client thread
 *
 */
public class Passenger extends Thread{
    private int id_passenger = 0;
    private int count_AtDest = 0;

    private Passenger_State passenger_state;

    /**
     * Constructor Passageiro, faz set ao estado do passageiro com o certo id
     * Manda uma mensagem pelo o Logger_stub que o estado mudou de um certo id
     * @param id
     */
    public Passenger(int id){
        passenger_state = Passenger_State.GOING_TO_AIRPORT;
        id_passenger = id;
        Logger_stub.getInstance().pass_state(passenger_state,id_passenger);
    }

    /**
     * Implementação do médodo run() que estabiliza o operar da thread
     * @return SHUT to logger_stub if work done
     * */
    @Override
    public void run(){
        travelToAirport();
        enterQueue();
        waitInQueue();
        showDocuments();
        boardThePlane();
        waitForEndOfFlight();
        leaveThePlane();
        death();
        Logger_stub.getInstance().shutdown();
    }

    /**
     * Passageiro vai para o aeroporto com intervalos random entre 0 e 10 segundos
     * Fica a dormir o numero que gerar vezes 100
     */
    private void travelToAirport() {
        Random gen = new Random();
        int time = gen.nextInt(10);
        try{
            Thread.sleep(time * 100); 
        }catch(Exception e){
            System.out.print("Error traveling to airport");
        }
    }

    /**
     * Passageiro entra na queue
     * Manda uma mensagem pelo o Logger_stub que o estado mudou de um certo id, e escreve no ficheiro
     * Manda uma mensagem pelo o DepAirp_stub para meter o passageiro de id em estado de entrada
     */
    private void enterQueue(){
        passenger_state = Passenger_State.IN_QUEUE;
        Logger_stub.getInstance().pass_state_log(passenger_state,id_passenger, "Passenger " + id_passenger + " is entering in queue");
        DepAirp_stub.getInstance().enterQueue(id_passenger);
    }

    /**
     * Passageiro espera na queue até ser chamado pela hospedeira para mostrar os documentos
     * Manda uma mensagem pelo o Logger_stub que o estado mudou de um certo id, e escreve no ficheiro
     * Manda uma mensagem pelo o DepAirp_stub para meter o passageiro de id em estado de espera
     */
    private void waitInQueue(){
        passenger_state = Passenger_State.IN_QUEUE;
        Logger_stub.getInstance().pass_state_log(passenger_state,id_passenger, "Passenger " + id_passenger + " is in queue");
        DepAirp_stub.getInstance().waitInQueue(id_passenger);
    }

    /**
     * Passageiro mostra documentos
     * Manda uma mensagem pelo o DepAirp_stub para meter o passageiro de id mostrou os documentos
     */
    private void showDocuments(){ DepAirp_stub.getInstance().showDocuments(id_passenger); }

    /**
     * Passageiro entra no avião
     * Manda uma mensagem pelo o Plane_stub para meter o passageiro de id entrou no avião
     */
    private void boardThePlane(){ Plane_stub.getInstance().boardThePlane(id_passenger); }

    /**
     * Passageiro espera que o voo che ao fim
     * Manda uma mensagem pelo o Logger_stub que o estado mudou de um certo id, e escreve no ficheiro
     * Manda uma mensagem pelo o Plane_stub para meter o passageiro de id está em voo
     */
    private void waitForEndOfFlight(){
        passenger_state = Passenger_State.IN_FLIGHT;
        Logger_stub.getInstance().pass_state_log(passenger_state,id_passenger,"Passenger " + id_passenger + " is in flight");
        Plane_stub.getInstance().waitForEndOfFlight();
    }
    /**
     * Passageiro sai do aviao
     * Manda uma mensagem pelo o Plane_stub a informar que o passageiro de id saiu do aviao
     */
    private void leaveThePlane(){ Plane_stub.getInstance().leaveThePlane(id_passenger); }

    /**
     * Passageiro sai do aeroporto e acaba a sua tarefa
     * Manda uma mensagem pelo o Logger_stub que o estado mudou de um certo id, e escreve no ficheiro
     * A informar que o passageiro já está no destino
     * Manda uma mensagem pelo o DestAirp_stub para adiconar o passageiro id num array que vai obter os passageiros que já sairam
     */
    private void death(){
        passenger_state = Passenger_State.AT_DESTINATION;
        DestAirp_stub.getInstance().Passenger_death(id_passenger);
        Logger_stub.getInstance().pass_state_log(passenger_state,id_passenger,"Passenger " + id_passenger + " is at destination");
    }

    /**
     * Obter ID do passageiro
     * @return  int - ID_passenger
     */
    public int getId_passenger(){ return id_passenger; }
}