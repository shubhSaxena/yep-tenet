package tenet.util.pattern.cbc;

import tenet.util.pattern.IParam;

/**
 * CBC�ĳ�����࣬
 * ��CBC��ָ�������ߣ�Caller���󣬿��ԶԵ����߽��лص������ǲ����жϻص����������Ҫ�жϻص�������Ҫʹ��{@link tenet.protocol.interrupt}
 * ����Tenet�ص㣬����Command��Ҫָ��ģ�����е�ִ��ʱ�䣬������֤һ������ִ��ʱ��ִ�У���ִ��ʱ��С��ģ����ʱ��ʱ����
 * ������ͨ������£�Լ������ʱ�䲻������ģ����ʱ�䡣
 * @see tenet.protocol.interrupt
 * @see tenet.core.Simulator
 * @author meilunsheng
 * @version 09.01
 */
public abstract class Command implements Comparable<Command> {

	protected ICaller m_caller;
	protected String m_name;
	protected IReceiver m_recv;
	protected Double m_time;

	/**
	 * Commandģʽԭ�͵Ĺ��캯��
	 * @param m_time ����ʱ��
	 * @param m_recv ���������
	 */
	protected Command(double m_time, IReceiver m_recv) {
		this(null, m_time, m_recv);
	}

	/**
	 * CBCģʽ���캯��
	 * @param caller ������
	 * @param m_time ����ʱ��
	 * @param m_recv ���������
	 */
	protected Command(ICaller caller, double m_time, IReceiver m_recv) {
		super();
		this.m_time = m_time;
		this.m_recv = m_recv;
		assert this.m_recv != null : "There must be a receiver for any command. But "
				+ this.getClass().getName()
				+ " Instance:"
				+ this.toString()
				+ " have no receiver";
		assert this.m_time != null : "There must be an execute-time for any command. But "
				+ this.getClass().getName()
				+ " Instance:"
				+ this.toString()
				+ " has not.";
		this.m_caller = caller;
	}

	public final IParam _execute() {
		IParam result = this.execute();
		if (this.m_caller != null) {
			m_caller.onRecall(this, this.m_recv, result);
			return null;
		} else
			return result;
	}

	@Override
	public final int compareTo(Command obj) {
		return m_time.compareTo(obj.m_time);
	}

	public void dump() {
		System.out.println(this.getName());
	}

	/**
	 * �����ʵ�ʵ���
	 * @return �ص��������൱�ں������؂���û��ָ��������ʱ��Ч��
	 */
	public abstract IParam execute();

	/**
	 * ��õ�����
	 * @return ������
	 */
	public final ICaller getCaller() {
		return this.m_caller;
	}

	/**
	 * ���ִ��ʱ��
	 * @return ִ��ʱ��
	 */
	public final Double getExecuteTime() {
		return this.m_time;
	}

	public final String getName() {
		if (m_name != null)
			return m_name;
		else
			return this.getClass().getName();
	}

	public final IReceiver getReceiver() {
		return this.m_recv;
	}

}
