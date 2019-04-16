
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RMIInterface extends Remote {

	public String getLeftNode(int level) throws RemoteException ;
	public String getRightNode(int level) throws RemoteException ;
	public void setLeftNode(int level, NodeInfo newNode) throws RemoteException;
	public void setRightNode(int level,NodeInfo newNode) throws RemoteException;
	public String getNumID() throws RemoteException;
	public String getNameID() throws RemoteException;
	public String searchByNameID(String targetString) throws RemoteException, MalformedURLException, NotBoundException;
	public String searchByNumID(String targetNum) throws RemoteException, MalformedURLException, NotBoundException;
	public String searchName(String searchTarget,int level,int direction) throws RemoteException, MalformedURLException, NotBoundException;
	public String searchNum(String searchTarget,int level) throws RemoteException, MalformedURLException, NotBoundException;
}