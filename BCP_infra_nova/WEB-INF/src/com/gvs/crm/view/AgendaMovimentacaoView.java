package com.gvs.crm.view;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;
import java.util.Properties;

import com.gvs.crm.component.CriacaoLabel;
import com.gvs.crm.component.DateInput;
import com.gvs.crm.component.EntidadePopup;
import com.gvs.crm.component.EventoDescricaoInput;
import com.gvs.crm.component.EventoSuperiorLabel;
import com.gvs.crm.component.EventoTituloInput;
import com.gvs.crm.component.SimNao2Select;
import com.gvs.crm.component.TipoMovimentoSelect;
import com.gvs.crm.component.ValidacaoSelect;
import com.gvs.crm.model.AgendaMovimentacao;
import com.gvs.crm.model.CRMModelManager;
import com.gvs.crm.model.Evento;
import com.gvs.crm.model.Evento.Fase;
import com.gvs.crm.model.Notificacao;
import com.gvs.crm.model.Usuario;
import com.gvs.crm.model.UsuarioHome;

import infra.control.Action;
import infra.security.User;
import infra.view.Button;
import infra.view.Image;
import infra.view.InputLong;
import infra.view.Label;
import infra.view.Link;
import infra.view.Table;
import infra.view.View;

public class AgendaMovimentacaoView extends EventoAbstratoView 
{
	public View execute(User user, Locale locale, Properties properties) throws Exception 
	{
		Evento e = (Evento) this.obterEvento();

		CRMModelManager mm = new CRMModelManager(user);
		UsuarioHome home = (UsuarioHome) mm.getHome("UsuarioHome");
		Usuario usuarioAtual = home.obterUsuarioPorUser(user);
		//OpcaoHome opcaoHome = (OpcaoHome) mm.getHome("OpcaoHome");
		//Collection<Integer> opcoes = opcaoHome.obterOpcoes(usuarioAtual);

		AgendaMovimentacao agendaMovimentacao = (AgendaMovimentacao) e;

		Evento superior = agendaMovimentacao.obterSuperior();
		boolean incluir = agendaMovimentacao.obterId() == 0;
		if (superior != null && incluir) 
		{
			agendaMovimentacao.atribuirOrigem(superior.obterOrigem());
			agendaMovimentacao.atribuirPrioridade(superior.obterPrioridade());
		}

		Table table = new Table(2);
		table.addHeader("Superior:");
		table.add(new EventoSuperiorLabel(agendaMovimentacao));

		table.addHeader("Aseguradora:");
		table.addData(new EntidadePopup("origemId", "origemNome",agendaMovimentacao.obterOrigem(), "aseguradora",agendaMovimentacao.permiteAtualizar()));
		table.addHeader("Responsable:");
		table.addData(new EntidadePopup("responsavelId", "responsavelNome",agendaMovimentacao.obterResponsavel(), "aseguradora",agendaMovimentacao.permiteAtualizar()));

		table.addHeader("Tipo Movimento:");
		if (incluir)
			table.add(new TipoMovimentoSelect("tipo", agendaMovimentacao.obterTipo()));
		else 
		{
			if (agendaMovimentacao.obterTipo() != null) 
			{
				if (agendaMovimentacao.permiteAtualizar())
					table.add(new TipoMovimentoSelect("tipo",agendaMovimentacao.obterTipo()));
				else
					table.add(new Label(agendaMovimentacao.obterTipo()));
			} 
			else
				table.add("");
		}
		
		table.addHeader("Validación:");
		table.add(new ValidacaoSelect("validacao",agendaMovimentacao.obterValidacao()));

		table.addHeader("Fecha Prevista:");
		table.add(new DateInput("data", agendaMovimentacao.obterDataPrevistaInicio(), agendaMovimentacao.permiteAtualizar()));

		table.addHeader("Movimiento:");
		Table mesAnoTable = new Table(4);
		mesAnoTable.addHeader("Mes:");
		if (agendaMovimentacao.permiteAtualizar())
			mesAnoTable.addData(new InputLong("mesmovimento",agendaMovimentacao.obterMesMovimento(), 2));
		else
			mesAnoTable.add(new Label(agendaMovimentacao.obterMesMovimento()));

		mesAnoTable.addHeader("Año:");
		if (agendaMovimentacao.permiteAtualizar())
			mesAnoTable.addData(new InputLong("anomovimento",agendaMovimentacao.obterAnoMovimento(), 4));
		else
			mesAnoTable.add(new Label(agendaMovimentacao.obterAnoMovimento()));

		table.add(mesAnoTable);
		
		//if(opcoes.contains(70) || usuarioAtual.obterNivel().equals(Usuario.ADMINISTRADOR))
		if(usuarioAtual.obterNivel().equals(Usuario.ADMINISTRADOR))
		{
			table.addHeader("Especial:");
			if(incluir)
				table.add(new SimNao2Select("especial", "Não"));
			else
			{
				String especial = agendaMovimentacao.obterEspecial(); 
				if(especial!=null)
					table.add(new SimNao2Select("especial", especial));
				else
					table.add(new SimNao2Select("especial", "Não"));
			}
			
			table.addHeader("Inscr. o Fecha Especial (Reaseguro):");
			if(incluir)
				table.add(new SimNao2Select("inscricaoEspecial", "Não"));
			else
			{
				String incricaoEspecial = agendaMovimentacao.obterInscricaoEspecial();
				if(incricaoEspecial!=null)
					table.add(new SimNao2Select("inscricaoEspecial", incricaoEspecial));
				else
					table.add(new SimNao2Select("inscricaoEspecial", "Não"));
			}
			
			table.addHeader("Endoso Especial:");
			if(incluir)
				table.add(new SimNao2Select("endosoEspecial", "Não"));
			else
			{
				String suplementoEspecial = agendaMovimentacao.obterSuplementoEspecial();
				if(suplementoEspecial!=null)
					table.add(new SimNao2Select("endosoEspecial", suplementoEspecial));
				else
					table.add(new SimNao2Select("endosoEspecial", "Não"));
			}
			
			table.addHeader("Capital Especial:");
			if(incluir)
				table.add(new SimNao2Select("capitalEspecial", "Não"));
			else
			{
				String capitalEspecial = agendaMovimentacao.obterCapitalEspecial();
				if(capitalEspecial!=null)
					table.add(new SimNao2Select("capitalEspecial", capitalEspecial));
				else
					table.add(new SimNao2Select("capitalEspecial", "Não"));
			}
			
			table.addHeader("Fecha Especial:");
			if(incluir)
				table.add(new SimNao2Select("dataEspecial", "Não"));
			else
			{
				String dataEspecial = agendaMovimentacao.obterDataEspecial();
				if(dataEspecial!=null)
					table.add(new SimNao2Select("dataEspecial", dataEspecial));
				else
					table.add(new SimNao2Select("dataEspecial", "Não"));
			}
			
			/*table.addHeader("Documento Especial:");
			if(incluir)
				table.add(new SimNao2Select("documentoEspecial", "Não"));
			else
			{
				String documentoEspecial = agendaMovimentacao.obterDocumentoEspecial();
				if(documentoEspecial!=null)
					table.add(new SimNao2Select("documentoEspecial", documentoEspecial));
				else
					table.add(new SimNao2Select("documentoEspecial", "Não"));
			}*/
			
			table.addHeader("Ap. Anterior Especial:");
			if(incluir)
				table.add(new SimNao2Select("apAnteriorEspecial", "Não"));
			else
			{
				String apAnteriorEspecial = agendaMovimentacao.obterApAnteriorEspecial();
				if(apAnteriorEspecial!=null)
					table.add(new SimNao2Select("apAnteriorEspecial", apAnteriorEspecial));
				else
					table.add(new SimNao2Select("apAnteriorEspecial", "Não"));
			}
			
			table.addHeader("Endoso Polizas:");
			if(incluir)
				table.add(new SimNao2Select("endosoApolice", "Não"));
			else
			{
				String endosoAplice = agendaMovimentacao.obterEndosoApolice();
				if(endosoAplice!=null)
					table.add(new SimNao2Select("endosoApolice", endosoAplice));
				else
					table.add(new SimNao2Select("endosoApolice", "Não"));
			}
		}

		table.addHeader("Título:");
		table.addData(new EventoTituloInput("titulo", agendaMovimentacao,
				agendaMovimentacao.permiteAtualizar()));
		table.addHeader("Descripción:");
		table.addData(new EventoDescricaoInput("descricao", agendaMovimentacao,
				agendaMovimentacao.permiteAtualizar()));

		if (incluir)
		{
			Button incluirButton = new Button("Agregar", new Action("incluirAgendaMovimentacao"));
			if (superior != null)
				incluirButton.getAction().add("superiorId", superior.obterId());
			incluirButton.getAction().add("origemMenuId", this.obterOrigemMenu().obterId());
			table.addFooter(incluirButton);

			Action voltarAction = new Action("novoEvento");
			voltarAction.add("passo", 2);
			voltarAction.add("origemMenuId", this.obterOrigemMenu().obterId());

			Button voltarButton = new Button("Volver", voltarAction);
			table.addFooter(voltarButton);
		}
		else
		{
			String tipo = agendaMovimentacao.obterTipo();
			if (!incluir)
			{
				table.addHeader("Creado por:");
				table.addData(new CriacaoLabel(agendaMovimentacao));
			}

			if (!agendaMovimentacao.obterComentarios().isEmpty())
			{
				table.addHeader("Comentarios:");
				table.add(new ComentariosView(agendaMovimentacao));
			}

			if (agendaMovimentacao.obterTipo() != null)
			{
				//if(!agendaMovimentacao.obterTipo().equals("Instrumento"))
				//{
				if (!agendaMovimentacao.obterInferiores().isEmpty())
				{
					table.addHeader("Sub Eventos:");
					table.add(this.inferioresView(agendaMovimentacao));
				}
				//}
			}

			if (tipo != null) 
			{
				if (tipo.equals("Instrumento") || tipo.equals("Instrumento archivo B") || tipo.equals("Meicos")) 
				{
					if (!agendaMovimentacao.obterFase().obterCodigo().equals(AgendaMovimentacao.AGENDADA)) 
					{
						Button agendarButton = new Button("Agendar",new Action("agendarMovimentacao"));
						agendarButton.getAction().add("id",agendaMovimentacao.obterId());
						agendarButton.setEnabled(agendaMovimentacao.permiteAtualizar());
						table.addFooter(agendarButton);
					}
					
					Collection<Fase> fases = agendaMovimentacao.obterFases();
					boolean podeConcluir = false;
					
					for(Iterator<Fase> i = fases.iterator() ; i.hasNext() ; )
					{
						Fase fase = i.next();
						
						if(fase.obterCodigo().equals(Evento.EVENTO_CONCLUIDO))
						{
							podeConcluir = true;
							break;
						}
					}
					
					if(podeConcluir)
					{
						Button concluirButton = new Button("Concluir", new Action("concluirEvento"));
						concluirButton.getAction().add("id", agendaMovimentacao.obterId());
						
						table.addFooter(concluirButton);
					}
				} 
				else 
				{
					Button verificarButton = new Button("Verificar Archivo",new Action("verificarArquivo"));
					verificarButton.getAction().add("id",agendaMovimentacao.obterId());
					verificarButton.getAction().add("view", true);
					verificarButton.getAction().add("origemMenuId",agendaMovimentacao.obterOrigem().obterId());
					verificarButton.setEnabled(!agendaMovimentacao.obterFase().obterCodigo().equals(Evento.EVENTO_CONCLUIDO));
					table.addFooter(verificarButton);
				}
			} 
			else 
			{
				Button verificarButton = new Button("Verificar Archivo",new Action("verificarArquivo"));
				verificarButton.getAction().add("id",agendaMovimentacao.obterId());
				verificarButton.getAction().add("view", true);
				verificarButton.getAction().add("origemMenuId",agendaMovimentacao.obterOrigem().obterId());
				verificarButton.setEnabled(agendaMovimentacao.permiteAtualizar());
				table.addFooter(verificarButton);
			}

			Button encaminharButton = new Button("Remitir", new Action(
					"encaminharEvento"));
			encaminharButton.getAction()
					.add("id", agendaMovimentacao.obterId());
			encaminharButton.getAction().add("origemMenuId",
					agendaMovimentacao.obterOrigem().obterId());
			encaminharButton.getAction().add("view", true);
			encaminharButton.setEnabled(agendaMovimentacao.permiteEncaminhar());
			table.addFooter(encaminharButton);

			Button comentarioButton = new Button("Comentario", new Action(
					"incluirComentario"));
			comentarioButton.getAction()
					.add("id", agendaMovimentacao.obterId());
			comentarioButton.getAction().add("view", true);
			comentarioButton.getAction().add("origemMenuId",
					agendaMovimentacao.obterOrigem().obterId());
			comentarioButton.setEnabled(agendaMovimentacao.permiteAtualizar());
			table.addFooter(comentarioButton);

			Action action = new Action("excluirAgendaMovimentacao");
			action.setConfirmation("Confirma exclusión ?");

			Button excluirButton = new Button("Eliminar", action);
			excluirButton.getAction().add("id", agendaMovimentacao.obterId());
			excluirButton.getAction().add("origemMenuId",agendaMovimentacao.obterOrigem().obterId());
			excluirButton.setEnabled(usuarioAtual.obterId() == 1 || agendaMovimentacao.permiteAtualizar());
			table.addFooter(excluirButton);

			Button voltarButton = new Button("Volver", new Action(
					"visualizarPaginaInicial"));
			voltarButton.getAction().add("id", usuarioAtual.obterId());
			table.addFooter(voltarButton);

		}
		return table;
	}

	private View inferioresView(AgendaMovimentacao agendaMovimentacao)throws Exception
	{
		Table table = new Table(5);
		table.setWidth("100%");
		table.addHeader("Data");
		table.addHeader("");
		table.addHeader("Descripción");
		table.addHeader("Fase");
		table.addHeader("Responsable");
		for (Iterator i = agendaMovimentacao.obterInferiores().iterator(); i.hasNext();)
		{
			Evento e = (Evento) i.next();
			if (e instanceof Notificacao)
			{
				Label responsavelLabel = new Label(e.obterResponsavel().obterNome());
				responsavelLabel.setBold(!e.foiLido());
				Label descricaoLabel = new Label(e.obterTitulo());
				descricaoLabel.setBold(!e.foiLido());

				Link responsavelLink = new Link(responsavelLabel, new Action("visualizarDetalhesEntidade"));
				responsavelLink.getAction().add("id",e.obterResponsavel().obterId());
				responsavelLink.getAction().add("origemMenuId",	agendaMovimentacao.obterOrigem().obterId());

				Link descricaoLink = new Link(descricaoLabel, new Action("visualizarEvento"));
				descricaoLink.getAction().add("id", e.obterId());
				descricaoLink.getAction().add("origemMenuId",agendaMovimentacao.obterOrigem().obterId());

				Label faseLabel = new Label(e.obterFase().obterNome());
				faseLabel.setBold(!e.foiLido());

				table.addData(new SimpleDateFormat("dd/MM/yyyy HH:mm").format(e.obterCriacao()));

				if (e.obterFase().obterCodigo().equals(Evento.EVENTO_CONCLUIDO))
					table.add(new Image("check.gif"));
				else
					table.add("");

				table.addData(descricaoLink);
				table.addData(faseLabel);
				table.addData(responsavelLink);
			}

		}
		return table;
	}
}