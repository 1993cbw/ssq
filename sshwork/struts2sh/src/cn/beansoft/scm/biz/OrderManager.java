/**
 * 
 */
package cn.beansoft.scm.biz;

import java.util.List;

import cn.beansoft.scm.dao.*;
import cn.beansoft.scm.entity.Order;

/**
 * �����Ͷ�����ҵ��ģ��
 * 
 * @author BeanSoft
 * 
 */
public class OrderManager {
	// DAO ʵ��
	private OrderDAO orderDao;
	// OrderItem �� DAO ʵ��
	private OrderItemDAO orderItemDao;

	/**
	 * ����������
	 * @param order
	 * @return
	 */
	public boolean createOrder(Order order) {
		try {
			orderDao.save(order);
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	/**
	 * �г�ָ���û��Ķ���.
	 * @param userId
	 * @param pageParams
	 * @return
	 */
	public List<Order> listMyOrders(long userId, int... pageParams ) {
		String hql = " from Order o where o.scmUser.id = " + userId;
		
		if(pageParams.length > 0) {
			return orderDao.findByHQL(hql, pageParams[0], pageParams[1]);
		} else {
			return orderDao.findByHQL(hql);
		}
	}
	
	/**
	 * �г�ָ���û��Ķ���, �����Ƿ�֧��������.
	 * @param userId
	 * @param payed - �Ƿ�֧��
	 * @param pageParams
	 * @return �����б�
	 */
	public List<Order> listMyOrders(long userId, boolean payed, int... pageParams ) {
		String hql = " from Order o where o.scmUser.id = " + userId + " and o.status  ";
		if(payed) {
			hql += " > 0";
		} else {
			hql += " = 0";
		}
		
		if(pageParams.length > 0) {
			return orderDao.findByHQL(hql, pageParams[0], pageParams[1]);
		} else {
			return orderDao.findByHQL(hql);
		}
	}
	
	public OrderDAO getOrderDao() {
		return orderDao;
	}

	public void setOrderDao(OrderDAO orderdao) {
		this.orderDao = orderdao;
	}

	public OrderItemDAO getOrderItemDao() {
		return orderItemDao;
	}

	public void setOrderItemDao(OrderItemDAO orderItemDao) {
		this.orderItemDao = orderItemDao;
	}
	

}
