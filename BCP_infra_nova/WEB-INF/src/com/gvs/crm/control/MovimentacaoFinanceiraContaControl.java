package com.gvs.crm.control;

import java.math.BigDecimal;

import com.gvs.crm.model.CRMModelManager;
import com.gvs.crm.model.Conta;
import com.gvs.crm.model.Entidade;
import com.gvs.crm.model.EntidadeHome;
import com.gvs.crm.model.EventoHome;
import com.gvs.crm.model.MovimentacaoFinanceiraConta;
import com.gvs.crm.model.Usuario;
import com.gvs.crm.model.UsuarioHome;
import com.gvs.crm.view.EventoView;

import infra.control.Action;
import infra.control.Control;

public class MovimentacaoFinanceiraContaControl extends Control {
	public void incluirMovimentacaoFinanceiraConta(Action action)
			throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Usuario usuarioAtual = usuarioHome.obterUsuarioPorUser(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		MovimentacaoFinanceiraConta mf = (MovimentacaoFinanceiraConta) mm
				.getEntity("MovimentacaoFinanceiraConta");
		mm.beginTransaction();
		try {

			mf.atribuirDataPrevista(action.getDate("data_prevista"));
			mf.atribuirSaldoAtual(new BigDecimal(action.getDouble("saldo_atual")));
			mf.atribuirSaldoAnterior(new BigDecimal(action.getDouble("saldo_anterior")));
			mf.atribuirSaldoEstrangeiro(new BigDecimal(action.getDouble("saldo_estrangeiro")));
			mf.atribuirDebito(new BigDecimal(action.getDouble("debito")));
			mf.atribuirCredito(new BigDecimal(action.getDouble("credito")));
			mf.atribuirDescricao(action.getString("descricao"));

			if (action.getLong("seguradoraId") == 0)
				throw new Exception("Elegiré Aseguradora");
			if (action.getLong("responsavelId") == 0)
				throw new Exception("Elegiré Responsable");
			if (action.getDate("data_prevista") == null)
				throw new Exception("Elegiré Fecha Prevista");
			if (action.getLong("contaId") == 0)
				throw new Exception("Elegiré Cuenta");

			Entidade seguradora = (Entidade) entidadeHome
					.obterEntidadePorId(action.getLong("seguradoraId"));
			Conta conta = (Conta) entidadeHome.obterEntidadePorId(action
					.getLong("contaId"));
			Entidade responsavel = (Entidade) entidadeHome
					.obterEntidadePorId(action.getLong("responsavelId"));
			mf.atribuirOrigem(seguradora);
			mf.atribuirConta(conta);
			mf.atribuirResponsavel(responsavel);

			if (action.getDouble("saldo_atual") == 0)
				throw new Exception("Elegiré Saldo Actual");
			if (action.getDouble("saldo_estrangeiro") == 0)
				throw new Exception("Elegiré Saldo Moneda Extranjera");

			mf.atribuirDataPrevista(action.getDate("data_prevista"));
			mf.atribuirSaldoAtual(new BigDecimal(action.getDouble("saldo_atual")));
			mf.atribuirSaldoAnterior(new BigDecimal(action.getDouble("saldo_anterior")));
			mf.atribuirSaldoEstrangeiro(new BigDecimal(action.getDouble("saldo_estrangeiro")));
			mf.atribuirDebito(new BigDecimal(action.getDouble("debito")));
			mf.atribuirCredito(new BigDecimal(action.getDouble("credito")));
			mf.atribuirDescricao(action.getString("descricao"));
			mf.atribuirConta(conta);
			mf.atribuirTitulo("Cuenta");
			mf.atribuirResponsavel(responsavel);
			mf.incluir();
			mf.atualizarTitulo("Cuenta " + conta.obterCodigo() + "-"
					+ conta.obterNome());

			mm.commitTransaction();
			this.setAlert("Movimiento Incluido");
			this.setResponseView(new EventoView(mf));

		} catch (Exception exception) {
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new EventoView(mf));
			mm.rollbackTransaction();
		}
	}

	public void atualizarMovimentacaoFinanceiraConta(Action action)
			throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Usuario usuarioAtual = usuarioHome.obterUsuarioPorUser(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		EventoHome eventoHome = (EventoHome) mm.getHome("EventoHome");
		MovimentacaoFinanceiraConta mf = (MovimentacaoFinanceiraConta) eventoHome
				.obterEventoPorId(action.getLong("id"));
		mm.beginTransaction();
		try {
			Entidade seguradora = (Entidade) entidadeHome
					.obterEntidadePorId(action.getLong("seguradoraId"));
			Conta conta = (Conta) entidadeHome.obterEntidadePorId(action
					.getLong("contaId"));
			Entidade responsavel = (Entidade) entidadeHome
					.obterEntidadePorId(action.getLong("responsavelId"));
			mf.atribuirOrigem(seguradora);
			mf.atribuirDataPrevista(action.getDate("data_prevista"));
			mf.atribuirConta(conta);

			mf.atribuirSaldoAtual(new BigDecimal(action.getDouble("saldo_atual")));
			mf.atribuirSaldoAnterior(new BigDecimal(action.getDouble("saldo_anterior")));
			mf.atribuirSaldoEstrangeiro(new BigDecimal(action.getDouble("saldo_estrangeiro")));
			mf.atribuirDebito(new BigDecimal(action.getDouble("debito")));
			mf.atribuirCredito(new BigDecimal(action.getDouble("credito")));
			mf.atribuirTitulo("Cuenta");
			mf.atribuirResponsavel(responsavel);
			mf.atribuirDescricao(action.getString("descricao"));

			if (action.getLong("seguradoraId") == 0)
				throw new Exception("Elegiré Aseguradora");
			if (action.getLong("responsavelId") == 0)
				throw new Exception("Elegiré Responsable");
			if (action.getDate("data_prevista") == null)
				throw new Exception("Elegiré Fecha Prevista");
			if (action.getLong("contaId") == 0)
				throw new Exception("Elegiré Cuenta");
			if (action.getDouble("saldo_atual") == 0)
				throw new Exception("Elegiré Saldo Actual");
			if (action.getDouble("saldo_estrangeiro") == 0)
				throw new Exception("Elegiré Saldo Moneda Extranjera");

			mf.atualizarDataPrevista(action.getDate("data_prevista"));
			mf.atualizarConta(conta);

			mf.atualizarSaldoAtual(action.getDouble("saldo_atual"));
			mf.atualizarSaldoAnterior(action.getDouble("saldo_anterior"));
			mf.atualizarSaldoEstrangeiro(action.getDouble("saldo_estrangeiro"));
			mf.atualizarDebito(action.getDouble("debito"));
			mf.atualizarCredito(action.getDouble("credito"));
			mf.atualizarDescricao(action.getString("descricao"));
			mf.atualizarTitulo("Cuenta " + conta.obterCodigo() + "-"
					+ conta.obterNome());

			mm.commitTransaction();
			this.setAlert("Movimiento Actualizado");
			this.setResponseView(new EventoView(mf));

		} catch (Exception exception) {
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new EventoView(mf));
			mm.rollbackTransaction();
		}
	}
}