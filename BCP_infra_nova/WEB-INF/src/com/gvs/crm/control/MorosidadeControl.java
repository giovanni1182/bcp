package com.gvs.crm.control;

import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

import com.gvs.crm.model.CRMModelManager;
import com.gvs.crm.model.Morosidade;
import com.gvs.crm.model.MorosidadeHome;
import com.gvs.crm.model.Usuario;
import com.gvs.crm.model.UsuarioHome;
import com.gvs.crm.view.CentralRiscoMorosidadeView;

import infra.control.Action;
import infra.control.Control;

public class MorosidadeControl extends Control 
{
	public void visualizarCentralRiscoMorosidade(Action action) throws Exception 
	{
		CRMModelManager mm = new CRMModelManager(this.getUser());
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		MorosidadeHome morosidadeHome = (MorosidadeHome) mm.getHome("MorosidadeHome");
		Usuario usuarioAtual = usuarioHome.obterUsuarioPorUser(this.getUser());
		String nomeAsegurado = null;
		String documento = null;
		//Collection<Entidade> aseguradoras = new ArrayList<>();
		Map<Long,Collection<Morosidade>> morosidades = new TreeMap<>();
		
		mm.beginTransaction();
		try 
		{
			nomeAsegurado = action.getString("nome").trim();
			documento = action.getString("documento").trim();
			
			if(!action.getBoolean("view"))
			{
				boolean erro = true;
				
				if(nomeAsegurado.length() == 0 && documento.length() == 0)
					throw new Exception("Complete el Nombre o Documento");
				else if(nomeAsegurado.length() < 10 && documento.length() > 4)
					erro = false;
				else if(nomeAsegurado.length() > 9)
				{
					if(documento.length() > 0)
					{
						if(documento.length() < 4)
							erro = true;
						else
							erro = false;
					}
					else
						erro = false;
				}
				
				if(erro)
				{
					if(nomeAsegurado.length() < 10)
						throw new Exception("El nombre debe tener al menos 10 caracteres");
					else if(documento.length() < 4)
						throw new Exception("El documento debe tener al menos 5 dígitos");
				}
				
				//aseguradoras = morosidadeHome.obterAseguradorasCentralRisco(nomeAsegurado, documento);
				morosidades = morosidadeHome.obterAseguradorasCentralRiscoNovo(nomeAsegurado, documento);
			}
			
			//this.setResponseView(new CentralRiscoMorosidadeView(usuarioAtual, aseguradoras, nomeAsegurado, documento));
			this.setResponseView(new CentralRiscoMorosidadeView(usuarioAtual, morosidades, nomeAsegurado, documento));
			
			mm.commitTransaction();

		} 
		catch (Exception exception) 
		{
			this.setAlert(Util.translateException(exception));
			//this.setResponseView(new CentralRiscoMorosidadeView(usuarioAtual, aseguradoras, nomeAsegurado, documento));
			this.setResponseView(new CentralRiscoMorosidadeView(usuarioAtual, morosidades, nomeAsegurado, documento));
			mm.rollbackTransaction();
		}
	}
}
