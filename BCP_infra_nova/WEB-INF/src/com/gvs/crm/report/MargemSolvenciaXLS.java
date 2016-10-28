package com.gvs.crm.report;

import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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
import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.Period;

import com.gvs.crm.model.Aseguradora;
import com.gvs.crm.model.ClassificacaoContas;
import com.gvs.crm.model.Conta;
import com.gvs.crm.model.CotacaoDolar;
import com.gvs.crm.model.CotacaoDolarHome;
import com.gvs.crm.model.EntidadeHome;
import com.gvs.crm.model.Inscricao;
import com.gvs.crm.model.VariacaoIPC;
import com.gvs.crm.model.VariacaoIPCHome;

public class MargemSolvenciaXLS extends Excel
{
	private Map<String,ClassificacaoContas> cContas;
	private Map<String,Conta> contas;
	private  EntidadeHome home;
	
	public MargemSolvenciaXLS(Aseguradora aseguradora, Date dataInicio, Date dataFim, EntidadeHome home, CotacaoDolarHome cotacaoHome, VariacaoIPCHome variacaoHome) throws Exception
	{
		String caminho = "C:/tmp/" + new Date().getTime() + ".xls";
		this.home = home;
		
		FileOutputStream stream = new FileOutputStream(caminho);
		
		this.setCaminho(caminho);
		
		SimpleDateFormat formataData = new SimpleDateFormat("MM/yyyy");
		DecimalFormat formataValor = new DecimalFormat("#,##0.00");
		
		String dataInicioStr = formataData.format(dataInicio);
		String dataFimStr = formataData.format(dataFim);
		
		Date dataInicioReal = formataData.parse(dataInicioStr);
		Date dataFimReal = formataData.parse(dataFimStr);
		
		this.instanciarContas();
		
		HSSFWorkbook wb = new HSSFWorkbook();
		
		HSSFFont fonteTitulo = wb.createFont();
		fonteTitulo.setFontHeightInPoints((short)10);
	    fonteTitulo.setFontName("Arial");
	    fonteTitulo.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
	    
	    HSSFCellStyle estiloTitulo = wb.createCellStyle();
        estiloTitulo.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        estiloTitulo.setFont(fonteTitulo);
        
        HSSFFont fonteTexto = wb.createFont();
        fonteTexto.setFontHeightInPoints((short)10);
        fonteTexto.setFontName("Arial");
        
        HSSFCellStyle estiloTexto = wb.createCellStyle();
        estiloTexto.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        estiloTexto.setFont(fonteTexto);
        
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
		if(aseguradora!=null)
			celula.setCellValue("Aseguradora: " + aseguradora.obterNome());
		else
			celula.setCellValue("Aseguradora: Todas");
		celula.setCellStyle(estiloTitulo_E);
		Region r = new Region(linha, (short)0, linha, (short)50);
	    planilha.addMergedRegion(r);
	    
	    linha++;
	    
	    row = planilha.createRow(linha);
		celula = row.createCell(coluna);
		celula.setCellValue("Periodo: " + formataData.format(dataInicio) + " hasta " + formataData.format(dataFim));
		celula.setCellStyle(estiloTitulo_E);
	    r = new Region(linha, (short)0, linha, (short)50);
	    planilha.addMergedRegion(r);
        
	    linha+=2;
	    
	    c = Calendar.getInstance();
		c.setTime(dataInicioReal);
        
		coluna = 0;
		
		row = planilha.createRow(linha);
		celula = row.createCell(coluna);
		celula.setCellValue("MARGEN DE SOLVENCIA");
		celula.setCellStyle(estiloTituloTabela);
	    r = new Region(linha, (short)0, linha, (short)3);
	    planilha.addMergedRegion(r);
		
	    coluna = 4;
		
	    int cont = 0;
	    
		while(c.getTime().compareTo(dataFimReal)<=0)
		{
			celula = row.createCell(coluna);
			celula.setCellValue(formataData.format(c.getTime()));
			celula.setCellStyle(estiloTituloTabelaC);
			
			coluna++;
			
			cont++;
			c.add(Calendar.MONTH, 1);
		}
		
		String[] meses = new String[cont];
		c.setTime(dataInicioReal);
		cont = 0;
		
		while(c.getTime().compareTo(dataFimReal)<=0)
		{
			meses[cont] = formataData.format(c.getTime());
			cont++;
			c.add(Calendar.MONTH, 1);
		}
		
        
        Map<Integer,String> calculos = new TreeMap<Integer,String>();
        calculos.put(1,"Patrimonio Propio No comprometido o Patrimonio Tecnico");
        calculos.put(2,"Capital Integrado en Efectivo");
        calculos.put(3,"Capital Secundario");
        calculos.put(4,"Aportes a Capitalizar");
        calculos.put(5,"Reservas s/ Utilidades");
        calculos.put(6,"Reservas de Revaluo");
        calculos.put(7,"Resultados Acumulados");
        calculos.put(8,"Resultado Ejercicio");
        calculos.put(9,"Cargos diferidos");
        calculos.put(10,"Prestamos a directores y personal superior");
        calculos.put(11,"30% del valor de los inmuebles (terrenos y edificaciones), exceptuando los destinados a ventas");
        calculos.put(12,"Activos no calificados según los regimenes de inversion, representatividad, custodia de valores, y liquidez (reporte extracontable)");
        calculos.put(13,"Participacion en otras sociedades subsidiarias y afiliadas (reporte extracontable)");
        calculos.put(14,"El impuesto a la renta sobre los resultados acumulados al corte de cada periodo considerado, siempre que no coincida con el cierre del ejercicio financiero o no se halle asentado contablemente");
        calculos.put(15,"Propuesta de distribucion de resultados acumulados");
        calculos.put(16,"La porcion \"excedente\" de los capitales asegurados retenidos sobre los limites determinados en el Regimen de Retencion de Riesgos");
        calculos.put(17,"Capital minimo exigido a la fecha REFERENCIAL");
        calculos.put(18,"Margen de Solvencia Minimo Requerido");
        calculos.put(19,"Superavit/Deficit");
        calculos.put(20,"Coeficiente");
        
        //21 Não existe mais
        
        //Map<Integer,String> calculos2 = new TreeMap<Integer,String>();
        calculos.put(22,"Siniestros pagados");
        calculos.put(23,"Gastos de liquidación de Siniestro");
        calculos.put(24,"Siniestros Recuperados Reaseguros Cedidos íS");
        calculos.put(25,"Recupero de Siniestro");
        calculos.put(26,"Siniestros incurridos y gastos de liquidación de Siniestros  Netos de Recuperos y/o Salvatajes y Reaseguros Pasivos de los ultimos 36 meses");
        calculos.put(27,"Siniestros incurridos y Gastos de Liquidación Siniestros  netos de Recupero y/o salvatajes");
        calculos.put(28,"Factor de Retención");
        calculos.put(29,"Sumatoria de las Primas Devengadas");
        calculos.put(30,"Sumatoria de las Primas devengadas de Reaseguros Aceptados");
        calculos.put(31,"Sumatoria de las Primas devengadas de Reaseguros Cedidos y gastos de contratación de reaseguro");
        calculos.put(32,"Primas retenidas");
        calculos.put(33,"Primas");
        calculos.put(34,"Factor de Retención - Método Prospectivo");
        calculos.put(35,"Factor de Retención por aplicar (mayor valor entre 0,5; (2.3) y (2.6))");
        
        calculos.put(36,"Primas");
        calculos.put(37,"16% s/ el total de 3.1");
        calculos.put(38,"Margen en caso de un IPC > 15%");
        
        calculos.put(39,"Total");
        calculos.put(40,"Provisiones técnicas de Siniestros al cierre del periodo de referencia");
        calculos.put(41,"Provisiones técnicas de Siniestros al inicio del periodo de referencia");
        calculos.put(42,"Para compañías con menos de 12 meses de antiqüedad");
        calculos.put(43,"Para compañías entre 12 y 24 meses de antigüedad");
        calculos.put(44,"Para compañías con más de 24 meses de antigüedad");
        calculos.put(45,"23% s/ el valor obtenido en 4.4 o 4.4.1 o 4.4.2");
        calculos.put(46,"Multiplicar (4.5) por (2.7) (más ajuste por variación del IPC si > 45%)");

        String nomeCalculo;
        linha++;
        int calculoId;
        double total;
        Map<String, Double> totalMemoria = new TreeMap<String, Double>();
        CotacaoDolar cotacao;
        Inscricao inscricao;
         
        for(Iterator<Integer> i = calculos.keySet().iterator() ; i.hasNext() ; )
        {
        	calculoId = i.next();
        	
        	nomeCalculo = calculos.get(calculoId);
        	
        	coluna = 0;
        	
        	if(calculoId == 22)
        	{
        		row = planilha.createRow(linha);
            	
            	celula = row.createCell(coluna);
        		celula.setCellValue("FACTOR DE RETENCIÓN");
        		celula.setCellStyle(estiloTituloTabela);
        	    r = new Region(linha, (short)coluna, linha, (short)3);
        	    planilha.addMergedRegion(r);
        	}
        	else if(calculoId == 36)
        	{
        		row = planilha.createRow(linha);
            	
            	celula = row.createCell(coluna);
        		celula.setCellValue("Margen de Solvencia requerido en función de las primas");
        		celula.setCellStyle(estiloTituloTabela);
        	    r = new Region(linha, (short)coluna, linha, (short)3);
        	    planilha.addMergedRegion(r);
        	}
        	else if(calculoId == 39)
        	{
        		row = planilha.createRow(linha);
            	
            	celula = row.createCell(coluna);
        		celula.setCellValue("Margen de Solvencia requerido en función de los siniestros");
        		celula.setCellStyle(estiloTituloTabela);
        	    r = new Region(linha, (short)coluna, linha, (short)3);
        	    planilha.addMergedRegion(r);
        	}
        	
        	if(calculoId == 22 || calculoId == 36 || calculoId == 39)
        	{
        		coluna = 4;
        		
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
        	
        	row = planilha.createRow(linha);
        	
        	celula = row.createCell(coluna);
    		celula.setCellValue(nomeCalculo);
    		celula.setCellStyle(estiloTexto_E);
    	    r = new Region(linha, (short)coluna, linha, (short)3);
    	    planilha.addMergedRegion(r);
    	    
    	    coluna=4;
    	    c.setTime(dataInicioReal);
    	    
    	    while(c.getTime().compareTo(dataFimReal)<=0)
    		{
    	    	total = 0;
    	    	mes = new SimpleDateFormat("MM").format(c.getTime());
    			ano = new SimpleDateFormat("yyyy").format(c.getTime());
    			mesAnoCalculo = mes+ano;
    			
    			c3Anos = Calendar.getInstance();
    			c3Anos.setTime(c.getTime());
    			c3Anos.add(Calendar.YEAR, -3);
    			mesTresAnosAnterior = new SimpleDateFormat("MM").format(c3Anos.getTime()) + new SimpleDateFormat("yyyy").format(c3Anos.getTime());
    	    	
    	    	if(calculoId == 1) //Patrimonio Propio No comprometido o Patrimonio Tecnico
    	    	{
    	    		conta = cContas.get("0301010000");
    				double valor1 = conta.obterTotalizacaoExistente(aseguradora, mesAnoCalculo);
    				
    				conta = cContas.get("0301020000");
    				double valor2 = conta.obterTotalizacaoExistente(aseguradora, mesAnoCalculo);
    				
    				conta = cContas.get("0302010000");
    				double valor3 = conta.obterTotalizacaoExistente(aseguradora, mesAnoCalculo);
    				
    				conta = cContas.get("0303010000");
    				double valor4 = conta.obterTotalizacaoExistente(aseguradora, mesAnoCalculo);
    				
    				conta = cContas.get("0303020000");
    				double valor5 = conta.obterTotalizacaoExistente(aseguradora, mesAnoCalculo);
    				
    				conta = cContas.get("0304000000");
    				double valor6 = conta.obterTotalizacaoExistente(aseguradora, mesAnoCalculo);
    				
    				conta = cContas.get("0305000000");
    				double valor7 = conta.obterTotalizacaoExistente(aseguradora, mesAnoCalculo);
    				
    				total = valor1 + valor2 + valor3 + valor4 + valor5 + valor6 + valor7;
    				
    				totalMemoria.put(mesAnoCalculo+"_"+calculoId, total);
    	    	}
    	    	else if(calculoId == 2) //Capital Integrado en Efectivo
    	    	{
    	    		conta = cContas.get("0301010000");
    				double valor1 = conta.obterTotalizacaoExistente(aseguradora, mesAnoCalculo);
    				
    				total = valor1;
    	    	}
    	    	else if(calculoId == 3) //Capital Secundario
    	    	{
    	    		conta = cContas.get("0301020000");
    				double valor1 = conta.obterTotalizacaoExistente(aseguradora, mesAnoCalculo);
    				
    				total = valor1;
    	    	}
    	    	else if(calculoId == 4) //Aportes a Capitalizar
    	    	{
    	    		conta = cContas.get("0302010000");
    				double valor1 = conta.obterTotalizacaoExistente(aseguradora, mesAnoCalculo);
    				
    				total = valor1;
    	    	}
    	    	else if(calculoId == 5) //Reservas s/ Utilidades
    	    	{
    	    		conta = cContas.get("0303010000");
    				double valor1 = conta.obterTotalizacaoExistente(aseguradora, mesAnoCalculo);
    				
    				total = valor1;
    	    	}
    	    	else if(calculoId == 6) //Reservas de Revaluo
    	    	{
    	    		conta = cContas.get("0303020000");
    				double valor1 = conta.obterTotalizacaoExistente(aseguradora, mesAnoCalculo);
    				
    				total = valor1;
    	    	}
    	    	else if(calculoId == 7) //Resultados Acumulados
    	    	{
    	    		conta = cContas.get("0304000000");
    				double valor1 = conta.obterTotalizacaoExistente(aseguradora, mesAnoCalculo);
    				
    				total = valor1;
    	    	}
    	    	else if(calculoId == 8) //Resultado Ejercicio
    	    	{
    	    		conta = cContas.get("0305000000");
    				double valor1 = conta.obterTotalizacaoExistente(aseguradora, mesAnoCalculo);
    				
    				total = valor1;
    	    	}
    	    	else if(calculoId == 9) //Cargos diferidos
    	    	{
    	    		conta = cContas.get("0109010000");
    				double valor1 = conta.obterTotalizacaoExistente(aseguradora, mesAnoCalculo);
    				
    				total = valor1;
    				
    				totalMemoria.put(mesAnoCalculo+"_"+calculoId, total);
    	    	}
    	    	else if(calculoId == 10) //Prestamos a directores y personal superior
    	    	{
    	    		conta2 = contas.get("0101010201");
    				double valor1 = conta2.obterTotalizacaoExistente(aseguradora, mesAnoCalculo);
    				
    				conta2 = contas.get("0107050101");
    				double valor2 = conta2.obterTotalizacaoExistente(aseguradora, mesAnoCalculo);
    				
    				total = valor1 + valor2;
    				
    				totalMemoria.put(mesAnoCalculo+"_"+calculoId, total);
    	    	}
    	    	else if(calculoId == 11) //30% del valor de los inmuebles (terrenos y edificaciones), exceptuando los destinados a ventas
    	    	{
    	    		/*conta = cContas.get("0107000000");
    				double valor1 = conta.obterTotalizacaoExistente(aseguradora, mesAnoCalculo);
    				
    				conta = cContas.get("0108000000");
    				double valor2 = conta.obterTotalizacaoExistente(aseguradora, mesAnoCalculo);
    				
    				total = (valor1 + valor2) * 0.3;*/
    	    		
    	    		conta2 = contas.get("0900000001");
    				double valor1 = conta2.obterTotalizacaoExistente(aseguradora, mesAnoCalculo);
    				
    				total = valor1;
    				
    				totalMemoria.put(mesAnoCalculo+"_"+calculoId, total);
    	    	}
    	    	else if(calculoId == 12) //Activos no calificados según los regimenes de inversion, representatividad, custodia de valores, y liquidez (reporte extracontable)
    	    	{
    	    		conta2 = contas.get("0900000002");
    				double valor1 = conta2.obterTotalizacaoExistente(aseguradora, mesAnoCalculo);
    				
    				total = valor1;
    	    		
    	    		totalMemoria.put(mesAnoCalculo+"_"+calculoId, total);
    	    	}
    	    	else if(calculoId == 13) //Participacion en otras sociedades subsidiarias y afiliadas (reporte extracontable)
    	    	{
    	    		conta2 = contas.get("0900000003");
    				double valor1 = conta2.obterTotalizacaoExistente(aseguradora, mesAnoCalculo);
    				
    				total = valor1;
    	    		
    	    		totalMemoria.put(mesAnoCalculo+"_"+calculoId, total);
    	    	}
    	    	else if(calculoId == 14) //El impuesto a la renta sobre los resultados acumulados al corte de cada periodo considerado, siempre que no coincida con el cierre del ejercicio financiero o no se halle asentado contablemente
    	    	{
    	    		conta2 = contas.get("0900000004");
    				double valor1 = conta2.obterTotalizacaoExistente(aseguradora, mesAnoCalculo);
    				
    				total = valor1;
    	    		
    	    		totalMemoria.put(mesAnoCalculo+"_"+calculoId, total);
    	    	}
    	    	else if(calculoId == 15) //Propuesta de distribucion de resultados acumulados
    	    	{
    	    		conta2 = contas.get("0900000005");
    				double valor1 = conta2.obterTotalizacaoExistente(aseguradora, mesAnoCalculo);
    				
    				total = valor1;
    	    		
    	    		totalMemoria.put(mesAnoCalculo+"_"+calculoId, total);
    	    	}
    	    	else if(calculoId == 16) //La porcion \"excedente\" de los capitales asegurados retenidos sobre los limites determinados en el Regimen de Retencion de Riesgos
    	    	{
    	    		conta2 = contas.get("0900000006");
    				double valor1 = conta2.obterTotalizacaoExistente(aseguradora, mesAnoCalculo);
    				
    				total = valor1;
    	    		
    	    		totalMemoria.put(mesAnoCalculo+"_"+calculoId, total);
    	    	}
    	    	else if(calculoId == 17) //Capital minimo exigido a la fecha REFERENCIAL
    	    	{
    	    		/*double valor1 = totalMemoria.get(mesAnoCalculo+"_"+1);
    	    		double valor2 = totalMemoria.get(mesAnoCalculo+"_"+9);
    	    		double valor3 = totalMemoria.get(mesAnoCalculo+"_"+10);
    	    		double valor4 = totalMemoria.get(mesAnoCalculo+"_"+11);
    	    		double valor5 = totalMemoria.get(mesAnoCalculo+"_"+12);
    	    		double valor6 = totalMemoria.get(mesAnoCalculo+"_"+13);
    	    		double valor7 = totalMemoria.get(mesAnoCalculo+"_"+14);
    	    		double valor8 = totalMemoria.get(mesAnoCalculo+"_"+15);
    	    		double valor9 = totalMemoria.get(mesAnoCalculo+"_"+16);
    	    		
    	    		total = valor1 - valor2 - valor3 - valor4 - valor5 - valor6 - valor7 - valor8 - valor9;*/
    	    		
    	    		cotacao = cotacaoHome.obterCotacao(Integer.valueOf(mes), Integer.valueOf(ano));
    	    		if(cotacao!=null)
    	    		{
    	    			total = cotacao.obterCotacao() * 500000;
    	    			
    	    			System.out.println("obterCotacao " + total);
    	    		}
    	    		
    	    		totalMemoria.put(mesAnoCalculo+"_"+calculoId, total);
    	    	}
    	    	else if(calculoId == 18) //Margen de Solvencia Minimo Requerido
    	    	{
    	    		//Dúvida, Gilberto irá passar o cálculo correto
    	    		
    	    		totalMemoria.put(mesAnoCalculo+"_"+calculoId, total);
    	    	}
    	    	else if(calculoId == 19) //Superavit/Deficit
    	    	{
    	    		double valor1 = totalMemoria.get(mesAnoCalculo+"_"+1);
    	    		double valor2 = totalMemoria.get(mesAnoCalculo+"_"+18);
    	    		
    	    		total = valor1 - valor2;
    	    		
    	    		totalMemoria.put(mesAnoCalculo+"_"+calculoId, total);
    	    	}
    	    	else if(calculoId == 20) //Coeficiente
    	    	{
    	    		double valor1 = totalMemoria.get(mesAnoCalculo+"_"+1);
    	    		double valor2 = totalMemoria.get(mesAnoCalculo+"_"+18);
    	    		
    	    		if(valor2 > 0)
    	    			total = valor1 / valor2;
    	    		
    	    		totalMemoria.put(mesAnoCalculo+"_"+calculoId, total);
    	    	}
    	    	else if(calculoId == 22) //Siniestros pagados
    	    	{
    	    		conta = cContas.get("0506000000");
    				double valor1 = conta.obterTotalizacaoExistente(aseguradora, mesAnoCalculo);
    				conta = cContas.get("0513000000");
    				double valor2 = conta.obterTotalizacaoExistente(aseguradora, mesAnoCalculo);
    				conta2 = contas.get("0513012002");
    				double valor3 = conta2.obterTotalizacaoExistente(aseguradora, mesAnoCalculo);
    				conta2 = contas.get("0513012004");
    				double valor4 = conta2.obterTotalizacaoExistente(aseguradora, mesAnoCalculo);
    				conta2 = contas.get("0513012005");
    				double valor5 = conta2.obterTotalizacaoExistente(aseguradora, mesAnoCalculo);
    				conta2 = contas.get("0513012007");
    				double valor6 = conta2.obterTotalizacaoExistente(aseguradora, mesAnoCalculo);
    				conta2 = contas.get("0513022002");
    				double valor7 = conta2.obterTotalizacaoExistente(aseguradora, mesAnoCalculo);
    				conta2 = contas.get("0513022004");
    				double valor8 = conta2.obterTotalizacaoExistente(aseguradora, mesAnoCalculo);
    				conta2 = contas.get("0513022005");
    				double valor9 = conta2.obterTotalizacaoExistente(aseguradora, mesAnoCalculo);
    				conta2 = contas.get("0513022007");
    				double valor10 = conta2.obterTotalizacaoExistente(aseguradora, mesAnoCalculo);
    				conta = cContas.get("0515000000");
    				double valor11 = conta.obterTotalizacaoExistente(aseguradora, mesAnoCalculo);
    				conta2 = contas.get("0515012002");
    				double valor12 = conta2.obterTotalizacaoExistente(aseguradora, mesAnoCalculo);
    				conta2 = contas.get("0515012004");
    				double valor13 = conta2.obterTotalizacaoExistente(aseguradora, mesAnoCalculo);
    				conta2 = contas.get("0515012005");
    				double valor14 = conta2.obterTotalizacaoExistente(aseguradora, mesAnoCalculo);
    				conta2 = contas.get("0515012007");
    				double valor15 = conta2.obterTotalizacaoExistente(aseguradora, mesAnoCalculo);
    				conta2 = contas.get("0515022002");
    				double valor16 = conta2.obterTotalizacaoExistente(aseguradora, mesAnoCalculo);
    				conta2 = contas.get("0515022004");
    				double valor17 = conta2.obterTotalizacaoExistente(aseguradora, mesAnoCalculo);
    				conta2 = contas.get("0515022005");
    				double valor18 = conta2.obterTotalizacaoExistente(aseguradora, mesAnoCalculo);
    				conta2 = contas.get("0515022007");
    				double valor19 = conta2.obterTotalizacaoExistente(aseguradora, mesAnoCalculo);
    	    		
    	    		total = valor1 + valor2 - valor3 - valor4 - valor5 - valor6 - valor7 - valor8 - valor9 - valor10 + valor11 - valor12 - valor13 - valor14 - valor15 - valor16 - valor17 - valor18 - valor19;
    	    		
    	    		totalMemoria.put(mesAnoCalculo+"_"+calculoId, total);
    	    	}
    	    	else if(calculoId == 23) //Gastos de liquidación de Siniestro
    	    	{
    	    		conta = cContas.get("0508010000");
    				double valor1 = conta.obterTotalizacaoExistente(aseguradora, mesAnoCalculo);
    				conta2 = contas.get("0508012002");
    				double valor2 = conta2.obterTotalizacaoExistente(aseguradora, mesAnoCalculo);
    				conta2 = contas.get("0508012004");
    				double valor3 = conta2.obterTotalizacaoExistente(aseguradora, mesAnoCalculo);
    				conta2 = contas.get("0508012005");
    				double valor4 = conta2.obterTotalizacaoExistente(aseguradora, mesAnoCalculo);
    				conta2 = contas.get("0508012007");
    				double valor5 = conta2.obterTotalizacaoExistente(aseguradora, mesAnoCalculo);
    	    		
    	    		total = valor1 + valor2 - valor3 - valor4 - valor5;
    	    		
    	    		totalMemoria.put(mesAnoCalculo+"_"+calculoId, total);
    	    	}
    	    	else if(calculoId == 24) //Siniestros Recuperados Reaseguros Cedidos íS
    	    	{
    	    		conta = cContas.get("0408000000");
    				double valor1 = conta.obterTotalizacaoExistente(aseguradora, mesAnoCalculo);
    				conta2 = contas.get("0408012002");
    				double valor2 = conta2.obterTotalizacaoExistente(aseguradora, mesAnoCalculo);
    				conta2 = contas.get("0408012004");
    				double valor3 = conta2.obterTotalizacaoExistente(aseguradora, mesAnoCalculo);
    				conta2 = contas.get("0408012005");
    				double valor4 = conta2.obterTotalizacaoExistente(aseguradora, mesAnoCalculo);
    				conta2 = contas.get("0408012007");
    				double valor5 = conta2.obterTotalizacaoExistente(aseguradora, mesAnoCalculo);
    				conta2 = contas.get("0408022002");
    				double valor6 = conta2.obterTotalizacaoExistente(aseguradora, mesAnoCalculo);
    				conta2 = contas.get("0408022004");
    				double valor7 = conta2.obterTotalizacaoExistente(aseguradora, mesAnoCalculo);
    				conta2 = contas.get("0408022005");
    				double valor8 = conta2.obterTotalizacaoExistente(aseguradora, mesAnoCalculo);
    				conta2 = contas.get("0408022007");
    				double valor9 = conta2.obterTotalizacaoExistente(aseguradora, mesAnoCalculo);
    				conta = cContas.get("0509000000");
    				double valor10 = conta.obterTotalizacaoExistente(aseguradora, mesAnoCalculo);
    				conta2 = contas.get("0509012002");
    				double valor11 = conta2.obterTotalizacaoExistente(aseguradora, mesAnoCalculo);
    				conta2 = contas.get("0509012004");
    				double valor12 = conta2.obterTotalizacaoExistente(aseguradora, mesAnoCalculo);
    				conta2 = contas.get("0509012005");
    				double valor13 = conta2.obterTotalizacaoExistente(aseguradora, mesAnoCalculo);
    				conta2 = contas.get("0509012007");
    				double valor14 = conta2.obterTotalizacaoExistente(aseguradora, mesAnoCalculo);
    				conta2 = contas.get("0509022002");
    				double valor15 = conta2.obterTotalizacaoExistente(aseguradora, mesAnoCalculo);
    				conta2 = contas.get("0509022004");
    				double valor16 = conta2.obterTotalizacaoExistente(aseguradora, mesAnoCalculo);
    				conta2 = contas.get("0509022005");
    				double valor17 = conta2.obterTotalizacaoExistente(aseguradora, mesAnoCalculo);
    				conta2 = contas.get("0509022007");
    				double valor18 = conta2.obterTotalizacaoExistente(aseguradora, mesAnoCalculo);
    				conta = cContas.get("0511000000");
    				double valor19 = conta.obterTotalizacaoExistente(aseguradora, mesAnoCalculo);
    				conta2 = contas.get("0511012002");
    				double valor20 = conta2.obterTotalizacaoExistente(aseguradora, mesAnoCalculo);
    				conta2 = contas.get("0511012004");
    				double valor21 = conta2.obterTotalizacaoExistente(aseguradora, mesAnoCalculo);
    				conta2 = contas.get("0511012005");
    				double valor22 = conta2.obterTotalizacaoExistente(aseguradora, mesAnoCalculo);
    				conta2 = contas.get("0511012007");
    				double valor23 = conta2.obterTotalizacaoExistente(aseguradora, mesAnoCalculo);
    				conta2 = contas.get("0511022002");
    				double valor24 = conta2.obterTotalizacaoExistente(aseguradora, mesAnoCalculo);
    				conta2 = contas.get("0511022004");
    				double valor25 = conta2.obterTotalizacaoExistente(aseguradora, mesAnoCalculo);
    				conta2 = contas.get("0511022005");
    				double valor26 = conta2.obterTotalizacaoExistente(aseguradora, mesAnoCalculo);
    				conta2 = contas.get("0511022007");
    				double valor27 = conta2.obterTotalizacaoExistente(aseguradora, mesAnoCalculo);
    				conta = cContas.get("0409000000");
    				double valor28 = conta.obterTotalizacaoExistente(aseguradora, mesAnoCalculo);
    				conta2 = contas.get("0409012002");
    				double valor29 = conta2.obterTotalizacaoExistente(aseguradora, mesAnoCalculo);
    				conta2 = contas.get("0409012004");
    				double valor30 = conta2.obterTotalizacaoExistente(aseguradora, mesAnoCalculo);
    				conta2 = contas.get("0409012005");
    				double valor31 = conta2.obterTotalizacaoExistente(aseguradora, mesAnoCalculo);
    				conta2 = contas.get("0409012007");
    				double valor32 = conta2.obterTotalizacaoExistente(aseguradora, mesAnoCalculo);
    				conta2 = contas.get("0409022002");
    				double valor33 = conta2.obterTotalizacaoExistente(aseguradora, mesAnoCalculo);
    				conta2 = contas.get("0409022004");
    				double valor34 = conta2.obterTotalizacaoExistente(aseguradora, mesAnoCalculo);
    				conta2 = contas.get("0409022005");
    				double valor35 = conta2.obterTotalizacaoExistente(aseguradora, mesAnoCalculo);
    				conta2 = contas.get("0409022007");
    				double valor36 = conta2.obterTotalizacaoExistente(aseguradora, mesAnoCalculo);
    				
    				total = valor1 - valor2 - valor3 - valor4 - valor5 - valor6 - valor7 - valor8 - valor9 - valor10 + valor11 + valor12 + valor13 + valor14 + valor15 + valor16 + valor17 + valor18 - valor19 + 
    						valor20 + valor21 + valor22 + valor23 + valor24 + valor25 + valor26 + valor27 + valor28 - valor29 - valor30 - valor31 - valor32 - valor33 - valor34 - valor35 - valor36;
    	    		
    				
    	    		totalMemoria.put(mesAnoCalculo+"_"+calculoId, total);
    	    	}
    	    	else if(calculoId == 25) //Recupero de Siniestro
    	    	{
    	    		conta = cContas.get("0407000000");
    				double valor1 = conta.obterTotalizacaoExistente(aseguradora, mesAnoCalculo);
    				
    				total = valor1;
    				
    				totalMemoria.put(mesAnoCalculo+"_"+calculoId, total);
    	    	}
    	    	else if(calculoId == 26) //Siniestros incurridos y gastos de liquidación de Siniestros  Netos de Recuperos y/o Salvatajes y Reaseguros Pasivos de los ultimos 36 meses
    	    	{
    	    		double valor1 = totalMemoria.get(mesAnoCalculo+"_"+22);
    	    		double valor2 = totalMemoria.get(mesAnoCalculo+"_"+23);
    	    		double valor3 = totalMemoria.get(mesAnoCalculo+"_"+24);
    	    		double valor4 = totalMemoria.get(mesAnoCalculo+"_"+25);
    				
    				total = valor1 + valor2 + valor3 - valor4;
    				
    				totalMemoria.put(mesAnoCalculo+"_"+calculoId, total);
    	    	}
    	    	else if(calculoId == 27) //Siniestros incurridos y Gastos de Liquidación Siniestros  netos de Recupero y/o salvatajes
    	    	{
    	    		double valor1 = totalMemoria.get(mesAnoCalculo+"_"+22);
    	    		double valor2 = totalMemoria.get(mesAnoCalculo+"_"+23);
    	    		double valor3 = totalMemoria.get(mesAnoCalculo+"_"+25);
    				
    				total = valor1 + valor2 - valor3;
    				
    				totalMemoria.put(mesAnoCalculo+"_"+calculoId, total);
    	    	}
    	    	else if(calculoId == 28) //Factor de Retención
    	    	{
    	    		double valor1 = totalMemoria.get(mesAnoCalculo+"_"+26);
    	    		double valor2 = totalMemoria.get(mesAnoCalculo+"_"+27);
    				
    				if(valor2 > 0)
    					total = valor1 / valor2;
    				
    				totalMemoria.put(mesAnoCalculo+"_"+calculoId, total);
    	    	}
    	    	else if(calculoId == 29) //Sumatoria de las Primas Devengadas
    	    	{
    	    		conta = cContas.get("0401000000");
    				double valor1 = conta.obterTotalizacaoExistente(aseguradora, mesAnoCalculo);
    				conta2 = contas.get("0401012002");
    				double valor2 = conta2.obterTotalizacaoExistente(aseguradora, mesAnoCalculo);
    				conta2 = contas.get("0401012004");
    				double valor3 = conta2.obterTotalizacaoExistente(aseguradora, mesAnoCalculo);
    				conta2 = contas.get("0401012005");
    				double valor4 = conta2.obterTotalizacaoExistente(aseguradora, mesAnoCalculo);
    				conta2 = contas.get("0401012007");
    				double valor5 = conta2.obterTotalizacaoExistente(aseguradora, mesAnoCalculo);
    				
   					total = valor1 - valor2 - valor3 - valor4 - valor5;
    				
    				totalMemoria.put(mesAnoCalculo+"_"+calculoId, total);
    	    	}
    	    	else if(calculoId == 30) //Sumatoria de las Primas devengadas de Reaseguros Aceptados
    	    	{
    	    		conta = cContas.get("0402000000");
    				double valor1 = conta.obterTotalizacaoExistente(aseguradora, mesAnoCalculo);
    				conta2 = contas.get("0402012002");
    				double valor2 = conta2.obterTotalizacaoExistente(aseguradora, mesAnoCalculo);
    				conta2 = contas.get("0402012004");
    				double valor3 = conta2.obterTotalizacaoExistente(aseguradora, mesAnoCalculo);
    				conta2 = contas.get("0402012005");
    				double valor4 = conta2.obterTotalizacaoExistente(aseguradora, mesAnoCalculo);
    				conta2 = contas.get("0402012007");
    				double valor5 = conta2.obterTotalizacaoExistente(aseguradora, mesAnoCalculo);
    				conta2 = contas.get("0402022002");
    				double valor6 = conta2.obterTotalizacaoExistente(aseguradora, mesAnoCalculo);
    				conta2 = contas.get("0402022004");
    				double valor7 = conta2.obterTotalizacaoExistente(aseguradora, mesAnoCalculo);
    				conta2 = contas.get("0402022005");
    				double valor8 = conta2.obterTotalizacaoExistente(aseguradora, mesAnoCalculo);
    				conta2 = contas.get("0402022007");
    				double valor9 = conta2.obterTotalizacaoExistente(aseguradora, mesAnoCalculo);
    				conta = cContas.get("0403000000");
    				double valor10 = conta.obterTotalizacaoExistente(aseguradora, mesAnoCalculo);
    				conta2 = contas.get("0403012002");
    				double valor11 = conta2.obterTotalizacaoExistente(aseguradora, mesAnoCalculo);
    				conta2 = contas.get("0403012004");
    				double valor12 = conta2.obterTotalizacaoExistente(aseguradora, mesAnoCalculo);
    				conta2 = contas.get("0403012005");
    				double valor13 = conta2.obterTotalizacaoExistente(aseguradora, mesAnoCalculo);
    				conta2 = contas.get("0403012007");
    				double valor14 = conta2.obterTotalizacaoExistente(aseguradora, mesAnoCalculo);
    				conta2 = contas.get("0403022002");
    				double valor15 = conta2.obterTotalizacaoExistente(aseguradora, mesAnoCalculo);
    				conta2 = contas.get("0403022004");
    				double valor16 = conta2.obterTotalizacaoExistente(aseguradora, mesAnoCalculo);
    				conta2 = contas.get("0403022005");
    				double valor17 = conta2.obterTotalizacaoExistente(aseguradora, mesAnoCalculo);
    				conta2 = contas.get("0403022007");
    				double valor18 = conta2.obterTotalizacaoExistente(aseguradora, mesAnoCalculo);
    				
   					total = (valor1 - valor2 - valor3 - valor4 - valor5 - valor6 - valor7 - valor8 - valor9) + (valor10 - valor11 - valor12 - valor13 - valor14 - valor15 - valor16 - valor17 - valor18);
    				
    				totalMemoria.put(mesAnoCalculo+"_"+calculoId, total);
    	    	}
    	    	else if(calculoId == 31) //Sumatoria de las Primas devengadas de Reaseguros Cedidos y gastos de contratación de reaseguro
    	    	{
    	    		conta = cContas.get("0501000000");
    				double valor1 = conta.obterTotalizacaoExistente(aseguradora, mesAnoCalculo);
    				conta2 = contas.get("0501012002");
    				double valor2 = conta2.obterTotalizacaoExistente(aseguradora, mesAnoCalculo);
    				conta2 = contas.get("0501012004");
    				double valor3 = conta2.obterTotalizacaoExistente(aseguradora, mesAnoCalculo);
    				conta2 = contas.get("0501012005");
    				double valor4 = conta2.obterTotalizacaoExistente(aseguradora, mesAnoCalculo);
    				conta2 = contas.get("0501012007");
    				double valor5 = conta2.obterTotalizacaoExistente(aseguradora, mesAnoCalculo);
    				conta = cContas.get("0502000000");
    				double valor6 = conta.obterTotalizacaoExistente(aseguradora, mesAnoCalculo);
    				conta2 = contas.get("0502012002");
    				double valor7 = conta2.obterTotalizacaoExistente(aseguradora, mesAnoCalculo);
    				conta2 = contas.get("0502012004");
    				double valor8 = conta2.obterTotalizacaoExistente(aseguradora, mesAnoCalculo);
    				conta2 = contas.get("0502012005");
    				double valor9 = conta2.obterTotalizacaoExistente(aseguradora, mesAnoCalculo);
    				conta2 = contas.get("0502012007");
    				double valor10 = conta2.obterTotalizacaoExistente(aseguradora, mesAnoCalculo);
    				conta = cContas.get("0510010000");
    				double valor11 = conta.obterTotalizacaoExistente(aseguradora, mesAnoCalculo);
    				conta2 = contas.get("0510012002");
    				double valor12 = conta2.obterTotalizacaoExistente(aseguradora, mesAnoCalculo);
    				conta2 = contas.get("0510012004");
    				double valor13 = conta2.obterTotalizacaoExistente(aseguradora, mesAnoCalculo);
    				conta2 = contas.get("0510012005");
    				double valor14 = conta2.obterTotalizacaoExistente(aseguradora, mesAnoCalculo);
    				conta2 = contas.get("0510012007");
    				double valor15 = conta2.obterTotalizacaoExistente(aseguradora, mesAnoCalculo);
    				conta = cContas.get("0512010000");
    				double valor16 = conta.obterTotalizacaoExistente(aseguradora, mesAnoCalculo);
    				conta2 = contas.get("0512012002");
    				double valor17 = conta2.obterTotalizacaoExistente(aseguradora, mesAnoCalculo);
    				conta2 = contas.get("0512012004");
    				double valor18 = conta2.obterTotalizacaoExistente(aseguradora, mesAnoCalculo);
    				conta2 = contas.get("0512012005");
    				double valor19 = conta2.obterTotalizacaoExistente(aseguradora, mesAnoCalculo);
    				conta2 = contas.get("0512012007");
    				double valor20 = conta2.obterTotalizacaoExistente(aseguradora, mesAnoCalculo);
    				
   					total = valor1 - valor2 - valor3 - valor4 - valor5 + valor6 - valor7 - valor8 - valor9 + valor10 + valor11 - valor12 - valor13 - valor14 - valor15 + valor16 - valor17 - valor18 - valor19 - valor20;
    				
    				totalMemoria.put(mesAnoCalculo+"_"+calculoId, total);
    	    	}
    	    	else if(calculoId == 32) //Primas retenidas
    	    	{
    	    		double valor1 = totalMemoria.get(mesAnoCalculo+"_"+29);
    	    		double valor2 = totalMemoria.get(mesAnoCalculo+"_"+30);
    	    		double valor3 = totalMemoria.get(mesAnoCalculo+"_"+31);
    				
   					total = valor1 + valor2 - valor3;
    				
    				totalMemoria.put(mesAnoCalculo+"_"+calculoId, total);
    	    	}
    	    	else if(calculoId == 33) //Primas
    	    	{
    	    		double valor1 = totalMemoria.get(mesAnoCalculo+"_"+29);
    	    		double valor2 = totalMemoria.get(mesAnoCalculo+"_"+30);
    				
   					total = valor1 + valor2;
    				
    				totalMemoria.put(mesAnoCalculo+"_"+calculoId, total);
    	    	}
    	    	else if(calculoId == 34) //Factor de Retención - Método Prospectivo
    	    	{
    	    		double valor1 = totalMemoria.get(mesAnoCalculo+"_"+32);
    	    		double valor2 = totalMemoria.get(mesAnoCalculo+"_"+33);
    				
    	    		if(valor2 > 0)
    	    			total = valor1 / valor2;
    				
    				totalMemoria.put(mesAnoCalculo+"_"+calculoId, total);
    	    	}
    	    	else if(calculoId == 35) //Factor de Retención por aplicar (mayor valor entre 0,5; (2.3) y (2.6))
    	    	{
    	    		double valor1 = totalMemoria.get(mesAnoCalculo+"_"+28);
    	    		double valor2 = totalMemoria.get(mesAnoCalculo+"_"+34);
    				
    	    		if(valor1>valor2)
    	    			total = valor1;
    	    		else
    	    			total = valor2;
    	    		
    	    		if(total<0.5)
    	    			total = 0.5;
    	    		
    				totalMemoria.put(mesAnoCalculo+"_"+calculoId, total);
    	    	}
    	    	else if(calculoId == 36) //Primas
    	    	{
    	    		double valor1 = totalMemoria.get(mesAnoCalculo+"_"+29);
    	    		double valor2 = totalMemoria.get(mesAnoCalculo+"_"+30);
    				
   					total = valor1 + valor2;
    				
    				totalMemoria.put(mesAnoCalculo+"_"+calculoId, total);
    	    	}
    	    	else if(calculoId == 37) //16% s/ el total de 3.1
    	    	{
    	    		double valor1 = totalMemoria.get(mesAnoCalculo+"_"+36);
    				
   					total = valor1 * 0.16;
    				
    				totalMemoria.put(mesAnoCalculo+"_"+calculoId, total);
    	    	}
    	    	else if(calculoId == 38) //Margen en caso de un IPC > 15%
    	    	{
    	    		double valor1 = totalMemoria.get(mesAnoCalculo+"_"+37);
    	    		double valor2 = totalMemoria.get(mesAnoCalculo+"_"+35);
    				
   					total = valor1 * valor2;
    				
    				totalMemoria.put(mesAnoCalculo+"_"+calculoId, total);
    	    	}
    	    	else if(calculoId == 39) //Total
    	    	{
    	    		double valor1 = totalMemoria.get(mesAnoCalculo+"_"+22);
    	    		double valor2 = totalMemoria.get(mesAnoCalculo+"_"+23);
    				
   					total = valor1 + valor2;
    				
    				totalMemoria.put(mesAnoCalculo+"_"+calculoId, total);
    	    	}
    	    	else if(calculoId == 40) //Provisiones técnicas de Siniestros al cierre del periodo de referencia
    	    	{
    	    		conta = cContas.get("0213010100");
    				double valor1 = conta.obterTotalizacaoExistente(aseguradora, mesTresAnosAnterior);
    				conta = cContas.get("0213010300");
    				double valor2 = conta.obterTotalizacaoExistente(aseguradora, mesTresAnosAnterior);
    				conta = cContas.get("0213020100");
    				double valor3 = conta.obterTotalizacaoExistente(aseguradora, mesTresAnosAnterior);
    				conta = cContas.get("0213020300");
    				double valor4 = conta.obterTotalizacaoExistente(aseguradora, mesTresAnosAnterior);
    				conta = cContas.get("0213030100");
    				double valor5 = conta.obterTotalizacaoExistente(aseguradora, mesTresAnosAnterior);
    				conta = cContas.get("0213030300");
    				double valor6 = conta.obterTotalizacaoExistente(aseguradora, mesTresAnosAnterior);
    				conta = cContas.get("0213040100");
    				double valor7 = conta.obterTotalizacaoExistente(aseguradora, mesTresAnosAnterior);
    				
    				
    				total = valor1 + valor2 + valor3 + valor4 + valor5 + valor6 + valor7;
    				
    				totalMemoria.put(mesAnoCalculo+"_"+calculoId, total);
    	    	}
    	    	else if(calculoId == 41) //Provisiones técnicas de Siniestros al inicio del periodo de referencia
    	    	{
    	    		conta = cContas.get("0213010100");
    				double valor1 = conta.obterTotalizacaoExistente(aseguradora, mesAnoCalculo);
    				conta = cContas.get("0213010300");
    				double valor2 = conta.obterTotalizacaoExistente(aseguradora, mesAnoCalculo);
    				conta = cContas.get("0213020100");
    				double valor3 = conta.obterTotalizacaoExistente(aseguradora, mesAnoCalculo);
    				conta = cContas.get("0213020300");
    				double valor4 = conta.obterTotalizacaoExistente(aseguradora, mesAnoCalculo);
    				conta = cContas.get("0213030100");
    				double valor5 = conta.obterTotalizacaoExistente(aseguradora, mesAnoCalculo);
    				conta = cContas.get("0213030300");
    				double valor6 = conta.obterTotalizacaoExistente(aseguradora, mesAnoCalculo);
    				conta = cContas.get("0213040100");
    				double valor7 = conta.obterTotalizacaoExistente(aseguradora, mesAnoCalculo);
    				
    				
    				total = valor1 + valor2 + valor3 + valor4 + valor5 + valor6 + valor7;
    				
    				totalMemoria.put(mesAnoCalculo+"_"+calculoId, total);
    	    	}
    	    	else if(calculoId == 42) //Para compañías con menos de 12 meses de antiqüedad
    	    	{
    	    		inscricao = aseguradora.obterUltimaInscricao();
    	    		if(inscricao!=null)
    	    		{
    	    			Date dataInicioInscricao = inscricao.obterDataResolucao();
    	    			
    	    			int anoInscricao = Integer.valueOf(new SimpleDateFormat("yyyy").format(dataInicioInscricao));
    	    			int mesInscricao = Integer.valueOf(new SimpleDateFormat("MM").format(dataInicioInscricao));
    	    			int diaInscricao = Integer.valueOf(new SimpleDateFormat("dd").format(dataInicioInscricao));
    	    			
    	    			int anoAtual = Integer.valueOf(new SimpleDateFormat("yyyy").format(c.getTime()));
    	    			int mesAtual = Integer.valueOf(new SimpleDateFormat("MM").format(c.getTime()));
    	    			int diaAtual = Integer.valueOf(new SimpleDateFormat("dd").format(c.getTime()));
    	    			
    	    			DateTime dtInicio = new DateTime(anoInscricao, mesInscricao, diaInscricao, 0, 0);
    	    			DateTime dtFinal = new DateTime(anoAtual, mesAtual, diaAtual, 0, 0);
    	    			
    	    			Interval intervalo = new Interval(dtInicio, dtFinal);
    	    			
    	    			Period period = intervalo.toPeriod();
    	    			
    	    			/*System.out.println("dtInicio " + new SimpleDateFormat("dd/MM/yyyy").format(dataInicioInscricao));
    	    			System.out.println("dtFinal " + new SimpleDateFormat("dd/MM/yyyy").format(c.getTime()));
    	    			System.out.println("Meses " + period.getMonths());
    	    			System.out.println("Anos " + period.getYears());*/
    	    			
    	    			if(period.getYears() == 0)
    	    			{
    	    				double valor1 = totalMemoria.get(mesAnoCalculo+"_"+39);
            	    		double valor2 = totalMemoria.get(mesAnoCalculo+"_"+40);
            	    		double valor3 = totalMemoria.get(mesAnoCalculo+"_"+41);
            	    		
            	    		total = valor1 + valor2 - valor3;
    	    			}
    	    		}
    	    		
    				totalMemoria.put(mesAnoCalculo+"_"+calculoId, total);
    	    	}
    	    	else if(calculoId == 43) //Para compañías entre 12 y 24 meses de antigüedad
    	    	{
    	    		inscricao = aseguradora.obterUltimaInscricao();
    	    		if(inscricao!=null)
    	    		{
    	    			Date dataInicioInscricao = inscricao.obterDataResolucao();
    	    			
    	    			int anoInscricao = Integer.valueOf(new SimpleDateFormat("yyyy").format(dataInicioInscricao));
    	    			int mesInscricao = Integer.valueOf(new SimpleDateFormat("MM").format(dataInicioInscricao));
    	    			int diaInscricao = Integer.valueOf(new SimpleDateFormat("dd").format(dataInicioInscricao));
    	    			
    	    			int anoAtual = Integer.valueOf(new SimpleDateFormat("yyyy").format(c.getTime()));
    	    			int mesAtual = Integer.valueOf(new SimpleDateFormat("MM").format(c.getTime()));
    	    			int diaAtual = Integer.valueOf(new SimpleDateFormat("dd").format(c.getTime()));
    	    			
    	    			DateTime dtInicio = new DateTime(anoInscricao, mesInscricao, diaInscricao, 0, 0);
    	    			DateTime dtFinal = new DateTime(anoAtual, mesAtual, diaAtual, 0, 0);
    	    			
    	    			Interval intervalo = new Interval(dtInicio, dtFinal);
    	    			
    	    			Period period = intervalo.toPeriod();
    	    			
    	    			/*System.out.println("dtInicio " + new SimpleDateFormat("dd/MM/yyyy").format(dataInicioInscricao));
    	    			System.out.println("dtFinal " + new SimpleDateFormat("dd/MM/yyyy").format(c.getTime()));
    	    			System.out.println("Meses " + period.getMonths());
    	    			System.out.println("Anos " + period.getYears());*/
    	    			
    	    			if(period.getYears() == 1 || period.getYears() == 2)
    	    			{
    	    				double valor1 = totalMemoria.get(mesAnoCalculo+"_"+39);
            	    		double valor2 = totalMemoria.get(mesAnoCalculo+"_"+40);
            	    		double valor3 = totalMemoria.get(mesAnoCalculo+"_"+41);
            	    		
            	    		total = (valor1 + valor2 - valor3) / 2;
    	    			}
    	    		}
    	    		
    				totalMemoria.put(mesAnoCalculo+"_"+calculoId, total);
    	    	}
    	    	else if(calculoId == 44) //Para compañías con más de 24 meses de antigüedad
    	    	{
    	    		inscricao = aseguradora.obterUltimaInscricao();
    	    		if(inscricao!=null)
    	    		{
    	    			Date dataInicioInscricao = inscricao.obterDataResolucao();
    	    			
    	    			int anoInscricao = Integer.valueOf(new SimpleDateFormat("yyyy").format(dataInicioInscricao));
    	    			int mesInscricao = Integer.valueOf(new SimpleDateFormat("MM").format(dataInicioInscricao));
    	    			int diaInscricao = Integer.valueOf(new SimpleDateFormat("dd").format(dataInicioInscricao));
    	    			
    	    			int anoAtual = Integer.valueOf(new SimpleDateFormat("yyyy").format(c.getTime()));
    	    			int mesAtual = Integer.valueOf(new SimpleDateFormat("MM").format(c.getTime()));
    	    			int diaAtual = Integer.valueOf(new SimpleDateFormat("dd").format(c.getTime()));
    	    			
    	    			DateTime dtInicio = new DateTime(anoInscricao, mesInscricao, diaInscricao, 0, 0);
    	    			DateTime dtFinal = new DateTime(anoAtual, mesAtual, diaAtual, 0, 0);
    	    			
    	    			Interval intervalo = new Interval(dtInicio, dtFinal);
    	    			
    	    			Period period = intervalo.toPeriod();
    	    			
    	    			/*System.out.println("dtInicio " + new SimpleDateFormat("dd/MM/yyyy").format(dataInicioInscricao));
    	    			System.out.println("dtFinal " + new SimpleDateFormat("dd/MM/yyyy").format(c.getTime()));
    	    			System.out.println("Meses " + period.getMonths());
    	    			System.out.println("Anos " + period.getYears());*/
    	    			
    	    			if(period.getYears() > 2)
    	    			{
    	    				double valor1 = totalMemoria.get(mesAnoCalculo+"_"+39);
            	    		double valor2 = totalMemoria.get(mesAnoCalculo+"_"+40);
            	    		double valor3 = totalMemoria.get(mesAnoCalculo+"_"+41);
            	    		
            	    		total = (valor1 + valor2 - valor3) / 3;
    	    			}
    	    		}
    	    		
    				totalMemoria.put(mesAnoCalculo+"_"+calculoId, total);
    	    	}
    	    	else if(calculoId == 45) //23% s/ el valor obtenido en 4.4
    	    	{
    	    		inscricao = aseguradora.obterUltimaInscricao();
    	    		if(inscricao!=null)
    	    		{
    	    			Date dataInicioInscricao = inscricao.obterDataResolucao();
    	    			
    	    			int anoInscricao = Integer.valueOf(new SimpleDateFormat("yyyy").format(dataInicioInscricao));
    	    			int mesInscricao = Integer.valueOf(new SimpleDateFormat("MM").format(dataInicioInscricao));
    	    			int diaInscricao = Integer.valueOf(new SimpleDateFormat("dd").format(dataInicioInscricao));
    	    			
    	    			int anoAtual = Integer.valueOf(new SimpleDateFormat("yyyy").format(c.getTime()));
    	    			int mesAtual = Integer.valueOf(new SimpleDateFormat("MM").format(c.getTime()));
    	    			int diaAtual = Integer.valueOf(new SimpleDateFormat("dd").format(c.getTime()));
    	    			
    	    			DateTime dtInicio = new DateTime(anoInscricao, mesInscricao, diaInscricao, 0, 0);
    	    			DateTime dtFinal = new DateTime(anoAtual, mesAtual, diaAtual, 0, 0);
    	    			
    	    			Interval intervalo = new Interval(dtInicio, dtFinal);
    	    			
    	    			Period period = intervalo.toPeriod();
    	    			
    	    			/*System.out.println("dtInicio " + new SimpleDateFormat("dd/MM/yyyy").format(dataInicioInscricao));
    	    			System.out.println("dtFinal " + new SimpleDateFormat("dd/MM/yyyy").format(c.getTime()));
    	    			System.out.println("Meses " + period.getMonths());
    	    			System.out.println("Anos " + period.getYears());*/
    	    			
    	    			if(period.getYears() == 0)
    	    			{
    	    				double valor1 = totalMemoria.get(mesAnoCalculo+"_"+42);
            	    		
            	    		total = valor1 * 0.23;
    	    			}
    	    			else if(period.getYears() == 1 || period.getYears() == 2)
    	    			{
    	    				double valor1 = totalMemoria.get(mesAnoCalculo+"_"+43);
            	    		
            	    		total = valor1 * 0.23;
    	    			}
    	    			else if(period.getYears() > 2)
    	    			{
    	    				double valor1 = totalMemoria.get(mesAnoCalculo+"_"+44);
            	    		
    	    				total = valor1 * 0.23;
    	    			}
    	    		}
    	    		
    				totalMemoria.put(mesAnoCalculo+"_"+calculoId, total);
    	    	}
    	    	else if(calculoId == 46) //Multiplicar (4.5) por (2.7) (más ajuste por variación del IPC si > 45%)
    	    	{
    	    		VariacaoIPC ipc = variacaoHome.obterVariacao(Integer.valueOf(mes), Integer.valueOf(ano));
    	    		if(ipc!=null)
    	    		{
    	    			double variacao = ipc.obterVariacao();
    	    			if(variacao>0.45)
    	    			{
    	    				double diferenca = (variacao - 0.45) * 1;
    	    				
    	    				double valor1 = totalMemoria.get(mesAnoCalculo+"_"+45);
    	    				double valor2 = totalMemoria.get(mesAnoCalculo+"_"+35);
    	    				
    	    				total = (valor1 * valor2) * diferenca;
    	    			}
    	    		}
    	    		
    				totalMemoria.put(mesAnoCalculo+"_"+calculoId, total);
    	    	}
    	    	
    	    	//Para tirar o -0.00
    	    	if(total == 0.00)
    	    		total = 0;
    	    	
    			celula = row.createCell(coluna);
    			celula.setCellValue(formataValor.format(total));
    			celula.setCellStyle(estiloTexto);
    			
    			coluna++;
    			
    			c.add(Calendar.MONTH, 1);
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
		
		String[] contas = {"0301010000","0301020000","0302010000","0303010000","0303020000","0304000000","0305000000","0109010000","0107000000","0108000000","0506000000","0513000000","0515000000","0508010000","0408000000","0509000000","0511000000",
							"0409000000","0407000000","0401000000","0402000000","0403000000","0501000000","0502000000","0510010000","0512010000","0213010100","0213010300","0213020100","0213020300","0213030100","0213030300","0213040100"
		};
		
		ClassificacaoContas cContas;
		for(int i = 0 ; i < contas.length ; i++)
		{
			cContas = (ClassificacaoContas) this.home.obterEntidadePorApelido(contas[i].trim());
			this.cContas.put(contas[i].trim(), cContas);
		}
		
		
		String[] contas2 = {"0101010201","0107050101","0513012002","0513012004","0513012005","0513012007","0513022002","0513022004","0513022005","0513022007","0515012002","0515012004","0515012005","0515012007","0515022002","0515022004","0515022005",
				"0515022007","0508012002","0508012004","0508012005","0508012007","0408012002","0408012004","0408012005","0408012007","0408022002","0408022004","0408022005","0408022007","0509012002","0509012004","0509012005","0509012007","0509022002",
				"0509022004","0509022005","0509022007","0511012002","0511012004","0511012005","0511012007","0511022002","0511022004","0511022005","0511022007","0409012002","0409012004","0409012005","0409012007","0409022002","0409022004","0409022005",
				"0409022007","0401012002","0401012004","0401012005","0401012007","0402012002","0402012004","0402012005","0402012007","0402022002","0402022004","0402022005","0402022007","0403012002","0403012004","0403012005","0403012007","0403022002",
				"0403022004","0403022005","0403022007","0501012002","0501012004","0501012005","0501012007","0502012002","0502012004","0502012005","0502012007","0510012002","0510012004","0510012005","0510012007","0512012002","0512012004","0512012005",
				"0512012007","0900000001","0900000002","0900000003","0900000004","0900000005","0900000006"
				
		};
		
		Conta conta;
		for(int i = 0 ; i < contas2.length ; i++)
		{
			conta = (Conta) this.home.obterEntidadePorApelido(contas2[i].trim());
			this.contas.put(contas2[i].trim(), conta);
		}
	}
}
