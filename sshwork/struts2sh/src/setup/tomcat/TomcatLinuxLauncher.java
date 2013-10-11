/*
 * @(#)TomcatLinuxLauncher.java 1.00 2006-12-17
 *
 * Copyright 2006 BeanSoft Studio. All rights reserved.
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package setup.tomcat;


/**
 * TomcatLinuxLauncher
 * 
 * Chinese documents:
 * Linux �汾����������.
 * 
 * @author BeanSoft
 * @version 1.00 2006-12-17
 */
public class TomcatLinuxLauncher extends TomcatWindowsLauncher {
	
	/* (non-Javadoc)
	 * @see setup.tomcat.TomcatWindowsLauncher#getStartupScript()
	 */
	@Override
	protected String getStartupScript() {
		// TODO Auto-generated method stub
		return "startup.sh";
	}

	/* (non-Javadoc)
	 * @see setup.tomcat.TomcatWindowsLauncher#getStopScript()
	 */
	@Override
	protected String getStopScript() {
		// TODO Auto-generated method stub
		return "shutdown.sh";
	}

	/* (non-Javadoc)
	 * @see servermon.launcher.IAppServerLauncher#startServer()
	 */
	public void startServer() throws Exception {
		// ���� Tomcat ������
		try {
			// "{Tomcat_Home}/bin"
			String msg = "�������� " + getAppServerName() + " ������...\n";
			System.out.println(msg);

			Runtime.getRuntime().exec(getAppServerHome() + "/bin/" + getStartupScript());
					
			msg = getAppServerName() + " �����������ű���ִ��...\n";
			log(msg);
		} catch (Exception e) {
			String msg = "�޷����� " + getAppServerName() + " ������:" + e + "\n";
			log(msg);
			throw e;
		}
	}

	/* (non-Javadoc)
	 * @see servermon.launcher.IAppServerLauncher#stopServer()
	 */
	public void stopServer() throws Exception {
		// "{Tomcat_Home}/bin"
				
		// ֹͣ Tomcat ���ȴ� 30 �����ڴ�ִ�н���
		try {
			Runtime.getRuntime().exec(getAppServerHome() + "/bin/" + getStopScript());
					
			String msg = "�ȴ� " + getAppServerName() + " �������ر�, " + getShutDownWaitTime() +" ���Ӻ����� Tomcat ������.\n";
					
			log(msg);
					
		} catch (Exception e) {
			String msg = "�޷��ر� " + getAppServerName() + " ������:" + e;
			log(msg + "\n");
			throw e;
		}
	}
	
	
}
