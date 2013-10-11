package filters;

import java.io.IOException;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import cn.beansoft.scm.dao.BaseDAO;
import cn.beansoft.scm.dao.ResourceDAO;
import cn.beansoft.scm.entity.Resource;
import cn.beansoft.scm.entity.User;


/**
 * URL��ȫ����Ȩ�޹�����.
 * @author BeanSoft
 * @see ResourceDAO
 */
public class SecurityFilter implements Filter {
	private ServletContext application;
	private static String errorMessage = "�Բ���, ����Ȩ�޲���, ��ʹ�ú��ʵ��ʺŵ�¼!";

	private BaseDAO getBaseDAO() {
		return (BaseDAO) WebApplicationContextUtils
				.getRequiredWebApplicationContext(
						application).getBean(
						"BaseDAO");
	}
	
	public void destroy() {
	}

	public void doFilter(ServletRequest req, ServletResponse resp,
			FilterChain chain) throws IOException, ServletException {
		// ȡ�����·��, ������ /beijingscm/admin/index.jsp --> /admin/index.jsp
		HttpServletRequest request= (HttpServletRequest)req;
		BaseDAO dao = getBaseDAO();
		User currentUser = (User)request.getSession().getAttribute("loginedUser");// ��ȡ�Ự�еĵ�ǰ��¼�û�.
		
		int roleId = currentUser == null ? -1 : currentUser.getUserType();
		
		String loginMessage = "";// ��¼��ʾ��Ϣ
		if(currentUser == null ) {
			loginMessage = " ����ǰ��δ��¼.";
		}
		
		
		String requestPath = request.getRequestURI();// /beijingscm/admin/index.jsp
		String contextPath = request.getContextPath();// /beijingscm/
		String relativePath = requestPath.substring(contextPath.length());// /admin/index.jsp ���·��
		String folderPath = null;// Ŀ¼�� /admin/index.jsp --> ��� /admin/, /index.jsp --> �����
		int �ڶ���б��λ�� = relativePath.indexOf('/', 1);
		
		if(�ڶ���б��λ�� > 0) {
			folderPath = relativePath.substring(0, �ڶ���б��λ�� + 1);
			folderPath += "*";
		}
		
//		System.out.println("�ڶ���б��λ��=" + �ڶ���б��λ��);
//		System.out.println("folderPath=" + folderPath);
//		System.out.println("contextPath=" + contextPath);
//		System.out.println("relativePath=" + relativePath);
		
		String �鿴��Դ�Ƿ񹫿�HQL = "select count(*) from Resource res where res.uri = ? or res.uri = ?" ;
		String �鿴��Դ�Ƿ�Ե�ǰ�û�����HQL = "select count(*) from Resource res where (res.uri = ? or res.uri = ?) and res.scmRole.id = ?";
		
		if(dao.queryForCount(�鿴��Դ�Ƿ񹫿�HQL, relativePath, folderPath) > 0) {
			System.out.println("��Դ������");
			
			if(dao.queryForCount(�鿴��Դ�Ƿ�Ե�ǰ�û�����HQL, relativePath, folderPath, roleId) > 0) {
				chain.doFilter(req, resp);				
			} else {
				
				if("true".equalsIgnoreCase(request.getParameter("ajax"))) {
					resp.setCharacterEncoding("UTF-8");
					resp.setContentType("text/html;charset=UTF-8");
					
					resp.getWriter().print(errorMessage + loginMessage);
				} else {
					request.setAttribute("message", errorMessage + loginMessage);
					request.getRequestDispatcher("/error.jsp").forward(request, resp);					
				}
				
			}
			
		} else {
			chain.doFilter(req, resp);
		}
				
	}

	public void init(FilterConfig config) throws ServletException {
		application = config.getServletContext();
	}

}