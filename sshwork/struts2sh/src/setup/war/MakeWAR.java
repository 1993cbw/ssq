package setup.war;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

/**
 * ���ݸ���Ŀ¼����WAR�ļ�������ѡ�񽫴�WAR������Tomcat��.
 * @author BeanSoft
 *
 */
public class MakeWAR extends JFrame
	implements ActionListener
{

	// ����
	private static final String TITLE = "WAR����ͷ������� by BeanSoft";
	/** WAR �ļ����� TODO ���޸Ĵ��ļ��� */
	private static final String WAR_FILE = "struts2sh.war";
	private static ZipOutputStream zipOutputStream;
	private static JProgressBar progressBar;
	private JFileChooser warFileChooser;
	private JTextField warTextField;
	private static byte buf[] = new byte[1024];
	private static int len;
	private static int totalFileCount;
	private static int doneFileCount;

	public static void main(String args[])
	{
		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		new MakeWAR();
	}

	public MakeWAR()
	{
		super(TITLE);
		warTextField = new JTextField(40);
		warFileChooser = new JFileChooser();
		warFileChooser.setFileSelectionMode(1);
		File warDir = new File("WebRoot");
		if (warDir.exists())
		{
			try
			{
				warTextField.setText(warDir.getCanonicalPath());
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			warFileChooser.setCurrentDirectory(warDir.getAbsoluteFile().getParentFile());
		}
		addWindowListener(new WindowAdapter() {

			public void windowClosing(WindowEvent e)
			{
				System.exit(0);
			}

		}
);
		JPanel mainPanel = new JPanel();
		mainPanel.setBorder(new EmptyBorder(3, 3, 3, 3));
		mainPanel.setLayout(new BoxLayout(mainPanel, 1));
		JPanel dirPanel = new JPanel();
		dirPanel.setBorder(BorderFactory.createTitledBorder("ѡ����Ҫ���Ϊ" + WAR_FILE + "��Ŀ¼"));
		dirPanel.add(warTextField);
		JButton button = new JButton("���...");
		button.addActionListener(this);
		dirPanel.add(button);
		JPanel panel3 = new JPanel();
		panel3.setBorder(new EmptyBorder(5, 5, 5, 5));
		panel3.setLayout(new BorderLayout());
		JButton startButton = new JButton("���� WAR");
		startButton.addActionListener(this);
		panel3.add(startButton, "East");
		mainPanel.add(dirPanel);
		mainPanel.add(panel3);
		getContentPane().add(mainPanel);
//		pack();
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		setSize(screen.width / 2 , screen.height / 2);
		
		Dimension size = getSize();
		setLocation((screen.width - size.width) / 2, (screen.height - size.height) / 2);
		setVisible(true);
	}

	public void actionPerformed(ActionEvent e)
	{
		if (e.getActionCommand().equals("���..."))
		{
			warFileChooser.setCurrentDirectory(new File(warTextField.getText()));
			int returnVal = warFileChooser.showDialog(this, "ѡ��Ŀ¼");
			if (returnVal == 0)
				try
				{
					warTextField.setText(warFileChooser.getSelectedFile().getCanonicalFile().getPath());
				}
				catch (Exception ex)
				{
					ex.printStackTrace();
				}
		} else
		if (e.getActionCommand().equals("���� WAR"))
		{
			final File warDir = new File(warTextField.getText());
			if (!(new File(warDir, "WEB-INF")).exists())
			{
				String msg = "��ѡ�е�Ŀ¼������Ч��WebӦ��Ŀ¼.\n��ѡ������Ŀ¼.";
				JOptionPane.showMessageDialog(this, msg, "����!", 0);
			} else
			{
				countFiles(warDir);
				progressBar = new JProgressBar(0, totalFileCount);
				progressBar.setValue(0);
				progressBar.setStringPainted(true);
				setVisible(false);
				getContentPane().removeAll();
				JPanel panel = new JPanel();
				panel.add(progressBar);
				getContentPane().add(panel);
				//pack();
				setVisible(true);
				final JFrame frame = this;
				Thread t = new Thread() {

					public void run()
					{
						try
						{
							File warFile = new File(WAR_FILE);
							MakeWAR.makeZip(warDir, warFile);
							String msg = "�ɹ����ΪWAR�ļ�, ���·��Ϊ: " + warFile.getCanonicalPath();
							JOptionPane.showMessageDialog(frame, msg, "WAR �������", -1);
							
							// ���� WAR ��
							String tomcatPath = JOptionPane.showInputDialog(frame, "������ Tomcat �İ�װĿ¼�Է�������Ŀ,�����ϣ������,��ѡ��ȡ��.");
							
							if(tomcatPath != null) {
								File tomcatDir = new File(tomcatPath);
								if (!(new File(tomcatDir, "webapps")).exists())
								{
									JOptionPane.showMessageDialog(frame, "��ѡ�е�Ŀ¼������Ч��TomcatӦ��Ŀ¼.\n��ѡ������Ŀ¼.", "����!", 0);
								} else {
									util.FileOperate.copyFile(warFile.getCanonicalPath(), new File(tomcatDir, "webapps" + File.separator + WAR_FILE).getCanonicalPath());
									JOptionPane.showMessageDialog(frame, "��Ŀ�ɹ������������µ�TomcatĿ¼��:" + tomcatPath + ", �����ڿ����������������鿴���.", "��Ŀ�������", -1);
								}
							}
						}
						catch (Exception e)
						{
							System.err.println(e);
						}
						System.exit(0);
					}

				}
;
				t.start();
			}
		}
	}

	/**
	 * �ݹ����� JAR �ļ�.
	 * @param directory
	 * @param zipFile
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	public static void makeZip(File directory, File zipFile)
		throws IOException, FileNotFoundException
	{
		// ɾ�����ļ�
		if(zipFile.exists()) {
			zipFile.delete();
		}
		
		zipOutputStream = new ZipOutputStream(new FileOutputStream(zipFile));
		String fileNames[] = directory.list();
		if (fileNames != null)
		{
			for (int i = 0; i < fileNames.length; i++)
				recurseFiles(new File(directory, fileNames[i]), "");

		}
		zipOutputStream.close();
	}

	/**
	 * �����ļ�����.
	 * @param file
	 */
	private static void countFiles(File file)
	{
		if (file.isDirectory())
		{
			String fileNames[] = file.list();
			if (fileNames != null)
			{
				for (int i = 0; i < fileNames.length; i++)
					countFiles(new File(file, fileNames[i]));

			}
		} else
		{
			totalFileCount++;
		}
	}

	/**
	 * �ݹ����ļ�.
	 * @param file
	 * @param pathName
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	private static void recurseFiles(File file, String pathName)
		throws IOException, FileNotFoundException
	{
		if (file.isDirectory())
		{
			pathName = pathName + file.getName() + "/";
			zipOutputStream.putNextEntry(new ZipEntry(pathName));
			String fileNames[] = file.list();
			if (fileNames != null)
			{
				for (int i = 0; i < fileNames.length; i++)
					recurseFiles(new File(file, fileNames[i]), pathName);

			}
		} else
		{
			ZipEntry zipEntry = new ZipEntry(pathName + file.getName());
			System.out.println(pathName + "  " + file.getName());
			FileInputStream fin = new FileInputStream(file);
			BufferedInputStream in = new BufferedInputStream(fin);
			zipOutputStream.putNextEntry(zipEntry);
			while ((len = in.read(buf)) >= 0) 
				zipOutputStream.write(buf, 0, len);
			in.close();
			zipOutputStream.closeEntry();
			doneFileCount++;
			progressBar.setValue(doneFileCount);
		}
	}

}
