package setup.swing;

/**
 * ���ò���.
 * @author BeanSoft
 *
 */
public interface ConfigParams {
	/** Hibernate ģ���ļ� */
	public static final String HIBERNATE_TEMPLATE_FILE = "/hibernate.cfg.txt";
	/** Hibernate �����ļ����� */
	public static final String HIBERNATE_TEMPLATE_XML = "/hibernate.cfg.xml";
	/** JDBC �������� */
	public static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";

	/** Web Ӧ�õ� Root Ŀ¼ */
	public static final String WEBROOT = "WebRoot";
	/** classes Ŀ¼ */
	public static final String WEB_INF_PATH_CLASSES = WEBROOT + "/WEB-INF/classes";
	/** WAR �ļ����� TODO ���޸Ĵ�Ŀ¼ */
	public static final String WAR_NAME = "struts2sh";
	/** ���ݿ����ӱ��� */
	public static final String ENCODING = "UTF-8";
	/** MySQL URL ������� */
	public static final String MYSQL_ENCODING = "?useUnicode=true&characterEncoding=" + ENCODING;
}