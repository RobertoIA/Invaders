package engine;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class MinimalFormatter extends Formatter {

	private static final DateFormat format = new SimpleDateFormat("h:mm:ss");
	private static final String lineSeparator = System
			.getProperty("line.separator");

	@Override
	public String format(LogRecord logRecord) {
		
		StringBuilder output = new StringBuilder()
				.append("[").append(logRecord.getLevel()).append('|')
				.append(format.format(new Date(logRecord.getMillis())))
				.append("]: ").append(logRecord.getMessage()).append(' ')
				.append(lineSeparator);
		
		return output.toString();
	}

}
