package com.gvs.crm.view;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Properties;

import com.gvs.crm.model.Aseguradora;
import com.gvs.crm.model.CRMModelManager;
import com.gvs.crm.model.ClassificacaoContas;
import com.gvs.crm.model.EntidadeHome;

import infra.control.Action;
import infra.security.User;
import infra.view.BasicView;
import infra.view.Block;
import infra.view.Button;
import infra.view.Check;
import infra.view.InputString;
import infra.view.Label;
import infra.view.Space;
import infra.view.Table;
import infra.view.View;

public class BalancoGeralAcumuladoView extends BasicView {

	private Aseguradora aseguradora;

	private boolean listar;

	private Date dataDe;

	private Date dataAte;

	public BalancoGeralAcumuladoView(Aseguradora aseguradora, boolean listar,
			Date dataDe, Date dataAte) throws Exception {
		this.aseguradora = aseguradora;
		this.listar = listar;
		this.dataDe = dataDe;
		this.dataAte = dataAte;
	}

	public View execute(User user, Locale arg1, Properties arg2)
			throws Exception {
		CRMModelManager mm = new CRMModelManager(user);
		EntidadeHome home = (EntidadeHome) mm.getHome("EntidadeHome");

		Table table = new Table(4);
		table.setWidth("100%");
		table.addStyle(Table.STYLE_ALTERNATE);

		table.addSubtitle("Visualizar Relatório");

		Block block = new Block(Block.HORIZONTAL);

		block.add(new Label("Mes:"));
		block.add(new Space());
		block.add(new InputString("mes", null, 2));
		block.add(new Space(2));
		block.add(new Label("Año:"));
		block.add(new Space());
		block.add(new InputString("ano", null, 4));

		//block.add(new MesAnoBalancoSelect("mesAno", this.aseguradora,
		// this.mesAno));
		block.add(new Space(2));
		Button visualizarButton = new Button("Visualizar", new Action(
				"visualizarRelatorioAseguradora"));
		visualizarButton.getAction().add("lista", true);
		visualizarButton.getAction().add("_tipo", "normal");
		visualizarButton.getAction().add("id", this.aseguradora.obterId());

		block.add(new Space(2));
		block.add(new Check("acumular", "True", false));
		block.add(new Space(2));
		block.add(new Label("Acumular por Aseguradora"));
		block.add(new Space(2));
		block.add(visualizarButton);
		table.setNextColSpan(table.getColumns());
		table.add(block);
		table.setNextColSpan(table.getColumns());
		table.addData(new Space());

		/*
		 * Block block2 = new Block(Block.HORIZONTAL);
		 * 
		 * block2.add(new Label("Fecha de:")); block2.add(new Space());
		 * block2.add(new InputDate("dataDe", this.dataDe)); block2.add(new
		 * Space(2)); block2.add(new Label("Fecha atá:")); block2.add(new
		 * Space()); block2.add(new InputDate("dataAte", this.dataAte));
		 * block2.add(new Space(2));
		 * 
		 * Button visualizarButton2 = new Button("Visualizar", new
		 * Action("visualizarRelatorioAseguradora"));
		 * visualizarButton2.getAction().add("lista", true);
		 * visualizarButton2.getAction().add("_tipo", "acumulado");
		 * visualizarButton2.getAction().add("id", this.aseguradora.obterId());
		 * block2.add(visualizarButton2);
		 */
		/*
		 * block2.add(new Space(2)); block2.add(new Check("acumular", "True",
		 * false)); block2.add(new Space(2)); block2.add(new Label("Acumular por
		 * Aseguradora")); table.setNextColSpan(table.getColumns());
		 * table.add(block2); table.setNextColSpan(table.getColumns());
		 */
		table.addData(new Space());

		if (listar) {
			ClassificacaoContas ativo1 = (ClassificacaoContas) home
					.obterEntidadePorApelido("0101000000");
			ClassificacaoContas passivo1 = (ClassificacaoContas) home
					.obterEntidadePorApelido("0201000000");

			ClassificacaoContas ativo2 = (ClassificacaoContas) home
					.obterEntidadePorApelido("0102000000");
			ClassificacaoContas passivo2 = (ClassificacaoContas) home
					.obterEntidadePorApelido("0202000000");

			ClassificacaoContas ativo3 = (ClassificacaoContas) home
					.obterEntidadePorApelido("0103000000");
			ClassificacaoContas passivo3 = (ClassificacaoContas) home
					.obterEntidadePorApelido("0203000000");

			ClassificacaoContas ativo4 = (ClassificacaoContas) home
					.obterEntidadePorApelido("0104000000");
			ClassificacaoContas passivo4 = (ClassificacaoContas) home
					.obterEntidadePorApelido("0204000000");

			ClassificacaoContas ativo5 = (ClassificacaoContas) home
					.obterEntidadePorApelido("0105000000");
			ClassificacaoContas passivo5 = (ClassificacaoContas) home
					.obterEntidadePorApelido("0205000000");

			ClassificacaoContas ativo6 = (ClassificacaoContas) home
					.obterEntidadePorApelido("0106000000");
			ClassificacaoContas passivo6 = (ClassificacaoContas) home
					.obterEntidadePorApelido("0206000000");

			ClassificacaoContas ativo7 = (ClassificacaoContas) home
					.obterEntidadePorApelido("0107000000");
			ClassificacaoContas passivo10 = (ClassificacaoContas) home
					.obterEntidadePorApelido("0210000000");

			ClassificacaoContas ativo8 = (ClassificacaoContas) home
					.obterEntidadePorApelido("0108000000");
			ClassificacaoContas passivo11 = (ClassificacaoContas) home
					.obterEntidadePorApelido("0211000000");

			ClassificacaoContas ativo9 = (ClassificacaoContas) home
					.obterEntidadePorApelido("0109000000");
			ClassificacaoContas passivo12 = (ClassificacaoContas) home
					.obterEntidadePorApelido("0212000000");

			ClassificacaoContas passivo13 = (ClassificacaoContas) home
					.obterEntidadePorApelido("0213000000");

			ClassificacaoContas passivo14 = (ClassificacaoContas) home
					.obterEntidadePorApelido("0214000000");

			ClassificacaoContas patrimonio1 = (ClassificacaoContas) home
					.obterEntidadePorApelido("0301000000");

			ClassificacaoContas patrimonio2 = (ClassificacaoContas) home
					.obterEntidadePorApelido("0302000000");

			ClassificacaoContas patrimonio3 = (ClassificacaoContas) home
					.obterEntidadePorApelido("0303000000");

			ClassificacaoContas patrimonio4 = (ClassificacaoContas) home
					.obterEntidadePorApelido("0304000000");

			Calendar dataDe2 = Calendar.getInstance();
			dataDe2.setTime(this.dataDe);

			double valorAtivo1 = 0;
			double valorPassivo1 = 0;

			double valorAtivo2 = 0;
			double valorPassivo2 = 0;

			double valorAtivo3 = 0;
			double valorPassivo3 = 0;

			double valorAtivo4 = 0;
			double valorPassivo4 = 0;

			double valorAtivo5 = 0;
			double valorPassivo5 = 0;

			double valorAtivo6 = 0;
			double valorPassivo6 = 0;

			double valorAtivo7 = 0;
			double valorPassivo10 = 0;

			double valorAtivo8 = 0;
			double valorPassivo11 = 0;

			double valorAtivo9 = 0;
			double valorPassivo12 = 0;

			double valorPassivo13 = 0;

			double valorPassivo14 = 0;

			double valorPatrimonio1 = 0;

			double valorPatrimonio2 = 0;

			double valorPatrimonio3 = 0;

			double valorPatrimonio4 = 0;

			while (dataDe2.getTime().before(this.dataAte)
					|| dataDe2.getTime().equals(this.dataAte)) {

				String mesAno = new SimpleDateFormat("MMyyyy").format(dataDe2
						.getTime());

				valorAtivo1 += ativo1.obterTotalizacaoExistente(aseguradora,
						mesAno);
				valorPassivo1 += passivo1.obterTotalizacaoExistente(
						aseguradora, mesAno);

				valorAtivo2 += ativo2.obterTotalizacaoExistente(aseguradora,
						mesAno);
				valorPassivo2 += passivo2.obterTotalizacaoExistente(
						aseguradora, mesAno);

				valorAtivo3 += ativo3.obterTotalizacaoExistente(aseguradora,
						mesAno);
				valorPassivo3 += passivo3.obterTotalizacaoExistente(
						aseguradora, mesAno);

				valorAtivo4 += ativo4.obterTotalizacaoExistente(aseguradora,
						mesAno);
				valorPassivo4 += passivo4.obterTotalizacaoExistente(
						aseguradora, mesAno);

				valorAtivo5 += ativo5.obterTotalizacaoExistente(aseguradora,
						mesAno);
				valorPassivo5 += passivo5.obterTotalizacaoExistente(
						aseguradora, mesAno);

				valorAtivo6 += ativo6.obterTotalizacaoExistente(aseguradora,
						mesAno);
				valorPassivo6 += passivo6.obterTotalizacaoExistente(
						aseguradora, mesAno);

				valorAtivo7 += ativo7.obterTotalizacaoExistente(aseguradora,
						mesAno);
				valorPassivo10 += passivo10.obterTotalizacaoExistente(
						aseguradora, mesAno);

				valorAtivo8 += ativo8.obterTotalizacaoExistente(aseguradora,
						mesAno);
				valorPassivo11 += passivo11.obterTotalizacaoExistente(
						aseguradora, mesAno);

				valorAtivo9 += ativo9.obterTotalizacaoExistente(aseguradora,
						mesAno);
				valorPassivo12 += passivo12.obterTotalizacaoExistente(
						aseguradora, mesAno);

				valorPassivo13 += passivo13.obterTotalizacaoExistente(
						aseguradora, mesAno);

				valorPassivo14 += passivo14.obterTotalizacaoExistente(
						aseguradora, mesAno);

				valorPatrimonio1 += patrimonio1.obterTotalizacaoExistente(
						aseguradora, mesAno);

				valorPatrimonio2 += patrimonio2.obterTotalizacaoExistente(
						aseguradora, mesAno);

				valorPatrimonio3 += patrimonio3.obterTotalizacaoExistente(
						aseguradora, mesAno);

				valorPatrimonio4 = patrimonio4.obterTotalizacaoExistente(
						aseguradora, mesAno);

				dataDe2.add(Calendar.MONTH, 1);
			}

			table.setNextHAlign(Table.HALIGN_CENTER);
			table.addHeader("ACTIVO");
			table.setNextHAlign(Table.HALIGN_CENTER);
			table.addHeader("VALOR");
			table.setNextHAlign(Table.HALIGN_CENTER);
			table.addHeader("PASIVO Y PATRIMONIO NETO");
			table.setNextHAlign(Table.HALIGN_CENTER);
			table.addHeader("VALOR");

			table.add(ativo1.obterNome());
			table.setNextHAlign(Table.HALIGN_RIGHT);
			table.add(new Label(valorAtivo1, "#,##0.00"));
			table.add(passivo1.obterNome());
			table.setNextHAlign(Table.HALIGN_RIGHT);
			table.add(new Label(valorPassivo1, "#,##0.00"));

			table.add(ativo2.obterNome());
			table.setNextHAlign(Table.HALIGN_RIGHT);
			table.add(new Label(valorAtivo2, "#,##0.00"));
			table.add(passivo2.obterNome());
			table.setNextHAlign(Table.HALIGN_RIGHT);
			table.add(new Label(valorPassivo2, "#,##0.00"));

			table.add(ativo3.obterNome());
			table.setNextHAlign(Table.HALIGN_RIGHT);
			table.add(new Label(valorAtivo3, "#,##0.00"));
			table.add(passivo3.obterNome());
			table.setNextHAlign(Table.HALIGN_RIGHT);
			table.add(new Label(valorPassivo3, "#,##0.00"));

			table.add(ativo4.obterNome());
			table.setNextHAlign(Table.HALIGN_RIGHT);
			table.add(new Label(valorAtivo4, "#,##0.00"));
			table.add(passivo4.obterNome());
			table.setNextHAlign(Table.HALIGN_RIGHT);
			table.add(new Label(valorPassivo4, "#,##0.00"));

			table.add(ativo5.obterNome());
			table.setNextHAlign(Table.HALIGN_RIGHT);
			table.add(new Label(valorAtivo5, "#,##0.00"));
			table.add(passivo5.obterNome());
			table.setNextHAlign(Table.HALIGN_RIGHT);
			table.add(new Label(valorPassivo5, "#,##0.00"));

			table.add(ativo6.obterNome());
			table.setNextHAlign(Table.HALIGN_RIGHT);
			table.add(new Label(valorAtivo6, "#,##0.00"));
			table.add(passivo6.obterNome());
			table.setNextHAlign(Table.HALIGN_RIGHT);
			table.add(new Label(valorPassivo6, "#,##0.00"));

			table.add(ativo7.obterNome());
			table.setNextHAlign(Table.HALIGN_RIGHT);
			table.add(new Label(valorAtivo7, "#,##0.00"));
			table.add(passivo10.obterNome());
			table.setNextHAlign(Table.HALIGN_RIGHT);
			table.add(new Label(valorPassivo10, "#,##0.00"));

			table.add(ativo8.obterNome());
			table.setNextHAlign(Table.HALIGN_RIGHT);
			table.add(new Label(valorAtivo8, "#,##0.00"));
			table.add(passivo11.obterNome());
			table.setNextHAlign(Table.HALIGN_RIGHT);
			table.add(new Label(valorPassivo11, "#,##0.00"));

			table.add(ativo9.obterNome());
			table.setNextHAlign(Table.HALIGN_RIGHT);
			table.add(new Label(valorAtivo9, "#,##0.00"));
			table.add(passivo12.obterNome());
			table.setNextHAlign(Table.HALIGN_RIGHT);
			table.add(new Label(valorPassivo12, "#,##0.00"));

			table.add("");
			table.add("");
			table.add(passivo13.obterNome());
			table.setNextHAlign(Table.HALIGN_RIGHT);
			table.add(new Label(valorPassivo13, "#,##0.00"));

			table.add("");
			table.add("");
			table.add(passivo14.obterNome());
			table.setNextHAlign(Table.HALIGN_RIGHT);
			table.add(new Label(valorPassivo14, "#,##0.00"));

			double totalAtivo = valorAtivo1 + valorAtivo2 + valorAtivo3
					+ valorAtivo4 + valorAtivo5 + valorAtivo6 + valorAtivo7
					+ valorAtivo8 + valorAtivo9;

			double totalPassivo = valorPassivo1 + valorPassivo2 + valorPassivo3
					+ valorPassivo4 + valorPassivo5 + valorPassivo6
					+ valorPassivo10 + valorPassivo11 + valorPassivo12
					+ valorPassivo13 + valorPassivo14;

			table.setNextColSpan(table.getColumns());
			table.add(new Space());

			table.add("");
			table.add("");
			table.addHeader("PASIVO TOTAL");
			table.setNextHAlign(Table.HALIGN_RIGHT);
			table.addHeader(new Label(totalPassivo, "#,##0.00"));

			table.setNextColSpan(table.getColumns());
			table.add(new Space());

			table.add("");
			table.add("");
			table.setNextHAlign(Table.HALIGN_CENTER);
			table.addHeader("PATRIMONIO NETO");
			table.add("");

			table.add("");
			table.add("");
			table.add(patrimonio1.obterNome());
			table.setNextHAlign(Table.HALIGN_RIGHT);
			table.addHeader(new Label(valorPatrimonio1, "#,##0.00"));

			table.add("");
			table.add("");
			table.add(patrimonio2.obterNome());
			table.setNextHAlign(Table.HALIGN_RIGHT);
			table.addHeader(new Label(valorPatrimonio2, "#,##0.00"));

			table.add("");
			table.add("");
			table.add(patrimonio3.obterNome());
			table.setNextHAlign(Table.HALIGN_RIGHT);
			table.addHeader(new Label(valorPatrimonio3, "#,##0.00"));

			table.add("");
			table.add("");
			table.add(patrimonio4.obterNome());
			table.setNextHAlign(Table.HALIGN_RIGHT);
			table.addHeader(new Label(valorPatrimonio4, "#,##0.00"));

			double totalPatrimonio = valorPatrimonio1 + valorPatrimonio2
					+ valorPatrimonio3 + valorPatrimonio4;

			double totalExercicio = totalAtivo
					- (totalPassivo + totalPatrimonio);
			
			System.out.println("totalAtivo BG: " + totalAtivo);
			System.out.println("totalPassivo BG: " + totalPassivo);
			System.out.println("totalPatrimonio BG: " + totalPatrimonio);
			System.out.println("totalExercicio BG: " + totalExercicio);

			table.add("");
			table.add("");
			table.add("RESULTADO DEL EJERCICIO");
			table.setNextHAlign(Table.HALIGN_RIGHT);
			table.addHeader(new Label(totalExercicio, "#,##0.00"));

			table.setNextColSpan(table.getColumns());
			table.add(new Space());

			double totalPatrimonioNeto = totalPatrimonio + totalExercicio;
			
			System.out.println("totalPatrimonioNeto BG: " + totalPatrimonioNeto);

			table.add("");
			table.add("");
			table.addHeader("TOTAL PATRIMONIO NETO");
			table.setNextHAlign(Table.HALIGN_RIGHT);
			table.addHeader(new Label(totalPatrimonioNeto, "#,##0.00"));

			table.setNextColSpan(table.getColumns());
			table.add(new Space());

			table.addHeader("TOTAL ACTIVO");
			table.setNextHAlign(Table.HALIGN_RIGHT);
			table.addHeader(new Label(totalAtivo, "#,##0.00"));
			table.addHeader("TOTAL PASIVO E PATRIMONIO NETO");
			table.setNextHAlign(Table.HALIGN_RIGHT);

			table.addHeader(new Label(totalPassivo + totalPatrimonioNeto,
					"#,##0.00"));
		}

		return table;
	}
}