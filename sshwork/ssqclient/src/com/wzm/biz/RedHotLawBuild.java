package com.wzm.biz;

import java.util.ArrayList;
import java.util.List;

import com.ssq.common.util.SsqUtils;
import com.wzm.server.dao.base.BaseDao;
import com.wzm.server.dao.ssq.SsqBaseStatsDao;
import com.wzm.server.dao.ssq.SsqRecordDao;
import com.wzm.server.entity.ssq.SsqLastSameStats;
import com.wzm.server.entity.ssq.SsqRecord;
import com.wzm.util.ClientBeanUtil;

public class RedHotLawBuild {

	public static void main(String[] args) {
//		buildRedHotLaw(2013102);
	}

	/**
	 * ��������������ʧ����������
	 */
	public static List<String> buildRedHotLaw(int ssqIndex) {
		SsqRecordDao ssqRecordDao = (SsqRecordDao)ClientBeanUtil.getDao("ssqRecordDao");

		List<String> result = new ArrayList<String>();
		
		result.add("˫ɫ��ǰ�ڣ�"+ssqIndex);
		
		for(int redHotNumber = 1;redHotNumber<=33;redHotNumber++) {
		
			String hql = "from SsqRecord s where ( s.r1=? or s.r2=? or s.r3=? or s.r4=? or s.r5=? or s.r6=? ) and s.ssqIndex<=? order by s.ssqIndex";
			List<SsqRecord> listSsqRecords = ssqRecordDao.findSsqRecordsByHql(hql, new Integer[]{redHotNumber, redHotNumber, redHotNumber, 
					redHotNumber, redHotNumber, redHotNumber, ssqIndex});
	
			result.addAll(build(ssqRecordDao, listSsqRecords, redHotNumber, ssqIndex));
		}
		
		return result;
	}
	
	public static List<String> buildRedHotLastSameLaw(int ssqIndex) {
		SsqBaseStatsDao ssqBaseStatsDao = (SsqBaseStatsDao)ClientBeanUtil.getDao("ssqBaseStatsDao");
		
		List<String> result = new ArrayList<String>();
		
		result.add("˫ɫ��ǰ�ڣ�"+ssqIndex);
		for(int lastSameCount=0;lastSameCount<=4;lastSameCount++) {
			String hql = "from SsqLastSameStats s where s.sameCount=? and  s.ssqIndex<=? order by s.ssqIndex";
			List<SsqLastSameStats> listSsqLastSameStatses = ssqBaseStatsDao.findSsqLastSameStatsesByHql(hql, new Integer[]{lastSameCount, ssqIndex});
			result.addAll(buildLastName(ssqBaseStatsDao, listSsqLastSameStatses, lastSameCount, ssqIndex));
		}
		
		return result;
	}
	
	/**
	 * ����ÿ�ں����Ƿ��������Ĺ���
	 * @param ssqIndex
	 * @return
	 */
	public static List<String> buildRedHotContinueLaw(boolean isContinue, int ssqIndex) {
		SsqRecordDao ssqRecordDao = (SsqRecordDao)ClientBeanUtil.getDao("ssqRecordDao");

		List<String> result = new ArrayList<String>();
		
		result.add("˫ɫ��ǰ�ڣ�"+ssqIndex);
		
		String hql = "from SsqRecord s where ( s.r1+1=s.r2 or s.r2+1=s.r3 or s.r3+1=s.r4 or s.r4+1=s.r5 or s.r5+1=s.r6 ) and s.ssqIndex<=? order by s.ssqIndex";
		if(!isContinue) {
			hql = "from SsqRecord s where ( s.r1+1!=s.r2 and s.r2+1!=s.r3 and s.r3+1!=s.r4 and s.r4+1!=s.r5 and s.r5+1!=s.r6 ) and s.ssqIndex<=? order by s.ssqIndex";
		}
		List listSsqRecords = ssqRecordDao.find(hql, new Integer[]{ssqIndex});

		List<SsqRecord> list = new ArrayList<SsqRecord>();
		for(Object o:listSsqRecords) {
			list.add((SsqRecord)o);
		}
		result.addAll(buildContinue(ssqRecordDao, list, isContinue, ssqIndex));
		
		return result;
	}

	private static List<String> build(SsqRecordDao ssqRecordDao, List<SsqRecord> list, int redHotNumber, int ssqIndex) {
		
		String hql = " select count(s.ssqIndex) from SsqRecord s where s.ssqIndex>=? and s.ssqIndex<=?";
		
		List<String> result= new ArrayList<String>();

		long max = -1;
		long min = 10000;
		
		String maxBeginStr = "";
		int maxContinue = -1;
		int continueCount = 1;
		
		int ssqIndex2 =0;
		for (int i = 1; i < list.size(); i++) {
			int ssqIndex1 = list.get(i - 1).getSsqIndex();
			ssqIndex2 = list.get(i).getSsqIndex();
			if(ssqIndex2==ssqIndex1+1) {
				continueCount++;
			} else {
				if(maxContinue<continueCount) {
					maxContinue = continueCount;
				}
				continueCount=1;
			}
			long count = ssqRecordDao.getFunctionLongValue(hql,
					new Integer[] { ssqIndex1, ssqIndex2 }) - 2;

			if (count > max) {
				max = count;
				maxBeginStr = String.valueOf(ssqIndex1);
			}

			if (count < min) {
				min = count;
			}
		}

		if(max == -1) {
			max =0;
		}
		
		if(min == 10000) {
			min =0;
		}
		
		long currentLoseCount = ssqRecordDao.getFunctionLongValue(hql,
				new Integer[] { ssqIndex2, ssqIndex }) -1;
		
		String str1 = "����"+SsqUtils.build2BitIntStr(redHotNumber)+"��";
		String str2 = "     �������������" + SsqUtils.build2BitIntStr(maxContinue);
		String str3 = "     �����©������" + SsqUtils.build2BitIntStr((int)max);
		String str4 = "     ��ǰ��©������" + SsqUtils.build2BitIntStr((int)currentLoseCount);
		result.add(str1 + str2 + str3 + str4);
		
		return result;
	}
	
	private static List<String> buildContinue(SsqRecordDao ssqRecordDao, List<SsqRecord> list, boolean isContinue, int ssqIndex) {
		
		String hql = " select count(s.ssqIndex) from SsqRecord s where s.ssqIndex>=? and s.ssqIndex<=?";
		
		List<String> result= new ArrayList<String>();

		long maxLoseCount = -1;
		long min = 10000;
		String maxBeginStr = "";
		int maxContinue = -1;
		int continueCount = 1;
		
		int ssqIndex2 =0;
		for (int i = 1; i < list.size(); i++) {
			int ssqIndex1 = list.get(i - 1).getSsqIndex();
			ssqIndex2 = list.get(i).getSsqIndex();
			if(ssqIndex2==ssqIndex1+1) {
				continueCount++;
			} else {
				if(maxContinue<continueCount) {
					maxContinue = continueCount;
				}
				continueCount=1;
			}
			long count = ssqRecordDao.getFunctionLongValue(hql,
					new Integer[] { ssqIndex1, ssqIndex2 }) - 2;

			if (count > maxLoseCount) {
				maxLoseCount = count;
				maxBeginStr = String.valueOf(ssqIndex1);
			}

			if (count < min) {
				min = count;
			}
		}

		if(maxLoseCount == -1) {
			maxLoseCount =0;
		}
		
		if(min == 10000) {
			min =0;
		}
		
		long currentLoseCount = ssqRecordDao.getFunctionLongValue(hql,
				new Integer[] { ssqIndex2, ssqIndex }) -1;
		
		if(ssqIndex!=ssqIndex2) {
			continueCount = 0;
		}
		
		String tmp = "";
		if(isContinue) {
			tmp = "ͬ�ں������������";
		} else {
			tmp = "ͬ�ں��򲻴���������";
		}
		result.add(tmp+"    �������������"+maxContinue + "-----" +"�����©������"+ maxLoseCount+"-----��ǰ��©������"+currentLoseCount +"-----��ǰ����������"+continueCount);
		
		return result;
	}
	
	private static List<String> buildLastName(BaseDao ssqBaseDao, List<SsqLastSameStats> list, int sameCount, int ssqIndex) {
		
		String hql = " select count(s.ssqIndex) from SsqRecord s where s.ssqIndex>=? and s.ssqIndex<=?";
		
		List<String> result= new ArrayList<String>();

		long maxLoseCount = -1;
		long min = 10000;
		
		String maxLoseBeginStr = "";
		String maxContinueEndStr = "";
		
		int maxContinue = -1;
		int continueCount = 1;
		
		int ssqIndex2 =0;
		for (int i = 1; i < list.size(); i++) {
			int ssqIndex1 = list.get(i - 1).getSsqIndex();
			ssqIndex2 = list.get(i).getSsqIndex();
			if(ssqIndex2==ssqIndex1+1) {
				continueCount++;
			} else {
				if(maxContinue<continueCount) {
					maxContinue = continueCount;
					maxContinueEndStr = String.valueOf(ssqIndex1);
				}
				continueCount=1;
			}
			long count = ssqBaseDao.getFunctionLongValue(hql,
					new Integer[] { ssqIndex1, ssqIndex2 }) - 2;

			if (count > maxLoseCount) {
				maxLoseCount = count;
				maxLoseBeginStr = String.valueOf(ssqIndex1);
			}

			if (count < min) {
				min = count;
			}
		}

		if(maxLoseCount == -1) {
			maxLoseCount =0;
		}
		
		if(min == 10000) {
			min =0;
		}
		
		long currentLoseCount = ssqBaseDao.getFunctionLongValue(hql,
				new Integer[] { ssqIndex2, ssqIndex }) -1;
		
		if(ssqIndex!=ssqIndex2) {
			continueCount = 0;
		}
		
		String tmp = "��������ͬ����"+sameCount+":";
		
		String tmp1 = "      �������������" + SsqUtils.build2BitIntStr(maxContinue);
		String tmp2 = "      �����©������" + SsqUtils.buildBitIntStr(3, (int)maxLoseCount);
		String tmp3 = "      ��ǰ��©������" + SsqUtils.buildBitIntStr(3, (int)currentLoseCount);
		String tmp4 = "      ��ǰ����������" + SsqUtils.build2BitIntStr((int)continueCount);
		String tmp5 = "      �����©��ʼ�ڣ�" + maxLoseBeginStr;
		String tmp6 = "      ������������ڣ�" + maxContinueEndStr;
		result.add(tmp + tmp1 + tmp2 + tmp3 + tmp4 + tmp5 + tmp6);
		
		return result;
	}

}
