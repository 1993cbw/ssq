package setup.swing;

import java.sql.Connection;
import java.util.Map;
import java.util.StringTokenizer;
import java.io.*;

import setup.db.DatabaseManager;
import setup.db.DatabaseManagerImplMysql;

/**
 * 6. ��ʼ�����ݿ�� ��ҳ
 * 
 */
public class CreateDBTablePage extends TextAreaButtonWizardPage {
	public static String getDescription() {
		return "��ʼ�����ݿ��";
	}

	public CreateDBTablePage() {
		setButtonText("������");
		enableSkip();
	}

	protected void renderingPage() {
		Map settings = getWizardDataMap();

		setText("��ҳ��������������ݿ��, ���ص������ļ��� setup/mysql.sql\n");
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
		DatabaseManager dbman = new DatabaseManagerImplMysql();
		// �������ݿ�
		try {
			Connection conn = dbman.checkConnection(ConfigParams.JDBC_DRIVER,
					url + ConfigParams.MYSQL_ENCODING, username, pwd);
			dbman.setConnectioin(conn);

			dbman.changeDatabase(dbName);

			appendLine("���ڴ������...");
			// Every sql statement is sperated by a ;
			StringTokenizer token = new StringTokenizer(util.FileUtil
					.readFileAsString("setup/mysql.sql", "UTF-8"), ";");
			boolean tableSuccess = true;// Whether all table has been created

			while (token.hasMoreElements()) {
				String value = token.nextElement().toString();
				System.out.println(value);
				tableSuccess &= dbman.executeUpdate(value);
			}

			if (tableSuccess) {
				appendLine("��񴴽����.");
				setProblem(null);
			} else {
				appendLine("�޷��������! ������������ݿ����������.");
				setProblem("�޷��������! ������������ݿ����������.");
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			dbman.closeConnection();
		}

		setBusy(false);

	}

}
