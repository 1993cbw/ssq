/**
 * 
 */
package setup.swing;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.netbeans.spi.wizard.WizardController;
import org.netbeans.spi.wizard.WizardPage;

/**
 * �������ʾ�����ı�(�Զ�����)���ṩһ�����뱻����İ�ť����ҳ��, ����������ִ��һЩ�������ִ����־.
 * 
 * 2008-08-03
 * 
 * @author BeanSoft
 * 
 */
public abstract class TextAreaButtonWizardPage extends WizardPage {
	JTextArea textArea = new JTextArea();
	JButton button = new JButton();
	final JCheckBox skipButton = new JCheckBox("�����˲���");
	private boolean allowSkip = false;

	public TextAreaButtonWizardPage() {
		// textArea.setFocusable(false);// �����ϣ����ʾ�����ֱ�ѡ���ƣ�������Ϊ���ɻ�ȡ����
		textArea.setEditable(false);
		textArea.setBackground(getBackground());
		textArea.setLineWrap(true);

		setLayout(new BorderLayout());
		add(new JScrollPane(textArea));

		// ���������水ť����¼�
		button.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				buttonClick();
			}

		});
		add(button, BorderLayout.EAST);
		
		
		// ����������ť����¼�
		skipButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				setAllowSkip(skipButton.isSelected());
				
				setProblem( validateContents(null, null) );
			}

		});
		
		
		skipButton.setEnabled(false);
		add(skipButton, BorderLayout.SOUTH);
		
	}
	
	/**
	 * �����������谴ť.
	 */
	public void enableSkip() {
		skipButton.setEnabled(true);
	}
	
	/**
	 * ���Ǵ˷�������ʾ��ʾ��Ϣ, ������ "��ť" �ſɼ������½���.
	 */
	protected String validateContents(Component component, Object event) {
		if(isAllowSkip()) {
			return null;
		}
		
		return "���� " + button.getText() + " ��ť�Լ���";
	}

	/**
	 * ������ʾ����.
	 * @param text
	 */
	public void setText(String text) {
		textArea.setText(text);
	}

	/**
	 * ���ð�ť����.
	 * @param text
	 */
	public void setButtonText(String text) {
		button.setText(text);
	}

	/**
	 * ���һ������
	 * @param text
	 */
	public void appendLine(String text) {
		textArea.append(text + "\n");
	}

	/**
	 * �ṩ��ť���ʱ�����õĻص�����.
	 */
	public abstract void buttonClick();

	/**
	 * ��ð�ť.
	 * @return
	 */
	public JButton getJButton() {
		return button;
	}

	/**
	 * @return the allowSkip
	 */
	public boolean isAllowSkip() {
		return allowSkip;
	}

	/**
	 * @param allowSkip the allowSkip to set
	 */
	public void setAllowSkip(boolean allowSkip) {
		this.allowSkip = allowSkip;
	}

}
