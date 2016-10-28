package com.gvs.crm.component;

import java.util.Date;

import infra.view.Block;
import infra.view.InputDate;
import infra.view.Label;
import infra.view.Space;

public class PeriodoDatasBlock extends Block
{
	public PeriodoDatasBlock(String nome1, Date dataInicio, String nome2, Date dataFim, boolean mesAno) throws Exception
	{
		super(Block.HORIZONTAL);
		
		Label l = new Label("hasta");
		l.setBold(true);
		
		this.add(new InputDate(nome1, dataInicio, mesAno));
		this.add(new Space(2));
		this.add(l);
		this.add(new Space(2));
		this.add(new InputDate(nome2, dataFim, mesAno));
	}
	
	public PeriodoDatasBlock(String nome1, Date dataInicio, String nome2, Date dataFim) throws Exception
	{
		super(Block.HORIZONTAL);
		
		Label l = new Label("hasta");
		l.setBold(true);
		
		this.add(new InputDate(nome1, dataInicio));
		this.add(new Space(2));
		this.add(l);
		this.add(new Space(2));
		this.add(new InputDate(nome2, dataFim));
	}
}
