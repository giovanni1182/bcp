package com.gvs.crm.view;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Properties;

import com.gvs.crm.model.DocumentoProduto;
import com.gvs.crm.model.Entidade;
import com.gvs.crm.model.Informe;

import infra.control.Action;
import infra.security.User;
import infra.view.Block;
import infra.view.Button;
import infra.view.InputText;
import infra.view.Label;
import infra.view.Space;
import infra.view.Table;
import infra.view.View;

public class InformeView extends EventoAbstratoView {
	public View execute(User arg0, Locale arg1, Properties arg2)
			throws Exception {
		Informe informe = (Informe) this.obterEvento();

		DocumentoProduto documento = (DocumentoProduto) informe.obterSuperior();

		Table table = new Table(2);

		table.setNextColSpan(table.getColumns());
		table.setNextHAlign(Table.HALIGN_CENTER);
		table.addHeader("INFORME SS.ICORAS.DCAS. Nº " + documento.obterNumero()
				+ " RESERVADO");

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
		table.addHeader(documento.obterTitulo());

		table.setNextColSpan(table.getColumns());
		table.add(new Space());

		Block block = new Block(Block.HORIZONTAL);

		Label label = new Label("Ref.:");
		label.setBold(true);

		block.add(label);
		block.add(new Space(3));
		block.add(new Label(documento.obterReferente()));

		table.add("");
		table.setNextHAlign(Table.HALIGN_RIGHT);
		table.add(block);

		Entidade.Atributo descricao = (Entidade.Atributo) documento
				.obterDocumento().obterAtributo("descricao");

		table.setNextColSpan(table.getColumns());

		String descricaoStr = "";

		if (descricao != null)
			descricaoStr = descricao.obterValor();

		if (informe.obterDescricao() != null)
			descricaoStr = informe.obterDescricao();

		table.add(new InputText("descricao", descricaoStr, 9, 100));

		table.setNextColSpan(table.getColumns());
		table.add(new Space());

		table.setNextHAlign(Table.HALIGN_CENTER);
		if (documento.obterAnalista() != null)
			table.addHeader(documento.obterAnalista().obterNome());
		else
			table.addHeader("Nenhum Analista");

		table.setNextHAlign(Table.HALIGN_CENTER);
		if (documento.obterChefeDivisao() != null)
			table.addHeader(documento.obterChefeDivisao().obterNome());
		else
			table.addHeader("Nenhum Jefe de División");

		Entidade.Atributo cargoAnalista = null;
		Entidade.Atributo cargoChefe = null;

		if (documento.obterAnalista() != null)
			cargoAnalista = (Entidade.Atributo) documento.obterAnalista()
					.obterAtributo("cargo");

		if (documento.obterChefeDivisao() != null)
			cargoChefe = (Entidade.Atributo) documento.obterChefeDivisao()
					.obterAtributo("cargo");

		if (cargoAnalista != null) {
			table.setNextHAlign(Table.HALIGN_CENTER);
			table.add(cargoAnalista.obterValor());
		} else
			table.add("");

		if (cargoChefe != null) {
			table.setNextHAlign(Table.HALIGN_CENTER);
			table.add(cargoChefe.obterValor());
		} else
			table.add("");

		table.setNextColSpan(table.getColumns());
		table.add(new Space());

		table.setNextColSpan(table.getColumns());
		table.setNextHAlign(Table.HALIGN_CENTER);
		table
				.addHeader("Al Señor SUPERINTENDENTE DE SEGUROS, com mil parecer favorable");

		table.setNextColSpan(table.getColumns());
		table.add(new Space());

		table.setNextColSpan(table.getColumns());
		table.setNextHAlign(Table.HALIGN_CENTER);
		table.addHeader("INTENDENCIA DE REASEGUROS Y AUXILIAR DEL SEGURO");

		Entidade.Atributo cargoIntendente = null;

		if (documento.obterIntendente() != null)
			cargoIntendente = (Entidade.Atributo) documento.obterIntendente()
					.obterAtributo("cargo");

		table.setNextColSpan(table.getColumns());
		table.setNextHAlign(Table.HALIGN_CENTER);
		if (cargoIntendente != null)
			table.add(documento.obterIntendente().obterNome() + " - "
					+ cargoIntendente.obterValor());
		else if (documento.obterIntendente() != null)
			table.add(documento.obterIntendente().obterNome());
		else
			table.add("Nenhum Intendente");

		Button atualizarButton = new Button("Actualizar", new Action(
				"atualizarInforme"));
		atualizarButton.getAction().add("id", informe.obterId());

		table.addFooter(atualizarButton);

		Button imprimirButton = new Button("Imprimir", new Action(
				"abrirInforme"));
		imprimirButton.getAction().add("id", informe.obterId());

		table.addFooter(imprimirButton);

		Button voltarButton = new Button("Volver", new Action(
				"visualizarEvento"));
		voltarButton.getAction().add("id", informe.obterSuperior().obterId());

		table.addFooter(voltarButton);

		return table;
	}
}