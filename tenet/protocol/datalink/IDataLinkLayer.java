package tenet.protocol.datalink;

import tenet.protocol.IProtocol;
import tenet.protocol.interrupt.InterruptObject;
import tenet.protocol.interrupt.InterruptParam;
import tenet.protocol.statecontrol.IStateSetable;
import tenet.util.pattern.serviceclient.IClient;

/**
 * Datalink��ı�׼�ӿ�.<br>
 * �ýӿڿ�ʹ��.<br>
 * �ýӿ�Ϊ��ΪIClient������Ϊ{@link tenet.protocol.datalink.MediumAddress}��������Ϊ�ýӿڵ�ʵ����MAC��ַ.<br>
 * @see tenet.protocol.statecontrol.IStateSetable
 * @see tenet.util.pattern.serviceclient
 * @see tenet.protocol.datalink.MediumAddress
 * @author meilunsheng
 * @version 09.01
 */
public interface IDataLinkLayer extends IStateSetable, IProtocol,
		IClient<MediumAddress> {

	/**
	 * ���ܹ��̵��ж��źŲ���.<br>
	 * �ض����� {@link IDataLinkLayer#INT_FRAME_RECEIVE}�жϷ���
	 * @see IDataLinkLayer#INT_FRAME_RECEIVE
	 * @author meilunsheng
	 * @version 09.01
	 */
	public class ReceiveParam extends InterruptParam {
		/**
		 * ���÷����ߣ������ж��ߣ�
		 */
		public InterruptObject caller;
		/**
		 * �жϷ����ߣ�����Ӧ��Datalinkʵ����
		 */
		public IDataLinkLayer datalink;
		/**
		 * ���ز���.��Ӧ��ͬ��{@link #status}���в�ͬ�ĺ���.
		 * @see tenet.protocol.datalink.IDataLinkLayer.ReceiveStatus
		 * @see #status
		 */
		public FrameParamStruct frame;
		/**
		 * ���ܹ��̵����״̬.
		 * @see tenet.protocol.datalink.IDataLinkLayer.ReceiveStatus
		 */
		public ReceiveStatus status;

		public ReceiveParam(ReceiveStatus status, InterruptObject caller,
				IDataLinkLayer datalink, FrameParamStruct frame) {
			super();
			this.status = status;
			this.caller = caller;
			this.datalink = datalink;
			this.frame = frame;
		}
	}

	/**
	 * ���պ����жϷ���״̬��ö��.
	 * @author meilunsheng
	 * @version 09.01
	 */
	public enum ReceiveStatus {
		/**
		 * datalink�㴦�ڷ�����״̬ʱ��������{@link tenet.protocol.datalink.IDataLinkLayer#receiveFrame(FrameParamStruct)}���ᵼ�µ����߽��յ�����ж�.<br>
		 * �жϵ�frame������Ϊ����{@link tenet.protocol.datalink.IDataLinkLayer#receiveFrame(FrameParamStruct)}ʱ��param����.
		 */
		dataLinkOff, 
		/**
		 * ���յ���Ӧethertype��֡�����Ǹ�֡����У�����<br>
		 * �жϵ�frame������Ϊʵ�ʽ��յ�frame
		 */
		frameCheckError, /**
		 * ��ͬһethertype�Ѿ��е���{@link tenet.protocol.datalink.IDataLinkLayer#receiveFrame(FrameParamStruct)}������Ȼ���ڽ��չ�����ʱ����һ�ε�{@link tenet.protocol.datalink.IDataLinkLayer#receiveFrame(FrameParamStruct)}���ý����̷��ظ��ж�.<br>
		 * �жϵ�frame������Ϊ��һ�ε���{@link tenet.protocol.datalink.IDataLinkLayer#receiveFrame(FrameParamStruct)}ʱ��param����.
		 */
		receiveCollision, /**
		 * �ڵ���receiveFrame�������κ�ԭ����datalink���ڴ��ڿ��Խ��н���֡��״̬ʱ������������ж�.<br>
		 * ��Ҫע����ǣ��������datalink���ٴ�������״̬�������޷����գ�������״̬Ϊ{@link #receiveCollision}�жϣ������ᴥ��״̬Ϊ{@link #dataLinkOff}.<br>
		 * �жϵ�frame������Ϊ����{@link tenet.protocol.datalink.IDataLinkLayer#receiveFrame(FrameParamStruct)}ʱ��param����.
		 */
		receiveError, /**
		 * ���յ���Ӧethertype��֡����֡У����ȷ��<br>
		 * �жϵ�frame������Ϊʵ�ʽ��յ�frame
		 */
		receiveOK
	}

	/**
	 * ������̵��ж��źŲ���.<br>
	 * �ض�����{@link IDataLinkLayer#INT_FRAME_TRANSMIT} �жϷ���
	 * @see IDataLinkLayer#INT_FRAME_TRANSMIT
	 * @author meilunsheng
	 * @version 09.01
	 */
	public class TransmitParam extends InterruptParam {
		/**
		 * ���÷����ߣ������ж��ߣ�
		 */
		public InterruptObject caller;
		/**
		 * �жϷ����ߣ�����Ӧ��Datalinkʵ����
		 */
		public IDataLinkLayer datalink;
		/**
		 * ���ز���.��Ӧ��ͬ��{@link #status}���в�ͬ�ĺ���.
		 * @see tenet.protocol.datalink.IDataLinkLayer.TransmitStatus
		 * @see #status
		 */
		public FrameParamStruct frame;
		/**
		 * ������̵����״̬.
		 * @see tenet.protocol.datalink.IDataLinkLayer.TransmitStatus
		 */
		public TransmitStatus status;

		public TransmitParam(TransmitStatus status, InterruptObject caller,
				IDataLinkLayer datalink, FrameParamStruct frame) {
			super();
			this.status = status;
			this.caller = caller;
			this.datalink = datalink;
			this.frame = frame;
		}
	}
	/**
	 * ���亯���жϷ���״̬��ö��.
	 * @author meilunsheng
	 * @version 09.01
	 */
	public enum TransmitStatus {
		/**
		 * datalink�㴦�ڷ�����״̬ʱ��������{@link tenet.protocol.datalink.IDataLinkLayer#transmitFrame(FrameParamStruct)}���ᵼ�µ����߽��յ�����ж�.<br>
		 * �жϵ�frame������Ϊ����transmitFrame{@link tenet.protocol.datalink.IDataLinkLayer#transmitFrame(FrameParamStruct)}ʱ��param����.
		 */
		dataLinkOff, 
		/**
		 * ��ͬһdatalink�Ѿ��е���{@link tenet.protocol.datalink.IDataLinkLayer#transmitFrame(FrameParamStruct)}������Ȼ���ڽ��չ�����ʱ����һ�ε�{@link tenet.protocol.datalink.IDataLinkLayer#transmitFrame(FrameParamStruct)}���ý����̷��ظ��ж�.<br>
		 * �жϵ�frame������Ϊ��һ�ε���{@link tenet.protocol.datalink.IDataLinkLayer#transmitFrame(FrameParamStruct)}ʱ��param����.
		 */
		transmitCollision, 
		/**
		 * �ڵ���transmitFrame�������κ�ԭ����datalink���ڴ��ڿ��Խ��д���֡��״̬���ߴ�������ݴ��ڶ�Ӧ��datalink���MTUʱ������������ж�.<br>
		 * ��Ҫע����ǣ��������datalink���ٴ�������״̬�������޷����䣬������״̬Ϊ{@link #transmitCollision}�жϣ������ᴥ��״̬Ϊ{@link #dataLinkOff}.<br>
		 * �жϵ�frame������Ϊ����{@link tenet.protocol.datalink.IDataLinkLayer#transmitFrame(FrameParamStruct)}ʱ��param����.
		 */
		transmitError, 
		/**
		 * ������ɴ��亯����ִ��
		 * �жϵ�frame������Ϊ����{@link tenet.protocol.datalink.IDataLinkLayer#transmitFrame(FrameParamStruct)}ʱ��param����.
		 */
		transmitOK
	}

	/**
	 * �ɵȴ��ж��źš�����datalink������{@link #receiveFrame(FrameParamStruct)}�󣬵�����������ʱ�����߱����ź��жϣ�����Ϊһ��{@link tenet.protocol.datalink.IDataLinkLayer.ReceiveParam}
	 * @see tenet.protocol.datalink.IDataLinkLayer.ReceiveParam
	 */
	public final static int INT_FRAME_RECEIVE = 0x80000200;
	/**
	 * �ɵȴ��ж��źš�����datalink������{@link #receiveFrame(FrameParamStruct)}�󣬵�����������ʱ�����ϲ�Э����ᱻ���ź��жϣ�����Ϊ��datalink��MAC
	 */
	public final static int INT_FRAME_RECEIVE_READY = 0x80000600;
	/**
	 * �ɵȴ��ж��źš�����datalink������{@link #transmitFrame(FrameParamStruct)}�󣬵�����������ʱ�����߱����ź��жϣ�����Ϊһ��{@link tenet.protocol.datalink.IDataLinkLayer.TransmitParam}
	 * @see tenet.protocol.datalink.IDataLinkLayer.TransmitParam
	 */
	public final static int INT_FRAME_TRANSMIT = 0x80000100;
	/**
	 * �ɵȴ��ж��źš�����datalink������{@link #transmitFrame(FrameParamStruct)}�󣬵�����������ʱ�����ϲ�Э����ᱻ���ź��жϣ�����Ϊ��datalink��MAC
	 */
	public final static int INT_FRAME_TRANSMIT_READY = 0x80000500;
	/**
	 * �ɵȴ��ж��źš���datalink���ڽ���״̬ʱ���������û�������������ӵ�datalink�����ã�����������ʱ����ã���ᵼ���豸ת��Ϊ�ǽ���״̬��ͬʱ�������ϲ�Э�鷢�͸��ж��źš�����Ϊ��datalink��MAC
	 */
	public final static int INT_INTERFACE_DOWN = 0x80000300;
	/**
	 * �ɵȴ��ж��źš���datalink���ڷǽ���״̬ʱ����������������ӵ�datalink�Լ�������ʾ���ʹ�ܣ���ᵼ���豸ת��Ϊ����״̬��ͬʱ�������ϲ�Э�鷢�͸��ж��źš�����Ϊ��datalink��MAC
	 */
	public final static int INT_INTERFACE_UP = 0x80000400;

	/**
	 * �ж��豸�Ƿ��ڽ���״̬
	 * @return <b>true</b> ����״̬�� <b>false</b> �ǽ���״̬
	 */
	public boolean isLinkUp();

	/**
	 * �������һ��֡��
	 * @param param ���ղ���<br>
	 * ����Ҫ��param��ethertypeͨ������ΪЭ���Ӧ��ethertype�����������Խ���û��ʵ�����á����ò����ڷ��������յ�����¿���ͨ���ж��������ء�
	 * @see FrameParamStruct
	 * @see ReceiveParam
	 * @see ReceiveStatus
	 * @see #INT_FRAME_RECEIVE
	 * @see #INT_FRAME_RECEIVE_READY
	 */
	public void receiveFrame(FrameParamStruct param);

	/**
	 * ������һ��֡.
	 * @param param ���Ͳ���
	 * @see FrameParamStruct
	 * @see TransmitParam
	 * @see TransmitStatus
	 * @see #INT_FRAME_TRANSMIT
	 * @see #INT_FRAME_TRANSMIT_READY
	 */
	public void transmitFrame(FrameParamStruct param);
	
	/**
	 * ���MTU
	 * @return MTU
	 */
	public int getMTU();
}
