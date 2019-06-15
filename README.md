# Skip-Node-with-RMI

## Configuration:
In your working directory, create a "node.conf" file with the following fields:

```
introducer=none
nameID=101
numID=213
port=3213
```

When the SkipGraph node is started, it will read this file.

---
Run:
```bash
cd src/
javac *.java

# Create your node.conf file

java SkipGraph
```
