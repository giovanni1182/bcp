package com.gvs.crm.view;

import java.util.Collection;
import java.util.Date;
import java.util.Locale;
import java.util.Properties;

import com.gvs.crm.component.PeriodoDatasBlock;
import com.gvs.crm.component.SimNao2Select;
import com.gvs.crm.model.Aseguradora;
import com.gvs.crm.model.CRMModelManager;
import com.gvs.crm.model.Entidade;
import com.gvs.crm.model.EntidadeHome;

import infra.control.Action;
import infra.security.User;
import infra.view.Block;
import infra.view.Button;
import infra.view.Check;
import infra.view.InputInteger;
import infra.view.Label;
import infra.view.Space;
import infra.view.Table;
import infra.view.View;

public class RelAlertaTrempanoView extends PortalView
{
	private Date dataInicio, dataFim;
	private int anoInicio, anoFim;
	private Collection<Aseguradora> aseguradorasMarcadas;
	private boolean todas, consulta;
	private String anoFiscal;
	private boolean alerta2;
	
	public RelAlertaTrempanoView(Date dataInicio, Date dataFim, int anoInicio, int anoFim, Collection<Aseguradora> aseguradorasMarcadas, boolean todas, boolean consulta, String anoFiscal, boolean alerta2)
	{
		this.dataInicio = dataInicio;
		this.dataFim = dataFim;
		this.anoInicio = anoInicio;
		this.anoFim = anoFim;
		this.aseguradorasMarcadas = aseguradorasMarcadas;
		this.todas = todas;
		this.consulta = consulta;
		this.anoFiscal = anoFiscal;
		this.alerta2 = alerta2;
	}
	
	public View getBody(User user, Locale locale, Properties properties) throws Exception
	{
		CRMModelManager mm = new CRMModelManager(user);
		EntidadeHome home = (EntidadeHome) mm.getHome("EntidadeHome");
		
		Collection<Entidade> aseguradoras = home.obterAseguradorasSemCoaseguradora();
		 
		Table mainTable = new Table(1);
		mainTable.setWidth("100%");
		
		Table table = new Table(2);
		
		table.addHeader("Mes:");
		table.add(new PeriodoDatasBlock("dataInicio", dataInicio, "dataFim", dataFim, true));
		table.addHeader("Año:");
		
		Block block = new Block(Block.HORIZONTAL);
		block.add(new InputInteger("anoInicio", anoInicio, 4));
		Label l = new Label("hasta");
		l.setBold(true);
		block.add(new Space(2));
		block.add(l);
		block.add(new Space(2));
		block.add(new InputInteger("anoFim", anoFim, 4));
		table.add(block);
		
		table.addHeader("Año Fiscal:");
		table.add(new SimNao2Select("anoFiscal", anoFiscal));
		
		mainTable.add(table);
		
		Aseguradora aseg;
		long id;
		Check check;
		String key;
		table = new Table(2);
		table.addSubtitle("");
		table.addStyle(Table.STYLE_ALTERNATE);
		
		table.setNextHAlign(Table.HALIGN_CENTER);
		table.setNextColSpan(table.getColumns());
		table.addHeader("Aseguradoras");
		
		table.setNextColSpan(table.getColumns());
		Button todasButton = new Button("Todas", new Action("calculoAlertaTrempano"));
		todasButton.getAction().add("todas", !todas);
		todasButton.getAction().add("view", true);
		table.add(todasButton);
		
		for(Entidade e : aseguradoras)
		{
			aseg = (Aseguradora) e;
			
			id = aseg.obterId();
			
			key = new Long(id).toString();
			
			block = new Block(Block.HORIZONTAL);
			
			if(consulta)
				check = new Check("aseguradoraId", key , aseguradorasMarcadas.contains(aseg));
			else
			{
				if(todas)
					check = new Check("aseguradoraId", key , true);
				else
					
					check = new Check("aseguradoraId", key , false);
			}
			
			block.add(check);
			block.add(new Space(2));
			l = new Label(aseg.obterNome());
			block.add(l);
			
			table.add(block);
		}
		
		Button consultarButton = new Button("Generar Excel", new Action("calculoAlertaTrempano"));
		consultarButton.getAction().add("alerta2", this.alerta2);
		table.addFooter(consultarButton);
		
		mainTable.add(table);
		
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
		return new Label("Alerta Temprana");
	}

	public Entidade getOrigemMenu() throws Exception
	{
		return null;
	}
}