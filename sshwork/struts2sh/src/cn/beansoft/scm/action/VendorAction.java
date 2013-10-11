package cn.beansoft.scm.action;

import java.io.File;
import java.text.ParseException;
import java.util.List;

import util.BeanDebugger;
import util.FileOperate;
import util.StringUtil;


import cn.beansoft.scm.biz.UserManager;
import cn.beansoft.scm.biz.VendorManager;
import cn.beansoft.scm.dao.*;
import cn.beansoft.scm.entity.User;
import cn.beansoft.scm.entity.Vendor;

import com.opensymphony.xwork2.ActionSupport;

/**
 * ��Ӧ�� Struts 2 Action.
 * @author BeanSoft
 */
public class VendorAction extends BaseActionSupport {
	private Vendor vendor;
	// ��Ӧ��ҵ�����
	private VendorManager vendorManager;

	// ͼƬ�ϴ��������
	private File photo;// �ϴ��ļ���ʱ�洢·��
	private String photoFileName;// ���ϴ����ļ���
	
	/**
	 * ��ӹ�Ӧ��
	 */
	public String add() {
		System.out.println("getPhotoFileName=" + getPhotoFileName());

		// ������ʱ�ļ����е��ļ��� /upload Ŀ¼��
		if (getPhotoFileName() != null) {
			// ����ļ���
			String outputFile = System.currentTimeMillis()
					+ new java.util.Random().nextInt(10000) + "."
					+ FileOperate.getExtension(this.getPhotoFileName());
			FileOperate.copyFile(getPhoto().getAbsolutePath(),
					getApplication().getRealPath(
							"/upload")
							+ "/" + outputFile);
			vendor.setPhoto("/upload/" + outputFile);

		}
		// ����ע������
		vendor.setRegDate(new java.util.Date());
		vendor.setAudited(false);
		// �� session ��ȡ��ǰ�û���Ϣ
		
		vendor.setUser(getSessionLoginedUser());

		BeanDebugger.dump(vendor);

		// �����û�
		if (vendorManager.add(vendor)) {
			setMessage("�¹�Ӧ��" + vendor.getName() + "��ӳɹ�,��ȴ������󷢲���Ʒ");
			return INPUT;
		} else {
			setMessage("���ʧ��,�������������Ϣ�Ƿ�����");
		}

		return INPUT;
	}

	// ���й�Ӧ���б�
	public String list() {
		setMessage(null);
		setTitle("���й�Ӧ���б�");
		setAttribute("vendors", vendorManager.getAll());
		return SUCCESS;
	}
	
	// �ҵĹ�Ӧ���б�
	public String myList() {
		setMessage(null);
		setTitle("�ҵĹ�Ӧ���б�");
		try {
			long user_id = getParameterLong("user_id");
			List<Vendor> vendors = vendorManager.findAllByUserId(user_id);
			
			if(vendors == null) {
				throw new Exception();
			}
			
			setAttribute("vendors", vendors);
		} catch (Exception e) {
			e.printStackTrace();
			// TODO Auto-generated catch block
			setMessage("���û�û���ύ�κ��̼���Ϣ");
		}
		
		return SUCCESS;
	}
	
	// ����ID���ҵ�����Ӧ��
	public String findById() {
		setMessage(null);
		try {
			long id = getParameterLong("id");
			vendor = vendorManager.findById(id);
			
			if(vendor == null) {
				throw new Exception();
			}
		} catch (Exception e) {
			setMessage("���̼���Ϣ������");
		}
		
		return SUCCESS;
	}
	
	// �������ֲ��ҹ�Ӧ���б�
	public String findListByName() {
		setMessage(null);
		setTitle("�������ֲ��ҵĹ�Ӧ���б�");
		try {
			String name = getParameter("vend_name");
			
			if(StringUtil.isEmpty(name)) {
				setMessage("�����빩Ӧ������");
			} else {
				setTitle("�������ְ���" + name + "�Ĺ�Ӧ���б�");
				List<Vendor> vendors = vendorManager.findAllByNameInclude(name);
				
				if(vendors == null || vendors.size() == 0) {
					throw new Exception();
				}
				
				setAttribute("vendors", vendors);				
			}

		} catch (Exception e) {
			setMessage("û�з��������Ĺ�Ӧ����Ϣ");
		}
		
		return SUCCESS;
	}
	
	// �������״̬���ҹ�Ӧ���б�
	public String findAllByAudited() {
		setMessage(null);
		boolean audited = false;
		
		try {
			audited = Boolean.parseBoolean(getParameter("audited"));
		} catch (Exception e) {
		}
		
		List<Vendor> vendors = vendorManager.findAllByAudited(audited);
		
		if(vendors == null || vendors.size() == 0) {
			setMessage("û�з��������Ĺ�Ӧ���б�");
		}
		
		setAttribute("vendors", vendors);
		
		setTitle(audited ? "����˹�Ӧ���б�" : "δ��˹�Ӧ���б�");
		
		return SUCCESS;
	}
	
	// ����������Ӧ��
	public String auditById() {
		setTitle("��Ӧ���������");
		setMessage(null);
		try {
			long id = getParameterLong("id");
			vendor = vendorManager.findById(id);
			if(vendor == null) {
				throw new Exception();
			}
			vendor.setAudited(true);
			vendorManager.update(vendor);
			setMessage("���Ϊ" + id + ",��Ϊ " + vendor.getName() + " �Ĺ�Ӧ�������ɹ�");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			setMessage("�˹�Ӧ����Ϣ������");
		}
		
		return SUCCESS;
	}
	
	/**
	 * �鿴��ǰ�û��ķֳ��ܶ�.
	 * @return
	 */
	public String viewDenductSum() {
		setTitle("�鿴���ķֳ��ܽ��");
		
		User currentUser = getSessionLoginedUser();
		
		if(currentUser == null) {
			setMessage("����δ��¼!");
			return SUCCESS;
		} else {
			setMessage(vendorManager.getDenductSum(currentUser.getId()) + " Ԫ");
		}
		return SUCCESS;
	}
	
	public File getPhoto() {
		return photo;
	}

	public void setPhoto(File photo) {
		this.photo = photo;
	}

	public VendorManager getVendorManager() {
		return vendorManager;
	}

	public void setVendorManager(VendorManager vendorManager) {
		this.vendorManager = vendorManager;
	}


	public String getPhotoFileName() {
		return photoFileName;
	}

	public void setPhotoFileName(String photoFileName) {
		this.photoFileName = photoFileName;
	}

	public Vendor getVendor() {
		return vendor;
	}

	public void setVendor(Vendor vendor) {
		this.vendor = vendor;
	}

}
