package tenet.protocol.statecontrol;

/**
 * ��ʹ�ܽӿڡ���ʾʵ������Ա�ʹ�ܣ����ã��ͽ��ã����ܲ�ѯ״̬��
 * @author meilunsheng
 * @version 09.01
 */
public interface IStateSetable {
	public void disable();

	public void enable();

	public boolean isEnable();
}
