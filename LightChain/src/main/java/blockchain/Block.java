package blockchain;

import java.util.ArrayList;
import java.util.List;

import hashing.Hasher;
import hashing.HashingTools;
import signature.SignedBytes;
import skipGraph.NodeInfo;
import skipGraph.SkipNode;
import util.Const;

public class Block extends NodeInfo {

	private static final long serialVersionUID = 1L;
	private final String prev;
	private final int owner;
	private List<Transaction> transactionSet;
	private final String hash;
	private List<SignedBytes> sigma;
	private Hasher hasher;
	private final int index;
	private int levels;

	/**
	 * @param prev the address of the previous block
	 * @param owner the address of the owner of the block
	 */
	public Block(String prev, int owner, String address, int idx,int levels) {
		super(address, 0, prev);
		this.index = idx;
		this.prev = prev;
		this.owner = owner;
		this.transactionSet = new ArrayList<>();
		this.sigma = new ArrayList<>();
		hasher = new HashingTools();
		this.hash = hasher.getHash(prev + owner, levels);
		super.setNumID(Integer.parseInt(this.hash, 2));
	}

	public Block(String prev, int owner, String address, List<Transaction> tList, int idx, int levels) {
		super(address, 0, prev);
		this.index = idx;
		this.prev = prev;
		this.owner = owner;
		this.transactionSet = tList;
		this.levels = levels;
		this.sigma = new ArrayList<>();
		hasher = new HashingTools();
		this.hash = hasher.getHash(prev + owner + getTransactionSetString(), levels);
		super.setNumID(Integer.parseInt(this.hash, 2));
	}

	public Block(Block blk) {
		super(blk.getAddress(), blk.getNumID(), blk.getNameID());
		hasher = new HashingTools();
		this.index = blk.getIndex();
		this.prev = blk.getPrev();
		this.owner = blk.getOwner();
		this.transactionSet = blk.getTransactionSet();
		this.hash = blk.getHash();
		this.sigma = blk.getSigma();
		this.levels = blk.getLevels();
	}

	public String getPrev() {
		return prev;
	}

	public int getOwner() {
		return owner;
	}

	public List<Transaction> getTransactionSet() {
		return transactionSet;
	}

	public String getHash() {
		return hash;
	}

	public List<SignedBytes> getSigma() {
		return sigma;
	}

	public void addSignature(SignedBytes signature) {
		sigma.add(signature);
	}

	public void addTransactions(List<Transaction> tList) {
		transactionSet = tList;
	}

	public int getIndex() {
		return index;
	}
	
	public int getLevels() {
		return levels;
	}
	
	public String toString() {
		
		return prev + owner + getTransactionSetString();
	}

	private String getTransactionSetString() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < transactionSet.size(); ++i)
			sb.append(transactionSet.get(i).toString());
		return sb.toString();
	}


	// returns the size of the object in bytes
	public long getSize(){
		long size = 0;
		// Size of prev (each character is 2 bytes)
		size+=prev.length() * 2;
		size+=4;//Owner size
		for(Transaction trx : transactionSet){
			size+=trx.getSize();
		}
		size+=hash.length()*2;
		for(SignedBytes sig : sigma){
			size+=sig.getSize();
		}
		//numID size
		size+=4;
		return size;
	}
	
}
