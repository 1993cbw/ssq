package cn.beansoft.scm.action;

import java.io.File;
import java.util.List;
import java.util.Map;

import util.BeanDebugger;
import util.FileOperate;
import util.PageBean;
import util.StringUtil;
import cn.beansoft.scm.biz.ProductManager;
import cn.beansoft.scm.biz.VendorManager;
import cn.beansoft.scm.entity.Product;
import cn.beansoft.scm.entity.User;
import cn.beansoft.scm.entity.Vendor;


/**
 * ��Ʒ Struts 2 Action.
 * @author BeanSoft
 */
public class ProductAction extends BaseActionSupport {
	private Product product;

	
	// ��Ӧ��ҵ�����
	private VendorManager vendorManager;
	// ��Ʒҵ�����
	private ProductManager productManager;
	

	// ͼƬ�ϴ��������
	private File photo;// �ϴ��ļ���ʱ�洢·��
	private String photoFileName;// ���ϴ����ļ���
	
	// �����ҵĹ�Ӧ���б�ת�������Ʒҳ��
	public String toAddPage() {
		setMessage(null);
		setTitle(null);
		// �� session ��ȡ��ǰ�û���Ϣ
		User currentUser = getSessionLoginedUser();
		
		if(currentUser == null) {
			setMessage("����δ��¼!");
			return SUCCESS;
		}
		
		try {
			long user_id = currentUser.getId();
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
	
	/**
	 * �����Ʒ
	 */
	public String add() {
		System.out.println(product.getVendor().getId());
		
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
			product.setPhoto("/upload/" + outputFile);

		}
		// ����ע������
		product.setAddDate(new java.util.Date());
		product.setAudited(false);
		// ��������Ϊ0
		product.setTotalSold(0);
		
		// ����ID���ҹ�Ӧ����Ϣ
		Vendor vendor = vendorManager.findById(product.getVendor().getId());
		if(vendor != null) {
			product.setVendor(vendor);
		} else {
			setMessage("���ṩ��Ʒ�Ĺ�Ӧ����Ϣ");
			return INPUT;
		}
		
		BeanDebugger.dump(product);

		// �����û�
		if (productManager.add(product)) {
			setMessage("����Ʒ" + product.getName() + "��ӳɹ�,��ȴ������󷢲���Ʒ");
			return SUCCESS;
		} else {
			setMessage("��Ʒ���ʧ��,�������������Ϣ�Ƿ�����");
		}

		return INPUT;
	}

	// ������Ʒ�б�(�����)
	public String list() {
		setMessage(null);
		setTitle("������Ʒ�б�");
		int currentPage = getParameterInt("page");
		
		PageBean pageBean = new PageBean();
		pageBean.setCurrentPage(currentPage);
		pageBean.setPageCount(2);
		pageBean.setRecordCount((int)productManager.getProductCountByAudited(true));
		pageBean.setPageUrl("product/list.action");
		setAttribute("pageBean", pageBean);
		
		setAttribute("products", 
				productManager.findAllByAudited(true, currentPage, pageBean.getPageCount()) );
		
		if("true".equalsIgnoreCase(getParameter("ajax"))) {
			return "ajax";
		}
		
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
	
	// ����ID���ҵ�����Ʒ
	public String findById() {
		setMessage(null);
		setTitle("�鿴��Ʒ��Ϣ");
		try {
			long id = getParameterLong("id");
			product = productManager.findById(id);
			
			if(product == null) {
				throw new Exception();
			}
		} catch (Exception e) {
			setMessage("����Ʒ��Ϣ������");
		}
		
		return SUCCESS;
	}
	
	// �������ֲ�����Ʒ�б�, ���������Ƿ��п��
	public String findProductByName() {
		setMessage(null);
		setTitle("��Ʒ���ҽ��");
		System.out.println("mergeParamsAsURI=" + mergeParamsAsURI());
		
		try {
			String name = getParameter("keyword");
			String availableStr = getParameter("available");
			
			boolean available = false;
			
			try {
				available = Boolean.parseBoolean(availableStr);
			} catch(Exception ex) {
				
			}
			
			if(StringUtil.isEmpty(name)) {
				setMessage("��������Ʒ����");
			} else {
				setTitle("�������ְ���" + name + "����Ʒ�б�");
				List<Product> products = productManager.findAllByNameIncludeAmount(name, available);
				
				int currentPage = getParameterInt("page");
				PageBean pageBean = new PageBean();
				pageBean.setCurrentPage(currentPage);
				pageBean.setPageCount(2);
				pageBean.setRecordCount((int)productManager.countFindAllByNameIncludeAmount(name, available));
				pageBean.setPageUrl("product/findByName.action?" + mergeParamsAsURI());
				setAttribute("pageBean", pageBean);
				
				if(products == null || products.size() == 0) {
					throw new Exception();
				}
				
				setAttribute("products", products);				
			}

		} catch (Exception e) {
			setMessage("û�з�����������Ʒ��Ϣ");
		}
		
		if("true".equalsIgnoreCase(getParameter("ajax"))) {
			return "ajax";
		}
		
		return SUCCESS;
	}
	
	// �������״̬������Ʒ�б�
	public String findAllByAudited() {
		setMessage(null);
		boolean audited = false;
		
		try {
			audited = Boolean.parseBoolean(getParameter("audited"));
		} catch (Exception e) {
		}
		
		List<Product> products = productManager.findAllByAudited(audited);
		
		if(products == null || products.size() == 0) {
			setMessage("û�з�����������Ʒ�б�");
		}
		
		setAttribute("products", products);
		
		setTitle(audited ? "�������Ʒ�б�" : "δ�����Ʒ�б�");
		
		return SUCCESS;
	}
	
	// ����������Ʒ
	public String auditById() {
		setTitle("��Ʒ�������");
		setMessage(null);
		try {
			long id = Long.parseLong(getParameter("id"));
			Product p  = productManager.findById(id);
			if(p == null) {
				throw new Exception();
			}
			p.setAudited(true);
			productManager.update(p);
			setMessage("���Ϊ" + id + ",��Ϊ " + p.getName() + " ����Ʒ�����ɹ�");
		} catch (Exception e) {
			setMessage("����Ʒ��Ϣ������");
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

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public ProductManager getProductManager() {
		return productManager;
	}

	public void setProductManager(ProductManager productManager) {
		this.productManager = productManager;
	}
}
