package simulation;

import blockchain.LightChainNode;
import blockchain.Parameters;
import blockchain.Transaction;
import skipGraph.NodeInfo;
import util.Const;
import util.Util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

import static util.Util.bytesToKilobytes;

public class Simulation {

	public static void startSimulation(Parameters params, int nodeCount ,int iterations, int pace){
		try {
			Random rnd = new Random();
			ArrayList<LightChainNode> nodes = new ArrayList<>();
			LightChainNode initialNode = null;
			int numFailures;
			for(int i=0;i<nodeCount;i++){
				try{
					int port = rnd.nextInt(65535);
					LightChainNode node;
					if(i==0){
						node = new LightChainNode(params, port, Const.DUMMY_INTRODUCER, true);
						initialNode = node;
					}else{
						node = new LightChainNode(params, port, initialNode.getAddress(), false);
					}
					nodes.add(node);
				}catch(RemoteException re){
					i--;
					continue;
				}
			}

			initialNode.insertGenesis();

			ConcurrentHashMap<NodeInfo, SimLog> map = new ConcurrentHashMap<>();
			CountDownLatch latch = new CountDownLatch(nodes.size());
			long startTime = System.currentTimeMillis();
			for (int i = 0; i < nodes.size(); ++i) {
				SimThread sim = new SimThread(nodes.get(i), latch, map, iterations, pace);
				sim.start();
			}
			latch.await();
			
			initialNode.printLevel(0);
			
			long endTime = System.currentTimeMillis();

			Util.log("Simulation Done. Time Taken " +(endTime - startTime)+ " ms");
			
			processData(map, iterations);

		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}


	public static void processData(ConcurrentHashMap<NodeInfo, SimLog> map,int iterations) {
		processTransactions(map, iterations);
		processMineAttempts(map, iterations);
		processMiscStats(map);
	}

	private static void processMineAttempts(ConcurrentHashMap<NodeInfo, SimLog> map,int iterations) {

		try {
			String logPath = System.getProperty("user.dir") + File.separator + "Logs" + File.separator
					+ "MineAttempts.csv";
			File logFile = new File(logPath);

			logFile.getParentFile().mkdirs();
			PrintWriter writer;

			writer = new PrintWriter(logFile);

			StringBuilder sb = new StringBuilder();

			sb.append("NumID," + "Honest," + "total time," + "foundTxMin," + "Validation time," + "successful, block size (KB)\n");

			int successSum = 0;

			for (NodeInfo cur : map.keySet()) {
				SimLog log = map.get(cur);
				List<MineAttemptLog> validMine = log.getValidMineAttemptLog();
				List<MineAttemptLog> failedMine = log.getFailedMineAttemptLog();

				sb.append(cur.getNumID() + "," + log.getMode()+",");
				for (int i = 0; i < validMine.size(); i++) {
					if (i != 0)
						sb.append(",,");
					sb.append(validMine.get(i));
				}
				successSum += validMine.size();
				for (int i = 0; i < failedMine.size(); i++) {
					if (i != 0 && validMine.size()>0)sb.append(",,");
					sb.append(failedMine.get(i));
				}
				sb.append('\n');
			}
			double successRate = (double)successSum / (1.0 * iterations * map.keySet().size()) * 100;
			sb.append("Success Rate = " + successRate + "\n");

			writer.write(sb.toString());
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private static void processTransactions(ConcurrentHashMap<NodeInfo, SimLog> map, int iterations) {

		try {
			String logPath = System.getProperty("user.dir") + File.separator + "Logs" + File.separator
					+ "TransactionValidationAttempts.csv";
			File logFile = new File(logPath);

			logFile.getParentFile().mkdirs();
			PrintWriter writer;

			writer = new PrintWriter(logFile);

			StringBuilder sb = new StringBuilder();

			sb.append("NumID," + "Honest," + "Transaction Trials," + "Transaction Success,"
					+ "Transaction time(per)," + "Authenticated count," + "Sound count," + "Correct count,"
					+ "Has Balance count," + "Successful, Avg Time per validator, Transaction size (KB)\n");

			int successSum = 0;

			for (NodeInfo cur : map.keySet()) {
				SimLog log = map.get(cur);
				List<TransactionLog> validTransactions = log.getValidTransactions();
				List<TransactionLog> failedTransactions = log.getFailedTransactions();

				sb.append(cur.getNumID() + "," + log.getMode() + "," + log.getTotalTransactionTrials() + ","
						+ log.getValidTransactionTrials() + ",");
				for (int i = 0; i < validTransactions.size(); i++) {
					if (i != 0)
						sb.append(",,,,");
					sb.append(validTransactions.get(i));
				}
				successSum += validTransactions.size();
				for (int i = 0; i < failedTransactions.size(); i++) {
					sb.append(",,,,");
					sb.append(failedTransactions.get(i));
				}
				sb.append('\n');
			}
			double successRate = (double)(successSum * 100.0) / (1.0 * iterations * map.keySet().size());
			sb.append("Success Rate = " + successRate + "\n");
			writer.write(sb.toString());
			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}

	private static void processMiscStats(ConcurrentHashMap<NodeInfo, SimLog> map){
		try{
			String logPath = System.getProperty("user.dir") + File.separator + "Logs" + File.separator
					+ "MiscStatistics.csv";
			File logFile = new File(logPath);

			logFile.getParentFile().mkdirs();
			PrintWriter writer;

			writer = new PrintWriter(logFile);

			StringBuilder sb = new StringBuilder();

			sb.append("NumID, Average Transaction validation time (ms), Average Transaction creation time (ms),"
					+"Average Block validation time (ms), Average Transaction collection time (ms), Average Transaction Size (KB), "
					+ "Average Block Size (KB)\n");

			for(NodeInfo nd : map.keySet()){
				SimLog log = map.get(nd);
				List<TransactionLog> validTransactions = log.getValidTransactions();
				List<MineAttemptLog> validBlocks = log.getValidMineAttemptLog();
				MiscStatisticsLog miscLog = log.getMiscLog();

				long avgTrxValidationTime = miscLog.getTransactionValidationTime()==0?0:(miscLog.getTransactionValidationTime() / miscLog.getTransactionValidationAttempts());
				long avgBlkValidationTime = miscLog.getBlockValidationTime()==0?0:(miscLog.getBlockValidationTime() / miscLog.getBlockValidationAttempts());
				long avgCollectionTime = miscLog.getTransactionCollectionTime()==0?0:(miscLog.getTransactionCollectionTime() / miscLog.getNumTransactionCollection());

				long totalTrxValidationTime = 0;
				long totalTrxSize = 0;
				for(TransactionLog trxLog : validTransactions){
					totalTrxValidationTime += trxLog.timeTaken();
					totalTrxSize += trxLog.getSize();
				}
				long avgTrxCreationTime = totalTrxValidationTime/(validTransactions.size()==0?1:validTransactions.size());
				long avgTrxCreationSize = totalTrxSize/(validTransactions.size()==0?1:validTransactions.size());

				long totalBlkSize = 0;
				for(MineAttemptLog blkLog : validBlocks){
					totalBlkSize += blkLog.getSize();
				}
				long avgBlkSize = totalBlkSize/(validBlocks.size()==0?1:validBlocks.size());

				sb.append(nd.getNumID()).append(",").append(avgTrxValidationTime).append(",").append(avgTrxCreationTime).append(",").append(avgBlkValidationTime).append(",").append(avgCollectionTime).append(",").append(bytesToKilobytes(avgTrxCreationSize)).append(",").append(bytesToKilobytes(avgBlkSize)).append("\n");

			}
			writer.write(sb.toString());
			writer.close();
		}catch (FileNotFoundException e){
			e.printStackTrace();
		}
	}
}
