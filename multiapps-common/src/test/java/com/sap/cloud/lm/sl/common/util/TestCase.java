package com.sap.cloud.lm.sl.common.util;

import com.sap.cloud.lm.sl.common.util.TestUtil.Expectation;

public abstract class TestCase<I extends TestInput> {

    protected I input;
    protected Expectation expectation;

    public TestCase(I input, Expectation expectation) {
        this.input = input;
        this.expectation = expectation;
    }

    protected void setUp() throws Exception {

    }

    public void run() throws Exception {
        setUp();
        test();
        tearDown();
    }

    protected abstract void test() throws Exception;

    protected void tearDown() throws Exception {

    }

}
