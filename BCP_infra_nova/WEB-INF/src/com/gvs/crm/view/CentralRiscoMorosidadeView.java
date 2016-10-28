package com.gvs.crm.view;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

import com.gvs.crm.model.Apolice;
import com.gvs.crm.model.CRMModelManager;
import com.gvs.crm.model.ClassificacaoContas;
import com.gvs.crm.model.Entidade;
import com.gvs.crm.model.Morosidade;
import com.gvs.crm.model.MorosidadeCentralRisco;

import infra.control.Action;
import infra.security.User;
import infra.view.Block;
import infra.view.Button;
import infra.view.InputString;
import infra.view.Label;
import infra.view.Space;
import infra.view.Table;
import infra.view.View;

public class CentralRiscoMorosidadeView extends PortalView 
{
	private Entidade entidade;
	private Collection<Entidade> aseguradoras;
	private String nome, documento;
	private Map<Long,Collection<Morosidade>> morosidades;
	
	public CentralRiscoMorosidadeView(Entidade entidade, Collection<Entidade> aseguradoras, String nome, String documento) throws Exception
	{
		this.entidade = entidade;
		this.aseguradoras = aseguradoras;
		this.nome = nome;
		this.documento = documento;
	}
	
	public CentralRiscoMorosidadeView(Entidade entidade, Map<Long,Collection<Morosidade>> morosidades, String nome, String documento) throws Exception
	{
		this.entidade = entidade;
		this.morosidades = morosidades;
		this.nome = nome;
		this.documento = documento;
	}
	
	public View getBody(User user, Locale locale, Properties properties) throws Exception 
	{
		CRMModelManager mm = new CRMModelManager(user);
		
		Table mainTable = new Table(1);
		mainTable.setWidth("100%");
		
		Table table = new Table(2);
		table.addHeader("Nombre Asegurado:");
		
		Block block = new Block(Block.HORIZONTAL);
		block.add(new InputString("nome", this.nome, 80));
		block.add(new Space(2));
		block.add(new Label("Al menos 10 caracteres"));
		table.add(block);
		
		table.addHeader("Nº del Documento:");
		block = new Block(Block.HORIZONTAL);
		block.add(new InputString("documento", this.documento, 20));
		block.add(new Space(2));
		block.add(new Label("Al menos 5 dígitos"));
		table.add(block);
		
		Button buscarButton = new Button("Buscar Morosidades", new Action("visualizarCentralRiscoMorosidade"));
		
		table.addFooter(buscarButton);
		
		mainTable.add(table);
		
		Table table2 = new Table(8);
		table2.addStyle(Table.STYLE_ALTERNATE);
		table2.setWidth("100%");
		table2.addSubtitle("Morosidades");
		
		//if(this.aseguradoras.size() == 0)
		if(this.morosidades.size() == 0)
		{
			table2.setNextHAlign(Table.HALIGN_CENTER);
			table2.addHeader("Nenhuma Morosidade");
		}
		else
		{
			//AseguradoraHome aseguradoraHome = (AseguradoraHome) mm.getHome("AseguradoraHome");
			
			table2.setNextHAlign(Table.HALIGN_CENTER);
			table2.addHeader("Aseg.");
			table2.setNextHAlign(Table.HALIGN_CENTER);
			table2.addHeader("Fecha Corte");
			table2.setNextHAlign(Table.HALIGN_CENTER);
			table2.addHeader("Sección");
			table2.setNextHAlign(Table.HALIGN_CENTER);
			table2.addHeader("Dias de Mora");
			table2.setNextHAlign(Table.HALIGN_CENTER);
			table2.addHeader("Cant. en Atraso");
			table2.setNextHAlign(Table.HALIGN_CENTER);
			table2.addHeader("Deudas en GS");
			table2.setNextHAlign(Table.HALIGN_CENTER);
			table2.addHeader("M/E");
			table2.setNextHAlign(Table.HALIGN_CENTER);
			table2.addHeader("Deudas en M/E");
			
			int j = 1;
			int totalQtde = 0;
			double totalDeudasGS = 0;
			Map<Long,MorosidadeCentralRisco> morosidadesCertas;
			Collection<Morosidade> morosidades;
			String data;
			Apolice apolice;
			ClassificacaoContas secao;
			MorosidadeCentralRisco morosidadeCerta;
			
			for(Long entidadeId : this.morosidades.keySet())
			{
				morosidades = this.morosidades.get(entidadeId);
				morosidadesCertas = new TreeMap<>();
				
				for(Morosidade morosidade : morosidades)
				{
					apolice = (Apolice) morosidade.obterSuperior();
					secao = apolice.obterSecao();
					
					if(morosidadesCertas.containsKey(secao.obterId()))
					{
						morosidadeCerta = morosidadesCertas.get(secao.obterId());
						morosidadeCerta.atribuirCotasAtraso(morosidadeCerta.obterCotasAtraso()+1);
						if(morosidadeCerta.obterDataCorte().getTime()<morosidade.obterDataCorte().getTime())
							morosidadeCerta.atribuirDataCorte(morosidade.obterDataCorte());
						
						morosidadeCerta.atribuirSecao(secao);
						morosidadeCerta.atribuirDeudasGs(morosidadeCerta.obterDeudasGs() + morosidade.obterValorGs());
						morosidadeCerta.atribuirMoedaDeudasMe(morosidade.obterTipoMoedaValorGs());
						morosidadeCerta.atribuirDeudasMe(morosidadeCerta.obterDeudasMe() + morosidade.obterValorMe());
						morosidadeCerta.atribuirDiasMora(morosidadeCerta.obterDiasMora() + morosidade.obterDiasAtraso());
						
						morosidadesCertas.put(secao.obterId(), morosidadeCerta);
					}
					else
					{
						morosidadeCerta = (MorosidadeCentralRisco) mm.getEntity("MorosidadeCentralRisco");
						
						morosidadeCerta.atribuirCotasAtraso(1);
						morosidadeCerta.atribuirDataCorte(morosidade.obterDataCorte());
						morosidadeCerta.atribuirSecao(secao);
						morosidadeCerta.atribuirDeudasGs(morosidade.obterValorGs());
						morosidadeCerta.atribuirMoedaDeudasMe(morosidade.obterTipoMoedaValorGs());
						morosidadeCerta.atribuirDeudasMe(morosidade.obterValorMe());
						morosidadeCerta.atribuirDiasMora(morosidade.obterDiasAtraso());
						
						morosidadesCertas.put(secao.obterId(), morosidadeCerta);
					}
				}
				
				for(MorosidadeCentralRisco morosidade : morosidadesCertas.values())
				{
					table2.setNextHAlign(Table.HALIGN_CENTER);
					table2.add(new Label(j));
					
					data = new SimpleDateFormat("MM/yyyy").format(morosidade.obterDataCorte());
					table2.setNextHAlign(Table.HALIGN_CENTER);
					table2.add(data);
					table2.add(morosidade.obterSecao().obterNome());
					table2.setNextHAlign(Table.HALIGN_CENTER);
					table2.add(new Label(morosidade.obterDiasMora()));
					table2.setNextHAlign(Table.HALIGN_CENTER);
					table2.add(new Label(morosidade.obterCotasAtraso(),"#,##0.00"));
					totalQtde+=morosidade.obterCotasAtraso();
					table2.setNextHAlign(Table.HALIGN_RIGHT);
					table2.add(new Label(morosidade.obterDeudasGs(),"#,##0.00"));
					if(morosidade.obterMoedaDeudasMe().toLowerCase().indexOf("guara")>-1)
						table2.add("");
					else
						table2.add(morosidade.obterMoedaDeudasMe());
					totalDeudasGS+=morosidade.obterDeudasGs();
					table2.setNextHAlign(Table.HALIGN_RIGHT);
					table2.add(new Label(morosidade.obterDeudasMe(),"#,##0.00"));
				}
				
				j++;
			}
			
			/*for(Entidade aseg : aseguradoras)
			{
				morosidades = aseguradoraHome.obterMorosidadesCentralRisco(aseg, nome, documento); 
				
				for(MorosidadeCentralRisco morosidade : morosidades)
				{
					data = new SimpleDateFormat("MM/yyyy").format(morosidade.obterDataCorte());
					
					table2.setNextHAlign(Table.HALIGN_CENTER);
					table2.add(new Label(j));
					table2.setNextHAlign(Table.HALIGN_CENTER);
					table2.add(data);
					table2.add(morosidade.obterSecao().obterNome());
					table2.setNextHAlign(Table.HALIGN_CENTER);
					table2.add(new Label(morosidade.obterDiasMora()));
					table2.setNextHAlign(Table.HALIGN_CENTER);
					table2.add(new Label(morosidade.obterCotasAtraso(),"#,##0.00"));
					totalQtde+=morosidade.obterCotasAtraso();
					table2.setNextHAlign(Table.HALIGN_RIGHT);
					table2.add(new Label(morosidade.obterDeudasGs(),"#,##0.00"));
					if(morosidade.obterMoedaDeudasMe().toLowerCase().indexOf("guara")>-1)
						table2.add("");
					else
						table2.add(morosidade.obterMoedaDeudasMe());
					totalDeudasGS+=morosidade.obterDeudasGs();
					table2.setNextHAlign(Table.HALIGN_RIGHT);
					table2.add(new Label(morosidade.obterDeudasMe(),"#,##0.00"));
				}
				
				if(morosidades.size() > 0)
					j++;
			}*/
			
			table2.setNextHAlign(Table.HALIGN_CENTER);
			table2.addHeader("TOTAL");
			table2.addHeader("");
			table2.addHeader("");
			table2.addHeader("");
			Label label = new Label(totalQtde);
			label.setBold(true);
			table2.setNextHAlign(Table.HALIGN_CENTER);
			table2.add(label);
			label = new Label(totalDeudasGS,"#,##0.00");
			label.setBold(true);
			table2.setNextHAlign(Table.HALIGN_RIGHT);
			table2.add(label);
			table2.addHeader("");
			table2.addHeader("");
		}
		
		mainTable.add(table2);
		
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
		return new Label("Lista de Morosidades - " + this.entidade.obterNome());
	}

	public Entidade getOrigemMenu() throws Exception 
	{
		return this.entidade;
	}
}