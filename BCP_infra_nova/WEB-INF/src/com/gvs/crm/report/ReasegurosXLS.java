package com.gvs.crm.report;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;

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
import com.gvs.crm.model.Entidade;
import com.gvs.crm.model.EntidadeHome;

import infra.config.InfraProperties;

public class ReasegurosXLS extends Excel
{
	public ReasegurosXLS(Aseguradora aseguradora, Date dataInicio,Date dataFim,Map reaseguros, EntidadeHome home, String textoUsuario) throws Exception
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
	      
	      celula.setCellValue("Produción Reaseguradoras en la Aseguradora " + aseguradora.obterNome());
	      celula.setCellStyle(estiloTitulo);
	    		         
	      Region r = new Region(1, (short)5, 1, (short)14);
	      planilha.addMergedRegion(r);
	      
	      row = planilha.createRow(2);
	      celula = row.createCell(5);
	      celula.setCellValue("Periodo: " + new SimpleDateFormat("dd/MM/yyyy").format(dataInicio) + " hasta " + new SimpleDateFormat("dd/MM/yyyy").format(dataFim));
	      celula.setCellStyle(estiloTitulo);
	      
	      r = new Region(2, (short)5, 2, (short)14);
	      planilha.addMergedRegion(r);
	      
	      row = planilha.createRow(6);
	      celula = row.createCell(0);
	      celula.setCellValue("Reaseguradora");
	      celula.setCellStyle(estiloTituloTabela);
	      r = new Region(6, (short)0, 6, (short)1);
	      planilha.addMergedRegion(r);
	      
	      celula = row.createCell(2);
	      celula.setCellValue("Tipo de Contrato");
	      celula.setCellStyle(estiloTituloTabela);
	      r = new Region(6, (short)2, 6, (short)3);
	      planilha.addMergedRegion(r);
	      
	      celula = row.createCell(4);
	      celula.setCellValue("Monto Cap. Reaseg.");
	      celula.setCellStyle(estiloTituloTabela);
	      r = new Region(6, (short)4, 6, (short)5);
	      planilha.addMergedRegion(r);
	      
	      celula = row.createCell(6);
	      celula.setCellValue("Monto Prima Reaseg.");
	      celula.setCellStyle(estiloTituloTabela);
	      r = new Region(6, (short)6, 6, (short)7);
	      planilha.addMergedRegion(r);
	      
	      celula = row.createCell(8);
	      celula.setCellValue("Monto Comisión");
	      celula.setCellStyle(estiloTituloTabela);
	      r = new Region(6, (short)8, 6, (short)10);
	      planilha.addMergedRegion(r);
	      
	      int linha = 7;
	      
	      for (Iterator i = reaseguros.values().iterator(); i.hasNext();) 
	      {
				String chave = (String) i.next();
				
				StringTokenizer st = new StringTokenizer(chave,"_");
				
				long id = Long.parseLong(st.nextToken());
				String tipoContrato = st.nextToken();
				
				Entidade reaseguradora = home.obterEntidadePorId(id);
				
				double capitalGs = reaseguradora.obterCapitalGsReaseguradora(aseguradora, dataInicio, dataFim,tipoContrato);
				double primaGs = reaseguradora.obterPrimaGsReaseguradora(aseguradora, dataInicio, dataFim,tipoContrato);
				double comissaoGs = reaseguradora.obterComissaoGsReaseguradora(aseguradora, dataInicio, dataFim,tipoContrato);

				if (capitalGs > 0 || primaGs > 0 || comissaoGs > 0) 
				{
					 row = planilha.createRow(linha); 
					 
					 celula = row.createCell(0);
					 celula.setCellValue(reaseguradora.obterNome());
					 celula.setCellStyle(estiloTextoE);
					 r = new Region(linha, (short)0, linha, (short)1);
					 planilha.addMergedRegion(r);
					 
					 celula = row.createCell(2);
					 celula.setCellValue(tipoContrato);
					 celula.setCellStyle(estiloTextoE);
					 r = new Region(linha, (short)2, linha, (short)3);
					 planilha.addMergedRegion(r);
					 
					 celula = row.createCell(4);
					 celula.setCellValue(format.format(capitalGs));
					 celula.setCellStyle(estiloTextoD);
					 r = new Region(linha, (short)4, linha, (short)5);
					 planilha.addMergedRegion(r);
					 
					 celula = row.createCell(6);
					 celula.setCellValue(format.format(primaGs));
					 celula.setCellStyle(estiloTextoD);
					 r = new Region(linha, (short)6, linha, (short)7);
					 planilha.addMergedRegion(r);
					 
					 celula = row.createCell(8);
					 celula.setCellValue(format.format(comissaoGs));
					 celula.setCellStyle(estiloTextoD);
					 r = new Region(linha, (short)8, linha, (short)10);
					 planilha.addMergedRegion(r);
				}
				
				linha++;
	      }
	      
	      linha++;
	      row = planilha.createRow(linha);
	      celula = row.createCell(0);
	      celula.setCellValue(textoUsuario);
	      celula.setCellStyle(estiloTitulo);
	      r = new Region(linha, (short)0, linha, (short)14);
	      planilha.addMergedRegion(r);
	      
	      wb.write(stream);

	      stream.flush();

	      stream.close();
	}
}
