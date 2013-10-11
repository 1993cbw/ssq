package test;
import org.hibernate.Transaction;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import cn.beansoft.scm.dao.*;
import cn.beansoft.scm.entity.User;
import cn.beansoft.scm.entity.Vendor;


public class VendorDAOTest {

	public static void main(String[] args) {
		ApplicationContext ctx = 
			new ClassPathXmlApplicationContext("applicationContext.xml");

		UserDAO userDao = (UserDAO)ctx.getBean("UserDAO");
		
		VendorDAO vendorDAO = (VendorDAO)ctx.getBean("VendorDAO");
		
		User user  = (User) userDao.findById(User.class, new Long(2l));

		System.out.println(user);
		
		Vendor vendor = new Vendor();
		vendor.setName("BeanSoft������2");
		vendor.setAddress("����־ǿ԰��������");
		vendor.setAuditDate(new java.util.Date());
		vendor.setRegDate(new java.util.Date());
		vendor.setAuditor(user);
		vendor.setUser(user);
		
		//user.getVendorsForAuditor().add(vendor);
		
		vendorDAO.save(vendor);
		//userDao.save(user);
//		
//		tran.commit();
//		
//		vendor = vendorDAO.findById(5L);
//		
//		System.out.println(vendor.getScmUserByAuditor().getRealName());;
	}

}