package com.gvs.crm.view;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.util.Properties;

import com.gvs.crm.model.AgendaMovimentacao;
import com.gvs.crm.model.Aseguradora;
import com.gvs.crm.model.Entidade;
import com.gvs.crm.model.Evento;
import com.gvs.crm.model.Notificacao;

import infra.control.Action;
import infra.security.User;
import infra.view.Button;
import infra.view.InputDate;
import infra.view.InputString;
import infra.view.Label;
import infra.view.Link;
import infra.view.Table;
import infra.view.View;

public class ControleAgendasView extends PortalView
{
	private Collection aseguradoras;
	
	public ControleAgendasView(Collection aseguradoras) throws Exception
	{
		this.aseguradoras = aseguradoras;
	}
	
	public View getBody(User user, Locale locale, Properties properties) throws Exception
	{
		Table table = new Table(10);
		table.setWidth("100%");
		table.addStyle(Table.STYLE_ALTERNATE);
		
		table.addHeader("Aseguradoras");
		table.addHeader("Sigla");
		table.addHeader("Agenda MCI");
		table.addHeader("Archivo Sgte.");
		table.addHeader("Error");
		table.addHeader("Fecha Ult. Env. Correo");
		table.addHeader("Fecha Error");
		table.addHeader("Fecha Grabación");
		table.addHeader("Cant. Registros");
		table.addHeader("Comentarios");
		
		for(Iterator i = this.aseguradoras.iterator() ; i.hasNext() ; )
		{
			Aseguradora aseguradora = (Aseguradora) i.next();
			
			AgendaMovimentacao agendaGravada = aseguradora.obterUltimaAgendaMCI();
			
			if(agendaGravada!=null)
			{
				Link nomeLink = new Link(aseguradora.obterNome(),new Action("visualizarDetalhesEntidade"));
				nomeLink.getAction().add("id", aseguradora.obterId());
				
				table.add(nomeLink);
				table.add(aseguradora.obterSigla());
				String ultimaAgendaStr = agendaGravada.obterMesMovimento() + "/" + agendaGravada.obterAnoMovimento();
				
				Link ultAgendaLink = new Link(ultimaAgendaStr,new Action("visualizarEvento"));
				ultAgendaLink.getAction().add("id", agendaGravada.obterId());
				
				table.add(ultAgendaLink);
				AgendaMovimentacao agendaSeguinte = (AgendaMovimentacao) aseguradora.obterAgendaMCISeguinte();
				
				String erro = "";
				Date dataErro = null;
				
				if(agendaSeguinte!=null)
				{
					erro = "No";
					
					String mesAno = agendaSeguinte.obterMesMovimento() + "/" + agendaSeguinte.obterAnoMovimento(); 
					
					Link agendaSeguinteLink = new Link(mesAno,new Action("visualizarEvento"));
					agendaSeguinteLink.getAction().add("id", agendaSeguinte.obterId());
					
					table.add(agendaSeguinteLink);
					
					Date criacao = null;
					
					for (Iterator j = agendaSeguinte.obterInferiores().iterator(); j.hasNext();)
					{
						Evento e = (Evento) j.next();
						if (e instanceof Notificacao)
						{
							if(e.obterTitulo().equals("Notificación de Error de Validación"))
							{
								erro = "Sí";
								
								if(criacao==null)
									criacao = e.obterCriacao();
								else
								{
									if(e.obterCriacao().compareTo(criacao) > 0)
										criacao = e.obterCriacao();
								}
							}
						}
					}
					
					dataErro = criacao;
				}
				else
					table.add("");
				
				table.add(erro);
				InputDate data = new InputDate("data_" + aseguradora.obterId(), aseguradora.obterUltimoEnvioCorreio());
				table.add(data);
				if(dataErro!=null)
				{
					String dataErroStr = new SimpleDateFormat("dd/MM/yyyy").format(dataErro);
					table.add(dataErroStr);
				}
				else
					table.add("");
				
				
				if(agendaSeguinte!=null)
				{
					if(agendaSeguinte.obterDataModificacaoArquivo()!=null)
					{
						String dataGravacaoStr = new SimpleDateFormat("dd/MM/yyyy").format(agendaSeguinte.obterDataModificacaoArquivo());
						table.add(dataGravacaoStr);
					}
					else
						table.add("");
				
					int qtdeRegistrosA = agendaSeguinte.obterQtdeRegistrosA(); 
					int qtdeRegistrosB = agendaSeguinte.obterQtdeRegistrosB();
					
					int total = qtdeRegistrosA + qtdeRegistrosB;
					
					table.add(new Label(total));
				}
				else
				{
					table.add("");
					table.add("");
				}
				
				InputString inputString = new InputString("comentario_" + aseguradora.obterId(),aseguradora.obterComentarioControle(),40);
				
				table.add(inputString);
			}
			else
			{
				Link nomeLink = new Link(aseguradora.obterNome(),new Action("visualizarDetalhesEntidade"));
				nomeLink.getAction().add("id", aseguradora.obterId());
				
				table.add(nomeLink);
				table.add(aseguradora.obterSigla());
				
				table.add("");
				table.add("");
				table.add("");
				InputDate data = new InputDate("data_" + aseguradora.obterId(), aseguradora.obterUltimoEnvioCorreio());
				table.add(data);
				table.add("");
				table.add("");
				table.add("");
				InputString inputString = new InputString("comentario_" + aseguradora.obterId(),aseguradora.obterComentarioControle(),40);
				
				table.add(inputString);
			}
		}
		
		Button atualizarButton = new Button("Actualizar",new Action("atualizarControleAgendas"));
		
		table.addFooter(atualizarButton);
		
		return table;
	}

	public Entidade getOrigemMenu() throws Exception
	{
		return null;
	}

	public String getSelectedGroup() throws Exception
	{
		return null;
	}

	public String getSelectedOption() throws Exception
	{
		return null;
	}

	public View getTitle() throws Exception
	{
		return new Label("Controlo Agendas Instrumento");
	}
}