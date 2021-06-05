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
    private interfaceLog log_int = null;
    /**
     * Constructor da Hospedeira
     * <br>
     * Set estado e obtem a instancia do logger stub para fazer alteração do estado
     * */
    public Hostess(interfaceDepAirp dep_int, interfacePlane plane_int, interfaceLog log_int) {
        hostess_state = Hostess_State.WAIT_FOR_NEXT_FLIGHT;
        this.dep_int = dep_int;
        this.plane_int = plane_int;
        this.log_int = log_int;
        //Logger_stub.getInstance().hostess_state(hostess_state);
        synchronized (interfaceLog.class){
            try {
                log_int.setST_Hostess(hostess_state);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Implementação do médodo run() que estabiliza o operar da thread
     * @return SHUT to logger_stub if work done
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
     * Manda uma mensagem pelo o Logger_stub a informar que o estado mudou e escreve no ficheiro
     * Manda uma mensagem pelo o Ddep_int a informar que está a espera de um avião
     * */
    private void waitForNextFlight() throws RemoteException{
        hostess_state = Hostess_State.WAIT_FOR_NEXT_FLIGHT;
        //Logger_stub.getInstance().hostess_state_log(hostess_state, "Hostess is waiting for next flight");
        synchronized (interfaceLog.class) {
            log_int.setST_Hostess(hostess_state);
            log_int.log_write("Hostess is waiting for next flight");
        }
        dep_int.waitForNextFlight();
    }

    /**
     * Hospedeira vai esperar que cheguem passageiros ao aeroporto
     * Manda uma mensagem pelo o Logger_stub a informar que o estado mudou e escreve no ficheiro
     * Manda uma mensagem pelo o Ddep_int a informar que está a preparar-se para o voo
     * * */
    private void prepareForPassBoarding() throws RemoteException{
        hostess_state = Hostess_State.WAIT_FOR_PASSENGER;
        //Logger_stub.getInstance().hostess_state_log(hostess_state, "Hostess is waiting for passenger");
        synchronized (interfaceLog.class) {
            log_int.setST_Hostess(hostess_state);
            log_int.log_write("Hostess is waiting for passenger");
        }
        dep_int.prepareForPassBoarding();
    }

    /**
     * Hospedeira vai esperar que cheguem mais passageiros ao aeroporto, para atingir a capacidade do avião
     * Manda uma mensagem pelo o Logger_stub a informar que o estado mudou e escreve no ficheiro
     * Manda uma mensagem pelo o Ddep_int a informar que está à espera pelo proximo passageiro
     * * */
    private void waitForNextPassenger()throws RemoteException {
        hostess_state = Hostess_State.WAIT_FOR_PASSENGER;
        //Logger_stub.getInstance().hostess_state_log(hostess_state, "Hostess is waiting for passenger");
        synchronized (interfaceLog.class) {
            log_int.setST_Hostess(hostess_state);
            log_int.log_write("Hostess is waiting for next passenger");
        }
        dep_int.waitForNextPassenger();
    }

    /**
     * Hospedeira vai verificar os documentos dos passageiros que estao na fila
     * Manda uma mensagem pelo o Logger_stub a informar que o estado mudou e escreve no ficheiro
     * Manda uma mensagem pelo o Ddep_int a informar que está a checkar os documentos
     * */
    private void checkDocuments() throws RemoteException{
        hostess_state = Hostess_State.CHECK_PASSENGER;
        //Logger_stub.getInstance().hostess_state_log(hostess_state, "Hostess is checking documents of passengers");
        synchronized (interfaceLog.class) {
            log_int.setST_Hostess(hostess_state);
            log_int.log_write("Hostess is checking documents of passengers");
        }
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
        //Logger_stub.getInstance().hostess_state_log(hostess_state, "Hostess tell pilot that he can fly");
        synchronized (interfaceLog.class) {
            log_int.setST_Hostess(hostess_state);
            log_int.log_write("Hostess tell pilot that he can fly");
        }
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