package com.gvs.crm.view;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Locale;
import java.util.Properties;

import com.gvs.crm.component.SeparadorLabel;
import com.gvs.crm.model.Apolice;
import com.gvs.crm.model.Entidade;
import com.gvs.crm.model.EntidadeBCP;
import com.gvs.crm.model.Plano;

import infra.control.Action;
import infra.security.User;
import infra.view.Block;
import infra.view.Label;
import infra.view.Link;
import infra.view.Space;
import infra.view.Table;
import infra.view.View;

public class ApoliceView extends EventoAbstratoView 
{
	private static final int DETALHE = 0;
	private static final int GERAL = 1;
	private static final int ENTIDADES = 2;

	public View execute(User arg0, Locale arg1, Properties properties) throws Exception 
	{
		Apolice apolice = (Apolice) this.obterEvento();
		
		DecimalFormat formataValor = new DecimalFormat("#,##0.00");

		//AgendaMovimentacao agenda = (AgendaMovimentacao) apolice.obterSuperior();

		Table table = new Table(1);
		table.setWidth("100%");

		int _pasta = Integer.parseInt(properties.getProperty("_pastaApolice","0"));

		Link dadosLink = new Link("Detalles", new Action("visualizarEvento"));
		((Label) dadosLink.getCaption()).setBold(_pasta == DETALHE);
		dadosLink.getAction().add("id", apolice.obterId());
		dadosLink.getAction().add("_pastaApolice", DETALHE);

		Link componentesLink = new Link("General", new Action("visualizarApoliceGeral"));
		((Label) componentesLink.getCaption()).setBold(_pasta == GERAL);
		componentesLink.getAction().add("id", apolice.obterId());
		componentesLink.getAction().add("_pastaApolice", GERAL);
		
		Link entidadesLink = new Link("Asegurados y Tomadores", new Action("visualizarEvento"));
		((Label) entidadesLink.getCaption()).setBold(_pasta == ENTIDADES);
		entidadesLink.getAction().add("id", apolice.obterId());
		entidadesLink.getAction().add("_pastaApolice", ENTIDADES);

		Block block100 = new Block(Block.HORIZONTAL);
		block100.add(dadosLink);
		block100.add(new SeparadorLabel());
		block100.add(entidadesLink);
		block100.add(new SeparadorLabel());
		block100.add(componentesLink);
		
		table.setNextColSpan(table.getColumns());
		table.add(block100);

		if(_pasta == DETALHE)
		{
			Table registro1Table = new Table(2);
	
			registro1Table.addSubtitle("Cabecera");
	
			registro1Table.addHeader("Creación:");
			registro1Table.add(new Label(apolice.obterCriacao(), "dd/MM/yyyy HH:mm:ss"));
			
			registro1Table.addHeader("Aseguradora:");
			registro1Table.add(apolice.obterOrigem().obterNome());
	
			registro1Table.addHeader("Responsable:");
			registro1Table.add(apolice.obterResponsavel().obterNome());
	
			table.add(registro1Table);
	
			Table registro2Table = new Table(5);
			registro2Table.addStyle(Table.STYLE_ALTERNATE);
			registro2Table.addSubtitle("Instrumento");
	
			registro2Table.addHeader("Número del Instrumento:");
			registro2Table.add(apolice.obterNumeroApolice());
	
			this.addEspaco(registro2Table);
			
			registro2Table.addHeader("Tipo de Instrumento:");
			registro2Table.add(apolice.obterTipo());
			
			registro2Table.addHeader("Instrumento:");
			registro2Table.add(apolice.obterStatusApolice());
	
			this.addEspaco(registro2Table);
			
			registro2Table.addHeader("Instrumento Anterior:");
			if (apolice.obterApoliceAnterior() != null) 
			{
				Link link = new Link(apolice.obterApoliceAnterior().obterNumeroApolice(), new Action("visualizarEvento"));
				link.getAction().add("id", apolice.obterApoliceAnterior().obterId());
	
				registro2Table.add(link);
			} 
			else
				registro2Table.add("");
			
			registro2Table.addHeader("Número del Endoso:");
			registro2Table.add(new Label(apolice.obterNumeroEndoso()));
		  
			this.addEspaco(registro2Table);
			
			registro2Table.addHeader("Certificado de Seguro Colectivo:");
			registro2Table.add(new Label(apolice.obterCertificado()));
			
			registro2Table.addHeader("Póliza Madre:");
			if (apolice.obterSuperior() instanceof Apolice) 
			{
				Apolice apoliceSupeior = (Apolice) apolice.obterSuperior();
				Link link = new Link(apoliceSupeior.obterNumeroApolice(),new Action("visualizarEvento"));
				link.getAction().add("id", apoliceSupeior.obterId());
	
				registro2Table.add(link);
			} 
			else
				registro2Table.add("");
			
			this.addEspaco(registro2Table);
	
			registro2Table.addHeader("Afectado por Siniestro:");
			registro2Table.add(apolice.obterAfetadoPorSinistro());
	
			registro2Table.addHeader("Póliza Flotante:");
			registro2Table.add(apolice.obterApoliceFlutuante());
			
			this.addEspaco(registro2Table);
			
			Plano plano = apolice.obterPlano();
			String identificadorPlano = "";
			
			if(plano!=null)
				identificadorPlano = plano.obterIdentificador();
			
			boolean mostraPlano = !identificadorPlano.equals("0") & !identificadorPlano.toLowerCase().equals("n") && !identificadorPlano.equals("");

			registro2Table.addHeader("Modalidad del Plan:");
			if(mostraPlano)
			{
				if (plano != null)
				{
					Link link = new Link(plano.obterPlano(), new Action("visualizarEvento"));
					link.getAction().add("id", apolice.obterPlano().obterId());
		
					registro2Table.add(link);
				} 
				else
					registro2Table.add(apolice.obterCodigoPlano());
			}
			else
				registro2Table.add("");
			
			registro2Table.addHeader("Sección del Plan:");
			if(mostraPlano)
			{
				if (plano != null)
					registro2Table.add(plano.obterSecao());
				else
					registro2Table.add("");
			}
			else
				registro2Table.add("");
			
			this.addEspaco(registro2Table);
			
			registro2Table.addHeader("Identificador del Plan:");
			if(mostraPlano)
			{
				if (plano != null)
					registro2Table.add(identificadorPlano);
				else
					registro2Table.add("");
			}
			else
				registro2Table.add("");
			
			registro2Table.addHeader("Número de la Factura:");
			registro2Table.add(apolice.obterNumeroFatura());
			
			this.addEspaco(registro2Table);
	
			registro2Table.addHeader("Fecha de Inicio de la Vigencia:");
			registro2Table.add(new Label(apolice.obterDataPrevistaInicio(),"dd/MM/yyyy"));
			
			registro2Table.addHeader("Fecha de Finalización de la Vigencia:");
			registro2Table.add(new Label(apolice.obterDataPrevistaConclusao(),"dd/MM/yyyy"));
			
			this.addEspaco(registro2Table);
	
			registro2Table.addHeader("Modalidad de Venta:");
			registro2Table.add(apolice.obterModalidadeVenda());
			
			registro2Table.addHeader("Dias de cobetura:");
			registro2Table.add(new Label(apolice.obterDiasCobertura()));
			
			this.addEspaco(registro2Table);
	
			registro2Table.addHeader("Fecha de Emisión:");
			registro2Table.add(new Label(apolice.obterDataEmissao(),"dd/MM/yyyy"));
			
			registro2Table.addHeader("Forma de Pago:");
			registro2Table.add(apolice.obterFormaPagamento());
			
			this.addEspaco(registro2Table);
	
			registro2Table.addHeader("Cantidad de Cuotas:");
			registro2Table.add(new Label(apolice.obterQtdeParcelas()));
			
			registro2Table.addHeader("Refinanciación:");
			registro2Table.add(new Label(apolice.obterRefinacao()));
			
			this.addEspaco(registro2Table);
	
			registro2Table.addHeader("Agente / Corredor:");
	
			Entidade agenteEnt = apolice.obterInscricaoAgente();
			Entidade corredor = apolice.obterCorredor();
			boolean temAgente = false;
			
			Block block = new Block(Block.VERTICAL);
			
			if (agenteEnt != null)
			{
				Link link = new Link(agenteEnt.obterNome(), new Action("visualizarDetalhesEntidade"));
				link.getAction().add("id", agenteEnt.obterId());
				
				block.add(link);
				temAgente = true;
			}
			
			if (corredor != null)
			{
				Link link = new Link(corredor.obterNome(), new Action("visualizarDetalhesEntidade"));
				link.getAction().add("id", corredor.obterId());
				
				if(agenteEnt!=null)
				{
					block.add(new Space(2));
					block.add(new Label("/"));
					block.add(new Space(2));
				}
				
				block.add(link);
				temAgente = true;
			}
			
			if(!temAgente)
				registro2Table.add("0000");
			else
				registro2Table.add(block);
			
			registro2Table.addHeader("Situación del Seguro:");
			registro2Table.add(apolice.obterSituacaoSeguro());
			
			this.addEspaco(registro2Table);
			
			registro2Table.add("");
			registro2Table.add("");
			
			table.add(registro2Table);
			
			registro2Table = new Table(5);
			registro2Table.addStyle(Table.STYLE_ALTERNATE);
			registro2Table.addSubtitle("Valores del Instrumento");
	
			registro2Table.addHeader("Capital en Riesgo en Gs:");
			registro2Table.add(new Label(apolice.obterCapitalGs(), "#,##0.00"));
	
			this.addEspaco(registro2Table);
			
			registro2Table.addHeader("Capital en Riesgo en M/E:");
			registro2Table.add(verificaMoeda(apolice.obterTipoMoedaCapitalGuarani()) + " " + formataValor.format(apolice.obterCapitalMe()));
	
			registro2Table.addHeader("Prima en Gs:");
			registro2Table.add(new Label(apolice.obterPrimaGs(), "#,##0.00"));
	
			this.addEspaco(registro2Table);
			
			registro2Table.addHeader("Prima en M/E:");
			registro2Table.add(verificaMoeda(apolice.obterTipoMoedaPrimaGs()) + " " + formataValor.format(apolice.obterPrimaMe()));
	
			registro2Table.addHeader("Principal en Gs:");
			registro2Table.add(new Label(apolice.obterPrincipalGs(), "#,##0.00"));

			this.addEspaco(registro2Table);
			
			registro2Table.addHeader("Principal en M/E:");
			registro2Table.add(verificaMoeda(apolice.obterTipoMoedaPrincipalGs()) + " " + formataValor.format(apolice.obterPrincipalMe()));

			registro2Table.addHeader("Incapacidad en Gs:");
			registro2Table.add(new Label(apolice.obterIncapacidadeGs(), "#,##0.00"));
			
			this.addEspaco(registro2Table);
	
			registro2Table.addHeader("Incapacidad en M/E:");
			registro2Table.add(verificaMoeda(apolice.obterTipoMoedaIncapacidadeGs()) + " " + formataValor.format(apolice.obterIncapacidadeMe()));
	
			registro2Table.addHeader("Enfermidad en Gs:");
			registro2Table.add(new Label(apolice.obterEnfermidadeGs(), "#,##0.00"));

			this.addEspaco(registro2Table);
			
			registro2Table.addHeader("Enfermidad en M/E:");
			registro2Table.add(verificaMoeda(apolice.obterTipoMoedaEnfermidadeGs()) + " " + formataValor.format(apolice.obterEnfermidadeMe()));
	
			registro2Table.addHeader("Accidentes en Gs:");
			registro2Table.add(new Label(apolice.obterAcidentesGs(), "#,##0.00"));
	
			this.addEspaco(registro2Table);
			
			registro2Table.addHeader("Accidentes en M/E:");
			registro2Table.add(verificaMoeda(apolice.obterTipoMoedaAcidentesGs()) + " " + formataValor.format(apolice.obterAcidentesMe()));
	
			registro2Table.addHeader("Otros en Gs:");
			registro2Table.add(new Label(apolice.obterOutrosGs(), "#,##0.00"));

			this.addEspaco(registro2Table);
			
			registro2Table.addHeader("Otros en M/E:");
			registro2Table.add(verificaMoeda(apolice.obterTipoMoedaOutrosGs()) + " " + formataValor.format(apolice.obterOutrosMe()));
	
			registro2Table.addHeader("Interés por Financiamento en Gs:");
			registro2Table.add(new Label(apolice.obterFinanciamentoGs(), "#,##0.00"));
			
			this.addEspaco(registro2Table);
	
			registro2Table.addHeader("Interés por Financiamento en M/E:");
			registro2Table.add(verificaMoeda(apolice.obterTipoMoedaFinanciamentoGs()) + " " + formataValor.format(apolice.obterFinanciamentoMe()));
	
			registro2Table.addHeader("Premio en Gs:");
			registro2Table.add(new Label(apolice.obterPremiosGs(), "#,##0.00"));
	
			this.addEspaco(registro2Table);
			
			registro2Table.addHeader("Premio en M/E:");
			registro2Table.add(verificaMoeda(apolice.obterTipoMoedaPremiosGs()) + " " + formataValor.format(apolice.obterPremiosMe()));
	
			registro2Table.addHeader("Comisión en Gs:");
			registro2Table.add(new Label(apolice.obterComissaoGs(), "#,##0.00"));

			this.addEspaco(registro2Table);
			
			registro2Table.addHeader("Comisión en M/E:");
			registro2Table.add(verificaMoeda(apolice.obterTipoMoedaComissaoGs()) + " " + formataValor.format(apolice.obterComissaoMe()));
	
			table.add(registro2Table);
		}
		else if(_pasta == ENTIDADES)
		{
			Table tableGeral = new Table(1);
			tableGeral.setWidth("100%");
			
			this.montaTabelaPessoas(apolice, tableGeral, 0);
			this.montaTabelaPessoas(apolice, tableGeral, 1);
			
			table.add(tableGeral);
		}

		return table;
	}
	
	private void montaTabelaPessoas(Apolice apolice, Table tableGeral, int tipo) throws Exception
	{
		Table table2 = new Table(7);
		table2.setWidth("100%");
		table2.addStyle(Table.STYLE_ALTERNATE);
		
		if(tipo == 0)
			table2.addSubtitle("Asegurados".toUpperCase());
		else
			table2.addSubtitle("Tomadores".toUpperCase());
		
		Date dataNascimento;
		Collection<EntidadeBCP> pessoas;
		if(tipo == 0)
			pessoas = apolice.obterAsegurados();
		else
			pessoas = apolice.obterTomadores();
		
		table2.setNextHAlign(Table.HALIGN_CENTER);
		table2.addHeader("Nombre");
		table2.setNextHAlign(Table.HALIGN_CENTER);
		table2.addHeader("Apellido");
		table2.setNextHAlign(Table.HALIGN_CENTER);
		table2.addHeader("Tipo Persona");
		table2.setNextHAlign(Table.HALIGN_CENTER);
		table2.addHeader("Tipo Documento");
		table2.setNextHAlign(Table.HALIGN_CENTER);
		table2.addHeader("Número del Documento");
		table2.setNextHAlign(Table.HALIGN_CENTER);
		table2.addHeader("Fecha de Nacimiento");
		table2.setNextHAlign(Table.HALIGN_CENTER);
		table2.addHeader("País");
		
		if(tipo == 0)
		{
			dataNascimento = apolice.obterDataNascimento();
			
			if(apolice.obterNomeAsegurado()!=null)
			{
				table2.add("(*) " + apolice.obterNomeAsegurado());
				table2.add("");
				table2.setNextHAlign(Table.HALIGN_CENTER);
				table2.add(apolice.obterTipoPessoa());
				table2.setNextHAlign(Table.HALIGN_CENTER);
				table2.add(apolice.obterTipoIdentificacao());
				table2.setNextHAlign(Table.HALIGN_CENTER);
				table2.add(apolice.obterNumeroIdentificacao());
				table2.setNextHAlign(Table.HALIGN_CENTER);
				if(dataNascimento!=null)
					table2.add(new SimpleDateFormat("dd/MM/yyyy").format(dataNascimento));
				else
					table2.add("");
				
				table2.add("");
			}
		}
		else
		{
			if(apolice.obterNomeTomador()!=null)
			{
				table2.add("(*) " + apolice.obterNomeTomador());
				table2.add("");
				table2.add("");
				table2.add("");
				table2.add("");
				table2.add("");
				table2.add("");
			}
		}
		
		for(EntidadeBCP pessoa : pessoas)
		{
			dataNascimento = pessoa.getDataNascimento();
			
			table2.add(pessoa.getNome());
			table2.add(pessoa.getSobreNome());
			table2.setNextHAlign(Table.HALIGN_CENTER);
			table2.add(pessoa.getTipoPessoa());
			table2.setNextHAlign(Table.HALIGN_CENTER);
			table2.add(pessoa.getTipoDocumento());
			table2.setNextHAlign(Table.HALIGN_CENTER);
			table2.add(pessoa.getNumeroDoc());
			table2.setNextHAlign(Table.HALIGN_CENTER);
			if(dataNascimento!=null)
				table2.add(new SimpleDateFormat("dd/MM/yyyy").format(dataNascimento));
			else
				table2.add("");
			
			table2.setNextHAlign(Table.HALIGN_CENTER);
			table2.add(pessoa.getPais());
		}
		
		tableGeral.add(table2);
	}
	
	private void addEspaco(Table table)
	{
		table.add(new Space(15));
	}
	
	private String verificaMoeda(String moeda)
	{
		if(moeda == null)
			return "";
		else
		{
			if(moeda.toLowerCase().indexOf("guara")>-1)
				return "";
			else
				return moeda;
		}
	}
}