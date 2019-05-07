public class SkipNodeTest {

    private final String PORT = "1234";
    private final String serverPORT = "1999";

    public void SearchNumIDTest() {
        // TODO: populate lookup Table
        NodeInfo lookupTable[][] =
        {
            { new NodeInfo("127.0.0.1:"+PORT, "10011", "my")    , new NodeInfo("127.0.0.1:"+PORT, "00100", "Oh") },
            { new NodeInfo("127.0.0.1:"+PORT, "10111", "name")  , new NodeInfo("127.0.0.1:"+PORT, "10100", "my") },
            { new NodeInfo("127.0.0.1:"+PORT, "11011", "is")    , new NodeInfo("127.0.0.1:"+PORT, "01100", "man") },
            { new NodeInfo("127.0.0.1:"+PORT, "00111", "jeff")  , new NodeInfo("127.0.0.1:"+PORT, "00110", "what") },
            { new NodeInfo("127.0.0.1:"+PORT, "10101", "hello") , new NodeInfo("127.0.0.1:"+PORT, "00101", "did") },
            { new NodeInfo("127.0.0.1:"+PORT, "10001", "man")   , new NodeInfo("127.0.0.1:"+PORT, "01101", "you") },
        };


        class TestCase {
            public int     numID;
            public int     level;
            public String  output;

            public TestCase(int numID, int level, String output) {
                this.numID  = numID;
                this.level  = level;
                this.output = output;
            }
        }


        // Initialize node with the given lookupTable
        // TODO: Complete the iniialization. Make sure there is a way to pass
        // the lookup Table to "node"
        SkipNode node = new SkipNode();
        node.setLookupTable(lookupTable);

        SkipNode server = (SkipNode) Naming.lookup("rmi://127.0.0.1:"+serverPORT);

        // TODO: populate test table
        TestCase tt[] = {
            new TestCase("10111", 5, "10011"),
            new TestCase("10111", 5, "10011"),
            new TestCase("10111", 5, "10011"),
            new TestCase("10111", 5, "10011"),
            new TestCase("10111", 5, "10011"),
            new TestCase("10111", 5, "10011"),
            new TestCase("10111", 5, "10011"),
        };


        for(TestCase tc : tt) {
            Assert(tc.ouput, server.searchNum(tc.numID, tc.level));
        }
    }
}
