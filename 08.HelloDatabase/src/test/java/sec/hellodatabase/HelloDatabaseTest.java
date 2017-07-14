package sec.hellodatabase;

import fi.helsinki.cs.tmc.edutestutils.MockStdio;
import fi.helsinki.cs.tmc.edutestutils.Points;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

@Points("S2.02")
public class HelloDatabaseTest {

    @Rule
    public MockStdio io = new MockStdio();

    private String databaseFile;
    private String databaseAddress;

    private List<String> agents;

    @Before
    public void setup() {
        this.databaseFile = "db-" + UUID.randomUUID().toString().substring(0, 6);
        this.databaseAddress = "jdbc:h2:file:./" + this.databaseFile;

        this.agents = new ArrayList<>(Arrays.asList("HelloWrld", " Brian Kernighan"));

    }

    @Test
    public void testSingleAgent() throws Throwable {
        initDatabase(databaseAddress, agents);
        HelloDatabase.main(new String[]{databaseAddress});
        removeFile(databaseFile);

        long count = Arrays.stream(io.getSysOut().split("\\r?\\n")).filter(l -> l.contains(agents.get(0)) && l.contains(agents.get(1))).count();
        assertFalse("Agents in database not found in output. Expected agent id " + agents.get(0) + " and name " + agents.get(1), count == 0);
        assertFalse("Each agent in database should be printed just once. Now an agent in database was printed multiple times: Agent id " + agents.get(0) + ", name " + agents.get(1), count > 1);
    }

    @Test
    public void testMultipleAgents() throws Throwable {
        Random rnd = new Random();
        for (int i = 0; i < rnd.nextInt(5) + 5; i++) {
            String randomString = UUID.randomUUID().toString();
            this.agents.add(randomString.substring(0, 6));
            this.agents.add(randomString.substring(8, 16));
        }

        initDatabase(databaseAddress, agents);
        HelloDatabase.main(new String[]{databaseAddress});
        removeFile(databaseFile);

        long count = Arrays.stream(io.getSysOut().split("\\r?\\n")).filter(l -> {
            for (int i = 0; i < this.agents.size() - 1; i += 2) {
                String id = this.agents.get(i);
                String name = this.agents.get(i + 1);

                if (l.contains(id) && l.contains(name)) {
                    return true;
                }
            }

            return false;
        }).count();

        assertEquals("When the database has " + this.agents.size() / 2 + " agents, all of them should be printed once.", this.agents.size() / 2, count);
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

    private void removeFile(String databaseFilename) throws IOException {
        Files.deleteIfExists(Paths.get(databaseFilename + ".mv.db"));
        Files.deleteIfExists(Paths.get(databaseFilename + ".trace.db"));
    }
}
