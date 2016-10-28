package com.gvs.crm.model.impl;

import java.util.Iterator;

import com.gvs.crm.model.Compromisso;
import com.gvs.crm.model.Evento;
import com.gvs.crm.model.Participacao;
import com.gvs.crm.model.Usuario;

public class ParticipacaoImpl extends MensagemImpl implements Participacao {
	public void aceitar(String comentario) throws Exception {
		this.obterSuperior().adicionarComentario(
				"Acepto por " + this.obterUsuarioAtual().obterNome(),
				comentario);
		this.atualizarDataPrevistaInicio(this.obterSuperior()
				.obterDataPrevistaInicio());
		this.atualizarDataPrevistaConclusao(this.obterSuperior()
				.obterDataPrevistaConclusao());
		this.atualizarFase(EVENTO_ACEITO);
		Evento superior = this.obterSuperior();
		if (superior != null) {
			boolean atualizarSuperior = true;
			for (Iterator i = superior.obterInferiores().iterator(); i
					.hasNext();) {
				Evento e = (Evento) i.next();
				if (!e.equals(this) && !e.obterFase().equals(EVENTO_ACEITO)) {
					atualizarSuperior = false;
					break;
				}
			}
			if (atualizarSuperior && superior instanceof Compromisso)
				((Compromisso) superior).confirmar();
		}
	}

	public void concluir(String comentario) throws Exception {
		if (comentario != null)
			this.adicionarComentario("Concluído por "
					+ obterUsuarioAtual().obterNome(), comentario);
		this.atualizarFase(Evento.EVENTO_CONCLUIDO);
	}

	public void encaminhar(Usuario usuario, String comentario) throws Exception {
		super.encaminhar(usuario, comentario);
		this.atualizarDataPrevistaInicio(null);
		this.atualizarDataPrevistaConclusao(null);
		this.atualizarFase(EVENTO_PENDENTE);
	}

	public String obterIcone() throws Exception {
		if (this.obterFase().equals(EVENTO_ACEITO))
			return "accept.gif";
		else if (this.obterFase().equals(EVENTO_RECUSADO))
			return "refuse.gif";
		else
			return "invite.gif";
	}

	public boolean permiteAceitar() throws Exception {
		return this.permiteAtualizar()
				&& this.obterFase().equals(EVENTO_PENDENTE);
	}

	public boolean permiteConcluir() throws Exception {
		return super.permiteAtualizar()
				&& this.obterFase().equals(EVENTO_ACEITO);
	}

	public boolean permiteEncaminhar() throws Exception {
		return this.obterUsuarioAtual().equals(this.obterResponsavel())
				&& !this.obterFase().equals(EVENTO_CONCLUIDO);
	}

	public boolean permiteRejeitar() throws Exception {
		return this.obterUsuarioAtual().equals(this.obterResponsavel())
				&& (this.obterFase().equals(EVENTO_PENDENTE) || this
						.obterFase().equals(EVENTO_ACEITO));
	}

	public void recusar(String comentario) throws Exception {
		this.obterSuperior().adicionarComentario(
				"Recusado por " + this.obterUsuarioAtual().obterNome(),
				comentario);
		this.atualizarDataPrevistaInicio(null);
		this.atualizarDataPrevistaConclusao(null);
		this.atualizarFase(EVENTO_RECUSADO);
	}
}