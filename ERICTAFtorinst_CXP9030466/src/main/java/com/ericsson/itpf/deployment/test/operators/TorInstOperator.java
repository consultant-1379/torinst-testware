package com.ericsson.itpf.deployment.test.operators;

import com.ericsson.cifwk.taf.GenericOperator;

public interface TorInstOperator extends GenericOperator {
	
	/**
	 * 
	 * @param host
	 * <br>The ip address of the target machine that the command
	 * is going to be executed on
	 * @param testCaseId
	 * <br>Is the name of the file that will be parsed. Each line will be
	 * read and passed the the commands object
	 * @return int
	 * <br>The test was either a success or a failure. This is checked by the return code of the test script
	 * 
	 */
	public abstract int execute(String host, String testCaseId);
	
	/**
	 * 
	 * @param host
	 * <br>The ip address of the target machine that the command
	 * is going to be executed on
	 * @param testCaseId
	 * <br>Is the name of the file that will be parsed. Each line will be
	 * read and passed the the commands object
	 */
	public abstract void copyTestScriptToServer(String host, String testCaseId);
	 
	
}
