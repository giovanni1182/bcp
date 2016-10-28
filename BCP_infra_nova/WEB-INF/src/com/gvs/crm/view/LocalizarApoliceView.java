package com.gvs.crm.view;

import java.util.Collection;
import java.util.Date;
import java.util.Locale;
import java.util.Properties;

import com.gvs.crm.component.AseguradorasSelect2;
import com.gvs.crm.component.PlanoSecaoSelect;
import com.gvs.crm.component.PlanoSelect;
import com.gvs.crm.component.SituacaoApoliceSelect2;
import com.gvs.crm.component.TipoInstrumentosSelect;
import com.gvs.crm.model.Aseguradora;
import com.gvs.crm.model.Entidade;

import infra.control.Action;
import infra.security.User;
import infra.view.Block;
import infra.view.Button;
import infra.view.Image;
import infra.view.InputDate;
import infra.view.InputString;
import infra.view.Label;
import infra.view.Space;
import infra.view.Table;
import infra.view.View;

public class LocalizarApoliceView extends PortalView 
{
	private Collection apolices;

	private String numeroApolice;

	private String secao;

	private Aseguradora aseguradora;

	private String nomeAsegurado, nomeAsegurado1, nomeAsegurado2;

	private String plano;

	private Entidade origemMenu;
	private int pagina;
	private String situacao;
	private Date dataInicio,dataFim;
	private String rucCi;
	private String tomador, tipoInstrumento;

	public LocalizarApoliceView(Collection apolices, String numeroApolice, String secao, Aseguradora aseguradora, String nomeAsegurado, String plano, Entidade origemMenu, int pagina,String situacao, Date dataInicio, Date dataFim, String rucCi, String tomador,String tipoInstrumento, String nomeAsegurado1, String nomeAsegurado2) 
	{
		this.apolices = apolices;
		this.numeroApolice = numeroApolice;
		this.secao = secao;
		this.aseguradora = aseguradora;
		this.nomeAsegurado = nomeAsegurado;
		this.plano = plano;
		this.origemMenu = origemMenu;
		this.pagina = pagina;
		this.situacao = situacao;
		this.dataInicio = dataInicio;
		this.dataFim = dataFim;
		this.rucCi = rucCi;
		this.tomador = tomador;
		this.tipoInstrumento = tipoInstrumento;
		this.nomeAsegurado1 = nomeAsegurado1;
		this.nomeAsegurado2 = nomeAsegurado2;
	}

	public View getBody(User user, Locale locale, Properties properties)throws Exception 
	{
		Table mainTable = new Table(1);
		mainTable.setWidth("100%");
		
		Table table = new Table(2);
		table.addSubtitle("Buscar Instrumento");

		table.addHeader("Nº Instrumento:");
		Block block = new Block(Block.HORIZONTAL);
		block.add(new InputString("numeroApolice", this.numeroApolice, 15));
		block.add(new Space(2));
		Image image = new Image("help2.png");
		image.setNote("El N° de Instrumento tiene 10 caracteres, siendo los 4 primeros caracteres el código de la sección utilizado por cada Aseguradora y los 6 últimos el número de la póliza.\nEn caso que tengas de 1 a 9 caracteres del  n° de instrumento, se deberá informar el nombre de la Aseguradora.\nEn caso que tengas los 10 caracteres del n° de instrumento, no hace falta informar el nombre de la Aseguradora.");
		block.add(image);
		table.add(block);
		
		table.addHeader("Instrumento:");
		table.add(new TipoInstrumentosSelect("tipoInstrumento", this.tipoInstrumento));

		table.addHeader("Sección:");
		
		Action action = new Action("localizarApolices");
		
		block = new Block(Block.HORIZONTAL);
		block.add(new PlanoSecaoSelect("secao", this.secao, action));
		block.add(new Space(2));
		image = new Image("help2.png");
		image.setNote("La Sección elegida en esta opción corresponde a la utilizada por la SIS (diferente a la ingresada en N° de Instrumento que corresponde a cada Aseguradora) y se refiere a las cuentas definidas para el Módulo Contable.");
		block.add(image);
		table.add(block);
		
		table.addHeader("Modalidad:");
		if(!this.secao.equals(""))
			table.add(new PlanoSelect("plano", this.secao, this.plano, null, true));
		else
			table.add("");

		table.addHeader("Aseguradora:");
		table.add(new AseguradorasSelect2("aseguradoraId",aseguradora, false, true));

		table.addHeader("Nombre Asegurado:");
		block = new Block(Block.HORIZONTAL);
		block.add(new InputString("nomeAsegurado", this.nomeAsegurado, 60));
		block.add(new Space(2));
		block.add(new Label("Al menos 10 caracteres"));
		table.add(block);
		
		table.addHeader("Nombre Tomador:");
		block = new Block(Block.HORIZONTAL);
		block.add(new InputString("tomador", this.tomador, 60));
		block.add(new Space(2));
		block.add(new Label("Al menos 10 caracteres"));
		table.add(block);
		
		table.addHeader("Nº del Documento: ");
		block = new Block(Block.HORIZONTAL);
		block.add(new InputString("rucCi",this.rucCi,15));
		block.add(new Space(2));
		block.add(new Label("Al menos 5 dígitos"));
		table.add(block);

		table.addHeader("Situación:");
		table.add(new SituacaoApoliceSelect2("situacao",this.situacao,true));
		
		table.addHeader("Fecha Emisión:");
		block = new Block(Block.HORIZONTAL);
		block.add(new InputDate("dataInicio",this.dataInicio));
		block.add(new Space(2));
		Label label = new Label("hasta");
		label.setBold(true);
		block.add(label);
		block.add(new Space(2));
		block.add(new InputDate("dataFim",this.dataFim));
		
		table.add(block);

		table.setNextColSpan(table.getColumns());
		table.add(new Space());
		table.setNextColSpan(table.getColumns());
		table.setNextHAlign(Table.HALIGN_CENTER);
		Action localizarEntidadeAction = new Action("localizarApolices");
		localizarEntidadeAction.add("origemMenuId", origemMenu.obterId());
		localizarEntidadeAction.add("listar", true);
		
		block = new Block(Block.HORIZONTAL);
		block.add(new Button("Buscar Instrumento", localizarEntidadeAction));
		block.add(new Space(4));
		
		localizarEntidadeAction = new Action("localizarApolices");
		localizarEntidadeAction.add("excel", true);
		localizarEntidadeAction.add("listar", true);
		
		block.add(new Button("Generar Excel", localizarEntidadeAction));
		
		table.add(block);
		
		mainTable.add(table);

		mainTable.add(new ApolicesView(null, this.apolices, false, this.pagina));
		
		return mainTable;
	}

	public String getSelectedGroup() throws Exception
	{
		return "Entidades/Eventos";
	}

	public String getSelectedOption() throws Exception 
	{
		return "Localizar";
	}

	public View getTitle() throws Exception 
	{
		return new Label("Buscar Pólizas");
	}

	public Entidade getOrigemMenu() throws Exception 
	{
		return this.origemMenu;

	}
}