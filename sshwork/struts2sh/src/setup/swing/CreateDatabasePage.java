/**
 * 
 */
package setup.swing;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.SwingUtilities;

import org.netbeans.spi.wizard.Wizard;
import org.netbeans.spi.wizard.WizardPanelNavResult;

import setup.db.DatabaseManager;
import setup.db.DatabaseManagerImplMysql;
import util.FileUtil;
import util.StringUtil;

/**
 * 5. �������ݿ� ��ҳ
 * 
 */
public class CreateDatabasePage extends TextAreaButtonWizardPage {
	public static String getDescription() {
		return "�������ݿ�";
	}
	
	public CreateDatabasePage() {
		setButtonText("��ʼ����");
		enableSkip();
	}
	
	protected void renderingPage() {
		Map settings = getWizardDataMap();

		setText("��ҳ����鲢�������ݿ�" + settings.get("jdbc.dbname"));
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
		setText("");
		appendLine("���ڼ�����ݿ� " + dbName + "�Ƿ����...");

		String url = "jdbc:mysql://" + host + ":" + port + "/";
		
		DatabaseManager dbman = new DatabaseManagerImplMysql();
		// �������ݿ�
		try {
			Connection conn = dbman.checkConnection(ConfigParams.JDBC_DRIVER, url, username,
					pwd);
			dbman.setConnectioin(conn);

			if (dbman.checkDatabaseExist(dbName)) {
				appendLine("���ݿ�" + dbName + "�Ѵ���,�����ظ�����!");
				setProblem(null);
			} else {
				appendLine("���ݿ�" + dbName + "������,���ڴ���...");
				
				if (dbman.createDatabase(dbName)) {
					appendLine("���ݿ�"
									+ dbName + "�����ɹ�!");
					setProblem(null);
				} else {
					appendLine("�޷��������ݿ�"
									+ dbName + "! ������������ݿ����������.");
					setProblem("�޷��������ݿ�"
							+ dbName + "! ������������ݿ����������.");
				}
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
