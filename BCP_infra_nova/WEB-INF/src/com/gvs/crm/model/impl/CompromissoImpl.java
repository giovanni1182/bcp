package com.gvs.crm.model.impl;

import java.util.Date;
import java.util.Iterator;

import com.gvs.crm.model.Compromisso;
import com.gvs.crm.model.Evento;
import com.gvs.crm.model.Participacao;
import com.gvs.crm.model.Usuario;

public class CompromissoImpl extends EventoImpl implements Compromisso {
	public void adicionarParticipante(Usuario usuario) throws Exception {
		Participacao participacao = (Participacao) this.getModelManager()
				.getEntity("Participacao");
		participacao.atribuirSuperior(this);
		participacao.atribuirOrigem(this.obterOrigem());
		participacao.atribuirResponsavel(usuario);
		participacao.atribuirTitulo("Participacion");
		participacao.atribuirTipo(this.obterTipo());
		participacao.incluir();
	}

	public void atualizarDataPrevistaConclusao(Date dataPrevistaConclusao)
			throws Exception {
		super.atualizarDataPrevistaConclusao(dataPrevistaConclusao);
	}

	public void atualizarDataPrevistaInicio(Date dataPrevistaInicio)
			throws Exception {
		super.atualizarDataPrevistaInicio(dataPrevistaInicio);
	}

	public void calcularPrevisoes() throws Exception {
		if (this.obterFase().equals(COMPROMISSO_CONFIRMADO)) {
			this.atualizarDuracao(new Long(this.obterDataPrevistaConclusao()
					.getTime()
					- this.obterDataPrevistaInicio().getTime()));
			if (this.obterSuperior() != null)
				this.obterSuperior().calcularPrevisoes();
		}
	}

	public void concluir(String comentario) throws Exception {
		super.concluir(comentario);
		for (Iterator i = this.obterInferiores().iterator(); i.hasNext();)
			((Evento) i.next()).concluir(null);
	}

	public void confirmar() throws Exception {
		this.atualizarFase(COMPROMISSO_CONFIRMADO);
		this.atualizarComoNaoLido();
	}

	public void notificarParticipantes() throws Exception {
		for (Iterator i = this.obterInferiores().iterator(); i.hasNext();) {
			Evento e = (Evento) i.next();
			if (e instanceof Participacao
					&& e.obterFase().equals(Participacao.EVENTO_ACEITO)) {
				e.adicionarComentario("Compromisso atualizado por "
						+ this.obterUsuarioAtual().obterNome(),
						"Compromisso atualizado pelo responsável.");
				e.atualizarDataPrevistaInicio(null);
				e.atualizarDataPrevistaConclusao(null);
				e.atualizarFase(EVENTO_PENDENTE);
				e.atualizarComoNaoLido();
			}
		}
	}

	public String obterIcone() throws Exception {
		return "calendar.gif";
	}

	public boolean permiteConcluir() throws Exception {
		if (!super.permiteConcluir())
			return false;
		for (Iterator i = this.obterInferiores().iterator(); i.hasNext();) {
			Evento e = (Evento) i.next();
			if (e.obterFase().equals(EVENTO_PENDENTE))
				return false;
		}
		return true;
	}

	public boolean permiteExcluir() throws Exception {
		return this.obterUsuarioAtual().equals(this.obterResponsavel());
	}
}