/**
 *  Log Class to produce log file each initiation
 *  @author António Ramos e Diogo Fernandes
 */

package Simulation.client;

import java.rmi.RemoteException;
import java.util.Random;

import Simulation.States.Passenger_State;
import Simulation.interfaces.interfaceDepAirp;
import Simulation.interfaces.interfaceDestAirp;
import Simulation.interfaces.interfacePlane;

/**
 * Passenger client thread
 *
 */
public class Passenger extends Thread{
    private int id_passenger = 0;
    private int count_AtDest = 0;

    private interfaceDepAirp dep_int;
    private interfacePlane plane_int;
    private interfaceDestAirp dest_int;
    private Passenger_State passenger_state;

    /**
     * Constructor Passageiro, faz set ao estado do passageiro com o certo id
     * @param id
     */
    public Passenger(int id, interfaceDepAirp dep_int, interfacePlane plane_int, interfaceDestAirp dest_int){
        passenger_state = Passenger_State.GOING_TO_AIRPORT;
        id_passenger = id;
        
        this.dep_int = dep_int;
        this.plane_int = plane_int;
        this.dest_int = dest_int;
    }

    /**
     * Implementação do médodo run() que estabiliza o operar da thread
     * */
    @Override
    public void run(){
        try{
            travelToAirport();
            enterQueue();
            waitInQueue();
            showDocuments();
            boardThePlane();
            waitForEndOfFlight();
            leaveThePlane();
            death();
        }catch(RemoteException e){
            System.out.print(e);
        }
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
     */
    private void enterQueue()throws RemoteException{
        passenger_state = Passenger_State.IN_QUEUE;
        dep_int.enterQueue(id_passenger);
    }

    /**
     * Passageiro espera na queue até ser chamado pela hospedeira para mostrar os documentos
     */
    private void waitInQueue() throws RemoteException{
        passenger_state = Passenger_State.IN_QUEUE;
        dep_int.waitInQueue(id_passenger);
    }

    /**
     * Passageiro mostra documentos
     */
    private void showDocuments()throws RemoteException{ dep_int.showDocuments(id_passenger); }

    /**
     * Passageiro entra no avião
     */
    private void boardThePlane()throws RemoteException{ plane_int.boardThePlane(id_passenger); }

    /**
     * Passageiro espera que o voo che ao fim
     */
    private void waitForEndOfFlight()throws RemoteException{
        passenger_state = Passenger_State.IN_FLIGHT;
        plane_int.waitForEndOfFlight(id_passenger);
    }
    /**
     * Passageiro sai do aviao
     */
    private void leaveThePlane()throws RemoteException{ plane_int.leaveThePlane(id_passenger); }

    /**
     * Passageiro sai do aeroporto e acaba a sua tarefa
     */
    private void death()throws RemoteException{
        passenger_state = Passenger_State.AT_DESTINATION;
        dest_int.Passenger_death(id_passenger);
    }

    /**
     * Obter ID do passageiro
     * @return  int - ID_passenger
     */
    public int getId_passenger(){ return id_passenger; }
}