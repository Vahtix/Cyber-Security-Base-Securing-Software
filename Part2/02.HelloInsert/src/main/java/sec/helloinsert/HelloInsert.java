package sec.helloinsert;

import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;
import org.h2.tools.RunScript;

public class HelloInsert {

    public static void main(String[] args) throws Exception {
        // Open connection to a database -- do not alter this code
        String databaseAddress = "jdbc:h2:file:./database";
        if (args.length > 0) {
            databaseAddress = args[0];
        }

        Connection connection = DriverManager.getConnection(databaseAddress, "sa", "");

        try {
            // If database has not yet been created, insert content
            RunScript.execute(connection, new FileReader("sql/database-schema.sql"));
            RunScript.execute(connection, new FileReader("sql/database-import.sql"));
        } catch (Throwable t) {
            System.err.println(t.getMessage());
        }

        // Add the code that first reads the agents from the database, then
        // asks for an agent (id and name) and stores the agent to the database.
        // Finally, the program prints the agents in the database again.

        String selectQuery = "SELECT * FROM Agent";
        Scanner reader = new Scanner(System.in);

        PreparedStatement safeSelect = connection.prepareStatement(selectQuery);
        ResultSet agents = safeSelect.executeQuery();
        
        System.out.println("Agents in database");
        while(agents.next()){
        	System.out.println(agents.getString("id") + " " + agents.getString("name"));
        }
        
        System.out.println("Add agent");
        System.out.println("Give agent id:");
        String newId = reader.nextLine();
        
        System.out.println("Give agent name:");
        String newName = reader.nextLine();
        
        String insertQuery = "INSERT INTO Agent (id, name) VALUES ('" + newId + "', '" + newName + "')";
        
        PreparedStatement safeInsert = connection.prepareStatement(insertQuery);
        safeInsert.executeUpdate();
        
        agents = safeSelect.executeQuery();
        
        System.out.println("Agents in database");
        while(agents.next()){
        	System.out.println(agents.getString("id") + " " + agents.getString("name"));
        }
        
        agents.close();
        connection.close();
        reader.close();
    }
}
