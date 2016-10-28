package com.gvs.crm.view;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Locale;
import java.util.Properties;

import com.gvs.crm.component.EntidadePopup;
import com.gvs.crm.component.SeparadorLabel;
import com.gvs.crm.model.Aseguradora;
import com.gvs.crm.model.CRMModelManager;
import com.gvs.crm.model.Entidade;
import com.gvs.crm.model.EventoHome;
import com.gvs.crm.model.MeicosAseguradora;
import com.gvs.crm.model.Usuario;
import com.gvs.crm.model.UsuarioHome;

import infra.control.Action;
import infra.security.User;
import infra.view.Block;
import infra.view.Button;
import infra.view.InputString;
import infra.view.Label;
import infra.view.Link;
import infra.view.Select;
import infra.view.Space;
import infra.view.Table;
import infra.view.View;

public class AseguradoraView extends EntidadeAbstrataView {
	private static final int DETALHE = 0;

	private static final int PERSONAS = 1;
	private static final int SUCURSALES = 2;
	private static final int FUSIONES = 3;
	private static final int ACCIONISTAS = 4;
	private static final int REASEGURADORA = 5;
	private static final int PLANES = 6;
	private static final int COASEGURADOR = 7;
	private static final int CONTROLE = 8;
	private static final int APOLICES = 9;
	private static final int MEICOS = 10;
	private static final int RATIOS = 11;
	private static final int CIRUC = 12;
	private static final int PRODUCAO = 13;
	
	//private static final int MARGEM_SOLVENCIA = 13;

	public View execute(User user, Locale locale, Properties properties)throws Exception 
	{
		CRMModelManager mm = new CRMModelManager(user);
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Usuario usuarioAtual = usuarioHome.obterUsuarioPorUser(user);
		
		Aseguradora aseguradora = (Aseguradora) this.obterEntidade();

		boolean incluir = aseguradora.obterId() == 0;

		Table mainTable = new Table(1);
		mainTable.setWidth("100%");
		
		Table table = new Table(2);

		int _pasta = Integer.parseInt(properties.getProperty("_pastaAseguradora", "0"));

		String _meicos = properties.getProperty("_meicos", "");

		String _tipo = properties.getProperty("_tipo", "");

		long meicosId = Long.parseLong(properties.getProperty("_meicosId", "0"));

		properties.remove("_meicosId");
		properties.remove("_meicos");

		if (incluir || _pasta > 13)
			_pasta = DETALHE;

		Link dadosLink = new Link("Detalles", new Action("visualizarDetalhesEntidade"));
		((Label) dadosLink.getCaption()).setBold(_pasta == DETALHE);
		dadosLink.getAction().add("id", aseguradora.obterId());
		dadosLink.getAction().add("_pastaAseguradora", DETALHE);
		dadosLink.setEnabled(!incluir);

		Link componentesLink = new Link("Personas", new Action("visualizarDetalhesEntidade"));
		((Label) componentesLink.getCaption()).setBold(_pasta == PERSONAS);
		componentesLink.getAction().add("id", aseguradora.obterId());
		componentesLink.getAction().add("_pastaAseguradora", PERSONAS);
		componentesLink.setEnabled(!incluir);

		Link sucursalesLink = new Link("Sucursales", new Action("visualizarDetalhesEntidade"));
		((Label) sucursalesLink.getCaption()).setBold(_pasta == SUCURSALES);
		sucursalesLink.getAction().add("id", aseguradora.obterId());
		sucursalesLink.getAction().add("_pastaAseguradora", SUCURSALES);
		sucursalesLink.setEnabled(!incluir);

		Link fusionesLink = new Link("Fusiones", new Action("visualizarDetalhesEntidade"));
		((Label) fusionesLink.getCaption()).setBold(_pasta == FUSIONES);
		fusionesLink.getAction().add("id", aseguradora.obterId());
		fusionesLink.getAction().add("_pastaAseguradora", FUSIONES);
		fusionesLink.setEnabled(!incluir);

		Link accionistasLink = new Link("Accionistas", new Action("visualizarDetalhesEntidade"));
		((Label) accionistasLink.getCaption()).setBold(_pasta == ACCIONISTAS);
		accionistasLink.getAction().add("id", aseguradora.obterId());
		accionistasLink.getAction().add("_pastaAseguradora", ACCIONISTAS);
		accionistasLink.setEnabled(!incluir);

		Link reaseguradoraLink = new Link("Reaseguradora", new Action("visualizarDetalhesEntidade"));
		((Label) reaseguradoraLink.getCaption())
				.setBold(_pasta == REASEGURADORA);
		reaseguradoraLink.getAction().add("id", aseguradora.obterId());
		reaseguradoraLink.getAction().add("_pastaAseguradora", REASEGURADORA);
		reaseguradoraLink.setEnabled(!incluir);

		Link planesLink = new Link("Planes", new Action("visualizarDetalhesEntidade"));
		((Label) planesLink.getCaption()).setBold(_pasta == PLANES);
		planesLink.getAction().add("id", aseguradora.obterId());
		planesLink.getAction().add("_pastaAseguradora", PLANES);
		planesLink.setEnabled(!incluir);

		Link coaseguradorLink = new Link("Coasegurador", new Action("visualizarDetalhesEntidade"));
		((Label) coaseguradorLink.getCaption()).setBold(_pasta == COASEGURADOR);
		coaseguradorLink.getAction().add("id", aseguradora.obterId());
		coaseguradorLink.getAction().add("_pastaAseguradora", COASEGURADOR);
		coaseguradorLink.setEnabled(!incluir);

		Link controleLink = new Link("Control SIS", new Action("visualizarDetalhesEntidade"));
		((Label) controleLink.getCaption()).setBold(_pasta == CONTROLE);
		controleLink.getAction().add("id", aseguradora.obterId());
		controleLink.getAction().add("_pastaAseguradora", CONTROLE);
		controleLink.setEnabled(!incluir);
		
		Link meicosLink = new Link("Meicos", new Action(
				"visualizarDetalhesEntidade"));
		((Label) meicosLink.getCaption()).setBold(_pasta == MEICOS);
		meicosLink.getAction().add("id", aseguradora.obterId());
		meicosLink.getAction().add("_pastaAseguradora", MEICOS);
		meicosLink.getAction().add("_meicos", "False");
		meicosLink.setEnabled(!incluir);

		Link ratiosLink = new Link("Rat. Finan.", new Action(
				"visualizarDetalhesEntidade"));
		((Label) ratiosLink.getCaption()).setBold(_pasta == RATIOS);
		ratiosLink.getAction().add("id", aseguradora.obterId());
		ratiosLink.getAction().add("_pastaAseguradora", RATIOS);
		ratiosLink.setEnabled(!incluir);
		
		Link CiRucLink = new Link("CI y RUC", new Action(
		"visualizarDetalhesEntidade"));
		((Label) CiRucLink.getCaption()).setBold(_pasta == CIRUC);
		CiRucLink.getAction().add("id", aseguradora.obterId());
		CiRucLink.getAction().add("_pastaAseguradora", CIRUC);
		CiRucLink.setEnabled(!incluir);
		
		Link historicoLink = new Link("Histórico de Eventos", new Action("visualizarEventosEntidade"));
		//((Label) historicoLink.getCaption()).setBold(_pasta == CIRUC);
		historicoLink.getAction().add("id", aseguradora.obterId());
		historicoLink.getAction().add("_pastaAseguradora", 0);
		historicoLink.setEnabled(!incluir);
		
		Link listadoLink = new Link("Listados", new Action("visualizarRelatorioAseguradora"));
		//((Label) listadoLink.getCaption()).setBold(_pasta == CIRUC);
		listadoLink.getAction().add("id", aseguradora.obterId());
		listadoLink.getAction().add("_pastaAseguradora", 0);
		listadoLink.getAction().add("lista", false);
		listadoLink.setEnabled(!incluir);
		
		Link producaoLink = new Link("Produción Agentes", new Action("visualizarDetalhesEntidade"));
		((Label) producaoLink.getCaption()).setBold(_pasta == PRODUCAO);
		producaoLink.getAction().add("id", aseguradora.obterId());
		producaoLink.getAction().add("_pastaAseguradora", PRODUCAO);
		producaoLink.setEnabled(!incluir);
		
		/*Link resolucaoLink = new Link("Resoluciones", new Action("visualizarDetalhesEntidade"));
		((Label) resolucaoLink.getCaption()).setBold(_pasta == RESOLUCAO);
		resolucaoLink.getAction().add("id", aseguradora.obterId());
		resolucaoLink.getAction().add("_pastaAseguradora", RESOLUCAO);
		resolucaoLink.setEnabled(!incluir);*/
		
		/*Link msLink = new Link("Margen Solvencia", new Action("visualizarDetalhesEntidade"));
		((Label) msLink.getCaption()).setBold(_pasta == MARGEM_SOLVENCIA);
		msLink.getAction().add("id", aseguradora.obterId());
		msLink.getAction().add("_pastaAseguradora", MARGEM_SOLVENCIA);
		msLink.setEnabled(!incluir);*/

		Block block = new Block(Block.HORIZONTAL);
		block.add(dadosLink);
		block.add(new SeparadorLabel());
		block.add(componentesLink);
		block.add(new SeparadorLabel());
		block.add(sucursalesLink);
		block.add(new SeparadorLabel());
		block.add(fusionesLink);
		block.add(new SeparadorLabel());
		block.add(accionistasLink);
		block.add(new SeparadorLabel());
		block.add(reaseguradoraLink);
		block.add(new SeparadorLabel());
		block.add(planesLink);
		block.add(new SeparadorLabel());
		block.add(coaseguradorLink);
		block.add(new SeparadorLabel());
		block.add(controleLink);
		block.add(new SeparadorLabel());
		block.add(meicosLink);
		block.add(new SeparadorLabel());
		block.add(ratiosLink);
		block.add(new SeparadorLabel());
		block.add(CiRucLink);
		block.add(new SeparadorLabel());
		block.add(historicoLink);
		block.add(new SeparadorLabel());
		block.add(listadoLink);
		block.add(new SeparadorLabel());
		block.add(producaoLink);
		/*block.add(new SeparadorLabel());
		block.add(resolucaoLink);*/
		/*block.add(new SeparadorLabel());
		block.add(msLink);*/

		//table.setNextColSpan(table.getColumns());
		mainTable.add(block);

		if (_pasta == DETALHE) {
			table.setNextColSpan(table.getColumns());
			table.add(new Space());

			if (!incluir) {
				table.addHeader("Creado en:");
				String data = new SimpleDateFormat("dd/MM/yyyy")
						.format(aseguradora.obterCriacao());
				table.add(data);
				
			}

			/*
			 * SimpleDateFormat st = new SimpleDateFormat("dd MMM yyyy",
			 * Locale.getDefault());
			 * 
			 * table.addHeader("Data:"); table.add(st.format(new Date()));
			 */

			table.addHeader("Superior:");

			if (incluir)
				table.add(new EntidadePopup("entidadeSuperiorId",
						"superiorNome", aseguradora.obterSuperior(), true));
			else
				table.add(new EntidadePopup("entidadeSuperiorId",
						"superiorNome", aseguradora.obterSuperior(),
						aseguradora.permiteAtualizar()));

			table.addHeader("Responsable:");
			if (incluir)
				table.add(new EntidadePopup("responsavelId", "responsavelNome",
						aseguradora.obterResponsavel(), "Usuario", true));
			else
				table.add(new EntidadePopup("responsavelId", "responsavelNome",
						aseguradora.obterResponsavel(), "Usuario", aseguradora
								.permiteAtualizar()));

			Entidade.Atributo tipo = (Entidade.Atributo) aseguradora
					.obterAtributo("tipo");

			table.addHeader("Tipo:");
			Select select = new Select("atributo_tipo", 1);
			select.add("", "", false);
			if (tipo != null) {
				select.add("Aseguradora", "Aseguradora", "Aseguradora"
						.equals(tipo.obterValor()));
				select.add("Coaseguradora", "Coaseguradora", "Coaseguradora"
						.equals(tipo.obterValor()));
			} else {
				select.add("Aseguradora", "Aseguradora", false);
				select.add("Coaseguradora", "Coaseguradora", false);
			}

			table.add(select);

			table.addHeader("Nombre:");
			table.add(new InputString("nome", aseguradora.obterNome(), 60));

			Entidade.Atributo nomeAbreviado = (Entidade.Atributo) aseguradora
					.obterAtributo("nomeabreviado");

			String nomeAbreviadoStr = "";
			if (nomeAbreviado != null)
				nomeAbreviadoStr = nomeAbreviado.obterValor();

			table.addHeader("Denominácion:");
			table.add(new InputString("atributo_nomeabreviado",
					nomeAbreviadoStr, 15));

			Entidade.Atributo nacionalidade = (Entidade.Atributo) aseguradora
					.obterAtributo("nacionalidade");

			String nacionalidadeStr = "";
			if (nacionalidade != null)
				nacionalidadeStr = nacionalidade.obterValor();

			table.addHeader("Ruc");
			table.add(new InputString("ruc", aseguradora.obterRuc(), 15));

			table.addHeader("Pais de Origen:");
			table.add(new InputString("atributo_nacionalidade",
					nacionalidadeStr, 30));

			table.addHeader("Nº de Inscripciòn:");
			if (aseguradora.obterSigla() != null)
				table.add(aseguradora.obterSigla());
			else
				table.add("");

			Entidade.Atributo situacao = (Entidade.Atributo) aseguradora
					.obterAtributo("situacao");

			table.addHeader("Stuación:");
			if (situacao != null) {
				if (situacao.obterValor() == null
						|| situacao.obterValor().equals("")
						|| situacao.obterValor().equals(" ")) {
					InputString input = new InputString("atributo_situacao",
							"No Activa", 20);
					input.setEnabled(false);

					table.addHeader(input);
				} else
					table.addHeader(situacao.obterValor());
			}
			else
			{
				InputString input = new InputString("atributo_situacao",
						"No Activa", 20);
				input.setEnabled(false);

				table.addHeader(input);
			}
			
			table.addHeader("Sigla:");
			table.addHeader(aseguradora.obterSigla());
			
			if (incluir) 
			{
				Button incluirButton = new Button("Agregar", new Action("incluirAseguradora"));

				table.addFooter(incluirButton);
			}
			else 
			{
				Button atualizarButton = new Button("Actualizar", new Action("atualizarAseguradora"));
				atualizarButton.getAction().add("id", aseguradora.obterId());
				atualizarButton.setEnabled(aseguradora.permiteAtualizar());
				table.addFooter(atualizarButton);

				/*Button excluirButton = new Button("Eliminar", new Action("excluirEntidade"));
				excluirButton.getAction().add("id", aseguradora.obterId());
				excluirButton.setEnabled(aseguradora.permiteExcluir() && aseguradora.permiteAtualizar());

				table.addFooter(excluirButton);*/
			}
			
			mainTable.add(table);

			mainTable.addSubtitle("Contactos:");
			mainTable.addHeader("Contactos:");
			mainTable.addData(new EntidadeContatosView(aseguradora));

			mainTable.addSubtitle("Direcciones:");
			mainTable.addHeader("Direcciones:");
			mainTable.add(new EntidadeEnderecosView(aseguradora));
		}
		else if (_pasta == PERSONAS) {
			mainTable.addSubtitle("Personas");
			//mainTable.setNextColSpan(table.getColumns());
			mainTable.add(new PersonasAseguradoraView(aseguradora));
		}

		else if (_pasta == SUCURSALES) {
			//table.setNextColSpan(table.getColumns());
			mainTable.add(new AseguradoraFiliaisView(aseguradora));
		}

		else if (_pasta == FUSIONES) {
			//table.setNextColSpan(table.getColumns());
			mainTable.add(new AseguradoraFusoesView(aseguradora));
		}

		else if (_pasta == ACCIONISTAS) {
			//table.setNextColSpan(table.getColumns());
			mainTable.add(new AcionistasAseguradoraView(aseguradora));
		}

		else if (_pasta == REASEGURADORA) {
			//table.setNextColSpan(table.getColumns());
			mainTable.add(new ReaseguradorasAseguradoraView(aseguradora));
		}

		else if (_pasta == PLANES) {
			//table.setNextColSpan(table.getColumns());
			mainTable.add(new PlanosView(aseguradora, usuarioAtual));
		}

		else if (_pasta == COASEGURADOR) {
			mainTable.add(new AseguradoraCoaseguradorasView(aseguradora));
		}

		else if (_pasta == CONTROLE) {
			mainTable.add(new InscricoesView(aseguradora));
		}
		else if (_pasta == APOLICES)
		{
			Collection apolices = new ArrayList();
			mainTable.add(new ApolicesView(aseguradora, apolices, true));
		} 
		else if (_pasta == MEICOS) 
		{
			EventoHome home = (EventoHome) mm.getHome("EventoHome");
			MeicosAseguradora meicos = null;

			if (meicosId > 0 && _meicos.equals("True")) {
				meicos = (MeicosAseguradora) home.obterEventoPorId(meicosId);
			}

			if (_tipo.equals(""))
				_tipo = "Auditoria Externa";

			mainTable.add(new AseguradoraMeicosView(aseguradora, _tipo, meicos));
		} 
		else if (_pasta == RATIOS) 
		{
			mainTable.add(new AseguradoraRatiosView(aseguradora));
		}
		else if (_pasta == CIRUC) 
		{
			mainTable.add(new GerarCiRucView(aseguradora));
		}
		else if (_pasta == PRODUCAO) 
		{
			String dataInicioStr = properties.getProperty("_dataInicio", "");
			Date dataInicio = null;
			if(!dataInicioStr.equals(""))
				dataInicio = new SimpleDateFormat("dd/MM/yyyy").parse(dataInicioStr);
			
			String dataFimStr = properties.getProperty("_dataFim", "");
			Date dataFim = null;
			if(!dataFimStr.equals(""))
				dataFim = new SimpleDateFormat("dd/MM/yyyy").parse(dataFimStr);
			
			mainTable.add(new ProducaoAgenteView(aseguradora, dataInicio, dataFim));
		}

		return mainTable;
	}
}