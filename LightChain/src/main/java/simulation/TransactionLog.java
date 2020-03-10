package simulation;

import java.io.Serializable;

import static util.Util.bytesToKilobytes;

class TransactionLog implements Serializable {
	private static final long serialVersionUID = 1L;

	private boolean success;
	private int isAuthenticated;
	private int isSound;
	private int isCorrect;
	private int hasBalance;
	private long timeTaken;
	private long timePerValidator;
	private long size;

	public TransactionLog(boolean success, int isAuthenticated, int isSound, int isCorrect, int hasBalance,
			long timeTaken, long timePerValidator, long size) {
		this.success = success;
		this.isAuthenticated = isAuthenticated;
		this.isSound = isSound;
		this.isCorrect = isCorrect;
		this.hasBalance = hasBalance;
		this.timeTaken = timeTaken;
		this.timePerValidator = timePerValidator;
		this.size = size;
	}

	public boolean isSuccessful() {
		return success;
	}

	public long timeTaken() {
		return timeTaken;
	}

	public long getSize(){return size;}

	@Override
	public String toString() {
		return timeTaken + "," + isAuthenticated + "," + isSound + "," + isCorrect + "," + hasBalance + "," + success
				+ "," + timePerValidator + "," + bytesToKilobytes(size) + "\n";
	}
}
