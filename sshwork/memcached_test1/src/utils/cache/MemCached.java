package utils.cache;

import java.util.Date;
import com.danga.MemCached.MemCachedClient;
import com.danga.MemCached.SockIOPool;

/**
 * ʹ��memcached�Ļ���ʵ����.
 * 
 * @author ��ľ����
 * 
 */
public class MemCached extends Thread{
	
	// ����ȫ�ֵ�Ψһʵ��
	protected static MemCachedClient mcc = null;

	protected static MemCached memCached = null;

	// �����뻺������������ӳ�
//	static {
//		// �������б����Ȩ��
//		String[] servers = { "192.168.1.4:11211" ,"192.168.1.5:11211"};
//		Integer[] weights = { 10,20 };
//
//		// ��ȡsocke���ӳص�ʵ������
//		SockIOPool pool = SockIOPool.getInstance();
//
//		// ���÷�������Ϣ
//		pool.setServers(servers);
//		pool.setWeights(weights);
//
//		// ���ó�ʼ����������С������������Լ������ʱ��
//		pool.setInitConn(5);
//		pool.setMinConn(5);
//		pool.setMaxConn(250);
//		pool.setMaxIdle(1000 * 60 * 60 * 6);
//
//		// �������̵߳�˯��ʱ��
//		pool.setMaintSleep(30);
//
//		// ����TCP�Ĳ��������ӳ�ʱ��
//		pool.setNagle(false);
//		pool.setSocketTO(3000);
////		pool.setSocketConnectTO(0);
//
//		// ��ʼ�����ӳ�
//		pool.initialize();
//
//		// ѹ�����ã�����ָ����С����λΪK�������ݶ��ᱻѹ��
////		mcc.setCompressEnable(true);
////		mcc.setCompressThreshold(64 * 1024);
//	}

	/**
	 * �����͹��췽����������ʵ������
	 * 
	 */
	protected MemCached() {

	}

	/**
	 * ��ȡΨһʵ��.
	 * 
	 * @return
	 */
	public synchronized static MemCached getInstance() {
		if(memCached==null) {
			// �������б����Ȩ��
			String[] servers = { "192.168.1.4:11211" };
			Integer[] weights = { 10};
	
			// ��ȡsocke���ӳص�ʵ������
			SockIOPool pool = SockIOPool.getInstance();
	
			// ���÷�������Ϣ
			pool.setServers(servers);
			pool.setWeights(weights);
	
			// ���ó�ʼ����������С������������Լ������ʱ��
			pool.setInitConn(5);
			pool.setMinConn(5);
			pool.setMaxConn(250);
			pool.setMaxIdle(1000 * 60 * 60 * 6);
	
			// �������̵߳�˯��ʱ��
			pool.setMaintSleep(30);
	
			// ����TCP�Ĳ��������ӳ�ʱ��
			pool.setNagle(false);
			pool.setSocketTO(3000);
	//				pool.setSocketConnectTO(0);
	
			// ��ʼ�����ӳ�
			pool.initialize();

		// ѹ�����ã�����ָ����С����λΪK�������ݶ��ᱻѹ��
//				mcc.setCompressEnable(true);
//				mcc.setCompressThreshold(64 * 1024);
			memCached = new MemCached();
			mcc = new MemCachedClient();
		}
		return memCached;
	}
	
	
	public static MemCachedClient getMemCachedClientInstance() {
		return mcc;
	}
	

	/**
	 * ���һ��ָ����ֵ��������.
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public boolean add(String key, Object value) {
		return mcc.add(key, value);
	}

	public boolean add(String key, Object value, Date expiry) {
		return mcc.add(key, value, expiry);
	}

	public boolean replace(String key, Object value) {
		return mcc.replace(key, value);
	}

	public boolean replace(String key, Object value, Date expiry) {
		return mcc.replace(key, value, expiry);
	}

	/**
	 * ����ָ���Ĺؼ��ֻ�ȡ����.
	 * 
	 * @param key
	 * @return
	 */
	public Object get(String key) {
		return mcc.get(key);
	}

	public static void main(String[] args) {
		MemCached cache = MemCached.getInstance();
		cache.start();
//		if(args[0].equals("add")) {
//			addObject();
//		} else if(args[0].equals("get")) {
//			getObject();
//		}
		
		
	}

	public static void addObject() {
		
		for(int i=0;i<1000;i++) {
			memCached.add("192.168.1.4_"+i, i);
			memCached.add("192.168.1.5_"+i, i);
		}
		
	}
	
	public static void getObject() {
		
		for(int i=0;i<1000;i++) {
			String key1 = "192.168.1.4_"+i;
			String key2 = "192.168.1.5_"+i;
			
			Object o1 =  memCached.get(key1);
			if(o1==null) {
				memCached.add(key1, i);
			}
			System.out.println(key1 + "  get value : " + memCached.get(key1));
			
			Object o2 =  memCached.get(key2);
			if(o2==null) {
				memCached.add(key2, i);
			}
			System.out.println(key2 + "  get value : " + memCached.get(key2));
		}
		
	}
}
