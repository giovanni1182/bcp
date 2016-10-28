package com.gvs.crm.view;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Properties;

import com.gvs.crm.model.DocumentoProduto;
import com.gvs.crm.model.Entidade;
import com.gvs.crm.model.Memorando;

import infra.control.Action;
import infra.security.User;
import infra.view.Block;
import infra.view.Button;
import infra.view.InputText;
import infra.view.Space;
import infra.view.Table;
import infra.view.View;

public class MemorandoView extends EventoAbstratoView {

	public View execute(User arg0, Locale arg1, Properties arg2)
			throws Exception {
		Memorando memorando = (Memorando) this.obterEvento();

		DocumentoProduto documento = (DocumentoProduto) memorando
				.obterSuperior();

		Table table = new Table(2);

		Block block = new Block(Block.HORIZONTAL);

		table.setNextColSpan(table.getColumns());
		table.setNextHAlign(Table.HALIGN_CENTER);

		String numero = "";

		if (documento.obterNumero() != null)
			numero = documento.obterNumero();

		table.addHeader("MEMORANDO SS.SG Nº " + numero);

		table.setNextColSpan(table.getColumns());
		table.add(new Space());

		table.addHeader("A:");
		table.addHeader(documento.obterOrigem().obterNome());

		Entidade.Atributo cargo = (Entidade.Atributo) documento.obterOrigem()
				.obterAtributo("cargo");
		table.addHeader("");
		if (cargo != null)
			table.add(cargo.obterValor());
		else
			table.addHeader("");

		table.setNextColSpan(table.getColumns());
		table.add(new Space());

		table.addHeader("DE:");
		table.addHeader(documento.obterResponsavel().obterNome());

		Entidade.Atributo cargo2 = (Entidade.Atributo) documento
				.obterResponsavel().obterAtributo("cargo");
		table.addHeader("");
		if (cargo2 != null)
			table.add(cargo2.obterValor());
		else
			table.addHeader("");

		table.setNextColSpan(table.getColumns());
		table.add(new Space());

		table.addHeader("MOTIVO:");
		table.addHeader(documento.obterTitulo());

		table.setNextColSpan(table.getColumns());
		table.add(new Space());

		SimpleDateFormat st = new SimpleDateFormat("dd MMM yyyy", Locale
				.getDefault());

		table.addHeader("FECHA:");
		table.addHeader(st.format(documento.obterCriacao()));

		table.setNextColSpan(table.getColumns());
		table.add(new Space());

		Entidade.Atributo descricao = (Entidade.Atributo) documento
				.obterDocumento().obterAtributo("descricao");

		table.setNextColSpan(table.getColumns());

		String descricaoStr = "";

		if (descricao != null)
			descricaoStr = descricao.obterValor();

		if (memorando.obterDescricao() != null)
			descricaoStr = memorando.obterDescricao();

		table.add(new InputText("descricao", descricaoStr, 9, 100));

		Button atualizarButton = new Button("Actualizar", new Action(
				"atualizarMemorando"));
		atualizarButton.getAction().add("id", memorando.obterId());

		table.addFooter(atualizarButton);

		Button imprimirButton = new Button("Imprimir", new Action(
				"abrirMemorando"));
		imprimirButton.getAction().add("id", memorando.obterId());

		table.addFooter(imprimirButton);

		Button voltarButton = new Button("Volver", new Action(
				"visualizarEvento"));
		voltarButton.getAction().add("id", memorando.obterSuperior().obterId());

		table.addFooter(voltarButton);

		return table;
	}
}