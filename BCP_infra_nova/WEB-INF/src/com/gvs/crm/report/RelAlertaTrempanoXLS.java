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

import com.gvs.crm.model.Aseguradora;
import com.gvs.crm.model.AseguradoraHome;
import com.gvs.crm.model.ClassificacaoContas;
import com.gvs.crm.model.Conta;
import com.gvs.crm.model.Entidade;
import com.gvs.crm.model.EntidadeHome;

public class RelAlertaTrempanoXLS extends Excel
{
	private  EntidadeHome home;
	private Map<String,ClassificacaoContas> cContas;
	private Map<String,Conta> contas;
	private boolean calculoAnual,anoFiscal;
	
	
	public RelAlertaTrempanoXLS(Collection<Aseguradora> aseguradoras, Date dataInicio, Date dataFim, int anoInicio, int anoFim, EntidadeHome home, AseguradoraHome aseguradoraHome, boolean anoFiscal) throws Exception
	{
		String caminho = "C:/tmp/" + new Date().getTime() + ".xls";
		this.home = home;
		this.anoFiscal = anoFiscal;
		
		FileOutputStream stream = new FileOutputStream(caminho);
		
		this.setCaminho(caminho);
		
		SimpleDateFormat formataData = new SimpleDateFormat("MM/yyyy");
		SimpleDateFormat formataDataAno = new SimpleDateFormat("yyyy");
		DecimalFormat formataValor = new DecimalFormat("#,##0.00");
		Date dataInicioReal,dataFimReal;
		
		if(dataInicio!=null)
		{
			String dataInicioStr = formataData.format(dataInicio);
			String dataFimStr = formataData.format(dataFim);
			
			dataInicioReal = formataData.parse(dataInicioStr);
			dataFimReal = formataData.parse(dataFimStr);
		}
		else
		{
			String dataInicioStr;
			String dataFimStr;
			
			dataInicioStr = "01/"+anoInicio;
			dataFimStr = "12/"+anoFim;
			
			dataInicioReal = formataData.parse(dataInicioStr);
			dataFimReal = formataData.parse(dataFimStr);
			
			calculoAnual = true;
		}
		
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
        
        HSSFCellStyle estiloTextoD = wb.createCellStyle();
        estiloTextoD.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
        estiloTextoD.setFont(fonteTexto);
        
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
        
        this.instanciarContas();
        
        Map<String,Collection<String>> calculos = new TreeMap<String, Collection<String>>();
		
		Collection<String> calc = new ArrayList<String>();
		calc.add("Prima neta / capital");
		calc.add("Reservas / capital");
		calc.add("Excedentes de capital y capital mínimo");
		
		calculos.put("Suficiencia de capital", calc);
		
		calc = new ArrayList<String>();
		calc.add("Activos fijos, valores no líquidos y las cuentas por cobrar / activos");
		calc.add("Cuentas a cobrar / activos");
		calc.add("Valores negociables / activos totales");
		
		calculos.put("Calidad de los activos", calc);
		
		calc = new ArrayList<String>();
		calc.add("Prima neta a la prima bruta");
		
		calculos.put("Reaseguro y reservas", calc);
		
		calc = new ArrayList<String>();
		calc.add("Evolucion de Primas netas Anulaciones");
		calc.add("Evolucion de las Primas netas de Reaseguros");
		calc.add("Demandas en disputa / reclamación total pagada");
		calc.add("Excedentes de capital/capital mínimo");
		calc.add("Siniestros Pendientes / Patrimonio  Neto");
		calc.add("Retorno sobre el Patrimonio Neto");
		calc.add("Margen sobre Primas Netas de Reaseguros");
		calc.add("Gastos Operativos/ Primas Netas de Anulaciones");
		calc.add("Gastos Brutos de Producción / Primas Netas de Anulaciones");
		calc.add("Provisiones Técnicas / Primas Netas de Anulaciones");
		calc.add("Provisiones Técnicas / Siniestros");
		calc.add("Siniestros pagados/Provision de siniestros");
		
		calculos.put("Gestión", calc);
		
		calc = new ArrayList<String>();
		calc.add("Renta de Inversiones");
		calc.add("Resultado Tecnico Neto/Primas Netas de Reaseguros");
		calc.add("Ratio de Ganancias (Ingresos - Egresos / Primas Directas)");
		
		calculos.put("Ganancia", calc);
        
        int linha = 1;
		int coluna = 0;
		int colunaFinal = 0;
		
		HSSFRow row;
		HSSFCell celula;
		Region r;
		Calendar c;
		String mes,ano,mesTresAnosAnterior,mesDoisAnosAnterior, mesUmAnoAnterior, mesAnoCalculo,nomeGrupo;
		Calendar c1Ano, c2Anos,c3Anos;
		BigDecimal total, porCem;
		ClassificacaoContas conta;
		Conta conta2;
		Collection<Entidade> todasAseguradora = home.obterAseguradorasSemCoaseguradora();
		porCem = new BigDecimal("100");
		Collection<String> calculos2;
		Collection<Entidade> grupoAseguradora;
		Date dataCalculo;
		
		row = planilha.createRow(linha);
		celula = row.createCell(coluna);
		if(dataInicio!=null)
			celula.setCellValue("ALERTA TREMPANA - de " + formataData.format(dataInicio) + " hasta " + formataData.format(dataFim));
		else
		{
			if(anoFiscal)
				celula.setCellValue("ALERTA TREMPANA - año fiscal de " + formataDataAno.format(dataInicioReal) + " hasta " + formataDataAno.format(dataFimReal));
			else
				celula.setCellValue("ALERTA TREMPANA - de " + formataDataAno.format(dataInicioReal) + " hasta " + formataDataAno.format(dataFimReal));
		}
		celula.setCellStyle(estiloTitulo);
		r = new Region(linha, (short)coluna, linha, (short)10);
		planilha.addMergedRegion(r);
		linha++;
		
		for(Aseguradora aseguradora : aseguradoras)
		{
			coluna = 0;
			
			nomeGrupo = aseguradora.obterGrupoAlertaTrempana();
			if(nomeGrupo.startsWith("Capiltal"))
				nomeGrupo = "Capital";
			
			grupoAseguradora = aseguradoraHome.obterGrupoAlertaTrempana(aseguradora);
			
			row = planilha.createRow(linha);
			celula = row.createCell(coluna);
			celula.setCellValue("Aseguradora: " + aseguradora.obterNome());
			celula.setCellStyle(estiloTitulo_E);
			r = new Region(linha, (short)coluna, linha, (short)200);
			planilha.addMergedRegion(r);
			linha++;
			
			c = Calendar.getInstance();
			c.setTime(dataFimReal);
			
			row = planilha.createRow(linha);
			while(c.getTime().compareTo(dataInicioReal)>=0)
			{
				if(coluna == 0)
					colunaFinal = coluna + 4;
				else
					colunaFinal = coluna + 2;
				
				celula = row.createCell(coluna);
				if(dataInicio!=null)
					celula.setCellValue("Periodo " + formataData.format(c.getTime()));
				else
					celula.setCellValue("Periodo " + formataDataAno.format(c.getTime()));
				celula.setCellStyle(estiloTextoN_E);
				r = new Region(linha, (short)coluna, linha, (short)colunaFinal);
				planilha.addMergedRegion(r);
				
				if(dataInicio!=null)
					c.add(Calendar.MONTH, -1);
				else
					c.add(Calendar.YEAR, -1);
				
				coluna = ++colunaFinal;
			}
			
			linha++;
			c = Calendar.getInstance();
			c.setTime(dataFimReal);
			
			coluna = -1;
			row = planilha.createRow(linha);
			celula = row.createCell(++coluna);
			celula.setCellValue("Clasificación");
			celula.setCellStyle(estiloTituloTabelaC);
			
			celula = row.createCell(++coluna);
			celula.setCellValue("Ratio");
			celula.setCellStyle(estiloTituloTabelaC);
			
			while(c.getTime().compareTo(dataInicioReal)>=0)
			{
				celula = row.createCell(++coluna);
				celula.setCellValue("Ratio Aseguradora");
				celula.setCellStyle(estiloTituloTabelaC);
				
				celula = row.createCell(++coluna);
				celula.setCellValue("Ratio Grupo " + nomeGrupo);
				celula.setCellStyle(estiloTituloTabelaC);
				
				celula = row.createCell(++coluna);
				celula.setCellValue("Ratio Mercado");
				celula.setCellStyle(estiloTituloTabelaC);
				
				if(dataInicio!=null)
					c.add(Calendar.MONTH, -1);
				else
					c.add(Calendar.YEAR, -1);
			}
			
			for(String grupo : calculos.keySet())
			{
				calculos2 = calculos.get(grupo);
				
				for(String nomeCalculo : calculos2)
				{
					coluna = -1;
					
					total = new BigDecimal("0.00");
					
					row = planilha.createRow(++linha);
					celula = row.createCell(++coluna);
					celula.setCellValue(grupo);
					celula.setCellStyle(estiloTexto);
					
					celula = row.createCell(++coluna);
					celula.setCellValue(nomeCalculo);
					celula.setCellStyle(estiloTexto);
					
					c = Calendar.getInstance();
					c.setTime(dataFimReal);
					
					while(c.getTime().compareTo(dataInicioReal)>=0)
					{
						mes = new SimpleDateFormat("MM").format(c.getTime());
						ano = new SimpleDateFormat("yyyy").format(c.getTime());
						mesAnoCalculo = mes+ano;
						
						dataCalculo = c.getTime();
						
						c1Ano = Calendar.getInstance();
						c1Ano.setTime(dataCalculo);
						c1Ano.add(Calendar.YEAR, -1);
						mesUmAnoAnterior = new SimpleDateFormat("MM").format(c1Ano.getTime()) + new SimpleDateFormat("yyyy").format(c1Ano.getTime());
						
						c2Anos = Calendar.getInstance();
						c2Anos.setTime(c.getTime());
						c2Anos.add(Calendar.YEAR, -2);
						mesDoisAnosAnterior = new SimpleDateFormat("MM").format(c2Anos.getTime()) + new SimpleDateFormat("yyyy").format(c2Anos.getTime());
						
						c3Anos = Calendar.getInstance();
						c3Anos.setTime(c.getTime());
						c3Anos.add(Calendar.YEAR, -3);
						mesTresAnosAnterior = new SimpleDateFormat("MM").format(c3Anos.getTime()) + new SimpleDateFormat("yyyy").format(c3Anos.getTime());
					
						if(nomeCalculo.equals("Prima neta / capital"))
						{
							conta = cContas.get("0401000000");
		    				//BigDecimal valor1 = this.totalConta(conta, dataCalculo, aseguradora, null);
							BigDecimal valor1 = this.totalConta(conta, dataCalculo, aseguradora, null);
		    				//BigDecimal valor1G = this.totalConta(conta, dataCalculo, null, grupoAseguradora);
							BigDecimal valor1G = this.totalConta(conta, dataCalculo, null, grupoAseguradora);
		    				//BigDecimal valor1T = this.totalConta(conta, dataCalculo, null, todasAseguradora);
							BigDecimal valor1T = this.totalConta(conta, dataCalculo, null, todasAseguradora);
		    				
		    				conta = cContas.get("0402000000");
		    				BigDecimal valor2 = this.totalConta(conta, dataCalculo, aseguradora, null);
		    				BigDecimal valor2G = this.totalConta(conta, dataCalculo, null, grupoAseguradora);
		    				BigDecimal valor2T = this.totalConta(conta, dataCalculo, null, todasAseguradora);
		    				
		    				conta = cContas.get("0403000000");
		    				BigDecimal valor3 = this.totalConta(conta, dataCalculo, aseguradora, null);
		    				BigDecimal valor3G = this.totalConta(conta, dataCalculo, null, grupoAseguradora);
		    				BigDecimal valor3T = this.totalConta(conta, dataCalculo, null, todasAseguradora);
		    				
		    				conta = cContas.get("0501000000");
		    				BigDecimal valor4 = this.totalConta(conta, dataCalculo, aseguradora, null);
		    				BigDecimal valor4G = this.totalConta(conta, dataCalculo, null, grupoAseguradora);
		    				BigDecimal valor4T = this.totalConta(conta, dataCalculo, null, todasAseguradora);
		    				
		    				conta = cContas.get("0502000000");
		    				BigDecimal valor5 = this.totalConta(conta, dataCalculo, aseguradora, null);
		    				BigDecimal valor5G = this.totalConta(conta, dataCalculo, null, grupoAseguradora);
		    				BigDecimal valor5T = this.totalConta(conta, dataCalculo, null, todasAseguradora);
		    				
		    				conta = cContas.get("0301000000");
		    				BigDecimal valor6 = this.totalConta(conta, dataCalculo, aseguradora, null);
		    				BigDecimal valor6G = this.totalConta(conta, dataCalculo, null, grupoAseguradora);
		    				BigDecimal valor6T = this.totalConta(conta, dataCalculo, null, todasAseguradora);
		    				
		    				if(valor6.doubleValue()!=0)
		    				{
		    					total = valor1.subtract(valor2).subtract(valor3).subtract(valor4).subtract(valor5);
		    					total = total.divide(valor6, 4, RoundingMode.HALF_EVEN);
		    					total = total.multiply(porCem);
		    				}
		    				
		        	    	if(total.doubleValue() == 0.00)
		        	    		total = new BigDecimal("0.00");
		    				
		    				celula = row.createCell(++coluna);
							celula.setCellValue(formataValor.format(total));
							celula.setCellStyle(estiloTextoD);
							
							if(valor6G.doubleValue()!=0)
							{
								total = valor1G.subtract(valor2G).subtract(valor3G).subtract(valor4G).subtract(valor5G);
								total = total.divide(valor6G, 4, RoundingMode.HALF_EVEN);
		    					total = total.multiply(porCem);
							}
		    				
		        	    	if(total.doubleValue() == 0.00)
		        	    		total = new BigDecimal("0.00");
		    				
		    				celula = row.createCell(++coluna);
							celula.setCellValue(formataValor.format(total));
							celula.setCellStyle(estiloTextoD);
							
							if(valor6T.doubleValue()!=0)
							{
								total = valor1T.subtract(valor2T).subtract(valor3T).subtract(valor4T).subtract(valor5T);
								total = total.divide(valor6T, 4, RoundingMode.HALF_EVEN);
		    					total = total.multiply(porCem);
							}
		    				
		        	    	if(total.doubleValue() == 0.00)
		        	    		total = new BigDecimal("0.00");
		    				
		    				celula = row.createCell(++coluna);
							celula.setCellValue(formataValor.format(total));
							celula.setCellStyle(estiloTextoD);
						}
						else if(nomeCalculo.equals("Reservas / capital"))
						{
							conta = cContas.get("0303000000");
							BigDecimal valor1 = this.totalConta(conta, dataCalculo, aseguradora, null);
							BigDecimal valor1G = this.totalConta(conta, dataCalculo, null, grupoAseguradora);
							BigDecimal valor1T = this.totalConta(conta, dataCalculo, null, todasAseguradora);
		    				
		    				conta = cContas.get("0301000000");
		    				BigDecimal valor2 = this.totalConta(conta, dataCalculo, aseguradora, null);
		    				BigDecimal valor2G = this.totalConta(conta, dataCalculo, null, grupoAseguradora);
		    				BigDecimal valor2T = this.totalConta(conta, dataCalculo, null, todasAseguradora);
		    				
		    				if(valor2.doubleValue()!=0)
		    				{
		    					total = valor1.divide(valor2, 4, RoundingMode.HALF_EVEN);
		    					total = total.multiply(porCem);
		    				}
		    				
		        	    	if(total.doubleValue() == 0.00)
		        	    		total = new BigDecimal("0.00");
		    				
		    				celula = row.createCell(++coluna);
							celula.setCellValue(formataValor.format(total));
							celula.setCellStyle(estiloTextoD);
							
							if(valor2G.doubleValue()!=0)
							{
								total = valor1G.divide(valor2G, 4, RoundingMode.HALF_EVEN);
		    					total = total.multiply(porCem);
							}
		    				
		        	    	if(total.doubleValue() == 0.00)
		        	    		total = new BigDecimal("0.00");
		    				
		    				celula = row.createCell(++coluna);
							celula.setCellValue(formataValor.format(total));
							celula.setCellStyle(estiloTextoD);
							
							if(valor2T.doubleValue()!=0)
							{
								total = valor1T.divide(valor2T, 4, RoundingMode.HALF_EVEN);
		    					total = total.multiply(porCem);
							}
		    				
		        	    	if(total.doubleValue() == 0.00)
		        	    		total = new BigDecimal("0.00");
		    				
		    				celula = row.createCell(++coluna);
							celula.setCellValue(formataValor.format(total));
							celula.setCellStyle(estiloTextoD);
						}
						else if(nomeCalculo.equals("Excedentes de capital y capital mínimo"))
						{
							conta = cContas.get("0301020000");
							BigDecimal valor1 = this.totalConta(conta, dataCalculo, aseguradora, null);
							BigDecimal valor1G = this.totalConta(conta, dataCalculo, null, grupoAseguradora);
							BigDecimal valor1T = this.totalConta(conta, dataCalculo, null, todasAseguradora);
		    				
		    				conta = cContas.get("0301010000");
		    				BigDecimal valor2 = this.totalConta(conta, dataCalculo, aseguradora, null);
		    				BigDecimal valor2G = this.totalConta(conta, dataCalculo, null, grupoAseguradora);
		    				BigDecimal valor2T = this.totalConta(conta, dataCalculo, null, todasAseguradora);
		    				
		    				if(valor2.doubleValue()!=0)
		    				{
		    					total = valor1.divide(valor2, 4, RoundingMode.HALF_EVEN);
		    					total = total.multiply(porCem);
		    				}
		    				
		        	    	if(total.doubleValue() == 0.00)
		        	    		total = new BigDecimal("0.00");
		    				
		    				celula = row.createCell(++coluna);
							celula.setCellValue(formataValor.format(total));
							celula.setCellStyle(estiloTextoD);
							
							if(valor2G.doubleValue()!=0)
							{
								total = valor1G.divide(valor2G, 4, RoundingMode.HALF_EVEN);
		    					total = total.multiply(porCem);
							}
		    				
		        	    	if(total.doubleValue() == 0.00)
		        	    		total = new BigDecimal("0.00");
		    				
		    				celula = row.createCell(++coluna);
							celula.setCellValue(formataValor.format(total));
							celula.setCellStyle(estiloTextoD);
							
							if(valor2T.doubleValue()!=0)
							{
								total = valor1T.divide(valor2T, 4, RoundingMode.HALF_EVEN);
		    					total = total.multiply(porCem);
							}
		    				
		        	    	if(total.doubleValue() == 0.00)
		        	    		total = new BigDecimal("0.00");
		    				
		    				celula = row.createCell(++coluna);
							celula.setCellValue(formataValor.format(total));
							celula.setCellStyle(estiloTextoD);
						}
						else if(nomeCalculo.equals("Activos fijos, valores no líquidos y las cuentas por cobrar / activos"))
						{
							conta = cContas.get("0107060000");
							BigDecimal valor1 = this.totalConta(conta, dataCalculo, aseguradora, null);
							BigDecimal valor1G = this.totalConta(conta, dataCalculo, null, grupoAseguradora);
							BigDecimal valor1T = this.totalConta(conta, dataCalculo, null, todasAseguradora);
		    				
		    				conta = cContas.get("0109000000");
		    				BigDecimal valor2 = this.totalConta(conta, dataCalculo, aseguradora, null);
		    				BigDecimal valor2G = this.totalConta(conta, dataCalculo, null, grupoAseguradora);
		    				BigDecimal valor2T = this.totalConta(conta, dataCalculo, null, todasAseguradora);
		    				
		    				conta = cContas.get("0102000000");
		    				BigDecimal valor3 = this.totalConta(conta, dataCalculo, aseguradora, null);
		    				BigDecimal valor3G = this.totalConta(conta, dataCalculo, null, grupoAseguradora);
		    				BigDecimal valor3T = this.totalConta(conta, dataCalculo, null, todasAseguradora);
		    				
		    				conta = cContas.get("0103000000");
		    				BigDecimal valor4 = this.totalConta(conta, dataCalculo, aseguradora, null);
		    				BigDecimal valor4G = this.totalConta(conta, dataCalculo, null, grupoAseguradora);
		    				BigDecimal valor4T = this.totalConta(conta, dataCalculo, null, todasAseguradora);
		    				
		    				conta = cContas.get("0104000000");
		    				BigDecimal valor5 = this.totalConta(conta, dataCalculo, aseguradora, null);
		    				BigDecimal valor5G = this.totalConta(conta, dataCalculo, null, grupoAseguradora);
		    				BigDecimal valor5T = this.totalConta(conta, dataCalculo, null, todasAseguradora);
		    				
		    				conta = cContas.get("0100000000");
		    				BigDecimal valor6 = this.totalConta(conta, dataCalculo, aseguradora, null);
		    				BigDecimal valor6G = this.totalConta(conta, dataCalculo, null, grupoAseguradora);
		    				BigDecimal valor6T = this.totalConta(conta, dataCalculo, null, todasAseguradora);
		    				
		    				if(valor6.doubleValue()!=0)
		    				{
		    					total = valor1.add(valor2).add(valor3).add(valor4).add(valor5);
		    					total = total.divide(valor6, 4, RoundingMode.HALF_EVEN);
		    					total = total.multiply(porCem);
		    				}
		    				
		        	    	if(total.doubleValue() == 0.00)
		        	    		total = new BigDecimal("0.00");
		    				
		    				celula = row.createCell(++coluna);
							celula.setCellValue(formataValor.format(total));
							celula.setCellStyle(estiloTextoD);
							
							if(valor6G.doubleValue()!=0)
							{
								total = valor1G.add(valor2G).add(valor3G).add(valor4G).add(valor5G);
		    					total = total.divide(valor6G, 4, RoundingMode.HALF_EVEN);
		    					total = total.multiply(porCem);
							}
		    				
		        	    	if(total.doubleValue() == 0.00)
		        	    		total = new BigDecimal("0.00");
		        	    	
		        	    	celula = row.createCell(++coluna);
							celula.setCellValue(formataValor.format(total));
							celula.setCellStyle(estiloTextoD);
		        	    	
							if(valor6T.doubleValue()!=0)
							{
								total = valor1T.add(valor2T).add(valor3T).add(valor4T).add(valor5T);
		    					total = total.divide(valor6T, 4, RoundingMode.HALF_EVEN);
		    					total = total.multiply(porCem);
							}
		    				
		        	    	if(total.doubleValue() == 0.00)
		        	    		total = new BigDecimal("0.00");
		    				
		    				celula = row.createCell(++coluna);
							celula.setCellValue(formataValor.format(total));
							celula.setCellStyle(estiloTextoD);
						}
						else if(nomeCalculo.equals("Cuentas a cobrar / activos"))
						{
		    				conta = cContas.get("0102000000");
		    				BigDecimal valor1 = this.totalConta(conta, dataCalculo, aseguradora, null);
		    				BigDecimal valor1G = this.totalConta(conta, dataCalculo, null, grupoAseguradora);
		    				BigDecimal valor1T = this.totalConta(conta, dataCalculo, null, todasAseguradora);
		    				
		    				conta = cContas.get("0103000000");
		    				BigDecimal valor2 = this.totalConta(conta, dataCalculo, aseguradora, null);
		    				BigDecimal valor2G = this.totalConta(conta, dataCalculo, null, grupoAseguradora);
		    				BigDecimal valor2T = this.totalConta(conta, dataCalculo, null, todasAseguradora);
		    				
		    				conta = cContas.get("0104000000");
		    				BigDecimal valor3 = this.totalConta(conta, dataCalculo, aseguradora, null);
		    				BigDecimal valor3G = this.totalConta(conta, dataCalculo, null, grupoAseguradora);
		    				BigDecimal valor3T = this.totalConta(conta, dataCalculo, null, todasAseguradora);
		    				
		    				conta = cContas.get("0100000000");
		    				BigDecimal valor4 = this.totalConta(conta, dataCalculo, aseguradora, null);
		    				BigDecimal valor4G = this.totalConta(conta, dataCalculo, null, grupoAseguradora);
		    				BigDecimal valor4T = this.totalConta(conta, dataCalculo, null, todasAseguradora);
		    				
		    				if(valor4.doubleValue()!=0)
		    				{
		    					total = valor1.add(valor2).add(valor3);
		    					total = total.divide(valor4, 4, RoundingMode.HALF_EVEN);
		    					total = total.multiply(porCem);
		    				}
		    				
		        	    	if(total.doubleValue() == 0.00)
		        	    		total = new BigDecimal("0.00");
		    				
		    				celula = row.createCell(++coluna);
							celula.setCellValue(formataValor.format(total));
							celula.setCellStyle(estiloTextoD);
							
							if(valor4G.doubleValue()!=0)
							{
								total = valor1G.add(valor2G).add(valor3G);
		    					total = total.divide(valor4G, 4, RoundingMode.HALF_EVEN);
		    					total = total.multiply(porCem);
							}
		    				
		        	    	if(total.doubleValue() == 0.00)
		        	    		total = new BigDecimal("0.00");
		    				
		    				celula = row.createCell(++coluna);
							celula.setCellValue(formataValor.format(total));
							celula.setCellStyle(estiloTextoD);
							
							if(valor4T.doubleValue()!=0)
							{
								total = valor1T.add(valor2T).add(valor3T);
		    					total = total.divide(valor4T, 4, RoundingMode.HALF_EVEN);
		    					total = total.multiply(porCem);
							}
		    				
		        	    	if(total.doubleValue() == 0.00)
		        	    		total = new BigDecimal("0.00");
		    				
		    				celula = row.createCell(++coluna);
							celula.setCellValue(formataValor.format(total));
							celula.setCellStyle(estiloTextoD);
						}
						else if(nomeCalculo.equals("Valores negociables / activos totales"))
						{
		    				conta = cContas.get("0107010000");
		    				BigDecimal valor1 = this.totalConta(conta, dataCalculo, aseguradora, null);
		    				BigDecimal valor1G = this.totalConta(conta, dataCalculo, null, grupoAseguradora);
		    				BigDecimal valor1T = this.totalConta(conta, dataCalculo, null, todasAseguradora);
		    				
		    				conta = cContas.get("0107020000");
		    				BigDecimal valor2 = this.totalConta(conta, dataCalculo, aseguradora, null);
		    				BigDecimal valor2G = this.totalConta(conta, dataCalculo, null, grupoAseguradora);
		    				BigDecimal valor2T = this.totalConta(conta, dataCalculo, null, todasAseguradora);
		    				
		    				conta = cContas.get("0107030000");
		    				BigDecimal valor3 = this.totalConta(conta, dataCalculo, aseguradora, null);
		    				BigDecimal valor3G = this.totalConta(conta, dataCalculo, null, grupoAseguradora);
		    				BigDecimal valor3T = this.totalConta(conta, dataCalculo, null, todasAseguradora);
		    				
		    				conta = cContas.get("0107040000");
		    				BigDecimal valor4 = this.totalConta(conta, dataCalculo, aseguradora, null);
		    				BigDecimal valor4G = this.totalConta(conta, dataCalculo, null, grupoAseguradora);
		    				BigDecimal valor4T = this.totalConta(conta, dataCalculo, null, todasAseguradora);
		    				
		    				conta = cContas.get("0100000000");
		    				BigDecimal valor5 = this.totalConta(conta, dataCalculo, aseguradora, null);
		    				BigDecimal valor5G = this.totalConta(conta, dataCalculo, null, grupoAseguradora);
		    				BigDecimal valor5T = this.totalConta(conta, dataCalculo, null, todasAseguradora);
		    				
		    				if(valor5.doubleValue()!=0)
		    				{
		    					total = valor1.add(valor2).add(valor3).add(valor4);
		    					total = total.divide(valor5, 4, RoundingMode.HALF_EVEN);
		    					total = total.multiply(porCem);
		    				}
		    				
		        	    	if(total.doubleValue() == 0.00)
		        	    		total = new BigDecimal("0.00");
		    				
		    				celula = row.createCell(++coluna);
							celula.setCellValue(formataValor.format(total));
							celula.setCellStyle(estiloTextoD);
							
							if(valor5G.doubleValue()!=0)
							{
								total = valor1G.add(valor2G).add(valor3G).add(valor4G);
		    					total = total.divide(valor5G, 4, RoundingMode.HALF_EVEN);
		    					total = total.multiply(porCem);
							}
		    				
		        	    	if(total.doubleValue() == 0.00)
		        	    		total = new BigDecimal("0.00");
		    				
		    				celula = row.createCell(++coluna);
							celula.setCellValue(formataValor.format(total));
							celula.setCellStyle(estiloTextoD);
							
							if(valor5T.doubleValue()!=0)
							{
								total = valor1T.add(valor2T).add(valor3T).add(valor4T);
		    					total = total.divide(valor5T, 4, RoundingMode.HALF_EVEN);
		    					total = total.multiply(porCem);
							}
		    				
		        	    	if(total.doubleValue() == 0.00)
		        	    		total = new BigDecimal("0.00");
		    				
		    				celula = row.createCell(++coluna);
							celula.setCellValue(formataValor.format(total));
							celula.setCellStyle(estiloTextoD);
						}
						else if(nomeCalculo.equals("Prima neta a la prima bruta"))
						{
		    				conta = cContas.get("0401000000");
		    				BigDecimal valor1 = this.totalConta(conta, dataCalculo, aseguradora, null);
		    				BigDecimal valor1G = this.totalConta(conta, dataCalculo, null, grupoAseguradora);
		    				BigDecimal valor1T = this.totalConta(conta, dataCalculo, null, todasAseguradora);
		    				
		    				conta = cContas.get("0402000000");
		    				BigDecimal valor2 = this.totalConta(conta, dataCalculo, aseguradora, null);
		    				BigDecimal valor2G = this.totalConta(conta, dataCalculo, null, grupoAseguradora);
		    				BigDecimal valor2T = this.totalConta(conta, dataCalculo, null, todasAseguradora);
		    				
		    				conta = cContas.get("0403000000");
		    				BigDecimal valor3 = this.totalConta(conta, dataCalculo, aseguradora, null);
		    				BigDecimal valor3G = this.totalConta(conta, dataCalculo, null, grupoAseguradora);
		    				BigDecimal valor3T = this.totalConta(conta, dataCalculo, null, todasAseguradora);
		    				
		    				conta = cContas.get("0501000000");
		    				BigDecimal valor4 = this.totalConta(conta, dataCalculo, aseguradora, null);
		    				BigDecimal valor4G = this.totalConta(conta, dataCalculo, null, grupoAseguradora);
		    				BigDecimal valor4T = this.totalConta(conta, dataCalculo, null, todasAseguradora);
		    				
		    				conta = cContas.get("0502000000");
		    				BigDecimal valor5 = this.totalConta(conta, dataCalculo, aseguradora, null);
		    				BigDecimal valor5G = this.totalConta(conta, dataCalculo, null, grupoAseguradora);
		    				BigDecimal valor5T = this.totalConta(conta, dataCalculo, null, todasAseguradora);
		    				
		    				if(valor1.doubleValue()!=0)
		    				{
		    					total = valor1.subtract(valor2).subtract(valor3).subtract(valor4).subtract(valor5);
		    					total = total.divide(valor1, 4, RoundingMode.HALF_EVEN);
		    					total = total.multiply(porCem);
		    				}
		    				
		        	    	if(total.doubleValue() == 0.00)
		        	    		total = new BigDecimal("0.00");
		    				
		    				celula = row.createCell(++coluna);
							celula.setCellValue(formataValor.format(total));
							celula.setCellStyle(estiloTextoD);
							
							if(valor1G.doubleValue()!=0)
							{
								total = valor1G.subtract(valor2G).subtract(valor3G).subtract(valor4G).subtract(valor5G);
		    					total = total.divide(valor1G, 4, RoundingMode.HALF_EVEN);
		    					total = total.multiply(porCem);
							}
		    				
		        	    	if(total.doubleValue() == 0.00)
		        	    		total = new BigDecimal("0.00");
		    				
		    				celula = row.createCell(++coluna);
							celula.setCellValue(formataValor.format(total));
							celula.setCellStyle(estiloTextoD);
							
							if(valor1T.doubleValue()!=0)
							{
								total = valor1T.subtract(valor2T).subtract(valor3T).subtract(valor4T).subtract(valor5T);
		    					total = total.divide(valor1T, 4, RoundingMode.HALF_EVEN);
		    					total = total.multiply(porCem);
							}
		    				
		        	    	if(total.doubleValue() == 0.00)
		        	    		total = new BigDecimal("0.00");
		    				
		    				celula = row.createCell(++coluna);
							celula.setCellValue(formataValor.format(total));
							celula.setCellStyle(estiloTextoD);
						}
						else if(nomeCalculo.equals("Evolucion de Primas netas Anulaciones"))
						{
							conta = cContas.get("0212000000");
		    				BigDecimal valor1 = this.totalConta(conta, dataCalculo, aseguradora, null);
		    				BigDecimal valor1G = this.totalConta(conta, dataCalculo, null, grupoAseguradora);
		    				BigDecimal valor1T = this.totalConta(conta, dataCalculo, null, todasAseguradora);
		    				
		    				BigDecimal valor2 = conta.obterTotalizacaoExistenteBIG(aseguradora, mesUmAnoAnterior);
		    				BigDecimal valor2G = conta.obterTotalizacaoExistenteBIG(grupoAseguradora, mesUmAnoAnterior);
		    				BigDecimal valor2T = conta.obterTotalizacaoExistenteBIG(todasAseguradora, mesUmAnoAnterior);
		    				
		    				BigDecimal valor3 = conta.obterTotalizacaoExistenteBIG(aseguradora, mesDoisAnosAnterior);
		    				BigDecimal valor3G = conta.obterTotalizacaoExistenteBIG(grupoAseguradora, mesDoisAnosAnterior);
		    				BigDecimal valor3T = conta.obterTotalizacaoExistenteBIG(todasAseguradora, mesDoisAnosAnterior);
							
		    				conta = cContas.get("0401000000");
		    				BigDecimal valor4 = this.totalConta(conta, dataCalculo, aseguradora, null);
		    				BigDecimal valor4G = this.totalConta(conta, dataCalculo, null, grupoAseguradora);
		    				BigDecimal valor4T = this.totalConta(conta, dataCalculo, null, todasAseguradora);
		    				
		    				BigDecimal valor5 = conta.obterTotalizacaoExistenteBIG(aseguradora, mesUmAnoAnterior);
		    				BigDecimal valor5G = conta.obterTotalizacaoExistenteBIG(grupoAseguradora, mesUmAnoAnterior);
		    				BigDecimal valor5T = conta.obterTotalizacaoExistenteBIG(todasAseguradora, mesUmAnoAnterior);
		    				
		    				conta = cContas.get("0402000000");
		    				BigDecimal valor6 = this.totalConta(conta, dataCalculo, aseguradora, null);
		    				BigDecimal valor6G = this.totalConta(conta, dataCalculo, null, grupoAseguradora);
		    				BigDecimal valor6T = this.totalConta(conta, dataCalculo, null, todasAseguradora);
		    				
		    				BigDecimal valor7 = conta.obterTotalizacaoExistenteBIG(aseguradora, mesUmAnoAnterior);
		    				BigDecimal valor7G = conta.obterTotalizacaoExistenteBIG(grupoAseguradora, mesUmAnoAnterior);
		    				BigDecimal valor7T = conta.obterTotalizacaoExistenteBIG(todasAseguradora, mesUmAnoAnterior);
		    				
		    				conta = cContas.get("0403000000");
		    				BigDecimal valor8 = this.totalConta(conta, dataCalculo, aseguradora, null);
		    				BigDecimal valor8G = this.totalConta(conta, dataCalculo, null, grupoAseguradora);
		    				BigDecimal valor8T = this.totalConta(conta, dataCalculo, null, todasAseguradora);
		    				
		    				BigDecimal valor9 = conta.obterTotalizacaoExistenteBIG(aseguradora, mesUmAnoAnterior);
		    				BigDecimal valor9G = conta.obterTotalizacaoExistenteBIG(grupoAseguradora, mesUmAnoAnterior);
		    				BigDecimal valor9T = conta.obterTotalizacaoExistenteBIG(todasAseguradora, mesUmAnoAnterior);
		    				
		    				BigDecimal numerador = valor1.subtract(valor2).add(valor4).add(valor6).add(valor8).subtract(valor2).subtract(valor3).add(valor5).add(valor7).add(valor9);
		    				BigDecimal divisor = valor2.subtract(valor3).add(valor5).add(valor7).add(valor9);
		    				
		    				if(divisor.doubleValue()!=0)
		    				{
		    					total = numerador.divide(divisor, 4, RoundingMode.HALF_EVEN);
		    					total = total.multiply(porCem);
		    				}
		    				
		        	    	if(total.doubleValue() == 0.00)
		        	    		total = new BigDecimal("0.00");
		    				
		    				celula = row.createCell(++coluna);
							celula.setCellValue(formataValor.format(total));
							celula.setCellStyle(estiloTextoD);
							
							numerador = valor1G.subtract(valor2G).add(valor4G).add(valor6G).add(valor8G).subtract(valor2G).subtract(valor3G).add(valor5G).add(valor7G).add(valor9G);
		    				divisor = valor2G.subtract(valor3G).add(valor5G).add(valor7G).add(valor9G);
							
							if(divisor.doubleValue()!=0)
							{
								total = numerador.divide(divisor, 4, RoundingMode.HALF_EVEN);
		    					total = total.multiply(porCem);
							}
		    				
		        	    	if(total.doubleValue() == 0.00)
		        	    		total = new BigDecimal("0.00");
		    				
		    				celula = row.createCell(++coluna);
							celula.setCellValue(formataValor.format(total));
							celula.setCellStyle(estiloTextoD);
							
							numerador = valor1T.subtract(valor2T).add(valor4T).add(valor6T).add(valor8T).subtract(valor2T).subtract(valor3T).add(valor5T).add(valor7T).add(valor9T);
		    				divisor = valor2T.subtract(valor3T).add(valor5T).add(valor7T).add(valor9T);
							
							if(divisor.doubleValue()!=0)
							{
								total = numerador.divide(divisor, 4, RoundingMode.HALF_EVEN);
		    					total = total.multiply(porCem);
							}
		    				
		        	    	if(total.doubleValue() == 0.00)
		        	    		total = new BigDecimal("0.00");
		    				
		    				celula = row.createCell(++coluna);
							celula.setCellValue(formataValor.format(total));
							celula.setCellStyle(estiloTextoD);
						}
						else if(nomeCalculo.equals("Evolucion de las Primas netas de Reaseguros"))
						{
							conta = cContas.get("0212000000");
		    				BigDecimal valor1 = this.totalConta(conta, dataCalculo, aseguradora, null);
		    				BigDecimal valor1G = this.totalConta(conta, dataCalculo, null, grupoAseguradora);
		    				BigDecimal valor1T = this.totalConta(conta, dataCalculo, null, todasAseguradora);
		    				
		    				BigDecimal valor2 = conta.obterTotalizacaoExistenteBIG(aseguradora, mesUmAnoAnterior);
		    				BigDecimal valor2G = conta.obterTotalizacaoExistenteBIG(grupoAseguradora, mesUmAnoAnterior);
		    				BigDecimal valor2T = conta.obterTotalizacaoExistenteBIG(todasAseguradora, mesUmAnoAnterior);
		    				
		    				BigDecimal valor3 = conta.obterTotalizacaoExistenteBIG(aseguradora, mesDoisAnosAnterior);
		    				BigDecimal valor3G = conta.obterTotalizacaoExistenteBIG(grupoAseguradora, mesDoisAnosAnterior);
		    				BigDecimal valor3T = conta.obterTotalizacaoExistenteBIG(todasAseguradora, mesDoisAnosAnterior);
							
		    				conta = cContas.get("0401000000");
		    				BigDecimal valor4 = this.totalConta(conta, dataCalculo, aseguradora, null);
		    				BigDecimal valor4G = this.totalConta(conta, dataCalculo, null, grupoAseguradora);
		    				BigDecimal valor4T = this.totalConta(conta, dataCalculo, null, todasAseguradora);
		    				
		    				BigDecimal valor5 = conta.obterTotalizacaoExistenteBIG(aseguradora, mesUmAnoAnterior);
		    				BigDecimal valor5G = conta.obterTotalizacaoExistenteBIG(grupoAseguradora, mesUmAnoAnterior);
		    				BigDecimal valor5T = conta.obterTotalizacaoExistenteBIG(todasAseguradora, mesUmAnoAnterior);
		    				
		    				conta = cContas.get("0402000000");
		    				BigDecimal valor6 = this.totalConta(conta, dataCalculo, aseguradora, null);
		    				BigDecimal valor6G = this.totalConta(conta, dataCalculo, null, grupoAseguradora);
		    				BigDecimal valor6T = this.totalConta(conta, dataCalculo, null, todasAseguradora);
		    				
		    				BigDecimal valor7 = conta.obterTotalizacaoExistenteBIG(aseguradora, mesUmAnoAnterior);
		    				BigDecimal valor7G = conta.obterTotalizacaoExistenteBIG(grupoAseguradora, mesUmAnoAnterior);
		    				BigDecimal valor7T = conta.obterTotalizacaoExistenteBIG(todasAseguradora, mesUmAnoAnterior);
		    				
		    				conta = cContas.get("0403000000");
		    				BigDecimal valor8 = this.totalConta(conta, dataCalculo, aseguradora, null);
		    				BigDecimal valor8G = this.totalConta(conta, dataCalculo, null, grupoAseguradora);
		    				BigDecimal valor8T = this.totalConta(conta, dataCalculo, null, todasAseguradora);
		    				
		    				BigDecimal valor9 = conta.obterTotalizacaoExistenteBIG(aseguradora, mesUmAnoAnterior);
		    				BigDecimal valor9G = conta.obterTotalizacaoExistenteBIG(grupoAseguradora, mesUmAnoAnterior);
		    				BigDecimal valor9T = conta.obterTotalizacaoExistenteBIG(todasAseguradora, mesUmAnoAnterior);
		    				
		    				conta = cContas.get("0109030000");
		    				BigDecimal valor10 = this.totalConta(conta, dataCalculo, aseguradora, null);
		    				BigDecimal valor10G = this.totalConta(conta, dataCalculo, null, grupoAseguradora);
		    				BigDecimal valor10T = this.totalConta(conta, dataCalculo, null, todasAseguradora);
		    				
		    				BigDecimal valor11 = conta.obterTotalizacaoExistenteBIG(aseguradora, mesUmAnoAnterior);
		    				BigDecimal valor11G = conta.obterTotalizacaoExistenteBIG(grupoAseguradora, mesUmAnoAnterior);
		    				BigDecimal valor11T = conta.obterTotalizacaoExistenteBIG(todasAseguradora, mesUmAnoAnterior);
		    				
		    				BigDecimal valor12 = conta.obterTotalizacaoExistenteBIG(aseguradora, mesDoisAnosAnterior);
		    				BigDecimal valor12G = conta.obterTotalizacaoExistenteBIG(grupoAseguradora, mesDoisAnosAnterior);
		    				BigDecimal valor12T = conta.obterTotalizacaoExistenteBIG(todasAseguradora, mesDoisAnosAnterior);
		    				
		    				conta = cContas.get("0501000000");
		    				BigDecimal valor13 = this.totalConta(conta, dataCalculo, aseguradora, null);
		    				BigDecimal valor13G = this.totalConta(conta, dataCalculo, null, grupoAseguradora);
		    				BigDecimal valor13T = this.totalConta(conta, dataCalculo, null, todasAseguradora);
		    				
		    				BigDecimal valor14 = conta.obterTotalizacaoExistenteBIG(aseguradora, mesUmAnoAnterior);
		    				BigDecimal valor14G = conta.obterTotalizacaoExistenteBIG(grupoAseguradora, mesUmAnoAnterior);
		    				BigDecimal valor14T = conta.obterTotalizacaoExistenteBIG(todasAseguradora, mesUmAnoAnterior);
		    				
		    				conta = cContas.get("0502000000");
		    				BigDecimal valor15 = this.totalConta(conta, dataCalculo, aseguradora, null);
		    				BigDecimal valor15G = this.totalConta(conta, dataCalculo, null, grupoAseguradora);
		    				BigDecimal valor15T = this.totalConta(conta, dataCalculo, null, todasAseguradora);
		    				
		    				BigDecimal valor16 = conta.obterTotalizacaoExistenteBIG(aseguradora, mesUmAnoAnterior);
		    				BigDecimal valor16G = conta.obterTotalizacaoExistenteBIG(grupoAseguradora, mesUmAnoAnterior);
		    				BigDecimal valor16T = conta.obterTotalizacaoExistenteBIG(todasAseguradora, mesUmAnoAnterior);
		    				
		    				BigDecimal numerador = valor1.subtract(valor2).add(valor4).add(valor6).add(valor8).subtract(valor10).subtract(valor11).add(valor13).add(valor15).subtract(valor2).subtract(valor3).add(valor5).add(valor7).add(valor9).subtract(valor11).subtract(valor12).add(valor14).add(valor16);
		    				BigDecimal divisor = valor2.subtract(valor3).add(valor5).add(valor7).add(valor9).subtract(valor11).subtract(valor12).add(valor14).add(valor16);
		    				
		    				if(divisor.doubleValue()!=0)
		    				{
		    					total = numerador.divide(divisor, 4, RoundingMode.HALF_EVEN);
		    					total = total.multiply(porCem);
		    				}
		    				
		        	    	if(total.doubleValue() == 0.00)
		        	    		total = new BigDecimal("0.00");
		    				
		    				celula = row.createCell(++coluna);
							celula.setCellValue(formataValor.format(total));
							celula.setCellStyle(estiloTextoD);
							
							numerador = valor1G.subtract(valor2G).add(valor4G).add(valor6G).add(valor8G).subtract(valor10G).subtract(valor11G).add(valor13G).add(valor15G).subtract(valor2G).subtract(valor3G).add(valor5G).add(valor7G).add(valor9G).subtract(valor11G).subtract(valor12G).add(valor14G).add(valor16G);
		    				divisor = valor2G.subtract(valor3G).add(valor5G).add(valor7G).add(valor9G).subtract(valor11G).subtract(valor12G).add(valor14G).add(valor16G);
							
							if(divisor.doubleValue()!=0)
							{
								total = numerador.divide(divisor, 4, RoundingMode.HALF_EVEN);
		    					total = total.multiply(porCem);
							}
		    				
		        	    	if(total.doubleValue() == 0.00)
		        	    		total = new BigDecimal("0.00");
		    				
		    				celula = row.createCell(++coluna);
							celula.setCellValue(formataValor.format(total));
							celula.setCellStyle(estiloTextoD);
							
							numerador = valor1T.subtract(valor2T).add(valor4T).add(valor6T).add(valor8T).subtract(valor10T).subtract(valor11T).add(valor13T).add(valor15T).subtract(valor2T).subtract(valor3T).add(valor5T).add(valor7T).add(valor9T).subtract(valor11T).subtract(valor12T).add(valor14T).add(valor16T);
		    				divisor = valor2T.subtract(valor3T).add(valor5T).add(valor7T).add(valor9T).subtract(valor11T).subtract(valor12T).add(valor14T).add(valor16T);
							
							if(divisor.doubleValue()!=0)
							{
								total = numerador.divide(divisor, 4, RoundingMode.HALF_EVEN);
		    					total = total.multiply(porCem);
							}
		    				
		        	    	if(total.doubleValue() == 0.00)
		        	    		total = new BigDecimal("0.00");
		    				
		    				celula = row.createCell(++coluna);
							celula.setCellValue(formataValor.format(total));
							celula.setCellStyle(estiloTextoD);
						}
						else if(nomeCalculo.equals("Demandas en disputa / reclamación total pagada"))
						{
							conta = cContas.get("0213030000");
							BigDecimal valor1 = this.totalConta(conta, dataCalculo, aseguradora, null);
							BigDecimal valor1G = this.totalConta(conta, dataCalculo, null, grupoAseguradora);
							BigDecimal valor1T = this.totalConta(conta, dataCalculo, null, todasAseguradora);
		    				
		    				conta = cContas.get("0506000000");
		    				BigDecimal valor2 = this.totalConta(conta, dataCalculo, aseguradora, null);
		    				BigDecimal valor2G = this.totalConta(conta, dataCalculo, null, grupoAseguradora);
		    				BigDecimal valor2T = this.totalConta(conta, dataCalculo, null, todasAseguradora);
		    				
		    				if(valor2.doubleValue()!=0)
		    				{
		    					total = valor1.divide(valor2, 4, RoundingMode.HALF_EVEN);
		    					total = total.multiply(porCem);
		    				}
		    				
		        	    	if(total.doubleValue() == 0.00)
		        	    		total = new BigDecimal("0.00");
		    				
		    				celula = row.createCell(++coluna);
							celula.setCellValue(formataValor.format(total));
							celula.setCellStyle(estiloTextoD);
							
							if(valor2G.doubleValue()!=0)
							{
								total = valor1G.divide(valor2G, 4, RoundingMode.HALF_EVEN);
		    					total = total.multiply(porCem);
							}
		    				
		        	    	if(total.doubleValue() == 0.00)
		        	    		total = new BigDecimal("0.00");
		    				
		    				celula = row.createCell(++coluna);
							celula.setCellValue(formataValor.format(total));
							celula.setCellStyle(estiloTextoD);
							
							if(valor2T.doubleValue()!=0)
							{
								total = valor1T.divide(valor2T, 4, RoundingMode.HALF_EVEN);
		    					total = total.multiply(porCem);
							}
		    				
		        	    	if(total.doubleValue() == 0.00)
		        	    		total = new BigDecimal("0.00");
		    				
		    				celula = row.createCell(++coluna);
							celula.setCellValue(formataValor.format(total));
							celula.setCellStyle(estiloTextoD);
						}
						else if(nomeCalculo.equals("Demandas en disputa / reclamación total pagada"))
						{
							conta = cContas.get("0213030000");
							BigDecimal valor1 = this.totalConta(conta, dataCalculo, aseguradora, null);
							BigDecimal valor1G = this.totalConta(conta, dataCalculo, null, grupoAseguradora);
							BigDecimal valor1T = this.totalConta(conta, dataCalculo, null, todasAseguradora);
		    				
		    				conta = cContas.get("0506000000");
		    				BigDecimal valor2 = this.totalConta(conta, dataCalculo, aseguradora, null);
		    				BigDecimal valor2G = this.totalConta(conta, dataCalculo, null, grupoAseguradora);
		    				BigDecimal valor2T = this.totalConta(conta, dataCalculo, null, todasAseguradora);
		    				
		    				if(valor2.doubleValue()!=0)
		    				{
		    					total = valor1.divide(valor2, 4, RoundingMode.HALF_EVEN);
		    					total = total.multiply(porCem);
		    				}
		    				
		        	    	if(total.doubleValue() == 0.00)
		        	    		total = new BigDecimal("0.00");
		    				
		    				celula = row.createCell(++coluna);
							celula.setCellValue(formataValor.format(total));
							celula.setCellStyle(estiloTextoD);
							
							if(valor2G.doubleValue()!=0)
							{
								total = valor1G.divide(valor2G, 4, RoundingMode.HALF_EVEN);
		    					total = total.multiply(porCem);
							}
		    				
		        	    	if(total.doubleValue() == 0.00)
		        	    		total = new BigDecimal("0.00");
		    				
		    				celula = row.createCell(++coluna);
							celula.setCellValue(formataValor.format(total));
							celula.setCellStyle(estiloTextoD);
							
							if(valor2T.doubleValue()!=0)
							{
								total = valor1T.divide(valor2T, 4, RoundingMode.HALF_EVEN);
		    					total = total.multiply(porCem);
							}
		    				
		        	    	if(total.doubleValue() == 0.00)
		        	    		total = new BigDecimal("0.00");
		    				
		    				celula = row.createCell(++coluna);
							celula.setCellValue(formataValor.format(total));
							celula.setCellStyle(estiloTextoD);
						}
						else if(nomeCalculo.equals("Excedentes de capital/capital mínimo"))
						{
							conta = cContas.get("0301020000");
							BigDecimal valor1 = this.totalConta(conta, dataCalculo, aseguradora, null);
							BigDecimal valor1G = this.totalConta(conta, dataCalculo, null, grupoAseguradora);
							BigDecimal valor1T = this.totalConta(conta, dataCalculo, null, todasAseguradora);
		    				
		    				conta = cContas.get("0301010000");
		    				BigDecimal valor2 = this.totalConta(conta, dataCalculo, aseguradora, null);
		    				BigDecimal valor2G = this.totalConta(conta, dataCalculo, null, grupoAseguradora);
		    				BigDecimal valor2T = this.totalConta(conta, dataCalculo, null, todasAseguradora);
		    				
		    				if(valor2.doubleValue()!=0)
		    				{
		    					total = valor1.divide(valor2, 4, RoundingMode.HALF_EVEN);
		    					total = total.multiply(porCem);
		    				}
		    				
		        	    	if(total.doubleValue() == 0.00)
		        	    		total = new BigDecimal("0.00");
		    				
		    				celula = row.createCell(++coluna);
							celula.setCellValue(formataValor.format(total));
							celula.setCellStyle(estiloTextoD);
							
							if(valor2G.doubleValue()!=0)
							{
								total = valor1G.divide(valor2G, 4, RoundingMode.HALF_EVEN);
		    					total = total.multiply(porCem);
							}
		    				
		        	    	if(total.doubleValue() == 0.00)
		        	    		total = new BigDecimal("0.00");
		    				
		    				celula = row.createCell(++coluna);
							celula.setCellValue(formataValor.format(total));
							celula.setCellStyle(estiloTextoD);
							
							if(valor2T.doubleValue()!=0)
							{
								total = valor1T.divide(valor2T, 4, RoundingMode.HALF_EVEN);
		    					total = total.multiply(porCem);
							}
		    				
		        	    	if(total.doubleValue() == 0.00)
		        	    		total = new BigDecimal("0.00");
		    				
		    				celula = row.createCell(++coluna);
							celula.setCellValue(formataValor.format(total));
							celula.setCellStyle(estiloTextoD);
						}
						else if(nomeCalculo.equals("Siniestros Pendientes / Patrimonio  Neto"))
						{
							conta = cContas.get("0213020000");
							BigDecimal valor1 = this.totalConta(conta, dataCalculo, aseguradora, null);
							BigDecimal valor1G = this.totalConta(conta, dataCalculo, null, grupoAseguradora);
							BigDecimal valor1T = this.totalConta(conta, dataCalculo, null, todasAseguradora);
		    				
		    				conta = cContas.get("0300000000");
		    				BigDecimal valor2 = this.totalConta(conta, dataCalculo, aseguradora, null);
		    				BigDecimal valor2G = this.totalConta(conta, dataCalculo, null, grupoAseguradora);
		    				BigDecimal valor2T = this.totalConta(conta, dataCalculo, null, todasAseguradora);
		    				
		    				if(valor2.doubleValue()!=0)
		    				{
		    					total = valor1.divide(valor2, 4, RoundingMode.HALF_EVEN);
		    					total = total.multiply(porCem);
		    				}
		    				
		        	    	if(total.doubleValue() == 0.00)
		        	    		total = new BigDecimal("0.00");
		    				
		    				celula = row.createCell(++coluna);
							celula.setCellValue(formataValor.format(total));
							celula.setCellStyle(estiloTextoD);
							
							if(valor2G.doubleValue()!=0)
							{
								total = valor1G.divide(valor2G, 4, RoundingMode.HALF_EVEN);
		    					total = total.multiply(porCem);
							}
		    				
		        	    	if(total.doubleValue() == 0.00)
		        	    		total = new BigDecimal("0.00");
		    				
		    				celula = row.createCell(++coluna);
							celula.setCellValue(formataValor.format(total));
							celula.setCellStyle(estiloTextoD);
							
							if(valor2T.doubleValue()!=0)
							{
								total = valor1T.divide(valor2T, 4, RoundingMode.HALF_EVEN);
		    					total = total.multiply(porCem);
							}
		    				
		        	    	if(total.doubleValue() == 0.00)
		        	    		total = new BigDecimal("0.00");
		    				
		    				celula = row.createCell(++coluna);
							celula.setCellValue(formataValor.format(total));
							celula.setCellStyle(estiloTextoD);
						}
						else if(nomeCalculo.equals("Retorno sobre el Patrimonio Neto"))
						{
							conta = cContas.get("0400000000");
							BigDecimal valor1 = this.totalConta(conta, dataCalculo, aseguradora, null);
							BigDecimal valor1G = this.totalConta(conta, dataCalculo, null, grupoAseguradora);
							BigDecimal valor1T = this.totalConta(conta, dataCalculo, null, todasAseguradora);
		    				
		    				conta = cContas.get("0500000000");
		    				BigDecimal valor2 = this.totalConta(conta, dataCalculo, aseguradora, null);
		    				BigDecimal valor2G = this.totalConta(conta, dataCalculo, null, grupoAseguradora);
		    				BigDecimal valor2T = this.totalConta(conta, dataCalculo, null, todasAseguradora);
		    				
		    				conta = cContas.get("0300000000");
		    				BigDecimal valor3 = this.totalConta(conta, dataCalculo, aseguradora, null);
		    				BigDecimal valor3G = this.totalConta(conta, dataCalculo, null, grupoAseguradora);
		    				BigDecimal valor3T = this.totalConta(conta, dataCalculo, null, todasAseguradora);
		    				
		    				if(valor3.doubleValue()!=0)
		    				{
		    					total = valor1.subtract(valor2);
		    					total = total.divide(valor3, 4, RoundingMode.HALF_EVEN);
		    					total = total.multiply(porCem);
		    				}
		    				
		        	    	if(total.doubleValue() == 0.00)
		        	    		total = new BigDecimal("0.00");
		    				
		    				celula = row.createCell(++coluna);
							celula.setCellValue(formataValor.format(total));
							celula.setCellStyle(estiloTextoD);
							
							if(valor3G.doubleValue()!=0)
							{
								total = valor1G.subtract(valor2G);
		    					total = total.divide(valor3G, 4, RoundingMode.HALF_EVEN);
		    					total = total.multiply(porCem);
							}
		    				
		        	    	if(total.doubleValue() == 0.00)
		        	    		total = new BigDecimal("0.00");
		    				
		    				celula = row.createCell(++coluna);
							celula.setCellValue(formataValor.format(total));
							celula.setCellStyle(estiloTextoD);
							
							if(valor3T.doubleValue()!=0)
							{
								total = valor1T.subtract(valor2T);
		    					total = total.divide(valor3T, 4, RoundingMode.HALF_EVEN);
		    					total = total.multiply(porCem);
							}
		    				
		        	    	if(total.doubleValue() == 0.00)
		        	    		total = new BigDecimal("0.00");
		    				
		    				celula = row.createCell(++coluna);
							celula.setCellValue(formataValor.format(total));
							celula.setCellStyle(estiloTextoD);
						}
						else if(nomeCalculo.equals("Margen sobre Primas Netas de Reaseguros"))
						{
							conta = cContas.get("0400000000");
							BigDecimal valor1 = this.totalConta(conta, dataCalculo, aseguradora, null);
							BigDecimal valor1G = this.totalConta(conta, dataCalculo, null, grupoAseguradora);
							BigDecimal valor1T = this.totalConta(conta, dataCalculo, null, todasAseguradora);
		    				
		    				conta = cContas.get("0500000000");
		    				BigDecimal valor2 = this.totalConta(conta, dataCalculo, aseguradora, null);
		    				BigDecimal valor2G = this.totalConta(conta, dataCalculo, null, grupoAseguradora);
		    				BigDecimal valor2T = this.totalConta(conta, dataCalculo, null, todasAseguradora);
		    				
		    				conta = cContas.get("0212000000");
		    				BigDecimal valor3 = this.totalConta(conta, dataCalculo, aseguradora, null);
		    				BigDecimal valor3G = this.totalConta(conta, dataCalculo, null, grupoAseguradora);
		    				BigDecimal valor3T = this.totalConta(conta, dataCalculo, null, todasAseguradora);
		    				
		    				BigDecimal valor4 = conta.obterTotalizacaoExistenteBIG(aseguradora, mesUmAnoAnterior);
		    				BigDecimal valor4G = conta.obterTotalizacaoExistenteBIG(grupoAseguradora, mesUmAnoAnterior);
		    				BigDecimal valor4T = conta.obterTotalizacaoExistenteBIG(todasAseguradora, mesUmAnoAnterior);
		    				
		    				conta = cContas.get("0401000000");
							BigDecimal valor5 = this.totalConta(conta, dataCalculo, aseguradora, null);
							BigDecimal valor5G = this.totalConta(conta, dataCalculo, null, grupoAseguradora);
							BigDecimal valor5T = this.totalConta(conta, dataCalculo, null, todasAseguradora);
							
							conta = cContas.get("0402000000");
							BigDecimal valor6 = this.totalConta(conta, dataCalculo, aseguradora, null);
							BigDecimal valor6G = this.totalConta(conta, dataCalculo, null, grupoAseguradora);
							BigDecimal valor6T = this.totalConta(conta, dataCalculo, null, todasAseguradora);
							
							conta = cContas.get("0403000000");
							BigDecimal valor7 = this.totalConta(conta, dataCalculo, aseguradora, null);
							BigDecimal valor7G = this.totalConta(conta, dataCalculo, null, grupoAseguradora);
							BigDecimal valor7T = this.totalConta(conta, dataCalculo, null, todasAseguradora);
							
							conta = cContas.get("0109030000");
		    				BigDecimal valor8 = this.totalConta(conta, dataCalculo, aseguradora, null);
		    				BigDecimal valor8G = this.totalConta(conta, dataCalculo, null, grupoAseguradora);
		    				BigDecimal valor8T = this.totalConta(conta, dataCalculo, null, todasAseguradora);
		    				
		    				BigDecimal valor9 = conta.obterTotalizacaoExistenteBIG(aseguradora, mesUmAnoAnterior);
		    				BigDecimal valor9G = conta.obterTotalizacaoExistenteBIG(grupoAseguradora, mesUmAnoAnterior);
		    				BigDecimal valor9T = conta.obterTotalizacaoExistenteBIG(todasAseguradora, mesUmAnoAnterior);
		    				
		    				conta = cContas.get("0501000000");
							BigDecimal valor10 = this.totalConta(conta, dataCalculo, aseguradora, null);
							BigDecimal valor10G = this.totalConta(conta, dataCalculo, null, grupoAseguradora);
							BigDecimal valor10T = this.totalConta(conta, dataCalculo, null, todasAseguradora);
							
							conta = cContas.get("0502000000");
							BigDecimal valor11 = this.totalConta(conta, dataCalculo, aseguradora, null);
							BigDecimal valor11G = this.totalConta(conta, dataCalculo, null, grupoAseguradora);
							BigDecimal valor11T = this.totalConta(conta, dataCalculo, null, todasAseguradora);
							
							BigDecimal numerador = valor1.subtract(valor2);
							BigDecimal divisor = valor3.subtract(valor4).add(valor5).add(valor6).add(valor7).subtract(valor8).subtract(valor9).add(valor10).add(valor11);
							
		    				if(divisor.doubleValue()!=0)
		    				{
		    					total = numerador.divide(divisor, 4, RoundingMode.HALF_EVEN);
		    					total = total.multiply(porCem);
		    				}
		    				
		        	    	if(total.doubleValue() == 0.00)
		        	    		total = new BigDecimal("0.00");
		    				
		    				celula = row.createCell(++coluna);
							celula.setCellValue(formataValor.format(total));
							celula.setCellStyle(estiloTextoD);
							
							numerador = valor1G.subtract(valor2G);
							divisor = valor3G.subtract(valor4G).add(valor5G).add(valor6G).add(valor7G).subtract(valor8G).subtract(valor9G).add(valor10G).add(valor11G);
							
							if(divisor.doubleValue()!=0)
							{
								total = numerador.divide(divisor, 4, RoundingMode.HALF_EVEN);
		    					total = total.multiply(porCem);
							}
		    				
		        	    	if(total.doubleValue() == 0.00)
		        	    		total = new BigDecimal("0.00");
		    				
		    				celula = row.createCell(++coluna);
							celula.setCellValue(formataValor.format(total));
							celula.setCellStyle(estiloTextoD);
							
							numerador = valor1T.subtract(valor2T);
							divisor = valor3T.subtract(valor4T).add(valor5T).add(valor6T).add(valor7T).subtract(valor8T).subtract(valor9T).add(valor10T).add(valor11T);
							
							if(divisor.doubleValue()!=0)
							{
								total = numerador.divide(divisor, 4, RoundingMode.HALF_EVEN);
		    					total = total.multiply(porCem);
							}
		    				
		        	    	if(total.doubleValue() == 0.00)
		        	    		total = new BigDecimal("0.00");
		    				
		    				celula = row.createCell(++coluna);
							celula.setCellValue(formataValor.format(total));
							celula.setCellStyle(estiloTextoD);
						}
						else if(nomeCalculo.equals("Gastos Operativos/ Primas Netas de Anulaciones"))
						{
							conta = cContas.get("0504000000");
							BigDecimal valor1 = this.totalConta(conta, dataCalculo, aseguradora, null);
							BigDecimal valor1G = this.totalConta(conta, dataCalculo, null, grupoAseguradora);
							BigDecimal valor1T = this.totalConta(conta, dataCalculo, null, todasAseguradora);
		    				
		    				conta = cContas.get("0510000000");
		    				BigDecimal valor2 = this.totalConta(conta, dataCalculo, aseguradora, null);
		    				BigDecimal valor2G = this.totalConta(conta, dataCalculo, null, grupoAseguradora);
		    				BigDecimal valor2T = this.totalConta(conta, dataCalculo, null, todasAseguradora);
		    				
		    				conta = cContas.get("0512000000");
							BigDecimal valor3 = this.totalConta(conta, dataCalculo, aseguradora, null);
							BigDecimal valor3G = this.totalConta(conta, dataCalculo, null, grupoAseguradora);
							BigDecimal valor3T = this.totalConta(conta, dataCalculo, null, todasAseguradora);
							
							conta = cContas.get("0514000000");
							BigDecimal valor4 = this.totalConta(conta, dataCalculo, aseguradora, null);
							BigDecimal valor4G = this.totalConta(conta, dataCalculo, null, grupoAseguradora);
							BigDecimal valor4T = this.totalConta(conta, dataCalculo, null, todasAseguradora);
							
							conta = cContas.get("0516000000");
							BigDecimal valor5 = this.totalConta(conta, dataCalculo, aseguradora, null);
							BigDecimal valor5G = this.totalConta(conta, dataCalculo, null, grupoAseguradora);
							BigDecimal valor5T = this.totalConta(conta, dataCalculo, null, todasAseguradora);
							
							conta = cContas.get("0525000000");
		    				BigDecimal valor6 = this.totalConta(conta, dataCalculo, aseguradora, null);
		    				BigDecimal valor6G = this.totalConta(conta, dataCalculo, null, grupoAseguradora);
		    				BigDecimal valor6T = this.totalConta(conta, dataCalculo, null, todasAseguradora);
		    				
		    				BigDecimal valor7 = conta.obterTotalizacaoExistenteBIG(aseguradora, mesUmAnoAnterior);
		    				BigDecimal valor7G = conta.obterTotalizacaoExistenteBIG(grupoAseguradora, mesUmAnoAnterior);
		    				BigDecimal valor7T = conta.obterTotalizacaoExistenteBIG(todasAseguradora, mesUmAnoAnterior);
		    				
		    				conta = cContas.get("0401000000");
							BigDecimal valor8 = this.totalConta(conta, dataCalculo, aseguradora, null);
							BigDecimal valor8G = this.totalConta(conta, dataCalculo, null, grupoAseguradora);
							BigDecimal valor8T = this.totalConta(conta, dataCalculo, null, todasAseguradora);
							
							conta = cContas.get("0402000000");
							BigDecimal valor9 = this.totalConta(conta, dataCalculo, aseguradora, null);
							BigDecimal valor9G = this.totalConta(conta, dataCalculo, null, grupoAseguradora);
							BigDecimal valor9T = this.totalConta(conta, dataCalculo, null, todasAseguradora);
							
							conta = cContas.get("0403000000");
		    				BigDecimal valor10 = this.totalConta(conta, dataCalculo, aseguradora, null);
		    				BigDecimal valor10G = this.totalConta(conta, dataCalculo, null, grupoAseguradora);
		    				BigDecimal valor10T = this.totalConta(conta, dataCalculo, null, todasAseguradora);
		    				
		    				conta = cContas.get("0212000000");
		    				BigDecimal valor11 = this.totalConta(conta, dataCalculo, aseguradora, null);
		    				BigDecimal valor11G = this.totalConta(conta, dataCalculo, null, grupoAseguradora);
		    				BigDecimal valor11T = this.totalConta(conta, dataCalculo, null, todasAseguradora);
		    				
		    				BigDecimal valor12 = conta.obterTotalizacaoExistenteBIG(aseguradora, mesUmAnoAnterior);
		    				BigDecimal valor12G = conta.obterTotalizacaoExistenteBIG(grupoAseguradora, mesUmAnoAnterior);
		    				BigDecimal valor12T = conta.obterTotalizacaoExistenteBIG(todasAseguradora, mesUmAnoAnterior);
		    				
							BigDecimal numerador = valor1.add(valor2).add(valor3).add(valor4).add(valor5).add(valor6);
							BigDecimal divisor = valor11.subtract(valor12).add(valor8).add(valor9).add(valor7).subtract(valor8).subtract(valor9).add(valor10);
							
		    				if(divisor.doubleValue()!=0)
		    				{
		    					total = numerador.divide(divisor, 4, RoundingMode.HALF_EVEN);
		    					total = total.multiply(porCem);
		    				}
		    				
		        	    	if(total.doubleValue() == 0.00)
		        	    		total = new BigDecimal("0.00");
		    				
		    				celula = row.createCell(++coluna);
							celula.setCellValue(formataValor.format(total));
							celula.setCellStyle(estiloTextoD);
							
							numerador = valor1G.add(valor2G).add(valor3G).add(valor4G).add(valor5G).add(valor6G);
							divisor = valor11G.subtract(valor12G).add(valor8G).add(valor9G).add(valor7G).subtract(valor8G).subtract(valor9G).add(valor10G);
							
							if(divisor.doubleValue()!=0)
							{
								total = numerador.divide(divisor, 4, RoundingMode.HALF_EVEN);
		    					total = total.multiply(porCem);
							}
		    				
		        	    	if(total.doubleValue() == 0.00)
		        	    		total = new BigDecimal("0.00");
		    				
		    				celula = row.createCell(++coluna);
							celula.setCellValue(formataValor.format(total));
							celula.setCellStyle(estiloTextoD);
							
							numerador = valor1T.add(valor2T).add(valor3T).add(valor4T).add(valor5T).add(valor6T);
							divisor = valor11T.subtract(valor12T).add(valor8T).add(valor9T).add(valor7T).subtract(valor8T).subtract(valor9T).add(valor10T);
							
							if(divisor.doubleValue()!=0)
							{
								total = numerador.divide(divisor, 4, RoundingMode.HALF_EVEN);
		    					total = total.multiply(porCem);
							}
		    				
		        	    	if(total.doubleValue() == 0.00)
		        	    		total = new BigDecimal("0.00");
		    				
		    				celula = row.createCell(++coluna);
							celula.setCellValue(formataValor.format(total));
							celula.setCellStyle(estiloTextoD);
						}
						else if(nomeCalculo.equals("Gastos Brutos de Producción / Primas Netas de Anulaciones"))
						{
							conta = cContas.get("0504000000");
							BigDecimal valor1 = this.totalConta(conta, dataCalculo, aseguradora, null);
							BigDecimal valor1G = this.totalConta(conta, dataCalculo, null, grupoAseguradora);
							BigDecimal valor1T = this.totalConta(conta, dataCalculo, null, todasAseguradora);
		    				
		    				conta = cContas.get("0510000000");
		    				BigDecimal valor2 = this.totalConta(conta, dataCalculo, aseguradora, null);
		    				BigDecimal valor2G = this.totalConta(conta, dataCalculo, null, grupoAseguradora);
		    				BigDecimal valor2T = this.totalConta(conta, dataCalculo, null, todasAseguradora);
		    				
		    				conta = cContas.get("0512000000");
							BigDecimal valor3 = this.totalConta(conta, dataCalculo, aseguradora, null);
							BigDecimal valor3G = this.totalConta(conta, dataCalculo, null, grupoAseguradora);
							BigDecimal valor3T = this.totalConta(conta, dataCalculo, null, todasAseguradora);
							
							conta = cContas.get("0514000000");
							BigDecimal valor4 = this.totalConta(conta, dataCalculo, aseguradora, null);
							BigDecimal valor4G = this.totalConta(conta, dataCalculo, null, grupoAseguradora);
							BigDecimal valor4T = this.totalConta(conta, dataCalculo, null, todasAseguradora);
							
							conta = cContas.get("0516000000");
							BigDecimal valor5 = this.totalConta(conta, dataCalculo, aseguradora, null);
							BigDecimal valor5G = this.totalConta(conta, dataCalculo, null, grupoAseguradora);
							BigDecimal valor5T = this.totalConta(conta, dataCalculo, null, todasAseguradora);
							
		    				BigDecimal valor7 = conta.obterTotalizacaoExistenteBIG(aseguradora, mesUmAnoAnterior);
		    				BigDecimal valor7G = conta.obterTotalizacaoExistenteBIG(grupoAseguradora, mesUmAnoAnterior);
		    				BigDecimal valor7T = conta.obterTotalizacaoExistenteBIG(todasAseguradora, mesUmAnoAnterior);
		    				
		    				conta = cContas.get("0401000000");
							BigDecimal valor8 = this.totalConta(conta, dataCalculo, aseguradora, null);
							BigDecimal valor8G = this.totalConta(conta, dataCalculo, null, grupoAseguradora);
							BigDecimal valor8T = this.totalConta(conta, dataCalculo, null, todasAseguradora);
							
							conta = cContas.get("0402000000");
							BigDecimal valor9 = this.totalConta(conta, dataCalculo, aseguradora, null);
							BigDecimal valor9G = this.totalConta(conta, dataCalculo, null, grupoAseguradora);
							BigDecimal valor9T = this.totalConta(conta, dataCalculo, null, todasAseguradora);
							
							conta = cContas.get("0403000000");
		    				BigDecimal valor10 = this.totalConta(conta, dataCalculo, aseguradora, null);
		    				BigDecimal valor10G = this.totalConta(conta, dataCalculo, null, grupoAseguradora);
		    				BigDecimal valor10T = this.totalConta(conta, dataCalculo, null, todasAseguradora);
		    				
		    				conta = cContas.get("0212000000");
		    				BigDecimal valor11 = this.totalConta(conta, dataCalculo, aseguradora, null);
		    				BigDecimal valor11G = this.totalConta(conta, dataCalculo, null, grupoAseguradora);
		    				BigDecimal valor11T = this.totalConta(conta, dataCalculo, null, todasAseguradora);
		    				
		    				BigDecimal valor12 = conta.obterTotalizacaoExistenteBIG(aseguradora, mesUmAnoAnterior);
		    				BigDecimal valor12G = conta.obterTotalizacaoExistenteBIG(grupoAseguradora, mesUmAnoAnterior);
		    				BigDecimal valor12T = conta.obterTotalizacaoExistenteBIG(todasAseguradora, mesUmAnoAnterior);
		    				
							BigDecimal numerador = valor1.add(valor2).add(valor3).add(valor4).add(valor5);
							BigDecimal divisor = valor11.subtract(valor12).add(valor8).add(valor9).add(valor7).subtract(valor8).subtract(valor9).add(valor10);
							
		    				if(divisor.doubleValue()!=0)
		    				{
		    					total = numerador.divide(divisor, 4, RoundingMode.HALF_EVEN);
		    					total = total.multiply(porCem);
		    				}
		    				
		        	    	if(total.doubleValue() == 0.00)
		        	    		total = new BigDecimal("0.00");
		    				
		    				celula = row.createCell(++coluna);
							celula.setCellValue(formataValor.format(total));
							celula.setCellStyle(estiloTextoD);
							
							numerador = valor1G.add(valor2G).add(valor3G).add(valor4G).add(valor5G);
							divisor = valor11G.subtract(valor12G).add(valor8G).add(valor9G).add(valor7G).subtract(valor8G).subtract(valor9G).add(valor10G);
							
							if(divisor.doubleValue()!=0)
							{
								total = numerador.divide(divisor, 4, RoundingMode.HALF_EVEN);
		    					total = total.multiply(porCem);
							}
		    				
		        	    	if(total.doubleValue() == 0.00)
		        	    		total = new BigDecimal("0.00");
		    				
		    				celula = row.createCell(++coluna);
							celula.setCellValue(formataValor.format(total));
							celula.setCellStyle(estiloTextoD);
							
							numerador = valor1T.add(valor2T).add(valor3T).add(valor4T).add(valor5T);
							divisor = valor11T.subtract(valor12T).add(valor8T).add(valor9T).add(valor7T).subtract(valor8T).subtract(valor9T).add(valor10T);
							
							if(divisor.doubleValue()!=0)
							{
								total = numerador.divide(divisor, 4, RoundingMode.HALF_EVEN);
		    					total = total.multiply(porCem);
							}
		    				
		        	    	if(total.doubleValue() == 0.00)
		        	    		total = new BigDecimal("0.00");
		    				
		    				celula = row.createCell(++coluna);
							celula.setCellValue(formataValor.format(total));
							celula.setCellStyle(estiloTextoD);
						}
						else if(nomeCalculo.equals("Provisiones Técnicas / Primas Netas de Anulaciones"))
						{
							conta = cContas.get("0212000000");
		    				BigDecimal valor1 = this.totalConta(conta, dataCalculo, aseguradora, null);
		    				BigDecimal valor1G = this.totalConta(conta, dataCalculo, null, grupoAseguradora);
		    				BigDecimal valor1T = this.totalConta(conta, dataCalculo, null, todasAseguradora);
		    				
		    				BigDecimal valor2 = conta.obterTotalizacaoExistenteBIG(aseguradora, mesUmAnoAnterior);
		    				BigDecimal valor2G = conta.obterTotalizacaoExistenteBIG(grupoAseguradora, mesUmAnoAnterior);
		    				BigDecimal valor2T = conta.obterTotalizacaoExistenteBIG(todasAseguradora, mesUmAnoAnterior);
		    				
							conta = cContas.get("0213000000");
							BigDecimal valor3 = this.totalConta(conta, dataCalculo, aseguradora, null);
							BigDecimal valor3G = this.totalConta(conta, dataCalculo, null, grupoAseguradora);
							BigDecimal valor3T = this.totalConta(conta, dataCalculo, null, todasAseguradora);
		    				
		    				conta = cContas.get("0401000000");
							BigDecimal valor4 = this.totalConta(conta, dataCalculo, aseguradora, null);
							BigDecimal valor4G = this.totalConta(conta, dataCalculo, null, grupoAseguradora);
							BigDecimal valor4T = this.totalConta(conta, dataCalculo, null, todasAseguradora);
							
							conta = cContas.get("0402000000");
							BigDecimal valor5 = this.totalConta(conta, dataCalculo, aseguradora, null);
							BigDecimal valor5G = this.totalConta(conta, dataCalculo, null, grupoAseguradora);
							BigDecimal valor5T = this.totalConta(conta, dataCalculo, null, todasAseguradora);
							
							conta = cContas.get("0403000000");
		    				BigDecimal valor6 = this.totalConta(conta, dataCalculo, aseguradora, null);
		    				BigDecimal valor6G = this.totalConta(conta, dataCalculo, null, grupoAseguradora);
		    				BigDecimal valor6T = this.totalConta(conta, dataCalculo, null, todasAseguradora);
		    				
		    				BigDecimal numerador = valor1.add(valor3);
							BigDecimal divisor = valor3.subtract(valor2).add(valor4).add(valor5).add(valor6);
		    				
							
		    				if(divisor.doubleValue()!=0)
		    				{
		    					total = numerador.divide(divisor, 4, RoundingMode.HALF_EVEN);
		    					total = total.multiply(porCem);
		    				}
		    				
		        	    	if(total.doubleValue() == 0.00)
		        	    		total = new BigDecimal("0.00");
		    				
		    				celula = row.createCell(++coluna);
							celula.setCellValue(formataValor.format(total));
							celula.setCellStyle(estiloTextoD);
							
							numerador = valor1G.add(valor3G);
							divisor = valor3G.subtract(valor2G).add(valor4G).add(valor5G).add(valor6G);
							
							if(divisor.doubleValue()!=0)
							{
								total = numerador.divide(divisor, 4, RoundingMode.HALF_EVEN);
		    					total = total.multiply(porCem);
							}
		    				
		        	    	if(total.doubleValue() == 0.00)
		        	    		total = new BigDecimal("0.00");
		    				
		    				celula = row.createCell(++coluna);
							celula.setCellValue(formataValor.format(total));
							celula.setCellStyle(estiloTextoD);
							
							numerador = valor1T.add(valor3T);
							divisor = valor3T.subtract(valor2T).add(valor4T).add(valor5T).add(valor6T);
							
							if(divisor.doubleValue()!=0)
							{
								total = numerador.divide(divisor, 4, RoundingMode.HALF_EVEN);
		    					total = total.multiply(porCem);
							}
		    				
		        	    	if(total.doubleValue() == 0.00)
		        	    		total = new BigDecimal("0.00");
		    				
		    				celula = row.createCell(++coluna);
							celula.setCellValue(formataValor.format(total));
							celula.setCellStyle(estiloTextoD);
						}
						else if(nomeCalculo.equals("Provisiones Técnicas / Siniestros"))
						{
							conta = cContas.get("0212000000");
		    				BigDecimal valor1 = this.totalConta(conta, dataCalculo, aseguradora, null);
		    				BigDecimal valor1G = this.totalConta(conta, dataCalculo, null, grupoAseguradora);
		    				BigDecimal valor1T = this.totalConta(conta, dataCalculo, null, todasAseguradora);
		    				
							conta = cContas.get("0213000000");
							BigDecimal valor2 = this.totalConta(conta, dataCalculo, aseguradora, null);
							BigDecimal valor2G = this.totalConta(conta, dataCalculo, null, grupoAseguradora);
							BigDecimal valor2T = this.totalConta(conta, dataCalculo, null, todasAseguradora);
		    				
		    				conta = cContas.get("0506000000");
							BigDecimal valor3 = this.totalConta(conta, dataCalculo, aseguradora, null);
							BigDecimal valor3G = this.totalConta(conta, dataCalculo, null, grupoAseguradora);
							BigDecimal valor3T = this.totalConta(conta, dataCalculo, null, todasAseguradora);
							
							conta = cContas.get("0507000000");
							BigDecimal valor4 = this.totalConta(conta, dataCalculo, aseguradora, null);
							BigDecimal valor4G = this.totalConta(conta, dataCalculo, null, grupoAseguradora);
							BigDecimal valor4T = this.totalConta(conta, dataCalculo, null, todasAseguradora);
							
							conta = cContas.get("0508000000");
		    				BigDecimal valor5 = this.totalConta(conta, dataCalculo, aseguradora, null);
		    				BigDecimal valor5G = this.totalConta(conta, dataCalculo, null, grupoAseguradora);
		    				BigDecimal valor5T = this.totalConta(conta, dataCalculo, null, todasAseguradora);
		    				
		    				conta = cContas.get("0509000000");
		    				BigDecimal valor6 = this.totalConta(conta, dataCalculo, aseguradora, null);
		    				BigDecimal valor6G = this.totalConta(conta, dataCalculo, null, grupoAseguradora);
		    				BigDecimal valor6T = this.totalConta(conta, dataCalculo, null, todasAseguradora);
		    				
		    				conta = cContas.get("0512000000");
		    				BigDecimal valor7 = this.totalConta(conta, dataCalculo, aseguradora, null);
		    				BigDecimal valor7G = this.totalConta(conta, dataCalculo, null, grupoAseguradora);
		    				BigDecimal valor7T = this.totalConta(conta, dataCalculo, null, todasAseguradora);
		    				
		    				conta = cContas.get("0407000000");
		    				BigDecimal valor8 = this.totalConta(conta, dataCalculo, aseguradora, null);
		    				BigDecimal valor8G = this.totalConta(conta, dataCalculo, null, grupoAseguradora);
		    				BigDecimal valor8T = this.totalConta(conta, dataCalculo, null, todasAseguradora);
		    				
		    				conta = cContas.get("0412000000");
		    				BigDecimal valor9 = this.totalConta(conta, dataCalculo, aseguradora, null);
		    				BigDecimal valor9G = this.totalConta(conta, dataCalculo, null, grupoAseguradora);
		    				BigDecimal valor9T = this.totalConta(conta, dataCalculo, null, todasAseguradora);
		    				
		    				conta = cContas.get("0408000000");
		    				BigDecimal valor10 = this.totalConta(conta, dataCalculo, aseguradora, null);
		    				BigDecimal valor10G = this.totalConta(conta, dataCalculo, null, grupoAseguradora);
		    				BigDecimal valor10T = this.totalConta(conta, dataCalculo, null, todasAseguradora);
		    				
		    				conta = cContas.get("0409000000");
		    				BigDecimal valor11 = this.totalConta(conta, dataCalculo, aseguradora, null);
		    				BigDecimal valor11G = this.totalConta(conta, dataCalculo, null, grupoAseguradora);
		    				BigDecimal valor11T = this.totalConta(conta, dataCalculo, null, todasAseguradora);
		    				
		    				BigDecimal numerador = valor1.add(valor2);
							BigDecimal divisor = valor3.add(valor4).add(valor5).add(valor6).add(valor7).subtract(valor8).subtract(valor9).subtract(valor10).subtract(valor11);
		    				
							
		    				if(divisor.doubleValue()!=0)
		    				{
		    					total = numerador.divide(divisor, 4, RoundingMode.HALF_EVEN);
		    					total = total.multiply(porCem);
		    				}
		    				
		        	    	if(total.doubleValue() == 0.00)
		        	    		total = new BigDecimal("0.00");
		    				
		    				celula = row.createCell(++coluna);
							celula.setCellValue(formataValor.format(total));
							celula.setCellStyle(estiloTextoD);
							
							numerador = valor1G.add(valor2G);
							divisor = valor3G.add(valor4G).add(valor5G).add(valor6G).add(valor7G).subtract(valor8G).subtract(valor9G).subtract(valor10G).subtract(valor11G);
							
							if(divisor.doubleValue()!=0)
							{
								total = numerador.divide(divisor, 4, RoundingMode.HALF_EVEN);
		    					total = total.multiply(porCem);
							}
		    				
		        	    	if(total.doubleValue() == 0.00)
		        	    		total = new BigDecimal("0.00");
		    				
		    				celula = row.createCell(++coluna);
							celula.setCellValue(formataValor.format(total));
							celula.setCellStyle(estiloTextoD);
							
							numerador = valor1T.add(valor2T);
							divisor = valor3T.add(valor4T).add(valor5T).add(valor6T).add(valor7T).subtract(valor8T).subtract(valor9T).subtract(valor10T).subtract(valor11T);
							
							if(divisor.doubleValue()!=0)
							{
								total = numerador.divide(divisor, 4, RoundingMode.HALF_EVEN);
		    					total = total.multiply(porCem);
							}
		    				
		        	    	if(total.doubleValue() == 0.00)
		        	    		total = new BigDecimal("0.00");
		    				
		    				celula = row.createCell(++coluna);
							celula.setCellValue(formataValor.format(total));
							celula.setCellStyle(estiloTextoD);
						}
						else if(nomeCalculo.equals("Siniestros pagados/Provision de siniestros"))
						{
							conta = cContas.get("0506000000");
		    				BigDecimal valor1 = this.totalConta(conta, dataCalculo, aseguradora, null);
		    				BigDecimal valor1G = this.totalConta(conta, dataCalculo, null, grupoAseguradora);
		    				BigDecimal valor1T = this.totalConta(conta, dataCalculo, null, todasAseguradora);
		    				
							conta = cContas.get("0213000000");
							BigDecimal valor2 = this.totalConta(conta, dataCalculo, aseguradora, null);
							BigDecimal valor2G = this.totalConta(conta, dataCalculo, null, grupoAseguradora);
							BigDecimal valor2T = this.totalConta(conta, dataCalculo, null, todasAseguradora);
		    				
		    				BigDecimal numerador = valor1;
							BigDecimal divisor = valor2;
		    				
		    				if(divisor.doubleValue()!=0)
		    				{
		    					total = numerador.divide(divisor, 4, RoundingMode.HALF_EVEN);
		    					total = total.multiply(porCem);
		    				}
		    				
		        	    	if(total.doubleValue() == 0.00)
		        	    		total = new BigDecimal("0.00");
		    				
		    				celula = row.createCell(++coluna);
							celula.setCellValue(formataValor.format(total));
							celula.setCellStyle(estiloTextoD);
							
							numerador = valor1G;
							divisor = valor2G;
							
							if(divisor.doubleValue()!=0)
							{
								total = numerador.divide(divisor, 4, RoundingMode.HALF_EVEN);
		    					total = total.multiply(porCem);
							}
		    				
		        	    	if(total.doubleValue() == 0.00)
		        	    		total = new BigDecimal("0.00");
		    				
		    				celula = row.createCell(++coluna);
							celula.setCellValue(formataValor.format(total));
							celula.setCellStyle(estiloTextoD);
							
							numerador = valor1T;
							divisor = valor2T;
							
							if(divisor.doubleValue()!=0)
							{
								total = numerador.divide(divisor, 4, RoundingMode.HALF_EVEN);
		    					total = total.multiply(porCem);
							}
		    				
		        	    	if(total.doubleValue() == 0.00)
		        	    		total = new BigDecimal("0.00");
		    				
		    				celula = row.createCell(++coluna);
							celula.setCellValue(formataValor.format(total));
							celula.setCellStyle(estiloTextoD);
						}
						else if(nomeCalculo.equals("Renta de Inversiones"))
						{
							conta = cContas.get("0425000000");
		    				BigDecimal valor1 = this.totalConta(conta, dataCalculo, aseguradora, null);
		    				BigDecimal valor1G = this.totalConta(conta, dataCalculo, null, grupoAseguradora);
		    				BigDecimal valor1T = this.totalConta(conta, dataCalculo, null, todasAseguradora);
		    				
							conta = cContas.get("0107000000");
							BigDecimal valor2 = this.totalConta(conta, dataCalculo, aseguradora, null);
							BigDecimal valor2G = this.totalConta(conta, dataCalculo, null, grupoAseguradora);
							BigDecimal valor2T = this.totalConta(conta, dataCalculo, null, todasAseguradora);
							
							BigDecimal valor3 = conta.obterTotalizacaoExistenteBIG(aseguradora, mesUmAnoAnterior);
							BigDecimal valor3G = conta.obterTotalizacaoExistenteBIG(grupoAseguradora, mesUmAnoAnterior);
							BigDecimal valor3T = conta.obterTotalizacaoExistenteBIG(todasAseguradora, mesUmAnoAnterior);
		    				
		    				conta = cContas.get("0526000000");
							BigDecimal valor4 = this.totalConta(conta, dataCalculo, aseguradora, null);
							BigDecimal valor4G = this.totalConta(conta, dataCalculo, null, grupoAseguradora);
							BigDecimal valor4T = this.totalConta(conta, dataCalculo, null, todasAseguradora);
							
							BigDecimal valor5 = conta.obterTotalizacaoExistenteBIG(aseguradora, mesUmAnoAnterior);
							BigDecimal valor5G = conta.obterTotalizacaoExistenteBIG(grupoAseguradora, mesUmAnoAnterior);
							BigDecimal valor5T = conta.obterTotalizacaoExistenteBIG(todasAseguradora, mesUmAnoAnterior);
							
		    				BigDecimal numerador = valor1;
							BigDecimal divisor = valor2.subtract(valor4).add(valor3).subtract(valor5);
		    				
							
		    				if(divisor.doubleValue()!=0)
		    				{
		    					total = numerador.divide(divisor, 4, RoundingMode.HALF_EVEN);
		    					total = total.multiply(porCem);
		    				}
		    				
		        	    	if(total.doubleValue() == 0.00)
		        	    		total = new BigDecimal("0.00");
		    				
		    				celula = row.createCell(++coluna);
							celula.setCellValue(formataValor.format(total));
							celula.setCellStyle(estiloTextoD);
							
							numerador = valor1G;
							divisor = valor2G.subtract(valor4G).add(valor3G).subtract(valor5G);
							
							if(divisor.doubleValue()!=0)
							{
								total = numerador.divide(divisor, 4, RoundingMode.HALF_EVEN);
		    					total = total.multiply(porCem);
							}
		    				
		        	    	if(total.doubleValue() == 0.00)
		        	    		total = new BigDecimal("0.00");
		    				
		    				celula = row.createCell(++coluna);
							celula.setCellValue(formataValor.format(total));
							celula.setCellStyle(estiloTextoD);
							
							numerador = valor1T;
							divisor = valor2T.subtract(valor4T).add(valor3T).subtract(valor5T);
							
							if(divisor.doubleValue()!=0)
							{
								total = numerador.divide(divisor, 4, RoundingMode.HALF_EVEN);
		    					total = total.multiply(porCem);
							}
		    				
		        	    	if(total.doubleValue() == 0.00)
		        	    		total = new BigDecimal("0.00");
		    				
		    				celula = row.createCell(++coluna);
							celula.setCellValue(formataValor.format(total));
							celula.setCellStyle(estiloTextoD);
						}
						else if(nomeCalculo.equals("Resultado Tecnico Neto/Primas Netas de Reaseguros"))
						{
							conta = cContas.get("0401000000");
		    				BigDecimal valor1 = this.totalConta(conta, dataCalculo, aseguradora, null);
		    				BigDecimal valor1G = this.totalConta(conta, dataCalculo, null, grupoAseguradora);
		    				BigDecimal valor1T = this.totalConta(conta, dataCalculo, null, todasAseguradora);
		    				
							conta = cContas.get("0402000000");
							BigDecimal valor2 = this.totalConta(conta, dataCalculo, aseguradora, null);
							BigDecimal valor2G = this.totalConta(conta, dataCalculo, null, grupoAseguradora);
							BigDecimal valor2T = this.totalConta(conta, dataCalculo, null, todasAseguradora);
							
		    				conta = cContas.get("0403000000");
							BigDecimal valor4 = this.totalConta(conta, dataCalculo, aseguradora, null);
							BigDecimal valor4G = this.totalConta(conta, dataCalculo, null, grupoAseguradora);
							BigDecimal valor4T = this.totalConta(conta, dataCalculo, null, todasAseguradora);
							
							conta = cContas.get("0404000000");
							BigDecimal valor5 = this.totalConta(conta, dataCalculo, aseguradora, null);
							BigDecimal valor5G = this.totalConta(conta, dataCalculo, null, grupoAseguradora);
							BigDecimal valor5T = this.totalConta(conta, dataCalculo, null, todasAseguradora);
							
							conta = cContas.get("0501000000");
							BigDecimal valor6 = this.totalConta(conta, dataCalculo, aseguradora, null);
							BigDecimal valor6G = this.totalConta(conta, dataCalculo, null, grupoAseguradora);
							BigDecimal valor6T = this.totalConta(conta, dataCalculo, null, todasAseguradora);
							
							conta = cContas.get("0502000000");
							BigDecimal valor7 = this.totalConta(conta, dataCalculo, aseguradora, null);
							BigDecimal valor7G = this.totalConta(conta, dataCalculo, null, grupoAseguradora);
							BigDecimal valor7T = this.totalConta(conta, dataCalculo, null, todasAseguradora);
							
							conta = cContas.get("0503000000");
							BigDecimal valor8 = this.totalConta(conta, dataCalculo, aseguradora, null);
							BigDecimal valor8G = this.totalConta(conta, dataCalculo, null, grupoAseguradora);
							BigDecimal valor8T = this.totalConta(conta, dataCalculo, null, todasAseguradora);
							
							conta = cContas.get("0505000000");
							BigDecimal valor9 = this.totalConta(conta, dataCalculo, aseguradora, null);
							BigDecimal valor9G = this.totalConta(conta, dataCalculo, null, grupoAseguradora);
							BigDecimal valor9T = this.totalConta(conta, dataCalculo, null, todasAseguradora);
							
							conta = cContas.get("0506000000");
							BigDecimal valor10 = this.totalConta(conta, dataCalculo, aseguradora, null);
							BigDecimal valor10G = this.totalConta(conta, dataCalculo, null, grupoAseguradora);
							BigDecimal valor10T = this.totalConta(conta, dataCalculo, null, todasAseguradora);
							
							conta = cContas.get("0507000000");
							BigDecimal valor11 = this.totalConta(conta, dataCalculo, aseguradora, null);
							BigDecimal valor11G = this.totalConta(conta, dataCalculo, null, grupoAseguradora);
							BigDecimal valor11T = this.totalConta(conta, dataCalculo, null, todasAseguradora);
							
							conta = cContas.get("0508000000");
							BigDecimal valor12 = this.totalConta(conta, dataCalculo, aseguradora, null);
							BigDecimal valor12G = this.totalConta(conta, dataCalculo, null, grupoAseguradora);
							BigDecimal valor12T = this.totalConta(conta, dataCalculo, null, todasAseguradora);
							
							conta = cContas.get("0509000000");
							BigDecimal valor13 = this.totalConta(conta, dataCalculo, aseguradora, null);
							BigDecimal valor13G = this.totalConta(conta, dataCalculo, null, grupoAseguradora);
							BigDecimal valor13T = this.totalConta(conta, dataCalculo, null, todasAseguradora);
							
							conta = cContas.get("0511000000");
							BigDecimal valor14 = this.totalConta(conta, dataCalculo, aseguradora, null);
							BigDecimal valor14G = this.totalConta(conta, dataCalculo, null, grupoAseguradora);
							BigDecimal valor14T = this.totalConta(conta, dataCalculo, null, todasAseguradora);
							
							conta = cContas.get("0513000000");
							BigDecimal valor15 = this.totalConta(conta, dataCalculo, aseguradora, null);
							BigDecimal valor15G = this.totalConta(conta, dataCalculo, null, grupoAseguradora);
							BigDecimal valor15T = this.totalConta(conta, dataCalculo, null, todasAseguradora);
							
							conta = cContas.get("0515000000");
							BigDecimal valor16 = this.totalConta(conta, dataCalculo, aseguradora, null);
							BigDecimal valor16G = this.totalConta(conta, dataCalculo, null, grupoAseguradora);
							BigDecimal valor16T = this.totalConta(conta, dataCalculo, null, todasAseguradora);
							
							conta = cContas.get("0406000000");
							BigDecimal valor17 = this.totalConta(conta, dataCalculo, aseguradora, null);
							BigDecimal valor17G = this.totalConta(conta, dataCalculo, null, grupoAseguradora);
							BigDecimal valor17T = this.totalConta(conta, dataCalculo, null, todasAseguradora);
							
							conta = cContas.get("0407000000");
							BigDecimal valor18 = this.totalConta(conta, dataCalculo, aseguradora, null);
							BigDecimal valor18G = this.totalConta(conta, dataCalculo, null, grupoAseguradora);
							BigDecimal valor18T = this.totalConta(conta, dataCalculo, null, todasAseguradora);
							
							conta = cContas.get("0408000000");
							BigDecimal valor19 = this.totalConta(conta, dataCalculo, aseguradora, null);
							BigDecimal valor19G = this.totalConta(conta, dataCalculo, null, grupoAseguradora);
							BigDecimal valor19T = this.totalConta(conta, dataCalculo, null, todasAseguradora);
							
							conta = cContas.get("0409000000");
							BigDecimal valor20 = this.totalConta(conta, dataCalculo, aseguradora, null);
							BigDecimal valor20G = this.totalConta(conta, dataCalculo, null, grupoAseguradora);
							BigDecimal valor20T = this.totalConta(conta, dataCalculo, null, todasAseguradora);
							
							conta = cContas.get("0412000000");
							BigDecimal valor21 = this.totalConta(conta, dataCalculo, aseguradora, null);
							BigDecimal valor21G = this.totalConta(conta, dataCalculo, null, grupoAseguradora);
							BigDecimal valor21T = this.totalConta(conta, dataCalculo, null, todasAseguradora);
							
							conta = cContas.get("0414000000");
							BigDecimal valor22 = this.totalConta(conta, dataCalculo, aseguradora, null);
							BigDecimal valor22G = this.totalConta(conta, dataCalculo, null, grupoAseguradora);
							BigDecimal valor22T = this.totalConta(conta, dataCalculo, null, todasAseguradora);
							
							conta = cContas.get("0405000000");
							BigDecimal valor23 = this.totalConta(conta, dataCalculo, aseguradora, null);
							BigDecimal valor23G = this.totalConta(conta, dataCalculo, null, grupoAseguradora);
							BigDecimal valor23T = this.totalConta(conta, dataCalculo, null, todasAseguradora);
							
							conta = cContas.get("0410000000");
							BigDecimal valor24 = this.totalConta(conta, dataCalculo, aseguradora, null);
							BigDecimal valor24G = this.totalConta(conta, dataCalculo, null, grupoAseguradora);
							BigDecimal valor24T = this.totalConta(conta, dataCalculo, null, todasAseguradora);
							
							conta = cContas.get("0411000000");
							BigDecimal valor25 = this.totalConta(conta, dataCalculo, aseguradora, null);
							BigDecimal valor25G = this.totalConta(conta, dataCalculo, null, grupoAseguradora);
							BigDecimal valor25T = this.totalConta(conta, dataCalculo, null, todasAseguradora);
							
							conta = cContas.get("0413000000");
							BigDecimal valor26 = this.totalConta(conta, dataCalculo, aseguradora, null);
							BigDecimal valor26G = this.totalConta(conta, dataCalculo, null, grupoAseguradora);
							BigDecimal valor26T = this.totalConta(conta, dataCalculo, null, todasAseguradora);
							
							conta = cContas.get("0415000000");
							BigDecimal valor27 = this.totalConta(conta, dataCalculo, aseguradora, null);
							BigDecimal valor27G = this.totalConta(conta, dataCalculo, null, grupoAseguradora);
							BigDecimal valor27T = this.totalConta(conta, dataCalculo, null, todasAseguradora);
							
							conta = cContas.get("0426000000");
							BigDecimal valor28 = this.totalConta(conta, dataCalculo, aseguradora, null);
							BigDecimal valor28G = this.totalConta(conta, dataCalculo, null, grupoAseguradora);
							BigDecimal valor28T = this.totalConta(conta, dataCalculo, null, todasAseguradora);
							
							conta = cContas.get("0504000000");
							BigDecimal valor29 = this.totalConta(conta, dataCalculo, aseguradora, null);
							BigDecimal valor29G = this.totalConta(conta, dataCalculo, null, grupoAseguradora);
							BigDecimal valor29T = this.totalConta(conta, dataCalculo, null, todasAseguradora);
							
							conta = cContas.get("0510000000");
							BigDecimal valor30 = this.totalConta(conta, dataCalculo, aseguradora, null);
							BigDecimal valor30G = this.totalConta(conta, dataCalculo, null, grupoAseguradora);
							BigDecimal valor30T = this.totalConta(conta, dataCalculo, null, todasAseguradora);
							
							conta = cContas.get("0512000000");
							BigDecimal valor31 = this.totalConta(conta, dataCalculo, aseguradora, null);
							BigDecimal valor31G = this.totalConta(conta, dataCalculo, null, grupoAseguradora);
							BigDecimal valor31T = this.totalConta(conta, dataCalculo, null, todasAseguradora);
							
							conta = cContas.get("0514000000");
							BigDecimal valor32 = this.totalConta(conta, dataCalculo, aseguradora, null);
							BigDecimal valor32G = this.totalConta(conta, dataCalculo, null, grupoAseguradora);
							BigDecimal valor32T = this.totalConta(conta, dataCalculo, null, todasAseguradora);
							
							conta = cContas.get("0516000000");
							BigDecimal valor33 = this.totalConta(conta, dataCalculo, aseguradora, null);
							BigDecimal valor33G = this.totalConta(conta, dataCalculo, null, grupoAseguradora);
							BigDecimal valor33T = this.totalConta(conta, dataCalculo, null, todasAseguradora);
							
							conta = cContas.get("0525000000");
							BigDecimal valor34 = this.totalConta(conta, dataCalculo, aseguradora, null);
							BigDecimal valor34G = this.totalConta(conta, dataCalculo, null, grupoAseguradora);
							BigDecimal valor34T = this.totalConta(conta, dataCalculo, null, todasAseguradora);
							
							conta2 = contas.get("0525010401");
							//BigDecimal valor35 = conta2.obterTotalizacaoExistenteBIG(aseguradora, mesAnoCalculo);
							BigDecimal valor35 = this.totalConta(conta2, dataCalculo, aseguradora, null);
							BigDecimal valor35G = this.totalConta(conta2, dataCalculo, null, grupoAseguradora);
							BigDecimal valor35T = this.totalConta(conta2, dataCalculo, null, todasAseguradora);
							
							conta = cContas.get("0527000000");
							BigDecimal valor36 = this.totalConta(conta, dataCalculo, aseguradora, null);
							BigDecimal valor36G = this.totalConta(conta, dataCalculo, null, grupoAseguradora);
							BigDecimal valor36T = this.totalConta(conta, dataCalculo, null, todasAseguradora);
							
							conta = cContas.get("0212000000");
							BigDecimal valor37 = this.totalConta(conta, dataCalculo, aseguradora, null);
							BigDecimal valor37G = this.totalConta(conta, dataCalculo, null, grupoAseguradora);
							BigDecimal valor37T = this.totalConta(conta, dataCalculo, null, todasAseguradora);
							
							BigDecimal valor38 = conta.obterTotalizacaoExistenteBIG(aseguradora, mesUmAnoAnterior);
							BigDecimal valor38G = conta.obterTotalizacaoExistenteBIG(grupoAseguradora, mesUmAnoAnterior);
							BigDecimal valor38T = conta.obterTotalizacaoExistenteBIG(todasAseguradora, mesUmAnoAnterior);
							
							conta = cContas.get("0109000000");
							BigDecimal valor39 = this.totalConta(conta, dataCalculo, aseguradora, null);
							BigDecimal valor39G = this.totalConta(conta, dataCalculo, null, grupoAseguradora);
							BigDecimal valor39T = this.totalConta(conta, dataCalculo, null, todasAseguradora);
							
							BigDecimal valor40 = conta.obterTotalizacaoExistenteBIG(aseguradora, mesUmAnoAnterior);
							BigDecimal valor40G = conta.obterTotalizacaoExistenteBIG(grupoAseguradora, mesUmAnoAnterior);
							BigDecimal valor40T = conta.obterTotalizacaoExistenteBIG(todasAseguradora, mesUmAnoAnterior);
							
		    				BigDecimal numerador = valor1.add(valor2).add(valor4).subtract(valor5).subtract(valor6).subtract(valor7).subtract(valor8).subtract(valor9).subtract(valor10).subtract(valor11).subtract(valor12).subtract(valor13).subtract(valor14).subtract(valor15).add(valor16).add(valor17).add(valor18).add(valor19).add(valor20).add(valor21).add(valor22).add(valor23).add(valor24).add(valor25).add(valor26).add(valor27).subtract(valor28).subtract(valor29).subtract(valor30).subtract(valor31).subtract(valor32).subtract(valor33).subtract(valor34).subtract(valor35).subtract(valor36);
							BigDecimal divisor = valor37.subtract(valor38).add(valor1).add(valor2).add(valor4).subtract(valor39).subtract(valor40).add(valor6).add(valor7);
							
		    				if(divisor.doubleValue()!=0)
		    				{
		    					total = numerador.divide(divisor, 4, RoundingMode.HALF_EVEN);
		    					total = total.multiply(porCem);
		    				}
		    				
		        	    	if(total.doubleValue() == 0.00)
		        	    		total = new BigDecimal("0.00");
		    				
		    				celula = row.createCell(++coluna);
							celula.setCellValue(formataValor.format(total));
							celula.setCellStyle(estiloTextoD);
							
							numerador = valor1G.add(valor2G).add(valor4G).subtract(valor5G).subtract(valor6G).subtract(valor7G).subtract(valor8G).subtract(valor9G).subtract(valor10G).subtract(valor11G).subtract(valor12G).subtract(valor13G).subtract(valor14G).subtract(valor15G).add(valor16G).add(valor17G).add(valor18G).add(valor19G).add(valor20G).add(valor21G).add(valor22G).add(valor23G).add(valor24G).add(valor25G).add(valor26G).add(valor27G).subtract(valor28G).subtract(valor29G).subtract(valor30G).subtract(valor31G).subtract(valor32G).subtract(valor33G).subtract(valor34G).subtract(valor35G).subtract(valor36G);
							divisor = valor37G.subtract(valor38G).add(valor1G).add(valor2G).add(valor4G).subtract(valor39G).subtract(valor40G).add(valor6G).add(valor7G);
							
							if(divisor.doubleValue()!=0)
							{
								total = numerador.divide(divisor, 4, RoundingMode.HALF_EVEN);
		    					total = total.multiply(porCem);
							}
		    				
		        	    	if(total.doubleValue() == 0.00)
		        	    		total = new BigDecimal("0.00");
		    				
		    				celula = row.createCell(++coluna);
							celula.setCellValue(formataValor.format(total));
							celula.setCellStyle(estiloTextoD);
							
							numerador = valor1T.add(valor2T).add(valor4T).subtract(valor5T).subtract(valor6T).subtract(valor7T).subtract(valor8T).subtract(valor9T).subtract(valor10T).subtract(valor11T).subtract(valor12T).subtract(valor13T).subtract(valor14T).subtract(valor15T).add(valor16T).add(valor17T).add(valor18T).add(valor19T).add(valor20T).add(valor21T).add(valor22T).add(valor23T).add(valor24T).add(valor25T).add(valor26T).add(valor27T).subtract(valor28T).subtract(valor29T).subtract(valor30T).subtract(valor31T).subtract(valor32T).subtract(valor33T).subtract(valor34T).subtract(valor35T).subtract(valor36T);
							divisor = valor37T.subtract(valor38T).add(valor1T).add(valor2T).add(valor4T).subtract(valor39T).subtract(valor40T).add(valor6T).add(valor7T);
							
							if(divisor.doubleValue()!=0)
							{
								total = numerador.divide(divisor, 4, RoundingMode.HALF_EVEN);
		    					total = total.multiply(porCem);
							}
		    				
		        	    	if(total.doubleValue() == 0.00)
		        	    		total = new BigDecimal("0.00");
		    				
		    				celula = row.createCell(++coluna);
							celula.setCellValue(formataValor.format(total));
							celula.setCellStyle(estiloTextoD);
						}
						else if(nomeCalculo.equals("Ratio de Ganancias (Ingresos - Egresos / Primas Directas)"))
						{
							conta = cContas.get("0400000000");
		    				BigDecimal valor1 = this.totalConta(conta, dataCalculo, aseguradora, null);
		    				BigDecimal valor1G = this.totalConta(conta, dataCalculo, null, grupoAseguradora);
		    				BigDecimal valor1T = this.totalConta(conta, dataCalculo, null, todasAseguradora);
		    				
							conta = cContas.get("0401000000");
							BigDecimal valor2 = this.totalConta(conta, dataCalculo, aseguradora, null);
							BigDecimal valor2G = this.totalConta(conta, dataCalculo, null, grupoAseguradora);
							BigDecimal valor2T = this.totalConta(conta, dataCalculo, null, todasAseguradora);
		    				
		    				BigDecimal numerador = valor1;
							BigDecimal divisor = valor2;
		    				
		    				if(divisor.doubleValue()!=0)
		    				{
		    					total = numerador.divide(divisor, 4, RoundingMode.HALF_EVEN);
		    					total = total.multiply(porCem);
		    				}
		    				
		        	    	if(total.doubleValue() == 0.00)
		        	    		total = new BigDecimal("0.00");
		    				
		    				celula = row.createCell(++coluna);
							celula.setCellValue(formataValor.format(total));
							celula.setCellStyle(estiloTextoD);
							
							numerador = valor1G;
							divisor = valor2G;
							
							if(divisor.doubleValue()!=0)
							{
								total = numerador.divide(divisor, 4, RoundingMode.HALF_EVEN);
		    					total = total.multiply(porCem);
							}
		    				
		        	    	if(total.doubleValue() == 0.00)
		        	    		total = new BigDecimal("0.00");
		    				
		    				celula = row.createCell(++coluna);
							celula.setCellValue(formataValor.format(total));
							celula.setCellStyle(estiloTextoD);
							
							numerador = valor1T;
							divisor = valor2T;
							
							if(divisor.doubleValue()!=0)
							{
								total = numerador.divide(divisor, 4, RoundingMode.HALF_EVEN);
		    					total = total.multiply(porCem);
							}
		    				
		        	    	if(total.doubleValue() == 0.00)
		        	    		total = new BigDecimal("0.00");
		    				
		    				celula = row.createCell(++coluna);
							celula.setCellValue(formataValor.format(total));
							celula.setCellStyle(estiloTextoD);
						}
						
						if(dataInicio!=null)
							c.add(Calendar.MONTH, -1);
						else
							c.add(Calendar.YEAR, -1);
					}
				}
			}
			
			linha+=3;
		}
		
		 wb.write(stream);
		 stream.flush();
		 stream.close();
	}
	
	private ClassificacaoContas classificacao;
	private Conta conta;
	
	private void instanciarContas() throws Exception
	{
		this.cContas = new TreeMap<String, ClassificacaoContas>();
		this.contas = new TreeMap<String, Conta>();
		
		String[] contas = {"0401000000","0402000000","0403000000","0501000000","0502000000","0301000000","0303000000","0301020000","0301010000","0107060000","0109000000","0102000000","0103000000","0104000000","0100000000","0107010000","0107020000",
				"0107030000","0107040000","0212000000","0109030000","0213030000","0506000000","0213020000","0300000000","0400000000","0500000000","0504000000","0510000000","0512000000","0514000000","0516000000","0525000000","0213000000","0507000000",
				"0508000000","0509000000","0407000000","0412000000","0408000000","0409000000","0425000000","0107000000","0526000000","0404000000","0503000000","0505000000","0511000000","0513000000","0515000000","0406000000","0407000000","0408000000",
				"0409000000","0414000000","0405000000","0410000000","0411000000","0413000000","0415000000","0426000000","0510000000","0512000000","0514000000","0516000000","0527000000"
		};
		
		
		for(int i = 0 ; i < contas.length ; i++)
		{
			classificacao = (ClassificacaoContas) this.home.obterEntidadePorApelido(contas[i].trim());
			this.cContas.put(contas[i].trim(), classificacao);
		}
		
		
		String[] contas2 = {"0525010401"};
		
		for(int i = 0 ; i < contas2.length ; i++)
		{
			conta = (Conta) this.home.obterEntidadePorApelido(contas2[i].trim());
			this.contas.put(contas2[i].trim(), conta);
		}
	}
	
	private BigDecimal totalConta(Entidade contaCalculo, Date data, Entidade aseguradora, Collection<Entidade> aseguradoras) throws Exception
	{
		BigDecimal total = new BigDecimal("0");
		
		if(calculoAnual)
		{
			if(contaCalculo instanceof ClassificacaoContas)
			{
				classificacao = (ClassificacaoContas) contaCalculo;
				
				if(aseguradora!=null)
				{
					if(anoFiscal)
						total = classificacao.obterTotalizacaoExistenteAnualFiscalBIG(aseguradora, Integer.valueOf(new SimpleDateFormat("yyyy").format(data)));
					else
						total = classificacao.obterTotalizacaoExistenteAnualBIG(aseguradora, Integer.valueOf(new SimpleDateFormat("yyyy").format(data)));
				}
				else
				{
					if(anoFiscal)
						total = classificacao.obterTotalizacaoExistenteAnualFiscalBIG(aseguradoras, Integer.valueOf(new SimpleDateFormat("yyyy").format(data)));
					else
						total = classificacao.obterTotalizacaoExistenteAnualBIG(aseguradoras, Integer.valueOf(new SimpleDateFormat("yyyy").format(data)));
				}
			}
			else
			{
				conta = (Conta) contaCalculo;
				
				if(aseguradora!=null)
				{
					if(anoFiscal)
						total = conta.obterTotalizacaoExistenteAnualFiscalBIG(aseguradora, Integer.valueOf(new SimpleDateFormat("yyyy").format(data)));
					else
						total = conta.obterTotalizacaoExistenteAnualBIG(aseguradora, Integer.valueOf(new SimpleDateFormat("yyyy").format(data)));
				}
				else
				{
					if(anoFiscal)
						total = conta.obterTotalizacaoExistenteAnualFiscalBIG(aseguradoras, Integer.valueOf(new SimpleDateFormat("yyyy").format(data)));
					else
						total = conta.obterTotalizacaoExistenteAnualBIG(aseguradoras, Integer.valueOf(new SimpleDateFormat("yyyy").format(data)));
				}
				
			}
		}
		else
		{
			if(contaCalculo instanceof ClassificacaoContas)
			{
				classificacao = (ClassificacaoContas) contaCalculo;
				if(aseguradora!=null)
					total=classificacao.obterTotalizacaoExistenteBIG(aseguradora, new SimpleDateFormat("MMyyyy").format(data));
				else
					total=classificacao.obterTotalizacaoExistenteBIG(aseguradoras, new SimpleDateFormat("MMyyyy").format(data));
			}
			else
			{
				conta = (Conta) contaCalculo;
				if(aseguradora!=null)
					total=conta.obterTotalizacaoExistenteBIG(aseguradora, new SimpleDateFormat("MMyyyy").format(data));
				else
					total=conta.obterTotalizacaoExistenteBIG(aseguradoras, new SimpleDateFormat("MMyyyy").format(data));
			}
		}
		
		return total;
	}
}
