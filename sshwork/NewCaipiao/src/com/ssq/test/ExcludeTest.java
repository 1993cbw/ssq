package com.ssq.test;

import java.io.IOException;
import java.util.List;

import com.ssq.common.util.CommonConstant;
import com.ssq.common.util.FileUtil;
import com.ssq.common.util.SsqUtils;
import com.ssq.util.FilterChainUtil;
import com.ssq.util.filter.FilterChain;

public class ExcludeTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		buildExclude();
		
	}

	public static void buildExclude() {
		List<String> sourceList = null;
		List<String> excludeList = null;
		List<String> excludeThirdList = null;
		try {
			sourceList = FileUtil.readLineFileWithoutCheckRepeat("C:/Users/Administrator/Documents/�༭2.TXT");
			excludeList = FileUtil.readLineFileWithoutCheckRepeat(CommonConstant.FILTER_PATH+"red-exclude.TXT");
			excludeThirdList = FileUtil.readLineFileWithoutCheckRepeat(CommonConstant.FILTER_PATH+"red-exclude-third-1.txt");
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// ��׼�ų���
		SsqUtils.excludeRedBets(sourceList, excludeList);
		
		// �������ų���
		SsqUtils.excludeRedBets(sourceList, excludeThirdList);
		
		
		// ����������
		FilterChain chain = null;
		try {
			chain = FilterChainUtil.buildAFullFilterChain();
		} catch (Exception e1) {
			e1.printStackTrace();
		} 

		for(int i=sourceList.size()-1;i>=0;i--) {
			if(chain.doFilter(sourceList.get(i))) {
				sourceList.remove(i);
			}
		}
		
		try {
			FileUtil.writeToFile("C:/Users/Administrator/Documents/�༭3.TXT", sourceList);
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("ok");
	}

}
