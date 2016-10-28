package com.gvs.crm.report;

import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
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

public class RelInformacaoMercadoXLS extends Excel
{
	private Map<String,ClassificacaoContas> cContas;
	private Map<String,Conta> contas;
	private  EntidadeHome home;
	private boolean calculoAnual;
	private Collection<Entidade> aseguradoras;
	
	public RelInformacaoMercadoXLS(Date dataInicio, Date dataFim, int anoInicio, int anoFim, EntidadeHome home) throws Exception
	{
		String caminho = "C:/tmp/" + new Date().getTime() + ".xls";
		this.home = home;
		
		FileOutputStream stream = new FileOutputStream(caminho);
		
		this.setCaminho(caminho);
		
		SimpleDateFormat formataData = new SimpleDateFormat("MM/yyyy");
		SimpleDateFormat formataDataAno = new SimpleDateFormat("yyyy");
		DecimalFormat formataValor = new DecimalFormat("#,##0.00");
		Date dataInicioReal,dataFimReal;
		
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
        
        HSSFCellStyle estiloTexto = wb.createCellStyle();
        estiloTexto.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        estiloTexto.setFont(fonteTexto);
        
        HSSFCellStyle estiloTextoN = wb.createCellStyle();
        estiloTextoN.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        estiloTextoN.setFont(fonteTituloN);
        
        HSSFCellStyle estiloTextoN_E = wb.createCellStyle();
        estiloTextoN_E.setFont(fonteTituloN);
        
        HSSFCellStyle estiloTexto_E = wb.createCellStyle();
        estiloTexto_E.setFont(fonteTexto);
        
        HSSFCellStyle estiloTitulo_E = wb.createCellStyle();
        estiloTitulo_E.setFont(fonteTitulo);
        
        HSSFFont fonteTituloTabela = wb.createFont();
	    fonteTituloTabela.setFontHeightInPoints((short)10);
	    fonteTituloTabela.setFontName("Arial");
	    fonteTituloTabela.setColor(HSSFColor.WHITE.index);
	    fonteTituloTabela.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        
        HSSFCellStyle estiloTituloTabelaC = wb.createCellStyle();
	    estiloTituloTabelaC.setAlignment(HSSFCellStyle.ALIGN_CENTER);
	    estiloTituloTabelaC.setFont(fonteTituloTabela);
	    estiloTituloTabelaC.setFillForegroundColor(HSSFColor.GREY_80_PERCENT.index);
	    estiloTituloTabelaC.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
	    
	    HSSFCellStyle estiloTituloTabela = wb.createCellStyle();
	    estiloTituloTabela.setFont(fonteTituloTabela);
	    estiloTituloTabela.setFillForegroundColor(HSSFColor.GREY_80_PERCENT.index);
	    estiloTituloTabela.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        
        HSSFSheet planilha = wb.createSheet("Plan");
		
		HSSFRow row;
		HSSFCell celula;
		Calendar c, c3Anos;
	    String mes,ano,mesTresAnosAnterior,mesAnoCalculo;
	    ClassificacaoContas conta;
	    Conta conta2;
	    Date data3Anos;
	    
	    int linha = 0;
		int coluna = 0;
		
		row = planilha.createRow(linha);
		celula = row.createCell(coluna);
		celula.setCellValue("INFORMACIÓN AGREGADA DEL MERCADO");
		celula.setCellStyle(estiloTitulo_E);
		Region r = new Region(linha, (short)0, linha, (short)50);
		planilha.addMergedRegion(r);
		    
		linha+=2;
		int contCalculo = 1;
		
		Map<Integer,String> calculos = new TreeMap<Integer,String>();
	    calculos.put(1,"Primas de seguros directos");
	    calculos.put(2,"Primas reaseguros aceptados - Local");
	    calculos.put(3,"Primas reaseguros aceptados - Exterior");
	    calculos.put(4,"Primas de reaseguros aceptados");
	    calculos.put(5,"Primas de seguros rama vida - sin reservas matematicas");
	    calculos.put(6,"Primas de seguros rama vida -  sin reservas matematicas - reaseguros aceptados");
	    calculos.put(7,"Primas de seguros rama vida - con reservas matematicas");
	    calculos.put(8,"Primas de seguros rama vida - con reservas matematicas - reaseguros aceptados");
	    calculos.put(9,"Primas de seguros directos rama no vida");
	    calculos.put(10,"Primas de seguros directos rama no vida - reaseguros aceptados");
	    
	    calculos.put(11,"Incendios");
	    calculos.put(12,"Transportes");
	    calculos.put(13,"Accidentes personales");
	    calculos.put(14,"Automóviles");
	    calculos.put(15,"Accidentes a pasajeros");
	    calculos.put(16,"Robo y asalto");
	    calculos.put(17,"Cristales, vidrios y espejos");
	    calculos.put(18,"Agropecuario");
	    calculos.put(19,"Riesgos varios");
	    calculos.put(20,"Responsabilidad civil");
	    calculos.put(21,"Aeronavegación");
	    calculos.put(22,"Riesgos técnicos");
	    calculos.put(23,"Caución");
	    calculos.put(24,"Vida");
	    
	    calculos.put(25,"Incendios");
	    calculos.put(26,"Transportes");
	    calculos.put(27,"Accidentes personales");
	    calculos.put(28,"Automóviles");
	    calculos.put(29,"Accidentes a pasajeros");
	    calculos.put(30,"Robo y asalto");
	    calculos.put(31,"Cristales, vidrios y espejos");
	    calculos.put(32,"Agropecuario");
	    calculos.put(33,"Riesgos varios");
	    calculos.put(34,"Responsabilidad civil");
	    calculos.put(35,"Aeronavegación");
	    calculos.put(36,"Riesgos técnicos");
	    calculos.put(37,"Caución");
	    calculos.put(38,"Vida");
	    
	    calculos.put(39,"Incendios");
	    calculos.put(40,"Transportes");
	    calculos.put(41,"Accidentes personales");
	    calculos.put(42,"Automóviles");
	    calculos.put(43,"Accidentes a pasajeros");
	    calculos.put(44,"Robo y asalto");
	    calculos.put(45,"Cristales, vidrios y espejos");
	    calculos.put(46,"Agropecuario");
	    calculos.put(47,"Riesgos varios");
	    calculos.put(48,"Responsabilidad civil");
	    calculos.put(49,"Aeronavegación");
	    calculos.put(50,"Riesgos técnicos");
	    calculos.put(51,"Caución");
	    calculos.put(52,"Vida");
	    
	    calculos.put(53,"Incendios");
	    calculos.put(54,"Transportes");
	    calculos.put(55,"Accidentes personales");
	    calculos.put(56,"Automóviles");
	    calculos.put(57,"Accidentes a pasajeros");
	    calculos.put(58,"Robo y asalto");
	    calculos.put(59,"Cristales, vidrios y espejos");
	    calculos.put(60,"Agropecuario");
	    calculos.put(61,"Riesgos varios");
	    calculos.put(62,"Responsabilidad civil");
	    calculos.put(63,"Aeronavegación");
	    calculos.put(64,"Riesgos técnicos");
	    calculos.put(65,"Caución");
	    calculos.put(66,"Vida");
	    
	    calculos.put(67,"Comisiones de seguros directos");
	    calculos.put(68,"Tasa media de intermediación");
	    calculos.put(69,"Siniestros pagados netos de recuperos de siniestros ");
	    calculos.put(70,"Gastos de gestión");
	    calculos.put(71,"Margen de utilidad técnica");
	    calculos.put(72,"Primas de reaseguros cedidas al exterior");
	    //calculos.put(73,"Patrimonio Neto");
	    calculos.put(74,"Siniestralidad media rama vida");
	    calculos.put(75,"Siniestralidad media rama patrimonial");
	    
	    calculos.put(76,"Provisiones riesgo en curso");
	    calculos.put(77,"Reservas matemáticas");
	    calculos.put(78,"Fondos de acumulación");
	    calculos.put(79,"Provisiones de siniestros");
	    
	    calculos.put(80,"Renta fija - Sector privado");
	    calculos.put(81,"Renta variable");
	    calculos.put(82,"Inversiones inmobiliarias");
	    calculos.put(83,"Titulos públicos");
	    calculos.put(84,"Préstamos");
	    calculos.put(85,"TOTAL");
	    
	    calculos.put(86,"Agentes de Seguros");
	    calculos.put(87,"Aseguradoras");
	    calculos.put(88,"Reaseguradoras");
	    calculos.put(89,"Intermediarios de seguros");
	    calculos.put(90,"Intermediario de reaseguros");
	    calculos.put(91,"Empresas de auditoria externa");
	    calculos.put(92,"Grupos coaseguradoras");
	    calculos.put(93,"TOTAL");
	    
	    calculos.put(94,"Prima seccion vida individual corto plazo");
	    calculos.put(95,"Prima seccion vida seguros colectivos");
	    calculos.put(96,"Prima seccion vida desgravamiento hipotecario");
	    calculos.put(97,"Prima seccion vida defuncion o sepelio");
	    
	    calculos.put(98,"Primas seccion vida individual -  largo plazo");
	    calculos.put(99,"Primas seccion seguros colectivos - largo plazo");
	    calculos.put(100,"Primas seccion pensiones voluntarias");
	    calculos.put(101,"Primas seccion desgravamen hipotecario - largo plazo");
	    
	    calculos.put(102,"Incendios");
	    calculos.put(103,"Transportes");
	    calculos.put(104,"Accidentes personales");
	    calculos.put(105,"Automóviles");
	    calculos.put(106,"Accidentes a pasajeros");
	    calculos.put(107,"Robo y asalto");
	    calculos.put(108,"Cristales, vidrios y espejos");
	    calculos.put(109,"Agropecuario");
	    calculos.put(110,"Riesgos varios");
	    calculos.put(111,"Responsabilidad civil");
	    calculos.put(112,"Aeronavegación");
	    calculos.put(113,"Riesgos técnicos");
	    calculos.put(114,"Caución");
	    calculos.put(115,"Primas directas renovadas");
	    calculos.put(116,"Primas seccion salud o enfermedad");
	    calculos.put(117,"Primas renovadadas salud o enfermedad largo plazo");
	    
	    calculos.put(118,"Primas reaseguros aceptados  - local");
	    calculos.put(119,"Primas reaseguros aceptados - exterior");
	    calculos.put(120,"Primas reaseguros aceptados vida - local");
	    calculos.put(121,"Primas reaseguros aceptados patrimonial - local");
	    calculos.put(122,"Primas reaseguros aceptados vida - exterior");
	    calculos.put(123,"Primas reaseguros aceptados patrimonial - exterior");
	    
	    calculos.put(124,"Primas directas automoviles");
	    calculos.put(125,"Primas seccion vida individual - corto plazo");
	    calculos.put(126,"Primas seccion vida colectiva - corto plazo");
	    calculos.put(127,"Primas seccion desgravamen hipotecario - corto plazo");
	    calculos.put(128,"Primas seccion defuncion o sepelio");
	    calculos.put(129,"Incendios");
	    calculos.put(130,"Seguros agrarios");
	    calculos.put(131,"Riesgos varios -cristales vidrios y espejos");
	    calculos.put(132,"Riesgos varios - riesgos varios");
	    calculos.put(133,"Riesgos tecnicos");
	    calculos.put(134,"Transportes");
	    calculos.put(135,"Caucion");
	    calculos.put(136,"Robo y asalto");
	    calculos.put(137,"Responsabilidad civil");
	    calculos.put(138,"Accidentes personales");
	    calculos.put(139,"Accidentes pasajeros");
	    calculos.put(140,"Aeronavegacion");
	    calculos.put(141,"Vida largo plazo seccion vida individual largo plazo");
	    calculos.put(142,"Primas seccion seguros colectivos - largo plazo");
	    calculos.put(143,"Primas seccion pensiones voluntarias");
	    calculos.put(144,"Primas seccion desgravamen hipotercario");
	    calculos.put(145,"Seguro de salud");
	    
	    Map<Integer,String> calculosNovos = new TreeMap<Integer, String>();
	    calculosNovos.put(146, "Primas Reaseguros Cedidos");
	    
	    
	    row = planilha.createRow(linha);
	    celula = row.createCell(0);
		celula.setCellValue("");
		celula.setCellStyle(estiloTituloTabelaC);
		
		coluna=1;
		int cont = 0;
		
		c = Calendar.getInstance();
	    c.setTime(dataInicioReal);
	    
	    while(c.getTime().compareTo(dataFimReal)<=0)
		{
			celula = row.createCell(coluna);
			if(dataInicio!=null)
				celula.setCellValue(formataData.format(c.getTime()));
			else
				celula.setCellValue(formataDataAno.format(c.getTime()));
			celula.setCellStyle(estiloTituloTabelaC);
			
			coluna++;
			cont++;
			if(dataInicio!=null)
				c.add(Calendar.MONTH, 1);
			else
				c.add(Calendar.YEAR, 1);
		}
	    
	    String[] meses = new String[cont];
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
	    
	    for(Iterator<Integer> i = calculos.keySet().iterator() ; i.hasNext() ;  )
    	{
	    	int calculoId = i.next();
	    	
	    	//System.out.println(calculoId);
	    	
	    	String nomeCalculo = calculos.get(calculoId);
	    	
	    	coluna = 0;
	    	
	    	if(calculoId == 11)
        	{
        		row = planilha.createRow(linha);
            	
            	celula = row.createCell(coluna);
        		celula.setCellValue("Composición de la cartera técnica (Primas directas)");
        		celula.setCellStyle(estiloTituloTabela);
        	}
	    	else if(calculoId == 25)
        	{
        		row = planilha.createRow(linha);
            	
            	celula = row.createCell(coluna);
        		celula.setCellValue("Composición de la cartera técnica (Siniestros pagados bruto)");
        		celula.setCellStyle(estiloTituloTabela);
        	}
	    	else if(calculoId == 39)
        	{
        		row = planilha.createRow(linha);
            	
            	celula = row.createCell(coluna);
        		celula.setCellValue("Compocisión de la cartera técnica (Recupero de siniestros)");
        		celula.setCellStyle(estiloTituloTabela);
        	}
	    	else if(calculoId == 53)
        	{
        		row = planilha.createRow(linha);
            	
            	celula = row.createCell(coluna);
        		celula.setCellValue("Composición de la cartera técnica (Siniestralidad)");
        		celula.setCellStyle(estiloTituloTabela);
        	}
	    	else if(calculoId == 67)
        	{
        		row = planilha.createRow(linha);
            	
            	celula = row.createCell(coluna);
        		celula.setCellValue("");
        		celula.setCellStyle(estiloTituloTabela);
        	}
	    	else if(calculoId == 69)
        	{
        		row = planilha.createRow(linha);
            	
            	celula = row.createCell(coluna);
        		celula.setCellValue("Esquema de utilización de primas");
        		celula.setCellStyle(estiloTituloTabela);
        	}
	    	else if(calculoId == 72)
        	{
        		row = planilha.createRow(linha);
            	
            	celula = row.createCell(coluna);
        		celula.setCellValue("");
        		celula.setCellStyle(estiloTituloTabela);
        	}
	    	else if(calculoId == 76)
        	{
        		row = planilha.createRow(linha);
            	
            	celula = row.createCell(coluna);
        		celula.setCellValue("Composición de las provisiones y reservas técnicas");
        		celula.setCellStyle(estiloTituloTabela);
        	}
	    	else if(calculoId == 80)
        	{
        		row = planilha.createRow(linha);
            	
            	celula = row.createCell(coluna);
        		celula.setCellValue("Componentes de los activos reportados como representativos de las provisiones y reservas técnicas (Guaranies Corrientes)");
        		celula.setCellStyle(estiloTituloTabela);
        	}
	    	else if(calculoId == 86)
        	{
        		row = planilha.createRow(linha);
            	
            	celula = row.createCell(coluna);
        		celula.setCellValue("");
        		celula.setCellStyle(estiloTituloTabela);
        	}
	    	else if(calculoId == 94)
        	{
        		row = planilha.createRow(linha);
            	
            	celula = row.createCell(coluna);
        		celula.setCellValue("Primas de seguros rama vida - sin reservas matematicas (seguros directos)");
        		celula.setCellStyle(estiloTituloTabela);
        	}
	    	else if(calculoId == 98)
        	{
        		row = planilha.createRow(linha);
            	
            	celula = row.createCell(coluna);
        		celula.setCellValue("Primas de seguros rama vida - con reservas matematicas (seguros directos)");
        		celula.setCellStyle(estiloTituloTabela);
        	}
	    	else if(calculoId == 102)
        	{
        		row = planilha.createRow(linha);
            	
            	celula = row.createCell(coluna);
        		celula.setCellValue("Primas de seguros rama patrimonial (seguros directos)");
        		celula.setCellStyle(estiloTituloTabela);
        	}
	    	else if(calculoId == 118)
        	{
        		row = planilha.createRow(linha);
            	
            	celula = row.createCell(coluna);
        		celula.setCellValue("Reaseguros aceptados");
        		celula.setCellStyle(estiloTituloTabela);
        	}
	    	else if(calculoId == 124)
        	{
        		row = planilha.createRow(linha);
            	
            	celula = row.createCell(coluna);
        		celula.setCellValue("Composición de la cartera técnica (prima de seguro directo)");
        		celula.setCellStyle(estiloTituloTabela);
        	}
	    	
	    	if(calculoId == 11 || calculoId == 25 || calculoId == 39 || calculoId == 53 || calculoId == 67 || calculoId == 69 || calculoId == 72 || calculoId == 76 || calculoId == 80 || calculoId == 86 || calculoId == 94 || calculoId == 98 || calculoId == 102 || calculoId == 118 || calculoId == 124)
        	{
        		coluna = 1;
        		
        		for(int j = 0 ; j < meses.length ; j++)
        		{
        			celula = row.createCell(coluna);
        			celula.setCellValue(meses[j]);
        			celula.setCellStyle(estiloTituloTabelaC);
        			
        			coluna++;
        		}
        			
        		linha++;
        		coluna = 0;
        	}
	    	
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
    			
		    	if(calculoId == 1)
		    	{
		    		conta = cContas.get("0401000000");
		    		total = this.totalConta(conta, c.getTime());
		    		
		    		totalMemoria.put(mesAnoCalculo+"_"+calculoId, total);
		    	}
		    	else if(calculoId == 2)
		    	{
		    		conta = cContas.get("0402000000");
		    		total = this.totalConta(conta, c.getTime());
		    		
		    		totalMemoria.put(mesAnoCalculo+"_"+calculoId, total);
		    	}
		    	else if(calculoId == 3)
		    	{
		    		conta = cContas.get("0403000000");
		    		total = this.totalConta(conta, c.getTime());
		    		
		    		totalMemoria.put(mesAnoCalculo+"_"+calculoId, total);
		    	}
		    	else if(calculoId == 4)
		    	{
		    		double valor1 = totalMemoria.get(mesAnoCalculo+"_"+2);
		    		double valor2 = totalMemoria.get(mesAnoCalculo+"_"+3);
		    		
		    		total = valor1 + valor2;
		    		
		    		totalMemoria.put(mesAnoCalculo+"_"+calculoId, total);
		    	}
		    	else if(calculoId == 5)
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
		    		
		    		totalMemoria.put(mesAnoCalculo+"_"+calculoId, total);
		    	}
		    	else if(calculoId == 6)
		    	{
		    		conta2 = contas.get("0401012001");
		    		double valor1 = this.totalConta(conta2, c.getTime());
		    		conta2 = contas.get("0401012003");
		    		double valor2 = this.totalConta(conta2, c.getTime());
		    		conta2 = contas.get("0401012006");
		    		double valor3 = this.totalConta(conta2, c.getTime());
		    		
		    		total = valor1 + valor2 + valor3;
		    		
		    		totalMemoria.put(mesAnoCalculo+"_"+calculoId, total);
		    	}
		    	else if(calculoId == 7)
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
		    		
		    		totalMemoria.put(mesAnoCalculo+"_"+calculoId, total);
		    	}
		    	else if(calculoId == 8)
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
		    		
		    		totalMemoria.put(mesAnoCalculo+"_"+calculoId, total);
		    	}
		    	else if(calculoId == 9)
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
		    		
		    		totalMemoria.put(mesAnoCalculo+"_"+calculoId, total);
		    	}
		    	else if(calculoId == 10)
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
		    		
		    		totalMemoria.put(mesAnoCalculo+"_"+calculoId, total);
		    	}
		    	else if(calculoId == 11)
		    	{
		    		conta = cContas.get("0401010100");
		    		double valor1 = this.totalConta(conta, c.getTime());
		    		
		    		total = valor1;
		    		
		    		totalMemoria.put(mesAnoCalculo+"_"+calculoId, total);
		    	}
		    	else if(calculoId == 12)
		    	{
		    		conta = cContas.get("0401010200");
		    		double valor1 = this.totalConta(conta, c.getTime());
		    		
		    		total = valor1;
		    		
		    		totalMemoria.put(mesAnoCalculo+"_"+calculoId, total);
		    	}
		    	else if(calculoId == 13)
		    	{
		    		conta = cContas.get("0401010300");
		    		double valor1 = this.totalConta(conta, c.getTime());
		    		
		    		total = valor1;
		    		
		    		totalMemoria.put(mesAnoCalculo+"_"+calculoId, total);
		    	}
		    	else if(calculoId == 14)
		    	{
		    		conta = cContas.get("0401010400");
		    		double valor1 = this.totalConta(conta, c.getTime());
		    		
		    		total = valor1;
		    		
		    		totalMemoria.put(mesAnoCalculo+"_"+calculoId, total);
		    	}
		    	else if(calculoId == 15)
		    	{
		    		conta = cContas.get("0401010500");
		    		double valor1 = this.totalConta(conta, c.getTime());
		    		
		    		total = valor1;
		    		
		    		totalMemoria.put(mesAnoCalculo+"_"+calculoId, total);
		    	}
		    	else if(calculoId == 16)
		    	{
		    		conta = cContas.get("0401010600");
		    		double valor1 = this.totalConta(conta, c.getTime());
		    		
		    		total = valor1;
		    		
		    		totalMemoria.put(mesAnoCalculo+"_"+calculoId, total);
		    	}
		    	else if(calculoId == 17)
		    	{
		    		conta = cContas.get("0401010700");
		    		double valor1 = this.totalConta(conta, c.getTime());
		    		
		    		total = valor1;
		    		
		    		totalMemoria.put(mesAnoCalculo+"_"+calculoId, total);
		    	}
		    	else if(calculoId == 18)
		    	{
		    		conta = cContas.get("0401010800");
		    		double valor1 = this.totalConta(conta, c.getTime());
		    		
		    		total = valor1;
		    		
		    		totalMemoria.put(mesAnoCalculo+"_"+calculoId, total);
		    	}
		    	else if(calculoId == 19)
		    	{
		    		conta = cContas.get("0401010900");
		    		double valor1 = this.totalConta(conta, c.getTime());
		    		
		    		total = valor1;
		    		
		    		totalMemoria.put(mesAnoCalculo+"_"+calculoId, total);
		    	}
		    	else if(calculoId == 20)
		    	{
		    		conta = cContas.get("0401011000");
		    		double valor1 = this.totalConta(conta, c.getTime());
		    		
		    		total = valor1;
		    		
		    		totalMemoria.put(mesAnoCalculo+"_"+calculoId, total);
		    	}
		    	else if(calculoId == 21)
		    	{
		    		conta = cContas.get("0401011100");
		    		double valor1 = this.totalConta(conta, c.getTime());
		    		
		    		total = valor1;
		    		
		    		totalMemoria.put(mesAnoCalculo+"_"+calculoId, total);
		    	}
		    	else if(calculoId == 22)
		    	{
		    		conta = cContas.get("0401011200");
		    		double valor1 = this.totalConta(conta, c.getTime());
		    		
		    		total = valor1;
		    		
		    		totalMemoria.put(mesAnoCalculo+"_"+calculoId, total);
		    	}
		    	else if(calculoId == 23)
		    	{
		    		conta = cContas.get("0401011300");
		    		double valor1 = this.totalConta(conta, c.getTime());
		    		
		    		total = valor1;
		    		
		    		totalMemoria.put(mesAnoCalculo+"_"+calculoId, total);
		    	}
		    	else if(calculoId == 24)
		    	{
		    		conta = cContas.get("0401012000");
		    		double valor1 = this.totalConta(conta, c.getTime());
		    		
		    		total = valor1;
		    		
		    		totalMemoria.put(mesAnoCalculo+"_"+calculoId, total);
		    	}
		    	else if(calculoId == 25)
		    	{
		    		conta = cContas.get("0506010100");
		    		double valor1 = this.totalConta(conta, c.getTime());
		    		conta = cContas.get("0508010100");
		    		double valor2 = this.totalConta(conta, c.getTime());
		    		conta = cContas.get("0508020100");
		    		double valor3 = this.totalConta(conta, c.getTime());
		    		
		    		total = valor1 + valor2 + valor3;
		    		
		    		totalMemoria.put(mesAnoCalculo+"_"+calculoId, total);
		    	}
		    	else if(calculoId == 26)
		    	{
		    		conta = cContas.get("0506010200");
		    		double valor1 = this.totalConta(conta, c.getTime());
		    		conta = cContas.get("0508010000");
		    		double valor2 = this.totalConta(conta, c.getTime());
		    		conta = cContas.get("0508020200");
		    		double valor3 = this.totalConta(conta, c.getTime());
		    		
		    		total = valor1 + valor2 + valor3;
		    		
		    		totalMemoria.put(mesAnoCalculo+"_"+calculoId, total);
		    	}
		    	else if(calculoId == 27)
		    	{
		    		conta = cContas.get("0506010400");
		    		double valor1 = this.totalConta(conta, c.getTime());
		    		conta = cContas.get("0508010300");
		    		double valor2 = this.totalConta(conta, c.getTime());
		    		conta = cContas.get("0508020300");
		    		double valor3 = this.totalConta(conta, c.getTime());
		    		
		    		total = valor1 + valor2 + valor3;
		    		
		    		totalMemoria.put(mesAnoCalculo+"_"+calculoId, total);
		    	}
		    	else if(calculoId == 28)
		    	{
		    		conta = cContas.get("0506010400");
		    		double valor1 = this.totalConta(conta, c.getTime());
		    		conta = cContas.get("0508010400");
		    		double valor2 = this.totalConta(conta, c.getTime());
		    		conta = cContas.get("0508020400");
		    		double valor3 = this.totalConta(conta, c.getTime());
		    		
		    		total = valor1 + valor2 + valor3;
		    		
		    		totalMemoria.put(mesAnoCalculo+"_"+calculoId, total);
		    	}
		    	else if(calculoId == 29)
		    	{
		    		conta = cContas.get("0506010500");
		    		double valor1 = this.totalConta(conta, c.getTime());
		    		conta = cContas.get("0508010500");
		    		double valor2 = this.totalConta(conta, c.getTime());
		    		conta = cContas.get("0508020500");
		    		double valor3 = this.totalConta(conta, c.getTime());
		    		
		    		total = valor1 + valor2 + valor3;
		    		
		    		totalMemoria.put(mesAnoCalculo+"_"+calculoId, total);
		    	}
		    	else if(calculoId == 30)
		    	{
		    		conta = cContas.get("0506010600");
		    		double valor1 = this.totalConta(conta, c.getTime());
		    		conta = cContas.get("0508010600");
		    		double valor2 = this.totalConta(conta, c.getTime());
		    		conta = cContas.get("0508020600");
		    		double valor3 = this.totalConta(conta, c.getTime());
		    		
		    		total = valor1 + valor2 + valor3;
		    		
		    		totalMemoria.put(mesAnoCalculo+"_"+calculoId, total);
		    	}
		    	else if(calculoId == 31)
		    	{
		    		conta = cContas.get("0506010700");
		    		double valor1 = this.totalConta(conta, c.getTime());
		    		conta = cContas.get("0508010700");
		    		double valor2 = this.totalConta(conta, c.getTime());
		    		conta = cContas.get("0508020700");
		    		double valor3 = this.totalConta(conta, c.getTime());
		    		
		    		total = valor1 + valor2 + valor3;
		    		
		    		totalMemoria.put(mesAnoCalculo+"_"+calculoId, total);
		    	}
		    	else if(calculoId == 32)
		    	{
		    		conta = cContas.get("0506010800");
		    		double valor1 = this.totalConta(conta, c.getTime());
		    		conta = cContas.get("0508010800");
		    		double valor2 = this.totalConta(conta, c.getTime());
		    		conta = cContas.get("0508020800");
		    		double valor3 = this.totalConta(conta, c.getTime());
		    		
		    		total = valor1 + valor2 + valor3;
		    		
		    		totalMemoria.put(mesAnoCalculo+"_"+calculoId, total);
		    	}
		    	else if(calculoId == 33)
		    	{
		    		conta = cContas.get("0506010900");
		    		double valor1 = this.totalConta(conta, c.getTime());
		    		conta = cContas.get("0508010900");
		    		double valor2 = this.totalConta(conta, c.getTime());
		    		conta = cContas.get("0508020900");
		    		double valor3 = this.totalConta(conta, c.getTime());
		    		
		    		total = valor1 + valor2 + valor3;
		    		
		    		totalMemoria.put(mesAnoCalculo+"_"+calculoId, total);
		    	}
		    	else if(calculoId == 34)
		    	{
		    		conta = cContas.get("0506011000");
		    		double valor1 = this.totalConta(conta, c.getTime());
		    		conta = cContas.get("0508011000");
		    		double valor2 = this.totalConta(conta, c.getTime());
		    		conta = cContas.get("0508021000");
		    		double valor3 = this.totalConta(conta, c.getTime());
		    		
		    		total = valor1 + valor2 + valor3;
		    		
		    		totalMemoria.put(mesAnoCalculo+"_"+calculoId, total);
		    	}
		    	else if(calculoId == 35)
		    	{
		    		conta = cContas.get("0506011100");
		    		double valor1 = this.totalConta(conta, c.getTime());
		    		conta = cContas.get("0508011100");
		    		double valor2 = this.totalConta(conta, c.getTime());
		    		conta = cContas.get("0508021100");
		    		double valor3 = this.totalConta(conta, c.getTime());
		    		
		    		total = valor1 + valor2 + valor3;
		    		
		    		totalMemoria.put(mesAnoCalculo+"_"+calculoId, total);
		    	}
		    	else if(calculoId == 36)
		    	{
		    		conta = cContas.get("0506011200");
		    		double valor1 = this.totalConta(conta, c.getTime());
		    		conta = cContas.get("0508011200");
		    		double valor2 = this.totalConta(conta, c.getTime());
		    		conta = cContas.get("0508021200");
		    		double valor3 = this.totalConta(conta, c.getTime());
		    		
		    		total = valor1 + valor2 + valor3;
		    		
		    		totalMemoria.put(mesAnoCalculo+"_"+calculoId, total);
		    	}
		    	else if(calculoId == 37)
		    	{
		    		conta = cContas.get("0506011300");
		    		double valor1 = this.totalConta(conta, c.getTime());
		    		conta = cContas.get("0508011300");
		    		double valor2 = this.totalConta(conta, c.getTime());
		    		conta = cContas.get("0508021300");
		    		double valor3 = this.totalConta(conta, c.getTime());
		    		
		    		total = valor1 + valor2 + valor3;
		    		
		    		totalMemoria.put(mesAnoCalculo+"_"+calculoId, total);
		    	}
		    	else if(calculoId == 38)
		    	{
		    		totalMemoria.put(mesAnoCalculo+"_"+calculoId, total);
		    	}
		    	else if(calculoId == 39)
		    	{
		    		conta = cContas.get("0407010100");
		    		double valor1 = this.totalConta(conta, c.getTime());
		    		
		    		total = valor1;
		    		
		    		totalMemoria.put(mesAnoCalculo+"_"+calculoId, total);
		    	}
		    	else if(calculoId == 40)
		    	{
		    		conta = cContas.get("0407010200");
		    		double valor1 = this.totalConta(conta, c.getTime());
		    		
		    		total = valor1;
		    		
		    		totalMemoria.put(mesAnoCalculo+"_"+calculoId, total);
		    	}
		    	else if(calculoId == 41)
		    	{
		    		conta = cContas.get("0407010300");
		    		double valor1 = this.totalConta(conta, c.getTime());
		    		
		    		total = valor1;
		    		
		    		totalMemoria.put(mesAnoCalculo+"_"+calculoId, total);
		    	}
		    	else if(calculoId == 42)
		    	{
		    		conta = cContas.get("0407010400");
		    		double valor1 = this.totalConta(conta, c.getTime());
		    		
		    		total = valor1;
		    		
		    		totalMemoria.put(mesAnoCalculo+"_"+calculoId, total);
		    	}
		    	else if(calculoId == 43)
		    	{
		    		conta = cContas.get("0407010500");
		    		double valor1 = this.totalConta(conta, c.getTime());
		    		
		    		total = valor1;
		    		
		    		totalMemoria.put(mesAnoCalculo+"_"+calculoId, total);
		    	}
		    	else if(calculoId == 44)
		    	{
		    		conta = cContas.get("0407010600");
		    		double valor1 = this.totalConta(conta, c.getTime());
		    		
		    		total = valor1;
		    		
		    		totalMemoria.put(mesAnoCalculo+"_"+calculoId, total);
		    	}
		    	else if(calculoId == 45)
		    	{
		    		conta = cContas.get("0407010700");
		    		double valor1 = this.totalConta(conta, c.getTime());
		    		
		    		total = valor1;
		    		
		    		totalMemoria.put(mesAnoCalculo+"_"+calculoId, total);
		    	}
		    	else if(calculoId == 46)
		    	{
		    		conta = cContas.get("0407010800");
		    		double valor1 = this.totalConta(conta, c.getTime());
		    		
		    		total = valor1;
		    		
		    		totalMemoria.put(mesAnoCalculo+"_"+calculoId, total);
		    	}
		    	else if(calculoId == 47)
		    	{
		    		conta = cContas.get("0407010900");
		    		double valor1 = this.totalConta(conta, c.getTime());
		    		
		    		total = valor1;
		    		
		    		totalMemoria.put(mesAnoCalculo+"_"+calculoId, total);
		    	}
		    	else if(calculoId == 48)
		    	{
		    		conta = cContas.get("0407011000");
		    		double valor1 = this.totalConta(conta, c.getTime());
		    		
		    		total = valor1;
		    		
		    		totalMemoria.put(mesAnoCalculo+"_"+calculoId, total);
		    	}
		    	else if(calculoId == 49)
		    	{
		    		conta = cContas.get("0407011100");
		    		double valor1 = this.totalConta(conta, c.getTime());
		    		
		    		total = valor1;
		    		
		    		totalMemoria.put(mesAnoCalculo+"_"+calculoId, total);
		    	}
		    	else if(calculoId == 50)
		    	{
		    		conta = cContas.get("0407011200");
		    		double valor1 = this.totalConta(conta, c.getTime());
		    		
		    		total = valor1;
		    		
		    		totalMemoria.put(mesAnoCalculo+"_"+calculoId, total);
		    	}
		    	else if(calculoId == 51)
		    	{
		    		conta = cContas.get("0407011300");
		    		double valor1 = this.totalConta(conta, c.getTime());
		    		
		    		total = valor1;
		    		
		    		totalMemoria.put(mesAnoCalculo+"_"+calculoId, total);
		    	}
		    	else if(calculoId == 52)
		    	{
		    		totalMemoria.put(mesAnoCalculo+"_"+calculoId, total);
		    	}
		    	else if(calculoId == 53)
		    	{
		    		/*conta = cContas.get("0506010100");
		    		double valor1 = this.totalConta(conta, c.getTime());
		    		conta = cContas.get("0508010100");
		    		double valor2 = this.totalConta(conta, c.getTime());
		    		conta = cContas.get("0508020100");
		    		double valor3 = this.totalConta(conta, c.getTime());
		    		conta = cContas.get("0401010100");
		    		double valor4 = this.totalConta(conta, c.getTime());
		    		if(valor4 > 0)
		    			total = (valor1 + valor2 + valor3) / valor4;*/
		    		
		    		double valor1 = totalMemoria.get(mesAnoCalculo+"_"+25);
		    		double valor2 = totalMemoria.get(mesAnoCalculo+"_"+11);
		    		
		    		if(valor2 > 0)
		    			total = valor1 / valor2;
		    		
		    		totalMemoria.put(mesAnoCalculo+"_"+calculoId, total);
		    	}
		    	else if(calculoId == 54)
		    	{
		    		/*conta = cContas.get("0506010200");
		    		double valor1 = this.totalConta(conta, c.getTime());
		    		conta = cContas.get("0508010000");
		    		double valor2 = this.totalConta(conta, c.getTime());
		    		conta = cContas.get("0508020200");
		    		double valor3 = this.totalConta(conta, c.getTime());
		    		conta = cContas.get("0401010200");
		    		double valor4 = this.totalConta(conta, c.getTime());
		    		
		    		if(valor4 > 0)
		    			total = (valor1 + valor2 + valor3) / valor4;*/
		    		
		    		double valor1 = totalMemoria.get(mesAnoCalculo+"_"+26);
		    		double valor2 = totalMemoria.get(mesAnoCalculo+"_"+12);
		    		
		    		if(valor2 > 0)
		    			total = valor1 / valor2;
		    		
		    		totalMemoria.put(mesAnoCalculo+"_"+calculoId, total);
		    	}
		    	else if(calculoId == 55)
		    	{
		    		/*conta = cContas.get("0506010400");
		    		double valor1 = this.totalConta(conta, c.getTime());
		    		conta = cContas.get("0508010300");
		    		double valor2 = this.totalConta(conta, c.getTime());
		    		conta = cContas.get("0508020300");
		    		double valor3 = this.totalConta(conta, c.getTime());
		    		conta = cContas.get("0401010300");
		    		double valor4 = this.totalConta(conta, c.getTime());
		    		
		    		if(valor4 > 0)
		    			total = (valor1 + valor2 + valor3) / valor4;*/
		    		
		    		double valor1 = totalMemoria.get(mesAnoCalculo+"_"+27);
		    		double valor2 = totalMemoria.get(mesAnoCalculo+"_"+13);
		    		
		    		if(valor2 > 0)
		    			total = valor1 / valor2;
		    		
		    		totalMemoria.put(mesAnoCalculo+"_"+calculoId, total);
		    	}
		    	else if(calculoId == 56)
		    	{
		    		/*conta = cContas.get("0506010400");
		    		double valor1 = this.totalConta(conta, c.getTime());
		    		conta = cContas.get("0508010400");
		    		double valor2 = this.totalConta(conta, c.getTime());
		    		conta = cContas.get("0508020400");
		    		double valor3 = this.totalConta(conta, c.getTime());
		    		conta = cContas.get("0401010400");
		    		double valor4 = this.totalConta(conta, c.getTime());
		    		
		    		if(valor4 > 0)
		    			total = (valor1 + valor2 + valor3) / valor4;*/
		    		
		    		double valor1 = totalMemoria.get(mesAnoCalculo+"_"+28);
		    		double valor2 = totalMemoria.get(mesAnoCalculo+"_"+14);
		    		
		    		if(valor2 > 0)
		    			total = valor1 / valor2;
		    		
		    		totalMemoria.put(mesAnoCalculo+"_"+calculoId, total);
		    	}
		    	else if(calculoId == 57)
		    	{
		    		/*conta = cContas.get("0506010500");
		    		double valor1 = this.totalConta(conta, c.getTime());
		    		conta = cContas.get("0508010500");
		    		double valor2 = this.totalConta(conta, c.getTime());
		    		conta = cContas.get("0508020500");
		    		double valor3 = this.totalConta(conta, c.getTime());
		    		conta = cContas.get("0401010500");
		    		double valor4 = this.totalConta(conta, c.getTime());
		    		
		    		if(valor4 > 0)
		    			total = (valor1 + valor2 + valor3) / valor4;*/
		    		
		    		double valor1 = totalMemoria.get(mesAnoCalculo+"_"+29);
		    		double valor2 = totalMemoria.get(mesAnoCalculo+"_"+15);
		    		
		    		if(valor2 > 0)
		    			total = valor1 / valor2;
		    		
		    		totalMemoria.put(mesAnoCalculo+"_"+calculoId, total);
		    	}
		    	else if(calculoId == 58)
		    	{
		    		/*conta = cContas.get("0506010600");
		    		double valor1 = this.totalConta(conta, c.getTime());
		    		conta = cContas.get("0508010600");
		    		double valor2 = this.totalConta(conta, c.getTime());
		    		conta = cContas.get("0508020600");
		    		double valor3 = this.totalConta(conta, c.getTime());
		    		conta = cContas.get("0401010600");
		    		double valor4 = this.totalConta(conta, c.getTime());
		    		
		    		if(valor4 > 0)
		    			total = (valor1 + valor2 + valor3) / valor4;*/
		    		
		    		double valor1 = totalMemoria.get(mesAnoCalculo+"_"+30);
		    		double valor2 = totalMemoria.get(mesAnoCalculo+"_"+16);
		    		
		    		if(valor2 > 0)
		    			total = valor1 / valor2;
		    		
		    		totalMemoria.put(mesAnoCalculo+"_"+calculoId, total);
		    	}
		    	else if(calculoId == 59)
		    	{
		    		/*conta = cContas.get("0506010700");
		    		double valor1 = this.totalConta(conta, c.getTime());
		    		conta = cContas.get("0508010700");
		    		double valor2 = this.totalConta(conta, c.getTime());
		    		conta = cContas.get("0508020700");
		    		double valor3 = this.totalConta(conta, c.getTime());
		    		conta = cContas.get("0401010700");
		    		double valor4 = this.totalConta(conta, c.getTime());
		    		
		    		if(valor4 > 0)
		    			total = (valor1 + valor2 + valor3) / valor4;*/
		    		
		    		double valor1 = totalMemoria.get(mesAnoCalculo+"_"+31);
		    		double valor2 = totalMemoria.get(mesAnoCalculo+"_"+17);
		    		
		    		if(valor2 > 0)
		    			total = valor1 / valor2;
		    		
		    		totalMemoria.put(mesAnoCalculo+"_"+calculoId, total);
		    	}
		    	else if(calculoId == 60)
		    	{
		    		/*conta = cContas.get("0506010800");
		    		double valor1 = this.totalConta(conta, c.getTime());
		    		conta = cContas.get("0508010800");
		    		double valor2 = this.totalConta(conta, c.getTime());
		    		conta = cContas.get("0508020800");
		    		double valor3 = this.totalConta(conta, c.getTime());
		    		conta = cContas.get("0401010800");
		    		double valor4 = this.totalConta(conta, c.getTime());
		    		
		    		if(valor4 > 0)
		    			total = (valor1 + valor2 + valor3) / valor4;*/
		    		
		    		double valor1 = totalMemoria.get(mesAnoCalculo+"_"+32);
		    		double valor2 = totalMemoria.get(mesAnoCalculo+"_"+18);
		    		
		    		if(valor2 > 0)
		    			total = valor1 / valor2;
		    		
		    		totalMemoria.put(mesAnoCalculo+"_"+calculoId, total);
		    	}
		    	else if(calculoId == 61)
		    	{
		    		/*conta = cContas.get("0506010900");
		    		double valor1 = this.totalConta(conta, c.getTime());
		    		conta = cContas.get("0508010900");
		    		double valor2 = this.totalConta(conta, c.getTime());
		    		conta = cContas.get("0508020900");
		    		double valor3 = this.totalConta(conta, c.getTime());
		    		conta = cContas.get("0401010900");
		    		double valor4 = this.totalConta(conta, c.getTime());
		    		
		    		if(valor4 > 0)
		    			total = (valor1 + valor2 + valor3) / valor4;*/
		    		
		    		double valor1 = totalMemoria.get(mesAnoCalculo+"_"+33);
		    		double valor2 = totalMemoria.get(mesAnoCalculo+"_"+19);
		    		
		    		if(valor2 > 0)
		    			total = valor1 / valor2;
		    		
		    		totalMemoria.put(mesAnoCalculo+"_"+calculoId, total);
		    	}
		    	else if(calculoId == 62)
		    	{
		    		/*conta = cContas.get("0506011000");
		    		double valor1 = this.totalConta(conta, c.getTime());
		    		conta = cContas.get("0508011000");
		    		double valor2 = this.totalConta(conta, c.getTime());
		    		conta = cContas.get("0508021000");
		    		double valor3 = this.totalConta(conta, c.getTime());
		    		conta = cContas.get("0401011000");
		    		double valor4 = this.totalConta(conta, c.getTime());
		    		
		    		if(valor4 > 0)
		    			total = (valor1 + valor2 + valor3) / valor4;*/
		    		
		    		double valor1 = totalMemoria.get(mesAnoCalculo+"_"+34);
		    		double valor2 = totalMemoria.get(mesAnoCalculo+"_"+20);
		    		
		    		if(valor2 > 0)
		    			total = valor1 / valor2;
		    		
		    		totalMemoria.put(mesAnoCalculo+"_"+calculoId, total);
		    	}
		    	else if(calculoId == 63)
		    	{
		    		/*conta = cContas.get("0506011100");
		    		double valor1 = this.totalConta(conta, c.getTime());
		    		conta = cContas.get("0508011100");
		    		double valor2 = this.totalConta(conta, c.getTime());
		    		conta = cContas.get("0508021100");
		    		double valor3 = this.totalConta(conta, c.getTime());
		    		conta = cContas.get("0401011100");
		    		double valor4 = this.totalConta(conta, c.getTime());
		    		
		    		if(valor4 > 0)
		    			total = (valor1 + valor2 + valor3) / valor4;*/
		    		
		    		double valor1 = totalMemoria.get(mesAnoCalculo+"_"+35);
		    		double valor2 = totalMemoria.get(mesAnoCalculo+"_"+21);
		    		
		    		if(valor2 > 0)
		    			total = valor1 / valor2;
		    		
		    		totalMemoria.put(mesAnoCalculo+"_"+calculoId, total);
		    	}
		    	else if(calculoId == 64)
		    	{
		    		/*conta = cContas.get("0506011200");
		    		double valor1 = this.totalConta(conta, c.getTime());
		    		conta = cContas.get("0508011200");
		    		double valor2 = this.totalConta(conta, c.getTime());
		    		conta = cContas.get("0508021200");
		    		double valor3 = this.totalConta(conta, c.getTime());
		    		conta = cContas.get("0401011200");
		    		double valor4 = this.totalConta(conta, c.getTime());
		    		
		    		if(valor4 > 0)
		    			total = (valor1 + valor2 + valor3) / valor4;*/
		    		
		    		double valor1 = totalMemoria.get(mesAnoCalculo+"_"+36);
		    		double valor2 = totalMemoria.get(mesAnoCalculo+"_"+22);
		    		
		    		if(valor2 > 0)
		    			total = valor1 / valor2;
		    		
		    		totalMemoria.put(mesAnoCalculo+"_"+calculoId, total);
		    	}
		    	else if(calculoId == 65)
		    	{/*
		    		conta = cContas.get("0506011300");
		    		double valor1 = this.totalConta(conta, c.getTime());
		    		conta = cContas.get("0508011300");
		    		double valor2 = this.totalConta(conta, c.getTime());
		    		conta = cContas.get("0508021300");
		    		double valor3 = this.totalConta(conta, c.getTime());
		    		conta = cContas.get("0401011300");
		    		double valor4 = this.totalConta(conta, c.getTime());
		    		
		    		if(valor4 > 0)
		    			total = (valor1 + valor2 + valor3) / valor4;*/
		    		
		    		double valor1 = totalMemoria.get(mesAnoCalculo+"_"+37);
		    		double valor2 = totalMemoria.get(mesAnoCalculo+"_"+23);
		    		
		    		if(valor2 > 0)
		    			total = valor1 / valor2;
		    		
		    		totalMemoria.put(mesAnoCalculo+"_"+calculoId, total);
		    	}
		    	else if(calculoId == 66)
		    	{
		    		totalMemoria.put(mesAnoCalculo+"_"+calculoId, total);
		    	}
		    	else if(calculoId == 67)
		    	{
		    		conta = cContas.get("0504010000");
		    		double valor1 = this.totalConta(conta, c.getTime());
		    		
		    		total = valor1;
		    		
		    		totalMemoria.put(mesAnoCalculo+"_"+calculoId, total);
		    	}
		    	else if(calculoId == 68)
		    	{
		    		/*conta = cContas.get("0401000000");
		    		double valor1 = this.totalConta(conta, c.getTime());*/
		    		double valor1 = totalMemoria.get(mesAnoCalculo+"_"+1);
		    		double valor2 = totalMemoria.get(mesAnoCalculo+"_"+67);
		    		//conta = cContas.get("0504010000");
		    		//double valor2 = this.totalConta(conta, c.getTime());
		    		
		    		if(valor2 > 0)
		    			total = valor1/valor2;
		    		
		    		totalMemoria.put(mesAnoCalculo+"_"+calculoId, total);
		    	}
		    	else if(calculoId == 69)
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
		    		
		    		totalMemoria.put(mesAnoCalculo+"_"+calculoId, total);
		    	}
		    	else if(calculoId == 70)
		    	{
		    		conta = cContas.get("0525000000");
		    		double valor1 = this.totalConta(conta, c.getTime());
		    		
	    			total = valor1;
		    		
		    		totalMemoria.put(mesAnoCalculo+"_"+calculoId, total);
		    	}
		    	else if(calculoId == 71)
		    	{
		    		//conta = cContas.get("0401000000");
		    		//double valor1 = this.totalConta(conta, c.getTime());
		    		double valor1 = totalMemoria.get(mesAnoCalculo+"_"+1);
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
		    		
		    		totalMemoria.put(mesAnoCalculo+"_"+calculoId, total);
		    	}
		    	else if(calculoId == 72)
		    	{
		    		conta = cContas.get("0502000000");
		    		double valor1 = this.totalConta(conta, c.getTime());
		    		
	    			total = valor1;
		    		
		    		totalMemoria.put(mesAnoCalculo+"_"+calculoId, total);
		    	}
		    	else if(calculoId == 73)
		    	{
		    		conta = cContas.get("0300000000");
		    		double valor1 = this.totalConta(conta, c.getTime());
		    		
	    			total = valor1;
		    		
		    		totalMemoria.put(mesAnoCalculo+"_"+calculoId, total);
		    	}
		    	else if(calculoId == 74)
		    	{
		    		conta = cContas.get("0507000000");
		    		double valor1 = this.totalConta(conta, c.getTime());
		    		conta = cContas.get("0506012000");
		    		double valor2 = this.totalConta(conta, c.getTime());
		    		conta = cContas.get("0501012000");
		    		double valor3 = this.totalConta(conta, c.getTime());
		    		//conta = cContas.get("0401000000");
		    		//double valor4 = this.totalConta(conta, c.getTime());
		    		double valor4 = totalMemoria.get(mesAnoCalculo+"_"+1);
		    		
		    		if(valor4 > 0)
		    			total = (valor1 + valor2 + valor3) / valor4;
		    		
		    		totalMemoria.put(mesAnoCalculo+"_"+calculoId, total);
		    	}
		    	else if(calculoId == 75)
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
		    		//conta = cContas.get("0401000000");
		    		//double valor7 = this.totalConta(conta, c.getTime());
		    		double valor7 = totalMemoria.get(mesAnoCalculo+"_"+1);
		    		
		    		if(valor7 > 0)
		    			total = (valor1 + valor2 - valor3 - valor4 - valor5 - valor6) / valor7;
		    		
		    		totalMemoria.put(mesAnoCalculo+"_"+calculoId, total);
		    	}
		    	else if(calculoId == 76)
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
		    		
		    		totalMemoria.put(mesAnoCalculo+"_"+calculoId, total);
		    	}
		    	else if(calculoId == 77)
		    	{
		    		conta = cContas.get("0212050100");
		    		double valor1 = this.totalConta(conta, c.getTime());
		    		
	    			total = valor1;
		    		
		    		totalMemoria.put(mesAnoCalculo+"_"+calculoId, total);
		    	}
		    	else if(calculoId == 78)
		    	{
		    		conta = cContas.get("0212050200");
		    		double valor1 = this.totalConta(conta, c.getTime());
		    		
	    			total = valor1;
		    		
		    		totalMemoria.put(mesAnoCalculo+"_"+calculoId, total);
		    	}
		    	else if(calculoId == 79)
		    	{
		    		conta = cContas.get("0213000000");
		    		double valor1 = this.totalConta(conta, c.getTime());
		    		
	    			total = valor1;
		    		
		    		totalMemoria.put(mesAnoCalculo+"_"+calculoId, total);
		    	}
		    	else if(calculoId == 80)
		    	{
		    		conta = cContas.get("0107010000");
		    		double valor1 = this.totalConta(conta, c.getTime());
		    		conta = cContas.get("0107010100");
		    		double valor2 = this.totalConta(conta, c.getTime());
		    		conta = cContas.get("0107020000");
		    		double valor3 = this.totalConta(conta, c.getTime());
		    		conta = cContas.get("0107020100");
		    		double valor4 = this.totalConta(conta, c.getTime());
		    		
	    			total = valor1 - valor2 + valor3 - valor4;
		    		
		    		totalMemoria.put(mesAnoCalculo+"_"+calculoId, total);
		    	}
		    	else if(calculoId == 81)
		    	{
		    		conta = cContas.get("0107030000");
		    		double valor1 = this.totalConta(conta, c.getTime());
		    		conta = cContas.get("0107040000");
		    		double valor2 = this.totalConta(conta, c.getTime());
		    		
	    			total = valor1 + valor2;
		    		
		    		totalMemoria.put(mesAnoCalculo+"_"+calculoId, total);
		    	}
		    	else if(calculoId == 82)
		    	{
		    		conta = cContas.get("0107060000");
		    		double valor1 = this.totalConta(conta, c.getTime());
		    		
	    			total = valor1;
		    		
		    		totalMemoria.put(mesAnoCalculo+"_"+calculoId, total);
		    	}
		    	else if(calculoId == 83)
		    	{
		    		conta = cContas.get("0107010100");
		    		double valor1 = this.totalConta(conta, c.getTime());
		    		conta = cContas.get("0107020100");
		    		double valor2 = this.totalConta(conta, c.getTime());
		    		
	    			total = valor1 + valor2;
		    		
		    		totalMemoria.put(mesAnoCalculo+"_"+calculoId, total);
		    	}
		    	else if(calculoId == 84)
		    	{
		    		conta = cContas.get("0107050000");
		    		double valor1 = this.totalConta(conta, c.getTime());
		    		
	    			total = valor1;
		    		
		    		totalMemoria.put(mesAnoCalculo+"_"+calculoId, total);
		    	}
		    	else if(calculoId == 85)
		    	{
		    		double valor1 = totalMemoria.get(mesAnoCalculo+"_"+80);
		    		double valor2 = totalMemoria.get(mesAnoCalculo+"_"+81);
		    		double valor3 = totalMemoria.get(mesAnoCalculo+"_"+82);
		    		double valor4 = totalMemoria.get(mesAnoCalculo+"_"+83);
		    		double valor5 = totalMemoria.get(mesAnoCalculo+"_"+84);
		    		
	    			total = valor1 + valor2 + valor3 + valor4 + valor5;
		    		
		    		totalMemoria.put(mesAnoCalculo+"_"+calculoId, total);
		    	}
		    	else if(calculoId == 86)
		    	{
		    		double valor1 = home.obterQtdeEntidadesVigentes(agentesDeSeguro, dataInicioValidadeInscricao, dataFimValidadeInscricao);
		    		
	    			total = valor1;
		    		
		    		totalMemoria.put(mesAnoCalculo+"_"+calculoId, total);
		    	}
		    	else if(calculoId == 87)
		    	{
		    		double valor1 = home.obterQtdeAseguradorasVigentes(aseguradoras, dataFimValidadeInscricao);
		    		
	    			total = valor1;
		    		
		    		totalMemoria.put(mesAnoCalculo+"_"+calculoId, total);
		    	}
		    	else if(calculoId == 88)
		    	{
		    		double valor1 = home.obterQtdeEntidadesVigentes(reaseguradoras, dataInicioValidadeInscricao, dataFimValidadeInscricao);
		    		
	    			total = valor1;
		    		
		    		totalMemoria.put(mesAnoCalculo+"_"+calculoId, total);
		    	}
		    	else if(calculoId == 89)
		    	{
		    		double valor1 = home.obterQtdeEntidadesVigentes(corredoresSeguros, dataInicioValidadeInscricao, dataFimValidadeInscricao);
		    		
	    			total = valor1;
		    		
		    		totalMemoria.put(mesAnoCalculo+"_"+calculoId, total);
		    	}
		    	else if(calculoId == 90)
		    	{
		    		double valor1 = home.obterQtdeEntidadesVigentes(corredoresReaseguros, dataInicioValidadeInscricao, dataFimValidadeInscricao);
		    		
	    			total = valor1;
		    		
		    		totalMemoria.put(mesAnoCalculo+"_"+calculoId, total);
		    	}
		    	else if(calculoId == 91)
		    	{
		    		double valor1 = home.obterQtdeEntidadesVigentes(auditoresExternos, dataInicioValidadeInscricao, dataFimValidadeInscricao);
		    		
	    			total = valor1;
		    		
		    		totalMemoria.put(mesAnoCalculo+"_"+calculoId, total);
		    	}
		    	else if(calculoId == 92)
		    	{
		    		double valor1 = home.obterQtdeAseguradorasVigentes(coaseguradoras, dataFimValidadeInscricao);
		    		
	    			total = valor1;
		    		
		    		totalMemoria.put(mesAnoCalculo+"_"+calculoId, total);
		    	}
		    	else if(calculoId == 93)
		    	{
		    		double valor1 = totalMemoria.get(mesAnoCalculo+"_"+86);
		    		double valor2 = totalMemoria.get(mesAnoCalculo+"_"+87);
		    		double valor3 = totalMemoria.get(mesAnoCalculo+"_"+88);
		    		double valor4 = totalMemoria.get(mesAnoCalculo+"_"+89);
		    		double valor5 = totalMemoria.get(mesAnoCalculo+"_"+90);
		    		double valor6 = totalMemoria.get(mesAnoCalculo+"_"+91);
		    		double valor7 = totalMemoria.get(mesAnoCalculo+"_"+92);
		    		
	    			total = valor1 + valor2 + valor3 + valor4 + valor5 + valor6 + valor7;
		    		
		    		totalMemoria.put(mesAnoCalculo+"_"+calculoId, total);
		    	}
		    	else if(calculoId == 94)
		    	{
		    		conta2 = contas.get("0401012001");
		    		double valor1 = this.totalConta(conta2, c.getTime());
		    		
	    			total = valor1;
		    		
		    		totalMemoria.put(mesAnoCalculo+"_"+calculoId, total);
		    	}
		    	else if(calculoId == 95)
		    	{
		    		conta2 = contas.get("0401012003");
		    		double valor1 = this.totalConta(conta2, c.getTime());
		    		
	    			total = valor1;
		    		
		    		totalMemoria.put(mesAnoCalculo+"_"+calculoId, total);
		    	}
		    	else if(calculoId == 96)
		    	{
		    		conta2 = contas.get("0401012006");
		    		double valor1 = this.totalConta(conta2, c.getTime());
		    		
	    			total = valor1;
		    		
		    		totalMemoria.put(mesAnoCalculo+"_"+calculoId, total);
		    	}
		    	else if(calculoId == 97)
		    	{
		    		conta2 = contas.get("0401012009");
		    		double valor1 = this.totalConta(conta2, c.getTime());
		    		
	    			total = valor1;
		    		
		    		totalMemoria.put(mesAnoCalculo+"_"+calculoId, total);
		    	}
		    	else if(calculoId == 98)
		    	{
		    		conta2 = contas.get("0401012002");
		    		double valor1 = this.totalConta(conta2, c.getTime());
		    		
	    			total = valor1;
		    		
		    		totalMemoria.put(mesAnoCalculo+"_"+calculoId, total);
		    	}
		    	else if(calculoId == 99)
		    	{
		    		conta2 = contas.get("0401012004");
		    		double valor1 = this.totalConta(conta2, c.getTime());
		    		
	    			total = valor1;
		    		
		    		totalMemoria.put(mesAnoCalculo+"_"+calculoId, total);
		    	}
		    	else if(calculoId == 100)
		    	{
		    		conta2 = contas.get("0401012005");
		    		double valor1 = this.totalConta(conta2, c.getTime());
		    		
	    			total = valor1;
		    		
		    		totalMemoria.put(mesAnoCalculo+"_"+calculoId, total);
		    	}
		    	else if(calculoId == 101)
		    	{
		    		conta2 = contas.get("0401012007");
		    		double valor1 = this.totalConta(conta2, c.getTime());
		    		
	    			total = valor1;
		    		
		    		totalMemoria.put(mesAnoCalculo+"_"+calculoId, total);
		    	}
		    	else if(calculoId == 102)
		    	{
		    		double valor1 = totalMemoria.get(mesAnoCalculo+"_"+11);
		    		
	    			total = valor1;
		    		
		    		totalMemoria.put(mesAnoCalculo+"_"+calculoId, total);
		    	}
		    	else if(calculoId == 103)
		    	{
		    		double valor1 = totalMemoria.get(mesAnoCalculo+"_"+12);
		    		
	    			total = valor1;
		    		
		    		totalMemoria.put(mesAnoCalculo+"_"+calculoId, total);
		    	}
		    	else if(calculoId == 104)
		    	{
		    		double valor1 = totalMemoria.get(mesAnoCalculo+"_"+13);
		    		
	    			total = valor1;
		    		
		    		totalMemoria.put(mesAnoCalculo+"_"+calculoId, total);
		    	}
		    	else if(calculoId == 105)
		    	{
		    		double valor1 = totalMemoria.get(mesAnoCalculo+"_"+14);
		    		
	    			total = valor1;
		    		
		    		totalMemoria.put(mesAnoCalculo+"_"+calculoId, total);
		    	}
		    	else if(calculoId == 106)
		    	{
		    		double valor1 = totalMemoria.get(mesAnoCalculo+"_"+15);
		    		
	    			total = valor1;
		    		
		    		totalMemoria.put(mesAnoCalculo+"_"+calculoId, total);
		    	}
		    	else if(calculoId == 107)
		    	{
		    		double valor1 = totalMemoria.get(mesAnoCalculo+"_"+16);
		    		
	    			total = valor1;
		    		
		    		totalMemoria.put(mesAnoCalculo+"_"+calculoId, total);
		    	}
		    	else if(calculoId == 108)
		    	{
		    		double valor1 = totalMemoria.get(mesAnoCalculo+"_"+17);
		    		
	    			total = valor1;
		    		
		    		totalMemoria.put(mesAnoCalculo+"_"+calculoId, total);
		    	}
		    	else if(calculoId == 109)
		    	{
		    		double valor1 = totalMemoria.get(mesAnoCalculo+"_"+18);
		    		
	    			total = valor1;
		    		
		    		totalMemoria.put(mesAnoCalculo+"_"+calculoId, total);
		    	}
		    	else if(calculoId == 110)
		    	{
		    		double valor1 = totalMemoria.get(mesAnoCalculo+"_"+19);
		    		
	    			total = valor1;
		    		
		    		totalMemoria.put(mesAnoCalculo+"_"+calculoId, total);
		    	}
		    	else if(calculoId == 111)
		    	{
		    		double valor1 = totalMemoria.get(mesAnoCalculo+"_"+20);
		    		
	    			total = valor1;
		    		
		    		totalMemoria.put(mesAnoCalculo+"_"+calculoId, total);
		    	}
		    	else if(calculoId == 112)
		    	{
		    		double valor1 = totalMemoria.get(mesAnoCalculo+"_"+21);
		    		
	    			total = valor1;
		    		
		    		totalMemoria.put(mesAnoCalculo+"_"+calculoId, total);
		    	}
		    	else if(calculoId == 113)
		    	{
		    		double valor1 = totalMemoria.get(mesAnoCalculo+"_"+22);
		    		
	    			total = valor1;
		    		
		    		totalMemoria.put(mesAnoCalculo+"_"+calculoId, total);
		    	}
		    	else if(calculoId == 114)
		    	{
		    		double valor1 = totalMemoria.get(mesAnoCalculo+"_"+23);
		    		
	    			total = valor1;
		    		
		    		totalMemoria.put(mesAnoCalculo+"_"+calculoId, total);
		    	}
		    	else if(calculoId == 115)
		    	{
		    		conta = cContas.get("0401020000");
		    		double valor1 = this.totalConta(conta, c.getTime());
		    		
	    			total = valor1;
		    		
		    		totalMemoria.put(mesAnoCalculo+"_"+calculoId, total);
		    	}
		    	else if(calculoId == 116)
		    	{
		    		conta2 = contas.get("0401012008");
		    		double valor1 = this.totalConta(conta2, c.getTime());
		    		
	    			total = valor1;
		    		
		    		totalMemoria.put(mesAnoCalculo+"_"+calculoId, total);
		    	}
		    	else if(calculoId == 117)
		    	{
		    		conta2 = contas.get("0401022005");
		    		double valor1 = this.totalConta(conta2, c.getTime());
		    		
	    			total = valor1;
		    		
		    		totalMemoria.put(mesAnoCalculo+"_"+calculoId, total);
		    	}
		    	else if(calculoId == 118)
		    	{
		    		double valor1 = totalMemoria.get(mesAnoCalculo+"_"+2);
	    			total = valor1;
		    		
		    		totalMemoria.put(mesAnoCalculo+"_"+calculoId, total);
		    	}
		    	else if(calculoId == 119)
		    	{
		    		double valor1 = totalMemoria.get(mesAnoCalculo+"_"+3);
	    			total = valor1;
		    		
		    		totalMemoria.put(mesAnoCalculo+"_"+calculoId, total);
		    	}
		    	else if(calculoId == 120)
		    	{
		    		totalMemoria.put(mesAnoCalculo+"_"+calculoId, total);
		    	}
		    	else if(calculoId == 121)
		    	{
		    		totalMemoria.put(mesAnoCalculo+"_"+calculoId, total);
		    	}
		    	else if(calculoId == 122)
		    	{
		    		totalMemoria.put(mesAnoCalculo+"_"+calculoId, total);
		    	}
		    	else if(calculoId == 123)
		    	{
		    		totalMemoria.put(mesAnoCalculo+"_"+calculoId, total);
		    	}
		    	else if(calculoId == 124)
		    	{
		    		double valor1 = totalMemoria.get(mesAnoCalculo+"_"+14);
		    		
		    		total = valor1;
		    		
		    		totalMemoria.put(mesAnoCalculo+"_"+calculoId, total);
		    	}
		    	else if(calculoId == 125)
		    	{
		    		double valor1 = totalMemoria.get(mesAnoCalculo+"_"+94);
		    		
		    		total = valor1;
		    		
		    		totalMemoria.put(mesAnoCalculo+"_"+calculoId, total);
		    	}
		    	else if(calculoId == 126)
		    	{
		    		double valor1 = totalMemoria.get(mesAnoCalculo+"_"+95);
		    		
		    		total = valor1;
		    		
		    		totalMemoria.put(mesAnoCalculo+"_"+calculoId, total);
		    	}
		    	else if(calculoId == 127)
		    	{
		    		double valor1 = totalMemoria.get(mesAnoCalculo+"_"+96);
		    		
		    		total = valor1;
		    		
		    		totalMemoria.put(mesAnoCalculo+"_"+calculoId, total);
		    	}
		    	else if(calculoId == 128)
		    	{
		    		double valor1 = totalMemoria.get(mesAnoCalculo+"_"+97);
		    		
		    		total = valor1;
		    		
		    		totalMemoria.put(mesAnoCalculo+"_"+calculoId, total);
		    	}
		    	else if(calculoId == 129)
		    	{
		    		double valor1 = totalMemoria.get(mesAnoCalculo+"_"+11);
		    		
	    			total = valor1;
		    		
		    		totalMemoria.put(mesAnoCalculo+"_"+calculoId, total);
		    	}
		    	else if(calculoId == 130)
		    	{
		    		double valor1 = totalMemoria.get(mesAnoCalculo+"_"+18);
		    		
	    			total = valor1;
		    		
		    		totalMemoria.put(mesAnoCalculo+"_"+calculoId, total);
		    	}
		    	else if(calculoId == 131)
		    	{
		    		double valor1 = totalMemoria.get(mesAnoCalculo+"_"+17);
		    		
	    			total = valor1;
		    		
		    		totalMemoria.put(mesAnoCalculo+"_"+calculoId, total);
		    	}
		    	else if(calculoId == 132)
		    	{
		    		double valor1 = totalMemoria.get(mesAnoCalculo+"_"+19);
		    		
	    			total = valor1;
		    		
		    		totalMemoria.put(mesAnoCalculo+"_"+calculoId, total);
		    	}
		    	else if(calculoId == 133)
		    	{
		    		double valor1 = totalMemoria.get(mesAnoCalculo+"_"+22);
		    		
	    			total = valor1;
		    		
		    		totalMemoria.put(mesAnoCalculo+"_"+calculoId, total);
		    	}
		    	else if(calculoId == 134)
		    	{
		    		double valor1 = totalMemoria.get(mesAnoCalculo+"_"+12);
		    		
	    			total = valor1;
		    		
		    		totalMemoria.put(mesAnoCalculo+"_"+calculoId, total);
		    	}
		    	else if(calculoId == 135)
		    	{
		    		double valor1 = totalMemoria.get(mesAnoCalculo+"_"+23);
		    		
	    			total = valor1;
		    		
		    		totalMemoria.put(mesAnoCalculo+"_"+calculoId, total);
		    	}
		    	else if(calculoId == 136)
		    	{
		    		double valor1 = totalMemoria.get(mesAnoCalculo+"_"+16);
		    		
	    			total = valor1;
		    		
		    		totalMemoria.put(mesAnoCalculo+"_"+calculoId, total);
		    	}
		    	else if(calculoId == 137)
		    	{
		    		double valor1 = totalMemoria.get(mesAnoCalculo+"_"+20);
		    		
	    			total = valor1;
		    		
		    		totalMemoria.put(mesAnoCalculo+"_"+calculoId, total);
		    	}
		    	else if(calculoId == 138)
		    	{
		    		double valor1 = totalMemoria.get(mesAnoCalculo+"_"+13);
		    		
	    			total = valor1;
		    		
		    		totalMemoria.put(mesAnoCalculo+"_"+calculoId, total);
		    	}
		    	else if(calculoId == 139)
		    	{
		    		double valor1 = totalMemoria.get(mesAnoCalculo+"_"+15);
		    		
	    			total = valor1;
		    		
		    		totalMemoria.put(mesAnoCalculo+"_"+calculoId, total);
		    	}
		    	else if(calculoId == 140)
		    	{
		    		double valor1 = totalMemoria.get(mesAnoCalculo+"_"+21);
		    		
	    			total = valor1;
		    		
		    		totalMemoria.put(mesAnoCalculo+"_"+calculoId, total);
		    	}
		    	else if(calculoId == 141)
		    	{
		    		double valor1 = totalMemoria.get(mesAnoCalculo+"_"+98);
		    		
	    			total = valor1;
		    		
		    		totalMemoria.put(mesAnoCalculo+"_"+calculoId, total);
		    	}
		    	else if(calculoId == 142)
		    	{
		    		double valor1 = totalMemoria.get(mesAnoCalculo+"_"+99);
		    		
	    			total = valor1;
		    		
		    		totalMemoria.put(mesAnoCalculo+"_"+calculoId, total);
		    	}
		    	else if(calculoId == 143)
		    	{
		    		double valor1 = totalMemoria.get(mesAnoCalculo+"_"+100);
		    		
	    			total = valor1;
		    		
		    		totalMemoria.put(mesAnoCalculo+"_"+calculoId, total);
		    	}
		    	else if(calculoId == 144)
		    	{
		    		double valor1 = totalMemoria.get(mesAnoCalculo+"_"+101);
		    		
	    			total = valor1;
		    		
		    		totalMemoria.put(mesAnoCalculo+"_"+calculoId, total);
		    	}
		    	else if(calculoId == 145)
		    	{
		    		double valor1 = totalMemoria.get(mesAnoCalculo+"_"+116);
		    		
	    			total = valor1;
		    		
		    		totalMemoria.put(mesAnoCalculo+"_"+calculoId, total);
		    	}
		    	
		    	if(calculoId == 38 || calculoId == 52 || calculoId == 66 || calculoId == 120 || calculoId == 121 || calculoId == 122 || calculoId == 123)
		    	{
		    		celula = row.createCell(coluna);
					celula.setCellValue("No disponible");
					celula.setCellStyle(estiloTexto);
		    	}
		    	else if(calculoId == 85 || calculoId == 93)
		    	{
			    	celula = row.createCell(coluna);
			    	if(calculoId == 93)
			    		celula.setCellValue(new Double(total).intValue());
			    	else
			    		celula.setCellValue(formataValor.format(total));
					celula.setCellStyle(estiloTextoN);
		    	}
		    	else if(calculoId>= 86 && calculoId<= 92)
		    	{
			    	celula = row.createCell(coluna);
					celula.setCellValue(new Double(total).intValue());
					celula.setCellStyle(estiloTexto);
		    	}
		    	else
		    	{
			    	celula = row.createCell(coluna);
					celula.setCellValue(formataValor.format(total));
					celula.setCellStyle(estiloTexto);
		    	}
		    	
				if(dataInicio!=null)
					c.add(Calendar.MONTH, 1);
				else
					c.add(Calendar.YEAR, 1);
			}
			 
			linha++;
    	}
	    
	    
	    wb.write(stream);
		stream.flush();
		stream.close();
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
				"0107060000","0107010100","0107020100","0107050000","0401020000"
		};
		
		ClassificacaoContas cContas;
		for(int i = 0 ; i < contas.length ; i++)
		{
			cContas = (ClassificacaoContas) this.home.obterEntidadePorApelido(contas[i].trim());
			this.cContas.put(contas[i].trim(), cContas);
		}
		
		
		String[] contas2 = {"0401012001","0401012009","0401012003","0401012006","0401012002","0401012004","0401012005","0401012007","0402012002","0402012004","0402012005","0402012007","0401012008","0401022005"};
		
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
				
				total+=cContas.obterTotalizacaoExistenteAnual(aseguradoras, Integer.valueOf(new SimpleDateFormat("yyyy").format(data)));
			}
			else
			{
				Conta conta = (Conta) entidade;
				
				total+=conta.obterTotalizacaoExistenteAnual(aseguradoras, Integer.valueOf(new SimpleDateFormat("yyyy").format(data)));
			}
		}
		else
		{
			if(entidade instanceof ClassificacaoContas)
			{
				ClassificacaoContas cContas = (ClassificacaoContas) entidade;
				
				total+=cContas.obterTotalizacaoExistente(aseguradoras, new SimpleDateFormat("MMyyyy").format(data));
			}
			else
			{
				Conta conta = (Conta) entidade;
				
				total+=conta.obterTotalizacaoExistente(aseguradoras, new SimpleDateFormat("MMyyyy").format(data));
			}
		}
		
		
		return total;
	}
}
