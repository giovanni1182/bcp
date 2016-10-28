package com.gvs.crm.email;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class EnviarEmail 
{
	
	/*EnviarEmail email = new EnviarEmail();
	
	String arq1 = "D:/Projetos Novos/7Grill/images/Etiquetas28.pdf";
	String arq2 = "D:/Projetos Novos/7Grill/images/Etiquetas28sem_seta.pdf";
	
	FileDataSource file1 = new FileDataSource(arq1);
	FileDataSource file2 = new FileDataSource(arq2);
	
	email.setFrom("giovanni.1105@terra.com.br");
	email.setTo("giovanni.1105@yahoo.com.br");
	email.setUser("giovanni.1105");
	email.setPassword("gandalf");
	email.setSubject("Teste do Titulo");
	email.setText("Corpo do texto");
	email.setHost("smtp.cwb.terra.com.br");
	email.addFile(file1);
	email.addFile(file2);
	email.enviar();*/
	
	private String to,from,host,subject,text,user,pass;
	private Collection files = new ArrayList();
	private Collection mbp2 = new ArrayList();
	private FileWriter file;
	
	class SimpleAuth extends Authenticator
	{
        public String username = null;
        public String password = null;
        public SimpleAuth(String user, String pwd)
        {
            username = user;
            password = pwd;
        }
        protected PasswordAuthentication getPasswordAuthentication()
        {
            return new PasswordAuthentication(username,password);
        }
    }    
	
	public EnviarEmail() throws Exception
	{
		file = new FileWriter("C:/tmp/LogEmail.txt");
	}
	
	public void setUser(String user)
	{
		this.user = user;
	}
	
	public void setPassword(String pass)
	{
		this.pass = pass;
	}
	
	
	public void setTo(String to)
	{
		this.to = to;
	}
	
	public void setFrom(String from)
	{
		this.from = from;
	}
	
	public void setHost(String host)
	{
		this.host = host;
	}
	
	public void setSubject(String subject)
	{
		this.subject = subject;
	}
	
	public void setText(String text)
	{
		this.text = text;
	}
	
	public void addFile(FileDataSource file)
	{
		this.files.add(file);
	}
	
	  public void enviar() throws Exception
	  {
		  try 
		  {
			  SimpleAuth auth = new SimpleAuth(this.user, this.pass);
			  
			  Properties props = new Properties();
			  props.put("mail.smtp.auth", "true");
			  props.put("mail.user", auth.username);
			  props.put("mail.host", host);
			  
			  Session session = Session.getInstance(props, auth);
			  session.setDebug(false);
		      // cria a mensagem
		      MimeMessage msg = new MimeMessage(session);
		      msg.setFrom(new InternetAddress(from));
		      InternetAddress[] address = {new InternetAddress(to)};
		      msg.setRecipients(Message.RecipientType.TO, address);
		      msg.setSubject(subject);

		      // cria a primeira parte da mensagem
		      MimeBodyPart mbp1 = new MimeBodyPart();
		      mbp1.setText(this.text);

		      // cria a segunda parte da mensage
		      //MimeBodyPart mbp2 = new MimeBodyPart();

		     // anexa o arquivo na mensagem
		      for(Iterator i = this.files.iterator() ; i.hasNext() ; )
		      {
		    	  FileDataSource file = (FileDataSource) i.next();
		    	  
		    	  MimeBodyPart novoMpb = new MimeBodyPart();
		    	  novoMpb.setDataHandler(new DataHandler(file));
		    	  novoMpb.setFileName(file.getName());
		    	  
		    	  this.mbp2.add(novoMpb);
		      }
		      
		      
		      // cria a Multipart
		      Multipart mp = new MimeMultipart();
		      mp.addBodyPart(mbp1);
		      
		      for(Iterator i = this.mbp2.iterator(); i.hasNext() ; )
		      {
		    	  MimeBodyPart mime = (MimeBodyPart) i.next();
		    	  mp.addBodyPart(mime);
		      }

		      // adiciona a Multipart na mensagem
		      msg.setContent(mp);

		      // configura a data: cabecalho
		      msg.setSentDate(new Date());
		      
		      // envia a mensagem
		      Transport.send(msg);
		      
		      file.write("Email enviado com sucesso para giovanni.1105@terra.com.br e gbrawerman@hotmail.com" + "\r\n");
		      file.close();
		 } 
		  catch (MessagingException mex) 
		  {
			  file.write(mex.toString() + "\r\n");
			  file.close();
				
			  mex.printStackTrace();
		      Exception ex = null;
		      if ((ex = mex.getNextException()) != null)
		    	  ex.printStackTrace();
		  }
	  }
}
