public class SkipNodeTest {

    public void SearchNumIDTest() {
        // TODO: populate lookup Table
        NodeInfo lookupTable[][] =
        {
            { new NodeInfo(/* add */), new NodeInfo(/* add */) },
            { new NodeInfo(/* add */), new NodeInfo(/* add */) },
            { new NodeInfo(/* add */), new NodeInfo(/* add */) },
            { new NodeInfo(/* add */), new NodeInfo(/* add */) },
            { new NodeInfo(/* add */), new NodeInfo(/* add */) },
            { new NodeInfo(/* add */), new NodeInfo(/* add */) },
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

        // TODO: populate test table
        TestCase tt[] = {
            new TestCase(/* add */),
            new TestCase(/* add */),
            new TestCase(/* add */),
            new TestCase(/* add */),
            new TestCase(/* add */),
            new TestCase(/* add */),
            new TestCase(/* add */),
        };


        for(TestCase tc : tt) {
            Assert(tc.ouput, node.searchNum(tc.numID, tc.level));
        }
    }
}
