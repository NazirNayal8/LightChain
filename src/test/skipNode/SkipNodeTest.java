package skipNode;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import org.assertj.core.api.JUnitSoftAssertions;


import org.junit.Test;
import org.junit.Rule;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.assertEquals;

import org.assertj.core.api.SoftAssertions;

public class SkipNodeTest {
    @Rule
    public final JUnitSoftAssertions softly = new JUnitSoftAssertions();
    private static final int PORT = 2039;
    private static final String serverPORT = "1999";

    @Test
    public void InsertTest() throws RemoteException {

        // Creating nodes, they should construct the following graph
        // with respect to the node 51
        // level 0:   null<-L --- R->null
        // level 1:   null<-L --- R->null
        // level 2:   null<-L --- R->59
        // level 3:   43<-L   --- R->59
        // level 4:   43<-L   --- R->59

        NodeInfo lookupTable1[][] = {
            { null,  null},
            { null,  null},
            { null , new NodeInfo("127.0.0.1:" + 1234, 59, "11110") },
            { new NodeInfo("127.0.0.1:" + 1234, 43,   "11000"), new NodeInfo("127.0.0.1:" + 1234, 59, "11110") },
            { new NodeInfo("127.0.0.1:" + 1234, 43,   "11000"), new NodeInfo("127.0.0.1:" + 1234, 59, "11110") },
        };




        SkipNode nodes1[] = {
            new SkipNode(new Configuration("127.0.0.1:1237", "00000", "27", "1234")),
            new SkipNode(new Configuration("127.0.0.1:1237", "10000", "35", "1235")),
            new SkipNode(new Configuration("127.0.0.1:1237", "11000", "43", "1236")),
            new SkipNode(new Configuration("none"          , "11100", "51", "1237")),
            new SkipNode(new Configuration("127.0.0.1:1237", "11110", "59", "1238")),
            new SkipNode(new Configuration("127.0.0.1:1237", "11111", "67", "1239")),
            new SkipNode(new Configuration("127.0.0.1:1237", "11011", "75", "1240")),
        };

        for (SkipNode n : nodes1) {
            if(n.getNumID() == 51) {
                continue;
            }
            n.insert();
        }

        for(int lvl = 0; lvl < 5; lvl++) {
            try {
                softly.assertThat(nodes1[3].lookup[lvl][0]).as("insert test at level: " + lvl).isEqualTo(lookupTable1[lvl][0]);
                softly.assertThat(nodes1[3].lookup[lvl][1]).as("insert test at level: " + lvl).isEqualTo(lookupTable1[lvl][1]);
            } catch (Exception e) {
                System.out.println("lol");
            }
        }

        // Creating nodes, they should construct the following graph
        // with respect to the node 51
        // level 0:   43<-L --- R->59
        // level 1:   43<-L --- R->59
        // level 2:   43<-L --- R->59
        // level 3: null<-L --- R->59
        // level 4: null<-L --- R->76
        // level 5: null<-L --- R->null

        NodeInfo lookupTable2[][] = {
            { new NodeInfo("127.0.0.1:" + 2237, 50,   "00011"), new NodeInfo("127.0.0.1:" + 2237, 52, "00010") },
            { new NodeInfo("127.0.0.1:" + 2237, 40,   "10000"), new NodeInfo("127.0.0.1:" + 2237, 80, "11111") },
            { new NodeInfo("127.0.0.1:" + 2237, 0,    "11000"), new NodeInfo("127.0.0.1:" + 2237, 80, "11111") },
            { null,                                             new NodeInfo("127.0.0.1:" + 2237, 80, "11111") },
            { null,                                             null},
        };


        SkipNode nodes2[] = {
            new SkipNode(new Configuration("127.0.0.1:2237", "00011", "50", "2234")),
            new SkipNode(new Configuration("127.0.0.1:2237", "10000", "40", "2235")),
            new SkipNode(new Configuration("127.0.0.1:2237", "11000", "0", "2236")),
            new SkipNode(new Configuration("none",           "11100", "51", "2237")),
            new SkipNode(new Configuration("127.0.0.1:2237", "11110", "100", "2238")),
            new SkipNode(new Configuration("127.0.0.1:2237", "11111", "80", "2239")),
            new SkipNode(new Configuration("127.0.0.1:2237", "00010", "52", "2240")),
        };

        for (SkipNode n : nodes2) {
            if(n.getNumID() == 51) {
                continue;
            }
            n.insert();
        }


        for(int lvl = 0; lvl < 5; lvl++) {
            try {
                softly.assertThat(nodes2[3].lookup[lvl][0]).as("insert test at level: " + lvl).isEqualTo(lookupTable2[lvl][0]);
                softly.assertThat(nodes2[3].lookup[lvl][1]).as("insert test at level: " + lvl).isEqualTo(lookupTable2[lvl][1]);
            } catch (Exception e) {
                System.out.println("lol");

            }
        }

        NodeInfo lookupTable3_59[][] = {
            { null, null},
            { null,                                            new NodeInfo("127.0.0.1:" + 1339, 67, "11111") },
            { new NodeInfo("127.0.0.1:" + 1337, 51, "11100"),  new NodeInfo("127.0.0.1:" + 1339, 67, "11111") },
            { new NodeInfo("127.0.0.1:" + 1337, 51, "11100"),  new NodeInfo("127.0.0.1:" + 1339, 67, "11111") },
            { new NodeInfo("127.0.0.1:" + 1337, 51, "11100"),  new NodeInfo("127.0.0.1:" + 1339, 67, "11111") },
        };

        NodeInfo lookupTable3_43[][] = {
            { null, null},
            { null, null},
            { null, new NodeInfo("127.0.0.1:" + 1340, 75, "11011") },
            { null, new NodeInfo("127.0.0.1:" + 1337, 51, "11100") },
            { new NodeInfo("127.0.0.1:" + 1335, 35, "10000"), new NodeInfo("127.0.0.1:" + 1337, 51, "11100") },
        };

        SkipNode nodes3[] = {
            new SkipNode(new Configuration("127.0.0.1:1338", "00000", "27", "1334")),
            new SkipNode(new Configuration("127.0.0.1:1336", "10000", "35", "1335")),
            new SkipNode(new Configuration("none",           "11000", "43", "1336")),
            new SkipNode(new Configuration("127.0.0.1:1336", "11100", "51", "1337")),
            new SkipNode(new Configuration("127.0.0.1:1336", "11110", "59", "1338")),
            new SkipNode(new Configuration("127.0.0.1:1338", "11111", "67", "1339")),
            new SkipNode(new Configuration("127.0.0.1:1338", "11011", "75", "1340")),
        };

        for (SkipNode n : nodes3) {
            if(n.getNumID() == 43) {
                continue;
            }
            n.insert();
        }

        for(int lvle = 0; lvle < 5; lvle++) {
            try {
                softly.assertThat(nodes3[2].lookup[lvle][0]).as("insert test at level: " + lvle).isEqualTo(lookupTable3_43[lvle][0]);
                softly.assertThat(nodes3[2].lookup[lvle][1]).as("insert test at level: " + lvle).isEqualTo(lookupTable3_43[lvle][1]);
                softly.assertThat(nodes3[4].lookup[lvle][0]).as("insert test at level: " + lvle).isEqualTo(lookupTable3_59[lvle][0]);
                softly.assertThat(nodes3[4].lookup[lvle][1]).as("insert test at level: " + lvle).isEqualTo(lookupTable3_59[lvle][1]);
            } catch (Exception e) {
                System.out.println("lol");
            }
        }

        // try {
        //     softly.assertAll();
        // } catch (SoftAssertionError e) {
        //     logAssertionErrorMessage("SoftAssertion errors example", e);
        // }
    }

    @Test
    public void SearchNumIDTest() throws RemoteException, MalformedURLException, NotBoundException {

        NodeInfo lookupTable1[][] = {
            { new NodeInfo("127.0.0.1:" + PORT, 13, "10101"), new NodeInfo("127.0.0.1:" + PORT, 16, "00101") },
            { new NodeInfo("127.0.0.1:" + PORT, 11, "01100"), new NodeInfo("127.0.0.1:" + PORT, 20, "01110") },
            { new NodeInfo("127.0.0.1:" + PORT, 12, "00001"), new NodeInfo("127.0.0.1:" + PORT, 19, "00101") },
            { new NodeInfo("127.0.0.1:" + PORT, 13, "00111"), new NodeInfo("127.0.0.1:" + PORT, 17, "00110") },
            { new NodeInfo("127.0.0.1:" + PORT, 10, "01011"), new NodeInfo("127.0.0.1:" + PORT, 31, "01001") }, };

        NodeInfo lookupTable2[][] = {
            { new NodeInfo("127.0.0.1:" + PORT, 14, "01100"), new NodeInfo("127.0.0.1:" + PORT, 16, "01110") },
            { new NodeInfo("127.0.0.1:" + PORT, 13, "00001"), new NodeInfo("127.0.0.1:" + PORT, 40, "00101") },
            { new NodeInfo("127.0.0.1:" + PORT, 11, "00111"), new NodeInfo("127.0.0.1:" + PORT, 27, "00110") },
            { new NodeInfo("127.0.0.1:" + PORT, 11, "10101"), new NodeInfo("127.0.0.1:" + PORT, 28, "00101") },
            { new NodeInfo("127.0.0.1:" + PORT, 10, "01011"), new NodeInfo("127.0.0.1:" + PORT, 31, "01001") }, };

        NodeInfo lookupTable3[][] = {};

        NodeInfo lookupTable4[][] = {
            { new NodeInfo("127.0.0.1:" + PORT, 5, "01011"), new NodeInfo("127.0.0.1:" + PORT, 50, "01001") }, };

        SkipNode node = new SkipNode(new Configuration("none", "01010", "15", serverPORT));

        RMIInterface server = (RMIInterface) Naming.lookup("//" + "127.0.0.1:" + serverPORT + "/RMIImpl");

        SearchTestCase tt1[] = {
            new SearchTestCase(40, 31, 4),
            new SearchTestCase(40, 17, 3),
            new SearchTestCase(40, 19, 2),
            new SearchTestCase(40, 20, 1),
            new SearchTestCase(40, 16, 0),
            new SearchTestCase(5,  10, 4),
            new SearchTestCase(5,  13, 3),
            new SearchTestCase(5,  12, 2),
            new SearchTestCase(5,  11, 1),
            new SearchTestCase(5,  13, 0),
            new SearchTestCase(12, 13, 4),
            new SearchTestCase(12, 13, 3),
            new SearchTestCase(12, 12, 2),
            new SearchTestCase(12, 13, 1),
            new SearchTestCase(12, 13, 0),
            new SearchTestCase(20, 17, 4),
            new SearchTestCase(11, 13, 4),
            new SearchTestCase(16, 16, 4),
        };

        SearchTestCase tt2[] = {
            new SearchTestCase(16, 16, 4),
            new SearchTestCase(17, 16, 4),
            new SearchTestCase(14, 14, 4),
            new SearchTestCase(14, 14, 3),
            new SearchTestCase(14, 14, 2),
            new SearchTestCase(14, 14, 1),
            new SearchTestCase(14, 14, 0),
        };

        SearchTestCase tt3[] = {
            new SearchTestCase(40, 15, 4),
            new SearchTestCase(0,  15, 4),
        };

        SearchTestCase tt4[] = {
            new SearchTestCase(60, 15, 4),
            new SearchTestCase(0,  5,  4),
        };

        MockSearchNode mock = new MockSearchNode(tt1, server);
        Registry reg = LocateRegistry.createRegistry(PORT);
        reg.rebind("RMIImpl", mock);

        mock.setTT(tt1);
        node.setLookupTable(lookupTable1);
        mock.runTests();
        node.setLookupTable(lookupTable2);
        mock.setTT(tt2);
        mock.runTests();
        node.setLookupTable(lookupTable3);
        mock.setTT(tt3);
        node.setLookupTable(lookupTable4);
        mock.setTT(tt4);
    }

    // public static void main(String args[]) {
    //     try {
    //         SearchNumIDTest();
    //     	InsertTest();
    //     } catch (Exception e) {
    //         e.printStackTrace();
    //     }
    // }
}
