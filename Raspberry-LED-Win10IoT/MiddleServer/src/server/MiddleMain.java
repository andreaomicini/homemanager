package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import alice.logictuple.LogicTuple;
import alice.logictuple.Value;
import alice.logictuple.Var;
import alice.logictuple.exceptions.InvalidLogicTupleException;
import alice.logictuple.exceptions.InvalidVarNameException;
import alice.tucson.api.ITucsonOperation;
import alice.tucson.api.NegotiationACC;
import alice.tucson.api.SynchACC;
import alice.tucson.api.TucsonAgentId;
import alice.tucson.api.TucsonMetaACC;
import alice.tucson.api.TucsonTupleCentreId;
import alice.tucson.api.exceptions.TucsonInvalidAgentIdException;
import alice.tucson.api.exceptions.TucsonInvalidTupleCentreIdException;
import alice.tucson.api.exceptions.TucsonOperationNotPossibleException;
import alice.tucson.api.exceptions.UnreachableNodeException;
import alice.tuplecentre.api.Tuple;
import alice.tuplecentre.api.TupleCentreId;
import alice.tuplecentre.api.exceptions.OperationTimeOutException;
import cli.System.AppDomainInitializer.Method;

/**
 * 
 * @author Luca Marzaduri
 */
public final class MiddleMain 
{
	static String middleHost = "localhost";
	static int middlePort = 10252;
	
    public static void main(final String[] args) 
    {
        /*
         * 1) Build a TuCSoN Agent identifier to contact the TuCSoN system.
         */
        TucsonAgentId aid = null;
        
    
    	/*
    	 * MiddleMain aid, middleHost, middlePort
    	 */
    	
        if (args.length == 3) 
        {
        	// aid
            try 
            {
				aid = new TucsonAgentId(args[0]);
			} 
            catch (TucsonInvalidAgentIdException e1) 
            {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
            
            //middleHost            
            middleHost = args[4];
            
            //Validity test for middlePort  
            try 
            {
          	 middlePort = Integer.parseInt(args[5]);
			
          	 if (middlePort < 1024 || middlePort > 65535) 
          	 {
          		 System.out.println("Usage: middleServer [middlePort>1024]");
          		 System.exit(1);
          	 }
            } 
            catch (NumberFormatException e) 
            {
            	System.out.println("Usage: java  middleServer [middlePort>1024]");
            	System.exit(1);
            }
            
        } 
        
        try 
        {
			aid = new TucsonAgentId("middleMain");
			middleHost = InetAddress.getLocalHost().getHostAddress();
		} 
        catch (TucsonInvalidAgentIdException e) 
        {
			e.printStackTrace();
			System.exit(1);
		} 
        catch (UnknownHostException e) 
        {
			e.printStackTrace();
		}         
        
        
        /*
         * 4) Create Socket for agent-middleServer comunication 
         */			
		ServerSocket serverSocket = null;
		Socket clientSocket = null;

		try 
		{
		  serverSocket = new ServerSocket(middlePort);
		  serverSocket.setReuseAddress(true);
		  System.out.println("Server socket opened: " + serverSocket);
		}
		catch (Exception e) 
		{
		  System.err.println("Server socket create error: " + e.getMessage());
		  e.printStackTrace();
		  System.exit(1);
		}

		try 
		{
		  while (true) 
		  {
			System.out.println("middleServer waiting for connections...\n");
			
			try 
			{
			  clientSocket = serverSocket.accept();
			  clientSocket.setSoTimeout(300);
			  System.out.println("middleServer: connection accepted: " + clientSocket);
			}
			catch (Exception e) 
			{
			  System.err.println("middleServer: connection problems: " + e.getMessage());
			  e.printStackTrace();
			  continue;
			}

			// New Thread to handle connection request
			try 
			{
			  new middleServerThread(clientSocket, aid).start();
			}
			catch (Exception e) 
			{
			  System.err.println("Server: problemi nel server thread: " + e.getMessage());
			  e.printStackTrace();
			  continue;
			}

		  } // while
		}
		catch (Exception e) 
		{
		  e.printStackTrace();
		  System.out.println("Server: termino...");
		  System.exit(2);
		}
    }

}



/*
 * middleServerThread  
 */
    
class middleServerThread extends Thread
{
  private Socket clientSocket = null;
  private SynchACC acc;
  private TucsonTupleCentreId tid;
  private TucsonAgentId aid;
  
  public middleServerThread(Socket clientSocket, TucsonAgentId aid) throws SocketException 
  {
    this.clientSocket = clientSocket;
    this.aid = aid;
    
    try
    {
	    /*
	     *  Get a TuCSoN ACC to enable interaction with the TuCSoN system.
	     */
	    final NegotiationACC negAcc = TucsonMetaACC.getNegotiationContext(aid);
	    acc = negAcc.playDefaultRole();
	       
	    
	} 
    catch (final TucsonInvalidAgentIdException e) 
    {
	   /*
	   * The chosen TuCSoN Agent ID is not admissible.
	   */
	      e.printStackTrace();
	      return;
	} 
    catch (final TucsonOperationNotPossibleException e) 
    {
	   /*
	   * The requested TuCSoN operation cannot be performed.
	   */
	      e.printStackTrace();
	      return;
	} 
    catch (final UnreachableNodeException e) 
    {
      /*
	   * The chosen target tuple centre is not reachable.
	   */
	      e.printStackTrace();
	      return;
	} 
    catch (final OperationTimeOutException e) 
	{
	   /*
	   * Operation timeout expired.
	   */
	      e.printStackTrace();
	      return;
	} 
  }
  
  public String[] parseCommand(String command)
  {
	  String res[] = new String[5];
	  
	  StringTokenizer tok = new StringTokenizer(command, "@", true);
	  res[0] = tok.nextToken().trim();
	  tok.nextToken();
	  res[1] = tok.nextToken(":").trim();
	  tok.nextToken();
	  res[2] = tok.nextToken("?").trim();
	  tok.nextToken();
	  try
	  {
		  res[3] = tok.nextToken("(").trim();
		  tok.nextToken();
		  res[4] = tok.nextToken(")").trim();
		  tok.nextToken();
		  res[4] += tok.nextToken(")").trim();		  
	  }
	  catch (Exception e)
	  {
		  res[4] = null;
	  }
	  
	  return res;
  }
  
  public ITucsonOperation performCommand(String command, LogicTuple tuple)
  {
	  ITucsonOperation op = null;
	 
	  try 
	  {
		  java.lang.reflect.Method meth;
		  
		  if (tuple == null)
		  {
			   meth= SynchACC.class.getMethod(command, new Class[]{ TupleCentreId.class, Long.class});
			   op = (ITucsonOperation) meth.invoke(acc, new Object[]{tid, null});
		  }
		  else
		  {
			  meth = SynchACC.class.getMethod(command, new Class[]{ TupleCentreId.class, Tuple.class, Long.class});
			  op = (ITucsonOperation) meth.invoke(acc, new Object[]{tid, tuple, null});
		  }
		  
	  } 
	  catch (NoSuchMethodException e) 
	  {
			// TODO Auto-generated catch block
			e.printStackTrace();
	  } 
	  catch (SecurityException e) 
	  {
			// TODO Auto-generated catch block
			e.printStackTrace();
	  } catch (IllegalAccessException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IllegalArgumentException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (InvocationTargetException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	  
	  return op;
  }
  
  public void run() 
  {
    DataInputStream inSock;
    DataOutputStream outSock;
    
    try 
    {
      try 
      {    	  
        inSock = new DataInputStream(clientSocket.getInputStream());
        outSock = new DataOutputStream(clientSocket.getOutputStream());
      }
    
      catch (Exception e) 
      {
    	  System.out.println("Input/Output Stream problem: ");
          e.printStackTrace();
          return;
      }
      
      // ----------------- RECEIVING COMMAND -----------------

      
      byte[] command = new byte[10000]; 
      
      try 
      {
    	  
    	  inSock.read(command);
    	  
    	  while (inSock.available() != 0)
    	  {
    		  inSock.read(command);
    	  }
    	  
      }
      catch(SocketTimeoutException ste)
      {
        System.out.println("Timeout: ");
        ste.printStackTrace();
        clientSocket.close();
        return;         
      }        
      catch (Exception e) 
      {
        e.printStackTrace();
        return;
      }
      
      
      try
      {
      	/*
      	 * Parsing Command
      	 */
    	  
      	String[] parsedCommand = this.parseCommand(new String(command, StandardCharsets.UTF_8));
      	
      	String tcName = parsedCommand[0];
      	String netid = parsedCommand[1];
      	String portno = parsedCommand[2];
      	String cmd = parsedCommand[3];
      	String arg = parsedCommand[4];
      	
      	/*
	     * Define the tuplecentre target of your coordination operations.
	     */
	    tid = new TucsonTupleCentreId(tcName, netid, portno);
	    
      	
      	
      	LogicTuple tuple = null;
      	
		if (arg != null)
		{
	        try 
			{
				tuple = LogicTuple.parse(arg);
			} 
	        catch (InvalidLogicTupleException e) 
	        {
				System.out.println("LogicTuple parsing error");
				e.printStackTrace();
			}
		}
        
        
        /*
         *  Perform the coordination operation
         */
        ITucsonOperation op = this.performCommand(cmd, tuple);
        
        
        /*
         * Check requested operation success.
         */
        List<LogicTuple> res = null;
        
        if (op != null)
        {
	        if (op.isResultSuccess()) 
	        {
	            System.out.println("[" + aid.getAgentName() + "]: Operation succeeded.");
	            
	            
	         // ----------------- SEND RESPONSE -----------------
	
	            
	        try
	        {
	        	res = op.getLogicTupleListResult();
	        }
	        catch (Exception e)
	        {
	        	res = new ArrayList<LogicTuple>();
	        	res.add(op.getLogicTupleResult());
	        	e.printStackTrace();
	        }
	        
	        System.out.println("[" + aid.getAgentName() + "]: Operation result is " + res);
	        
	        outSock.write(res.toString().getBytes("UTF-8"));
	        
	        } 
	        else 
	        {
	            System.out.println("[" + aid.getAgentName() + "]: Operation failed.");
	            outSock.write("Failed command execution".getBytes("UTF-8"));
	        }
        }
        else
        {
        	outSock.write("Command not found".getBytes("UTF-8"));
        }
        
      }
      catch (Exception e) 
      {
        System.err.println("\nComunication error: " + e.getMessage());
        e.printStackTrace();
        clientSocket.close();
	        return;
	  }
  }
  catch (Exception e) 
  {
    e.printStackTrace();
    System.out.println("\nFatal error");
   }
  }
}
