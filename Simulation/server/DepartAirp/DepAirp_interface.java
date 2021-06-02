package Simulation.server.DepartAirp;

import Simulation.message.Message;
import Simulation.message.struct.DepartureAirpMessage;
import Simulation.server.Serverable;


import static Simulation.message.struct.DepartureAirpMessage.DpAirpMessage.*;

/**
 * Departure interface para implementar as mensagens recebidas
 */
public class DepAirp_interface implements Serverable {
    private DepartAirport depAirp = null;

    /**
     * Pass instance of Class DepartAirport
     *
     * @param depAirp
     */
    public DepAirp_interface(DepartAirport depAirp) {
        this.depAirp = depAirp;
    }

    /**
     * Process and reply messages received
     *
     * @param requestMessage
     * @return
     */
    @Override
    public Message processAndReply(Message requestMessage) {
        Message response = null;
        Enum requestType = requestMessage.getType();
        System.out.println(requestType.toString());

        if (PIL_INFORM_PLANE_RDY_BOARD.equals(requestType)) {
            depAirp.informPlaneReadyForBoarding();
            response = new DepartureAirpMessage(SUCCESS);
        } else if (PIL_WAIT_FOR_ALL_BOARDING.equals(requestType)) {
            depAirp.waitForAllInBoarding();
            response = new DepartureAirpMessage(SUCCESS);
        } else if (PIL_PARK_AT_TRANSFER_GATE.equals(requestType)) {
            depAirp.parkAtTransferGate();
            response = new DepartureAirpMessage(SUCCESS);
        } else if (HOS_PREPARE_PASS_BOARDING.equals(requestType)) {
            depAirp.prepareForPassBoarding();
            response = new DepartureAirpMessage(SUCCESS);
        } else if (HOS_CHECK_DOCUMENTS.equals(requestType)) {
            depAirp.checkDocuments();
            response = new DepartureAirpMessage(SUCCESS);
        } else if (HOS_WAIT_FOR_NEXT_PASSENGER.equals(requestType)) {
            depAirp.waitForNextPassenger();
            response = new DepartureAirpMessage(SUCCESS);
        } else if (HOS_INFORM_PLANE_TAKEOFF.equals(requestType)) {
            depAirp.informPlaneReadyToTakeOff();
            response = new DepartureAirpMessage(SUCCESS);
        } else if (HOS_WAIT_NEXT_FLIGHT.equals(requestType)) {
            depAirp.waitForNextFlight();
            response = new DepartureAirpMessage(SUCCESS);
        } else if (PASS_ENTER_QUEUE.equals(requestType)) {
            int id = ((DepartureAirpMessage) requestMessage).getPerson_id();
            depAirp.enterQueue(id);
            response = new DepartureAirpMessage(SUCCESS);
        } else if (PASS_WAIT_QUEUE.equals(requestType)) {
            int id = ((DepartureAirpMessage) requestMessage).getPerson_id();
            depAirp.waitInQueue(id);
            response = new DepartureAirpMessage(SUCCESS);
        } else if (PASS_SHOW_DOCS.equals(requestType)) {
            int id = ((DepartureAirpMessage) requestMessage).getPerson_id();
            depAirp.showDocuments(id);
            response = new DepartureAirpMessage(SUCCESS);
        } else if (GET_CURRENT_CAPACITY.equals(requestType)) {
            int current_capacity = depAirp.getCurrent_capacity();
            response = new DepartureAirpMessage(SUCCESS, current_capacity);
        } else if (GET_BOARD_MIN.equals(requestType)) {
            int board_min = depAirp.getBoardingMin();
            response = new DepartureAirpMessage(SUCCESS, board_min);
        } else if (GET_BOARD_MAX.equals(requestType)) {
            int board_max = depAirp.getBoardingMax();
            response = new DepartureAirpMessage(SUCCESS, board_max);
        } else if (GET_IS_QUEUE_EMPTY.equals(requestType)) {
            boolean queueEmpty = depAirp.getIsQueueEmpty();
            response = new DepartureAirpMessage(SUCCESS, queueEmpty);
        } else if (STILL_PASSENGER.equals(requestType)) {
            boolean passengerLeft = depAirp.stillPassenger();
            response = new DepartureAirpMessage(SUCCESS, passengerLeft);
        } else if (GET_PASSENGER_LEFT.equals(requestType)) {
            int passengerLeft = depAirp.getPassenger_left();
            response = new DepartureAirpMessage(SUCCESS, passengerLeft);
        }
        return response;
    }
}