package com.gvs.crm.view;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.util.Properties;

import com.gvs.crm.component.Border;
import com.gvs.crm.component.EntidadePopup;
import com.gvs.crm.model.Aseguradora;
import com.gvs.crm.model.AuxiliarSeguro;

import infra.control.Action;
import infra.security.User;
import infra.view.BasicView;
import infra.view.Block;
import infra.view.Button;
import infra.view.InputDate;
import infra.view.Label;
import infra.view.Link;
import infra.view.Space;
import infra.view.Table;
import infra.view.View;

public class ListaProducaoAgentesView extends BasicView
{
	private AuxiliarSeguro auxiliar;
	private Collection<Aseguradora> aseguradoras;
	private Date dataInicio;
	private Date dataFim;
	private boolean lista;
	private boolean auxiliarSeguro;
	
	public ListaProducaoAgentesView(AuxiliarSeguro auxiliar,Date dataInicio, Date dataFim,boolean lista, Collection<Aseguradora> aseguradoras, boolean auxiliarSeguro) throws Exception
	{
		this.auxiliar = auxiliar;
		this.aseguradoras = aseguradoras;
		this.dataInicio = dataInicio;
		this.dataFim = dataFim;
		this.lista = lista;
		this.auxiliarSeguro = auxiliarSeguro;
	}

	public View execute(User user, Locale locale, Properties properties) throws Exception
	{
		Table table2 = new Table(1);
		table2.setWidth("100%");
		if(auxiliarSeguro)
			table2.addSubtitle("Lista de productividade Agente Seguro");
		else
			table2.addSubtitle("Lista de productividade Corredor de Seguros");
		
		Table table = new Table(8);
		table.setWidth("100%");
		table.addStyle(Table.STYLE_ALTERNATE);

		table.setNextColSpan(table.getColumns());

		Block block = new Block(Block.HORIZONTAL);

		Label label = null;
		
		if(auxiliarSeguro)
			label = new Label("Agente:");
		else
			label = new Label("Corredor:");
		
		label.setBold(true);

		Label label2 = new Label("Periodo:");
		label2.setBold(true);

		Label label3 = new Label("hasta el:");
		label3.setBold(true);

		block.add(label);
		block.add(new Space(2));
		block.add(new EntidadePopup("auxiliarId", "auxiliarNome", auxiliar,"AuxiliarSeguro", true));
		block.add(new Space(10));
		block.add(label2);
		block.add(new Space(2));
		block.add(new InputDate("dataInicio", dataInicio));
		block.add(new Space(2));
		block.add(label3);
		block.add(new Space(2));
		block.add(new InputDate("dataFim", dataFim));
		block.add(new Space(5));
		table2.add(block);
		
		Block block2 = new Block(Block.HORIZONTAL);

		Button button = new Button("Listar", new Action("visualizarProdutividadeAuxiliar"));
		button.getAction().add("lista", true);
		button.getAction().add("auxiliar", this.auxiliarSeguro);

		Button button2 = new Button("Volver", new Action("visualizarInspecaoSitu"));
		
		Button button3 = new Button("Generar Excel", new Action("visualizarProdutividadeAuxiliar"));
		button3.getAction().add("lista", true);
		button3.getAction().add("excel", true);
		button3.getAction().add("auxiliar", this.auxiliarSeguro);

		block2.add(button);
		block2.add(new Space(4));
		block2.add(button3);
		block2.add(new Space(4));
		block2.add(button2);
		
		table2.add(new Space());
		table2.setNextHAlign(Table.HALIGN_CENTER);
		table2.add(block2);

		if (this.lista)
		{
			double totalGeralComissaoGs = 0;
			double totalGeralComissaoMe = 0;
			double totalGeralPrima = 0;
			double totalGeralPremio = 0;
			double totalGeralCapitalGs = 0;
			double totalGeralCapitalMe = 0;
			
			table.addSubtitle("");
			DecimalFormat format = new DecimalFormat("#,##0.00");
			
			for (Iterator<Aseguradora> i = aseguradoras.iterator(); i.hasNext();)
			{
				Aseguradora aseguradora = i.next();
				
				double totalComissaoGs = 0;
				double totalComissaoMe = 0;
				double totalPrima = 0;
				double totalPremio = 0;
				double totalCapitalGs = 0;
				double totalCapitalMe = 0;
				
				Calendar c = Calendar.getInstance();
				c.setTime(dataInicio);
				
				table.setNextHAlign(Table.HALIGN_CENTER);
				table.addHeader(aseguradora.obterNome());
				table.setNextHAlign(Table.HALIGN_CENTER);
				table.addHeader("Mes/Año");
				table.setNextHAlign(Table.HALIGN_CENTER);
				table.addHeader("Comissão Gs");
				table.setNextHAlign(Table.HALIGN_CENTER);
				table.addHeader("Comissão ME");
				table.setNextHAlign(Table.HALIGN_CENTER);
				table.addHeader("Prima");
				table.setNextHAlign(Table.HALIGN_CENTER);
				table.addHeader("Premio");
				table.setNextHAlign(Table.HALIGN_CENTER);
				table.addHeader("Capital  Gs");
				table.setNextHAlign(Table.HALIGN_CENTER);
				table.addHeader("Capital  ME");
				
				while(c.getTime().compareTo(dataFim)<=0)
				{
					double comissaoGs = 0;
					double comissaoMe = 0;
					double prima = 0;
					double premio = 0;
					double capitalGs = 0;
					double capitalMe = 0;
					
					String diaInicio = new Integer(c.getActualMinimum(Calendar.DAY_OF_MONTH)).toString();
					String diaFim = new Integer(c.getActualMaximum(Calendar.DAY_OF_MONTH)).toString();
					
					if(diaInicio.toString().length()==1)
						diaInicio = "0" + diaInicio;
					
					String mes = new SimpleDateFormat("MM/yyyy").format(c.getTime());
					Date dataInicio2 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(diaInicio+"/"+mes +" 00:00:00");
					Date dataFim2 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(diaFim+"/"+mes +" 23:59:59");
					
					//System.out.println(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(dataInicio2) + " - " + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(dataFim2));
					
					Collection<String> dados = aseguradora.obterApolicesAcumuladas(auxiliar, dataInicio2, dataFim2, this.auxiliarSeguro);
					
					for(Iterator<String> j = dados.iterator() ; j.hasNext() ; )
					{
						String linhaSuja = j.next();
						
						String[] linha = linhaSuja.split(";");
						
						comissaoGs = Double.parseDouble(linha[0]);
						comissaoMe = Double.parseDouble(linha[1]);
						prima = Double.parseDouble(linha[2]);
						premio = Double.parseDouble(linha[3]);
						capitalGs = Double.parseDouble(linha[4]);
						capitalMe = Double.parseDouble(linha[5]);
						
						totalComissaoGs+=comissaoGs;
						totalComissaoMe+=comissaoMe;
						totalPrima+=prima;
						totalPremio+=premio;
						totalCapitalGs+=capitalGs;
						totalCapitalMe+=capitalMe;
						
						totalGeralComissaoGs+=comissaoGs;
						totalGeralComissaoMe+=comissaoMe;
						totalGeralPrima+=prima;
						totalGeralPremio+=premio;
						totalGeralCapitalGs+=capitalGs;
						totalGeralCapitalMe+=capitalMe;
					}
					
					table.add("");
					table.setNextHAlign(Table.HALIGN_CENTER);
					Link link = new Link(new Label(c.getTime(), "MM/yyyy"), new Action("visualizarProdutividadeAuxiliar"));
					link.getAction().add("lista", true);
					link.getAction().add("apolices", true);
					link.getAction().add("aseguradoraId", aseguradora.obterId());
					link.getAction().add("dataInicio2", dataInicio2);
					link.getAction().add("dataFim2", dataFim2);
					link.getAction().add("auxiliar", this.auxiliarSeguro);
					table.add(link);
					table.setNextHAlign(Table.HALIGN_RIGHT);
					table.add(format.format(comissaoGs));
					table.setNextHAlign(Table.HALIGN_RIGHT);
					table.add(format.format(comissaoMe));
					table.setNextHAlign(Table.HALIGN_RIGHT);
					table.add(format.format(prima));
					table.setNextHAlign(Table.HALIGN_RIGHT);
					table.add(format.format(premio));
					table.setNextHAlign(Table.HALIGN_RIGHT);
					table.add(format.format(capitalGs));
					table.setNextHAlign(Table.HALIGN_RIGHT);
					table.add(format.format(capitalMe));
					
					c.add(Calendar.MONTH, 1);
				}
				
				table.add("");
				table.setNextHAlign(Table.HALIGN_CENTER);
				table.addHeader("TOTAL");
				table.setNextHAlign(Table.HALIGN_RIGHT);
				table.addHeader(format.format(totalComissaoGs));
				table.setNextHAlign(Table.HALIGN_RIGHT);
				table.addHeader(format.format(totalComissaoMe));
				table.setNextHAlign(Table.HALIGN_RIGHT);
				table.addHeader(format.format(totalPrima));
				table.setNextHAlign(Table.HALIGN_RIGHT);
				table.addHeader(format.format(totalPremio));
				table.setNextHAlign(Table.HALIGN_RIGHT);
				table.addHeader(format.format(totalCapitalGs));
				table.setNextHAlign(Table.HALIGN_RIGHT);
				table.addHeader(format.format(totalCapitalMe));
			}
			
			table.add("");
			table.setNextHAlign(Table.HALIGN_CENTER);
			table.addHeader("TOTAL GENERAL");
			table.setNextHAlign(Table.HALIGN_RIGHT);
			table.addHeader(format.format(totalGeralComissaoGs));
			table.setNextHAlign(Table.HALIGN_RIGHT);
			table.addHeader(format.format(totalGeralComissaoMe));
			table.setNextHAlign(Table.HALIGN_RIGHT);
			table.addHeader(format.format(totalGeralPrima));
			table.setNextHAlign(Table.HALIGN_RIGHT);
			table.addHeader(format.format(totalGeralPremio));
			table.setNextHAlign(Table.HALIGN_RIGHT);
			table.addHeader(format.format(totalGeralCapitalGs));
			table.setNextHAlign(Table.HALIGN_RIGHT);
			table.addHeader(format.format(totalGeralCapitalMe));
		}
		
		table2.add(table);
		Border border = new Border(table2);

		return border;
	}
}
