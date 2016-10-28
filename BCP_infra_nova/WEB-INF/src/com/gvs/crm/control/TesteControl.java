package com.gvs.crm.control;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.SimpleEmail;

import com.gvs.crm.model.CRMModelManager;
import com.gvs.crm.model.EntidadeHome;
import com.gvs.crm.model.EventoHome;
import com.gvs.crm.model.UsuarioHome;
import com.gvs.crm.view.TesteEmailView;
import com.gvs.crm.view.TesteView;

import infra.control.Action;
import infra.control.Control;

public class TesteControl extends Control
{
	public void testeAction(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		EventoHome eventoHome = (EventoHome) mm.getHome("EventoHome");
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");

		int pagina = action.getInt("_pagina");

		if (pagina <= 0)
			pagina = 1;

		//System.out.println("Pagina: " + pagina);

		this.setResponseView(new TesteView(eventoHome.obterTeste(pagina),pagina));
	}
	
	public void enviarEmail(Action action) throws Exception
	{
		try
		{
			String msg = "";
			
			if(!action.getBoolean("view"))
			{
				if(action.getBoolean("gmail"))
				{
					Email email = new SimpleEmail();
					email.setHostName("smtp.googlemail.com");
					email.setSmtpPort(465);
					email.setAuthenticator(new DefaultAuthenticator("cavaquiolo1105@gmail.com", "gandalf714"));
					email.setSSLOnConnect(true);
					email.setFrom("cavaquiolo1105@gmail.com");
					email.setSubject("Teste email");
					email.setMsg("Email automático enviado com sucesso pelo Gmail");
					email.addTo("giovanni@gdsd.com.br");
					email.addCc("gbrawer@bcp.gov.py");
					email.send();
					
					msg = "Email enviado com sucesso pelo Gmail";
				}
				else
				{
					//Funcionando o envio, somente para emails internos do bcp
					Email email = new SimpleEmail();
					email.setHostName("mail.bcp.gov.py");
					email.setSmtpPort(25);
					email.setFrom("sisvalidacion@bcp.gov.py");
					email.setSubject("Teste email");
					email.setMsg("Email automático enviado com sucesso pelo BCP");
					email.addTo("gbrawer@bcp.gov.py");
					email.addCc("davidg@regionalseguros.com.py");
					email.addCc("giovanni@gdsd.com.br");
					email.send();
					
					//email.setAuthenticator(new DefaultAuthenticator("sisvalidacion@bcp.gov.py", "Validacion2015"));
					
					msg = "Email enviado com sucesso pelo BCP";
				}
			}
			
			this.setResponseView(new TesteEmailView(msg));
			
		}
		catch (Exception e)
		{
			this.setResponseView(new TesteEmailView(e.getMessage()));
		}
	}
}