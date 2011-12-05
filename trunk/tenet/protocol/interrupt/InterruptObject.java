package tenet.protocol.interrupt;

import java.util.TreeMap;

import tenet.core.Simulator;
import tenet.util.pattern.IParam;
import tenet.util.pattern.cbc.Command;
import tenet.util.pattern.cbc.ICaller;
import tenet.util.pattern.cbc.IReceiver;

/**
 * ���ж϶���.<br>
 * ���������������˸ö�����Ա������жϣ��Ӷ�ʵ���첽�ص���<br>
 * Tenet 11'��һ�����߳�ģ����߳�ͬ���������̵�ģ���������ڵ��̳߳�����ֱ��ģ��ͬ����������˲������첽�ص���ģ���������̡�<br>
 * ���ж�ģʽ�ǽ���첽�ص�������ģʽ֮һ����Ҫ��ѭ����Լ����
 * <ul>
 * <li>�κ�һ�������߿��Ը���������ض����źŽ��еȴ����������ڵȴ��ĺ���{@link #wait(int, Double)}����������������������䣬���߱�֤�ж��ź�����������ִ�����ʱ���ᱻ����</li>
 * </ul>
 * �����ģ��ͬ�������Ĺ��̣���Ҫ�����������¹涨��
 * <ul>
 * <li>�κ�һ����Ҫģ��ͬ�������ĺ�����������ģ���߼���������߷��ͺ�����жϣ���ʹ�����߲���������ź��ϵȴ�</li>
 * <li>����һ����������������ģ��ͬ�������ĺ���</li>
 * </ul>
 * �ж�ģʽ���жϵ����������ڻص�������ʹ��{@link #interrupt(int, InterruptParam)}ʱ�����жϣ���ʹ��{@link #delayInterrupt(int, InterruptParam, double)}ʱ�жϡ�
 * �����Ҫ�жϵ�������������ڹ�����ʹ��{@link tenet.util.pattern.cbc}�ṩ��CBCģʽ�����ߵ��첽�ص����������౾��Ҳ�Ǹ�interruptObjectʱ��������ʹ��{@link #delayInterrupt(int, InterruptParam, double)}.
 * @author meilunsheng
 * @version 09.01
 */
public abstract class InterruptObject implements ICaller, IReceiver {

	protected class DelayInterrupt extends Command {

		InterruptParam m_param;
		int m_signal;

		protected DelayInterrupt(double m_time, IReceiver m_recv, int signal,
				InterruptParam param) {
			super(m_time, m_recv);
			this.m_signal = signal;
			this.m_param = param;
		}

		@Override
		public IParam execute() {
			((InterruptObject) this.m_recv).interrupt(m_signal, m_param);
			return null;
		}

	}

	protected class TimeoutCommand extends Command {

		private int m_signal;

		protected TimeoutCommand(ICaller caller, double m_time,
				IReceiver m_recv, int signal) {
			super(caller, m_time, m_recv);
			m_signal = signal;
		}

		@Override
		public IParam execute() {
			return new SignalParam(m_signal);
		}

	}

	public TreeMap<Integer, TimeoutCommand> m_timeout;

	public InterruptObject() {
		super();
		this.m_timeout = new TreeMap<Integer, TimeoutCommand>();
	}

	/**
	 * �ӳٴ����жϣ������жϻص���
	 * @param signal �ж��ź�
	 * @param param �жϲ���
	 * @param delay �ӳ�ʱ��
	 */
	public void delayInterrupt(int signal, InterruptParam param, double delay) {
		if (this.m_timeout.containsKey(signal)) {
			Simulator.getInstance().schedule(
					new DelayInterrupt(
							Simulator.getInstance().getTime() + delay, this,
							signal, param));
		}
	}

	/**
	 * �ж��Ƿ��Ѿ��ڵȴ�ĳ���ж��ź�
	 * @param signal �ж��ź�
	 * @return <b>null</b>û�еȴ�<br>
	 * ����Ϊ���źŵȴ���ʱ��Command
	 */
	public TimeoutCommand hasWaiter(int signal) {
		return m_timeout.get(signal);
	}
	
	/**
	 * �����жϣ����жϻص���
	 * @param signal �ж��ź�
	 * @param param �жϲ���
	 */
	public void interrupt(int signal, InterruptParam param) {
		if (this.m_timeout.containsKey(signal)) {
			// this.m_timeout.remove(signal);
			if (TimeoutCommand.class.isInstance(param))
				this.m_timeout.remove(new Integer(signal));
			this.interruptHandle(signal, param);
		}
	}

	
	/**
	 * �ж��źŴ������
	 * @param signal
	 * @param param
	 */
	protected abstract void interruptHandle(int signal, InterruptParam param);

	@Override
	public void onRecall(Command cmd, IReceiver recv, IParam result) {
		if (cmd instanceof TimeoutCommand) {
			int signal = ((SignalParam) result).getSignal();
			if (this.m_timeout.get(signal) != cmd)
				return;
			// this.m_timeout.remove(new Integer(signal));
			interrupt(signal, new TimeoutInterrupt());
		}
	}

	/**
	 * ָ�����ж��źŵȴ�λ��λ
	 * @param signal �ж��ź�
	 */
	public void resetInterrupt(int signal) {
		this.m_timeout.remove(signal);
	}

	/**
	 * �ȴ�ָ�����ж��źŴ�������ʱ
	 * @param signal �ж��ź�
	 * @param timeout ��ʱʱ�䣬��ΪDouble.NaNʱ��Ϊ������ʱ��
	 * @return ���źŵȴ���ʱ��TimeoutCommand
	 */
	public TimeoutCommand wait(int signal, Double timeout) {
		if (timeout!=null&&!timeout.isNaN()) {
			TimeoutCommand tc = new TimeoutCommand(this, Simulator
					.getInstance().getTime() + timeout, null, signal);
			this.m_timeout.put(signal, tc);
			Simulator.getInstance().schedule(tc);
			return tc;
		} else {
			TimeoutCommand tc = new TimeoutCommand(this, Double.NaN, null,
					signal);
			this.m_timeout.put(signal, tc);
			return tc;
		}
	}
}
