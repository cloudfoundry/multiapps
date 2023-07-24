package org.cloudfoundry.multiapps.mta.util;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.atomic.AtomicLong;

public abstract class LimitedInputStream extends FilterInputStream {

    private final AtomicLong counter;
    private final long maxSize;

    public LimitedInputStream(InputStream in, long maxSize) {
        super(in);
        this.counter = new AtomicLong(0);
        this.maxSize = maxSize;
    }

    @Override
    public int read() throws IOException {
        int i = super.read();
        if (i > 0) {
            counter.incrementAndGet();
            checkSize();
        }
        return i;
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        int i = super.read(b, off, len);
        if (i > 0) {
            counter.addAndGet(i);
            checkSize();
        }
        return i;
    }

    private void checkSize() {
        long currentSize = counter.get();
        if (currentSize >= maxSize) {
            raiseError(maxSize, currentSize);
        }
    }

    protected abstract void raiseError(long maxSize, long currentSize);
}
