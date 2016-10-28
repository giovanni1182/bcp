package com.gvs.crm.view;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

import com.gvs.crm.component.AseguradorasSelect2;
import com.gvs.crm.component.NotificacaoSelect;
import com.gvs.crm.component.TipoProcessamentoSelect;
import com.gvs.crm.model.Entidade;
import com.gvs.crm.model.Evento;
import com.gvs.crm.model.Processamento;
import com.gvs.crm.model.Processamento.Agenda;

import infra.control.Action;
import infra.security.User;
import infra.view.Block;
import infra.view.Button;
import infra.view.InputDate;
import infra.view.InputInteger;
import infra.view.Label;
import infra.view.Link;
import infra.view.Space;
import infra.view.Table;
import infra.view.View;

public class RelProcessamentoView extends PortalView
{
	private Entidade aseguradora;
	private int ano, erro, mes;
	private String tipo;
	private Collection<Agenda> agendas;
	private Date dataInicio, dataFim;
	
	public RelProcessamentoView(Entidade aseguradora, int mes, int ano, String tipo, Collection<Agenda> agendas, int erro, Date dataInicio, Date dataFim)
	{
		this.aseguradora = aseguradora;
		this.mes = mes;
		this.ano = ano;
		this.tipo = tipo;
		this.agendas = agendas;
		this.erro = erro;
		this.dataInicio = dataInicio;
		this.dataFim = dataFim;
	}
	
	public View getBody(User user, Locale locale, Properties properties) throws Exception
	{
		Table mainTable = new Table(1);
		mainTable.setWidth("100%");
		
		Table table = new Table(2);
		
		Block block = new Block(Block.HORIZONTAL);
		
		String blockStr = "(Agenda o  Libro)";
		
		table.addHeader("Aseguradora:");
		//table.add(new EntidadePopup("aseguradoraId", "aseguradoraNome", this.aseguradora, "Aseguradora", true));
		table.add(new AseguradorasSelect2("aseguradoraId",this.aseguradora, false, true));
		
		table.addHeader("Mes:");
		block.add(new InputInteger("mes", this.mes, 2));
		block.add(new Space(2));
		block.add(new Label(blockStr));
		table.add(block);
		
		table.addHeader("Año:");
		block = new Block(Block.HORIZONTAL);
		block.add(new InputInteger("ano", this.ano, 4));
		block.add(new Space(2));
		block.add(new Label(blockStr));
		table.add(block);
		
		table.addHeader("Tipo:");
		table.add(new TipoProcessamentoSelect("tipo", this.tipo));
		table.addHeader("Notificación:");
		table.add(new NotificacaoSelect("erro", this.erro));
		
		table.addHeader("Periodo:");
		block = new Block(Block.HORIZONTAL);
		block.add(new InputDate("dataInicio", this.dataInicio));
		block.add(new Space(2));
		block.add(new Label("hasta"));
		block.add(new Space(2));
		block.add(new InputDate("dataFim", this.dataFim));
		block.add(new Space(2));
		block.add(new Label("(Fecha Validación)"));
		table.add(block);
		
		Button consultarButton = new Button("Buscar", new Action("relProcessamentos"));
		table.addFooter(consultarButton);
		
		consultarButton = new Button("Excel", new Action("relProcessamentos"));
		consultarButton.getAction().add("excel", true);
		consultarButton.setEnabled(agendas.size() > 0);
		table.addFooter(consultarButton);
		
		mainTable.add(table);
		
		Map<String,Agenda> agendasAgrupadas = new TreeMap<String, Agenda>();
		
		Agenda agenda;
		Evento evento;
		Processamento p;
		
		for(Iterator<Agenda> i = this.agendas.iterator() ; i.hasNext() ; )
		{
			agenda = i.next();
			
			p = agenda.obterProcessamento();
			
			agendasAgrupadas.put(p.obterDataPrevistaInicio()+"_"+p.obterId(), agenda);
		}
		
		int qtde = agendasAgrupadas.size();
		
		if(qtde > 0)
		{
			table = new Table(6);
			table.setWidth("50%");
			table.addSubtitle(qtde + " Validaciones");
			table.addStyle(Table.STYLE_ALTERNATE);
			
			table.setNextHAlign(Table.HALIGN_CENTER);
			table.addHeader("Aseguradora");
			table.setNextHAlign(Table.HALIGN_CENTER);
			table.addHeader("Mes");
			table.setNextHAlign(Table.HALIGN_CENTER);
			table.addHeader("Año");
			table.setNextHAlign(Table.HALIGN_CENTER);
			table.addHeader("Tipo");
			table.setNextHAlign(Table.HALIGN_CENTER);
			table.addHeader("Notificación");
			table.setNextHAlign(Table.HALIGN_CENTER);
			table.addHeader("Fecha Validación");
			
			for(Iterator<Agenda> i = agendasAgrupadas.values().iterator() ; i.hasNext() ; )
			{
				agenda = i.next();
				evento = agenda.obterEvento();
				p = agenda.obterProcessamento();
				
				String tipoAgenda = p.obterTipo();
				String mesStr = agenda.obterMes() + "";
				int mes = agenda.obterMes();
				if(new Integer(mes).toString().length() == 1)
					mesStr = "0" + mes;
				
				if(tipoAgenda.equals("Contabil"))
					tipoAgenda = "Contable";
				else if(tipoAgenda.equals("Livro"))
					tipoAgenda = "Libro";
				
				String mensagem = "";
				
				boolean agendaComErro = agenda.agendaComErro();
				if(agendaComErro)
				{
					if(evento == null)
						mensagem = agenda.obterMensagem();
					else
						mensagem = "Notificación de Error de Validación";
				}
				else
					mensagem = "Archivo del mes fue incluido satisfactoriamente";
				
				table.add(agenda.obterAseguradora().obterNome());
				table.setNextHAlign(Table.HALIGN_CENTER);
				table.add(mesStr);
				table.setNextHAlign(Table.HALIGN_CENTER);
				table.add(new Label(agenda.obterAno()));
				table.setNextHAlign(Table.HALIGN_CENTER);
				table.add(tipoAgenda);
				if(evento!=null)
				{
					Link link = new Link(mensagem, new Action("visualizarEvento"));
					link.getAction().add("id", evento.obterId());
					link.setNovaJanela(true);
					
					table.add(link);
				}
				else
					table.add(mensagem);
				
				Link link = new Link(new SimpleDateFormat("dd/MM/yyyy").format(p.obterDataPrevistaInicio()), new Action("visualizarEvento"));
				link.getAction().add("id", p.obterId());
				link.setNovaJanela(true);
				
				table.setNextHAlign(Table.HALIGN_CENTER);
				table.add(link);
				
			}
			
			mainTable.add(table);
		}
		
		
		return mainTable;
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
		return new Label("Validaciones");
	}

	public Entidade getOrigemMenu() throws Exception
	{
		return null;
	}
}