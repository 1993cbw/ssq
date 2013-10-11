/*
 * @(#)PageControl.java 1.00 2004-9-22
 *
 * Copyright 2004 2004 . All rights reserved.
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package util;

/**
 * PageControl, ��ҳ����, �����ж���ҳ�����Ƿ�������ҳ.
 *
 * 2008-07-22 ����������·�ҳHTML���빦��
 *
 * @author beansoft
 * @version 1.1 2008-9-22
 */
public class PageBean {
    /** ÿҳ��ʾ��¼�� */
    private int pageCount;
    /** �Ƿ�����һҳ */
    private boolean hasPrevPage;
    /** ��¼���� */
    private int recordCount;
    /** �Ƿ�����һҳ */
    private boolean hasNextPage;
    /**��ҳ���� */
    private int totalPage;
    /** ��ǰҳ���� */
    private int currentPage;

    /**
     * ��ҳǰ��ҳ���ַ
     */
    private String pageUrl;

    /**
     * �����ҳ HTML ҳ����ת����, �����Ӻ;�̬��������.
     * 2008-07-22
     * @return HTML ����
     */
    public String getPageJumpLinkHtml() {
	    if(StringUtil.isEmpty(pageUrl)) {
	    	return "";
		}
	    
	    // ����Ƿ��в�������, û�оͼ���һ��?
	    if(pageUrl.indexOf('?') == -1) {
	    	pageUrl = pageUrl + '?';
	    }
	    
	    StringBuffer buff = new StringBuffer("<span id='pageText'>");
	    
	    // ��һҳ��ҳ���
	    if(currentPage > 1) {
	    	buff.append("[ <a href='" + pageUrl + "&page=" + (currentPage - 1) + "' title='ת���� "
					+ (currentPage - 1) + " ҳ'>��һҳ</a> ] ");
	    } else {
	    	buff.append("[ ��һҳ ] ");
	    }
	    
	    // ��һҳ��ҳ���
	    if(currentPage < getTotalPage()) {
	    	buff.append("[ <a href='" + pageUrl + "&page=" + (currentPage + 1)+ "' title='ת���� "
					+ (currentPage + 1) + " ҳ'>��һҳ</a> ] ");
	    } else {
	    	buff.append("[ ��һҳ ] ");
	    }
	    
	    buff.append("</span>");
	    
	    return buff.toString();
    }
    
    /**
     * ���ҳ����Ϣ: ��${currentPage}ҳ/��${totalPage}ҳ
     * @return
     */
    public String getPageCountHtml() {
    	return "��" + currentPage + "ҳ/��" + getTotalPage() + "ҳ";
    }
    
    /**
     *  ��� JavaScript ��ת��������
     * @return
     */
    public String getJavaScriptJumpCode() {
	    if(StringUtil.isEmpty(pageUrl)) {
	    	return "";
		}
	    
	    // ����Ƿ��в�������, û�оͼ���һ��?
	    if(pageUrl.indexOf("?") == -1) {
	    	pageUrl = pageUrl + '?';
	    }
	    
    	return "<script>" + 
    	"// ҳ����ת����\n" +
    	"// ����: ����ҳ��ı�Ԫ�أ�����������������\n" +
    "function jumpPage(input) {\n" +
    "	// ҳ����ͬ�Ͳ�����ת\n" +
   " 	if(input.value == " + currentPage + ") {" +
    "		return;\n" +
    "	}" +
    "	var newUrl = '" + pageUrl + "&page=' + input.value;\n" +
    "	document.location = newUrl;\n" +
   " }\n" +
   " </script>";
    
    }
    
    /**
     * ���ҳ����ת��ѡ���������. ʾ�����:
     * <pre>
ת��
	  <!-- ��� HTML SELECT Ԫ��, ��ѡ�е�ǰҳ����� -->
      <select onchange='jumpPage(this);'>
      
      <c:forEach var="i" begin="1" end="${totalPage}">
        <option value="${i}"
		
		<c:if test="${currentPage == i}">
		selected
		</c:if>

	>��${i}ҳ</option>
	  </c:forEach>
	  
      </select>
      ����ҳ�룺<input type="text" value="${currentPage}" id="jumpPageBox" size="3"> 
      <input type="button" value="��ת" onclick="jumpPage(document.getElementById('jumpPageBox'))">     
</pre> 
     * @return
     */
    public String getPageFormJumpHtml() {
    	String s = "ת��\n" + 
    	"\t  <!-- ��� HTML SELECT Ԫ��, ��ѡ�е�ǰҳ����� -->\n" + 
    	"      <select onchange='jumpPage(this);'>\n" + 
    	"      \n";
    	
    	for(int i = 1; i <= getTotalPage(); i++ ) {
    		s +=  "<option value=" + i + "\n";
    		
    		if(currentPage == i) {
    			s+= " selected ";
    		}
    		
    		s += "\t>��" + i + "ҳ</option>\n";
    	}

    	s+=
    	"      </select>\n" + 
    	"      ����ҳ�룺<input type=\"text\" value=\"" + currentPage + "\" id=\"jumpPageBox\" size=\"3\"> \n" + 
    	"      <input type=\"button\" value=\"��ת\" onclick=\"jumpPage(document.getElementById('jumpPageBox'))\">  ";
    	return s;
    }
    
    /**
     * ���з�ҳ����.
     */
    private void calculate() {
        if (getPageCount() == 0) {
            setPageCount(1);
        }

        totalPage = (int) Math.ceil(1.0 * getRecordCount() / getPageCount()); // ��ҳ����
        if (totalPage == 0)
            totalPage = 1;

        // Check current page range, 2006-08-03
        if(currentPage > totalPage) {
        	currentPage = totalPage;
        }
//        System.out.println("currentPage=" + currentPage);
//        System.out.println("maxPage=" + maxPage);
//        // Fixed logic error at 2004-09-25
        hasNextPage = currentPage < totalPage;
        hasPrevPage = currentPage > 1;
        return;
    }

    /**
     * @return Returns the ���ҳ����.
     */
    public int getTotalPage() {
    	calculate();
        return totalPage;
    }

    /**
     * @param currentPage
     *            The ���ҳ���� to set.
     */
    private void setTotalPage(int maxPage) {
        this.totalPage = maxPage;
    }

    /**
     * �Ƿ�����һҳ����
     */
    public boolean hasPrevPage() {
    	calculate();
        return hasPrevPage;
   }

    /**
     * �Ƿ�����һҳ����
     */
    public boolean hasNextPage() {
    	calculate();
        return hasNextPage;
    }

    // Test
    public static void main(String[] args) {
        PageBean pc = new PageBean();
        pc.setCurrentPage(2);
        pc.setPageCount(4);
        pc.setRecordCount(5);
        pc.setPageUrl("product/list.do");

        System.out.println("��ǰҳ " + pc.getCurrentPage());
        System.out.println("����һҳ " + pc.hasPrevPage());
        System.out.println("����һҳ " + pc.hasNextPage());
        System.out.println("��ҳ���� " + pc.getTotalPage());
        
        System.out.println("��ҳ HTML ���� " + pc.getPageJumpLinkHtml());
    }

    /**
     * @return Returns the ��ǰҳ����.
     */
    public int getCurrentPage() {
        return currentPage;
    }

    /**
     * ���õ�ǰҳ��, �� 1 ��ʼ.
	 * @param currentPage
     *            The ��ǰҳ���� to set.
     */
    public void setCurrentPage(int currentPage) {
        if (currentPage <= 0) {
           currentPage = 1;
		}
        this.currentPage = currentPage;
    }

    /**
     * @return Returns the recordCount.
     */
    public int getRecordCount() {
        return recordCount;
    }

    /**
     * @param recordCount
     *            The recordCount to set.
     */
    public void setRecordCount(int property1) {
        this.recordCount = property1;
    }

    /**
     * @return Returns the ÿҳ��ʾ��¼��.
     */
    public int getPageCount() {
        return pageCount;
    }

    /**
     * @param pageCount
     *            The ÿҳ��ʾ��¼�� to set.
     */
    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

	public String getPageUrl()
	{
		return pageUrl;
	}

	public void setPageUrl(String value)
	{
		pageUrl = value;
	}
}
