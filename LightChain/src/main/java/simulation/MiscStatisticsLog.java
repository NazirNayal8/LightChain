package simulation;

import java.io.Serializable;

public class MiscStatisticsLog implements Serializable {
    // number of transaction validation Attempts
    private int transactionValidationAttempts;
    // total time spent on transaction validation attempts
    private long transactionValidationTime;
    // number of successful attempts to collect transactions into a block
    private int numTransactionCollection;
    // total time spent collecting transactions to blocks
    private long transactionCollectionTime;
    // number of block validation Attempts
    private int blockValidationAttempts;
    // total time spent on block validation attempts
    private long blockValidationTime;

    public MiscStatisticsLog() {
        transactionValidationAttempts=0;
        transactionValidationTime=0;
        numTransactionCollection=0;
        transactionCollectionTime=0;
        blockValidationAttempts=0;
        blockValidationTime=0;
    }

    public void logTransactionValidationAttempt(long time){
        transactionValidationAttempts++;
        transactionValidationTime+=time;
    }

    public void logBlockValidationAttempt(long time){
        blockValidationAttempts++;
        blockValidationTime+=time;
    }

    public void logTransactionCollectionAttempt(long time){
        numTransactionCollection++;
        transactionCollectionTime+=time;
    }

    public int getTransactionValidationAttempts() {
        return transactionValidationAttempts;
    }

    public long getTransactionValidationTime() {
        return transactionValidationTime;
    }

    public int getNumTransactionCollection() {
        return numTransactionCollection;
    }

    public long getTransactionCollectionTime() {
        return transactionCollectionTime;
    }

    public int getBlockValidationAttempts() {
        return blockValidationAttempts;
    }

    public long getBlockValidationTime() {
        return blockValidationTime;
    }
}
