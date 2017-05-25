package cereal.report;

import cereal.LibCereal;
import com.sun.jna.Pointer;

import java.io.Closeable;
import java.io.IOException;

public class Report implements Closeable {
    private static final LibCereal LIB = LibCereal.INSTANCE;

    final Pointer ptr;

    public Report() {  this.ptr = LIB.report_new();  }

    public Report setAction(final String action) {
        LIB.report_set_action(this.ptr, action);
        return this;
    }

    public String getAction() {  return LIB.report_get_action(this.ptr);  }

    public Report setProcess(final String process) {
        LIB.report_set_process(this.ptr, process);
        return this;
    }

    public String getProcess() {  return LIB.report_get_process(this.ptr);  }

    public Report setRequestNumber(long requestNumber) {
        LIB.report_set_request_number(this.ptr, requestNumber);
        return this;
    }

    public long getRequestNumber() {  return LIB.report_get_request_number(this.ptr);  }

    public Report setDurationNanos(long durationNanos) {
        LIB.report_set_duration_nanos(this.ptr, durationNanos);
        return this;
    }

    public long getDurationNanos() {  return LIB.report_get_duration_nanos(this.ptr);  }

    public Report setCommand(final String command) {
        LIB.report_set_command(this.ptr, command);
        return this;
    }

    public String getCommand() {  return LIB.report_get_command(this.ptr);  }


    public void destroy() {  LIB.report_destroy(this.ptr);  }

    @Override
    public void close() throws IOException {  this.destroy();   }
}
