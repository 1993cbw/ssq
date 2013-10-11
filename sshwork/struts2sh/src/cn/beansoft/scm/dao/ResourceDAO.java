package cn.beansoft.scm.dao;

import java.util.List;

/**
 * ��Դ����DAO.
 * 
 * @see cn.beansoft.scm.entity.Resource
 * @author BeanSoft
 */
public class ResourceDAO extends BaseDAO {

	/**
	 * �����Ƿ������Դ��Ȩ������
	 * 
	 * @param uri -
	 *            URI ��ַ
	 * @return
	 */
	public boolean hasUrlResources(String uri) {
		String queryString = "select count(model.id) from Resource as model where model.uri = ?";
		List results = getHibernateTemplate().find(queryString, uri);
		long count = 0;

		if (results != null && results.size() > 0) {
			count = (Long) results.get(0);
		}

		return count > 0;
	}

}