package com.gvs.crm.report;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
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
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.hssf.util.Region;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.util.IOUtils;

import com.gvs.crm.model.ApoliceHome;

import infra.config.InfraProperties;

public class AnualidadeApolicesSinistrosXLS extends Excel
{
	private Calendar calendarInicio = null;
	private Calendar calendarFim = null;
	
	private Date dataInicio = new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2006 00:00:00");
	private Date dataFim = new SimpleDateFormat("dd/MM/yyyy").parse("31/12/2006 23:59:59");
	
	private Date dataInicio2 = new SimpleDateFormat("dd/MM/yyyy").parse("01/07/2006 00:00:00");
	private Date dataFim2 = new SimpleDateFormat("dd/MM/yyyy").parse("30/06/2007 23:59:59");
	private String opcao = "";
	
	public AnualidadeApolicesSinistrosXLS(String situacaoSeguro, ApoliceHome home, String textoUsuario, String opcao, boolean userAdmin) throws Exception
	{
		this.opcao = opcao;
		String caminho = "C:/tmp/" + new Date().getTime() + ".xls";
		
		DecimalFormat format = new DecimalFormat("#,##0.00");
		DecimalFormat formatP = new DecimalFormat("0.0");
		
		FileOutputStream stream = new FileOutputStream(caminho);
		
		this.setCaminho(caminho);
		
		String[] tituloAno = new String[]{"2006", "2007", "2008", "2009", "2010", "2011", "2012", "2013", "2014"};
		String[] tituloFiscal = new String[]{"2006-2007", "2007-2008", "2008-209", "2009-2010", "2010-2011", "2011-2012", "2012-2013", "2013-2014", "2014-2015"};
		String[] titulo = new String[]{};
		
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
	      
		  celula.setCellValue("Histórico Pólizas/Siniestros");
	      celula.setCellStyle(estiloTitulo);
	      
	      Region r = new Region(1, (short)5, 1, (short)14);
	      planilha.addMergedRegion(r);
	      
	      row = planilha.createRow(2);
	      celula = row.createCell(5);
	      if(situacaoSeguro.equals(""))
	    	  celula.setCellValue("Situacion del Seguro: Todas");
	      else
	    	  celula.setCellValue("Situacion del Seguro: " + situacaoSeguro);
	      celula.setCellStyle(estiloTituloE);
	      r = new Region(2, (short)5, 2, (short)14);
	      planilha.addMergedRegion(r);
	      
	      row = planilha.createRow(6);
	      celula = row.createCell(0);
    	  celula.setCellValue("Cantidad de Pólizas emitidas");
	      celula.setCellStyle(estiloTituloE);
	      r = new Region(6, (short)0, 6, (short)14);
	      planilha.addMergedRegion(r);
	      
	      row = planilha.createRow(7);
	      celula = row.createCell(0);
    	  celula.setCellValue("Sección");
	      celula.setCellStyle(estiloTituloTabela);
	      r = new Region(7, (short)0, 7, (short)3);
	      planilha.addMergedRegion(r);
	      
	      int cel = 4;
	      
	      if(opcao.equals("ano"))
	    	  titulo = tituloAno;
	      else
	    	  titulo = tituloFiscal;
			
	      for(int i = 0 ; i < titulo.length ; i++)
	      {
	    	  String t = titulo[i];
			
	    	  celula = row.createCell(cel);
	    	  celula.setCellValue(t);
	    	  celula.setCellStyle(estiloTituloTabela);
	    	  r = new Region(7, (short)cel, 7, (short)++cel);
	    	  planilha.addMergedRegion(r);
		     
	    	  ++cel;
	      }
	      
	      int total2006 = 0;
	      int total2007 = 0;
	      int total2008 = 0;
	      int total2009 = 0;
	      int total2010 = 0;
	      int total2011 = 0;
	      int total2012 = 0;
	      int total2013 = 0;
	      int total2014 = 0;
	      
	      Collection secoes = new ArrayList();
	      secoes = home.obterSecoes(userAdmin);
			
	      this.AnoMaisUm();
			
	      	Map ano2006 = new TreeMap();
			ano2006 = home.obterQtdeApolicesAnualPorSecao(calendarInicio.getTime(), calendarFim.getTime(), situacaoSeguro, userAdmin);
			
			this.AnoMaisUm();
			
			Map ano2007 = new TreeMap();
			ano2007 = home.obterQtdeApolicesAnualPorSecao(calendarInicio.getTime(), calendarFim.getTime(), situacaoSeguro, userAdmin);
			
			this.AnoMaisUm();
			
			Map ano2008 = new TreeMap();
			ano2008 = home.obterQtdeApolicesAnualPorSecao(calendarInicio.getTime(), calendarFim.getTime(), situacaoSeguro, userAdmin);
			
			this.AnoMaisUm();
			
			Map ano2009 = new TreeMap();
			ano2009 = home.obterQtdeApolicesAnualPorSecao(calendarInicio.getTime(), calendarFim.getTime(), situacaoSeguro, userAdmin);
			
			this.AnoMaisUm();
			
			Map ano2010 = new TreeMap();
			ano2010 = home.obterQtdeApolicesAnualPorSecao(calendarInicio.getTime(), calendarFim.getTime(), situacaoSeguro, userAdmin);
			
			this.AnoMaisUm();
			
			Map ano2011 = new TreeMap();
			ano2011 = home.obterQtdeApolicesAnualPorSecao(calendarInicio.getTime(), calendarFim.getTime(), situacaoSeguro, userAdmin);
			
			this.AnoMaisUm();
			
			Map ano2012 = new TreeMap();
			ano2012 = home.obterQtdeApolicesAnualPorSecao(calendarInicio.getTime(), calendarFim.getTime(), situacaoSeguro, userAdmin);
			
			this.AnoMaisUm();
			
			Map ano2013 = new TreeMap();
			ano2013 = home.obterQtdeApolicesAnualPorSecao(calendarInicio.getTime(), calendarFim.getTime(), situacaoSeguro, userAdmin);
			
			this.AnoMaisUm();
			
			Map ano2014 = new TreeMap();
			ano2014 = home.obterQtdeApolicesAnualPorSecao(calendarInicio.getTime(), calendarFim.getTime(), situacaoSeguro, userAdmin);
			
			int linha = 8;
			
			for(Iterator i = secoes.iterator() ; i.hasNext() ; )
			{
				String secao = (String) i.next();
				
				 row = planilha.createRow(linha);
			     celula = row.createCell(0);
		    	 celula.setCellValue(secao);
			     celula.setCellStyle(estiloTextoE);
			     r = new Region(linha, (short)0, linha, (short)3);
			     planilha.addMergedRegion(r);
			      
			    celula = row.createCell(4);
			    celula.setCellStyle(estiloTextoD);
			    
			    int qtde = 0;
			    
			    if(ano2006.containsKey(secao))
				{
					qtde = Integer.parseInt(ano2006.get(secao).toString());
					celula.setCellValue(qtde);
					total2006+=qtde;
				}
				else
					celula.setCellValue(qtde);
			    
			    r = new Region(linha, (short)4, linha, (short)5);
			    planilha.addMergedRegion(r);
				
			    qtde = 0;
			    celula = row.createCell(6);
			    celula.setCellStyle(estiloTextoD);
			    
				if(ano2007.containsKey(secao))
				{
					qtde = Integer.parseInt(ano2007.get(secao).toString());
					celula.setCellValue(qtde);
					total2007+=qtde;
				}
				else
					celula.setCellValue(qtde);
				
				r = new Region(linha, (short)6, linha, (short)7);
			    planilha.addMergedRegion(r);
				
				qtde = 0;
			    celula = row.createCell(8);
			    celula.setCellStyle(estiloTextoD);
					
				if(ano2008.containsKey(secao))
				{
					qtde = Integer.parseInt(ano2008.get(secao).toString());
					celula.setCellValue(qtde);
					total2008+=qtde;
				}
				else
					celula.setCellValue(qtde);
				
				r = new Region(linha, (short)8, linha, (short)9);
			    planilha.addMergedRegion(r);
				
			    qtde = 0;
			    celula = row.createCell(10);
			    celula.setCellStyle(estiloTextoD);
					
				if(ano2009.containsKey(secao))
				{
					qtde = Integer.parseInt(ano2009.get(secao).toString());
					celula.setCellValue(qtde);
					total2009+=qtde;
				}
				else
					celula.setCellValue(qtde);
				
				r = new Region(linha, (short)10, linha, (short)11);
			    planilha.addMergedRegion(r);
			    
			    qtde = 0;
			    celula = row.createCell(12);
			    celula.setCellStyle(estiloTextoD);
				
				if(ano2010.containsKey(secao))
				{
					qtde = Integer.parseInt(ano2010.get(secao).toString());
					celula.setCellValue(qtde);
					total2010+=qtde;
				}
				else
					celula.setCellValue(qtde);
				
				r = new Region(linha, (short)12, linha, (short)13);
			    planilha.addMergedRegion(r);
				
			    qtde = 0;
			    celula = row.createCell(14);
			    celula.setCellStyle(estiloTextoD);
				
				if(ano2011.containsKey(secao))
				{
					qtde = Integer.parseInt(ano2011.get(secao).toString());
					celula.setCellValue(qtde);
					total2011+=qtde;
				}
				else
					celula.setCellValue(qtde);
				
				r = new Region(linha, (short)14, linha, (short)15);
			    planilha.addMergedRegion(r);
			    
			    qtde = 0;
			    celula = row.createCell(16);
			    celula.setCellStyle(estiloTextoD);
				
				if(ano2012.containsKey(secao))
				{
					qtde = Integer.parseInt(ano2012.get(secao).toString());
					celula.setCellValue(qtde);
					total2012+=qtde;
				}
				else
					celula.setCellValue(qtde);
				
				r = new Region(linha, (short)16, linha, (short)17);
			    planilha.addMergedRegion(r);
			    
			    qtde = 0;
			    celula = row.createCell(18);
			    celula.setCellStyle(estiloTextoD);
				
				if(ano2013.containsKey(secao))
				{
					qtde = Integer.parseInt(ano2013.get(secao).toString());
					celula.setCellValue(qtde);
					total2013+=qtde;
				}
				else
					celula.setCellValue(qtde);
				
				r = new Region(linha, (short)18, linha, (short)19);
			    planilha.addMergedRegion(r);
			    
			    qtde = 0;
			    celula = row.createCell(20);
			    celula.setCellStyle(estiloTextoD);
				
				if(ano2014.containsKey(secao))
				{
					qtde = Integer.parseInt(ano2014.get(secao).toString());
					celula.setCellValue(qtde);
					total2014+=qtde;
				}
				else
					celula.setCellValue(qtde);
				
				r = new Region(linha, (short)20, linha, (short)21);
			    planilha.addMergedRegion(r);
			    
			    linha++;
			}
			
			row = planilha.createRow(linha);
		    celula = row.createCell(0);
	    	celula.setCellValue("TOTAL");
		    celula.setCellStyle(estiloTextoN);
		    r = new Region(linha, (short)0, linha, (short)3);
		    planilha.addMergedRegion(r);
		    
		    celula = row.createCell(4);
	    	celula.setCellValue(total2006);
		    celula.setCellStyle(estiloTextoN_D);
		    r = new Region(linha, (short)4, linha, (short)5);
		    planilha.addMergedRegion(r);
		    
		    celula = row.createCell(6);
	    	celula.setCellValue(total2007);
		    celula.setCellStyle(estiloTextoN_D);
		    r = new Region(linha, (short)6, linha, (short)7);
		    planilha.addMergedRegion(r);
		    
		    celula = row.createCell(8);
	    	celula.setCellValue(total2008);
		    celula.setCellStyle(estiloTextoN_D);
		    r = new Region(linha, (short)8, linha, (short)9);
		    planilha.addMergedRegion(r);
		    
		    celula = row.createCell(10);
	    	celula.setCellValue(total2009);
		    celula.setCellStyle(estiloTextoN_D);
		    r = new Region(linha, (short)10, linha, (short)11);
		    planilha.addMergedRegion(r);
		    
		    celula = row.createCell(12);
	    	celula.setCellValue(total2010);
		    celula.setCellStyle(estiloTextoN_D);
		    r = new Region(linha, (short)12, linha, (short)13);
		    planilha.addMergedRegion(r);
		    
		    celula = row.createCell(14);
	    	celula.setCellValue(total2011);
		    celula.setCellStyle(estiloTextoN_D);
		    r = new Region(linha, (short)14, linha, (short)15);
		    planilha.addMergedRegion(r);
		    
		    celula = row.createCell(16);
	    	celula.setCellValue(total2012);
		    celula.setCellStyle(estiloTextoN_D);
		    r = new Region(linha, (short)16, linha, (short)17);
		    planilha.addMergedRegion(r);
		    
		    celula = row.createCell(18);
	    	celula.setCellValue(total2013);
		    celula.setCellStyle(estiloTextoN_D);
		    r = new Region(linha, (short)18, linha, (short)19);
		    planilha.addMergedRegion(r);
		    
		    celula = row.createCell(20);
	    	celula.setCellValue(total2014);
		    celula.setCellStyle(estiloTextoN_D);
		    r = new Region(linha, (short)20, linha, (short)21);
		    planilha.addMergedRegion(r);
			
			total2006 = 0;
			total2007 = 0;
			total2008 = 0;
			total2009 = 0;
			total2010 = 0;
			total2011 = 0;
			total2012 = 0;
			total2013 = 0;
			total2014 = 0;
			
			linha+=2;
			
			 row = planilha.createRow(linha);
	      celula = row.createCell(0);
    	  celula.setCellValue("Cantidad de Siniestros ocurridos");
	      celula.setCellStyle(estiloTituloE);
	      r = new Region(linha, (short)0, linha, (short)17);
	      planilha.addMergedRegion(r);
	      
	      linha++;
	      
	      row = planilha.createRow(linha);
	      celula = row.createCell(0);
    	  celula.setCellValue("Sección");
	      celula.setCellStyle(estiloTituloTabela);
	      r = new Region(linha, (short)0, linha, (short)3);
	      planilha.addMergedRegion(r);
	      
	      cel = 4;
			
	      for(int i = 0 ; i < titulo.length ; i++)
	      {
	    	  String t = titulo[i];
	    	  
	    	  celula = row.createCell(cel);
	    	  celula.setCellValue(t);
	    	  celula.setCellStyle(estiloTituloTabela);
	    	  r = new Region(linha, (short)cel, linha, (short)++cel);
	    	  planilha.addMergedRegion(r);
	    	  
	    	  ++cel;
	      }
	      
	      linha++;
	      
	      this.AnoMaisUm();
				
			ano2006 = new TreeMap();
			ano2006 = home.obterQtdeSinistrosAnualPorSecao(calendarInicio.getTime(), calendarFim.getTime(), situacaoSeguro, userAdmin);
			
			this.AnoMaisUm();
			
			ano2007 = new TreeMap();
			ano2007 = home.obterQtdeSinistrosAnualPorSecao(calendarInicio.getTime(), calendarFim.getTime(), situacaoSeguro, userAdmin);
			
			this.AnoMaisUm();
			
			ano2008 = new TreeMap();
			ano2008 = home.obterQtdeSinistrosAnualPorSecao(calendarInicio.getTime(), calendarFim.getTime(), situacaoSeguro, userAdmin);
			
			this.AnoMaisUm();
			
			ano2009 = new TreeMap();
			ano2009 = home.obterQtdeSinistrosAnualPorSecao(calendarInicio.getTime(), calendarFim.getTime(), situacaoSeguro, userAdmin);
			
			this.AnoMaisUm();
			
			ano2010 = new TreeMap();
			ano2010 = home.obterQtdeSinistrosAnualPorSecao(calendarInicio.getTime(), calendarFim.getTime(), situacaoSeguro, userAdmin);
			
			this.AnoMaisUm();
			
			ano2011 = new TreeMap();
			ano2011 = home.obterQtdeSinistrosAnualPorSecao(calendarInicio.getTime(), calendarFim.getTime(), situacaoSeguro, userAdmin);
			
			this.AnoMaisUm();
			
			ano2012 = new TreeMap();
			ano2012 = home.obterQtdeSinistrosAnualPorSecao(calendarInicio.getTime(), calendarFim.getTime(), situacaoSeguro, userAdmin);
			
			this.AnoMaisUm();
			
			ano2013 = new TreeMap();
			ano2013 = home.obterQtdeSinistrosAnualPorSecao(calendarInicio.getTime(), calendarFim.getTime(), situacaoSeguro, userAdmin);
			
			this.AnoMaisUm();
			
			ano2014 = new TreeMap();
			ano2014 = home.obterQtdeSinistrosAnualPorSecao(calendarInicio.getTime(), calendarFim.getTime(), situacaoSeguro, userAdmin);
			
			for(Iterator i = secoes.iterator() ; i.hasNext() ; )
			{
				String secao = (String) i.next();
				
				 row = planilha.createRow(linha);
			     celula = row.createCell(0);
		    	 celula.setCellValue(secao);
			     celula.setCellStyle(estiloTextoE);
			     r = new Region(linha, (short)0, linha, (short)3);
			     planilha.addMergedRegion(r);
			      
			    celula = row.createCell(4);
			    celula.setCellStyle(estiloTextoD);
			    
			    int qtde = 0;
			    
			    if(ano2006.containsKey(secao))
				{
					qtde = Integer.parseInt(ano2006.get(secao).toString());
					celula.setCellValue(qtde);
					total2006+=qtde;
				}
				else
					celula.setCellValue(qtde);
			    
			    r = new Region(linha, (short)4, linha, (short)5);
			    planilha.addMergedRegion(r);
				
			    qtde = 0;
			    celula = row.createCell(6);
			    celula.setCellStyle(estiloTextoD);
			    
				if(ano2007.containsKey(secao))
				{
					qtde = Integer.parseInt(ano2007.get(secao).toString());
					celula.setCellValue(qtde);
					total2007+=qtde;
				}
				else
					celula.setCellValue(qtde);
				
				r = new Region(linha, (short)6, linha, (short)7);
			    planilha.addMergedRegion(r);
				
				qtde = 0;
			    celula = row.createCell(8);
			    celula.setCellStyle(estiloTextoD);
					
				if(ano2008.containsKey(secao))
				{
					qtde = Integer.parseInt(ano2008.get(secao).toString());
					celula.setCellValue(qtde);
					total2008+=qtde;
				}
				else
					celula.setCellValue(qtde);
				
				r = new Region(linha, (short)8, linha, (short)9);
			    planilha.addMergedRegion(r);
				
			    qtde = 0;
			    celula = row.createCell(10);
			    celula.setCellStyle(estiloTextoD);
					
				if(ano2009.containsKey(secao))
				{
					qtde = Integer.parseInt(ano2009.get(secao).toString());
					celula.setCellValue(qtde);
					total2009+=qtde;
				}
				else
					celula.setCellValue(qtde);
				
				r = new Region(linha, (short)10, linha, (short)11);
			    planilha.addMergedRegion(r);
			    
			    qtde = 0;
			    celula = row.createCell(12);
			    celula.setCellStyle(estiloTextoD);
				
				if(ano2010.containsKey(secao))
				{
					qtde = Integer.parseInt(ano2010.get(secao).toString());
					celula.setCellValue(qtde);
					total2010+=qtde;
				}
				else
					celula.setCellValue(qtde);
				
				r = new Region(linha, (short)12, linha, (short)13);
			    planilha.addMergedRegion(r);
				
			    qtde = 0;
			    celula = row.createCell(14);
			    celula.setCellStyle(estiloTextoD);
				
				if(ano2011.containsKey(secao))
				{
					qtde = Integer.parseInt(ano2011.get(secao).toString());
					celula.setCellValue(qtde);
					total2011+=qtde;
				}
				else
					celula.setCellValue(qtde);
				
				r = new Region(linha, (short)14, linha, (short)15);
			    planilha.addMergedRegion(r);
			    
			    qtde = 0;
			    celula = row.createCell(16);
			    celula.setCellStyle(estiloTextoD);
				
				if(ano2012.containsKey(secao))
				{
					qtde = Integer.parseInt(ano2012.get(secao).toString());
					celula.setCellValue(qtde);
					total2012+=qtde;
				}
				else
					celula.setCellValue(qtde);
				
				r = new Region(linha, (short)16, linha, (short)17);
			    planilha.addMergedRegion(r);
			    
			    qtde = 0;
			    celula = row.createCell(18);
			    celula.setCellStyle(estiloTextoD);
				
				if(ano2013.containsKey(secao))
				{
					qtde = Integer.parseInt(ano2013.get(secao).toString());
					celula.setCellValue(qtde);
					total2013+=qtde;
				}
				else
					celula.setCellValue(qtde);
				
				r = new Region(linha, (short)18, linha, (short)19);
			    planilha.addMergedRegion(r);
			    
			    qtde = 0;
			    celula = row.createCell(20);
			    celula.setCellStyle(estiloTextoD);
				
				if(ano2014.containsKey(secao))
				{
					qtde = Integer.parseInt(ano2014.get(secao).toString());
					celula.setCellValue(qtde);
					total2014+=qtde;
				}
				else
					celula.setCellValue(qtde);
				
				r = new Region(linha, (short)20, linha, (short)21);
			    planilha.addMergedRegion(r);
			    
			    linha++;
			}
			
			row = planilha.createRow(linha);
			celula = row.createCell(0);
	    	celula.setCellValue("TOTAL");
		    celula.setCellStyle(estiloTextoN);
		    r = new Region(linha, (short)0, linha, (short)3);
		    planilha.addMergedRegion(r);
		    
		    celula = row.createCell(4);
	    	celula.setCellValue(total2006);
		    celula.setCellStyle(estiloTextoN_D);
		    r = new Region(linha, (short)4, linha, (short)5);
		    planilha.addMergedRegion(r);
		    
		    celula = row.createCell(6);
	    	celula.setCellValue(total2007);
		    celula.setCellStyle(estiloTextoN_D);
		    r = new Region(linha, (short)6, linha, (short)7);
		    planilha.addMergedRegion(r);
		    
		    celula = row.createCell(8);
	    	celula.setCellValue(total2008);
		    celula.setCellStyle(estiloTextoN_D);
		    r = new Region(linha, (short)8, linha, (short)9);
		    planilha.addMergedRegion(r);
		    
		    celula = row.createCell(10);
	    	celula.setCellValue(total2009);
		    celula.setCellStyle(estiloTextoN_D);
		    r = new Region(linha, (short)10, linha, (short)11);
		    planilha.addMergedRegion(r);
		    
		    celula = row.createCell(12);
	    	celula.setCellValue(total2010);
		    celula.setCellStyle(estiloTextoN_D);
		    r = new Region(linha, (short)12, linha, (short)13);
		    planilha.addMergedRegion(r);
		    
		    celula = row.createCell(14);
	    	celula.setCellValue(total2011);
		    celula.setCellStyle(estiloTextoN_D);
		    r = new Region(linha, (short)14, linha, (short)15);
		    planilha.addMergedRegion(r);
		    
		    celula = row.createCell(16);
	    	celula.setCellValue(total2012);
		    celula.setCellStyle(estiloTextoN_D);
		    r = new Region(linha, (short)16, linha, (short)17);
		    planilha.addMergedRegion(r);
		    
		    celula = row.createCell(18);
	    	celula.setCellValue(total2013);
		    celula.setCellStyle(estiloTextoN_D);
		    r = new Region(linha, (short)18, linha, (short)19);
		    planilha.addMergedRegion(r);
		    
		    celula = row.createCell(20);
	    	celula.setCellValue(total2014);
		    celula.setCellStyle(estiloTextoN_D);
		    r = new Region(linha, (short)20, linha, (short)21);
		    planilha.addMergedRegion(r);
		    
		    linha+=2;
		    row = planilha.createRow(linha);
		    celula = row.createCell(0);
	    	celula.setCellValue(textoUsuario);
		    celula.setCellStyle(estiloTituloE);
		    r = new Region(linha, (short)5, linha, (short)14);
		    planilha.addMergedRegion(r);
	      
	      wb.write(stream);
	  	
	      stream.flush();
	
	      stream.close();
	}
	
	private void AnoMaisUm() throws Exception
	{
		if(calendarInicio == null)
		{
			this.calendarInicio = Calendar.getInstance();
			this.calendarFim = Calendar.getInstance();
		
			if(opcao.equals("ano"))
			{
				calendarInicio.setTime(dataInicio);
				calendarFim.setTime(dataFim);
			}
			else
			{
				calendarInicio.setTime(dataInicio2);
				calendarFim.setTime(dataFim2);
			}
		}
		else
		{
			String ano = new SimpleDateFormat("yyyy").format(this.calendarInicio.getTime()); 
			/*String anoAtual = new SimpleDateFormat("yyyy").format(new Date());
			String ano = new SimpleDateFormat("yyyy").format(this.calendarInicio.getTime());*/
			
			if(ano.equals("2014"))
			{
				if(opcao.equals("ano"))
				{
					calendarInicio.setTime(dataInicio);
					calendarFim.setTime(dataFim);
				}
				else
				{
					calendarInicio.setTime(dataInicio2);
					calendarFim.setTime(dataFim2);
				}
			}
			else
			{
				calendarInicio.add(Calendar.YEAR, 1);
				calendarFim.add(Calendar.YEAR, 1);
			}
		}
	}
}
