package com.gvs.crm.view;

import java.util.Iterator;

import com.gvs.crm.component.MargemSolvenciaMesAnoSelect;
import com.gvs.crm.model.Aseguradora;

import infra.control.Action;
import infra.view.Button;
import infra.view.InputDouble;
import infra.view.Label;
import infra.view.Table;

public class AseguradoraMargemSolvenciaView extends Table
{
	public AseguradoraMargemSolvenciaView(Aseguradora aseguradora) throws Exception
	{
		super(2);
		this.addSubtitle("Nuevo Valor");
		
		this.addHeader("Mes/Año:");
		this.add(new MargemSolvenciaMesAnoSelect("mesAno",null));
		this.addHeader("Valor:");
		this.add(new InputDouble("valor",0,5));
		
		Button incluirButton = new Button("Agregar",new Action("incluirMargemSolvencia"));
		incluirButton.getAction().add("id", aseguradora.obterId());
		
		this.setNextHAlign(HALIGN_RIGHT);
		this.add(incluirButton);
		this.add("");
		
		if(aseguradora.obterMargensSolvencia().size() > 0)
		{
			this.addSubtitle("Margens Agregadas");
			
			Table table = new Table(3);
			table.addStyle(STYLE_ALTERNATE);
			
			table.addHeader("");
			table.addHeader("Mes/Año");
			table.addHeader("Valor");
			
			for(Iterator i = aseguradora.obterMargensSolvencia().values().iterator() ; i.hasNext() ; )
			{
				Aseguradora.MargemSolvencia margem = (Aseguradora.MargemSolvencia) i.next();
				
				table.add("");
				table.add(margem.obterMesAno());
				table.add(new Label(margem.obterValor()));
			}
			
			this.setNextColSpan(getColumns());
			this.add(table);
		}
	}
}
