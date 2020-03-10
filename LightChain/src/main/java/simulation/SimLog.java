package simulation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SimLog implements Serializable {

	private boolean mode;
	private List<TransactionLog> validTransactions;
	private List<TransactionLog> failedTransactions;
	private List<MineAttemptLog> validMineLog;
	private List<MineAttemptLog> failedMineLog;
	private MiscStatisticsLog miscLog;

	public SimLog(boolean mode) {
		this.mode = mode;
		validTransactions = new ArrayList<>();
		failedTransactions = new ArrayList<>();
		validMineLog = new ArrayList<>();
		failedMineLog = new ArrayList<>();
		miscLog = new MiscStatisticsLog();
	}

	public void logTransaction(boolean success, int isAuthenticated, int isSound, int isCorrect, int hasBalance,
			long time,long timePerValidator,long size) {
		TransactionLog log = new TransactionLog(success, isAuthenticated, isSound, isCorrect, hasBalance, time,timePerValidator,size);
		if (success)
			validTransactions.add(log);
		else
			failedTransactions.add(log);
	}

	public void logMineAttemptLog(boolean foundTxMin, boolean success, long totalTime, long validationTime, long size) {
		MineAttemptLog log = new MineAttemptLog(foundTxMin, success, totalTime, validationTime,size);
		if(success)
			validMineLog.add(log);
		else
			failedMineLog.add(log);
	}

	public void logTransactionCollectionTime(long transactionCollectionTime){
		miscLog.logTransactionCollectionAttempt(transactionCollectionTime);
	}

	public void logTransactionValidationAttempt(long transactionValidationTime){
		miscLog.logTransactionValidationAttempt(transactionValidationTime);
	}

	public void logBlockValidationAttempt(long blockValidationTime){
		miscLog.logBlockValidationAttempt(blockValidationTime);
	}

	public List<TransactionLog> getValidTransactions() {
		return validTransactions;
	}

	public List<TransactionLog> getFailedTransactions(){
		return failedTransactions;
	}
	
	public List<MineAttemptLog> getValidMineAttemptLog(){
		return validMineLog;
	}
	
	public List<MineAttemptLog> getFailedMineAttemptLog(){
		return failedMineLog;
	}

	public MiscStatisticsLog getMiscLog(){return miscLog;}
	
	public boolean getMode() {
		return mode;
	}
	
	public int getTotalTransactionTrials() {
		return validTransactions.size() + failedTransactions.size();
	}
	
	public int getValidTransactionTrials() {
		return validTransactions.size();
	}
	
	public int getFailedTransactionTrials() {
		return failedTransactions.size();
	}
	
	public int getTotalMineTrials() {
		return validMineLog.size() + failedMineLog.size();
	}
	
	public int getValidMineTrials() {
		return validMineLog.size();
	}
	
	public int getFailedMineTrials() {
		return failedMineLog.size();
	}
	
	

}
