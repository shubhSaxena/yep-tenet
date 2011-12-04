package tenet.node;

import tenet.protocol.statecontrol.IStateSetable;
import tenet.util.pattern.cbc.IReceiver;
import tenet.util.pattern.serviceclient.IClient;
import tenet.util.pattern.serviceclient.IRegistryableService;

/**
 * �ڵ�ӿڣ����нڵ����ֱ�ӻ��ӵ�ʵ�ָýӿڣ�����Э�飨����������⣩���ܹ�ע�ᵽ�ڵ���
 * @author meilunsheng
 * @version 09.01
 */
public interface INode extends IRegistryableService<Object>, IStateSetable, IReceiver {
	public void dump();
	public void setAddress(IClient<?> protocol, byte[] address);
	public byte[] getAddress(IClient<?> protocol);
}
