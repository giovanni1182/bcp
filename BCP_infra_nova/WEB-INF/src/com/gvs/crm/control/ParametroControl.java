package com.gvs.crm.control;

import java.util.Date;

import com.gvs.crm.model.AgendaMeicos;
import com.gvs.crm.model.CRMModelManager;
import com.gvs.crm.model.Entidade;
import com.gvs.crm.model.EntidadeHome;
import com.gvs.crm.model.EventoHome;
import com.gvs.crm.model.Parametro;
import com.gvs.crm.view.ConfiguracaoView;
import com.gvs.crm.view.ConsistenciaView;
import com.gvs.crm.view.ControleDocumentoView;
import com.gvs.crm.view.EntidadeView;
import com.gvs.crm.view.EventoView;
import com.gvs.crm.view.FeriadoView;
import com.gvs.crm.view.IndicadoresView;

import infra.control.Action;
import infra.control.Control;

public class ParametroControl extends Control {
	public void novaConsistencia(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		EventoHome eventoHome = (EventoHome) mm.getHome("EventoHome");

		Entidade entidade = entidadeHome.obterEntidadePorId(action
				.getLong("entidadeId"));
		Entidade origemMenu = entidadeHome.obterEntidadePorId(action
				.getLong("origemMenuId"));

		this.setResponseView(new ConsistenciaView(entidade, origemMenu));
	}

	public void novoFeriado(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		EventoHome eventoHome = (EventoHome) mm.getHome("EventoHome");

		Entidade entidade = entidadeHome.obterEntidadePorId(action
				.getLong("entidadeId"));
		Entidade origemMenu = entidadeHome.obterEntidadePorId(action
				.getLong("origemMenuId"));

		this.setResponseView(new FeriadoView(entidade, origemMenu));
	}

	public void novoControleDocumento(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		EventoHome eventoHome = (EventoHome) mm.getHome("EventoHome");

		Parametro parametro = (Parametro) entidadeHome
				.obterEntidadePorId(action.getLong("entidadeId"));
		Entidade origemMenu = entidadeHome.obterEntidadePorId(action
				.getLong("origemMenuId"));

		this.setResponseView(new ControleDocumentoView(parametro));
	}

	public void novoIndicador(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		EventoHome eventoHome = (EventoHome) mm.getHome("EventoHome");

		Parametro parametro = (Parametro) entidadeHome
				.obterEntidadePorId(action.getLong("entidadeId"));
		Entidade origemMenu = entidadeHome.obterEntidadePorId(action
				.getLong("origemMenuId"));

		this.setResponseView(new IndicadoresView(parametro));
	}

	public void incluirIndicador(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		EventoHome eventoHome = (EventoHome) mm.getHome("EventoHome");

		Parametro parametro = (Parametro) entidadeHome
				.obterEntidadePorId(action.getLong("id"));

		mm.beginTransaction();
		try {

			String descricao = action.getString("descricao");

			if (descricao.equals(""))
				throw new Exception("Preencha a Descripición");

			String excludente = "Não";

			if (action.getString("excludente") != null)
				excludente = action.getString("excludente");

			parametro.adicionarIndicador(action.getString("tipo"), descricao,
					action.getInt("peso"), excludente);

			mm.commitTransaction();

			this.setResponseView(new IndicadoresView(parametro));

		} catch (Exception exception) {
			this.setAlert(Util.translateException(exception));
			mm.rollbackTransaction();
			this.setResponseView(new EntidadeView(parametro));
		}
	}

	public void atualizarIndicador(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		EventoHome eventoHome = (EventoHome) mm.getHome("EventoHome");

		Parametro parametro = (Parametro) entidadeHome
				.obterEntidadePorId(action.getLong("parametroId"));

		mm.beginTransaction();
		try {

			String[] tipo = action.getStringArray("tipo2");

			for (int i = 0; i < tipo.length; i++) {
				String tipoStr = tipo[i];

				int[] seqs = action.getIntArray("seq" + tipoStr);

				for (int j = 0; j < seqs.length; j++) {
					int seq = seqs[j];

					String descricao = action.getString("descricao" + tipoStr
							+ seq);

					int peso = action.getInt("peso" + tipoStr + seq);

					Parametro.Indicador indicador = parametro.obterIndicador(
							tipoStr, seq);

					indicador.atualizar(descricao, peso);
				}
			}

			mm.commitTransaction();

			this.setResponseView(new IndicadoresView(parametro));

		} catch (Exception exception) {
			this.setAlert(Util.translateException(exception));
			mm.rollbackTransaction();
			this.setResponseView(new EntidadeView(parametro));
		}
	}

	public void excluirIndicador(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		EventoHome eventoHome = (EventoHome) mm.getHome("EventoHome");

		Parametro parametro = (Parametro) entidadeHome
				.obterEntidadePorId(action.getLong("id"));

		Parametro.Indicador indicador = parametro.obterIndicador(action
				.getString("tipo"), action.getInt("seq"));

		mm.beginTransaction();
		try {
			parametro.removerIndicador(indicador);

			mm.commitTransaction();

			this.setResponseView(new IndicadoresView(parametro));

		} catch (Exception exception) {
			this.setAlert(Util.translateException(exception));
			mm.rollbackTransaction();
			this.setResponseView(new EntidadeView(parametro));
		}
	}

	public void incluirControleDocumento(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		EventoHome eventoHome = (EventoHome) mm.getHome("EventoHome");

		Parametro parametro = (Parametro) entidadeHome
				.obterEntidadePorId(action.getLong("id"));

		mm.beginTransaction();
		try {

			String descricao = action.getString("documento");

			if (descricao.equals(""))
				throw new Exception("Preencha o Documento");

			if (action.getDate("dataLimite") == null)
				throw new Exception("Preencha a Fecha a Presentar");

			parametro.adicionarControleDocumento(descricao, action
					.getDate("dataLimite"));

			mm.commitTransaction();

			this.setResponseView(new ControleDocumentoView(parametro));

		} catch (Exception exception) {
			this.setAlert(Util.translateException(exception));
			mm.rollbackTransaction();
			this.setResponseView(new ControleDocumentoView(parametro));
		}
	}

	public void atualizarControleDocumento(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		EventoHome eventoHome = (EventoHome) mm.getHome("EventoHome");

		Parametro parametro = (Parametro) entidadeHome
				.obterEntidadePorId(action.getLong("id"));

		mm.beginTransaction();
		try {

			int[] controlesId = action.getIntArray("seq");

			for (int i = 0; i < controlesId.length; i++) {
				if (action.getDate("dataLimite" + controlesId[i]) == null)
					throw new Exception("Preencha a Fecha a Presentar");

				Parametro.ControleDocumento controle = parametro
						.obterControleDocumento(controlesId[i]);

				String descricao = action.getString("descricao"
						+ controlesId[i]);

				Date dataLimite = action.getDate("dataLimite" + controlesId[i]);

				controle.atualizar(descricao, dataLimite);
			}

			if (action.getLong("agendaId") > 0) {
				AgendaMeicos agenda = (AgendaMeicos) eventoHome
						.obterEventoPorId(action.getLong("agendaId"));

				this.setResponseView(new EventoView(agenda));
			} else
				this.setResponseView(new ControleDocumentoView(parametro));

			mm.commitTransaction();

		} catch (Exception exception) {
			this.setAlert(Util.translateException(exception));
			mm.rollbackTransaction();
			this.setResponseView(new ControleDocumentoView(parametro));
		}
	}

	public void excluirControleDocumento(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		EventoHome eventoHome = (EventoHome) mm.getHome("EventoHome");

		Parametro parametro = (Parametro) entidadeHome
				.obterEntidadePorId(action.getLong("id"));

		Parametro.ControleDocumento controle = parametro
				.obterControleDocumento(action.getInt("seq2"));

		mm.beginTransaction();
		try {

			parametro.excluirControleDocumento(controle);

			mm.commitTransaction();

			this.setResponseView(new ControleDocumentoView(parametro));

		} catch (Exception exception) {
			this.setAlert(Util.translateException(exception));
			mm.rollbackTransaction();
			this.setResponseView(new EntidadeView(parametro));
		}
	}

	public void novaConfiguracao(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		EventoHome eventoHome = (EventoHome) mm.getHome("EventoHome");

		Entidade entidade = entidadeHome.obterEntidadePorId(action
				.getLong("entidadeId"));
		Entidade origemMenu = entidadeHome.obterEntidadePorId(action
				.getLong("origemMenuId"));

		this.setResponseView(new ConfiguracaoView(entidade, origemMenu));
	}

	public void visualizarFeriado(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		Parametro parametro = (Parametro) entidadeHome
				.obterEntidadePorId(action.getLong("entidadeId"));
		Entidade origemMenu = entidadeHome.obterEntidadePorId(action
				.getLong("origemMenuId"));
		Parametro.Feriado feriado = (Parametro.Feriado) parametro
				.obterFeriado(action.getInt("id"));
		this.setResponseView(new FeriadoView(feriado, origemMenu));
	}

	public void visualizarConsistencia(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		Parametro parametro = (Parametro) entidadeHome
				.obterEntidadePorId(action.getLong("entidadeId"));
		Entidade origemMenu = entidadeHome.obterEntidadePorId(action
				.getLong("origemMenuId"));

		Parametro.Consistencia consistencia = (Parametro.Consistencia) parametro
				.obterConsistencia(action.getInt("id"), action.getInt("regra"));
		this.setResponseView(new ConsistenciaView(consistencia, origemMenu));
	}

	public void excluirFeriado(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		Parametro parametro = (Parametro) entidadeHome
				.obterEntidadePorId(action.getLong("entidadeId"));
		Parametro.Feriado feriado = parametro.obterFeriado(action.getInt("id"));
		mm.beginTransaction();
		try {
			parametro.removerFeriado(feriado);
			mm.commitTransaction();
		} catch (Exception exception) {
			this.setAlert(Util.translateException(exception));
			mm.rollbackTransaction();
		}
		this.setResponseView(new EntidadeView(parametro));
	}

	public void excluirConsistencia(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		Parametro parametro = (Parametro) entidadeHome
				.obterEntidadePorId(action.getLong("entidadeId"));
		Parametro.Consistencia consistencia = parametro.obterConsistencia(
				action.getInt("id"), action.getInt("regra"));
		mm.beginTransaction();
		try {
			parametro.removerConsistencia(consistencia, action.getInt("regra"));
			parametro.obterConsistencias().remove(consistencia);
			mm.commitTransaction();
			this.setResponseView(new ConsistenciaView(parametro, parametro));
		} catch (Exception exception) {
			this.setAlert(Util.translateException(exception));
			mm.rollbackTransaction();
		}
	}

	public void incluirFeriado(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		EventoHome eventoHome = (EventoHome) mm.getHome("EventoHome");
		Parametro parametro = (Parametro) entidadeHome
				.obterEntidadePorId(action.getLong("entidadeId"));
		mm.beginTransaction();
		try {
			if (action.getDate("data") == null)
				throw new Exception("Elegiré la fecha");

			String descricao = action.getString("descricao");
			Date data = action.getDate("data");
			parametro.adicionarFeriado(descricao, data);
			mm.commitTransaction();

			this.setResponseView(new EntidadeView(parametro));
		} catch (Exception exception) {
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new FeriadoView(parametro, parametro));
			mm.rollbackTransaction();
		}
	}

	public void incluirConsistencia(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		EventoHome eventoHome = (EventoHome) mm.getHome("EventoHome");
		Parametro parametro = (Parametro) entidadeHome
				.obterEntidadePorId(action.getLong("entidadeId"));
		mm.beginTransaction();
		try {

			String operando1 = action.getString("operando1");
			String operador = action.getString("operador");
			String operando2 = action.getString("operando2");
			String mensagem = action.getString("mensagem");
			int regra = action.getInt("regra");

			parametro.adicionarConsistencia(operando1, operador, operando2,
					mensagem, regra);
			mm.commitTransaction();

			this.setResponseView(new ConsistenciaView(parametro, parametro));
		} catch (Exception exception) {
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new ConsistenciaView(parametro, parametro));
			mm.rollbackTransaction();
		}
	}

	public void atualizarFeriado(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		Parametro parametro = (Parametro) entidadeHome
				.obterEntidadePorId(action.getLong("entidadeId"));
		Parametro.Feriado feriado = (Parametro.Feriado) parametro
				.obterFeriado(action.getInt("id"));
		mm.beginTransaction();
		try {
			feriado.atualizarValor(action.getString("descricao"), action
					.getDate("data"));
			this.setResponseView(new EntidadeView(parametro));
			mm.commitTransaction();
		} catch (Exception exception) {
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new EntidadeView(parametro));
			mm.rollbackTransaction();
		}
	}

	public void atualizarConsistencia(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		Parametro parametro = (Parametro) entidadeHome
				.obterEntidadePorId(action.getLong("entidadeId"));
		Parametro.Consistencia consistencia = (Parametro.Consistencia) parametro
				.obterConsistencia(action.getInt("id"), action.getInt("regra"));
		mm.beginTransaction();
		try {
			consistencia.atualizarValor(action.getString("operando1"), action
					.getString("operador"), action.getString("operando2"),
					action.getString("mensagem"), action.getInt("regra"));
			this.setResponseView(new ConsistenciaView(parametro, parametro));
			mm.commitTransaction();
		} catch (Exception exception) {
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new EntidadeView(parametro));
			mm.rollbackTransaction();
		}
	}

}