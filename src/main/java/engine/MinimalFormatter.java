package engine;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

/**
 * Implements a simple logging format.
 * 
 * @author <a href="mailto:RobertoIA1987@gmail.com">Roberto Izquierdo Amo</a>
 * 
 */
public class MinimalFormatter extends Formatter {

	/** Format for the date. */
	private static final DateFormat FORMAT = new SimpleDateFormat("h:mm:ss");
	/** System line separator. */
	private static final String LINE_SEPARATOR = System
			.getProperty("line.separator");

	@Override
	public final String format(final LogRecord logRecord) {

		StringBuilder output = new StringBuilder().append("[")
				.append(logRecord.getLevel()).append('|')
				.append(FORMAT.format(new Date(logRecord.getMillis())))
				.append("]: ").append(logRecord.getMessage()).append(' ')
				.append(LINE_SEPARATOR);

		return output.toString();
	}

}
