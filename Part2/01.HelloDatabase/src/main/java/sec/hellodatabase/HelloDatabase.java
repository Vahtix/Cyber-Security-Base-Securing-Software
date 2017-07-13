package sec.hellodatabase;

import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import org.h2.tools.RunScript;

public class HelloDatabase {

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

        // Add the code that reads the Agents from the database 
        // and prints them here
        
        ResultSet prints = connection.createStatement().executeQuery("SELECT * FROM Agent");
        
        while(prints.next()){
            System.out.println(prints.getString("id") + " " + prints.getString("name"));
        }
        
        prints.close();
        connection.close();
    }
}
