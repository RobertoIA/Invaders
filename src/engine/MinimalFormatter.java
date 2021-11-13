package engine;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

/**
 * Implements a simple logging format.
 * 간단한 로깅 형식을 구현합니다.
 *
 * @author <a href="mailto:RobertoIA1987@gmail.com">Roberto Izquierdo Amo</a>
 *
 */
public class MinimalFormatter extends Formatter {

	/** Format for the date.
	 * 날짜 형식입니다. */
	private static final DateFormat FORMAT = new SimpleDateFormat("h:mm:ss");
	/** System line separator.
	 * 시스템 줄 구분자. */
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
