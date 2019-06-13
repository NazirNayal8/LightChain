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

