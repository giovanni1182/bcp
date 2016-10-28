package com.gvs.crm.report;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.hssf.util.Region;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.util.IOUtils;

import com.gvs.crm.model.ClassificacaoContas;
import com.gvs.crm.model.Conta;
import com.gvs.crm.model.Entidade;

import infra.config.InfraProperties;

public class RelatorioSaldosAseguradoraXLS extends Excel
{
	public RelatorioSaldosAseguradoraXLS(Collection<Entidade> entidades, String mes, String ano, Entidade aseguradora) throws Exception
	{
		String caminho = "C:/tmp/" + new Date().getTime() + ".xls";
		
		DecimalFormat format = new DecimalFormat("#,##0.00");
		
		FileOutputStream stream = new FileOutputStream(caminho);
		
		this.setCaminho(caminho);
		
		HSSFWorkbook wb = new HSSFWorkbook();

		HSSFSheet planilha = wb.createSheet("Planilha");
      
		  HSSFFont fonteTitulo = wb.createFont();
	      fonteTitulo.setFontHeightInPoints((short)10);
	      fonteTitulo.setFontName("Arial");
	      fonteTitulo.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
	      
	      HSSFFont fonteTituloTabela = wb.createFont();
	      fonteTituloTabela.setFontHeightInPoints((short)8);
	      fonteTituloTabela.setFontName("Arial");
	      fonteTituloTabela.setColor(HSSFColor.WHITE.index);
	      
	      HSSFFont fonteTexto = wb.createFont();
	      fonteTexto.setFontHeightInPoints((short)8);
	      fonteTexto.setFontName("Arial");
	      
	      HSSFFont fonteTextoN = wb.createFont();
	      fonteTextoN.setFontHeightInPoints((short)8);
	      fonteTextoN.setFontName("Arial");
	      fonteTextoN.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
	      
	      HSSFCellStyle estiloTitulo = wb.createCellStyle();
	      estiloTitulo.setAlignment(HSSFCellStyle.ALIGN_CENTER);
	      estiloTitulo.setFont(fonteTitulo);
	      
	      HSSFCellStyle estiloTexto = wb.createCellStyle();
	      estiloTexto.setAlignment(HSSFCellStyle.ALIGN_CENTER);
	      estiloTexto.setFont(fonteTexto);
	      
	      HSSFCellStyle estiloTextoN = wb.createCellStyle();
	      estiloTextoN.setAlignment(HSSFCellStyle.ALIGN_CENTER);
	      estiloTextoN.setFont(fonteTextoN);
	      
	      HSSFCellStyle estiloTextoE = wb.createCellStyle();
	      estiloTextoE.setAlignment(HSSFCellStyle.ALIGN_LEFT);
	      estiloTextoE.setFont(fonteTexto);
	      
	      HSSFCellStyle estiloTextoD = wb.createCellStyle();
	      estiloTextoD.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
	      estiloTextoD.setFont(fonteTexto);
	      
	      HSSFCellStyle estiloTituloTabela = wb.createCellStyle();
	      estiloTituloTabela.setAlignment(HSSFCellStyle.ALIGN_CENTER);
	      estiloTituloTabela.setFont(fonteTituloTabela);
	      estiloTituloTabela.setFillForegroundColor(HSSFColor.GREY_80_PERCENT.index);
	      estiloTituloTabela.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
	      
	      HSSFCellStyle estiloTituloTabelaE = wb.createCellStyle();
	      estiloTituloTabelaE.setAlignment(HSSFCellStyle.ALIGN_LEFT);
	      estiloTituloTabelaE.setFont(fonteTituloTabela);
	      estiloTituloTabelaE.setFillForegroundColor(HSSFColor.GREY_80_PERCENT.index);
	      estiloTituloTabelaE.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
	      
	      String dirImages = InfraProperties.getInstance().getProperty("report.images.url2");
	      
	      InputStream is = new FileInputStream(dirImages + "/bcp.jpg");
	      byte [] bytes = IOUtils.toByteArray (is);
	      int pictureIdx = wb.addPicture(bytes, Workbook.PICTURE_TYPE_JPEG); 
	      is.close();
	      
	      HSSFClientAnchor anchoVivaBem = new HSSFClientAnchor(0,0,0, 0, (short) 0, 0, (short)4 , 5);  
	      anchoVivaBem.setAnchorType(3);  
	      planilha.createDrawingPatriarch().createPicture(anchoVivaBem, pictureIdx);
	      
	      HSSFRow row = planilha.createRow(1);
	      HSSFCell celula = row.createCell(5);
	      
	      celula.setCellValue("Saldos " + aseguradora.obterNome());
	      celula.setCellStyle(estiloTitulo);
	    		         
	      Region r = new Region(1, (short)5, 1, (short)14);
	      planilha.addMergedRegion(r);
	      
	      row = planilha.createRow(2);
	      celula = row.createCell(5);
	      celula.setCellValue("Mês/Año: " + mes+"/"+ano);
	      celula.setCellStyle(estiloTitulo);
	      
	      r = new Region(2, (short)5, 2, (short)14);
	      planilha.addMergedRegion(r);
	      
	      row = planilha.createRow(6);
	      celula = row.createCell(0);
	      celula.setCellValue("Cuenta");
	      celula.setCellStyle(estiloTituloTabela);
	      
	      celula = row.createCell(1);
	      celula.setCellValue("Nombre");
	      celula.setCellStyle(estiloTituloTabela);
	      r = new Region(6, (short)1, 6, (short)4);
	      planilha.addMergedRegion(r);
	      
	      celula = row.createCell(5);
	      celula.setCellValue("Saldo Anterior");
	      celula.setCellStyle(estiloTituloTabela);
	      r = new Region(6, (short)5, 6, (short)6);
	      planilha.addMergedRegion(r);
	      
	      celula = row.createCell(7);
	      celula.setCellValue("Débito");
	      celula.setCellStyle(estiloTituloTabela);
	      r = new Region(6, (short)7, 6, (short)8);
	      planilha.addMergedRegion(r);
	      
	      celula = row.createCell(9);
	      celula.setCellValue("Crédito");
	      celula.setCellStyle(estiloTituloTabela);
	      r = new Region(6, (short)7, 6, (short)10);
	      planilha.addMergedRegion(r);
	      
	      celula = row.createCell(11);
	      celula.setCellValue("Saldo Actual");
	      celula.setCellStyle(estiloTituloTabela);
	      r = new Region(6, (short)11, 6, (short)12);
	      planilha.addMergedRegion(r);
	      
	      int linha = 7;
	      
	      Map superiores = new TreeMap();
	      
	      for(Iterator<Entidade> i = entidades.iterator() ; i.hasNext() ; )
	      {
	    	  Entidade e = i.next();
	    	  if (e instanceof ClassificacaoContas) 
	    	  {
	    		  ClassificacaoContas cContas = (ClassificacaoContas) e;
	    		  superiores.put(cContas.obterCodigo(), cContas);
	    	  }
	    	  else if(e instanceof Conta) 
	    	  {
	    		  Conta conta = (Conta) e;
	    		  superiores.put(conta.obterCodigo(), conta);
	    	  }
	    	  
	    	  for (Iterator j = e.obterSuperiores().iterator(); j.hasNext();) 
	    	  {
	    		  Entidade e2 = (Entidade) j.next();
	
	    		  if (e2 instanceof ClassificacaoContas) 
	    		  {
	    			  if (!e2.obterNome().equals("PLAN DE CUENTAS")) 
	    			  {
	    				  ClassificacaoContas cContas = (ClassificacaoContas) e2;
	    				  superiores.put(cContas.obterCodigo(), cContas);
	    			  }
	    		  }
	    	  }
	      }
	      
	      for(Iterator<Entidade> i = superiores.values().iterator() ; i.hasNext() ; )
	      {
	    	  Entidade e = i.next();
	    	  
	    	  double saldoAnterior = 0;
	    	  double debito = 0;
	    	  double credito = 0;
	    	  double saldoAtual = 0;
	    	  
		      if (e instanceof ClassificacaoContas) 
		      {
		    	  ClassificacaoContas cContas = (ClassificacaoContas) e;
		    	  saldoAnterior = cContas.obterTotalizacaoSaldoAnteriorExistente(aseguradora, mes+ano);
		    	  debito = cContas.obterTotalizacaoDebitoExistente(aseguradora, mes+ano);
		    	  credito = cContas.obterTotalizacaoCreditoExistente(aseguradora, mes+ano);
		    	  saldoAtual = cContas.obterTotalizacaoExistente(aseguradora, mes+ano);
		      }
		      else if(e instanceof Conta) 
			  {
		    	  Conta conta = (Conta) e;
		    	  saldoAnterior = conta.obterTotalizacaoSaldoAnteriorExistente(aseguradora, mes+ano);
		    	  debito = conta.obterTotalizacaoDebitoExistente(aseguradora, mes+ano);
		    	  credito = conta.obterTotalizacaoCreditoExistente(aseguradora, mes+ano);
		    	  saldoAtual = conta.obterTotalizacaoExistente(aseguradora, mes+ano);
			  }
					
		      if (saldoAtual != 0	|| credito != 0	|| debito != 0 || saldoAnterior != 0)
		      {
				 row = planilha.createRow(linha);
			     celula = row.createCell(0);
			     celula.setCellValue(e.obterApelido());
			     celula.setCellStyle(estiloTextoE);
			      
			     celula = row.createCell(1);
			     celula.setCellValue(e.obterNome());
			     celula.setCellStyle(estiloTextoE);
			     r = new Region(linha, (short)1, linha, (short)4);
			     planilha.addMergedRegion(r);
			      
				 celula = row.createCell(5);
				 celula.setCellValue(format.format(saldoAnterior));
				 celula.setCellStyle(estiloTextoE);
				 r = new Region(linha, (short)5, linha, (short)6);
				 planilha.addMergedRegion(r);
				 
				 celula = row.createCell(7);
				 celula.setCellValue(format.format(debito));
				 celula.setCellStyle(estiloTextoE);
				 r = new Region(linha, (short)7, linha, (short)8);
				 planilha.addMergedRegion(r);
				 
				 celula = row.createCell(9);
				 celula.setCellValue(format.format(credito));
				 celula.setCellStyle(estiloTextoE);
				 r = new Region(linha, (short)9, linha, (short)10);
				 planilha.addMergedRegion(r);
				 
				 celula = row.createCell(11);
				 celula.setCellValue(format.format(saldoAtual));
				 celula.setCellStyle(estiloTextoE);
				 r = new Region(linha, (short)11, linha, (short)12);
				 planilha.addMergedRegion(r);
				 
				 linha++;
		      }
	      	}
	      
	      wb.write(stream);

	      stream.flush();

	      stream.close();
	}
}
