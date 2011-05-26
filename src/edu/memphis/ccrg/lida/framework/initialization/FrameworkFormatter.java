package edu.memphis.ccrg.lida.framework.initialization;

import java.text.MessageFormat;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class FrameworkFormatter extends Formatter {

	@Override
	public String format(LogRecord logRecord) {
		String logMessages = new String("");
		// String dateString="";
		long actualTick = 0L;
		// String name;

		String message = logRecord.getMessage();
		if (message != null) {
			MessageFormat mf = new MessageFormat(message);

			Object[] param = logRecord.getParameters();
			if (param != null && param[0] instanceof Long) {
				actualTick = (Long) param[0];
			}
			logMessages = String.format("%010d :%010d :%-10s :%-60s \t-> %s %n",
					logRecord.getSequenceNumber(), actualTick, logRecord
							.getLevel(), logRecord.getLoggerName(), mf
							.format(logRecord.getParameters()));
			return logMessages;
		}
		return "";
	}
}
