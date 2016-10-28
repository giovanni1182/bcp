package com.gvs.crm.control;

import com.gvs.crm.model.Apolice;
import com.gvs.crm.model.CRMModelManager;
import com.gvs.crm.model.Entidade;
import com.gvs.crm.model.EntidadeHome;
import com.gvs.crm.model.EventoHome;
import com.gvs.crm.model.OcorrenciaApolice;
import com.gvs.crm.model.Usuario;
import com.gvs.crm.model.UsuarioHome;
import com.gvs.crm.view.EventoView;

import infra.control.Action;
import infra.control.Control;

public class OcorrenciaApoliceControl extends Control {
	public void atualizarOcorrenciaApolice(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Usuario usuarioAtual = usuarioHome.obterUsuarioPorUser(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		EventoHome eventoHome = (EventoHome) mm.getHome("EventoHome");

		OcorrenciaApolice ocorrencia = (OcorrenciaApolice) eventoHome
				.obterEventoPorId(action.getLong("id"));

		mm.beginTransaction();
		try {
			Apolice apolice = null;

			if (action.getLong("aseguradoraId") == 0)
				throw new Exception("Elegiré el Aseguradora");
			Entidade seguradora = entidadeHome.obterEntidadePorId(action
					.getLong("aseguradoraId"));
			ocorrencia.atribuirOrigem(seguradora);

			if (action.getLong("responsavelId") == 0)
				throw new Exception("Elegiré el Responsable");
			Entidade responsavel = entidadeHome.obterEntidadePorId(action
					.getLong("responsavelId"));
			ocorrencia.atribuirResponsavel(responsavel);

			ocorrencia.atribuirOrigem(seguradora);
			ocorrencia.atribuirResponsavel(responsavel);
			ocorrencia.atribuirTipo(action.getString("tipo"));
			ocorrencia.atribuirTitulo(action.getString("titulo"));
			ocorrencia.atribuirDescricao(action.getString("descricao"));

			if (seguradora == null)
				throw new Exception("A aseguradora não foi encontrado");
			if (responsavel == null)
				throw new Exception("O responsable não foi selecionado");
			if (action.getString("tipo").equals(""))
				throw new Exception("Elegiré el Tipo de Reporte");
			if (action.getLong("apoliceId") > 0)
				apolice = (Apolice) eventoHome.obterEventoPorId(action
						.getLong("apoliceId"));

			ocorrencia.atualizarOrigem(seguradora);
			ocorrencia.atualizarTipo(action.getString("tipo"));
			ocorrencia.atualizarDescricao(action.getString("descricao"));

			ocorrencia.atualizarExpediente(action.getString("expediente"));

			if (action.getDate("dataSuspeita") != null)
				ocorrencia
						.atualizarDataSuspeita(action.getDate("dataSuspeita"));

			if (action.getDate("dataReporte") != null)
				ocorrencia.atualizarDataReporte(action.getDate("dataReporte"));

			ocorrencia.atualizarNumeroConta(action.getString("numeroConta"));
			ocorrencia.atualizarEntidade(action.getString("entidade"));
			ocorrencia.atualizarTitular(action.getString("titular"));
			ocorrencia.atualizarCidade(action.getString("cidade"));
			ocorrencia.atualizarPais(action.getString("pais"));
			ocorrencia.atualizarRazao(action.getString("razao"));
			ocorrencia.atualizarEndereco(action.getString("endereco"));
			ocorrencia.atualizarBairro(action.getString("bairro"));
			ocorrencia.atualizarTelefone(action.getString("telefone"));

			if (apolice != null)
				ocorrencia.atualizarApolice(apolice);

			this.setResponseView(new EventoView(ocorrencia));
			mm.commitTransaction();
		} catch (Exception exception) {
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new EventoView(ocorrencia));
			mm.rollbackTransaction();
		}
	}

	public void incluirOcorrenciaApolice(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Usuario usuarioAtual = usuarioHome.obterUsuarioPorUser(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		EventoHome eventoHome = (EventoHome) mm.getHome("EventoHome");

		OcorrenciaApolice ocorrencia = (OcorrenciaApolice) mm
				.getEntity("OcorrenciaApolice");

		mm.beginTransaction();
		try {
			long seguradoraId = action.getLong("aseguradoraId");
			Entidade seguradora = entidadeHome.obterEntidadePorId(seguradoraId);

			long responsavelId = action.getLong("responsavelId");
			Entidade responsavel = entidadeHome
					.obterEntidadePorId(responsavelId);

			Entidade destino = entidadeHome.obterEntidadePorApelido("bcp");

			Apolice apolice = null;

			ocorrencia.atribuirOrigem(seguradora);
			ocorrencia.atribuirDestino(destino);
			ocorrencia.atribuirResponsavel(responsavel);
			ocorrencia.atribuirTipo(action.getString("tipo"));
			ocorrencia.atribuirTitulo("Ocurrencia de la Póliza");
			ocorrencia.atribuirDescricao(action.getString("descricao"));

			if (seguradoraId == 0)
				throw new Exception("Elegiré el Aseguradora");
			if (seguradora == null)
				throw new Exception("A aseguradora não foi encontrado");
			if (responsavelId == 0)
				throw new Exception("Elegiré el Responsable");
			if (responsavel == null)
				throw new Exception("O responsable não foi selecionado");
			if (action.getString("tipo").equals(""))
				throw new Exception("Elegiré el Tipo de Reporte");
			if (action.getLong("apoliceId") > 0)
				apolice = (Apolice) eventoHome.obterEventoPorId(action
						.getLong("apoliceId"));

			ocorrencia.incluir();

			ocorrencia.atualizarExpediente(action.getString("expediente"));

			if (action.getDate("dataSuspeita") != null)
				ocorrencia
						.atualizarDataSuspeita(action.getDate("dataSuspeita"));

			if (action.getDate("dataReporte") != null)
				ocorrencia.atualizarDataReporte(action.getDate("dataReporte"));

			ocorrencia.atualizarNumeroConta(action.getString("numeroConta"));
			ocorrencia.atualizarEntidade(action.getString("entidade"));
			ocorrencia.atualizarTitular(action.getString("titular"));
			ocorrencia.atualizarCidade(action.getString("cidade"));
			ocorrencia.atualizarPais(action.getString("pais"));
			ocorrencia.atualizarRazao(action.getString("razao"));
			ocorrencia.atualizarEndereco(action.getString("endereco"));
			ocorrencia.atualizarBairro(action.getString("bairro"));
			ocorrencia.atualizarTelefone(action.getString("telefone"));

			if (apolice != null)
				ocorrencia.atualizarApolice(apolice);

			this.setResponseView(new EventoView(ocorrencia));
			mm.commitTransaction();
		} catch (Exception exception) {
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new EventoView(ocorrencia));
			mm.rollbackTransaction();
		}
	}
}