import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class SkipNodeTest {

    private static final int PORT = 2039;
    private static final int serverPORT = 1999;

    public static void SearchNumIDTest() throws Exception {

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

        SkipNode node = new SkipNode("01010", 15, serverPORT);

        RMIInterface server = (RMIInterface) Naming.lookup("//" + "127.0.0.1:" + serverPORT + "/RMIImpl");

        TestCase tt1[] = {
            new TestCase(40, 31, 4),
            new TestCase(40, 17, 3),
            new TestCase(40, 19, 2),
            new TestCase(40, 20, 1),
            new TestCase(40, 16, 0),
            new TestCase(5,  10, 4),
            new TestCase(5,  13, 3),
            new TestCase(5,  12, 2),
            new TestCase(5,  11, 1),
            new TestCase(5,  13, 0),
            new TestCase(12, 13, 4),
            new TestCase(12, 13, 3),
            new TestCase(12, 12, 2),
            new TestCase(12, 13, 1),
            new TestCase(12, 13, 0),
            new TestCase(20, 17, 4),
            new TestCase(11, 13, 4),
            new TestCase(16, 16, 4),
        };

        TestCase tt2[] = {
            new TestCase(16, 16, 4),
            new TestCase(17, 16, 4),
            new TestCase(14, 14, 4),
            new TestCase(14, 14, 3),
            new TestCase(14, 14, 2),
            new TestCase(14, 14, 1),
            new TestCase(14, 14, 0),
        };

        TestCase tt3[] = {
            new TestCase(40, 15, 4),
            new TestCase(0,  15, 4),
        };

        TestCase tt4[] = {
            new TestCase(60, 15, 4),
            new TestCase(0,  5,  4),
        };

        mockNode mock = new mockNode(tt1, server);
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

    public static void main(String args[]) {
        try {
            SearchNumIDTest();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}

class mockNode extends UnicastRemoteObject implements RMIInterface {

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED   = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    int counter = 0;
    TestCase tt[];
    RMIInterface server;

    public void setTT(TestCase tt[]) {
        this.tt = tt;
        counter = 0;
        System.out.println("=== TEST ===");
    }

    public mockNode(TestCase tt[], RMIInterface server) throws RemoteException {
        this.tt = tt;
        this.server = server;
    }

    @Override
    public String searchNum(int searchTarget, int level, int numIDOfNode) throws RemoteException {
        if (tt[counter++].returnedNumID != numIDOfNode) {
            System.err.println(ANSI_RED+"Error" + ANSI_RESET + ":   Expected: " + tt[counter - 1].returnedNumID + ". Got: " + numIDOfNode);
        } else {
            System.out.println(ANSI_GREEN + "Success"+ ANSI_RESET+ ": Test: " + tt[counter - 1].targetNumID +", at level: " + tt[counter-1].level +". Got: " + numIDOfNode);

        }
        return null;
    }

    @Override
    public String getLeftNode(int level) throws RemoteException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getRightNode(int level) throws RemoteException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setLeftNode(int level, NodeInfo newNode) throws RemoteException {
        // TODO Auto-generated method stub

    }

    @Override
    public void setRightNode(int level, NodeInfo newNode) throws RemoteException {
        // TODO Auto-generated method stub

    }

    @Override
    public int getNumID() throws RemoteException {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public String getNameID() throws RemoteException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String searchByNameID(String targetString) throws RemoteException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String searchByNumID(int targetNum) throws RemoteException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String searchName(String searchTarget, int level, int direction) throws RemoteException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setLookupTable(NodeInfo[][] newLookupTable) throws RemoteException {
        // TODO Auto-generated method stub

    }

    public void runTests() {
        for (TestCase tc : tt) {
            try {
                server.searchNum(tc.targetNumID, tc.level, 15);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }
}

class TestCase {
    public int targetNumID;
    public int returnedNumID;
    public int level;


    public TestCase(int targetNumID, int returnedNumId, int level) {
        this.targetNumID = targetNumID;
        this.returnedNumID = returnedNumId;
        this.level = level;
    }
}
