package util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


/**
 * ʹ�� JDK �Դ��� MD5 �㷨ʵ��ժҪ.
 * @author BeanSoft
 *
 */
public class MD5Bean {

	/**
	 * MD5 ժҪ����.
	 * @param input
	 * @return
	 */
	public static String md5(byte[] input) {
		//1. ��ȡժҪ�㷨����
		 MessageDigest md5;
		try {
			md5 = MessageDigest.getInstance("MD5");
			 // 2. �����Դ���� => byte[] "123456".getBytes()
			 // 3. ���� digest(byte[]) ==> byte[] ժҪ�ֽ�
			 byte[] result = md5.digest(input);
			 
			 StringBuffer sb = new StringBuffer();
			 
			 // 4. �Ѷ�����ת����16�����ַ�������ʾ
			 for(int i = 0; i < result.length; i++) {
				 String hex = (Integer.toHexString(result[i]));// byte ��������Ϊ int, 16���ƴ���2λ, ֻ�������λ��Ч
//				 System.out.println("hex=" + hex);
				 // �������� 16 �����ַ���
				 if(hex.length() > 2) {
					 hex = hex.substring(hex.length() - 2);
				 }
				 
				 // ��3ǰ�����, ��� 03
				 if(hex.length() < 2) {
					 hex = "0" + hex;
				 }

				 sb.append(hex.toUpperCase()); // ת�ɴ�д
			 }
			 
//			 System.out.println(sb);// ���
			 return sb.toString();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;

	}
	
	/**
	 * ���ַ���ʹ��Ĭ�ϱ���MD5ժҪ.
	 * @param string
	 * @return
	 */
	public static String md5(String string) {
		return md5(string.getBytes());
	}
	
	/**
	 * ���ַ���ʹ�ø�������MD5ժҪ.
	 * @param string
	 * @param encoding
	 * @return
	 */
	public static String md5(String string, String encoding) {
		try {
			return md5(string.getBytes(encoding));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}	
	
	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		System.out.println(md5("1234".getBytes()));
		System.out.println(Integer.toBinaryString(2008));
	}

}
