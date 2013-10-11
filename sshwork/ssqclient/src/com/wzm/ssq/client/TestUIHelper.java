package com.wzm.ssq.client;

import java.util.ArrayList;
import java.util.List;

import com.wzm.server.service.formula.FormulaService;
import com.wzm.server.service.ssq.SsqForcastService;
import com.wzm.server.service.ssq.SsqRecordService;
import com.wzm.util.ClientBeanUtil;
import com.wzm.util.SsqStatsType;

public class TestUIHelper {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

	}

	public static void buildSSqRecordAndStats() {
		SsqRecordService ssqService = (SsqRecordService) ClientBeanUtil
				.getService("ssqRecordService");

		SsqForcastService forcastService = (SsqForcastService) ClientBeanUtil
				.getService("ssqForcastService");

		FormulaService formulaService = (FormulaService) ClientBeanUtil
				.getService("formulaService");

		// ����˫ɫ�򿪽���¼
		ssqService.writeAllSsqRecord("");

		// д��Ŀǰδ���ڵ�ͳ����Ϣ
		writeNotExistSsqStatsByNow(ssqService);

		// д��Ŀǰδ���ڵ�Ԥ����Ϣ
		forcastNotExist(forcastService);
		
		// ���칫ʽ��ؽ��
		buildFormulaResult(formulaService);

	}

	/**
	 * ���칫ʽ��ؽ��
	 * @param formulaService
	 */
	private static void buildFormulaResult(FormulaService formulaService) {
		try {
			formulaService.caclAllRedFormula();
			formulaService.verifyAllRedFormula();
			
			formulaService.writeAllRedFormulaCaclVerifyMulStats(50);
			formulaService.writeAllRedFormulaCaclVerifyMulStats(60);
			formulaService.writeAllRedFormulaCaclVerifyMulStats(80);
			formulaService.writeAllRedFormulaCaclVerifyMulStats(100);
			
			
			formulaService.writeAllRedFormulaCaclVerifyMulForcast(50,300);
			formulaService.writeAllRedFormulaCaclVerifyMulForcast(60,300);
			formulaService.writeAllRedFormulaCaclVerifyMulForcast(80,300);
			formulaService.writeAllRedFormulaCaclVerifyMulForcast(100,300);
			
			formulaService.writeAllRedFormulaCaclVerifyMulForcast(50,400);
			formulaService.writeAllRedFormulaCaclVerifyMulForcast(60,400);
			formulaService.writeAllRedFormulaCaclVerifyMulForcast(80,400);
			formulaService.writeAllRedFormulaCaclVerifyMulForcast(100,400);
			
			formulaService.verifyAllRedFormulaCaclVerifyMulForcast(50,300);
			formulaService.verifyAllRedFormulaCaclVerifyMulForcast(60,300);
			formulaService.verifyAllRedFormulaCaclVerifyMulForcast(80,300);
			formulaService.verifyAllRedFormulaCaclVerifyMulForcast(100,300);
			
			formulaService.verifyAllRedFormulaCaclVerifyMulForcast(50,400);
			formulaService.verifyAllRedFormulaCaclVerifyMulForcast(60,400);
			formulaService.verifyAllRedFormulaCaclVerifyMulForcast(80,400);
			formulaService.verifyAllRedFormulaCaclVerifyMulForcast(100,400);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void writeNotExistSsqStatsByNow(SsqRecordService ssqService) {

		// һ����Χ��ͳ����Ϣ
		List<Integer> spaceNums = new ArrayList<Integer>();
		spaceNums.add(10);
		spaceNums.add(20);
		spaceNums.add(30);
		spaceNums.add(40);
		spaceNums.add(50);
		spaceNums.add(60);
		spaceNums.add(80);
		spaceNums.add(100);

		// ����ͳ�Ʊ�
		ssqService.writeNotExistSsqStatsByNow(SsqStatsType.BASE_STATS);
		
		// ������ʧͳ�Ʊ�
		ssqService.writeNotExistSsqStatsByNow(SsqStatsType.COL_ROW_STATS);

		// ����ͳ��
		ssqService.writeNotExistSsqStatsByNow(SsqStatsType.BLUE_STATS);

		// β��ͳ��
		ssqService.writeNotExistSsqStatsByNow(SsqStatsType.TAIL_STATS);

		// ����ͳ��
		ssqService.writeNotExistSsqStatsByNow(SsqStatsType.PRIME_STATS);

		// ����ͳ��
		ssqService.writeNotExistSsqStatsByNow(SsqStatsType.COLD_HOT_STATS);

		// ACֵ��ɢ��ͳ��
		ssqService.writeNotExistSsqStatsByNow(SsqStatsType.AC_SANDU_STATS);
		
		ssqService.writeNotExistSsqStatsByNow(SsqStatsType.LAST_SAME_STATS);
		

		// ����ͳ��
		for (int spaceNum : spaceNums) {
			ssqService.writeNotExistSsqMulStatsByNow(
					SsqStatsType.MUL_BLUE_STATS, spaceNum);
			ssqService.writeNotExistSsqMulStatsByNow(
					SsqStatsType.MUL_TAIL_STATS, spaceNum);
			ssqService.writeNotExistSsqMulStatsByNow(
					SsqStatsType.MUL_COLD_HOT_STATS, spaceNum);
			ssqService.writeNotExistSsqMulStatsByNow(
					SsqStatsType.MUL_PRIME_STATS, spaceNum);
		}
	}

	public static void forcastNotExist(SsqForcastService forcastService) {
		List<Integer> spaceNums = new ArrayList<Integer>();
		spaceNums.add(10);
		spaceNums.add(20);
		spaceNums.add(30);
		spaceNums.add(40);
		spaceNums.add(50);
		spaceNums.add(60);
		spaceNums.add(80);
		spaceNums.add(100);

		List<Integer> forcastSpaceNums = new ArrayList<Integer>();
		forcastSpaceNums.add(200);
		forcastSpaceNums.add(300);
		forcastSpaceNums.add(400);
		for (int spaceNum : spaceNums) {
			for (int forcastSpaceNum : forcastSpaceNums) {
				forcastService.writeNotExistSsqTailForcast(spaceNum,
						forcastSpaceNum);
				forcastService.verifyNotExistSsqTailForcast(spaceNum,
						forcastSpaceNum);
				forcastService.writeNotExistSsqChForcast(spaceNum,
						forcastSpaceNum);
				forcastService.verifyNotExistSsqCHForcast(spaceNum,
						forcastSpaceNum);
			}
		}
	}

}
