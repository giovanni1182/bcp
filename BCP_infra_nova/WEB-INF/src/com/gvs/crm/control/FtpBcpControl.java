package com.gvs.crm.control;

import com.gvs.crm.model.CRMModelManager;
import com.gvs.crm.model.Usuario;
import com.gvs.crm.model.UsuarioHome;
import com.gvs.crm.model.impl.FtpBcpImpl;
import com.gvs.crm.view.PaginaInicialView;

import infra.control.Action;
import infra.control.Control;

public class FtpBcpControl extends Control
{
	public void downloadFtp(Action action) throws Exception
	{
		CRMModelManager mm = new CRMModelManager(this.getUser());
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		FtpBcpImpl ftpBcp = (FtpBcpImpl) mm.getHome("FtpBcpImpl");
		Usuario usuarioAtual = (Usuario) usuarioHome.obterUsuarioPorUser(this.getUser());
		mm.beginTransaction();
		try
		{
			//ftpBcp.conectarSFTP();
			ftpBcp.conectarFTP();
			
			mm.commitTransaction();
			
			this.setAlert("Download FTP terminado com sucesso");
			
			this.setResponseView(new PaginaInicialView(usuarioAtual, usuarioAtual));
		}
		catch (Exception exception)
		{
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new PaginaInicialView(usuarioAtual, usuarioAtual));
			mm.rollbackTransaction();
		}
	}
}
