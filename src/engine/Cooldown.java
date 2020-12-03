package engine;

/**
 * Imposes a cooldown period between two actions. // 두 동작 사이에 재사용 대기 시간 부과
 * 
 * @author <a href="mailto:RobertoIA1987@gmail.com">Roberto Izquierdo Amo</a>
 * 
 */
public class Cooldown {

	/** Cooldown duration. // 재사용 대기 시간 */
	private int milliseconds;
	/** Maximum difference between durations. // 지속 시간 간의 최대 차이 */
	private int variance;
	/** Duration of this run, varies between runs if variance > 0. // 이 동작의 지속 시간은 분산이 0 보다 크면 실행마다 다름  */
	private int duration;
	/** Beginning time. // 시작 시간  */
	private long time;

	/**
	 * Constructor, established the time until the action can be performed
	 * again.
	 * // 생성자, 작업을 다시 수행할 수 있을 때까지의 시간을 설정
	 * 
	 * @param milliseconds
	 *            Time until cooldown period is finished. // 재사용 대기 시간이 끝날 때까지의 시간
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
	 * // 생성자, +/- 분산의 변동을 사용하여 작업을 다시 수행할 수 있을 때까지의 시간 설정
	 * 
	 * @param milliseconds
	 *            Time until cooldown period is finished. // 재사용 대기 시간이 끝날 때까지의 시간
	 * @param variance
	 *            Variance in the cooldown period. // 재사용 대기 기간의 변동
	 */
	protected Cooldown(final int milliseconds, final int variance) {
		this.milliseconds = milliseconds;
		this.variance = variance;
		this.time = 0;
	}

	/**
	 * Checks if the cooldown is finished. // 재사용 대기시간이 종료되었는지 확인
	 * 
	 * @return Cooldown state. // 재사용 대기 상태
	 */
	public final boolean checkFinished() {
		if ((this.time == 0)
				|| this.time + this.duration < System.currentTimeMillis())
			return true;
		return false;
	}

	/**
	 * Restarts the cooldown. // 재사용 대기열 다시 시작
	 */
	public final void reset() {
		this.time = System.currentTimeMillis();
		if (this.variance != 0)
			this.duration = (this.milliseconds - this.variance)
					+ (int) (Math.random()
							* (this.milliseconds + this.variance));
	}
}
