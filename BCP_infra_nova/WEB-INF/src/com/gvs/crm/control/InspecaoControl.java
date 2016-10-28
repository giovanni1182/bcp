package com.gvs.crm.control;

import com.gvs.crm.model.CRMModelManager;
import com.gvs.crm.model.Entidade;
import com.gvs.crm.model.EntidadeHome;
import com.gvs.crm.model.EventoHome;
import com.gvs.crm.model.Inspecao;
import com.gvs.crm.model.Usuario;
import com.gvs.crm.model.UsuarioHome;
import com.gvs.crm.view.EventoView;

import infra.control.Action;
import infra.control.Control;

public class InspecaoControl extends Control {
	public void atualizarInspecao(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Usuario usuario = usuarioHome.obterUsuarioPorUser(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		EventoHome eventoHome = (EventoHome) mm.getHome("EventoHome");

		Inspecao inspecao = (Inspecao) eventoHome.obterEventoPorId(action
				.getLong("id"));

		mm.beginTransaction();
		try {
			if (action.getLong("origemId") == 0)
				throw new Exception("Elegiré a Aseguradora");
			if (action.getLong("inspetorId") == 0)
				throw new Exception("Elegiré o Inspector Responsable");
			if (action.getDate("dataPrevistaInicio") == null)
				throw new Exception("Elegiré a Fecha Prevista Inicio");
			if (action.getDate("dataPrevistaConclusao") == null)
				throw new Exception("Elegiré a Fecha Prevista Termino");
			if (action.getLong("responsavelId") == 0)
				throw new Exception("Elegiré lo Responsable");

			Entidade responsavel = entidadeHome.obterEntidadePorId(action
					.getLong("responsavelId"));

			Entidade inspetor = entidadeHome.obterEntidadePorId(action
					.getLong("inspetorId"));

			inspecao.atualizarInspetor(inspetor);
			inspecao.atualizarResponsavel(responsavel);

			Entidade origem = entidadeHome.obterEntidadePorId(action
					.getLong("origemId"));

			inspecao.atualizarOrigem(origem);

			inspecao.atualizarDataPrevistaInicio(action
					.getDate("dataPrevistaInicio"));

			inspecao.atualizarDataPrevistaConclusao(action
					.getDate("dataPrevistaConclusao"));

			inspecao.atualizarDiasCorridos(action.getString("diasCorridos"));

			if (action.getDate("dataPrevistaInicioReal") != null)
				inspecao.atualizarDataInicioReal(action
						.getDate("dataPrevistaInicioReal"));

			if (action.getDate("dataPrevistaConclusaoReal") != null)
				inspecao.atualizarDataTerminoReal(action
						.getDate("dataPrevistaConclusaoReal"));

			inspecao.atualizarTitulo(action.getString("titulo"));
			inspecao.atualizarDescricao(action.getString("descricao"));
			inspecao.atualizarTipo(action.getString("tipo"));

			this.setResponseView(new EventoView(inspecao));
			mm.commitTransaction();
			this.setAlert("Inspección Actualizada");
		} catch (Exception exception) {
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new EventoView(inspecao));
			mm.rollbackTransaction();
		}
	}

	public void incluirInspecao(Action action) throws Exception
	{
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		
		Inspecao inspecao = (Inspecao) mm.getEntity("Inspecao");

		mm.beginTransaction();
		try
		{

			inspecao.atribuirTitulo(action.getString("titulo"));
			inspecao.atribuirDescricao(action.getString("descricao"));
			inspecao.atribuirTipo(action.getString("tipo"));
			
			if (action.getLong("origemId") == 0)
				throw new Exception("Elegiré a Aseguradora");
			Entidade origem = entidadeHome.obterEntidadePorId(action
					.getLong("origemId"));
			inspecao.atribuirOrigem(origem);

			if (action.getLong("inspetorId") == 0)
				throw new Exception("Elegiré o Inspector Responsable");
			Entidade inspetor = entidadeHome.obterEntidadePorId(action.getLong("inspetorId"));
			inspecao.atribuirResponsavel(inspetor);
			inspecao.atribuirInspetor(inspetor);

			if (action.getDate("dataPrevistaInicio") == null)
				throw new Exception("Elegiré a Fecha Prevista Inicio");
			inspecao.atribuirDataPrevistaInicio(action
					.getDate("dataPrevistaInicio"));

			if (action.getDate("dataPrevistaConclusao") == null)
				throw new Exception("Elegiré a Fecha Prevista Termino");
			inspecao.atribuirDataPrevistaConclusao(action
					.getDate("dataPrevistaConclusao"));

			Entidade bcp = entidadeHome.obterEntidadePorApelido("bcp");

			//inspecao.atribuirOrigem(origem);
			inspecao.atribuirDestino(bcp);
			
			inspecao.atribuirResponsavel(inspetor);
			inspecao.incluir();

			inspecao.atualizarInspetor(inspetor);

			inspecao.atualizarDataPrevistaInicio(action.getDate("dataPrevistaInicio"));

			inspecao.atualizarDataPrevistaConclusao(action.getDate("dataPrevistaConclusao"));

			inspecao.atualizarDiasCorridos(action.getString("diasCorridos"));

			if (action.getDate("dataPrevistaInicioReal") != null)
				inspecao.atualizarDataInicioReal(action.getDate("dataPrevistaInicioReal"));

			if (action.getDate("dataPrevistaConclusaoReal") != null)
				inspecao.atualizarDataTerminoReal(action.getDate("dataPrevistaConclusaoReal"));

			this.setResponseView(new EventoView(inspecao));
			mm.commitTransaction();
			this.setAlert("Inspección Incluida");

		} 
		catch (Exception exception)
		{
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new EventoView(inspecao));
			mm.rollbackTransaction();
		}
	}
}