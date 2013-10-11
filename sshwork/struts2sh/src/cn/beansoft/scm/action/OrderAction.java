package cn.beansoft.scm.action;

import java.util.List;

import util.StringUtil;

import cn.beansoft.scm.entity.Order;

/**
 * ����Action.
 * @author BeanSoft
 *
 */
public class OrderAction extends CartAction {
	/**
	 *  �ҵĶ����б�
	 * @return
	 */
	public String myOrderList() {
		setMessage(null);
		setTitle("�ҵĶ����б�");
		try {
			long user_id = getSessionLoginedUser().getId();
			
			List<Order> orders = getOrderManager().listMyOrders(user_id);
			
			// ����Ƿ�֧���Ĳ���
			if(!StringUtil.isEmpty(getParameter("payed"))) {
				boolean payed = Boolean.parseBoolean(getParameter("payed"));
				orders = getOrderManager().listMyOrders(user_id, payed);
				
				setTitle("�ҵĶ����б� - " + (payed? "��֧��" : "δ֧��") );
			}
			
			System.out.println(orders.size());

			if(orders == null || orders.size() == 0) {
				setMessage("û�з��������Ķ�����Ϣ");
			}

			setAttribute("orders", orders);
		} catch (Exception e) {
			e.printStackTrace();
			setMessage("���û�û���κζ�����Ϣ");
		}

		return SUCCESS;
	}
	
	/**
	 * �鿴������������ϸ����.
	 * @return
	 */
	public String viewOrderDetail() {
		resetMessages();

		setTitle("�鿴��������");
		try {
			long orderId = getParameterLong("id");
			
			Order order = (Order) getBaseDAO().findById(Order.class, orderId);
			
			System.out.println(order);

			setAttribute("order", order);
		} catch (Exception e) {
			e.printStackTrace();
			setMessage("�˶�����Ϣ������");
		}
		
		return "viewOrder";
	}
	
	/**
	 *  �ҵ��ҵĹ�������
	 * @return
	 */
	public String myBuyCount() {
		setMessage(null);
		setTitle("�ҵĹ�������");
		try {
			long user_id = getSessionLoginedUser().getId();
			
			String hql = "select sum(o.amount) from OrderItem o where o.scmOrder.scmUser.id = " + user_id;
			
			long count = getBaseDAO().queryForCount(hql);
			setMessage("��ǰ������" + count + "��");
			
			hql = "select sum(o.cost) from Order o where o.scmUser.id = " + user_id;
			
			double cost = getBaseDAO().queryForSum(hql);
			
			setMessage(getMessage() + "<br>" + "������֧��Ϊ:" + cost + "Ԫ");
		} catch (Exception e) {
			e.printStackTrace();
			setMessage("��ǰû���κι�����Ϣ");
		}

		return "message";
	}	

}
