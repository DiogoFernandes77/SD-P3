package Simulation.server.DestinationAirp;

import Simulation.message.Message;
import Simulation.message.struct.DestinationAirpMessage;


import Simulation.server.Serverable;


import static Simulation.message.struct.DestinationAirpMessage.DstAirpMessage.*;
/**
 * Destination interface para implementar as mensagens recebidas
 */
public class DestAirp_interface implements Serverable{
    private DestAirport dest_airp = null;

    public DestAirp_interface(DestAirport dest_airp){
        this.dest_airp = dest_airp;
    }

    /**
     * Process and reply messages received
     * @param requestMessage
     * @return
     */
    @Override
    public Message processAndReply(Message requestMessage) {
        Message response = null;
        Enum requestType = requestMessage.getType();

        if (PASS_DEATH.equals(requestType)) {
            int id = ((DestinationAirpMessage) requestMessage).getId();
            dest_airp.Passenger_death(id);
            response = new DestinationAirpMessage(SUCCESS);
        }

        return response;
    }
}