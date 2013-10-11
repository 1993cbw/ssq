/**
 * 
 */
package cn.beansoft.scm.biz;

import java.util.List;

import cn.beansoft.scm.dao.*;
import cn.beansoft.scm.entity.Product;

/**
 * ��Ʒҵ��ģ��.
 * @author BeanSoft
 */

public class ProductManager {
	// DAO ʵ��
	private ProductDAO dao;

	/**
	 * @return the cn.beansoft.scm.dao
	 */
	public ProductDAO getDao() {
		return dao;
	}

	/**
	 * @param cn.beansoft.scm.dao the cn.beansoft.scm.dao to set
	 */
	public void setDao(ProductDAO dao) {
		this.dao = dao;
	}
	
    
    /**
     * ִ������û������ݿ�Ĳ���, ���ȼ����û��Ƿ��Ѿ�����.
     * @param user �û�����
     */
    public boolean add(Product bean) {
        dao.save(bean);
        return true;
    }
    
     /**
     * �����û��������ݿ��в����û�����.
     * @param username �û���
     * @return User �û�����
     */
    public List<Product> findByName(String name) {
        List<Product> results = dao.findByName("Product", name);
        
        return results;
    }
    
    /**
     * ���ݰ������ֺ��Ƿ��п�������Ʒ��Ϣ.
     * @param name - ��Ʒ��
     * @param available - �Ƿ��п��
     * @return - ��ѯ���, ��Ʒ�б�
     */
    public List<Product> findAllByNameIncludeAmount(String name, boolean available, int... pageParams) {
    	String hql = "from Product where name like '%" + name + "%' and audited = true";
    	if(available == true) {
    		hql += " and amount > 0";
    	}
    	
    	System.out.println("hql=" + hql);
    	
        List<Product> results = dao.findByHQL(hql, pageParams);
        
        return results;
    }
    /**
     * ����������ֺ��Ƿ��п�����Ʒ�ļ�¼��.
     * @param name - ��Ʒ��
     * @param available - �Ƿ��п��
     * @return - ��¼��
     */    
    public long countFindAllByNameIncludeAmount(String name, boolean available) {
    	String hql = "select count(id) from Product where name like '%" + name + "%' and audited = true";
    	if(available == true) {
    		hql += " and amount > 0";
    	}
    	
    	System.out.println("hql=" + hql);
    	
    	return dao.queryForCount(hql);
    }
    
    /**
     * �������״̬����������Ʒ����(����ҳ����)
     * @return List<Product>
     * @param pageParams - ����1: ��ǰҳ��, ����2: ��ʾ������
     */
    public List<Product> findAllByAudited(boolean audited, int... pageParams) {
    	if(pageParams.length == 0) {
    		return dao.findByAudited(audited);
    	} else {
    		return dao.pagedQuery("from Product where audited = " + audited, pageParams[0], pageParams[1]);
    	}
    }
    
    /**
     * �������˵Ĳ�Ʒ��¼��.
     * @param audited
     * @return
     */
    public long getProductCountByAudited(boolean audited) {
    	return dao.queryForCount("select count(id) from Product where audited = " + audited);
    }
    
    /**
     * ����ID�����ݿ��в��Ҷ���.
     * @param ID ���
     * @return �ҵ��Ķ���
     */
    public Product findById(long id) {
        return (Product) dao.findById(Product.class, id);
    }
    
    /**
     * ��ȡ���в�Ʒ�б�.
     * @return ���в�Ʒ�б�
     */
    public List<Product> getAll() {
        return getDao().findAll("Product");
    }
    
    /**
     * ���¶�����Ϣ.
     */
    public void update(Product bean) {
        dao.update(bean);
    }

}