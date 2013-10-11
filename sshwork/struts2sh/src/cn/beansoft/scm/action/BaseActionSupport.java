package cn.beansoft.scm.action;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;

import cn.beansoft.scm.dao.BaseDAO;
import cn.beansoft.scm.entity.User;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

/**
 * ��װһЩ������Action����.
 * @author BeanSoft
 *
 */
public abstract class BaseActionSupport extends ActionSupport {
	// ���ص���Ϣ
	private String message;
	private String title;// ҳ����ʾ����
	private BaseDAO baseDAO;// ������DAO����
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	
	public BaseDAO getBaseDAO() {
		return baseDAO;
	}

	public void setBaseDAO(BaseDAO baseDAO) {
		this.baseDAO = baseDAO;
	}
	
	/**
	 * ������URL�����ϲ���һ��URL�ַ���(page��������), �ṩ��ҳʱ��ʾ.
	 * @return �ַ���, ��: para1=11&para2=bb
	 */
	public String mergeParamsAsURI() {
		Map<String, String[]> params = getRequest().getParameterMap();
		
		StringBuffer out = new StringBuffer();
		
		Set<String> keys = params.keySet();
		
		for (String key : keys) {
			if(!"page".equals(key)) {
				// TODO ����Mapֵ������
				String[] values = params.get(key);
				
				if(values.length > 1) {
					values = getRequest().getParameterValues(key);
				} else {
					values = new String[] {getParameter(key)};
				}
				
				System.out.println("key=" + key);
				
				try {
					for(String value : values) {
						System.out.println("value=" + value);
						
						out.append(java.net.URLEncoder.encode(key, "UTF-8") + "=");
						out.append(java.net.URLEncoder.encode(value, "UTF-8") + "&");
					}

				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
		}
		
		// ɾ��ĩβ����� & �ַ�
		if(out.toString().endsWith("&")) {
			out.deleteCharAt(out.length() - 1);
		}
		
		return out.toString();
	}

	/**
	 * ���ú�ҳ����ʾ�йصĲ���, title��message���Զ�����Ϊ��.
	 */
	void resetMessages() {
		setMessage("");
		setTitle("");
	}
	
	/**
	 * ��ȡ��ǰ�Ự�ĵ�¼�û���Ϣ
	 * @return User
	 */
	public User getSessionLoginedUser() {
		User currentUser = (User) getSession("loginedUser");
		return currentUser;
	}
	
	/**
	 * ��ȡ������
	 * @param name
	 * @return
	 */
	public String getParameter(String name) {
		return ServletActionContext.getRequest().getParameter(name);
	}
	
	/**
	 * ����������Ϊ��������.
	 * @param name ��������
	 * @return
	 */
	public int getParameterInt(String name) {
		String s = getParameter(name);
		
		if(s == null) {
			return 0;
		} else {
			try {
				return Integer.parseInt(s);
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return 0;
	}
	
	/**
	 * ����������Ϊ����������.
	 * @param name ��������
	 * @return
	 */
	public long getParameterLong(String name) {
		String s = getParameter(name);
		
		if(s == null) {
			return 0L;
		} else {
			try {
				return Long.parseLong(s);
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return 0L;
	}	
	
	/**
	 * ���� request ������.
	 * @param name ������
	 * @param value ����ֵ
	 */
	public void setAttribute(String name, Object value) {
		ServletActionContext.getRequest().setAttribute(name, value);
	}
	
	/**
	 * ��ȡ request ������.
	 * @param name ������
	 */
	public Object getAttribute(String name) {
		return ServletActionContext.getRequest().getAttribute(name);
	}	
	
	/**
	 * ��ȡ session �е�����ֵ
	 * @param name
	 * @return
	 */
	public static Object getSession(String name) {
		ActionContext ctx = ActionContext.getContext();
		Map session = ctx.getSession();

		return session.get(name);
	}
	
	/**
	 * �� session ��������ֵ
	 * @param name
	 * @param value
	 */
	public static void setSession(Object name, Object value) {
		ActionContext ctx = ActionContext.getContext();
		Map session = ctx.getSession();
		session.put(name, value);
	}
	
	/**
	 * ��ȡ application ����.
	 * @return
	 */
	public static ServletContext getApplication() {
		return ServletActionContext.getServletContext();
	}
	
	/**
	 * ��ȡ�������.
	 * @return
	 */
	public static HttpServletRequest getRequest() {
		return ServletActionContext.getRequest();
	}
	

}
