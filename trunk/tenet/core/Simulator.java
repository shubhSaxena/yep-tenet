package tenet.core;

import java.util.ArrayList;
import java.util.PriorityQueue;

import org.knf.tenet.result.ResultManager;

import tenet.anticheat.ICallWatcher;
import tenet.node.INode;
import tenet.util.pattern.IParam;
import tenet.util.pattern.cbc.Command;
import tenet.util.pattern.cbc.IInvoker;
import tenet.util.pattern.cbc.IReceiver;

/**
 * ģ������.<br>
 * ������Ϊ��������ڣ�ʹ��ʱͨ��{@link #getInstance()}���ʵ����
 * @author meilunsheng
 * @version 09.01
 */
public class Simulator implements IInvoker, IReceiver {

	private static Simulator m_instance = null;

	/**
	 * ���ʵ��
	 * @return Simulatorʵ��
	 */
	public static Simulator getInstance() {
		if (m_instance == null)
			m_instance = new Simulator();
		return m_instance;
	}

	/**
	 * ��õ�ǰģ����ʱ��
	 * @return ��ǰģ����ʱ��
	 */
	public static double GetTime() {
		return getInstance().getTime();
	}

	/**
	 * ��ģ��������һ������ĵ���
	 * @param cmd ��Ҫ�����ȵ�����
	 */
	public static void Schedule(Command cmd) {
		getInstance().schedule(cmd);
	}

	private PriorityQueue<Command> m_cmdQueue;

	private ICallWatcher m_cwatcher;

	private ArrayList<INode> m_nodes;

	private double m_time;

	protected Simulator() {
		super();
		this.m_cmdQueue = new PriorityQueue<Command>();
		this.m_nodes = new ArrayList<INode>();
		this.m_time = 0;
	}

	public void dump() {
		System.out.println("------Nodes-----");
		for (INode p : this.m_nodes)
			p.dump();
		System.out.println("------Commands-----");
		for (Command c : this.m_cmdQueue)
			c.dump();
	}

	public double getTime() {
		return m_time;
	}

	/**
	 * ��ʼģ�⣬�����յ��ȶ��������˳��ִ�����ֱ��ִ����{@link SystemStop}�������û��������Ҫ��ִ��ʱ��ϵͳֹͣ
	 * �����ȵ��������ִ��ʱ���������ִ�У��ڵ���ģʽ�£�����ʹ��{@link SystemPause}����ģ���߳�
	 */
	public void run() {
		while (!this.m_cmdQueue.isEmpty()) {
			Command cmd = this.m_cmdQueue.poll();
			m_time = cmd.getExecuteTime();
			IParam result = cmd._execute();
			assert result instanceof SystemPause : "System Pause";
			if (cmd instanceof SystemStop){
				ResultManager.getInstance().dump();
				break;
			}
		}
	}

	public void schedule(Command cmd) {
		if (m_cwatcher != null) {
			Exception e = new Exception();
			m_cwatcher.eventRecord(m_cwatcher, e.getStackTrace());
		}
		this.m_cmdQueue.add(cmd);
	}

	public void setCallWatcher(ICallWatcher cwatcher) {
		this.m_cwatcher = cwatcher;
	}

	/**
	 * �������ȣ�������֤����ᱻִ�С�
	 * @param cmd
	 */
	public void unschedule(Command cmd) {
		this.m_cmdQueue.remove(cmd);
	}
}
