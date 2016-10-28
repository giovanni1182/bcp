package com.gvs.crm.view;

import java.util.Iterator;
import java.util.Locale;
import java.util.Properties;

import com.gvs.crm.model.Entidade;
import com.gvs.crm.model.Parametro;

import infra.control.Action;
import infra.security.User;
import infra.view.Block;
import infra.view.Button;
import infra.view.Image;
import infra.view.InputDate;
import infra.view.InputInteger;
import infra.view.InputString;
import infra.view.Label;
import infra.view.Link;
import infra.view.Space;
import infra.view.Table;
import infra.view.View;

public class ControleDocumentoView extends PortalView {

	private Parametro.ControleDocumento controle;

	private Parametro parametro;

	public ControleDocumentoView(Parametro parametro) throws Exception {
		this.parametro = parametro;
	}

	public View getBody(User user, Locale locale, Properties properties)
			throws Exception {
		Table table = new Table(1);

		table.addSubtitle("Nuevo Documento");

		table.setWidth("100%");

		Block block = new Block(Block.HORIZONTAL);
		Label label = new Label("Documento:");
		label.setBold(true);

		Label label2 = new Label("Fecha a Presentar:");
		label2.setBold(true);

		block.add(label);
		block.add(new Space(2));
		block.add(new InputString("documento", "", 80));
		block.add(new Space(2));
		block.add(label2);
		block.add(new Space(2));
		block.add(new InputDate("dataLimite", null));
		block.add(new Space(2));

		Button incluirButton = new Button("Agregar", new Action(
				"incluirControleDocumento"));
		incluirButton.getAction().add("id", this.parametro.obterId());

		block.add(incluirButton);

		table.add(block);

		if (parametro.obterControleDocumentos().size() > 0) {
			table.setNextColSpan(table.getColumns());
			table.add(new Space());

			Table table2 = new Table(4);

			table2.addSubtitle("Documentos");
			table2.addStyle(Table.STYLE_ALTERNATE);

			table2.addHeader("");
			table2.addHeader("Nº");
			table2.addHeader("Documento");
			table2.addHeader("Fecha a Presentar");

			for (Iterator i = parametro.obterControleDocumentos().values()
					.iterator(); i.hasNext();) {
				Parametro.ControleDocumento controle = (Parametro.ControleDocumento) i
						.next();

				Link link = new Link(new Image("delete.gif"), new Action(
						"excluirControleDocumento"));
				link.getAction().add("id", parametro.obterId());
				link.getAction().add("seq2", controle.obterSequencial());

				InputInteger inputInteger = new InputInteger("seq", controle
						.obterSequencial(), 4);
				inputInteger.setEnabled(false);

				table2.add(link);
				table2.add(inputInteger);
				table2.add(new InputString("descricao"
						+ controle.obterSequencial(),
						controle.obterDescricao(), 100));
				table2.add(new InputDate("dataLimite"
						+ controle.obterSequencial(), controle
						.obterDataLimite()));
			}

			table.setNextColSpan(table.getColumns());
			table.add(table2);

			Button atualizarButton = new Button("Actualizar", new Action(
					"atualizarControleDocumento"));
			atualizarButton.getAction().add("id", parametro.obterId());

			table.addFooter(atualizarButton);
		}

		Button voltarButton = new Button("Volver", new Action(
				"visualizarDetalhesEntidade"));
		voltarButton.getAction().add("id", parametro.obterId());

		table.addFooter(voltarButton);

		return table;
	}

	public String getSelectedGroup() throws Exception {
		return null;
	}

	public String getSelectedOption() throws Exception {
		return null;
	}

	public View getTitle() throws Exception {
		return new Label("Controle de Documentos");
	}

	public Entidade getOrigemMenu() throws Exception {
		return this.parametro;
	}

}