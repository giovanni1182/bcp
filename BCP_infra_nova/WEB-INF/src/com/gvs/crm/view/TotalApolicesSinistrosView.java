package com.gvs.crm.view;

import java.io.File;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.util.Properties;

import com.gvs.crm.component.AseguradorasSelect2;
import com.gvs.crm.component.PlanoSecaoSelect;
import com.gvs.crm.component.SituacaoSeguroSelect;
import com.gvs.crm.model.AgendaMovimentacao;
import com.gvs.crm.model.Aseguradora;
import com.gvs.crm.model.Entidade;

import infra.control.Action;
import infra.security.User;
import infra.view.Block;
import infra.view.Button;
import infra.view.Check;
import infra.view.InputDate;
import infra.view.Label;
import infra.view.Link;
import infra.view.Space;
import infra.view.Table;
import infra.view.View;

public class TotalApolicesSinistrosView extends PortalView
{
	private Aseguradora aseguradora;
	private Date dataInicio;
	private Date dataFim;
	private boolean mostrarTela;
	private boolean gerarArquivo;
	private File file;
	private Collection aseguradoras;
	private String secao;
	private String situacaoSeguro;
	
	public TotalApolicesSinistrosView(Aseguradora aseguradora, Date dataInicio, Date dataFim, boolean mostrarTela, boolean gerarArquivo,File file, Collection aseguradoras, String secao, String situacaoSeguro) throws Exception
	{
		this.aseguradora = aseguradora;
		this.dataInicio = dataInicio;
		this.dataFim = dataFim;
		this.mostrarTela = mostrarTela;
		this.gerarArquivo = gerarArquivo;
		this.file = file;
		this.aseguradoras = aseguradoras;
		this.secao = secao;
		this.situacaoSeguro = situacaoSeguro;
	}
	
	public View getBody(User user, Locale locale, Properties properties) throws Exception
	{
		Table table = new Table(2);
		table.addSubtitle("");
		table.setWidth("90%");
		
		table.addHeader("Aseguradora:");
		table.add(new AseguradorasSelect2("aseguradora",this.aseguradora, false, true));
		table.addHeader("Sección");
		table.add(new PlanoSecaoSelect("secao", this.secao, null));
		table.addHeader("Situacion del Seguro:");
		table.add(new SituacaoSeguroSelect("situacaoSeguro", this.situacaoSeguro));
		Block block = new Block(Block.HORIZONTAL);
		table.addHeader("Periodo:");
		block.add(new InputDate("dataInicio",dataInicio));
		block.add(new Space(2));
		Label label = new Label("hasta");
		label.setBold(true);
		block.add(label);
		block.add(new Space(2));
		block.add(new InputDate("dataFim",dataFim));
		table.add(block);
		
		table.addSubtitle("Opciones");
		
		Block block2 = new Block(Block.HORIZONTAL);
		block2.add(new Check("tela", "true",mostrarTela));
		block2.add(new Space(2));
		block2.add(new Label("Visualizar en la pantalia"));
		table.setNextColSpan(table.getColumns());
		table.add(block2);
		
		Block block3 = new Block(Block.HORIZONTAL);
		block3.add(new Check("arquivo", "true",gerarArquivo));
		block3.add(new Space(2));
		block3.add(new Label("Generar archivo(.txt)"));
		table.setNextColSpan(table.getColumns());
		table.add(block3);
		
		Button button = new Button("Generar",new Action("visualizarTotalApoliceSinistro"));
		
		table.setNextColSpan(table.getColumns());
		table.setNextHAlign(Table.HALIGN_CENTER);
		table.add(button);
		
		if(this.file!=null)
		{
			table.addSubtitle("Archivo Generado");
			table.addHeader("Archivo:");
			Link link = new Link(this.file.getName(), new Action("downloadArquivo"));
			link.getAction().add("arquivoDownload", file.getName());
			table.add(link);
		}
		
		if(this.mostrarTela)
		{
			Table table2 = null;
			
			if(this.secao.equals(""))
				table2 = new Table(4);
			else
				table2 = new Table(6);
			
			table2.setWidth("90%");
			
			String titulo = "Aseguradoras";
			
			if(!this.secao.equals(""))
				titulo +=" - Sección: " + this.secao;
			
			table2.addSubtitle(titulo);
			table2.addStyle(Table.STYLE_ALTERNATE);
			
			table2.addHeader("Aseguradoras");
			table2.addHeader("Cantidad Pólizas");
			if(!this.secao.equals(""))
			{
				table2.setNextHAlign(Table.HALIGN_CENTER);
				table2.addHeader("%");
			}
			table2.addHeader("Cantidad Siniestros");
			if(!this.secao.equals(""))
			{
				table2.setNextHAlign(Table.HALIGN_CENTER);
				table2.addHeader("%");
			}
			table2.addHeader("Últ.Mes Grabado");
			
			for(Iterator i = this.aseguradoras.iterator() ; i.hasNext() ; )
			{
				Aseguradora aseg = (Aseguradora) i.next();
				
				double porcentagemApolices = 0;
				double porcentagemSinistros = 0;
				
				int qtdeApolice = aseg.obterQtdeApolicesPorPeriodo(dataInicio, dataFim, secao, this.situacaoSeguro);
				int qtdeSinistros = aseg.obterQtdeSinistrosPorPeriodo(dataInicio, dataFim, secao, this.situacaoSeguro);
				if(!this.secao.equals(""))
				{
					porcentagemApolices = aseg.obterPorcentagemApolices(qtdeApolice);
					porcentagemSinistros = aseg.obterPorcentagemSinistros(qtdeSinistros);
				}
				
				table2.add(aseg.obterNome());
				table2.setNextHAlign(Table.HALIGN_RIGHT);
				table2.add(new Label(qtdeApolice));
				if(!this.secao.equals(""))
				{
					table2.setNextHAlign(Table.HALIGN_RIGHT);
					table2.add(new Label(porcentagemApolices,"#,#0.0"));
				}
				table2.setNextHAlign(Table.HALIGN_RIGHT);
				table2.add(new Label(qtdeSinistros));
				if(!this.secao.equals(""))
				{
					table2.setNextHAlign(Table.HALIGN_RIGHT);
					table2.add(new Label(porcentagemSinistros,"#,#0.0"));
				}
				table2.setNextHAlign(Table.HALIGN_CENTER);
				
				AgendaMovimentacao ag = aseg.obterUltimaAgendaMCI();
				String mesAno = "";
				if(ag!=null)
					mesAno = ag.obterMesMovimento() + "/" + ag.obterAnoMovimento();
				
				table2.add(mesAno);
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
		return new Label("Cantidad Polizas/Siniestros");
	}
}