package com.gvs.crm.view;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Properties;

import com.gvs.crm.model.Ditame;
import com.gvs.crm.model.DocumentoProduto;
import com.gvs.crm.model.Entidade;

import infra.control.Action;
import infra.security.User;
import infra.view.Block;
import infra.view.Button;
import infra.view.InputText;
import infra.view.Label;
import infra.view.Space;
import infra.view.Table;
import infra.view.View;

public class DitameView extends EventoAbstratoView {
	public View execute(User arg0, Locale arg1, Properties arg2)
			throws Exception {
		Ditame ditame = (Ditame) this.obterEvento();

		DocumentoProduto documento = (DocumentoProduto) ditame.obterSuperior();

		Table table = new Table(2);

		Block block = new Block(Block.HORIZONTAL);

		table.setNextColSpan(table.getColumns());
		table.setNextHAlign(Table.HALIGN_CENTER);
		table.addHeader(documento.obterTitulo());

		table.setNextColSpan(table.getColumns());
		table.add(new Space());

		SimpleDateFormat st = new SimpleDateFormat("dd MMM yyyy", Locale
				.getDefault());

		table.setNextColSpan(table.getColumns());
		table.setNextHAlign(Table.HALIGN_RIGHT);
		table.add("Asunción, " + st.format(ditame.obterCriacao()));

		table.setNextColSpan(table.getColumns());
		table.add("Señor");

		Block block2 = new Block(Block.HORIZONTAL);

		Label label = null;
		if (documento.obterSuperIntendente() != null)
			label = new Label(documento.obterSuperIntendente().obterNome());
		else
			label = new Label("");
		label.setBold(true);

		block2.add(label);
		block2.add(new Label(", Superintendente de Seguros"));

		table.setNextColSpan(table.getColumns());
		table.add(block2);

		table.setNextColSpan(table.getColumns());
		table.add(new Space());

		Entidade.Atributo descricao = (Entidade.Atributo) documento
				.obterDocumento().obterAtributo("descricao");

		table.setNextColSpan(table.getColumns());

		String descricaoStr = "";

		if (descricao != null)
			descricaoStr = descricao.obterValor();

		if (ditame.obterDescricao() != null)
			descricaoStr = ditame.obterDescricao();

		table.add(new InputText("descricao", descricaoStr, 9, 100));

		Button atualizarButton = new Button("Actualizar", new Action(
				"atualizarDitame"));
		atualizarButton.getAction().add("id", ditame.obterId());

		table.addFooter(atualizarButton);

		Button imprimirButton = new Button("Imprimir",
				new Action("abrirDitame"));
		imprimirButton.getAction().add("id", ditame.obterId());

		table.addFooter(imprimirButton);

		Button voltarButton = new Button("Volver", new Action(
				"visualizarEvento"));
		voltarButton.getAction().add("id", ditame.obterSuperior().obterId());

		table.addFooter(voltarButton);

		return table;

	}
}