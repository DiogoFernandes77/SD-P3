package Simulation.server.LogPackage;

import Simulation.server.ServerCom;
import Simulation.server.Proxy;
import java.net.SocketTimeoutException;
/**
 * Logger_Server is the class that instantiates the Logger server
 */
public class Logger_Server {
    public static boolean waitConnection;
    public static int nPassenger,boardMax,boardMin;
    /**
     * nPassenger, boarding min, boarding max
     * Default 21 8 3 if args.length == 0
     * @param args - nPassenger, boarding min, boarding max
     */
    public static void main(String[] args){
        if(args.length == 3){//custom config
            try{
                nPassenger = Integer.parseInt(args[0]);
                boardMin = Integer.parseInt(args[1]);
                boardMax = Integer.parseInt(args[2]);
            }catch(Exception e){
                System.out.print("Args must be numbers \n");
                System.exit(1);
            }
            if(nPassenger == 0){
                System.out.print(" Nº passenger can't be 0 \n");
                System.exit(1);
            }
            if(boardMax < boardMin){
                System.out.print(" Boarding max needs to be higher than boarding min \n");
                System.exit(1);
            }
            System.out.print("Config Ok \n");
        }else if(args.length == 0){//default config
            nPassenger = 21;
            boardMax = 8;
            boardMin = 5;
            System.out.print("Config Ok \n");
        }else{
            System.out.print("Arguments missing/wrong \n");
            System.out.print("N_max_passengers boardMax boardMin\n");
            System.exit(1);
        }

        Logger_Class logger_class = new Logger_Class(nPassenger);
        Logger_Interface logger_int = new Logger_Interface(logger_class, nPassenger);

        ServerCom scon = new ServerCom (4004);
        scon.start();

        ServerCom sconi;
        Simulation.server.Proxy proxy;

        waitConnection = true;
        while (waitConnection) {
            try {
                sconi = scon.accept();
                proxy = new Proxy(sconi, logger_int);
                proxy.start();
            }
            catch (SocketTimeoutException e) {}
        }
        scon.end();
    }
}