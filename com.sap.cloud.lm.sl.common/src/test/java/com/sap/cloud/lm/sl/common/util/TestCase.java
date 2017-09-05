package com.sap.cloud.lm.sl.common.util;

public abstract class TestCase<I extends TestInput> {

    protected I input;
    protected String expected;

    public TestCase(I input, String expected) {
        this.input = input;
        this.expected = expected;
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
