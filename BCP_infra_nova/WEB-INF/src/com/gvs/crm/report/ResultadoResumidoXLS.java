package com.gvs.crm.report;

import java.io.FileOutputStream;
import java.math.BigDecimal;
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

public class ResultadoResumidoXLS extends Excel 
{
	private Collection<Entidade> aseguradoras;
	private DecimalFormat formataValor;
	private Date dataInicioReal,dataFimReal;
	private int linha;
	private HSSFSheet planilha;
	private HSSFCellStyle estiloTituloTabela,estiloTituloTabelaC,estiloTextoN_E,estiloTexto, estiloTextoD, estiloTexto_E,estiloTextoN_D;
	private  EntidadeHome home;
	private Map<String,ClassificacaoContas> cContas;
	private Map<String,Conta> contas;
	private boolean calculoAnual,anoFiscal;
	
	public ResultadoResumidoXLS(Date dataInicio, Date dataFim, int anoInicio, int anoFim, EntidadeHome home, boolean anoFiscal) throws Exception
	{
		String caminho = "C:/tmp/" + new Date().getTime() + ".xls";
		
		FileOutputStream stream = new FileOutputStream(caminho);
		
		this.home = home;
		this.anoFiscal = anoFiscal;
		
		this.setCaminho(caminho);
		
		SimpleDateFormat formataData = new SimpleDateFormat("MM/yyyy");
		SimpleDateFormat formataDataAno = new SimpleDateFormat("yyyy");
		formataValor = new DecimalFormat("#,##0.00");
		
		aseguradoras = home.obterAseguradorasSemCoaseguradora();
		this.instanciarContas();
		
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
		Calendar c;
	    String key;
	    ClassificacaoContas conta;
	    Conta conta2;
	    
	    linha = 0;
		int coluna = 0;
		
		row = planilha.createRow(linha);
		celula = row.createCell(coluna);
		celula.setCellValue("RESULTADO RESUMIDO");
		celula.setCellStyle(estiloTitulo_E);
		Region r = new Region(linha, (short)0, linha, (short)50);
		planilha.addMergedRegion(r);
		
		row = planilha.createRow(++linha);
		celula = row.createCell(coluna);
		if(dataInicio!=null)
			celula.setCellValue("Periodo: " + formataData.format(dataInicioReal) + " hasta " + formataData.format(dataFimReal));
		else
			celula.setCellValue("Periodo: " + formataDataAno.format(dataInicioReal) + " hasta " + formataDataAno.format(dataFimReal));
		celula.setCellStyle(estiloTitulo_E);
		r = new Region(linha, (short)0, linha, (short)50);
		planilha.addMergedRegion(r);
		
		linha+=2;
		Date dataCalculo, anoAnterior;
		
		Collection<String> calculos = new ArrayList<>();
		calculos.add("*INGRESOS TÉCNICOS DE PRODUCCIÓN");
		calculos.add("PRIMAS DIRECTAS");
		calculos.add("PRIMAS REASEGUROS ACEPTADOS - LOCAL");
		calculos.add("DESAFECTACIÓN DE PROVISIONES TÉCNICAS DE SEGUROS");
		calculos.add("*EGRESOS TÉCNICOS DE PRODUCCIÓN");
		calculos.add("PRIMAS REASEGUROS CEDIDOS - LOCAL");
		calculos.add("CONSTITUCIÓN DE PROVISIONES TÉCNICAS DE SEGUROS");
		calculos.add("*PRIMAS NETAS GANADAS");
		calculos.add("*SINIESTROS");
		calculos.add("SINIESTROS");
		calculos.add("PRESTACIONES E INDEMNIZACIONES SEGUROS DE VIDA");
		calculos.add("GASTOS DE LIQUIDACIÓN DE SINIESTROS, SALVATAJE Y RECUPERO");
		calculos.add("PARTICIPACIÓN RECUPERO REASEGUROS CEDIDOS - LOCAL");
		calculos.add("SINIESTROS REASEGUROS ACEPTADOS - LOCAL");
		calculos.add("CONSTITUCIÓN DE PROVISIONES TÉCNICAS DE SINIESTROS");
		calculos.add("*RECUPERO DE SINIESTROS");
		calculos.add("RECUPERO DE SINIESTROS");
		calculos.add("SINIESTROS RECUPERADOS REASEGUROS CEDIDOS LOCAL");
		calculos.add("PARTICIPACIÓN RECUPERO REASEGUROS ACEPTADOS - LOCAL");
		calculos.add("DESAFECTACIÓN DE PROVISIONES TÉCNICAS POR SINIESTROS");
		calculos.add("*SINIESTROS NETOS OCURRIDOS");
		calculos.add("*UTILIDAD / PÉRDIDA TÉCNICA BRUTA");
		calculos.add("*OTROS INGRESOS TÉCNICOS");
		calculos.add("REINTEGRO DE GASTOS DE PRODUCCIÓN");
		calculos.add("OTROS INGRESOS POR REASEGUROS CEDIDOS - LOCAL");
		calculos.add("OTROS INGRESOS POR REASEGUROS ACEPTADOS - LOCAL");
		calculos.add("DESAFECTACIÓN DE PREVISIONES");
		calculos.add("*OTROS EGRESOS TÉCNICOS");
		calculos.add("GASTOS DE PRODUCCIÓN");
		calculos.add("GASTOS DE CESIÓN REASEGUROS - LOCAL");
		calculos.add("GASTOS DE REASEGUROS ACEPTADOS - LOCAL");
		calculos.add("GASTOS TÉCNICOS DE EXPLOTACIÓN");
		calculos.add("CONSTITUCIÓN DE PREVISIONES");
		calculos.add("*UTILIDAD / PÉRDIDA TÉCNICA NETA");
		calculos.add("INGRESOS DE INVERSIÓN");
		calculos.add("GASTOS DE INVERSIÓN");
		calculos.add("*UTILIDAD / PÉRDIDA NETA SOBRE INVERSIONES");
		calculos.add("*RESULTADOS EXTRAORDINARIOS (NETOS)");
		calculos.add("*UTILIDAD / PÉRDIDA NETA ANTES DE IMPUESTO");
		calculos.add("*Impuesto a la Renta".toUpperCase());
		calculos.add("*UTILIDAD / PÉRDIDA NETA DEL EJERCICIO");
		
		row = planilha.createRow(++linha);
		celula = row.createCell(0);
		celula.setCellValue("");
		celula.setCellStyle(estiloTituloTabelaC);
		r = new Region(linha, (short)0, linha, (short)6);
		planilha.addMergedRegion(r);
		
		c = Calendar.getInstance();
		c.setTime(dataInicioReal);
		
		coluna = 6;
		
		while(c.getTime().compareTo(dataFimReal)<=0)
		{
			dataCalculo = c.getTime();
		
			celula = row.createCell(++coluna);
			if(dataInicio!=null)
				celula.setCellValue(formataData.format(dataCalculo));
			else
				celula.setCellValue(formataDataAno.format(dataCalculo));
			celula.setCellStyle(estiloTituloTabelaC);
			
			if(dataInicio!=null)
				c.add(Calendar.MONTH, 1);
			else
				c.add(Calendar.YEAR, 1);
		}
		
		BigDecimal total;
		Map<String,BigDecimal> totais = new TreeMap<>();
		
		for(String nomeConta : calculos)
		{
			total = new BigDecimal("0");
			
			row = planilha.createRow(++linha);
			celula = row.createCell(0);
			if(nomeConta.startsWith("*"))
			{
				celula.setCellValue(nomeConta.replace("*", ""));
				celula.setCellStyle(estiloTextoN_E);
			}
			else
			{
				celula.setCellValue(nomeConta);
				celula.setCellStyle(estiloTexto_E);
			}
			r = new Region(linha, (short)0, linha, (short)6);
			planilha.addMergedRegion(r);
			
			c.setTime(dataInicioReal);
			coluna = 6;
			
			while(c.getTime().compareTo(dataFimReal)<=0)
			{
				dataCalculo = c.getTime();
				
				if(nomeConta.equals("PRIMAS DIRECTAS"))
				{
					conta = cContas.get("0401000000");
					BigDecimal valor1 = this.totalConta(conta, dataCalculo);
					
					total = valor1;
					
					totais.put("PRIMAS DIRECTAS"+dataCalculo.getTime(), total);
				}
				else if(nomeConta.equals("PRIMAS REASEGUROS ACEPTADOS - LOCAL"))
				{
					conta = cContas.get("0402000000");
					BigDecimal valor1 = this.totalConta(conta, dataCalculo);
					conta = cContas.get("0403000000");
					BigDecimal valor2 = this.totalConta(conta, dataCalculo);
					
					total = valor1.add(valor2);
					
					totais.put("PRIMAS REASEGUROS ACEPTADOS - LOCAL"+dataCalculo.getTime(), total);
				}
				else if(nomeConta.equals("DESAFECTACIÓN DE PROVISIONES TÉCNICAS DE SEGUROS"))
				{
					conta = cContas.get("0404000000");
					BigDecimal valor1 = this.totalConta(conta, dataCalculo);
					
					total = valor1;
					
					totais.put("DESAFECTACIÓN DE PROVISIONES TÉCNICAS DE SEGUROS"+dataCalculo.getTime(), total);
				}
				else if(nomeConta.equals("PRIMAS REASEGUROS CEDIDOS - LOCAL"))
				{
					conta = cContas.get("0501000000");
					BigDecimal valor1 = this.totalConta(conta, dataCalculo);
					conta = cContas.get("0502000000");
					BigDecimal valor2 = this.totalConta(conta, dataCalculo);
					
					total = valor1.add(valor2);
					
					totais.put("PRIMAS REASEGUROS CEDIDOS - LOCAL"+dataCalculo.getTime(), total);
				}
				else if(nomeConta.equals("CONSTITUCIÓN DE PROVISIONES TÉCNICAS DE SEGUROS"))
				{
					conta = cContas.get("0503000000");
					BigDecimal valor1 = this.totalConta(conta, dataCalculo);
					
					total = valor1;
					
					totais.put("CONSTITUCIÓN DE PROVISIONES TÉCNICAS DE SEGUROS"+dataCalculo.getTime(), total);
				}
				else if(nomeConta.equals("*PRIMAS NETAS GANADAS"))
				{
					BigDecimal valor1 = totais.get("PRIMAS DIRECTAS"+dataCalculo.getTime());
					BigDecimal valor2 = totais.get("PRIMAS REASEGUROS ACEPTADOS - LOCAL"+dataCalculo.getTime());
					BigDecimal valor3 = totais.get("DESAFECTACIÓN DE PROVISIONES TÉCNICAS DE SEGUROS"+dataCalculo.getTime());
					BigDecimal valor4 = totais.get("PRIMAS REASEGUROS CEDIDOS - LOCAL"+dataCalculo.getTime());
					BigDecimal valor5 = totais.get("CONSTITUCIÓN DE PROVISIONES TÉCNICAS DE SEGUROS"+dataCalculo.getTime());
					
					total = valor1.add(valor2).add(valor3).subtract((valor4).add(valor5));
					
					totais.put("PRIMAS NETAS GANADAS"+dataCalculo.getTime(), total);
				}
				else if(nomeConta.equals("SINIESTROS"))
				{
					conta = cContas.get("0506000000");
					BigDecimal valor1 = this.totalConta(conta, dataCalculo);
					
					total = valor1;
					
					totais.put("SINIESTROS"+dataCalculo.getTime(), total);
				}
				else if(nomeConta.equals("PRESTACIONES E INDEMNIZACIONES SEGUROS DE VIDA"))
				{
					conta = cContas.get("0507000000");
					BigDecimal valor1 = this.totalConta(conta, dataCalculo);
					
					total = valor1;
					
					totais.put("PRESTACIONES E INDEMNIZACIONES SEGUROS DE VIDA"+dataCalculo.getTime(), total);
				}
				else if(nomeConta.equals("GASTOS DE LIQUIDACIÓN DE SINIESTROS, SALVATAJE Y RECUPERO"))
				{
					conta = cContas.get("0508000000");
					BigDecimal valor1 = this.totalConta(conta, dataCalculo);
					
					total = valor1;
					
					totais.put("GASTOS DE LIQUIDACIÓN DE SINIESTROS, SALVATAJE Y RECUPERO"+dataCalculo.getTime(), total);
				}
				else if(nomeConta.equals("PARTICIPACIÓN RECUPERO REASEGUROS CEDIDOS - LOCAL"))
				{
					conta = cContas.get("0509000000");
					BigDecimal valor1 = this.totalConta(conta, dataCalculo);
					conta = cContas.get("0511000000");
					BigDecimal valor2 = this.totalConta(conta, dataCalculo);
					
					total = valor1.add(valor2);
					
					totais.put("PARTICIPACIÓN RECUPERO REASEGUROS CEDIDOS - LOCAL"+dataCalculo.getTime(), total);
				}
				else if(nomeConta.equals("SINIESTROS REASEGUROS ACEPTADOS - LOCAL"))
				{
					conta = cContas.get("0513000000");
					BigDecimal valor1 = this.totalConta(conta, dataCalculo);
					conta = cContas.get("0515000000");
					BigDecimal valor2 = this.totalConta(conta, dataCalculo);
					
					total = valor1.add(valor2);
					
					totais.put("SINIESTROS REASEGUROS ACEPTADOS - LOCAL"+dataCalculo.getTime(), total);
				}
				else if(nomeConta.equals("CONSTITUCIÓN DE PROVISIONES TÉCNICAS DE SINIESTROS"))
				{
					conta = cContas.get("0505000000");
					BigDecimal valor1 = this.totalConta(conta, dataCalculo);
					
					total = valor1;
					
					totais.put("CONSTITUCIÓN DE PROVISIONES TÉCNICAS DE SINIESTROS"+dataCalculo.getTime(), total);
				}
				else if(nomeConta.equals("RECUPERO DE SINIESTROS"))
				{
					conta = cContas.get("0407000000");
					BigDecimal valor1 = this.totalConta(conta, dataCalculo);
					
					total = valor1;
					
					totais.put("RECUPERO DE SINIESTROS"+dataCalculo.getTime(), total);
				}
				else if(nomeConta.equals("SINIESTROS RECUPERADOS REASEGUROS CEDIDOS LOCAL"))
				{
					conta = cContas.get("0408000000");
					BigDecimal valor1 = this.totalConta(conta, dataCalculo);
					conta = cContas.get("0409000000");
					BigDecimal valor2 = this.totalConta(conta, dataCalculo);
					
					total = valor1.add(valor2);
					
					totais.put("SINIESTROS RECUPERADOS REASEGUROS CEDIDOS LOCAL"+dataCalculo.getTime(), total);
				}
				else if(nomeConta.equals("PARTICIPACIÓN RECUPERO REASEGUROS ACEPTADOS - LOCAL"))
				{
					conta = cContas.get("0412000000");
					BigDecimal valor1 = this.totalConta(conta, dataCalculo);
					conta = cContas.get("0414000000");
					BigDecimal valor2 = this.totalConta(conta, dataCalculo);
					
					total = valor1.add(valor2);
					
					totais.put("PARTICIPACIÓN RECUPERO REASEGUROS ACEPTADOS - LOCAL"+dataCalculo.getTime(), total);
				}
				else if(nomeConta.equals("DESAFECTACIÓN DE PROVISIONES TÉCNICAS POR SINIESTROS"))
				{
					conta = cContas.get("0406000000");
					BigDecimal valor1 = this.totalConta(conta, dataCalculo);
					
					total = valor1;
					
					totais.put("DESAFECTACIÓN DE PROVISIONES TÉCNICAS POR SINIESTROS"+dataCalculo.getTime(), total);
				}
				else if(nomeConta.equals("*SINIESTROS NETOS OCURRIDOS"))
				{
					BigDecimal valor1 = totais.get("SINIESTROS"+dataCalculo.getTime());
					BigDecimal valor2 = totais.get("PRESTACIONES E INDEMNIZACIONES SEGUROS DE VIDA"+dataCalculo.getTime());
					BigDecimal valor3 = totais.get("GASTOS DE LIQUIDACIÓN DE SINIESTROS, SALVATAJE Y RECUPERO"+dataCalculo.getTime());
					BigDecimal valor4 = totais.get("PARTICIPACIÓN RECUPERO REASEGUROS CEDIDOS - LOCAL"+dataCalculo.getTime());
					BigDecimal valor5 = totais.get("SINIESTROS REASEGUROS ACEPTADOS - LOCAL"+dataCalculo.getTime());
					BigDecimal valor6 = totais.get("CONSTITUCIÓN DE PROVISIONES TÉCNICAS DE SINIESTROS"+dataCalculo.getTime());
					
					BigDecimal totalSinistro = valor1.add(valor2).add(valor3).add(valor4).add(valor5).add(valor6);
					
					BigDecimal valor7 = totais.get("RECUPERO DE SINIESTROS"+dataCalculo.getTime());
					BigDecimal valor8 = totais.get("SINIESTROS RECUPERADOS REASEGUROS CEDIDOS LOCAL"+dataCalculo.getTime());
					BigDecimal valor9 = totais.get("PARTICIPACIÓN RECUPERO REASEGUROS ACEPTADOS - LOCAL"+dataCalculo.getTime());
					BigDecimal valor10 = totais.get("DESAFECTACIÓN DE PROVISIONES TÉCNICAS POR SINIESTROS"+dataCalculo.getTime());
					
					BigDecimal totalRecuperoSinistro = valor7.add(valor8).add(valor9).add(valor10);
					
					total = totalSinistro.subtract(totalRecuperoSinistro);
					
					totais.put("SINIESTROS NETOS OCURRIDOS"+dataCalculo.getTime(), total);
				}
				else if(nomeConta.equals("*UTILIDAD / PÉRDIDA TÉCNICA BRUTA"))
				{
					BigDecimal valor1 = totais.get("PRIMAS NETAS GANADAS"+dataCalculo.getTime());
					BigDecimal valor2 = totais.get("SINIESTROS NETOS OCURRIDOS"+dataCalculo.getTime());
					
					total = valor1.subtract(valor2);
					
					
					totais.put("UTILIDAD / PÉRDIDA TÉCNICA BRUTA"+dataCalculo.getTime(), total);
				}
				else if(nomeConta.equals("REINTEGRO DE GASTOS DE PRODUCCIÓN"))
				{
					conta = cContas.get("0405000000");
					BigDecimal valor1 = this.totalConta(conta, dataCalculo);
					
					total = valor1;
					
					totais.put("REINTEGRO DE GASTOS DE PRODUCCIÓN"+dataCalculo.getTime(), total);
				}
				else if(nomeConta.equals("OTROS INGRESOS POR REASEGUROS CEDIDOS - LOCAL"))
				{
					conta = cContas.get("0410000000");
					BigDecimal valor1 = this.totalConta(conta, dataCalculo);
					
					conta = cContas.get("0411000000");
					BigDecimal valor2 = this.totalConta(conta, dataCalculo);
					
					total = valor1.add(valor2);
					
					totais.put("OTROS INGRESOS POR REASEGUROS CEDIDOS - LOCAL"+dataCalculo.getTime(), total);
				}
				else if(nomeConta.equals("OTROS INGRESOS POR REASEGUROS ACEPTADOS - LOCAL"))
				{
					conta = cContas.get("0413000000");
					BigDecimal valor1 = this.totalConta(conta, dataCalculo);
					
					conta = cContas.get("0415000000");
					BigDecimal valor2 = this.totalConta(conta, dataCalculo);
					
					total = valor1.add(valor2);
					
					totais.put("OTROS INGRESOS POR REASEGUROS ACEPTADOS - LOCAL"+dataCalculo.getTime(), total);
				}
				else if(nomeConta.equals("DESAFECTACIÓN DE PREVISIONES"))
				{
					conta = cContas.get("0426000000");
					BigDecimal valor1 = this.totalConta(conta, dataCalculo);
					
					total = valor1;
					
					totais.put("DESAFECTACIÓN DE PREVISIONES"+dataCalculo.getTime(), total);
				}
				else if(nomeConta.equals("GASTOS DE PRODUCCIÓN"))
				{
					conta = cContas.get("0504000000");
					BigDecimal valor1 = this.totalConta(conta, dataCalculo);
					
					total = valor1;
					
					totais.put("GASTOS DE PRODUCCIÓN"+dataCalculo.getTime(), total);
				}
				else if(nomeConta.equals("GASTOS DE CESIÓN REASEGUROS - LOCAL"))
				{
					conta = cContas.get("0510000000");
					BigDecimal valor1 = this.totalConta(conta, dataCalculo);
					
					conta = cContas.get("0512000000");
					BigDecimal valor2 = this.totalConta(conta, dataCalculo);
					
					total = valor1.add(valor2);
					
					totais.put("GASTOS DE CESIÓN REASEGUROS - LOCAL"+dataCalculo.getTime(), total);
				}
				else if(nomeConta.equals("GASTOS DE REASEGUROS ACEPTADOS - LOCAL"))
				{
					conta = cContas.get("0514000000");
					BigDecimal valor1 = this.totalConta(conta, dataCalculo);
					
					conta = cContas.get("0516000000");
					BigDecimal valor2 = this.totalConta(conta, dataCalculo);
					
					total = valor1.add(valor2);
					
					totais.put("GASTOS DE REASEGUROS ACEPTADOS - LOCAL"+dataCalculo.getTime(), total);
				}
				else if(nomeConta.equals("GASTOS TÉCNICOS DE EXPLOTACIÓN"))
				{
					conta = cContas.get("0525000000");
					BigDecimal valor1 = this.totalConta(conta, dataCalculo);
					
					conta2 = contas.get("0525010401");
					BigDecimal valor2 = this.totalConta(conta2, dataCalculo);
					
					total = valor1.subtract(valor2);
					
					totais.put("GASTOS TÉCNICOS DE EXPLOTACIÓN"+dataCalculo.getTime(), total);
				}
				else if(nomeConta.equals("CONSTITUCIÓN DE PREVISIONES"))
				{
					conta = cContas.get("0527000000");
					BigDecimal valor1 = this.totalConta(conta, dataCalculo);
					
					total = valor1;
					
					totais.put("CONSTITUCIÓN DE PREVISIONES"+dataCalculo.getTime(), total);
				}
				else if(nomeConta.equals("*UTILIDAD / PÉRDIDA TÉCNICA NETA"))
				{
					BigDecimal valor1 = totais.get("UTILIDAD / PÉRDIDA TÉCNICA BRUTA"+dataCalculo.getTime());
					
					BigDecimal valor2 = totais.get("REINTEGRO DE GASTOS DE PRODUCCIÓN"+dataCalculo.getTime());
					BigDecimal valor3 = totais.get("OTROS INGRESOS POR REASEGUROS CEDIDOS - LOCAL"+dataCalculo.getTime());
					BigDecimal valor4 = totais.get("OTROS INGRESOS POR REASEGUROS ACEPTADOS - LOCAL"+dataCalculo.getTime());
					BigDecimal valor5 = totais.get("DESAFECTACIÓN DE PREVISIONES"+dataCalculo.getTime());
					
					BigDecimal totalIngresso = valor2.add(valor3).add(valor4).add(valor5);
					
					totais.put("TOTAL INGRESSO"+dataCalculo.getTime(), totalIngresso);
					
					BigDecimal valor6 = totais.get("GASTOS DE PRODUCCIÓN"+dataCalculo.getTime());
					BigDecimal valor7 = totais.get("GASTOS DE CESIÓN REASEGUROS - LOCAL"+dataCalculo.getTime());
					BigDecimal valor8 = totais.get("GASTOS DE REASEGUROS ACEPTADOS - LOCAL"+dataCalculo.getTime());
					BigDecimal valor9 = totais.get("GASTOS TÉCNICOS DE EXPLOTACIÓN"+dataCalculo.getTime());
					BigDecimal valor10 = totais.get("CONSTITUCIÓN DE PREVISIONES"+dataCalculo.getTime());
					
					BigDecimal totalEgresso = valor6.add(valor7).add(valor8).add(valor9).add(valor10);
					
					totais.put("TOTAL EGRESSO"+dataCalculo.getTime(), totalEgresso);
					
					total = valor1.add(totalIngresso).subtract(totalEgresso);
					
					totais.put("UTILIDAD / PÉRDIDA TÉCNICA NETA"+dataCalculo.getTime(), total);
				}
				else if(nomeConta.equals("INGRESOS DE INVERSIÓN"))
				{
					conta = cContas.get("0425000000");
					BigDecimal valor1 = this.totalConta(conta, dataCalculo);
					
					total = valor1;
					
					totais.put("INGRESOS DE INVERSIÓN"+dataCalculo.getTime(), total);
				}
				else if(nomeConta.equals("GASTOS DE INVERSIÓN"))
				{
					conta = cContas.get("0526000000");
					BigDecimal valor1 = this.totalConta(conta, dataCalculo);
					
					total = valor1;
					
					totais.put("GASTOS DE INVERSIÓN"+dataCalculo.getTime(), total);
				}
				else if(nomeConta.equals("*UTILIDAD / PÉRDIDA NETA SOBRE INVERSIONES"))
				{
					BigDecimal valor1 = totais.get("INGRESOS DE INVERSIÓN"+dataCalculo.getTime());
					BigDecimal valor2 = totais.get("GASTOS DE INVERSIÓN"+dataCalculo.getTime());
					
					total = valor1.subtract(valor2);
					
					totais.put("UTILIDAD / PÉRDIDA NETA SOBRE INVERSIONES"+dataCalculo.getTime(), total);
				}
				else if(nomeConta.equals("*RESULTADOS EXTRAORDINARIOS (NETOS)"))
				{
					conta = cContas.get("0435000000");
					BigDecimal valor1 = this.totalConta(conta, dataCalculo);
					
					conta = cContas.get("0535000000");
					BigDecimal valor2 = this.totalConta(conta, dataCalculo);
					
					total = valor1.subtract(valor2);
					
					totais.put("RESULTADOS EXTRAORDINARIOS (NETOS)"+dataCalculo.getTime(), total);
				}
				else if(nomeConta.equals("*UTILIDAD / PÉRDIDA NETA ANTES DE IMPUESTO"))
				{
					BigDecimal valor1 = totais.get("UTILIDAD / PÉRDIDA TÉCNICA NETA"+dataCalculo.getTime());
					BigDecimal valor2 = totais.get("UTILIDAD / PÉRDIDA NETA SOBRE INVERSIONES"+dataCalculo.getTime());
					BigDecimal valor3 = totais.get("RESULTADOS EXTRAORDINARIOS (NETOS)"+dataCalculo.getTime());
					
					total = valor1.add(valor2).add(valor3);
					
					totais.put("UTILIDAD / PÉRDIDA NETA ANTES DE IMPUESTO"+dataCalculo.getTime(), total);
				}
				else if(nomeConta.equals("*Impuesto a la Renta".toUpperCase()))
				{
					conta2 = contas.get("0525010401");
					BigDecimal valor1 = this.totalConta(conta2, dataCalculo);
					
					total = valor1;
					
					totais.put("Impuesto a la Renta".toUpperCase()+dataCalculo.getTime(), total);
				}
				else if(nomeConta.equals("*UTILIDAD / PÉRDIDA NETA DEL EJERCICIO"))
				{
					BigDecimal valor1 = totais.get("UTILIDAD / PÉRDIDA NETA ANTES DE IMPUESTO"+dataCalculo.getTime());
					BigDecimal valor2 = totais.get("Impuesto a la Renta".toUpperCase()+dataCalculo.getTime());
					
					total = valor1.subtract(valor2);
				}
				
				//MOSTRA O TOTAL
				if(!nomeConta.equals("*INGRESOS TÉCNICOS DE PRODUCCIÓN") && !nomeConta.equals("*EGRESOS TÉCNICOS DE PRODUCCIÓN") && !nomeConta.equals("*SINIESTROS") && !nomeConta.equals("*RECUPERO DE SINIESTROS") && !nomeConta.equals("*OTROS INGRESOS TÉCNICOS") && !nomeConta.equals("*OTROS EGRESOS TÉCNICOS"))
				{
					celula = row.createCell(++coluna);
					celula.setCellValue(formataValor.format(total));
					if(nomeConta.startsWith("*"))
						celula.setCellStyle(estiloTextoN_D);
					else
						celula.setCellStyle(estiloTextoD);
				}
				
				if(dataInicio!=null)
					c.add(Calendar.MONTH, 1);
				else
					c.add(Calendar.YEAR, 1);
			}
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
		
		String[] contas = {"0401000000","0402000000","0403000000","0404000000","0501000000","0502000000","0503000000","0508000000","0509000000","0511000000","0513000000","0515000000","0505000000","0407000000","0408000000","0409000000","0412000000",
				"0414000000","0406000000","0405000000","0410000000","0411000000","0413000000","0415000000","0426000000","0504000000","0510000000","0512000000","0514000000","0516000000","0525000000","0527000000","0425000000","0526000000","0435000000",
				"0535000000","0506000000","0507000000"
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
	
	private BigDecimal totalConta(Entidade contaCalculo, Date data) throws Exception
	{
		BigDecimal total = new BigDecimal("0");
		
		if(calculoAnual)
		{
			if(contaCalculo instanceof ClassificacaoContas)
			{
				classificacao = (ClassificacaoContas) contaCalculo;
				
				if(anoFiscal)
					total = classificacao.obterTotalizacaoExistenteAnualFiscalBIG(aseguradoras, Integer.valueOf(new SimpleDateFormat("yyyy").format(data)));
				else
					total = classificacao.obterTotalizacaoExistenteAnualBIG(aseguradoras, Integer.valueOf(new SimpleDateFormat("yyyy").format(data)));
			}
			else
			{
				conta = (Conta) contaCalculo;
				
				if(anoFiscal)
					total = conta.obterTotalizacaoExistenteAnualFiscalBIG(aseguradoras, Integer.valueOf(new SimpleDateFormat("yyyy").format(data)));
				else
					total = conta.obterTotalizacaoExistenteAnualBIG(aseguradoras, Integer.valueOf(new SimpleDateFormat("yyyy").format(data)));
			}
		}
		else
		{
			if(contaCalculo instanceof ClassificacaoContas)
			{
				classificacao = (ClassificacaoContas) contaCalculo;
				total=classificacao.obterTotalizacaoExistenteBIG(aseguradoras, new SimpleDateFormat("MMyyyy").format(data));
			}
			else
			{
				conta = (Conta) contaCalculo;
				total=conta.obterTotalizacaoExistenteBIG(aseguradoras, new SimpleDateFormat("MMyyyy").format(data));
			}
		}
		
		return total;
	}
}
