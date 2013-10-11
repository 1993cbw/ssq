package cn.beansoft.scm.entity;

import java.io.Serializable;
import java.util.*;

import cn.beansoft.scm.dao.*;

/**
 * ���ﳵʵ����
 * 
 * @author BeanSoft
 * 
 */
public class Cart implements Serializable {
	// Map<��Ʒ����, ������>
	private Map<Long, OrderItem> items = new HashMap<Long, OrderItem>();
	private double cost;

	/**
	 * ��Ӷ�����
	 * @param item
	 */
	public void addItem(OrderItem item) {
		items.put(item.getProduct().getId(), item);
	}
	
	/**
	 * ������Ʒ��Ż�ȡ������
	 * @param productId
	 * @return
	 */
	public OrderItem getItem(long productId) {
		return items.get(productId);
	}

	/**
	 * ������Ʒ��Ŀ
	 * @param productId
	 * @param num
	 */
	public void modifyItemNumber(long productId, int num) {
		OrderItem oldItem = items.get(productId);
		oldItem.setAmount(num);
	}

	/**
	 * ���ݱ��ɾ����Ʒ
	 * @param pid
	 */
	public void removeItemByProductId(long pid) {
		items.remove(pid);
	}

	/**
	 * ��չ��ﳵ
	 */
	public void empty() {
		items.clear();
	}

	/**
	 * ���ﳵ�Ƿ�Ϊ��
	 * @return
	 */
	public boolean isEmpty() {
		if (items.size() == 0) {
			return true;
		}
		return false;
	}

	/**
	 * ��Ʒ�ܼ۸�
	 * @return
	 */
	public double getCost() {
		double totalCost = 0;
		
		Iterator<OrderItem> it = items.values().iterator();
		
		while (it.hasNext()) {
			OrderItem item = it.next();
			
			totalCost += item.getCost();
		}
		
		return totalCost;
	}

	/**
	 * ��ù��ﳵ��Ŀ��Map�б�.
	 * @return
	 */

	public Map<Long, OrderItem> getItems() {
		return items;
	}
	
	/**
	 * ��ù��ﳵ�е����ж�����(��Ʒ�б�)
	 * @return
	 */
	public Collection<OrderItem> getOrderItems() {
		return items.values();
	}

	public void setItems(Map<Long, OrderItem> items) {
		this.items = items;
	}
	
	/**
	 * ��Ʒ����(������Ʒ�ܸ���)
	 * @return
	 */
	public int getItemCount() {
		if(items != null) {
			return items.size();
		}
		
		return 0;
	}

	public static void main(String[] args) {
		Cart cart = new Cart();
		Map items = cart.getItems();
		Iterator it = items.values().iterator();
		while (it.hasNext()) {
			it.next();
		}
	}
}
