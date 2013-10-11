package util.mail;

/*
 * @(#)MailSender.java 1.00 2004-8-3
 *
 * Copyright 2004 BeanSoft Studio. All rights reserved.
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
import java.util.*;
import java.io.*;
import javax.mail.*;
import javax.mail.internet.*;

/**
 * MailSender, ֧�ֻ��� SMTP ��֤���ı���ʽ�ʼ�����.
 * 
 * @author BeanSoft
 * @version 1.1 2006-8-1
 */
public class MailSender {
	
    /** ������ */
    private String from;
    /** ������ */
    private String to;
    /** ���� */
    private String subject;
    /** ���� */
    private String body;
    
    private boolean htmlFormat;

    private static Properties props = new Properties();
    static {
        try {
            InputStream in = MailSender.class
                    .getResourceAsStream("MailSender.ini");
            props.load(in);
            in.close();
        } catch (Exception ex) {
            System.err.println("�޷����������ļ� MailSender.ini:" + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public MailSender() {
    }
    
    /** 
     * �����ʼ�.
     * @return boolean - ���ͽ�� 
     */
    public boolean sendMail() {
        if (getBody() == null || getTo() == null || getFrom() == null
                || getSubject() == null) { return false; }
        //--[ Obtain a session
        try {
            //--[ Set up the default parameters
            //        Properties props = new Properties();
            //        props.put("mail.transport.protocol", "smtp" );
            //        props.put("mail.smtp.host", smtpServer );
            //        props.put("mail.smtp.port", "25" );

            //--[ Create the session and create a new mail message
    		java.util.Properties propsSmtp = new java.util.Properties();
    		propsSmtp.put("mail.smtp.auth", "true");
    		propsSmtp.put("mail.smtp.host", props.get("mail.smtp.host"));
//    		propsSmtp.put("mail.debug", "true");
    		
            Session mailSession = Session.getDefaultInstance(propsSmtp);
            Message msg = new MimeMessage(mailSession);

            //--[ Set the FROM, TO, DATE and SUBJECT fields
            msg.setFrom(new InternetAddress(getFrom()));// ���÷�������Ϣ
            msg.addRecipients(Message.RecipientType.TO, InternetAddress
                    .parse(getTo()));// �����ռ�����Ϣ, ���Զ������ռ���
            msg.setSentDate(new Date());// ���÷�������
            msg.setSubject(getSubject());// ��������

            //--[ Create the body of the mail
            if(isHtmlFormat()) {
            	msg.setContent(getBody(),
        		"text/html;charset=GBK");// HTML �ʼ�
            } else  {
            	msg.setText(getBody());// �����ʼ�������
            }
           

            Transport transport = mailSession.getTransport("smtp");// �������Ӻͷ��ʼ�����
            transport.connect( propsSmtp.getProperty("mail.smtp.host"), props.getProperty("username"), 
            		props.getProperty("password"));// connect����������, SMTP, �������ʺ�, ����

            transport.sendMessage(msg, msg.getAllRecipients());// send ���ʼ�
            
            transport.close();// �ر�����
        } catch (Exception e) {
            System.out.println(e);
            //            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * @return Returns the body.
     */
    public String getBody() {
        return body;
    }

    /**
     * @param body
     *            The body to set.
     */
    public void setBody(String body) {
        this.body = body;
    }

    /**
     * @return Returns the from.
     */
    public String getFrom() {
        return from;
    }

    /**
     * @param from
     *            The from to set.
     */
    public void setFrom(String from) {
        this.from = from;
    }

    /**
     * @return Returns the subject.
     */
    public String getSubject() {
        return subject;
    }

    /**
     * @param subject
     *            The subject to set.
     */
    public void setSubject(String subject) {
        this.subject = subject;
    }

    /**
     * @return Returns the to.
     */
    public String getTo() {
        return to;
    }

    /**
     * @param to
     *            The to to set.
     */
    public void setTo(String to) {
        this.to = to;
    }

    public static void main(String[] args) {
        MailSender sender = new MailSender();

        sender.setFrom("\"Admin\" <admin@earth.org>");
        sender.setTo("beansoft@earth.org");
        sender.setSubject("ȡ������");
        sender.setBody("������������123456!");

        System.out.println(sender.sendMail());
    }

	public boolean isHtmlFormat() {
		return htmlFormat;
	}

	public void setHtmlFormat(boolean htmlFormat) {
		this.htmlFormat = htmlFormat;
	}
}
