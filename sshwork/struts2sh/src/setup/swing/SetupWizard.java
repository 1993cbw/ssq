/**
 * 
 */
package setup.swing;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;

import org.netbeans.api.wizard.WizardDisplayer;
import org.netbeans.spi.wizard.Wizard;
import org.netbeans.spi.wizard.WizardPage;

/**
 * ��������������������.
 * 
 * @author BeanSoft
 * 
 */
public class SetupWizard {

	/**
	 * ����ֻ��ʾ�ı�����ҳ��.
	 * @param stepName - ������
	 * @param text - ��ʾ�Ķ����ı�
	 * @return WizardPage ����
	 */
	static WizardPage createTextPage(String stepName, String text) {
		WizardPage page = new WizardPage(stepName);
		JTextArea textArea = new JTextArea(text);
		// textArea.setFocusable(false);
		textArea.setEditable(false);
		textArea.setBackground(page.getBackground());

		page.setLayout(new BorderLayout());
		page.add(new JScrollPane(textArea));

		return page;
	}

	/**
	 * ����Դ�ļ�����ֻ��ʾ�ı�����ҳ��.
	 * @param stepName - ������
	 * @param resFile - ��ʾ�Ķ����ı���Դ�ļ�
	 * @return WizardPage ����
	 */
	static WizardPage createTextPageFromResFile(String stepName, String resFile) {
		try {
			return createTextPage(stepName, util.FileUtil
					.readFileAsString(SetupWizard.class
							.getResourceAsStream(resFile)));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			return createTextPage(stepName, "����: �޷�������Դ�ļ� " + resFile);
		}
	}

	public static void main(String[] args) {
		System.setProperty("wizard.sidebar.image",
				"org/netbeans/modules/wizard/bg_small.png");

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}

		// ��ʾ���������ú�ͼ��
		final Frame f = new Frame("struts2sh ��Ŀ������");
		f.setIconImage(java.awt.Toolkit.getDefaultToolkit().getImage(
				SetupWizard.class.getResource("network.png")));

		f.pack();
		f.setLocation(100, 100);
		f.setVisible(true);

		Wizard wiz = WizardPage.createWizard("struts2sh ��Ŀ������", new WizardPage[] {
				// Both of these classes are subclasses of
				// WizardPage. We could also pass instances
				// of WizardPage.
				createTextPageFromResFile("����˵��", "summary.txt"),
				createTextPageFromResFile("����Ⱦ�����", "req.txt"),
				new ConfigDBPage(), 
				new CheckDBConnectionPage(),
				new CreateDatabasePage(),
				new CreateDBTablePage(),
				new SaveHibernateCfgPage(),
				new DeployPage(),
				new LaunchTomcatPage(),
				createTextPageFromResFile("���", "finish.txt") });


		Map gatheredSettings = (Map) WizardDisplayer.showWizard(wiz,
				new java.awt.Rectangle(100, 100, 700, 500), new AbstractAction() {
					public void actionPerformed(ActionEvent e) {
						try {
							JOptionPane
									.showMessageDialog(
											f,
											util.FileUtil
											.readFileAsString(SetupWizard.class
													.getResourceAsStream("help.txt"))
									);
						} catch (HeadlessException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} catch (Exception e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}

					}

				}, null);
		System.out.println(gatheredSettings);

		System.exit(0);
	}

}
