package tenet.protocol.datalink;

import java.util.Arrays;

import tenet.protocol.interrupt.InterruptParam;
import tenet.util.ByteLib;

/**
 * MAC�࣬������Ϊ�ж��źŵĲ��������õ���д��ʽΪaa:bb:cc:dd:ee:ff��ʽ��
 * @see tenet.protocol.interrupt.InterruptParam
 * @author meilunsheng
 * @version 09.01
 */
public class MediumAddress extends InterruptParam {
	/**
	 * �㲥MAC��ַ.
	 */
	public static final MediumAddress MAC_ALLONE = new MediumAddress(
			"FF:FF:FF:FF:FF:FF");
	/**
	 * 0 MAC.����ΪMAC��ַδ֪�ĺ��塣
	 */
	public static final MediumAddress MAC_ZERO = new MediumAddress(
			"00:00:00:00:00:00");

	/**
	 * ��һ��6λ�ı��ش�����һ��MAC�ࡣע������һ��byte������������ʽת����unsigned byte
	 * @param addr
	 * @return ��Ӧ��MAC��
	 */
	public static MediumAddress fromBytes(byte[] addr) {
		int[] t = new int[6];
		for (int i = 0; i < 6; ++i)
			t[i] = ByteLib.byteToUnsigned(addr[i], 0);
		return new MediumAddress(t);
	}

	/**
	 * ��һ��6��Ԫ�ص�������������һ��MAC�ࡣÿ�����������8bits��Ч
	 * @param addr
	 * @return ��Ӧ��MAC��
	 */
	public static MediumAddress fromBytes(int[] addr) {
		return new MediumAddress(addr);
	}

	/**
	 * ���ַ�������MAC��
	 * @param addr
	 * @return ��Ӧ��MAC��
	 */
	public static MediumAddress fromString(String addr) {
		return new MediumAddress(addr);
	}

	protected int[] address;

	public MediumAddress(int[] address) {
//		AssertLib
//				.AssertTrue(address != null && address.length == 6,
//						"MAC must be a 6 bytes array.[@MediumAddress.<constructor>(byte[])]");
		this.address = address.clone();
	}

	public MediumAddress(String address) {
		parse(address);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MediumAddress other = (MediumAddress) obj;
		if (!Arrays.equals(address, other.address))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(address);
		return result;
	}

	protected void parse(String addr) {
//		boolean check = false;
//		if (addr != null) {
//			String[] parts = addr.split("\\-");
//			if (parts.length == 6)
//				for (String part : parts)
//					check = check && part.matches("[0-9a-f][0-9a-f]");
//			else
//				check = false;
//		} else
//			check = false;
//		AssertLib.AssertTrue(check, "MAC format mistake.");
		String[] ad = addr.split(":");
		this.address = new int[6];
		for (int i = 0; i < 6; ++i)
			address[i] = Integer.valueOf(ad[i], 16);
	}

	/**
	 *  ����MAC��ĵ�ַת��Ϊ6�ֽ�byte��
	 * @return byte��
	 */
	public byte[] toBytes() {
		byte[] ret = new byte[6];
		for (int i = 0; i < 6; ++i)
			ret[i] = ByteLib.byteFromUnsigned(address[i], 0);
		return ret;
	}

	/**
	 * ����MAC��ĵ�ַת�����ַ���
	 * @return ���ϸ�ʽ��׼���ַ���
	 */
	public String toString() {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < 6; ++i)
			builder.append(i == 0 ? "" : ":").append(
					Integer.toHexString(this.address[i]));
		return builder.toString();
	}
}
