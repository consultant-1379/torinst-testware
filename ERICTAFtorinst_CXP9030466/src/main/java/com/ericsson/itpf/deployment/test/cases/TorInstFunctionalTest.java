package com.ericsson.itpf.deployment.test.cases;

import javax.inject.Inject;

import org.testng.annotations.Test;

import com.ericsson.cifwk.taf.TestCase;
import com.ericsson.cifwk.taf.TorTestCaseHelper;
import com.ericsson.cifwk.taf.annotations.Context;
import com.ericsson.cifwk.taf.annotations.DataDriven;
import com.ericsson.cifwk.taf.annotations.Input;
import com.ericsson.cifwk.taf.annotations.Output;
import com.ericsson.cifwk.taf.annotations.TestId;
import com.ericsson.cifwk.taf.guice.OperatorRegistry;
import com.ericsson.itpf.deployment.test.operators.TorInstOperator;

public class TorInstFunctionalTest extends TorTestCaseHelper implements
		TestCase {

	@Inject
	OperatorRegistry<TorInstOperator> registry;
	
	@TestId(id= "TORF_22326", title = "Test each service group has a campaign committed")
	@Test(groups={"Unit"})
	@Context(context = {Context.API})
	@DataDriven(name = "TorinstTestCases")
	public void testServiceGroupHasCampComitted(
			@Input("host") String host,
			@Output("expectedRc") int expectedRc
			){
		TorInstOperator operator = registry.provide(TorInstOperator.class);
		operator.copyTestScriptToServer(host, "TORF_22326");
		int scriptRc = operator.execute(host, "TORF_22326");
		assertTrue(scriptRc == expectedRc);
	}
	
	@TestId(id= "TORF_22325", title = "Test that the correct number of shares are mounted on all nodes")
	@Test(groups={"Unit"})
	@Context(context = {Context.API})
	@DataDriven(name = "TorinstTestCases")
	public void testSharesMountedOnNodes(
			@Input("host") String host,
			@Output("expectedRc") int expectedRc
			){
		TorInstOperator operator = registry.provide(TorInstOperator.class);
		operator.copyTestScriptToServer(host, "TORF_22325");
		int scriptRc = operator.execute(host, "TORF_22325");
		assertTrue(scriptRc == expectedRc);
	}
	
	
}
