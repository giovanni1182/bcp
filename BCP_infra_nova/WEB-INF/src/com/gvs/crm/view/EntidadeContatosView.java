package com.gvs.crm.view;

import java.util.Collection;
import java.util.Iterator;

import com.gvs.crm.model.AuxiliarSeguro;
import com.gvs.crm.model.Entidade;

import infra.control.Action;
import infra.view.Block;
import infra.view.Button;
import infra.view.Image;
import infra.view.Link;
import infra.view.Table;

public class EntidadeContatosView extends Table
{
	public EntidadeContatosView(Entidade entidade) throws Exception
	{
		super(4);
		Collection contatos = entidade.obterContatos();
		
		boolean podeAtualizar = false;
		if(entidade instanceof AuxiliarSeguro)
		{
			AuxiliarSeguro aux = (AuxiliarSeguro) entidade;
			podeAtualizar = entidade.permiteAtualizar() || aux.permiteAtualizarEndereco();
		}
		else
			podeAtualizar = entidade.permiteAtualizar();
		
		for (Iterator i = contatos.iterator(); i.hasNext();)
		{
			Entidade.Contato contato = (Entidade.Contato) i.next();
			Block block = new Block(Block.HORIZONTAL);
			if(podeAtualizar)
			{
				Action visualizarAction = new Action("visualizarEntidadeContato");
				visualizarAction.add("entidadeId", entidade.obterId());
				visualizarAction.add("id", contato.obterId());
				visualizarAction.add("origemMenuId", entidade.obterId());
				block.add(new Link(new Image("replace.gif"), visualizarAction));

				Action excluirAction = new Action("excluirEntidadeContato");
				excluirAction.add("entidadeId", entidade.obterId());
				excluirAction.add("id", contato.obterId());
				excluirAction
						.setConfirmation("Confirma exclusión del contacto ?");
				block.add(new Link(new Image("delete.gif"), excluirAction));
			}

			this.addData(block);
			this.addHeader(contato.obterNome() + " : ");
			this.addHeader(contato.obterNomeContato() + " - ");
			this.addData(contato.obterValor());
		}
		if(podeAtualizar)
		{
			Button novoLink = new Button("[Nuevo contacto]", new Action(
					"novoEntidadeContato"));
			novoLink.getAction().add("entidadeId", entidade.obterId());
			novoLink.getAction().add("origemMenuId", entidade.obterId());
			this.setNextColSpan(this.getColumns());
			this.add(novoLink);
		}
	}
}