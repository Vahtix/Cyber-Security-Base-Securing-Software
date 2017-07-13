package sec.helloinsert;

import fi.helsinki.cs.tmc.edutestutils.MockStdio;
import fi.helsinki.cs.tmc.edutestutils.Points;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

@Points("S2.03")
public class HelloInsertTest {

    @Rule
    public MockStdio io = new MockStdio();

    private String databaseFile;
    private String databaseAddress;

    private List<String> agents;

    @Before
    public void setup() {
        this.databaseFile = "db-" + UUID.randomUUID().toString().substring(0, 6);
        this.databaseAddress = "jdbc:h2:file:./" + this.databaseFile;

        this.agents = new ArrayList<>();
    }

    @Test
    public void testAddAgent() throws Throwable {
        String id = "Major";
        String name = "Tickle";

        testAddAgent(id, name);
    }

    @Test
    public void testRandomAgent() throws Throwable {
        String id = UUID.randomUUID().toString().substring(0, 6);
        String name = UUID.randomUUID().toString().substring(0, 6);

        testAddAgent(id, name);
    }

    private void testAddAgent(String id, String name) throws Throwable {
        initDatabase(databaseAddress, agents);

        io.setSysIn(id + "\n" + name + "\n");
        HelloInsert.main(new String[]{databaseAddress});

        List<Agent> agents = getAgents(databaseAddress);
        removeFile(databaseFile);
        
        long count = agents.stream().filter(a -> a.id.equals(id) && a.name.equals(name)).count();
        assertTrue("Verify that the agent that the user inputs is added to the database.\nNow, no agents with the given id and name were found.", count == 1);        
    }

    private void initDatabase(String databaseAddress, List<String> entries) throws Throwable {
        Connection connection = DriverManager.getConnection(databaseAddress, "sa", "");
        connection.createStatement().executeUpdate("CREATE TABLE Agent (\n"
                + "    id varchar(9) PRIMARY KEY,\n"
                + "    name varchar(200)\n"
                + ");");

        for (int i = 0; i < entries.size() - 1; i += 2) {
            connection.createStatement().executeUpdate("INSERT INTO Agent(id, name) VALUES ('" + entries.get(i) + "', '" + entries.get(i + 1) + "');");
        }
        connection.commit();
        connection.close();
    }

    private List<Agent> getAgents(String databaseAddress) throws SQLException {
        Connection connection = DriverManager.getConnection(databaseAddress, "sa", "");

        List<Agent> resultList = new ArrayList<>();
        ResultSet rs = connection.createStatement().executeQuery("SELECT * FROM Agent;");
        while (rs.next()) {

            Agent a = new Agent();
            a.id = rs.getString("id");
            a.name = rs.getString("name");

            resultList.add(a);
        }
        
        rs.close();
        connection.close();

        return resultList;
    }

    private void removeFile(String databaseFilename) throws IOException {
        Files.deleteIfExists(Paths.get(databaseFilename + ".mv.db"));
        Files.deleteIfExists(Paths.get(databaseFilename + ".trace.db"));
    }

    private static class Agent {

        String id;
        String name;
    }
}
