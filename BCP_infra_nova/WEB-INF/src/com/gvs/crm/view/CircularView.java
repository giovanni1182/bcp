package com.gvs.crm.view;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Properties;

import com.gvs.crm.model.Circular;
import com.gvs.crm.model.DocumentoProduto;
import com.gvs.crm.model.Entidade;

import infra.control.Action;
import infra.security.User;
import infra.view.Block;
import infra.view.Button;
import infra.view.InputText;
import infra.view.Space;
import infra.view.Table;
import infra.view.View;

public class CircularView extends EventoAbstratoView {
	public View execute(User arg0, Locale arg1, Properties arg2)
			throws Exception {
		Circular circular = (Circular) this.obterEvento();

		DocumentoProduto documento = (DocumentoProduto) circular
				.obterSuperior();

		Table table = new Table(2);

		Block block = new Block(Block.HORIZONTAL);

		table.setNextColSpan(table.getColumns());
		table.setNextHAlign(Table.HALIGN_CENTER);

		String numero = "";

		if (documento.obterNumero() != null)
			numero = documento.obterNumero();

		table.addHeader("CIRCULAR SS.SG Nº " + numero);

		table.setNextColSpan(table.getColumns());
		table.add(new Space());

		SimpleDateFormat st = new SimpleDateFormat("dd MMM yyyy", Locale
				.getDefault());

		table.setNextColSpan(table.getColumns());
		table.setNextHAlign(Table.HALIGN_RIGHT);
		table.add("Asunción, " + st.format(documento.obterCriacao()));

		table.setNextColSpan(table.getColumns());
		table.add(new Space());

		table.setNextColSpan(table.getColumns());
		table.addHeader(documento.obterReferente());

		table.setNextColSpan(table.getColumns());
		table.add(new Space());

		Entidade.Atributo descricao = (Entidade.Atributo) documento
				.obterDocumento().obterAtributo("descricao");

		String descricaoStr = "";

		if (descricao != null)
			descricaoStr = descricao.obterValor();

		if (circular.obterDescricao() != null)
			descricaoStr = circular.obterDescricao();

		table.setNextColSpan(table.getColumns());
		table.add(new InputText("descricao", descricaoStr, 9, 100));

		table.setNextColSpan(table.getColumns());
		table.add(new Space());
		table.setNextColSpan(table.getColumns());
		table.add(new Space());
		table.setNextColSpan(table.getColumns());
		table.add(new Space());

		Entidade.Atributo cargo = (Entidade.Atributo) documento.obterOrigem()
				.obterAtributo("cargo");

		table.setNextColSpan(table.getColumns());
		table.setNextHAlign(Table.HALIGN_CENTER);
		table.addHeader(documento.obterOrigem().obterNome());

		table.setNextColSpan(table.getColumns());
		table.setNextHAlign(Table.HALIGN_CENTER);
		if (cargo != null)
			table.add(cargo.obterValor());
		else
			table.add("");

		Button atualizarButton = new Button("Actualizar", new Action(
				"atualizarCircular"));
		atualizarButton.getAction().add("id", circular.obterId());

		table.addFooter(atualizarButton);

		Button imprimirButton = new Button("Imprimir", new Action(
				"abrirCircular"));
		imprimirButton.getAction().add("id", circular.obterId());

		table.addFooter(imprimirButton);

		Button voltarButton = new Button("Volver", new Action(
				"visualizarEvento"));
		voltarButton.getAction().add("id", circular.obterSuperior().obterId());

		table.addFooter(voltarButton);

		return table;
	}
}