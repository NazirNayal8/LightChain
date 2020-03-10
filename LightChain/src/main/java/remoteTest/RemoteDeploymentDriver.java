package remoteTest;

import blockchain.LightChainNode;
import blockchain.Parameters;
import org.apache.log4j.PropertyConfigurator;
import util.PropertyManager;
import util.Util;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import static util.Util.getIntroducerIP;

public class RemoteDeploymentDriver {
    private static String IP;
    public static void main(String []args){
        Util.local = false;
        IP = Util.grabIP();
        try {
			System.setProperty("java.rmi.server.hostname",IP);
			System.setProperty("java.rmi.server.useLocalHostname", "false");
		}catch (Exception e) {
			System.err.println("Exception in initialization. Please try running the program again.");
			System.exit(0);
		}
        PropertyConfigurator.configure("log4j.properties");
        if(args.length==0){
            System.out.println("Please enter a valid flag. -slave to run a slave, -master to run a master");
            return;
        }
        boolean master;
        if(args[0].equalsIgnoreCase("-slave")){
            master = false;
        }else if(args[0].equalsIgnoreCase("-master")){
            master = true;
        }else{
            System.out.println("Please enter a valid flag. -slave to run a slave, -master to run a master");
            return;
        }

        PropertyManager propMng = new PropertyManager("simulation.config");
        Parameters params = new Parameters();
        params.setAlpha(Integer.parseInt(propMng.getProperty("alpha", "10")));
        params.setTxMin(Integer.parseInt(propMng.getProperty("txmin","1")));
        params.setSignaturesThreshold(Integer.parseInt(propMng.getProperty("signaturesThreshold","1")));
        params.setInitialBalance(Integer.parseInt(propMng.getProperty("initialBalance", "20")));
        params.setLevels(Integer.parseInt(propMng.getProperty("levels", "30")));
        params.setValidationFees(Integer.parseInt(propMng.getProperty("validationFees", "1")));
        params.setMode(Boolean.parseBoolean(propMng.getProperty("Mode", "True")));
        String adrs = getIntroducerIP(propMng.getProperty("NodeInitializerAddress",null),"1099");

        if((adrs!=null && adrs.equalsIgnoreCase("First")) || master){
            runMaster(params);
        }else {
            runSlave(params, adrs);
        }
    }

    public static void runSlave(Parameters params, String adrs){
        Configuration conf = new Configuration();
        if(adrs==null){
            conf.parseIntroducer();
        }else{
            conf.setIntroducer(adrs);
        }
        LightChainNode slave;
        int port = 1099;
        for(int i=0;i<10;i++){
            try{
            slave = new LightChainNode(params, port, conf.getIntroducer(), false);
            break;
            } catch (RemoteException e) {
//                e.printStackTrace();
            }
            port = (new Random()).nextInt(65000);
        }
        Scanner in = new Scanner(System.in);
        while(true){
            System.out.println("Enter X to exit");
            String inp = in.nextLine();
            if(inp.equalsIgnoreCase("x")){
                break;
            }
        }
    }

    public static void runMaster(Parameters params){
        try{
            LightChainNode master = new LightChainNode(params, 1099, "none", true);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        Scanner in = new Scanner(System.in);
        while(true){
            System.out.println("Enter X to exit");
            String inp = in.nextLine();
            if(inp.equalsIgnoreCase("x")){
                break;
            }
        }
    }
}
