package com.gvs.crm.report;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

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

import infra.config.InfraProperties;

public class ConsolidadoApolicesSinistrosXLS extends Excel
{
	public ConsolidadoApolicesSinistrosXLS(Aseguradora aseguradora, String opcao, String situacaoSeguro, Date dataInicio, Date dataFim,Collection informacoes
			, String textoUsuario) throws Exception
	{
		String caminho = "C:/tmp/" + new Date().getTime() + ".xls";
		
		DecimalFormat format = new DecimalFormat("#,##0.00");
		DecimalFormat formatP = new DecimalFormat("0.0");
		
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
	      
		  celula.setCellValue("Consolidado Pólizas/Sección");
	      celula.setCellStyle(estiloTitulo);
	      
	      Region r = new Region(1, (short)5, 1, (short)14);
	      planilha.addMergedRegion(r);
	      
	      row = planilha.createRow(2);
	      celula = row.createCell(5);
	      if(aseguradora!=null)
	    	  celula.setCellValue("Aseguradora: " + aseguradora.obterNome());
	      else
	    	  celula.setCellValue("Aseguradora: Todas");
	      celula.setCellStyle(estiloTituloE);
	      r = new Region(2, (short)5, 2, (short)14);
	      planilha.addMergedRegion(r);
	      
	      row = planilha.createRow(3);
	      celula = row.createCell(5);
    	  celula.setCellValue("Consolidado por: " + opcao);
	      celula.setCellStyle(estiloTituloE);
	      r = new Region(3, (short)5, 3, (short)14);
	      planilha.addMergedRegion(r);
	      
	      row = planilha.createRow(4);
	      celula = row.createCell(5);
	      if(!situacaoSeguro.equals(""))
	    	  celula.setCellValue("Situacion del Seguro: " + situacaoSeguro);
	      else
	    	  celula.setCellValue("Situacion del Seguro: Todas");
	      celula.setCellStyle(estiloTituloE);
	      r = new Region(4, (short)5, 4, (short)14);
	      planilha.addMergedRegion(r);
	      
	      row = planilha.createRow(5);
	      celula = row.createCell(5);
    	  celula.setCellValue("Periodo: " + new SimpleDateFormat("dd/MM/yyyy").format(dataInicio) + " hasta " + new SimpleDateFormat("dd/MM/yyyy").format(dataFim));
	      celula.setCellStyle(estiloTituloE);
	      r = new Region(5, (short)5, 5, (short)14);
	      planilha.addMergedRegion(r);
	      
	      row = planilha.createRow(7);
	      celula = row.createCell(0);
	      celula.setCellValue("Sección");
	      celula.setCellStyle(estiloTituloTabela);
	      r = new Region(7, (short)0, 7, (short)1);
	      planilha.addMergedRegion(r);
	      
	      celula = row.createCell(2);
	      celula.setCellValue("Modalidad");
	      celula.setCellStyle(estiloTituloTabela);
	      r = new Region(7, (short)2, 7, (short)3);
	      planilha.addMergedRegion(r);
	      
	      celula = row.createCell(4);
	      celula.setCellValue("Cantidad");
	      celula.setCellStyle(estiloTituloTabela);
	      
	      celula = row.createCell(5);
	      celula.setCellValue("%");
	      celula.setCellStyle(estiloTituloTabela);
	      
	      celula = row.createCell(6);
	      celula.setCellValue("Capital GS");
	      celula.setCellStyle(estiloTituloTabela);
	      r = new Region(7, (short)6, 7, (short)7);
	      planilha.addMergedRegion(r);
	      
	      celula = row.createCell(8);
	      if(opcao.equals("Siniestros"))
	    	  celula.setCellValue("Monto GS");
	      else
	    	  celula.setCellValue("Capital ME");
	      celula.setCellStyle(estiloTituloTabela);
	      r = new Region(7, (short)8, 7, (short)9);
	      planilha.addMergedRegion(r);
	      
	      celula = row.createCell(10);
    	  celula.setCellValue("Prima GS");
	      celula.setCellStyle(estiloTituloTabela);
	      r = new Region(7, (short)10, 7, (short)11);
	      planilha.addMergedRegion(r);
	      
	      celula = row.createCell(12);
	      if(opcao.equals("Siniestros"))
	    	  celula.setCellValue("Juducializado GS");
	      else
	    	  celula.setCellValue("Prima ME");
	      celula.setCellStyle(estiloTituloTabela);
	      r = new Region(7, (short)12, 7, (short)13);
	      planilha.addMergedRegion(r);
	      
	      double totalQtde = 0;
	      double totalCapitalGS = 0;
	      double totalCapitalME = 0;
	      double totalPrimaGS = 0;
	      double totalPrimaME = 0;
	      double totalMontanteGS = 0;
	      double totalJuizGS = 0;
		
	      for(Iterator i = informacoes.iterator() ; i.hasNext() ; )
	      {
	    	  String linha = (String) i.next();
			
	    	  String[] linhaArray = linha.split(";");
			
	    	  int qtde = Integer.parseInt(linhaArray[1]);
			
	    	  totalQtde+=qtde;
	      }
	      
	      int linhaExcel = 8;
	      
	      for(Iterator i = informacoes.iterator() ; i.hasNext() ; )
	      {
	    	  String linha = (String) i.next();
				
	    	  String[] linhaArray = linha.split(";");
				
	    	  String secao = linhaArray[0];
	    	  String modalidade = linhaArray[6];
	    	  double qtde = Integer.parseInt(linhaArray[1]);
				
	    	  double capitalGS = 0;
	    	  double capitalME = 0;
	    	  double primaGS = 0;
	    	  double primaME = 0;
	    	  double montanteGS = 0;
	    	  double juizGS = 0;
				
	    	  capitalGS = Double.parseDouble(linhaArray[2]);
	    	  totalCapitalGS+=capitalGS;
	    	  primaGS = Double.parseDouble(linhaArray[4]);
	    	  totalPrimaGS+=primaGS;
				
	    	  if(opcao.equals("Pólizas"))
	    	  {
	    		  capitalME = Double.parseDouble(linhaArray[3]);
	    		  totalCapitalME+=capitalME;
	    		  primaME = Double.parseDouble(linhaArray[5]);
	    		  totalPrimaME+=primaME;
	    	  }
	    	  else
	    	  {
	    		  montanteGS = Double.parseDouble(linhaArray[3]);
	    		  totalMontanteGS+=montanteGS;
	    		  juizGS = Double.parseDouble(linhaArray[5]);
	    		  totalJuizGS+=juizGS;
	    	  }
	    	  
	    	  row = planilha.createRow(linhaExcel);
		      celula = row.createCell(0);
		      celula.setCellValue(secao);
		      celula.setCellStyle(estiloTextoE);
		      r = new Region(linhaExcel, (short)0, linhaExcel, (short)1);
		      planilha.addMergedRegion(r);
		      
		      celula = row.createCell(2);
		      celula.setCellValue(modalidade);
		      celula.setCellStyle(estiloTextoE);
		      r = new Region(linhaExcel, (short)2, linhaExcel, (short)3);
		      planilha.addMergedRegion(r);
		      
		      celula = row.createCell(4);
		      celula.setCellValue(qtde);
		      celula.setCellStyle(estiloTextoD);
		      
		      celula = row.createCell(5);
		      double porcentagem = (qtde * 100) / totalQtde;
		      celula.setCellValue(formatP.format(porcentagem));
		      celula.setCellStyle(estiloTextoD);
		      
		      celula = row.createCell(6);
		      celula.setCellValue(format.format(capitalGS));
		      celula.setCellStyle(estiloTextoD);
		      r = new Region(linhaExcel, (short)6, linhaExcel, (short)7);
		      planilha.addMergedRegion(r);
		      
		      celula = row.createCell(8);
		      if(opcao.equals("Siniestros"))
		    	  celula.setCellValue(format.format(montanteGS));
		      else
		    	  celula.setCellValue(format.format(capitalME));
		      celula.setCellStyle(estiloTextoD);
		      r = new Region(linhaExcel, (short)8, linhaExcel, (short)9);
		      planilha.addMergedRegion(r);
		      
		      celula = row.createCell(10);
	    	  celula.setCellValue(format.format(primaGS));
		      celula.setCellStyle(estiloTextoD);
		      r = new Region(linhaExcel, (short)10, linhaExcel, (short)11);
		      planilha.addMergedRegion(r);
		      
		      celula = row.createCell(12);
		      if(opcao.equals("Siniestros"))
		    	  celula.setCellValue(format.format(juizGS));
		      else
		    	  celula.setCellValue(format.format(primaME));
		      celula.setCellStyle(estiloTextoD);
		      r = new Region(linhaExcel, (short)12, linhaExcel, (short)13);
		      planilha.addMergedRegion(r);
		      
		      linhaExcel++;
	      }
	      
	      row = planilha.createRow(linhaExcel);
	      celula = row.createCell(0);
	      celula.setCellValue("TOTAL");
	      celula.setCellStyle(estiloTextoN);
	      r = new Region(linhaExcel, (short)0, linhaExcel, (short)1);
	      planilha.addMergedRegion(r);
	      
	      celula = row.createCell(4);
	      celula.setCellValue(totalQtde);
	      celula.setCellStyle(estiloTextoN_D);
	      
	      celula = row.createCell(6);
	      celula.setCellValue(format.format(totalCapitalGS));
	      celula.setCellStyle(estiloTextoN_D);
	      r = new Region(linhaExcel, (short)6, linhaExcel, (short)7);
	      planilha.addMergedRegion(r);
	      
	      celula = row.createCell(8);
	      if(opcao.equals("Siniestros"))
	    	  celula.setCellValue(format.format(totalMontanteGS));
	      else
	    	  celula.setCellValue(format.format(totalCapitalME));
	      celula.setCellStyle(estiloTextoN_D);
	      r = new Region(linhaExcel, (short)8, linhaExcel, (short)9);
	      planilha.addMergedRegion(r);
	      
	      celula = row.createCell(10);
    	  celula.setCellValue(format.format(totalPrimaGS));
	      celula.setCellStyle(estiloTextoN_D);
	      r = new Region(linhaExcel, (short)10, linhaExcel, (short)11);
	      planilha.addMergedRegion(r);
	      
	      celula = row.createCell(12);
	      if(opcao.equals("Siniestros"))
	    	  celula.setCellValue(format.format(totalJuizGS));
	      else
	    	  celula.setCellValue(format.format(totalPrimaME));
	      celula.setCellStyle(estiloTextoN_D);
	      r = new Region(linhaExcel, (short)12, linhaExcel, (short)13);
	      planilha.addMergedRegion(r);
	      
	      linhaExcel+=2;
	      row = planilha.createRow(linhaExcel);
	      celula = row.createCell(0);
    	  celula.setCellValue(textoUsuario);
	      celula.setCellStyle(estiloTituloE);
	      r = new Region(linhaExcel, (short)0, linhaExcel, (short)14);
	      planilha.addMergedRegion(r);
	      
	      wb.write(stream);
	
	      stream.flush();
	
	      stream.close();
	}
}
