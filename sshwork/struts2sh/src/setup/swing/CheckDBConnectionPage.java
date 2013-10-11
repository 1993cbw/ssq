package setup.swing;

import java.sql.Connection;
import java.util.Map;

import setup.db.DatabaseManager;
import setup.db.DatabaseManagerImplMysql;

/**
 * 4. �������ݿ�������ҳ
 * 
 */
public class CheckDBConnectionPage extends TextAreaButtonWizardPage {
	public static String getDescription() {
		return "�������ݿ�����";
	}

	public CheckDBConnectionPage() {
		setButtonText("��������");
		enableSkip();
	}
	
	protected void renderingPage() {
		Map settings = getWizardDataMap();

		setText("��ҳ�����MySQL ���ݿ������Ƿ����");
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
		setText("�����������ݿ�...");

		String url = "jdbc:mysql://" + host + ":" + port + "/";
		DatabaseManager dbman = new DatabaseManagerImplMysql();
		try {
			Connection conn = dbman.checkConnection(ConfigParams.JDBC_DRIVER,
					url, username, pwd);
			conn.close();
			appendLine("���ݿ����ӳɹ�!");

			setProblem(null);
		} catch (Exception e) {
			// e.printStackTrace();
			appendLine("���ݿ�����ʧ��!");
			appendLine(e.getMessage());

			setProblem("���ݿ�����ʧ��, �������!");
		}

		setBusy(false);

	}

}
