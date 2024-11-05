package org.cloudfoundry.multiapps.mta.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.function.ObjIntConsumer;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;

import org.cloudfoundry.multiapps.common.ContentException;
import org.cloudfoundry.multiapps.common.SLException;
import org.cloudfoundry.multiapps.mta.Messages;

public final class InflatorUtil {

    private static final int BUFFER_SIZE = 4 * 1024; // 4KB

    private InflatorUtil() {

    }

    public static void inflate(EntryToInflate entryToInflate, ObjIntConsumer<byte[]> decompressedBytesConsumer) {
        Inflater inflater = new Inflater(true);
        try (InputStream ignored = entryToInflate.inputStream()) {
            inflateEntryContent(inflater, entryToInflate, decompressedBytesConsumer);
        } catch (DataFormatException | IOException e) {
            throw new SLException(e, e.getMessage());
        } finally {
            inflater.end();
        }
    }

    private static void inflateEntryContent(Inflater inflater, EntryToInflate entryToInflate,
                                            ObjIntConsumer<byte[]> decompressedBytesConsumer)
        throws IOException, DataFormatException {
        byte[] compressedBytes = new byte[BUFFER_SIZE];
        byte[] decompressedBytesBuffer = new byte[BUFFER_SIZE];
        int numberOfReadBytes;
        long currentSizeInBytes = 0;
        while ((numberOfReadBytes = entryToInflate.inputStream()
                                                  .read(compressedBytes)) != -1) {
            inflater.setInput(compressedBytes, 0, numberOfReadBytes);
            while (!inflater.finished()) {
                int decompressedBytesNumber = inflater.inflate(decompressedBytesBuffer);
                validateIfMaxSizeIsExceeded(entryToInflate, currentSizeInBytes, decompressedBytesNumber);
                decompressedBytesConsumer.accept(decompressedBytesBuffer, decompressedBytesNumber);
                currentSizeInBytes += decompressedBytesNumber;
                if (decompressedBytesNumber == 0) {
                    break;
                }
            }
        }
    }

    private static void validateIfMaxSizeIsExceeded(EntryToInflate entryToInflate, long currentSizeInBytes, int decompressedBytesNumber) {
        if (currentSizeInBytes + decompressedBytesNumber > entryToInflate.maxSizeInBytes()) {
            throw new ContentException(Messages.ERROR_SIZE_OF_FILE_EXCEEDS_CONFIGURED_MAX_SIZE_LIMIT,
                                       currentSizeInBytes + decompressedBytesNumber,
                                       entryToInflate.name(),
                                       entryToInflate.maxSizeInBytes());
        }
    }

}
