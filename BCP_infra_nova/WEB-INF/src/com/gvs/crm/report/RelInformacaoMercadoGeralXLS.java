package com.gvs.crm.report;

import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
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

public class RelInformacaoMercadoGeralXLS extends Excel 
{
	private boolean calculoAnual,anoFiscal;
	private Collection<Entidade> aseguradoras;
	private DecimalFormat formataValor;
	private Date dataInicioReal,dataFimReal;
	private int linha;
	private HSSFSheet planilha;
	private HSSFCellStyle estiloTituloTabela,estiloTituloTabelaC,estiloTextoN_E,estiloTexto, estiloTextoD, estiloTexto_E,estiloTextoN_D;
	
	public RelInformacaoMercadoGeralXLS(Date dataInicio, Date dataFim, int anoInicio, int anoFim, EntidadeHome home, String tipo, Map<String,String> contas, boolean anoFiscal, boolean totalNormal, boolean totalPorcentagem, Collection<String> calculoTotal) throws Exception
	{
		String caminho = "C:/tmp/" + new Date().getTime() + ".xls";
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
		celula.setCellValue("INFORMACIÓN AGREGADA DEL MERCADO - " + tipo);
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
				celula.setCellValue("Periodo Año Fiscal: " + formataDataAno.format(dataInicioReal) + " hasta " + formataDataAno.format(dataFimReal));
			else
				celula.setCellValue("Periodo: " + formataDataAno.format(dataInicioReal) + " hasta " + formataDataAno.format(dataFimReal));
		}
		celula.setCellStyle(estiloTitulo_E);
		r = new Region(linha, (short)0, linha, (short)50);
		planilha.addMergedRegion(r);
		
		linha+=2;
		Entidade e;
		double valor,total,porcentagem,totalPeriodo,v;
		Date dataCalculo;
		coluna = -1;
		
		c = Calendar.getInstance();
		c.setTime(dataInicioReal);
		
		Map<Long,Double> totaisAseguradora = new TreeMap<>();
		
		while(c.getTime().compareTo(dataFimReal)<=0)
		{
			valor = 0;
			
			if(calculoAnual)
			{
				for(String apelidoconta : contas.keySet())
				{
					if(apelidoconta.indexOf("_") > 0)
						apelidoconta = apelidoconta.substring(apelidoconta.indexOf("_")+1,apelidoconta.length());
					
					System.out.println(apelidoconta);
						
					if(!apelidoconta.startsWith("Composta"))
					{
						e = home.obterEntidadePorApelido(apelidoconta);
						if(e instanceof ClassificacaoContas)
						{
							conta = (ClassificacaoContas) e;
							valor+= conta.obterTotalizacaoExistenteAnual(aseguradoras, Integer.valueOf(new SimpleDateFormat("yyyy").format(c.getTime())));
						}
						else
						{
							conta2 = (Conta) e;
							valor+= conta2.obterTotalizacaoExistenteAnual(aseguradoras, Integer.valueOf(new SimpleDateFormat("yyyy").format(c.getTime())));
						}
					}
				}
			}
			else
			{
				for(Entidade aseguradora : aseguradoras)
				{
					for(String apelidoconta : contas.keySet())
					{
						if(apelidoconta.indexOf("_") > 0)
							apelidoconta = apelidoconta.substring(apelidoconta.indexOf("_")+1,apelidoconta.length());
						
						//System.out.println(apelidoconta);
						
						if(!apelidoconta.startsWith("Composta"))
						{
							e = home.obterEntidadePorApelido(apelidoconta);
							if(e instanceof ClassificacaoContas)
							{
								conta = (ClassificacaoContas) e;
								valor+= conta.obterTotalizacaoExistente(aseguradora, new SimpleDateFormat("MMyyyy").format(c.getTime()));
							}
							else
							{
								conta2 = (Conta) e;
								valor+= conta2.obterTotalizacaoExistente(aseguradora, new SimpleDateFormat("MMyyyy").format(c.getTime()));
							}
						}
					}
				}
			}
			
			totaisAseguradora.put(c.getTimeInMillis(), valor);
			
			if(dataInicio!=null)
				c.add(Calendar.MONTH, 1);
			else
				c.add(Calendar.YEAR, 1);
		}
		
		row = planilha.createRow(linha);
		c.setTime(dataInicioReal);
		
		while(c.getTime().compareTo(dataFimReal)<=0)
		{
			dataCalculo = c.getTime();
			
			if(coluna == -1)
			{
				celula = row.createCell(++coluna);
				celula.setCellValue(tipo);
				celula.setCellStyle(estiloTituloTabelaC);
			}
			
			for(String contaStr : contas.values())
			{
				celula = row.createCell(++coluna);
				if(dataInicio!=null)
					celula.setCellValue(contaStr + " - " + formataData.format(dataCalculo));
				else
					celula.setCellValue(contaStr + " - " + formataDataAno.format(dataCalculo));
				celula.setCellStyle(estiloTituloTabelaC);
			}
			
			celula = row.createCell(++coluna);
			if(dataInicio!=null)
				celula.setCellValue("TOTAL "+ tipo.toUpperCase() + " - " + formataData.format(dataCalculo));
			else
				celula.setCellValue("TOTAL "+ tipo.toUpperCase() + " - " + formataDataAno.format(dataCalculo));
			celula.setCellStyle(estiloTituloTabelaC);
			
			if(totalPorcentagem)
			{
				celula = row.createCell(++coluna);
				if(dataInicio!=null)
					celula.setCellValue("Porcentaje - " + formataData.format(dataCalculo));
				else
					celula.setCellValue("Porcentaje - " + formataDataAno.format(dataCalculo));
				celula.setCellStyle(estiloTituloTabelaC);
			}
			
			++coluna;
			if(dataInicio!=null)
				c.add(Calendar.MONTH, 1);
			else
				c.add(Calendar.YEAR, 1);
		}
		
		Map<String,Double> totalContas = new TreeMap<>();
		
		for(Entidade aseguradora : aseguradoras)
		{
			coluna = 0;
			porcentagem = 0;
			
			row = planilha.createRow(++linha);
			celula = row.createCell(coluna);
			celula.setCellValue(aseguradora.obterNome());
			celula.setCellStyle(estiloTexto_E);
			
			c.setTime(dataInicioReal);
			
			while(c.getTime().compareTo(dataFimReal)<=0)
			{
				valor = 0;
				total = 0;
				
				dataCalculo = c.getTime();
				
				for(String apelidoconta : contas.keySet())
				{
					if(apelidoconta.indexOf("_") > 0)
						apelidoconta = apelidoconta.substring(apelidoconta.indexOf("_")+1,apelidoconta.length());
					
					if(apelidoconta.startsWith("Composta"))
					{
						if(apelidoconta.equals("Composta1"))
						{
							e = home.obterEntidadePorApelido("0525000000");
							valor = this.totalConta(e, dataCalculo, aseguradora);
							e = home.obterEntidadePorApelido("0525010401");
							valor+= this.totalConta(e, dataCalculo, aseguradora);
						}
						if(apelidoconta.equals("Composta2"))
						{
							e = home.obterEntidadePorApelido("0405000000");
							valor = this.totalConta(e, dataCalculo, aseguradora);
							e = home.obterEntidadePorApelido("0410000000");
							valor+= this.totalConta(e, dataCalculo, aseguradora);
							e = home.obterEntidadePorApelido("0411000000");
							valor+= this.totalConta(e, dataCalculo, aseguradora);
							e = home.obterEntidadePorApelido("0413000000");
							valor+= this.totalConta(e, dataCalculo, aseguradora);
							e = home.obterEntidadePorApelido("0415000000");
							valor+= this.totalConta(e, dataCalculo, aseguradora);
							e = home.obterEntidadePorApelido("0426000000");
							valor+= this.totalConta(e, dataCalculo, aseguradora);
							e = home.obterEntidadePorApelido("0525010401");
							valor+= this.totalConta(e, dataCalculo, aseguradora);
							e = home.obterEntidadePorApelido("0504000000");
							valor-= this.totalConta(e, dataCalculo, aseguradora);
							e = home.obterEntidadePorApelido("0510000000");
							valor-= this.totalConta(e, dataCalculo, aseguradora);
							e = home.obterEntidadePorApelido("0512000000");
							valor-= this.totalConta(e, dataCalculo, aseguradora);
							e = home.obterEntidadePorApelido("0514000000");
							valor-= this.totalConta(e, dataCalculo, aseguradora);
							e = home.obterEntidadePorApelido("0516000000");
							valor-= this.totalConta(e, dataCalculo, aseguradora);
							e = home.obterEntidadePorApelido("0527000000");
							valor-= this.totalConta(e, dataCalculo, aseguradora);
							e = home.obterEntidadePorApelido("0525000000");
							valor-= this.totalConta(e, dataCalculo, aseguradora);
						}
						if(apelidoconta.equals("Composta3"))
						{
							e = home.obterEntidadePorApelido("0435000000");
							valor = this.totalConta(e, dataCalculo, aseguradora);
							e = home.obterEntidadePorApelido("0535000000");
							valor-= this.totalConta(e, dataCalculo, aseguradora);
						}
					}
					else
					{
						e = home.obterEntidadePorApelido(apelidoconta);
						valor = this.totalConta(e, dataCalculo, aseguradora);
					}
					
					//Inibir -0,00
					if(valor == 0)
						valor = 0;
					
					celula = row.createCell(++coluna);
					celula.setCellValue(formataValor.format(valor));
					celula.setCellStyle(estiloTextoD);
					
					total+=valor;
					
					key = apelidoconta+"_"+ dataCalculo.getTime();
					
					if(totalContas.containsKey(key))
					{
						v = totalContas.get(key);
						v+=valor;
						totalContas.put(key,v);
					}
					else
						totalContas.put(key,valor);
				}
				
				if(totalNormal)
				{
					celula = row.createCell(++coluna);
					celula.setCellValue(formataValor.format(total));
					celula.setCellStyle(estiloTextoD);
				}
				else
				{
					total = 0;
					
					String operacao = "";
					for(String contaStr : calculoTotal)
					{
						if(contaStr.startsWith("0"))
						{
							key = contaStr+"_"+ dataCalculo.getTime();
							
							//System.out.println(new SimpleDateFormat("MM/yyyy").format(dataCalculo.getTime()) + " - " +contaStr+ " = " + totalContas.get(key));
							
							if(operacao.equals(""))
								total = totalContas.get(key);
							else
							{
								if(operacao.equals("+"))
									total+= totalContas.get(key);
								else if(operacao.equals("-"))
									total-= totalContas.get(key);
							}
						}
						else
							operacao = contaStr;
					}
					
					celula = row.createCell(++coluna);
					celula.setCellValue(formataValor.format(total));
					celula.setCellStyle(estiloTextoD);
				}
				
				key = "total_"+dataCalculo.getTime();
				if(totalContas.containsKey(key))
				{
					v = totalContas.get(key);
					v+=total;
					totalContas.put(key,v);
				}
				else
					totalContas.put(key,total);
				
				if(totalPorcentagem)
				{
					totalPeriodo = totaisAseguradora.get(c.getTimeInMillis());
					//System.out.println("totalPeriodo " + totalPeriodo);
					if(totalPeriodo!=0)
						porcentagem = (total * 100) / totalPeriodo;
					
					celula = row.createCell(++coluna);
					celula.setCellValue(formataValor.format(porcentagem));
					celula.setCellStyle(estiloTextoD);
					
					key = "porcentagem_"+dataCalculo.getTime();
					if(totalContas.containsKey(key))
					{
						v = totalContas.get(key);
						v+=porcentagem;
						totalContas.put(key,v);
					}
					else
						totalContas.put(key,porcentagem);
				}
				
				//System.out.println("porcentagem " + porcentagem);
				
				++coluna;
				
				if(dataInicio!=null)
					c.add(Calendar.MONTH, 1);
				else
					c.add(Calendar.YEAR, 1);
			}
		}
		
		coluna = 0;
		row = planilha.createRow(++linha);
		celula = row.createCell(coluna);
		celula.setCellValue("TOTAL");
		celula.setCellStyle(estiloTextoN);
		
		c.setTime(dataInicioReal);
		
		while(c.getTime().compareTo(dataFimReal)<=0)
		{
			dataCalculo = c.getTime();
			
			for(String apelidoconta : contas.keySet())
			{
				if(apelidoconta.indexOf("_") > 0)
					apelidoconta = apelidoconta.substring(apelidoconta.indexOf("_")+1,apelidoconta.length());
				
				valor = totalContas.get(apelidoconta+"_"+dataCalculo.getTime());
				
				celula = row.createCell(++coluna);
				celula.setCellValue(formataValor.format(valor));
				celula.setCellStyle(estiloTextoN_D);
			}
			
			key = "total_"+dataCalculo.getTime();
			valor = totalContas.get(key);
			celula = row.createCell(++coluna);
			celula.setCellValue(formataValor.format(valor));
			celula.setCellStyle(estiloTextoN_D);
			
			if(totalPorcentagem)
			{
				key = "porcentagem_"+dataCalculo.getTime();
				valor = totalContas.get(key);
				celula = row.createCell(++coluna);
				celula.setCellValue(formataValor.format(valor));
				celula.setCellStyle(estiloTextoN_D);
				
				
			}
			
			++coluna;
			if(dataInicio!=null)
				c.add(Calendar.MONTH, 1);
			else
				c.add(Calendar.YEAR, 1);
		}
		
		
		wb.write(stream);
		stream.flush();
		stream.close();
	}
	
	private double totalConta(Entidade contaCalculo, Date data, Entidade aseguradora) throws Exception
	{
		double total = 0;
		
		if(calculoAnual)
		{
			if(contaCalculo instanceof ClassificacaoContas)
			{
				ClassificacaoContas cContas = (ClassificacaoContas) contaCalculo;
				if(anoFiscal)
					total=cContas.obterTotalizacaoExistenteAnualFiscalBIG(aseguradora, Integer.valueOf(new SimpleDateFormat("yyyy").format(data))).doubleValue();
				else
					total=cContas.obterTotalizacaoExistenteAnual(aseguradora, Integer.valueOf(new SimpleDateFormat("yyyy").format(data)));
			}
			else
			{
				Conta conta = (Conta) contaCalculo;
				if(anoFiscal)
					total=conta.obterTotalizacaoExistenteAnualFiscalBIG(aseguradora, Integer.valueOf(new SimpleDateFormat("yyyy").format(data))).doubleValue();
				else
					total=conta.obterTotalizacaoExistenteAnual(aseguradora, Integer.valueOf(new SimpleDateFormat("yyyy").format(data)));
			}
		}
		else
		{
			if(contaCalculo instanceof ClassificacaoContas)
			{
				ClassificacaoContas cContas = (ClassificacaoContas) contaCalculo;
				
				total=cContas.obterTotalizacaoExistente(aseguradora, new SimpleDateFormat("MMyyyy").format(data));
			}
			else
			{
				Conta conta = (Conta) contaCalculo;
				
				total=conta.obterTotalizacaoExistente(aseguradora, new SimpleDateFormat("MMyyyy").format(data));
			}
		}
		
		return total;
	}
}
