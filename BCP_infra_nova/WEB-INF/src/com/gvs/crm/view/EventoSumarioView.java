package com.gvs.crm.view;

import com.gvs.crm.component.DateLabel;
import com.gvs.crm.model.Evento;

import infra.view.Block;
import infra.view.Label;
import infra.view.Space;
import infra.view.Table;

public class EventoSumarioView extends Table {
	public EventoSumarioView(Evento evento) throws Exception {
		super(2);
		this.addSubtitle(evento.obterClasseDescricao());
		this.addHeader("Creado por:");
		Block block = new Block(Block.HORIZONTAL);
		block.add(new Label(evento.obterCriador().obterNome()));
		block.add(new Space());
		block.add(new Label("en"));
		block.add(new Space());
		block.add(new DateLabel(evento.obterCriacao()));
		this.add(block);
		this.addHeader("Nombre:");
		this.addData(evento.obterOrigem().obterNome());
		if (evento.obterTipo() != null && !evento.obterTipo().equals("")) {
			this.addHeader("Tipo:");
			this.addData(evento.obterTipo());
		}
		this.addHeader("Título:");
		this.addHeader(evento.obterTitulo());
		String descricao = evento.obterDescricao();
		if (descricao != null && !descricao.equals("")) {
			this.addHeader("Descripción:");
			this.addData(evento.obterDescricao());
		}
		if (evento.obterDataPrevistaInicio() != null) {
			this.addHeader("Fecha Prevista:");
			Block block2 = new Block(Block.HORIZONTAL);
			block2.add(new Label(evento.obterDataPrevistaInicio(),
					"EEEE dd MMMM yyyy HH:mm"));
			if (evento.obterDataPrevistaConclusao() != null) {
				block2.add(new Space());
				block2.add(new Label("às"));
				block2.add(new Space());
				block2.add(new Label(evento.obterDataPrevistaConclusao(),
						"HH:mm"));
			}
			this.addData(block2);
		}
		if (evento.obterQuantidadeComentarios() > 0) {
			this.addHeader("Comentarios:");
			this.addData(new ComentariosView(evento));
		}
	}
}