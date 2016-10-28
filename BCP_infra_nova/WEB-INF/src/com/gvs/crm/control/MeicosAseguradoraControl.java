package com.gvs.crm.control;

import java.util.Date;

import com.gvs.crm.model.Aseguradora;
import com.gvs.crm.model.CRMModelManager;
import com.gvs.crm.model.EntidadeHome;
import com.gvs.crm.model.EventoHome;
import com.gvs.crm.model.MeicosAseguradora;
import com.gvs.crm.view.EntidadeView;

import infra.control.Action;
import infra.control.Control;

public class MeicosAseguradoraControl extends Control {
	public void atualizarMeicos(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		EventoHome eventoHome = (EventoHome) mm.getHome("EventoHome");

		Aseguradora aseguradora = (Aseguradora) entidadeHome.obterEntidadePorId(action.getLong("id"));

		MeicosAseguradora meicos = (MeicosAseguradora) eventoHome.obterEventoPorId(action.getLong("meicosId"));

		mm.beginTransaction();
		try {
			int[] seqs = action.getIntArray("seq");

			for (int i = 0; i < seqs.length; i++) 
			{
				int seq = seqs[i];

				if (!meicos.obterTipo().equals("Controle de Documentos")) 
				{
					MeicosAseguradora.Indicador indicador = meicos.obterIndicador(seq);

					String marcado = action.getString("marcado" + seq);

					indicador.atualizar(marcado);
				} 
				else 
				{
					MeicosAseguradora.ControleDocumento documento = meicos.obterDocumento(seq);

					Date dataEntrega = action.getDate("dataEntrega" + seq);

					if (dataEntrega != null)
						documento.atualizar(dataEntrega);
				}
			}

			this.setResponseView(new EntidadeView(aseguradora));
			mm.commitTransaction();

		} catch (Exception exception) {
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new EntidadeView(aseguradora));
			mm.rollbackTransaction();
		}
	}
}