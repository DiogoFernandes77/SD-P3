package Simulation.server.Plane;

import Simulation.server.ServerCom;

import java.net.SocketTimeoutException;

import Simulation.server.Proxy;

/**
 * Plane server
 */
public class Plane_server {
    public static boolean waitConnection;

    /**
     * Doesn't have args
     * @param args
     */
    public static void main(String[] args){
        
        Plane plane = new Plane();

        Plane_interface plane_inter = new Plane_interface(plane);

        ServerCom scon = new ServerCom(4002);
        Proxy proxy;
        ServerCom sconi;
        scon.start();
        waitConnection = true;
        while(waitConnection){
            
            try{
                sconi = scon.accept();
                proxy = new Proxy(sconi, plane_inter);
                proxy.start();

            }catch (SocketTimeoutException e) {}
        }
        scon.end();
    }
}