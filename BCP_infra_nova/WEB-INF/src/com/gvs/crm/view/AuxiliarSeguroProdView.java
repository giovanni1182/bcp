package com.gvs.crm.view;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.util.Properties;

import com.gvs.crm.component.Border;
import com.gvs.crm.model.Apolice;
import com.gvs.crm.model.Aseguradora;
import com.gvs.crm.model.AuxiliarSeguro;

import infra.control.Action;
import infra.security.User;
import infra.view.BasicView;
import infra.view.Block;
import infra.view.Label;
import infra.view.Link;
import infra.view.Space;
import infra.view.Table;
import infra.view.View;

public class AuxiliarSeguroProdView extends BasicView
{
	private AuxiliarSeguro auxiliar;
	private Collection<Aseguradora> aseguradoras;
	private Date dataInicio;
	private Date dataFim;
	private boolean lista;
	private boolean auxiliarSeguro;

	public AuxiliarSeguroProdView(AuxiliarSeguro auxiliar,Collection<Aseguradora> aseguradoras, Date dataInicio, Date dataFim,boolean lista, boolean auxiliarSeguro) throws Exception
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
		
		Table table = new Table(14);
		table.setWidth("100%");
		table.addStyle(Table.STYLE_ALTERNATE);

		table.setNextColSpan(table.getColumns());

		Block block = new Block(Block.HORIZONTAL);

		Label label = null;
		
		if(auxiliarSeguro)
			label = new Label("Agente:");
		else
			label = new Label("Corredor:");

		Label label2 = new Label("Periodo:");
		label2.setBold(true);

		Label label3 = new Label("hasta el:");
		label3.setBold(true);

		block.add(label);
		block.add(new Space(2));
		block.add(new Label(auxiliar.obterNome()));
		block.add(new Space(10));
		block.add(label2);
		block.add(new Space(2));
		block.add(new Label(dataInicio, "dd/MM/yyyy"));
		block.add(new Space(2));
		block.add(label3);
		block.add(new Space(2));
		block.add(new Label(dataFim, "dd/MM/yyyy"));
		block.add(new Space(5));

		/*Link link = new Link(new Image("visualizar.GIF"), new Action(
				"visualizarProdutividadeAuxiliar"));
		link.getAction().add("lista", true);

		block.add(link);*/

		table2.add(block);
		
		/*Block block2 = new Block(Block.HORIZONTAL);

		Button button = new Button("Listar", new Action("visualizarProdutividadeAuxiliar"));
		button.getAction().add("lista", true);

		Button button2 = new Button("Volver", new Action("visualizarInspecaoSitu"));
		
		Button button3 = new Button("Generar Excel", new Action("visualizarProdutividadeAuxiliar"));
		button3.getAction().add("lista", true);
		button3.getAction().add("excel", true);

		block2.add(button);
		block2.add(new Space(8));
		block2.add(button3);
		block2.add(new Space(8));
		block2.add(button2);
		
		table2.add(new Space());
		table2.setNextHAlign(Table.HALIGN_CENTER);
		table2.add(block2);*/

		table.setNextColSpan(table.getColumns());
		table.add(new Space());

		if (this.lista)
		{
			for (Iterator<Aseguradora> i = aseguradoras.iterator(); i.hasNext();)
			{
				Aseguradora aseguradora = i.next();

				table.setNextColSpan(table.getColumns());
				table.setNextHAlign(Table.HALIGN_LEFT);
				table.addHeader("Aseguradora: " + aseguradora.obterNome());

				table.addHeader("Nº Póliza");
				table.addHeader("Situación");
				table.addHeader("Asegurado");
				table.addHeader("Emisión");
				table.setNextColSpan(2);
				table.setNextHAlign(Table.HALIGN_CENTER);
				table.addHeader("Vigencia");
				table.addHeader("Tp. Operación");
				table.addHeader("Secccón");
				//table.addHeader("Agente");
				table.setNextColSpan(2);
				table.setNextHAlign(Table.HALIGN_CENTER);
				table.addHeader("Comisión");
				table.addHeader("Prima");
				table.addHeader("Premio");
				table.setNextColSpan(2);
				table.setNextHAlign(Table.HALIGN_CENTER);
				table.addHeader("Cap. Asegurado");

				table.add("");
				table.add("");
				table.add("");
				table.add("");
				table.setNextHAlign(Table.HALIGN_CENTER);
				table.add("Inicio");
				table.setNextHAlign(Table.HALIGN_CENTER);
				table.add("Fin");
				table.add("");
				//table.add("");
				table.add("");
				//table.add("%");
				table.setNextHAlign(Table.HALIGN_CENTER);
				table.add("Gs");
				table.setNextHAlign(Table.HALIGN_CENTER);
				table.add("M.E.");
				table.add("");
				table.add("");
				table.setNextHAlign(Table.HALIGN_CENTER);
				table.add("Gs");
				table.setNextHAlign(Table.HALIGN_CENTER);
				table.add("M.E.");
				
				Collection<Apolice> apolices = aseguradora.obterApolicesProducao(this.auxiliar,this.dataInicio, this.dataFim, this.auxiliarSeguro); 
				
				System.out.println(apolices.size());

				for (Iterator<Apolice> j = apolices.iterator(); j.hasNext();) 
				{
					Apolice apolice = j.next();
					
					System.out.println(apolice.obterId());

					Link link2 = new Link(apolice.obterNumeroApolice(),	new Action("visualizarEvento"));
					link2.getAction().add("id", apolice.obterId());

					table.add(link2);

					table.add(apolice.obterSituacaoSeguro());
					table.add(apolice.obterNomeAsegurado());
					table.add(new SimpleDateFormat("dd/MM/yyyy").format(apolice
							.obterDataEmissao()));
					table.add(new SimpleDateFormat("dd/MM/yyyy").format(apolice
							.obterDataPrevistaInicio()));
					table.add(new SimpleDateFormat("dd/MM/yyyy").format(apolice
							.obterDataPrevistaConclusao()));
					table.add(apolice.obterTipo());
					table.add(apolice.obterSecao().obterNome());
					/*if (apolice.obterInscricaoAgente() != null)
						table.add(apolice.obterInscricaoAgente().obterNome());
					else
						table.add("");*/

					//table.setNextHAlign(Table.HALIGN_RIGHT);
					//table.add(new Label(apolice.obterComissaoGs()));//,
																	// "#,##0.00"));

					/*double comissaoGs = (apolice.obterPrimaGs() * apolice
							.obterComissaoGs()) / 100;

					double comissaoMe = (apolice.obterPrimaMe() * apolice
							.obterComissaoMe()) / 100;
*/
					table.setNextHAlign(Table.HALIGN_RIGHT);
					table.add(new Label(apolice.obterComissaoGs()));//, "#,##0.00"));
					table.setNextHAlign(Table.HALIGN_RIGHT);
					table.add(new Label(apolice.obterComissaoMe()));//, "#,##0.00"));
					table.setNextHAlign(Table.HALIGN_RIGHT);
					table.add(new Label(apolice.obterPrimaGs()));//,
																 // "#,##0.00"));
					table.setNextHAlign(Table.HALIGN_RIGHT);
					table.add(new Label(apolice.obterPremiosGs()));//,
																   // "#,##0.00"));
					table.setNextHAlign(Table.HALIGN_RIGHT);
					table.add(new Label(apolice.obterCapitalGs()));//,
																   // "#,##0.00"));
					table.setNextHAlign(Table.HALIGN_RIGHT);
					table.add(new Label(apolice.obterCapitalMe()));//,
																   // "#,##0.00"));
					table.setNextHAlign(Table.HALIGN_RIGHT);
				}
				
				table.setNextColSpan(table.getColumns());
				table.add(new Space());
			}
		}

		table2.add(table);
		Border border = new Border(table2);

		return border;
	}
}