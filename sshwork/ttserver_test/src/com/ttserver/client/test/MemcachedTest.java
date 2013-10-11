package com.ttserver.client.test;

import com.danga.MemCached.MemCachedClient;
import com.danga.MemCached.SockIOPool;

/**
 * �����õ���java_memcached-release-2.5.1.jar��
 *      ���ص�ַ�� http://github.com/gwhalin/Memcached-Java-Client/downloads
 * 
 * @author Administrator
 * 
 */
public class MemcachedTest {
    String[] servers = { "192.168.1.4:11211" };

    Integer[] weights = { 3 };
    MemCachedClient mcc = new MemCachedClient();
    // ����һ��ʵ������SockIOPool
    SockIOPool pool = SockIOPool.getInstance();

    public MemcachedTest() {
        pool.setServers(servers);
        pool.setWeights(weights);

        pool.setInitConn(5);
        pool.setMinConn(5);
        pool.setMaxConn(250);
        pool.setMaxIdle(30);

        pool.setMaintSleep(30);
        pool.initialize();
    }

    public void putObject() {
        for (int i = 1; i < 10; i++) {
            Boolean b = mcc.add("key" + i, "hi, michael" + i); // �����ǰ���ڣ���ȥ����
            System.out.println("key" + i + b);
        }
    }

    public void replaceObject() {
        Boolean b = mcc.replace("key2", "hi, tom"); // ���key�����ڣ��򷵻�flase������ʧ��
        System.out.println("update " + b);
    }

    public void getObject() {
        Object obj = mcc.get("key2");
        System.out.println(obj);
    }

    
    public static void main(String args[]) {
        MemcachedTest tc = new MemcachedTest();
         tc.putObject();
        // tc.replaceObject();
         tc.getObject();

        tc.pool.shutDown();

    }
}