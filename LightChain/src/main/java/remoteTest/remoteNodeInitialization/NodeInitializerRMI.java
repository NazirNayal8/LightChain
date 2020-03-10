package remoteTest.remoteNodeInitialization;

import java.rmi.Remote;
import java.rmi.RemoteException;

// A node initializer is a runnable jar file through which nodes can initialize one another
public interface NodeInitializerRMI extends Remote {
    public String getIntroducer(String myIp) throws RemoteException;
    public void reset() throws RemoteException;
}
