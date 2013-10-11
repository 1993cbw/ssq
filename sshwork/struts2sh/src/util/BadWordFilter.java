package util;

import java.util.*;
import java.io.*;
/**
 * 
 * ����ط�, ����������.
 *
 */
public class BadWordFilter {
	private static List<String> wordList;// �������б�
	
	static {
		setWordList(testReadBadWordsListFromFile());
	}
	
	/**
	 * ����������.
	 * @param input
	 * @return
	 */
	public static String filterBadWords(String input) {
		if(StringUtil.isEmpty(input)) {
			return input;
		}

	
		for(String word : getWordList()) {
			input = input.replaceAll(word, "****");
		}
		
		return input;
	}
	
	/**
	 * �������ļ�����Ƿ��ʻ��б�.
	 * @return
	 */
	private static List<String> testReadBadWordsListFromFile() {
		// �������ļ����عؼ�����Ϣ, ���뵽һ��List��, һ��һ���ؼ���
		
		InputStream in = BadWordFilter.class.getResourceAsStream("/badwords.txt");
		
		ArrayList<String> list = new ArrayList<String>();// �ؼ���
		
		// ת�� Reader, һ�ζ�һ���� BufferedReader
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			
			String line;
			
			while( (line = br.readLine()) != null) {
				list.add(line);
			}
			
			in.close();
			br.close();
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return list;
	}
	
	/**
	 * ��������ַ����Ƿ��зǷ��ʻ�.
	 * @param input
	 * @return
	 */
	public static boolean hasBadWords(String input) {
	
		if(StringUtil.isEmpty(input)) {
			return false;
		}
		
		for(String word : getWordList()) {
			if(input.indexOf(word) != -1) {
				return true;
			}
		}
		
		return false;
	}
	
	public static void main(String[] args) {
//		setWordList(testReadBadWordsListFromFile());
		System.out.println(filterBadWords("����, ��Ҫ���ư���!"));
		System.out.println(hasBadWords("���� ��������̫ƽ ������!"));
	}

	public static List<String> getWordList() {
		return wordList;
	}

	public static void setWordList(List<String> wordList) {
		BadWordFilter.wordList = wordList;
	}


}
