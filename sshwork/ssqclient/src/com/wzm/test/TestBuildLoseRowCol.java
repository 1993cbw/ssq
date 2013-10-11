package com.wzm.test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ssq.common.util.FileUtil;


public class TestBuildLoseRowCol {
	
	public static final int COL_TYPE = 1;
	public static final int ROW_TYPE = 2;
	
	
	// Ĭ�϶�������
	private int type =1;
	
	public static List<Map<Integer, String>> rowList = new ArrayList<Map<Integer,String>>();
	
	private static Map<Integer, String> rowMap1 = new HashMap<Integer, String>();
	private static Map<Integer, String> rowMap2 = new HashMap<Integer, String>();
	private static Map<Integer, String> rowMap3 = new HashMap<Integer, String>();
	private static Map<Integer, String> rowMap4 = new HashMap<Integer, String>();
	private static Map<Integer, String> rowMap5 = new HashMap<Integer, String>();
	private static Map<Integer, String> rowMap6 = new HashMap<Integer, String>();
	
	
	public TestBuildLoseRowCol(int type) {
		this.type = type;
		
		rowList.clear();
		rowMap1.clear();
		rowMap2.clear();
		rowMap3.clear();
		rowMap4.clear();
		rowMap5.clear();
		rowMap6.clear();
		
		if(this.type ==1) {
			initColType();
		} else if(this.type ==2) {
			initRowType();
		} else {
			initOtherType();
		}
		
	}

	private void initColType() {
		rowMap1.put(1, null);
		rowMap1.put(7, null);
		rowMap1.put(13, null);
		rowMap1.put(19, null);
		rowMap1.put(25, null);
		rowMap1.put(31, null);
		
		rowMap2.put(2, null);
		rowMap2.put(8, null);
		rowMap2.put(14, null);
		rowMap2.put(20, null);
		rowMap2.put(26, null);
		rowMap2.put(32, null);
		
		rowMap3.put(3, null);
		rowMap3.put(9, null);
		rowMap3.put(15, null);
		rowMap3.put(21, null);
		rowMap3.put(27, null);
		rowMap3.put(33, null);
		
		rowMap4.put(4, null);
		rowMap4.put(10, null);
		rowMap4.put(16, null);
		rowMap4.put(22, null);
		rowMap4.put(28, null);
		
		rowMap5.put(5, null);
		rowMap5.put(11, null);
		rowMap5.put(17, null);
		rowMap5.put(23, null);
		rowMap5.put(29, null);
		
		rowMap6.put(6, null);
		rowMap6.put(12, null);
		rowMap6.put(18, null);
		rowMap6.put(24, null);
		rowMap6.put(30, null);
		
		addToTotal();
	}
	
	private void initOtherType() {
		rowMap1.put(1, null);
		rowMap1.put(7, null);
		rowMap1.put(20, null);
		rowMap1.put(19, null);
		rowMap1.put(32, null);
		rowMap1.put(31, null);
		
		rowMap2.put(2, null);
		rowMap2.put(8, null);
		rowMap2.put(14, null);
		rowMap2.put(13, null);
		rowMap2.put(26, null);
		rowMap2.put(25, null);
		
		rowMap3.put(3, null);
		rowMap3.put(9, null);
		rowMap3.put(28, null);
		rowMap3.put(21, null);
		rowMap3.put(27, null);
		rowMap3.put(10, null);
		
		rowMap4.put(04, null);
		rowMap4.put(33, null);
		rowMap4.put(16, null);
		rowMap4.put(22, null);
		rowMap4.put(15, null);
		
		rowMap5.put(5, null);
		rowMap5.put(11, null);
		rowMap5.put(30, null);
		rowMap5.put(18, null);
		rowMap5.put(29, null);
		
		rowMap6.put(6, null);
		rowMap6.put(12, null);
		rowMap6.put(23, null);
		rowMap6.put(24, null);
		rowMap6.put(17, null);
		
		addToTotal();
		
	}
	
	private void initRowType() {
		rowMap1.put(1, null);
		rowMap1.put(2, null);
		rowMap1.put(3, null);
		rowMap1.put(4, null);
		rowMap1.put(5, null);
		rowMap1.put(6, null);
		
		rowMap2.put(7, null);
		rowMap2.put(8, null);
		rowMap2.put(9, null);
		rowMap2.put(10, null);
		rowMap2.put(11, null);
		rowMap2.put(12, null);
		
		rowMap3.put(13, null);
		rowMap3.put(14, null);
		rowMap3.put(15, null);
		rowMap3.put(16, null);
		rowMap3.put(17, null);
		rowMap3.put(18, null);
		
		rowMap4.put(19, null);
		rowMap4.put(20, null);
		rowMap4.put(21, null);
		rowMap4.put(22, null);
		rowMap4.put(23, null);
		rowMap4.put(24, null);
		
		rowMap5.put(25, null);
		rowMap5.put(26, null);
		rowMap5.put(27, null);
		rowMap5.put(28, null);
		rowMap5.put(29, null);
		rowMap5.put(30, null);
		
		rowMap6.put(31, null);
		rowMap6.put(32, null);
		rowMap6.put(33, null);
		
		addToTotal();
	}

	private void addToTotal() {
		rowList.add(rowMap1);
		rowList.add(rowMap2);
		rowList.add(rowMap3);
		rowList.add(rowMap4);
		rowList.add(rowMap5);
		rowList.add(rowMap6);
	}
	
	public static void main(String[] args) {
		
	}

	public static void buildFeature(int type, String fileName) {
		
		List<String> result = new ArrayList<String>();
		
		new TestBuildLoseRowCol(type);
		
		List<String> list = new ArrayList<String>();
		try {
			list = FileUtil.getSSQHistoryList();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// ������ʧ3�л�3�������
		int maxContinueLose3Step = 0;
		int maxContinueLose3StepTmp = 0;
		String maxContinueLose3StepIndexStr = "";
		
		String allContinueLose3Str = "";
		int count1 =0;
		
		// ��ʧ3�л�3���������
		int maxSpaceLose3Step = 0;
		int maxSpaceLose3StepTmp = 0;
		String maxSpaceLose3StepIndexStr = "";
		
		// ������ʧ2�л�2�������
		int maxContinueLose2Step = 0;
		int maxContinueLose2StepTmp = 0;
		String maxContinueLose2StepIndexStr = "";
		
		String allContinueLose2Str = "";
		int count2 =0;
		
		// ��ʧ2�л�2���������
		int maxSpaceLose2Step = 0;
		int maxSpaceLose2StepTmp = 0;
		String maxSpaceLose2StepIndexStr = "";
		
		
		// ������ʧ1�л�1�������
		int maxContinueLose1Step = 0;
		int maxContinueLose1StepTmp = 0;
		String maxContinueLose1StepIndexStr = "";
		
		String allContinueLose1Str = "";
		int count3 =0;
		
		// ��ʧ1�л�1���������
		int maxSpaceLose1Step = 0;
		int maxSpaceLose1StepTmp = 0;
		String maxSpaceLose1StepIndexStr = "";
		
		String lastStr = "";
		String lastStrs = "";
		int count4 =0;
		
		Map<String, Integer> statsMap = new HashMap<String, Integer>();
		
		for(String str:list) {
			String[] strs = str.substring(8).substring(0,17).replaceAll(" ", ",").split(",");

			int t1 = Integer.parseInt(strs[0]);
			int t2 = Integer.parseInt(strs[1]);
			int t3 = Integer.parseInt(strs[2]);
			int t4 = Integer.parseInt(strs[3]);
			int t5 = Integer.parseInt(strs[4]);
			int t6 = Integer.parseInt(strs[5]);
			
			boolean[] b = new boolean[6];
			for(int i=0;i<6;i++) {
				if(rowList.get(i).containsKey(t1)) {
					b[i] = true;
				}
				
				if(rowList.get(i).containsKey(t2)) {
					b[i] = true;
				}
				
				if(rowList.get(i).containsKey(t3)) {
					b[i] = true;
				}
				
				if(rowList.get(i).containsKey(t4)) {
					b[i] = true;
				}
				
				if(rowList.get(i).containsKey(t5)) {
					b[i] = true;
				}
				
				if(rowList.get(i).containsKey(t6)) {
					b[i] = true;
				}
				
			}
			
			String str1 = "";
			for(int i=0;i<6;i++) {
				if(!b[i]) {
					str1 = str1+(i+1);
				} 
 			}
			
			if(str1.length()<3) {
				if(maxContinueLose3StepTmp >= 2) {
					if(count1==8) {
						allContinueLose3Str  = allContinueLose3Str + "\n";
						count1=0;
					}
					allContinueLose3Str = allContinueLose3Str+str.substring(0,8)+"-----��������"+maxContinueLose3StepTmp+"  ";
					
					count1++;
				}
				
				if(maxContinueLose3StepTmp !=0) {
					if(maxContinueLose3StepTmp>maxContinueLose3Step) {
						maxContinueLose3Step = maxContinueLose3StepTmp;
						maxContinueLose3StepIndexStr = str;
					}
				}
				maxContinueLose3StepTmp =0;
				
				maxSpaceLose3StepTmp++;
			} else { 
				maxContinueLose3StepTmp++;
				
				if(maxSpaceLose3StepTmp !=0) {
					if(maxSpaceLose3StepTmp>maxSpaceLose3Step) {
						maxSpaceLose3Step = maxSpaceLose3StepTmp;
						maxSpaceLose3StepIndexStr = str;
					}
				}
				maxSpaceLose3StepTmp =0;
			}
			
			if(str1.length()!=2) {
				if(maxContinueLose2StepTmp >= 2) {
					if(count2==8) {
						allContinueLose2Str  = allContinueLose2Str + "\n";
						count2=0;
					}
					allContinueLose2Str = allContinueLose2Str+str.substring(0,8)+"-----��������"+maxContinueLose2StepTmp+"  ";
					
					count2++;
				}
				
				if(maxContinueLose2StepTmp !=0) {
					if(maxContinueLose2StepTmp>maxContinueLose2Step) {
						maxContinueLose2Step = maxContinueLose2StepTmp;
						maxContinueLose2StepIndexStr = str;
					}
				}
				maxContinueLose2StepTmp =0;
				
				maxSpaceLose2StepTmp++;
			} else { 
				maxContinueLose2StepTmp++;
				
				if(maxSpaceLose2StepTmp !=0) {
					if(maxSpaceLose2StepTmp>maxSpaceLose2Step) {
						maxSpaceLose2Step = maxSpaceLose2StepTmp;
						maxSpaceLose2StepIndexStr = str;
					}
				}
				maxSpaceLose2StepTmp =0;
			}
			
			if(str1.length()!=1) {
				if(maxContinueLose1StepTmp >= 2) {
					if(count3==8) {
						allContinueLose1Str  = allContinueLose1Str + "\n";
						count3=0;
					}
					
					allContinueLose1Str = allContinueLose1Str+str.substring(0,8)+"-----��������"+maxContinueLose1StepTmp+"  ";
					count3++;
				}
				
				if(maxContinueLose1StepTmp !=0) {
					if(maxContinueLose1StepTmp>maxContinueLose1Step) {
						maxContinueLose1Step = maxContinueLose1StepTmp;
						maxContinueLose1StepIndexStr = str;
					}
				}
				maxContinueLose1StepTmp =0;
				
				maxSpaceLose1StepTmp++;
			} else { 
				maxContinueLose1StepTmp++;
				
				if(maxSpaceLose1StepTmp !=0) {
					if(maxSpaceLose1StepTmp>maxSpaceLose1Step) {
						maxSpaceLose1Step = maxSpaceLose1StepTmp;
						maxSpaceLose1StepIndexStr = str;
					}
				}
				maxSpaceLose1StepTmp =0;
			}
			
			
			if(str1.length()==0) {
				str1="000";
			} else if(str1.length()==1) {
				str1="00"+str1;
			} else if(str1.length()==2) {
				str1="0"+str1;
			} else {
				str1=str1.substring(0,3);
			}
			
			if(lastStr.equals("")) {
				lastStr = str1;
			} else if(lastStr.equals(str1)) {
				if(count4==4) {
					lastStrs = lastStrs+"\n";
					count4=0;
				}
				lastStrs = lastStrs+ str+"--->"+str1+"  ";
				count4++;
			} else {
				lastStr = str1;
			}
			
			int aa = Integer.parseInt(str1.substring(0,1)) + Integer.parseInt(str1.substring(1,2)) + Integer.parseInt(str1.substring(2,3));
			int aa1 = aa % 4;
			String aaStr = aa<10?("0"+aa):aa+"";
			
			int bb = Integer.parseInt(str1);
			int bb1 = bb % 4;
			String bbStr = bb<10?("00"+bb):((bb<100)?("0"+bb):bb+"");
			
			result.add(str+"----->"+str1+"  "+aaStr+ "  "+aa1+"  "+bbStr+"  "+bb1);
			if(statsMap.containsKey(str1)) {
				statsMap.put(str1, statsMap.get(str1)+1);
			} else {
				statsMap.put(str1, 1);
			}
		}
		
		result.add("\n-------3��ʧͳ����-------");
		result.add("\n"+maxContinueLose3StepIndexStr+"-------3��ʧ�������-------" + maxContinueLose3Step);
		result.add(maxSpaceLose3StepIndexStr+"-------3��ʧ�����-------" + maxSpaceLose3Step);
		
		result.add("\n-------��ʧ���������£�-------");
		result.add(allContinueLose3Str);
				
		result.add("\n-------2��ʧͳ����-------");
		result.add("\n"+maxContinueLose2StepIndexStr+"-------2��ʧ�������-------" + maxContinueLose2Step);
		result.add(maxSpaceLose2StepIndexStr+"-------2��ʧ�����-------" + maxSpaceLose2Step);
		
		result.add("\n-------��ʧ���������£�-------");
		result.add(allContinueLose2Str);
				
		result.add("\n-------1��ʧͳ����-------");
		result.add("\n"+maxContinueLose1StepIndexStr+"-------1��ʧ�������-------" + maxContinueLose1Step);
		result.add(maxSpaceLose1StepIndexStr+"-------1��ʧ�����-------" + maxSpaceLose1Step);
		
		result.add("\n-------��ʧ���������£�-------");
		result.add(allContinueLose1Str);
		
		result.add("\n��������:\n"+lastStrs);
		
		List<String> keyList = new ArrayList<String>();
		for(String key:statsMap.keySet()) {
			keyList.add(key);
		}
		Collections.sort(keyList);
		
		for(String key:keyList) {
			result.add(key+"--------->"+statsMap.get(key));
		}
		
		try {
			FileUtil.writeToFile(fileName, result);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
