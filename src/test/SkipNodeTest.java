public class SkipNodeTest {
    public void SearchNumIDTest() {


        // TODO: populate lookupTable
        NodeInfo[][] lookupTable = new NodeInfo[][] {
            { new NodeInfo(), new NodeInfo() },
                { new NodeInfo(), new NodeInfo() },
                { new NodeInfo(), new NodeInfo() },
                { new NodeInfo(), new NodeInfo() },
                { new NodeInfo(), new NodeInfo() },
                { new NodeInfo(), new NodeInfo() },
        };

        class TestCase {
            public int     numID;
            public int     level;
            public String  output;

            public TestCase(int numID, int level, String output) {
                this.numID = numID;
                this.level = level;
                this.output = output;
            }
        }


        // Initialize node with the given lookupTable
        SkipNode node = new SkipNode();


        // TODO: populate test table
        TestCase tt[] = new TestCase[]{
                new TestCase(),
                new TestCase(),
                new TestCase(),
                new TestCase(),
                new TestCase(),
                new TestCase(),
                new TestCase(),
        };


        for(TestCase tc : tt) {
            Assert(tc.ouput, node.searchNum(tc.numID, tc.level));
        }
    }
}
