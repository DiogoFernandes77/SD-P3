package Simulation.server.Plane;

import Simulation.message.Message;

import Simulation.message.struct.PlaneMessage;
import Simulation.server.Serverable;


import static Simulation.message.struct.PlaneMessage.PlMessage.*;

/**
 * Plane class - process and reply methods
 */
public class Plane_interface implements Serverable {

    private Plane plane = null;

    /**
     * Instantiate a plane
     * @param plane
     */
    public Plane_interface(Plane plane) { this.plane = plane; }

    /**
     * Process and reply request
     * @param requestMessage
     * @return
     */
    @Override
    public Message processAndReply(Message requestMessage) {
        Message response = null;
        Enum requestType = requestMessage.getType();

        if (PIL_FlY_TO_DEST.equals(requestType)) {
            plane.flyToDestinationPoint();
            response = new PlaneMessage(SUCCESS);
        } else if (PIL_SET_FLIGHT_ID.equals(requestType)) {
            int id = ((PlaneMessage) requestMessage).getId();
            plane.setFlightId(id);
            response = new PlaneMessage(SUCCESS);
        } else if (PIL_AN_ARRIVAL.equals(requestType)) {
            plane.announceArrival();
            response = new PlaneMessage(SUCCESS);
        } else if (PIL_FLY_TO_DEP.equals(requestType)) {
            plane.flyToDeparturePoint();
            response = new PlaneMessage(SUCCESS);
        } else if (HOS_WAIT_BOARDING.equals(requestType)) {
            plane.waitBoarding();
            response = new PlaneMessage(SUCCESS);
        } else if (PASS_BOARD_PLANE.equals(requestType)) {
            int id = ((PlaneMessage) requestMessage).getId();
            plane.boardThePlane(id);
            response = new PlaneMessage(SUCCESS);
        } else if (PASS_WAIT_END_FLIGHT.equals(requestType)) {
            plane.waitForEndOfFlight();
            response = new PlaneMessage(SUCCESS);
        } else if (PASS_LEAVE_PLANE.equals(requestType)) {
            int id = ((PlaneMessage) requestMessage).getId();
            plane.leaveThePlane(id);
            response = new PlaneMessage(SUCCESS);
        } else if (GET_CAPACITY.equals(requestType)) {
            int capacity = plane.getCapacity();
            response = new PlaneMessage(SUCCESS, capacity);
        }

        return response;
    }
}