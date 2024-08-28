package com.ericsson.itpf.deployment.test.operators;


import com.ericsson.cifwk.taf.annotations.Operator;
import com.ericsson.cifwk.taf.annotations.Context;
import com.ericsson.cifwk.taf.data.DataHandler;
import com.ericsson.cifwk.taf.data.Host;
import com.ericsson.cifwk.taf.handlers.RemoteFileHandler;
import com.ericsson.cifwk.taf.handlers.implementation.SshRemoteCommandExecutor;



@SuppressWarnings("deprecation")
@Operator(context = Context.API)
public class TorInstApiOperator implements TorInstOperator {
	
	public void copyTestScriptToServer(String host, String testCaseId) {
		Host apihost = DataHandler.getHostByName(host);
		RemoteFileHandler remote = new RemoteFileHandler(apihost);
		String localFileLocation=testCaseId+".sh";
		String remoteFileLocation="/opt/ericsson/torinst/"+testCaseId+".sh";
		System.out.println("[INFO] Copying file "+localFileLocation+" to server "+apihost.getIp()+". Location = "+remoteFileLocation);
		remote.copyLocalFileToRemote(localFileLocation, remoteFileLocation);
		
	}
	public int execute(String host, String testCaseId){
		Host apihost = DataHandler.getHostByName(host);
		SshRemoteCommandExecutor cmdHelper = new SshRemoteCommandExecutor(apihost);
		System.out.println("[INFO] Changing permission of script "+testCaseId+".sh by executing command [chmod +x /opt/ericsson/torinst/"+testCaseId+".sh]");
		cmdHelper.simplExec("chmod +x /opt/ericsson/torinst/"+testCaseId+".sh");
		System.out.println("[INFO] Executing script [/opt/ericsson/torinst/"+testCaseId+".sh] on server "+apihost.getIp());
		int rc=Integer.parseInt(cmdHelper.simplExec("/opt/ericsson/torinst/"+testCaseId+".sh; echo $?"));
		System.out.println("[INFO] Return code of script "+testCaseId+".sh is "+rc);
		return rc;
	}

}
