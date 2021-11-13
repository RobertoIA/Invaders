package engine;

/**
 * Imposes a cooldown period between two actions.
 * 두 행동 사이에 재사용 대기시간을 부과합니다.
 *
 * @author <a href="mailto:RobertoIA1987@gmail.com">Roberto Izquierdo Amo</a>
 *
 */
public class Cooldown {

	/** Cooldown duration.
	 * 재사용 대기시간. */
	private int milliseconds;
	/** Maximum difference between durations.
	 * duration들 간의 최대 차이입니다. */
	private int variance;
	/** Duration of this run, varies between runs if variance > 0.
	 * 이 실행 duration은 (variance > 0)인 경우 실행 간에 다릅니다. */
	private int duration;
	/** Beginning time.
	 * 시작 시간입니다. */
	private long time;

	/**
	 * Constructor, established the time until the action can be performed
	 * again.
	 * 생성자, 작업을 다시 수행할 수 있을 때까지의 시간을 설정합니다.
	 *
	 * @param milliseconds
	 *            Time until cooldown period is finished.
	 *            재사용 대기 시간이 끝날 때까지의 시간입니다.
	 */
	protected Cooldown(final int milliseconds) {
		this.milliseconds = milliseconds;
		this.variance = 0;
		this.duration = milliseconds;
		this.time = 0;
	}

	/**
	 * Constructor, established the time until the action can be performed
	 * again, with a variation of +/- variance.
	 * 생성자는 +/- variation의 변형으로 작업을 다시 수행할 수 있을 때까지의 시간을 설정합니다.
	 *
	 * @param milliseconds
	 *            Time until cooldown period is finished.
	 *            재사용 대기 시간이 끝날 때까지의 시간입니다.
	 * @param variance
	 *            Variance in the cooldown period.
	 *            재사용 대기 시간의 variance.
	 */
	protected Cooldown(final int milliseconds, final int variance) {
		this.milliseconds = milliseconds;
		this.variance = variance;
		this.time = 0;
	}

	/**
	 * Checks if the cooldown is finished.
	 * 쿨다운이 완료되었는지 확인합니다.
	 *
	 * @return Cooldown state.
	 */
	public final boolean checkFinished() {
		if ((this.time == 0)
				|| this.time + this.duration < System.currentTimeMillis())
			return true;
		return false;
	}

	/**
	 * Restarts the cooldown.
	 * 재사용 대기시간을 다시 시작합니다.
	 */
	public final void reset() {
		this.time = System.currentTimeMillis();
		if (this.variance != 0)
			this.duration = (this.milliseconds - this.variance)
					+ (int) (Math.random()
					* (this.milliseconds + this.variance));
	}
}
