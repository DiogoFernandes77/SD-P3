package Simulation.client;

/**
 * PassengerClient is the class that instantiates a passenger thread
 */
public class PassengerClient{
    
    public static void main(String[] args)
    {
        
        Passenger passenger = new Passenger(Integer.parseInt(args[0]));
        System.out.println("Starting Passenger " + args[0] + " Thread");
        passenger.start();
        try
        {
            passenger.join();
        }
        catch (InterruptedException ex)
        {
            System.out.println("Interrupter Exception Error - " + ex.toString());
        }
        System.out.println("Passenger Thread Ended");
    }
}
