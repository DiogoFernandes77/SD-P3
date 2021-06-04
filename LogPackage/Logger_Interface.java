package Simulation.server.LogPackage;

import Simulation.States.*;
import Simulation.message.Message;
import Simulation.message.struct.LoggerMessage;
import Simulation.server.LogPackage.Logger_Class;
import Simulation.server.Proxy;
import Simulation.server.Serverable;

import java.util.ArrayList;

import static Simulation.message.struct.LoggerMessage.LG_Message.*;
/**
 * Logger interface para implementar as mensagens recebidas
 */
public class Logger_Interface implements Serverable {
    private Logger_Class logger = null;
    private int counter;
    public Logger_Interface(Logger_Class log, int counter) {
        this.logger = log;
        this.counter = counter + 2;
    }

    /**
     * Process and reply messages received
     * @param inMessage
     * @return
     */
    @Override
    public Message processAndReply(Message inMessage) {
        Message response = null;
        Enum type = inMessage.getType();

        if(PASS_STATE.equals(type)){
            int id = ((LoggerMessage) inMessage).getId();
            Passenger_State ST_Passenger = ((LoggerMessage) inMessage).getST_Passenger();
            synchronized (Logger_Class.class){
                logger.setST_Passenger(id, ST_Passenger);
                //logger.log_write("");
            }
        }else if(PASS_STATE_LOG.equals(type)){
          int id = ((LoggerMessage) inMessage).getId();
          Passenger_State ST_Passenger = ((LoggerMessage) inMessage).getST_Passenger();
          String log = ((LoggerMessage) inMessage).getLog();
          synchronized (Logger_Class.class){
              logger.setST_Passenger(id,ST_Passenger);
              logger.log_write(log);
          }
        }else if(PIL_STATE.equals(type)){
            Pilot_State ST_Pilot = ((LoggerMessage) inMessage).getST_Pilot();
            synchronized (Logger_Class.class){
                logger.setST_Pilot(ST_Pilot);
            }
        } else if(PIL_STATE_LOG.equals(type) ) {
            Pilot_State ST_Pilot = ((LoggerMessage) inMessage).getST_Pilot();
            String log = ((LoggerMessage) inMessage).getLog();
            if (ST_Pilot.equals(Pilot_State.READY_FOR_BOARDING)){
                int FN = ((LoggerMessage) inMessage).getFN();
                synchronized (Logger_Class.class){
                    logger.setST_Pilot(ST_Pilot);
                    logger.setFN(FN);
                    logger.board_start("\nFlight " + FN + ": boarding started.\n");
                }
            } else if (ST_Pilot.equals(Pilot_State.DEBOARDING)){
                int FN = ((LoggerMessage) inMessage).getFN();
                synchronized (Logger_Class.class){
                    logger.setST_Pilot(ST_Pilot);
                    logger.board_start("\nFlight " + FN + ": arrived.\n");
                }
            } else if (ST_Pilot.equals(Pilot_State.FLYING_BACK)){
                int FN = ((LoggerMessage) inMessage).getFN();
                synchronized (Logger_Class.class){
                    logger.setST_Pilot(ST_Pilot);
                    logger.board_start("\nFlight " + FN + ": returning.\n");
                }
            }else {
                synchronized (Logger_Class.class){
                    logger.setST_Pilot(ST_Pilot);
                    logger.log_write(log);
                }
            }
        }else if (HOS_STATE.equals(type)){
            Hostess_State ST_Hostess = ((LoggerMessage) inMessage).getST_Hostess();
            synchronized (Logger_Class.class){
                logger.setST_Hostess(ST_Hostess);
            }
        } else if (HOS_STATE_LOG.equals(type)){
            Hostess_State ST_Hostess = ((LoggerMessage) inMessage).getST_Hostess();
            String log = ((LoggerMessage) inMessage).getLog();
            synchronized (Logger_Class.class){
                logger.setST_Hostess(ST_Hostess);
                logger.log_write(log);
            }
        }else if(PASS_CHECK.equals(type)){
            String log = ((LoggerMessage) inMessage).getLog();
            synchronized (Logger_Class.class){
                logger.pass_check(log);
            }
        }else if (DEPARTED.equals(type)){
            int capacity = ((LoggerMessage) inMessage).getCapacity();
            synchronized (Logger_Class.class){
                logger.departed(capacity);
            }
        }else if (PASS_ATL.equals(type)){
            ArrayList<Integer> ATL = ((LoggerMessage) inMessage).getATL();
            synchronized (Logger_Class.class){
                logger.setATL(ATL);
            }
        } else if (PASS_IN_Q.equals(type)){
            ArrayList<Integer> Q = ((LoggerMessage) inMessage).getQ();
            synchronized (Logger_Class.class){
                logger.setQ(Q);
            }
        }else if (PASS_ENTER_PLANE.equals(type)){
            ArrayList<Integer> IN_F = ((LoggerMessage) inMessage).getIN_F();
            synchronized (Logger_Class.class){
                logger.setIN_F(IN_F);
            }
        } else if (SHUT.equals(type)){
            if(--counter == 0){
                synchronized (Logger_Class.class){
                    logger.summary();
                }
            
            Logger_Server.waitConnection = false;
            (((Proxy) (Thread.currentThread())).getScon()).setTimeout(10);
            }
        }
        response = new LoggerMessage(SUCCESS);  
        return response;
    }
}