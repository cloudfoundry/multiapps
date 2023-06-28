package org.cloudfoundry.multiapps.mta.handlers;

import java.io.IOException;
import java.io.InputStream;

import org.cloudfoundry.multiapps.common.ContentException;
import org.cloudfoundry.multiapps.mta.Messages;

public class LimitedSizeInputStream extends InputStream {

    private final InputStream inputStream;
    private final long maxSize;
    private final String entryName;
    private long total;

    public LimitedSizeInputStream(InputStream inputStream, long maxSize, String entryName) {
        this.inputStream = inputStream;
        this.maxSize = maxSize;
        this.entryName = entryName;
    }

    @Override
    public int read() throws IOException {
        int i = inputStream.read();
        if (i >= 0)
            incrementCounter(1);
        return i;
    }

    @Override
    public int read(byte b[]) throws IOException {
        return read(b, 0, b.length);
    }

    @Override
    public int read(byte b[], int off, int len) throws IOException {
        int i = inputStream.read(b, off, len);
        if (i >= 0)
            incrementCounter(i);
        return i;
    }

    private void incrementCounter(int size) throws IOException {
        total += size;
        if (total > maxSize)
            throw new ContentException(Messages.ERROR_PROCESSED_SIZE_OF_FILE_EXCEEDS_CONFIGURED_MAX_SIZE_LIMIT, total, entryName, maxSize);
    }

}
