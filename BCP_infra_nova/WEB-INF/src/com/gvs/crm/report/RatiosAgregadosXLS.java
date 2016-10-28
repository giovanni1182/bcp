package com.gvs.crm.report;

import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import org.apache.poi.hssf.util.Region;

import com.gvs.crm.model.ClassificacaoContas;
import com.gvs.crm.model.Conta;
import com.gvs.crm.model.Entidade;
import com.gvs.crm.model.EntidadeHome;

public class RatiosAgregadosXLS extends Excel
{
	private Map<String,ClassificacaoContas> cContas;
	private Map<String,Conta> contas;
	private  EntidadeHome home;
	
	public RatiosAgregadosXLS(Date dataInicio, Date dataFim, Collection<Entidade> aseguradoras, EntidadeHome home) throws Exception
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
        fonteTexto.setFontHeightInPoints((short)8);
        fonteTexto.setFontName("Arial");
        
        HSSFCellStyle estiloTexto = wb.createCellStyle();
        estiloTexto.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        estiloTexto.setFont(fonteTexto);
        
        HSSFCellStyle estiloTitulo_E = wb.createCellStyle();
        estiloTitulo_E.setFont(fonteTitulo);
		
		Collection<String> ratios = new ArrayList<String>();
		ratios.add("Año");
		ratios.add("Mes");
		ratios.add("Evolución de Primas Netas de Anulaciones");
		ratios.add("Evolución de Primas Netas de Reaseguros");
		ratios.add("Siniestros Brutos / Primas Netas de Anulaciones");
		ratios.add("Siniestros Netos de Reaseguros / Primas Netas de Reaseguros");
		ratios.add("Gastos y Costos / Primas Netas de Reaseguros");
		ratios.add("Gastos Operativos/ Primas Netas de Anulaciones");
		ratios.add("Gastos Operativos / Primas Netas de Reaseguros");
		ratios.add("Gastos Brutos de Producción / Primas Netas de Anulaciones");
		ratios.add("Gastos de Explotación / Primas netas de Anulaciones");
		ratios.add("Provisiones Técnicas / Primas Netas de Anulaciones");
		ratios.add("Provisiones Técnicas / Siniestros");
		ratios.add("Cambios en el patrimonio Neto");
		ratios.add("Financiamiento del Activo");
		ratios.add("Activos Inmovilizados /  Activo Total");
		ratios.add("Capital en Riesgo / Patrimonio Neto");
		ratios.add("Primas Netas de Reaseguros / Patrimonio  Neto");
		ratios.add("Siniestros Pendientes / Patrimonio  Neto");
		ratios.add("Retorno sobre el Patrimonio Neto");
		ratios.add("Margen sobre Primas Netas de Reaseguros");
		ratios.add("Renta de Inversiones");
		ratios.add("Resultado Técnico Neto/ Primas Netas de Reaseguros");
		ratios.add("Cambio en los Flujos de Efectivo por Actividades Operativas");
		ratios.add("Cambio en los Flujos de Efectivo por Actividades de Inversión");
		ratios.add("Utilidad/pérdida técnica bruta");
		ratios.add("Utilidad/pérdida técnica neta");
		ratios.add("Utilidad/pérdida neta del ejercicio");
		
		HSSFSheet planilha = wb.createSheet("Plan");
		
		HSSFRow row;
		HSSFCell celula;
		Calendar c, c1Ano, c2Anos;
	    String mes,ano,mesAnoAnterior,mesDoisAnosAnterior,nomeRatios,mesAnoCalculo;
	    ClassificacaoContas conta;
	    Conta conta2;
	    Date data1Ano, data2Anos;
	    
	    int linha = 0;
		int coluna = 0;
	    row = planilha.createRow(linha);
	    
	    if(aseguradoras.size() == home.obterAseguradoras().size())
	    {
	    	celula = row.createCell(coluna);
			celula.setCellValue("Calculo de Ratios Agregados de todas las aseguradorass Fecha " + formataData.format(dataInicio) + " hasta " + formataData.format(dataFim));
			celula.setCellStyle(estiloTitulo_E);
	    }
	    else
	    {
	    	Entidade aseg;
	    	String nomes = "Calculo de Ratios Agregados";
	    	for(Iterator<Entidade> i = aseguradoras.iterator() ; i.hasNext() ; )
			{
	    		aseg = i.next();
	    		
    			nomes +=" - "+ aseg.obterNome();
			}
	    	
	    	celula = row.createCell(coluna);
			celula.setCellValue(nomes);
			celula.setCellStyle(estiloTitulo_E);
	    }
	    
	    Region r = new Region(0, (short)0, 0, (short)ratios.size());
	    planilha.addMergedRegion(r);
	    
	    linha++;
	    row = planilha.createRow(linha);
	    
	    
	    for(Iterator<String> j = ratios.iterator() ; j.hasNext() ; )
		{
			nomeRatios = j.next();
			
			celula = row.createCell(coluna);
			celula.setCellValue(nomeRatios);
			celula.setCellStyle(estiloTitulo);
			
			coluna++;
		}
	    
	    linha++;
	    
	    c = Calendar.getInstance();
		c.setTime(dataInicioReal);
		c1Ano = Calendar.getInstance();
		c2Anos = Calendar.getInstance();
	    
		while(c.getTime().compareTo(dataFimReal)<=0)
		{
			c1Ano.setTime(c.getTime());
			c1Ano.add(Calendar.YEAR, -1);
			data1Ano = c1Ano.getTime();
			mes = new SimpleDateFormat("MM").format(data1Ano);
			ano = new SimpleDateFormat("yyyy").format(data1Ano);
			mesAnoAnterior = mes+ano;
			
			c2Anos.setTime(c.getTime());
			c2Anos.add(Calendar.YEAR, -2);
			data2Anos = c2Anos.getTime();
			mes = new SimpleDateFormat("MM").format(data2Anos);
			ano = new SimpleDateFormat("yyyy").format(data2Anos);
			mesDoisAnosAnterior = mes+ano;
			
			mes = new SimpleDateFormat("MM").format(c.getTime());
			ano = new SimpleDateFormat("yyyy").format(c.getTime());
			mesAnoCalculo = mes+ano;
			
			/*System.out.println("mesAnoCalculo " + mesAnoCalculo);
			System.out.println("mesAnoAnterior " + mesAnoAnterior);
			System.out.println("mesDoisAnosAnterior " + mesDoisAnosAnterior);*/
			
			coluna = 0;
			
			row = planilha.createRow(linha);
			celula = row.createCell(coluna);
			celula.setCellValue(ano);
			celula.setCellStyle(estiloTexto);
			
			celula = row.createCell(++coluna);
			celula.setCellValue(mes);
			celula.setCellStyle(estiloTexto);
			
			// CONTAS PRA FAZER O CÁLCULO do RATIOS 1
			
			conta = cContas.get("0212000000");
			double cContas1ValorAtual = conta.obterTotalizacaoExistente(aseguradoras, mesAnoCalculo);
			double cContas1ValorAnterior = conta.obterTotalizacaoExistente(aseguradoras,mesAnoAnterior);
			double cContas1ValorDoisAnosAnterior = conta.obterTotalizacaoExistente(aseguradoras,mesDoisAnosAnterior);

			conta = cContas.get("0401000000");
			double cContas2ValorAtual = conta.obterTotalizacaoExistente(aseguradoras,mesAnoCalculo);
			double cContas2ValorAnterior = conta.obterTotalizacaoExistente(aseguradoras,mesAnoAnterior);

			conta = cContas.get("0402000000");
			double cContas3ValorAtual = conta.obterTotalizacaoExistente(aseguradoras,mesAnoCalculo);
			double cContas3ValorAnterior = conta.obterTotalizacaoExistente(aseguradoras,mesAnoAnterior);
			
			conta = cContas.get("0403000000");
			double cContas4ValorAtual = conta.obterTotalizacaoExistente(aseguradoras,mesAnoCalculo);
			double cContas4ValorAnterior = conta.obterTotalizacaoExistente(aseguradoras,mesAnoAnterior);
			
			double totalDividendoRatio1 = (cContas1ValorAtual - cContas1ValorAnterior + cContas2ValorAtual	+ cContas3ValorAtual + cContas4ValorAtual) - (cContas1ValorAnterior - cContas1ValorDoisAnosAnterior	+ cContas2ValorAnterior	+ cContas3ValorAnterior + cContas4ValorAnterior);
			double totalDivisorRatio1 = (cContas1ValorAnterior - cContas1ValorDoisAnosAnterior)	+ cContas2ValorAnterior + cContas3ValorAnterior	+ cContas4ValorAnterior;

			double totalRatio1 = 0;

			if (totalDivisorRatio1 > 0)
			{
				totalRatio1 = (totalDividendoRatio1 / totalDivisorRatio1) * 100;
				if(totalDividendoRatio1 == 0 && totalDivisorRatio1 < 0)
					totalRatio1 = totalRatio1*-1;
			}
			
			celula = row.createCell(++coluna);
			celula.setCellValue(formataValor.format(totalRatio1));
			celula.setCellStyle(estiloTexto);
			
			//Calculo do Indicador RATIO2
			
			conta = cContas.get("0109030000");
			double cContas5ValorAtual = conta.obterTotalizacaoExistente(aseguradoras,	mesAnoCalculo);
			
			double cContas5ValorAnterior = conta.obterTotalizacaoExistente(aseguradoras,	mesAnoAnterior);
			double cContas5ValorDoisAnosAnterior = conta.obterTotalizacaoExistente(aseguradoras,mesDoisAnosAnterior);
			
			conta = cContas.get("0501000000");
			double cContas6ValorAtual = conta.obterTotalizacaoExistente(aseguradoras,	mesAnoCalculo);
			double cContas6ValorAnterior = conta.obterTotalizacaoExistente(aseguradoras,mesAnoAnterior);

			conta = cContas.get("0502000000");
			double cContas7ValorAtual = conta.obterTotalizacaoExistente(aseguradoras,	mesAnoCalculo);
			double cContas7ValorAnterior = conta.obterTotalizacaoExistente(aseguradoras,mesAnoAnterior);

			double totalDividendoRatio2 = ((cContas1ValorAtual	- cContas1ValorAnterior + cContas2ValorAtual + cContas3ValorAtual + cContas4ValorAtual) - (cContas5ValorAtual - cContas5ValorAnterior + cContas6ValorAtual + cContas7ValorAtual)
					- (cContas1ValorAnterior - cContas1ValorDoisAnosAnterior + cContas2ValorAnterior + cContas3ValorAnterior + cContas4ValorAnterior) - (cContas5ValorAnterior - cContas5ValorDoisAnosAnterior + cContas6ValorAnterior + cContas7ValorAnterior));
			double totalDivisorRatio2 = (cContas1ValorAnterior - cContas1ValorDoisAnosAnterior + cContas2ValorAnterior + cContas3ValorAnterior + cContas4ValorAnterior)	- (cContas5ValorAnterior - cContas5ValorDoisAnosAnterior + cContas6ValorAnterior + cContas7ValorAnterior);

			double totalRatio2 = 0;

			if (totalDivisorRatio2 !=0)
			{
				totalRatio2 = (totalDividendoRatio2 / totalDivisorRatio2) * 100;
				if(totalDividendoRatio2 == 0 && totalDivisorRatio2 < 0)
					totalRatio2 = totalRatio2*-1;
			}
			
			celula = row.createCell(++coluna);
			celula.setCellValue(formataValor.format(totalRatio2));
			celula.setCellStyle(estiloTexto);
			
			//Calculo do Indicador RATIO 3
			
			conta = cContas.get("0506000000");
			double cContas8ValorAtual = conta.obterTotalizacaoExistente(aseguradoras, mesAnoCalculo);
			double cContas8ValorAnterior = conta.obterTotalizacaoExistente(aseguradoras,mesAnoAnterior);
			
			conta = cContas.get("0507000000");
			double cContas9ValorAtual = conta.obterTotalizacaoExistente(aseguradoras,	mesAnoCalculo);
			double cContas9ValorAnterior = conta.obterTotalizacaoExistente(aseguradoras,	mesAnoAnterior);
			
			conta = cContas.get("0508000000");
			double cContas10ValorAtual = conta.obterTotalizacaoExistente(aseguradoras,mesAnoCalculo);
			double cContas10ValorAnterior = conta.obterTotalizacaoExistente(aseguradoras,mesAnoAnterior);
			
			conta = cContas.get("0509000000");
			double cContas11ValorAtual = conta.obterTotalizacaoExistente(aseguradoras,mesAnoCalculo);
			double cContas11ValorAnterior = conta.obterTotalizacaoExistente(aseguradoras,mesAnoAnterior);
			
			conta = cContas.get("0512000000");
			double cContas12ValorAtual = conta.obterTotalizacaoExistente(aseguradoras,mesAnoCalculo);
			double cContas12ValorAnterior = conta.obterTotalizacaoExistente(aseguradoras,mesAnoAnterior);
			
			conta = cContas.get("0407000000");
			double cContas13ValorAtual = conta.obterTotalizacaoExistente(aseguradoras,mesAnoCalculo);
			double cContas13ValorAnterior = conta.obterTotalizacaoExistente(aseguradoras,mesAnoAnterior);
			
			conta = cContas.get("0412000000");
			double cContas14ValorAtual = conta.obterTotalizacaoExistente(aseguradoras,mesAnoCalculo);

			double totalDividendoRatio3 = cContas8ValorAtual + cContas9ValorAtual + cContas10ValorAtual	+ cContas11ValorAtual + cContas12ValorAtual	- cContas13ValorAtual - cContas14ValorAtual;
			double totalDivisorRatio3 = cContas1ValorAtual - cContas1ValorAnterior + cContas2ValorAtual	+ cContas3ValorAtual + cContas4ValorAtual;
			double totalRatio3 = 0;

			if (totalDivisorRatio3 !=0)
			{
				totalRatio3 = (totalDividendoRatio3 / totalDivisorRatio3) * 100;
				if(totalDividendoRatio3 == 0 && totalDivisorRatio3 < 0)
					totalRatio3 = totalRatio3*-1;
			}
			
			celula = row.createCell(++coluna);
			celula.setCellValue(formataValor.format(totalRatio3));
			celula.setCellStyle(estiloTexto);
			
			
			//Calculo do Indicador RATIO 4

			conta = cContas.get("0408000000");
			double cContas15ValorAtual = conta.obterTotalizacaoExistente(aseguradoras,mesAnoCalculo);
			double cContas15ValorAnterior = conta.obterTotalizacaoExistente(aseguradoras,mesAnoAnterior);
			
			conta = cContas.get("0409000000");
			double cContas16ValorAtual = conta.obterTotalizacaoExistente(aseguradoras,mesAnoCalculo);
			double cContas16ValorAnterior = conta.obterTotalizacaoExistente(aseguradoras,mesAnoAnterior);

			double totalDividendoRatio4 = cContas8ValorAtual
					+ cContas9ValorAtual + cContas10ValorAtual
					+ cContas11ValorAtual + cContas12ValorAtual
					- cContas13ValorAtual - cContas14ValorAtual
					- cContas15ValorAtual - cContas16ValorAtual;

			double totalDivisorRatio4 = cContas1ValorAtual
					- cContas1ValorAnterior + cContas2ValorAtual
					+ cContas3ValorAtual + cContas4ValorAtual
					- cContas5ValorAtual - cContas4ValorAnterior
					+ cContas6ValorAtual + cContas7ValorAtual;
			
			double totalRatio4 = 0;

			if (totalDivisorRatio4 != 0)
			{
				totalRatio4 = (totalDividendoRatio4 / totalDivisorRatio4) * 100;
				if(totalDividendoRatio4 == 0 && totalDivisorRatio4 < 0)
					totalRatio4 = totalRatio4*-1;
			}
			
			celula = row.createCell(++coluna);
			celula.setCellValue(formataValor.format(totalRatio4));
			celula.setCellStyle(estiloTexto);
			
			//Calculo do Indicador RATIO 5
			
			conta = cContas.get("0504000000");
			double cContas17ValorAtual = conta.obterTotalizacaoExistente(aseguradoras,mesAnoCalculo);
			double cContas17ValorAnterior = conta.obterTotalizacaoExistente(aseguradoras,mesAnoAnterior);
			
			conta = cContas.get("0510000000");
			double cContas18ValorAtual = conta.obterTotalizacaoExistente(aseguradoras,mesAnoCalculo);
			double cContas18ValorAnterior = conta.obterTotalizacaoExistente(aseguradoras,mesAnoAnterior);
			
			conta = cContas.get("0514000000");
			double cContas19ValorAtual = conta.obterTotalizacaoExistente(aseguradoras,mesAnoCalculo);
			
			conta = cContas.get("0516000000");
			double cContas20ValorAtual = conta.obterTotalizacaoExistente(aseguradoras,mesAnoCalculo);
			
			conta = cContas.get("0405000000");
			double cContas21ValorAtual = conta.obterTotalizacaoExistente(aseguradoras,mesAnoCalculo);
			
			conta = cContas.get("0410000000");
			double cContas22ValorAtual = conta.obterTotalizacaoExistente(aseguradoras,mesAnoCalculo);
			double cContas22ValorAnterior = conta.obterTotalizacaoExistente(aseguradoras,mesAnoAnterior);
			
			conta = cContas.get("0411000000");
			double cContas23ValorAtual = conta.obterTotalizacaoExistente(aseguradoras,mesAnoCalculo);
			double cContas23ValorAnterior = conta.obterTotalizacaoExistente(aseguradoras,mesAnoAnterior);
			
			conta = cContas.get("0412000000");
			double cContas24ValorAtual = conta.obterTotalizacaoExistente(aseguradoras,mesAnoCalculo);
			double cContas24ValorAnterior = conta.obterTotalizacaoExistente(aseguradoras,mesAnoAnterior);
			
			conta = cContas.get("0525000000");
			double cContas25ValorAtual = conta.obterTotalizacaoExistente(aseguradoras,mesAnoCalculo);

			double totalDividendoRatio5 = ((cContas17ValorAtual
					+ cContas18ValorAtual + cContas12ValorAtual
					+ cContas19ValorAtual + cContas20ValorAtual
					- cContas21ValorAtual - cContas22ValorAtual
					- cContas23ValorAtual - cContas24ValorAtual)
					+ (cContas25ValorAtual) + (cContas8ValorAtual
					+ cContas9ValorAtual + cContas10ValorAtual
					+ cContas11ValorAtual + cContas12ValorAtual
					- cContas13ValorAtual - cContas24ValorAtual)
					- (cContas15ValorAtual + cContas16ValorAtual));

			double totalDivisorRatio5 = (cContas1ValorAtual
					- cContas1ValorAnterior + cContas2ValorAtual
					+ cContas3ValorAtual + cContas4ValorAtual)
					- (cContas5ValorAtual - cContas5ValorAnterior
					+ cContas6ValorAtual + cContas7ValorAtual);

			double totalRatio5 = 0;

			if (totalDivisorRatio5 != 0)
			{
				totalRatio5 = (totalDividendoRatio5 / totalDivisorRatio5) * 100;
				if(totalDividendoRatio5 == 0 && totalDivisorRatio5 < 0)
					totalRatio5 = totalRatio5*-1;
			}
			
			celula = row.createCell(++coluna);
			celula.setCellValue(formataValor.format(totalRatio5));
			celula.setCellStyle(estiloTexto);
			
			//Calculo do Indicador RATIO 6

			double totalDividendoRatio6 = cContas17ValorAtual
					+ cContas18ValorAtual + cContas12ValorAtual
					+ cContas19ValorAtual + cContas20ValorAtual
					+ cContas25ValorAtual;

			double totalDivisorRatio6 = cContas1ValorAtual
					- cContas1ValorAnterior + cContas2ValorAtual
					+ cContas3ValorAtual + cContas4ValorAtual;

			double totalRatio6 = 0;

			if (totalDivisorRatio6 != 0)
			{
				totalRatio6 = (totalDividendoRatio6 / totalDivisorRatio6) * 100;
				if(totalDividendoRatio6 == 0 && totalDivisorRatio6 < 0)
					totalRatio6 = totalRatio6*-1;
			}
			
			celula = row.createCell(++coluna);
			celula.setCellValue(formataValor.format(totalRatio6));
			celula.setCellStyle(estiloTexto);
			
			//Calculo do Indicador RATIO 7

			double totalDividendoRatio7 = (cContas17ValorAtual
					+ cContas18ValorAtual + cContas12ValorAtual
					+ cContas19ValorAtual + cContas20ValorAtual
					- cContas21ValorAtual - cContas22ValorAtual
					- cContas23ValorAtual - cContas24ValorAtual)
					+ cContas25ValorAtual;

			double totalDivisorRatio7 = (cContas1ValorAtual
					- cContas1ValorAnterior + cContas2ValorAtual
					+ cContas3ValorAtual + cContas4ValorAtual)
					- (cContas5ValorAtual - cContas5ValorAnterior
					+ cContas6ValorAtual + cContas7ValorAtual);

			double totalRatio7 = 0;

			if (totalDivisorRatio7 != 0)
			{
				totalRatio7 = (totalDividendoRatio7 / totalDivisorRatio7) * 100;
				if(totalDividendoRatio7 == 0 && totalDivisorRatio7 < 0)
					totalRatio7 = totalRatio7*-1;
			}
			
			celula = row.createCell(++coluna);
			celula.setCellValue(formataValor.format(totalRatio7));
			celula.setCellStyle(estiloTexto);
			
			
			//Calculo do Indicador RATIO 8

			double totalDividendoRatio8 = cContas17ValorAtual
					+ cContas18ValorAtual + cContas12ValorAtual
					+ cContas19ValorAtual + cContas20ValorAtual;

			double totalDivisorRatio8 = cContas1ValorAtual
					- cContas1ValorAnterior + cContas2ValorAtual
					+ cContas3ValorAtual + cContas4ValorAtual;

			double totalRatio8 = 0;

			if (totalDivisorRatio8 != 0)
			{
				totalRatio8 = (totalDividendoRatio8 / totalDivisorRatio8) * 100;
				if(totalDividendoRatio8 == 0 && totalDivisorRatio8 < 0)
					totalRatio8 = totalRatio8*-1;
			}
			
			celula = row.createCell(++coluna);
			celula.setCellValue(formataValor.format(totalRatio8));
			celula.setCellStyle(estiloTexto);
			
			//Calculo do Indicador RATIO 9

			double totalDividendoRatio9 = cContas25ValorAtual;

			double totalDivisorRatio9 = cContas1ValorAtual
					- cContas1ValorAnterior + cContas2ValorAtual
					+ cContas3ValorAtual + cContas4ValorAtual;

			double totalRatio9 = 0;

			if (totalDivisorRatio9 != 0)
			{
				totalRatio9 = (totalDividendoRatio9 / totalDivisorRatio9) * 100;
				if(totalDividendoRatio9 == 0 && totalDivisorRatio9 < 0)
					totalRatio9 = totalRatio9*-1;
			}
			
			celula = row.createCell(++coluna);
			celula.setCellValue(formataValor.format(totalRatio9));
			celula.setCellStyle(estiloTexto);

			
			//Calculo do Indicador RATIO 10
			
			conta = cContas.get("0213000000");
			double cContas26ValorAtual = conta.obterTotalizacaoExistente(aseguradoras,mesAnoCalculo);
			double cContas26ValorAnterior = conta.obterTotalizacaoExistente(aseguradoras,mesAnoAnterior);

			double totalDividendoRatio10 = cContas1ValorAtual
					+ cContas26ValorAtual;

			double totalDivisorRatio10 = cContas1ValorAtual
					- cContas1ValorAnterior + cContas2ValorAtual
					+ cContas3ValorAtual + cContas4ValorAtual;

			double totalRatio10 = 0;

			if (totalDivisorRatio10 != 0)
			{
				totalRatio10 = (totalDividendoRatio10 / totalDivisorRatio10) * 100;
				if(totalDividendoRatio10 == 0 && totalDivisorRatio10 < 0)
					totalRatio10 = totalRatio10*-1;
			}
			
			celula = row.createCell(++coluna);
			celula.setCellValue(formataValor.format(totalRatio10));
			celula.setCellStyle(estiloTexto);
			
			//Calculo do Indicador RATIO 11

			double totalDividendoRatio11 = cContas1ValorAtual
					+ cContas26ValorAtual;

			double totalDivisorRatio11 = (cContas8ValorAtual
					+ cContas9ValorAtual + cContas10ValorAtual
					+ cContas11ValorAtual + cContas12ValorAtual
					- cContas13ValorAtual - cContas14ValorAtual)
					- (cContas15ValorAtual + cContas16ValorAtual);

			double totalRatio11 = 0;

			if (totalDivisorRatio11 != 0)
			{
				totalRatio11 = (totalDividendoRatio11 / totalDivisorRatio11) * 100;
				if(totalDividendoRatio11 == 0 && totalDivisorRatio11 < 0)
					totalRatio11 = totalRatio11*-1;
			}
			
			celula = row.createCell(++coluna);
			celula.setCellValue(formataValor.format(totalRatio11));
			celula.setCellStyle(estiloTexto);
			
			
			//Calculo do Indicador RATIO 12
			
			conta = cContas.get("0300000000");
			double cContas27ValorAtual = conta.obterTotalizacaoExistente(aseguradoras,mesAnoCalculo);
			double cContas27ValorAnterior = conta.obterTotalizacaoExistente(aseguradoras,mesAnoAnterior);

			double totalDividendoRatio12 = cContas27ValorAtual
					- cContas27ValorAnterior;

			double totalDivisorRatio12 = cContas27ValorAnterior;

			double totalRatio12 = 0;

			if (totalDivisorRatio12 != 0)
			{
				totalRatio12 = (totalDividendoRatio12 / totalDivisorRatio12) * 100;
				if(totalDividendoRatio12 == 0 && totalDivisorRatio12 < 0)
					totalRatio12 = totalRatio12*-1;
			}
			
			celula = row.createCell(++coluna);
			celula.setCellValue(formataValor.format(totalRatio12));
			celula.setCellStyle(estiloTexto);
			
			
			//Calculo do Indicador RATIO 13
			
			conta = cContas.get("0100000000");
			double cContas28ValorAtual = conta.obterTotalizacaoExistente(aseguradoras,mesAnoCalculo);

			double totalDividendoRatio13 = cContas27ValorAtual;

			double totalDivisorRatio13 = cContas28ValorAtual;

			double totalRatio13 = 0;

			if (totalDivisorRatio13 != 0)
			{
				totalRatio13 = (totalDividendoRatio13 / totalDivisorRatio13) * 100;
				if(totalDividendoRatio13 == 0 && totalDivisorRatio13 < 0)
					totalRatio13 = totalRatio13*-1;
			}
			
			celula = row.createCell(++coluna);
			celula.setCellValue(formataValor.format(totalRatio13));
			celula.setCellStyle(estiloTexto);
			
			
			//Calculo do Indicador RATIO 14
			
			conta = cContas.get("0108000000");
			double cContas29ValorAtual = conta.obterTotalizacaoExistente(aseguradoras,mesAnoCalculo);

			double totalDividendoRatio14 = cContas29ValorAtual;

			double totalDivisorRatio14 = cContas28ValorAtual;

			double totalRatio14 = 0;

			if (totalDivisorRatio14 != 0)
			{
				totalRatio14 = (totalDividendoRatio14 / totalDivisorRatio14) * 100;
				if(totalDividendoRatio14 == 0 && totalDivisorRatio14 < 0)
					totalRatio14 = totalRatio14*-1;
			}
			
			celula = row.createCell(++coluna);
			celula.setCellValue(formataValor.format(totalRatio14));
			celula.setCellStyle(estiloTexto);

			
			//Calculo do Indicador RATIO 15
			
			conta = cContas.get("0701010000");
			double cContas30ValorAtual = conta.obterTotalizacaoExistente(aseguradoras,mesAnoCalculo);
			
			conta = cContas.get("0601010000");
			double cContas31ValorAtual = conta.obterTotalizacaoExistente(aseguradoras,mesAnoCalculo);

			double totalDividendoRatio15 = cContas30ValorAtual- cContas31ValorAtual;

			double totalDivisorRatio15 = cContas27ValorAtual;

			double totalRatio15 = 0;

			if (totalDivisorRatio15 != 0)
			{
				totalRatio15 = (totalDividendoRatio15 / totalDivisorRatio15) * 100;
				if(totalDividendoRatio15 == 0 && totalDivisorRatio15 < 0)
					totalRatio15 = totalRatio15*-1;
			}
			
			celula = row.createCell(++coluna);
			celula.setCellValue(formataValor.format(totalRatio15));
			celula.setCellStyle(estiloTexto);
			
			
			//Calculo do Indicador RATIO 16
			double totalDividendoRatio16 = cContas1ValorAtual
					- cContas1ValorAnterior + cContas2ValorAtual
					+ cContas3ValorAtual + cContas4ValorAtual
					- cContas5ValorAtual - cContas5ValorAnterior
					+ cContas6ValorAtual + cContas7ValorAtual;

			double totalDivisorRatio16 = cContas27ValorAtual;

			double totalRatio16 = 0;

			if (totalDivisorRatio16 != 0)
			{
				totalRatio16 = (totalDividendoRatio16 / totalDivisorRatio16) * 100;
				if(totalDividendoRatio16 == 0 && totalDivisorRatio16 < 0)
					totalRatio16 = totalRatio16*-1;
			}
			
			celula = row.createCell(++coluna);
			celula.setCellValue(formataValor.format(totalRatio16));
			celula.setCellStyle(estiloTexto);
			
			
			//Calculo do Indicador RATIO 17
			
			conta = cContas.get("0213020000");
			double cContas32ValorAtual = conta.obterTotalizacaoExistente(aseguradoras,mesAnoCalculo);
			
			double totalDividendoRatio17 = cContas32ValorAtual;

			double totalDivisorRatio17 = cContas27ValorAtual;

			double totalRatio17 = 0;

			if (totalDivisorRatio17 != 0)
			{
				totalRatio17 = (totalDividendoRatio17 / totalDivisorRatio17) * 100;
				if(totalDividendoRatio17 == 0 && totalDivisorRatio17 < 0)
					totalRatio17 = totalRatio17*-1;
			}
			
			celula = row.createCell(++coluna);
			celula.setCellValue(formataValor.format(totalRatio17));
			celula.setCellStyle(estiloTexto);
			
			
			//Calculo do Indicador RATIO 18
			
			//Calculo alterado, de 0540 para 0400 - 0500
			//conta = cContas.get("0540000000");
			conta = cContas.get("0400000000");
			double aux = conta.obterTotalizacaoExistente(aseguradoras,mesAnoCalculo);
			conta = cContas.get("0500000000");
			aux-= conta.obterTotalizacaoExistente(aseguradoras,mesAnoCalculo);
			
			//double cContas33ValorAtual = conta.obterTotalizacaoExistente(aseguradoras,mesAnoCalculo);
			double cContas33ValorAtual = aux;

			double totalDividendoRatio18 = cContas33ValorAtual;

			double totalDivisorRatio18 = cContas27ValorAtual;

			double totalRatio18 = 0;

			if (totalDivisorRatio18 != 0)
			{
				totalRatio18 = (totalDividendoRatio18 / totalDivisorRatio18) * 100;
				if(totalDividendoRatio18 == 0 && totalDivisorRatio18 < 0)
					totalRatio18 = totalRatio18*-1;
			}
			
			celula = row.createCell(++coluna);
			celula.setCellValue(formataValor.format(totalRatio18));
			celula.setCellStyle(estiloTexto);
			
			
			//Calculo do Indicador RATIO 19

			double totalDividendoRatio19 = cContas33ValorAtual;

			double totalDivisorRatio19 = cContas1ValorAtual
					- cContas1ValorAnterior + cContas2ValorAtual
					+ cContas3ValorAtual + cContas4ValorAtual
					- cContas5ValorAtual - cContas5ValorAnterior
					+ cContas6ValorAtual + cContas7ValorAtual;

			double totalRatio19 = 0;

			if (totalDivisorRatio19 != 0)
			{
				totalRatio19 = (totalDividendoRatio19 / totalDivisorRatio19) * 100;
				if(totalDividendoRatio19 == 0 && totalDivisorRatio19 < 0)
					totalRatio19 = totalRatio19*-1;
			}
			
			celula = row.createCell(++coluna);
			celula.setCellValue(formataValor.format(totalRatio19));
			celula.setCellStyle(estiloTexto);
			
			
			//Calculo do Indicador RATIO 20
			
			conta = cContas.get("0425000000");
			double cContas34ValorAtual = conta.obterTotalizacaoExistente(aseguradoras,mesAnoCalculo);
			double cContas34ValorAnterior = conta.obterTotalizacaoExistente(aseguradoras,mesAnoAnterior);
			
			conta = cContas.get("0107000000");
			double cContas35ValorAtual = conta.obterTotalizacaoExistente(aseguradoras,mesAnoCalculo);
			double cContas35ValorAnterior = conta.obterTotalizacaoExistente(aseguradoras,mesAnoAnterior);
			
			conta = cContas.get("0526000000");
			double cContas36ValorAtual = conta.obterTotalizacaoExistente(aseguradoras,mesAnoCalculo);
			double cContas36ValorAnterior = conta.obterTotalizacaoExistente(aseguradoras,mesAnoAnterior);
			

			double totalDividendoRatio20 = cContas34ValorAtual;

			double totalDivisorRatio20 = cContas35ValorAtual
					- cContas36ValorAtual + cContas35ValorAnterior
					- cContas36ValorAnterior;

			double totalRatio20 = 0;

			if (totalDivisorRatio20 != 0)
			{
				totalRatio20 = (totalDividendoRatio20 / totalDivisorRatio20) * 100;
				if(totalDividendoRatio20 == 0 && totalDivisorRatio20 < 0)
					totalRatio20 = totalRatio20*-1;
			}
			
			celula = row.createCell(++coluna);
			celula.setCellValue(formataValor.format(totalRatio20));
			celula.setCellStyle(estiloTexto);
			
			
			//Calculo do Indicador RATIO 21
			
			conta = cContas.get("0404000000");
			double cContas37ValorAtual = conta.obterTotalizacaoExistente(aseguradoras,mesAnoCalculo);
			
			conta = cContas.get("0503000000");
			double cContas38ValorAtual = conta.obterTotalizacaoExistente(aseguradoras,mesAnoCalculo);
			
			conta = cContas.get("0505000000");
			double cContas39ValorAtual = conta.obterTotalizacaoExistente(aseguradoras,mesAnoCalculo);
			double cContas39ValorAnterior = conta.obterTotalizacaoExistente(aseguradoras,mesAnoAnterior);
			
			conta = cContas.get("0511000000");
			double cContas40ValorAtual = conta.obterTotalizacaoExistente(aseguradoras,mesAnoCalculo);
			double cContas40ValorAnterior = conta.obterTotalizacaoExistente(aseguradoras,mesAnoAnterior);
			
			conta = cContas.get("0513000000");
			double cContas41ValorAtual = conta.obterTotalizacaoExistente(aseguradoras,mesAnoCalculo);
			double cContas41ValorAnterior = conta.obterTotalizacaoExistente(aseguradoras,mesAnoAnterior);
			
			conta = cContas.get("0515000000");
			double cContas42ValorAtual = conta.obterTotalizacaoExistente(aseguradoras,mesAnoCalculo);
			double cContas42ValorAnterior = conta.obterTotalizacaoExistente(aseguradoras,mesAnoAnterior);
			
			conta = cContas.get("0406000000");
			double cContas43ValorAtual = conta.obterTotalizacaoExistente(aseguradoras,mesAnoCalculo);
			double cContas43ValorAnterior = conta.obterTotalizacaoExistente(aseguradoras,mesAnoAnterior);
			
			conta = cContas.get("0414000000");
			double cContas44ValorAtual = conta.obterTotalizacaoExistente(aseguradoras,mesAnoCalculo);
			double cContas44ValorAnterior = conta.obterTotalizacaoExistente(aseguradoras,mesAnoAnterior);
			
			conta = cContas.get("0413000000");
			double cContas45ValorAtual = conta.obterTotalizacaoExistente(aseguradoras,mesAnoCalculo);
			
			conta = cContas.get("0415000000");
			double cContas46ValorAtual = conta.obterTotalizacaoExistente(aseguradoras,mesAnoCalculo);
			
			conta = cContas.get("0426000000");
			double cContas47ValorAtual = conta.obterTotalizacaoExistente(aseguradoras,mesAnoCalculo);
			double cContas47ValorAnterior = conta.obterTotalizacaoExistente(aseguradoras,mesAnoAnterior);
			
			conta2 = contas.get("0525010401");
			double conta48ValorAtual = conta2.obterTotalizacaoExistente(aseguradoras,mesAnoCalculo);
			double conta48ValorAnterior = conta2.obterTotalizacaoExistente(aseguradoras,mesAnoAnterior);
			
			conta = cContas.get("0527000000");
			double cContas49ValorAtual = conta.obterTotalizacaoExistente(aseguradoras,mesAnoCalculo);
			double cContas49ValorAnterior = conta.obterTotalizacaoExistente(aseguradoras,mesAnoAnterior);

			double totalDividendoRatio21 = cContas2ValorAtual
					+ cContas3ValorAtual + cContas4ValorAtual
					+ cContas37ValorAtual - cContas6ValorAtual
					- cContas7ValorAtual - cContas38ValorAtual
					- cContas39ValorAtual - cContas8ValorAtual
					- cContas9ValorAtual - cContas10ValorAtual
					- cContas11ValorAtual - cContas40ValorAtual
					- cContas41ValorAtual - cContas42ValorAtual
					+ cContas43ValorAtual + cContas13ValorAtual
					+ cContas15ValorAtual + cContas16ValorAtual
					+ cContas14ValorAtual + cContas44ValorAtual
					+ cContas21ValorAtual + cContas22ValorAtual
					+ cContas23ValorAtual + cContas45ValorAtual
					+ cContas46ValorAtual + cContas47ValorAtual
					- cContas17ValorAtual - cContas18ValorAtual
					- cContas12ValorAtual - cContas19ValorAtual
					- cContas20ValorAtual - cContas25ValorAtual
					- conta48ValorAtual - cContas49ValorAtual;

			double totalDivisorRatio21 = (cContas1ValorAtual
					- cContas1ValorAnterior + cContas2ValorAtual
					+ cContas3ValorAtual + cContas4ValorAtual)
					- (cContas5ValorAtual - cContas5ValorAnterior
					+ cContas6ValorAtual + cContas7ValorAtual);

			double totalRatio21 = 0;

			if (totalDivisorRatio21 != 0)
			{
				totalRatio21 = (totalDividendoRatio21 / totalDivisorRatio21) * 100;
				if(totalDividendoRatio21 == 0 && totalDivisorRatio21 < 0)
					totalRatio21 = totalRatio21*-1;
			}
			
			celula = row.createCell(++coluna);
			celula.setCellValue(formataValor.format(totalRatio21));
			celula.setCellStyle(estiloTexto);
			

			//Calculo do Indicador RATIO 24 *** TÁ CERTO EM PULAR DO 21 PARA O 24, O PESSOA NÃO QUER O 22 E 23
			
			conta = cContas.get("0405020000");
			double cContas51ValorAtual = conta.obterTotalizacaoExistente(aseguradoras,mesAnoCalculo);
			
			conta = cContas.get("0102010000");
			double cContas52ValorAtual = conta.obterTotalizacaoExistente(aseguradoras,mesAnoCalculo);
			
			conta = cContas.get("0103010000");
			double cContas53ValorAtual = conta.obterTotalizacaoExistente(aseguradoras,mesAnoCalculo);
			
			conta = cContas.get("0102020000");
			double cContas54ValorAtual = conta.obterTotalizacaoExistente(aseguradoras,mesAnoCalculo);
			
			conta = cContas.get("0103020000");
			double cContas55ValorAtual = conta.obterTotalizacaoExistente(aseguradoras,mesAnoCalculo);
			
			conta = cContas.get("0203000000");
			double cContas56ValorAtual = conta.obterTotalizacaoExistente(aseguradoras,mesAnoCalculo);
			
			conta = cContas.get("0106010000");
			double cContas57ValorAtual = conta.obterTotalizacaoExistente(aseguradoras,mesAnoCalculo);
			
			conta = cContas.get("0202030000");
			double cContas58ValorAtual = conta.obterTotalizacaoExistente(aseguradoras,mesAnoCalculo);
			
			conta = cContas.get("0202040000");
			double cContas59ValorAtual = conta.obterTotalizacaoExistente(aseguradoras,mesAnoCalculo);
			
			conta = cContas.get("0214010000");
			double cContas60ValorAtual = conta.obterTotalizacaoExistente(aseguradoras,mesAnoCalculo);
			
			conta = cContas.get("0202010000");
			double cContas61ValorAtual = conta.obterTotalizacaoExistente(aseguradoras,mesAnoCalculo);
			
			conta = cContas.get("0202020000");
			double cContas62ValorAtual = conta.obterTotalizacaoExistente(aseguradoras,mesAnoCalculo);
			
			conta = cContas.get("0202050000");
			double cContas63ValorAtual = conta.obterTotalizacaoExistente(aseguradoras,mesAnoCalculo);
			
			conta = cContas.get("0202060000");
			double cContas64ValorAtual = conta.obterTotalizacaoExistente(aseguradoras,mesAnoCalculo);
			
			conta = cContas.get("0102032000");
			double cContas65ValorAtual = conta.obterTotalizacaoExistente(aseguradoras,mesAnoCalculo);
			
			conta = cContas.get("0103032000");
			double cContas66ValorAtual = conta.obterTotalizacaoExistente(aseguradoras,mesAnoCalculo);
			
			conta = cContas.get("0102032100");
			double cContas67ValorAtual = conta.obterTotalizacaoExistente(aseguradoras,mesAnoCalculo);
			
			conta = cContas.get("0103032100");
			double cContas68ValorAtual = conta.obterTotalizacaoExistente(aseguradoras,mesAnoCalculo);
			
			conta = cContas.get("0106020000");
			double cContas69ValorAtual = conta.obterTotalizacaoExistente(aseguradoras,mesAnoCalculo);
			
			conta = cContas.get("0525010700");
			double cContas70ValorAtual = conta.obterTotalizacaoExistente(aseguradoras,mesAnoCalculo);
			
			conta = cContas.get("0525010800");
			double cContas71ValorAtual = conta.obterTotalizacaoExistente(aseguradoras,mesAnoCalculo);
			
			conta = cContas.get("0525010400");
			double cContas72ValorAtual = conta.obterTotalizacaoExistente(aseguradoras,mesAnoCalculo);
			
			conta = cContas.get("0102050000");
			double cContas73ValorAtual = conta.obterTotalizacaoExistente(aseguradoras,mesAnoCalculo);
			
			conta = cContas.get("0102060000");
			double cContas74ValorAtual = conta.obterTotalizacaoExistente(aseguradoras,mesAnoCalculo);
			
			conta = cContas.get("0104000000");
			double cContas75ValorAtual = conta.obterTotalizacaoExistente(aseguradoras,mesAnoCalculo);
			
			conta = cContas.get("0105000000");
			double cContas76ValorAtual = conta.obterTotalizacaoExistente(aseguradoras,mesAnoCalculo);
			
			conta = cContas.get("0109010000");
			double cContas77ValorAtual = conta.obterTotalizacaoExistente(aseguradoras,mesAnoCalculo);
			
			conta = cContas.get("0109020000");
			double cContas78ValorAtual = conta.obterTotalizacaoExistente(aseguradoras,mesAnoCalculo);
			
			conta = cContas.get("0109070000");
			double cContas79ValorAtual = conta.obterTotalizacaoExistente(aseguradoras,mesAnoCalculo);
			
			conta = cContas.get("0109080000");
			double cContas80ValorAtual = conta.obterTotalizacaoExistente(aseguradoras,mesAnoCalculo);
			
			conta = cContas.get("0206000000");
			double cContas81ValorAtual = conta.obterTotalizacaoExistente(aseguradoras,mesAnoCalculo);
			
			conta = cContas.get("0210000000");
			double cContas82ValorAtual = conta.obterTotalizacaoExistente(aseguradoras,mesAnoCalculo);
			
			conta = cContas.get("0211000000");
			double cContas83ValorAtual = conta.obterTotalizacaoExistente(aseguradoras,mesAnoCalculo);
			
			conta = cContas.get("0205000000");
			double cContas84ValorAtual = conta.obterTotalizacaoExistente(aseguradoras,mesAnoCalculo);
			
			conta = cContas.get("0214030000");
			double cContas86ValorAtual = conta.obterTotalizacaoExistente(aseguradoras,mesAnoCalculo);
			
			conta = cContas.get("0214040000");
			double cContas87ValorAtual = conta.obterTotalizacaoExistente(aseguradoras,mesAnoCalculo);
			
			conta = cContas.get("0109040000");
			double cContas88ValorAtual = conta.obterTotalizacaoExistente(aseguradoras,mesAnoCalculo);
			
			conta = cContas.get("0109050000");
			double cContas89ValorAtual = conta.obterTotalizacaoExistente(aseguradoras,mesAnoCalculo);
			
			conta = cContas.get("0109060000");
			double cContas90ValorAtual = conta.obterTotalizacaoExistente(aseguradoras,mesAnoCalculo);
			
			conta = cContas.get("0211010200");
			double cContas91ValorAtual = conta.obterTotalizacaoExistente(aseguradoras,mesAnoCalculo);
			
			conta = cContas.get("0405020000");
			double cContas51ValorAnterior = conta.obterTotalizacaoExistente(aseguradoras,mesAnoAnterior);
			
			conta = cContas.get("0102010000");
			double cContas52ValorAnterior = conta.obterTotalizacaoExistente(aseguradoras,mesAnoAnterior);
			
			conta = cContas.get("0103010000");
			double cContas53ValorAnterior = conta.obterTotalizacaoExistente(aseguradoras,mesAnoAnterior);
			
			conta = cContas.get("0102020000");
			double cContas54ValorAnterior = conta.obterTotalizacaoExistente(aseguradoras,mesAnoAnterior);
			
			conta = cContas.get("0103020000");
			double cContas55ValorAnterior = conta.obterTotalizacaoExistente(aseguradoras,mesAnoAnterior);
			
			conta = cContas.get("0203000000");
			double cContas56ValorAnterior = conta.obterTotalizacaoExistente(aseguradoras,mesAnoAnterior);
			
			conta = cContas.get("0106010000");
			double cContas57ValorAnterior = conta.obterTotalizacaoExistente(aseguradoras,mesAnoAnterior);
			
			conta = cContas.get("0202030000");
			double cContas58ValorAnterior = conta.obterTotalizacaoExistente(aseguradoras,mesAnoAnterior);
			
			conta = cContas.get("0202040000");
			double cContas59ValorAnterior = conta.obterTotalizacaoExistente(aseguradoras,mesAnoAnterior);
			
			conta = cContas.get("0214010000");
			double cContas60ValorAnterior = conta.obterTotalizacaoExistente(aseguradoras,mesAnoAnterior);
			
			conta = cContas.get("0202010000");
			double cContas61ValorAnterior = conta.obterTotalizacaoExistente(aseguradoras,mesAnoAnterior);
			
			conta = cContas.get("0202020000");
			double cContas62ValorAnterior = conta.obterTotalizacaoExistente(aseguradoras,mesAnoAnterior);
			
			conta = cContas.get("0202050000");
			double cContas63ValorAnterior = conta.obterTotalizacaoExistente(aseguradoras,mesAnoAnterior);
			
			conta = cContas.get("0202060000");
			double cContas64ValorAnterior = conta.obterTotalizacaoExistente(aseguradoras,mesAnoAnterior);
			
			conta = cContas.get("0102032000");
			double cContas65ValorAnterior = conta.obterTotalizacaoExistente(aseguradoras,mesAnoAnterior);
			
			conta = cContas.get("0103032000");
			double cContas66ValorAnterior = conta.obterTotalizacaoExistente(aseguradoras,mesAnoAnterior);
			
			conta = cContas.get("0102032100");
			double cContas67ValorAnterior = conta.obterTotalizacaoExistente(aseguradoras,mesAnoAnterior);
			
			conta = cContas.get("0103032100");
			double cContas68ValorAnterior = conta.obterTotalizacaoExistente(aseguradoras,mesAnoAnterior);
			
			conta = cContas.get("0106020000");
			double cContas69ValorAnterior = conta.obterTotalizacaoExistente(aseguradoras,mesAnoAnterior);
			
			conta = cContas.get("0525010700");
			double cContas70ValorAnterior = conta.obterTotalizacaoExistente(aseguradoras,mesAnoAnterior);
			
			conta = cContas.get("0525010800");
			double cContas71ValorAnterior = conta.obterTotalizacaoExistente(aseguradoras,mesAnoAnterior);
			
			conta = cContas.get("0525010400");
			double cContas72ValorAnterior = conta.obterTotalizacaoExistente(aseguradoras,mesAnoAnterior);
			
			conta = cContas.get("0102050000");
			double cContas73ValorAnterior = conta.obterTotalizacaoExistente(aseguradoras,mesAnoAnterior);
			
			conta = cContas.get("0102060000");
			double cContas74ValorAnterior = conta.obterTotalizacaoExistente(aseguradoras,mesAnoAnterior);
			
			conta = cContas.get("0104000000");
			double cContas75ValorAnterior = conta.obterTotalizacaoExistente(aseguradoras,mesAnoAnterior);
			
			conta = cContas.get("0105000000");
			double cContas76ValorAnterior = conta.obterTotalizacaoExistente(aseguradoras,mesAnoAnterior);
			
			conta = cContas.get("0109010000");
			double cContas77ValorAnterior = conta.obterTotalizacaoExistente(aseguradoras,mesAnoAnterior);
			
			conta = cContas.get("0109020000");
			double cContas78ValorAnterior = conta.obterTotalizacaoExistente(aseguradoras,mesAnoAnterior);
			
			conta = cContas.get("0109070000");
			double cContas79ValorAnterior = conta.obterTotalizacaoExistente(aseguradoras,mesAnoAnterior);
			
			conta = cContas.get("0109080000");
			double cContas80ValorAnterior = conta.obterTotalizacaoExistente(aseguradoras,mesAnoAnterior);
			
			conta = cContas.get("0206000000");
			double cContas81ValorAnterior = conta.obterTotalizacaoExistente(aseguradoras,mesAnoAnterior);
			
			conta = cContas.get("0210000000");
			double cContas82ValorAnterior = conta.obterTotalizacaoExistente(aseguradoras,mesAnoAnterior);
			
			conta = cContas.get("0211000000");
			double cContas83ValorAnterior = conta.obterTotalizacaoExistente(aseguradoras,mesAnoAnterior);
			
			conta = cContas.get("0205000000");
			double cContas84ValorAnterior = conta.obterTotalizacaoExistente(aseguradoras,mesAnoAnterior);
			
			conta = cContas.get("0214030000");
			double cContas86ValorAnterior = conta.obterTotalizacaoExistente(aseguradoras,mesAnoAnterior);
			
			conta = cContas.get("0214040000");
			double cContas87ValorAnterior = conta.obterTotalizacaoExistente(aseguradoras,mesAnoAnterior);
			
			conta = cContas.get("0109040000");
			double cContas88ValorAnterior = conta.obterTotalizacaoExistente(aseguradoras,mesAnoAnterior);
			
			conta = cContas.get("0109050000");
			double cContas89ValorAnterior = conta.obterTotalizacaoExistente(aseguradoras,mesAnoAnterior);
			
			conta = cContas.get("0109060000");
			double cContas90ValorAnterior = conta.obterTotalizacaoExistente(aseguradoras,mesAnoAnterior);
			
			conta = cContas.get("0211010200");
			double cContas91ValorAnterior = conta.obterTotalizacaoExistente(aseguradoras,mesAnoAnterior);
			
			double totalDividendoRatio24 = (cContas2ValorAtual
					+ cContas3ValorAtual + cContas4ValorAtual + 
					cContas51ValorAtual
					+ cContas52ValorAtual + cContas53ValorAtual
					+ cContas54ValorAtual + cContas55ValorAtual
					+ cContas56ValorAtual + cContas57ValorAtual
					+ cContas58ValorAtual + cContas59ValorAtual
					+ cContas1ValorAtual + cContas60ValorAtual
					+ cContas39ValorAtual + cContas8ValorAtual
					+ cContas9ValorAtual + cContas10ValorAtual
					+ cContas11ValorAtual + cContas40ValorAtual
					+ cContas41ValorAtual + cContas42ValorAtual
					- cContas43ValorAtual - cContas13ValorAtual
					- cContas15ValorAtual - cContas16ValorAtual
					- cContas24ValorAtual - cContas44ValorAtual
					+ cContas39ValorAtual - cContas43ValorAtual
					+ cContas61ValorAtual + cContas62ValorAtual
					+ cContas63ValorAtual + cContas64ValorAtual
					+ cContas26ValorAtual + cContas65ValorAtual
					+ cContas66ValorAtual + cContas67ValorAtual
					+ cContas68ValorAtual + cContas69ValorAtual
					+ cContas39ValorAtual - conta48ValorAtual
					+ cContas70ValorAtual + cContas71ValorAtual
					+ cContas72ValorAtual + cContas49ValorAtual
					- cContas47ValorAtual - cContas17ValorAtual
					+ cContas73ValorAtual + cContas74ValorAtual
					+ cContas75ValorAtual + cContas76ValorAtual
					+ cContas77ValorAtual + cContas78ValorAtual
					+ cContas79ValorAtual + cContas80ValorAtual
					+ cContas81ValorAtual + cContas82ValorAtual
					+ cContas83ValorAtual + cContas6ValorAtual
					+ cContas7ValorAtual - cContas18ValorAtual
					- cContas12ValorAtual + cContas22ValorAtual
					+ cContas23ValorAtual + cContas84ValorAtual
					+ cContas81ValorAtual + cContas86ValorAtual
					+ cContas87ValorAtual + cContas5ValorAtual
					+ cContas88ValorAtual + cContas89ValorAtual
					+ cContas90ValorAtual + cContas72ValorAtual
					+ cContas91ValorAtual)

					- (cContas2ValorAnterior + cContas3ValorAnterior +
					cContas4ValorAnterior
					+ cContas51ValorAnterior
					+ cContas52ValorAnterior
					+ cContas53ValorAnterior
					+ cContas54ValorAnterior
					+ cContas55ValorAnterior
					+ cContas56ValorAnterior
					+ cContas57ValorAnterior
					+ cContas58ValorAnterior
					+ cContas59ValorAnterior
					+ cContas1ValorAnterior
					+ cContas60ValorAnterior
					+ cContas39ValorAnterior
					+ cContas8ValorAnterior + cContas9ValorAnterior
					+ cContas10ValorAnterior
					+ cContas11ValorAnterior
					+ cContas40ValorAnterior
					+ cContas41ValorAnterior
					+ cContas42ValorAnterior
					- cContas43ValorAnterior
					- cContas13ValorAnterior
					- cContas15ValorAnterior
					- cContas16ValorAnterior
					- cContas24ValorAnterior
					- cContas44ValorAnterior
					+ cContas39ValorAnterior
					- cContas43ValorAnterior
					+ cContas61ValorAnterior
					+ cContas62ValorAnterior
					+ cContas63ValorAnterior
					+ cContas64ValorAnterior
					+ cContas26ValorAnterior
					+ cContas65ValorAnterior
					+ cContas66ValorAnterior
					+ cContas67ValorAnterior
					+ cContas68ValorAnterior
					+ cContas69ValorAnterior
					+ cContas39ValorAnterior - conta48ValorAnterior
					+ cContas70ValorAnterior
					+ cContas71ValorAnterior
					+ cContas72ValorAnterior
					+ cContas49ValorAnterior
					- cContas47ValorAnterior
					- cContas17ValorAnterior
					+ cContas73ValorAnterior
					+ cContas74ValorAnterior
					+ cContas75ValorAnterior
					+ cContas76ValorAnterior
					+ cContas77ValorAnterior
					+ cContas78ValorAnterior
					+ cContas79ValorAnterior
					+ cContas80ValorAnterior
					+ cContas81ValorAnterior
					+ cContas82ValorAnterior
					+ cContas83ValorAnterior
					+ cContas6ValorAnterior + cContas7ValorAnterior
					- cContas18ValorAnterior
					- cContas12ValorAnterior
					+ cContas22ValorAnterior
					+ cContas23ValorAnterior
					+ cContas84ValorAnterior
					+ cContas81ValorAnterior
					+ cContas86ValorAnterior
					+ cContas87ValorAnterior
					+ cContas5ValorAnterior
					+ cContas88ValorAnterior
					+ cContas89ValorAnterior
					+ cContas90ValorAnterior
					+ cContas72ValorAnterior
					+ cContas91ValorAnterior);

			double totalDivisorRatio24 = cContas2ValorAnterior
					+ cContas3ValorAnterior + cContas4ValorAnterior
					+ cContas51ValorAnterior
					+ cContas52ValorAnterior
					+ cContas53ValorAnterior
					+ cContas54ValorAnterior
					+ cContas55ValorAnterior
					+ cContas56ValorAnterior
					+ cContas57ValorAnterior
					+ cContas58ValorAnterior
					+ cContas59ValorAnterior
					+ cContas1ValorAnterior
					+ cContas60ValorAnterior
					+ cContas39ValorAnterior
					+ cContas8ValorAnterior + cContas9ValorAnterior
					+ cContas10ValorAnterior
					+ cContas11ValorAnterior
					+ cContas40ValorAnterior
					+ cContas41ValorAnterior
					+ cContas42ValorAnterior
					- cContas43ValorAnterior
					- cContas13ValorAnterior
					- cContas15ValorAnterior
					- cContas16ValorAnterior
					- cContas24ValorAnterior
					- cContas44ValorAnterior
					+ cContas39ValorAnterior
					- cContas43ValorAnterior
					+ cContas61ValorAnterior
					+ cContas62ValorAnterior
					+ cContas63ValorAnterior
					+ cContas64ValorAnterior
					+ cContas26ValorAnterior
					+ cContas65ValorAnterior
					+ cContas66ValorAnterior
					+ cContas67ValorAnterior
					+ cContas68ValorAnterior
					+ cContas69ValorAnterior
					+ cContas39ValorAnterior - conta48ValorAnterior
					+ cContas70ValorAnterior
					+ cContas71ValorAnterior
					+ cContas72ValorAnterior
					+ cContas49ValorAnterior
					- cContas47ValorAnterior
					- cContas17ValorAnterior
					+ cContas73ValorAnterior
					+ cContas74ValorAnterior
					+ cContas75ValorAnterior
					+ cContas76ValorAnterior
					+ cContas77ValorAnterior
					+ cContas78ValorAnterior
					+ cContas79ValorAnterior
					+ cContas80ValorAnterior
					+ cContas81ValorAnterior
					+ cContas82ValorAnterior
					+ cContas83ValorAnterior
					+ cContas6ValorAnterior + cContas7ValorAnterior
					- cContas18ValorAnterior
					- cContas12ValorAnterior
					+ cContas22ValorAnterior
					+ cContas23ValorAnterior
					+ cContas84ValorAnterior
					+ cContas81ValorAnterior
					+ cContas86ValorAnterior
					+ cContas87ValorAnterior
					+ cContas5ValorAnterior
					+ cContas88ValorAnterior
					+ cContas89ValorAnterior
					+ cContas90ValorAnterior
					+ cContas72ValorAnterior
					+ cContas91ValorAnterior;

			double totalRatio24 = 0;

			if (totalDivisorRatio24 != 0)
			{
				totalRatio24 = (totalDividendoRatio24 / totalDivisorRatio24) * 100;
				if(totalDividendoRatio24 == 0 && totalDivisorRatio24 < 0)
					totalRatio24 = totalRatio24*-1;
			}
			
			celula = row.createCell(++coluna);
			celula.setCellValue(formataValor.format(totalRatio24));
			celula.setCellStyle(estiloTexto);
			
			
			//Calculo do Indicador RATIO 25
			
			conta = cContas.get("0108010000");
			double cContas92ValorAtual = conta.obterTotalizacaoExistente(aseguradoras,mesAnoCalculo);
			double cContas92ValorAnterior = conta.obterTotalizacaoExistente(aseguradoras,mesAnoAnterior);
			double cContas92ValorAnterior2Anos = conta.obterTotalizacaoExistente(aseguradoras, mesDoisAnosAnterior);
			
			conta = cContas.get("0303020100");
			double cContas93ValorAtual = conta.obterTotalizacaoExistente(aseguradoras,mesAnoCalculo);
			double cContas93ValorAnterior = conta.obterTotalizacaoExistente(aseguradoras,mesAnoAnterior);
			double cContas93ValorAnterior2Anos = conta.obterTotalizacaoExistente(aseguradoras, mesDoisAnosAnterior);
			
			conta = cContas.get("0107010000");
			double cContas94ValorAtual = conta.obterTotalizacaoExistente(aseguradoras,mesAnoCalculo);
			double cContas94ValorAnterior = conta.obterTotalizacaoExistente(aseguradoras,mesAnoAnterior);
			double cContas94ValorAnterior2Anos = conta.obterTotalizacaoExistente(aseguradoras, mesDoisAnosAnterior);
			
			conta = cContas.get("0303020200");
			double cContas95ValorAtual = conta.obterTotalizacaoExistente(aseguradoras,mesAnoCalculo);
			double cContas95ValorAnterior = conta.obterTotalizacaoExistente(aseguradoras,mesAnoAnterior);
			double cContas95ValorAnterior2Anos = conta.obterTotalizacaoExistente(aseguradoras, mesDoisAnosAnterior);
			
			conta = cContas.get("0107060000");
			double cContas96ValorAtual = conta.obterTotalizacaoExistente(aseguradoras,mesAnoCalculo);
			double cContas96ValorAnterior = conta.obterTotalizacaoExistente(aseguradoras,mesAnoAnterior);
			double cContas96ValorAnterior2Anos = conta.obterTotalizacaoExistente(aseguradoras, mesDoisAnosAnterior);
							
			
			double totalDividendoRatio25 = 
					((cContas92ValorAtual + cContas92ValorAnterior 
					+ cContas70ValorAtual
					- cContas93ValorAtual + cContas93ValorAnterior)
					- (cContas96ValorAtual - cContas96ValorAnterior)
					+ (cContas94ValorAtual - cContas94ValorAnterior)
					+ cContas34ValorAtual 
					- cContas36ValorAtual
					+ cContas95ValorAtual - cContas95ValorAnterior)

					- ((cContas92ValorAnterior + cContas92ValorAnterior2Anos
					+ cContas70ValorAnterior
					- cContas93ValorAnterior + cContas93ValorAnterior2Anos)
					- (cContas96ValorAnterior - cContas96ValorAnterior2Anos)
					+ (cContas94ValorAnterior - cContas94ValorAnterior2Anos)
					+ cContas34ValorAnterior
					- cContas36ValorAnterior
					+ cContas95ValorAnterior - cContas95ValorAnterior2Anos);

			double totalDivisorRatio25 =
					((cContas92ValorAnterior + cContas92ValorAnterior2Anos
					+ cContas70ValorAnterior
					- cContas93ValorAnterior + cContas93ValorAnterior2Anos)
					- (cContas96ValorAnterior - cContas96ValorAnterior2Anos)
					+ (cContas94ValorAnterior - cContas94ValorAnterior2Anos)
					+ cContas34ValorAnterior
					- cContas36ValorAnterior
					+ cContas95ValorAnterior - cContas95ValorAnterior2Anos);

			double totalRatio25 = 0;

			if (totalDivisorRatio25 != 0)
			{
				totalRatio25 = (totalDividendoRatio25 / totalDivisorRatio25) * 100;
				if(totalDividendoRatio25 == 0 && totalDivisorRatio25 < 0)
					totalRatio25 = totalRatio25*-1;
			}
			
			celula = row.createCell(++coluna);
			celula.setCellValue(formataValor.format(totalRatio25));
			celula.setCellStyle(estiloTexto);
			
			
			
			//Calculo Utilidad/pérdida técnica bruta
			conta = cContas.get("0401000000");
			double valorIngresso1 = conta.obterTotalizacaoExistente(aseguradoras, mesAnoCalculo);
			conta = cContas.get("0402000000");
			double valorIngresso2 = conta.obterTotalizacaoExistente(aseguradoras, mesAnoCalculo);
			conta = cContas.get("0403000000");
			double valorIngresso3 = conta.obterTotalizacaoExistente(aseguradoras, mesAnoCalculo);
			conta = cContas.get("0404000000");
			double valorIngresso4 = conta.obterTotalizacaoExistente(aseguradoras, mesAnoCalculo);
			conta = cContas.get("0501000000");
			double valorIngresso5 = conta.obterTotalizacaoExistente(aseguradoras, mesAnoCalculo);
			conta = cContas.get("0502000000");
			double valorIngresso6 = conta.obterTotalizacaoExistente(aseguradoras, mesAnoCalculo);
			conta = cContas.get("0503000000");
			double valorIngresso7 = conta.obterTotalizacaoExistente(aseguradoras, mesAnoCalculo);
			
			conta = cContas.get("0506000000");
			double valorSinistro1 = conta.obterTotalizacaoExistente(aseguradoras, mesAnoCalculo);
			conta = cContas.get("0507000000");
			double valorSinistro2 = conta.obterTotalizacaoExistente(aseguradoras, mesAnoCalculo);
			conta = cContas.get("0508000000");
			double valorSinistro3 = conta.obterTotalizacaoExistente(aseguradoras, mesAnoCalculo);
			conta = cContas.get("0509000000");
			double valorSinistro4 = conta.obterTotalizacaoExistente(aseguradoras, mesAnoCalculo);
			conta = cContas.get("0511000000");
			double valorSinistro5 = conta.obterTotalizacaoExistente(aseguradoras, mesAnoCalculo);
			conta = cContas.get("0513000000");
			double valorSinistro6 = conta.obterTotalizacaoExistente(aseguradoras, mesAnoCalculo);
			conta = cContas.get("0515000000");
			double valorSinistro7 = conta.obterTotalizacaoExistente(aseguradoras, mesAnoCalculo);
			conta = cContas.get("0505000000");
			double valorSinistro8 = conta.obterTotalizacaoExistente(aseguradoras, mesAnoCalculo);
			
			conta = cContas.get("0407000000");
			double valorRecuperoSinistro1 = conta.obterTotalizacaoExistente(aseguradoras, mesAnoCalculo);
			conta = cContas.get("0408000000");
			double valorRecuperoSinistro2 = conta.obterTotalizacaoExistente(aseguradoras, mesAnoCalculo);
			conta = cContas.get("0409000000");
			double valorRecuperoSinistro3 = conta.obterTotalizacaoExistente(aseguradoras, mesAnoCalculo);
			conta = cContas.get("0412000000");
			double valorRecuperoSinistro4 = conta.obterTotalizacaoExistente(aseguradoras, mesAnoCalculo);
			conta = cContas.get("0414000000");
			double valorRecuperoSinistro5 = conta.obterTotalizacaoExistente(aseguradoras, mesAnoCalculo);
			conta = cContas.get("0406000000");
			double valorRecuperoSinistro6 = conta.obterTotalizacaoExistente(aseguradoras, mesAnoCalculo);
			
			
			double totalPrimasNetas = (valorIngresso1 + valorIngresso2
					+ valorIngresso3 + valorIngresso4)
					- (valorIngresso5 + valorIngresso6 + valorIngresso7);
			
			double totalSinistro = valorSinistro1 + valorSinistro2
					+ valorSinistro3 + valorSinistro4 + valorSinistro5
					+ valorSinistro6 + valorSinistro7 + valorSinistro8;
			
			double totalRecuperoSinistro = valorRecuperoSinistro1
					+ valorRecuperoSinistro2 + valorRecuperoSinistro3
					+ valorRecuperoSinistro4 + valorRecuperoSinistro5
					+ valorRecuperoSinistro6;
			
			double totalSinistroNeto = totalSinistro - totalRecuperoSinistro;
			
			double totalUtilidade = totalPrimasNetas - totalSinistroNeto;
			
			celula = row.createCell(++coluna);
			celula.setCellValue(formataValor.format(totalUtilidade));
			celula.setCellStyle(estiloTexto);
			
			
			//Calculo UTILIDAD / PÉRDIDA TÉCNICA NETA
			
			conta = cContas.get("0405000000");
			double valorIngressoTecnico1 = conta.obterTotalizacaoExistente(aseguradoras, mesAnoCalculo);
			conta = cContas.get("0410000000");
			double valorIngressoTecnico2 = conta.obterTotalizacaoExistente(aseguradoras, mesAnoCalculo);
			conta = cContas.get("0411000000");
			double valorIngressoTecnico3 = conta.obterTotalizacaoExistente(aseguradoras, mesAnoCalculo);
			conta = cContas.get("0413000000");
			double valorIngressoTecnico4 = conta.obterTotalizacaoExistente(aseguradoras, mesAnoCalculo);
			conta = cContas.get("0415000000");
			double valorIngressoTecnico5 = conta.obterTotalizacaoExistente(aseguradoras, mesAnoCalculo);
			conta = cContas.get("0426000000");
			double valorIngressoTecnico6 = conta.obterTotalizacaoExistente(aseguradoras, mesAnoCalculo);
			
			conta = cContas.get("0504000000");
			double valorEgressoTecnico1 = conta.obterTotalizacaoExistente(aseguradoras,mesAnoCalculo);
			conta = cContas.get("0510000000");
			double valorEgressoTecnico2 = conta.obterTotalizacaoExistente(aseguradoras, mesAnoCalculo);
			conta = cContas.get("0512000000");
			double valorEgressoTecnico3 = conta.obterTotalizacaoExistente(aseguradoras, mesAnoCalculo);
			conta = cContas.get("0514000000");
			double valorEgressoTecnico4 = conta.obterTotalizacaoExistente(aseguradoras, mesAnoCalculo);
			conta = cContas.get("0516000000");
			double valorEgressoTecnico5 = conta.obterTotalizacaoExistente(aseguradoras, mesAnoCalculo);
			conta = cContas.get("0525000000");
			double valorEgressoTecnico6 = conta.obterTotalizacaoExistente(aseguradoras, mesAnoCalculo);
			conta2 = contas.get("0525010401");
			double valorEgressoTecnico7 = conta2.obterTotalizacaoExistente(aseguradoras, mesAnoCalculo);
			conta = cContas.get("0527000000");
			double valorEgressoTecnico8 = conta.obterTotalizacaoExistente(aseguradoras, mesAnoCalculo);
			
			double totalEgressoTecnico = valorEgressoTecnico1
					+ valorEgressoTecnico2 + valorEgressoTecnico3
					+ valorEgressoTecnico4 + valorEgressoTecnico5
					+ valorEgressoTecnico6 - valorEgressoTecnico7
					+ valorEgressoTecnico8;
			
			double totalIngressoTecnico = valorIngressoTecnico1
					+ valorIngressoTecnico2 + valorIngressoTecnico3
					+ valorIngressoTecnico4 + valorIngressoTecnico5
					+ valorIngressoTecnico6;
			
			double totalUtilidadeNeta = (totalUtilidade + totalIngressoTecnico)	- totalEgressoTecnico;
			
			celula = row.createCell(++coluna);
			celula.setCellValue(formataValor.format(totalUtilidadeNeta));
			celula.setCellStyle(estiloTexto);
			
			
			//Calculo UTILIDAD / PÉRDIDA NETA DEL EJERCICIO
			
			conta = cContas.get("0425000000");
			double valorInversao1 = conta.obterTotalizacaoExistente(aseguradoras, mesAnoCalculo);
			conta = cContas.get("0526000000");
			double valorInversao2 = conta.obterTotalizacaoExistente(aseguradoras, mesAnoCalculo);
			conta = cContas.get("0435000000");
			double valorExtraordinario1 = conta.obterTotalizacaoExistente(aseguradoras, mesAnoCalculo);
			conta = cContas.get("0535000000");
			double valorExtraordinario2 = conta.obterTotalizacaoExistente(aseguradoras, mesAnoCalculo);
			
			double totalInversao = valorInversao1 - valorInversao2;
			double totalExtraordinario = valorExtraordinario1 - valorExtraordinario2;
			
			double totalImposta = totalUtilidadeNeta + totalInversao + totalExtraordinario;
			
			celula = row.createCell(++coluna);
			celula.setCellValue(formataValor.format(totalImposta - valorEgressoTecnico7));
			celula.setCellStyle(estiloTexto);
			
			c.add(Calendar.MONTH, 1);
			
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
		
		ClassificacaoContas cContas = (ClassificacaoContas) this.home.obterEntidadePorApelido("0212000000");
		this.cContas.put("0212000000", cContas);
		
		cContas = (ClassificacaoContas) this.home.obterEntidadePorApelido("0401000000");
		this.cContas.put("0401000000", cContas);
		
		cContas = (ClassificacaoContas) this.home.obterEntidadePorApelido("0402000000");
		this.cContas.put("0402000000", cContas);
		
		cContas = (ClassificacaoContas) this.home.obterEntidadePorApelido("0403000000");
		this.cContas.put("0403000000", cContas);
		
		cContas = (ClassificacaoContas) this.home.obterEntidadePorApelido("0109030000");
		this.cContas.put("0109030000", cContas);
		
		cContas = (ClassificacaoContas) this.home.obterEntidadePorApelido("0501000000");
		this.cContas.put("0501000000", cContas);
		
		cContas = (ClassificacaoContas) this.home.obterEntidadePorApelido("0502000000");
		this.cContas.put("0502000000", cContas);
		
		cContas = (ClassificacaoContas) this.home.obterEntidadePorApelido("0506000000");
		this.cContas.put("0506000000", cContas);
				
		cContas = (ClassificacaoContas) this.home.obterEntidadePorApelido("0507000000");
		this.cContas.put("0507000000", cContas);
		
		cContas = (ClassificacaoContas) this.home.obterEntidadePorApelido("0508000000");
		this.cContas.put("0508000000", cContas);
		
		cContas = (ClassificacaoContas) this.home.obterEntidadePorApelido("0509000000");
		this.cContas.put("0509000000", cContas);
		
		cContas = (ClassificacaoContas) this.home.obterEntidadePorApelido("0512000000");
		this.cContas.put("0512000000", cContas);
		
		cContas = (ClassificacaoContas) this.home.obterEntidadePorApelido("0407000000");
		this.cContas.put("0407000000", cContas);
		
		cContas = (ClassificacaoContas) this.home.obterEntidadePorApelido("0412000000");
		this.cContas.put("0412000000", cContas);
		
		cContas = (ClassificacaoContas) this.home.obterEntidadePorApelido("0408000000");
		this.cContas.put("0408000000", cContas);
		
		cContas = (ClassificacaoContas) this.home.obterEntidadePorApelido("0409000000");
		this.cContas.put("0409000000", cContas);
		
		cContas = (ClassificacaoContas) this.home.obterEntidadePorApelido("0504000000");
		this.cContas.put("0504000000", cContas);
		
		cContas = (ClassificacaoContas) this.home.obterEntidadePorApelido("0510000000");
		this.cContas.put("0510000000", cContas);
		
		cContas = (ClassificacaoContas) this.home.obterEntidadePorApelido("0514000000");
		this.cContas.put("0514000000", cContas);
		
		cContas = (ClassificacaoContas) this.home.obterEntidadePorApelido("0516000000");
		this.cContas.put("0516000000", cContas);
		
		cContas = (ClassificacaoContas) this.home.obterEntidadePorApelido("0405000000");
		this.cContas.put("0405000000", cContas);
		
		cContas = (ClassificacaoContas) this.home.obterEntidadePorApelido("0410000000");
		this.cContas.put("0410000000", cContas);
		
		cContas = (ClassificacaoContas) this.home.obterEntidadePorApelido("0411000000");
		this.cContas.put("0411000000", cContas);
		
		cContas = (ClassificacaoContas) this.home.obterEntidadePorApelido("0412000000");
		this.cContas.put("0412000000", cContas);
		
		cContas = (ClassificacaoContas) this.home.obterEntidadePorApelido("0525000000");
		this.cContas.put("0525000000", cContas);
		
		cContas = (ClassificacaoContas) this.home.obterEntidadePorApelido("0213000000");
		this.cContas.put("0213000000", cContas);
		
		cContas = (ClassificacaoContas) this.home.obterEntidadePorApelido("0300000000");
		this.cContas.put("0300000000", cContas);
		
		cContas = (ClassificacaoContas) this.home.obterEntidadePorApelido("0100000000");
		this.cContas.put("0100000000", cContas);
		
		cContas = (ClassificacaoContas) this.home.obterEntidadePorApelido("0108000000");
		this.cContas.put("0108000000", cContas);
		
		cContas = (ClassificacaoContas) this.home.obterEntidadePorApelido("0701010000");
		this.cContas.put("0701010000", cContas);
		
		cContas = (ClassificacaoContas) this.home.obterEntidadePorApelido("0601010000");
		this.cContas.put("0601010000", cContas);
		
		cContas = (ClassificacaoContas) this.home.obterEntidadePorApelido("0213020000");
		this.cContas.put("0213020000", cContas);
		
		cContas = (ClassificacaoContas) this.home.obterEntidadePorApelido("0305000000");
		this.cContas.put("0305000000", cContas);
		
		cContas = (ClassificacaoContas) this.home.obterEntidadePorApelido("0425000000");
		this.cContas.put("0425000000", cContas);
		
		cContas = (ClassificacaoContas) this.home.obterEntidadePorApelido("0107000000");
		this.cContas.put("0107000000", cContas);
		
		cContas = (ClassificacaoContas) this.home.obterEntidadePorApelido("0526000000");
		this.cContas.put("0526000000", cContas);
		
		cContas = (ClassificacaoContas) this.home.obterEntidadePorApelido("0404000000");
		this.cContas.put("0404000000", cContas);
		
		cContas = (ClassificacaoContas) this.home.obterEntidadePorApelido("0503000000");
		this.cContas.put("0503000000", cContas);
		
		cContas = (ClassificacaoContas) this.home.obterEntidadePorApelido("0505000000");
		this.cContas.put("0505000000", cContas);
		
		cContas = (ClassificacaoContas) this.home.obterEntidadePorApelido("0511000000");
		this.cContas.put("0511000000", cContas);
		
		cContas = (ClassificacaoContas) this.home.obterEntidadePorApelido("0513000000");
		this.cContas.put("0513000000", cContas);
		
		cContas = (ClassificacaoContas) this.home.obterEntidadePorApelido("0515000000");
		this.cContas.put("0515000000", cContas);
		
		cContas = (ClassificacaoContas) this.home.obterEntidadePorApelido("0406000000");
		this.cContas.put("0406000000", cContas);
		
		cContas = (ClassificacaoContas) this.home.obterEntidadePorApelido("0414000000");
		this.cContas.put("0414000000", cContas);
		
		cContas = (ClassificacaoContas) this.home.obterEntidadePorApelido("0413000000");
		this.cContas.put("0413000000", cContas);
		
		cContas = (ClassificacaoContas) this.home.obterEntidadePorApelido("0415000000");
		this.cContas.put("0415000000", cContas);
		
		cContas = (ClassificacaoContas) this.home.obterEntidadePorApelido("0426000000");
		this.cContas.put("0426000000", cContas);
		
		Conta conta = (Conta) this.home.obterEntidadePorApelido("0525010401");
		this.contas.put("0525010401", conta);
		
		cContas = (ClassificacaoContas) this.home.obterEntidadePorApelido("0527000000");
		this.cContas.put("0527000000", cContas);
		
		cContas = (ClassificacaoContas) this.home.obterEntidadePorApelido("0405020000");
		this.cContas.put("0405020000", cContas);
		
		cContas = (ClassificacaoContas) this.home.obterEntidadePorApelido("0102010000");
		this.cContas.put("0102010000", cContas);
		
		cContas = (ClassificacaoContas) this.home.obterEntidadePorApelido("0103010000");
		this.cContas.put("0103010000", cContas);
		
		cContas = (ClassificacaoContas) this.home.obterEntidadePorApelido("0102020000");
		this.cContas.put("0102020000", cContas);
		
		cContas = (ClassificacaoContas) this.home.obterEntidadePorApelido("0103020000");
		this.cContas.put("0103020000", cContas);
		
		cContas = (ClassificacaoContas) this.home.obterEntidadePorApelido("0203000000");
		this.cContas.put("0203000000", cContas);
		
		cContas = (ClassificacaoContas) this.home.obterEntidadePorApelido("0106010000");
		this.cContas.put("0106010000", cContas);
		
		cContas = (ClassificacaoContas) this.home.obterEntidadePorApelido("0202030000");
		this.cContas.put("0202030000", cContas);
		
		cContas = (ClassificacaoContas) this.home.obterEntidadePorApelido("0202040000");
		this.cContas.put("0202040000", cContas);
		
		cContas = (ClassificacaoContas) this.home.obterEntidadePorApelido("0214010000");
		this.cContas.put("0214010000", cContas);
		
		cContas = (ClassificacaoContas) this.home.obterEntidadePorApelido("0202010000");
		this.cContas.put("0202010000", cContas);
		
		cContas = (ClassificacaoContas) this.home.obterEntidadePorApelido("0202020000");
		this.cContas.put("0202020000", cContas);
		
		cContas = (ClassificacaoContas) this.home.obterEntidadePorApelido("0202050000");
		this.cContas.put("0202050000", cContas);
		
		cContas = (ClassificacaoContas) this.home.obterEntidadePorApelido("0202060000");
		this.cContas.put("0202060000", cContas);
		
		cContas = (ClassificacaoContas) this.home.obterEntidadePorApelido("0102032000");
		this.cContas.put("0102032000", cContas);
		
		cContas = (ClassificacaoContas) this.home.obterEntidadePorApelido("0103032000");
		this.cContas.put("0103032000", cContas);
		
		cContas = (ClassificacaoContas) this.home.obterEntidadePorApelido("0102032100");
		this.cContas.put("0102032100", cContas);
		
		cContas = (ClassificacaoContas) this.home.obterEntidadePorApelido("0103032100");
		this.cContas.put("0103032100", cContas);
		
		cContas = (ClassificacaoContas) this.home.obterEntidadePorApelido("0106020000");
		this.cContas.put("0106020000", cContas);
		
		cContas = (ClassificacaoContas) this.home.obterEntidadePorApelido("0525010700");
		this.cContas.put("0525010700", cContas);
		
		cContas = (ClassificacaoContas) this.home.obterEntidadePorApelido("0525010800");
		this.cContas.put("0525010800", cContas);
		
		cContas = (ClassificacaoContas) this.home.obterEntidadePorApelido("0525010400");
		this.cContas.put("0525010400", cContas);
		
		cContas = (ClassificacaoContas) this.home.obterEntidadePorApelido("0102050000");
		this.cContas.put("0102050000", cContas);
		
		cContas = (ClassificacaoContas) this.home.obterEntidadePorApelido("0102060000");
		this.cContas.put("0102060000", cContas);
		
		cContas = (ClassificacaoContas) this.home.obterEntidadePorApelido("0104000000");
		this.cContas.put("0104000000", cContas);
		
		cContas = (ClassificacaoContas) this.home.obterEntidadePorApelido("0105000000");
		this.cContas.put("0105000000", cContas);
		
		cContas = (ClassificacaoContas) this.home.obterEntidadePorApelido("0109010000");
		this.cContas.put("0109010000", cContas);
		
		cContas = (ClassificacaoContas) this.home.obterEntidadePorApelido("0109020000");
		this.cContas.put("0109020000", cContas);
		
		cContas = (ClassificacaoContas) this.home.obterEntidadePorApelido("0109070000");
		this.cContas.put("0109070000", cContas);
		
		cContas = (ClassificacaoContas) this.home.obterEntidadePorApelido("0109080000");
		this.cContas.put("0109080000", cContas);
		
		cContas = (ClassificacaoContas) this.home.obterEntidadePorApelido("0206000000");
		this.cContas.put("0206000000", cContas);
		
		cContas = (ClassificacaoContas) this.home.obterEntidadePorApelido("0210000000");
		this.cContas.put("0210000000", cContas);
		
		cContas = (ClassificacaoContas) this.home.obterEntidadePorApelido("0211000000");
		this.cContas.put("0211000000", cContas);
		
		cContas = (ClassificacaoContas) this.home.obterEntidadePorApelido("0205000000");
		this.cContas.put("0205000000", cContas);
		
		cContas = (ClassificacaoContas) this.home.obterEntidadePorApelido("0214030000");
		this.cContas.put("0214030000", cContas);
		
		cContas = (ClassificacaoContas) this.home.obterEntidadePorApelido("0214040000");
		this.cContas.put("0214040000", cContas);
		
		cContas = (ClassificacaoContas) this.home.obterEntidadePorApelido("0109040000");
		this.cContas.put("0109040000", cContas);
		
		cContas = (ClassificacaoContas) this.home.obterEntidadePorApelido("0109050000");
		this.cContas.put("0109050000", cContas);
		
		cContas = (ClassificacaoContas) this.home.obterEntidadePorApelido("0109060000");
		this.cContas.put("0109060000", cContas);
		
		cContas = (ClassificacaoContas) this.home.obterEntidadePorApelido("0211010200");
		this.cContas.put("0211010200", cContas);
		
		cContas = (ClassificacaoContas) this.home.obterEntidadePorApelido("0108010000");
		this.cContas.put("0108010000", cContas);
		
		cContas = (ClassificacaoContas) this.home.obterEntidadePorApelido("0303020100");
		this.cContas.put("0303020100", cContas);
		
		cContas = (ClassificacaoContas) this.home.obterEntidadePorApelido("0107010000");
		this.cContas.put("0107010000", cContas);
		
		cContas = (ClassificacaoContas) this.home.obterEntidadePorApelido("0303020200");
		this.cContas.put("0303020200", cContas);
		
		cContas = (ClassificacaoContas) this.home.obterEntidadePorApelido("0107060000");
		this.cContas.put("0107060000", cContas);
		
		cContas = (ClassificacaoContas) this.home.obterEntidadePorApelido("0435000000");
		this.cContas.put("0435000000", cContas);
		
		cContas = (ClassificacaoContas) this.home.obterEntidadePorApelido("0535000000");
		this.cContas.put("0535000000", cContas);
		
		cContas = (ClassificacaoContas) this.home.obterEntidadePorApelido("0540000000");
		this.cContas.put("0540000000", cContas);
		
		cContas = (ClassificacaoContas) this.home.obterEntidadePorApelido("0400000000");
		this.cContas.put("0400000000", cContas);
		
		cContas = (ClassificacaoContas) this.home.obterEntidadePorApelido("0500000000");
		this.cContas.put("0500000000", cContas);
	}
}
