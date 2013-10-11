/**
 * 
 */
package cn.beansoft.scm.dao;

import java.sql.SQLException;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;


/**
 * �ṩ���¶���, ִ�� HQL ���, ��ҳ��ͨ�ù��ܵ�DAO��.
 * 
 * @author BeanSoft
 * 
 */
@org.springframework.transaction.annotation.Transactional
public class BaseDAO extends HibernateDaoSupport {

	/**
	 * ����ʵ��
	 * 
	 * @param entity
	 */
	public void save(Object entity) {
		getHibernateTemplate().save(entity);
	}

	public void saveOrUpdate(Object entity) {
		getHibernateTemplate().saveOrUpdate(entity);
	}

	/**
	 * ����ID��ʵ�����Ͳ���ʵ��.
	 * 
	 * @param t -
	 *            ʵ������
	 * @param id -
	 *            ���л�ID
	 * @return - ʵ�����
	 */
	public Object findById(Class type		, java.io.Serializable id) {
		return getHibernateTemplate().get(type, id);
	}

	/**
	 * ����ID��ʵ��������ʵ��.
	 * 
	 * @param entityName -
	 *            ʵ����
	 * @param id -
	 *            ���л�ID
	 * @return - ʵ�����
	 */
	public Object findById(String entityName, java.io.Serializable id) {
		return getHibernateTemplate().get(entityName, id);
	}

	/**
	 * ɾ��ʵ����.
	 * 
	 * @param bean
	 */
	public void delete(Object bean) {
		getHibernateTemplate().delete(bean);
	}

	/**
	 * ����IDɾ��ʵ����.
	 * 
	 * @param entityName
	 *            ʵ����
	 * @param id
	 *            ���
	 * @return ɾ�������ݼ�¼��
	 */
	public Integer deleteById(String entityName, final String id) {
		final String hql = "delete " + entityName + " where id = " + id;

		return (Integer) getHibernateTemplate().execute(
				new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query query = session.createQuery(hql);

						return query.executeUpdate();

					}
				});
	}

	/**
	 * ����ʵ��
	 * 
	 * @param entity
	 *            ʵ�����
	 */
	public void update(Object entity) {
		getHibernateTemplate().update(entity);
	}

	/**
	 * �г���������.
	 * 
	 * @param entityName - ʵ����
	 * @return
	 */
	public List findAll(String entityName) {
		return findByHQL("from " + entityName);
	}


	/**
	 * �������ֲ���.
	 * 
	 * @param entityName
	 * @param name
	 * @return
	 */
	public List findByName(String entityName, String name) {
		return findByProperty(entityName, "name", name);
	}

	/**
	 * ����������������ֵ����.
	 * 
	 * @param entityName
	 * @param propertyName
	 * @param value
	 * @return
	 */
	public List findByProperty(String entityName, String propertyName,
			Object value) {
		String hql = "from " + entityName + " where " + propertyName + " = ?";
		return findByHQL(hql, value);
	}

	/**
	 * ����ѯCOUNT�����Ϊ����������ʾ. HQL = select count(id) from Entity
	 * 
	 * @param hql ��ѯ���
	 * @param args �����б�
	 * @return ��������
	 */
	public Integer queryForCount( final String hql, final Object... args) {
		List list = getHibernateTemplate().executeFind(new HibernateCallback() {
			 public Object doInHibernate(Session session)
			 throws HibernateException, SQLException {
			 Query query = session.createQuery(hql);
			 
			 for(int i =0; i < args.length; i++) {
				 query.setParameter(i, args[i]);
			 }
			 

			 List list = query.list();
			 return list;
			 }
			});
		
		// Hibernate��count���㷵��һ�㶼�Ƕ���
		
		return Integer.parseInt(list.get(0) + "");		
	}

	/**
	 * ����ѯSUM�����Ϊ˫����������ʾ. HQL = select sum(value) from Entity
	 * 
	 * @param queryString
	 *            ��ѯ���
	 * @return double
	 */
	public double queryForSum(String queryString) {
		List results = findByHQL(queryString);
		long count = 0;

		if (results != null && results.size() > 0) {
			if (results.get(0) == null) {
				return 0;
			}

			return (Double) results.get(0);
		}

		return 0;
	}

	/**
	 * ���� HQL ��ѯ������Ϣ, ��0����������, ��: findByHQL(hql); findByHQL(hql, value);
	 * findByHQL(hql, values);
	 * 
	 * @param hql -
	 *            Hibernate ��ѯ����
	 * @param values -
	 *            �ɱ����
	 * @return ʵ���б�
	 */
	public List findByHQL(String hql, Object... values) {
		System.out.println("hql=" + hql);
		return getHibernateTemplate().find(hql, values);
	}

	/**
	 * ʹ��hql �����з�ҳ��ѯ����
	 * 
	 * @param hql
	 *            ��Ҫ��ѯ��hql���
	 * @param values
	 *            ���hql�ж����������Ҫ���룬values���Ǵ���Ĳ�������
	 * @param offset
	 *            ��һ����¼����
	 * @param pageSize
	 *            ÿҳ��Ҫ��ʾ�ļ�¼��
	 * @return ��ǰҳ�����м�¼
	 */
	public List pagedQuery(final String hql, int currentPage,
			final int pageSize, final Object... values) {
		if (currentPage == 0) {
			currentPage = 1;
		}

		final int curPage = currentPage;

		List list = getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				Query query = session.createQuery(hql);

				if (values != null) {
					for (int i = 0; i < values.length; i++) {
						query.setParameter(i, values[i]);
					}
				}

				List result = query.setFirstResult((curPage - 1) * pageSize)
						.setMaxResults(pageSize).list();
				return result;
			}
		});
		return list;
	}

}
