package skipGraph;

import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Shadi Hamdan
 *
 */

public class lookupTable {
	public int maxLevels;
	private ConcurrentHashMap<Integer, NodeInfo> dataNodes;
	private HashMap<Integer, Table> lookup;
	public static final int LEFT = 0;
	public static final int RIGHT = 1;
	
	
	/*
	 * The buffer is there so we can finalize a node's table and insertion before we add it to the other nodes. This prevents any access to it during search etc.
	 */
	private NodeInfo nodeBuffer;
	private  Table tableBuffer;


	
	public lookupTable(int maxLevels) {
		this.maxLevels = maxLevels;
		this.dataNodes = new ConcurrentHashMap<>();
		this.lookup = new HashMap<>();
	}
	
	public int size() {
		return dataNodes.size();
	}
	
	public Set<Integer> keySet(){
		return lookup.keySet();
	}
	
	
	/**
	 * Adds a node to the data nodes.
	 * 
	 * @param nd The node that is to be added the lookup
	 * @return false if the numID given was added previously, true otherwise.
	 */
	public boolean addNode(NodeInfo nd) {
		NodeInfo ret = dataNodes.put(nd.getNumID(), nd);
		if (ret == null) {
			lookup.put(nd.getNumID(), new Table());
		}
		return ret == null;
	}
	
	/**
	 * Adds the node to the buffer. This allows the user to finish finalising the node's lookup table before making it accessible.
	 * This also makes the node inaccessible from getBestNum and getBestName. Once the node is finalised, you can use {@link lookupTable#finalizeNode()} 
	 * to commit the node to the lookup table.
	 * 
	 * @param node The NodeInfo of the node you want to add tto the buffer.
	 */
	public void initializeNode(NodeInfo node) {
		nodeBuffer = node;
		tableBuffer = new Table();
	}
	
	/**
	 * Commits the node in buffer to the lookup table. 
	 * @return Returns true if the node was committed properly. Returns false if the node was not initialised properly
	 * and thus not committed.
	 */
	public boolean finalizeNode() {
		if(this.nodeBuffer==null || this.tableBuffer == null) return false;
		else {
			dataNodes.put(nodeBuffer.getNumID(), nodeBuffer);
			lookup.put(nodeBuffer.getNumID(), tableBuffer);
			nodeBuffer = null;
			tableBuffer = null;
			return true;
		}
	}
	
	/**
	 * Removes all references to the node with the given numID
	 * @param numID the numID of the node you want to remove
	 * @return the stored node info of the given numID.
	 */
	public NodeInfo remove(int numID) {
		lookup.remove(numID);
		return dataNodes.remove(numID);
	}
	
	/**
	 * Gets the information of the node with the given numID 
	 * 
	 * @param numID the numID of the node you want to remove
	 * @return the stored node info of the given numID
	 */
	public NodeInfo get(int numID) {
		return dataNodes.get(numID);
	}

	
	/**
	 * Get the neighbor of the node with the given numID at the given level and direction.
	 * 
	 * @param numID     The numID of the node that you want to check the neighbour
	 *                  of
	 * @param level     The level on the lookup table
	 * @param direction The direction (lookupTable.RIGHT or lookupTable.LEFT)
	 * @return The information of the desired neighbour or null if the numID is invalid
	 */
	public NodeInfo get(int numID, int level, int direction) {
		if(!dataNodes.contains(numID)) return null;
		return lookup.get(numID).get(level, direction);
	}
	
	/**
	 * Put the given newNode as a neighbor of the node with the given numID at the given level and direction if the node in place is the given expectedOldNode
	 * 
	 * @param                 numID The numID of the node that you want to check
	 *                        the neighbour of
	 * @param                 level The level on the lookup table
	 * @param                 direction The direction (lookupTable.RIGHT or
	 *                        lookupTable.LEFT)
	 * @param newNode		  The node that you want in this location
	 * @param expectedOldNode The node that you think is in this location (This is to ensure that the lookup has not been modified since you last used "get()"
	 * @return				  Returns true if the node was placed properly and the expectedOldNode was what it replaced. False and the lookup is not modified otherwise.
	 */
	public boolean put(int numID, int level, int direction, NodeInfo newNode, NodeInfo expectedOldNode) {
		if(nodeBuffer != null && nodeBuffer.getNumID()==numID) {
			return tableBuffer.safePut(level, direction, newNode, expectedOldNode);
		}
		if(!lookup.containsKey(numID)) return false;
		return lookup.get(numID).safePut(level, direction, newNode, expectedOldNode);
	}
	
	/**
	 * Returns the data node with the numID that is closest to the current node.
	 * 
	 * @param numID  The numID you are looking for
	 * @return The numID of the node that is closest to the argument.
	 */
	public int getBestNum(int numID) {
		long bestDif = Long.MAX_VALUE;
		int bestNum = -1;
		for (int cur : dataNodes.keySet()) {
			int dif = Math.abs(numID - cur);
			if (dif < bestDif) {
				bestDif = dif;
				bestNum = cur;
			}
		}
		return bestNum;
	}
	
	/*
	 * This method receives a nameID and returns the index of the data node which has
	 * the most common prefix with the given nameID
	 */
	public int getBestName(String name,int direction) {
		try {
			int best = -1;
			int num = -1;
			for (int cur : dataNodes.keySet()) {
				if(num == -1) num = cur;
				int tmp = commonBits(name, dataNodes.get(cur).getNameID());
				if(tmp > best) {
					best = tmp;
					num = cur;
				}
			}
			for(int cur : dataNodes.keySet()) {
				int bits = commonBits(name,dataNodes.get(cur).getNameID());
				if(bits == best) {
					if(direction == RIGHT) {
						if(dataNodes.get(cur).getNumID() > num) {
							num = dataNodes.get(cur).getNumID();
						}
					}else if(dataNodes.get(cur).getNumID() < num) {
							num = dataNodes.get(cur).getNumID();
					}
				}
			}
			return num;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	private static int commonBits(String name1, String name2) {
		if(name1 == null || name2 == null) {
			return -1;
		}
		if(name1.length() != name2.length())
			return -1;
		int i = 0;
		for(i = 0; i < name1.length() && name1.charAt(i) == name2.charAt(i) ; ++i);
			return i;
		}
	
	class Table{
		private ConcurrentHashMap<Integer, NodeInfo> table;
		
		public Table() {
			table = new ConcurrentHashMap<Integer, NodeInfo>();
		}
		
		public NodeInfo get(int level, int direction) {
			if(!validate(level,direction)) return null;
			return table.get(getInd(level,direction));
		}
		
		private NodeInfo put(int level, int direction, NodeInfo newNode) {
			if(!validate(level,direction)) return null;
			if(newNode == null) return remove(level, direction);
			NodeInfo res = table.put(getInd(level,direction), newNode);
			return res;
		}
		
		private NodeInfo remove(int level, int direction) {
			return table.remove(getInd(level, direction));
		}
		
		//TODO: see if we can get rid of expectedOldNode==null
		public boolean safePut(int level, int direction, NodeInfo newNode, NodeInfo expectedOldNode) {
			//synchronized (this) {
				NodeInfo cur = put(level,direction,newNode);
				if(expectedOldNode==null || equal(cur,expectedOldNode)) return true;
				else {
					put(level,direction,cur);
					System.exit(0);
					return false;
				}
			//}
		}
		
		private boolean equal(NodeInfo nodeA, NodeInfo nodeB) {
			if(nodeA == null && nodeB == null) {
				return true;
			}else if(nodeA==null || nodeB == null) return false;
			
			return nodeA.equals(nodeB);
		}
		
		private int getInd(int level, int direction) {
			return 2*level+direction;
		}
		
		private boolean validate(int level, int direction) {
			return validateLevel(level) && validateDir(direction);
		}
		
		private boolean validateLevel(int level) {
			return level >=0 && level < maxLevels;
		}
		
		private boolean validateDir(int direction) {
			return direction == 1 || direction == 0;
		}
	}
}
