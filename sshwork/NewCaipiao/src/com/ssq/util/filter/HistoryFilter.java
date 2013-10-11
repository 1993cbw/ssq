package com.ssq.util.filter;

import java.io.IOException;
import java.util.List;

import com.ssq.common.util.FileUtil;

/**
 * ��ʷ������¼���ˣ���Ͷע��������ǰ�Ŀ�����¼
 * @author Administrator
 *
 */
public class HistoryFilter implements Filter {
	
	private List<String> historyList;
	
	public HistoryFilter() {
		try {
			historyList = FileUtil.getSSQHistoryList();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		for(int i=0;i<historyList.size();i++) {
			historyList.set(i, historyList.get(i).substring(8).substring(0,17).replaceAll(" ", ","));
		}
	}

	@Override
	public boolean doFilter(String str) {
		
		if(historyList != null && historyList.contains(str) ) {
			return true;
		}
		return false;
	}
	
	public String toString() {
		String str = "��ʷ������¼����";
		return str;
	}
	
	public static void main(String[] args) {
		HistoryFilter f = new HistoryFilter();
		boolean b = f.doFilter("02,04,14,18,20,22");
		System.out.println(b);
	}

}
