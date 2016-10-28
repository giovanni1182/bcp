package com.gvs.crm.report;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.DecimalFormat;
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

import com.gvs.crm.model.Aseguradora;
import com.gvs.crm.model.AseguradoraHome;
import com.gvs.crm.model.ClassificacaoContas;
import com.gvs.crm.model.Conta;
import com.gvs.crm.model.Entidade;

import infra.config.InfraProperties;

public class PlanoContasXLS extends Excel
{
	public PlanoContasXLS(AseguradoraHome home, String mes,String ano) throws Exception
	{
		DecimalFormat formataValor = new DecimalFormat("#,##0.00");
		
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
       
       HSSFCellStyle estiloData = wb.createCellStyle();
       estiloData.setDataFormat(HSSFDataFormat.getBuiltinFormat("m/d/yy"));
       
       HSSFCellStyle estiloTitulo = wb.createCellStyle();
       estiloTitulo.setAlignment(HSSFCellStyle.ALIGN_CENTER);
       estiloTitulo.setFont(fonteTitulo);
       
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
       
       HSSFCellStyle estiloTextoD = wb.createCellStyle();
       estiloTextoD.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
       estiloTextoD.setFont(fonteTexto);
       
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
       
       InputStream is = new FileInputStream(dirImages + "/bcp.jpg");
       byte [] bytes = IOUtils.toByteArray (is);
       int pictureIdx = wb.addPicture(bytes, Workbook.PICTURE_TYPE_JPEG); 
       is.close();
       
       HSSFClientAnchor anchoVivaBem = new HSSFClientAnchor(0,0,0, 0, (short) 0, 0, (short)4 , 4);  
       anchoVivaBem.setAnchorType(3);  
       planilha.createDrawingPatriarch().createPicture(anchoVivaBem, pictureIdx);
       
       HSSFRow row = planilha.createRow(1);
       HSSFCell celula = row.createCell(5);
       
       celula.setCellValue("PLAN DE CUENTAS Aseguradoras");
       celula.setCellStyle(estiloTitulo);
       Region r = new Region(1, (short)5, 1, (short)7);
       planilha.addMergedRegion(r);
       
       row = planilha.createRow(2);
       celula = row.createCell(5);
       celula.setCellValue("Mes/Año: " + mes + "/" + ano);
       celula.setCellStyle(estiloTitulo);
       r = new Region(2, (short)5, 2, (short)7);
       planilha.addMergedRegion(r);
       
       row = planilha.createRow(5);
       celula = row.createCell(0);
       celula.setCellValue("Codigo");
       celula.setCellStyle(estiloTituloTabela);
       
       celula = row.createCell(1);
       celula.setCellValue("Nombre de la Cuenta");
       celula.setCellStyle(estiloTituloTabela);
       r = new Region(5, (short)1, 5, (short)3);
       planilha.addMergedRegion(r);
       
       Collection contas = home.obterContas();
		
       Collection aseguradoras = home.obterAseguradorasPorMenor80();
       
       int coluna = 4;
       
       for(Iterator j = aseguradoras.iterator() ; j.hasNext() ; )
       {
    	   Aseguradora aseguradora = (Aseguradora) j.next();
    	   
    	   celula = row.createCell(coluna);
           celula.setCellValue(aseguradora.obterNome());
           celula.setCellStyle(estiloTituloTabela);
           coluna++;
       }
       
       int linha = 6;
       coluna = 4;
       
       for(Iterator i = contas.iterator() ; i.hasNext() ; )
		{
			Entidade e = (Entidade) i.next();
			
			 row = planilha.createRow(linha);
			 celula = row.createCell(0);
		     celula.setCellValue(e.obterApelido());
		     celula.setCellStyle(estiloTextoE);
		       
		     celula = row.createCell(1);
		     celula.setCellValue(e.obterNome());
		     celula.setCellStyle(estiloTextoE);
		     r = new Region(linha, (short)1, linha, (short)3);
		     planilha.addMergedRegion(r);
			
			for(Iterator j = aseguradoras.iterator() ; j.hasNext() ; )
			{
				Aseguradora aseguradora = (Aseguradora) j.next();
				
				if(e instanceof ClassificacaoContas)
				{
					ClassificacaoContas cContas = (ClassificacaoContas) e;
					
					String valor = formataValor.format(cContas.obterTotalizacaoExistente(aseguradora, mes+ano));
					celula = row.createCell(coluna);
			        celula.setCellValue(valor);
			        celula.setCellStyle(estiloTextoD);
			        coluna++;
				}
				else if(e instanceof Conta)
				{
					Conta conta = (Conta) e;
					
					String valor = formataValor.format(conta.obterTotalizacaoExistente(aseguradora, mes+ano));
					celula = row.createCell(coluna);
			        celula.setCellValue(valor);
			        celula.setCellStyle(estiloTextoD);
			        coluna++;
				}
			}
			
			linha++;
			coluna = 4;
		}
       
       wb.write(stream);

       stream.flush();

       stream.close();
	}
}
