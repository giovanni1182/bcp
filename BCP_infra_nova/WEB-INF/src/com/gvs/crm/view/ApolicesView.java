package com.gvs.crm.view;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Locale;
import java.util.Properties;

import com.gvs.crm.component.Border;
import com.gvs.crm.component.PlanoApoliceSelect;
import com.gvs.crm.component.SituacaoApoliceSelect;
import com.gvs.crm.component.TipoApoliceSelect;
import com.gvs.crm.model.Apolice;
import com.gvs.crm.model.Aseguradora;
import com.gvs.crm.model.ClassificacaoContas;
import com.gvs.crm.model.EntidadeBCP;
import com.gvs.crm.model.Plano;

import infra.control.Action;
import infra.security.User;
import infra.view.BasicView;
import infra.view.Block;
import infra.view.Button;
import infra.view.Image;
import infra.view.Label;
import infra.view.Link;
import infra.view.Space;
import infra.view.Table;
import infra.view.View;

public class ApolicesView extends BasicView {
	private Collection<Apolice> apolices;

	private boolean filtros;

	private Aseguradora aseguradora;

	private String _secao;

	private String _plano;

	private String _tipo;

	private String _situacao;

	private boolean listar;

	private boolean borda;
	
	private int pagina;

	public ApolicesView(Aseguradora aseguradora, Collection<Apolice> apolices,boolean filtros) throws Exception 
	{
		this.aseguradora = aseguradora;
		this.apolices = apolices;
		this.filtros = filtros;
		this.borda = true;
	}
	
	public ApolicesView(Aseguradora aseguradora, Collection<Apolice> apolices,boolean filtros, int pagina) throws Exception 
	{
		this.aseguradora = aseguradora;
		this.apolices = apolices;
		this.filtros = filtros;
		this.pagina = pagina;
	}

	public ApolicesView(Aseguradora aseguradora, boolean filtros,String _secao, String _plano, String _tipo, String _situacao,boolean listar) throws Exception 
	{
		this.aseguradora = aseguradora;
		this.apolices = aseguradora.obterApolicesComoOrigem();
		this.filtros = filtros;
		this._secao = _secao;
		this._plano = _plano;
		this._tipo = _tipo;
		this._situacao = _situacao;
		this.listar = listar;
		this.borda = true;
	}

	public View execute(User user, Locale locale, Properties properties) throws Exception 
	{
		Table table = new Table(11);
		table.setWidth("100%");

		table.addStyle(Table.STYLE_ALTERNATE);
		
		int qtde = this.apolices.size(); 

		if (qtde == 1)
			table.addSubtitle(qtde + " Instrumento");
		else
			table.addSubtitle(qtde + " Instrumentos");

		if(qtde > 0)
		{
			//table.addHeader("");
			table.setNextHAlign(Table.HALIGN_CENTER);
			table.addHeader("Vigencia");
			table.setNextHAlign(Table.HALIGN_CENTER);
			table.addHeader("Numero");
		}

		//if(qtde > 0)
			//table.addHeader("Sección");

		table.setNextHAlign(Table.HALIGN_CENTER);
		if (filtros) 
		{
			Block block = new Block(Block.HORIZONTAL);
			block.add(new PlanoApoliceSelect("plano", aseguradora,this._plano));
			block.add(new Space(2));

			Link link = new Link(new Image("visualizar.GIF"), new Action("consultarApolices"));
			link.getAction().add("id", aseguradora.obterId());
			link.getAction().add("listar", true);
			block.add(link);
			table.add(block);
		} 
		else if(qtde > 0)
			table.addHeader("Plan");

		if(qtde > 0)
		{
			table.setNextHAlign(Table.HALIGN_CENTER);
			table.addHeader("Asegurado");
			table.setNextHAlign(Table.HALIGN_CENTER);
			table.addHeader("Tipo Doc.");
			table.setNextHAlign(Table.HALIGN_CENTER);
			table.addHeader("Numero Doc.");
			table.setNextHAlign(Table.HALIGN_CENTER);
			table.addHeader("Tomador");
		}
		table.setNextHAlign(Table.HALIGN_CENTER);
		if (filtros) 
		{
			Block block = new Block(Block.HORIZONTAL);
			block.add(new TipoApoliceSelect("tipo", aseguradora, this._tipo));
			block.add(new Space(2));

			Link link = new Link(new Image("visualizar.GIF"), new Action("consultarApolices"));
			link.getAction().add("id", aseguradora.obterId());
			link.getAction().add("listar", true);
			block.add(link);
			table.add(block);
		}
		else if(qtde > 0)
			table.addHeader("Tipo");

		table.setNextHAlign(Table.HALIGN_CENTER);
		if(qtde > 0)
			table.addHeader("Aseguradora");

		table.setNextHAlign(Table.HALIGN_CENTER);
		if (filtros) 
		{
			Block block = new Block(Block.HORIZONTAL);
			block.add(new SituacaoApoliceSelect("situacao", aseguradora,this._situacao));
			block.add(new Space(2));

			Link link = new Link(new Image("visualizar.GIF"), new Action("consultarApolices"));
			link.getAction().add("id", aseguradora.obterId());
			link.getAction().add("listar", true);
			block.add(link);

			table.add(block);
		} 
		else if(qtde > 0)
			table.addHeader("Situación");
		
		table.setNextHAlign(Table.HALIGN_CENTER);
		if(qtde > 0)
			table.addHeader("Sospechosa");

		if (filtros && listar) 
		{
			if(qtde > 0)
			{
				Block blockV;
				Collection<EntidadeBCP> pessoas;
				Link link;
				Plano plano;
				String identificadorPlano;
				boolean mostraPlano;
				
				for (Apolice apolice : apolices) 
				{
					plano = apolice.obterPlano();
					identificadorPlano = "";
					
					if(plano!=null)
						identificadorPlano = plano.obterIdentificador();
					
					mostraPlano = !identificadorPlano.equals("0") & !identificadorPlano.toLowerCase().equals("n") && !identificadorPlano.equals("");
					
					blockV = new Block(Block.VERTICAL);
					
					//table.add(new EventoImage(apolice));
					table.add(new SimpleDateFormat("dd/MM/yyyy").format(apolice.obterDataPrevistaInicio())	+ " - "	+ new SimpleDateFormat("dd/MM/yyyy").format(apolice.obterDataPrevistaConclusao()));
	
					link = new Link(apolice.obterNumeroApolice(), new Action("visualizarEvento"));
					link.getAction().add("id", apolice.obterId());
	
					table.add(link);
					//table.add(apolice.obterSecao().obterApelido());
					if(mostraPlano)
						table.add(plano.obterPlano()  + " " + plano.obterIdentificador());
					else
						table.add("");
					
					if(apolice.obterNomeAsegurado()!=null)
						blockV.add(new Label(apolice.obterNomeAsegurado()));
					
					pessoas = apolice.obterAsegurados();
					
					for(EntidadeBCP pessoa : pessoas)
						blockV.add(new Label(pessoa.getNome() + " " + pessoa.getSobreNome()));
					
					table.add(blockV);
					
					blockV = new Block(Block.VERTICAL);
					if(apolice.obterNomeTomador()!=null)
						blockV.add(new Label(apolice.obterNomeTomador()));
					
					pessoas = apolice.obterTomadores();
					
					for(EntidadeBCP pessoa : pessoas)
						blockV.add(new Label(pessoa.getNome() + " " + pessoa.getSobreNome()));
					
					table.add(blockV);
					table.add(apolice.obterTipo());
	
					table.add(apolice.obterOrigem().obterNome());
					table.add(apolice.obterSituacaoSeguro());
				}
			}
			else
			{
				table.setNextColSpan(table.getColumns());
				table.setNextHAlign(Table.HALIGN_CENTER);
				table.addHeader("NO FUE ENCONTRADA PÓLIZA PARA LA PESQUISA REALIZADA");
			}
		} 
		else if (!filtros) 
		{
			double capitalRiegosGs = 0;
			double primaGs = 0;
			double principalGs = 0;
			double incapacidadeGs = 0;
			double enfermidadeGs = 0;
			double acidentesGs = 0;
			double outrosGs = 0;
			double iteresGs = 0;
			double premioGs = 0;
			double comissaoGs = 0;
			
			if(qtde > 0)
			{
				Link link;
				ClassificacaoContas cConta;
				Plano plano;
				String numeroIndentificacao;
				Collection<EntidadeBCP> pessoas;
				Block blockV,blockVTipoDoc,blockVDoc;
				String identificadorPlano;
				boolean mostraPlano;
				
				for (Apolice apolice : apolices) 
				{
					identificadorPlano = "";
					plano = apolice.obterPlano();
					
					if(plano!=null)
						identificadorPlano = plano.obterIdentificador();
					
					mostraPlano = !identificadorPlano.equals("0") & !identificadorPlano.toLowerCase().equals("n") && !identificadorPlano.equals("");
					
					blockV = new Block(Block.VERTICAL);
					blockVTipoDoc = new Block(Block.VERTICAL);
					blockVDoc = new Block(Block.VERTICAL);
					
					//table.add(new EventoImage(apolice));
					table.add(new SimpleDateFormat("dd/MM/yyyy").format(apolice.obterDataPrevistaInicio()) + " - "	+ new SimpleDateFormat("dd/MM/yyyy").format(apolice.obterDataPrevistaConclusao()));
	
					link = new Link(apolice.obterNumeroApolice(), new Action("visualizarEvento"));
					link.getAction().add("id", apolice.obterId());
					link.setNovaJanela(true);
					table.add(link);
					
					/*cConta = apolice.obterSecao();
					if (cConta != null)
						table.add(cConta.obterApelido());
					else
						table.add("");*/
	
					if(mostraPlano)
						table.add(plano.obterPlano());
					else
						table.add("");
					
					if(apolice.obterNomeAsegurado()!=null)
						blockV.add(new Label(apolice.obterNomeAsegurado()));
					
					if(apolice.obterTipoIdentificacao()!=null)
						blockVTipoDoc.add(new Label(apolice.obterTipoIdentificacao()));
					
					numeroIndentificacao = apolice.obterNumeroIdentificacao();
					
					if(numeroIndentificacao!=null)
						blockVDoc.add(new Label(numeroIndentificacao.trim()));
					
					pessoas = apolice.obterAsegurados();
					
					for(EntidadeBCP pessoa : pessoas)
					{
						blockV.add(new Label(pessoa.getNome() + " " + pessoa.getSobreNome()));
						blockVTipoDoc.add(new Label(pessoa.getTipoDocumento()));
						blockVDoc.add(new Label(pessoa.getNumeroDoc()));
					}
					
					table.add(blockV);
					table.add(blockVTipoDoc);
					table.add(blockVDoc);
					
					blockV = new Block(Block.VERTICAL);
					if(apolice.obterNomeTomador()!=null)
						blockV.add(new Label(apolice.obterNomeTomador()));
					
					pessoas = apolice.obterTomadores();
					
					for(EntidadeBCP pessoa : pessoas)
						blockV.add(new Label(pessoa.getNome() + " " + pessoa.getSobreNome()));
					
					table.add(blockV);
					
					table.add(apolice.obterTipo());
	
					table.add(apolice.obterOrigem().obterNome());
					table.add(apolice.obterSituacaoSeguro());
					
					table.setNextHAlign(Table.HALIGN_CENTER);
					if(numeroIndentificacao!=null)
					{
						link = new Link(apolice.obterApoliceSuspeita(), new Action("listarApolicesSuspeitas"));
						link.getAction().add("rucCi", numeroIndentificacao.trim());
						
						table.add(link);
					}
					else
						table.add("No");
					
					capitalRiegosGs+=apolice.obterCapitalGs();
					primaGs+=apolice.obterPrimaGs();
					principalGs+=apolice.obterPrincipalGs();
					incapacidadeGs+=apolice.obterIncapacidadeGs();
					enfermidadeGs+=apolice.obterEnfermidadeGs();
					acidentesGs+=apolice.obterAcidentesGs();
					outrosGs+=apolice.obterOutrosGs();
					iteresGs+=apolice.obterFinanciamentoGs();
					premioGs+=apolice.obterPremiosGs();
					comissaoGs+=apolice.obterComissaoGs();
				}
			}
			else
			{
				table.setNextColSpan(table.getColumns());
				table.setNextHAlign(Table.HALIGN_CENTER);
				table.addHeader("NO FUE ENCONTRADA PÓLIZA PARA LA PESQUISA REALIZADA");
			}
			
			Table table2 = new Table(2);
			table2.addSubtitle("Totalización");
			
			table2.addHeader("Cantidad de Pólizas:");
			table2.setNextHAlign(Table.HALIGN_RIGHT);
			table2.add(new Label(qtde));
			table2.addHeader("Capital en Riesgo en Gs:");
			table2.setNextHAlign(Table.HALIGN_RIGHT);
			table2.add(new Label(capitalRiegosGs,"#,##0.00"));
			table2.addHeader("Prima en Gs:");
			table2.setNextHAlign(Table.HALIGN_RIGHT);
			table2.add(new Label(primaGs,"#,##0.00"));
			table2.addHeader("Principal en Gs:");
			table2.setNextHAlign(Table.HALIGN_RIGHT);
			table2.add(new Label(principalGs,"#,##0.00"));
			table2.addHeader("Incapacidad en Gs:");
			table2.setNextHAlign(Table.HALIGN_RIGHT);
			table2.add(new Label(incapacidadeGs,"#,##0.00"));
			table2.addHeader("Enfermidad en Gs:");
			table2.setNextHAlign(Table.HALIGN_RIGHT);
			table2.add(new Label(enfermidadeGs,"#,##0.00"));
			table2.addHeader("Accidentes en Gs:");
			table2.setNextHAlign(Table.HALIGN_RIGHT);
			table2.add(new Label(acidentesGs,"#,##0.00"));
			table2.addHeader("Otros en Gs:");
			table2.setNextHAlign(Table.HALIGN_RIGHT);
			table2.add(new Label(outrosGs,"#,##0.00"));
			table2.addHeader("Interés por Financiamento en Gs:");
			table2.setNextHAlign(Table.HALIGN_RIGHT);
			table2.add(new Label(iteresGs,"#,##0.00"));
			table2.addHeader("Premio en Gs:");
			table2.setNextHAlign(Table.HALIGN_RIGHT);
			table2.add(new Label(premioGs,"#,##0.00"));
			table2.addHeader("Comisión en Gs:");
			table2.setNextHAlign(Table.HALIGN_RIGHT);
			table2.add(new Label(comissaoGs,"#,##0.00"));
			
			table.setNextColSpan(table.getColumns());
			table.add(table2);
		}

		if (filtros)
		{
			Button voltarButton = new Button("Volver", new Action("visualizarDetalhesEntidade"));
			voltarButton.getAction().add("id", this.aseguradora.obterId());
			voltarButton.getAction().add("_pastaAseguradora", 0);
			table.addFooter(voltarButton);
		}

		if (this.borda)
		{
			Border border = new Border(table);
			return border;
		}
		else
			return table;
	}
}