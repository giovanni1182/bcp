package com.gvs.crm.view;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.util.Properties;

import org.joda.time.DateTime;
import org.joda.time.Seconds;

import com.gvs.crm.component.EntidadePopup;
import com.gvs.crm.model.Entidade;
import com.gvs.crm.model.Log;
import com.gvs.crm.model.Usuario;

import infra.control.Action;
import infra.security.User;
import infra.view.Block;
import infra.view.Button;
import infra.view.InputDate;
import infra.view.Label;
import infra.view.Space;
import infra.view.Table;
import infra.view.View;

public class LogsView extends PortalView 
{
	private Usuario entidade;
	private Usuario usuario;
	private Date dataInicio, dataFim;
	private Collection<Log> logs;
	
	public LogsView(Usuario entidade, Usuario usuario, Date dataInicio, Date dataFim, Collection<Log> logs) throws Exception 
	{
		this.entidade = entidade;
		this.usuario = usuario;
		this.dataInicio = dataInicio;
		this.dataFim = dataFim;
		this.logs = logs;
	}

	public View getBody(User user, Locale locale, Properties properties)throws Exception 
	{
		Table table = new Table(2);
		
		table.addHeader("Usuario:");
		table.add(new EntidadePopup("usuarioId", "usuarioNome", usuario, "usuario", true));
		table.addHeader("Fecha:");
		Block block = new Block(Block.HORIZONTAL);
		block.add(new InputDate("dataInicio", this.dataInicio));
		block.add(new Space(2));
		block.add(new Label("hasta"));
		block.add(new Space(2));
		block.add(new InputDate("dataFim", this.dataFim));
		table.add(block);
		
		Button buscarButton = new Button("Buscar", new Action("exibirLogs"));
		table.setNextColSpan(table.getColumns());
		table.add(new Space());
		table.setNextColSpan(table.getColumns());
		table.setNextHAlign(Table.HALIGN_CENTER);
		table.add(buscarButton);
		
		Table table2 = new Table(4);
		table2.setWidth("100%");
		
		table2.addSubtitle(this.logs.size()  + " Logs");
		table2.addStyle(Table.STYLE_ALTERNATE);
		
		if(this.logs.size() > 0)
		{
			table2.setNextHAlign(Table.HALIGN_CENTER);
			table2.addHeader("Usuario");
			table2.setNextHAlign(Table.HALIGN_CENTER);
			table2.addHeader("Fecha Inicio");
			table2.setNextHAlign(Table.HALIGN_CENTER);
			table2.addHeader("Fecha Fin");
			table2.setNextHAlign(Table.HALIGN_CENTER);
			table2.addHeader("Duración");
			
			int totalHoras = 0;
			int totalMinutos = 0;
			int totalSegundos = 0;
			
			for(Iterator<Log> i = this.logs.iterator() ; i.hasNext() ; )
			{
				Log log = i.next();
				
				Date dataInicio2 = log.obterDataPrevistaInicio();
				Date dataFim2 = log.obterDataPrevistaConclusao();
				
				table2.add(log.obterOrigem().obterNome());
				table2.setNextHAlign(Table.HALIGN_CENTER);
				
				table2.add(new Label(dataInicio2, "dd/MM/yyyy HH:mm:ss"));
				table2.setNextHAlign(Table.HALIGN_CENTER);
				table2.add(new Label(dataFim2, "dd/MM/yyyy HH:mm:ss"));
				
				int ano = Integer.parseInt(new SimpleDateFormat("yyyy").format(dataInicio2));
				int mes = Integer.parseInt(new SimpleDateFormat("MM").format(dataInicio2));
				int dia = Integer.parseInt(new SimpleDateFormat("dd").format(dataInicio2));
				int hora = Integer.parseInt(new SimpleDateFormat("HH").format(dataInicio2));
				int minuto = Integer.parseInt(new SimpleDateFormat("mm").format(dataInicio2));
				int segundo = Integer.parseInt(new SimpleDateFormat("ss").format(dataInicio2));
				
				int ano2 = Integer.parseInt(new SimpleDateFormat("yyyy").format(dataFim2));
				int mes2 = Integer.parseInt(new SimpleDateFormat("MM").format(dataFim2));
				int dia2 = Integer.parseInt(new SimpleDateFormat("dd").format(dataFim2));
				int hora2 = Integer.parseInt(new SimpleDateFormat("HH").format(dataFim2));
				int minuto2 = Integer.parseInt(new SimpleDateFormat("mm").format(dataFim2));
				int segundo2 = Integer.parseInt(new SimpleDateFormat("ss").format(dataFim2));
				
				DateTime inicio2 = new DateTime(ano,mes,dia,hora,minuto,segundo,0);
				DateTime fim2 = new DateTime(ano2,mes2,dia2,hora2,minuto2,segundo2,0);
				
				int segundos = Seconds.secondsBetween(inicio2, fim2).getSeconds();
				
				int horaT = (int)(segundos / (60 * 60)); 
				int minT = (int)((segundos - (horaT * 60 * 60)) / 60);
				int segT = (int)(segundos - (horaT * 60 * 60) - (minT * 60));
				
				totalHoras+=horaT;
				totalMinutos+=minT;
				totalSegundos+= segT;
				
				table2.setNextHAlign(Table.HALIGN_RIGHT);
				table2.add(horaT + "Hr "+ minT+"min " + segT+"sec");
			}
			
			totalSegundos+=(totalMinutos * 60) + (totalHoras * 3600);
			
			int hora = (int)(totalSegundos / (60 * 60)); 
			int min = (int)((totalSegundos - (hora * 60 * 60)) / 60);
			int seg = (int)(totalSegundos - (hora * 60 * 60) - (min * 60));
			
			table2.setNextHAlign(Table.HALIGN_CENTER);
			table2.addHeader("TOTAL");
			table2.add("");
			table2.add("");
			table2.setNextHAlign(Table.HALIGN_RIGHT);
			table2.addHeader(hora + "Hr "+ min+"min " + seg +"sec");
			
			table.setNextColSpan(table.getColumns());
			table.add(table2);
			
		}

		return table;
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
		return new Label("Logs");
	}

	public Entidade getOrigemMenu() throws Exception
	{
		return this.entidade;
	}
}