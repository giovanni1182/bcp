package com.gvs.crm.control;

import com.gvs.crm.model.CRMModelManager;
import com.gvs.crm.model.Circular;
import com.gvs.crm.model.EventoHome;
import com.gvs.crm.report.CircularReport;
import com.gvs.crm.view.EventoView;

import infra.control.Action;
import infra.control.Control;

public class CircularControl extends Control
{
	public void abrirCircular(Action action) throws Exception
	{
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EventoHome eventoHome = (EventoHome) mm.getHome("EventoHome");
		Circular circular = (Circular) eventoHome.obterEventoPorId(action.getLong("id"));

		mm.beginTransaction();
		try 
		{
			this.setResponseReport(new CircularReport(circular, action.getLocale()));
			mm.commitTransaction();

		}
		catch (Exception exception)
		{
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new EventoView(circular.obterSuperior()));
			mm.rollbackTransaction();
		}
	}

	public void atualizarCircular(Action action) throws Exception
	{
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EventoHome eventoHome = (EventoHome) mm.getHome("EventoHome");
		Circular circular = (Circular) eventoHome.obterEventoPorId(action.getLong("id"));

		mm.beginTransaction();
		try
		{
			circular.atualizarDescricao(action.getString("descricao"));
			this.setResponseView(new EventoView(circular));
			mm.commitTransaction();
		}
		catch (Exception exception)
		{
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new EventoView(circular));
			mm.rollbackTransaction();
		}
	}
}