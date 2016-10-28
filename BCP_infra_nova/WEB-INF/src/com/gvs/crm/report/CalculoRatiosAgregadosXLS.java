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

public class CalculoRatiosAgregadosXLS extends Excel 
{
	private Map<String,ClassificacaoContas> cContas;
	private Map<String,Conta> contas;
	private  EntidadeHome home;
	
	public CalculoRatiosAgregadosXLS(Date dataInicio, Date dataFim, Collection<Entidade> aseguradoras, EntidadeHome home) throws Exception
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
        
        HSSFCellStyle estiloTitulo_E = wb.createCellStyle();
        estiloTitulo_E.setFont(fonteTitulo);
        
        HSSFFont fonteTexto = wb.createFont();
        fonteTexto.setFontHeightInPoints((short)8);
        fonteTexto.setFontName("Arial");
        
        HSSFCellStyle estiloTexto = wb.createCellStyle();
        estiloTexto.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        estiloTexto.setFont(fonteTexto);
		
		Collection<String> ratios = new ArrayList<String>();
		//ratios.add("Cód Seguradora");
		//ratios.add("Nombre Seguradora");
		ratios.add("Año");
		ratios.add("Mes");
		ratios.add("Prima neta / capital");
		ratios.add("Reservas / capital");
		ratios.add("Excedentes de capital/capital mínimo");
		ratios.add("(Activos fijos + valores no líquidos + cuentas por cobrar )/ activos");
		ratios.add("Valores negociables / activos totales");
		ratios.add("Promedio Reservas para dos años ");
		ratios.add("Activos fijos, valores no líquidos y las cuentas por cobrar / activos");
		ratios.add("Beneficio neto de las primas netas/Numero de Primas Emitidas");
		ratios.add("Cuentas a cobrar / activos");
		ratios.add("Demandas en disputa /Reclamación total pagada");
		ratios.add("Tasas de Cesion de Reaseguros (Gastos de Reaseguros/Primas Brutas Emitidas)");
		ratios.add("Ratio Ingreso Inversiones (Ingreso de Inversion/Primas Directas)");
		ratios.add("Ratio de Ganancias (Ingresos - Egresos)/Primas Directas");
		ratios.add("Primas no consumidas (Provision riesgo en curso/primas emitidas)");
		ratios.add("Siniestros pagados/Provision de siniestros");
		ratios.add("Capital/Primas brutas");
		ratios.add("Primas Brutas/Capital");
		ratios.add("Capital/Provision Tecnica de Siniestros");
		ratios.add("Ingreso Neto/Capital");
		ratios.add("Comisiones primas emitidas");
		ratios.add("Inversiones bienes raices/ activos");
		ratios.add("Inversiones renta fija / activos");
		ratios.add("Inversiones renta fija/capital");
		ratios.add("Inversiones bienes raices/capital");
		ratios.add("Ingreso Neto Inversiones");
		ratios.add("Comisiones directas");
		ratios.add("Comisiones por reaseguros incurridos");
		ratios.add("Gastos generales");
		ratios.add("Prima Bruta/Prima Neta");
		ratios.add("Prima neta/Prima bruta");
		
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
			celula.setCellValue("Calculo de Ratios Agregados 1 de todas las Aseguradoras Fecha " + formataData.format(dataInicio) + " hasta " + formataData.format(dataFim));
			celula.setCellStyle(estiloTitulo_E);
	    }
	    else
	    {
	    	Entidade aseg;
	    	String nomes = "Calculo de Ratios Agregados 1";
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
			
			coluna = 0;
			
			row = planilha.createRow(linha);
			celula = row.createCell(coluna);
			celula.setCellValue(ano);
			celula.setCellStyle(estiloTexto);
			
			celula = row.createCell(++coluna);
			celula.setCellValue(mes);
			celula.setCellStyle(estiloTexto);
			
			//Cálculo Prima neta / capital
			conta = cContas.get("0401000000");
			double cContas1ValorAtual = conta.obterTotalizacaoExistente(aseguradoras, mesAnoCalculo);
			conta = cContas.get("0301000000");
			double cContas2ValorAtual = conta.obterTotalizacaoExistente(aseguradoras, mesAnoCalculo);
			
			double total = 0;

			if (cContas2ValorAtual !=0)
			{
				total = cContas1ValorAtual / cContas2ValorAtual;
				if(cContas1ValorAtual == 0 && cContas2ValorAtual < 0)
					total = total*-1;
			}
			
			celula = row.createCell(++coluna);
			celula.setCellValue(formataValor.format(total));
			celula.setCellStyle(estiloTexto);
			
			//Cálculo Excedentes de capital/capital mínimo
			conta = cContas.get("0303000000");
			double cContas3ValorAtual = conta.obterTotalizacaoExistente(aseguradoras, mesAnoCalculo);
			
			total = 0;

			if (cContas2ValorAtual !=0)
			{
				total = cContas3ValorAtual / cContas2ValorAtual;
				if(cContas3ValorAtual == 0 && cContas2ValorAtual < 0)
					total = total*-1;
			}
			
			celula = row.createCell(++coluna);
			celula.setCellValue(formataValor.format(total));
			celula.setCellStyle(estiloTexto);
			
			
			//Cálculo Excedentes de capital/capital mínimo
			conta = cContas.get("0301020000");
			double cContas4ValorAtual = conta.obterTotalizacaoExistente(aseguradoras, mesAnoCalculo);
			conta = cContas.get("0301010000");
			double cContas5ValorAtual = conta.obterTotalizacaoExistente(aseguradoras, mesAnoCalculo);
			
			total = 0;

			if (cContas5ValorAtual !=0)
			{
				total = cContas4ValorAtual / cContas5ValorAtual;
				if(cContas4ValorAtual == 0 && cContas5ValorAtual < 0)
					total = total*-1;
			}
			
			celula = row.createCell(++coluna);
			celula.setCellValue(formataValor.format(total));
			celula.setCellStyle(estiloTexto);
			
			
			//Cálculo (Activos fijos + valores no líquidos + cuentas por cobrar )/ activos
			conta = cContas.get("0107060000");
			double cContas6ValorAtual = conta.obterTotalizacaoExistente(aseguradoras, mesAnoCalculo);
			conta = cContas.get("0109000000");
			double cContas7ValorAtual = conta.obterTotalizacaoExistente(aseguradoras, mesAnoCalculo);
			conta = cContas.get("0102000000");
			double cContas8ValorAtual = conta.obterTotalizacaoExistente(aseguradoras, mesAnoCalculo);
			conta = cContas.get("0103000000");
			double cContas9ValorAtual = conta.obterTotalizacaoExistente(aseguradoras, mesAnoCalculo);
			conta = cContas.get("0104000000");
			double cContas10ValorAtual = conta.obterTotalizacaoExistente(aseguradoras, mesAnoCalculo);
			conta = cContas.get("0100000000");
			double cContas11ValorAtual = conta.obterTotalizacaoExistente(aseguradoras, mesAnoCalculo);
			
			total = 0;
			
			double numerador = cContas6ValorAtual + cContas7ValorAtual + cContas8ValorAtual + cContas9ValorAtual + cContas10ValorAtual;
			double denominador = cContas11ValorAtual;

			if (denominador !=0)
			{
				total = numerador / denominador;
				if(numerador == 0 && denominador < 0)
					total = total*-1;
			}
			
			celula = row.createCell(++coluna);
			celula.setCellValue(formataValor.format(total));
			celula.setCellStyle(estiloTexto);
			
			
			//Cálculo Valores negociables / activos totales
			conta = cContas.get("0107010000");
			double cContas12ValorAtual = conta.obterTotalizacaoExistente(aseguradoras, mesAnoCalculo);
			conta = cContas.get("0107020000");
			double cContas13ValorAtual = conta.obterTotalizacaoExistente(aseguradoras, mesAnoCalculo);
			conta = cContas.get("0107030000");
			double cContas14ValorAtual = conta.obterTotalizacaoExistente(aseguradoras, mesAnoCalculo);
			conta = cContas.get("0107040000");
			double cContas15ValorAtual = conta.obterTotalizacaoExistente(aseguradoras, mesAnoCalculo);
			
			total = 0;
			
			numerador = cContas12ValorAtual + cContas13ValorAtual + cContas14ValorAtual + cContas15ValorAtual;
			denominador = cContas11ValorAtual;

			if (denominador !=0)
			{
				total = numerador / denominador;
				if(numerador == 0 && denominador < 0)
					total = total*-1;
			}
			
			celula = row.createCell(++coluna);
			celula.setCellValue(formataValor.format(total));
			celula.setCellStyle(estiloTexto);
			
			
			//Cálculo Prima neta/Prima bruta
			conta = cContas.get("0402000000");
			double cContas16ValorAtual = conta.obterTotalizacaoExistente(aseguradoras, mesAnoCalculo);
			conta = cContas.get("0403000000");
			double cContas17ValorAtual = conta.obterTotalizacaoExistente(aseguradoras, mesAnoCalculo);
			conta = cContas.get("0501000000");
			double cContas18ValorAtual = conta.obterTotalizacaoExistente(aseguradoras, mesAnoCalculo);
			conta = cContas.get("0502000000");
			double cContas19ValorAtual = conta.obterTotalizacaoExistente(aseguradoras, mesAnoCalculo);
			
			/* Não usar o calculo, somente as contas. O calculo está errado
			 * 
			 * total = 0;
			
			numerador = cContas1ValorAtual;
			denominador = cContas1ValorAtual - cContas16ValorAtual - cContas17ValorAtual - cContas18ValorAtual - cContas19ValorAtual;

			if (denominador !=0)
			{
				total = numerador / denominador;
				if(numerador == 0 && denominador < 0)
					total = total*-1;
			}
			
			celula = row.createCell(++coluna);
			celula.setCellValue(formataValor.format(total));
			celula.setCellStyle(estiloTexto);*/
			
			
			//Cálculo Promedio Reservas para dos años 
			conta = cContas.get("0213000000");
			double cContas20ValorAtual = conta.obterTotalizacaoExistente(aseguradoras, mesAnoCalculo);
			conta = cContas.get("0506000000");
			double cContas21ValorAtual = conta.obterTotalizacaoExistente(aseguradoras, mesAnoCalculo);
			conta = cContas.get("0408000000");
			double cContas22ValorAtual = conta.obterTotalizacaoExistente(aseguradoras, mesAnoCalculo);
			conta = cContas.get("0409000000");
			double cContas23ValorAtual = conta.obterTotalizacaoExistente(aseguradoras, mesAnoCalculo);
			
			total = 0;
			
			numerador = cContas20ValorAtual;
			denominador = cContas21ValorAtual - cContas22ValorAtual - cContas23ValorAtual;

			if (denominador !=0)
			{
				total = numerador / denominador;
				if(numerador == 0 && denominador < 0)
					total = total*-1;
			}
			
			celula = row.createCell(++coluna);
			celula.setCellValue(formataValor.format(total));
			celula.setCellStyle(estiloTexto);
			
			
			//Cálculo Activos fijos, valores no líquidos y las cuentas por cobrar / activos
			
			total = 0;
			
			numerador = cContas11ValorAtual + cContas6ValorAtual + cContas7ValorAtual + cContas8ValorAtual + cContas9ValorAtual + cContas10ValorAtual;
			denominador = cContas11ValorAtual;

			if (denominador !=0)
			{
				total = numerador / denominador;
				if(numerador == 0 && denominador < 0)
					total = total*-1;
			}
			
			celula = row.createCell(++coluna);
			celula.setCellValue(formataValor.format(total));
			celula.setCellStyle(estiloTexto);
			
			//Cálculo Beneficio neto de las primas netas/Numero de Primas Emitidas
			total = cContas1ValorAtual + cContas16ValorAtual + cContas17ValorAtual - (cContas18ValorAtual + cContas19ValorAtual);
			
			celula = row.createCell(++coluna);
			celula.setCellValue(formataValor.format(total));
			celula.setCellStyle(estiloTexto);
			
			
			//Cálculo Cuentas a cobrar / activos
			total = 0;
			
			numerador = cContas8ValorAtual + cContas9ValorAtual + cContas10ValorAtual;
			denominador = cContas11ValorAtual;

			if (denominador !=0)
			{
				total = numerador / denominador;
				if(numerador == 0 && denominador < 0)
					total = total*-1;
			}
			
			celula = row.createCell(++coluna);
			celula.setCellValue(formataValor.format(total));
			celula.setCellStyle(estiloTexto);
			
			
			//Cálculo Demandas en disputa /Reclamación total pagada
			conta = cContas.get("0213030000");
			double cContas25ValorAtual = conta.obterTotalizacaoExistente(aseguradoras, mesAnoCalculo);
			
			total = 0;
			
			numerador = cContas25ValorAtual;
			denominador = cContas21ValorAtual;

			if (denominador !=0)
			{
				total = numerador / denominador;
				if(numerador == 0 && denominador < 0)
					total = total*-1;
			}
			
			celula = row.createCell(++coluna);
			celula.setCellValue(formataValor.format(total));
			celula.setCellStyle(estiloTexto);
			
			
			//Cálculo Tasas de Cesion de Reaseguros (Gastos de Reaseguros/Primas Brutas Emitidas)
			conta = cContas.get("0512000000");
			double cContas26ValorAtual = conta.obterTotalizacaoExistente(aseguradoras, mesAnoCalculo);
			
			total = 0;
			
			numerador = cContas18ValorAtual + cContas19ValorAtual + cContas26ValorAtual;
			denominador = cContas1ValorAtual;

			if (denominador !=0)
			{
				total = numerador / denominador;
				if(numerador == 0 && denominador < 0)
					total = total*-1;
			}
			
			celula = row.createCell(++coluna);
			celula.setCellValue(formataValor.format(total));
			celula.setCellStyle(estiloTexto);
			
			
			//Cálculo Ratio Ingreso Inversiones (Ingreso de Inversion/Primas Directas)
			conta = cContas.get("0425000000");
			double cContas27ValorAtual = conta.obterTotalizacaoExistente(aseguradoras, mesAnoCalculo);
			
			total = 0;
			
			numerador = cContas27ValorAtual;
			denominador = cContas1ValorAtual;

			if (denominador !=0)
			{
				total = numerador / denominador;
				if(numerador == 0 && denominador < 0)
					total = total*-1;
			}
			
			celula = row.createCell(++coluna);
			celula.setCellValue(formataValor.format(total));
			celula.setCellStyle(estiloTexto);
			
			
			//Cálculo Ratio de Ganancias (Ingresos - Egresos)/Primas Directas
			conta = cContas.get("0400000000");
			double cContas28ValorAtual = conta.obterTotalizacaoExistente(aseguradoras, mesAnoCalculo);
			
			total = 0;
			
			numerador = cContas28ValorAtual;
			denominador = cContas1ValorAtual;

			if (denominador !=0)
			{
				total = numerador / denominador;
				if(numerador == 0 && denominador < 0)
					total = total*-1;
			}
			
			celula = row.createCell(++coluna);
			celula.setCellValue(formataValor.format(total));
			celula.setCellStyle(estiloTexto);
			
			
			//Cálculo Primas no consumidas (Provision riesgo en curso/primas emitidas)
			conta = cContas.get("0212000000");
			double cContas29ValorAtual = conta.obterTotalizacaoExistente(aseguradoras, mesAnoCalculo);
			
			total = 0;
			
			numerador = cContas29ValorAtual;
			denominador = cContas28ValorAtual;

			if (denominador !=0)
			{
				total = numerador / denominador;
				if(numerador == 0 && denominador < 0)
					total = total*-1;
			}
			
			celula = row.createCell(++coluna);
			celula.setCellValue(formataValor.format(total));
			celula.setCellStyle(estiloTexto);
			
			
			//Cálculo Siniestros pagados/Provision de siniestros
			total = 0;
			
			numerador = cContas21ValorAtual;
			denominador = cContas20ValorAtual;

			if (denominador !=0)
			{
				total = numerador / denominador;
				if(numerador == 0 && denominador < 0)
					total = total*-1;
			}
			
			celula = row.createCell(++coluna);
			celula.setCellValue(formataValor.format(total));
			celula.setCellStyle(estiloTexto);
			
			
			//Cálculo Capital/Primas brutas
			total = 0;
			
			numerador = cContas2ValorAtual;
			denominador = cContas1ValorAtual;

			if (denominador !=0)
			{
				total = numerador / denominador;
				if(numerador == 0 && denominador < 0)
					total = total*-1;
			}
			
			celula = row.createCell(++coluna);
			celula.setCellValue(formataValor.format(total));
			celula.setCellStyle(estiloTexto);
			
			
			//Cálculo Primas Brutas/Capital
			total = 0;
			
			numerador = cContas1ValorAtual;
			denominador = cContas2ValorAtual;

			if (denominador !=0)
			{
				total = numerador / denominador;
				if(numerador == 0 && denominador < 0)
					total = total*-1;
			}
			
			celula = row.createCell(++coluna);
			celula.setCellValue(formataValor.format(total));
			celula.setCellStyle(estiloTexto);
			
			
			//Cálculo Capital/Provision Tecnica de Siniestros
			total = 0;
			
			numerador = cContas2ValorAtual;
			denominador = cContas20ValorAtual;

			if (denominador !=0)
			{
				total = numerador / denominador;
				if(numerador == 0 && denominador < 0)
					total = total*-1;
			}
			
			celula = row.createCell(++coluna);
			celula.setCellValue(formataValor.format(total));
			celula.setCellStyle(estiloTexto);
			
			
			//Cálculo Ingreso Neto/Capital
			total = 0;
			
			numerador = cContas28ValorAtual;
			denominador = cContas2ValorAtual;

			if (denominador !=0)
			{
				total = numerador / denominador;
				if(numerador == 0 && denominador < 0)
					total = total*-1;
			}
			
			celula = row.createCell(++coluna);
			celula.setCellValue(formataValor.format(total));
			celula.setCellStyle(estiloTexto);
			
			
			//Cálculo Comisiones primas emitidas
			conta = cContas.get("0109020000");
			double cContas30ValorAtual = conta.obterTotalizacaoExistente(aseguradoras, mesAnoCalculo);
			conta = cContas.get("0206020000");
			double cContas31ValorAtual = conta.obterTotalizacaoExistente(aseguradoras, mesAnoCalculo);
			conta = cContas.get("0410010000");
			double cContas32ValorAtual = conta.obterTotalizacaoExistente(aseguradoras, mesAnoCalculo);
			conta = cContas.get("0411010000");
			double cContas33ValorAtual = conta.obterTotalizacaoExistente(aseguradoras, mesAnoCalculo);
			conta = cContas.get("0504000000");
			double cContas34ValorAtual = conta.obterTotalizacaoExistente(aseguradoras, mesAnoCalculo);
			
			total = (cContas30ValorAtual - cContas31ValorAtual) + (cContas32ValorAtual + cContas33ValorAtual - cContas34ValorAtual);
			
			celula = row.createCell(++coluna);
			celula.setCellValue(formataValor.format(total));
			celula.setCellStyle(estiloTexto);
			
			
			//Cálculo Inversiones bienes raices/ activos
			total = 0;
			
			numerador = cContas6ValorAtual;
			denominador = cContas11ValorAtual;

			if (denominador !=0)
			{
				total = numerador / denominador;
				if(numerador == 0 && denominador < 0)
					total = total*-1;
			}
			
			celula = row.createCell(++coluna);
			celula.setCellValue(formataValor.format(total));
			celula.setCellStyle(estiloTexto);
			
			
			//Cálculo Inversiones renta fija / activos
			conta = cContas.get("0526010000");
			double cContas35ValorAtual = conta.obterTotalizacaoExistente(aseguradoras, mesAnoCalculo);
			
			total = 0;
			
			numerador = cContas35ValorAtual;
			denominador = cContas11ValorAtual;

			if (denominador !=0)
			{
				total = numerador / denominador;
				if(numerador == 0 && denominador < 0)
					total = total*-1;
			}
			
			celula = row.createCell(++coluna);
			celula.setCellValue(formataValor.format(total));
			celula.setCellStyle(estiloTexto);
			
			
			//Cálculo Inversiones renta fija/capital
			total = 0;
			
			numerador = cContas35ValorAtual;
			denominador = cContas2ValorAtual;

			if (denominador !=0)
			{
				total = numerador / denominador;
				if(numerador == 0 && denominador < 0)
					total = total*-1;
			}
			
			celula = row.createCell(++coluna);
			celula.setCellValue(formataValor.format(total));
			celula.setCellStyle(estiloTexto);
			
			
			//Cálculo Inversiones bienes raices/capital
			total = 0;
			
			numerador = cContas6ValorAtual;
			denominador = cContas2ValorAtual;

			if (denominador !=0)
			{
				total = numerador / denominador;
				if(numerador == 0 && denominador < 0)
					total = total*-1;
			}
			
			celula = row.createCell(++coluna);
			celula.setCellValue(formataValor.format(total));
			celula.setCellStyle(estiloTexto);
			
			
			//Cálculo Ingreso Neto Inversiones
			conta = cContas.get("0107000000");
			double cContas36ValorAtual = conta.obterTotalizacaoExistente(aseguradoras, mesAnoCalculo);
			
			total = cContas28ValorAtual - cContas36ValorAtual;
			
			celula = row.createCell(++coluna);
			celula.setCellValue(formataValor.format(total));
			celula.setCellStyle(estiloTexto);
			
			
			//Cálculo Comisiones directas
			conta = cContas.get("0404010000");
			double cContas37ValorAtual = conta.obterTotalizacaoExistente(aseguradoras, mesAnoCalculo);
			
			total = cContas37ValorAtual;
			
			celula = row.createCell(++coluna);
			celula.setCellValue(formataValor.format(total));
			celula.setCellStyle(estiloTexto);
			
			
			//Cálculo Comisiones por reaseguros incurridos
			
			total = cContas18ValorAtual + cContas19ValorAtual;
			
			celula = row.createCell(++coluna);
			celula.setCellValue(formataValor.format(total));
			celula.setCellStyle(estiloTexto);
			
			
			//Cálculo Gastos generales
			conta = cContas.get("0525000000");
			double cContas38ValorAtual = conta.obterTotalizacaoExistente(aseguradoras, mesAnoCalculo);
			
			total = cContas34ValorAtual + cContas38ValorAtual;
			
			celula = row.createCell(++coluna);
			celula.setCellValue(formataValor.format(total));
			celula.setCellStyle(estiloTexto);
			
			
			//Cálculo Prima Bruta/Prima Neta
			
			total = 0;
			
			numerador = cContas1ValorAtual;
			denominador = cContas1ValorAtual - cContas16ValorAtual - cContas17ValorAtual - cContas18ValorAtual - cContas19ValorAtual;

			if (denominador !=0)
			{
				total = numerador / denominador;
				if(numerador == 0 && denominador < 0)
					total = total*-1;
			}
			
			celula = row.createCell(++coluna);
			celula.setCellValue(formataValor.format(total));
			celula.setCellStyle(estiloTexto);
			
			
			//Cálculo Prima Bruta/Prima Neta
			
			total = 0;
			
			numerador = cContas1ValorAtual - cContas16ValorAtual - cContas17ValorAtual - cContas18ValorAtual - cContas19ValorAtual;
			denominador = cContas1ValorAtual;

			if (denominador !=0)
			{
				total = numerador / denominador;
				if(numerador == 0 && denominador < 0)
					total = total*-1;
			}
			
			celula = row.createCell(++coluna);
			celula.setCellValue(formataValor.format(total));
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
		
		cContas = (ClassificacaoContas) this.home.obterEntidadePorApelido("0301000000");
		this.cContas.put("0301000000", cContas);
		
		cContas = (ClassificacaoContas) this.home.obterEntidadePorApelido("0303000000");
		this.cContas.put("0303000000", cContas);
		
		cContas = (ClassificacaoContas) this.home.obterEntidadePorApelido("0301020000");
		this.cContas.put("0301020000", cContas);
		
		cContas = (ClassificacaoContas) this.home.obterEntidadePorApelido("0301010000");
		this.cContas.put("0301010000", cContas);
		
		cContas = (ClassificacaoContas) this.home.obterEntidadePorApelido("0109000000");
		this.cContas.put("0109000000", cContas);
		
		cContas = (ClassificacaoContas) this.home.obterEntidadePorApelido("0102000000");
		this.cContas.put("0102000000", cContas);
		
		cContas = (ClassificacaoContas) this.home.obterEntidadePorApelido("0103000000");
		this.cContas.put("0103000000", cContas);
		
		cContas = (ClassificacaoContas) this.home.obterEntidadePorApelido("0107020000");
		this.cContas.put("0107020000", cContas);
		
		cContas = (ClassificacaoContas) this.home.obterEntidadePorApelido("0107030000");
		this.cContas.put("0107030000", cContas);
		
		cContas = (ClassificacaoContas) this.home.obterEntidadePorApelido("0107040000");
		this.cContas.put("0107040000", cContas);
		
		cContas = (ClassificacaoContas) this.home.obterEntidadePorApelido("0213030000");
		this.cContas.put("0213030000", cContas);
		
		cContas = (ClassificacaoContas) this.home.obterEntidadePorApelido("0206020000");
		this.cContas.put("0206020000", cContas);
		
		cContas = (ClassificacaoContas) this.home.obterEntidadePorApelido("0410010000");
		this.cContas.put("0410010000", cContas);
		
		cContas = (ClassificacaoContas) this.home.obterEntidadePorApelido("0526010000");
		this.cContas.put("0526010000", cContas);
		
		cContas = (ClassificacaoContas) this.home.obterEntidadePorApelido("0404010000");
		this.cContas.put("0404010000", cContas);
		
		cContas = (ClassificacaoContas) this.home.obterEntidadePorApelido("0411010000");
		this.cContas.put("0411010000", cContas);
	}
	
}
