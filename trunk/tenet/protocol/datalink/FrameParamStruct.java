package tenet.protocol.datalink;

import java.util.Arrays;

import tenet.protocol.interrupt.InterruptParam;
import tenet.util.ByteLib;

/**
 * ����Ϊһ��֡�ĳ���ṹ��������һ��֡������ļ�����.
 * ��������Datalink��ӿڵĲ������ݣ��Լ����Ӧ���ж��źŷ���ʱ�Ĳ���.
 * @see tenet.protocol.interrupt.InterruptParam
 * @author meilunsheng
 * @version 09.01
 *  
 */
public class FrameParamStruct extends InterruptParam {
	/**
	 * �þ�̬���̽�һ��byte��ת����һ��FrameParamStruct�ṹ.
	 * @param _data ��Ҫ��ת����byte��
	 * @return ת����Ľṹ
	 */
	public static FrameParamStruct fromBytes(byte[] _data) {
		return new FrameParamStruct(MediumAddress.fromBytes(Arrays.copyOfRange(
				_data, 0, 6)), MediumAddress.fromBytes(Arrays.copyOfRange(
				_data, 6, 12)), ByteLib.bytesToInt(
				Arrays.copyOfRange(_data, 12, 14), 0), Arrays.copyOfRange(
				_data, 14, _data.length));

	}

	/**
	 * ������
	 */
	public byte[] dataParam;
	/**
	 * Ŀ��MAC
	 */
	public MediumAddress destinationParam;
	/**
	 * ԴMAC
	 */
	public MediumAddress sourceParam;
	/**
	 * ��֡���ص��ϲ�Э���ethertype��ţ����ò���Ӧ��ֵ���ܵ����շ�˫��������жϱ�������
	 */
	public int typeParam;

	/**
	 * @param destinationParam Ŀ��MAC������Ϊ��
	 * @param sourceParam ԴMAC������Ϊ��
	 * @param typeParam ethertype������Ϊ��
	 * @param dataParam ���ݶΣ�����Ϊ��
	 */
	public FrameParamStruct(MediumAddress destinationParam,
			MediumAddress sourceParam, int typeParam, byte[] dataParam) {
		super();
		this.destinationParam = destinationParam;
		this.sourceParam = sourceParam;
		this.typeParam = typeParam;
		this.dataParam = dataParam;
	}

	/**
	 * ���ö���ת���ɶ�Ӧ�ı��ش���
	 * @return ת����Ĵ�
	 */
	public byte[] toBytes() {
		byte[] ret = new byte[14 + dataParam.length];
		System.arraycopy(destinationParam.toBytes(), 0, ret, 0, 6);
		System.arraycopy(sourceParam.toBytes(), 0, ret, 6, 6);
		ByteLib.bytesFromInt(ret, 12, typeParam);
		System.arraycopy(dataParam, 0, ret, 14, dataParam.length);
		return ret;
	}

	/**
	 * ����ԭ�е�Object.toString()�������֡����Ϣ�����ݶβ��ֽ����ֽڵ����ascii��.
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("DST=").append(destinationParam.toString()).append("\t");
		builder.append("SRC=").append(sourceParam.toString()).append("\t");
		builder.append("TYPE=").append(typeParam).append("\t");
		builder.append("DATA=");
		for (byte b : dataParam)
			builder.append(b).append(",");
		builder.append("EOF");
		return builder.toString();
	}
}
