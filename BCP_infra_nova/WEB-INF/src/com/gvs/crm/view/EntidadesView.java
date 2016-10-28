package com.gvs.crm.view;

import java.util.Collection;
import java.util.Iterator;

import com.gvs.crm.component.DateLabel;
import com.gvs.crm.component.EntidadeNomeLink;
import com.gvs.crm.model.AuxiliarSeguro;
import com.gvs.crm.model.Entidade;

import infra.view.Block;
import infra.view.Image;
import infra.view.Space;
import infra.view.Table;

public class EntidadesView extends Table {
	public EntidadesView(Collection entidades) throws Exception
	{
		super(6);
		this.setWidth("100%");
		this.addStyle(Table.STYLE_ALTERNATE);
		if (entidades.isEmpty()) {
			this.setNextColSpan(this.getColumns());
			this.setNextHAlign(Table.HALIGN_CENTER);
			this.addData("Ningúna entidad a ser visualizada.");
		} 
		else 
		{
			this.addHeader("");
			this.addHeader("Nombre");
			this.addHeader("Tipo");
			this.addHeader("Responsable");
			this.addHeader("Creación");
			this.addHeader("Actualización");

			for (Iterator i = entidades.iterator(); i.hasNext();) {
				Entidade entidade = (Entidade) i.next();

				/*
				 * if(entidade instanceof Produto) { Produto produto = (Produto)
				 * entidade; this.addData(new Image(entidade.obterIcone()));
				 * this.addData("");
				 * this.addData(entidade.obterDescricaoClasse());
				 * this.addData(entidade.obterResponsavel().obterNome());
				 * this.addData(new DateLabel(entidade.obterCriacao()));
				 * this.addData(new DateLabel(entidade.obterAtualizacao()));
				 * if(!produto.obterInferiores().isEmpty())
				 * this.adicionarInferiores(produto.obterInferiores(),1,this); }
				 * else {
				 */
				this.addData(new Image(entidade.obterIcone()));
				this.addData(new EntidadeNomeLink(entidade));
				if (entidade instanceof AuxiliarSeguro) {
					Entidade.Atributo atributo = (Entidade.Atributo) entidade
							.obterAtributo("atividade");
					if (atributo != null)
						this.addData(atributo.obterValor());
					else
						this.addData("");
				} else
					this.addData(entidade.obterDescricaoClasse());
				this.addData(entidade.obterResponsavel().obterNome());
				this.addData(new DateLabel(entidade.obterCriacao()));
				this.addData(new DateLabel(entidade.obterAtualizacao()));
				//}
			}
		}
		
		entidades.clear();
	}

	private void adicionarInferiores(Collection inferiores, int nivel,
			Table table) throws Exception {
		for (Iterator j = inferiores.iterator(); j.hasNext();) {
			Entidade componente = (Entidade) j.next();
			Block blockComponente = new Block(Block.HORIZONTAL);
			this.add("");
			blockComponente.add(new Space(nivel * 4));
			blockComponente.add(new Image(componente.obterIcone()));
			blockComponente.add(new Space(3));
			blockComponente.add(new EntidadeNomeLink(componente));
			table.add(blockComponente);
			this.add(componente.obterClasse());
			this.add(componente.obterResponsavel().obterNome());
			this.add(new DateLabel(componente.obterCriacao()));
			this.add(new DateLabel(componente.obterAtualizacao()));
		}
	}
}