package cn.beansoft.scm.action;

import java.io.File;
import java.util.List;

import util.BeanDebugger;
import util.FileOperate;
import util.PageBean;
import util.StringUtil;
import util.mail.MailSender;


import cn.beansoft.scm.biz.UserManager;
import cn.beansoft.scm.entity.User;

/**
 * �û� Struts 2 Action.
 * @author BeanSoft
 */
public class UserAction extends BaseActionSupport {
	private User user;

	// �û�ҵ�����
	private UserManager userManager;

	// ͼƬ�ϴ��������
	private File photo;// �ϴ��ļ���ʱ�洢·��
	private String photoFileName;// ���ϴ����ļ���
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}


	/**
	 * ��¼
	 */
	public String login() {
		setMessage(null);
		System.out
				.println("�û���:" + user.getName() + ",����" + user.getPassword());

		try {
			if (getUserManager().checkLogin(user.getName(), user.getPassword(),
					user.getUserType())) {
				// ���µ�¼����
				user = getUserManager().findByName(
						user.getName());
				// �����ֹ��������ݺ���intΪnull������
				if(user.getLoginCount() == null) {
					user.setLoginCount(0);
				}
				
				user.setLoginCount(user.getLoginCount() + 1);
				userManager.update(user);
				
				// ����¼�û���Ϣ���� session
				setSession("username", user.getName());
				setSession("loginedUser", user);
				
				System.out.println("��¼�ɹ�");

				// ����Ա��ת��������ҳ
				// TODO ����ΪRole��������ҳ
				if(user.getUserType() == 4) {
					return "adminSuccess";
				}
				return SUCCESS;
			} else {
				setMessage("��������û���������������û����Ͳ���ȷ,������");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			setMessage(e.getMessage());
			e.printStackTrace();
		}
		return INPUT;
	}

	/**
	 * ע��, ����ͷ��.
	 */
	public String reg() {
		resetMessages();
		// 1. �����֤��
		String regcode = (String) getSession("regcode");
		// 2. ����������֤��
		String regcodeInput = getParameter("keycode");
		// TODO �û�Ψһ��̨��֤ 1. ����SQL��unique 2. ��java����
		// TODO ����һ����֤
		// �û������дʽ�ֹע��
		if (util.BadWordFilter.hasBadWords(getParameter("user.username"))) {
			setMessage("�����û����������й�����������������дʻ�");
			return "success";
		}

		if (regcodeInput == null || regcodeInput.length() == 0) {
			setMessage("��������֤��");
			return "success";
		} else if (regcodeInput.equals(regcode)) {
			// 3. ��֤����ɹ�, ��������
			// ����MD5����
			user.setPassword(util.MD5Bean.md5(user.getPassword().getBytes()));
			// ����ע������
			user.setRegDate(new java.util.Date());
			// ���õ�¼����
			user.setLoginCount(0);			
			// ���ü���״̬Ϊ1
			user.setActive(true);
			getUserManager().save(user);

			System.out.println("getPhotoFileName()=" + getPhotoFileName());

			// �������ļ��������ϴ���ͼƬ, ������ʱ�ļ����е��ļ��� /upload Ŀ¼��
			if (getPhotoFileName() != null && getPhotoFileName().length() > 0) {
				// �ϴ�Ŀ¼
				String uploadFolder = getApplication()
						.getRealPath("/upload");
				String uploadPhotoFileName = System.currentTimeMillis() + "_"// �Ƽ��ĳ���_��_��
						+ new java.util.Random().nextInt(10000) + "_"
						+ util.Counter.getInstance().nextValue() + "."
						+ util.FileOperate.getExtension(getPhotoFileName());
				
				util.FileOperate.copyFile(getPhoto().getAbsolutePath(),
						uploadFolder + java.io.File.separator
								+ uploadPhotoFileName);
				// ����ͷ��Ĵ洢·��
				user.setPhoto("/upload/" +uploadPhotoFileName);
				getUserManager().update(user);
			}
			
			// ����ע��֪ͨ�ʼ�
			String message = "<html><body>��ע�������û�, �û�������:" + user.getName()
					+ ", ��ӭʹ�����ǵ�ϵͳ! ���ʼ�����ȷ�����������Ƿ���ȷ. " + "</body></html>";
			;

			MailSender mailSender = new MailSender();
			mailSender.setFrom("admin@beansoft.cn");
			mailSender.setTo(user.getEmail());
			mailSender.setSubject("ע���û��ɹ�");
			mailSender.setBody(message);
			mailSender.setHtmlFormat(true);

			if (mailSender.sendMail()) {
				setMessage("���û�" + user.getName() + "ע��ɹ�,�������Ͻǽ��е�¼, ����������������֪ͨ�ʼ�");
			} else {
				setMessage("ע��ɹ�, ����������޷����������ʼ�, �����������Ա��ϵ!");
			}
			
		}
		return INPUT;
	}

	// �û�ע�� AJAX ��֤����
	public String ajaxValidate() {
		setMessage(null);
		String value = getParameter("value");
		String what = getParameter("what");
		
		// �û������дʽ�ֹע���ʹ��
		if (util.BadWordFilter.hasBadWords(value)) {
			setMessage("�����û����������й�����������������дʻ�");
		}
		
		if ("user.name".equals(what)) {

			User user = userManager.findByName(value);
			if (user != null) {
				setMessage("�û����Ѵ���");
			}
			// û��FilterӦ���ֹ�ר��
			// System.out.println("value1=" +
			// util.StringUtil.changeEncoding(value, "ISO8859-1", "UTF-8"));
			System.out.println("value=" + value);


		} else if ("user.email".equals(what)) {
			System.out.println("user.email");
			System.out.println("value=" + value);
			User user = userManager.findByEmail(value);
			if (user != null) {
				setMessage("�����ַ�ѱ�ע��");
			}
		} else if ("keycode".equals(what)) {
			System.out.println("keycode");
			System.out.println("value=" + value);
			// 1. �����֤��
			String regcode = (String) getSession("regcode");

			if (value == null || value.length() == 0) {
				setMessage("��֤�벻��Ϊ��");
			} else if (!value.equals(regcode)) {
				setMessage("��֤�벻��ȷ");
			}
		}else if ("keycode".equals(what)) {
			System.out.println("keycode");
			System.out.println("value=" + value);
			// 1. �����֤��
			String regcode = (String) getSession("regcode");

			if (value == null || value.length() == 0) {
				setMessage("��֤�벻��Ϊ��");
			} else if (!value.equals(regcode)) {
				setMessage("��֤�벻��ȷ");
			}
		} else if ("roleName".equals(what)) {
			// ����ɫ���Ƿ��ظ�
			System.out.println("roleName");
			System.out.println("value=" + value);
			List roles = getBaseDAO().findByProperty("Role", "roleName", value);

			if (roles != null && roles.size() > 0) {
				setMessage("�˽�ɫ�Ѵ���");
			}
		}
			
		return "success";

	}

	// ע���û��б�

	// ��ҳ�б���
	public String list() {
		resetMessages();
		// 1. �ѵ�ǰҳ�ӱ�����ȡ����
		int currentPage = getParameterInt("page");
		// 2. ȡ������¼����
		int totalRecord = getUserManager().getTotalUsers();
		// 3. ���ݵ�ǰҳ��ÿҳ��ʾ���ݴ�ҵ���ȡ������
		List users = getUserManager().pageUsers(currentPage, 5);
		// 4. ���÷�ҳBean
		PageBean pageBean = new PageBean();
		pageBean.setCurrentPage(currentPage);
		pageBean.setPageCount(5);
		pageBean.setRecordCount(totalRecord);
		pageBean.setPageUrl("user/list.action");
		// 5. �����ݺͷ�ҳbean����request�������з��ظ�ǰ̨������ʾ
		setAttribute("title", "ע���û��б�");
		setAttribute("users", users);
		setAttribute("pageBean", pageBean);
		
		System.out.println("��ҳ��:" + pageBean.getTotalPage());

		// ת��excel��ͼ��
		if ("true".equals(getParameter("excel"))) {
			return "excel";
		}

		return SUCCESS;
	}
	
	/**
	 * ���ݱ�Ų����û���Ϣ
	 * @return
	 * @throws Exception - ����ʱ����
	 */
	public String findById()throws Exception {
		setMessage(null);
			long id = Long.parseLong(getParameter("id"));
			user = userManager.findById(id);
			
			if(user == null) {
				setMessage("���û���Ϣ������");
				throw new Exception("���û���Ϣ������");
			}

		
		return SUCCESS;
	}

	// �޸�����
	public String changePassword() {
		setMessage(null);
		
		String oldPassword = getParameter("password");// ������
		String newPassword = getParameter("password_new");// ������
		String newPasswordRepeat = getParameter("password_new_repeat");// �ظ�������
		
		if(StringUtil.isEmpty(oldPassword)) {
			setMessage("�����������!");
			return SUCCESS;
		}
		
		if(StringUtil.isEmpty(newPassword)) {
			setMessage("������������!");
			return SUCCESS;
		}
		
	    if(!newPassword.equals(newPasswordRepeat)) {
	    	setMessage("������������벻һ��!");
	    	return SUCCESS;
	    }		
		
		// �� session ��ȡ��ǰ�û���Ϣ

		
		User currentUser = getSessionLoginedUser();
		
		if(currentUser == null) {
			setMessage("����δ��¼!");
			return SUCCESS;
		}
		
		currentUser.setPassword(getParameter("password_new"));
		
		try {
			if(userManager.changeUserPassword(oldPassword, currentUser)) {
				setMessage("�����޸ĳɹ�!");
				return SUCCESS;
			} else {
				setMessage("�����޸�ʧ��, ���������Ƿ���ȷ!");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return SUCCESS;
		
	}
	
	/**
	 * ע��, ����ͷ��.
	 */
	public String update() {
		setMessage(null);
		// �� session ��ȡ��ǰ�û���Ϣ
		User currentUser = getSessionLoginedUser();
		
		if(currentUser == null) {
			setMessage("����δ��¼!");
			return INPUT;
		}
		
		// ���±�Ҫ��Ϣ
		currentUser.setAddress(user.getAddress());
		currentUser.setPostCode(user.getPostCode());
		currentUser.setHomePhone(user.getHomePhone());
		currentUser.setCellPhone(user.getCellPhone());
		currentUser.setOfficePhone(user.getOfficePhone());
		currentUser.setIm(user.getIm());
		currentUser.setWebsite(user.getWebsite());
		currentUser.setNote(user.getNote());
		
		
		System.out.println("getPhotoFileName=" + getPhotoFileName());
		System.out.println("user.getBirthday()=" + user.getBirthday());

		// ������ʱ�ļ����е��ļ��� /upload Ŀ¼��
		if (getPhotoFileName() != null) {
			// ɾ����ͷ���ļ�
			if(!StringUtil.isEmpty(currentUser.getPhoto())) {
				FileOperate.delFile(getApplication().getRealPath(currentUser.getPhoto()));
			}
			
			// ����ļ���
			String outputFile = System.currentTimeMillis()
					+ new java.util.Random().nextInt(10000) + "."
					+ FileOperate.getExtension(this.getPhotoFileName());
			FileOperate.copyFile(getPhoto().getAbsolutePath(),
					getApplication().getRealPath(
							"/upload")
							+ "/" + outputFile);
			currentUser.setPhoto("/upload/" + outputFile);

		}

		BeanDebugger.dump(currentUser);

		// �����û�
		getUserManager().update(currentUser);
		setMessage("�û�" + currentUser.getName() + "�����޸ĳɹ�");


		return INPUT;
	}
	
	/**
	 * ȡ������, ������������벢���͵��û�ע��ʱ�ṩ������.
	 * @return
	 */
	public String retrievePassword() {
/**
		1.	����Email�ҵ��û���Ϣ
		2.	����һ����������벢���µ����ݿ�
		3.	����һ���ʼ������û�������
*/
		String email = getParameter("email");
		
		User user = userManager.findByEmail(email);
		
		if(user == null) {
			setMessage("����ȡ��ʧ��: ��������ʼ���Ч");
			
			return SUCCESS;
		} else {
			String password = new java.util.Random().nextInt(1000000000) + "";
			
			user.setPassword(util.MD5Bean.md5(password));
			
			userManager.update(user);
			
	        MailSender sender = new MailSender();

	        sender.setFrom("\"Admin\" <admin@earth.org>");
	        sender.setTo(email);
	        sender.setSubject("SCMϵͳȡ������֪ͨ�ʼ�");
	        sender.setBody("Ӧ����Ҫ��, ϵͳ�����������ʻ�����, ϵͳ���ɵ���������" + password + ", ��������¼���޸Ĵ�����!");

	        System.out.println(sender.sendMail());
	        
	        setMessage("�����������Ѿ����͵�����ע��ʱ�ṩ������, �����ʼ������µ�¼");
		}
		
		return SUCCESS;
	}
	
	

	public File getPhoto() {
		return photo;
	}

	public void setPhoto(File photo) {
		this.photo = photo;
	}

	public UserManager getUserManager() {
		return userManager;
	}

	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}



	public String getPhotoFileName() {
		return photoFileName;
	}

	public void setPhotoFileName(String photoFileName) {
		this.photoFileName = photoFileName;
	}


}
