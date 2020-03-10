package remoteTest.remoteNodeInitialization;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class NodeInitializer extends UnicastRemoteObject implements NodeInitializerRMI{
//    ReadWriteLock lock;

    String ip;

    public NodeInitializer() throws RemoteException {
        super();
//        lock = new ReentrantReadWriteLock(true);
    }

    @Override
    public synchronized String getIntroducer(String myIp) throws RemoteException {
        try{
            Thread.sleep(6000);
            System.out.println("Received get request");
        }catch (Exception e){}
        if(ip==null){
            ip=myIp;
            System.out.println("Set IP to " + ip);
            return "First";
        }else{
            return ip;
        }
    }

    @Override
    public void reset() throws RemoteException {
        System.out.println("Received reset request");
        ip=null;
    }
}
