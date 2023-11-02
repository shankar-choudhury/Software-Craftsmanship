package Geology;

import java.util.Optional;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

/**
 * Logging helper and handler for testing classes
 */
class LoggerTestingHandler extends Handler {

    private String lastLog = null;

    @Override
    public void publish(LogRecord record) {
        lastLog = record.getMessage();
    }

    @Override
    public void flush() {
        throw new AssertionError();
    }

    @Override
    public void close() throws SecurityException {
        throw new AssertionError();
    }

    public Optional<String> getLastLog() {
        return Optional.ofNullable(lastLog);
    }

    public void clearLogRecords() {
        lastLog = null;
    }
}