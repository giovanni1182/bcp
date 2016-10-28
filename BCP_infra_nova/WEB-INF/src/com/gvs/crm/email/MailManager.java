// FrontEnd Plus GUI for JAD
// DeCompiled : MailManager.class

package com.gvs.crm.email;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

// Referenced classes of package com.gio.crm.model:
//            MailServer, Usuario, MailMessage

public class MailManager
{
    class SimpleAuth extends Authenticator
    {

        public String username;
        public String password;

        protected PasswordAuthentication getPasswordAuthentication()
        {
            return new PasswordAuthentication(username, password);
        }

        public SimpleAuth(String user, String pwd)
        {
            username = null;
            password = null;
            username = user;
            password = pwd;
        }
    }


    private String userName, pass, from, to, smtp;
    private int port;
    
    public String getUserName()
    {
		return userName;
	}

	public void setUserName(String userName)
	{
		this.userName = userName;
	}

	public String getPass()
	{
		return pass;
	}

	public void setPass(String pass)
	{
		this.pass = pass;
	}

	public String getFrom()
	{
		return from;
	}

	public void setFrom(String from)
	{
		this.from = from;
	}

	public String getTo()
	{
		return to;
	}

	public void setTo(String to)
	{
		this.to = to;
	}

	public String getSmtp()
	{
		return smtp;
	}

	public void setSmtp(String smtp)
	{
		this.smtp = smtp;
	}

	public int getPort()
	{
		return port;
	}

	public void setPort(int port)
	{
		this.port = port;
	}

    public MailManager()
    {
    }

    public void send(MailServer server, MailMessage msg) throws Exception
    {
        try
        {
            Properties mailProps = new Properties();
            Session mailSession = null;
            
            if(this.userName!=null && this.pass!=null)
            {
            	server.setNeedsAuth(true);
                SimpleAuth auth = null;
                auth = new SimpleAuth(this.userName, this.pass);
                mailProps.put("mail.user", auth.username);
                mailProps.put("mail.smtp.auth", "true");
                mailSession = Session.getInstance(mailProps, auth);
            }
            else
            	mailSession = Session.getInstance(mailProps);
            
            if(this.smtp!=null)
            	mailProps.put("mail.host", this.smtp);
            if(this.port!=0)
            	mailProps.put("mail.smtp.port", this.port);
            
            mailProps.put("mail.from", msg.getFrom());
            mailProps.put("mail.to", msg.getTo());
            
            mailSession.setDebug(false);
            
            Message email = new MimeMessage(mailSession);
            
            email.setRecipients(javax.mail.Message.RecipientType.TO, InternetAddress.parse(msg.getTo()));
            if(!msg.getCc().trim().equals(""))
                email.setRecipients(javax.mail.Message.RecipientType.CC, InternetAddress.parse(msg.getCc()));
            if(!msg.getBcc().trim().equals(""))
                email.setRecipients(javax.mail.Message.RecipientType.BCC, InternetAddress.parse(msg.getBcc()));
            email.setFrom(new InternetAddress(msg.getFrom()));
            email.setSubject(msg.getSubject());
            email.setContent(msg.getText(), "text/plain");
            Transport.send(email);
        }
        catch(Exception e)
        {
            System.out.println("ERRO: " + e);
            e.printStackTrace(System.out);
            throw e;
        }
        System.out.println("Email enviado com sucesso");
    }

    public void send(MailServer server, String to, String cc, String bcc, String from, String subject, String text)
        throws Exception
    {
        MailMessage msg = new MailMessage(to, cc, bcc, from, subject, text);
        send(server, msg);
    }

    public static void main(String args[])
    {
        MailServer server = new MailServer("mail.localhost.com.br", "pburglin", "c3po", true);
        MailManager m = new MailManager();
        try
        {
            m.send(server, "giovanni@horst.com.br", "", "", "giovanni@horst.com.br", "Esse e um email de teste", "Corpo do email");
        }
        catch(Exception e)
        {
            e.printStackTrace(System.out);
        }
    }
}
