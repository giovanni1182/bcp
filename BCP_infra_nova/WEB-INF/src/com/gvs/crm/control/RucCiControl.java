package com.gvs.crm.control;

import java.util.ArrayList;
import java.util.Collection;

import com.gvs.crm.model.CRMModelManager;
import com.gvs.crm.model.RucCiHome;
import com.gvs.crm.view.PesquisaRucCiView;

import infra.control.Action;
import infra.control.Control;

public class RucCiControl extends Control
{
	public void pesquisaRucCi(Action action) throws Exception
	{
		CRMModelManager mm = new CRMModelManager(this.getUser());
		RucCiHome rucCiHome = (RucCiHome) mm.getHome("RucCiHome");
		String tipoDoc = action.getString("tipoDoc");
		String numeroDoc = action.getString("numeroDoc").trim();
		Collection<String> pessoas = new ArrayList<String>();
		boolean listar = action.getBoolean("listar");
		try 
		{
			if(!action.getBoolean("view"))
			{
				if(numeroDoc.length() == 0)
					throw new Exception("Preencha o Nº do documento");
				
				pessoas = rucCiHome.obterPessoaPorDoc(tipoDoc, numeroDoc);
				
				/*String s = "1;BR;Giovanni;Silva;11/11/1982";
				pessoas.add(s);*/
			}
			
			this.setResponseView(new PesquisaRucCiView(tipoDoc, numeroDoc, pessoas,listar));
		}
		catch (Exception exception)
		{
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new PesquisaRucCiView(tipoDoc, numeroDoc, pessoas,listar));
		}
	}
}
