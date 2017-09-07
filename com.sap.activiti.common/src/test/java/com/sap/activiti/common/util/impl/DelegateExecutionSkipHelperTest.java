package com.sap.activiti.common.util.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.activiti.engine.delegate.DelegateExecution;
import org.junit.Before;
import org.junit.Test;

import com.sap.activiti.common.impl.MockDelegateExecution;
import com.sap.activiti.common.util.ISkipHelper;

public class DelegateExecutionSkipHelperTest {

	private ISkipHelper classUnderTest;
	private DelegateExecution context;

	@Before
	public void setUp() {
		context = new MockDelegateExecution();
		classUnderTest = new DelegateExecutionSkipHelper(context);
	}

	@Test
	public void creatingSkipRequests() {
		classUnderTest.createSkipRequest("step1");
		classUnderTest.createSkipRequest("step2");

		assertEquals(ISkipHelper.SkipRequest.SKIP.toString(),
				context.getVariable(AbstractSkipHelper.SKIP_STEP_PREFIX + "step1"));

		assertEquals(ISkipHelper.SkipRequest.SKIP.toString(),
				context.getVariable(AbstractSkipHelper.SKIP_STEP_PREFIX + "step2"));
	}

	@Test
	public void removingSkipRequests() {
		classUnderTest.createSkipRequest("step1");
		assertEquals(ISkipHelper.SkipRequest.SKIP.toString(),
				context.getVariable(AbstractSkipHelper.SKIP_STEP_PREFIX + "step1"));

		classUnderTest.removeSkipRequest("step1");
		assertEquals(ISkipHelper.SkipRequest.NONE.toString(),
				context.getVariable(AbstractSkipHelper.SKIP_STEP_PREFIX + "step1"));

		classUnderTest.removeSkipRequest("step2");
		assertNull(context.getVariable(AbstractSkipHelper.SKIP_STEP_PREFIX + "step2"));
	}

	@Test
	public void checkingForSkipRequests() {
		assertFalse(classUnderTest.hasSkipRequest("step1"));

		classUnderTest.createSkipRequest("step1");
		assertTrue(classUnderTest.hasSkipRequest("step1"));

		classUnderTest.removeSkipRequest("step1");
		assertFalse(classUnderTest.hasSkipRequest("step1"));
	}

}
