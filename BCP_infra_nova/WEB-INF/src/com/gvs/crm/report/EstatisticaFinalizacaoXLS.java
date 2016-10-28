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
import com.gvs.crm.model.Aseguradora;
import com.gvs.crm.model.EntidadeHome;

import infra.config.InfraProperties;

public class EstatisticaFinalizacaoXLS extends Excel
{
	public EstatisticaFinalizacaoXLS(Aseguradora aseguradora, Date dataFim, ApoliceHome home, EntidadeHome entidadeHome, String textoUsuario) throws Exception
	{
		String caminho = "C:/tmp/" + new Date().getTime() + ".xls";
		
		DecimalFormat formataValor = new DecimalFormat("#,##0.00");
	
		FileOutputStream stream = new FileOutputStream(caminho);
		
		this.setCaminho(caminho);
		
		HSSFWorkbook wb = new HSSFWorkbook();

      HSSFSheet planilha = wb.createSheet("Planilha");
      
      HSSFFont fonteTitulo = wb.createFont();
      fonteTitulo.setFontHeightInPoints((short)10);
      fonteTitulo.setFontName("Arial");
      fonteTitulo.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
      
      HSSFFont fonteTexto = wb.createFont();
      fonteTexto.setFontHeightInPoints((short)9);
      fonteTexto.setFontName("Arial");
      
      HSSFFont fonteTextoN = wb.createFont();
      fonteTextoN.setFontHeightInPoints((short)9);
      fonteTextoN.setFontName("Arial");
      fonteTextoN.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
      
      HSSFCellStyle estiloTitulo = wb.createCellStyle();
      estiloTitulo.setAlignment(HSSFCellStyle.ALIGN_CENTER);
      estiloTitulo.setFont(fonteTitulo);
      
      HSSFCellStyle estiloTexto = wb.createCellStyle();
      estiloTexto.setAlignment(HSSFCellStyle.ALIGN_CENTER);
      estiloTexto.setFont(fonteTexto);
      
      HSSFCellStyle estiloTextoD = wb.createCellStyle();
      estiloTextoD.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
      estiloTextoD.setFont(fonteTexto);
      
      HSSFCellStyle estiloTextoE = wb.createCellStyle();
      estiloTextoE.setAlignment(HSSFCellStyle.ALIGN_JUSTIFY);
      estiloTextoE.setFont(fonteTexto);
      
      HSSFCellStyle estiloTextoN = wb.createCellStyle();
      estiloTextoN.setAlignment(HSSFCellStyle.ALIGN_CENTER);
      estiloTextoN.setFont(fonteTextoN);
      
      HSSFCellStyle estiloTextoN_E = wb.createCellStyle();
      estiloTextoN.setFont(fonteTextoN);
      
      HSSFFont fonteTituloTabela = wb.createFont();
      fonteTituloTabela.setFontHeightInPoints((short)10);
      fonteTituloTabela.setFontName("Arial");
      fonteTituloTabela.setColor(HSSFColor.WHITE.index);
      
      HSSFCellStyle estiloTituloTabelaC = wb.createCellStyle();
      estiloTituloTabelaC.setAlignment(HSSFCellStyle.ALIGN_CENTER);
      estiloTituloTabelaC.setFont(fonteTituloTabela);
      estiloTituloTabelaC.setFillForegroundColor(HSSFColor.GREY_80_PERCENT.index);
      estiloTituloTabelaC.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
      
      HSSFCellStyle estiloTituloTabela = wb.createCellStyle();
      estiloTituloTabela.setAlignment(HSSFCellStyle.ALIGN_LEFT);
      estiloTituloTabela.setFont(fonteTituloTabela);
      estiloTituloTabela.setFillForegroundColor(HSSFColor.GREY_80_PERCENT.index);
      estiloTituloTabela.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
      
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
      
      celula.setCellValue("SUPERINTENDENCIA DE SEGUROS");
      celula.setCellStyle(estiloTitulo);
      Region r = new Region(1, (short)5, 1, (short)11);
      planilha.addMergedRegion(r);
      
      row = planilha.createRow(2);
      celula = row.createCell(5);
      
   	  celula.setCellValue("Estadística Finalización de Vigencia del Instrumento");
      celula.setCellStyle(estiloTitulo);
      r = new Region(2, (short)5, 2, (short)11);
      planilha.addMergedRegion(r);
      
      row = planilha.createRow(3);
      celula = row.createCell(5);
      
      if(aseguradora == null)
    	  celula.setCellValue("Aseguradora: Todas");
      else
    	  celula.setCellValue("Aseguradora: " + aseguradora.obterNome());
      celula.setCellStyle(estiloTitulo);
      r = new Region(3, (short)5, 3, (short)11);
      planilha.addMergedRegion(r);
      
      row = planilha.createRow(4);
      celula = row.createCell(5);
      
   	  celula.setCellValue("Mes/Anõ: " + new SimpleDateFormat("MM/yyyy").format(dataFim));
      celula.setCellStyle(estiloTitulo);
      r = new Region(4, (short)5, 4, (short)11);
      planilha.addMergedRegion(r);
      
      int linha = 6;
      Collection<Aseguradora> aseguradoras = new ArrayList<Aseguradora>();
      
      Calendar c = Calendar.getInstance();
      c.setTime(dataFim);
	
      String dataFimStr = c.getActualMaximum(Calendar.DAY_OF_MONTH) +"/"+ new SimpleDateFormat("MM").format(dataFim) +"/"+ new SimpleDateFormat("yyyy").format(dataFim);
      dataFim = new SimpleDateFormat("dd/MM/yyyy").parse(dataFimStr);
	
      c.add(Calendar.MONTH, -11);
      Date dataInicio = c.getTime();
      String dataInicioStr = c.getActualMinimum(Calendar.DAY_OF_MONTH) +"/"+ new SimpleDateFormat("MM").format(dataInicio) +"/"+ new SimpleDateFormat("yyyy").format(dataInicio);
      dataInicio = new SimpleDateFormat("dd/MM/yyyy").parse(dataInicioStr);
      
      if(aseguradora == null)
    	  aseguradoras.addAll(entidadeHome.obterAseguradoras());
      else
    	  aseguradoras.add(aseguradora);
		
      c.setTime(dataInicio);
      
      for(Iterator<Aseguradora> i = aseguradoras.iterator() ; i.hasNext() ; )
      {
    	  Aseguradora aseg = i.next();
    	  
    	  row = planilha.createRow(linha);
          celula = row.createCell(0);
          
          celula.setCellValue(aseg.obterNome());
          celula.setCellStyle(estiloTextoN_E);
          r = new Region(linha, (short)0, linha, (short)13);
          planilha.addMergedRegion(r);
          
          linha++;
          
          row = planilha.createRow(linha);
          celula = row.createCell(0);
          celula.setCellValue("");
          celula.setCellStyle(estiloTituloTabela);
          r = new Region(linha, (short)0, linha, (short)1);
          planilha.addMergedRegion(r);
          
          int coluna = 2;
          while(c.getTime().compareTo(dataFim)<=0)
          {
        	  celula = row.createCell(coluna);
              celula.setCellValue(new SimpleDateFormat("MM/yyyy").format(c.getTime()));
              celula.setCellStyle(estiloTituloTabelaC);
        	  c.add(Calendar.MONTH, 1);
        	  
        	  coluna++;
          }
          
          linha++;
          coluna = 2;
          c.setTime(dataInicio);
          
          row = planilha.createRow(linha);
          celula = row.createCell(0);
          celula.setCellValue("Cantidad");
          celula.setCellStyle(estiloTextoE);
          r = new Region(linha, (short)0, linha, (short)1);
          planilha.addMergedRegion(r);
          
          while(c.getTime().compareTo(dataFim)<=0)
          {
        	  String linhaSuja = home.obterDadosAquivoFinalizacao(aseg, c.getTime());
        	  
        	  String[] linhaStr = linhaSuja.split(";");
        	  String qtde = linhaStr[0];
        	  
              celula = row.createCell(coluna);
              celula.setCellValue(qtde);
              celula.setCellStyle(estiloTexto);
        	  
        	  c.add(Calendar.MONTH, 1);
        	  coluna++;
          }
          
          c.setTime(dataInicio);
          
          linha+=2;
      }
      
      linha+=2;
      row = planilha.createRow(linha);
      celula = row.createCell(0);
   	  celula.setCellValue(textoUsuario);
      celula.setCellStyle(estiloTitulo);
      r = new Region(linha, (short)0, linha, (short)11);
      planilha.addMergedRegion(r);
      
      wb.write(stream);

      stream.flush();

      stream.close();
	}
}
