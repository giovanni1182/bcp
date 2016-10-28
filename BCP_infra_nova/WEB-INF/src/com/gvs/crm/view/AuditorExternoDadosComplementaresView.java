package com.gvs.crm.view;

import java.text.SimpleDateFormat;

import com.gvs.crm.model.AuditorExterno;
import com.gvs.crm.model.Entidade;

import infra.control.Action;
import infra.view.Block;
import infra.view.Button;
import infra.view.InputDate;
import infra.view.InputString;
import infra.view.InputText;
import infra.view.Label;
import infra.view.Space;
import infra.view.Table;

public class AuditorExternoDadosComplementaresView extends Table {
	public AuditorExternoDadosComplementaresView(AuditorExterno auditor)
			throws Exception {
		super(2);

		/*
		 * this.addSubtitle("Póliza Contratada - Caución");
		 * 
		 * Entidade.Atributo poliza = (Entidade.Atributo)
		 * auditor.obterAtributo("poliza");
		 * 
		 * this.addHeader("Nº de la Póliza:"); if(poliza!=null) this.add(new
		 * InputString("atributo_poliza", poliza.obterValor(), 20)); else
		 * this.add(new InputString("atributo_poliza", null, 20));
		 * 
		 * Entidade.Atributo secao = (Entidade.Atributo)
		 * auditor.obterAtributo("secao");
		 * 
		 * this.addHeader("Sección:"); if(secao!=null) this.add(new
		 * InputString("atributo_secao", secao.obterValor(), 15)); else
		 * this.add(new InputString("atributo_secao", null, 30));
		 * 
		 * Entidade.Atributo dataInicioVigencia = (Entidade.Atributo)
		 * auditor.obterAtributo("datainiciovigencia");
		 * 
		 * this.addHeader("Fecha de Vigencia:");
		 * 
		 * Block block = new Block(Block.HORIZONTAL);
		 * 
		 * if(dataInicioVigencia!=null &&
		 * !dataInicioVigencia.obterValor().equals("")) block.add(new
		 * InputDate("atributo_datainiciovigencia", new
		 * SimpleDateFormat("dd/MM/yyyy").parse(dataInicioVigencia.obterValor())));
		 * else block.add(new InputDate("atributo_datainiciovigencia", null));
		 * 
		 * block.add(new Space(4));
		 * 
		 * Entidade.Atributo dataFimVigencia = (Entidade.Atributo)
		 * auditor.obterAtributo("datafimvigencia");
		 * 
		 * block.add(new Label("Hasta"));
		 * 
		 * block.add(new Space(4));
		 * 
		 * if(dataFimVigencia!=null && !dataFimVigencia.obterValor().equals(""))
		 * block.add(new InputDate("atributo_datafimvigencia", new
		 * SimpleDateFormat("dd/MM/yyyy").parse(dataFimVigencia.obterValor())));
		 * else block.add(new InputDate("atributo_datafimvigencia", null));
		 * 
		 * this.add(block);
		 * 
		 * this.addHeader("Compañia Aseguradora:");
		 * 
		 * this.add(new EntidadePopup("aseguradoraId", "aseguradoraNome",
		 * auditor.obterAseguradora(), "aseguradora", true));
		 * 
		 * Entidade.Atributo monto = (Entidade.Atributo)
		 * auditor.obterAtributo("monto");
		 * 
		 * this.addHeader("Monto:"); if(monto!=null) this.add(new
		 * InputString("atributo_monto", monto.obterValor(), 15)); else
		 * this.add(new InputString("atributo_monto", null, 30));
		 * 
		 * Entidade.Atributo premio = (Entidade.Atributo)
		 * auditor.obterAtributo("premio");
		 * 
		 * this.addHeader("Premio:"); if(premio!=null) this.add(new
		 * InputString("atributo_premio", premio.obterValor(), 15)); else
		 * this.add(new InputString("atributo_premio", null, 30));
		 * 
		 * Entidade.Atributo numeroEndoso = (Entidade.Atributo)
		 * auditor.obterAtributo("numeroendoso");
		 * 
		 * this.addHeader("Numero Endoso:"); if(numeroEndoso!=null) this.add(new
		 * InputString("atributo_numeroendoso", numeroEndoso.obterValor(), 15));
		 * else this.add(new InputString("atributo_numeroendoso", null, 30));
		 * 
		 * Entidade.Atributo endoso = (Entidade.Atributo)
		 * auditor.obterAtributo("endoso");
		 * 
		 * this.addHeader("Endoso:"); if(endoso!=null) this.add(new
		 * InputString("atributo_endoso", endoso.obterValor(), 15)); else
		 * this.add(new InputString("atributo_endoso", null, 30));
		 * 
		 * Block ramoBlock = new Block(Block.HORIZONTAL); Select ramoSelect =
		 * new Select("ramo", 1);
		 * 
		 * this.addHeader("Ramo:");
		 * 
		 * for(Iterator i = auditor.obterNomeRamos().iterator() ; i.hasNext() ; ) {
		 * String ramo = (String) i.next();
		 * 
		 * ramoSelect.add(ramo, ramo, ramo.equals(auditor.obterRamo())); }
		 * 
		 * ramoBlock.add(ramoSelect); ramoBlock.add(new Space(4));
		 * ramoBlock.add(new InputString("novoRamo", null, 20));
		 * 
		 * this.setNextColSpan(this.getColumns());
		 * 
		 * this.add(ramoBlock);
		 */

		Table table = new Table(2);

		table.addSubtitle("Datos Referentes al Respaldo Internacional");

		Entidade.Atributo corresponsalia = (Entidade.Atributo) auditor
				.obterAtributo("corresponsalia2");

		table.addHeader("Corresponsalia:");
		if (corresponsalia != null)
			table.add(new InputString("atributo_corresponsalia2",
					corresponsalia.obterValor(), 30));
		else
			table.add(new InputString("atributo_corresponsalia2", null, 30));

		Entidade.Atributo representante = (Entidade.Atributo) auditor
				.obterAtributo("representante2");

		table.addHeader("Representaciones:");
		if (representante != null)
			table.add(new InputString("atributo_representante2", representante
					.obterValor(), 30));
		else
			table.add(new InputString("atributo_representante2", null, 30));

		Entidade.Atributo dataInicio = (Entidade.Atributo) auditor
				.obterAtributo("datainicio");

		Entidade.Atributo dataFim = (Entidade.Atributo) auditor
				.obterAtributo("datafim");

		table.addHeader("Fecha Inicio:");

		Block block2 = new Block(Block.HORIZONTAL);

		if (dataInicio != null && !dataInicio.obterValor().equals(""))
			block2.add(new InputDate("atributo_datainicio",
					new SimpleDateFormat("dd/MM/yyyy").parse(dataInicio
							.obterValor())));
		else
			block2.add(new InputDate("atributo_datainicio", null));

		block2.add(new Space(4));

		block2.add(new Label("Finalización:"));

		block2.add(new Space(4));

		if (dataFim != null && !dataFim.obterValor().equals(""))
			block2.add(new InputDate("atributo_datafim", new SimpleDateFormat(
					"dd/MM/yyyy").parse(dataFim.obterValor())));
		else
			block2.add(new InputDate("atributo_datafim", null));

		table.add(block2);

		Entidade.Atributo asociaciones = (Entidade.Atributo) auditor
				.obterAtributo("asociaciones");

		table.addHeader("Asociaciones Profesionales:");
		if (asociaciones != null)
			table.add(new InputText("atributo_asociaciones", asociaciones
					.obterValor(), 4, 80));
		else
			table.add(new InputText("atributo_asociaciones", null, 4, 80));

		this.setNextColSpan(this.getColumns());
		this.add(table);

		Table table2 = new Table(1);

		table2.addSubtitle("Organización de la Firma");

		Entidade.Atributo organizacao = (Entidade.Atributo) auditor
				.obterAtributo("organizacao");

		if (asociaciones != null)
			table2.add(new InputText("atributo_organizacao", organizacao
					.obterValor(), 4, 80));
		else
			table2.add(new InputText("atributo_organizacao", null, 4, 80));

		this.setNextColSpan(this.getColumns());
		this.add(table2);

		Button atualizarButton = new Button("Actualizar", new Action(
				"atualizarDadosComplementares"));
		atualizarButton.getAction().add("id", auditor.obterId());
		atualizarButton.setEnabled(auditor.permiteAtualizar());

		this.addFooter(atualizarButton);
	}
}