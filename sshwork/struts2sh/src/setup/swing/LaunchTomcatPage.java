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

import setup.tomcat.TomcatLinuxLauncher;
import setup.tomcat.TomcatWindowsLauncher;
import util.OS;

/**
 * 9. ���� Tomcat ��ҳ
 */
public class LaunchTomcatPage extends TextAreaButtonWizardPage {
	public static String getDescription() {
		return "���� Tomcat";
	}
	

	public LaunchTomcatPage() {
		setButtonText("���� Tomcat");
		enableSkip();
	}
	
	protected void renderingPage() {
		Map settings = getWizardDataMap();

		setText("������Ŀ�Ѿ��ɹ�����, ������������ Tomcat :" + getWizardData("tomcat.home") + 
				", ������Ŀǰֻ֧�� Windows �� Linux, ���� Tomcat������������ȷ.");
	}

	@Override
	public void buttonClick() {
		
		setBusy(true);

		// ������ݿ�����
		appendLine("����׼������ Tomcat ������...");

		String tomcatPath = (String) getWizardData("tomcat.home");
		if(tomcatPath != null) {
			TomcatWindowsLauncher launcher = new TomcatWindowsLauncher();
			
			if(OS.isLinux()) {
				launcher = new TomcatLinuxLauncher();
			}
			
				try {
					launcher.setAppServerHome(tomcatPath);
					launcher.startServer();
					
					appendLine("Tomcat�����������ɹ�:" + tomcatPath );
					setProblem(null);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					appendLine("Tomcat ����ʧ��, ��������Ҫ�ֹ�������Tomcat.");
					setProblem("Tomcat ����ʧ��, ��������Ҫ�ֹ�������Tomcat.");
				}
				
		}
		setBusy(false);
	}

}