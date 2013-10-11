/*
 * DeployPage.java
 *
 * Created on __DATE__, __TIME__
 */

package setup.swing;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Map;

import javax.swing.*;

/**
 * 8. ������Ŀ ��ҳ
 */
public class DeployPage extends TextAreaButtonWizardPage {
	public static String getDescription() {
		return "������Ŀ";
	}
	

	public DeployPage() {
		setButtonText("������Ŀ");
		
		JPanel p = new JPanel();
		p.add(new JLabel("������ Tomcat �İ�װ·��:"));
		final JTextField tf = new JTextField();
		tf.setColumns(20);
		tf.setName("tomcat.home");
		p.add(tf);
		
		JButton browseButton = new JButton("���...");
		browseButton.addActionListener(new ActionListener() {
			JFileChooser chooser = new JFileChooser();
			public void actionPerformed(ActionEvent e) {
				chooser.setMultiSelectionEnabled(false);
				chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				chooser.setDialogTitle("��ѡ�� Tomcat �İ�װĿ¼");
				if(chooser.showOpenDialog(DeployPage.this) == JFileChooser.APPROVE_OPTION) {
					try {
						tf.setText(chooser.getSelectedFile().getCanonicalPath());
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						tf.setText(chooser.getSelectedFile().getPath());
					}
				}
			}
			
		});
		p.add(browseButton);
		
		add(p, BorderLayout.NORTH);
	}
	
	

	protected String validateContents(Component component, Object event) {
		String tomcatPath = (String) getWizardData("tomcat.home");
		
		if(util.StringUtil.isEmpty(tomcatPath)) {
			return ("Tomcat ��װĿ¼����Ϊ��");

		} else {
			File tomcatDir = new File(tomcatPath);
			if (!(new File(tomcatDir, "webapps")).exists())
			{
				return ("�������Ŀ¼������Ч��Tomcat ��װĿ¼");
			}
		}
		
		return null;
	}


	protected void renderingPage() {
		Map settings = getWizardDataMap();

		setText("����׼�������Ѿ����, ���Ե�� \"������Ŀ\"��ť�� WebRoot Ŀ¼�µ�������Ϊ struts2sh��Ŀ������ Tomcat ��");
	}

	@Override
	public void buttonClick() {
		if(validateContents(null, null) != null) {
			return;
		}
		
		setBusy(true);

		// ������ݿ�����
		appendLine("����׼������ WebӦ��...");

		String tomcatPath = (String) getWizardData("tomcat.home");
		if(tomcatPath != null) {
				try {
					util.FileOperate.copyFolder(new File(ConfigParams.WEBROOT).getCanonicalPath(),
							new File(tomcatPath, 
									"webapps/" + ConfigParams.WAR_NAME).getCanonicalPath());
					appendLine("��Ŀ" + ConfigParams.WAR_NAME + "�ɹ������������µ�TomcatĿ¼��:" + tomcatPath + ", �����ڿ����������������鿴���.");
					setProblem(null);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					setProblem("��Ŀ����ʧ��");
				}
				
		}
		setBusy(false);
	}

}