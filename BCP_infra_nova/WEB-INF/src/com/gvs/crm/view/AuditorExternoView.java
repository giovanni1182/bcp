package com.gvs.crm.view;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Properties;

import com.gvs.crm.component.EntidadePopup;
import com.gvs.crm.component.SeparadorLabel;
import com.gvs.crm.model.AuditorExterno;
import com.gvs.crm.model.CRMModelManager;
import com.gvs.crm.model.Entidade;
import com.gvs.crm.model.Usuario;
import com.gvs.crm.model.UsuarioHome;

import infra.control.Action;
import infra.security.User;
import infra.view.Block;
import infra.view.Button;
import infra.view.InputDate;
import infra.view.InputString;
import infra.view.Label;
import infra.view.Link;
import infra.view.Space;
import infra.view.Table;
import infra.view.View;

public class AuditorExternoView extends EntidadeAbstrataView {

	private static final int DETALHE = 0;

	private static final int DATOS_COMPLEMENTARIOS = 1;

	private static final int CLIENTES = 2;

	private static final int PERSONAS = 3;

	private static final int INGRESO = 4;

	private static final int TECNOLOGIA = 5;

	private static final int CONTROLE = 6;

	public View execute(User user, Locale locale, Properties properties)
			throws Exception {

		AuditorExterno auditor = (AuditorExterno) this.obterEntidade();

		CRMModelManager mm = new CRMModelManager(user);
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Usuario responsavel = (Usuario) usuarioHome.obterUsuarioPorUser(user);

		boolean incluir = auditor.obterId() == 0;

		Table table = new Table(2);
		table.setWidth("100%");

		int _pasta = Integer.parseInt(properties.getProperty("_pastaAuditor",
				"0"));

		if (incluir || _pasta > 6)
			_pasta = DETALHE;

		Link dadosLink = new Link("Detalles", new Action(
				"visualizarDetalhesEntidade"));
		((Label) dadosLink.getCaption()).setBold(_pasta == DETALHE);
		dadosLink.getAction().add("id", auditor.obterId());
		dadosLink.getAction().add("_pastaAuditor", DETALHE);
		dadosLink.setEnabled(!incluir);

		Link componentesLink = new Link("Datos Complementarios", new Action(
				"visualizarDetalhesEntidade"));
		((Label) componentesLink.getCaption())
				.setBold(_pasta == DATOS_COMPLEMENTARIOS);
		componentesLink.getAction().add("id", auditor.obterId());
		componentesLink.getAction().add("_pastaAuditor", DATOS_COMPLEMENTARIOS);
		componentesLink.setEnabled(!incluir);

		Link sucursalesLink = new Link("Clientes", new Action(
				"visualizarDetalhesEntidade"));
		((Label) sucursalesLink.getCaption()).setBold(_pasta == CLIENTES);
		sucursalesLink.getAction().add("id", auditor.obterId());
		sucursalesLink.getAction().add("_pastaAuditor", CLIENTES);
		sucursalesLink.setEnabled(!incluir);

		Link fusionesLink = new Link("Personas", new Action(
				"visualizarDetalhesEntidade"));
		((Label) fusionesLink.getCaption()).setBold(_pasta == PERSONAS);
		fusionesLink.getAction().add("id", auditor.obterId());
		fusionesLink.getAction().add("_pastaAuditor", PERSONAS);
		fusionesLink.setEnabled(!incluir);

		Link accionistasLink = new Link("Ingreso", new Action(
				"visualizarDetalhesEntidade"));
		((Label) accionistasLink.getCaption()).setBold(_pasta == INGRESO);
		accionistasLink.getAction().add("id", auditor.obterId());
		accionistasLink.getAction().add("_pastaAuditor", INGRESO);
		accionistasLink.setEnabled(!incluir);

		Link controleLink = new Link("Control SIS", new Action(
				"visualizarDetalhesEntidade"));
		((Label) controleLink.getCaption()).setBold(_pasta == CONTROLE);
		controleLink.getAction().add("id", auditor.obterId());
		controleLink.getAction().add("_pastaAuditor", CONTROLE);
		controleLink.setEnabled(!incluir);

		Link tecnologiaLink = new Link("Tecnologia y Seguridad", new Action(
				"visualizarDetalhesEntidade"));
		((Label) tecnologiaLink.getCaption()).setBold(_pasta == TECNOLOGIA);
		tecnologiaLink.getAction().add("id", auditor.obterId());
		tecnologiaLink.getAction().add("_pastaAuditor", TECNOLOGIA);
		tecnologiaLink.setEnabled(!incluir);

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
		block.add(controleLink);
		block.add(new SeparadorLabel());
		block.add(tecnologiaLink);

		table.setNextColSpan(table.getColumns());
		table.add(block);

		if (_pasta == DETALHE) {

			table.addSubtitle("Detalle");

			if (!incluir) {
				table.addHeader("Creado en:");
				String data = new SimpleDateFormat("dd/MM/yyyy").format(auditor
						.obterCriacao());
				table.add(data);
			}

			table.addHeader("Nombre:");
			table.add(new InputString("nome", auditor.obterNome(), 50));

			Entidade.Atributo nacionalidade = (Entidade.Atributo) auditor
					.obterAtributo("nacionalidade");

			String nacionalidadeStr = "";
			if (nacionalidade != null)
				nacionalidadeStr = nacionalidade.obterValor();

			table.addHeader("Pais de Origen:");
			table.add(new InputString("atributo_nacionalidade",
					nacionalidadeStr, 30));

			table.addHeader("Superior:");

			if (incluir)
				table.add(new EntidadePopup("entidadeSuperiorId",
						"superiorNome", auditor.obterSuperior(), true));
			else
				table.add(new EntidadePopup("entidadeSuperiorId",
						"superiorNome", auditor.obterSuperior(), auditor
								.permiteAtualizar()));

			table.addHeader("Responsable:");
			if (incluir)
				table.add(new EntidadePopup("responsavelId", "responsavelNome",
						responsavel, "Usuario", true));
			else
				table.add(new EntidadePopup("responsavelId", "responsavelNome",
						auditor.obterResponsavel(), "Usuario", auditor
								.permiteAtualizar()));

			Entidade.Atributo razaoSocial = (Entidade.Atributo) auditor
					.obterAtributo("razaosocial");

			String razaoSocialStr = "";
			if (razaoSocial != null)
				razaoSocialStr = razaoSocial.obterValor();

			table.addHeader("Razón Social:");
			table.add(new InputString("atributo_razaosocial", razaoSocialStr,
					60));

			Entidade.Atributo representante = (Entidade.Atributo) auditor
					.obterAtributo("representante");

			String representanteStr = "";
			if (representante != null)
				representanteStr = representante.obterValor();

			table.addHeader("Representante Legal:");
			table.add(new InputString("atributo_representante",
					representanteStr, 50));

			Entidade.Atributo corresponsalia = (Entidade.Atributo) auditor
					.obterAtributo("corresponsalia");

			String corresponsaliaStr = "";
			if (corresponsalia != null)
				corresponsaliaStr = corresponsalia.obterValor();

			table.addHeader("Corresponsalia:");
			table.add(new InputString("atributo_corresponsalia",
					corresponsaliaStr, 30));

			table.addHeader("Identificación:");

			Block block2 = new Block(Block.HORIZONTAL);

			block2.add(new InputString("ruc", auditor.obterRuc(), 15));
			block2.add(new Space());
			block2.add(new Label("Cédula de identidad(CI) o RUC"));

			table.add(block2);

			Entidade.Atributo situacao = (Entidade.Atributo) auditor
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
			} else {
				InputString input = new InputString("atributo_situacao",
						"No Activa", 20);
				input.setEnabled(false);

				table.addHeader(input);
			}

			Table table2 = new Table(2);

			table2.addSubtitle("Datos Generales");

			Entidade.Atributo dataConstituicao = (Entidade.Atributo) auditor
					.obterAtributo("dataconstituicao");

			table2.addHeader("Fecha de Constitución:");
			if (dataConstituicao != null
					&& !dataConstituicao.obterValor().equals(""))
				table2.add(new InputDate("atributo_dataconstituicao",
						new SimpleDateFormat("dd/MM/yyyy")
								.parse(dataConstituicao.obterValor())));
			else
				table2.add(new InputDate("atributo_dataconstituicao", null));

			Entidade.Atributo dataInscricao = (Entidade.Atributo) auditor
					.obterAtributo("datainscricao");

			table2.addHeader("Fecha de Inscripción en el Registro:");
			if (dataInscricao != null && !dataInscricao.obterValor().equals(""))
				table2.add(new InputDate("atributo_datainscricao",
						new SimpleDateFormat("dd/MM/yyyy").parse(dataInscricao
								.obterValor())));
			else
				table2.add(new InputDate("atributo_datainscricao", null));

			Entidade.Atributo patenteMunicipal = (Entidade.Atributo) auditor
					.obterAtributo("patentemunicipal");

			table2.addHeader("Patente Municipal:");
			if (patenteMunicipal != null)
				table2.add(new InputString("atributo_patentemunicipal",
						patenteMunicipal.obterValor(), 15));
			else
				table2.add(new InputString("atributo_patentemunicipal", null,
						15));

			Entidade.Atributo matriculaContador = (Entidade.Atributo) auditor
					.obterAtributo("matriculacontador");

			table2.addHeader("Matricula Contador:");
			if (matriculaContador != null)
				table2.add(new InputString("atributo_matriculacontador",
						matriculaContador.obterValor(), 15));
			else
				table2.add(new InputString("atributo_matriculacontador", null,
						15));

			Entidade.Atributo inscricaoCiv = (Entidade.Atributo) auditor
					.obterAtributo("inscricaociv");

			table2.addHeader("Inscripición C.N.V:");
			if (inscricaoCiv != null)
				table2.add(new InputString("atributo_inscricaociv",
						inscricaoCiv.obterValor(), 15));
			else
				table2.add(new InputString("atributo_inscricaociv", null, 15));

			Entidade.Atributo inscricaoBancos = (Entidade.Atributo) auditor
					.obterAtributo("inscricaobancos");

			table2.addHeader("Inscripición Sup. Bancos:");
			if (inscricaoBancos != null)
				table2.add(new InputString("atributo_inscricaobancos",
						inscricaoBancos.obterValor(), 15));
			else
				table2
						.add(new InputString("atributo_inscricaobancos", null,
								15));

			Entidade.Atributo experienciaSeguros = (Entidade.Atributo) auditor
					.obterAtributo("experienciaseguros");

			table2.addHeader("Experiencia en Seguros:");
			if (experienciaSeguros != null)
				table2.add(new InputString("atributo_experienciaseguros",
						experienciaSeguros.obterValor(), 15));
			else
				table2.add(new InputString("atributo_experienciaseguros", null,
						15));

			Entidade.Atributo experienciaGeral = (Entidade.Atributo) auditor
					.obterAtributo("experienciageral");

			table2.addHeader("Experiencia en General:");
			if (experienciaGeral != null)
				table2.add(new InputString("atributo_experienciageral",
						experienciaGeral.obterValor(), 15));
			else
				table2.add(new InputString("atributo_experienciageral", null,
						15));

			table.setNextColSpan(table.getColumns());
			table.add(table2);

			table.addSubtitle("Contactos:");
			table.addHeader("Contactos:");
			table.addData(new EntidadeContatosView(auditor));

			table.addSubtitle("Direcciones:");
			table.addHeader("Direcciones:");
			table.add(new EntidadeEnderecosView(auditor));

			if (incluir) {
				Button incluirButton = new Button("Agregar", new Action(
						"incluirAuditorExterno"));
				table.addFooter(incluirButton);
			} else {
				Button atualizarButton = new Button("Actualizar", new Action(
						"atualizarAuditorExterno"));
				atualizarButton.getAction().add("id", auditor.obterId());
				atualizarButton.setEnabled(auditor.permiteAtualizar());
				table.addFooter(atualizarButton);

				/*Button excluirButton = new Button("Eliminar", new Action(
						"excluirEntidade"));
				excluirButton.getAction().add("id", auditor.obterId());
				excluirButton.setEnabled(auditor.permiteAtualizar() && auditor.permiteExcluir());
				table.addFooter(excluirButton);*/
			}
		}

		else if (_pasta == DATOS_COMPLEMENTARIOS) {
			table.setNextColSpan(table.getColumns());
			table.add(new AuditorExternoDadosComplementaresView(auditor));
		}

		else if (_pasta == CLIENTES) {
			table.setNextColSpan(table.getColumns());
			table.add(new AuditorExternoClientesView(auditor));
		}

		else if (_pasta == PERSONAS) {
			table.setNextColSpan(table.getColumns());
			table.add(new AuditorExternoPersonasView(auditor));
		}

		else if (_pasta == INGRESO) {
			table.setNextColSpan(table.getColumns());
			table.add(new AuditorExternoIngressoView(auditor));
		}

		else if (_pasta == TECNOLOGIA) {
			table.setNextColSpan(table.getColumns());
			table.add(new AuditorExternoTecnologiaView(auditor));
		}

		else if (_pasta == CONTROLE) {
			table.setNextColSpan(table.getColumns());
			table.addSubtitle("Control SIS");
			table.add(new InscricoesView(auditor));
		}

		return table;
	}

}