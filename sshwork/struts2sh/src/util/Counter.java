package util;

/**
 * ������
 * @author BeanSoft
 *
 */
public class Counter {
	private long start = 0L;// ��һ�������ļ����س�ʼֵ, ����ʱ��ֵ�����ļ�
	
	private static Counter instance;
	
	// ��������private
	private Counter() {
		
	}
	
	// ��¶һ����ȡʵ���ľ�̬����
	public static synchronized Counter getInstance() {
		if(instance == null) {
			instance = new Counter();
		}
		
		return instance;
	}
	
	public synchronized long nextValue() {
		return ++start;
	}
	
	public static void main(String[] args) {
		Counter a = new Counter();
		Counter b = new Counter();
		a.start = b.start;
	}
	
}