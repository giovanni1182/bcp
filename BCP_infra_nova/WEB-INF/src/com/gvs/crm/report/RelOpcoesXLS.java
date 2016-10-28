package com.gvs.crm.report;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
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

import com.gvs.crm.model.OpcaoHome;
import com.gvs.crm.model.Usuario;

import infra.config.InfraProperties;

public class RelOpcoesXLS extends Excel
{
	public RelOpcoesXLS(Usuario usuario, int opcao, Collection dados, OpcaoHome home) throws Exception
	{
		String caminho = "C:/tmp/" + new Date().getTime() + ".xls";
		
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
	      
	      HSSFCellStyle estiloTituloTabela = wb.createCellStyle();
	      estiloTituloTabela.setAlignment(HSSFCellStyle.ALIGN_CENTER);
	      estiloTituloTabela.setFont(fonteTituloTabela);
	      estiloTituloTabela.setFillForegroundColor(HSSFColor.GREY_80_PERCENT.index);
	      estiloTituloTabela.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
	      
	      HSSFCellStyle estiloTextoE = wb.createCellStyle();
	      estiloTextoE.setAlignment(HSSFCellStyle.ALIGN_JUSTIFY);
	      estiloTextoE.setFont(fonteTexto);
	      
	      HSSFCellStyle estiloTituloE = wb.createCellStyle();
	      estiloTituloE.setFont(fonteTitulo);
		
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
	      
		  celula.setCellValue("Listado Opciones");
	      celula.setCellStyle(estiloTituloE);
	      
	      Region r = new Region(1, (short)5, 1, (short)14);
	      planilha.addMergedRegion(r);
	      
	      row = planilha.createRow(6);
	      celula = row.createCell(0);
	      if(usuario!=null)
	      {
	    	  celula.setCellValue("Usuario");
		      celula.setCellStyle(estiloTituloTabela);
		      r = new Region(6, (short)0, 6, (short)3);
		      planilha.addMergedRegion(r);
		      
		      celula = row.createCell(4);
		      celula.setCellValue("Opciones");
		      celula.setCellStyle(estiloTituloTabela);
		      r = new Region(6, (short)4, 6, (short)7);
		      planilha.addMergedRegion(r);
		      
		      row = planilha.createRow(7);
		      celula = row.createCell(0);
		      celula.setCellStyle(estiloTextoE);
		      celula.setCellValue(usuario.obterNome());
	      }
	      else
	      {
	    	  celula.setCellValue("Opción");
		      celula.setCellStyle(estiloTituloTabela);
		      r = new Region(6, (short)0, 6, (short)3);
		      planilha.addMergedRegion(r);
		      
		      celula = row.createCell(4);
		      celula.setCellValue("Usuarios");
		      celula.setCellStyle(estiloTituloTabela);
		      r = new Region(6, (short)4, 6, (short)7);
		      planilha.addMergedRegion(r);
		      
		      row = planilha.createRow(7);
		      celula = row.createCell(0);
		      celula.setCellStyle(estiloTextoE);
		      celula.setCellValue(home.obterNome(opcao));
	      }
	      
	      r = new Region(7, (short)0, 7, (short)3);
	      planilha.addMergedRegion(r);
	      
	      String str = "";
	      
	      for(Iterator i = dados.iterator() ; i.hasNext() ; )
	      {
	    	  if(usuario!=null)
	    	  {
	    		  String opcaoStr = (String) i.next();
	    		  str+= opcaoStr + "\n";
	    	  }
	    	  else
	    	  {
	    		  Usuario usuario2 = (Usuario) i.next();
	    		  str+=usuario2.obterNome() + "\n";
	    	  }
	      }
	      
	      celula = row.createCell(4);
	      celula.setCellValue(str);
	      celula.setCellStyle(estiloTextoE);
	      r = new Region(7, (short)4, 9, (short)7);
	      planilha.addMergedRegion(r);
	      
	      wb.write(stream);
		  	
	      stream.flush();
	
	      stream.close();
	}
	
}
