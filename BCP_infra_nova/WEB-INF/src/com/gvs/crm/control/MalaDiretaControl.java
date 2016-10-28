package com.gvs.crm.control;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.gvs.crm.model.CRMModelManager;
import com.gvs.crm.model.Entidade;
import com.gvs.crm.model.EntidadeHome;
import com.gvs.crm.model.Evento;
import com.gvs.crm.model.EventoHome;
import com.gvs.crm.model.MalaDireta;
import com.gvs.crm.model.Usuario;
import com.gvs.crm.model.UsuarioHome;
import com.gvs.crm.view.EventoView;
import com.gvs.crm.view.PaginaInicialView;

import infra.control.Action;
import infra.control.Control;

public class MalaDiretaControl extends Control {
	public void atualizarMalaDireta(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Usuario usuarioAtual = usuarioHome.obterUsuarioPorUser(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		Entidade origemMenu = entidadeHome.obterEntidadePorId(action
				.getLong("origemMenuId"));
		EventoHome eventoHome = (EventoHome) mm.getHome("EventoHome");
		MalaDireta malaDireta = (MalaDireta) eventoHome.obterEventoPorId(action
				.getLong("id"));
		mm.beginTransaction();
		try {
			long origemId = action.getLong("origemId");
			if (origemId == 0)
				throw new Exception("O Cliente deve ser selecionado");
			Entidade origem = entidadeHome.obterEntidadePorId(origemId);
			if (origem == null)
				throw new Exception("O Cliente não foi encontrado");
			if (action.getString("tipo").equals(""))
				throw new Exception("Escolha o tipo de Mala Direta");
			if (action.getString("data").equals(""))
				throw new Exception("Escolha uma Data para a Mala Direta");

			Date dataPrevista = new SimpleDateFormat("dd/MM/yyyy HH:mm")
					.parse(action.getString("data") + " "
							+ action.getString("inicio"));

			malaDireta.atribuirOrigem(origem);
			malaDireta.atualizarTitulo(action.getString("titulo"));
			malaDireta.atualizarTipo(action.getString("tipo"));
			malaDireta.atualizarDataPrevistaInicio(dataPrevista);
			malaDireta.atualizarDataPrevistaConclusao(dataPrevista);
			malaDireta.atualizarDescricao(action.getString("descricao"));
			malaDireta.atualizarPrioridade(Evento.PRIORIDADE_NORMAL);
			this
					.setResponseView(new PaginaInicialView(usuarioAtual,
							origemMenu));
			mm.commitTransaction();
		} catch (Exception exception) {
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new EventoView(malaDireta));
			mm.rollbackTransaction();
		}
	}

	public void incluirMalaDireta(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Usuario usuarioAtual = usuarioHome.obterUsuarioPorUser(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		Entidade origemMenu = entidadeHome.obterEntidadePorId(action
				.getLong("origemMenuId"));
		MalaDireta malaDireta = (MalaDireta) mm.getEntity("MalaDireta");
		mm.beginTransaction();
		try {

			long origemId = action.getLong("origemId");
			Entidade origem = entidadeHome.obterEntidadePorId(origemId);
			malaDireta.atribuirOrigem(origem);
			malaDireta.atribuirTipo(action.getString("tipo"));
			malaDireta.atribuirDescricao(action.getString("descricao"));

			if (origemId == 0)
				throw new Exception("O Cliente origem deve ser selecionado");
			if (origem == null)
				throw new Exception("O Cliente não foi encontrado");
			if (action.getString("tipo").equals(""))
				throw new Exception("Escolha o tipo de Mala Direta");
			if (action.getString("data").equals(""))
				throw new Exception("Escolha uma Data para o Contato");

			Date dataPrevista = new SimpleDateFormat("dd/MM/yyyy HH:mm")
					.parse(action.getString("data") + " "
							+ action.getString("inicio"));

			malaDireta.atribuirOrigem(origem);
			malaDireta.atribuirResponsavel(usuarioAtual);
			malaDireta.atribuirTitulo(action.getString("titulo"));
			malaDireta.atribuirTipo(action.getString("tipo"));
			malaDireta.atribuirDataPrevistaInicio(dataPrevista);
			malaDireta.atribuirDataPrevistaConclusao(dataPrevista);
			malaDireta.atribuirDescricao(action.getString("descricao"));
			malaDireta.incluir();

			this
					.setResponseView(new PaginaInicialView(usuarioAtual,
							origemMenu));
			mm.commitTransaction();
		} catch (Exception exception) {
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new EventoView(malaDireta, origemMenu));
			mm.rollbackTransaction();
		}
	}
}