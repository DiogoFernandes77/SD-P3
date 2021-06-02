package Simulation.server.DestinationAirp;

import Simulation.server.ServerCom;

import java.net.SocketTimeoutException;

import Simulation.server.Proxy;

/**
 * Destination Server
 */
public class DestAirp_server {

    public static boolean waitConnection;

    /**
     * doesn't have args
     * @param args
     */
    public static void main(String[] args){
        DestAirport destAirp = new DestAirport();
        DestAirp_interface dest_inter = new DestAirp_interface(destAirp);

        ServerCom scon = new ServerCom(4003);
        Proxy proxy;
        ServerCom sconi;
        scon.start();
        waitConnection = true;
        while(waitConnection){
            try{
                sconi = scon.accept();
                proxy = new Proxy(sconi, dest_inter);
                proxy.start();
            }catch (SocketTimeoutException e) {}
        }
        scon.end();
    }
}