package tenet.protocol;

/**
 * Э��ӿڣ�����Э�鶼��Ҫֱ�ӻ��ӵ�ʵ������ӿ�
 * @author meilunsheng
 * @version 09.01
 */
public interface IProtocol {
	/**
	 * 
	 */
	public void dump();

	/**
	 * �õ�Э��ı�ʶ
	 * @return Э��ı�ʶ
	 */
	public String getIdentify();

	/**
	 * ���ض��������
	 * @return ��������
	 */
	public String getName();
}
