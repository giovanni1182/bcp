package com.gvs.crm.model.impl;

import java.util.Date;
import java.util.Iterator;

import com.gvs.crm.model.Evento;
import com.gvs.crm.model.Tarefa;

import infra.sql.SQLUpdate;

public class TarefaImpl extends EventoImpl implements Tarefa {

	public void calcularPrevisoes() throws Exception {
		if (this.obterInferiores().isEmpty()) {
			if (this.obterDataPrevistaInicio() != null
					&& this.obterDataPrevistaConclusao() != null) {
				this.atualizarDuracao(new Long(this
						.obterDataPrevistaConclusao().getTime()
						- this.obterDataPrevistaInicio().getTime()));
			}
		} else {
			long duracao = 0;
			for (Iterator i = this.obterInferiores().iterator(); i.hasNext();) {
				Evento e = (Evento) i.next();
				duracao = duracao + e.obterDuracao();
			}
			if (duracao > 0)
				this.atualizarDuracao(new Long(duracao));
		}
		if (this.obterSuperior() != null)
			this.obterSuperior().calcularPrevisoes();
	}

	public void concluir(Date dataEfetivaConclusao, String comentario)
			throws Exception {
		// TODO implementar a ação de concluir uma tarefa
	}

	public void excluir() throws Exception {
		super.excluir();
		SQLUpdate delete = this.getModelManager().createSQLUpdate(
				"delete from tarefa where id=?");
		delete.addLong(this.obterId());
		delete.execute();
	}

	public void incluir() throws Exception {
		super.incluir();
		SQLUpdate insert = this.getModelManager().createSQLUpdate(
				"insert into tarefa (id) values (?)");
		insert.addLong(this.obterId());
		insert.execute();
	}

	public void iniciar(Date dataEfetivaInicio) throws Exception {
		// TODO implementar a ação e iniciar uma tarefa
	}

	public Date obterDataEfetivaConclusao() throws Exception {
		return null;
	}

	public Date obterDataEfetivaInicio() throws Exception {
		return null;
	}

	public long obterDuracaoEfetiva() throws Exception {
		return 0;
	}

	public String obterIcone() throws Exception {
		return "task.gif";
	}

	public boolean permiteConcluir() throws Exception {
		return this.obterInferiores().isEmpty()
				&& this.obterUsuarioAtual().equals(this.obterResponsavel())
				&& !this.obterFase().equals(Tarefa.EVENTO_CONCLUIDO);
	}

	public boolean permiteExcluir() throws Exception {
		return this.obterUsuarioAtual().equals(this.obterResponsavel())
				&& this.obterFase().equals(EVENTO_PENDENTE);
	}

	public boolean permiteIniciar() throws Exception {
		return false;
	}
}