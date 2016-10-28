package com.gvs.crm.report;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
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

import com.gvs.crm.model.Aseguradora;
import com.gvs.crm.model.Plano;
import com.gvs.crm.model.Usuario;

import infra.config.InfraProperties;

public class AseguradoraPlanosXLS extends Excel 
{
	public AseguradoraPlanosXLS(Aseguradora aseguradora, Usuario usuario, String textoUsuario) throws Exception
	{
		String caminho = "C:/tmp/" + aseguradora.obterNome() + "_Planes" + usuario.obterId() + ".xls";
		
		Collection planos = aseguradora.obterPlanosOrdenadorPorSecao();
		
		/* String ano = new SimpleDateFormat("yyyy").format(data);
	     String mes = this.getMesExtenso(data);*/
		
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
       
       HSSFCellStyle estiloTextoN = wb.createCellStyle();
       estiloTextoN.setFont(fonteTextoN);
       
       HSSFCellStyle estiloTitulo = wb.createCellStyle();
       estiloTitulo.setAlignment(HSSFCellStyle.ALIGN_CENTER);
       estiloTitulo.setFont(fonteTitulo);
       
       HSSFCellStyle estiloTituloJ = wb.createCellStyle();
       estiloTituloJ.setAlignment(HSSFCellStyle.ALIGN_JUSTIFY);
       estiloTituloJ.setFont(fonteTitulo);
       
       HSSFCellStyle estiloTexto = wb.createCellStyle();
       estiloTexto.setAlignment(HSSFCellStyle.ALIGN_CENTER);
       estiloTexto.setFont(fonteTexto);
       
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
       estiloTextoCorE.setFont(fonteTextoN);
       estiloTextoCorE.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
       estiloTextoCorE.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
       
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
       
       HSSFRow row = planilha.createRow(0);
       HSSFCell celula = row.createCell(5);
       
       celula.setCellValue("NUESTRA VISIÓN: Desarrollar una gestión eficiente, autónoma y "+
"basada en la excelencia de sus talentos, tendiente a contribuir com la " +
"mejor calidad de vida de los habitantes de nuestro pais y lograr el " +
"reconocimiento nacional e internacional como institución confiable y " +
"creíble");
       celula.setCellStyle(estiloTituloJ);
       
       Region r = new Region(0, (short)5, 5, (short)11);
       planilha.addMergedRegion(r);
       
       row = planilha.createRow(7);
       celula = row.createCell(0);
       
       String[] nomeV = aseguradora.obterNome().split("-");
       String titulo = nomeV[0].toUpperCase();
       
       celula.setCellValue(titulo);
       celula.setCellStyle(estiloTitulo);
       
       r = new Region(7, (short)0, 7, (short)11);
       planilha.addMergedRegion(r);
       
       row = planilha.createRow(8);
       celula = row.createCell(0);
       
       celula.setCellValue("PLANES DE SEGUROS INSCRIPTOS EN EL REGISTRO PÚBLICO");
       celula.setCellStyle(estiloTitulo);
       
       r = new Region(8, (short)0, 8, (short)11);
       planilha.addMergedRegion(r);
       
       row = planilha.createRow(10);
       celula = row.createCell(0);
       celula.setCellValue("CÓDIGO");
       celula.setCellStyle(estiloTituloTabela);
       
       celula = row.createCell(1);
       celula.setCellValue("DENOMINACIÓN");
       celula.setCellStyle(estiloTituloTabela);
       
       r = new Region(10, (short)1, 10, (short)7);
       planilha.addMergedRegion(r);
       
       celula = row.createCell(8);
       celula.setCellValue("RESOLUCIÓN / NOTA");
       celula.setCellStyle(estiloTituloTabela);
       
       r = new Region(10, (short)8, 10, (short)9);
       planilha.addMergedRegion(r);
       
       celula = row.createCell(10);
       celula.setCellValue("FECHA");
       celula.setCellStyle(estiloTituloTabela);
       
       r = new Region(10, (short)10, 10, (short)11);
       planilha.addMergedRegion(r);
       
       Map secao = new TreeMap();
       
       int linha = 11;
       
       for(Iterator i = planos.iterator() ; i.hasNext() ; )
       {
	       	Plano plano = (Plano) i.next();
	       	
	        row = planilha.createRow(linha);
	        celula = row.createCell(0);
	       	
	       	if(!secao.containsKey(plano.obterSecao()))
			{
	       		celula = row.createCell(0);
	            celula.setCellValue("SECCIÓN: " + plano.obterSecao());
	            celula.setCellStyle(estiloTextoCorE);
	            
	            r = new Region(linha, (short)0, linha, (short)11);
	            planilha.addMergedRegion(r);
	            linha++;
	            row = planilha.createRow(linha);
			}
	       	
	       	celula = row.createCell(0);
            celula.setCellValue(plano.obterIdentificador());
            celula.setCellStyle(estiloTexto);
            
            celula = row.createCell(1);
            celula.setCellValue(plano.obterTitulo());
            celula.setCellStyle(estiloTextoE);
            
            r = new Region(linha, (short)1, linha, (short)7);
            planilha.addMergedRegion(r);
            
            celula = row.createCell(8);
            celula.setCellValue(plano.obterResolucao());
            celula.setCellStyle(estiloTexto);
            
            r = new Region(linha, (short)8, linha, (short)9);
            planilha.addMergedRegion(r);
            
            celula = row.createCell(10);
            celula.setCellValue(new SimpleDateFormat("dd/MM/yyyy").format(plano.obterDataResolucao()));
            celula.setCellStyle(estiloTexto);
            
            r = new Region(linha, (short)10, linha, (short)11);
            planilha.addMergedRegion(r);
	       	
	       	
	       	linha++;
	        secao.put(plano.obterSecao(), plano.obterSecao());
       }
       
       row = planilha.createRow(linha);
       celula = row.createCell(0);
       celula.setCellValue("Última actualización: " + new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
       celula.setCellStyle(estiloTitulo);
       
       r = new Region(linha, (short)0, linha, (short)11);
       planilha.addMergedRegion(r);
       
       row = planilha.createRow(linha+=2);
       celula = row.createCell(0);
       celula.setCellValue("NUESTRA MISIÓN: Preservar y velar por la estabilidad del valor de la moneda, promover la eficacia y estabilidad " +
    		   "del sistema financiero y servir a la Sociedad con excelencia en su rol de banco de bancos y agente financiero del " +
    		   "Estado.)");
       celula.setCellStyle(estiloTituloJ);
       
       r = new Region(linha, (short)0, linha+=2, (short)11);
       planilha.addMergedRegion(r);
       
      /* linha+=3;
       row = planilha.createRow(linha);
       celula = row.createCell(0);
       celula.setCellValue("Elaborado por: IETA - Intendencia de Estudios Técnicos y Actuariales.");
       celula.setCellStyle(estiloTextoN);
       r = new Region(linha, (short)0, linha, (short)11);
       planilha.addMergedRegion(r);
       
       linha+=2;
       row = planilha.createRow(linha);
       celula = row.createCell(0);
       celula.setCellValue(textoUsuario);
       celula.setCellStyle(estiloTextoN);
       r = new Region(linha, (short)0, linha, (short)11);
       planilha.addMergedRegion(r);*/
       
       wb.write(stream);

       stream.flush();

       stream.close();
	}
}
