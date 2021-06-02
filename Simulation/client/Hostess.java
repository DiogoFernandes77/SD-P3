/**
 * Log Class to produce log file each initiation
 *
 * @author António Ramos e Diogo Fernandes
 */
package Simulation.client;

import Simulation.stub.Logger_stub;
import Simulation.stub.Plane_stub;
import Simulation.States.Hostess_State;
import Simulation.stub.DepAirp_stub;

/**
 * Client Hospedeira de bordo
 *
 */
public class Hostess extends Thread {

    private Hostess_State hostess_state;
    private boolean end_flag = false;

    /**
     * Constructor da Hospedeira
     * <br>
     * Set estado e obtem a instancia do logger stub para fazer alteração do estado
     * */
    public Hostess() {
        hostess_state = Hostess_State.WAIT_FOR_NEXT_FLIGHT;
        Logger_stub.getInstance().hostess_state(hostess_state);
    }

    /**
     * Implementação do médodo run() que estabiliza o operar da thread
     * @return SHUT to logger_stub if work done
     * */
    @Override
    public void run() {
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

            while (getCurrent_capacity() != Plane_stub.getInstance().getCapacity()) {//garantir que os passageiros estao todos dentro do aviao antes de levantar voo, é raro entrar aqui mas pode acontecer
                waitBoarding();
            }

            System.out.print(" BOARDING COMPLETE \n");
            informPlaneReadyToTakeOff();
        } while (stillPassenger());
        Logger_stub.getInstance().shutdown();
        System.out.println("HOSTESS RUNS ENDED \n");
    }

    /**
     * Hospedeira vai ficar à espera que um avião chegue ao aeroporto
     * Manda uma mensagem pelo o Logger_stub a informar que o estado mudou e escreve no ficheiro
     * Manda uma mensagem pelo o DDepAirp_stub a informar que está a espera de um avião
     * */
    private void waitForNextFlight() {
        hostess_state = Hostess_State.WAIT_FOR_NEXT_FLIGHT;
        Logger_stub.getInstance().hostess_state_log(hostess_state, "Hostess is waiting for next flight");
        DepAirp_stub.getInstance().waitForNextFlight();
    }

    /**
     * Hospedeira vai esperar que cheguem passageiros ao aeroporto
     * Manda uma mensagem pelo o Logger_stub a informar que o estado mudou e escreve no ficheiro
     * Manda uma mensagem pelo o DDepAirp_stub a informar que está a preparar-se para o voo
     * * */
    private void prepareForPassBoarding() {
        hostess_state = Hostess_State.WAIT_FOR_PASSENGER;
        Logger_stub.getInstance().hostess_state_log(hostess_state, "Hostess is waiting for passenger");
        DepAirp_stub.getInstance().prepareForPassBoarding();
    }

    /**
     * Hospedeira vai esperar que cheguem mais passageiros ao aeroporto, para atingir a capacidade do avião
     * Manda uma mensagem pelo o Logger_stub a informar que o estado mudou e escreve no ficheiro
     * Manda uma mensagem pelo o DDepAirp_stub a informar que está à espera pelo proximo passageiro
     * * */
    private void waitForNextPassenger() {
        hostess_state = Hostess_State.WAIT_FOR_PASSENGER;
        Logger_stub.getInstance().hostess_state_log(hostess_state, "Hostess is waiting for passenger");
        DepAirp_stub.getInstance().waitForNextPassenger();
    }

    /**
     * Hospedeira vai verificar os documentos dos passageiros que estao na fila
     * Manda uma mensagem pelo o Logger_stub a informar que o estado mudou e escreve no ficheiro
     * Manda uma mensagem pelo o DDepAirp_stub a informar que está a checkar os documentos
     * */
    private void checkDocuments() {
        hostess_state = Hostess_State.CHECK_PASSENGER;
        Logger_stub.getInstance().hostess_state_log(hostess_state, "Hostess is checking documents of passengers");
        DepAirp_stub.getInstance().checkDocuments();
    }

    /**
     * Hospedeira vai verificar/esperar que todos os passageiros entram no avião
     * */
    private void waitBoarding() {
        Plane_stub.getInstance().waitBoarding();
    }

    /**
     * Hospedeira avisa o piloto para levantar voo
     * */
    private void informPlaneReadyToTakeOff() {
        hostess_state = Hostess_State.READY_TO_FLY;
        Logger_stub.getInstance().hostess_state_log(hostess_state, "Hostess tell pilot that he can fly");
        DepAirp_stub.getInstance().informPlaneReadyToTakeOff();
    }

    /**
     * Obter o numero de passageiros restantes na fila
     * @return int n
     */
    private int getPassenger_left() {
        return DepAirp_stub.getInstance().getPassenger_left();
    }

    /**
     * Verificar se existe passageiros na fila à espera para entrar no avião
     * @return <li>true if exist <li>false if not
     */
    private boolean stillPassenger() {
        return DepAirp_stub.getInstance().stillPassenger();
    }

    /**
     * Obter o numero min de embarque para permitir que o aviao descole
     * @return int n
     */
    private int getBoardingMin() {
        return DepAirp_stub.getInstance().getBoardingMin();
    }

    /**
     * Obter o número max de embarque para permitir que o avião descole
     * @return int n
     */
    private int getBoardingMax() {
        return DepAirp_stub.getInstance().getBoardingMax();
    }

    /**
     * Obter a capacidade do aviao para permitir que este descole
     * @return int n
     */
    private int getCurrent_capacity() {
        return DepAirp_stub.getInstance().getCurrent_capacity();
    }

    /**
     * Verificar se a fila dos passageiros está vazia
     * @return <li>True if is empty <li>False if is not
     */
    private boolean getIsQueueEmpty() {
        return DepAirp_stub.getInstance().getIsQueueEmpty();
    }
}