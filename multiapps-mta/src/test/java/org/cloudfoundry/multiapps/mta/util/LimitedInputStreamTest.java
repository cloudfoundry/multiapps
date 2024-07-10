package org.cloudfoundry.multiapps.mta.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.InputStream;

import org.cloudfoundry.multiapps.common.SLException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class LimitedInputStreamTest {

    @Mock
    private InputStream inputStream;

    private LimitedInputStream limitedInputStream;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this)
                          .close();
    }

    @Test
    void testRead() throws IOException {
        limitedInputStream = new LimitedInputStreamImpl(inputStream, 3);
        when(inputStream.read()).thenReturn(1, 0);
        readBytesFromStream(2);
        Exception exception = assertThrows(SLException.class, () -> limitedInputStream.read());
        assertEquals(exception.getMessage(), "Max size reached 3, current size 3");
    }

    private void readBytesFromStream(int bytesToRead) throws IOException {
        for (int i = 0; i < bytesToRead; i++) {
            limitedInputStream.read();
        }
    }

    @Test
    void testReadWithBuffer() throws IOException {
        limitedInputStream = new LimitedInputStreamImpl(inputStream, 3);
        when(inputStream.read(any(byte[].class), anyInt(), anyInt())).thenReturn(1, 1);
        readBytesFromStream(2);
        Exception exception = assertThrows(SLException.class, () -> limitedInputStream.read(new byte[1], 1, 1));
        assertEquals(exception.getMessage(), "Max size reached 3, current size 3");
    }

    private static class LimitedInputStreamImpl extends LimitedInputStream {

        public LimitedInputStreamImpl(InputStream in, long maxSize) {
            super(in, maxSize);
        }

        @Override
        protected void raiseError(long maxSize, long currentSize) {
            throw new SLException("Max size reached {0}, current size {1}", maxSize, currentSize);
        }
    }
}
