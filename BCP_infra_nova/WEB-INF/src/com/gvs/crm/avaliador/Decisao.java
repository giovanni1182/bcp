package com.gvs.crm.avaliador;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class Decisao {
	public static final int DECISAO_NULA = 0;

	public static final int DECISAO_APROVADA = 1;

	public static final int DECISAO_REJEITADA = 2;

	private int decisao;

	private Collection mensagens = new ArrayList();

	private String comentario = "";

	public Decisao() {
	}

	public int obterDecisao() {
		return decisao;
	}

	public Collection obterMensagens() {
		return this.mensagens;
	}

	public String obterComentario() {
		return this.comentario;
	}

	public String obterDescricao() throws Exception {
		StringBuffer s = new StringBuffer();
		if (this.decisao == DECISAO_NULA)
			s.append("NULA");
		else if (this.decisao == DECISAO_APROVADA)
			s.append("APROVADA");
		else if (this.decisao == DECISAO_REJEITADA)
			s.append("REJEITADA");
		else
			s.append("?");
		s.append(" [");
		for (Iterator i = this.mensagens.iterator(); i.hasNext();) {
			s.append((String) i.next());
			if (i.hasNext())
				s.append(", ");
		}
		s.append("]");
		return s.toString();
	}

	public void atribuirDecisao(int decisao) {
		this.decisao = decisao;
	}

	public void adicionarMensagem(String mensagem) {
		this.mensagens.add(mensagem);
	}

	public void atribuirComentario(String comentario) {
		this.comentario = comentario;
	}

	public void agregar(Decisao avaliacao) {
		if (this.decisao == Decisao.DECISAO_NULA)
			this.decisao = avaliacao.obterDecisao();
		else if (this.decisao == Decisao.DECISAO_APROVADA
				&& avaliacao.obterDecisao() == Decisao.DECISAO_REJEITADA)
			this.decisao = avaliacao.obterDecisao();
		for (Iterator i = avaliacao.obterMensagens().iterator(); i.hasNext();)
			this.adicionarMensagem((String) i.next());
		if (!avaliacao.obterComentario().equals(""))
			this.comentario = this.comentario + "\n"
					+ avaliacao.obterComentario();
	}
}