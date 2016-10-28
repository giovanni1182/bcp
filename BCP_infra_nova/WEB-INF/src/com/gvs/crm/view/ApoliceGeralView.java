package com.gvs.crm.view;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

import com.gvs.crm.component.Border;
import com.gvs.crm.model.AnulacaoInstrumento;
import com.gvs.crm.model.Apolice;
import com.gvs.crm.model.AspectosLegais;
import com.gvs.crm.model.DadosCoaseguro;
import com.gvs.crm.model.DadosPrevisao;
import com.gvs.crm.model.DadosReaseguro;
import com.gvs.crm.model.Entidade;
import com.gvs.crm.model.Evento;
import com.gvs.crm.model.FaturaSinistro;
import com.gvs.crm.model.Morosidade;
import com.gvs.crm.model.Plano;
import com.gvs.crm.model.Refinacao;
import com.gvs.crm.model.RegistroAnulacao;
import com.gvs.crm.model.RegistroCobranca;
import com.gvs.crm.model.RegistroGastos;
import com.gvs.crm.model.Sinistro;
import com.gvs.crm.model.Suplemento;

import infra.control.Action;
import infra.security.User;
import infra.view.BasicView;
import infra.view.Block;
import infra.view.Button;
import infra.view.Label;
import infra.view.Link;
import infra.view.Space;
import infra.view.Table;
import infra.view.View;

public class ApoliceGeralView extends BasicView {

	private Apolice apolice;

	public ApoliceGeralView(Apolice apolice) throws Exception 
	{
		this.apolice = apolice;
	}

	public View execute(User arg0, Locale arg1, Properties arg2) throws Exception 
	{
		//AgendaMovimentacao agenda = (AgendaMovimentacao) apolice.obterSuperior();

		Table table = new Table(1);
		//table.addStyle(Table.STYLE_ALTERNATE);
		table.setWidth("100%");

		//DadosPrevisao dadosPrevisao = null;
		AnulacaoInstrumento anulacao = null;
		//AspectosLegais aspectosLegais = null;
		Refinacao refinacao = null;
		Collection<AspectosLegais> aspectosLegais = new ArrayList<>();
		
		Collection<DadosPrevisao> dadosPrevisaoCol = new ArrayList<>();
		Collection<DadosReaseguro> dadosReaseguros = new ArrayList<>();
		Collection<DadosCoaseguro> dadosCoaseguros = new ArrayList<>();
		Map<String,Sinistro> sinistros = new TreeMap<>();
		Map<Long,FaturaSinistro> faturasSinistro = new TreeMap<>();
		Map<Long,RegistroGastos> registrosGastos = new TreeMap<>();
		Collection<RegistroCobranca> registroCobranca = new ArrayList<>();
		Collection<Suplemento> suplementos = new ArrayList<>();
		Map<Long,RegistroAnulacao> registrosAnulacao = new TreeMap<>();
		Collection<Morosidade> morosidades = new ArrayList<>();

		for (Evento e : apolice.obterInferiores())
		{
			if (e instanceof DadosPrevisao)
				dadosPrevisaoCol.add((DadosPrevisao)e);
			else if (e instanceof DadosReaseguro) 
			{
				DadosReaseguro dados = (DadosReaseguro) e;
				dadosReaseguros.add(dados);

				for (Evento e2 : dados.obterInferiores())
				{
					if (e2 instanceof RegistroAnulacao)
						registrosAnulacao.put(dados.obterId(), (RegistroAnulacao) e2);
				}
			} 
			else if (e instanceof DadosCoaseguro)
				dadosCoaseguros.add((DadosCoaseguro)e);
			else if (e instanceof Sinistro)
			{
				Sinistro sinistro = (Sinistro) e;
				
				if(sinistros.containsKey(sinistro.obterNumero()))
				{
					Sinistro s = (Sinistro) sinistros.get(sinistro.obterNumero());
					if(!s.obterSituacao().equals("Pagado"))
					{
						sinistros.remove(sinistro.obterNumero());
						sinistros.put(sinistro.obterNumero(), sinistro);
					}
				}
				else
					sinistros.put(sinistro.obterNumero(), sinistro);

				for (Evento e2 : sinistro.obterInferiores()) 
				{
					if (e2 instanceof FaturaSinistro)
						faturasSinistro.put(e.obterCriacao().getTime(), (FaturaSinistro)e2);
					else if (e2 instanceof RegistroGastos)
						registrosGastos.put(e.obterCriacao().getTime(), (RegistroGastos)e2);
				}
			} 
			else if (e instanceof AnulacaoInstrumento)
				anulacao = (AnulacaoInstrumento) e;
			else if (e instanceof RegistroCobranca)
				registroCobranca.add((RegistroCobranca)e);
			else if (e instanceof Suplemento)
				suplementos.add((Suplemento)e);
			else if (e instanceof AspectosLegais)
				aspectosLegais.add((AspectosLegais)e);
			else if (e instanceof Refinacao)
				refinacao = (Refinacao) e;
			else if (e instanceof Morosidade)
				morosidades.add((Morosidade)e);
		}

		Table registro1Table = new Table(2);

		registro1Table.addSubtitle("Cabecera");

		registro1Table.addHeader("Creación:");
		registro1Table.add(new Label(apolice.obterCriacao(), "dd/MM/yyyy HH:mm:ss"));
		
		registro1Table.addHeader("Aseguradora:");
		registro1Table.add(apolice.obterOrigem().obterNome());

		registro1Table.addHeader("Responsable:");
		registro1Table.add(apolice.obterResponsavel().obterNome());

		Link link = new Link("Asegurados y Tomadores", new Action("visualizarEvento"));
		link.getAction().add("id", apolice.obterId());
		link.getAction().add("_pastaApolice", 2);
		
		registro1Table.add("");
		registro1Table.add(link);
		
		table.add(registro1Table);

		Table registro2Table = new Table(5);
		registro2Table.addStyle(Table.STYLE_ALTERNATE);

		registro2Table.addSubtitle("Instrumento");

		registro2Table.addHeader("Número del Instrumento:");
		registro2Table.add(apolice.obterNumeroApolice());
		
		this.addEspaco(registro2Table);

		registro2Table.addHeader("Instrumento:");
		registro2Table.add(apolice.obterStatusApolice());

		registro2Table.addHeader("Instrumento Anterior:");
		if (apolice.obterApoliceAnterior() != null)
		{
			link = new Link(apolice.obterApoliceAnterior().obterNumeroApolice(), new Action("visualizarEvento"));
			link.getAction().add("id", apolice.obterApoliceAnterior().obterId());

			registro2Table.add(link);
		} 
		else
			registro2Table.add("");
		
		this.addEspaco(registro2Table);

		registro2Table.addHeader("Tipo Instrumento:");
		registro2Table.add(apolice.obterTipo());

		registro2Table.addHeader("Póliza Madre:");
		if (apolice.obterSuperior() instanceof Apolice) 
		{
			Apolice apoliceSupeior = (Apolice) apolice.obterSuperior();
			link = new Link(apoliceSupeior.obterNumeroApolice(),new Action("visualizarEvento"));
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
				link = new Link(plano.obterPlano(), new Action("visualizarEvento"));
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
		registro2Table.add(new SimpleDateFormat("dd/MM/yyyy").format(apolice.obterDataPrevistaInicio()));

		registro2Table.addHeader("Fecha de Finalización de la Vigencia:");
		registro2Table.add(new SimpleDateFormat("dd/MM/yyyy").format(apolice.obterDataPrevistaConclusao()));

		this.addEspaco(registro2Table);
		
		registro2Table.addHeader("Modalidad de Venta:");
		registro2Table.add(apolice.obterModalidadeVenda());

		registro2Table.addHeader("Dias de cobetura:");
		registro2Table.add(new Label(apolice.obterDiasCobertura()));

		this.addEspaco(registro2Table);
		
		registro2Table.addHeader("Fecha de Emisión:");
		registro2Table.add(new SimpleDateFormat("dd/MM/yyyy").format(apolice.obterDataEmissao()));

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
		
		Block block = new Block(Block.HORIZONTAL);
		
		if (agenteEnt != null)
		{
			link = new Link(agenteEnt.obterNome(), new Action("visualizarDetalhesEntidade"));
			link.getAction().add("id", agenteEnt.obterId());
			
			block.add(link);
			temAgente = true;
		}
		
		if (corredor != null)
		{
			link = new Link(corredor.obterNome(), new Action("visualizarDetalhesEntidade"));
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

		registro2Table.addSubtitle("Valores del Instrumento");

		registro2Table.addHeader("Capital en Riesgo en Gs:");
		registro2Table.add(new Label(apolice.obterCapitalGs(), "#,##0.00"));
		
		this.addEspaco(registro2Table);

		registro2Table.addHeader("Capital en Riesgo en M/E:");
		Block block4 = new Block(Block.HORIZONTAL);
		block4.add(new Label(verificaMoeda(apolice.obterTipoMoedaCapitalGuarani())));
		block4.add(new Space(2));
		block4.add(new Label(apolice.obterCapitalMe(), "#,##0.00"));

		registro2Table.add(block4);

		registro2Table.addHeader("Prima en Gs:");
		registro2Table.add(new Label(apolice.obterPrimaGs(), "#,##0.00"));

		this.addEspaco(registro2Table);
		
		registro2Table.addHeader("Prima en M/E:");
		Block block5 = new Block(Block.HORIZONTAL);
		block5.add(new Label(verificaMoeda(apolice.obterTipoMoedaPrimaGs())));
		block5.add(new Space(2));
		block5.add(new Label(apolice.obterPrimaMe(), "#,##0.00"));

		registro2Table.add(block5);

		registro2Table.addHeader("Principal en Gs:");
		registro2Table.add(new Label(apolice.obterPrincipalGs(), "#,##0.00"));

		this.addEspaco(registro2Table);
		
		registro2Table.addHeader("Principal en M/E:");
		Block block6 = new Block(Block.HORIZONTAL);
		block6.add(new Label(verificaMoeda(apolice.obterTipoMoedaPrincipalGs())));
		block6.add(new Space(2));
		block6.add(new Label(apolice.obterPrincipalMe(), "#,##0.00"));

		registro2Table.add(block6);

		registro2Table.addHeader("Incapacidad en Gs:");
		registro2Table.add(new Label(apolice.obterIncapacidadeGs(), "#,##0.00"));

		this.addEspaco(registro2Table);
		
		registro2Table.addHeader("Incapacidad en M/E:");
		Block block7 = new Block(Block.HORIZONTAL);
		block7.add(new Label(verificaMoeda(apolice.obterTipoMoedaIncapacidadeGs())));
		block7.add(new Space(2));
		block7.add(new Label(apolice.obterIncapacidadeMe(), "#,##0.00"));

		registro2Table.add(block7);

		registro2Table.addHeader("Enfermidad en Gs:");
		registro2Table.add(new Label(apolice.obterEnfermidadeGs(), "#,##0.00"));

		this.addEspaco(registro2Table);
		
		registro2Table.addHeader("Enfermidad en M/E:");
		Block block8 = new Block(Block.HORIZONTAL);
		block8.add(new Label(verificaMoeda(apolice.obterTipoMoedaEnfermidadeGs())));
		block8.add(new Space(2));
		block8.add(new Label(apolice.obterEnfermidadeMe(), "#,##0.00"));

		registro2Table.add(block8);

		registro2Table.addHeader("Accidentes en Gs:");
		registro2Table.add(new Label(apolice.obterAcidentesGs(), "#,##0.00"));

		this.addEspaco(registro2Table);
		
		registro2Table.addHeader("Accidentes en M/E:");
		Block block9 = new Block(Block.HORIZONTAL);
		block9.add(new Label(verificaMoeda(apolice.obterTipoMoedaAcidentesGs())));
		block9.add(new Space(2));
		block9.add(new Label(apolice.obterAcidentesMe(), "#,##0.00"));

		registro2Table.add(block9);

		registro2Table.addHeader("Otros en Gs:");
		registro2Table.add(new Label(apolice.obterOutrosGs(), "#,##0.00"));

		this.addEspaco(registro2Table);
		
		registro2Table.addHeader("Otros en M/E:");
		Block block10 = new Block(Block.HORIZONTAL);
		block10.add(new Label(verificaMoeda(apolice.obterTipoMoedaOutrosGs())));
		block10.add(new Space(2));
		block10.add(new Label(apolice.obterOutrosMe(), "#,##0.00"));

		registro2Table.add(block10);

		registro2Table.addHeader("Interés por Financiamento en Gs:");
		registro2Table.add(new Label(apolice.obterFinanciamentoGs(), "#,##0.00"));

		this.addEspaco(registro2Table);
		
		registro2Table.addHeader("Interés por Financiamento en M/E:");
		Block block11 = new Block(Block.HORIZONTAL);
		block11.add(new Label(verificaMoeda(apolice.obterTipoMoedaFinanciamentoGs())));
		block11.add(new Space(2));
		block11.add(new Label(apolice.obterFinanciamentoMe(), "#,##0.00"));

		registro2Table.add(block11);

		registro2Table.addHeader("Premio en Gs:");
		registro2Table.add(new Label(apolice.obterPremiosGs(), "#,##0.00"));

		this.addEspaco(registro2Table);
		
		registro2Table.addHeader("Premio en M/E:");
		Block block12 = new Block(Block.HORIZONTAL);
		block12.add(new Label(verificaMoeda(apolice.obterTipoMoedaPremiosGs())));
		block12.add(new Space(2));
		block12.add(new Label(apolice.obterPremiosMe(), "#,##0.00"));

		registro2Table.add(block12);

		registro2Table.addHeader("Comisión en Gs:");
		registro2Table.add(new Label(apolice.obterComissaoGs(), "#,##0.00"));

		this.addEspaco(registro2Table);
		
		registro2Table.addHeader("Comisión en M/E:");
		Block block13 = new Block(Block.HORIZONTAL);
		block13.add(new Label(verificaMoeda(apolice.obterTipoMoedaComissaoGs())));
		block13.add(new Space(2));
		block13.add(new Label(apolice.obterComissaoMe(), "#,##0.00"));

		registro2Table.add(block13);

		table.add(registro2Table);

		Table registro9Table = new Table(6);
		registro9Table.setWidth("60%");
		registro9Table.addStyle(Table.STYLE_ALTERNATE);
		registro9Table.addSubtitle("Registro de Cobranza");

		if (registroCobranca.size() > 0) 
		{
			registro9Table.setNextHAlign(Table.HALIGN_CENTER);
			registro9Table.addHeader("Fecha de Cobro");
			registro9Table.setNextHAlign(Table.HALIGN_CENTER);
			registro9Table.addHeader("Fecha de Vencimento");
			registro9Table.setNextHAlign(Table.HALIGN_CENTER);
			registro9Table.addHeader("Número de la Cuota");
			registro9Table.setNextHAlign(Table.HALIGN_CENTER);
			registro9Table.addHeader("Valor en Gs");
			registro9Table.setNextHAlign(Table.HALIGN_CENTER);
			registro9Table.addHeader("Valor en M/E");
			registro9Table.setNextHAlign(Table.HALIGN_CENTER);
			registro9Table.addHeader("Interés de la Cobranza");
			
			for (RegistroCobranca cobranca : registroCobranca)
			{
				registro9Table.setNextHAlign(Table.HALIGN_CENTER);
				registro9Table.add(new SimpleDateFormat("dd/MM/yyyy").format(cobranca.obterDataCobranca()));
				registro9Table.setNextHAlign(Table.HALIGN_CENTER);
				registro9Table.add(new SimpleDateFormat("dd/MM/yyyy").format(cobranca.obterDataVencimento()));
				registro9Table.setNextHAlign(Table.HALIGN_CENTER);
				registro9Table.add(new Label(cobranca.obterNumeroParcela()));
				registro9Table.setNextHAlign(Table.HALIGN_CENTER);
				registro9Table.add(new Label(cobranca.obterValorCobrancaGs(),"#,##0.00"));
				
				Block block23 = new Block(Block.HORIZONTAL);
				block23.add(new Label(verificaMoeda(cobranca.obterTipoMoedaValorCobrancaGs())));
				block23.add(new Space(2));
				block23.add(new Label(cobranca.obterValorCobrancaMe(),"#,##0.00"));

				registro9Table.setNextHAlign(Table.HALIGN_CENTER);
				registro9Table.add(block23);
				
				registro9Table.setNextHAlign(Table.HALIGN_CENTER);
				registro9Table.add(new Label(cobranca.obterValorInteres(),"#,##0.00"));
			}

			table.add(registro9Table);
		}

		if (refinacao != null) 
		{
			Table registro13Table = new Table(5);
			registro13Table.addStyle(Table.STYLE_ALTERNATE);
			registro13Table.setWidth("60%");
			registro13Table.addSubtitle("Registro de Refinanciación del Instrumento");

			registro13Table.setNextHAlign(Table.HALIGN_CENTER);
			registro13Table.addHeader("Fecha Inicio de Vigencia");
			registro13Table.setNextHAlign(Table.HALIGN_CENTER);
			registro13Table.addHeader("Fecha Final de Vigencia");
			registro13Table.setNextHAlign(Table.HALIGN_CENTER);
			registro13Table.addHeader("Interés por financiamento Gs");
			registro13Table.setNextHAlign(Table.HALIGN_CENTER);
			registro13Table.addHeader("Interés por financiamento M/E");
			registro13Table.setNextHAlign(Table.HALIGN_CENTER);
			registro13Table.addHeader("Cantidad de Cuotas");
			
			registro13Table.setNextHAlign(Table.HALIGN_CENTER);
			registro13Table.add(new SimpleDateFormat("dd/MM/yyyy").format(refinacao.obterDataPrevistaInicio()));
			registro13Table.setNextHAlign(Table.HALIGN_CENTER);
			registro13Table.add(new SimpleDateFormat("dd/MM/yyyy").format(refinacao.obterDataPrevistaConclusao()));
			registro13Table.setNextHAlign(Table.HALIGN_CENTER);
			registro13Table.add(new Label(refinacao.obterFinanciamentoGs(),"#,##0.00"));
			
			Block block24 = new Block(Block.HORIZONTAL);
			block24.add(new Label(verificaMoeda(refinacao.obterTipoMoedaFinanciamentoGs())));
			block24.add(new Space(2));
			block24.add(new Label(refinacao.obterFinanciamentoMe(), "#,##0.00"));

			registro13Table.setNextHAlign(Table.HALIGN_CENTER);
			registro13Table.add(block24);
			
			registro13Table.setNextHAlign(Table.HALIGN_CENTER);
			registro13Table.add(new Label(refinacao.obterQtdeParcelas()));

			table.add(registro13Table);
		}

		if (sinistros.size() > 0) 
		{
			Table registro6AuxTable = new Table(1);
			registro6AuxTable.setWidth("100%");
			registro6AuxTable.addSubtitle("Siniestros");

			Table registro6Table;

			int w = 1;

			for (Sinistro sinistro : sinistros.values())
			{
				registro6Table = new Table(5);
				registro6Table.addStyle(Table.STYLE_ALTERNATE);

				registro6AuxTable.addSubtitle("Siniestro " + w);

				registro6Table.addHeader("Número del Siniestro:");
				registro6Table.add(sinistro.obterNumero());

				this.addEspaco(registro6Table);
				
				registro6Table.addHeader("Fecha del Siniestro:");
				registro6Table.add(new SimpleDateFormat("dd/MM/yyyy").format(sinistro.obterDataSinistro()));

				registro6Table.addHeader("Fecha de la Denuncia:");
				registro6Table.add(new SimpleDateFormat("dd/MM/yyyy").format(sinistro.obterDataDenuncia()));

				this.addEspaco(registro6Table);
				
				registro6Table.addHeader("Número Liquidador:");
				String agente = "";
				if (sinistro.obterAuxiliar() != null)
				{
					agente = sinistro.obterAuxiliar().obterInscricaoAtiva().obterInscricao() + " - " + sinistro.obterAuxiliar().obterNome();
					Link link2 = new Link(agente, new Action("visualizarDetalhesEntidade"));
					link2.getAction().add("id", sinistro.obterAuxiliar().obterId());
					registro6Table.add(link2);
				} 
				else
					registro6Table.add("");

				registro6Table.addHeader("Monto estimado en Gs:");
				registro6Table.add(new Label(sinistro.obterMontanteGs(),"#,##0.00"));

				this.addEspaco(registro6Table);
				
				registro6Table.addHeader("Monto estimado en M/E:");
				Block block17 = new Block(Block.HORIZONTAL);
				block17.add(new Label(verificaMoeda(sinistro.obterTipoMoedaMontanteGs())));
				block17.add(new Space(2));
				block17.add(new Label(sinistro.obterMontanteMe(), "#,##0.00"));

				registro6Table.add(block17);

				registro6Table.addHeader("Situación del Siniestro:");
				registro6Table.add(sinistro.obterSituacao());

				this.addEspaco(registro6Table);
				
				registro6Table.addHeader("Recupero de Terceiro:");
				registro6Table.add(new Label(sinistro.obterValorRecuperacaoTerceiro(), "#,##0.00"));

				registro6Table.addHeader("Fecha del Recupero:");
				if (sinistro.obterDataRecuperacao() != null)
					registro6Table.add(new SimpleDateFormat("dd/MM/yyyy").format(sinistro.obterDataRecuperacao()));
				else
					registro6Table.add("");
				
				this.addEspaco(registro6Table);

				registro6Table.addHeader("Fecha de Finalización del Pago:");
				if (sinistro.obterDataPagamento() != null)
					registro6Table.add(new SimpleDateFormat("dd/MM/yyyy").format(sinistro.obterDataPagamento()));
				else
					registro6Table.add("");

				registro6Table.addHeader("Recupero del Reaseguro:");
				registro6Table.add(new Label(sinistro.obterValorRecuperacao(),"#,##0.00"));

				this.addEspaco(registro6Table);
				
				registro6Table.addHeader("Participación de Reaseguro:");
				registro6Table.add(new Label(sinistro.obterParticipacao(),"#,##0.00"));

				registro6Table.addHeader("Codificacion del Plan:");
				if(sinistro.obterCodificacaoPlano()!=null)
					registro6Table.add(sinistro.obterCodificacaoPlano().obterTitulo());
				else
					registro6Table.add("");
				
				this.addEspaco(registro6Table);
				
				registro6Table.addHeader("Codificacion de Cobertura:");
				if(sinistro.obterCodificacaoCobertura()!=null)
					registro6Table.add(sinistro.obterCodificacaoCobertura().obterTitulo());
				else
					registro6Table.add("");
				
				registro6Table.addHeader("Codificacion de Riesgo:");
				if(sinistro.obterCodificacaoRisco()!=null)
					registro6Table.add(sinistro.obterCodificacaoRisco().obterTitulo());
				else
					registro6Table.add("");
				
				this.addEspaco(registro6Table);
				
				registro6Table.addHeader("Codificacion de Detalle:");
				if(sinistro.obterCodificacaoDetalhe()!=null)
					registro6Table.add(sinistro.obterCodificacaoDetalhe().obterTitulo());
				else
					registro6Table.add("");
				
				registro6Table.addHeader("Descripción:");
				registro6Table.setNextColSpan(4);
				registro6Table.add(sinistro.obterDescricao());

				registro6AuxTable.add(registro6Table);

				Collection<FaturaSinistro> faturasSinistro2 = new ArrayList<>();
				Collection<RegistroGastos> registrosGastos2 = new ArrayList<>();

				for (FaturaSinistro fatura : faturasSinistro.values()) 
				{
					if (fatura.obterSuperior().obterId() == sinistro.obterId())
						faturasSinistro2.add(fatura);
				}

				for (RegistroGastos gastos : registrosGastos.values())
				{
					if (gastos.obterSuperior().obterId() == sinistro.obterId())
						registrosGastos2.add(gastos);
				}

				Table registro14Table = new Table(5);
				registro14Table.addStyle(Table.STYLE_ALTERNATE);
				//registro14Table.setWidth("100%");

				registro14Table.addSubtitle("Registro de Gastos");

				if (registrosGastos2.size() > 0) 
				{
					for (RegistroGastos gastos : registrosGastos2) 
					{
						registro14Table.addHeader("Número Liquidador:");

						String agente2 = "";
						if(gastos.obterAuxiliarSeguro() != null) 
						{
							agente2 = gastos.obterAuxiliarSeguro().obterInscricaoAtiva().obterInscricao() + " - " + gastos.obterAuxiliarSeguro().obterNome();
							Link link3 = new Link(agente2, new Action("visualizarDetalhesEntidade"));
							link3.getAction().add("id",gastos.obterAuxiliarSeguro().obterId());
							registro14Table.add(link3);
						}
						else
							registro14Table.add("");
						
						this.addEspaco(registro14Table);

						registro14Table.addHeader("Tipo de Pago o Gasto:");
						registro14Table.add(gastos.obterTipo());

						registro14Table.addHeader("Nombre de Terceiro:");
						registro14Table.add(gastos.obterNomeTerceiro());

						this.addEspaco(registro14Table);
						
						registro14Table.addHeader("Importe Abonado en Gs:");
						registro14Table.add(new Label(gastos.obterAbonoGs(),"#,##0.00"));

						registro14Table.addHeader("Importe Abonado en M/E:");
						Block block24 = new Block(Block.HORIZONTAL);
						block24.add(new Label(verificaMoeda(gastos.obterTipoMoedaAbonoGs())));
						block24.add(new Space(2));
						block24.add(new Label(gastos.obterAbonoMe(),"#,##0.00"));

						registro14Table.add(block24);
						
						this.addEspaco(registro14Table);

						registro14Table.addHeader("Cargo Banco:");
						String bancoStr = "";
						if (gastos.obterBanco() != null)
						{
							bancoStr = gastos.obterBanco().obterApelido() + " - " + gastos.obterBanco().obterNome();
							Link link4 = new Link(bancoStr, new Action("visualizarDetalhesEntidade"));
							link4.getAction().add("id",gastos.obterBanco().obterId());
							registro14Table.add(link4);
						}
						else
							registro14Table.add("");

						registro14Table.addHeader("Fecha de Pago:");
						if (gastos.obterDataPagamento() != null)
							registro14Table.add(new SimpleDateFormat("dd/MM/yyyy").format(gastos.obterDataPagamento()));
						else
							registro14Table.add("");
						
						this.addEspaco(registro14Table);

						registro14Table.addHeader("Número del cheque:");
						registro14Table.add(gastos.obterNumeroCheque());

						registro14Table.addHeader("Situación del Siniestro:");
						registro14Table.add(gastos.obterSituacaoSinistro());
						
						this.addEspaco(registro14Table);
						
						registro14Table.addHeader("Situación del Pago:");
						registro14Table.add(gastos.obterSituacaoPagamento());

						registro6AuxTable.add(registro14Table);
					}
				}

				Table registro7Table = new Table(7);
				registro7Table.addStyle(Table.STYLE_ALTERNATE);
				registro7Table.setWidth("60%");

				registro7Table.addSubtitle("Registro de Facturas Recibidas y Pagadas a Proveedores");

				registro7Table.setNextHAlign(Table.HALIGN_CENTER);
				registro7Table.addHeader("Factura");
				registro7Table.setNextHAlign(Table.HALIGN_CENTER);
				registro7Table.addHeader("Proveedor");
				registro7Table.setNextHAlign(Table.HALIGN_CENTER);
				registro7Table.addHeader("Monto");
				registro7Table.setNextHAlign(Table.HALIGN_CENTER);
				registro7Table.addHeader("Documento");
				registro7Table.setNextHAlign(Table.HALIGN_CENTER);
				registro7Table.addHeader("Fecha del Documento");
				registro7Table.setNextHAlign(Table.HALIGN_CENTER);
				registro7Table.addHeader("Pago");
				registro7Table.setNextHAlign(Table.HALIGN_CENTER);
				registro7Table.addHeader("Situación");

				if (faturasSinistro2.size() > 0)
				{
					for (FaturaSinistro fatura : faturasSinistro2) 
					{
						registro7Table.setNextHAlign(Table.HALIGN_CENTER);
						registro7Table.add(fatura.obterNumeroFatura());
						String provedor = fatura.obterRucProvedor() + " - "	+ fatura.obterNomeProvedor();
						registro7Table.add(provedor);
						registro7Table.setNextHAlign(Table.HALIGN_CENTER);
						registro7Table.add(new Label(fatura.obterMontanteDocumento(), "#,##0.00"));
						String documento = fatura.obterTipo() + " - "+ fatura.obterNumeroDocumento();
						registro7Table.add(documento);
						registro7Table.setNextHAlign(Table.HALIGN_CENTER);
						if (fatura.obterDataDocumento() != null)
							registro7Table.add(new SimpleDateFormat("dd/MM/yyyy").format(fatura.obterDataDocumento()));
						else
							registro7Table.add("");
						registro7Table.setNextHAlign(Table.HALIGN_CENTER);
						if (fatura.obterDataPagamento() != null)
							registro7Table.add(new SimpleDateFormat("dd/MM/yyyy").format(fatura.obterDataPagamento()));
						else
							registro7Table.add("");
						
						registro7Table.add(fatura.obterSituacaoFatura());
					}

					registro6AuxTable.add(registro7Table);
				}
				w++;
			}

			table.add(registro6AuxTable);
		}

		if (anulacao != null) 
		{
			Table registro8Table = new Table(5);
			registro8Table.addStyle(Table.STYLE_ALTERNATE);
			//registro8Table.setWidth("100%");

			registro8Table.addSubtitle("Registro de Anulación del Instrumento");

			registro8Table.addHeader("Fecha Anulación:");
			registro8Table.add(new SimpleDateFormat("dd/MM/yyyy").format(anulacao.obterDataAnulacao()));

			this.addEspaco(registro8Table);
			
			registro8Table.addHeader("Tipo Anulación:");
			registro8Table.add(anulacao.obterTipo());

			registro8Table.addHeader("Capital en Gs:");
			registro8Table.add(new Label(anulacao.obterCapitalGs(), "#,##0.00"));
			
			this.addEspaco(registro8Table);

			registro8Table.addHeader("Capital en M/E:");
			Block block18 = new Block(Block.HORIZONTAL);
			block18.add(new Label(verificaMoeda(anulacao.obterTipoMoedaCapitalGs())));
			block18.add(new Space(2));
			block18.add(new Label(anulacao.obterCapitalMe(), "#,##0.00"));

			registro8Table.add(block18);

			registro8Table.addHeader("Dias corridos:");
			registro8Table.add(new Label(anulacao.obterDiasCorridos()));

			this.addEspaco(registro8Table);
			
			registro8Table.addHeader("Solicitada por:");
			registro8Table.add(anulacao.obterSolicitadoPor());

			registro8Table.addHeader("Prima en Gs:");
			registro8Table.add(new Label(anulacao.obterPrimaGs(), "#,##0.00"));

			this.addEspaco(registro8Table);
			
			registro8Table.addHeader("Prima en M/E:");
			Block block19 = new Block(Block.HORIZONTAL);
			block19.add(new Label(verificaMoeda(anulacao.obterTipoMoedaPrimaGs())));
			block19.add(new Space(2));
			block19.add(new Label(anulacao.obterPrimaMe(), "#,##0.00"));

			registro8Table.add(block19);

			registro8Table.addHeader("Comisión en Gs:");
			registro8Table.add(new Label(anulacao.obterComissaoGs(), "#,##0.00"));
			
			this.addEspaco(registro8Table);

			registro8Table.addHeader("Comisión en M/E:");
			Block block20 = new Block(Block.HORIZONTAL);
			block20.add(new Label(verificaMoeda(anulacao.obterTipoMoedaComissaoGs())));
			block20.add(new Space(2));
			block20.add(new Label(anulacao.obterComissaoMe(), "#,##0.00"));

			registro8Table.add(block20);

			registro8Table.addHeader("Comisión a recuperar en Gs:");
			registro8Table.add(new Label(anulacao.obterComissaoRecuperarGs(),"#,##0.00"));
			
			this.addEspaco(registro8Table);

			registro8Table.addHeader("Comisión a recuperar en M/E:");
			Block block21 = new Block(Block.HORIZONTAL);
			block21.add(new Label(verificaMoeda(anulacao.obterTipoMoedaComissaoRecuperarGs())));
			block21.add(new Space(2));
			block21.add(new Label(anulacao.obterComissaoRecuperarMe(),"#,##0.00"));

			registro8Table.add(block21);

			registro8Table.addHeader("Saldo en Gs:");
			registro8Table.add(new Label(anulacao.obterSaldoAnulacaoGs(),"#,##0.00"));
			
			this.addEspaco(registro8Table);

			registro8Table.addHeader("Saldo en M/E:");
			Block block22 = new Block(Block.HORIZONTAL);
			block22.add(new Label(verificaMoeda(anulacao.obterTipoMoedaSaldoAnulacaoGs())));
			block22.add(new Space(2));
			block22.add(new Label(anulacao.obterSaldoAnulacaoMe(), "#,##0.00"));

			registro8Table.add(block22);

			registro8Table.addHeader("Destino:");
			registro8Table.add(anulacao.obterDestinoSaldoAnulacao());
			
			this.addEspaco(registro8Table);
			
			registro8Table.add("");
			registro8Table.add("");

			registro8Table.addHeader("Motivo Anulación:");
			registro8Table.setNextColSpan(4);
			registro8Table.add(anulacao.obterDescricao());

			table.add(registro8Table);
		}

		if (dadosPrevisaoCol.size() > 0)
		{
			Table registro3Table = new Table(6);
			registro3Table.addStyle(Table.STYLE_ALTERNATE);
			registro3Table.setWidth("60%");

			registro3Table.addSubtitle("Datos de Provisiones y Reservas");

			registro3Table.setNextHAlign(Table.HALIGN_CENTER);
			registro3Table.addHeader("Fecha del Corte");
			registro3Table.setNextHAlign(Table.HALIGN_CENTER);
			registro3Table.addHeader("Riesgo en Curso");
			registro3Table.setNextHAlign(Table.HALIGN_CENTER);
			registro3Table.addHeader("Siniestros Pendientes");
			registro3Table.setNextHAlign(Table.HALIGN_CENTER);
			registro3Table.addHeader("Reservas Matemáticas");
			registro3Table.setNextHAlign(Table.HALIGN_CENTER);
			registro3Table.addHeader("Fondos de Acumulación");
			registro3Table.setNextHAlign(Table.HALIGN_CENTER);
			registro3Table.addHeader("Deudores por Premio");
			
			for(DadosPrevisao dadosPrevisao : dadosPrevisaoCol)
			{
				registro3Table.setNextHAlign(Table.HALIGN_CENTER);
				if(dadosPrevisao.obterDataCorte()!=null)
					registro3Table.add(new SimpleDateFormat("dd/MM/yyyy").format(dadosPrevisao.obterDataCorte()));
				else
					registro3Table.add("");
				
				registro3Table.setNextHAlign(Table.HALIGN_CENTER);
				registro3Table.add(new Label(dadosPrevisao.obterCurso(), "#,##0.00"));
				registro3Table.setNextHAlign(Table.HALIGN_CENTER);
				registro3Table.add(new Label(dadosPrevisao.obterSinistroPendente(),"#,##0.00"));
				registro3Table.setNextHAlign(Table.HALIGN_CENTER);
				registro3Table.add(new Label(dadosPrevisao.obterReservasMatematicas(), "#,##0.00"));
				registro3Table.setNextHAlign(Table.HALIGN_CENTER);
				registro3Table.add(new Label(dadosPrevisao.obterFundosAcumulados(),	"#,##0.00"));
				registro3Table.setNextHAlign(Table.HALIGN_CENTER);
				registro3Table.add(new Label(dadosPrevisao.obterPremios(),"#,##0.00"));
			}
			
			table.add(registro3Table);
		}

		if (dadosReaseguros.size() > 0) 
		{
			Table registro4AuxTable = new Table(1);

			registro4AuxTable.setWidth("100%");

			registro4AuxTable.addSubtitle("Datos del Reaseguro");

			Table registro4Table;

			int w = 1;

			for (DadosReaseguro dados : dadosReaseguros) 
			{
				registro4Table = new Table(5);
				registro4Table.addStyle(Table.STYLE_ALTERNATE);

				registro4AuxTable.addSubtitle("Reaseguradora " + w);

				registro4Table.addHeader("Reaseguradora:");

				if (dados.obterReaseguradora() != null) 
				{
					Link link2 = new Link(dados.obterReaseguradora().obterNome(), new Action("visualizarDetalhesEntidade"));
					link2.getAction().add("id",dados.obterReaseguradora().obterId());
					registro4Table.add(link2);
				} 
				else
					registro4Table.add("000");
				
				this.addEspaco(registro4Table);

				registro4Table.addHeader("Tipo de Contrato:");
				registro4Table.add(dados.obterTipoContrato());

				registro4Table.addHeader("Corredora:");
				if (dados.obterCorredora() != null) 
				{
					Link link3 = new Link(dados.obterCorredora().obterNome(), new Action("visualizarDetalhesEntidade"));
					link3.getAction().add("id",dados.obterCorredora().obterId());

					registro4Table.add(link3);
				} 
				else
					registro4Table.add("000");
				
				this.addEspaco(registro4Table);

				registro4Table.addHeader("Fecha de Inicio de Vigencia:");
				registro4Table.add(new SimpleDateFormat("dd/MM/yyyy").format(dados.obterDataPrevistaInicio()));

				registro4Table.addHeader("Situación:");
				registro4Table.add(dados.obterSituacao());
				
				this.addEspaco(registro4Table);

				registro4Table.addHeader("Fecha Final de Vigencia:");
				registro4Table.add(new SimpleDateFormat("dd/MM/yyyy").format(dados.obterDataPrevistaConclusao()));

				registro4Table.addHeader("Número del Endoso o Suplemento:");
				registro4Table.add(new Label(dados.obterValorEndoso(),"#,##0.00"));
				
				this.addEspaco(registro4Table);

				registro4Table.addHeader("Capital en Gs:");
				registro4Table.add(new Label(dados.obterCapitalGs(), "#,##0.00"));

				registro4Table.addHeader("Capital en M/E:");
				Block block2 = new Block(Block.HORIZONTAL);
				block2.add(new Label(verificaMoeda(dados.obterTipoMoedaCapitalGs())));
				block2.add(new Space(2));
				block2.add(new Label(dados.obterCapitalMe(), "#,##0.00"));

				registro4Table.add(block2);
				
				this.addEspaco(registro4Table);

				registro4Table.addHeader("Prima en Gs:");
				registro4Table.add(new Label(dados.obterPrimaGs(), "#,##0.00"));

				registro4Table.addHeader("Prima en M/E:");
				Block block3 = new Block(Block.HORIZONTAL);
				block3.add(new Label(verificaMoeda(dados.obterTipoMoedaPrimaGs())));
				block3.add(new Space(2));
				block3.add(new Label(dados.obterPrimaMe(), "#,##0.00"));

				registro4Table.add(block3);
				
				this.addEspaco(registro4Table);

				registro4Table.addHeader("Comisión en Gs:");
				registro4Table.add(new Label(dados.obterComissaoGs(),"#,##0.00"));

				registro4Table.addHeader("Comisión en M/E:");
				Block block14 = new Block(Block.HORIZONTAL);
				block14.add(new Label(verificaMoeda(dados.obterTipoMoedaComissaoGs())));
				block14.add(new Space(2));
				block14.add(new Label(dados.obterComissaoMe(), "#,##0.00"));

				registro4Table.add(block14);
				registro4Table.add("");
				registro4Table.add("");
				registro4Table.add("");

				registro4AuxTable.add(registro4Table);

				Collection<RegistroAnulacao> rAnulacao = new ArrayList<>();

				for (RegistroAnulacao anulacao2 : registrosAnulacao.values()) 
				{
					if (anulacao2.obterSuperior().obterId() == dados.obterId())
						rAnulacao.add(anulacao2);
				}

				Table registro15Table = new Table(5);
				registro15Table.addStyle(Table.STYLE_ALTERNATE);
				registro15Table.addSubtitle("Registro de Anulación de Reaseguro");

				for (RegistroAnulacao anulacao2 : rAnulacao) 
				{
					registro15Table.addHeader("Fecha Anulación:");
					registro15Table.add(new SimpleDateFormat("dd/MM/yyyy").format(anulacao2.obterDataAnulacao()));
					
					this.addEspaco(registro15Table);
					
					registro15Table.addHeader("Tipo de Anulación:");
					registro15Table.add(anulacao2.obterTipo());

					registro15Table.addHeader("Capital Anulado en Gs:");
					registro15Table.add(new Label(anulacao2.obterCapitalGs(), "#,##0.00"));
					
					this.addEspaco(registro15Table);

					registro15Table.addHeader("Capital Anulado en M/E:");
					Block block24 = new Block(Block.HORIZONTAL);
					block24.add(new Label(verificaMoeda(anulacao2.obterTipoMoedaCapitalGs())));
					block24.add(new Space(2));
					block24.add(new Label(anulacao2.obterCapitalMe(),"#,##0.00"));

					registro15Table.add(block24);

					registro15Table.addHeader("Prima Anulada en Gs:");
					registro15Table.add(new Label(anulacao2.obterPrimaGs(),"#,##0.00"));
					
					this.addEspaco(registro15Table);

					registro15Table.addHeader("Prima Anulada en M/E:");
					Block block25 = new Block(Block.HORIZONTAL);
					block25.add(new Label(verificaMoeda(anulacao2.obterTipoMoedaCapitalGs())));
					block25.add(new Space(2));
					block25.add(new Label(anulacao2.obterPrimaMe(), "#,##0.00"));

					registro15Table.add(block25);

					registro15Table.addHeader("Comisión Anulada en Gs:");
					registro15Table.add(new Label(anulacao2.obterComissaoGs(),"#,##0.00"));
					
					this.addEspaco(registro15Table);

					registro15Table.addHeader("Comisión Anulada en M/E:");
					Block block26 = new Block(Block.HORIZONTAL);
					block26.add(new Label(verificaMoeda(anulacao2.obterTipoMoedaComissaoGs())));
					block26.add(new Space(2));
					block26.add(new Label(anulacao2.obterComissaoMe(),"#,##0.00"));

					registro15Table.add(block26);

					registro15Table.addHeader("Dias corridos:");
					registro15Table.add(new Label(anulacao2.obterDiasCorridos()));
					registro15Table.add("");
					registro15Table.add("");
					registro15Table.add("");

					registro15Table.addHeader("Motivo de Anulación:");
					registro15Table.setNextColSpan(4);
					registro15Table.add(anulacao2.obterDescricao());

					registro4AuxTable.add(registro15Table);
				}

				w++;
			}

			table.add(registro4AuxTable);
		}

		if (dadosCoaseguros.size() > 0)
		{
			Table registro5Table = new Table(7);
			registro5Table.addStyle(Table.STYLE_ALTERNATE);
			registro5Table.setWidth("60%");
			registro5Table.addSubtitle("Datos del Coaseguro");

			registro5Table.setNextHAlign(Table.HALIGN_CENTER);
			registro5Table.addHeader("Aseguradora");
			registro5Table.setNextHAlign(Table.HALIGN_CENTER);
			registro5Table.addHeader("Grupo");
			registro5Table.setNextHAlign(Table.HALIGN_CENTER);
			registro5Table.addHeader("Porcentagem de Participación");
			registro5Table.setNextHAlign(Table.HALIGN_CENTER);
			registro5Table.addHeader("Capital en Riesgo en Gs");
			registro5Table.setNextHAlign(Table.HALIGN_CENTER);
			registro5Table.addHeader("Capital en Riesgo en M/E");
			registro5Table.setNextHAlign(Table.HALIGN_CENTER);
			registro5Table.addHeader("Prima en Gs");
			registro5Table.setNextHAlign(Table.HALIGN_CENTER);
			registro5Table.addHeader("Prima en M/E");
			
			for (DadosCoaseguro dados : dadosCoaseguros) 
			{
				Link link2 = new Link(dados.obterAseguradora().obterNome(),	new Action("visualizarDetalhesEntidade"));
				link2.getAction().add("id", dados.obterAseguradora().obterId());
				registro5Table.add(link2);

				registro5Table.add(dados.obterGrupo());
				registro5Table.setNextHAlign(Table.HALIGN_CENTER);
				registro5Table.add(new Label(dados.obterParticipacao(),"#,##0.00"));
				registro5Table.setNextHAlign(Table.HALIGN_CENTER);
				registro5Table.add(new Label(dados.obterCapitalGs(), "#,##0.00"));

				Block block15 = new Block(Block.HORIZONTAL);
				block15.add(new Label(verificaMoeda(dados.obterTipoMoedaCapitalGs())));
				block15.add(new Space(2));
				block15.add(new Label(dados.obterCapitalMe(), "#,##0.00"));

				registro5Table.setNextHAlign(Table.HALIGN_CENTER);
				registro5Table.add(block15);
				
				registro5Table.setNextHAlign(Table.HALIGN_CENTER);
				registro5Table.add(new Label(dados.obterPrimaGs(), "#,##0.00"));
				
				Block block16 = new Block(Block.HORIZONTAL);
				block16.add(new Label(verificaMoeda(dados.obterTipoMoedaPrimaGs())));
				block16.add(new Space(2));
				block16.add(new Label(dados.obterPrimaMe(), "#,##0.00"));

				registro5Table.setNextHAlign(Table.HALIGN_CENTER);
				registro5Table.add(block16);
			}
			
			table.add(registro5Table);
		}

		if (aspectosLegais.size() > 0)
		{
			Table registro10Table = new Table(5);
			registro10Table.addStyle(Table.STYLE_ALTERNATE);
			registro10Table.addSubtitle("Registro Sobre Aspectos Legales");

			int w = 1;
			for (AspectosLegais aspectos : aspectosLegais)
			{
				registro10Table.addSubtitle("Aspectos Legales " + w);
				
				registro10Table.addHeader("Número de Orden:");
				registro10Table.add(aspectos.obterNumeroOrdem());
				
				this.addEspaco(registro10Table);
	
				registro10Table.addHeader("Asunto Cuestionado:");
				registro10Table.add(aspectos.obterAssunto());
	
				registro10Table.addHeader("Carácter de la Demanda:");
				registro10Table.add(aspectos.obterTipo());
	
				this.addEspaco(registro10Table);
				
				registro10Table.addHeader("Notificación:");
				if(aspectos.obterDataNotificacao()!=null)
					registro10Table.add(new SimpleDateFormat("dd/MM/yyyy").format(aspectos.obterDataNotificacao()));
				else
					registro10Table.add("");
	
				registro10Table.addHeader("Demandado:");
				registro10Table.add(aspectos.obterDemandado());
	
				this.addEspaco(registro10Table);
				
				registro10Table.addHeader("Actor o Demandante:");
				registro10Table.add(aspectos.obterDemandante());
	
				registro10Table.addHeader("Turno:");
				registro10Table.add(aspectos.obterTurno());
				
				this.addEspaco(registro10Table);
	
				registro10Table.addHeader("Juzgado:");
				registro10Table.add(aspectos.obterJulgado());
	
				registro10Table.addHeader("Número de la Secretaria:");
				registro10Table.add(aspectos.obterSecretaria());
	
				this.addEspaco(registro10Table);
				
				registro10Table.addHeader("Juez:");
				registro10Table.add(aspectos.obterJuiz());
	
				registro10Table.addHeader("Circunscripción:");
				registro10Table.add(aspectos.obterCircunscricao());
	
				this.addEspaco(registro10Table);
				
				registro10Table.addHeader("Abogado:");
				registro10Table.add(aspectos.obterAdvogado());
	
				registro10Table.addHeader("Fecha de la Demanda:");
				if(aspectos.obterDataDemanda()!=null)
					registro10Table.add(new SimpleDateFormat("dd/MM/yyyy").format(aspectos.obterDataDemanda()));
				else
					registro10Table.add("");
	
				this.addEspaco(registro10Table);
				
				registro10Table.addHeader("Fuero:");
				registro10Table.add(aspectos.obterForum());
	
				registro10Table.addHeader("Monto de la Sentencia:");
				registro10Table.add(new Label(aspectos.obterMontanteSentenca(), "#,##0.00"));
	
				this.addEspaco(registro10Table);
				
				registro10Table.addHeader("Monto Demandado:");
				registro10Table.add(new Label(aspectos.obterMontanteDemandado(), "#,##0.00"));
	
				registro10Table.addHeader("Responsabilidad Máxima:");
				registro10Table.add(new Label(aspectos.obterResponsabilidadeMaxima(), "#,##0.00"));
	
				this.addEspaco(registro10Table);
				
				registro10Table.addHeader("Fecha de Cancelación:");
				if(aspectos.obterDataCancelamento()!=null)
					registro10Table.add(new SimpleDateFormat("dd/MM/yyyy").format(aspectos.obterDataCancelamento()));
				else
					registro10Table.add("");
	
				registro10Table.addHeader("Provisión Pendiente:");
				registro10Table.add(new Label(aspectos.obterSinistroPendente(), "#,##0.00"));
				registro10Table.add("");
				registro10Table.add("");
				registro10Table.add("");
	
				registro10Table.addHeader("Objeto de la Causa:");
				registro10Table.setNextColSpan(4);
				registro10Table.add(aspectos.obterObjetoCausa());
	
				registro10Table.addHeader("Observaciones:");
				registro10Table.setNextColSpan(4);
				registro10Table.add(aspectos.obterDescricao());
	
				w++;
			}
			
			table.add(registro10Table);
		}

		if (suplementos.size() > 0) 
		{
			Table registro11Table = new Table(7);
			registro11Table.addStyle(Table.STYLE_ALTERNATE);
			registro11Table.setWidth("60%");

			registro11Table.addSubtitle("Registro de Endoso o Suplemento");
			
			registro11Table.setNextHAlign(Table.HALIGN_CENTER);
			registro11Table.addHeader("Número del Endoso");
			registro11Table.setNextHAlign(Table.HALIGN_CENTER);
			registro11Table.addHeader("Fecha Emisión");
			registro11Table.setNextHAlign(Table.HALIGN_CENTER);
			registro11Table.addHeader("Fecha Vigencia Inicial");
			registro11Table.setNextHAlign(Table.HALIGN_CENTER);
			registro11Table.addHeader("Fecha Vencimiento");
			registro11Table.setNextHAlign(Table.HALIGN_CENTER);
			registro11Table.addHeader("Ajuste Prima em Gs");
			registro11Table.setNextHAlign(Table.HALIGN_CENTER);
			registro11Table.addHeader("Ajuste Prima em M/E");
			registro11Table.setNextHAlign(Table.HALIGN_CENTER);
			registro11Table.addHeader("Razón o causa de su Emisión");
			for (Suplemento suplemento : suplementos) 
			{
				registro11Table.setNextHAlign(Table.HALIGN_CENTER);
				registro11Table.add(suplemento.obterNumero());
				registro11Table.setNextHAlign(Table.HALIGN_CENTER);
				registro11Table.add(new SimpleDateFormat("dd/MM/yyyy").format(suplemento.obterDataEmissao()));
				registro11Table.setNextHAlign(Table.HALIGN_CENTER);
				registro11Table.add(new SimpleDateFormat("dd/MM/yyyy").format(suplemento.obterDataPrevistaInicio()));
				registro11Table.setNextHAlign(Table.HALIGN_CENTER);
				registro11Table.add(new SimpleDateFormat("dd/MM/yyyy").format(suplemento.obterDataPrevistaConclusao()));
				registro11Table.setNextHAlign(Table.HALIGN_CENTER);				
				registro11Table.add(new Label(suplemento.obterPrimaGs(),"#,##0.00"));

				Block block24 = new Block(Block.HORIZONTAL);
				block24.add(new Label(verificaMoeda(suplemento.obterTipoMoedaPrimaGs())));
				block24.add(new Space(2));
				block24.add(new Label(suplemento.obterPrimaMe(), "#,##0.00"));

				registro11Table.setNextHAlign(Table.HALIGN_CENTER);
				registro11Table.add(block24);
				
				registro11Table.add(suplemento.obterRazao());
			}
			
			table.add(registro11Table);
		}

		Table registro16Table = new Table(6);
		registro16Table.addStyle(Table.STYLE_ALTERNATE);
		registro16Table.setWidth("60%");

		registro16Table.addSubtitle("Registro sobre Morosidade");

		registro16Table.setNextHAlign(Table.HALIGN_CENTER);
		registro16Table.addHeader("Fecha del Corte");
		registro16Table.setNextHAlign(Table.HALIGN_CENTER);
		registro16Table.addHeader("Número de Cuota");
		registro16Table.setNextHAlign(Table.HALIGN_CENTER);
		registro16Table.addHeader("Fecha de Vencimiento");
		registro16Table.setNextHAlign(Table.HALIGN_CENTER);
		registro16Table.addHeader("Dias en Atraso");
		registro16Table.setNextHAlign(Table.HALIGN_CENTER);
		registro16Table.addHeader("Valor en Gs");
		registro16Table.setNextHAlign(Table.HALIGN_CENTER);
		registro16Table.addHeader("Valor en Me");

		if (morosidades.size() > 0) 
		{
			for (Morosidade morosidade : morosidades) 
			{
				registro16Table.setNextHAlign(Table.HALIGN_CENTER);
				registro16Table.add(new SimpleDateFormat("dd/MM/yyyy").format(morosidade.obterDataCorte()));
				registro16Table.setNextHAlign(Table.HALIGN_CENTER);
				registro16Table.add(new Label(morosidade.obterNumeroParcela()));
				registro16Table.setNextHAlign(Table.HALIGN_CENTER);
				registro16Table.add(new SimpleDateFormat("dd/MM/yyyy").format(morosidade.obterDataVencimento()));
				registro16Table.setNextHAlign(Table.HALIGN_CENTER);
				registro16Table.add(new Label(morosidade.obterDiasAtraso()));
				registro16Table.setNextHAlign(Table.HALIGN_CENTER);
				registro16Table.add(new Label(morosidade.obterValorGs(),"#,##0.00"));
				
				Block block26 = new Block(Block.HORIZONTAL);
				block26.add(new Label(verificaMoeda(morosidade.obterTipoMoedaValorGs())));
				block26.add(new Space(2));
				block26.add(new Label(morosidade.obterValorMe(), "#,##0.00"));
				
				registro16Table.setNextHAlign(Table.HALIGN_CENTER);
				registro16Table.add(block26);
			}

			table.add(registro16Table);
		}

		Button voltarButton = new Button("Volver", new Action("visualizarEvento"));
		voltarButton.getAction().add("id", apolice.obterId());
		voltarButton.getAction().add("_pastaApolice", "0");

		table.addFooter(voltarButton);

		Border border = new Border(table);

		return border;
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
	
	private void addEspaco(Table table)
	{
		table.add(new Space(15));
	}
}