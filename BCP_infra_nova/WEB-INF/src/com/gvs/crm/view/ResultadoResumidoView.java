package com.gvs.crm.view;

import java.util.Locale;
import java.util.Properties;

import com.gvs.crm.model.Aseguradora;
import com.gvs.crm.model.CRMModelManager;
import com.gvs.crm.model.ClassificacaoContas;
import com.gvs.crm.model.Conta;
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

public class ResultadoResumidoView extends BasicView {

	private Aseguradora aseguradora;

	private boolean listar;

	private String mesAno;

	private String mes;

	private String ano;

	public ResultadoResumidoView(Aseguradora aseguradora, boolean listar,
			String mes, String ano) throws Exception {
		this.aseguradora = aseguradora;
		this.listar = listar;
		this.mes = mes;
		this.ano = ano;
		this.mesAno = mes + ano;
	}

	public View execute(User user, Locale arg1, Properties arg2)
			throws Exception {
		CRMModelManager mm = new CRMModelManager(user);
		EntidadeHome home = (EntidadeHome) mm.getHome("EntidadeHome");

		Table table = new Table(3);
		table.setWidth("100%");
		table.addStyle(Table.STYLE_ALTERNATE);

		table.addSubtitle("Visualizar Relatório");

		Block block = new Block(Block.HORIZONTAL);

		block.add(new Label("Mes:"));
		block.add(new Space());
		block.add(new InputString("mes", this.mes, 2));
		block.add(new Space(2));
		block.add(new Label("Año:"));
		block.add(new Space());
		block.add(new InputString("ano", this.ano, 4));
		block.add(new Space(2));

		Button visualizarButton = new Button("Visualizar", new Action(
				"visualizarRelatorioAseguradora"));
		visualizarButton.getAction().add("lista", true);
		visualizarButton.getAction().add("id", this.aseguradora.obterId());
		//visualizarButton.getAction().add("_tipo", "normal");

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
		 * block2.add(new InputDate("dataDe", null)); block2.add(new Space(2));
		 * block2.add(new Label("hasta:")); block2.add(new Space());
		 * block2.add(new InputDate("dataAte", null)); block2.add(new Space(2));
		 * 
		 * Button visualizarButton2 = new Button("Visualizar", new
		 * Action("visualizarRelatorioAseguradora"));
		 * visualizarButton2.getAction().add("lista", true);
		 * visualizarButton2.getAction().add("_tipo", "acumulado");
		 * visualizarButton2.getAction().add("id", this.aseguradora.obterId());
		 * block2.add(visualizarButton2); block2.add(new Space(2));
		 * block2.add(new Check("acumular", "True", false)); block2.add(new
		 * Space(2)); block2.add(new Label("Acumular por Aseguradora"));
		 * 
		 * table.setNextColSpan(table.getColumns()); table.add(block2);
		 * table.setNextColSpan(table.getColumns()); table.addData(new Space());
		 */

		if (listar) {
			if (this.mesAno.length() == 5)
				this.mesAno = "0" + this.mesAno;

			table.add("");
			table.setNextHAlign(Table.HALIGN_CENTER);
			table.addHeader("Año Actual");
			table.setNextHAlign(Table.HALIGN_CENTER);
			table.addHeader("Año Anterior");

			table.setNextColSpan(table.getColumns());
			table.addHeader("INGRESOS TÉCNICOS DE PRODUCCIÓN");

			ClassificacaoContas ingresso1 = (ClassificacaoContas) home.obterEntidadePorApelido("0401000000");
			ClassificacaoContas ingresso2 = (ClassificacaoContas) home.obterEntidadePorApelido("0402000000");
			ClassificacaoContas ingresso3 = (ClassificacaoContas) home.obterEntidadePorApelido("0403000000");
			ClassificacaoContas ingresso4 = (ClassificacaoContas) home.obterEntidadePorApelido("0404000000");

			double valorIngresso1 = ingresso1.obterTotalizacaoExistente(aseguradora, this.mesAno);
			double valorAnteriorIngresso1 = ingresso1.obterTotalizacaoSaldoAnoAnterior(aseguradora, this.mesAno);
			double valorIngresso2 = ingresso2.obterTotalizacaoExistente(aseguradora, this.mesAno);
			double valorAnteriorIngresso2 = ingresso2.obterTotalizacaoSaldoAnoAnterior(aseguradora, this.mesAno);
			double valorIngresso3 = ingresso3.obterTotalizacaoExistente(aseguradora, this.mesAno);
			double valorAnteriorIngresso3 = ingresso3.obterTotalizacaoSaldoAnoAnterior(aseguradora, this.mesAno);
			double valorIngresso4 = ingresso4.obterTotalizacaoExistente(aseguradora, this.mesAno);
			double valorAnteriorIngresso4 = ingresso4.obterTotalizacaoSaldoAnoAnterior(aseguradora, this.mesAno);

			table.add(ingresso1.obterNome());
			table.setNextHAlign(Table.HALIGN_RIGHT);
			table.add(new Label(valorIngresso1, "#,##0.00"));
			table.setNextHAlign(Table.HALIGN_RIGHT);
			table.add(new Label(valorAnteriorIngresso1, "#,##0.00"));

			table.add(ingresso2.obterNome());
			table.setNextHAlign(Table.HALIGN_RIGHT);
			table.add(new Label(valorIngresso2 + valorIngresso3, "#,##0.00"));
			table.setNextHAlign(Table.HALIGN_RIGHT);
			table.add(new Label(valorAnteriorIngresso2	+ valorAnteriorIngresso3, "#,##0.00"));

			table.add(ingresso4.obterNome());
			table.setNextHAlign(Table.HALIGN_RIGHT);
			table.add(new Label(valorIngresso4, "#,##0.00"));
			table.setNextHAlign(Table.HALIGN_RIGHT);
			table.add(new Label(valorAnteriorIngresso4, "#,##0.00"));

			table.setNextColSpan(table.getColumns());
			table.addHeader("EGRESOS TÉCNICOS DE PRODUCCIÓN");

			ClassificacaoContas ingresso5 = (ClassificacaoContas) home
					.obterEntidadePorApelido("0501000000");
			ClassificacaoContas ingresso6 = (ClassificacaoContas) home
					.obterEntidadePorApelido("0502000000");
			ClassificacaoContas ingresso7 = (ClassificacaoContas) home
					.obterEntidadePorApelido("0503000000");

			double valorIngresso5 = ingresso5.obterTotalizacaoExistente(aseguradora, this.mesAno);
			double valorAnteriorIngresso5 = ingresso5.obterTotalizacaoSaldoAnoAnterior(aseguradora, this.mesAno);
			double valorIngresso6 = ingresso6.obterTotalizacaoExistente(aseguradora, this.mesAno);
			double valorAnteriorIngresso6 = ingresso6.obterTotalizacaoSaldoAnoAnterior(aseguradora, this.mesAno);
			double valorIngresso7 = ingresso7.obterTotalizacaoExistente(aseguradora, this.mesAno);
			double valorAnteriorIngresso7 = ingresso7.obterTotalizacaoSaldoAnoAnterior(aseguradora, this.mesAno);

			table.add(ingresso5.obterNome());
			table.setNextHAlign(Table.HALIGN_RIGHT);
			table.add(new Label(valorIngresso5 + valorIngresso6, "#,##0.00"));
			table.setNextHAlign(Table.HALIGN_RIGHT);
			table.add(new Label(valorAnteriorIngresso5+ valorAnteriorIngresso6, "#,##0.00"));

			table.add(ingresso7.obterNome());
			table.setNextHAlign(Table.HALIGN_RIGHT);
			table.add(new Label(valorIngresso7, "#,##0.00"));
			table.setNextHAlign(Table.HALIGN_RIGHT);
			table.add(new Label(valorAnteriorIngresso7, "#,##0.00"));
			
			//		********************************
			
			double totalPrimasNetas = (valorIngresso1 + valorIngresso2
					+ valorIngresso3 + valorIngresso4)
					- (valorIngresso5 + valorIngresso6 + valorIngresso7);

			double totalPrimasNetasAnterior = (valorAnteriorIngresso1
					+ valorAnteriorIngresso2 + valorAnteriorIngresso3 + valorAnteriorIngresso4)
					- (valorAnteriorIngresso5 + valorAnteriorIngresso6 + valorAnteriorIngresso7);

			table.addHeader("PRIMAS NETAS GANADAS");
			table.setNextHAlign(Table.HALIGN_RIGHT);
			table.addHeader(new Label((totalPrimasNetas), "#,##0.00"));
			table.setNextHAlign(Table.HALIGN_RIGHT);
			table.addHeader(new Label((totalPrimasNetasAnterior), "#,##0.00"));

			table.addHeader("SINIESTROS");
			table.add("");
			table.add("");

			ClassificacaoContas sinistro1 = (ClassificacaoContas) home
					.obterEntidadePorApelido("0506000000");

			double valorSinistro1 = sinistro1.obterTotalizacaoExistente(aseguradora, this.mesAno);

			double valorAnteriorSinistro1 = sinistro1.obterTotalizacaoSaldoAnoAnterior(aseguradora, this.mesAno);

			table.add(sinistro1.obterNome());
			table.setNextHAlign(Table.HALIGN_RIGHT);
			table.add(new Label(valorSinistro1, "#,##0.00"));
			table.setNextHAlign(Table.HALIGN_RIGHT);
			table.add(new Label(valorAnteriorSinistro1, "#,##0.00"));

			ClassificacaoContas sinistro2 = (ClassificacaoContas) home.obterEntidadePorApelido("0507000000");

			double valorSinistro2 = sinistro2.obterTotalizacaoExistente(aseguradora, this.mesAno);
			double valorAnteriorSinistro2 = sinistro2.obterTotalizacaoSaldoAnoAnterior(aseguradora, this.mesAno);

			table.add(sinistro2.obterNome());
			table.setNextHAlign(Table.HALIGN_RIGHT);
			table.add(new Label(valorSinistro2, "#,##0.00"));
			table.setNextHAlign(Table.HALIGN_RIGHT);
			table.add(new Label(valorAnteriorSinistro2, "#,##0.00"));

			ClassificacaoContas sinistro3 = (ClassificacaoContas) home
					.obterEntidadePorApelido("0508000000");

			double valorSinistro3 = sinistro3.obterTotalizacaoExistente(aseguradora, this.mesAno);

			double valorAnteriorSinistro3 = sinistro3.obterTotalizacaoSaldoAnoAnterior(aseguradora, this.mesAno);

			table.add(sinistro3.obterNome());
			table.setNextHAlign(Table.HALIGN_RIGHT);
			table.add(new Label(valorSinistro3, "#,##0.00"));
			table.setNextHAlign(Table.HALIGN_RIGHT);
			table.add(new Label(valorAnteriorSinistro3, "#,##0.00"));

			ClassificacaoContas sinistro4 = (ClassificacaoContas) home
					.obterEntidadePorApelido("0509000000");
			ClassificacaoContas sinistro5 = (ClassificacaoContas) home
					.obterEntidadePorApelido("0511000000");

			double valorSinistro4 = sinistro4.obterTotalizacaoExistente(aseguradora, this.mesAno);
			double valorAnteriorSinistro4 = sinistro4.obterTotalizacaoSaldoAnoAnterior(aseguradora, this.mesAno);
			double valorSinistro5 = sinistro5.obterTotalizacaoExistente(aseguradora, this.mesAno);
			double valorAnteriorSinistro5 = sinistro5.obterTotalizacaoSaldoAnoAnterior(aseguradora, this.mesAno);

			table.add(sinistro4.obterNome());
			table.setNextHAlign(Table.HALIGN_RIGHT);
			table.add(new Label(valorSinistro4 + valorSinistro5, "#,##0.00"));
			table.setNextHAlign(Table.HALIGN_RIGHT);
			table.add(new Label(valorAnteriorSinistro4	+ valorAnteriorSinistro5, "#,##0.00"));

			ClassificacaoContas sinistro6 = (ClassificacaoContas) home
					.obterEntidadePorApelido("0513000000");
			ClassificacaoContas sinistro7 = (ClassificacaoContas) home
					.obterEntidadePorApelido("0515000000");

			double valorSinistro6 = sinistro6.obterTotalizacaoExistente(aseguradora, this.mesAno);
			double valorAnteriorSinistro6 = sinistro6.obterTotalizacaoSaldoAnoAnterior(aseguradora, this.mesAno);
			double valorSinistro7 = sinistro7.obterTotalizacaoExistente(aseguradora, this.mesAno);
			double valorAnteriorSinistro7 = sinistro7.obterTotalizacaoSaldoAnoAnterior(aseguradora, this.mesAno);

			table.add(sinistro6.obterNome());
			table.setNextHAlign(Table.HALIGN_RIGHT);
			table.add(new Label(valorSinistro6 + valorSinistro7, "#,##0.00"));
			table.setNextHAlign(Table.HALIGN_RIGHT);
			table.add(new Label(valorAnteriorSinistro6	+ valorAnteriorSinistro7, "#,##0.00"));

			ClassificacaoContas sinistro8 = (ClassificacaoContas) home
					.obterEntidadePorApelido("0505000000");

			double valorSinistro8 = sinistro8.obterTotalizacaoExistente(aseguradora, this.mesAno);
			double valorAnteriorSinistro8 = sinistro8.obterTotalizacaoSaldoAnoAnterior(aseguradora, this.mesAno);

			table.add(sinistro8.obterNome());
			table.setNextHAlign(Table.HALIGN_RIGHT);
			table.add(new Label(valorSinistro8, "#,##0.00"));
			table.setNextHAlign(Table.HALIGN_RIGHT);
			table.add(new Label(valorAnteriorSinistro8, "#,##0.00"));

			table.addHeader("RECUPERO DE SINIESTROS");
			table.add("");
			table.add("");

			ClassificacaoContas recuperoSinistro1 = (ClassificacaoContas) home
					.obterEntidadePorApelido("0407000000");

			double valorRecuperoSinistro1 = recuperoSinistro1.obterTotalizacaoExistente(aseguradora, this.mesAno);
			double valorRecuperoAnteriorSinistro1 = recuperoSinistro1.obterTotalizacaoSaldoAnoAnterior(aseguradora, this.mesAno);

			table.add(recuperoSinistro1.obterNome());
			table.setNextHAlign(Table.HALIGN_RIGHT);
			table.add(new Label(valorRecuperoSinistro1, "#,##0.00"));
			table.setNextHAlign(Table.HALIGN_RIGHT);
			table.add(new Label(valorRecuperoAnteriorSinistro1, "#,##0.00"));

			ClassificacaoContas recuperoSinistro2 = (ClassificacaoContas) home
					.obterEntidadePorApelido("0408000000");
			ClassificacaoContas recuperoSinistro3 = (ClassificacaoContas) home
					.obterEntidadePorApelido("0409000000");

			double valorRecuperoSinistro2 = recuperoSinistro2.obterTotalizacaoExistente(aseguradora, this.mesAno);
			double valorRecuperoAnteriorSinistro2 = recuperoSinistro2.obterTotalizacaoSaldoAnoAnterior(aseguradora, this.mesAno);
			double valorRecuperoSinistro3 = recuperoSinistro3.obterTotalizacaoExistente(aseguradora, this.mesAno);
			double valorRecuperoAnteriorSinistro3 = recuperoSinistro3.obterTotalizacaoSaldoAnoAnterior(aseguradora, this.mesAno);

			table.add(recuperoSinistro2.obterNome());
			table.setNextHAlign(Table.HALIGN_RIGHT);
			table.add(new Label(valorRecuperoSinistro2
							+ valorRecuperoSinistro3, "#,##0.00"));
			table.setNextHAlign(Table.HALIGN_RIGHT);
			table.add(new Label(valorRecuperoAnteriorSinistro2
					+ valorRecuperoAnteriorSinistro3, "#,##0.00"));

			ClassificacaoContas recuperoSinistro4 = (ClassificacaoContas) home
					.obterEntidadePorApelido("0412000000");
			ClassificacaoContas recuperoSinistro5 = (ClassificacaoContas) home
					.obterEntidadePorApelido("0414000000");

			double valorRecuperoSinistro4 = recuperoSinistro4.obterTotalizacaoExistente(aseguradora, this.mesAno);
			double valorRecuperoAnteriorSinistro4 = recuperoSinistro4.obterTotalizacaoSaldoAnoAnterior(aseguradora, this.mesAno);
			double valorRecuperoSinistro5 = recuperoSinistro5.obterTotalizacaoExistente(aseguradora, this.mesAno);
			double valorRecuperoAnteriorSinistro5 = recuperoSinistro5.obterTotalizacaoSaldoAnoAnterior(aseguradora, this.mesAno);

			table.add(recuperoSinistro4.obterNome());
			table.setNextHAlign(Table.HALIGN_RIGHT);
			table
					.add(new Label(valorRecuperoSinistro4
							+ valorRecuperoSinistro5, "#,##0.00"));
			table.setNextHAlign(Table.HALIGN_RIGHT);
			table.add(new Label(valorRecuperoAnteriorSinistro4
					+ valorRecuperoAnteriorSinistro5, "#,##0.00"));

			ClassificacaoContas recuperoSinistro6 = (ClassificacaoContas) home
					.obterEntidadePorApelido("0406000000");

			double valorRecuperoSinistro6 = recuperoSinistro6.obterTotalizacaoExistente(aseguradora, this.mesAno);
			double valorRecuperoAnteriorSinistro6 = recuperoSinistro6.obterTotalizacaoSaldoAnoAnterior(aseguradora, this.mesAno);

			table.add(recuperoSinistro6.obterNome());
			table.setNextHAlign(Table.HALIGN_RIGHT);
			table.add(new Label(valorRecuperoSinistro6, "#,##0.00"));
			table.setNextHAlign(Table.HALIGN_RIGHT);
			table.add(new Label(valorRecuperoAnteriorSinistro6, "#,##0.00"));

			double totalSinistro = valorSinistro1 + valorSinistro2
					+ valorSinistro3 + valorSinistro4 + valorSinistro5
					+ valorSinistro6 + valorSinistro7 + valorSinistro8;
			
			double totalSinistroAnterior = valorAnteriorSinistro1
					+ valorAnteriorSinistro2 + valorAnteriorSinistro3
					+ valorAnteriorSinistro4 + valorAnteriorSinistro5
					+ valorAnteriorSinistro6 + valorAnteriorSinistro7
					+ valorAnteriorSinistro8;
			
			double totalRecuperoSinistro = valorRecuperoSinistro1
					+ valorRecuperoSinistro2 + valorRecuperoSinistro3
					+ valorRecuperoSinistro4 + valorRecuperoSinistro5
					+ valorRecuperoSinistro6;
			
			double totalRecuperoSinistroAnterior = valorRecuperoAnteriorSinistro1
					+ valorRecuperoAnteriorSinistro2
					+ valorRecuperoAnteriorSinistro3
					+ valorRecuperoAnteriorSinistro4
					+ valorRecuperoAnteriorSinistro5
					+ valorRecuperoAnteriorSinistro6;
			
			double totalSinistroNeto = totalSinistro - totalRecuperoSinistro;
			
			double totalSinistroNetoAnterior = totalSinistroAnterior
					- totalRecuperoSinistroAnterior;

			table.addHeader("SINIESTROS NETOS OCURRIDOS");
			table.setNextHAlign(Table.HALIGN_RIGHT);
			table.addHeader(new Label(totalSinistro - totalRecuperoSinistro,
					"#,##0.00"));
			table.setNextHAlign(Table.HALIGN_RIGHT);
			table.addHeader(new Label(totalSinistroAnterior
					- totalRecuperoSinistroAnterior, "#,##0.00"));

			double totalUtilidade = totalPrimasNetas - totalSinistroNeto;

			double totalUtilidadeAnterior = totalPrimasNetasAnterior
					- totalSinistroNetoAnterior;

			table.addHeader("UTILIDAD / PÉRDIDA TÉCNICA BRUTA");
			table.setNextHAlign(Table.HALIGN_RIGHT);
			table.addHeader(new Label(totalUtilidade, "#,##0.00"));
			table.setNextHAlign(Table.HALIGN_RIGHT);
			table.addHeader(new Label(totalUtilidadeAnterior, "#,##0.00"));

			table.addHeader("OTROS INGRESOS TÉCNICOS");
			table.add("");
			table.add("");

			ClassificacaoContas ingressoTecnico1 = (ClassificacaoContas) home
					.obterEntidadePorApelido("0405000000");

			double valorIngressoTecnico1 = ingressoTecnico1.obterTotalizacaoExistente(aseguradora, this.mesAno);
			double valorIngressoAnteriorTecnico1 = ingressoTecnico1.obterTotalizacaoSaldoAnoAnterior(aseguradora, this.mesAno);

			table.add(ingressoTecnico1.obterNome());
			table.setNextHAlign(Table.HALIGN_RIGHT);
			table.add(new Label(valorIngressoTecnico1, "#,##0.00"));
			table.setNextHAlign(Table.HALIGN_RIGHT);
			table.add(new Label(valorIngressoAnteriorTecnico1, "#,##0.00"));

			ClassificacaoContas ingressoTecnico2 = (ClassificacaoContas) home
					.obterEntidadePorApelido("0410000000");
			ClassificacaoContas ingressoTecnico3 = (ClassificacaoContas) home
					.obterEntidadePorApelido("0411000000");

			double valorIngressoTecnico2 = ingressoTecnico2.obterTotalizacaoExistente(aseguradora, this.mesAno);
			double valorIngressoAnteriorTecnico2 = ingressoTecnico2.obterTotalizacaoSaldoAnoAnterior(aseguradora, this.mesAno);
			double valorIngressoTecnico3 = ingressoTecnico3.obterTotalizacaoExistente(aseguradora, this.mesAno);
			double valorIngressoAnteriorTecnico3 = ingressoTecnico3.obterTotalizacaoSaldoAnoAnterior(aseguradora, this.mesAno);

			table.add(ingressoTecnico2.obterNome());
			table.setNextHAlign(Table.HALIGN_RIGHT);
			table.add(new Label(valorIngressoTecnico2 + valorIngressoTecnico3,
					"#,##0.00"));
			table.setNextHAlign(Table.HALIGN_RIGHT);
			table.add(new Label(valorIngressoAnteriorTecnico2
					+ valorIngressoAnteriorTecnico3, "#,##0.00"));

			ClassificacaoContas ingressoTecnico4 = (ClassificacaoContas) home
					.obterEntidadePorApelido("0413000000");
			ClassificacaoContas ingressoTecnico5 = (ClassificacaoContas) home
					.obterEntidadePorApelido("0415000000");

			double valorIngressoTecnico4 = ingressoTecnico4.obterTotalizacaoExistente(aseguradora, this.mesAno);
			double valorIngressoAnteriorTecnico4 = ingressoTecnico4.obterTotalizacaoSaldoAnoAnterior(aseguradora, this.mesAno);
			double valorIngressoTecnico5 = ingressoTecnico5.obterTotalizacaoExistente(aseguradora, this.mesAno);
			double valorIngressoAnteriorTecnico5 = ingressoTecnico5.obterTotalizacaoSaldoAnoAnterior(aseguradora, this.mesAno);

			table.add(ingressoTecnico4.obterNome());
			table.setNextHAlign(Table.HALIGN_RIGHT);
			table.add(new Label(valorIngressoTecnico4 + valorIngressoTecnico5,
					"#,##0.00"));
			table.setNextHAlign(Table.HALIGN_RIGHT);
			table.add(new Label(valorIngressoAnteriorTecnico4
					+ valorIngressoAnteriorTecnico5, "#,##0.00"));

			ClassificacaoContas ingressoTecnico6 = (ClassificacaoContas) home
					.obterEntidadePorApelido("0426000000");

			double valorIngressoTecnico6 = ingressoTecnico6.obterTotalizacaoExistente(aseguradora, this.mesAno);
			double valorIngressoAnteriorTecnico6 = ingressoTecnico6.obterTotalizacaoSaldoAnoAnterior(aseguradora, this.mesAno);

			table.add(ingressoTecnico6.obterNome());
			table.setNextHAlign(Table.HALIGN_RIGHT);
			table.add(new Label(valorIngressoTecnico6, "#,##0.00"));
			table.setNextHAlign(Table.HALIGN_RIGHT);
			table.add(new Label(valorIngressoAnteriorTecnico6, "#,##0.00"));

			table.addHeader("OTROS EGRESOS TÉCNICOS");
			table.add("");
			table.add("");

			ClassificacaoContas egressoTecnico1 = (ClassificacaoContas) home
					.obterEntidadePorApelido("0504000000");

			double valorEgressoTecnico1 = egressoTecnico1.obterTotalizacaoExistente(aseguradora, this.mesAno);
			double valorEgressoAnteriorTecnico1 = egressoTecnico1.obterTotalizacaoSaldoAnoAnterior(aseguradora, this.mesAno);

			table.add(egressoTecnico1.obterNome());
			table.setNextHAlign(Table.HALIGN_RIGHT);
			table.add(new Label(valorEgressoTecnico1, "#,##0.00"));
			table.setNextHAlign(Table.HALIGN_RIGHT);
			table.add(new Label(valorEgressoAnteriorTecnico1, "#,##0.00"));

			ClassificacaoContas egressoTecnico2 = (ClassificacaoContas) home
					.obterEntidadePorApelido("0510000000");
			ClassificacaoContas egressoTecnico3 = (ClassificacaoContas) home
					.obterEntidadePorApelido("0512000000");

			double valorEgressoTecnico2 = egressoTecnico2.obterTotalizacaoExistente(aseguradora, this.mesAno);
			double valorEgressoAnteriorTecnico2 = egressoTecnico2.obterTotalizacaoSaldoAnoAnterior(aseguradora, this.mesAno);
			double valorEgressoTecnico3 = egressoTecnico3.obterTotalizacaoExistente(aseguradora, this.mesAno);
			double valorEgressoAnteriorTecnico3 = egressoTecnico3.obterTotalizacaoSaldoAnoAnterior(aseguradora, this.mesAno);

			table.add(egressoTecnico2.obterNome());
			table.setNextHAlign(Table.HALIGN_RIGHT);
			table.add(new Label(valorEgressoTecnico2 + valorEgressoTecnico3,
					"#,##0.00"));
			table.setNextHAlign(Table.HALIGN_RIGHT);
			table.add(new Label(valorEgressoAnteriorTecnico2
					+ valorEgressoAnteriorTecnico3, "#,##0.00"));

			ClassificacaoContas egressoTecnico4 = (ClassificacaoContas) home
					.obterEntidadePorApelido("0514000000");
			ClassificacaoContas egressoTecnico5 = (ClassificacaoContas) home
					.obterEntidadePorApelido("0516000000");

			double valorEgressoTecnico4 = egressoTecnico4.obterTotalizacaoExistente(aseguradora, this.mesAno);
			double valorEgressoAnteriorTecnico4 = egressoTecnico4.obterTotalizacaoSaldoAnoAnterior(aseguradora, this.mesAno);
			double valorEgressoTecnico5 = egressoTecnico5.obterTotalizacaoExistente(aseguradora, this.mesAno);
			double valorEgressoAnteriorTecnico5 = egressoTecnico5.obterTotalizacaoSaldoAnoAnterior(aseguradora, this.mesAno);

			table.add(egressoTecnico4.obterNome());
			table.setNextHAlign(Table.HALIGN_RIGHT);
			table.add(new Label(valorEgressoTecnico4 + valorEgressoTecnico5,
					"#,##0.00"));
			table.setNextHAlign(Table.HALIGN_RIGHT);
			table.add(new Label(valorEgressoAnteriorTecnico4
					+ valorEgressoAnteriorTecnico5, "#,##0.00"));

			ClassificacaoContas egressoTecnico6 = (ClassificacaoContas) home.obterEntidadePorApelido("0525000000");

			Conta egressoTecnico7 = (Conta) home.obterEntidadePorApelido("0525010401");

			double valorEgressoTecnico6 = egressoTecnico6.obterTotalizacaoExistente(aseguradora, this.mesAno);
			double valorEgressoAnteriorTecnico6 = egressoTecnico6.obterTotalizacaoSaldoAnoAnterior(aseguradora, this.mesAno);
			double valorEgressoTecnico7 = egressoTecnico7.obterTotalizacaoExistente(aseguradora, this.mesAno);
			double valorEgressoAnteriorTecnico7 = egressoTecnico7.obterTotalizacaoSaldoAnoAnterior(aseguradora, this.mesAno);

			table.add(egressoTecnico6.obterNome());
			table.setNextHAlign(Table.HALIGN_RIGHT);
			//table.add(new Label(valorEgressoTecnico6,"#,##0.00"));
			table.add(new Label(valorEgressoTecnico6 - valorEgressoTecnico7,
					"#,##0.00"));
			table.setNextHAlign(Table.HALIGN_RIGHT);
			table.add(new Label(valorEgressoAnteriorTecnico6
					- valorEgressoAnteriorTecnico7, "#,##0.00"));
			//table.add(new Label(valorEgressoAnteriorTecnico6,"#,##0.00"));

			ClassificacaoContas egressoTecnico8 = (ClassificacaoContas) home
					.obterEntidadePorApelido("0527000000");

			double valorEgressoTecnico8 = egressoTecnico8.obterTotalizacaoExistente(aseguradora, this.mesAno);
			double valorEgressoAnteriorTecnico8 = egressoTecnico8.obterTotalizacaoSaldoAnoAnterior(aseguradora, this.mesAno);

			table.add(egressoTecnico8.obterNome());
			table.setNextHAlign(Table.HALIGN_RIGHT);
			table.add(new Label(valorEgressoTecnico8, "#,##0.00"));
			table.setNextHAlign(Table.HALIGN_RIGHT);
			table.add(new Label(valorEgressoAnteriorTecnico8, "#,##0.00"));

			double totalIngressoTecnico = valorIngressoTecnico1
					+ valorIngressoTecnico2 + valorIngressoTecnico3
					+ valorIngressoTecnico4 + valorIngressoTecnico5
					+ valorIngressoTecnico6;
			
			double totalIngressoTecnicoAnterior = valorIngressoAnteriorTecnico1
					+ valorIngressoAnteriorTecnico2
					+ valorIngressoAnteriorTecnico3
					+ valorIngressoAnteriorTecnico4
					+ valorIngressoAnteriorTecnico5
					+ valorIngressoAnteriorTecnico6;

			double totalEgressoTecnico = valorEgressoTecnico1
					+ valorEgressoTecnico2 + valorEgressoTecnico3
					+ valorEgressoTecnico4 + valorEgressoTecnico5
					+ valorEgressoTecnico6 - valorEgressoTecnico7
					+ valorEgressoTecnico8;
			double totalEgressoTecnicoAnterior = valorEgressoAnteriorTecnico1
					+ valorEgressoAnteriorTecnico2
					+ valorEgressoAnteriorTecnico3
					+ valorEgressoAnteriorTecnico4
					+ valorEgressoAnteriorTecnico5
					+ valorEgressoAnteriorTecnico6
					+ valorEgressoAnteriorTecnico7
					+ valorEgressoAnteriorTecnico8;

			double totalUtilidadeNeta = (totalUtilidade + totalIngressoTecnico)	- totalEgressoTecnico;
			double totalUtilidadeNetaAnterior = (totalUtilidadeAnterior + totalIngressoTecnicoAnterior)
					- totalEgressoTecnicoAnterior;

			table.addHeader("UTILIDAD / PÉRDIDA TÉCNICA NETA");
			table.setNextHAlign(Table.HALIGN_RIGHT);
			table.addHeader(new Label(totalUtilidadeNeta, "#,##0.00"));
			table.setNextHAlign(Table.HALIGN_RIGHT);
			table.addHeader(new Label(totalUtilidadeNetaAnterior, "#,##0.00"));

			ClassificacaoContas inversao1 = (ClassificacaoContas) home
					.obterEntidadePorApelido("0425000000");

			double valorInversao1 = inversao1.obterTotalizacaoExistente(aseguradora, this.mesAno);
			double valorAnteriorInversao1 = inversao1.obterTotalizacaoSaldoAnoAnterior(aseguradora, this.mesAno);

			table.add(inversao1.obterNome());
			table.setNextHAlign(Table.HALIGN_RIGHT);
			table.add(new Label(valorInversao1, "#,##0.00"));
			table.setNextHAlign(Table.HALIGN_RIGHT);
			table.add(new Label(valorAnteriorInversao1, "#,##0.00"));

			ClassificacaoContas inversao2 = (ClassificacaoContas) home
					.obterEntidadePorApelido("0526000000");

			double valorInversao2 = inversao2.obterTotalizacaoExistente(aseguradora, this.mesAno);
			double valorAnteriorInversao2 = inversao2.obterTotalizacaoSaldoAnoAnterior(aseguradora, this.mesAno);

			table.add(inversao2.obterNome());
			table.setNextHAlign(Table.HALIGN_RIGHT);
			table.add(new Label(valorInversao2, "#,##0.00"));
			table.setNextHAlign(Table.HALIGN_RIGHT);
			table.add(new Label(valorAnteriorInversao2, "#,##0.00"));

			double totalInversao = valorInversao1 - valorInversao2;
			double totalInversaoAnterior = valorAnteriorInversao1
					- valorAnteriorInversao2;

			table.addHeader("UTILIDAD / PÉRDIDA NETA SOBRE INVERSIONES");
			table.setNextHAlign(Table.HALIGN_RIGHT);
			table.addHeader(new Label(totalInversao, "#,##0.00"));
			table.setNextHAlign(Table.HALIGN_RIGHT);
			table.addHeader(new Label(totalInversaoAnterior, "#,##0.00"));

			ClassificacaoContas extraordinario1 = (ClassificacaoContas) home
					.obterEntidadePorApelido("0435000000");
			ClassificacaoContas extraordinario2 = (ClassificacaoContas) home
					.obterEntidadePorApelido("0535000000");

			double valorExtraordinario1 = extraordinario1.obterTotalizacaoExistente(aseguradora, this.mesAno);
			double valorAnteriorExtraordinario1 = extraordinario1.obterTotalizacaoSaldoAnoAnterior(aseguradora, this.mesAno);
			double valorExtraordinario2 = extraordinario2.obterTotalizacaoExistente(aseguradora, this.mesAno);
			double valorAnteriorExtraordinario2 = extraordinario2.obterTotalizacaoSaldoAnoAnterior(aseguradora, this.mesAno);

			double totalExtraordinario = valorExtraordinario1
					- valorExtraordinario2;
			double totalExtraordinarioAnterior = valorAnteriorExtraordinario1
					- valorAnteriorExtraordinario2;

			table.addHeader("RESULTADOS EXTRAORDINARIOS (NETOS)");
			table.setNextHAlign(Table.HALIGN_RIGHT);
			table.addHeader(new Label(totalExtraordinario, "#,##0.00"));
			table.setNextHAlign(Table.HALIGN_RIGHT);
			table.addHeader(new Label(totalExtraordinarioAnterior, "#,##0.00"));

			double totalImposta = totalUtilidadeNeta + totalInversao
					+ totalExtraordinario;
			double totalImpostaAnterior = totalUtilidadeNetaAnterior
					+ totalInversaoAnterior + totalExtraordinarioAnterior;

			table.addHeader("UTILIDAD / PÉRDIDA NETA ANTES DE IMPUESTO");
			table.setNextHAlign(Table.HALIGN_RIGHT);
			table.addHeader(new Label(totalImposta, "#,##0.00"));
			table.setNextHAlign(Table.HALIGN_RIGHT);
			table.addHeader(new Label(totalImpostaAnterior, "#,##0.00"));

			table.addHeader(egressoTecnico7.obterNome());
			table.setNextHAlign(Table.HALIGN_RIGHT);
			table.addHeader(new Label(valorEgressoTecnico7, "#,##0.00"));
			table.setNextHAlign(Table.HALIGN_RIGHT);
			table.addHeader(new Label(valorEgressoAnteriorTecnico7,"#,##0.00"));

			table.addHeader("UTILIDAD / PÉRDIDA NETA DEL EJERCICIO");
			table.setNextHAlign(Table.HALIGN_RIGHT);
			table.addHeader(new Label(totalImposta - valorEgressoTecnico7,"#,##0.00"));
			table.setNextHAlign(Table.HALIGN_RIGHT);
			table.addHeader(new Label(totalImpostaAnterior - valorEgressoAnteriorTecnico7, "#,##0.00"));

		}

		return table;
	}
}