package tenet.protocol.datalink.sfdatalink;

import java.util.ArrayList;

import tenet.core.Simulator;
import tenet.node.l2switch.L2Switch;
import tenet.protocol.datalink.FrameParamStruct;
import tenet.protocol.datalink.IDataLinkLayer;
import tenet.protocol.datalink.MediumAddress;
import tenet.protocol.datalink.IDataLinkLayer.ReceiveParam;
import tenet.protocol.datalink.IDataLinkLayer.ReceiveStatus;
import tenet.protocol.datalink.SEther.SimpleEthernetDatalink;
import tenet.protocol.interrupt.InterruptObject;
import tenet.protocol.physics.Link;
import tenet.util.pattern.serviceclient.IClient;

/**
* ���㽻����Ҫʹ�õ��ľ��д���ת��������IDatalinkLayer
* һ����˵��ֻ��L2Switch��ʹ�õ�StoreForwardingDatalink
* ͬʱ��StoreForwardingDatalink�󶨵���Nodeһ����L2Switch
*/
public class StoreForwardingDatalink extends SimpleEthernetDatalink {

	protected final static boolean debug=false;

    /**
     * ��Ҫת����֡�Ķ���
     */
	public ArrayList<FrameParamStruct> m_send_queue;

	public StoreForwardingDatalink(MediumAddress m_mac) {
		super(m_mac);
		m_send_queue = new ArrayList<FrameParamStruct>();
	}

	/**
	* ����·�Ͽ�ʱ���еĴ������
	*/
	@Override
	protected void linkDown() {
		super.linkDown(); //���ø�����̣�ʹ��״̬�ı���źŴﵽ������ضԶ���
		//���������жϵĵȴ�״̬��������յ���������жϵ����߼�����
		this.resetInterrupt(Link.INT_LINK_READ_ERROR);
		this.resetInterrupt(Link.INT_LINK_READ_OK);
		this.resetInterrupt(INT_RECEIVE_LINKDOWN);
		this.resetInterrupt(INT_RECEIVE_COLLISION);
		this.resetInterrupt(Link.INT_LINK_SEND_ERROR);
		this.resetInterrupt(Link.INT_LINK_SEND_OK);
		this.resetInterrupt(INT_SEND_LINKDOWN);
		this.resetInterrupt(INT_SEND_COLLISION);
		this.m_send_queue.clear();//���еȴ����͵�֡�����Ա�����
	}

	/**
	* ����·��Ϊ����״̬ʱ���еĴ���
	*/
	@Override
	protected void linkUp() {
		super.linkUp();//���ø�����̣�ʹ��״̬�ı���źŴﵽ������ضԶ���
		this.waitReceiveSignal();//�ȴ����н�����Ҫ���ж��ź�
	}

	void sendnext(){
		if(m_transmit_client.size()==0 && !m_send_queue.isEmpty()){
			transmitFrame(m_send_queue.get(0));
			m_send_queue.remove(0);
			
		}
			
	}
	//�������Щ���̶�����SimpleEthernetDatalink.interruptHandler�е��õĺ����������Ӧ���ж��źſ���ȥԭ�����鿴
	
	/**
	 * �������֡���ִ�������
	 */
	@Override
	protected void onReadError(FrameParamStruct param) {
		//drop the param
		//resetReceiveInterrupts(param.typeParam);
	}
	
	public static final MediumAddress BROADCAST_MA = MediumAddress.ZERO_MA;//new MediumAddress("FF:FF:FF:FF:FF:FF");
	private boolean isBroadcast(MediumAddress mac){
		if (mac.toString().equals(BROADCAST_MA.toString())) return true;
		return false;
	}
	/**
	 * �������֡��ȷ�����
	 */
	@Override
	protected void onReadOK(FrameParamStruct param) {
		//resetReceiveInterrupts(param.typeParam);
		if (m_node instanceof L2Switch){
			L2Switch Switch = (L2Switch)m_node;
			Switch.learnMAC(param.sourceParam,this);
			if (!isBroadcast(param.destinationParam) && Switch.m_macport_map.containsKey(param.destinationParam)){
				SimpleEthernetDatalink des = Switch.m_macport_map.get(param.destinationParam);
				((StoreForwardingDatalink)des).m_send_queue.add(param);
				((StoreForwardingDatalink)des).sendnext();
			}else
				for (SimpleEthernetDatalink DataLink: Switch.m_datalinks.values()){
					((StoreForwardingDatalink)DataLink).m_send_queue.add(param);
					((StoreForwardingDatalink)DataLink).sendnext();
				}
		}
	}

	/**
	 * �������֡����У���������
	 */
	@Override
	protected void onReadOKwithCheckError(FrameParamStruct param) {
		//drop the param
		//resetReceiveInterrupts(param.typeParam);
	}

	/**
	 * ������ֽ��ճ�ͻ�����жϵ����
	 */
	@Override
	protected void onReceiveRequireCollision(FrameParamStruct param) {
		//drop the param
	}

	/**
	 * ������ַǽ���״̬���յ����жϵ����
	 */
	@Override
	protected void onReceiveRequireLinkDown(FrameParamStruct param) {
		//drop the param
		//resetReceiveInterrupts(param.typeParam);
	}

	/**
	 * ������֡���ִ�������
	 */
	@Override
	protected void onSendError(FrameParamStruct param) {
		//drop the param and sent next
		resetTransmitInterrupts(param.typeParam);
		sendnext();
	}

	/**
	 * ����ɹ�����֡�����
	 */
	@Override
	protected void onSendOK(FrameParamStruct param) {
		//sent next
		resetTransmitInterrupts(param.typeParam);
		sendnext();
	}

	/**
	 * ������ַ��ͳ�ͻ�����жϵ����
	 */
	@Override
	protected void onTransmitRequireCollision(FrameParamStruct param) {
		//drop the param and sent next
		resetTransmitInterrupts(param.typeParam);
		sendnext();
	}

	/**
	 * ������ַǽ���״̬���͵����жϵ����
	 */
	@Override
	protected void onTransmitRequireLinkDown(FrameParamStruct param) {
		//drop the param and sent next
		resetTransmitInterrupts(param.typeParam);
		sendnext();
	}
}
