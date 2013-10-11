package com.wzm.test;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.wzm.invoker.HttpinvokeInterface;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		String path = Test.class.getResource("/").getPath()+"com/wzm/test/";
		ApplicationContext ctx = new FileSystemXmlApplicationContext(path + "httpinvoker-client.xml");

		HttpinvokeInterface service = (HttpinvokeInterface) ctx.getBean("httpinvokeService"); // ͨ���ӿڵõ���������Ϣ
		int result = service.getHello(); // ���÷������˵ķ���
		System.out.println(result);
		System.out.println("Զ�̵��ã����Գɹ�!");
	}

}