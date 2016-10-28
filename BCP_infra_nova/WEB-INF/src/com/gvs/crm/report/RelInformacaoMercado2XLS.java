package com.gvs.crm.report;

import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.hssf.util.Region;

import com.gvs.crm.model.ClassificacaoContas;
import com.gvs.crm.model.Conta;
import com.gvs.crm.model.Entidade;
import com.gvs.crm.model.EntidadeHome;

public class RelInformacaoMercado2XLS extends Excel
{
	private Map<String,ClassificacaoContas> cContas;
	private Map<String,Conta> contas;
	private  EntidadeHome home;
	private boolean calculoAnual, anoFiscal;
	private Collection<Entidade> aseguradoras;
	private DecimalFormat formataValor;
	private Date dataInicioReal,dataFimReal,dataInicio;
	private int linha;
	private HSSFSheet planilha;
	private String[] meses;
	private HSSFCellStyle estiloTituloTabela,estiloTituloTabelaC,estiloTextoN_E,estiloTexto, estiloTextoD, estiloTexto_E,estiloTextoN_D;
	
	public RelInformacaoMercado2XLS(Date dataInicio, Date dataFim, int anoInicio, int anoFim, EntidadeHome home, boolean anoFiscal) throws Exception
	{
		String caminho = "C:/tmp/" + new Date().getTime() + ".xls";
		this.home = home;
		this.dataInicio = dataInicio;
		this.anoFiscal = anoFiscal;
		
		FileOutputStream stream = new FileOutputStream(caminho);
		
		this.setCaminho(caminho);
		
		SimpleDateFormat formataData = new SimpleDateFormat("MM/yyyy");
		SimpleDateFormat formataDataAno = new SimpleDateFormat("yyyy");
		formataValor = new DecimalFormat("#,##0.00");
		
		aseguradoras = home.obterAseguradorasSemCoaseguradora();
		
		if(dataInicio!=null)
		{
			String dataInicioStr = formataData.format(dataInicio);
			String dataFimStr = formataData.format(dataFim);
			
			dataInicioReal = formataData.parse(dataInicioStr);
			dataFimReal = formataData.parse(dataFimStr);
		}
		else
		{
			String dataInicioStr = "01/"+anoInicio;
			String dataFimStr = "12/"+anoFim;
			
			dataInicioReal = formataData.parse(dataInicioStr);
			dataFimReal = formataData.parse(dataFimStr);
			
			calculoAnual = true;
		}
		
		this.instanciarContas();
		
		HSSFWorkbook wb = new HSSFWorkbook();
		
		HSSFFont fonteTitulo = wb.createFont();
		fonteTitulo.setFontHeightInPoints((short)10);
	    fonteTitulo.setFontName("Arial");
	    fonteTitulo.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
	    
	    HSSFFont fonteTituloN = wb.createFont();
	    fonteTituloN.setFontHeightInPoints((short)10);
	    fonteTituloN.setFontName("Arial");
	    fonteTituloN.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
	    
	    HSSFCellStyle estiloTitulo = wb.createCellStyle();
        estiloTitulo.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        estiloTitulo.setFont(fonteTitulo);
        
        HSSFFont fonteTexto = wb.createFont();
        fonteTexto.setFontHeightInPoints((short)10);
        fonteTexto.setFontName("Arial");
        
        estiloTexto = wb.createCellStyle();
        estiloTexto.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        estiloTexto.setFont(fonteTexto);
        
        estiloTextoD = wb.createCellStyle();
        estiloTextoD.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
        estiloTextoD.setFont(fonteTexto);
        
        HSSFCellStyle estiloTextoN = wb.createCellStyle();
        estiloTextoN.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        estiloTextoN.setFont(fonteTituloN);
        
        estiloTextoN_D = wb.createCellStyle();
        estiloTextoN_D.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
        estiloTextoN_D.setFont(fonteTituloN);
        
        estiloTextoN_E = wb.createCellStyle();
        estiloTextoN_E.setFont(fonteTituloN);
        
        estiloTexto_E = wb.createCellStyle();
        estiloTexto_E.setFont(fonteTexto);
        
        HSSFCellStyle estiloTitulo_E = wb.createCellStyle();
        estiloTitulo_E.setFont(fonteTitulo);
        
        HSSFFont fonteTituloTabela = wb.createFont();
	    fonteTituloTabela.setFontHeightInPoints((short)10);
	    fonteTituloTabela.setFontName("Arial");
	    fonteTituloTabela.setColor(HSSFColor.WHITE.index);
	    fonteTituloTabela.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        
        estiloTituloTabelaC = wb.createCellStyle();
	    estiloTituloTabelaC.setAlignment(HSSFCellStyle.ALIGN_CENTER);
	    estiloTituloTabelaC.setFont(fonteTituloTabela);
	    estiloTituloTabelaC.setFillForegroundColor(HSSFColor.GREY_80_PERCENT.index);
	    estiloTituloTabelaC.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
	    
	    estiloTituloTabela = wb.createCellStyle();
	    estiloTituloTabela.setFont(fonteTituloTabela);
	    estiloTituloTabela.setFillForegroundColor(HSSFColor.GREY_80_PERCENT.index);
	    estiloTituloTabela.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        
        planilha = wb.createSheet("Plan");
		
		HSSFRow row;
		HSSFCell celula;
		Calendar c, c3Anos;
	    String mes,ano,mesTresAnosAnterior,mesAnoCalculo;
	    ClassificacaoContas conta;
	    Conta conta2;
	    Date data3Anos;
	    
	    linha = 0;
		int coluna = 0;
		
		row = planilha.createRow(linha);
		celula = row.createCell(coluna);
		celula.setCellValue("INFORMACIÓN AGREGADA DEL MERCADO");
		celula.setCellStyle(estiloTitulo_E);
		Region r = new Region(linha, (short)0, linha, (short)50);
		planilha.addMergedRegion(r);
		
		row = planilha.createRow(++linha);
		celula = row.createCell(coluna);
		if(dataInicio!=null)
			celula.setCellValue("Periodo: " + formataData.format(dataInicioReal) + " hasta " + formataData.format(dataFimReal));
		else
		{
			if(anoFiscal)
				celula.setCellValue("Periodo Anõ Fiscal: " + formataDataAno.format(dataInicioReal) + " hasta " + formataDataAno.format(dataFimReal));
			else
				celula.setCellValue("Periodo: " + formataDataAno.format(dataInicioReal) + " hasta " + formataDataAno.format(dataFimReal));
		}
		celula.setCellStyle(estiloTitulo_E);
		r = new Region(linha, (short)0, linha, (short)50);
		planilha.addMergedRegion(r);
		    
		linha+=2;
		int contCalculo = 1;
		
		Map<String,Collection<String>> grupos = new TreeMap<String, Collection<String>>();
		
		Collection<String> calculos = new ArrayList<String>();
	    calculos.add("Primas de seguros directos");
	    calculos.add("Primas Reaseguros Cedidos");
	    calculos.add("Primas reaseguros aceptados - Local");
	    calculos.add("Primas reaseguros aceptados - Exterior");
	    calculos.add("Primas de reaseguros aceptados");
	    calculos.add("Primas de seguros rama vida - sin reservas matematicas");
	    calculos.add("Primas de seguros rama vida -  sin reservas matematicas - reaseguros aceptados");
	    calculos.add("Primas de seguros rama vida - con reservas matematicas");
	    calculos.add("Primas de seguros rama vida - con reservas matematicas - reaseguros aceptados");
	    calculos.add("Primas de seguros directos rama no vida");
	    calculos.add("Primas de seguros directos rama no vida - reaseguros aceptados");
	    
	    grupos.put("A", calculos);
	    
	    calculos = new ArrayList<String>();
	    calculos.add("Incendios");
	    calculos.add("Transportes");
	    calculos.add("Accidentes personales");
	    calculos.add("Automóviles");
	    calculos.add("Accidentes a pasajeros");
	    calculos.add("Robo y asalto");
	    calculos.add("Cristales, vidrios y espejos");
	    calculos.add("Agropecuario");
	    calculos.add("Riesgos varios");
	    calculos.add("Responsabilidad civil");
	    calculos.add("Aeronavegación");
	    calculos.add("Riesgos técnicos");
	    calculos.add("Caución");
	    calculos.add("Vida");
	    calculos.add("TOTAL");
	    
	    grupos.put("B#Composición de la cartera técnica (Primas directas)", calculos);
	    
	    calculos = new ArrayList<String>();
	    calculos.add("Incendios");
	    calculos.add("Transportes");
	    calculos.add("Accidentes personales");
	    calculos.add("Automóviles");
	    calculos.add("Accidentes a pasajeros");
	    calculos.add("Robo y asalto");
	    calculos.add("Cristales, vidrios y espejos");
	    calculos.add("Agropecuario");
	    calculos.add("Riesgos varios");
	    calculos.add("Responsabilidad civil");
	    calculos.add("Aeronavegación");
	    calculos.add("Riesgos técnicos");
	    calculos.add("Caución");
	    calculos.add("Vida");
	    
	    grupos.put("C#Composición de la cartera técnica (Siniestros pagados bruto)", calculos);
	    
	    calculos = new ArrayList<String>();
	    calculos.add("Incendios");
	    calculos.add("Transportes");
	    calculos.add("Accidentes personales");
	    calculos.add("Automóviles");
	    calculos.add("Accidentes a pasajeros");
	    calculos.add("Robo y asalto");
	    calculos.add("Cristales, vidrios y espejos");
	    calculos.add("Agropecuario");
	    calculos.add("Riesgos varios");
	    calculos.add("Responsabilidad civil");
	    calculos.add("Aeronavegación");
	    calculos.add("Riesgos técnicos");
	    calculos.add("Caución");
	    calculos.add("Vida");
	    
	    grupos.put("D#Compocisión de la cartera técnica (Recupero de siniestros)", calculos);
	    
	   calculos = new ArrayList<String>();
	    calculos.add("Incendios");
	    calculos.add("Transportes");
	    calculos.add("Accidentes personales");
	    calculos.add("Automóviles");
	    calculos.add("Accidentes a pasajeros");
	    calculos.add("Robo y asalto");
	    calculos.add("Cristales, vidrios y espejos");
	    calculos.add("Agropecuario");
	    calculos.add("Riesgos varios");
	    calculos.add("Responsabilidad civil");
	    calculos.add("Aeronavegación");
	    calculos.add("Riesgos técnicos");
	    calculos.add("Caución");
	    calculos.add("Vida");
	    
	    grupos.put("E#Composición de la cartera técnica (Siniestralidad)", calculos);
	    
	    calculos = new ArrayList<String>();
	    calculos.add("Comisiones de seguros directos");
	    calculos.add("Tasa media de intermediación");
	    
	    grupos.put("F", calculos);
	    
	    calculos = new ArrayList<String>();
	    calculos.add("Siniestros pagados netos de recuperos de siniestros");
	    calculos.add("Gastos de gestión");
	    calculos.add("Margen de utilidad técnica");
	    
	    grupos.put("G#Esquema de utilización de primas", calculos);
	    
	    calculos = new ArrayList<String>();
	    calculos.add("Primas de reaseguros cedidas al exterior");
	    //calculos.add(73,"Patrimonio Neto");
	    calculos.add("Siniestralidad media rama vida");
	    calculos.add("Siniestralidad media rama patrimonial");
	    
	    grupos.put("H", calculos);
	    
	    calculos = new ArrayList<String>();
	    calculos.add("Provisiones riesgo en curso");
	    calculos.add("Reservas matemáticas");
	    calculos.add("Fondos de acumulación");
	    calculos.add("Provisiones de siniestros");
	    
	    grupos.put("I#Composición de las provisiones y reservas técnicas", calculos);
	    
	    calculos = new ArrayList<String>();
	    calculos.add("Renta fija - Sector privado");
	    calculos.add("Renta variable");
	    calculos.add("Inversiones inmobiliarias");
	    calculos.add("Titulos públicos");
	    calculos.add("Préstamos");
	    calculos.add("TOTAL");
	    
	    grupos.put("J#Componentes de los activos reportados como representativos de las provisiones y reservas técnicas (Guaranies Corrientes)", calculos);
	    
	    calculos = new ArrayList<String>();
	    calculos.add("Agentes de Seguros");
	    calculos.add("Aseguradoras");
	    calculos.add("Reaseguradoras");
	    calculos.add("Intermediarios de seguros");
	    calculos.add("Intermediario de reaseguros");
	    calculos.add("Empresas de auditoria externa");
	    calculos.add("Grupos coaseguradoras");
	    calculos.add("TOTAL");
	    
	    grupos.put("K", calculos);
	    
	    calculos = new ArrayList<String>();
	    calculos.add("Prima seccion vida individual corto plazo");
	    calculos.add("Prima seccion vida seguros colectivos");
	    calculos.add("Prima seccion vida desgravamiento hipotecario");
	    calculos.add("Prima seccion vida defuncion o sepelio");
	    
	    grupos.put("L#Primas de seguros rama vida - sin reservas matematicas (seguros directos)", calculos);
	    
	    calculos = new ArrayList<String>();
	    calculos.add("Primas seccion vida individual -  largo plazo");
	    calculos.add("Primas seccion seguros colectivos - largo plazo");
	    calculos.add("Primas seccion pensiones voluntarias");
	    calculos.add("Primas seccion desgravamen hipotecario - largo plazo");
	    
	    grupos.put("M#Primas de seguros rama vida - con reservas matematicas (seguros directos)", calculos);
	    
	    calculos = new ArrayList<String>();
	    calculos.add("Incendios");
	    calculos.add("Transportes");
	    calculos.add("Accidentes personales");
	    calculos.add("Automóviles");
	    calculos.add("Accidentes a pasajeros");
	    calculos.add("Robo y asalto");
	    calculos.add("Cristales, vidrios y espejos");
	    calculos.add("Agropecuario");
	    calculos.add("Riesgos varios");
	    calculos.add("Responsabilidad civil");
	    calculos.add("Aeronavegación");
	    calculos.add("Riesgos técnicos");
	    calculos.add("Caución");
	    calculos.add("Primas directas renovadas");
	    calculos.add("Primas seccion salud o enfermedad");
	    calculos.add("Primas renovadadas salud o enfermedad largo plazo");
	    calculos.add("TOTAL");
	    
	    grupos.put("N#Primas de seguros rama patrimonial (seguros directos)", calculos);
	    
	    calculos = new ArrayList<String>();
	    calculos.add("Primas reaseguros aceptados  - local");
	    calculos.add("Primas reaseguros aceptados - exterior");
	    calculos.add("Primas reaseguros aceptados vida - local");
	    calculos.add("Primas reaseguros aceptados patrimonial - local");
	    calculos.add("Primas reaseguros aceptados vida - exterior");
	    calculos.add("Primas reaseguros aceptados patrimonial - exterior");
	    
	    grupos.put("O#Reaseguros aceptados", calculos);
	    
	    calculos = new ArrayList<String>();
	    calculos.add("Primas directas automoviles");
	    calculos.add("Primas seccion vida individual - corto plazo");
	    calculos.add("Primas seccion vida colectiva - corto plazo");
	    calculos.add("Primas seccion desgravamen hipotecario - corto plazo");
	    calculos.add("Primas seccion defuncion o sepelio");
	    calculos.add("Incendios");
	    calculos.add("Seguros agrarios");
	    calculos.add("Riesgos varios -cristales vidrios y espejos");
	    calculos.add("Riesgos varios - riesgos varios");
	    calculos.add("Riesgos tecnicos");
	    calculos.add("Transportes");
	    calculos.add("Caucion");
	    calculos.add("Robo y asalto");
	    calculos.add("Responsabilidad civil");
	    calculos.add("Accidentes personales");
	    calculos.add("Accidentes pasajeros");
	    calculos.add("Aeronavegacion");
	    calculos.add("Vida largo plazo seccion vida individual largo plazo");
	    calculos.add("Primas seccion seguros colectivos - largo plazo");
	    calculos.add("Primas seccion pensiones voluntarias");
	    calculos.add("Primas seccion desgravamen hipotercario");
	    calculos.add("Seguro de salud");
	    calculos.add("TOTAL");
	    
	    grupos.put("P#Composición de la cartera técnica (prima de seguro directo)", calculos);
	    
	    calculos = new ArrayList<String>();
	    calculos.add("Capital Social");
	    calculos.add("Cuentas pendientes de capitalizacion");
	    calculos.add("Reservas");
	    calculos.add("Resultados Acumulados");
	    calculos.add("Resultados del Ejercicio");
	    calculos.add("TOTAL");
	    
	    grupos.put("Q#Patrimonio Neto", calculos);
	    
	    calculos = new ArrayList<String>();
	    calculos.add("INVERSIONES");
	    calculos.add("TÍTULOS VALORES DE RENTA FIJA - LOCAL");
	    calculos.add("EMITIDOS POR EL ESTADO Y ENTIDADES PÚBLICAS");
	    calculos.add("Bonos");
	    calculos.add("Letras de Regulación Monetaria");
	    calculos.add("EMITIDOS POR ENTIDADES DEL SECTOR FINANCIERO");
	    calculos.add("Certificados de Depósitos de Ahorro");
	    calculos.add("Títulos de Inversión");
	    calculos.add("EMITIDOS POR ENTIDADES DEL SECTOR PRIVADO");
	    calculos.add("Títulos de Deuda - Sector Privado");
	    calculos.add("EMITIDOS POR EMPRESAS VINCULADAS");
	    calculos.add("Títulos de Deuda - Empresas Vinculadas");
	    calculos.add("DEPÓSITOS RESTRINGIDOS");
	    calculos.add("Depósitos - Restringidos en Sistema Financiero");
	    calculos.add("Depósitos - Embargados");
	    calculos.add("INVERSIONES ESPECIALES - RENTA FIJA");
	    calculos.add("Inversiones Especiales - Renta Fija cuenta 01");
	    calculos.add("INTERESES DEVENGADOS S/RENTA FIJA");
	    calculos.add("TÍTULOS VALORES DE RENTA FIJA - EXTERIOR");
	    calculos.add("EMITIDOS POR EL ESTADO Y ENTIDADES PÚBLICAS - EXTERIOR");
	    calculos.add("Bonos - Exterior");
	    calculos.add("EMITIDOS POR ENTIDADES DEL SECTOR FINANCIERO - Exterior");
	    calculos.add("Certificados de Depósitos de Ahorro - Exterior");
	    calculos.add("Títulos de Inversión - Exterior");
	    calculos.add("EMITIDOS POR ENTIDADES DEL SECTOR PRIVADO - EXTERIOR");
	    calculos.add("Títulos de Deuda - Sector Privado - Exterior");
	    calculos.add("EMITIDOS POR EMPRESAS VINCULADAS - EXTERIOR");
	    calculos.add("Títulos de Deuda - Empresas Vinculadas - Exterior");
	    calculos.add("INVERSIONES ESPECIALES - sobre TÍTULOS DE RENTA FÍSICA - EXTERIOR");
	    calculos.add("Inversiones Especiales - sobre Títulos de Renta Física cuenta 01 - Exterior");
	    calculos.add("INTERESES DEVENGADOS S/RENTA FIJA - EXTERIOR");
	    calculos.add("(PREVISIONES S/TÍTULOS VALORES DE RENTA FIJA)");
	    calculos.add("TÍTULOS VALORES DE RENTA VARIABLE - LOCAL");
	    calculos.add("EMITIDOS POR ENTIDADES DEL SECTOR FINANCIERO - LOCAL");
	    calculos.add("Acciones - Sector Financiero - Local");
	    calculos.add("EMITIDOS POR ENTIDADES DEL SECTOR PRIVADO - LOCAL");
	    calculos.add("Acciones - Sector Privado - Local");
	    calculos.add("EMITIDOS POR EMPRESAS VINCULADAS - LOCAL");
	    calculos.add("Acciones - Empresas Vinculadas - Local");
	    calculos.add("INVERSIONES ESPECIALES - RENTA VARIABLE");
	    calculos.add("Inversiones Especiales - Renta Variable cuenta 01");
	    calculos.add("DIVIDENDOS Y PARTICIPACIONES - RENTA VARIABLE - LOCAL");
	    calculos.add("Dividendos por Cobrar Sector Financiero - Local");
	    calculos.add("Dividendos por Cobrar Sector Privado - Local");
	    calculos.add("Dividendos por Cobrar Empresas Relacionadas - Local");
	    calculos.add("(PREVISIONES S/TÍTULOS VALORES DE RENTA VARIABLE) - LOCAL");
	    calculos.add("TÍTULOS VALORES DE RENTA VARIABLE - EXTERIOR");
	    calculos.add("EMITIDOS POR ENTIDADES DEL SECTOR FINANCIERO - EXTERIOR");
	    calculos.add("Acciones - Sector Financiero - Exterior");
	    calculos.add("EMITIDOS POR ENTIDADES DEL SECTOR PRIVADO - EXTERIOR");
	    calculos.add("Acciones - Sector Privado - Exterior");
	    calculos.add("EMITIDOS POR EMPRESAS VINCULADAS - EXTERIOR");
	    calculos.add("Acciones - Empresas Vinculadas - Exterior");
	    calculos.add("INVERSIONES ESPECIALES - RENTA VARIABLE - EXTERIOR");
	    calculos.add("Inversiones Especiales - Renta Variable cuenta 01 - Exterior");
	    calculos.add("DIVIDENDOS Y PARTICIPACIONES - RENTA VARIABLE - EXTERIOR");
	    calculos.add("Dividendos por Cobrar Sector Financiero - Exterior");
	    calculos.add("Dividendos por Cobrar Sector Privado - Exterior");
	    calculos.add("Dividendos por Cobrar Empresas Relacionadas - Exterior");
	    calculos.add("INVERSIONES POR PRÉSTAMOS");
	    calculos.add("PRÉSTAMOS HIPOTECARIOS");
	    calculos.add("Préstamos Hipotecarios a Directores y Personal Superior");
	    calculos.add("Préstamos Hipotecarios a Empresas Vinculadas");
	    calculos.add("Préstamos Hipotecarios al Personal");
	    calculos.add("Préstamos Hipotecarios a Terceros");
	    calculos.add("PRÉSTAMOS VIDA");
	    calculos.add("Préstamos s/Pólizas Vida");
	    calculos.add("Préstamos Automáticos s/ Pólizas Vida");
	    calculos.add("PRÉSTAMOS POR VENTA DE BIENES");
	    calculos.add("Deudores por Venta de Bienes Inmuebles");
	    calculos.add("Deudores por Venta de Bienes Inmuebles - Entidades Relacionadas");
	    calculos.add("INTERESES DEVENGADOS - PRÉSTAMOS HIPOTECARIOS");
	    calculos.add("INTERESES DEVENGADOS - PRÉSTAMOS VIDA");
	    calculos.add("INTERESES DEVENGADOS S/VENTA DE BIENES A PLAZO");
	    calculos.add("INVERSIONES INMOBILIARIAS");
	    calculos.add("TERRENOS");
	    calculos.add("EDIFICIOS");
	    calculos.add("CONSTRUCCIONES EN CURSO");
	    calculos.add("ANTICIPOS PARA COMPRA DE INMUEBLES");
	    calculos.add("TOTAL");
	    grupos.put("R#INVERSIONES", calculos);
	    
		coluna=1;
		int cont = 0;
		
		c = Calendar.getInstance();
		c.setTime(dataInicioReal);
	    while(c.getTime().compareTo(dataFimReal)<=0)
		{
			cont++;
			if(dataInicio!=null)
				c.add(Calendar.MONTH, 1);
			else
				c.add(Calendar.YEAR, 1);
		}
	    
	    meses = new String[cont];
		c.setTime(dataInicioReal);
		cont = 0;
		
		while(c.getTime().compareTo(dataFimReal)<=0)
		{
			if(dataInicio!=null)
				meses[cont] = formataData.format(c.getTime());
			else
				meses[cont] = formataDataAno.format(c.getTime());
			
			cont++;
			
			if(dataInicio!=null)
				c.add(Calendar.MONTH, 1);
			else
				c.add(Calendar.YEAR, 1);
		}
	    
	    linha++;
	    
	    Map<String, Double> totalMemoria = new TreeMap<String, Double>();
	    Date dataInicioValidadeInscricao = null;
	    Date dataFimValidadeInscricao = null;
	    if(dataInicio!=null)
	    	dataFimValidadeInscricao = new SimpleDateFormat("dd/MM/yyyy").parse("31/12/"+new SimpleDateFormat("yyyy").format(dataFimReal));
	    Collection<Entidade> agentesDeSeguro = home.obterEntidades("AS");
	    //Collection<Entidade> aseguradoras = home.obterEntidades("A");
	    Collection<Entidade> reaseguradoras = home.obterEntidades("R");
	    Collection<Entidade> corredoresSeguros = home.obterEntidades("CS");
	    Collection<Entidade> corredoresReaseguros = home.obterEntidades("CR");
	    Collection<Entidade> auditoresExternos = home.obterEntidades("AE");
	    Collection<Entidade> coaseguradoras = home.obterEntidades("GC");
	    
	    Map<Long,Double> totalGuaranisCorrentesMap = new TreeMap<Long, Double>();
	    Map<Long,Double> totalPrimaRamaPratimonialMap = new TreeMap<Long, Double>();
	    Map<Long,Double> totalCarteiraTecnicalMap = new TreeMap<Long, Double>();
	    Map<Long,Double> totalInversionesMap = new TreeMap<Long, Double>();
	    
	    double totalGuaranisCorrentes;
	    double totalPrimaRamaPratimonial;
	    double totalCarteiraTecnica;
	    double totalInversiones;
	    
	    for(String nomeGrupo : grupos.keySet())
	    {
	    	calculos = grupos.get(nomeGrupo);
	    	
	    	if(nomeGrupo.indexOf("#")>-1)
	    		nomeGrupo = nomeGrupo.substring(nomeGrupo.indexOf("#")+1,nomeGrupo.length());
	    	
	    	//System.out.println("Grupo = " + nomeGrupo);
	    	
	    	coluna = 0;
	    	
	    	row = planilha.createRow(linha);
        	
        	celula = row.createCell(coluna);
        	if(nomeGrupo.length() > 1)
        		celula.setCellValue(nomeGrupo);
        	else
        		celula.setCellValue("");
    		celula.setCellStyle(estiloTituloTabela);
    		
    		coluna = 1;
    		
    		for(int j = 0 ; j < meses.length ; j++)
    		{
    			if(nomeGrupo.equals("Componentes de los activos reportados como representativos de las provisiones y reservas técnicas (Guaranies Corrientes)") || nomeGrupo.equals("Composición de la cartera técnica (prima de seguro directo)") || nomeGrupo.equals("INVERSIONES") || nomeGrupo.equals("Primas de seguros rama patrimonial (seguros directos)"))
    			{
	    			celula = row.createCell(coluna);
	    			celula.setCellValue("Valores " + meses[j]);
	    			celula.setCellStyle(estiloTituloTabelaC);
	    			
	    			celula = row.createCell(++coluna);
	    			celula.setCellValue("Porcentaje " + meses[j]);
	    			celula.setCellStyle(estiloTituloTabelaC);
    			}
    			else
    			{
    				celula = row.createCell(coluna);
	    			celula.setCellValue(meses[j]);
	    			celula.setCellStyle(estiloTituloTabelaC);
    			}
    			
    			coluna++;
    		}
    			
    		linha++;
	    	
    		/*totalGuaranisCorrentes = 0;
	 	    totalPrimaRamaPratimonial = 0;
	 	    totalCarteiraTecnica = 0;*/
	 	   
		    for(String nomeCalculo : calculos)
	    	{
	    		//System.out.println("nomeCalculo = " + nomeCalculo);
		    	
		    	coluna = 0;
		    	
		    	if(nomeCalculo.startsWith("TOTAL"))
		    	{
			    	row = planilha.createRow(linha);
			    	celula = row.createCell(coluna);
					celula.setCellValue(nomeCalculo);
					celula.setCellStyle(estiloTextoN_E);
		    	}
		    	else
		    	{
		    		row = planilha.createRow(linha);
			    	celula = row.createCell(coluna);
					celula.setCellValue(nomeCalculo);
					celula.setCellStyle(estiloTexto_E);
		    	}
				
				c.setTime(dataInicioReal);
				
				 while(c.getTime().compareTo(dataFimReal)<=0)
				 {
					 double total = 0;
					 double totalPorcentagem = 0;
					 boolean mostraPorcentagem = false;
					 totalGuaranisCorrentes = 0;
					 totalPrimaRamaPratimonial = 0;
					 totalCarteiraTecnica = 0;
					 totalInversiones = 0;
					 coluna++;
			    	
			    	mes = new SimpleDateFormat("MM").format(c.getTime());
	    			ano = new SimpleDateFormat("yyyy").format(c.getTime());
	    			mesAnoCalculo = mes+ano;
	    			
	    			if(dataInicio!=null)
	    			{
	    				//dataInicioValidadeInscricao = new SimpleDateFormat("dd/MM/yyyy").parse("01/"+mes+"/"+ano);
	    				dataInicioValidadeInscricao = new SimpleDateFormat("dd/MM/yyyy").parse("31/12/"+ano);
	    				
	    				/*if(mes.equals("02"))
	    					dataFimValidadeInscricao = new SimpleDateFormat("dd/MM/yyyy").parse("28/"+mes+"/"+ano);
	    				else
	    					dataFimValidadeInscricao = new SimpleDateFormat("dd/MM/yyyy").parse("30/"+mes+"/"+ano);*/
	    			}
	    			else
	    			{
	    				dataInicioValidadeInscricao = new SimpleDateFormat("dd/MM/yyyy").parse("01/01/"+ano);
	    				dataFimValidadeInscricao = new SimpleDateFormat("dd/MM/yyyy").parse("30/12/"+ano);
	    			}
	    			
			    	if(nomeCalculo.equals("Primas de seguros directos"))
			    	{
			    		conta = cContas.get("0401000000");
			    		total = this.totalConta(conta, c.getTime());
			    		
			    		totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
			    	}
			    	else if(nomeCalculo.equals("Primas Reaseguros Cedidos"))
			    	{
			    		conta = cContas.get("0501000000");
			    		double valor1 = this.totalConta(conta, c.getTime());
			    		conta = cContas.get("0502000000");
			    		double valor2 = this.totalConta(conta, c.getTime());
			    		
			    		total = valor1 + valor2;
			    		
			    		totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
			    	}
			    	if(nomeCalculo.equals("Primas reaseguros aceptados - Local"))
			    	{
			    		conta = cContas.get("0402000000");
			    		total = this.totalConta(conta, c.getTime());
			    		
			    		totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
			    	}
			    	else if(nomeCalculo.equals("Primas reaseguros aceptados - Exterior"))
			    	{
			    		conta = cContas.get("0403000000");
			    		total = this.totalConta(conta, c.getTime());
			    		
			    		totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
			    	}
			    	else if(nomeCalculo.equals("Primas de reaseguros aceptados"))
			    	{
			    		double valor1 = totalMemoria.get(mesAnoCalculo+"_APrimas reaseguros aceptados - Local");
			    		double valor2 = totalMemoria.get(mesAnoCalculo+"_APrimas reaseguros aceptados - Exterior");
			    		
			    		total = valor1 + valor2;
			    		
			    		totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
			    	}
			    	else if(nomeCalculo.equals("Primas de seguros rama vida - sin reservas matematicas"))
			    	{
			    		conta2 = contas.get("0401012001");
			    		double valor1 = this.totalConta(conta2, c.getTime());
			    		conta2 = contas.get("0401012003");
			    		double valor2 = this.totalConta(conta2, c.getTime());
			    		conta2 = contas.get("0401012006");
			    		double valor3 = this.totalConta(conta2, c.getTime());
			    		conta2 = contas.get("0401012009");
			    		double valor4 = this.totalConta(conta2, c.getTime());
			    		
			    		total = valor1 + valor2 + valor3 + valor4;
			    		
			    		totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
			    	}
			    	else if(nomeCalculo.equals("Primas de seguros rama vida -  sin reservas matematicas - reaseguros aceptados"))
			    	{
			    		conta2 = contas.get("0401012001");
			    		double valor1 = this.totalConta(conta2, c.getTime());
			    		conta2 = contas.get("0401012003");
			    		double valor2 = this.totalConta(conta2, c.getTime());
			    		conta2 = contas.get("0401012006");
			    		double valor3 = this.totalConta(conta2, c.getTime());
			    		
			    		total = valor1 + valor2 + valor3;
			    		
			    		totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
			    	}
			    	else if(nomeCalculo.equals("Primas de seguros rama vida - con reservas matematicas"))
			    	{
			    		conta2 = contas.get("0401012002");
			    		double valor1 = this.totalConta(conta2, c.getTime());
			    		conta2 = contas.get("0401012004");
			    		double valor2 = this.totalConta(conta2, c.getTime());
			    		conta2 = contas.get("0401012005");
			    		double valor3 = this.totalConta(conta2, c.getTime());
			    		conta2 = contas.get("0401012007");
			    		double valor4 = this.totalConta(conta2, c.getTime());
			    		
			    		total = valor1 + valor2 + valor3 + valor4;
			    		
			    		totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
			    	}
			    	else if(nomeCalculo.equals("Primas de seguros rama vida - con reservas matematicas - reaseguros aceptados"))
			    	{
			    		conta2 = contas.get("0402012002");
			    		double valor1 = this.totalConta(conta2, c.getTime());
			    		conta2 = contas.get("0401012004");
			    		double valor2 = this.totalConta(conta2, c.getTime());
			    		conta2 = contas.get("0401012005");
			    		double valor3 = this.totalConta(conta2, c.getTime());
			    		conta2 = contas.get("0401012007");
			    		double valor4 = this.totalConta(conta2, c.getTime());
			    		
			    		total = valor1 + valor2 + valor3 + valor4;
			    		
			    		totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
			    	}
			    	else if(nomeCalculo.equals("Primas de seguros directos rama no vida"))
			    	{
			    		conta = cContas.get("0401010100");
			    		double valor1 = this.totalConta(conta, c.getTime());
			    		conta = cContas.get("0401010200");
			    		double valor2 = this.totalConta(conta, c.getTime());
			    		conta = cContas.get("0401010300");
			    		double valor3 = this.totalConta(conta, c.getTime());
			    		conta = cContas.get("0401010400");
			    		double valor4 = this.totalConta(conta, c.getTime());
			    		conta = cContas.get("0401010500");
			    		double valor5 = this.totalConta(conta, c.getTime());
			    		conta = cContas.get("0401010600");
			    		double valor6 = this.totalConta(conta, c.getTime());
			    		conta = cContas.get("0401010700");
			    		double valor7 = this.totalConta(conta, c.getTime());
			    		conta = cContas.get("0401010800");
			    		double valor8 = this.totalConta(conta, c.getTime());
			    		conta = cContas.get("0401010900");
			    		double valor9 = this.totalConta(conta, c.getTime());
			    		conta = cContas.get("0401011000");
			    		double valor10 = this.totalConta(conta, c.getTime());
			    		conta = cContas.get("0401011100");
			    		double valor11 = this.totalConta(conta, c.getTime());
			    		conta = cContas.get("0401011200");
			    		double valor12 = this.totalConta(conta, c.getTime());
			    		conta = cContas.get("0401011300");
			    		double valor13 = this.totalConta(conta, c.getTime());
			    		
			    		total = valor1 + valor2 + valor3 + valor4 + valor5 + valor6 + valor7 + valor8 + valor9 + valor10 + valor11 + valor12 + valor13;
			    		
			    		totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
			    	}
			    	else if(nomeCalculo.equals("Primas de seguros directos rama no vida - reaseguros aceptados"))
			    	{
			    		conta = cContas.get("0402000000");
			    		double valor1 = this.totalConta(conta, c.getTime());
			    		conta = cContas.get("0403000000");
			    		double valor2 = this.totalConta(conta, c.getTime());
			    		conta = cContas.get("0402010200");
			    		double valor3 = this.totalConta(conta, c.getTime());
			    		conta = cContas.get("0402022000");
			    		double valor4 = this.totalConta(conta, c.getTime());
			    		conta = cContas.get("0403012000");
			    		double valor5 = this.totalConta(conta, c.getTime());
			    		conta = cContas.get("0403022000");
			    		double valor6 = this.totalConta(conta, c.getTime());
			    		
			    		total = valor1 + valor2 - valor3 - valor4 - valor5 - valor6;
			    		
			    		totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
			    	}
			    	else if(nomeCalculo.equals("Incendios") && nomeGrupo.equals("Composición de la cartera técnica (Primas directas)"))
			    	{
			    		conta = cContas.get("0401010100");
			    		double valor1 = this.totalConta(conta, c.getTime());
			    		
			    		total = valor1;
			    		
			    		totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
			    	}
			    	else if(nomeCalculo.equals("Transportes") && nomeGrupo.equals("Composición de la cartera técnica (Primas directas)"))
			    	{
			    		conta = cContas.get("0401010200");
			    		double valor1 = this.totalConta(conta, c.getTime());
			    		
			    		total = valor1;
			    		
			    		totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
			    	}
			    	else if(nomeCalculo.equals("Accidentes personales") && nomeGrupo.equals("Composición de la cartera técnica (Primas directas)"))
			    	{
			    		conta = cContas.get("0401010300");
			    		double valor1 = this.totalConta(conta, c.getTime());
			    		
			    		total = valor1;
			    		
			    		totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
			    	}
			    	else if(nomeCalculo.equals("Automóviles") && nomeGrupo.equals("Composición de la cartera técnica (Primas directas)"))
			    	{
			    		conta = cContas.get("0401010400");
			    		double valor1 = this.totalConta(conta, c.getTime());
			    		
			    		total = valor1;
			    		
			    		totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
			    	}
			    	else if(nomeCalculo.equals("Accidentes a pasajeros") && nomeGrupo.equals("Composición de la cartera técnica (Primas directas)"))
			    	{
			    		conta = cContas.get("0401010500");
			    		double valor1 = this.totalConta(conta, c.getTime());
			    		
			    		total = valor1;
			    		
			    		totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
			    	}
			    	else if(nomeCalculo.equals("Robo y asalto") && nomeGrupo.equals("Composición de la cartera técnica (Primas directas)"))
			    	{
			    		conta = cContas.get("0401010600");
			    		double valor1 = this.totalConta(conta, c.getTime());
			    		
			    		total = valor1;
			    		
			    		totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
			    	}
			    	else if(nomeCalculo.equals("Cristales, vidrios y espejos") && nomeGrupo.equals("Composición de la cartera técnica (Primas directas)"))
			    	{
			    		conta = cContas.get("0401010700");
			    		double valor1 = this.totalConta(conta, c.getTime());
			    		
			    		total = valor1;
			    		
			    		totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
			    	}
			    	else if(nomeCalculo.equals("Agropecuario") && nomeGrupo.equals("Composición de la cartera técnica (Primas directas)"))
			    	{
			    		conta = cContas.get("0401010800");
			    		double valor1 = this.totalConta(conta, c.getTime());
			    		
			    		total = valor1;
			    		
			    		totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
			    	}
			    	else if(nomeCalculo.equals("Riesgos varios") && nomeGrupo.equals("Composición de la cartera técnica (Primas directas)"))
			    	{
			    		conta = cContas.get("0401010900");
			    		double valor1 = this.totalConta(conta, c.getTime());
			    		
			    		total = valor1;
			    		
			    		totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
			    	}
			    	else if(nomeCalculo.equals("Responsabilidad civil") && nomeGrupo.equals("Composición de la cartera técnica (Primas directas)"))
			    	{
			    		conta = cContas.get("0401011000");
			    		double valor1 = this.totalConta(conta, c.getTime());
			    		
			    		total = valor1;
			    		
			    		totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
			    	}
			    	else if(nomeCalculo.equals("Aeronavegación") && nomeGrupo.equals("Composición de la cartera técnica (Primas directas)"))
			    	{
			    		conta = cContas.get("0401011100");
			    		double valor1 = this.totalConta(conta, c.getTime());
			    		
			    		total = valor1;
			    		
			    		totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
			    	}
			    	else if(nomeCalculo.equals("Riesgos técnicos") && nomeGrupo.equals("Composición de la cartera técnica (Primas directas)"))
			    	{
			    		conta = cContas.get("0401011200");
			    		double valor1 = this.totalConta(conta, c.getTime());
			    		
			    		total = valor1;
			    		
			    		totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
			    	}
			    	else if(nomeCalculo.equals("Caución") && nomeGrupo.equals("Composición de la cartera técnica (Primas directas)"))
			    	{
			    		conta = cContas.get("0401011300");
			    		double valor1 = this.totalConta(conta, c.getTime());
			    		
			    		total = valor1;
			    		
			    		totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
			    	}
			    	else if(nomeCalculo.equals("Vida") && nomeGrupo.equals("Composición de la cartera técnica (Primas directas)"))
			    	{
			    		conta = cContas.get("0401012000");
			    		double valor1 = this.totalConta(conta, c.getTime());
			    		
			    		total = valor1;
			    		
			    		totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
			    	}
			    	else if(nomeCalculo.equals("TOTAL") && nomeGrupo.equals("Composición de la cartera técnica (Primas directas)"))
			    	{
			    		double valor1 = totalMemoria.get(mesAnoCalculo+"_"+nomeGrupo+"Incendios");
			    		double valor2 = totalMemoria.get(mesAnoCalculo+"_"+nomeGrupo+"Transportes");
			    		double valor3 = totalMemoria.get(mesAnoCalculo+"_"+nomeGrupo+"Accidentes personales");
			    		double valor4 = totalMemoria.get(mesAnoCalculo+"_"+nomeGrupo+"Automóviles");
			    		double valor5 = totalMemoria.get(mesAnoCalculo+"_"+nomeGrupo+"Accidentes a pasajeros");
			    		double valor6 = totalMemoria.get(mesAnoCalculo+"_"+nomeGrupo+"Robo y asalto");
			    		double valor7 = totalMemoria.get(mesAnoCalculo+"_"+nomeGrupo+"Cristales, vidrios y espejos");
			    		double valor8 = totalMemoria.get(mesAnoCalculo+"_"+nomeGrupo+"Agropecuario");
			    		double valor9 = totalMemoria.get(mesAnoCalculo+"_"+nomeGrupo+"Riesgos varios");
			    		double valor10 = totalMemoria.get(mesAnoCalculo+"_"+nomeGrupo+"Responsabilidad civil");
			    		double valor11 = totalMemoria.get(mesAnoCalculo+"_"+nomeGrupo+"Aeronavegación");
			    		double valor12 = totalMemoria.get(mesAnoCalculo+"_"+nomeGrupo+"Riesgos técnicos");
			    		double valor13 = totalMemoria.get(mesAnoCalculo+"_"+nomeGrupo+"Caución");
			    		double valor14 = totalMemoria.get(mesAnoCalculo+"_"+nomeGrupo+"Vida");
			    		
			    		total = valor1 + valor2 + valor3 + valor4 + valor5 + valor6 + valor7 + valor8 + valor9 + valor10 + valor11 + valor12 + valor13 + valor14;
			    		
			    		totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
			    	}
			    	else if(nomeCalculo.equals("Incendios") && nomeGrupo.equals("Composición de la cartera técnica (Siniestros pagados bruto)"))
			    	{
			    		conta = cContas.get("0506010100");
			    		double valor1 = this.totalConta(conta, c.getTime());
			    		conta = cContas.get("0508010100");
			    		double valor2 = this.totalConta(conta, c.getTime());
			    		conta = cContas.get("0508020100");
			    		double valor3 = this.totalConta(conta, c.getTime());
			    		
			    		total = valor1 + valor2 + valor3;
			    		
			    		totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
			    	}
			    	else if(nomeCalculo.equals("Transportes") && nomeGrupo.equals("Composición de la cartera técnica (Siniestros pagados bruto)"))
			    	{
			    		conta = cContas.get("0506010200");
			    		double valor1 = this.totalConta(conta, c.getTime());
			    		conta = cContas.get("0508010000");
			    		double valor2 = this.totalConta(conta, c.getTime());
			    		conta = cContas.get("0508020200");
			    		double valor3 = this.totalConta(conta, c.getTime());
			    		
			    		total = valor1 + valor2 + valor3;
			    		
			    		totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
			    	}
			    	else if(nomeCalculo.equals("Accidentes personales") && nomeGrupo.equals("Composición de la cartera técnica (Siniestros pagados bruto)"))
			    	{
			    		conta = cContas.get("0506010400");
			    		double valor1 = this.totalConta(conta, c.getTime());
			    		conta = cContas.get("0508010300");
			    		double valor2 = this.totalConta(conta, c.getTime());
			    		conta = cContas.get("0508020300");
			    		double valor3 = this.totalConta(conta, c.getTime());
			    		
			    		total = valor1 + valor2 + valor3;
			    		
			    		totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
			    	}
			    	else if(nomeCalculo.equals("Automóviles") && nomeGrupo.equals("Composición de la cartera técnica (Siniestros pagados bruto)"))
			    	{
			    		conta = cContas.get("0506010400");
			    		double valor1 = this.totalConta(conta, c.getTime());
			    		conta = cContas.get("0508010400");
			    		double valor2 = this.totalConta(conta, c.getTime());
			    		conta = cContas.get("0508020400");
			    		double valor3 = this.totalConta(conta, c.getTime());
			    		
			    		total = valor1 + valor2 + valor3;
			    		
			    		totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
			    	}
			    	else if(nomeCalculo.equals("Accidentes a pasajeros") && nomeGrupo.equals("Composición de la cartera técnica (Siniestros pagados bruto)"))
			    	{
			    		conta = cContas.get("0506010500");
			    		double valor1 = this.totalConta(conta, c.getTime());
			    		conta = cContas.get("0508010500");
			    		double valor2 = this.totalConta(conta, c.getTime());
			    		conta = cContas.get("0508020500");
			    		double valor3 = this.totalConta(conta, c.getTime());
			    		
			    		total = valor1 + valor2 + valor3;
			    		
			    		totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
			    	}
			    	else if(nomeCalculo.equals("Robo y asalto") && nomeGrupo.equals("Composición de la cartera técnica (Siniestros pagados bruto)"))
			    	{
			    		conta = cContas.get("0506010600");
			    		double valor1 = this.totalConta(conta, c.getTime());
			    		conta = cContas.get("0508010600");
			    		double valor2 = this.totalConta(conta, c.getTime());
			    		conta = cContas.get("0508020600");
			    		double valor3 = this.totalConta(conta, c.getTime());
			    		
			    		total = valor1 + valor2 + valor3;
			    		
			    		totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
			    	}
			    	else if(nomeCalculo.equals("Cristales, vidrios y espejos") && nomeGrupo.equals("Composición de la cartera técnica (Siniestros pagados bruto)"))
			    	{
			    		conta = cContas.get("0506010700");
			    		double valor1 = this.totalConta(conta, c.getTime());
			    		conta = cContas.get("0508010700");
			    		double valor2 = this.totalConta(conta, c.getTime());
			    		conta = cContas.get("0508020700");
			    		double valor3 = this.totalConta(conta, c.getTime());
			    		
			    		total = valor1 + valor2 + valor3;
			    		
			    		totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
			    	}
			    	else if(nomeCalculo.equals("Agropecuario") && nomeGrupo.equals("Composición de la cartera técnica (Siniestros pagados bruto)"))
			    	{
			    		conta = cContas.get("0506010800");
			    		double valor1 = this.totalConta(conta, c.getTime());
			    		conta = cContas.get("0508010800");
			    		double valor2 = this.totalConta(conta, c.getTime());
			    		conta = cContas.get("0508020800");
			    		double valor3 = this.totalConta(conta, c.getTime());
			    		
			    		total = valor1 + valor2 + valor3;
			    		
			    		totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
			    	}
			    	else if(nomeCalculo.equals("Riesgos varios") && nomeGrupo.equals("Composición de la cartera técnica (Siniestros pagados bruto)"))
			    	{
			    		conta = cContas.get("0506010900");
			    		double valor1 = this.totalConta(conta, c.getTime());
			    		conta = cContas.get("0508010900");
			    		double valor2 = this.totalConta(conta, c.getTime());
			    		conta = cContas.get("0508020900");
			    		double valor3 = this.totalConta(conta, c.getTime());
			    		
			    		total = valor1 + valor2 + valor3;
			    		
			    		totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
			    	}
			    	else if(nomeCalculo.equals("Responsabilidad civil") && nomeGrupo.equals("Composición de la cartera técnica (Siniestros pagados bruto)"))
			    	{
			    		conta = cContas.get("0506011000");
			    		double valor1 = this.totalConta(conta, c.getTime());
			    		conta = cContas.get("0508011000");
			    		double valor2 = this.totalConta(conta, c.getTime());
			    		conta = cContas.get("0508021000");
			    		double valor3 = this.totalConta(conta, c.getTime());
			    		
			    		total = valor1 + valor2 + valor3;
			    		
			    		totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
			    	}
			    	else if(nomeCalculo.equals("Aeronavegación") && nomeGrupo.equals("Composición de la cartera técnica (Siniestros pagados bruto)"))
			    	{
			    		conta = cContas.get("0506011100");
			    		double valor1 = this.totalConta(conta, c.getTime());
			    		conta = cContas.get("0508011100");
			    		double valor2 = this.totalConta(conta, c.getTime());
			    		conta = cContas.get("0508021100");
			    		double valor3 = this.totalConta(conta, c.getTime());
			    		
			    		total = valor1 + valor2 + valor3;
			    		
			    		totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
			    	}
			    	else if(nomeCalculo.equals("Riesgos técnicos") && nomeGrupo.equals("Composición de la cartera técnica (Siniestros pagados bruto)"))
			    	{
			    		conta = cContas.get("0506011200");
			    		double valor1 = this.totalConta(conta, c.getTime());
			    		conta = cContas.get("0508011200");
			    		double valor2 = this.totalConta(conta, c.getTime());
			    		conta = cContas.get("0508021200");
			    		double valor3 = this.totalConta(conta, c.getTime());
			    		
			    		total = valor1 + valor2 + valor3;
			    		
			    		totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
			    	}
			    	else if(nomeCalculo.equals("Caución") && nomeGrupo.equals("Composición de la cartera técnica (Siniestros pagados bruto)"))
			    	{
			    		conta = cContas.get("0506011300");
			    		double valor1 = this.totalConta(conta, c.getTime());
			    		conta = cContas.get("0508011300");
			    		double valor2 = this.totalConta(conta, c.getTime());
			    		conta = cContas.get("0508021300");
			    		double valor3 = this.totalConta(conta, c.getTime());
			    		
			    		total = valor1 + valor2 + valor3;
			    		
			    		totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
			    	}
			    	else if(nomeCalculo.equals("Vida") && nomeGrupo.equals("Composición de la cartera técnica (Siniestros pagados bruto)"))
			    	{
			    		totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
			    	}
			    	else if(nomeCalculo.equals("Incendios") && nomeGrupo.equals("Compocisión de la cartera técnica (Recupero de siniestros)"))
			    	{
			    		conta = cContas.get("0407010100");
			    		double valor1 = this.totalConta(conta, c.getTime());
			    		
			    		total = valor1;
			    		
			    		totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
			    	}
			    	else if(nomeCalculo.equals("Transportes") && nomeGrupo.equals("Compocisión de la cartera técnica (Recupero de siniestros)"))
			    	{
			    		conta = cContas.get("0407010200");
			    		double valor1 = this.totalConta(conta, c.getTime());
			    		
			    		total = valor1;
			    		
			    		totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
			    	}
			    	else if(nomeCalculo.equals("Accidentes personales") && nomeGrupo.equals("Compocisión de la cartera técnica (Recupero de siniestros)"))
			    	{
			    		conta = cContas.get("0407010300");
			    		double valor1 = this.totalConta(conta, c.getTime());
			    		
			    		total = valor1;
			    		
			    		totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
			    	}
			    	else if(nomeCalculo.equals("Automóviles") && nomeGrupo.equals("Compocisión de la cartera técnica (Recupero de siniestros)"))
			    	{
			    		conta = cContas.get("0407010400");
			    		double valor1 = this.totalConta(conta, c.getTime());
			    		
			    		total = valor1;
			    		
			    		totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
			    	}
			    	else if(nomeCalculo.equals("Accidentes a pasajeros") && nomeGrupo.equals("Compocisión de la cartera técnica (Recupero de siniestros)"))
			    	{
			    		conta = cContas.get("0407010500");
			    		double valor1 = this.totalConta(conta, c.getTime());
			    		
			    		total = valor1;
			    		
			    		totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
			    	}
			    	else if(nomeCalculo.equals("Robo y asalto") && nomeGrupo.equals("Compocisión de la cartera técnica (Recupero de siniestros)"))
			    	{
			    		conta = cContas.get("0407010600");
			    		double valor1 = this.totalConta(conta, c.getTime());
			    		
			    		total = valor1;
			    		
			    		totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
			    	}
			    	else if(nomeCalculo.equals("Cristales, vidrios y espejos") && nomeGrupo.equals("Compocisión de la cartera técnica (Recupero de siniestros)"))
			    	{
			    		conta = cContas.get("0407010700");
			    		double valor1 = this.totalConta(conta, c.getTime());
			    		
			    		total = valor1;
			    		
			    		totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
			    	}
			    	else if(nomeCalculo.equals("Agropecuario") && nomeGrupo.equals("Compocisión de la cartera técnica (Recupero de siniestros)"))
			    	{
			    		conta = cContas.get("0407010800");
			    		double valor1 = this.totalConta(conta, c.getTime());
			    		
			    		total = valor1;
			    		
			    		totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
			    	}
			    	else if(nomeCalculo.equals("Riesgos varios") && nomeGrupo.equals("Compocisión de la cartera técnica (Recupero de siniestros)"))
			    	{
			    		conta = cContas.get("0407010900");
			    		double valor1 = this.totalConta(conta, c.getTime());
			    		
			    		total = valor1;
			    		
			    		totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
			    	}
			    	else if(nomeCalculo.equals("Responsabilidad civil") && nomeGrupo.equals("Compocisión de la cartera técnica (Recupero de siniestros)"))
			    	{
			    		conta = cContas.get("0407011000");
			    		double valor1 = this.totalConta(conta, c.getTime());
			    		
			    		total = valor1;
			    		
			    		totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
			    	}
			    	else if(nomeCalculo.equals("Aeronavegación") && nomeGrupo.equals("Compocisión de la cartera técnica (Recupero de siniestros)"))
			    	{
			    		conta = cContas.get("0407011100");
			    		double valor1 = this.totalConta(conta, c.getTime());
			    		
			    		total = valor1;
			    		
			    		totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
			    	}
			    	else if(nomeCalculo.equals("Riesgos técnicos") && nomeGrupo.equals("Compocisión de la cartera técnica (Recupero de siniestros)"))
			    	{
			    		conta = cContas.get("0407011200");
			    		double valor1 = this.totalConta(conta, c.getTime());
			    		
			    		total = valor1;
			    		
			    		totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
			    	}
			    	else if(nomeCalculo.equals("Caución") && nomeGrupo.equals("Compocisión de la cartera técnica (Recupero de siniestros)"))
			    	{
			    		conta = cContas.get("0407011300");
			    		double valor1 = this.totalConta(conta, c.getTime());
			    		
			    		total = valor1;
			    		
			    		totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
			    	}
			    	else if(nomeCalculo.equals("Vida") && nomeGrupo.equals("Compocisión de la cartera técnica (Recupero de siniestros)"))
			    	{
			    		totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
			    	}
			    	else if(nomeCalculo.equals("Incendios") && nomeGrupo.equals("Composición de la cartera técnica (Siniestralidad)"))
			    	{
			    		double valor1 = totalMemoria.get(mesAnoCalculo+"_Composición de la cartera técnica (Siniestros pagados bruto)Incendios");
			    		double valor2 = totalMemoria.get(mesAnoCalculo+"_Composición de la cartera técnica (Primas directas)Incendios");
			    		
			    		if(valor2 > 0)
			    			total = valor1 / valor2;
			    		
			    		totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
			    	}
			    	else if(nomeCalculo.equals("Transportes") && nomeGrupo.equals("Composición de la cartera técnica (Siniestralidad)"))
			    	{
			    		double valor1 = totalMemoria.get(mesAnoCalculo+"_Composición de la cartera técnica (Siniestros pagados bruto)Transportes");
			    		double valor2 = totalMemoria.get(mesAnoCalculo+"_Composición de la cartera técnica (Primas directas)Transportes");
			    		
			    		if(valor2 > 0)
			    			total = valor1 / valor2;
			    		
			    		totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
			    	}
			    	else if(nomeCalculo.equals("Accidentes personales") && nomeGrupo.equals("Composición de la cartera técnica (Siniestralidad)"))
			    	{
			    		double valor1 = totalMemoria.get(mesAnoCalculo+"_Composición de la cartera técnica (Siniestros pagados bruto)Accidentes personales");
			    		double valor2 = totalMemoria.get(mesAnoCalculo+"_Composición de la cartera técnica (Primas directas)Accidentes personales");
			    		
			    		if(valor2 > 0)
			    			total = valor1 / valor2;
			    		
			    		totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
			    	}
			    	else if(nomeCalculo.equals("Automóviles") && nomeGrupo.equals("Composición de la cartera técnica (Siniestralidad)"))
			    	{
			    		double valor1 = totalMemoria.get(mesAnoCalculo+"_Composición de la cartera técnica (Siniestros pagados bruto)Automóviles");
			    		double valor2 = totalMemoria.get(mesAnoCalculo+"_Composición de la cartera técnica (Primas directas)Automóviles");
			    		
			    		if(valor2 > 0)
			    			total = valor1 / valor2;
			    		
			    		totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
			    	}
			    	else if(nomeCalculo.equals("Accidentes a pasajeros") && nomeGrupo.equals("Composición de la cartera técnica (Siniestralidad)"))
			    	{
			    		double valor1 = totalMemoria.get(mesAnoCalculo+"_Composición de la cartera técnica (Siniestros pagados bruto)Accidentes a pasajeros");
			    		double valor2 = totalMemoria.get(mesAnoCalculo+"_Composición de la cartera técnica (Primas directas)Accidentes a pasajeros");
			    		
			    		if(valor2 > 0)
			    			total = valor1 / valor2;
			    		
			    		totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
			    	}
			    	else if(nomeCalculo.equals("Robo y asalto") && nomeGrupo.equals("Composición de la cartera técnica (Siniestralidad)"))
			    	{
			    		double valor1 = totalMemoria.get(mesAnoCalculo+"_Composición de la cartera técnica (Siniestros pagados bruto)Robo y asalto");
			    		double valor2 = totalMemoria.get(mesAnoCalculo+"_Composición de la cartera técnica (Primas directas)Robo y asalto");
			    		
			    		if(valor2 > 0)
			    			total = valor1 / valor2;
			    		
			    		totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
			    	}
			    	else if(nomeCalculo.equals("Cristales, vidrios y espejos") && nomeGrupo.equals("Composición de la cartera técnica (Siniestralidad)"))
			    	{
			    		double valor1 = totalMemoria.get(mesAnoCalculo+"_Composición de la cartera técnica (Siniestros pagados bruto)Cristales, vidrios y espejos");
			    		double valor2 = totalMemoria.get(mesAnoCalculo+"_Composición de la cartera técnica (Primas directas)Cristales, vidrios y espejos");
			    		
			    		if(valor2 > 0)
			    			total = valor1 / valor2;
			    		
			    		totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
			    	}
			    	else if(nomeCalculo.equals("Agropecuario") && nomeGrupo.equals("Composición de la cartera técnica (Siniestralidad)"))
			    	{
			    		double valor1 = totalMemoria.get(mesAnoCalculo+"_Composición de la cartera técnica (Siniestros pagados bruto)Agropecuario");
			    		double valor2 = totalMemoria.get(mesAnoCalculo+"_Composición de la cartera técnica (Primas directas)Agropecuario");
			    		
			    		if(valor2 > 0)
			    			total = valor1 / valor2;
			    		
			    		totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
			    	}
			    	else if(nomeCalculo.equals("Riesgos varios") && nomeGrupo.equals("Composición de la cartera técnica (Siniestralidad)"))
			    	{
			    		double valor1 = totalMemoria.get(mesAnoCalculo+"_Composición de la cartera técnica (Siniestros pagados bruto)Riesgos varios");
			    		double valor2 = totalMemoria.get(mesAnoCalculo+"_Composición de la cartera técnica (Primas directas)Riesgos varios");
			    		
			    		if(valor2 > 0)
			    			total = valor1 / valor2;
			    		
			    		totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
			    	}
			    	else if(nomeCalculo.equals("Responsabilidad civil") && nomeGrupo.equals("Composición de la cartera técnica (Siniestralidad)"))
			    	{
			    		double valor1 = totalMemoria.get(mesAnoCalculo+"_Composición de la cartera técnica (Siniestros pagados bruto)Responsabilidad civil");
			    		double valor2 = totalMemoria.get(mesAnoCalculo+"_Composición de la cartera técnica (Primas directas)Responsabilidad civil");
			    		
			    		if(valor2 > 0)
			    			total = valor1 / valor2;
			    		
			    		totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
			    	}
			    	else if(nomeCalculo.equals("Aeronavegación") && nomeGrupo.equals("Composición de la cartera técnica (Siniestralidad)"))
			    	{
			    		double valor1 = totalMemoria.get(mesAnoCalculo+"_Composición de la cartera técnica (Siniestros pagados bruto)Aeronavegación");
			    		double valor2 = totalMemoria.get(mesAnoCalculo+"_Composición de la cartera técnica (Primas directas)Aeronavegación");
			    		
			    		if(valor2 > 0)
			    			total = valor1 / valor2;
			    		
			    		totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
			    	}
			    	else if(nomeCalculo.equals("Riesgos técnicos") && nomeGrupo.equals("Composición de la cartera técnica (Siniestralidad)"))
			    	{
			    		double valor1 = totalMemoria.get(mesAnoCalculo+"_Composición de la cartera técnica (Siniestros pagados bruto)Riesgos técnicos");
			    		double valor2 = totalMemoria.get(mesAnoCalculo+"_Composición de la cartera técnica (Primas directas)Riesgos técnicos");
			    		
			    		if(valor2 > 0)
			    			total = valor1 / valor2;
			    		
			    		totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
			    	}
			    	else if(nomeCalculo.equals("Caución") && nomeGrupo.equals("Composición de la cartera técnica (Siniestralidad)"))
			    	{
			    		double valor1 = totalMemoria.get(mesAnoCalculo+"_Composición de la cartera técnica (Siniestros pagados bruto)Caución");
			    		double valor2 = totalMemoria.get(mesAnoCalculo+"_Composición de la cartera técnica (Primas directas)Caución");
			    		
			    		if(valor2 > 0)
			    			total = valor1 / valor2;
			    		
			    		totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
			    	}
			    	else if(nomeCalculo.equals("Vida") && nomeGrupo.equals("Composición de la cartera técnica (Siniestralidad)"))
			    	{
			    		totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
			    	}
			    	else if(nomeCalculo.equals("Comisiones de seguros directos"))
			    	{
			    		conta = cContas.get("0504010000");
			    		double valor1 = this.totalConta(conta, c.getTime());
			    		
			    		total = valor1;
			    		
			    		totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
			    	}
			    	else if(nomeCalculo.equals("Tasa media de intermediación"))
			    	{
			    		double valor1 = totalMemoria.get(mesAnoCalculo+"_APrimas de seguros directos");
			    		double valor2 = totalMemoria.get(mesAnoCalculo+"_FComisiones de seguros directos");
			    		
			    		if(valor2 > 0)
			    			total = valor1/valor2;
			    		
			    		totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
			    	}
			    	else if(nomeCalculo.equals("Siniestros pagados netos de recuperos de siniestros"))
			    	{
			    		conta = cContas.get("0506000000");
			    		double valor1 = this.totalConta(conta, c.getTime());
			    		conta = cContas.get("0507000000");
			    		double valor2 = this.totalConta(conta, c.getTime());
			    		conta = cContas.get("0508000000");
			    		double valor3 = this.totalConta(conta, c.getTime());
			    		conta = cContas.get("0407000000");
			    		double valor4 = this.totalConta(conta, c.getTime());
			    		
		    			total = valor1 + valor2 + valor3 + valor4;
			    		
		    			totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
			    	}
			    	else if(nomeCalculo.equals("Gastos de gestión"))
			    	{
			    		conta = cContas.get("0525000000");
			    		double valor1 = this.totalConta(conta, c.getTime());
			    		
		    			total = valor1;
			    		
		    			totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
			    	}
			    	else if(nomeCalculo.equals("Margen de utilidad técnica"))
			    	{
			    		double valor1 = totalMemoria.get(mesAnoCalculo+"_APrimas de seguros directos");
			    		
			    		conta = cContas.get("0504010000");
			    		double valor2 = this.totalConta(conta, c.getTime());
			    		conta = cContas.get("0506000000");
			    		double valor3 = this.totalConta(conta, c.getTime());
			    		conta = cContas.get("0507000000");
			    		double valor4 = this.totalConta(conta, c.getTime());
			    		conta = cContas.get("0508000000");
			    		double valor5 = this.totalConta(conta, c.getTime());
			    		conta = cContas.get("0407000000");
			    		double valor6 = this.totalConta(conta, c.getTime());
			    		conta = cContas.get("0525000000");
			    		double valor7 = this.totalConta(conta, c.getTime());
			    		
		    			total = valor1 - (valor2 + valor3 + valor4 + valor5 + valor6 + valor7);
			    		
		    			totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
			    	}
			    	else if(nomeCalculo.equals("Primas de reaseguros cedidas al exterior"))
			    	{
			    		conta = cContas.get("0502000000");
			    		double valor1 = this.totalConta(conta, c.getTime());
			    		
		    			total = valor1;
			    		
		    			totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
			    	}
			    	else if(nomeCalculo.equals("Patrimonio Neto"))
			    	{
			    		conta = cContas.get("0300000000");
			    		double valor1 = this.totalConta(conta, c.getTime());
			    		
		    			total = valor1;
			    		
		    			totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
			    	}
			    	else if(nomeCalculo.equals("Siniestralidad media rama vida"))
			    	{
			    		conta = cContas.get("0507000000");
			    		double valor1 = this.totalConta(conta, c.getTime());
			    		conta = cContas.get("0506012000");
			    		double valor2 = this.totalConta(conta, c.getTime());
			    		conta = cContas.get("0501012000");
			    		double valor3 = this.totalConta(conta, c.getTime());
			    		double valor4 = totalMemoria.get(mesAnoCalculo+"_APrimas de seguros directos");
			    		
			    		if(valor4 > 0)
			    			total = (valor1 + valor2 + valor3) / valor4;
			    		
			    		totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
			    	}
			    	else if(nomeCalculo.equals("Siniestralidad media rama patrimonial"))
			    	{
			    		conta = cContas.get("0506000000");
			    		double valor1 = this.totalConta(conta, c.getTime());
			    		conta = cContas.get("0508000000");
			    		double valor2 = this.totalConta(conta, c.getTime());
			    		conta = cContas.get("0407000000");
			    		double valor3 = this.totalConta(conta, c.getTime());
			    		conta = cContas.get("0507000000");
			    		double valor4 = this.totalConta(conta, c.getTime());
			    		conta = cContas.get("0506012000");
			    		double valor5 = this.totalConta(conta, c.getTime());
			    		conta = cContas.get("0508012000");
			    		double valor6 = this.totalConta(conta, c.getTime());
			    		double valor7 = totalMemoria.get(mesAnoCalculo+"_APrimas de seguros directos");
			    		
			    		if(valor7 > 0)
			    			total = (valor1 + valor2 - valor3 - valor4 - valor5 - valor6) / valor7;
			    		
			    		totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
			    	}
			    	else if(nomeCalculo.equals("Provisiones riesgo en curso"))
			    	{
			    		conta = cContas.get("0212010000");
			    		double valor1 = this.totalConta(conta, c.getTime());
			    		conta = cContas.get("0212020000");
			    		double valor2 = this.totalConta(conta, c.getTime());
			    		conta = cContas.get("0212030000");
			    		double valor3 = this.totalConta(conta, c.getTime());
			    		conta = cContas.get("0212040000");
			    		double valor4 = this.totalConta(conta, c.getTime());
			    		conta = cContas.get("0109030100");
			    		double valor5 = this.totalConta(conta, c.getTime());
			    		conta = cContas.get("0109040100");
			    		double valor6 = this.totalConta(conta, c.getTime());
			    		conta = cContas.get("0109050100");
			    		double valor7 = this.totalConta(conta, c.getTime());
			    		conta = cContas.get("0109060100");
			    		double valor8 = this.totalConta(conta, c.getTime());
			    		
		    			total = valor1 + valor2 + valor3 + valor4 - valor5 - valor6 - valor7 - valor8;
			    		
		    			totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
			    	}
			    	else if(nomeCalculo.equals("Reservas matemáticas"))
			    	{
			    		conta = cContas.get("0212050100");
			    		double valor1 = this.totalConta(conta, c.getTime());
			    		
		    			total = valor1;
			    		
		    			totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
			    	}
			    	else if(nomeCalculo.equals("Fondos de acumulación"))
			    	{
			    		conta = cContas.get("0212050200");
			    		double valor1 = this.totalConta(conta, c.getTime());
			    		
		    			total = valor1;
			    		
		    			totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
			    	}
			    	else if(nomeCalculo.equals("Provisiones de siniestros"))
			    	{
			    		conta = cContas.get("0213000000");
			    		double valor1 = this.totalConta(conta, c.getTime());
			    		
		    			total = valor1;
			    		
		    			totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
			    	}
			    	else if(nomeCalculo.equals("Renta fija - Sector privado"))
			    	{
			    		conta = cContas.get("0107010000");
			    		double valor1 = this.totalConta(conta, c.getTime());
			    		conta = cContas.get("0107010100");
			    		double valor2 = this.totalConta(conta, c.getTime());
			    		conta = cContas.get("0107020000");
			    		double valor3 = this.totalConta(conta, c.getTime());
			    		conta = cContas.get("0107020100");
			    		double valor4 = this.totalConta(conta, c.getTime());
			    		
			    		//Fazendo o total geral desse grupo
			    		conta = cContas.get("0107030000");
			    		totalGuaranisCorrentes+= this.totalConta(conta, c.getTime());
			    		conta = cContas.get("0107040000");
			    		totalGuaranisCorrentes+= this.totalConta(conta, c.getTime());
			    		conta = cContas.get("0107060000");
			    		totalGuaranisCorrentes+= this.totalConta(conta, c.getTime());
			    		conta = cContas.get("0107010100");
			    		totalGuaranisCorrentes+= this.totalConta(conta, c.getTime());
			    		conta = cContas.get("0107020100");
			    		totalGuaranisCorrentes+= this.totalConta(conta, c.getTime());
			    		conta = cContas.get("0107050000");
			    		totalGuaranisCorrentes+= this.totalConta(conta, c.getTime());
			    		
		    			total = valor1 - valor2 + valor3 - valor4;
		    			
		    			totalGuaranisCorrentes+=total;
		    			
		    			if(totalGuaranisCorrentes > 0)
		    				totalPorcentagem = (total*100)/totalGuaranisCorrentes;
		    			
		    			totalGuaranisCorrentesMap.put(c.getTimeInMillis(), totalGuaranisCorrentes);
			    		
		    			totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
		    			totalMemoria.put(mesAnoCalculo+"_P"+nomeGrupo+nomeCalculo, totalPorcentagem);
		    			
		    			mostraPorcentagem = true;
			    	}
			    	else if(nomeCalculo.equals("Renta variable"))
			    	{
			    		conta = cContas.get("0107030000");
			    		double valor1 = this.totalConta(conta, c.getTime());
			    		conta = cContas.get("0107040000");
			    		double valor2 = this.totalConta(conta, c.getTime());
			    		
			    		total = valor1 + valor2;
			    		
			    		double divisor = totalGuaranisCorrentesMap.get(c.getTimeInMillis());
			    		
			    		if(divisor > 0)
			    			totalPorcentagem = (total * 100) / divisor;
			    		
		    			totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
		    			totalMemoria.put(mesAnoCalculo+"_P"+nomeGrupo+nomeCalculo, totalPorcentagem);
		    			
		    			mostraPorcentagem = true;
			    	}
			    	else if(nomeCalculo.equals("Inversiones inmobiliarias"))
			    	{
			    		conta = cContas.get("0107060000");
			    		double valor1 = this.totalConta(conta, c.getTime());
			    		
			    		total = valor1;
			    		
			    		double divisor = totalGuaranisCorrentesMap.get(c.getTimeInMillis());
			    		
			    		if(divisor > 0)
			    			totalPorcentagem = (valor1 * 100) / divisor;
		    			
		    			totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
		    			totalMemoria.put(mesAnoCalculo+"_P"+nomeGrupo+nomeCalculo, totalPorcentagem);
		    			
		    			mostraPorcentagem = true;
			    	}
			    	else if(nomeCalculo.equals("Titulos públicos"))
			    	{
			    		conta = cContas.get("0107010100");
			    		double valor1 = this.totalConta(conta, c.getTime());
			    		conta = cContas.get("0107020100");
			    		double valor2 = this.totalConta(conta, c.getTime());
			    		
			    		total = valor1 + valor2;
			    		
			    		double divisor = totalGuaranisCorrentesMap.get(c.getTimeInMillis());
			    		
			    		if(divisor > 0)
			    			totalPorcentagem = (total * 100) /divisor;
			    		
		    			totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
		    			totalMemoria.put(mesAnoCalculo+"_P"+nomeGrupo+nomeCalculo, totalPorcentagem);
		    			
		    			mostraPorcentagem = true;
			    	}
			    	else if(nomeCalculo.equals("Préstamos"))
			    	{
			    		conta = cContas.get("0107050000");
			    		double valor1 = this.totalConta(conta, c.getTime());
			    		
			    		total = valor1;
			    		
			    		double divisor = totalGuaranisCorrentesMap.get(c.getTimeInMillis());
			    		
			    		if(divisor > 0)
			    			totalPorcentagem = (valor1 * 100) / divisor;
			    		
		    			totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
		    			totalMemoria.put(mesAnoCalculo+"_P"+nomeGrupo+nomeCalculo, totalPorcentagem);
		    			
		    			mostraPorcentagem = true;
			    	}
			    	else if(nomeCalculo.equals("TOTAL") && nomeGrupo.equals("Componentes de los activos reportados como representativos de las provisiones y reservas técnicas (Guaranies Corrientes)"))
			    	{
			    		double valor1 = totalMemoria.get(mesAnoCalculo+"_"+nomeGrupo+"Renta fija - Sector privado");
			    		double valor2 = totalMemoria.get(mesAnoCalculo+"_"+nomeGrupo+"Renta variable");
			    		double valor3 = totalMemoria.get(mesAnoCalculo+"_"+nomeGrupo+"Inversiones inmobiliarias");
			    		double valor4 = totalMemoria.get(mesAnoCalculo+"_"+nomeGrupo+"Titulos públicos");
			    		double valor5 = totalMemoria.get(mesAnoCalculo+"_"+nomeGrupo+"Préstamos");
			    		
		    			total = valor1 + valor2 + valor3 + valor4 + valor5;
		    			
		    			valor1 = totalMemoria.get(mesAnoCalculo+"_P"+nomeGrupo+"Renta fija - Sector privado");
			    		valor2 = totalMemoria.get(mesAnoCalculo+"_P"+nomeGrupo+"Renta variable");
			    		valor3 = totalMemoria.get(mesAnoCalculo+"_P"+nomeGrupo+"Inversiones inmobiliarias");
			    		valor4 = totalMemoria.get(mesAnoCalculo+"_P"+nomeGrupo+"Titulos públicos");
			    		valor5 = totalMemoria.get(mesAnoCalculo+"_P"+nomeGrupo+"Préstamos");
		    			
		    			totalPorcentagem = valor1 + valor2 + valor3 + valor4 + valor5;
			    		
		    			totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
		    			totalMemoria.put(mesAnoCalculo+"_P"+nomeGrupo+nomeCalculo, totalPorcentagem);
		    			
		    			mostraPorcentagem = true;
			    	}
			    	else if(nomeCalculo.equals("Agentes de Seguros"))
			    	{
			    		double valor1 = home.obterQtdeEntidadesVigentes(agentesDeSeguro, dataInicioValidadeInscricao, dataFimValidadeInscricao);
			    		
		    			total = valor1;
			    		
		    			totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
			    	}
			    	else if(nomeCalculo.equals("Aseguradoras"))
			    	{
			    		double valor1 = home.obterQtdeAseguradorasVigentes(aseguradoras, dataFimValidadeInscricao);
			    		
		    			total = valor1;
			    		
		    			totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
			    	}
			    	else if(nomeCalculo.equals("Reaseguradoras"))
			    	{
			    		double valor1 = home.obterQtdeEntidadesVigentes(reaseguradoras, dataInicioValidadeInscricao, dataFimValidadeInscricao);
			    		
		    			total = valor1;
			    		
		    			totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
			    	}
			    	else if(nomeCalculo.equals("Intermediarios de seguros"))
			    	{
			    		double valor1 = home.obterQtdeEntidadesVigentes(corredoresSeguros, dataInicioValidadeInscricao, dataFimValidadeInscricao);
			    		
		    			total = valor1;
			    		
		    			totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
			    	}
			    	else if(nomeCalculo.equals("Intermediario de reaseguros"))
			    	{
			    		double valor1 = home.obterQtdeEntidadesVigentes(corredoresReaseguros, dataInicioValidadeInscricao, dataFimValidadeInscricao);
			    		
		    			total = valor1;
			    		
		    			totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
			    	}
			    	else if(nomeCalculo.equals("Empresas de auditoria externa"))
			    	{
			    		double valor1 = home.obterQtdeEntidadesVigentes(auditoresExternos, dataInicioValidadeInscricao, dataFimValidadeInscricao);
			    		
		    			total = valor1;
			    		
		    			totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
			    	}
			    	else if(nomeCalculo.equals("Grupos coaseguradoras"))
			    	{
			    		double valor1 = home.obterQtdeAseguradorasVigentes(coaseguradoras, dataFimValidadeInscricao);
			    		
		    			total = valor1;
			    		
		    			totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
			    	}
			    	else if(nomeCalculo.equals("TOTAL") && nomeGrupo.equals("K"))
			    	{
			    		double valor1 = totalMemoria.get(mesAnoCalculo+"_KAgentes de Seguros");
			    		double valor2 = totalMemoria.get(mesAnoCalculo+"_KAseguradoras");
			    		double valor3 = totalMemoria.get(mesAnoCalculo+"_KReaseguradoras");
			    		double valor4 = totalMemoria.get(mesAnoCalculo+"_KIntermediarios de seguros");
			    		double valor5 = totalMemoria.get(mesAnoCalculo+"_KIntermediario de reaseguros");
			    		double valor6 = totalMemoria.get(mesAnoCalculo+"_KEmpresas de auditoria externa");
			    		double valor7 = totalMemoria.get(mesAnoCalculo+"_KGrupos coaseguradoras");
			    		
		    			total = valor1 + valor2 + valor3 + valor4 + valor5 + valor6 + valor7;
			    		
		    			totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
			    	}
			    	else if(nomeCalculo.equals("Prima seccion vida individual corto plazo"))
			    	{
			    		conta2 = contas.get("0401012001");
			    		double valor1 = this.totalConta(conta2, c.getTime());
			    		
		    			total = valor1;
			    		
		    			totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
			    	}
			    	else if(nomeCalculo.equals("Prima seccion vida seguros colectivos"))
			    	{
			    		conta2 = contas.get("0401012003");
			    		double valor1 = this.totalConta(conta2, c.getTime());
			    		
		    			total = valor1;
			    		
		    			totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
			    	}
			    	else if(nomeCalculo.equals("Prima seccion vida desgravamiento hipotecario"))
			    	{
			    		conta2 = contas.get("0401012006");
			    		double valor1 = this.totalConta(conta2, c.getTime());
			    		
		    			total = valor1;
			    		
		    			totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
			    	}
			    	else if(nomeCalculo.equals("Prima seccion vida defuncion o sepelio"))
			    	{
			    		conta2 = contas.get("0401012009");
			    		double valor1 = this.totalConta(conta2, c.getTime());
			    		
		    			total = valor1;
			    		
		    			totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
			    	}
			    	else if(nomeCalculo.equals("Primas seccion vida individual -  largo plazo"))
			    	{
			    		conta2 = contas.get("0401012002");
			    		double valor1 = this.totalConta(conta2, c.getTime());
			    		
		    			total = valor1;
			    		
		    			totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
			    	}
			    	else if(nomeCalculo.equals("Primas seccion seguros colectivos - largo plazo") && nomeGrupo.equals("Primas de seguros rama vida - con reservas matematicas (seguros directos)"))
			    	{
			    		conta2 = contas.get("0401012004");
			    		double valor1 = this.totalConta(conta2, c.getTime());
			    		
		    			total = valor1;
			    		
		    			totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
			    	}
			    	else if(nomeCalculo.equals("Primas seccion pensiones voluntarias") && nomeGrupo.equals("Primas de seguros rama vida - con reservas matematicas (seguros directos)"))
			    	{
			    		conta2 = contas.get("0401012005");
			    		double valor1 = this.totalConta(conta2, c.getTime());
			    		
		    			total = valor1;
			    		
		    			totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
			    	}
			    	else if(nomeCalculo.equals("Primas seccion desgravamen hipotecario - largo plazo"))
			    	{
			    		conta2 = contas.get("0401012007");
			    		double valor1 = this.totalConta(conta2, c.getTime());
			    		
		    			total = valor1;
			    		
		    			totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
			    	}
			    	
			    	else if(nomeCalculo.equals("Incendios") && nomeGrupo.equals("Primas de seguros rama patrimonial (seguros directos)"))
			    	{
			    		double valor1 = totalMemoria.get(mesAnoCalculo+"_Composición de la cartera técnica (Primas directas)Incendios");
			    		
			    		//Somar o total geral antes pro calculo
			    		double valor2 = totalMemoria.get(mesAnoCalculo+"_Composición de la cartera técnica (Primas directas)Transportes");
			    		double valor3 = totalMemoria.get(mesAnoCalculo+"_Composición de la cartera técnica (Primas directas)Accidentes personales");
			    		double valor4 = totalMemoria.get(mesAnoCalculo+"_Composición de la cartera técnica (Primas directas)Automóviles");
			    		double valor5 = totalMemoria.get(mesAnoCalculo+"_Composición de la cartera técnica (Primas directas)Accidentes a pasajeros");
			    		double valor6 = totalMemoria.get(mesAnoCalculo+"_Composición de la cartera técnica (Primas directas)Robo y asalto");
			    		double valor7 = totalMemoria.get(mesAnoCalculo+"_Composición de la cartera técnica (Primas directas)Cristales, vidrios y espejos");
			    		double valor8 = totalMemoria.get(mesAnoCalculo+"_Composición de la cartera técnica (Primas directas)Agropecuario");
			    		double valor9 = totalMemoria.get(mesAnoCalculo+"_Composición de la cartera técnica (Primas directas)Riesgos varios");
			    		double valor10 = totalMemoria.get(mesAnoCalculo+"_Composición de la cartera técnica (Primas directas)Responsabilidad civil");
			    		double valor11 = totalMemoria.get(mesAnoCalculo+"_Composición de la cartera técnica (Primas directas)Aeronavegación");
			    		double valor12 = totalMemoria.get(mesAnoCalculo+"_Composición de la cartera técnica (Primas directas)Riesgos técnicos");
			    		double valor13 = totalMemoria.get(mesAnoCalculo+"_Composición de la cartera técnica (Primas directas)Caución");
			    		conta = cContas.get("0401020000");
			    		double valor14 = this.totalConta(conta, c.getTime());
			    		conta2 = contas.get("0401012008");
			    		double valor15 = this.totalConta(conta2, c.getTime());
			    		conta2 = contas.get("0401022005");
			    		double valor16 = this.totalConta(conta2, c.getTime());
			    		
			    		total = valor1;
			    		
			    		totalPrimaRamaPratimonial = valor1 + valor2 + valor3 + valor4 + valor5 + valor6 + valor7 + valor8 + valor9 + valor10 + valor11 + valor12 + valor13 + valor14 + valor15 + valor16;
			    		
			    		totalPrimaRamaPratimonialMap.put(c.getTimeInMillis(), totalPrimaRamaPratimonial);
		    			
			    		if(totalPrimaRamaPratimonial > 0)
			    			totalPorcentagem = (valor1 *100) / totalPrimaRamaPratimonial;
			    		
		    			totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
		    			totalMemoria.put(mesAnoCalculo+"_P"+nomeGrupo+nomeCalculo, totalPorcentagem);
		    			
		    			mostraPorcentagem = true;
			    	}
			    	else if(nomeCalculo.equals("Transportes") && nomeGrupo.equals("Primas de seguros rama patrimonial (seguros directos)"))
			    	{
			    		double valor1 = totalMemoria.get(mesAnoCalculo+"_Composición de la cartera técnica (Primas directas)Transportes");
			    		
			    		total = valor1;
			    		
			    		double divisor = totalPrimaRamaPratimonialMap.get(c.getTimeInMillis());
			    		
			    		if(divisor > 0)
			    			totalPorcentagem = (valor1 * 100) / divisor;
			    		
		    			totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
		    			totalMemoria.put(mesAnoCalculo+"_P"+nomeGrupo+nomeCalculo, totalPorcentagem);
		    			
		    			mostraPorcentagem = true;
			    	}
			    	else if(nomeCalculo.equals("Accidentes personales") && nomeGrupo.equals("Primas de seguros rama patrimonial (seguros directos)"))
			    	{
			    		double valor1 = totalMemoria.get(mesAnoCalculo+"_Composición de la cartera técnica (Primas directas)Accidentes personales");
			    		
			    		total = valor1;
			    		
			    		double divisor = totalPrimaRamaPratimonialMap.get(c.getTimeInMillis());
			    		
			    		if(divisor > 0)
			    			totalPorcentagem = (valor1 * 100) / divisor;
			    		
		    			totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
		    			totalMemoria.put(mesAnoCalculo+"_P"+nomeGrupo+nomeCalculo, totalPorcentagem);
		    			
		    			mostraPorcentagem = true;
			    	}
			    	else if(nomeCalculo.equals("Automóviles") && nomeGrupo.equals("Primas de seguros rama patrimonial (seguros directos)"))
			    	{
			    		double valor1 = totalMemoria.get(mesAnoCalculo+"_Composición de la cartera técnica (Primas directas)Automóviles");
			    		
			    		total = valor1;
			    		
			    		double divisor = totalPrimaRamaPratimonialMap.get(c.getTimeInMillis());
			    		
			    		if(divisor > 0)
			    			totalPorcentagem = (valor1 * 100) / divisor;
			    		
		    			totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
		    			totalMemoria.put(mesAnoCalculo+"_P"+nomeGrupo+nomeCalculo, totalPorcentagem);
		    			
		    			mostraPorcentagem = true;
			    	}
			    	else if(nomeCalculo.equals("Accidentes a pasajeros") && nomeGrupo.equals("Primas de seguros rama patrimonial (seguros directos)"))
			    	{
			    		double valor1 = totalMemoria.get(mesAnoCalculo+"_Composición de la cartera técnica (Primas directas)Accidentes a pasajeros");
			    		
			    		total = valor1;
			    		
			    		double divisor = totalPrimaRamaPratimonialMap.get(c.getTimeInMillis());
			    		
			    		if(divisor > 0)
			    			totalPorcentagem = (valor1 * 100) / divisor;
			    		
		    			totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
		    			totalMemoria.put(mesAnoCalculo+"_P"+nomeGrupo+nomeCalculo, totalPorcentagem);
		    			
		    			mostraPorcentagem = true;
			    	}
			    	else if(nomeCalculo.equals("Robo y asalto") && nomeGrupo.equals("Primas de seguros rama patrimonial (seguros directos)"))
			    	{
			    		double valor1 = totalMemoria.get(mesAnoCalculo+"_Composición de la cartera técnica (Primas directas)Robo y asalto");
			    		
			    		total = valor1;
			    		
			    		double divisor = totalPrimaRamaPratimonialMap.get(c.getTimeInMillis());
			    		
			    		if(divisor > 0)
			    			totalPorcentagem = (valor1 * 100) / divisor;
			    		
		    			totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
		    			totalMemoria.put(mesAnoCalculo+"_P"+nomeGrupo+nomeCalculo, totalPorcentagem);
		    			
		    			mostraPorcentagem = true;
			    	}
			    	else if(nomeCalculo.equals("Cristales, vidrios y espejos") && nomeGrupo.equals("Primas de seguros rama patrimonial (seguros directos)"))
			    	{
			    		double valor1 = totalMemoria.get(mesAnoCalculo+"_Composición de la cartera técnica (Primas directas)Cristales, vidrios y espejos");
			    		
			    		total = valor1;
			    		
			    		double divisor = totalPrimaRamaPratimonialMap.get(c.getTimeInMillis());
			    		
			    		if(divisor > 0)
			    			totalPorcentagem = (valor1 * 100) / divisor;
			    		
		    			totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
		    			totalMemoria.put(mesAnoCalculo+"_P"+nomeGrupo+nomeCalculo, totalPorcentagem);
		    			
		    			mostraPorcentagem = true;
			    	}
			    	else if(nomeCalculo.equals("Agropecuario") && nomeGrupo.equals("Primas de seguros rama patrimonial (seguros directos)"))
			    	{
			    		double valor1 = totalMemoria.get(mesAnoCalculo+"_Composición de la cartera técnica (Primas directas)Agropecuario");
			    		
			    		total = valor1;
			    		
			    		double divisor = totalPrimaRamaPratimonialMap.get(c.getTimeInMillis());
			    		
			    		if(divisor > 0)
			    			totalPorcentagem = (valor1 * 100) / divisor;
			    		
		    			totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
		    			totalMemoria.put(mesAnoCalculo+"_P"+nomeGrupo+nomeCalculo, totalPorcentagem);
		    			
		    			mostraPorcentagem = true;
			    	}
			    	else if(nomeCalculo.equals("Riesgos varios") && nomeGrupo.equals("Primas de seguros rama patrimonial (seguros directos)"))
			    	{
			    		double valor1 = totalMemoria.get(mesAnoCalculo+"_Composición de la cartera técnica (Primas directas)Riesgos varios");
			    		
			    		total = valor1;
			    		
			    		double divisor = totalPrimaRamaPratimonialMap.get(c.getTimeInMillis());
			    		
			    		if(divisor > 0)
			    			totalPorcentagem = (valor1 * 100) / divisor;
			    		
		    			totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
		    			totalMemoria.put(mesAnoCalculo+"_P"+nomeGrupo+nomeCalculo, totalPorcentagem);
		    			
		    			mostraPorcentagem = true;
			    	}
			    	else if(nomeCalculo.equals("Responsabilidad civil") && nomeGrupo.equals("Primas de seguros rama patrimonial (seguros directos)"))
			    	{
			    		double valor1 = totalMemoria.get(mesAnoCalculo+"_Composición de la cartera técnica (Primas directas)Responsabilidad civil");
			    		
			    		total = valor1;
			    		
			    		double divisor = totalPrimaRamaPratimonialMap.get(c.getTimeInMillis());
			    		
			    		if(divisor > 0)
			    			totalPorcentagem = (valor1 * 100) / divisor;
			    		
		    			totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
		    			totalMemoria.put(mesAnoCalculo+"_P"+nomeGrupo+nomeCalculo, totalPorcentagem);
		    			
		    			mostraPorcentagem = true;
			    	}
			    	else if(nomeCalculo.equals("Aeronavegación") && nomeGrupo.equals("Primas de seguros rama patrimonial (seguros directos)"))
			    	{
			    		double valor1 = totalMemoria.get(mesAnoCalculo+"_Composición de la cartera técnica (Primas directas)Aeronavegación");
			    		
			    		total = valor1;
			    		
			    		double divisor = totalPrimaRamaPratimonialMap.get(c.getTimeInMillis());
			    		
			    		if(divisor > 0)
			    			totalPorcentagem = (valor1 * 100) / divisor;
			    		
		    			totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
		    			totalMemoria.put(mesAnoCalculo+"_P"+nomeGrupo+nomeCalculo, totalPorcentagem);
		    			
		    			mostraPorcentagem = true;
			    	}
			    	else if(nomeCalculo.equals("Riesgos técnicos") && nomeGrupo.equals("Primas de seguros rama patrimonial (seguros directos)"))
			    	{
			    		double valor1 = totalMemoria.get(mesAnoCalculo+"_Composición de la cartera técnica (Primas directas)Riesgos técnicos");
			    		
			    		total = valor1;
			    		
			    		double divisor = totalPrimaRamaPratimonialMap.get(c.getTimeInMillis());
			    		
			    		if(divisor > 0)
			    			totalPorcentagem = (valor1 * 100) / divisor;
			    		
		    			totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
		    			totalMemoria.put(mesAnoCalculo+"_P"+nomeGrupo+nomeCalculo, totalPorcentagem);
		    			
		    			mostraPorcentagem = true;
			    	}
			    	else if(nomeCalculo.equals("Caución") && nomeGrupo.equals("Primas de seguros rama patrimonial (seguros directos)"))
			    	{
			    		double valor1 = totalMemoria.get(mesAnoCalculo+"_Composición de la cartera técnica (Primas directas)Caución");
			    		
			    		total = valor1;
			    		
			    		double divisor = totalPrimaRamaPratimonialMap.get(c.getTimeInMillis());
			    		
			    		if(divisor > 0)
			    			totalPorcentagem = (valor1 * 100) / divisor;
			    		
		    			totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
		    			totalMemoria.put(mesAnoCalculo+"_P"+nomeGrupo+nomeCalculo, totalPorcentagem);
		    			
		    			mostraPorcentagem = true;
			    	}
			    	else if(nomeCalculo.equals("Primas directas renovadas"))
			    	{
			    		conta = cContas.get("0401020000");
			    		double valor1 = this.totalConta(conta, c.getTime());
			    		
			    		total = valor1;
			    		
			    		double divisor = totalPrimaRamaPratimonialMap.get(c.getTimeInMillis());
			    		
			    		if(divisor > 0)
			    			totalPorcentagem = (valor1 * 100) / divisor;
			    		
		    			totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
		    			totalMemoria.put(mesAnoCalculo+"_P"+nomeGrupo+nomeCalculo, totalPorcentagem);
		    			
		    			mostraPorcentagem = true;
			    	}
			    	else if(nomeCalculo.equals("Primas seccion salud o enfermedad"))
			    	{
			    		conta2 = contas.get("0401012008");
			    		double valor1 = this.totalConta(conta2, c.getTime());
			    		
			    		total = valor1;
			    		
			    		double divisor = totalPrimaRamaPratimonialMap.get(c.getTimeInMillis());
			    		
			    		if(divisor > 0)
			    			totalPorcentagem = (valor1 * 100) / divisor;
			    		
		    			totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
		    			totalMemoria.put(mesAnoCalculo+"_P"+nomeGrupo+nomeCalculo, totalPorcentagem);
		    			
		    			mostraPorcentagem = true;
			    		
		    			totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
			    	}
			    	else if(nomeCalculo.equals("Primas renovadadas salud o enfermedad largo plazo"))
			    	{
			    		conta2 = contas.get("0401022005");
			    		double valor1 = this.totalConta(conta2, c.getTime());
			    		
			    		total = valor1;
			    		
			    		double divisor = totalPrimaRamaPratimonialMap.get(c.getTimeInMillis());
			    		
			    		if(divisor > 0)
			    			totalPorcentagem = (valor1 * 100) / divisor;
			    		
		    			totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
		    			totalMemoria.put(mesAnoCalculo+"_P"+nomeGrupo+nomeCalculo, totalPorcentagem);
		    			
		    			mostraPorcentagem = true;
			    	}
			    	else if(nomeCalculo.equals("TOTAL") && nomeGrupo.equals("Primas de seguros rama patrimonial (seguros directos)"))
			    	{
			    		double valor1 = totalMemoria.get(mesAnoCalculo+"_"+nomeGrupo+"Incendios");
			    		double valor2 = totalMemoria.get(mesAnoCalculo+"_"+nomeGrupo+"Transportes");
			    		double valor3 = totalMemoria.get(mesAnoCalculo+"_"+nomeGrupo+"Accidentes personales");
			    		double valor4 = totalMemoria.get(mesAnoCalculo+"_"+nomeGrupo+"Automóviles");
			    		double valor5 = totalMemoria.get(mesAnoCalculo+"_"+nomeGrupo+"Accidentes a pasajeros");
			    		double valor6 = totalMemoria.get(mesAnoCalculo+"_"+nomeGrupo+"Robo y asalto");
			    		double valor7 = totalMemoria.get(mesAnoCalculo+"_"+nomeGrupo+"Cristales, vidrios y espejos");
			    		double valor8 = totalMemoria.get(mesAnoCalculo+"_"+nomeGrupo+"Agropecuario");
			    		double valor9 = totalMemoria.get(mesAnoCalculo+"_"+nomeGrupo+"Riesgos varios");
			    		double valor10 = totalMemoria.get(mesAnoCalculo+"_"+nomeGrupo+"Responsabilidad civil");
			    		double valor11 = totalMemoria.get(mesAnoCalculo+"_"+nomeGrupo+"Aeronavegación");
			    		double valor12 = totalMemoria.get(mesAnoCalculo+"_"+nomeGrupo+"Riesgos técnicos");
			    		double valor13 = totalMemoria.get(mesAnoCalculo+"_"+nomeGrupo+"Caución");
			    		double valor14 = totalMemoria.get(mesAnoCalculo+"_"+nomeGrupo+"Primas directas renovadas");
			    		double valor15 = totalMemoria.get(mesAnoCalculo+"_"+nomeGrupo+"Primas seccion salud o enfermedad");
			    		double valor16 = totalMemoria.get(mesAnoCalculo+"_"+nomeGrupo+"Primas renovadadas salud o enfermedad largo plazo");
			    		
		    			total = valor1 + valor2 + valor3 + valor4 + valor5 + valor6 + valor7 + valor8 + valor9 + valor10 + valor11 + valor12 + valor13 + valor14 + valor15 + valor16;
		    			
		    			valor1 = totalMemoria.get(mesAnoCalculo+"_P"+nomeGrupo+"Incendios");
			    		valor2 = totalMemoria.get(mesAnoCalculo+"_P"+nomeGrupo+"Transportes");
			    		valor3 = totalMemoria.get(mesAnoCalculo+"_P"+nomeGrupo+"Accidentes personales");
			    		valor4 = totalMemoria.get(mesAnoCalculo+"_P"+nomeGrupo+"Automóviles");
			    		valor5 = totalMemoria.get(mesAnoCalculo+"_P"+nomeGrupo+"Accidentes a pasajeros");
			    		valor6 = totalMemoria.get(mesAnoCalculo+"_P"+nomeGrupo+"Robo y asalto");
			    		valor7 = totalMemoria.get(mesAnoCalculo+"_P"+nomeGrupo+"Cristales, vidrios y espejos");
			    		valor8 = totalMemoria.get(mesAnoCalculo+"_P"+nomeGrupo+"Agropecuario");
			    		valor9 = totalMemoria.get(mesAnoCalculo+"_P"+nomeGrupo+"Riesgos varios");
			    		valor10 = totalMemoria.get(mesAnoCalculo+"_P"+nomeGrupo+"Responsabilidad civil");
			    		valor11 = totalMemoria.get(mesAnoCalculo+"_P"+nomeGrupo+"Aeronavegación");
			    		valor12 = totalMemoria.get(mesAnoCalculo+"_P"+nomeGrupo+"Riesgos técnicos");
			    		valor13 = totalMemoria.get(mesAnoCalculo+"_P"+nomeGrupo+"Caución");
			    		valor14 = totalMemoria.get(mesAnoCalculo+"_P"+nomeGrupo+"Primas directas renovadas");
			    		valor15 = totalMemoria.get(mesAnoCalculo+"_P"+nomeGrupo+"Primas seccion salud o enfermedad");
			    		valor16 = totalMemoria.get(mesAnoCalculo+"_P"+nomeGrupo+"Primas renovadadas salud o enfermedad largo plazo");
			    		
		    			totalPorcentagem = valor1 + valor2 + valor3 + valor4 + valor5 + valor6 + valor7 + valor8 + valor9 + valor10 + valor11 + valor12 + valor13 + valor14 + valor15 + valor16;
			    		
		    			totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
		    			totalMemoria.put(mesAnoCalculo+"_p"+nomeGrupo+nomeCalculo, totalPorcentagem);
			    	}
			    	else if(nomeCalculo.equals("Primas reaseguros aceptados  - local"))
			    	{
			    		double valor1 = totalMemoria.get(mesAnoCalculo+"_APrimas reaseguros aceptados - Local");
		    			total = valor1;
			    		
		    			totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
			    	}
			    	else if(nomeCalculo.equals("Primas reaseguros aceptados - exterior"))
			    	{
			    		double valor1 = totalMemoria.get(mesAnoCalculo+"_APrimas reaseguros aceptados - Exterior");
		    			total = valor1;
			    		
		    			totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
			    	}
			    	else if(nomeCalculo.equals("Primas reaseguros aceptados vida - local"))
			    	{
			    		totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
			    	}
			    	else if(nomeCalculo.equals("Primas reaseguros aceptados patrimonial - local"))
			    	{
			    		totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
			    	}
			    	else if(nomeCalculo.equals("Primas reaseguros aceptados vida - exterior"))
			    	{
			    		totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
			    	}
			    	else if(nomeCalculo.equals("Primas reaseguros aceptados patrimonial - exterior"))
			    	{
			    		totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
			    	}
			    	else if(nomeCalculo.equals("Primas directas automoviles"))
			    	{
			    		double valor1 = totalMemoria.get(mesAnoCalculo+"_Composición de la cartera técnica (Primas directas)Automóviles");
			    		
			    		//Somar o total antes
			    		double valor2 = totalMemoria.get(mesAnoCalculo+"_Primas de seguros rama vida - sin reservas matematicas (seguros directos)Prima seccion vida individual corto plazo");
			    		double valor3 = totalMemoria.get(mesAnoCalculo+"_Primas de seguros rama vida - sin reservas matematicas (seguros directos)Prima seccion vida seguros colectivos");
			    		double valor4 = totalMemoria.get(mesAnoCalculo+"_Primas de seguros rama vida - sin reservas matematicas (seguros directos)Prima seccion vida desgravamiento hipotecario");
			    		double valor5 = totalMemoria.get(mesAnoCalculo+"_Primas de seguros rama vida - sin reservas matematicas (seguros directos)Prima seccion vida defuncion o sepelio");
			    		double valor6 = totalMemoria.get(mesAnoCalculo+"_Composición de la cartera técnica (Primas directas)Incendios");
			    		double valor7 = totalMemoria.get(mesAnoCalculo+"_Composición de la cartera técnica (Primas directas)Agropecuario");
			    		double valor8 = totalMemoria.get(mesAnoCalculo+"_Composición de la cartera técnica (Primas directas)Cristales, vidrios y espejos");
			    		double valor9 = totalMemoria.get(mesAnoCalculo+"_Composición de la cartera técnica (Primas directas)Riesgos varios");
			    		double valor10 = totalMemoria.get(mesAnoCalculo+"_Composición de la cartera técnica (Primas directas)Riesgos técnicos");
			    		double valor11 = totalMemoria.get(mesAnoCalculo+"_Composición de la cartera técnica (Primas directas)Transportes");
			    		double valor12 = totalMemoria.get(mesAnoCalculo+"_Composición de la cartera técnica (Primas directas)Caución");
			    		double valor13 = totalMemoria.get(mesAnoCalculo+"_Composición de la cartera técnica (Primas directas)Robo y asalto");
			    		double valor14 = totalMemoria.get(mesAnoCalculo+"_Composición de la cartera técnica (Primas directas)Responsabilidad civil");
			    		double valor15 = totalMemoria.get(mesAnoCalculo+"_Composición de la cartera técnica (Primas directas)Accidentes personales");
			    		double valor16 = totalMemoria.get(mesAnoCalculo+"_Composición de la cartera técnica (Primas directas)Accidentes a pasajeros");
			    		double valor17 = totalMemoria.get(mesAnoCalculo+"_Composición de la cartera técnica (Primas directas)Aeronavegación");
			    		double valor18 = totalMemoria.get(mesAnoCalculo+"_Primas de seguros rama vida - con reservas matematicas (seguros directos)Primas seccion vida individual -  largo plazo");
			    		double valor19 = totalMemoria.get(mesAnoCalculo+"_Primas de seguros rama vida - con reservas matematicas (seguros directos)Primas seccion seguros colectivos - largo plazo");
			    		double valor20 = totalMemoria.get(mesAnoCalculo+"_Primas de seguros rama vida - con reservas matematicas (seguros directos)Primas seccion pensiones voluntarias");
			    		double valor21 = totalMemoria.get(mesAnoCalculo+"_Primas de seguros rama vida - con reservas matematicas (seguros directos)Primas seccion desgravamen hipotecario - largo plazo");
			    		double valor22 = totalMemoria.get(mesAnoCalculo+"_Primas de seguros rama patrimonial (seguros directos)Primas seccion salud o enfermedad");
			    		
			    		total = valor1;
			    		
			    		totalCarteiraTecnica = valor1 + valor2 + valor3 + valor4 + valor5 + valor6 + valor7 + valor8 + valor9 + valor10 + valor11 + valor12 + valor13 + valor14 + valor15 + valor16 + valor17 + valor18 + valor19 + valor20 + valor21 + valor22;
			    		
			    		totalCarteiraTecnicalMap.put(c.getTimeInMillis(), totalCarteiraTecnica);
			    		
			    		if(totalCarteiraTecnica > 0)
			    			totalPorcentagem = (valor1 * 100) / totalCarteiraTecnica;
			    		
			    		totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
			    		totalMemoria.put(mesAnoCalculo+"_P"+nomeGrupo+nomeCalculo, totalPorcentagem);
			    		
			    		mostraPorcentagem = true;
			    	}
			    	else if(nomeCalculo.equals("Primas seccion vida individual - corto plazo"))
			    	{
			    		double valor1 = totalMemoria.get(mesAnoCalculo+"_Primas de seguros rama vida - sin reservas matematicas (seguros directos)Prima seccion vida individual corto plazo");
			    		
			    		total = valor1;
			    		
			    		double divisor = totalCarteiraTecnicalMap.get(c.getTimeInMillis());
			    		
			    		if(divisor > 0)
			    			totalPorcentagem = (valor1 * 100) / divisor;
			    		
			    		totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
			    		totalMemoria.put(mesAnoCalculo+"_P"+nomeGrupo+nomeCalculo, totalPorcentagem);
			    		
			    		mostraPorcentagem = true;
			    	}
			    	else if(nomeCalculo.equals("Primas seccion vida colectiva - corto plazo"))
			    	{
			    		double valor1 = totalMemoria.get(mesAnoCalculo+"_Primas de seguros rama vida - sin reservas matematicas (seguros directos)Prima seccion vida seguros colectivos");
			    		
			    		total = valor1;
			    		
			    		double divisor = totalCarteiraTecnicalMap.get(c.getTimeInMillis());
			    		
			    		if(divisor > 0)
			    			totalPorcentagem = (valor1 * 100) / divisor;
			    		
			    		totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
			    		totalMemoria.put(mesAnoCalculo+"_P"+nomeGrupo+nomeCalculo, totalPorcentagem);
			    		
			    		mostraPorcentagem = true;
			    	}
			    	else if(nomeCalculo.equals("Primas seccion desgravamen hipotecario - corto plazo"))
			    	{
			    		double valor1 = totalMemoria.get(mesAnoCalculo+"_Primas de seguros rama vida - sin reservas matematicas (seguros directos)Prima seccion vida desgravamiento hipotecario");
			    		
			    		total = valor1;
			    		
			    		double divisor = totalCarteiraTecnicalMap.get(c.getTimeInMillis());
			    		
			    		if(divisor > 0)
			    			totalPorcentagem = (valor1 * 100) / divisor;
			    		
			    		totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
			    		totalMemoria.put(mesAnoCalculo+"_P"+nomeGrupo+nomeCalculo, totalPorcentagem);
			    		
			    		mostraPorcentagem = true;
			    	}
			    	else if(nomeCalculo.equals("Primas seccion defuncion o sepelio"))
			    	{
			    		double valor1 = totalMemoria.get(mesAnoCalculo+"_Primas de seguros rama vida - sin reservas matematicas (seguros directos)Prima seccion vida defuncion o sepelio");

			    		total = valor1;
			    		
			    		double divisor = totalCarteiraTecnicalMap.get(c.getTimeInMillis());
			    		
			    		if(divisor > 0)
			    			totalPorcentagem = (valor1 * 100) / divisor;
			    		
			    		totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
			    		totalMemoria.put(mesAnoCalculo+"_P"+nomeGrupo+nomeCalculo, totalPorcentagem);
			    		
			    		mostraPorcentagem = true;
			    	}
			    	else if(nomeCalculo.equals("Incendios") && nomeGrupo.equals("Composición de la cartera técnica (prima de seguro directo)"))
			    	{
			    		double valor1 = totalMemoria.get(mesAnoCalculo+"_Composición de la cartera técnica (Primas directas)Incendios");
			    		
			    		total = valor1;
			    		
			    		double divisor = totalCarteiraTecnicalMap.get(c.getTimeInMillis());
			    		
			    		if(divisor > 0)
			    			totalPorcentagem = (valor1 * 100) / divisor;
			    		
		    			totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
		    			totalMemoria.put(mesAnoCalculo+"_P"+nomeGrupo+nomeCalculo, totalPorcentagem);
		    			
		    			mostraPorcentagem = true;
			    	}
			    	else if(nomeCalculo.equals("Seguros agrarios") && nomeGrupo.equals("Composición de la cartera técnica (prima de seguro directo)"))
			    	{
			    		double valor1 = totalMemoria.get(mesAnoCalculo+"_Composición de la cartera técnica (Primas directas)Agropecuario");
			    		
			    		total = valor1;
			    		
			    		double divisor = totalCarteiraTecnicalMap.get(c.getTimeInMillis());
			    		
			    		if(divisor > 0)
			    			totalPorcentagem = (valor1 * 100) / divisor;
			    		
			    		totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
			    		totalMemoria.put(mesAnoCalculo+"_P"+nomeGrupo+nomeCalculo, totalPorcentagem);
			    		
			    		mostraPorcentagem = true;
			    	}
			    	else if(nomeCalculo.equals("Riesgos varios -cristales vidrios y espejos") && nomeGrupo.equals("Composición de la cartera técnica (prima de seguro directo)"))
			    	{
			    		double valor1 = totalMemoria.get(mesAnoCalculo+"_Composición de la cartera técnica (Primas directas)Cristales, vidrios y espejos");
			    		
			    		total = valor1;
			    		
			    		double divisor = totalCarteiraTecnicalMap.get(c.getTimeInMillis());
			    		
			    		if(divisor > 0)
			    			totalPorcentagem = (valor1 * 100) / divisor;
			    		
			    		totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
			    		totalMemoria.put(mesAnoCalculo+"_P"+nomeGrupo+nomeCalculo, totalPorcentagem);
			    		
			    		mostraPorcentagem = true;
			    	}
			    	else if(nomeCalculo.equals("Riesgos varios - riesgos varios") && nomeGrupo.equals("Composición de la cartera técnica (prima de seguro directo)"))
			    	{
			    		double valor1 = totalMemoria.get(mesAnoCalculo+"_Composición de la cartera técnica (Primas directas)Riesgos varios");
			    		
			    		total = valor1;
			    		
			    		double divisor = totalCarteiraTecnicalMap.get(c.getTimeInMillis());
			    		
			    		if(divisor > 0)
			    			totalPorcentagem = (valor1 * 100) / divisor;
			    		
			    		totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
			    		totalMemoria.put(mesAnoCalculo+"_P"+nomeGrupo+nomeCalculo, totalPorcentagem);
			    		
			    		mostraPorcentagem = true;
			    	}
			    	else if(nomeCalculo.equals("Riesgos tecnicos") && nomeGrupo.equals("Composición de la cartera técnica (prima de seguro directo)"))
			    	{
			    		double valor1 = totalMemoria.get(mesAnoCalculo+"_Composición de la cartera técnica (Primas directas)Riesgos técnicos");
			    		
			    		total = valor1;
			    		
			    		double divisor = totalCarteiraTecnicalMap.get(c.getTimeInMillis());
			    		
			    		if(divisor > 0)
			    			totalPorcentagem = (valor1 * 100) / divisor;
			    		
			    		totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
			    		totalMemoria.put(mesAnoCalculo+"_P"+nomeGrupo+nomeCalculo, totalPorcentagem);
			    		
			    		mostraPorcentagem = true;
			    	}
			    	else if(nomeCalculo.equals("Transportes") && nomeGrupo.equals("Composición de la cartera técnica (prima de seguro directo)"))
			    	{
			    		double valor1 = totalMemoria.get(mesAnoCalculo+"_Composición de la cartera técnica (Primas directas)Transportes");
			    		
			    		total = valor1;
			    		
			    		double divisor = totalCarteiraTecnicalMap.get(c.getTimeInMillis());
			    		
			    		if(divisor > 0)
			    			totalPorcentagem = (valor1 * 100) / divisor;
			    		
			    		totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
			    		totalMemoria.put(mesAnoCalculo+"_P"+nomeGrupo+nomeCalculo, totalPorcentagem);
			    		
			    		mostraPorcentagem = true;
			    	}
			    	else if(nomeCalculo.equals("Caucion") && nomeGrupo.equals("Composición de la cartera técnica (prima de seguro directo)"))
			    	{
			    		double valor1 = totalMemoria.get(mesAnoCalculo+"_Composición de la cartera técnica (Primas directas)Caución");
			    		
			    		total = valor1;
			    		
			    		double divisor = totalCarteiraTecnicalMap.get(c.getTimeInMillis());
			    		
			    		if(divisor > 0)
			    			totalPorcentagem = (valor1 * 100) / divisor;
			    		
			    		totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
			    		totalMemoria.put(mesAnoCalculo+"_P"+nomeGrupo+nomeCalculo, totalPorcentagem);
			    		
			    		mostraPorcentagem = true;
			    	}
			    	else if(nomeCalculo.equals("Robo y asalto") && nomeGrupo.equals("Composición de la cartera técnica (prima de seguro directo)"))
			    	{
			    		double valor1 = totalMemoria.get(mesAnoCalculo+"_Composición de la cartera técnica (Primas directas)Robo y asalto");
			    		
			    		total = valor1;
			    		
			    		double divisor = totalCarteiraTecnicalMap.get(c.getTimeInMillis());
			    		
			    		if(divisor > 0)
			    			totalPorcentagem = (valor1 * 100) / divisor;
			    		
			    		totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
			    		totalMemoria.put(mesAnoCalculo+"_P"+nomeGrupo+nomeCalculo, totalPorcentagem);
			    		
			    		mostraPorcentagem = true;
			    	}
			    	else if(nomeCalculo.equals("Responsabilidad civil") && nomeGrupo.equals("Composición de la cartera técnica (prima de seguro directo)"))
			    	{
			    		double valor1 = totalMemoria.get(mesAnoCalculo+"_Composición de la cartera técnica (Primas directas)Responsabilidad civil");
			    		
			    		total = valor1;
			    		
			    		double divisor = totalCarteiraTecnicalMap.get(c.getTimeInMillis());
			    		
			    		if(divisor > 0)
			    			totalPorcentagem = (valor1 * 100) / divisor;
			    		
			    		totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
			    		totalMemoria.put(mesAnoCalculo+"_P"+nomeGrupo+nomeCalculo, totalPorcentagem);
			    		
			    		mostraPorcentagem = true;
			    	}
			    	else if(nomeCalculo.equals("Accidentes personales") && nomeGrupo.equals("Composición de la cartera técnica (prima de seguro directo)"))
			    	{
			    		double valor1 = totalMemoria.get(mesAnoCalculo+"_Composición de la cartera técnica (Primas directas)Accidentes personales");
			    		
			    		total = valor1;
			    		
			    		double divisor = totalCarteiraTecnicalMap.get(c.getTimeInMillis());
			    		
			    		if(divisor > 0)
			    			totalPorcentagem = (valor1 * 100) / divisor;
			    		
			    		totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
			    		totalMemoria.put(mesAnoCalculo+"_P"+nomeGrupo+nomeCalculo, totalPorcentagem);
			    		
			    		mostraPorcentagem = true;
			    	}
			    	else if(nomeCalculo.equals("Accidentes pasajeros") && nomeGrupo.equals("Composición de la cartera técnica (prima de seguro directo)"))
			    	{
			    		double valor1 = totalMemoria.get(mesAnoCalculo+"_Composición de la cartera técnica (Primas directas)Accidentes a pasajeros");
			    		
			    		total = valor1;
			    		
			    		double divisor = totalCarteiraTecnicalMap.get(c.getTimeInMillis());
			    		
			    		if(divisor > 0)
			    			totalPorcentagem = (valor1 * 100) / divisor;
			    		
			    		totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
			    		totalMemoria.put(mesAnoCalculo+"_P"+nomeGrupo+nomeCalculo, totalPorcentagem);
			    		
			    		mostraPorcentagem = true;
			    	}
			    	else if(nomeCalculo.equals("Aeronavegacion") && nomeGrupo.equals("Composición de la cartera técnica (prima de seguro directo)"))
			    	{
			    		double valor1 = totalMemoria.get(mesAnoCalculo+"_Composición de la cartera técnica (Primas directas)Aeronavegación");
			    		
			    		total = valor1;
			    		
			    		double divisor = totalCarteiraTecnicalMap.get(c.getTimeInMillis());
			    		
			    		if(divisor > 0)
			    			totalPorcentagem = (valor1 * 100) / divisor;
			    		
			    		totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
			    		totalMemoria.put(mesAnoCalculo+"_P"+nomeGrupo+nomeCalculo, totalPorcentagem);
			    		
			    		mostraPorcentagem = true;
			    	}
			    	else if(nomeCalculo.equals("Vida largo plazo seccion vida individual largo plazo"))
			    	{
			    		double valor1 = totalMemoria.get(mesAnoCalculo+"_Primas de seguros rama vida - con reservas matematicas (seguros directos)Primas seccion vida individual -  largo plazo");
			    		
			    		total = valor1;
			    		
			    		double divisor = totalCarteiraTecnicalMap.get(c.getTimeInMillis());
			    		
			    		if(divisor > 0)
			    			totalPorcentagem = (valor1 * 100) / divisor;
			    		
			    		totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
			    		totalMemoria.put(mesAnoCalculo+"_P"+nomeGrupo+nomeCalculo, totalPorcentagem);
			    		
			    		mostraPorcentagem = true;
			    	}
			    	else if(nomeCalculo.equals("Primas seccion seguros colectivos - largo plazo") && nomeGrupo.equals("Composición de la cartera técnica (prima de seguro directo)"))
			    	{
			    		double valor1 = totalMemoria.get(mesAnoCalculo+"_Primas de seguros rama vida - con reservas matematicas (seguros directos)Primas seccion seguros colectivos - largo plazo");
			    		
			    		total = valor1;
			    		
			    		double divisor = totalCarteiraTecnicalMap.get(c.getTimeInMillis());
			    		
			    		if(divisor > 0)
			    			totalPorcentagem = (valor1 * 100) / divisor;
			    		
			    		totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
			    		totalMemoria.put(mesAnoCalculo+"_P"+nomeGrupo+nomeCalculo, totalPorcentagem);
			    		
			    		mostraPorcentagem = true;
			    	}
			    	else if(nomeCalculo.equals("Primas seccion pensiones voluntarias") && nomeGrupo.equals("Composición de la cartera técnica (prima de seguro directo)"))
			    	{
			    		double valor1 = totalMemoria.get(mesAnoCalculo+"_Primas de seguros rama vida - con reservas matematicas (seguros directos)Primas seccion pensiones voluntarias");
			    		
			    		total = valor1;	
			    		
			    		double divisor = totalCarteiraTecnicalMap.get(c.getTimeInMillis());
			    		
			    		if(divisor > 0)
			    			totalPorcentagem = (valor1 * 100) / divisor;
			    		
			    		totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
			    		totalMemoria.put(mesAnoCalculo+"_P"+nomeGrupo+nomeCalculo, totalPorcentagem);
			    		
			    		mostraPorcentagem = true;
			    	}
			    	else if(nomeCalculo.equals("Primas seccion desgravamen hipotercario"))
			    	{
			    		double valor1 = totalMemoria.get(mesAnoCalculo+"_Primas de seguros rama vida - con reservas matematicas (seguros directos)Primas seccion desgravamen hipotecario - largo plazo");
			    		
			    		total = valor1;
			    		
			    		double divisor = totalCarteiraTecnicalMap.get(c.getTimeInMillis());
			    		
			    		if(divisor > 0)
			    			totalPorcentagem = (valor1 * 100) / divisor;
			    		
			    		totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
			    		totalMemoria.put(mesAnoCalculo+"_P"+nomeGrupo+nomeCalculo, totalPorcentagem);
			    		
			    		mostraPorcentagem = true;
			    	}
			    	else if(nomeCalculo.equals("Seguro de salud"))
			    	{
			    		double valor1 = totalMemoria.get(mesAnoCalculo+"_Primas de seguros rama patrimonial (seguros directos)Primas seccion salud o enfermedad");
			    		
			    		total = valor1;
			    		
			    		double divisor = totalCarteiraTecnicalMap.get(c.getTimeInMillis());
			    		
			    		if(divisor > 0)
			    			totalPorcentagem = (valor1 * 100) / divisor;
			    		
			    		totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
			    		totalMemoria.put(mesAnoCalculo+"_P"+nomeGrupo+nomeCalculo, totalPorcentagem);
			    		
			    		mostraPorcentagem = true;
			    	}
			    	else if(nomeCalculo.equals("TOTAL") && nomeGrupo.equals("Composición de la cartera técnica (prima de seguro directo)"))
			    	{
			    		double valor1 = totalMemoria.get(mesAnoCalculo+"_"+nomeGrupo+"Primas directas automoviles");
			    		double valor2 = totalMemoria.get(mesAnoCalculo+"_"+nomeGrupo+"Primas seccion vida individual - corto plazo");
			    		double valor3 = totalMemoria.get(mesAnoCalculo+"_"+nomeGrupo+"Primas seccion vida colectiva - corto plazo");
			    		double valor4 = totalMemoria.get(mesAnoCalculo+"_"+nomeGrupo+"Primas seccion desgravamen hipotecario - corto plazo");
			    		double valor5 = totalMemoria.get(mesAnoCalculo+"_"+nomeGrupo+"Primas seccion defuncion o sepelio");
			    		double valor6 = totalMemoria.get(mesAnoCalculo+"_"+nomeGrupo+"Incendios");
			    		double valor7 = totalMemoria.get(mesAnoCalculo+"_"+nomeGrupo+"Seguros agrarios");
			    		double valor8 = totalMemoria.get(mesAnoCalculo+"_"+nomeGrupo+"Riesgos varios -cristales vidrios y espejos");
			    		double valor9 = totalMemoria.get(mesAnoCalculo+"_"+nomeGrupo+"Riesgos varios - riesgos varios");
			    		double valor10 = totalMemoria.get(mesAnoCalculo+"_"+nomeGrupo+"Riesgos tecnicos");
			    		double valor11 = totalMemoria.get(mesAnoCalculo+"_"+nomeGrupo+"Transportes");
			    		double valor12 = totalMemoria.get(mesAnoCalculo+"_"+nomeGrupo+"Caucion");
			    		double valor13 = totalMemoria.get(mesAnoCalculo+"_"+nomeGrupo+"Robo y asalto");
			    		double valor14 = totalMemoria.get(mesAnoCalculo+"_"+nomeGrupo+"Responsabilidad civil");
			    		double valor15 = totalMemoria.get(mesAnoCalculo+"_"+nomeGrupo+"Accidentes personales");
			    		double valor16 = totalMemoria.get(mesAnoCalculo+"_"+nomeGrupo+"Accidentes pasajeros");
			    		double valor17 = totalMemoria.get(mesAnoCalculo+"_"+nomeGrupo+"Aeronavegacion");
			    		double valor18 = totalMemoria.get(mesAnoCalculo+"_"+nomeGrupo+"Vida largo plazo seccion vida individual largo plazo");
			    		double valor19 = totalMemoria.get(mesAnoCalculo+"_"+nomeGrupo+"Primas seccion seguros colectivos - largo plazo");
			    		double valor20 = totalMemoria.get(mesAnoCalculo+"_"+nomeGrupo+"Primas seccion pensiones voluntarias");
			    		double valor21 = totalMemoria.get(mesAnoCalculo+"_"+nomeGrupo+"Primas seccion desgravamen hipotercario");
			    		double valor22 = totalMemoria.get(mesAnoCalculo+"_"+nomeGrupo+"Seguro de salud");
			    		
		    			total = valor1 + valor2 + valor3 + valor4 + valor5 + valor6 + valor7 + valor8 + valor9 + valor10 + valor11 + valor12 + valor13 + valor14 + valor15 + valor16 + valor17 + valor18 + valor19 + valor20 + valor21 + valor22;
			    		
		    			valor1 = totalMemoria.get(mesAnoCalculo+"_P"+nomeGrupo+"Primas directas automoviles");
			    		valor2 = totalMemoria.get(mesAnoCalculo+"_P"+nomeGrupo+"Primas seccion vida individual - corto plazo");
			    		valor3 = totalMemoria.get(mesAnoCalculo+"_P"+nomeGrupo+"Primas seccion vida colectiva - corto plazo");
			    		valor4 = totalMemoria.get(mesAnoCalculo+"_P"+nomeGrupo+"Primas seccion desgravamen hipotecario - corto plazo");
			    		valor5 = totalMemoria.get(mesAnoCalculo+"_P"+nomeGrupo+"Primas seccion defuncion o sepelio");
			    		valor6 = totalMemoria.get(mesAnoCalculo+"_P"+nomeGrupo+"Incendios");
			    		valor7 = totalMemoria.get(mesAnoCalculo+"_P"+nomeGrupo+"Seguros agrarios");
			    		valor8 = totalMemoria.get(mesAnoCalculo+"_P"+nomeGrupo+"Riesgos varios -cristales vidrios y espejos");
			    		valor9 = totalMemoria.get(mesAnoCalculo+"_P"+nomeGrupo+"Riesgos varios - riesgos varios");
			    		valor10 = totalMemoria.get(mesAnoCalculo+"_P"+nomeGrupo+"Riesgos tecnicos");
			    		valor11 = totalMemoria.get(mesAnoCalculo+"_P"+nomeGrupo+"Transportes");
			    		valor12 = totalMemoria.get(mesAnoCalculo+"_P"+nomeGrupo+"Caucion");
			    		valor13 = totalMemoria.get(mesAnoCalculo+"_P"+nomeGrupo+"Robo y asalto");
			    		valor14 = totalMemoria.get(mesAnoCalculo+"_P"+nomeGrupo+"Responsabilidad civil");
			    		valor15 = totalMemoria.get(mesAnoCalculo+"_P"+nomeGrupo+"Accidentes personales");
			    		valor16 = totalMemoria.get(mesAnoCalculo+"_P"+nomeGrupo+"Accidentes pasajeros");
			    		valor17 = totalMemoria.get(mesAnoCalculo+"_P"+nomeGrupo+"Aeronavegacion");
			    		valor18 = totalMemoria.get(mesAnoCalculo+"_P"+nomeGrupo+"Vida largo plazo seccion vida individual largo plazo");
			    		valor19 = totalMemoria.get(mesAnoCalculo+"_P"+nomeGrupo+"Primas seccion seguros colectivos - largo plazo");
			    		valor20 = totalMemoria.get(mesAnoCalculo+"_P"+nomeGrupo+"Primas seccion pensiones voluntarias");
			    		valor21 = totalMemoria.get(mesAnoCalculo+"_P"+nomeGrupo+"Primas seccion desgravamen hipotercario");
			    		valor22 = totalMemoria.get(mesAnoCalculo+"_P"+nomeGrupo+"Seguro de salud");
			    		
		    			totalPorcentagem = valor1 + valor2 + valor3 + valor4 + valor5 + valor6 + valor7 + valor8 + valor9 + valor10 + valor11 + valor12 + valor13 + valor14 + valor15 + valor16 + valor17 + valor18 + valor19 + valor20 + valor21 + valor22;
		    			
			    		totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
			    		totalMemoria.put(mesAnoCalculo+"_P"+nomeGrupo+nomeCalculo, totalPorcentagem);
			    		
			    		mostraPorcentagem = true;
			    	}
			    	else if(nomeCalculo.equals("Capital Social"))
			    	{
			    		conta = cContas.get("0301000000");
			    		double valor1 = this.totalConta(conta, c.getTime());
			    		
		    			total = valor1;
			    		
			    		totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
			    	}
			    	else if(nomeCalculo.equals("Cuentas pendientes de capitalizacion"))
			    	{
			    		conta = cContas.get("0302000000");
			    		double valor1 = this.totalConta(conta, c.getTime());
			    		
		    			total = valor1;
			    		
			    		totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
			    	}
			    	else if(nomeCalculo.equals("Reservas"))
			    	{
			    		conta = cContas.get("0303000000");
			    		double valor1 = this.totalConta(conta, c.getTime());
			    		
		    			total = valor1;
			    		
			    		totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
			    	}
			    	else if(nomeCalculo.equals("Resultados Acumulados"))
			    	{
			    		conta = cContas.get("0304000000");
			    		double valor1 = this.totalConta(conta, c.getTime());
			    		
		    			total = valor1;
			    		
			    		totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
			    	}
			    	else if(nomeCalculo.equals("Resultados del Ejercicio"))
			    	{
			    		conta = cContas.get("0305000000");
			    		double valor1 = this.totalConta(conta, c.getTime());
			    		
		    			total = valor1;
			    		
			    		totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
			    	}
			    	else if(nomeCalculo.equals("TOTAL") && nomeGrupo.equals("Patrimonio Neto"))
			    	{
			    		double valor1 = totalMemoria.get(mesAnoCalculo+"_"+nomeGrupo+"Capital Social");
			    		double valor2 = totalMemoria.get(mesAnoCalculo+"_"+nomeGrupo+"Cuentas pendientes de capitalizacion");
			    		double valor3 = totalMemoria.get(mesAnoCalculo+"_"+nomeGrupo+"Reservas");
			    		double valor4 = totalMemoria.get(mesAnoCalculo+"_"+nomeGrupo+"Resultados Acumulados");
			    		double valor5 = totalMemoria.get(mesAnoCalculo+"_"+nomeGrupo+"Resultados del Ejercicio");
			    		
		    			total = valor1 + valor2 + valor3 + valor4 + valor5;
			    		
			    		totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
			    	}
			    	else if(nomeCalculo.equals("INVERSIONES"))
			    	{
			    		conta = cContas.get("0107000000");
			    		double valor1 = this.totalConta(conta, c.getTime());
			    		
			    		//Calcular o total antes do calculo
			    		conta = cContas.get("0107010000");
			    		double valor2 = this.totalConta(conta, c.getTime());
			    		conta = cContas.get("0107010100");
			    		double valor3 = this.totalConta(conta, c.getTime());
			    		conta2 = contas.get("0107010101");
			    		double valor4 = this.totalConta(conta2, c.getTime());
			    		conta2 = contas.get("0107010102");
			    		double valor5 = this.totalConta(conta2, c.getTime());
			    		conta = cContas.get("0107010200");
			    		double valor6 = this.totalConta(conta, c.getTime());
			    		conta2 = contas.get("0107010201");
			    		double valor7 = this.totalConta(conta2, c.getTime());
			    		conta2 = contas.get("0107010202");
			    		double valor8 = this.totalConta(conta2, c.getTime());
			    		conta = cContas.get("0107010300");
			    		double valor9 = this.totalConta(conta, c.getTime());
			    		conta2 = contas.get("0107010301");
			    		double valor10 = this.totalConta(conta2, c.getTime());
			    		conta = cContas.get("0107010400");
			    		double valor11 = this.totalConta(conta, c.getTime());
			    		conta2 = contas.get("0107010401");
			    		double valor12 = this.totalConta(conta2, c.getTime());
			    		conta = cContas.get("0107010500");
			    		double valor13 = this.totalConta(conta, c.getTime());
			    		conta2 = contas.get("0107010501");
			    		double valor14 = this.totalConta(conta2, c.getTime());
			    		conta2 = contas.get("0107010502");
			    		double valor15 = this.totalConta(conta2, c.getTime());
			    		conta = cContas.get("0107010600");
			    		double valor16 = this.totalConta(conta, c.getTime());
			    		conta2 = contas.get("0107010601");
			    		double valor17 = this.totalConta(conta2, c.getTime());
			    		conta = cContas.get("0107014000");
			    		double valor18 = this.totalConta(conta, c.getTime());
			    		conta = cContas.get("0107020000");
			    		double valor19 = this.totalConta(conta, c.getTime());
			    		conta = cContas.get("0107020100");
			    		double valor20 = this.totalConta(conta, c.getTime());
			    		conta2 = contas.get("0107020101");
			    		double valor21 = this.totalConta(conta2, c.getTime());
			    		conta = cContas.get("0107020200");
			    		double valor22 = this.totalConta(conta, c.getTime());
			    		conta2 = contas.get("0107020201");
			    		double valor23 = this.totalConta(conta2, c.getTime());
			    		conta2 = contas.get("0107020202");
			    		double valor24 = this.totalConta(conta2, c.getTime());
			    		conta = cContas.get("0107020300");
			    		double valor25 = this.totalConta(conta, c.getTime());
			    		conta2 = contas.get("0107020301");
			    		double valor26 = this.totalConta(conta2, c.getTime());
			    		conta = cContas.get("0107020400");
			    		double valor27 = this.totalConta(conta, c.getTime());
			    		conta2 = contas.get("0107020401");
			    		double valor28 = this.totalConta(conta2, c.getTime());
			    		conta = cContas.get("0107020500");
			    		double valor29 = this.totalConta(conta, c.getTime());
			    		conta2 = contas.get("0107020501");
			    		double valor30 = this.totalConta(conta2, c.getTime());
			    		conta = cContas.get("0107024000");
			    		double valor31 = this.totalConta(conta, c.getTime());
			    		conta = cContas.get("0107025000");
			    		double valor32 = this.totalConta(conta, c.getTime());
			    		conta = cContas.get("0107030000");
			    		double valor33 = this.totalConta(conta, c.getTime());
			    		conta = cContas.get("0107030100");
			    		double valor34 = this.totalConta(conta, c.getTime());
			    		conta2 = contas.get("0107030101");
			    		double valor35 = this.totalConta(conta2, c.getTime());
			    		conta = cContas.get("0107030200");
			    		double valor36 = this.totalConta(conta, c.getTime());
			    		conta2 = contas.get("0107030201");
			    		double valor37 = this.totalConta(conta2, c.getTime());
			    		conta = cContas.get("0107030300");
			    		double valor38 = this.totalConta(conta, c.getTime());
			    		conta2 = contas.get("0107030301");
			    		double valor39 = this.totalConta(conta2, c.getTime());
			    		conta = cContas.get("0107030400");
			    		double valor40 = this.totalConta(conta, c.getTime());
			    		conta2 = contas.get("0107030401");
			    		double valor41 = this.totalConta(conta2, c.getTime());
			    		conta = cContas.get("0107034000");
			    		double valor42 = this.totalConta(conta, c.getTime());
			    		conta2 = contas.get("0107034001");
			    		double valor43 = this.totalConta(conta2, c.getTime());
			    		conta2 = contas.get("0107034002");
			    		double valor44 = this.totalConta(conta2, c.getTime());
			    		conta2 = contas.get("0107034003");
			    		double valor45 = this.totalConta(conta2, c.getTime());
			    		conta = cContas.get("0107035000");
			    		double valor46 = this.totalConta(conta, c.getTime());
			    		conta = cContas.get("0107040000");
			    		double valor47 = this.totalConta(conta, c.getTime());
			    		conta = cContas.get("0107040100");
			    		double valor48 = this.totalConta(conta, c.getTime());
			    		conta2 = contas.get("0107040101");
			    		double valor49 = this.totalConta(conta2, c.getTime());
			    		conta = cContas.get("0107040200");
			    		double valor50 = this.totalConta(conta, c.getTime());
			    		conta2 = contas.get("0107040201");
			    		double valor51 = this.totalConta(conta2, c.getTime());
			    		conta = cContas.get("0107040300");
			    		double valor52 = this.totalConta(conta, c.getTime());
			    		conta2 = contas.get("0107040301");
			    		double valor53 = this.totalConta(conta2, c.getTime());
			    		conta = cContas.get("0107040400");
			    		double valor54 = this.totalConta(conta, c.getTime());
			    		conta2 = contas.get("0107040401");
			    		double valor55 = this.totalConta(conta2, c.getTime());
			    		conta = cContas.get("0107044000");
			    		double valor56 = this.totalConta(conta, c.getTime());
			    		conta2 = contas.get("0107044001");
			    		double valor57 = this.totalConta(conta2, c.getTime());
			    		conta2 = contas.get("0107044002");
			    		double valor58 = this.totalConta(conta2, c.getTime());
			    		conta2 = contas.get("0107044003");
			    		double valor59 = this.totalConta(conta2, c.getTime());
			    		conta = cContas.get("0107050000");
			    		double valor60 = this.totalConta(conta, c.getTime());
			    		conta = cContas.get("0107050100");
			    		double valor61 = this.totalConta(conta, c.getTime());
			    		conta2 = contas.get("0107050101");
			    		double valor62 = this.totalConta(conta2, c.getTime());
			    		conta2 = contas.get("0107050102");
			    		double valor63 = this.totalConta(conta2, c.getTime());
			    		conta2 = contas.get("0107050103");
			    		double valor64 = this.totalConta(conta2, c.getTime());
			    		conta2 = contas.get("0107050104");
			    		double valor65 = this.totalConta(conta2, c.getTime());
			    		conta = cContas.get("0107050200");
			    		double valor66 = this.totalConta(conta, c.getTime());
			    		conta2 = contas.get("0107050201");
			    		double valor67 = this.totalConta(conta2, c.getTime());
			    		conta2 = contas.get("0107050202");
			    		double valor68 = this.totalConta(conta2, c.getTime());
			    		conta = cContas.get("0107050300");
			    		double valor69 = this.totalConta(conta, c.getTime());
			    		conta2 = contas.get("0107050301");
			    		double valor70 = this.totalConta(conta2, c.getTime());
			    		conta2 = contas.get("0107050302");
			    		double valor71 = this.totalConta(conta2, c.getTime());
			    		conta = cContas.get("0107054000");
			    		double valor72 = this.totalConta(conta, c.getTime());
			    		conta = cContas.get("0107054200");
			    		double valor73 = this.totalConta(conta, c.getTime());
			    		conta = cContas.get("0107054400");
			    		double valor74 = this.totalConta(conta, c.getTime());
			    		conta = cContas.get("0107060000");
			    		double valor75 = this.totalConta(conta, c.getTime());
			    		conta = cContas.get("0107060100");
			    		double valor76 = this.totalConta(conta, c.getTime());
			    		conta = cContas.get("0107060200");
			    		double valor77 = this.totalConta(conta, c.getTime());
			    		conta = cContas.get("0107060300");
			    		double valor78 = this.totalConta(conta, c.getTime());
			    		conta = cContas.get("0107060400");
			    		double valor79 = this.totalConta(conta, c.getTime());
			    		
			    		total = valor1;
			    		
			    		totalInversiones = valor1 + valor2 + valor3 + valor4 + valor5 + valor6 + valor7 + valor8 + valor9 + valor10 + valor11 + valor12 + valor13 + valor14 + valor15 + valor16 + valor17 + valor18
			    				+ valor19 + valor20 + valor21 + valor22 + valor23 + valor24 + valor25 + valor26 + valor27 + valor28 + valor29 + valor30 + valor31 + valor32 + valor33 + valor34 + valor35 + valor36
			    				+ valor37 + valor38 + valor39 + valor40 + valor41 + valor42 + valor43 + valor44 + valor45 + valor46 + valor47 + valor48 + valor49 + valor50 + valor51 + valor52 + valor53 + valor54
			    				+ valor55 + valor56 + valor57 + valor58 + valor59 + valor60 + valor61 + valor62 + valor63 + valor64 + valor65 + valor66 + valor67 + valor68 + valor69 + valor70 + valor71 + valor72
			    				+ valor73 + valor74 + valor75 + valor76 + valor77 + valor78 + valor79;
			    		
			    		if(totalInversiones > 0)
			    			totalPorcentagem = (valor1 * 100) / totalInversiones;
			    		
			    		totalInversionesMap.put(c.getTimeInMillis(), totalInversiones);
			    		
			    		totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
			    		totalMemoria.put(mesAnoCalculo+"_P"+nomeGrupo+nomeCalculo, totalPorcentagem);
			    		
			    		mostraPorcentagem = true;
			    	}
			    	else if(nomeCalculo.equals("TÍTULOS VALORES DE RENTA FIJA - LOCAL"))
			    	{
			    		conta = cContas.get("0107010000");
			    		
			    		double valor1 = this.totalConta(conta, c.getTime());
			    		
			    		total = valor1;
			    		
			    		double divisor = totalInversionesMap.get(c.getTimeInMillis());
			    		
			    		if(divisor > 0)
			    			totalPorcentagem = (valor1 *100) / divisor;
			    		
			    		totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
			    		totalMemoria.put(mesAnoCalculo+"_P"+nomeGrupo+nomeCalculo, totalPorcentagem);
			    		
			    		mostraPorcentagem = true;
			    	}
			    	else if(nomeCalculo.equals("EMITIDOS POR EL ESTADO Y ENTIDADES PÚBLICAS"))
			    	{
			    		conta = cContas.get("0107010100");
			    		double valor1 = this.totalConta(conta, c.getTime());
			    		
			    		total = valor1;
			    		
			    		double divisor = totalInversionesMap.get(c.getTimeInMillis());
			    		
			    		if(divisor > 0)
			    			totalPorcentagem = (valor1 *100) / divisor;
			    		
			    		totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
			    		totalMemoria.put(mesAnoCalculo+"_P"+nomeGrupo+nomeCalculo, totalPorcentagem);
			    		
			    		mostraPorcentagem = true;
			    	}
			    	else if(nomeCalculo.equals("Bonos"))
			    	{
			    		conta2 = contas.get("0107010101");
			    		double valor1 = this.totalConta(conta2, c.getTime());
			    		
			    		total = valor1;
			    		
			    		double divisor = totalInversionesMap.get(c.getTimeInMillis());
			    		
			    		if(divisor > 0)
			    			totalPorcentagem = (valor1 *100) / divisor;
			    		
			    		totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
			    		totalMemoria.put(mesAnoCalculo+"_P"+nomeGrupo+nomeCalculo, totalPorcentagem);
			    		
			    		mostraPorcentagem = true;
			    	}
			    	else if(nomeCalculo.equals("Letras de Regulación Monetaria"))
			    	{
			    		conta2 = contas.get("0107010102");
			    		double valor1 = this.totalConta(conta2, c.getTime());
			    		
			    		total = valor1;
			    		
			    		double divisor = totalInversionesMap.get(c.getTimeInMillis());
			    		
			    		if(divisor > 0)
			    			totalPorcentagem = (valor1 *100) / divisor;
			    		
			    		totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
			    		totalMemoria.put(mesAnoCalculo+"_P"+nomeGrupo+nomeCalculo, totalPorcentagem);
			    		
			    		mostraPorcentagem = true;
			    	}
			    	else if(nomeCalculo.equals("EMITIDOS POR ENTIDADES DEL SECTOR FINANCIERO"))
			    	{
			    		conta = cContas.get("0107010200");
			    		double valor1 = this.totalConta(conta, c.getTime());
			    		
			    		total = valor1;
			    		
			    		double divisor = totalInversionesMap.get(c.getTimeInMillis());
			    		
			    		if(divisor > 0)
			    			totalPorcentagem = (valor1 *100) / divisor;
			    		
			    		totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
			    		totalMemoria.put(mesAnoCalculo+"_P"+nomeGrupo+nomeCalculo, totalPorcentagem);
			    		
			    		mostraPorcentagem = true;
			    	}
			    	else if(nomeCalculo.equals("Certificados de Depósitos de Ahorro"))
			    	{
			    		conta2 = contas.get("0107010201");
			    		double valor1 = this.totalConta(conta2, c.getTime());
			    		
			    		total = valor1;
			    		
			    		double divisor = totalInversionesMap.get(c.getTimeInMillis());
			    		
			    		if(divisor > 0)
			    			totalPorcentagem = (valor1 *100) / divisor;
			    		
			    		totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
			    		totalMemoria.put(mesAnoCalculo+"_P"+nomeGrupo+nomeCalculo, totalPorcentagem);
			    		
			    		mostraPorcentagem = true;
			    	}
			    	else if(nomeCalculo.equals("Títulos de Inversión"))
			    	{
			    		conta2 = contas.get("0107010202");
			    		double valor1 = this.totalConta(conta2, c.getTime());
			    		
			    		total = valor1;
			    		
			    		double divisor = totalInversionesMap.get(c.getTimeInMillis());
			    		
			    		if(divisor > 0)
			    			totalPorcentagem = (valor1 *100) / divisor;
			    		
			    		totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
			    		totalMemoria.put(mesAnoCalculo+"_P"+nomeGrupo+nomeCalculo, totalPorcentagem);
			    		
			    		mostraPorcentagem = true;
			    	}
			    	else if(nomeCalculo.equals("EMITIDOS POR ENTIDADES DEL SECTOR PRIVADO"))
			    	{
			    		conta = cContas.get("0107010300");
			    		double valor1 = this.totalConta(conta, c.getTime());
			    		
			    		total = valor1;
			    		
			    		double divisor = totalInversionesMap.get(c.getTimeInMillis());
			    		
			    		if(divisor > 0)
			    			totalPorcentagem = (valor1 *100) / divisor;
			    		
			    		totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
			    		totalMemoria.put(mesAnoCalculo+"_P"+nomeGrupo+nomeCalculo, totalPorcentagem);
			    		
			    		mostraPorcentagem = true;
			    	}
			    	else if(nomeCalculo.equals("Títulos de Deuda - Sector Privado"))
			    	{
			    		conta2 = contas.get("0107010301");
			    		double valor1 = this.totalConta(conta2, c.getTime());
			    		
			    		total = valor1;
			    		
			    		double divisor = totalInversionesMap.get(c.getTimeInMillis());
			    		
			    		if(divisor > 0)
			    			totalPorcentagem = (valor1 *100) / divisor;
			    		
			    		totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
			    		totalMemoria.put(mesAnoCalculo+"_P"+nomeGrupo+nomeCalculo, totalPorcentagem);
			    		
			    		mostraPorcentagem = true;
			    	}
			    	else if(nomeCalculo.equals("EMITIDOS POR EMPRESAS VINCULADAS"))
			    	{
			    		conta = cContas.get("0107010400");
			    		double valor1 = this.totalConta(conta, c.getTime());
			    		
			    		total = valor1;
			    		
			    		double divisor = totalInversionesMap.get(c.getTimeInMillis());
			    		
			    		if(divisor > 0)
			    			totalPorcentagem = (valor1 *100) / divisor;
			    		
			    		totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
			    		totalMemoria.put(mesAnoCalculo+"_P"+nomeGrupo+nomeCalculo, totalPorcentagem);
			    		
			    		mostraPorcentagem = true;
			    	}
			    	else if(nomeCalculo.equals("Títulos de Deuda - Empresas Vinculadas"))
			    	{
			    		conta2 = contas.get("0107010401");
			    		double valor1 = this.totalConta(conta2, c.getTime());
			    		
			    		total = valor1;
			    		
			    		double divisor = totalInversionesMap.get(c.getTimeInMillis());
			    		
			    		if(divisor > 0)
			    			totalPorcentagem = (valor1 *100) / divisor;
			    		
			    		totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
			    		totalMemoria.put(mesAnoCalculo+"_P"+nomeGrupo+nomeCalculo, totalPorcentagem);
			    		
			    		mostraPorcentagem = true;
			    	}
			    	else if(nomeCalculo.equals("DEPÓSITOS RESTRINGIDOS"))
			    	{
			    		conta = cContas.get("0107010500");
			    		double valor1 = this.totalConta(conta, c.getTime());
			    		
			    		total = valor1;
			    		
			    		double divisor = totalInversionesMap.get(c.getTimeInMillis());
			    		
			    		if(divisor > 0)
			    			totalPorcentagem = (valor1 *100) / divisor;
			    		
			    		totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
			    		totalMemoria.put(mesAnoCalculo+"_P"+nomeGrupo+nomeCalculo, totalPorcentagem);
			    		
			    		mostraPorcentagem = true;
			    	}
			    	else if(nomeCalculo.equals("Depósitos - Restringidos en Sistema Financiero"))
			    	{
			    		conta2 = contas.get("0107010501");
			    		double valor1 = this.totalConta(conta2, c.getTime());
			    		
			    		total = valor1;
			    		
			    		double divisor = totalInversionesMap.get(c.getTimeInMillis());
			    		
			    		if(divisor > 0)
			    			totalPorcentagem = (valor1 *100) / divisor;
			    		
			    		totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
			    		totalMemoria.put(mesAnoCalculo+"_P"+nomeGrupo+nomeCalculo, totalPorcentagem);
			    		
			    		mostraPorcentagem = true;
			    	}
			    	else if(nomeCalculo.equals("Depósitos - Embargados"))
			    	{
			    		conta2 = contas.get("0107010502");
			    		double valor1 = this.totalConta(conta2, c.getTime());
			    		
			    		total = valor1;
			    		
			    		double divisor = totalInversionesMap.get(c.getTimeInMillis());
			    		
			    		if(divisor > 0)
			    			totalPorcentagem = (valor1 *100) / divisor;
			    		
			    		totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
			    		totalMemoria.put(mesAnoCalculo+"_P"+nomeGrupo+nomeCalculo, totalPorcentagem);
			    		
			    		mostraPorcentagem = true;
			    	}
			    	else if(nomeCalculo.equals("INVERSIONES ESPECIALES - RENTA FIJA"))
			    	{
			    		conta = cContas.get("0107010600");
			    		double valor1 = this.totalConta(conta, c.getTime());
			    		
			    		total = valor1;
			    		
			    		double divisor = totalInversionesMap.get(c.getTimeInMillis());
			    		
			    		if(divisor > 0)
			    			totalPorcentagem = (valor1 *100) / divisor;
			    		
			    		totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
			    		totalMemoria.put(mesAnoCalculo+"_P"+nomeGrupo+nomeCalculo, totalPorcentagem);
			    		
			    		mostraPorcentagem = true;
			    	}
			    	else if(nomeCalculo.equals("Inversiones Especiales - Renta Fija cuenta 01"))
			    	{
			    		conta2 = contas.get("0107010601");
			    		double valor1 = this.totalConta(conta2, c.getTime());
			    		
			    		total = valor1;
			    		
			    		double divisor = totalInversionesMap.get(c.getTimeInMillis());
			    		
			    		if(divisor > 0)
			    			totalPorcentagem = (valor1 *100) / divisor;
			    		
			    		totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
			    		totalMemoria.put(mesAnoCalculo+"_P"+nomeGrupo+nomeCalculo, totalPorcentagem);
			    		
			    		mostraPorcentagem = true;
			    	}
			    	else if(nomeCalculo.equals("INTERESES DEVENGADOS S/RENTA FIJA"))
			    	{
			    		conta = cContas.get("0107014000");
			    		double valor1 = this.totalConta(conta, c.getTime());
			    		
			    		total = valor1;
			    		
			    		double divisor = totalInversionesMap.get(c.getTimeInMillis());
			    		
			    		if(divisor > 0)
			    			totalPorcentagem = (valor1 *100) / divisor;
			    		
			    		totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
			    		totalMemoria.put(mesAnoCalculo+"_P"+nomeGrupo+nomeCalculo, totalPorcentagem);
			    		
			    		mostraPorcentagem = true;
			    	}
			    	else if(nomeCalculo.equals("TÍTULOS VALORES DE RENTA FIJA - EXTERIOR"))
			    	{
			    		conta = cContas.get("0107020000");
			    		double valor1 = this.totalConta(conta, c.getTime());
			    		
			    		total = valor1;
			    		
			    		double divisor = totalInversionesMap.get(c.getTimeInMillis());
			    		
			    		if(divisor > 0)
			    			totalPorcentagem = (valor1 *100) / divisor;
			    		
			    		totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
			    		totalMemoria.put(mesAnoCalculo+"_P"+nomeGrupo+nomeCalculo, totalPorcentagem);
			    		
			    		mostraPorcentagem = true;
			    	}
			    	else if(nomeCalculo.equals("EMITIDOS POR EL ESTADO Y ENTIDADES PÚBLICAS - EXTERIOR"))
			    	{
			    		conta = cContas.get("0107020100");
			    		double valor1 = this.totalConta(conta, c.getTime());
			    		
			    		total = valor1;
			    		
			    		double divisor = totalInversionesMap.get(c.getTimeInMillis());
			    		
			    		if(divisor > 0)
			    			totalPorcentagem = (valor1 *100) / divisor;
			    		
			    		totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
			    		totalMemoria.put(mesAnoCalculo+"_P"+nomeGrupo+nomeCalculo, totalPorcentagem);
			    		
			    		mostraPorcentagem = true;
			    	}
			    	else if(nomeCalculo.equals("Bonos - Exterior"))
			    	{
			    		conta2 = contas.get("0107020101");
			    		double valor1 = this.totalConta(conta2, c.getTime());
			    		
			    		total = valor1;
			    		
			    		double divisor = totalInversionesMap.get(c.getTimeInMillis());
			    		
			    		if(divisor > 0)
			    			totalPorcentagem = (valor1 *100) / divisor;
			    		
			    		totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
			    		totalMemoria.put(mesAnoCalculo+"_P"+nomeGrupo+nomeCalculo, totalPorcentagem);
			    		
			    		mostraPorcentagem = true;
			    	}
			    	else if(nomeCalculo.equals("EMITIDOS POR ENTIDADES DEL SECTOR FINANCIERO - Exterior"))
			    	{
			    		conta = cContas.get("0107020200");
			    		double valor1 = this.totalConta(conta, c.getTime());
			    		
			    		total = valor1;
			    		
			    		double divisor = totalInversionesMap.get(c.getTimeInMillis());
			    		
			    		if(divisor > 0)
			    			totalPorcentagem = (valor1 *100) / divisor;
			    		
			    		totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
			    		totalMemoria.put(mesAnoCalculo+"_P"+nomeGrupo+nomeCalculo, totalPorcentagem);
			    		
			    		mostraPorcentagem = true;
			    	}
			    	else if(nomeCalculo.equals("Certificados de Depósitos de Ahorro - Exterior"))
			    	{
			    		conta2 = contas.get("0107020201");
			    		double valor1 = this.totalConta(conta2, c.getTime());
			    		
			    		total = valor1;
			    		
			    		double divisor = totalInversionesMap.get(c.getTimeInMillis());
			    		
			    		if(divisor > 0)
			    			totalPorcentagem = (valor1 *100) / divisor;
			    		
			    		totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
			    		totalMemoria.put(mesAnoCalculo+"_P"+nomeGrupo+nomeCalculo, totalPorcentagem);
			    		
			    		mostraPorcentagem = true;
			    	}
			    	else if(nomeCalculo.equals("Títulos de Inversión - Exterior"))
			    	{
			    		conta2 = contas.get("0107020202");
			    		double valor1 = this.totalConta(conta2, c.getTime());
			    		
			    		total = valor1;
			    		
			    		double divisor = totalInversionesMap.get(c.getTimeInMillis());
			    		
			    		if(divisor > 0)
			    			totalPorcentagem = (valor1 *100) / divisor;
			    		
			    		totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
			    		totalMemoria.put(mesAnoCalculo+"_P"+nomeGrupo+nomeCalculo, totalPorcentagem);
			    		
			    		mostraPorcentagem = true;
			    	}
			    	else if(nomeCalculo.equals("EMITIDOS POR ENTIDADES DEL SECTOR PRIVADO - EXTERIOR"))
			    	{
			    		conta = cContas.get("0107020300");
			    		double valor1 = this.totalConta(conta, c.getTime());
			    		
			    		total = valor1;
			    		
			    		double divisor = totalInversionesMap.get(c.getTimeInMillis());
			    		
			    		if(divisor > 0)
			    			totalPorcentagem = (valor1 *100) / divisor;
			    		
			    		totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
			    		totalMemoria.put(mesAnoCalculo+"_P"+nomeGrupo+nomeCalculo, totalPorcentagem);
			    		
			    		mostraPorcentagem = true;
			    	}
			    	else if(nomeCalculo.equals("Títulos de Deuda - Sector Privado - Exterior"))
			    	{
			    		conta2 = contas.get("0107020301");
			    		double valor1 = this.totalConta(conta2, c.getTime());
			    		
			    		total = valor1;
			    		
			    		double divisor = totalInversionesMap.get(c.getTimeInMillis());
			    		
			    		if(divisor > 0)
			    			totalPorcentagem = (valor1 *100) / divisor;
			    		
			    		totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
			    		totalMemoria.put(mesAnoCalculo+"_P"+nomeGrupo+nomeCalculo, totalPorcentagem);
			    		
			    		mostraPorcentagem = true;
			    	}
			    	else if(nomeCalculo.equals("EMITIDOS POR EMPRESAS VINCULADAS - EXTERIOR"))
			    	{
			    		conta = cContas.get("0107020400");
			    		double valor1 = this.totalConta(conta, c.getTime());
			    		
			    		total = valor1;
			    		
			    		double divisor = totalInversionesMap.get(c.getTimeInMillis());
			    		
			    		if(divisor > 0)
			    			totalPorcentagem = (valor1 *100) / divisor;
			    		
			    		totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
			    		totalMemoria.put(mesAnoCalculo+"_P"+nomeGrupo+nomeCalculo, totalPorcentagem);
			    		
			    		mostraPorcentagem = true;
			    	}
			    	else if(nomeCalculo.equals("Títulos de Deuda - Empresas Vinculadas - Exterior"))
			    	{
			    		conta2 = contas.get("0107020401");
			    		double valor1 = this.totalConta(conta2, c.getTime());
			    		
			    		total = valor1;
			    		
			    		double divisor = totalInversionesMap.get(c.getTimeInMillis());
			    		
			    		if(divisor > 0)
			    			totalPorcentagem = (valor1 *100) / divisor;
			    		
			    		totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
			    		totalMemoria.put(mesAnoCalculo+"_P"+nomeGrupo+nomeCalculo, totalPorcentagem);
			    		
			    		mostraPorcentagem = true;
			    	}
			    	else if(nomeCalculo.equals("INVERSIONES ESPECIALES - sobre TÍTULOS DE RENTA FÍSICA - EXTERIOR"))
			    	{
			    		conta = cContas.get("0107020500");
			    		double valor1 = this.totalConta(conta, c.getTime());
			    		
			    		total = valor1;
			    		
			    		double divisor = totalInversionesMap.get(c.getTimeInMillis());
			    		
			    		if(divisor > 0)
			    			totalPorcentagem = (valor1 *100) / divisor;
			    		
			    		totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
			    		totalMemoria.put(mesAnoCalculo+"_P"+nomeGrupo+nomeCalculo, totalPorcentagem);
			    		
			    		mostraPorcentagem = true;
			    	}
			    	else if(nomeCalculo.equals("Inversiones Especiales - sobre Títulos de Renta Física cuenta 01 - Exterior"))
			    	{
			    		conta2 = contas.get("0107020501");
			    		double valor1 = this.totalConta(conta2, c.getTime());
			    		
			    		total = valor1;
			    		
			    		double divisor = totalInversionesMap.get(c.getTimeInMillis());
			    		
			    		if(divisor > 0)
			    			totalPorcentagem = (valor1 *100) / divisor;
			    		
			    		totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
			    		totalMemoria.put(mesAnoCalculo+"_P"+nomeGrupo+nomeCalculo, totalPorcentagem);
			    		
			    		mostraPorcentagem = true;
			    	}
			    	else if(nomeCalculo.equals("INTERESES DEVENGADOS S/RENTA FIJA - EXTERIOR"))
			    	{
			    		conta = cContas.get("0107024000");
			    		double valor1 = this.totalConta(conta, c.getTime());
			    		
			    		total = valor1;
			    		
			    		double divisor = totalInversionesMap.get(c.getTimeInMillis());
			    		
			    		if(divisor > 0)
			    			totalPorcentagem = (valor1 *100) / divisor;
			    		
			    		totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
			    		totalMemoria.put(mesAnoCalculo+"_P"+nomeGrupo+nomeCalculo, totalPorcentagem);
			    		
			    		mostraPorcentagem = true;
			    	}
			    	else if(nomeCalculo.equals("(PREVISIONES S/TÍTULOS VALORES DE RENTA FIJA)"))
			    	{
			    		conta = cContas.get("0107025000");
			    		double valor1 = this.totalConta(conta, c.getTime());
			    		
			    		total = valor1;
			    		
			    		double divisor = totalInversionesMap.get(c.getTimeInMillis());
			    		
			    		if(divisor > 0)
			    			totalPorcentagem = (valor1 *100) / divisor;
			    		
			    		totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
			    		totalMemoria.put(mesAnoCalculo+"_P"+nomeGrupo+nomeCalculo, totalPorcentagem);
			    		
			    		mostraPorcentagem = true;
			    	}
			    	else if(nomeCalculo.equals("TÍTULOS VALORES DE RENTA VARIABLE - LOCAL"))
			    	{
			    		conta = cContas.get("0107030000");
			    		double valor1 = this.totalConta(conta, c.getTime());
			    		
			    		total = valor1;
			    		
			    		double divisor = totalInversionesMap.get(c.getTimeInMillis());
			    		
			    		if(divisor > 0)
			    			totalPorcentagem = (valor1 *100) / divisor;
			    		
			    		totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
			    		totalMemoria.put(mesAnoCalculo+"_P"+nomeGrupo+nomeCalculo, totalPorcentagem);
			    		
			    		mostraPorcentagem = true;
			    	}
			    	else if(nomeCalculo.equals("EMITIDOS POR ENTIDADES DEL SECTOR FINANCIERO - LOCAL"))
			    	{
			    		conta = cContas.get("0107030100");
			    		double valor1 = this.totalConta(conta, c.getTime());
			    		
			    		total = valor1;
			    		
			    		double divisor = totalInversionesMap.get(c.getTimeInMillis());
			    		
			    		if(divisor > 0)
			    			totalPorcentagem = (valor1 *100) / divisor;
			    		
			    		totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
			    		totalMemoria.put(mesAnoCalculo+"_P"+nomeGrupo+nomeCalculo, totalPorcentagem);
			    		
			    		mostraPorcentagem = true;
			    	}
			    	else if(nomeCalculo.equals("Acciones - Sector Financiero - Local"))
			    	{
			    		conta2 = contas.get("0107030101");
			    		double valor1 = this.totalConta(conta2, c.getTime());
			    		
			    		total = valor1;
			    		
			    		double divisor = totalInversionesMap.get(c.getTimeInMillis());
			    		
			    		if(divisor > 0)
			    			totalPorcentagem = (valor1 *100) / divisor;
			    		
			    		totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
			    		totalMemoria.put(mesAnoCalculo+"_P"+nomeGrupo+nomeCalculo, totalPorcentagem);
			    		
			    		mostraPorcentagem = true;
			    	}
			    	else if(nomeCalculo.equals("EMITIDOS POR ENTIDADES DEL SECTOR PRIVADO - LOCAL"))
			    	{
			    		conta = cContas.get("0107030200");
			    		double valor1 = this.totalConta(conta, c.getTime());
			    		
			    		total = valor1;
			    		
			    		double divisor = totalInversionesMap.get(c.getTimeInMillis());
			    		
			    		if(divisor > 0)
			    			totalPorcentagem = (valor1 *100) / divisor;
			    		
			    		totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
			    		totalMemoria.put(mesAnoCalculo+"_P"+nomeGrupo+nomeCalculo, totalPorcentagem);
			    		
			    		mostraPorcentagem = true;
			    	}
			    	else if(nomeCalculo.equals("Acciones - Sector Privado - Local"))
			    	{
			    		conta2 = contas.get("0107030201");
			    		double valor1 = this.totalConta(conta2, c.getTime());
			    		
			    		total = valor1;
			    		
			    		double divisor = totalInversionesMap.get(c.getTimeInMillis());
			    		
			    		if(divisor > 0)
			    			totalPorcentagem = (valor1 *100) / divisor;
			    		
			    		totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
			    		totalMemoria.put(mesAnoCalculo+"_P"+nomeGrupo+nomeCalculo, totalPorcentagem);
			    		
			    		mostraPorcentagem = true;
			    	}
			    	else if(nomeCalculo.equals("EMITIDOS POR EMPRESAS VINCULADAS - LOCAL"))
			    	{
			    		conta = cContas.get("0107030300");
			    		double valor1 = this.totalConta(conta, c.getTime());
			    		
			    		total = valor1;
			    		
			    		double divisor = totalInversionesMap.get(c.getTimeInMillis());
			    		
			    		if(divisor > 0)
			    			totalPorcentagem = (valor1 *100) / divisor;
			    		
			    		totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
			    		totalMemoria.put(mesAnoCalculo+"_P"+nomeGrupo+nomeCalculo, totalPorcentagem);
			    		
			    		mostraPorcentagem = true;
			    	}
			    	else if(nomeCalculo.equals("Acciones - Empresas Vinculadas - Local"))
			    	{
			    		conta2 = contas.get("0107030301");
			    		double valor1 = this.totalConta(conta2, c.getTime());
			    		
			    		total = valor1;
			    		
			    		double divisor = totalInversionesMap.get(c.getTimeInMillis());
			    		
			    		if(divisor > 0)
			    			totalPorcentagem = (valor1 *100) / divisor;
			    		
			    		totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
			    		totalMemoria.put(mesAnoCalculo+"_P"+nomeGrupo+nomeCalculo, totalPorcentagem);
			    		
			    		mostraPorcentagem = true;
			    	}
			    	else if(nomeCalculo.equals("INVERSIONES ESPECIALES - RENTA VARIABLE"))
			    	{
			    		conta = cContas.get("0107030400");
			    		double valor1 = this.totalConta(conta, c.getTime());
			    		
			    		total = valor1;

			    		double divisor = totalInversionesMap.get(c.getTimeInMillis());
			    		
			    		if(divisor > 0)
			    			totalPorcentagem = (valor1 *100) / divisor;
			    		
			    		totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
			    		totalMemoria.put(mesAnoCalculo+"_P"+nomeGrupo+nomeCalculo, totalPorcentagem);
			    		
			    		mostraPorcentagem = true;
			    	}
			    	else if(nomeCalculo.equals("Inversiones Especiales - Renta Variable cuenta 01"))
			    	{
			    		conta2 = contas.get("0107030401");
			    		double valor1 = this.totalConta(conta2, c.getTime());
			    		
			    		total = valor1;
			    		
			    		double divisor = totalInversionesMap.get(c.getTimeInMillis());
			    		
			    		if(divisor > 0)
			    			totalPorcentagem = (valor1 *100) / divisor;
			    		
			    		totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
			    		totalMemoria.put(mesAnoCalculo+"_P"+nomeGrupo+nomeCalculo, totalPorcentagem);
			    		
			    		mostraPorcentagem = true;
			    	}
			    	else if(nomeCalculo.equals("DIVIDENDOS Y PARTICIPACIONES - RENTA VARIABLE - LOCAL"))
			    	{
			    		conta = cContas.get("0107034000");
			    		double valor1 = this.totalConta(conta, c.getTime());
			    		
			    		total = valor1;
			    		
			    		double divisor = totalInversionesMap.get(c.getTimeInMillis());
			    		
			    		if(divisor > 0)
			    			totalPorcentagem = (valor1 *100) / divisor;
			    		
			    		totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
			    		totalMemoria.put(mesAnoCalculo+"_P"+nomeGrupo+nomeCalculo, totalPorcentagem);
			    		
			    		mostraPorcentagem = true;
			    	}
			    	else if(nomeCalculo.equals("Dividendos por Cobrar Sector Financiero - Local"))
			    	{
			    		conta2 = contas.get("0107034001");
			    		double valor1 = this.totalConta(conta2, c.getTime());
			    		
			    		total = valor1;
			    		
			    		double divisor = totalInversionesMap.get(c.getTimeInMillis());
			    		
			    		if(divisor > 0)
			    			totalPorcentagem = (valor1 *100) / divisor;
			    		
			    		totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
			    		totalMemoria.put(mesAnoCalculo+"_P"+nomeGrupo+nomeCalculo, totalPorcentagem);
			    		
			    		mostraPorcentagem = true;
			    	}
			    	else if(nomeCalculo.equals("Dividendos por Cobrar Sector Privado - Local"))
			    	{
			    		conta2 = contas.get("0107034002");
			    		double valor1 = this.totalConta(conta2, c.getTime());
			    		
			    		total = valor1;
			    		
			    		double divisor = totalInversionesMap.get(c.getTimeInMillis());
			    		
			    		if(divisor > 0)
			    			totalPorcentagem = (valor1 *100) / divisor;
			    		
			    		totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
			    		totalMemoria.put(mesAnoCalculo+"_P"+nomeGrupo+nomeCalculo, totalPorcentagem);
			    		
			    		mostraPorcentagem = true;
			    	}
			    	else if(nomeCalculo.equals("Dividendos por Cobrar Empresas Relacionadas - Local"))
			    	{
			    		conta2 = contas.get("0107034003");
			    		double valor1 = this.totalConta(conta2, c.getTime());
			    		
			    		total = valor1;
			    		
			    		double divisor = totalInversionesMap.get(c.getTimeInMillis());
			    		
			    		if(divisor > 0)
			    			totalPorcentagem = (valor1 *100) / divisor;
			    		
			    		totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
			    		totalMemoria.put(mesAnoCalculo+"_P"+nomeGrupo+nomeCalculo, totalPorcentagem);
			    		
			    		mostraPorcentagem = true;
			    	}
			    	else if(nomeCalculo.equals("(PREVISIONES S/TÍTULOS VALORES DE RENTA VARIABLE) - LOCAL"))
			    	{
			    		conta = cContas.get("0107035000");
			    		double valor1 = this.totalConta(conta, c.getTime());
			    		
			    		total = valor1;
			    		
			    		double divisor = totalInversionesMap.get(c.getTimeInMillis());
			    		
			    		if(divisor > 0)
			    			totalPorcentagem = (valor1 *100) / divisor;
			    		
			    		totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
			    		totalMemoria.put(mesAnoCalculo+"_P"+nomeGrupo+nomeCalculo, totalPorcentagem);
			    		
			    		mostraPorcentagem = true;
			    	}
			    	else if(nomeCalculo.equals("TÍTULOS VALORES DE RENTA VARIABLE - EXTERIOR"))
			    	{
			    		conta = cContas.get("0107040000");
			    		double valor1 = this.totalConta(conta, c.getTime());
			    		
			    		total = valor1;
			    		
			    		double divisor = totalInversionesMap.get(c.getTimeInMillis());
			    		
			    		if(divisor > 0)
			    			totalPorcentagem = (valor1 *100) / divisor;
			    		
			    		totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
			    		totalMemoria.put(mesAnoCalculo+"_P"+nomeGrupo+nomeCalculo, totalPorcentagem);
			    		
			    		mostraPorcentagem = true;
			    	}
			    	else if(nomeCalculo.equals("EMITIDOS POR ENTIDADES DEL SECTOR FINANCIERO - EXTERIOR"))
			    	{
			    		conta = cContas.get("0107040100");
			    		double valor1 = this.totalConta(conta, c.getTime());
			    		
			    		total = valor1;
			    		
			    		double divisor = totalInversionesMap.get(c.getTimeInMillis());
			    		
			    		if(divisor > 0)
			    			totalPorcentagem = (valor1 *100) / divisor;
			    		
			    		totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
			    		totalMemoria.put(mesAnoCalculo+"_P"+nomeGrupo+nomeCalculo, totalPorcentagem);
			    		
			    		mostraPorcentagem = true;
			    	}
			    	else if(nomeCalculo.equals("Acciones - Sector Financiero - Exterior"))
			    	{
			    		conta2 = contas.get("0107040101");
			    		double valor1 = this.totalConta(conta2, c.getTime());
			    		
			    		total = valor1;
			    		
			    		double divisor = totalInversionesMap.get(c.getTimeInMillis());
			    		
			    		if(divisor > 0)
			    			totalPorcentagem = (valor1 *100) / divisor;
			    		
			    		totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
			    		totalMemoria.put(mesAnoCalculo+"_P"+nomeGrupo+nomeCalculo, totalPorcentagem);
			    		
			    		mostraPorcentagem = true;
			    	}
			    	else if(nomeCalculo.equals("EMITIDOS POR ENTIDADES DEL SECTOR PRIVADO - EXTERIOR"))
			    	{
			    		conta = cContas.get("0107040200");
			    		double valor1 = this.totalConta(conta, c.getTime());
			    		
			    		total = valor1;
			    		
			    		double divisor = totalInversionesMap.get(c.getTimeInMillis());
			    		
			    		if(divisor > 0)
			    			totalPorcentagem = (valor1 *100) / divisor;
			    		
			    		totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
			    		totalMemoria.put(mesAnoCalculo+"_P"+nomeGrupo+nomeCalculo, totalPorcentagem);
			    		
			    		mostraPorcentagem = true;
			    	}
			    	else if(nomeCalculo.equals("Acciones - Sector Privado - Exterior"))
			    	{
			    		conta2 = contas.get("0107040201");
			    		double valor1 = this.totalConta(conta2, c.getTime());
			    		
			    		total = valor1;
			    		
			    		double divisor = totalInversionesMap.get(c.getTimeInMillis());
			    		
			    		if(divisor > 0)
			    			totalPorcentagem = (valor1 *100) / divisor;
			    		
			    		totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
			    		totalMemoria.put(mesAnoCalculo+"_P"+nomeGrupo+nomeCalculo, totalPorcentagem);
			    		
			    		mostraPorcentagem = true;
			    	}
			    	else if(nomeCalculo.equals("EMITIDOS POR EMPRESAS VINCULADAS - EXTERIOR"))
			    	{
			    		conta = cContas.get("0107040300");
			    		double valor1 = this.totalConta(conta, c.getTime());
			    		
			    		total = valor1;
			    		
			    		double divisor = totalInversionesMap.get(c.getTimeInMillis());
			    		
			    		if(divisor > 0)
			    			totalPorcentagem = (valor1 *100) / divisor;
			    		
			    		totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
			    		totalMemoria.put(mesAnoCalculo+"_P"+nomeGrupo+nomeCalculo, totalPorcentagem);
			    		
			    		mostraPorcentagem = true;
			    	}
			    	else if(nomeCalculo.equals("Acciones - Empresas Vinculadas - Exterior"))
			    	{
			    		conta2 = contas.get("0107040301");
			    		double valor1 = this.totalConta(conta2, c.getTime());
			    		
			    		total = valor1;
			    		
			    		double divisor = totalInversionesMap.get(c.getTimeInMillis());
			    		
			    		if(divisor > 0)
			    			totalPorcentagem = (valor1 *100) / divisor;
			    		
			    		totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
			    		totalMemoria.put(mesAnoCalculo+"_P"+nomeGrupo+nomeCalculo, totalPorcentagem);
			    		
			    		mostraPorcentagem = true;
			    	}
			    	else if(nomeCalculo.equals("INVERSIONES ESPECIALES - RENTA VARIABLE - EXTERIOR"))
			    	{
			    		conta = cContas.get("0107040400");
			    		double valor1 = this.totalConta(conta, c.getTime());
			    		
			    		total = valor1;
			    		
			    		double divisor = totalInversionesMap.get(c.getTimeInMillis());
			    		
			    		if(divisor > 0)
			    			totalPorcentagem = (valor1 *100) / divisor;
			    		
			    		totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
			    		totalMemoria.put(mesAnoCalculo+"_P"+nomeGrupo+nomeCalculo, totalPorcentagem);
			    		
			    		mostraPorcentagem = true;
			    	}
			    	else if(nomeCalculo.equals("Inversiones Especiales - Renta Variable cuenta 01 - Exterior"))
			    	{
			    		conta2 = contas.get("0107040401");
			    		double valor1 = this.totalConta(conta2, c.getTime());
			    		
			    		total = valor1;
			    		
			    		double divisor = totalInversionesMap.get(c.getTimeInMillis());
			    		
			    		if(divisor > 0)
			    			totalPorcentagem = (valor1 *100) / divisor;
			    		
			    		totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
			    		totalMemoria.put(mesAnoCalculo+"_P"+nomeGrupo+nomeCalculo, totalPorcentagem);
			    		
			    		mostraPorcentagem = true;
			    	}
			    	else if(nomeCalculo.equals("DIVIDENDOS Y PARTICIPACIONES - RENTA VARIABLE - EXTERIOR"))
			    	{
			    		conta = cContas.get("0107044000");
			    		double valor1 = this.totalConta(conta, c.getTime());
			    		
			    		total = valor1;
			    		
			    		double divisor = totalInversionesMap.get(c.getTimeInMillis());
			    		
			    		if(divisor > 0)
			    			totalPorcentagem = (valor1 *100) / divisor;
			    		
			    		totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
			    		totalMemoria.put(mesAnoCalculo+"_P"+nomeGrupo+nomeCalculo, totalPorcentagem);
			    		
			    		mostraPorcentagem = true;
			    	}
			    	else if(nomeCalculo.equals("Dividendos por Cobrar Sector Financiero - Exterior"))
			    	{
			    		conta2 = contas.get("0107044001");
			    		double valor1 = this.totalConta(conta2, c.getTime());
			    		
			    		total = valor1;
			    		
			    		double divisor = totalInversionesMap.get(c.getTimeInMillis());
			    		
			    		if(divisor > 0)
			    			totalPorcentagem = (valor1 *100) / divisor;
			    		
			    		totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
			    		totalMemoria.put(mesAnoCalculo+"_P"+nomeGrupo+nomeCalculo, totalPorcentagem);
			    		
			    		mostraPorcentagem = true;
			    	}
			    	else if(nomeCalculo.equals("Dividendos por Cobrar Sector Privado - Exterior"))
			    	{
			    		conta2 = contas.get("0107044002");
			    		double valor1 = this.totalConta(conta2, c.getTime());
			    		
			    		total = valor1;
			    		
			    		double divisor = totalInversionesMap.get(c.getTimeInMillis());
			    		
			    		if(divisor > 0)
			    			totalPorcentagem = (valor1 *100) / divisor;
			    		
			    		totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
			    		totalMemoria.put(mesAnoCalculo+"_P"+nomeGrupo+nomeCalculo, totalPorcentagem);
			    		
			    		mostraPorcentagem = true;
			    	}
			    	else if(nomeCalculo.equals("Dividendos por Cobrar Empresas Relacionadas - Exterior"))
			    	{
			    		conta2 = contas.get("0107044003");
			    		double valor1 = this.totalConta(conta2, c.getTime());
			    		
			    		total = valor1;
			    		
			    		double divisor = totalInversionesMap.get(c.getTimeInMillis());
			    		
			    		if(divisor > 0)
			    			totalPorcentagem = (valor1 *100) / divisor;
			    		
			    		totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
			    		totalMemoria.put(mesAnoCalculo+"_P"+nomeGrupo+nomeCalculo, totalPorcentagem);
			    		
			    		mostraPorcentagem = true;
			    	}
			    	else if(nomeCalculo.equals("INVERSIONES POR PRÉSTAMOS"))
			    	{
			    		conta = cContas.get("0107050000");
			    		double valor1 = this.totalConta(conta, c.getTime());
			    		
			    		total = valor1;
			    		
			    		double divisor = totalInversionesMap.get(c.getTimeInMillis());
			    		
			    		if(divisor > 0)
			    			totalPorcentagem = (valor1 *100) / divisor;
			    		
			    		totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
			    		totalMemoria.put(mesAnoCalculo+"_P"+nomeGrupo+nomeCalculo, totalPorcentagem);
			    		
			    		mostraPorcentagem = true;
			    	}
			    	else if(nomeCalculo.equals("PRÉSTAMOS HIPOTECARIOS"))
			    	{
			    		conta = cContas.get("0107050100");
			    		double valor1 = this.totalConta(conta, c.getTime());
			    		
			    		total = valor1;
			    		
			    		double divisor = totalInversionesMap.get(c.getTimeInMillis());
			    		
			    		if(divisor > 0)
			    			totalPorcentagem = (valor1 *100) / divisor;
			    		
			    		totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
			    		totalMemoria.put(mesAnoCalculo+"_P"+nomeGrupo+nomeCalculo, totalPorcentagem);
			    		
			    		mostraPorcentagem = true;
			    	}
			    	else if(nomeCalculo.equals("Préstamos Hipotecarios a Directores y Personal Superior"))
			    	{
			    		conta2 = contas.get("0107050101");
			    		double valor1 = this.totalConta(conta2, c.getTime());
			    		
			    		total = valor1;
			    		
			    		double divisor = totalInversionesMap.get(c.getTimeInMillis());
			    		
			    		if(divisor > 0)
			    			totalPorcentagem = (valor1 *100) / divisor;
			    		
			    		totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
			    		totalMemoria.put(mesAnoCalculo+"_P"+nomeGrupo+nomeCalculo, totalPorcentagem);
			    		
			    		mostraPorcentagem = true;
			    	}
			    	else if(nomeCalculo.equals("Préstamos Hipotecarios a Empresas Vinculadas"))
			    	{
			    		conta2 = contas.get("0107050102");
			    		double valor1 = this.totalConta(conta2, c.getTime());
			    		
			    		total = valor1;
			    		
			    		double divisor = totalInversionesMap.get(c.getTimeInMillis());
			    		
			    		if(divisor > 0)
			    			totalPorcentagem = (valor1 *100) / divisor;
			    		
			    		totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
			    		totalMemoria.put(mesAnoCalculo+"_P"+nomeGrupo+nomeCalculo, totalPorcentagem);
			    		
			    		mostraPorcentagem = true;
			    	}
			    	else if(nomeCalculo.equals("Préstamos Hipotecarios al Personal"))
			    	{
			    		conta2 = contas.get("0107050103");
			    		double valor1 = this.totalConta(conta2, c.getTime());
			    		
			    		total = valor1;
			    		
			    		double divisor = totalInversionesMap.get(c.getTimeInMillis());
			    		
			    		if(divisor > 0)
			    			totalPorcentagem = (valor1 *100) / divisor;
			    		
			    		totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
			    		totalMemoria.put(mesAnoCalculo+"_P"+nomeGrupo+nomeCalculo, totalPorcentagem);
			    		
			    		mostraPorcentagem = true;
			    	}
			    	else if(nomeCalculo.equals("Préstamos Hipotecarios a Terceros"))
			    	{
			    		conta2 = contas.get("0107050104");
			    		double valor1 = this.totalConta(conta2, c.getTime());
			    		
			    		total = valor1;
			    		
			    		double divisor = totalInversionesMap.get(c.getTimeInMillis());
			    		
			    		if(divisor > 0)
			    			totalPorcentagem = (valor1 *100) / divisor;
			    		
			    		totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
			    		totalMemoria.put(mesAnoCalculo+"_P"+nomeGrupo+nomeCalculo, totalPorcentagem);
			    		
			    		mostraPorcentagem = true;
			    	}
			    	else if(nomeCalculo.equals("PRÉSTAMOS VIDA"))
			    	{
			    		conta = cContas.get("0107050200");
			    		double valor1 = this.totalConta(conta, c.getTime());
			    		
			    		total = valor1;
			    		
			    		double divisor = totalInversionesMap.get(c.getTimeInMillis());
			    		
			    		if(divisor > 0)
			    			totalPorcentagem = (valor1 *100) / divisor;
			    		
			    		totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
			    		totalMemoria.put(mesAnoCalculo+"_P"+nomeGrupo+nomeCalculo, totalPorcentagem);
			    		
			    		mostraPorcentagem = true;
			    	}
			    	else if(nomeCalculo.equals("Préstamos s/Pólizas Vida"))
			    	{
			    		conta2 = contas.get("0107050201");
			    		double valor1 = this.totalConta(conta2, c.getTime());
			    		
			    		total = valor1;
			    		
			    		double divisor = totalInversionesMap.get(c.getTimeInMillis());
			    		
			    		if(divisor > 0)
			    			totalPorcentagem = (valor1 *100) / divisor;
			    		
			    		totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
			    		totalMemoria.put(mesAnoCalculo+"_P"+nomeGrupo+nomeCalculo, totalPorcentagem);
			    		
			    		mostraPorcentagem = true;
			    	}
			    	else if(nomeCalculo.equals("Préstamos Automáticos s/ Pólizas Vida"))
			    	{
			    		conta2 = contas.get("0107050202");
			    		double valor1 = this.totalConta(conta2, c.getTime());
			    		
			    		total = valor1;
			    		
			    		double divisor = totalInversionesMap.get(c.getTimeInMillis());
			    		
			    		if(divisor > 0)
			    			totalPorcentagem = (valor1 *100) / divisor;
			    		
			    		totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
			    		totalMemoria.put(mesAnoCalculo+"_P"+nomeGrupo+nomeCalculo, totalPorcentagem);
			    		
			    		mostraPorcentagem = true;
			    	}
			    	else if(nomeCalculo.equals("PRÉSTAMOS POR VENTA DE BIENES"))
			    	{
			    		conta = cContas.get("0107050300");
			    		double valor1 = this.totalConta(conta, c.getTime());
			    		
			    		total = valor1;
			    		
			    		double divisor = totalInversionesMap.get(c.getTimeInMillis());
			    		
			    		if(divisor > 0)
			    			totalPorcentagem = (valor1 *100) / divisor;
			    		
			    		totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
			    		totalMemoria.put(mesAnoCalculo+"_P"+nomeGrupo+nomeCalculo, totalPorcentagem);
			    		
			    		mostraPorcentagem = true;
			    	}
			    	else if(nomeCalculo.equals("Deudores por Venta de Bienes Inmuebles"))
			    	{
			    		conta2 = contas.get("0107050301");
			    		double valor1 = this.totalConta(conta2, c.getTime());
			    		
			    		total = valor1;
			    		
			    		double divisor = totalInversionesMap.get(c.getTimeInMillis());
			    		
			    		if(divisor > 0)
			    			totalPorcentagem = (valor1 *100) / divisor;
			    		
			    		totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
			    		totalMemoria.put(mesAnoCalculo+"_P"+nomeGrupo+nomeCalculo, totalPorcentagem);
			    		
			    		mostraPorcentagem = true;
			    	}
			    	else if(nomeCalculo.equals("Deudores por Venta de Bienes Inmuebles - Entidades Relacionadas"))
			    	{
			    		conta2 = contas.get("0107050302");
			    		double valor1 = this.totalConta(conta2, c.getTime());
			    		
			    		total = valor1;
			    		
			    		double divisor = totalInversionesMap.get(c.getTimeInMillis());
			    		
			    		if(divisor > 0)
			    			totalPorcentagem = (valor1 *100) / divisor;
			    		
			    		totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
			    		totalMemoria.put(mesAnoCalculo+"_P"+nomeGrupo+nomeCalculo, totalPorcentagem);
			    		
			    		mostraPorcentagem = true;
			    	}
			    	else if(nomeCalculo.equals("INTERESES DEVENGADOS - PRÉSTAMOS HIPOTECARIOS"))
			    	{
			    		conta = cContas.get("0107054000");
			    		double valor1 = this.totalConta(conta, c.getTime());
			    		
			    		total = valor1;
			    		
			    		double divisor = totalInversionesMap.get(c.getTimeInMillis());
			    		
			    		if(divisor > 0)
			    			totalPorcentagem = (valor1 *100) / divisor;
			    		
			    		totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
			    		totalMemoria.put(mesAnoCalculo+"_P"+nomeGrupo+nomeCalculo, totalPorcentagem);
			    		
			    		mostraPorcentagem = true;
			    	}
			    	else if(nomeCalculo.equals("INTERESES DEVENGADOS - PRÉSTAMOS VIDA"))
			    	{
			    		conta = cContas.get("0107054200");
			    		double valor1 = this.totalConta(conta, c.getTime());
			    		
			    		total = valor1;
			    		
			    		double divisor = totalInversionesMap.get(c.getTimeInMillis());
			    		
			    		if(divisor > 0)
			    			totalPorcentagem = (valor1 *100) / divisor;
			    		
			    		totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
			    		totalMemoria.put(mesAnoCalculo+"_P"+nomeGrupo+nomeCalculo, totalPorcentagem);
			    		
			    		mostraPorcentagem = true;
			    	}
			    	else if(nomeCalculo.equals("INTERESES DEVENGADOS S/VENTA DE BIENES A PLAZO"))
			    	{
			    		conta = cContas.get("0107054400");
			    		double valor1 = this.totalConta(conta, c.getTime());
			    		
			    		total = valor1;
			    		
			    		double divisor = totalInversionesMap.get(c.getTimeInMillis());
			    		
			    		if(divisor > 0)
			    			totalPorcentagem = (valor1 *100) / divisor;
			    		
			    		totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
			    		totalMemoria.put(mesAnoCalculo+"_P"+nomeGrupo+nomeCalculo, totalPorcentagem);
			    		
			    		mostraPorcentagem = true;
			    	}
			    	else if(nomeCalculo.equals("INVERSIONES INMOBILIARIAS"))
			    	{
			    		conta = cContas.get("0107060000");
			    		double valor1 = this.totalConta(conta, c.getTime());
			    		
			    		total = valor1;
			    		
			    		double divisor = totalInversionesMap.get(c.getTimeInMillis());
			    		
			    		if(divisor > 0)
			    			totalPorcentagem = (valor1 *100) / divisor;
			    		
			    		totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
			    		totalMemoria.put(mesAnoCalculo+"_P"+nomeGrupo+nomeCalculo, totalPorcentagem);
			    		
			    		mostraPorcentagem = true;
			    	}
			    	else if(nomeCalculo.equals("TERRENOS"))
			    	{
			    		conta = cContas.get("0107060100");
			    		double valor1 = this.totalConta(conta, c.getTime());
			    		
			    		total = valor1;
			    		
			    		double divisor = totalInversionesMap.get(c.getTimeInMillis());
			    		
			    		if(divisor > 0)
			    			totalPorcentagem = (valor1 *100) / divisor;
			    		
			    		totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
			    		totalMemoria.put(mesAnoCalculo+"_P"+nomeGrupo+nomeCalculo, totalPorcentagem);
			    		
			    		mostraPorcentagem = true;
			    	}
			    	else if(nomeCalculo.equals("EDIFICIOS"))
			    	{
			    		conta = cContas.get("0107060200");
			    		double valor1 = this.totalConta(conta, c.getTime());
			    		
			    		total = valor1;
			    		
			    		double divisor = totalInversionesMap.get(c.getTimeInMillis());
			    		
			    		if(divisor > 0)
			    			totalPorcentagem = (valor1 *100) / divisor;
			    		
			    		totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
			    		totalMemoria.put(mesAnoCalculo+"_P"+nomeGrupo+nomeCalculo, totalPorcentagem);
			    		
			    		mostraPorcentagem = true;
			    	}
			    	else if(nomeCalculo.equals("CONSTRUCCIONES EN CURSO"))
			    	{
			    		conta = cContas.get("0107060300");
			    		double valor1 = this.totalConta(conta, c.getTime());
			    		
			    		total = valor1;
			    		
			    		double divisor = totalInversionesMap.get(c.getTimeInMillis());
			    		
			    		if(divisor > 0)
			    			totalPorcentagem = (valor1 *100) / divisor;
			    		
			    		totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
			    		totalMemoria.put(mesAnoCalculo+"_P"+nomeGrupo+nomeCalculo, totalPorcentagem);
			    		
			    		mostraPorcentagem = true;
			    	}
			    	else if(nomeCalculo.equals("ANTICIPOS PARA COMPRA DE INMUEBLES"))
			    	{
			    		conta = cContas.get("0107060400");
			    		double valor1 = this.totalConta(conta, c.getTime());
			    		
			    		total = valor1;
			    		
			    		double divisor = totalInversionesMap.get(c.getTimeInMillis());
			    		
			    		if(divisor > 0)
			    			totalPorcentagem = (valor1 *100) / divisor;
			    		
			    		totalMemoria.put(mesAnoCalculo+"_"+nomeGrupo+nomeCalculo, total);
			    		totalMemoria.put(mesAnoCalculo+"_P"+nomeGrupo+nomeCalculo, totalPorcentagem);
			    		
			    		mostraPorcentagem = true;
			    	}
			    	else if(nomeCalculo.equals("TOTAL") && nomeGrupo.equals("INVERSIONES"))
			    	{
			    		double valor1 = totalMemoria.get(mesAnoCalculo+"_"+nomeGrupo+"INVERSIONES");
			    		double valor2 = totalMemoria.get(mesAnoCalculo+"_"+nomeGrupo+"TÍTULOS VALORES DE RENTA FIJA - LOCAL");
			    		double valor3 = totalMemoria.get(mesAnoCalculo+"_"+nomeGrupo+"EMITIDOS POR EL ESTADO Y ENTIDADES PÚBLICAS");
			    		double valor4 = totalMemoria.get(mesAnoCalculo+"_"+nomeGrupo+"Bonos");
			    		double valor5 = totalMemoria.get(mesAnoCalculo+"_"+nomeGrupo+"Letras de Regulación Monetaria");
			    		double valor6 = totalMemoria.get(mesAnoCalculo+"_"+nomeGrupo+"EMITIDOS POR ENTIDADES DEL SECTOR FINANCIERO");
			    		double valor7 = totalMemoria.get(mesAnoCalculo+"_"+nomeGrupo+"Certificados de Depósitos de Ahorro");
			    		double valor8 = totalMemoria.get(mesAnoCalculo+"_"+nomeGrupo+"Títulos de Inversión");
			    		double valor9 = totalMemoria.get(mesAnoCalculo+"_"+nomeGrupo+"EMITIDOS POR ENTIDADES DEL SECTOR PRIVADO");
			    		double valor10 = totalMemoria.get(mesAnoCalculo+"_"+nomeGrupo+"Títulos de Deuda - Sector Privado");
			    		double valor11 = totalMemoria.get(mesAnoCalculo+"_"+nomeGrupo+"EMITIDOS POR EMPRESAS VINCULADAS");
			    		double valor12 = totalMemoria.get(mesAnoCalculo+"_"+nomeGrupo+"Títulos de Deuda - Empresas Vinculadas");
			    		double valor13 = totalMemoria.get(mesAnoCalculo+"_"+nomeGrupo+"DEPÓSITOS RESTRINGIDOS");
			    		double valor14 = totalMemoria.get(mesAnoCalculo+"_"+nomeGrupo+"Depósitos - Restringidos en Sistema Financiero");
			    		double valor15 = totalMemoria.get(mesAnoCalculo+"_"+nomeGrupo+"Depósitos - Embargados");
			    		double valor16 = totalMemoria.get(mesAnoCalculo+"_"+nomeGrupo+"INVERSIONES ESPECIALES - RENTA FIJA");
			    		double valor17 = totalMemoria.get(mesAnoCalculo+"_"+nomeGrupo+"Inversiones Especiales - Renta Fija cuenta 01");
			    		double valor18 = totalMemoria.get(mesAnoCalculo+"_"+nomeGrupo+"INTERESES DEVENGADOS S/RENTA FIJA");
			    		double valor19 = totalMemoria.get(mesAnoCalculo+"_"+nomeGrupo+"TÍTULOS VALORES DE RENTA FIJA - EXTERIOR");
			    		double valor20 = totalMemoria.get(mesAnoCalculo+"_"+nomeGrupo+"EMITIDOS POR EL ESTADO Y ENTIDADES PÚBLICAS - EXTERIOR");
			    		double valor21 = totalMemoria.get(mesAnoCalculo+"_"+nomeGrupo+"Bonos - Exterior");
			    		double valor22 = totalMemoria.get(mesAnoCalculo+"_"+nomeGrupo+"EMITIDOS POR ENTIDADES DEL SECTOR FINANCIERO - Exterior");
			    		double valor23 = totalMemoria.get(mesAnoCalculo+"_"+nomeGrupo+"Certificados de Depósitos de Ahorro - Exterior");
			    		double valor24 = totalMemoria.get(mesAnoCalculo+"_"+nomeGrupo+"Títulos de Inversión - Exterior");
			    		double valor25 = totalMemoria.get(mesAnoCalculo+"_"+nomeGrupo+"EMITIDOS POR ENTIDADES DEL SECTOR PRIVADO - EXTERIOR");
			    		double valor26 = totalMemoria.get(mesAnoCalculo+"_"+nomeGrupo+"Títulos de Deuda - Sector Privado - Exterior");
			    		double valor27 = totalMemoria.get(mesAnoCalculo+"_"+nomeGrupo+"EMITIDOS POR EMPRESAS VINCULADAS - EXTERIOR");
			    		double valor28 = totalMemoria.get(mesAnoCalculo+"_"+nomeGrupo+"Títulos de Deuda - Empresas Vinculadas - Exterior");
			    		double valor29 = totalMemoria.get(mesAnoCalculo+"_"+nomeGrupo+"INVERSIONES ESPECIALES - sobre TÍTULOS DE RENTA FÍSICA - EXTERIOR");
			    		double valor30 = totalMemoria.get(mesAnoCalculo+"_"+nomeGrupo+"Inversiones Especiales - sobre Títulos de Renta Física cuenta 01 - Exterior");
			    		double valor31 = totalMemoria.get(mesAnoCalculo+"_"+nomeGrupo+"INTERESES DEVENGADOS S/RENTA FIJA - EXTERIOR");
			    		double valor32 = totalMemoria.get(mesAnoCalculo+"_"+nomeGrupo+"(PREVISIONES S/TÍTULOS VALORES DE RENTA FIJA)");
			    		double valor33 = totalMemoria.get(mesAnoCalculo+"_"+nomeGrupo+"TÍTULOS VALORES DE RENTA VARIABLE - LOCAL");
			    		double valor34 = totalMemoria.get(mesAnoCalculo+"_"+nomeGrupo+"EMITIDOS POR ENTIDADES DEL SECTOR FINANCIERO - LOCAL");
			    		double valor35 = totalMemoria.get(mesAnoCalculo+"_"+nomeGrupo+"Acciones - Sector Financiero - Local");
			    		double valor36 = totalMemoria.get(mesAnoCalculo+"_"+nomeGrupo+"EMITIDOS POR ENTIDADES DEL SECTOR PRIVADO - LOCAL");
			    		double valor37 = totalMemoria.get(mesAnoCalculo+"_"+nomeGrupo+"Acciones - Sector Privado - Local");
			    		double valor38 = totalMemoria.get(mesAnoCalculo+"_"+nomeGrupo+"EMITIDOS POR EMPRESAS VINCULADAS - LOCAL");
			    		double valor39 = totalMemoria.get(mesAnoCalculo+"_"+nomeGrupo+"Acciones - Empresas Vinculadas - Local");
			    		double valor40 = totalMemoria.get(mesAnoCalculo+"_"+nomeGrupo+"INVERSIONES ESPECIALES - RENTA VARIABLE");
			    		double valor41 = totalMemoria.get(mesAnoCalculo+"_"+nomeGrupo+"Inversiones Especiales - Renta Variable cuenta 01");
			    		double valor42 = totalMemoria.get(mesAnoCalculo+"_"+nomeGrupo+"DIVIDENDOS Y PARTICIPACIONES - RENTA VARIABLE - LOCAL");
			    		double valor43 = totalMemoria.get(mesAnoCalculo+"_"+nomeGrupo+"Dividendos por Cobrar Sector Financiero - Local");
			    		double valor44 = totalMemoria.get(mesAnoCalculo+"_"+nomeGrupo+"Dividendos por Cobrar Sector Privado - Local");
			    		double valor45 = totalMemoria.get(mesAnoCalculo+"_"+nomeGrupo+"Dividendos por Cobrar Empresas Relacionadas - Local");
			    		double valor46 = totalMemoria.get(mesAnoCalculo+"_"+nomeGrupo+"(PREVISIONES S/TÍTULOS VALORES DE RENTA VARIABLE) - LOCAL");
			    		double valor47 = totalMemoria.get(mesAnoCalculo+"_"+nomeGrupo+"TÍTULOS VALORES DE RENTA VARIABLE - EXTERIOR");
			    		double valor48 = totalMemoria.get(mesAnoCalculo+"_"+nomeGrupo+"EMITIDOS POR ENTIDADES DEL SECTOR FINANCIERO - EXTERIOR");
			    		double valor49 = totalMemoria.get(mesAnoCalculo+"_"+nomeGrupo+"Acciones - Sector Financiero - Exterior");
			    		double valor50 = totalMemoria.get(mesAnoCalculo+"_"+nomeGrupo+"EMITIDOS POR ENTIDADES DEL SECTOR PRIVADO - EXTERIOR");
			    		double valor51 = totalMemoria.get(mesAnoCalculo+"_"+nomeGrupo+"Acciones - Sector Privado - Exterior");
			    		double valor52 = totalMemoria.get(mesAnoCalculo+"_"+nomeGrupo+"EMITIDOS POR EMPRESAS VINCULADAS - EXTERIOR");
			    		double valor53 = totalMemoria.get(mesAnoCalculo+"_"+nomeGrupo+"Acciones - Empresas Vinculadas - Exterior");
			    		double valor54 = totalMemoria.get(mesAnoCalculo+"_"+nomeGrupo+"INVERSIONES ESPECIALES - RENTA VARIABLE - EXTERIOR");
			    		double valor55 = totalMemoria.get(mesAnoCalculo+"_"+nomeGrupo+"Inversiones Especiales - Renta Variable cuenta 01 - Exterior");
			    		double valor56 = totalMemoria.get(mesAnoCalculo+"_"+nomeGrupo+"DIVIDENDOS Y PARTICIPACIONES - RENTA VARIABLE - EXTERIOR");
			    		double valor57 = totalMemoria.get(mesAnoCalculo+"_"+nomeGrupo+"Dividendos por Cobrar Sector Financiero - Exterior");
			    		double valor58 = totalMemoria.get(mesAnoCalculo+"_"+nomeGrupo+"Dividendos por Cobrar Sector Privado - Exterior");
			    		double valor59 = totalMemoria.get(mesAnoCalculo+"_"+nomeGrupo+"Dividendos por Cobrar Empresas Relacionadas - Exterior");
			    		double valor60 = totalMemoria.get(mesAnoCalculo+"_"+nomeGrupo+"INVERSIONES POR PRÉSTAMOS");
			    		double valor61 = totalMemoria.get(mesAnoCalculo+"_"+nomeGrupo+"PRÉSTAMOS HIPOTECARIOS");
			    		double valor62 = totalMemoria.get(mesAnoCalculo+"_"+nomeGrupo+"Préstamos Hipotecarios a Directores y Personal Superior");
			    		double valor63 = totalMemoria.get(mesAnoCalculo+"_"+nomeGrupo+"Préstamos Hipotecarios a Empresas Vinculadas");
			    		double valor64 = totalMemoria.get(mesAnoCalculo+"_"+nomeGrupo+"Préstamos Hipotecarios al Personal");
			    		double valor65 = totalMemoria.get(mesAnoCalculo+"_"+nomeGrupo+"Préstamos Hipotecarios a Terceros");
			    		double valor66 = totalMemoria.get(mesAnoCalculo+"_"+nomeGrupo+"PRÉSTAMOS VIDA");
			    		double valor67 = totalMemoria.get(mesAnoCalculo+"_"+nomeGrupo+"Préstamos s/Pólizas Vida");
			    		double valor68 = totalMemoria.get(mesAnoCalculo+"_"+nomeGrupo+"Préstamos Automáticos s/ Pólizas Vida");
			    		double valor69 = totalMemoria.get(mesAnoCalculo+"_"+nomeGrupo+"PRÉSTAMOS POR VENTA DE BIENES");
			    		double valor70 = totalMemoria.get(mesAnoCalculo+"_"+nomeGrupo+"Deudores por Venta de Bienes Inmuebles");
			    		double valor71 = totalMemoria.get(mesAnoCalculo+"_"+nomeGrupo+"Deudores por Venta de Bienes Inmuebles - Entidades Relacionadas");
			    		double valor72 = totalMemoria.get(mesAnoCalculo+"_"+nomeGrupo+"INTERESES DEVENGADOS - PRÉSTAMOS HIPOTECARIOS");
			    		double valor73 = totalMemoria.get(mesAnoCalculo+"_"+nomeGrupo+"INTERESES DEVENGADOS - PRÉSTAMOS VIDA");
			    		double valor74 = totalMemoria.get(mesAnoCalculo+"_"+nomeGrupo+"INTERESES DEVENGADOS S/VENTA DE BIENES A PLAZO");
			    		double valor75 = totalMemoria.get(mesAnoCalculo+"_"+nomeGrupo+"INVERSIONES INMOBILIARIAS");
			    		double valor76 = totalMemoria.get(mesAnoCalculo+"_"+nomeGrupo+"TERRENOS");
			    		double valor77 = totalMemoria.get(mesAnoCalculo+"_"+nomeGrupo+"EDIFICIOS");
			    		double valor78 = totalMemoria.get(mesAnoCalculo+"_"+nomeGrupo+"CONSTRUCCIONES EN CURSO");
			    		double valor79 = totalMemoria.get(mesAnoCalculo+"_"+nomeGrupo+"ANTICIPOS PARA COMPRA DE INMUEBLES");
			    		
			    		total = valor1 + valor2 + valor3 + valor4 + valor5 + valor6 + valor7 + valor8 + valor9 + valor10 + valor11 + valor12 + valor13 + valor14 + valor15 + valor16 + valor17 + valor18
			    				+ valor19 + valor20 + valor21 + valor22 + valor23 + valor24 + valor25 + valor26 + valor27 + valor28 + valor29 + valor30 + valor31 + valor32 + valor33 + valor34 + valor35 + valor36
			    				+ valor37 + valor38 + valor39 + valor40 + valor41 + valor42 + valor43 + valor44 + valor45 + valor46 + valor47 + valor48 + valor49 + valor50 + valor51 + valor52 + valor53 + valor54
			    				+ valor55 + valor56 + valor57 + valor58 + valor59 + valor60 + valor61 + valor62 + valor63 + valor64 + valor65 + valor66 + valor67 + valor68 + valor69 + valor70 + valor71 + valor72
			    				+ valor73 + valor74 + valor75 + valor76 + valor77 + valor78 + valor79;
			    		
			    		valor1 = totalMemoria.get(mesAnoCalculo+"_P"+nomeGrupo+"INVERSIONES");
			    		valor2 = totalMemoria.get(mesAnoCalculo+"_P"+nomeGrupo+"TÍTULOS VALORES DE RENTA FIJA - LOCAL");
			    		valor3 = totalMemoria.get(mesAnoCalculo+"_P"+nomeGrupo+"EMITIDOS POR EL ESTADO Y ENTIDADES PÚBLICAS");
			    		valor4 = totalMemoria.get(mesAnoCalculo+"_P"+nomeGrupo+"Bonos");
			    		valor5 = totalMemoria.get(mesAnoCalculo+"_P"+nomeGrupo+"Letras de Regulación Monetaria");
			    		valor6 = totalMemoria.get(mesAnoCalculo+"_P"+nomeGrupo+"EMITIDOS POR ENTIDADES DEL SECTOR FINANCIERO");
			    		valor7 = totalMemoria.get(mesAnoCalculo+"_P"+nomeGrupo+"Certificados de Depósitos de Ahorro");
			    		valor8 = totalMemoria.get(mesAnoCalculo+"_P"+nomeGrupo+"Títulos de Inversión");
			    		valor9 = totalMemoria.get(mesAnoCalculo+"_P"+nomeGrupo+"EMITIDOS POR ENTIDADES DEL SECTOR PRIVADO");
			    		valor10 = totalMemoria.get(mesAnoCalculo+"_P"+nomeGrupo+"Títulos de Deuda - Sector Privado");
			    		valor11 = totalMemoria.get(mesAnoCalculo+"_P"+nomeGrupo+"EMITIDOS POR EMPRESAS VINCULADAS");
			    		valor12 = totalMemoria.get(mesAnoCalculo+"_P"+nomeGrupo+"Títulos de Deuda - Empresas Vinculadas");
			    		valor13 = totalMemoria.get(mesAnoCalculo+"_P"+nomeGrupo+"DEPÓSITOS RESTRINGIDOS");
			    		valor14 = totalMemoria.get(mesAnoCalculo+"_P"+nomeGrupo+"Depósitos - Restringidos en Sistema Financiero");
			    		valor15 = totalMemoria.get(mesAnoCalculo+"_P"+nomeGrupo+"Depósitos - Embargados");
			    		valor16 = totalMemoria.get(mesAnoCalculo+"_P"+nomeGrupo+"INVERSIONES ESPECIALES - RENTA FIJA");
			    		valor17 = totalMemoria.get(mesAnoCalculo+"_P"+nomeGrupo+"Inversiones Especiales - Renta Fija cuenta 01");
			    		valor18 = totalMemoria.get(mesAnoCalculo+"_P"+nomeGrupo+"INTERESES DEVENGADOS S/RENTA FIJA");
			    		valor19 = totalMemoria.get(mesAnoCalculo+"_P"+nomeGrupo+"TÍTULOS VALORES DE RENTA FIJA - EXTERIOR");
			    		valor20 = totalMemoria.get(mesAnoCalculo+"_P"+nomeGrupo+"EMITIDOS POR EL ESTADO Y ENTIDADES PÚBLICAS - EXTERIOR");
			    		valor21 = totalMemoria.get(mesAnoCalculo+"_P"+nomeGrupo+"Bonos - Exterior");
			    		valor22 = totalMemoria.get(mesAnoCalculo+"_P"+nomeGrupo+"EMITIDOS POR ENTIDADES DEL SECTOR FINANCIERO - Exterior");
			    		valor23 = totalMemoria.get(mesAnoCalculo+"_P"+nomeGrupo+"Certificados de Depósitos de Ahorro - Exterior");
			    		valor24 = totalMemoria.get(mesAnoCalculo+"_P"+nomeGrupo+"Títulos de Inversión - Exterior");
			    		valor25 = totalMemoria.get(mesAnoCalculo+"_P"+nomeGrupo+"EMITIDOS POR ENTIDADES DEL SECTOR PRIVADO - EXTERIOR");
			    		valor26 = totalMemoria.get(mesAnoCalculo+"_P"+nomeGrupo+"Títulos de Deuda - Sector Privado - Exterior");
			    		valor27 = totalMemoria.get(mesAnoCalculo+"_P"+nomeGrupo+"EMITIDOS POR EMPRESAS VINCULADAS - EXTERIOR");
			    		valor28 = totalMemoria.get(mesAnoCalculo+"_P"+nomeGrupo+"Títulos de Deuda - Empresas Vinculadas - Exterior");
			    		valor29 = totalMemoria.get(mesAnoCalculo+"_P"+nomeGrupo+"INVERSIONES ESPECIALES - sobre TÍTULOS DE RENTA FÍSICA - EXTERIOR");
			    		valor30 = totalMemoria.get(mesAnoCalculo+"_P"+nomeGrupo+"Inversiones Especiales - sobre Títulos de Renta Física cuenta 01 - Exterior");
			    		valor31 = totalMemoria.get(mesAnoCalculo+"_P"+nomeGrupo+"INTERESES DEVENGADOS S/RENTA FIJA - EXTERIOR");
			    		valor32 = totalMemoria.get(mesAnoCalculo+"_P"+nomeGrupo+"(PREVISIONES S/TÍTULOS VALORES DE RENTA FIJA)");
			    		valor33 = totalMemoria.get(mesAnoCalculo+"_P"+nomeGrupo+"TÍTULOS VALORES DE RENTA VARIABLE - LOCAL");
			    		valor34 = totalMemoria.get(mesAnoCalculo+"_P"+nomeGrupo+"EMITIDOS POR ENTIDADES DEL SECTOR FINANCIERO - LOCAL");
			    		valor35 = totalMemoria.get(mesAnoCalculo+"_P"+nomeGrupo+"Acciones - Sector Financiero - Local");
			    		valor36 = totalMemoria.get(mesAnoCalculo+"_P"+nomeGrupo+"EMITIDOS POR ENTIDADES DEL SECTOR PRIVADO - LOCAL");
			    		valor37 = totalMemoria.get(mesAnoCalculo+"_P"+nomeGrupo+"Acciones - Sector Privado - Local");
			    		valor38 = totalMemoria.get(mesAnoCalculo+"_P"+nomeGrupo+"EMITIDOS POR EMPRESAS VINCULADAS - LOCAL");
			    		valor39 = totalMemoria.get(mesAnoCalculo+"_P"+nomeGrupo+"Acciones - Empresas Vinculadas - Local");
			    		valor40 = totalMemoria.get(mesAnoCalculo+"_P"+nomeGrupo+"INVERSIONES ESPECIALES - RENTA VARIABLE");
			    		valor41 = totalMemoria.get(mesAnoCalculo+"_P"+nomeGrupo+"Inversiones Especiales - Renta Variable cuenta 01");
			    		valor42 = totalMemoria.get(mesAnoCalculo+"_P"+nomeGrupo+"DIVIDENDOS Y PARTICIPACIONES - RENTA VARIABLE - LOCAL");
			    		valor43 = totalMemoria.get(mesAnoCalculo+"_P"+nomeGrupo+"Dividendos por Cobrar Sector Financiero - Local");
			    		valor44 = totalMemoria.get(mesAnoCalculo+"_P"+nomeGrupo+"Dividendos por Cobrar Sector Privado - Local");
			    		valor45 = totalMemoria.get(mesAnoCalculo+"_P"+nomeGrupo+"Dividendos por Cobrar Empresas Relacionadas - Local");
			    		valor46 = totalMemoria.get(mesAnoCalculo+"_P"+nomeGrupo+"(PREVISIONES S/TÍTULOS VALORES DE RENTA VARIABLE) - LOCAL");
			    		valor47 = totalMemoria.get(mesAnoCalculo+"_P"+nomeGrupo+"TÍTULOS VALORES DE RENTA VARIABLE - EXTERIOR");
			    		valor48 = totalMemoria.get(mesAnoCalculo+"_P"+nomeGrupo+"EMITIDOS POR ENTIDADES DEL SECTOR FINANCIERO - EXTERIOR");
			    		valor49 = totalMemoria.get(mesAnoCalculo+"_P"+nomeGrupo+"Acciones - Sector Financiero - Exterior");
			    		valor50 = totalMemoria.get(mesAnoCalculo+"_P"+nomeGrupo+"EMITIDOS POR ENTIDADES DEL SECTOR PRIVADO - EXTERIOR");
			    		valor51 = totalMemoria.get(mesAnoCalculo+"_P"+nomeGrupo+"Acciones - Sector Privado - Exterior");
			    		valor52 = totalMemoria.get(mesAnoCalculo+"_P"+nomeGrupo+"EMITIDOS POR EMPRESAS VINCULADAS - EXTERIOR");
			    		valor53 = totalMemoria.get(mesAnoCalculo+"_P"+nomeGrupo+"Acciones - Empresas Vinculadas - Exterior");
			    		valor54 = totalMemoria.get(mesAnoCalculo+"_P"+nomeGrupo+"INVERSIONES ESPECIALES - RENTA VARIABLE - EXTERIOR");
			    		valor55 = totalMemoria.get(mesAnoCalculo+"_P"+nomeGrupo+"Inversiones Especiales - Renta Variable cuenta 01 - Exterior");
			    		valor56 = totalMemoria.get(mesAnoCalculo+"_P"+nomeGrupo+"DIVIDENDOS Y PARTICIPACIONES - RENTA VARIABLE - EXTERIOR");
			    		valor57 = totalMemoria.get(mesAnoCalculo+"_P"+nomeGrupo+"Dividendos por Cobrar Sector Financiero - Exterior");
			    		valor58 = totalMemoria.get(mesAnoCalculo+"_P"+nomeGrupo+"Dividendos por Cobrar Sector Privado - Exterior");
			    		valor59 = totalMemoria.get(mesAnoCalculo+"_P"+nomeGrupo+"Dividendos por Cobrar Empresas Relacionadas - Exterior");
			    		valor60 = totalMemoria.get(mesAnoCalculo+"_P"+nomeGrupo+"INVERSIONES POR PRÉSTAMOS");
			    		valor61 = totalMemoria.get(mesAnoCalculo+"_P"+nomeGrupo+"PRÉSTAMOS HIPOTECARIOS");
			    		valor62 = totalMemoria.get(mesAnoCalculo+"_P"+nomeGrupo+"Préstamos Hipotecarios a Directores y Personal Superior");
			    		valor63 = totalMemoria.get(mesAnoCalculo+"_P"+nomeGrupo+"Préstamos Hipotecarios a Empresas Vinculadas");
			    		valor64 = totalMemoria.get(mesAnoCalculo+"_P"+nomeGrupo+"Préstamos Hipotecarios al Personal");
			    		valor65 = totalMemoria.get(mesAnoCalculo+"_P"+nomeGrupo+"Préstamos Hipotecarios a Terceros");
			    		valor66 = totalMemoria.get(mesAnoCalculo+"_P"+nomeGrupo+"PRÉSTAMOS VIDA");
			    		valor67 = totalMemoria.get(mesAnoCalculo+"_P"+nomeGrupo+"Préstamos s/Pólizas Vida");
			    		valor68 = totalMemoria.get(mesAnoCalculo+"_P"+nomeGrupo+"Préstamos Automáticos s/ Pólizas Vida");
			    		valor69 = totalMemoria.get(mesAnoCalculo+"_P"+nomeGrupo+"PRÉSTAMOS POR VENTA DE BIENES");
			    		valor70 = totalMemoria.get(mesAnoCalculo+"_P"+nomeGrupo+"Deudores por Venta de Bienes Inmuebles");
			    		valor71 = totalMemoria.get(mesAnoCalculo+"_P"+nomeGrupo+"Deudores por Venta de Bienes Inmuebles - Entidades Relacionadas");
			    		valor72 = totalMemoria.get(mesAnoCalculo+"_P"+nomeGrupo+"INTERESES DEVENGADOS - PRÉSTAMOS HIPOTECARIOS");
			    		valor73 = totalMemoria.get(mesAnoCalculo+"_P"+nomeGrupo+"INTERESES DEVENGADOS - PRÉSTAMOS VIDA");
			    		valor74 = totalMemoria.get(mesAnoCalculo+"_P"+nomeGrupo+"INTERESES DEVENGADOS S/VENTA DE BIENES A PLAZO");
			    		valor75 = totalMemoria.get(mesAnoCalculo+"_P"+nomeGrupo+"INVERSIONES INMOBILIARIAS");
			    		valor76 = totalMemoria.get(mesAnoCalculo+"_P"+nomeGrupo+"TERRENOS");
			    		valor77 = totalMemoria.get(mesAnoCalculo+"_P"+nomeGrupo+"EDIFICIOS");
			    		valor78 = totalMemoria.get(mesAnoCalculo+"_P"+nomeGrupo+"CONSTRUCCIONES EN CURSO");
			    		valor79 = totalMemoria.get(mesAnoCalculo+"_P"+nomeGrupo+"ANTICIPOS PARA COMPRA DE INMUEBLES");
			    		
			    		totalPorcentagem = valor1 + valor2 + valor3 + valor4 + valor5 + valor6 + valor7 + valor8 + valor9 + valor10 + valor11 + valor12 + valor13 + valor14 + valor15 + valor16 + valor17 + valor18
			    				+ valor19 + valor20 + valor21 + valor22 + valor23 + valor24 + valor25 + valor26 + valor27 + valor28 + valor29 + valor30 + valor31 + valor32 + valor33 + valor34 + valor35 + valor36
			    				+ valor37 + valor38 + valor39 + valor40 + valor41 + valor42 + valor43 + valor44 + valor45 + valor46 + valor47 + valor48 + valor49 + valor50 + valor51 + valor52 + valor53 + valor54
			    				+ valor55 + valor56 + valor57 + valor58 + valor59 + valor60 + valor61 + valor62 + valor63 + valor64 + valor65 + valor66 + valor67 + valor68 + valor69 + valor70 + valor71 + valor72
			    				+ valor73 + valor74 + valor75 + valor76 + valor77 + valor78 + valor79;
			    		
			    		mostraPorcentagem = true;
			    	}
			    	
			    	if((nomeCalculo.equals("Vida") && !nomeGrupo.equals("Composición de la cartera técnica (Primas directas)")) || nomeCalculo.equals("Primas reaseguros aceptados vida - local") || nomeCalculo.equals("Primas reaseguros aceptados patrimonial - local") || nomeCalculo.equals("Primas reaseguros aceptados vida - exterior") || nomeCalculo.equals("Primas reaseguros aceptados patrimonial - exterior"))
			    	{
			    		celula = row.createCell(coluna);
						celula.setCellValue("No disponible");
						celula.setCellStyle(estiloTexto);
			    	}
			    	else if(nomeCalculo.equals("Agentes de Seguros") || nomeCalculo.equals("Aseguradoras") || nomeCalculo.equals("Reaseguradoras") || nomeCalculo.equals("Intermediarios de seguros") || nomeCalculo.equals("Intermediario de reaseguros") || nomeCalculo.equals("Empresas de auditoria externa") || nomeCalculo.equals("Grupos coaseguradoras") || nomeCalculo.equals("Grupos coaseguradoras") || (nomeCalculo.equals("TOTAL") && nomeGrupo.equals("K")))
			    	{
				    	celula = row.createCell(coluna);
			    		celula.setCellValue(new Double(total).intValue());
			    		celula.setCellStyle(estiloTextoD);
			    	}
			    	else
			    	{
			    		//Inibir o -0,00
			    		if(formataValor.format(total).equals("-0,00"))
			    			total = 0;
			    			
				    	celula = row.createCell(coluna);
						celula.setCellValue(formataValor.format(total));
						if(nomeCalculo.startsWith("TOTAL"))
							celula.setCellStyle(estiloTextoN_D);
						else
							celula.setCellStyle(estiloTextoD);
						
						if(mostraPorcentagem)
						{
							celula = row.createCell(++coluna);
							celula.setCellValue(formataValor.format(totalPorcentagem));
							if(nomeCalculo.startsWith("TOTAL"))
								celula.setCellStyle(estiloTextoN_D);
							else
								celula.setCellStyle(estiloTextoD);
						}
			    	}
			    	
					if(dataInicio!=null)
						c.add(Calendar.MONTH, 1);
					else
						c.add(Calendar.YEAR, 1);
				}
				 
				linha++;
	    	}
	    }
	    
	    this.calcularOutros("Magnitud relativa de las aseguradoras", cContas.get("0300000000"));
	    this.calcularOutros("Participacion en la Produccion del Mercado", cContas.get("0401000000"));
	    
	    
	    wb.write(stream);
		stream.flush();
		stream.close();
	}
	
	private void calcularOutros(String titulo, ClassificacaoContas conta) throws Exception
	{
		int coluna = 0;
		Calendar c = Calendar.getInstance();
		String mes,ano,mesAnoCalculo;
		
    	HSSFRow row = planilha.createRow(linha);
    	HSSFCell celula = row.createCell(coluna);
   		celula.setCellValue(titulo);
		celula.setCellStyle(estiloTituloTabela);
		coluna = 1;
		for(int j = 0 ; j < meses.length ; j++)
		{
			celula = row.createCell(coluna);
			celula.setCellValue("Valores " + meses[j]);
			celula.setCellStyle(estiloTituloTabelaC);
			
			celula = row.createCell(++coluna);
			celula.setCellValue(" Porcentaje " + meses[j]);
			celula.setCellStyle(estiloTituloTabelaC);
			
			coluna++;
		}
		
		linha++;
		
		Map<String,BigDecimal> totalMap = new TreeMap<String, BigDecimal>();
		Map<String,BigDecimal> totalPorcentagemMap = new TreeMap<String, BigDecimal>();
		Map<String,BigDecimal> divisorMap = new TreeMap<String, BigDecimal>();
		BigDecimal divisor = new BigDecimal("0.00");
		
		c.setTime(dataInicioReal);
		while(c.getTime().compareTo(dataFimReal)<=0)
		{
			mes = new SimpleDateFormat("MM").format(c.getTime());
			ano = new SimpleDateFormat("yyyy").format(c.getTime());
			mesAnoCalculo = mes+ano;
			
			BigDecimal total = new BigDecimal("0.00");
			
			for(Entidade aseguradora : aseguradoras)
			{
				BigDecimal valor1 = conta.obterTotalizacaoExistenteBIG(aseguradora, mesAnoCalculo);
				total = total.add(valor1);
			}
			
			divisorMap.put(mesAnoCalculo, total);
			
			if(dataInicio!=null)
				c.add(Calendar.MONTH, 1);
			else
				c.add(Calendar.YEAR, 1);
		}
		
		for(Entidade aseguradora : aseguradoras)
		{
			row = planilha.createRow(linha);
        	celula = row.createCell(0);
       		celula.setCellValue(aseguradora.obterNome());
    		celula.setCellStyle(estiloTexto_E);
    		coluna = 1;
    		 
    		BigDecimal total = new BigDecimal("0.00");
    		BigDecimal totalPorcentagem = new BigDecimal("0.00");
    		
    		c.setTime(dataInicioReal);
    		while(c.getTime().compareTo(dataFimReal)<=0)
			{
    			mes = new SimpleDateFormat("MM").format(c.getTime());
    			ano = new SimpleDateFormat("yyyy").format(c.getTime());
    			mesAnoCalculo = mes+ano;
    			
    			BigDecimal valor1 = conta.obterTotalizacaoExistenteBIG(aseguradora, mesAnoCalculo);
    			divisor = divisorMap.get(mesAnoCalculo);
    			
    			total = valor1;
    			
    			if(divisor.doubleValue() > 0)
    			{
    				//System.out.println("valor1 = " + valor1);
    				//System.out.println("divisor = " + divisor);
    				
    				totalPorcentagem = valor1.multiply(new BigDecimal("100.00"));
    				totalPorcentagem = totalPorcentagem.divide(divisor, 4, RoundingMode.HALF_EVEN);
    				
    				//System.out.println("totalPorcentagem = " + totalPorcentagem);
    			}
    			
    			celula = row.createCell(coluna);
    			celula.setCellValue(formataValor.format(total));
				celula.setCellStyle(estiloTextoD);
				
				celula = row.createCell(++coluna);
    			celula.setCellValue(formataValor.format(totalPorcentagem));
				celula.setCellStyle(estiloTextoD);
				
				if(totalMap.containsKey(mesAnoCalculo))
				{
					BigDecimal v = totalMap.get(mesAnoCalculo);
					v = v.add(total);
					
					totalMap.put(mesAnoCalculo, v);
				}
				else
					totalMap.put(mesAnoCalculo, total);
				
				if(totalPorcentagemMap.containsKey(mesAnoCalculo))
				{
					BigDecimal v = totalPorcentagemMap.get(mesAnoCalculo);
					v = v.add(totalPorcentagem);
					
					totalPorcentagemMap.put(mesAnoCalculo, v);
				}
				else
					totalPorcentagemMap.put(mesAnoCalculo, totalPorcentagem);
				 
				if(dataInicio!=null)
					c.add(Calendar.MONTH, 1);
				else
					c.add(Calendar.YEAR, 1);
				 
				coluna++;
			}
    		
    		linha++;
		}
		
		row = planilha.createRow(linha);
    	celula = row.createCell(0);
   		celula.setCellValue("TOTAL");
		celula.setCellStyle(estiloTextoN_E);
		coluna = 1;
		
		BigDecimal total = new BigDecimal("0.00");
		BigDecimal totalPorcentagem = new BigDecimal("0.00");
		
		c.setTime(dataInicioReal);
		while(c.getTime().compareTo(dataFimReal)<=0)
		{
			mes = new SimpleDateFormat("MM").format(c.getTime());
			ano = new SimpleDateFormat("yyyy").format(c.getTime());
			mesAnoCalculo = mes+ano;
			
			total = totalMap.get(mesAnoCalculo);
			totalPorcentagem = totalPorcentagemMap.get(mesAnoCalculo);
			
			celula = row.createCell(coluna);
			celula.setCellValue(formataValor.format(total));
			celula.setCellStyle(estiloTextoN_D);
			
			celula = row.createCell(++coluna);
			celula.setCellValue(formataValor.format(totalPorcentagem));
			celula.setCellStyle(estiloTextoN_D);
			
			if(dataInicio!=null)
				c.add(Calendar.MONTH, 1);
			else
				c.add(Calendar.YEAR, 1);
			 
			coluna++;
		}
		
		linha++;
	}
	
	private void instanciarContas() throws Exception
	{
		this.cContas = new TreeMap<String, ClassificacaoContas>();
		this.contas = new TreeMap<String, Conta>();
		
		String[] contas = {"0401000000","0402000000","0403000000","0401010100","0401010200","0401010300","0401010400","0401010500","0401010600","0401010700","0401010800","0401010900","0401011000","0401011100","0401011200","0401011300",
				"0402000000","0403000000","0402010200","0402022000","0403012000","0403022000","0401010100","0401010200","0401010300","0401010400","0401010500","0401010600","0401010700","0401010800","0401010900","0401011000","0401011100",
				"0401011200","0401011300","0401012000","0506010100","0508010100","0508020100","0506010200","0508010000","0508020200","0506010400","0508010300","0508020300","0506010400","0508010400","0508020400","0506010500","0508010500",
				"0508020500","0506010600","0508010600","0508020600","0506010700","0508010700","0508020700","0506010800","0508010800","0508020800","0506010900","0508010900","0508020900","0506011000","0508011000","0508021000","0506011100",
				"0508011100","0508021100","0506011200","0508011200","0508021200","0506011300","0508011300","0508021300","0407010100","0407010200","0407010300","0407010400","0407010500","0407010600","0407010700","0407010800","0407010900",
				"0407011000","0407011100","0407011200","0407011300","0506010100","0508010100","0508020100","0401010100","0506010200","0508010000","0508020200","0401010200","0506010400","0508010300","0508020300","0401010300","0506010400",
				"0508010400","0508020400","0401010400","0506010500","0508010500","0508020500","0401010500","0506010600","0508010600","0508020600","0401010600","0506010700","0508010700","0508020700","0401010700","0506010800","0508010800",
				"0508020800","0401010800","0506010900","0508010900","0508020900","0401010900","0506011000","0508011000","0508021000","0401011000","0506011100","0508011100","0508021100","0401011100","0506011200","0508011200","0508021200",
				"0401011200","0506011300","0508011300","0508021300","0401011300","0504010000","0506000000","0507000000","0508000000","0407000000","0525000000","0502000000","0300000000","0506012000","0501012000","0506012000","0508012000",
				"0212010000","0212020000","0212030000","0212040000","0109030100","0109040100","0109050100","0109060100","0212050100","0212050200","0213000000","0107010000","0107010100","0107020000","0107020100","0107030000","0107040000",
				"0107060000","0107010100","0107020100","0107050000","0401020000","0501000000","0502000000","0301000000","0302000000","0303000000","0304000000","0305000000","0107000000","0107010200","0107010300","0107010400","0107010500",
				"0107010600","0107014000","0107020200","0107020300","0107020400","0107020500","0107024000","0107025000","0107030100","0107030200","0107030300","0107030400","0107034000","0107035000","0107040100","0107040200",
				"0107040300","0107040400","0107044000","0107050000","0107050100","0107050200","0107050300","0107054000","0107054200","0107054400","0107060000","0107060100","0107060200","0107060300","0107060400"
		};
		
		ClassificacaoContas cContas;
		for(int i = 0 ; i < contas.length ; i++)
		{
			//System.out.println(contas[i].trim());
			
			cContas = (ClassificacaoContas) this.home.obterEntidadePorApelido(contas[i].trim());
			this.cContas.put(contas[i].trim(), cContas);
		}
		
		String[] contas2 = {"0401012001","0401012009","0401012003","0401012006","0401012002","0401012004","0401012005","0401012007","0402012002","0402012004","0402012005","0402012007","0401012008","0401022005","0107010101","0107050202",
				"0107040201","0107010501","0107010502","0107040301","0107050301","0107050302","0107040401","0107044001","0107044003","0107044002","0107050101","0107030401","0107020202","0107020301","0107030301","0107020201","0107010201",
				"0107010102","0107020101","0107030201","0107030101","0107040101","0107020501","0107034003","0107050102","0107050201","0107010202","0107010301","0107010401","0107010601","0107034002","0107034001","0107050103","0107050104",
				"0107020401"
				};
		
		Conta conta;
		for(int i = 0 ; i < contas2.length ; i++)
		{
			conta = (Conta) this.home.obterEntidadePorApelido(contas2[i].trim());
			this.contas.put(contas2[i].trim(), conta);
		}
	}
	
	private double totalConta(Entidade entidade, Date data) throws Exception
	{
		double total = 0;
		
		if(calculoAnual)
		{
			if(entidade instanceof ClassificacaoContas)
			{
				ClassificacaoContas cContas = (ClassificacaoContas) entidade;
				if(anoFiscal)
					total = cContas.obterTotalizacaoExistenteAnualFiscalBIG(aseguradoras, Integer.valueOf(new SimpleDateFormat("yyyy").format(data))).doubleValue();
				else
					total=cContas.obterTotalizacaoExistenteAnual(aseguradoras, Integer.valueOf(new SimpleDateFormat("yyyy").format(data)));
			}
			else
			{
				Conta conta = (Conta) entidade;
				if(anoFiscal)
					total = conta.obterTotalizacaoExistenteAnualFiscalBIG(aseguradoras, Integer.valueOf(new SimpleDateFormat("yyyy").format(data))).doubleValue();
				else
					total=conta.obterTotalizacaoExistenteAnual(aseguradoras, Integer.valueOf(new SimpleDateFormat("yyyy").format(data)));
			}
		}
		else
		{
			if(entidade instanceof ClassificacaoContas)
			{
				ClassificacaoContas cContas = (ClassificacaoContas) entidade;
				
				total=cContas.obterTotalizacaoExistente(aseguradoras, new SimpleDateFormat("MMyyyy").format(data));
			}
			else
			{
				Conta conta = (Conta) entidade;
				
				total=conta.obterTotalizacaoExistente(aseguradoras, new SimpleDateFormat("MMyyyy").format(data));
			}
		}
		
		return total;
	}
}
