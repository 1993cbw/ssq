/**
 * 
 */
package cn.beansoft.scm.biz;

import java.util.List;

import cn.beansoft.scm.dao.*;
import cn.beansoft.scm.entity.User;

/**
 * �û�ҵ��ģ��, ������¼, ע��, �޸ĵȵ�.
 * @author BeanSoft
 *
 */
public class UserManager {
	// DAO ʵ��
	private UserDAO dao;

	/**
	 * @return the cn.beansoft.scm.dao
	 */
	public UserDAO getDao() {
		return dao;
	}

	/**
	 * @param cn.beansoft.scm.dao the cn.beansoft.scm.dao to set
	 */
	public void setDao(UserDAO dao) {
		this.dao = dao;
	}
	
	   /**
     * �����û�����������е�¼���.
     * @param username �û���
     * @param password ����
     */
    public boolean checkLogin(String username, String password, int userType) throws Exception {
		// 1. ���������MD5ժҪ����
		password = util.MD5Bean.md5(password.getBytes());
		// 2. �Ƿ���ڸ����û�
        User user = findByName(username);
        if (user == null) {
			// 3. �û�������
			throw new Exception("�û�������");
		}
        
        // 4. ����Ƿ񼤻�
        if (user.isActive() == false) {
			throw new Exception("�û��ʺ���δ����, �������Ա��ϵ.");
		}        
        
        
        // 5. �������, �û������Ƿ�ƥ��
        if(user != null && user.getPassword() !=  null && user.getPassword().equals(password)
        		&& user.getUserType() == userType) {
            return true;
        }
        
        return false;
    }
    
    
    /**
     * ִ������û������ݿ�Ĳ���, ���ȼ����û��Ƿ��Ѿ�����.
     * @param user �û�����
     */
    public boolean save(User user) {
        User userCheck = findByName(user.getName());
        
        if(userCheck != null) {
            return false;
        }
        
        dao.save(user);
        return true;
    }
    
    public void delete(User user) {
    	dao.delete(user);
    }
    
     /**
     * �����û��������ݿ��в����û�����.
     * @param username �û���
     * @return User �û�����
     */
    public User findByName(String username) {
        List<User> users = dao.findByName(username);
        if(users != null && users.size() > 0) {
        	return users.get(0);
        }
        
        return null;
    }
    
    /**
     * ����ID�����ݿ��в��Ҷ���.
     * @param ID ���
     * @return �ҵ��Ķ���
     */
    public User findById(long id) {
        return (User) dao.findById(User.class, new Long(id));
    }    
    
    /**
     * ����Email�����ݿ��в����û�����.
     * @param username �û���
     * @return User �û�����
     */
    public User findByEmail(String email) {
        List<User> users = dao.findByEmail(email);
        if(users != null && users.size() > 0) {
        	return users.get(0);
        }
        
        return null;
    }
    
    /**
     * ��ȡ�����û��б�.
     * ί�е� DAO �����.
     * @return �����û��б�
     */
    public List<User> getAllUsers() {
        return getDao().findAll("User");
    }
    
    /**
     * �����û�����. ����֮ǰ��Ҫ���ȼ��������Ƿ���ȷ.
     * @param oldPassword ������
     * @param user ������������û�����
     */
    public boolean changeUserPassword(String oldPassword, User user) throws Exception {
        if(!checkLogin(user.getName(), oldPassword, user.getUserType())) {
            return false;
        }
        
        user.setPassword(util.MD5Bean.md5(user.getPassword()));
        
        dao.update(user);
        
        return true;
    }
    
    /**
     * �����û���Ϣ.
     * @param oldPassword ������
     * @param user ������������û�����
     */
    public void update(User user) {
        dao.update(user);
    }

	/**
	 * ��ȡ�û�������
	 * @return
	 */
	public int getTotalUsers() {
		return (int)( dao.queryForCount("select count(id) from User") );
	}
    
	/**
	 * ��ҳ�г��û�����
	 * @param currentPage ��ǰҳ
	 * @param pageSize ÿҳ��ʾ����
	 * @return ��ҳ�������
	 */
	@SuppressWarnings("unchecked")
	public List<User> pageUsers(int currentPage, int pageSize) {
		return dao.pagedQuery("from User", currentPage, pageSize);
	}	
}
