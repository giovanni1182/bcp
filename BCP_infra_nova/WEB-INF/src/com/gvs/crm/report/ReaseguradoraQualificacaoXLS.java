package com.gvs.crm.report;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.hssf.util.Region;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.util.IOUtils;

import com.gvs.crm.model.Reaseguradora;

import infra.config.InfraProperties;

public class ReaseguradoraQualificacaoXLS extends Excel
{
	public ReaseguradoraQualificacaoXLS(Collection<Reaseguradora> reaseguradoras, Date data, String textoUsuario) throws Exception
	{
		String caminho = "C:/tmp/" + new Date().getTime() + ".xls";
		
		 String ano = new SimpleDateFormat("yyyy").format(data);
	     String mes = this.getMesExtenso(data);
		
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
      
      HSSFCellStyle estiloData = wb.createCellStyle();
      estiloData.setDataFormat(HSSFDataFormat.getBuiltinFormat("m/d/yy"));
      
      HSSFCellStyle estiloTitulo = wb.createCellStyle();
      estiloTitulo.setAlignment(HSSFCellStyle.ALIGN_CENTER);
      estiloTitulo.setFont(fonteTitulo);
      
      HSSFCellStyle estiloTituloE = wb.createCellStyle();
      estiloTituloE.setFont(fonteTitulo);
      
      HSSFCellStyle estiloTexto = wb.createCellStyle();
      estiloTexto.setAlignment(HSSFCellStyle.ALIGN_CENTER);
      estiloTexto.setFont(fonteTexto);
      
      HSSFCellStyle estiloTextoJ = wb.createCellStyle();
      estiloTextoJ.setAlignment(HSSFCellStyle.ALIGN_JUSTIFY);
      estiloTextoJ.setFont(fonteTexto);
      
      HSSFFont fonteTextoN = wb.createFont();
      fonteTextoN.setFontHeightInPoints((short)8);
      fonteTextoN.setFontName("Arial");
      fonteTextoN.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
      
      HSSFCellStyle estiloTextoN = wb.createCellStyle();
      estiloTextoN.setFont(fonteTextoN);
      
      HSSFCellStyle estiloTextoCor = wb.createCellStyle();
      estiloTextoCor.setAlignment(HSSFCellStyle.ALIGN_CENTER);
      estiloTextoCor.setFont(fonteTexto);
      estiloTextoCor.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
      estiloTextoCor.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
      
      HSSFCellStyle estiloTextoE = wb.createCellStyle();
      estiloTextoE.setAlignment(HSSFCellStyle.ALIGN_LEFT);
      estiloTextoE.setFont(fonteTexto);
      
      HSSFCellStyle estiloTextoCorE = wb.createCellStyle();
      estiloTextoCorE.setAlignment(HSSFCellStyle.ALIGN_LEFT);
      estiloTextoCorE.setFont(fonteTexto);
      estiloTextoCorE.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
      estiloTextoCorE.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
      
      HSSFCellStyle estiloTituloTabela = wb.createCellStyle();
      estiloTituloTabela.setAlignment(HSSFCellStyle.ALIGN_CENTER);
      estiloTituloTabela.setFont(fonteTituloTabela);
      estiloTituloTabela.setFillForegroundColor(HSSFColor.GREY_80_PERCENT.index);
      estiloTituloTabela.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
      
      String dirImages = InfraProperties.getInstance().getProperty("report.images.url2");
      
      //String dirImages = "D:/Projetos/BCP_infra_nova/images";
      
      InputStream is = new FileInputStream(dirImages + "/bcp.jpg");
      byte [] bytes = IOUtils.toByteArray (is);
      int pictureIdx = wb.addPicture(bytes, Workbook.PICTURE_TYPE_JPEG); 
      is.close();
      
      HSSFClientAnchor anchoVivaBem = new HSSFClientAnchor(0,0,0, 0, (short) 0, 0, (short)3 , 5);  
      anchoVivaBem.setAnchorType(3);  
      planilha.createDrawingPatriarch().createPicture(anchoVivaBem, pictureIdx);
      
      HSSFRow row = planilha.createRow(6);
      HSSFCell celula = row.createCell(0);
      
      celula.setCellValue("SUPERINTENDENCIA DE SEGUROS");
      celula.setCellStyle(estiloTitulo);
      
      Region r = new Region(6, (short)0, 6, (short)13);
      planilha.addMergedRegion(r);
      
      row = planilha.createRow(7);
      celula = row.createCell(0);
      
      celula.setCellValue("INTENDENCIA DE CONTROL FINANCIERO");
      celula.setCellStyle(estiloTitulo);
      
      r = new Region(7, (short)0, 7, (short)13);
      planilha.addMergedRegion(r);
      
      row = planilha.createRow(8);
      celula = row.createCell(0);
      
      celula.setCellValue("REGISTRO DE REASEGURADORAS");
      celula.setCellStyle(estiloTitulo);
      r = new Region(8, (short)0, 8, (short)13);
      planilha.addMergedRegion(r);
      
      row = planilha.createRow(9);
      celula = row.createCell(0);
      celula.setCellValue("FECHA " + new SimpleDateFormat("dd/MM/yyyy").format(data));
      celula.setCellStyle(estiloTitulo);
      r = new Region(9, (short)0, 9, (short)13);
      planilha.addMergedRegion(r);
      
      int linha = 11;
      
      for(Iterator<Reaseguradora> i = reaseguradoras.iterator() ; i.hasNext() ; )
      {
    	  Reaseguradora reaseguradora = (Reaseguradora) i.next();
    	  
    	  row = planilha.createRow(linha);
          celula = row.createCell(0);
          celula.setCellValue(reaseguradora.obterNome());
          celula.setCellStyle(estiloTituloE);
          r = new Region(linha, (short)0, linha, (short)13);
          planilha.addMergedRegion(r);
          
          linha++;
          
          row = planilha.createRow(linha);
          celula = row.createCell(0);
          celula.setCellValue("Calificación");
          celula.setCellStyle(estiloTextoCor);
          r = new Region(linha, (short)0, linha, (short)1);
          planilha.addMergedRegion(r);
          
          celula = row.createCell(2);
          celula.setCellValue("Nivel");
          celula.setCellStyle(estiloTextoCor);
          r = new Region(linha, (short)2, linha, (short)3);
          planilha.addMergedRegion(r);
          
          celula = row.createCell(4);
          celula.setCellValue("Cod. Calificadora");
          celula.setCellStyle(estiloTextoCor);
          r = new Region(linha, (short)4, linha, (short)5);
          planilha.addMergedRegion(r);
          
          celula = row.createCell(6);
          celula.setCellValue("Calificadora");
          celula.setCellStyle(estiloTextoCor);
          r = new Region(linha, (short)6, linha, (short)8);
          planilha.addMergedRegion(r);
          
          celula = row.createCell(9);
          celula.setCellValue("Fecha de la Calificación");
          celula.setCellStyle(estiloTextoCor);
          r = new Region(linha, (short)9, linha, (short)11);
          planilha.addMergedRegion(r);
          
          linha++;
          
          for(Iterator j = reaseguradora.obterClassificacoes().iterator() ; j.hasNext() ; )
	      {
        	  Reaseguradora.Classificacao classificacao = (Reaseguradora.Classificacao) j.next();
        	  
        	  row = planilha.createRow(linha);
              celula = row.createCell(0);
              celula.setCellValue(classificacao.obterClassificacao());
              celula.setCellStyle(estiloTextoE);
              r = new Region(linha, (short)0, linha, (short)1);
              planilha.addMergedRegion(r);
              
              celula = row.createCell(2);
              celula.setCellValue(classificacao.obterNivel());
              celula.setCellStyle(estiloTextoE);
              r = new Region(linha, (short)2, linha, (short)3);
              planilha.addMergedRegion(r);
              
              celula = row.createCell(4);
              celula.setCellValue(classificacao.obterCodigo());
              celula.setCellStyle(estiloTextoE);
              r = new Region(linha, (short)4, linha, (short)5);
              planilha.addMergedRegion(r);
              
              celula = row.createCell(6);
              celula.setCellValue(classificacao.obterQualificacao());
              celula.setCellStyle(estiloTextoE);
              r = new Region(linha, (short)6, linha, (short)8);
              planilha.addMergedRegion(r);
              
              celula = row.createCell(9);
              celula.setCellValue(new SimpleDateFormat("dd/MM/yyyy").format(classificacao.obterData()));
              celula.setCellStyle(estiloTexto);
              r = new Region(linha, (short)9, linha, (short)11);
              planilha.addMergedRegion(r);
              
              linha++;
	      }
      }
      
      linha++;
      row = planilha.createRow(linha);
      celula = row.createCell(0);
      celula.setCellValue("Elaborado por: ICORAS - Intendencia de Control de Operaciones de Reaseguros y Auxiliares del Seguro.");
      celula.setCellStyle(estiloTextoN);
      r = new Region(linha, (short)0, linha, (short)9);
      planilha.addMergedRegion(r);
      
      linha+=2;
      row = planilha.createRow(linha);
      celula = row.createCell(0);
      celula.setCellValue(textoUsuario);
      celula.setCellStyle(estiloTextoN);
      r = new Region(linha, (short)0, linha, (short)9);
      planilha.addMergedRegion(r);
      
      wb.write(stream);

      stream.flush();

      stream.close();
	}
}
