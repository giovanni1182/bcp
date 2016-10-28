package com.gvs.crm.view;

import java.util.Iterator;
import java.util.Locale;
import java.util.Properties;

import com.gvs.crm.component.IndicadoresSelect;
import com.gvs.crm.model.Entidade;
import com.gvs.crm.model.Parametro;

import infra.control.Action;
import infra.security.User;
import infra.view.Button;
import infra.view.Check;
import infra.view.Image;
import infra.view.InputInteger;
import infra.view.InputString;
import infra.view.Label;
import infra.view.Link;
import infra.view.Table;
import infra.view.View;

public class IndicadoresView extends PortalView {

	private Parametro parametro;

	public IndicadoresView(Parametro parametro) throws Exception {
		this.parametro = parametro;
	}

	public View getBody(User user, Locale locale, Properties properties)
			throws Exception {
		Table table = new Table(2);

		boolean atualizar = false;

		table.addSubtitle("Nuevo Indicador");

		table.addHeader("Tipo");
		table.add(new IndicadoresSelect("tipo", ""));

		table.addHeader("Descripción");
		table.add(new InputString("descricao", "", 80));

		table.addHeader("Peso");
		table.add(new InputInteger("peso", 0, 3));

		table.addHeader("Excludente");
		table.add(new Check("excludente", "Sim", false));

		Button incluirButton = new Button("Agregar", new Action(
				"incluirIndicador"));
		incluirButton.getAction().add("id", this.parametro.obterId());

		table.setNextColSpan(table.getColumns());
		table.setNextHAlign(Table.HALIGN_CENTER);
		table.add(incluirButton);

		if (parametro.obterIndicadores("Auditoria Externa").size() > 0) {
			Table table2 = new Table(5);

			InputString inputStrng = new InputString("tipo2",
					"Auditoria Externa", 20);
			inputStrng.setEnabled(false);

			table2.addSubtitle(inputStrng);

			table2.setWidth("100%");
			table2.addStyle(Table.STYLE_ALTERNATE);

			table2.addHeader("");
			table2.addHeader("Nº");
			table2.addHeader("Descripción");
			table2.addHeader("Peso");
			table2.addHeader("Excludente");

			for (Iterator i = parametro.obterIndicadores("Auditoria Externa")
					.values().iterator(); i.hasNext();) {
				Parametro.Indicador indicador = (Parametro.Indicador) i.next();

				Link link = new Link(new Image("delete.gif"), new Action(
						"excluirIndicador"));
				link.getAction().add("id", this.parametro.obterId());
				link.getAction().add("seq", indicador.obterSequencial());
				link.getAction().add("tipo", indicador.obterTipo());

				table2.add(link);

				InputInteger inputInteger = new InputInteger(
						"seqAuditoria Externa", indicador.obterSequencial(), 5);
				inputInteger.setEnabled(false);

				table2.add(inputInteger);

				table2.add(new InputString("descricaoAuditoria Externa"
						+ indicador.obterSequencial(), indicador
						.obterDescricao(), 80));
				table2
						.add(new InputInteger("pesoAuditoria Externa"
								+ indicador.obterSequencial(), indicador
								.obterPeso(), 3));
				if (indicador.eExcludente())
					table2.add("*");
				else
					table2.add("");
			}

			table.setNextColSpan(table.getColumns());
			table.add(table2);

			atualizar = true;
		}

		if (parametro.obterIndicadores("Inspeción").size() > 0) {
			Table table2 = new Table(5);

			InputString inputStrng = new InputString("tipo2", "Inspeción", 20);
			inputStrng.setEnabled(false);

			table2.addSubtitle(inputStrng);

			table2.setWidth("100%");
			table2.addStyle(Table.STYLE_ALTERNATE);

			table2.addHeader("");
			table2.addHeader("Nº");
			table2.addHeader("Descripción");
			table2.addHeader("Peso");
			table2.addHeader("Excludente");

			for (Iterator i = parametro.obterIndicadores("Inspeción").values()
					.iterator(); i.hasNext();) {
				Parametro.Indicador indicador = (Parametro.Indicador) i.next();

				Link link = new Link(new Image("delete.gif"), new Action(
						"excluirIndicador"));
				link.getAction().add("id", this.parametro.obterId());
				link.getAction().add("seq", indicador.obterSequencial());
				link.getAction().add("tipo", indicador.obterTipo());

				table2.add(link);

				InputInteger inputInteger = new InputInteger("seqInspeción",
						indicador.obterSequencial(), 5);
				inputInteger.setEnabled(false);

				table2.add(inputInteger);

				table2.add(new InputString("descricaoInspeción"
						+ indicador.obterSequencial(), indicador
						.obterDescricao(), 80));
				table2
						.add(new InputInteger("pesoInspeción"
								+ indicador.obterSequencial(), indicador
								.obterPeso(), 3));
				if (indicador.eExcludente())
					table2.add("*");
				else
					table2.add("");
			}

			table.setNextColSpan(table.getColumns());
			table.add(table2);

			atualizar = true;
		}

		if (parametro.obterIndicadores("Otros Indicadores").size() > 0) {
			Table table2 = new Table(5);

			InputString inputStrng = new InputString("tipo2",
					"Otros Indicadores", 20);
			inputStrng.setEnabled(false);

			table2.addSubtitle(inputStrng);

			table2.setWidth("100%");
			table2.addStyle(Table.STYLE_ALTERNATE);

			table2.addHeader("");
			table2.addHeader("Nº");
			table2.addHeader("Descripción");
			table2.addHeader("Peso");
			table2.addHeader("Excludente");

			for (Iterator i = parametro.obterIndicadores("Otros Indicadores")
					.values().iterator(); i.hasNext();) {
				Parametro.Indicador indicador = (Parametro.Indicador) i.next();

				Link link = new Link(new Image("delete.gif"), new Action(
						"excluirIndicador"));
				link.getAction().add("id", this.parametro.obterId());
				link.getAction().add("seq", indicador.obterSequencial());
				link.getAction().add("tipo", indicador.obterTipo());

				table2.add(link);

				InputInteger inputInteger = new InputInteger(
						"seqOtros Indicadores", indicador.obterSequencial(), 5);
				inputInteger.setEnabled(false);

				table2.add(inputInteger);

				table2.add(new InputString("descricaoOtros Indicadores"
						+ indicador.obterSequencial(), indicador
						.obterDescricao(), 80));
				table2
						.add(new InputInteger("pesoOtros Indicadores"
								+ indicador.obterSequencial(), indicador
								.obterPeso(), 3));
				if (indicador.eExcludente())
					table2.add("*");
				else
					table2.add("");
			}

			table.setNextColSpan(table.getColumns());
			table.add(table2);

			atualizar = true;
		}

		if (parametro.obterIndicadores("Ratios").size() > 0) {
			Table table2 = new Table(5);

			InputString inputStrng = new InputString("tipo2", "Ratios", 20);
			inputStrng.setEnabled(false);

			table2.addSubtitle(inputStrng);

			table2.setWidth("100%");
			table2.addStyle(Table.STYLE_ALTERNATE);

			table2.addHeader("");
			table2.addHeader("Nº");
			table2.addHeader("Descripción");
			table2.addHeader("Peso");
			table2.addHeader("Excludente");

			for (Iterator i = parametro.obterIndicadores("Ratios").values()
					.iterator(); i.hasNext();) {
				Parametro.Indicador indicador = (Parametro.Indicador) i.next();

				Link link = new Link(new Image("delete.gif"), new Action(
						"excluirIndicador"));
				link.getAction().add("id", this.parametro.obterId());
				link.getAction().add("seq", indicador.obterSequencial());
				link.getAction().add("tipo", indicador.obterTipo());

				table2.add(link);

				InputInteger inputInteger = new InputInteger("seqRatios",
						indicador.obterSequencial(), 5);
				inputInteger.setEnabled(false);

				table2.add(inputInteger);

				table2.add(new InputString("descricaoRatios"
						+ indicador.obterSequencial(), indicador
						.obterDescricao(), 80));
				table2
						.add(new InputInteger("pesoRatios"
								+ indicador.obterSequencial(), indicador
								.obterPeso(), 3));
				if (indicador.eExcludente())
					table2.add("*");
				else
					table2.add("");
			}

			table.setNextColSpan(table.getColumns());
			table.add(table2);

			atualizar = true;
		}

		if (atualizar) {
			Button atualizarButton = new Button("Actualizar", new Action(
					"atualizarIndicador"));
			atualizarButton.getAction().add("parametroId",
					this.parametro.obterId());

			table.addFooter(atualizarButton);
		}

		Button voltarButton = new Button("Volver", new Action(
				"visualizarDetalhesEntidade"));
		voltarButton.getAction().add("id", this.parametro.obterId());

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
		return new Label("Avaliación de Indicadores");
	}

	public Entidade getOrigemMenu() throws Exception {
		return this.parametro;
	}

}