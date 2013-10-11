package com.ssq.common.util;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Queue;

/**
 * @author Floder
 * @version 0.1 2008-01-30
 * @�����ǲ���Ͷע������
 */
public class CombinationUtil {
	
	/**
	 * �˷����Ǵ�һ��(source.length��)����������ȡ��n��Ԫ�ؽ�����ϣ���������б�ķ�����
	 * ����Ҫ1��2��3��4��5��6��7��8��9��ȡ6����������ϣ������ж��������(��������)
	 * @param source  ��������
	 * @param n  ȡ����Ԫ�ؽ�����ϣ�����С��source�ĳ��ȣ�����1��
	 * @param dyn  ��̬����û����һ�μ�1����ʼʱ��n���
	 * @param result java.util.List<String> �����
	 */
	public static void createCombo(Object[]source, int n,int dyn,List<String> result){
		createCombo(source, n, dyn, result, ",");
	}
	
	/**
	 * �˷����Ǵ�һ��(source.length��)����������ȡ��n��Ԫ�ؽ�����ϣ���������б�ķ�����
	 * ����Ҫ1��2��3��4��5��6��7��8��9��ȡ6����������ϣ������ж��������(��������)
	 * @param source  ��������
	 * @param n  ȡ����Ԫ�ؽ�����ϣ�����С��source�ĳ��ȣ�����1��
	 * @param dyn  ��̬����û����һ�μ�1����ʼʱ��n���
	 * @param result java.util.List<String> �����
	 * @param joinStr �����ַ���
	 */
	public static void createCombo(Object[]source, int n,int dyn,List<String> result,String joinStr){
		if(source.length==2) {
			if(dyn==2) {
				String str = source[0]+joinStr+source[1];
				result.add(str);
			} else {
				
			}
		} else {
			if(dyn==2 ) { //&& source.length>=n) {
				if(dyn==n) {
					
					for(int i=0;i<source.length-1;i++) {
						for(int j=i+1;j<source.length;j++) {
							StringBuffer sb = new StringBuffer();
							sb.append(source[i]+joinStr+source[j]);
							sb.append(joinStr);
							sb.append(source[j]);
							result.add(sb.toString());
						}
					}
				} else {
					for(int j=1;j<source.length;j++) {
						String str = source[0]+joinStr+source[j];
						result.add(str);
					}
				}
			} else {
				if(dyn>n-1) {
					for(int i=0;i<source.length-n+1;i++) {
						for(int j=i+1;j<source.length;j++) {
							String str = source[i]+joinStr+source[j];
							Object[] oo = new Object[source.length-j];
							oo[0] = str;
							for(int x=j+1,index=1;x<source.length;x++,index++) {
								oo[index] = source[x];
							}
							createCombo(oo, n,dyn-1,result,joinStr);
						}
					}
				}
				else {
					for(int j=1;j<source.length;j++) {
						String str = source[0]+joinStr+source[j];
						Object[] oo = new Object[source.length-j];
						oo[0] = str;
						for(int x=j+1,index=1;x<source.length;x++,index++) {
							oo[index] = source[x];
						}
						createCombo(oo, n,dyn-1,result,joinStr);
					}
				}	
			}
		}
	}
	
	
//	public static void createCombo2(Object[]source, int n,int dyn, Queue<String> result){
//		if(source.length==2) {
//			if(dyn==2) {
//				String str = source[0]+","+source[1];
//				while(result.size()>=100000) {
//					try {
//						Thread.sleep(1000);
//					} catch (InterruptedException e) {
//						e.printStackTrace();
//					}
//				}
//				result.offer(str);
//			} 
//		} else {
//			if(dyn==2 ) { //&& source.length>=n) {
//				if(dyn==n) {
//					
//					for(int i=0;i<source.length-1;i++) {
//						for(int j=i+1;j<source.length;j++) {
//							StringBuffer sb = new StringBuffer();
//							sb.append(source[i]+","+source[j]);
//							sb.append(",");
//							sb.append(source[j]);
////							result.add(sb.toString());
//							
//							while(result.size()>=100000) {
//								try {
//									Thread.sleep(1000);
//								} catch (InterruptedException e) {
//									e.printStackTrace();
//								}
//							}
//							result.offer(sb.toString());
//						}
//					}
//				} else {
//					for(int j=1;j<source.length;j++) {
//						String str = source[0]+","+source[j];
////						result.add(str);
//						
//						while(result.size()>=100000) {
//							try {
//								Thread.sleep(1000);
//							} catch (InterruptedException e) {
//								e.printStackTrace();
//							}
//						}
//						result.offer(str);
//					}
//				}
//			} else {
//				if(dyn>n-1) {
//					for(int i=0;i<source.length-n+1;i++) {
//						for(int j=i+1;j<source.length;j++) {
//							String str = source[i]+","+source[j];
//							Object[] oo = new Object[source.length-j];
//							oo[0] = str;
//							for(int x=j+1,index=1;x<source.length;x++,index++) {
//								oo[index] = source[x];
//							}
//							createCombo2(oo, n,dyn-1,result);
//						}
//					}
//				}
//				else {
//					for(int j=1;j<source.length;j++) {
//						String str = source[0]+","+source[j];
//						Object[] oo = new Object[source.length-j];
//						oo[0] = str;
//						for(int x=j+1,index=1;x<source.length;x++,index++) {
//							oo[index] = source[x];
//						}
//						createCombo2(oo, n,dyn-1,result);
//					}
//				}	
//			}
//		}
//	}
	
	
	/**
	 * ��str���а�����������3,2,1 �����Ϊ1,2,3
	 * @param str
	 * @return
	 */
	public static String sortStr(String str) {
		String[] tmp = str.split(",");
		List<Integer> intTmp = new ArrayList<Integer>();
		for(int i=0;i<tmp.length;i++) {
			intTmp.add(new Integer(Integer.parseInt(tmp[i])));
		}
		Collections.sort(intTmp);
		
		StringBuffer sb = new StringBuffer();
		for(int i=0;i<intTmp.size();i++){
			sb.append(  (((Integer)intTmp.get(i)).intValue()<10?"0"+intTmp.get(i):intTmp.get(i))+",");
		}
		return sb.toString().substring(0,sb.toString().length()-1);
	}
	
	/**
	 * ���������Ŀ
	 * @param n1    �ϱ꣬��6
	 * @param n2    �±꣬�ش��ڵ����ϱ꣬��33
	 * @return
	 */
	public static long caclCombinationCount(long n1, long n2) {
		long m = 1;
		long n = 1;
		
		for(long i=n1;i>=1;i--,n2--) {
			m = m*n2;
			n = n*i;
		}
		
		return m/n;
	}
	
	public static List<String> mergeTwoCollection(List<String> l1, List<String> l2) {
		List<String> result = new ArrayList<String>();
		for(String str1:l1) {
			for(String str2:l2) {
				String str = sortStr(str1 + "," +str2);
				result.add(str);
			}
		}
		return result;
	}
	
	public static void main(String[] args) {
		Object[] m = {3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33}; 
		
		List<String> dd1 = new ArrayList<String>();
		CombinationUtil.createCombo(m,5,5,dd1);
		
		List<String> dd2 = new ArrayList<String>();
		dd2.add(2+"");
		
		List<String> dd = mergeTwoCollection(dd1, dd2);
		
		System.out.println(dd);
		
	}
}
