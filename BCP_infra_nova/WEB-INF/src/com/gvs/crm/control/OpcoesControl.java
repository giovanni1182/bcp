package com.gvs.crm.control;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

import com.gvs.crm.model.Aseguradora;
import com.gvs.crm.model.AseguradoraHome;
import com.gvs.crm.model.CRMModelManager;
import com.gvs.crm.model.EntidadeHome;
import com.gvs.crm.model.OpcaoHome;
import com.gvs.crm.model.Usuario;
import com.gvs.crm.model.UsuarioHome;
import com.gvs.crm.report.RelOpcoes2XLS;
import com.gvs.crm.report.RelOpcoesXLS;
import com.gvs.crm.view.LiberarOpcoesView;
import com.gvs.crm.view.RelOpcaoResEscaneadaView;
import com.gvs.crm.view.RelOpcoesView;

import infra.control.Action;
import infra.control.Control;

public class OpcoesControl extends Control
{
	public void liberarOpcoes(Action action) throws Exception 
	{
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		OpcaoHome opcaoHome = (OpcaoHome) mm.getHome("OpcaoHome");
		int opcao = action.getInt("liberarOpcao");

		mm.beginTransaction();
		try
		{
			if(action.getBoolean("view"))
				this.setResponseView(new LiberarOpcoesView(opcao));
			else
			{
				opcao = action.getInt("opcao2");
				
				if(action.getLong("usuario") == 0)
					throw new Exception("Usuario en blanco");
				
				Usuario usuario = (Usuario) entidadeHome.obterEntidadePorId(action.getLong("usuario"));
				
				if(!opcaoHome.obterUsuarios(opcao).contains(usuario))
					opcaoHome.incluirUsuario(opcao, usuario);
				
				this.setResponseView(new LiberarOpcoesView(opcao));
			}
			
			mm.commitTransaction();
		}
		catch (Exception exception)
		{
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new LiberarOpcoesView(opcao));
			mm.rollbackTransaction();
		}
	}
	
	public void relOpcoes(Action action) throws Exception 
	{
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		OpcaoHome opcaoHome = (OpcaoHome) mm.getHome("OpcaoHome");
		Usuario usuario = null;
		int opcao = action.getInt("liberarOpcao");
		Collection dados = new ArrayList();
		try
		{
			if(action.getBoolean("view"))
				this.setResponseView(new RelOpcoesView(usuario,0, dados));
			else
			{
				if((opcao > 0 && action.getLong("usuarioId") > 0) || (opcao == 0 && action.getLong("usuarioId") == 0))
					throw new Exception("Elija una de las opciones");
				
				if(action.getLong("usuarioId") > 0)
				{
					usuario = (Usuario) entidadeHome.obterEntidadePorId(action.getLong("usuarioId"));
					dados = opcaoHome.obterOpcoesPorNome(usuario);
				}
				
				if(opcao==-1)
					dados = opcaoHome.obterOpcoes();
				else if(opcao > 0)
					dados = opcaoHome.obterUsuarios(opcao);
				else if(usuario!=null)
					dados = opcaoHome.obterOpcoesPorNome(usuario);
				
				//if(action.getBoolean("excel"))
				//{
					if(opcao == -1)
					{
						RelOpcoes2XLS xls = new RelOpcoes2XLS(usuario, opcao, dados, opcaoHome);
						InputStream arquivo = xls.obterArquivo();
						this.setResponseInputStream(arquivo);
				        this.setResponseFileName("Listado Opciones.xls");
				        this.setResponseContentType("application/vnd.ms-excel");
				        this.setResponseContentSize(arquivo.available());
					}
					else
					{
						RelOpcoesXLS xls = new RelOpcoesXLS(usuario, opcao, dados, opcaoHome);
						InputStream arquivo = xls.obterArquivo();
						this.setResponseInputStream(arquivo);
				        this.setResponseFileName("Listado Opciones.xls");
				        this.setResponseContentType("application/vnd.ms-excel");
				        this.setResponseContentSize(arquivo.available());
					}
				//}
				//else
					//this.setResponseView(new RelOpcoesView(usuario,opcao, dados));
			}
		}
		catch (Exception exception)
		{
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new RelOpcoesView(usuario,0, dados));
		}
	}
	
	public void excluirOpcao(Action action) throws Exception 
	{
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		OpcaoHome opcaoHome = (OpcaoHome) mm.getHome("OpcaoHome");
		int opcao = action.getInt("opcao2");

		mm.beginTransaction();
		try
		{
			Usuario usuario = (Usuario) entidadeHome.obterEntidadePorId(action.getLong("usuario2"));
			
			opcaoHome.excluirUsuario(opcao, usuario);
				
			this.setResponseView(new LiberarOpcoesView(opcao));
			
			mm.commitTransaction();
		}
		catch (Exception exception)
		{
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new LiberarOpcoesView(opcao));
			mm.rollbackTransaction();
		}
	}
	
	public void relOpcoesResEscaneada(Action action) throws Exception 
	{
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		AseguradoraHome aseguradoraHome = (AseguradoraHome) mm.getHome("AseguradoraHome");
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		OpcaoHome opcaoHome = (OpcaoHome) mm.getHome("OpcaoHome");
		Collection<Aseguradora> aseguradoras = aseguradoraHome.obterAseguradoras();
		Collection<Usuario> usuariosResEscaneada = usuarioHome.obterUsuariosResEscaneada();
		Map<Long,Aseguradora> escolhidas = new TreeMap<Long,Aseguradora>();
		Usuario usuario = null;
		mm.beginTransaction();
		try
		{
			long usuarioId = action.getLong("usuarioId2");
			if(usuarioId == 0)
				usuarioId = action.getLong("usuarioId");	
			if(usuarioId > 0)
			{
				usuario = (Usuario) entidadeHome.obterEntidadePorId(usuarioId);
				escolhidas = usuario.obterAseguradorasResEscaneada();
			}
			
			if(action.getBoolean("view"))
				this.setResponseView(new RelOpcaoResEscaneadaView(usuario, escolhidas.values(), aseguradoras,usuariosResEscaneada));
			else
			{
				if(usuarioId == 0)
					throw new Exception("Usuario en blanco");
				
				String[] aseguradorasArray = action.getStringArray("check");
				if(aseguradorasArray.length == 0)
					throw new Exception("Elegir las Aseguradoras");
				
				if(!opcaoHome.obterUsuarios(65).contains(usuario))
					opcaoHome.incluirUsuario(65, usuario);
				
				if(!opcaoHome.obterUsuarios(79).contains(usuario))
					opcaoHome.incluirUsuario(79, usuario);
				
				for(int i = 0 ; i < aseguradorasArray.length ; i++)
				{
					long id = Long.valueOf(aseguradorasArray[i]);
					
					if(!escolhidas.containsKey(id))
					{
						Aseguradora aseg = (Aseguradora) entidadeHome.obterEntidadePorId(id);
						usuario.addResEscaneada(aseg);
					}
				}
				
				escolhidas = usuario.obterAseguradorasResEscaneada();
				usuariosResEscaneada = usuarioHome.obterUsuariosResEscaneada();
				
				this.setResponseView(new RelOpcaoResEscaneadaView(usuario, escolhidas.values(), aseguradoras,usuariosResEscaneada));
			}
			
			mm.commitTransaction();
		}
		catch (Exception exception)
		{
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new RelOpcaoResEscaneadaView(usuario, escolhidas.values(), aseguradoras,usuariosResEscaneada));
			mm.rollbackTransaction();
		}
	}
}
