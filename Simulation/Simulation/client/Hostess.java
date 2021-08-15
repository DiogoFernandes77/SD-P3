/**
 * Hostess Class
 *
 * @author António Ramos e Diogo Fernandes
 */
package Simulation.client;

import java.rmi.RemoteException;

import Simulation.States.Hostess_State;
import Simulation.interfaces.*;

/**
 * Client Hostess
 *
 */
public class Hostess extends Thread {

    private Hostess_State hostess_state;
    private boolean end_flag = false;

    private interfaceDepAirp dep_int = null;
    private interfacePlane plane_int = null;
    /**
     * Constructor da Hospedeira
     * <br>
     * Set estado e obtem a instancia do logger stub para fazer alteração do estado
     * */
    public Hostess(interfaceDepAirp dep_int, interfacePlane plane_int) {
        hostess_state = Hostess_State.WAIT_FOR_NEXT_FLIGHT;
        this.dep_int = dep_int;
        this.plane_int = plane_int;
    }

    /**
     * Implementação do médodo run() que estabiliza o operar da thread
     * */
    @Override
    public void run() {
        try{
            do {
                waitForNextFlight();
                prepareForPassBoarding();
                while (getCurrent_capacity() < getBoardingMin() || (getCurrent_capacity() < getBoardingMax() && !getIsQueueEmpty())) {
                    if (getPassenger_left() == 0) {
                        System.out.println("LAST PASSENGER FLIGHT \n");
                        break;
                    }
                    waitForNextPassenger();
                    checkDocuments();
                }
                System.out.print(" CHECK COMPLETE \n");

                while (getCurrent_capacity() != plane_int.getCapacity()) {//garantir que os passageiros estao todos dentro do aviao antes de levantar voo, é raro entrar aqui mas pode acontecer
                    waitBoarding();
                }

                System.out.print(" BOARDING COMPLETE \n");
                informPlaneReadyToTakeOff();
            } while (stillPassenger());
            System.out.println("HOSTESS RUNS ENDED \n");
        }catch(RemoteException e){
            System.out.print(e);
        }
    }

    /**
     * Hospedeira vai ficar à espera que um avião chegue ao aeroporto
     * */
    private void waitForNextFlight() throws RemoteException{
        hostess_state = Hostess_State.WAIT_FOR_NEXT_FLIGHT;
        dep_int.waitForNextFlight();
    }

    /**
     * Hospedeira vai esperar que cheguem passageiros ao aeroporto
     * * */
    private void prepareForPassBoarding() throws RemoteException{
        hostess_state = Hostess_State.WAIT_FOR_PASSENGER;
        dep_int.prepareForPassBoarding();
    }

    /**
     * Hospedeira vai esperar que cheguem mais passageiros ao aeroporto, para atingir a capacidade do avião
     * * */
    private void waitForNextPassenger()throws RemoteException {
        hostess_state = Hostess_State.WAIT_FOR_PASSENGER;
        dep_int.waitForNextPassenger();
    }

    /**
     * Hospedeira vai verificar os documentos dos passageiros que estao na fila
     * */
    private void checkDocuments() throws RemoteException{
        hostess_state = Hostess_State.CHECK_PASSENGER;
        dep_int.checkDocuments();
    }

    /**
     * Hospedeira vai verificar/esperar que todos os passageiros entram no avião
     * */
    private void waitBoarding() throws RemoteException{
        plane_int.waitBoarding();
    }

    /**
     * Hospedeira avisa o piloto para levantar voo
     * */
    private void informPlaneReadyToTakeOff() throws RemoteException{
        hostess_state = Hostess_State.READY_TO_FLY;
        dep_int.informPlaneReadyToTakeOff();
    }

    /**
     * Obter o numero de passageiros restantes na fila
     * @return int n
     */
    private int getPassenger_left() throws RemoteException{
        return dep_int.getPassenger_left();
    }

    /**
     * Verificar se existe passageiros na fila à espera para entrar no avião
     * @return <li>true if exist <li>false if not
     */
    private boolean stillPassenger()throws RemoteException {
        return dep_int.stillPassenger();
    }

    /**
     * Obter o numero min de embarque para permitir que o aviao descole
     * @return int n
     */
    private int getBoardingMin() throws RemoteException{
        return dep_int.getBoardingMin();
    }

    /**
     * Obter o número max de embarque para permitir que o avião descole
     * @return int n
     */
    private int getBoardingMax() throws RemoteException{
        return dep_int.getBoardingMax();
    }

    /**
     * Obter a capacidade do aviao para permitir que este descole
     * @return int n
     */
    private int getCurrent_capacity() throws RemoteException{
        return dep_int.getCurrent_capacity();
    }

    /**
     * Verificar se a fila dos passageiros está vazia
     * @return <li>True if is empty <li>False if is not
     */
    private boolean getIsQueueEmpty() throws RemoteException{
        return dep_int.getIsQueueEmpty();
    }
}