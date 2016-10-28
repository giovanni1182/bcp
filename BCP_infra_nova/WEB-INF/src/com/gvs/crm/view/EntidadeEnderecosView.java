package com.gvs.crm.view;

import java.util.Collection;
import java.util.Iterator;

import com.gvs.crm.model.AuxiliarSeguro;
import com.gvs.crm.model.Entidade;
import com.gvs.crm.model.Entidade.Endereco;

import infra.control.Action;
import infra.view.Button;
import infra.view.Image;
import infra.view.Link;
import infra.view.Table;

public class EntidadeEnderecosView extends Table
{
	public EntidadeEnderecosView(Entidade entidade) throws Exception
	{
		super(4);
		//this.setWidth("100%");
		Collection enderecos = entidade.obterEnderecos();
		
		boolean podeAtualizar = false;
		if(entidade instanceof AuxiliarSeguro)
		{
			AuxiliarSeguro aux = (AuxiliarSeguro) entidade;
			podeAtualizar = entidade.permiteAtualizar() || aux.permiteAtualizarEndereco();
		}
		else
			podeAtualizar = entidade.permiteAtualizar();
		
		for (Iterator i = enderecos.iterator(); i.hasNext();)
		{
			Endereco endereco = (Endereco) i.next();
			Table table = new Table(3);
			if (podeAtualizar)
			{
				Action visualizarAction = new Action(
						"visualizarEntidadeEndereco");
				visualizarAction.add("entidadeId", entidade.obterId());
				visualizarAction.add("id", endereco.obterId());
				visualizarAction.add("origemMenuId", entidade.obterId());
				table.add(new Link(new Image("replace.gif"), visualizarAction));

				Action excluirAction = new Action("excluirEntidadeEndereco");
				excluirAction.add("entidadeId", entidade.obterId());
				excluirAction.add("id", endereco.obterId());
				excluirAction
						.setConfirmation("Confirma exclusión del Direccione ?");
				table.add(new Link(new Image("delete.gif"), excluirAction));
			}
			table.addHeader(endereco.obterNome() + ":");
			this.setNextColSpan(this.getColumns());
			this.addHeader(table);
			this.addHeader("Calle:");
			this.addData(endereco.obterRua());
			this.addHeader("Número:");
			this.addData(endereco.obterNumero());
			this.addHeader("Complemento:");
			this.addData(endereco.obterComplemento());
			this.addHeader("Caja Postal:");
			this.addData(endereco.obterCep());
			this.addHeader("Barrio:");
			this.addData(endereco.obterBairro());
			this.addHeader("Ciudad:");
			this.addData(endereco.obterCidade());
			this.addHeader("Departamento:");
			this.addData(endereco.obterEstado());
			this.addHeader("País:");
			this.addData(endereco.obterPais());
		}
		if (podeAtualizar)
		{
			Button novoLink = new Button("[Nueva Dirección]", new Action(
					"novoEntidadeEndereco"));
			novoLink.getAction().add("entidadeId", entidade.obterId());
			novoLink.getAction().add("origemMenuId", entidade.obterId());
			this.setNextColSpan(this.getColumns());
			this.add(novoLink);
		}
	}
}