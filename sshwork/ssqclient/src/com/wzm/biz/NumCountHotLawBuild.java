package com.wzm.biz;

import java.util.ArrayList;
import java.util.List;

import com.wzm.server.dao.ssq.SsqBaseStatsDao;
import com.wzm.server.entity.ssq.SsqBaseStats;
import com.wzm.util.ClientBeanUtil;

public class NumCountHotLawBuild {
	
	// ����ż������
	public static final String EVEN_COUNT = "evenCount";
	
	// ������������
	public static final String ODD_COUNT = "oddCount";
	
	// ����С������
	public static final String SMALL_COUNT = "smallCount";
	
	// �����������
	public static final String BIG_COUNT = "bigCount";
	
	// ����С��������
	public static final String FIRST_ZONE_COUNT = "firstZoneCount";
	
	// ��������������
	public static final String SECOND_ZONE_COUNT = "secondZoneSum";
	
	// �������������
	public static final String THIRD_ZONE_COUNT = "thirdZoneSum";
	
	// ������������
	public static final String PRIME_COUNT = "primeCount";
	
	public static void main(String[] args) {
		List<String> list = buildNumCount(PRIME_COUNT, 20131112);
		for(String str:list) {
			System.out.println(str);
		}
	}

	public static List<String> buildNumCount(String countType, int ssqIndex) {
		SsqBaseStatsDao ssqBaseStatsDao = (SsqBaseStatsDao)ClientBeanUtil.getDao("ssqBaseStatsDao");

		List<String> result = new ArrayList<String>();
		result.add("�������ͣ�"+countType);
		
		String hql = "select distinct "+countType+" from SsqBaseStats s where s.ssqIndex<=? order by "+countType;

		List<SsqBaseStats> listSum = ssqBaseStatsDao.findSsqBaseStatsesByHql(hql, new Object[]{ssqIndex});

		for (Object baseStats : listSum) {
			int sum = ((Integer) baseStats).intValue();

			result.addAll(build(ssqBaseStatsDao, countType, sum));
		}
		
		return result;
	}
	
	private static List<String> build(SsqBaseStatsDao ssqBaseStatsDao, String countType, int count) {
		List<String> result = new ArrayList<String>();
		result.add("\n-----------" + count + "-----------");
		
		String hql = "from SsqBaseStats s where s."+countType+" = ?";
		List<SsqBaseStats> list = ssqBaseStatsDao.findSsqBaseStatsesByHql(hql, new Integer[]{count});

		hql = " select count(s.ssqIndex) from SsqBaseStats s where s.ssqIndex>=? and s.ssqIndex<=?";

		long maxLose = -1;
		long minLose = 10000;
		
		String maxLoseBeginStr = "";
		
		int maxContinue = -1;
		int continueCount = 1;
		String maxContinueEndStr = "";
		
		for (int i = 1; i < list.size(); i++) {
			int ssqIndex1 = list.get(i - 1).getSsqIndex();
			int ssqIndex2 = list.get(i).getSsqIndex();
			
			if(ssqIndex2==ssqIndex1+1) {
				continueCount++;
			} else {
				if(maxContinue<continueCount) {
					maxContinue = continueCount;
					maxContinueEndStr = String.valueOf(ssqIndex1);
				}
				continueCount=1;
			}
			
			long tmpCount = ssqBaseStatsDao.getFunctionLongValue(hql,
					new Integer[] { ssqIndex1, ssqIndex2 }) - 2;

			if (tmpCount > maxLose) {
				maxLose = tmpCount;
				maxLoseBeginStr = String.valueOf(ssqIndex1);
			}

			if (tmpCount < minLose) {
				minLose = tmpCount;
			}
//			result.add(ssqIndex1 + "----->" + ssqIndex2 + "       ��©������" + tmpCount);
		}

		if(maxLose == -1) {
			maxLose =0;
		}
		
		if(minLose == 10000) {
			minLose =0;
		}
		
		String str1 = "��С��©������"+minLose;
		String str2 = "       �����©������" + maxLose;
		String str3 = "       �����©��ʼ�ڣ�" + maxLoseBeginStr;
		String str4 = "       �������������" + maxContinue;
		String str5 = "       ������������ڣ�" + maxContinueEndStr;
		result.add(str1 + str2 + str3 + str4 + str5);
		
		return result;
		
	}

}
