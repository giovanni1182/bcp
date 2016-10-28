package com.gvs.crm.view;

import java.util.Collection;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

import com.gvs.crm.model.Entidade;
import com.gvs.crm.model.Evento;
import com.gvs.crm.model.Processamento;
import com.gvs.crm.model.Processamento.Agenda;

import infra.control.Action;
import infra.security.User;
import infra.view.Button;
import infra.view.Link;
import infra.view.Table;
import infra.view.View;

public class ProcessamentoView extends EventoAbstratoView
{
	public View execute(User arg0, Locale arg1, Properties arg2) throws Exception
	{
		Processamento processamento = (Processamento) this.obterEvento();
		
		boolean instrumentoOuContabil = processamento.obterTipo().equals("Instrumento") || processamento.obterTipo().equals("Contabil");
		
		Table table = new Table(5);
		table.setWidth("50%");
		table.addStyle(Table.STYLE_ALTERNATE);
		
		Collection<Agenda> agendas = processamento.obterAgendas();
		Map<String,Agenda> agendasSucesso = new TreeMap<>();
		
		for(Agenda agenda : agendas)
		{
			if(!agenda.agendaComErro())
				agendasSucesso.put(agenda.obterNomeArquivo(), agenda);
		}
		
		int qtde = agendas.size();
		
		if(qtde > 0)
		{
			table.addSubtitle(qtde + " Validaciones");
			
			table.add("");
			table.setNextHAlign(Table.HALIGN_CENTER);
			table.addHeader("Archivo");
			table.setNextHAlign(Table.HALIGN_CENTER);
			table.addHeader("Aseguradora");
			table.setNextHAlign(Table.HALIGN_CENTER);
			table.addHeader("Mes/Año");
			table.setNextHAlign(Table.HALIGN_CENTER);
			table.addHeader("Mensaje");
			String nomeArquivo,nomeAseguradora,mesStr;
			int mes,ano;
			Evento evento;
			Link link;
			Entidade aseguradora = null;
			Button forcarButton;
			
			for(Agenda agenda : agendas)
			{
				nomeArquivo = agenda.obterNomeArquivo();
				aseguradora = agenda.obterAseguradora();
				nomeAseguradora = aseguradora.obterNome();
				mes = agenda.obterMes();
				ano = agenda.obterAno();
				
				mesStr = "";
				if(new Integer(mes).toString().length() == 1)
					mesStr = "0"+mes;
				else
					mesStr = new Integer(mes).toString();
				
				mesStr+="/"+ano;
				
				if(agenda.agendaComErro() && instrumentoOuContabil && agenda.podeForcar() && !agendasSucesso.containsKey(nomeArquivo))
				{
					forcarButton = new Button("Forzar Procesamiento", new Action("forcarProcessamento"));
					forcarButton.getAction().add("arquivo", nomeArquivo);
					forcarButton.getAction().add("id", processamento.obterId());
					
					table.add(forcarButton);
					
				}
				else
					table.add("");
				
				evento = agenda.obterEvento();
				table.setNextHAlign(Table.HALIGN_CENTER);
				if(evento!=null)
				{
					link = new Link(nomeArquivo, new Action("visualizarEvento"));
					link.getAction().add("id", evento.obterId());
					link.setNovaJanela(true);
					
					table.add(link);
				}
				else
					table.add(nomeArquivo);
				
				link = new Link(nomeAseguradora, new Action("visualizarDetalhesEntidade"));
				link.getAction().add("id", aseguradora.obterId());
				table.add(link);
				table.setNextHAlign(Table.HALIGN_CENTER);
				table.add(mesStr);
				
				table.add(agenda.obterMensagem());
			}
		}
		
		Button excluirButton = new Button("Excluir", new Action("excluirEvento"));
		excluirButton.getAction().add("id", processamento.obterId());
		
		table.addFooter(excluirButton);
		
		return table;
	}
}