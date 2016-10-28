package com.gvs.crm.report;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

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

import com.gvs.crm.model.Aseguradora;
import com.gvs.crm.model.ClassificacaoContas;
import com.gvs.crm.model.EntidadeHome;

import infra.config.InfraProperties;

public class CentralRiscoSinistrosXLS extends Excel
{
	public CentralRiscoSinistrosXLS(Aseguradora aseguradora, Date dataInicio, Date dataFim, EntidadeHome home) throws Exception
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
      
      HSSFCellStyle estiloTituloE = wb.createCellStyle();
      estiloTituloE.setFont(fonteTitulo);
      
      HSSFCellStyle estiloTexto = wb.createCellStyle();
      estiloTexto.setAlignment(HSSFCellStyle.ALIGN_CENTER);
      estiloTexto.setFont(fonteTexto);
      
      HSSFCellStyle estiloTextoN = wb.createCellStyle();
      estiloTextoN.setAlignment(HSSFCellStyle.ALIGN_CENTER);
      estiloTextoN.setFont(fonteTextoN);
      
      HSSFCellStyle estiloTextoN_E = wb.createCellStyle();
      estiloTextoN_E.setAlignment(HSSFCellStyle.ALIGN_LEFT);
      estiloTextoN_E.setFont(fonteTextoN);
      
      HSSFCellStyle estiloTextoN_D = wb.createCellStyle();
      estiloTextoN_D.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
      estiloTextoN_D.setFont(fonteTextoN);
      
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
      
      HSSFClientAnchor anchoVivaBem = new HSSFClientAnchor(0,0,0, 0, (short) 0, 0, (short)3 , 5);  
      anchoVivaBem.setAnchorType(3);  
      planilha.createDrawingPatriarch().createPicture(anchoVivaBem, pictureIdx);
      
      HSSFRow row = planilha.createRow(1);
      HSSFCell celula = row.createCell(5);
      
   	  celula.setCellValue("Aseguradora " + aseguradora.obterNome());
      celula.setCellStyle(estiloTituloE);
      Region r = new Region(1, (short)5, 1, (short)14);
      planilha.addMergedRegion(r);
      
      row = planilha.createRow(3);
      celula = row.createCell(5);
      
   	  celula.setCellValue("Periodo" + new SimpleDateFormat("dd/MM/yyyy").format(dataInicio) + " hasta " + new SimpleDateFormat("dd/MM/yyyy").format(dataFim));
      celula.setCellStyle(estiloTituloE);
      r = new Region(3, (short)5, 3, (short)14);
      planilha.addMergedRegion(r);
      
      row = planilha.createRow(6);
      celula = row.createCell(0);
      
   	  celula.setCellValue("Vigentes");
      celula.setCellStyle(estiloTituloE);
      r = new Region(6, (short)0, 6, (short)14);
      planilha.addMergedRegion(r);
      
      row = planilha.createRow(7);
      celula = row.createCell(0);
      
   	  celula.setCellValue("Sección");
      celula.setCellStyle(estiloTituloTabela);
      r = new Region(7, (short)0, 7, (short)3);
      planilha.addMergedRegion(r);
      
      celula = row.createCell(4);
      celula.setCellValue("Cantidad");
      celula.setCellStyle(estiloTituloTabela);
      r = new Region(7, (short)4, 7, (short)5);
      planilha.addMergedRegion(r);
      
      celula = row.createCell(6);
      celula.setCellValue("Valor Total (Gs)");
      celula.setCellStyle(estiloTituloTabela);
      r = new Region(7, (short)6, 7, (short)7);
      planilha.addMergedRegion(r);
      
      celula = row.createCell(8);
      celula.setCellValue("Valor Pago Aseguradora (Gs)");
      celula.setCellStyle(estiloTituloTabela);
      r = new Region(7, (short)8, 7, (short)10);
      planilha.addMergedRegion(r);
      
      int linhaN = 8;
      int qtdeVigente = 0;
      double totalSinistroPagosVigente = 0;
      double totalSinistroRecuperadosVigente = 0;
      
      boolean entrou = false;
      String[] linha;
      long secaoId;
      int qtde2;
      double totalSinistroPagos2,totalSinistroRecuperados2;
      ClassificacaoContas cContas;
      
      for(String linhaSuja : aseguradora.obterSecoesSinistrosVigente(dataInicio, dataFim))
      {
    	  linha = linhaSuja.split(";");
			
    	  secaoId = Long.parseLong(linha[0]); 
    	  qtde2 = Integer.parseInt(linha[1]);
    	  totalSinistroPagos2 = Double.parseDouble(linha[2]);
			
    	  cContas = (ClassificacaoContas) home.obterEntidadePorId(secaoId);
			
    	  row = planilha.createRow(linhaN);
    	  celula = row.createCell(0);
    	  celula.setCellValue(cContas.obterNome());
    	  celula.setCellStyle(estiloTextoE);
    	  r = new Region(linhaN, (short)0, linhaN, (short)3);
    	  planilha.addMergedRegion(r);
			
    	  totalSinistroRecuperados2 = cContas.obterValorSinistrosRecuperadosPorSecaoVigente(aseguradora,dataInicio, dataFim);
			
    	  qtdeVigente+=qtde2;
    	  totalSinistroPagosVigente+=totalSinistroPagos2;
    	  totalSinistroRecuperadosVigente+=totalSinistroRecuperados2;
			
    	  celula = row.createCell(4);
    	  celula.setCellValue(qtde2);
    	  celula.setCellStyle(estiloTexto);
    	  r = new Region(linhaN, (short)4, linhaN, (short)5);
    	  planilha.addMergedRegion(r);
		    
    	  celula = row.createCell(6);
    	  celula.setCellValue(format.format(totalSinistroPagos2));
    	  celula.setCellStyle(estiloTextoD);
    	  r = new Region(linhaN, (short)6, linhaN, (short)7);
    	  planilha.addMergedRegion(r);
		    
    	  celula = row.createCell(8);
    	  celula.setCellValue(format.format(totalSinistroRecuperados2));
    	  celula.setCellStyle(estiloTextoD);
    	  r = new Region(linhaN, (short)8, linhaN, (short)10);
    	  planilha.addMergedRegion(r);
		     
    	  linhaN++;
		    
    	  entrou = true;
      }
      
      if(!entrou)
    	  linhaN++;
      
      row = planilha.createRow(linhaN);
      celula = row.createCell(0);
      celula.setCellValue("TOTAL");
      celula.setCellStyle(estiloTextoN);
      r = new Region(linhaN, (short)0, linhaN, (short)3);
      planilha.addMergedRegion(r);
	    
      celula = row.createCell(4);
      celula.setCellValue(qtdeVigente);
      celula.setCellStyle(estiloTextoN);
      r = new Region(linhaN, (short)4, linhaN, (short)5);
      planilha.addMergedRegion(r);
	    
      celula = row.createCell(6);
      celula.setCellValue(format.format(totalSinistroPagosVigente));
      celula.setCellStyle(estiloTextoN_D);
      r = new Region(linhaN, (short)6, linhaN, (short)7);
      planilha.addMergedRegion(r);
	    
      celula = row.createCell(8);
      celula.setCellValue(format.format(totalSinistroRecuperadosVigente));
      celula.setCellStyle(estiloTextoN_D);
      r = new Region(linhaN, (short)8, linhaN, (short)10);
      planilha.addMergedRegion(r);
     
      linhaN+=2;
		
      row = planilha.createRow(linhaN);
      celula = row.createCell(0);
      celula.setCellValue("No Vigentes");
      celula.setCellStyle(estiloTituloE);
      r = new Region(linhaN, (short)0, linhaN, (short)14);
      planilha.addMergedRegion(r);
	    
      linhaN++;
	    
      row = planilha.createRow(linhaN);
      celula = row.createCell(0);
	      
   	  celula.setCellValue("Sección");
      celula.setCellStyle(estiloTituloTabela);
      r = new Region(linhaN, (short)0, linhaN, (short)3);
      planilha.addMergedRegion(r);
	      
      celula = row.createCell(4);
      celula.setCellValue("Cantidad");
      celula.setCellStyle(estiloTituloTabela);
      r = new Region(linhaN, (short)4, linhaN, (short)5);
      planilha.addMergedRegion(r);
	      
      celula = row.createCell(6);
      celula.setCellValue("Valor Total (Gs)");
      celula.setCellStyle(estiloTituloTabela);
      r = new Region(linhaN, (short)6, linhaN, (short)7);
      planilha.addMergedRegion(r);
	      
      celula = row.createCell(8);
      celula.setCellValue("Valor Pago Aseguradora (Gs)");
      celula.setCellStyle(estiloTituloTabela);
      r = new Region(linhaN, (short)8, linhaN, (short)10);
      planilha.addMergedRegion(r);
	    
      entrou = false;
	    
      int qtdeNoVigente = 0;
      double totalSinistroPagosNoVigente = 0;
      double totalSinistroRecuperadosNoVigente = 0;
      linhaN++;	
		
      for(String linhaSuja : aseguradora.obterSecoesSinistrosNoVigente(dataInicio, dataFim))
      {
    	  linha = linhaSuja.split(";");
			
    	  secaoId = Long.parseLong(linha[0]); 
    	  qtde2 = Integer.parseInt(linha[1]);
    	  totalSinistroPagos2 = Double.parseDouble(linha[2]);
			
    	  cContas = (ClassificacaoContas) home.obterEntidadePorId(secaoId);
			
    	  row = planilha.createRow(linhaN);
    	  celula = row.createCell(0);
    	  celula.setCellValue(cContas.obterNome());
    	  celula.setCellStyle(estiloTextoE);
    	  r = new Region(linhaN, (short)0, linhaN, (short)3);
    	  planilha.addMergedRegion(r);
			
    	  totalSinistroRecuperados2 = cContas.obterValorSinistrosRecuperadosPorSecaoNoVigente(aseguradora,dataInicio, dataFim);
			
    	  qtdeNoVigente+=qtde2;
    	  totalSinistroPagosNoVigente+=totalSinistroPagos2;
    	  totalSinistroRecuperadosNoVigente+=totalSinistroRecuperados2;
			
    	  celula = row.createCell(4);
    	  celula.setCellValue(qtde2);
    	  celula.setCellStyle(estiloTexto);
    	  r = new Region(linhaN, (short)4, linhaN, (short)5);
    	  planilha.addMergedRegion(r);
		    
    	  celula = row.createCell(6);
    	  celula.setCellValue(format.format(totalSinistroPagos2));
    	  celula.setCellStyle(estiloTextoD);
    	  r = new Region(linhaN, (short)6, linhaN, (short)7);
    	  planilha.addMergedRegion(r);
		    
    	  celula = row.createCell(8);
    	  celula.setCellValue(format.format(totalSinistroRecuperados2));
    	  celula.setCellStyle(estiloTextoD);
    	  r = new Region(linhaN, (short)8, linhaN, (short)10);
    	  planilha.addMergedRegion(r);
		     
    	  linhaN++;
		    
    	  entrou = true;
      }
	  
      if(!entrou)
    	  linhaN++;
	      
      row = planilha.createRow(linhaN);
      celula = row.createCell(0);
      celula.setCellValue("TOTAL");
      celula.setCellStyle(estiloTextoN);
      r = new Region(linhaN, (short)0, linhaN, (short)3);
      planilha.addMergedRegion(r);
		    
      celula = row.createCell(4);
      celula.setCellValue(qtdeNoVigente);
      celula.setCellStyle(estiloTextoN);
      r = new Region(linhaN, (short)4, linhaN, (short)5);
      planilha.addMergedRegion(r);
		    
      celula = row.createCell(6);
      celula.setCellValue(format.format(totalSinistroPagosNoVigente));
      celula.setCellStyle(estiloTextoN_D);
      r = new Region(linhaN, (short)6, linhaN, (short)7);
      planilha.addMergedRegion(r);
		    
      celula = row.createCell(8);
      celula.setCellValue(format.format(totalSinistroRecuperadosNoVigente));
      celula.setCellStyle(estiloTextoN_D);
      r = new Region(linhaN, (short)8, linhaN, (short)10);
      planilha.addMergedRegion(r);
	     
      linhaN+=2;
			
      row = planilha.createRow(linhaN);
      celula = row.createCell(0);
      celula.setCellValue("Vigentes Judicializados");
      celula.setCellStyle(estiloTituloE);
      r = new Region(linhaN, (short)0, linhaN, (short)14);
      planilha.addMergedRegion(r);
		    
      linhaN++;
		    
      row = planilha.createRow(linhaN);
      celula = row.createCell(0);
		      
   	  celula.setCellValue("Sección");
      celula.setCellStyle(estiloTituloTabela);
      r = new Region(linhaN, (short)0, linhaN, (short)3);
      planilha.addMergedRegion(r);
		      
      celula = row.createCell(4);
      celula.setCellValue("Cantidad");
      celula.setCellStyle(estiloTituloTabela);
      r = new Region(linhaN, (short)4, linhaN, (short)5);
      planilha.addMergedRegion(r);
		      
      celula = row.createCell(6);
      celula.setCellValue("Valor Total (Gs)");
      celula.setCellStyle(estiloTituloTabela);
      r = new Region(linhaN, (short)6, linhaN, (short)7);
      planilha.addMergedRegion(r);
		      
      celula = row.createCell(8);
      celula.setCellValue("Valor Pago Aseguradora (Gs)");
      celula.setCellStyle(estiloTituloTabela);
      r = new Region(linhaN, (short)8, linhaN, (short)10);
      planilha.addMergedRegion(r);
		    
      entrou = false;
		    
      int qtdeVigenteJudicializado = 0;
      double totalSinistroPagosVigenteJudicializado = 0;
      double totalSinistroRecuperadosVigenteJudicializado = 0;
			
      linhaN++;
			
      for(String linhaSuja : aseguradora.obterSecoesSinistrosVigenteJudicializado(dataInicio, dataFim))
      {
    	  linha = linhaSuja.split(";");
					
    	  secaoId = Long.parseLong(linha[0]); 
    	  qtde2 = Integer.parseInt(linha[1]);
    	  totalSinistroPagos2 = Double.parseDouble(linha[2]);
		
    	  cContas = (ClassificacaoContas) home.obterEntidadePorId(secaoId);
		
    	  row = planilha.createRow(linhaN);
    	  celula = row.createCell(0);
    	  celula.setCellValue(cContas.obterNome());
    	  celula.setCellStyle(estiloTextoE);
    	  r = new Region(linhaN, (short)0, linhaN, (short)3);
    	  planilha.addMergedRegion(r);
		
    	  totalSinistroRecuperados2 = cContas.obterValorSinistrosRecuperadosPorSecaoVigenteJudicializado(aseguradora, dataInicio, dataFim);
		
    	  qtdeVigenteJudicializado+=qtde2;
    	  totalSinistroPagosVigenteJudicializado+=totalSinistroPagos2;
    	  totalSinistroRecuperadosVigenteJudicializado+=totalSinistroRecuperados2;
		
    	  celula = row.createCell(4);
    	  celula.setCellValue(qtde2);
    	  celula.setCellStyle(estiloTexto);
    	  r = new Region(linhaN, (short)4, linhaN, (short)5);
    	  planilha.addMergedRegion(r);
	    
    	  celula = row.createCell(6);
    	  celula.setCellValue(format.format(totalSinistroPagos2));
    	  celula.setCellStyle(estiloTextoD);
    	  r = new Region(linhaN, (short)6, linhaN, (short)7);
    	  planilha.addMergedRegion(r);
	    
    	  celula = row.createCell(8);
    	  celula.setCellValue(format.format(totalSinistroRecuperados2));
    	  celula.setCellStyle(estiloTextoD);
    	  r = new Region(linhaN, (short)8, linhaN, (short)10);
    	  planilha.addMergedRegion(r);
	     
    	  linhaN++;
	    
    	  entrou = true;
      }
		      
      if(!entrou)
    	  linhaN++;
		      
      row = planilha.createRow(linhaN);
      celula = row.createCell(0);
      celula.setCellValue("TOTAL");
      celula.setCellStyle(estiloTextoN);
      r = new Region(linhaN, (short)0, linhaN, (short)3);
      planilha.addMergedRegion(r);
			    
      celula = row.createCell(4);
      celula.setCellValue(qtdeVigenteJudicializado);
      celula.setCellStyle(estiloTextoN);
      r = new Region(linhaN, (short)4, linhaN, (short)5);
      planilha.addMergedRegion(r);
	    
      celula = row.createCell(6);
      celula.setCellValue(format.format(totalSinistroPagosVigenteJudicializado));
      celula.setCellStyle(estiloTextoN_D);
      r = new Region(linhaN, (short)6, linhaN, (short)7);
      planilha.addMergedRegion(r);
	    
      celula = row.createCell(8);
      celula.setCellValue(format.format(totalSinistroRecuperadosVigenteJudicializado));
      celula.setCellStyle(estiloTextoN_D);
      r = new Region(linhaN, (short)8, linhaN, (short)10);
      planilha.addMergedRegion(r);
	    
      linhaN+=2;
		
      row = planilha.createRow(linhaN);
      celula = row.createCell(0);
      celula.setCellValue("No Vigentes Judicializados");
      celula.setCellStyle(estiloTituloE);
      r = new Region(linhaN, (short)0, linhaN, (short)14);
      planilha.addMergedRegion(r);
			    
      linhaN++;
			    
      row = planilha.createRow(linhaN);
      celula = row.createCell(0);
			      
      celula.setCellValue("Sección");
      celula.setCellStyle(estiloTituloTabela);
      r = new Region(linhaN, (short)0, linhaN, (short)3);
      planilha.addMergedRegion(r);
  
      celula = row.createCell(4);
      celula.setCellValue("Cantidad");
      celula.setCellStyle(estiloTituloTabela);
      r = new Region(linhaN, (short)4, linhaN, (short)5);
      planilha.addMergedRegion(r);
  
      celula = row.createCell(6);
      celula.setCellValue("Valor Total (Gs)");
      celula.setCellStyle(estiloTituloTabela);
      r = new Region(linhaN, (short)6, linhaN, (short)7);
      planilha.addMergedRegion(r);
  
      celula = row.createCell(8);
      celula.setCellValue("Valor Pago Aseguradora (Gs)");
      celula.setCellStyle(estiloTituloTabela);
      r = new Region(linhaN, (short)8, linhaN, (short)10);
      planilha.addMergedRegion(r);

      entrou = false;
			    
      int qtdeNoVigenteJudicializado = 0;
      double totalSinistroPagosNoVigenteJudicializado = 0;
      double totalSinistroRecuperadosNoVigenteJudicializado = 0;
				
      linhaN++;
				
      for(String linhaSuja : aseguradora.obterSecoesSinistrosNoVigenteJudicializado(dataInicio, dataFim))
      {
    	  linha = linhaSuja.split(";");
		
    	  secaoId = Long.parseLong(linha[0]); 
    	  qtde2 = Integer.parseInt(linha[1]);
    	  totalSinistroPagos2 = Double.parseDouble(linha[2]);
		
    	  cContas = (ClassificacaoContas) home.obterEntidadePorId(secaoId);
		
    	  row = planilha.createRow(linhaN);
    	  celula = row.createCell(0);
    	  celula.setCellValue(cContas.obterNome());
    	  celula.setCellStyle(estiloTextoE);
    	  r = new Region(linhaN, (short)0, linhaN, (short)3);
    	  planilha.addMergedRegion(r);
		
    	  totalSinistroRecuperados2 = cContas.obterValorSinistrosRecuperadosPorSecaoNoVigenteJudicializado(aseguradora, dataInicio, dataFim);
		
    	  qtdeNoVigenteJudicializado+=qtde2;
    	  totalSinistroPagosNoVigenteJudicializado+=totalSinistroPagos2;
    	  totalSinistroRecuperadosNoVigenteJudicializado+=totalSinistroRecuperados2;
		
    	  celula = row.createCell(4);
    	  celula.setCellValue(qtde2);
    	  celula.setCellStyle(estiloTexto);
    	  r = new Region(linhaN, (short)4, linhaN, (short)5);
    	  planilha.addMergedRegion(r);
	    
    	  celula = row.createCell(6);
    	  celula.setCellValue(format.format(totalSinistroPagos2));
    	  celula.setCellStyle(estiloTextoD);
    	  r = new Region(linhaN, (short)6, linhaN, (short)7);
    	  planilha.addMergedRegion(r);
	    
    	  celula = row.createCell(8);
    	  celula.setCellValue(format.format(totalSinistroRecuperados2));
    	  celula.setCellStyle(estiloTextoD);
    	  r = new Region(linhaN, (short)8, linhaN, (short)10);
    	  planilha.addMergedRegion(r);
	     
    	  linhaN++;
	    
    	  entrou = true;
      }
			      
      if(!entrou)
    	  linhaN++;
			      
      row = planilha.createRow(linhaN);
      celula = row.createCell(0);
      celula.setCellValue("TOTAL");
      celula.setCellStyle(estiloTextoN);
      r = new Region(linhaN, (short)0, linhaN, (short)3);
      planilha.addMergedRegion(r);
    
      celula = row.createCell(4);
      celula.setCellValue(qtdeNoVigenteJudicializado);
      celula.setCellStyle(estiloTextoN);
      r = new Region(linhaN, (short)4, linhaN, (short)5);
      planilha.addMergedRegion(r);
    
      celula = row.createCell(6);
      celula.setCellValue(format.format(totalSinistroPagosNoVigenteJudicializado));
      celula.setCellStyle(estiloTextoN_D);
      r = new Region(linhaN, (short)6, linhaN, (short)7);
      planilha.addMergedRegion(r);
    
      celula = row.createCell(8);
      celula.setCellValue(format.format(totalSinistroRecuperadosNoVigenteJudicializado));
      celula.setCellStyle(estiloTextoN_D);
      r = new Region(linhaN, (short)8, linhaN, (short)10);
      planilha.addMergedRegion(r);
    
      linhaN++;
    
      row = planilha.createRow(linhaN);
      celula = row.createCell(0);
      celula.setCellValue("TOTAL GENERAL");
      celula.setCellStyle(estiloTextoN);
      r = new Region(linhaN, (short)0, linhaN, (short)3);
      planilha.addMergedRegion(r);
    
      celula = row.createCell(4);
      celula.setCellValue(qtdeVigente + qtdeNoVigente + qtdeVigenteJudicializado + qtdeNoVigenteJudicializado);
      celula.setCellStyle(estiloTextoN);
      r = new Region(linhaN, (short)4, linhaN, (short)5);
      planilha.addMergedRegion(r);
    
      celula = row.createCell(6);
      celula.setCellValue(format.format(totalSinistroPagosVigente + totalSinistroPagosNoVigente + totalSinistroPagosVigenteJudicializado + totalSinistroPagosNoVigenteJudicializado));
      celula.setCellStyle(estiloTextoN_D);
      r = new Region(linhaN, (short)6, linhaN, (short)7);
      planilha.addMergedRegion(r);
    
      celula = row.createCell(8);
      celula.setCellValue(format.format(totalSinistroRecuperadosVigente + totalSinistroRecuperadosNoVigente + totalSinistroRecuperadosVigenteJudicializado + totalSinistroRecuperadosNoVigenteJudicializado));
      celula.setCellStyle(estiloTextoN_D);
      r = new Region(linhaN, (short)8, linhaN, (short)10);
      planilha.addMergedRegion(r);
		
      wb.write(stream);

      stream.flush();

      stream.close();
	}
}
