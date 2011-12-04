package tenet.protocol.network.ipv4;

import tenet.protocol.interrupt.InterruptParam;


/**
 * IPЭ���ȡ�����жϲ���
 * ����ȡ�ɹ�ʱstateΪOK,��ӦֵΪ��������
 * ���²����,���¶�ȡ����ʱ,stateΪError,��Ӧֵ��destinationΪ�ӿ�IP,sourceΪ0,uidΪ����Э���,���ݶ�Ϊnull
 * @author meilunsheng
 *
 */
public class IPReceiveParam extends InterruptParam {
	public enum ReceiveType{
		OK,Error
	}
	
	public IPReceiveParam(ReceiveType state, int destination, int source,
			int uid, byte[] data) {
		super();
		this.state = state;
		this.destination = destination;
		this.source = source;
		this.uid = uid;
		this.data = data;
	}
	
	public ReceiveType getState() {
		return state;
	}
	public int getDestination() {
		return destination;
	}
	public int getSource() {
		return source;
	}
	public int getUid() {
		return uid;
	}
	public byte[] getData() {
		return data;
	}

	/**
	 * ״̬
	 */
	protected ReceiveType state;
	/**
	 * Ŀ��IP
	 */
	protected int destination; 
	/**
	 * ԴIP
	 */
	protected int source;
	/**
	 * �����Э���
	 */
	protected int uid;
	/**
	 * ���ݶ�
	 */
	protected byte[] data;
}
