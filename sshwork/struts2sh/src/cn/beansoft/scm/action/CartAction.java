package cn.beansoft.scm.action;

import java.io.File;
import java.util.Collection;
import java.util.List;

import util.StringUtil;
import cn.beansoft.scm.biz.*;
import cn.beansoft.scm.entity.*;


/**
 * ���ﳵ Struts 2 Action.
 * @author BeanSoft
 */
public class CartAction extends BaseActionSupport {
	// ��Ӧ��ҵ�����
	private VendorManager vendorManager;
	// ��Ʒҵ�����
	private ProductManager productManager;
	// ����ҵ�����
	private OrderManager orderManager;


	// ͼƬ�ϴ��������
	private File photo;// �ϴ��ļ���ʱ�洢·��
	private String photoFileName;// ���ϴ����ļ���

	/**
	 * ��Session�л�ȡ���ﳵ����, ���û��, ������һ��.
	 * @return Cart
	 */
	public static Cart getCart() {
		Cart cart = (Cart)getSession("cart");

		if(cart == null) {
			cart = new Cart();
			setSession("cart", cart);
		}

		return cart;
	}

	/**
	 * ���ﳵ��������
	 */
//	void testCart() {
//		Product p = productManager.findById(1L);
//		OrderItem item =  new OrderItem();
//		item.setProduct(p);
//		item.setAmount(2);
//		getCart().addItem(item);
//	}

	/**
	 * ����ID���ҵ�����Ʒ
	 * @param productId
	 * @return
	 */
	private Product findProductById(long productId) {
		Product p = productManager.findById(productId);
		// Ԥ�ȶ�ȡ, ���� lazy �쳣
		p.getVendor().getName();

		return p;
	}

	/**
	 * ʹ��AJAX��ʽ��ӹ��ﳵ��Ʒ.
	 * @return
	 */
	public String ajaxAddProduct() {
		setMessage(null);

		String productId = getParameter("product_id");
		try {
			long pid = Long.parseLong(productId);

			if(getCart().getItem(pid) == null) {
				Product p = findProductById(pid);

				if(p == null) {
					setMessage("�����Ʒʧ��: ����Ʒ������!");
				} else {
					OrderItem item = new OrderItem();
					item.setAmount(1);
					// �������ʱ��Ʒ����
					item.setPrice(p.getPrice());
					item.setRate(p.getRate());
					item.setRebate(p.getRebate());

					item.setAddDate(new java.util.Date());
					item.setProduct(p);
					getCart().addItem(item);
					setMessage("�����Ʒ�ɹ�: ���ﳵ�й��д���Ʒ1��!");
				}
			} else {
				OrderItem item = getCart().getItem(pid);
				item.setAmount(item.getAmount() + 1);
				setMessage("�����Ʒ�ɹ�: ���ﳵ�й��д���Ʒ" + item.getAmount() + "��!");
			}
		} catch(Exception ex) {
		}

		return SUCCESS;
	}

	/**
	 * AJAX ��ʽ�޸���Ʒ��Ŀ
	 * @return
	 */
	public String ajaxChangeAmout() {
		setMessage(null);

		String productId = getParameter("product_id");
		String amountStr = getParameter("amount");
		try {
			long pid = Long.parseLong(productId);
			int amount = Integer.parseInt(amountStr);

			if(getCart().getItem(pid) == null) {
				Product p = findProductById(pid);

				if(p == null) {
					setMessage("��Ʒ������!");
				} else {
					OrderItem item = new OrderItem();
					item.setAmount(amount);
					item.setPrice(p.getPrice());
					item.setRate(p.getRate());
					item.setRebate(p.getRebate());

					item.setAddDate(new java.util.Date());
					item.setProduct(p);
					getCart().addItem(item);
				}
			} else {
				OrderItem item = getCart().getItem(pid);
				item.setAmount(amount);
			}
		} catch(Exception ex) {
		}

		return SUCCESS;
	}


	/**
	 * ʹ��AJAX��ʽ�ӹ��ﳵ�����Ʒ.
	 * @return
	 */
	public String ajaxRemoeOutProduct() {
		setMessage(null);

		String productId = getParameter("product_id");
		try {
			long pid = Long.parseLong(productId);

			getCart().removeItemByProductId(pid);
		} catch(Exception ex) {
		}

		return SUCCESS;
	}

	/**
	 * ��չ��ﳵ
	 * @return
	 */
	public String emtpyCart() {
		getCart().empty();
		return null;
	}

	/**
	 * ��������
	 * @return
	 */
	public String createOrder() {
		setMessage(null);
		// �� session ��ȡ��ǰ�û���Ϣ
		User currentUser = getSessionLoginedUser();

		if(currentUser == null) {
			setMessage("����δ��¼,���¼���ٴ���!");
			return SUCCESS;
		}

		if(getCart().isEmpty()) {
			setMessage("���ﳵΪ��, ���ܴ�������!");
			return SUCCESS;
		}

		// ��������
		Order order = new Order();
		order.setAddDate(new java.util.Date());
		order.setScmUser(currentUser);
		order.setStatus(0);
		order.setCost(getCart().getCost());
		// ��������
		Collection<OrderItem> items = getCart().getOrderItems();

		for(OrderItem item : items) {
			item.setScmOrder(order);
			order.getOrderItems().add(item);
		}

		if(getOrderManager().createOrder(order)) {
			setMessage("���������ɹ�!");
			// �޸Ŀ�������
			for(OrderItem item : items) {
				Product p = item.getProduct();
				p.setAmount(p.getAmount() - item.getAmount());
				if(p.getTotalSold() == null) {
					p.setTotalSold(0);
				}
				
				// ����
				p.setTotalSold(p.getTotalSold() 
						+ item.getAmount());
				productManager.update(p);
			}
			//��չ��ﳵ
			getCart().empty();
		} else {
			setMessage("��������ʧ��!");
		}

		return SUCCESS;
	}
	
	/**
	 * ���ﳵ��Ʒ��
	 * @return
	 */
	public String count() {
		setMessage(getCart().getItemCount() + "");
		return SUCCESS;
	}

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
			setMessage("���û�û���ύ�κ��̼���Ϣ");
		}

		return SUCCESS;
	}



	// ���ﳵ������Ʒ�б�
	public String list() {
		setMessage(null);
		setTitle("���ﳵ��Ʒ�б�");
//		testCart();
		setAttribute("orders", getCart().getOrderItems());
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
//		try {
//			long id = Long.parseLong(getParameter("id"));
//			vendor = vendorManager.findById(id);
//			if(vendor == null) {
//				throw new Exception();
//			}
//			vendor.setAudited(true);
//			vendorManager.update(vendor);
//			setMessage("���Ϊ" + id + ",��Ϊ " + vendor.getName() + " �Ĺ�Ӧ�������ɹ�");
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			setMessage("�˹�Ӧ����Ϣ������");
//		}
//
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



	public ProductManager getProductManager() {
		return productManager;
	}

	public void setProductManager(ProductManager productManager) {
		this.productManager = productManager;
	}

	public OrderManager getOrderManager() {
		return orderManager;
	}

	public void setOrderManager(OrderManager orderManager) {
		this.orderManager = orderManager;
	}
}