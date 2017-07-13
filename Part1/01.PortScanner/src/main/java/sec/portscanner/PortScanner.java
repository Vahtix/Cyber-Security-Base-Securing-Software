package sec.portscanner;

import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

public class PortScanner {

    final static int MIN_PORT = 1024;
    final static int MAX_PORT = 49151;

    public static void main(String[] args) throws Exception {
        Scanner reader = new Scanner(System.in);

        System.out.println("Which address should I scan?");
        String address = reader.nextLine();
        System.out.println("Start at port?");
        int start = Integer.parseInt(reader.nextLine());
        System.out.println("End at port?");
        int end = Integer.parseInt(reader.nextLine());

        Set<Integer> ports = getAccessiblePorts(address, start, end);
        System.out.println("");

        if (ports.isEmpty()) {
            System.out.println("None found :(");
        } else {
            System.out.println("Found:");
            ports.stream().forEach(p -> System.out.println("\t" + p));
        }
        
        reader.close();
    }

    public static Set<Integer> getAccessiblePorts(String address, int start, int end) throws UnknownHostException, IOException {
        Set<Integer> accessiblePorts = new TreeSet<>();
        start = Math.max(start, MIN_PORT);
        end = Math.min(end, MAX_PORT);

    	Socket s;

    	System.out.println("Scanning...");
        
        for(int i = start; i <= end; i++){
        	    
        	try{           
        		s = new Socket(address, i);

                	System.out.println("Port " + i + " is available");

                	accessiblePorts.add(s.getPort());
                	s.close();		

        	}catch(ConnectException e){
                	System.out.println("Port " + i  + " is not available");
            	}			
        } 
        return accessiblePorts;
    }
}
