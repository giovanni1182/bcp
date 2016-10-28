package com.gvs.crm.control;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import com.gvs.crm.model.CRMModelManager;
import com.gvs.crm.model.Entidade;
import com.gvs.crm.model.EntidadeHome;
import com.gvs.crm.model.Raiz;
import com.gvs.crm.model.Usuario;
import com.gvs.crm.model.UsuarioHome;
import com.gvs.crm.report.EstatisticaUsuarioXLS;
import com.gvs.crm.report.UsuariosPDF;
import com.gvs.crm.security.CRMLogonView;
import com.gvs.crm.view.EntidadeView;
import com.gvs.crm.view.EstatisticaUsuarioView;
import com.gvs.crm.view.PaginaInicialView;
import com.gvs.crm.view.ResponsabilidadesView;
import com.gvs.crm.view.SenhaUsuarioView;
import com.gvs.crm.view.UsuariosView;

import infra.control.Action;
import infra.control.Control;
import infra.control.ControlManager;

public class UsuarioControl extends Control
{
	public void sair(Action action) throws Exception 
	{
		CRMModelManager mm = new CRMModelManager(this.getUser());
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Usuario usuario = usuarioHome.obterUsuarioPorUser(getUser());
		mm.beginTransaction();
		try
		{
			usuario.atualizarLogPendente();
			
			mm.commitTransaction();
			
			if(action.getBoolean("logoff"))
			{
				ControlManager c = new ControlManager();
				Action action2 = new Action("_logoff");
				c.execute(action2);
				
				//this.forward(new Action("_logoff"));
			}
			
			this.setResponseView(new CRMLogonView());
		}
		catch (Exception e)
		{
			System.out.println(e.getMessage());
		}
	}
	
	public void initial(Action action) throws Exception
	{
		if (this.getUser() != null)
			this.forward(new Action("visualizarPaginaInicial"));
		else
			this.setResponseView(new CRMLogonView());
	}

	public void alterarSenhaUsuario(Action action) throws Exception
	{
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		Entidade origemMenu = entidadeHome.obterEntidadePorId(action.getLong("origemMenuId"));
		Usuario usuario = (Usuario) entidadeHome.obterEntidadePorId(action.getLong("id"));
		this.setResponseView(new SenhaUsuarioView(usuario, origemMenu));
	}

	public void atualizarSenhaUsuario(Action action) throws Exception
	{
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		Entidade origemMenu = entidadeHome.obterEntidadePorId(action.getLong("origemMenuId"));
		Usuario usuario = (Usuario) entidadeHome.obterEntidadePorId(action.getLong("id"));
		mm.beginTransaction();
		try
		{
			if(action.getString("novaSenha1").trim().equals("") || action.getString("novaSenha2").trim().equals(""))
				throw new Exception("Nueva Contrasenã en blanco");
				
			usuario.atualizarSenha(action.getString("senhaAtual"), action.getString("novaSenha1"), action.getString("novaSenha2"));
			this.setAlert("Contrasenã combiada");
			this.setResponseView(new PaginaInicialView(usuario, origemMenu));
			mm.commitTransaction();
		} catch (Exception exception) {
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new SenhaUsuarioView(usuario, origemMenu));
			mm.rollbackTransaction();
		}
	}

	public void atualizarUsuario(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		Usuario usuario = (Usuario) entidadeHome.obterEntidadePorId(action.getLong("id"));
		usuario.atribuirApelido(action.getString("apelido"));
		usuario.atribuirNome(action.getString("nome"));
		usuario.atribuirChave(action.getString("chave"));
		mm.beginTransaction();
		try 
		{
			usuario.atualizar();
			usuario.atualizarNivel(action.getString("nivel"));
			for (Iterator i = action.getParameters().keySet().iterator(); i.hasNext();)
			{
				String key = (String) i.next();
				if (key.startsWith("atributo"))
				{
					String nome = key.substring(9, key.length());
					Entidade.Atributo entidadeAtributo = usuario
							.obterAtributo(nome);
					entidadeAtributo.atualizarValor(action.getString(key));
				}
			}
			this.setAlert("Usuário actualizado");
			this.setResponseView(new EntidadeView(usuario));
			mm.commitTransaction();
		} 
		catch (Exception exception)
		{
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new EntidadeView(usuario));
			mm.rollbackTransaction();
		}
	}

	public void converterUsuarioEmPessoa(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");

		Entidade origem = entidadeHome.obterEntidadePorId(action
				.getLong("origemMenuId"));

		Usuario usuario = (Usuario) entidadeHome.obterEntidadePorId(action
				.getLong("id"));
		mm.beginTransaction();
		try {
			usuario.converterEmPessoa();
			this.setAlert("Usuário convertido");
			UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
			Usuario usuarioAtual = (Usuario) usuarioHome
					.obterUsuarioPorUser(this.getUser());
			this.setResponseView(new PaginaInicialView(usuarioAtual, origem));
			mm.commitTransaction();
		} catch (Exception exception) {
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new EntidadeView(usuario));
			mm.rollbackTransaction();
		}
	}

	public void incluirUsuario(Action action) throws Exception
	{
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		Entidade superior = entidadeHome.obterEntidadePorId(action.getLong("superiorId"));
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Usuario responsavel = (Usuario) usuarioHome.obterUsuarioPorUser(this.getUser());
		Usuario usuario = (Usuario) mm.getEntity("Usuario");
		usuario.atribuirApelido(action.getString("apelido"));
		usuario.atribuirNome(action.getString("nome"));
		usuario.atribuirChave(action.getString("chave"));
		usuario.atribuirSuperior(superior);
		usuario.atribuirResponsavel(responsavel);
		mm.beginTransaction();
		try
		{
			usuario.incluir();
			usuario.atualizarNivel(action.getString("nivel"));
			
			for (Iterator i = action.getParameters().keySet().iterator(); i.hasNext();)
			{
				String key = (String) i.next();
				if (key.startsWith("atributo"))
				{
					String nome = key.substring(9, key.length());
					Entidade.Atributo entidadeAtributo = usuario
							.obterAtributo(nome);
					entidadeAtributo.atualizarValor(action.getString(key));
				}
			}
			this.setAlert("Usuário incluido");
			this.setResponseView(new EntidadeView(usuario));
			mm.commitTransaction();
		} catch (Exception exception) {
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new EntidadeView(superior));
			mm.rollbackTransaction();
		}
	}

	public void visualizarPaginaInicial(Action action) throws Exception
	{
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Entidade origemMenu = entidadeHome.obterEntidadePorId(action
				.getLong("origemMenuId"));
		Usuario usuario = usuarioHome.obterUsuarioPorUser(this.getUser());
		if (origemMenu instanceof Raiz)
			origemMenu = usuario;

		entidadeHome.limparSujeiraBDEntidades();
		
		this.setResponseView(new PaginaInicialView(usuario, origemMenu));
	}

	public void visualizarResponsabilidadesUsuario(Action action) throws Exception
	{
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		Entidade origemMenu = entidadeHome.obterEntidadePorId(action
				.getLong("origemMenuId"));
		Usuario usuario = (Usuario) entidadeHome.obterEntidadePorId(action
				.getLong("id"));
		this.setResponseView(new ResponsabilidadesView((Usuario) usuario,
				usuario.obterChave().equals(this.getUser().getName()),
				origemMenu));
	}
	
	public void excluirUsuarioLogicamente(Action action) throws Exception
	{
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		Usuario usuario = (Usuario) entidadeHome.obterEntidadePorId(action.getLong("id"));
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Usuario usuarioAtual = usuarioHome.obterUsuarioPorUser(getUser());
		
		usuario.excluirLogicamente();
		
		this.setAlert("Usuario eliminado");
		
		Entidade superior = usuario.obterSuperior();
		
		if(superior!=null)
			this.setResponseView(new EntidadeView(superior));
		else
			this.setResponseView(new PaginaInicialView(usuarioAtual,usuarioAtual));
	}
	
	public void visualizarUsuariosCadastrados(Action action) throws Exception
	{
		CRMModelManager mm = new CRMModelManager(this.getUser());
		UsuarioHome home = (UsuarioHome) mm.getHome("UsuarioHome");
		
		this.setResponseView(new UsuariosView(home.obterUsuariosCadastrados()));
	}
	
	public void usuariosCadastradosPDF(Action action) throws Exception
	{
		CRMModelManager mm = new CRMModelManager(this.getUser());
		UsuarioHome home = (UsuarioHome) mm.getHome("UsuarioHome");
		Usuario usuarioAtual = home.obterUsuarioPorUser(this.getUser());
		
		String hora = "_" + usuarioAtual.obterNome() + "_" + new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date());
		
		String textoUsuario = "Generado : " + usuarioAtual.obterNome() + " " + new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date());
		
		UsuariosPDF pdf = new UsuariosPDF(home.obterUsuariosCadastrados(), textoUsuario);
		InputStream arquivo = pdf.obterArquivo();
		String nome = "Listado Usuarios"+hora+".pdf";
		String mime = "application/pdf";
		
		this.setResponseInputStream(arquivo);
        this.setResponseFileName(nome);
        this.setResponseContentType(mime);
        this.setResponseContentSize(arquivo.available());
	}
	
	public void estatisticaUsuario(Action action) throws Exception
	{
		CRMModelManager mm = new CRMModelManager(this.getUser());
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Usuario usuarioAtual = usuarioHome.obterUsuarioPorUser(this.getUser());
		Date dataInicio = action.getDate("dataInicio");
		Date dataFim = action.getDate("dataFim");
		try 
		{
			if(action.getBoolean("view"))
				this.setResponseView(new EstatisticaUsuarioView(dataInicio, dataFim, new ArrayList<Usuario>()));
			else
			{
				if(dataInicio == null || dataFim == null)
					throw new Exception("Data en blanco");
				
				String dataInicioStr = new SimpleDateFormat("dd/MM/yyyy").format(dataInicio) + " 00:00:00";
				dataInicio = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(dataInicioStr);
				
				String dataFimStr = new SimpleDateFormat("dd/MM/yyyy").format(dataFim) + " 23:59:59";
				dataFim = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(dataFimStr);
				
				Collection<Usuario> usuarios = usuarioHome.obterUsuariosPorLog(dataInicio, dataFim);
				
				if(action.getBoolean("excel"))
				{
					String textoUsuario = "Generado: " + usuarioAtual.obterNome() + " " + new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date());
					
					EstatisticaUsuarioXLS xls = new EstatisticaUsuarioXLS(dataInicio,dataFim,usuarios,textoUsuario);
					
					this.setResponseInputStream(xls.obterArquivo());
			        this.setResponseFileName("Estadística Utilización del Sistema.xls");
			        this.setResponseContentType("application/vnd.ms-excel");
			        this.setResponseContentSize(xls.obterArquivo().available());
				}
				else
					this.setResponseView(new EstatisticaUsuarioView(dataInicio, dataFim, usuarios));
			}
		}
		catch (Exception exception) 
		{
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new EstatisticaUsuarioView(dataInicio, dataFim, new ArrayList<Usuario>()));
		}
	}
}