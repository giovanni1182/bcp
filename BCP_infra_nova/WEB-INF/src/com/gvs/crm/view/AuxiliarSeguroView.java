package com.gvs.crm.view;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.util.Properties;

import com.gvs.crm.component.EntidadePopup;
import com.gvs.crm.component.RamoAuxiliarSelect;
import com.gvs.crm.component.SeparadorLabel;
import com.gvs.crm.model.AuxiliarSeguro;
import com.gvs.crm.model.Entidade;

import infra.control.Action;
import infra.security.User;
import infra.view.Block;
import infra.view.Button;
import infra.view.Image;
import infra.view.InputString;
import infra.view.Label;
import infra.view.Link;
import infra.view.Select;
import infra.view.Space;
import infra.view.Table;
import infra.view.View;

public class AuxiliarSeguroView extends EntidadeAbstrataView {

	private static final int DETALHE = 0;

	private static final int CONTROLE = 1;

	//private static final int PRODUTIVIDADE = 2;
	
	private static final int PRODUCAO = 3;

	public View execute(User user, Locale locale, Properties properties) throws Exception
	{
		AuxiliarSeguro auxiliar = (AuxiliarSeguro) this.obterEntidade();

		boolean incluir = auxiliar.obterId() == 0;

		Table table = new Table(2);
		//table.setWidth("100%");

		int _pasta = Integer.parseInt(properties.getProperty("_pastaAuxiliar",
				"0"));

		if (incluir || _pasta > 3)
			_pasta = DETALHE;

		Link dadosLink = new Link("Detalles", new Action(
				"visualizarDetalhesEntidade"));
		((Label) dadosLink.getCaption()).setBold(_pasta == DETALHE);
		dadosLink.getAction().add("id", auxiliar.obterId());
		dadosLink.getAction().add("_pastaAuxiliar", DETALHE);
		dadosLink.setEnabled(!incluir);

		Link controleLink = new Link("Control SIS", new Action(
				"visualizarDetalhesEntidade"));
		((Label) controleLink.getCaption()).setBold(_pasta == CONTROLE);
		controleLink.getAction().add("id", auxiliar.obterId());
		controleLink.getAction().add("_pastaAuxiliar", CONTROLE);
		controleLink.setEnabled(!incluir);

		Link produtoLink = new Link("Produción Aseguradoras", new Action("visualizarDetalhesEntidade"));
		((Label) produtoLink.getCaption()).setBold(_pasta == PRODUCAO);
		produtoLink.getAction().add("id", auxiliar.obterId());
		produtoLink.getAction().add("_pastaAuxiliar", PRODUCAO);
		produtoLink.setEnabled(!incluir);

		Block block2 = new Block(Block.HORIZONTAL);
		block2.add(dadosLink);
		block2.add(new SeparadorLabel());
		block2.add(controleLink);
		block2.add(new SeparadorLabel());
		block2.add(produtoLink);
		/*
		 * block2.add(new SeparadorLabel()); block2.add(produtoLink);
		 */

		table.setNextColSpan(table.getColumns());
		table.add(block2);

		if (_pasta == DETALHE) {

			table.addSubtitle("Detalle");

			if (!incluir) {
				table.addHeader("Creado en:");
				String data = new SimpleDateFormat("dd/MM/yyyy")
						.format(auxiliar.obterCriacao());
				table.add(data);
			}

			table.addHeader("Superior:");

			if (incluir)
				table.add(new EntidadePopup("entidadeSuperiorId",
						"superiorNome", auxiliar.obterSuperior(), true));
			else
				table.add(new EntidadePopup("entidadeSuperiorId",
						"superiorNome", auxiliar.obterSuperior(), auxiliar
								.permiteAtualizar()));

			table.addHeader("Responsable:");
			if (incluir)
				table.add(new EntidadePopup("responsavelId", "responsavelNome",
						auxiliar.obterResponsavel(), "Usuario", true));
			else
				table.add(new EntidadePopup("responsavelId", "responsavelNome",
						auxiliar.obterResponsavel(), "Usuario", auxiliar
								.permiteAtualizar()));

			table.addHeader("Aseguradora:");
			if (incluir)
				table.add(new EntidadePopup("aseguradoraId", "aseguradoraNome",
						auxiliar.obterAseguradora(), "Aseguradora", true));
			else if (auxiliar.obterAseguradora() != null)
				table.add(new EntidadePopup("aseguradoraId", "aseguradoraNome",
						auxiliar.obterAseguradora(), "Aseguradora", auxiliar
								.permiteAtualizar()));
			else
				table.add(new EntidadePopup("aseguradoraId", "aseguradoraNome",
						auxiliar.obterAseguradora(), "Aseguradora", true));

			table.addHeader("Nombre:");
			table.add(new InputString("nome", auxiliar.obterNome(), 60));

			Entidade.Atributo nomeAbreviado = (Entidade.Atributo) auxiliar
					.obterAtributo("nomeabreviado");

			String nomeAbreviadoStr = "";
			if (nomeAbreviado != null)
				nomeAbreviadoStr = nomeAbreviado.obterValor();

			table.addHeader("Denominácion:");
			table.add(new InputString("atributo_nomeabreviado",
					nomeAbreviadoStr, 15));

			Entidade.Atributo nacionalidade = (Entidade.Atributo) auxiliar
					.obterAtributo("nacionalidade");

			String nacionalidadeStr = "";
			if (nacionalidade != null)
				nacionalidadeStr = nacionalidade.obterValor();

			table.addHeader("Pais de Origen:");
			table.add(new InputString("atributo_nacionalidade",
					nacionalidadeStr, 30));

			Entidade.Atributo inscricao = (Entidade.Atributo) auxiliar
					.obterAtributo("inscricao");

			String inscricaoStr = "";
			if (inscricao != null)
				inscricaoStr = inscricao.obterValor();

			/*
			 * table.addHeader("Nº de Inscripción:"); table.add(new
			 * InputString("atributo_inscricao", inscricaoStr, 15));
			 */

			table.addHeader("Identificación:");

			Block block = new Block(Block.HORIZONTAL);

			block.add(new InputString("ruc", auxiliar.obterRuc(), 15));
			block.add(new Space());
			block.add(new Label("Cédula de identidad(RUC)"));

			table.add(block);

			Entidade.Atributo atividade = (Entidade.Atributo) auxiliar
					.obterAtributo("atividade");

			table.addHeader("Actividade:");
			Select select = new Select("atributo_atividade", 1);
			select.add("", "", false);
			if (atividade != null) {
				select.add("Agentes de Seguros", "Agentes de Seguros",
						"Agentes de Seguros".equals(atividade.obterValor()));
				select.add("Liquidadores de Siniestros",
						"Liquidadores de Siniestros",
						"Liquidadores de Siniestros".equals(atividade
								.obterValor()));
				select.add("Corredores de Reaseguros",
						"Corredores de Reaseguros", "Corredores de Reaseguros"
								.equals(atividade.obterValor()));
				select.add("Corredores de Seguros", "Corredores de Seguros",
						"Corredores de Seguros".equals(atividade.obterValor()));
				select.add("Auditores Externos", "Auditores Externos",
						"Auditores Externos".equals(atividade.obterValor()));
			} else {
				select.add("Agentes de Seguros", "Agentes de Seguros", false);
				select.add("Liquidadores de Siniestros",
						"Liquidadores de Siniestros", false);
				select.add("Corredores de Reaseguros",
						"Corredores de Reaseguros", false);
				select.add("Corredores de Seguros", "Corredores de Seguros",
						false);
				select.add("Auditores Externos", "Auditores Externos", false);
			}

			table.add(select);

			Entidade.Atributo condicao = (Entidade.Atributo) auxiliar
					.obterAtributo("condicao");

			table.addHeader("Condición:");
			Select select2 = new Select("atributo_condicao", 1);
			select2.add("", "", false);

			if (condicao != null) {
				select2.add("Inscripción", "Inscripción", "Inscripción"
						.equals(condicao.obterValor()));
				select2.add("Renovación", "Renovación", "Renovación"
						.equals(condicao.obterValor()));
			} else {
				select2.add("Inscripción", "Inscripción", false);
				select2.add("Renovación", "Renovación", false);
			}

			table.add(select2);

			Entidade.Atributo situacao = (Entidade.Atributo) auxiliar
					.obterAtributo("situacao");

			table.addHeader("Stuación:");
			if (situacao != null) 
			{
				if (situacao.obterValor() == null) 
				{
					InputString input = new InputString("atributo_situacao", "No Activa", 20);
					input.setEnabled(false);

					table.addHeader(input);
				}
				else
					table.addHeader(situacao.obterValor().toUpperCase());
			} 
			else 
			{
				InputString input = new InputString("atributo_situacao", "No Activa", 20);
				input.setEnabled(false);

				table.addHeader(input);
			}

			table.addHeader("Ramo de la póliza:");

			Block ramoBlock = new Block(Block.HORIZONTAL);

			ramoBlock.add(new RamoAuxiliarSelect("ramo", auxiliar, null));
			ramoBlock.add(new Space(4));
			ramoBlock.add(new InputString("novoRamo", null, 20));
			if (!incluir) {
				ramoBlock.add(new Space(2));

				Button incluirRamoButton = new Button("Agregar Ramo",
						new Action("incluirRamoAuxiliarSeguro"));
				incluirRamoButton.getAction().add("id", auxiliar.obterId());
				incluirRamoButton.setEnabled(auxiliar.permiteAtualizar());

				ramoBlock.add(incluirRamoButton);
			}

			table.add(ramoBlock);
			table.setNextColSpan(table.getColumns());
			table.add(new Space());

			if (auxiliar.obterRamos().size() > 0) 
			{
				table.addHeader("Ramos:");

				Table tableRamo = new Table(2);

				for (Iterator i = auxiliar.obterRamos().iterator(); i.hasNext();) 
				{
					AuxiliarSeguro.Ramo ramo = (AuxiliarSeguro.Ramo) i.next();

					Link link3 = new Link(new Image("delete.gif"), new Action("excluirRamoAuxiliarSeguro"));
					link3.getAction().add("id", auxiliar.obterId());
					link3.getAction().add("seq", ramo.obterSeq());

					if (auxiliar.permiteAtualizar())
						tableRamo.add(link3);
					else
						tableRamo.add("");

					tableRamo.add(ramo.obterRamo());
				}

				table.add(tableRamo);
			}

			table.addSubtitle("Contactos:");
			table.addHeader("Contactos:");
			table.addData(new EntidadeContatosView(auxiliar));

			table.addSubtitle("Direcciones:");
			table.addHeader("Direcciones:");
			table.add(new EntidadeEnderecosView(auxiliar));
			
			if (incluir) {
				Button incluirButton = new Button("Agregar", new Action(
						"incluirAuxiliarSeguro"));

				table.addFooter(incluirButton);
			} 
			else 
			{
				Button atualizarButton = new Button("Actualizar", new Action("atualizarAuxiliarSeguro"));
				atualizarButton.setEnabled(auxiliar.permiteAtualizar());
				atualizarButton.getAction().add("id", auxiliar.obterId());
				table.addFooter(atualizarButton);

				Button excluirButton = new Button("Eliminar", new Action("excluirEntidade"));
				excluirButton.getAction().add("id", auxiliar.obterId());
				excluirButton.setEnabled(auxiliar.permiteExcluir() && auxiliar.permiteAtualizar());
				table.addFooter(excluirButton);

				Button imprimirButton = new Button("Imprimir", new Action("imprimirAuxiliar"));
				imprimirButton.getAction().add("id", auxiliar.obterId());
				table.addFooter(imprimirButton);
			}
		}

		else if (_pasta == CONTROLE)
		{
			table.setWidth("50%");
			table.setNextColSpan(table.getColumns());
			table.addSubtitle("Control SIS");
			table.add(new InscricoesView(auxiliar));
		}
		/*else if (_pasta == PRODUTIVIDADE)
		{
			table.setNextColSpan(table.getColumns());
			table.add(new InscricoesView(auxiliar));
		}*/
		else if(_pasta == PRODUCAO)
		{
			String dataInicioStr = properties.getProperty("_dataInicio", "");
			Date dataInicio = null;
			if(!dataInicioStr.equals(""))
				dataInicio = new SimpleDateFormat("dd/MM/yyyy").parse(dataInicioStr);
			
			String dataFimStr = properties.getProperty("_dataFim", "");
			Date dataFim = null;
			if(!dataFimStr.equals(""))
				dataFim = new SimpleDateFormat("dd/MM/yyyy").parse(dataFimStr);
			
			table.setNextColSpan(table.getColumns());
			table.add(new ProducaoAseguradorasView(auxiliar, dataInicio, dataFim));
		}

		return table;
	}
}