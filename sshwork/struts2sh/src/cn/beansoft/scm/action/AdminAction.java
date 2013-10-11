package cn.beansoft.scm.action;
import java.util.ArrayList;
import java.util.List;

import util.BeanDebugger;
import util.PageBean;
import util.StringUtil;
import util.mail.MailSender;
import cn.beansoft.scm.dao.BaseDAO;
import cn.beansoft.scm.entity.*;



/**
 * ϵͳ����Աģ��.
 * 
 * @author BeanSoft
 * 
 */
public class AdminAction extends UserAction {
	
	/**
	 * ���ݱ��ɾ��ʵ��.
	 * ��Ҫ�Ĳ���: 
	 * entity=ʵ����
	 * id=����
	 * ��ѡ����:entityDesc=ʵ����������
	 * @return
	 */
	public String deleteByID() {
		resetMessages();
		String entityName = getParameter("entity");// ʵ����
		String entityDesc = getParameter("entityDesc");// ʵ����������
		
		System.out.println(entityDesc);
		
		String id = getParameter("id");// ����
		if(id != null && entityName != null) {
			getBaseDAO().deleteById(entityName, id);
			if(entityDesc == null) {
				entityDesc = "����";
			}
			
			setMessage("���Ϊ " + id + " ��" + entityDesc + " ɾ���ɹ�");
			
			setAttribute("returnURL", getRequest().getHeader("referer"));
		}
		
		
		return "message";

	}
	
	/**
	 * �����û������� ==> ��ʾ�û���Ϣ�Ľ��ҳ��
	 */
	public String searchByUserName() {

		User u = getUserManager().findByName(getUser().getName());

		setAttribute("user", u);

		return "viewUser";
	}

	/**
	 * ����ID�����û���Ϣ����ʾ.
	 * @return
	 * @throws Exception
	 */
	public String findUserById() throws Exception {
		super.findById();// ί�и��෽��
		return "viewUser";
	}
	
	/**
	 * ת���û��޸�ҳ��
	 * @return
	 * @throws Exception 
	 */
	public String toEditUser() throws Exception {
		findUserById();
		return "editUser";
	}

	/**
	 * �޸��û�����.
	 */
	public String updateUser() {
		setMessage(null);
		// �� session ��ȡ��ǰ�û���Ϣ
		User userInDB = getUserManager().findById(getParameterLong("user.id"));
		
		if(userInDB == null) {
			setMessage("����ʧ��, ���û���Ϣ������!");
			return "editUser";
		}
		
		// ���±�Ҫ��Ϣ
		userInDB.setRealName(getUser().getRealName());
		userInDB.setAddress(getUser().getAddress());
		userInDB.setPostCode(getUser().getPostCode());
		userInDB.setHomePhone(getUser().getHomePhone());
		userInDB.setCellPhone(getUser().getCellPhone());
		userInDB.setOfficePhone(getUser().getOfficePhone());
		userInDB.setIm(getUser().getIm());
		userInDB.setWebsite(getUser().getWebsite());
		userInDB.setNote(getUser().getNote());
		// �ʺ���������
		userInDB.setActive(getUser().isActive());
		
		// ����ǿ�ʱ�������벢֪ͨ�û�
		String password = getParameter("password");
		
		if(!StringUtil.isEmpty(password)) {
			userInDB.setPassword(util.MD5Bean.md5(password));
			
	        MailSender sender = new MailSender();
	
	        sender.setFrom("\"Admin\" <admin@beansoft.cn>");
	        sender.setTo(userInDB.getEmail());
	        sender.setSubject("SCMϵͳ�޸�����֪ͨ�ʼ�");
	        sender.setBody("����Ա�޸��������ʻ�����, ��������" + password + ", ��������¼���޸Ĵ�����!");
	
	        System.out.println(sender.sendMail());
		}
        
		BeanDebugger.dump(getUser());

		// �����û�
		getUserManager().update(userInDB);
		setMessage("�û�" + userInDB.getName() + "�����޸ĳɹ�");


		return "message";
	}
	
	/**
	 * ����û�==> ֱ�Ӽ���, �����ע����, ����Ҫʵ���û��ʼ�֪ͨ, �������ܺ�Userģ���ע������ͬ��
	 */
	public String addUser() {
		// TODO �û�Ψһ��̨��֤ 1. ����SQL��unique 2. ��java����
		// �û������дʽ�ֹע��
		// 2008-10-29 fix bug: getParameter(getUser().getName())
		if (util.BadWordFilter.hasBadWords(getUser().getName())) {
			setMessage("�����û����������й�����������������дʻ�");
			return "message";
		}

		// ����MD5����
		getUser().setPassword(
				util.MD5Bean.md5(getUser().getPassword().getBytes()));
		// ����ע������
		getUser().setRegDate(new java.util.Date());
		// ���ü���״̬
		getUser().setActive(true);

		getUserManager().save(getUser());

		// �������ļ��������ϴ���ͼƬ, ������ʱ�ļ����е��ļ��� /upload Ŀ¼��
		if (getPhotoFileName() != null && getPhotoFileName().length() > 0) {
			// �ϴ�Ŀ¼
			String uploadFolder = getApplication()
					.getRealPath("/upload");
			String uploadPhotoFileName = System.currentTimeMillis() + "_"
					+ new java.util.Random().nextInt(10000) + "_"
					+ util.Counter.getInstance().nextValue() + "."
					+ util.FileOperate.getExtension(getPhotoFileName());
			
			util.FileOperate.copyFile(getPhoto().getAbsolutePath(),
					uploadFolder + java.io.File.separator
							+ uploadPhotoFileName);
			// ����ͷ��Ĵ洢·��
			getUser().setPhoto("/upload/" +uploadPhotoFileName);
			getUserManager().update(getUser());
		}

		setMessage("�û���ӳɹ�");
		return "message";
	}
	
	/**
	 * ��ҳ��ʾ�û��б�.
	 * @return
	 */
	public String listUser() {
		super.list();// ���ø�����б���
		
		PageBean pageBean =  (PageBean) getAttribute("pageBean");
		if(pageBean != null) {
			pageBean.setPageUrl("admin/listUser.action");
		}
		
		return "userList";
	}
	
	/**
	 * ���� ID ɾ���û�.
	 * @return
	 */
	public String deleteUser() {
		int id = getParameterInt("id");
		
		if(id > 0) {
			User u = getUserManager().findById(id);
			getUserManager().delete(u);
			setMessage("ɾ���ɹ�");
			setAttribute("returnURL", getRequest().getHeader("referer"));
		}
		
		
		return "message";
	}
	
	public String advanceUserQuery() {
		setMessage("��ѯ����Ϊ:" + getParameter("condition"));
		return "message";
	}
	
	/**
	 * �������ݿ��Application�����е���վȫ������.
	 * @return
	 */
	public String updateAppConfig() {
		BaseDAO dao = getBaseDAO();
		AppConfig appConfig = (AppConfig) dao.findById(AppConfig.class, new Integer(1));
		
		appConfig.setAfficheContent(getParameter("appConfig.afficheContent"));
		appConfig.setAppTitle(getParameter("appConfig.appTitle"));
		appConfig.setBadwords(getParameter("appConfig.badwords"));
		appConfig.setAfficheTitle(getParameter("appConfig.afficheTitle"));
		appConfig.setCopyright(getParameter("appConfig.copyright"));
	
		BeanDebugger.dump(appConfig);
		
		// ����ϵͳ����		
		dao.update(appConfig);
		setTitle("ϵͳ����");
		setMessage("��վ���ø��³ɹ�");
		getApplication().setAttribute("appConfig", appConfig);

		// �������������д��б�
		String words = appConfig.getBadwords();
		String[] wordsArray = words.split("\r\n");
		ArrayList<String> wordLists = new ArrayList<String>();
		for (String word : wordsArray) {
			System.out.println("�������д� " + word);
			wordLists.add(word);
		}
		
		util.BadWordFilter.setWordList(wordLists);

		
		return "message";
	}
	
	/**
	 * ��ӽ�ɫ.
	 * @return
	 */
	public String addRole() {
		super.resetMessages();
		Role role = new Role();
		role.setRoleName(getParameter("roleName"));
		getBaseDAO().save(role);
		
		setTitle("����");
		setMessage("��ɫ��ӳɹ�");
		
		return "message";
	}
	
	/**
	 * �鿴��ɫ�б�.
	 * @return
	 */
	public String roleList() {
		super.resetMessages();
		List roles = getBaseDAO().findAll("Role");
		setAttribute("roles", roles);
		
		return "roleList";
	}
	
	/**
	 * ת�������Դҳ��.
	 * @return
	 */
	public String toAddResource() {
		roleList();
		return "addResource";
	}
	
	/**
	 * �����Դ�������.
	 * @return
	 */
	public String addResource() {
		super.resetMessages();
		setTitle("���� - �����Դ���");
		
		Resource r = null;
		
		String uri = getParameter("uri");
		
		if(StringUtil.isEmpty(uri)) {
			setMessage("��Դ����Ϊ��");
			return "message";
		}
		
		int roleId = getParameterInt("roleId");
		
		String hql = "from Resource res where res.uri = ? and res.scmRole.id = ?";
		
		List<Resource> roles = getBaseDAO().findByHQL(hql, uri, roleId);
		
		if(roles != null && roles.size() > 0) {
			setMessage("����Դ����Ѵ���, �������δ�����κβ���.");
		} else {
			r = new Resource();
			r.setUri(getParameter("uri"));
			r.setScmRole((Role) getBaseDAO().findById(Role.class, getParameterInt("roleId")));			
			r.setAddDate(new java.util.Date());
			r.setNote(getParameter("note"));
			
			getBaseDAO().save(r);
			setMessage("��Դ�����ӳɹ�.");			
		}
		
		return "message";
	}
	
	public String resourceList() {
		List<Resource> resources = getBaseDAO().findByHQL("from Resource order by uri");
		setAttribute("resources", resources);
		return "resourceList";
		
	}
	
}
