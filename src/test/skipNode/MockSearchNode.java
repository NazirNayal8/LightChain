package skipNode;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import  org.junit.Test;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.assertEquals;


class MockSearchNode extends UnicastRemoteObject implements RMIInterface {

    private static final long serialVersionUID = 1L;
    int counter = 0;
    SearchTestCase tt[];
    RMIInterface server;

    public void setTT(SearchTestCase tt[]) {
        this.tt = tt;
        counter = 0;
        System.out.println("=== TEST ===");
    }

    public MockSearchNode(SearchTestCase tt[], RMIInterface server) throws RemoteException {
        this.tt = tt;
        this.server = server;
    }

    @Override
    @Test
    public String searchNum(int searchTarget, int level, int numIDOfNode) throws RemoteException {
        assertTrue(tt[counter++].returnedNumID == numIDOfNode);
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
        for (SearchTestCase tc : tt) {
            try {
                server.searchNum(tc.targetNumID, tc.level, 15);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }
}
