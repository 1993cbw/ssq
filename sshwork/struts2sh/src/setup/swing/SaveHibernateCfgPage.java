package setup.swing;

import java.sql.Connection;
import java.util.Map;
import java.util.StringTokenizer;
import java.io.*;

import org.netbeans.spi.wizard.WizardPanelNavResult;

import setup.db.DatabaseManager;
import setup.db.DatabaseManagerImplMysql;
import util.FileUtil;

/**
 * 7. ���� Hibernate ���Ӳ��� ��ҳ
 * 
 */
public class SaveHibernateCfgPage extends TextAreaButtonWizardPage {
	public static String getDescription() {
		return "���� Hibernate ���Ӳ���";
	}

	public SaveHibernateCfgPage() {
		setButtonText("����Hibernate����");
		enableSkip();
	}

	protected void renderingPage() {
		Map settings = getWizardDataMap();

		setText("��ҳ�������ݿ�������Ϣд�� WEB-INF/classes ����� Hibernate ȫ�������ļ�"
				+ ConfigParams.HIBERNATE_TEMPLATE_XML);
	}

	@Override
	public void buttonClick() {
		setBusy(true);

		Map settings = getWizardDataMap();
		String username = (String) (settings.get("jdbc.username"));
		String host = (String) (settings.get("jdbc.host"));
		String port = (String) (settings.get("jdbc.port"));
		String dbName = (String) (settings.get("jdbc.dbname"));
		String pwd = (String) (settings.get("jdbc.password"));

		// ������ݿ�����
		appendLine("�����������ݿ�...");

		String url = "jdbc:mysql://" + host + ":" + port + "/";
		// �������ݿ����Ӳ���
		try {
			String template = util.FileUtil.readFileAsString(getClass()
					.getResourceAsStream(ConfigParams.HIBERNATE_TEMPLATE_FILE));

			template = template.replaceAll("_username_", username);
			template = template.replaceAll("_password_", pwd);
			template = template.replaceAll("_url_", url + dbName);

			appendLine("�����ļ���ϸ��Ϣ:" + template);

			if (!FileUtil.writeFileString(ConfigParams.WEB_INF_PATH_CLASSES
					+ "/" + ConfigParams.HIBERNATE_TEMPLATE_XML, template,
					"UTF-8")) {
				setProblem("�޷�д�� Hibernate �����ļ���Ϣ "
						+ ConfigParams.HIBERNATE_TEMPLATE_FILE);

			} else {
				appendLine("�ɹ����� Hibernate �����ļ���Ϣ:"
						+ ConfigParams.WEB_INF_PATH_CLASSES + "/"
						+ ConfigParams.HIBERNATE_TEMPLATE_XML);
				setProblem(null);
			}

		} catch (Exception e) {
			setProblem("�޷���ȡ������ Hibernate �����ļ�ģ�� "
					+ ConfigParams.HIBERNATE_TEMPLATE_FILE);
		}

		setBusy(false);
	}

}