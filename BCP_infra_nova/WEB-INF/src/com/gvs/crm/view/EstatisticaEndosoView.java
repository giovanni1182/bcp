package com.gvs.crm.view;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

import com.gvs.crm.component.AseguradorasSelect2;
import com.gvs.crm.model.ApoliceHome;
import com.gvs.crm.model.Aseguradora;
import com.gvs.crm.model.CRMModelManager;
import com.gvs.crm.model.Entidade;
import com.gvs.crm.model.EntidadeHome;

import infra.control.Action;
import infra.security.User;
import infra.view.Block;
import infra.view.Button;
import infra.view.InputDate;
import infra.view.Label;
import infra.view.Space;
import infra.view.Table;
import infra.view.View;

public class EstatisticaEndosoView extends PortalView
{
	private Aseguradora aseguradora;
	private Date dataFim;
	private boolean listar;
	
	public EstatisticaEndosoView(Aseguradora aseguradora, Date dataFim, boolean listar) throws Exception
	{
		this.aseguradora = aseguradora;
		this.dataFim = dataFim;
		this.listar = listar;
	}
	
	public View getBody(User user, Locale locale, Properties properties) throws Exception
	{
		Table table = new Table(2);
		table.setWidth("100%");
		
		table.setNextWidth("8%");
		table.addHeader("Aseguradora:");
		table.add(new AseguradorasSelect2("aseguradoraId",this.aseguradora, false, true));
		table.addHeader("Mes/Año:");
		table.add(new InputDate("dataInicio",this.dataFim));
		
		Block block = new Block(Block.HORIZONTAL);
		Button consultarButton = new Button("Consultar en la Pantalha",new Action("estatisticaEndoso"));
		block.add(new Space(25));
		block.add(consultarButton);
		block.add(new Space(4));
		consultarButton = new Button("Generar Excel",new Action("estatisticaEndoso"));
		consultarButton.getAction().add("excel", true);
		block.add(consultarButton);
		table.setNextColSpan(table.getColumns());
		table.add(new Space());
		table.setNextColSpan(table.getColumns());
		table.add(block);
		
		table.addSubtitle("");
		
		Map<String,Double> ajustesMap = new TreeMap<String,Double>();
				
		if(listar)
		{
			CRMModelManager mm = new CRMModelManager(user);
			ApoliceHome home = (ApoliceHome) mm.getHome("ApoliceHome");
			EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
			Collection<Aseguradora> aseguradoras = new ArrayList<Aseguradora>();
			
			Calendar c = Calendar.getInstance();
			c.setTime(dataFim);
			
			String dataFimStr = c.getActualMaximum(Calendar.DAY_OF_MONTH) +"/"+ new SimpleDateFormat("MM").format(dataFim) +"/"+ new SimpleDateFormat("yyyy").format(dataFim);
			dataFim = new SimpleDateFormat("dd/MM/yyyy").parse(dataFimStr);
			
			c.add(Calendar.MONTH, -11);
			Date dataInicio = c.getTime();
			String dataInicioStr = c.getActualMinimum(Calendar.DAY_OF_MONTH) +"/"+ new SimpleDateFormat("MM").format(dataInicio) +"/"+ new SimpleDateFormat("yyyy").format(dataInicio);
			dataInicio = new SimpleDateFormat("dd/MM/yyyy").parse(dataInicioStr);
			
			//System.out.println("Inicio " + new SimpleDateFormat("dd/MM/yyyy").format(dataInicio));
			//System.out.println("Fim " + new SimpleDateFormat("dd/MM/yyyy").format(dataFim));
			
			Table table2 = new Table(13);
			table2.setWidth("100%");
			table2.addStyle(Table.STYLE_ALTERNATE);
			
			if(this.aseguradora == null)
				aseguradoras.addAll(entidadeHome.obterAseguradoras());
			else
				aseguradoras.add(aseguradora);
			
			c.setTime(dataInicio);
			
			for(Iterator<Aseguradora> i = aseguradoras.iterator() ; i.hasNext() ; )
			{
				Aseguradora aseg = i.next();
				
				table2.addSubtitle(aseg.obterNome());
				
				table2.add("");
				while(c.getTime().compareTo(dataFim)<=0)
				{
					table2.setNextHAlign(Table.HALIGN_CENTER);
					table2.addHeader(new SimpleDateFormat("MM/yyyy").format(c.getTime()));
					c.add(Calendar.MONTH, 1);
				}
				
				c.setTime(dataInicio);
				table2.add("Cantidad");
				while(c.getTime().compareTo(dataFim)<=0)
				{
					String mesAno = (new SimpleDateFormat("MM/yyyy").format(c.getTime()));
					
					String linhaSuja = home.obterDadosAquivoEndoso(aseg, c.getTime());
					
					String[] linha = linhaSuja.split(";");
					
					String qtde = linha[0];
					double montante = Double.parseDouble(linha[1]);
					ajustesMap.put(mesAno, montante);
					
					table2.setNextHAlign(Table.HALIGN_CENTER);
					table2.add(qtde);
					
					c.add(Calendar.MONTH, 1);
				}
				
				c.setTime(dataInicio);
				table2.add("Ajuste de Prima en Gs");
				while(c.getTime().compareTo(dataFim)<=0)
				{
					String mesAno = (new SimpleDateFormat("MM/yyyy").format(c.getTime()));
					
					double montante = 0;
					if(ajustesMap.containsKey(mesAno))
						montante = ajustesMap.get(mesAno);
					
					table2.setNextHAlign(Table.HALIGN_CENTER);
					table2.add(new Label(montante,"#,##0.00"));
					
					c.add(Calendar.MONTH, 1);
				}
				
				c.setTime(dataInicio);
			}
			
			table.setNextColSpan(table.getColumns());
			table.add(table2);
		}
		
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
		return new Label("Estadística Endoso o Suplemento");
	}
}
