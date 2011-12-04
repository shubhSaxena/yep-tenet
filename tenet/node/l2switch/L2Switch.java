package tenet.node.l2switch;

import java.util.Collection;
import java.util.HashMap;
import java.util.PriorityQueue;

import tenet.core.Simulator;
import tenet.node.INode;
import tenet.protocol.datalink.FrameParamStruct;
import tenet.protocol.datalink.IDataLinkLayer;
import tenet.protocol.datalink.MediumAddress;
import tenet.protocol.datalink.SEther.SimpleEthernetDatalink;
import tenet.protocol.interrupt.InterruptObject;
import tenet.protocol.interrupt.InterruptParam;
import tenet.util.pattern.serviceclient.IClient;

public class L2Switch extends InterruptObject implements INode {

	public class MACWithTimestamp implements Comparable<MACWithTimestamp> {

		public Double m_time;
		public MediumAddress m_mac;

		public MACWithTimestamp(Double m_time, MediumAddress m_mac) {
			super();
			this.m_time = m_time;
			this.m_mac = m_mac;
		}

		@Override
		public int compareTo(MACWithTimestamp arg0) {
			return m_time.compareTo(arg0.m_time);
		}

	}

	protected final static int TIMER = 0x00000001;

	/**
	 * �洢���㽻�����ϵ�����SimpleEthernetDatalink����Ϣ
	 */
	public HashMap<MediumAddress, SimpleEthernetDatalink> m_datalinks;

	/**
	 * �洢ת��Ŀ�ĵ�����Ӧ�ĳ���,��MAC��
	 */
	public HashMap<MediumAddress, SimpleEthernetDatalink> m_macport_map;
	
	public PriorityQueue<MACWithTimestamp> m_timeout;
	protected boolean m_state = false;

	public L2Switch() {
		super();
		wait(TIMER, 1.0); // ͨ����ʱ�ȴ���ÿ�봥��һ�μ�ʱ��
		m_datalinks = new HashMap<MediumAddress, SimpleEthernetDatalink>();
		m_macport_map = new HashMap<MediumAddress, SimpleEthernetDatalink>();
		m_timeout = new PriorityQueue<MACWithTimestamp>();
	}

	@Override
	public void disable() {
		if (!m_state)
			return;
		for (IDataLinkLayer iface : m_datalinks.values())
			iface.disable();
		m_state = false;
	}

	@Override
	public void dump() {

	}

	@Override
	public void enable() {
		if (m_state)
			return;
		for (IDataLinkLayer iface : m_datalinks.values())
			iface.enable();
		m_state = true;
	}

	/**
	 * ���mac��ַ����Ӧ�Ķ��㽻�����ϵ�SimpleEthernetDatalink
	 */
	public SimpleEthernetDatalink getDatalink(MediumAddress mac) {
		return m_datalinks.get(mac);
	}

	/**
	 * ��ö��㽻�����ϵ�����SimpleEthernetDatalink 
	 */
	public Collection<SimpleEthernetDatalink> getDatalinks() {
		return m_datalinks.values();
	}

	/**
	 * ��mac���л��mac��ַ����Ӧ�Ķ˿��ϵ�SimpleEthernetDatalink
	 */
	public SimpleEthernetDatalink getTransmitDatalink(MediumAddress mac) {
		return m_macport_map.get(mac);
	}

	@Override
	protected void interruptHandle(int signal, InterruptParam param) {
		switch (signal) {
		case TIMER:
			// TODO ���¿�ʼ��ʱ����ʱ
			this.resetInterrupt(TIMER);
			wait(TIMER, 1.0);
			// TODO ����Ƿ�����Ҫ�����mac��¼
			//I am lazy...
			
		}
	}
	

	@Override
	public boolean isEnable() {
		return m_state;
	}

	public void learnMAC(MediumAddress mac, SimpleEthernetDatalink iface) {
	    // TODO ����MAC����MAC��ַ������Ӧ��SimpleEthernetDatalink�������
		if (m_macport_map.containsKey(mac) && m_macport_map.get(mac)!=iface ) m_macport_map.remove(mac);
		m_macport_map.put(mac, iface);
	}

	@Override
	public void registryClient(IClient client) {
		// TODO ��һ��IDatalinkLayer���õ����㽻������
		if (client instanceof SimpleEthernetDatalink){
			m_datalinks.put((MediumAddress)client.getUniqueID(), (SimpleEthernetDatalink)client);
			client.attachTo(this);
		}
		
	}

	@Override
	public void unregistryClient(IClient<Object> client) {
		if (m_datalinks.containsKey((MediumAddress)client.getUniqueID())){
				m_datalinks.remove((MediumAddress)client.getUniqueID());
				client.detachFrom(this);
		}
	}

	@Override
	public void unregistryClient(Object id) {
		if (id instanceof MediumAddress){
			IClient<MediumAddress> client = this.m_datalinks.get(id);
			m_datalinks.remove(id);
			client.detachFrom(this);
		}
	}
	
	@Override
	public void setAddress(IClient<?> protocol, byte[] address){
		
	}
	
	@Override
	public byte[] getAddress(IClient<?> protocol){
		return null;
	}
	
}
