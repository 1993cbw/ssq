/**
 * 
 */
package cn.beansoft.scm.biz;

import java.util.List;

import cn.beansoft.scm.dao.*;
import cn.beansoft.scm.entity.Vendor;

/**
 * ��Ӧ��ҵ��ģ��.
 * @author BeanSoft
 */

public class VendorManager {
	// DAO ʵ��
	private VendorDAO dao;

	/**
	 * @return the cn.beansoft.scm.dao
	 */
	public VendorDAO getDao() {
		return dao;
	}

	/**
	 * @param cn.beansoft.scm.dao the cn.beansoft.scm.dao to set
	 */
	public void setDao(VendorDAO dao) {
		this.dao = dao;
	}
	
    
    /**
     * ִ������û������ݿ�Ĳ���, ���ȼ����û��Ƿ��Ѿ�����.
     * @param user �û�����
     */
    public boolean add(Vendor bean) {
        dao.save(bean);
        return true;
    }
    
     /**
     * �����û��������ݿ��в����û�����.
     * @param username �û���
     * @return User �û�����
     */
    public List<Vendor> findByName(String name) {
        List<Vendor> results = dao.findByName(name);
        
        return results;
    }
    
    public List<Vendor> findAllByNameInclude(String name) {
        List<Vendor> results = dao.findAllByNameInclude(name);
        
        return results;
    }    
    
    /**
     * �����û�ID�������й�Ӧ�̶���.
     * @param id �û�ID
     * @return List<Vendor>
     */
    public List<Vendor> findAllByUserId(long id) {
        return dao.findAllByUserId(id);
    }
    
    /**
     * �������״̬�������й�Ӧ�̶���.
     * @param 
     * @return List<Vendor>
     */
    public List<Vendor> findAllByAudited(boolean audited) {
        return dao.findByAudited(audited);
    }
    
    /**
     * ����ID�����ݿ��в��Ҷ���.
     * @param ID ���
     * @return �ҵ��Ķ���
     */
    public Vendor findById(long id) {
        return dao.findById(id);
    }
    
    /**
     * ��ȡ�����û��б�.
     * ί�е� DAO �����.
     * @return �����û��б�
     */
    public List<Vendor> getAll() {
        return getDao().findAll("Vendor");
    }
    
    
    /**
     * ���¶�����Ϣ.
     */
    public void update(Vendor bean) {
        dao.update(bean);
    }

	/**
	 * ��ȡ�����û��ķֳ��ܶ�.
	 * @param userId
	 * @return
	 */    
    public double getDenductSum(long userId) {
    	return dao.getDenductSum(userId);
    }
    
}
