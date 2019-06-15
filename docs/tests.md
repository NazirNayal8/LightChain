# # Documentation
## # SkipNode Tests
There are a couple of tests which are included in the "testing" branch

### # SearchByNumID Tests

The test cases are constructed using a fake Lookup table which will be given to
a test node. Then a listener is used to monitor all the RMI calls made by
the test node, and compare it to a given test table.

For example, a lookup table would look like this

```java
NodeInfo lookupTable[][] = {
  { new NodeInfo(mockURL, 13, "10101"), new NodeInfo(mockURL, 16, "00101") },
  { new NodeInfo(mockURL, 11, "01100"), new NodeInfo(mockURL, 20, "01110") },
  { new NodeInfo(mockURL, 12, "00001"), new NodeInfo(mockURL, 19, "00101") },
  { new NodeInfo(mockURL, 13, "00111"), new NodeInfo(mockURL, 17, "00110") },
  { new NodeInfo(mockURL, 10, "01011"), new NodeInfo(mockURL, 31, "01001") },
};
```

The `mockURL` is a variable containing the URL of the listener which will be
used to monitor the RMI calls made by the test node.
For this given lookupTable, writing a test table from the `TestCase` class is trivial:

```java
TestCase tt[] = {
    //           targetNumID,  expected Return value, level
    new TestCase(40,           31,                    4),
    new TestCase(5,            10,                    4),
    ...
};
```


Finally, this test table is given to an instant of mockNode, and then running the
tests is a matter of calling the `runTests` method of the mockNode:

```java
 mock.setTT(tt);
 mock.runTests();
```

### # InsertNode Tests

To test node insertion, one can construct a cluster of nodes, each with a known
numID and URL, connected them to one introducer, and call the insert method on
them. This will result in the nodes using the  RMI protocol asking the introducer
to add them to the skipGraph. After the insertion of all the nodes is complete.
By comparing the introducer's lookup table with the expected lookup table, we can
insure that the method is working correctly.

Example:
```java
NodeInfo lookupTable[][] = {
//                       node URL,      port, numID, nameID                 node URL,      port, numID, nameID
          { new NodeInfo("127.0.0.1:" + 1234, 43,    "11000"), new NodeInfo("127.0.0.1:" + 1234, 59,    "11110") },
          { new NodeInfo("127.0.0.1:" + 1234, 43,    "11000"), new NodeInfo("127.0.0.1:" + 1234, 59,    "11110") },
          { new NodeInfo("127.0.0.1:" + 1234, 43,    "11000"), new NodeInfo("127.0.0.1:" + 1234, 59,    "11110") },
          { null,                                              new NodeInfo("127.0.0.1:" + 1234, 59,    "11110") },
          { null,                                              new NodeInfo("127.0.0.1:" + 1234, 75,    "11011") },
      };
```

Notice that all the nodes have the same introducer. However there can be other
bugs when there is a tree of introducer, so other configurations should be tested
aswell.

After constructing the expected lookup table, write an array of nodes that should
theoretically generate this lookup table.

```java
//                                    introducer,       nameID, numID, port
SkipNode nodes1[] = {
      new SkipNode(new Configuration("none",           "00000", "27",  "1234")),
      new SkipNode(new Configuration("127.0.0.1:1234", "10000", "35",  "1235")),
      new SkipNode(new Configuration("127.0.0.1:1234", "11000", "43",  "1236")),
      new SkipNode(new Configuration("127.0.0.1:1234", "11100", "51",  "1237")),
      new SkipNode(new Configuration("127.0.0.1:1234", "11110", "59",  "1238")),
      new SkipNode(new Configuration("127.0.0.1:1234", "11111", "67",  "1239")),
      new SkipNode(new Configuration("127.0.0.1:1234", "11011", "75",  "1240")),
  };
```

Now looping over this array and calling the insert() method should generate the
expected lookup table in the introducer node.


Checking that is done by looping over the lookup Table and comparing it with the
expected lookup Table and reporting the results.
