package com.wzm.test;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.wzm.server.service.ssq.SsqRecordService;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		String path = Test.class.getResource("/").getPath();
		ApplicationContext ctx = new FileSystemXmlApplicationContext(path + "httpinvoker-client.xml");

		SsqRecordService service = (SsqRecordService) ctx.getBean("ssqRecordService"); // ͨ���ӿڵõ���������Ϣ
//		service.writeAllSsqBaseStats(); // ���÷������˵ķ���
		System.out.println("Զ�̵��ã����Գɹ�!");
	}

}