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
import Simulation.interfaces.interfaceLog;
import Simulation.server.LogPackage.Logger_Class;

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
    private interfaceLog log_int = null;
    private Passenger_State passenger_state;

    /**
     * Constructor Passageiro, faz set ao estado do passageiro com o certo id
     * Manda uma mensagem pelo o //Logger_stub que o estado mudou de um certo id
     * @param id
     */
    public Passenger(int id, interfaceDepAirp dep_int, interfacePlane plane_int, interfaceDestAirp dest_int, interfaceLog log_int){
        passenger_state = Passenger_State.GOING_TO_AIRPORT;
        id_passenger = id;
        
        this.dep_int = dep_int;
        this.plane_int = plane_int;
        this.dest_int = dest_int;
        this.log_int = log_int;
        try {
            synchronized (interfaceLog.class) {
                this.log_int.setST_Passenger(id_passenger, passenger_state);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * Implementação do médodo run() que estabiliza o operar da thread
     * @return SHUT to //Logger_stub if work done
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
            //Logger_stub.shutdown();
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
     * Manda uma mensagem pelo o //Logger_stub que o estado mudou de um certo id, e escreve no ficheiro
     * Manda uma mensagem pelo o dep_int para meter o passageiro de id em estado de entrada
     */
    private void enterQueue()throws RemoteException{
        passenger_state = Passenger_State.IN_QUEUE;
        synchronized (interfaceLog.class) {
            log_int.setST_Passenger(id_passenger, passenger_state);
            log_int.log_write("Passenger " + id_passenger + " is entering in queue");
        }
        dep_int.enterQueue(id_passenger);
    }

    /**
     * Passageiro espera na queue até ser chamado pela hospedeira para mostrar os documentos
     * Manda uma mensagem pelo o //Logger_stub que o estado mudou de um certo id, e escreve no ficheiro
     * Manda uma mensagem pelo o dep_int para meter o passageiro de id em estado de espera
     */
    private void waitInQueue() throws RemoteException{
        passenger_state = Passenger_State.IN_QUEUE;
        synchronized (interfaceLog.class) {
            log_int.setST_Passenger(id_passenger, passenger_state);
            log_int.log_write("Passenger " + id_passenger + " is in queue");
        }
        dep_int.waitInQueue(id_passenger);
    }

    /**
     * Passageiro mostra documentos
     * Manda uma mensagem pelo o dep_int para meter o passageiro de id mostrou os documentos
     */
    private void showDocuments()throws RemoteException{ dep_int.showDocuments(id_passenger); }

    /**
     * Passageiro entra no avião
     * Manda uma mensagem pelo o plane_int para meter o passageiro de id entrou no avião
     */
    private void boardThePlane()throws RemoteException{ plane_int.boardThePlane(id_passenger); }

    /**
     * Passageiro espera que o voo che ao fim
     * Manda uma mensagem pelo o //Logger_stub que o estado mudou de um certo id, e escreve no ficheiro
     * Manda uma mensagem pelo o plane_int para meter o passageiro de id está em voo
     */
    private void waitForEndOfFlight()throws RemoteException{
        passenger_state = Passenger_State.IN_FLIGHT;
        synchronized (interfaceLog.class) {
            log_int.setST_Passenger(id_passenger, passenger_state);
            log_int.log_write("Passenger " + id_passenger + " is in flight");
        }
        plane_int.waitForEndOfFlight();
    }
    /**
     * Passageiro sai do aviao
     * Manda uma mensagem pelo o plane_int a informar que o passageiro de id saiu do aviao
     */
    private void leaveThePlane()throws RemoteException{ plane_int.leaveThePlane(id_passenger); }

    /**
     * Passageiro sai do aeroporto e acaba a sua tarefa
     * Manda uma mensagem pelo o //Logger_stub que o estado mudou de um certo id, e escreve no ficheiro
     * A informar que o passageiro já está no destino
     * Manda uma mensagem pelo o dest_int para adiconar o passageiro id num array que vai obter os passageiros que já sairam
     */
    private void death()throws RemoteException{
        passenger_state = Passenger_State.AT_DESTINATION;
        synchronized (interfaceLog.class) {
            log_int.setST_Passenger(id_passenger, passenger_state);
            log_int.log_write("Passenger " + id_passenger + " is at destination");
        }
        dest_int.Passenger_death(id_passenger);
    }

    /**
     * Obter ID do passageiro
     * @return  int - ID_passenger
     */
    public int getId_passenger(){ return id_passenger; }
}