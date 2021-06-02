package Simulation.client;
/**
 * PilotClients is the class that instantiates the pilot thread
 */
public class PilotClient{
    public static void main(String[] args)
    {   System.out.println("Starting Pilot Thread");
        Pilot pil = new Pilot();
        
        pil.start();
        try
        {
            pil.join();
        }
        catch (InterruptedException ex)
        {
            System.out.println("Interrupter Exception Error - " + ex.toString());
        }
        System.out.println("Pilot Thread Ended");
    }
}
