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

import com.gvs.crm.model.ClassificacaoContas;
import com.gvs.crm.model.Conta;
import com.gvs.crm.model.Entidade;

import infra.config.InfraProperties;

public class RelTodasAsContasXLS extends Excel 
{
	public RelTodasAsContasXLS(Date data, Collection contas, String aseguradorasMenor80) throws Exception
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
        
        HSSFClientAnchor anchoVivaBem = new HSSFClientAnchor(0,0,0, 0, (short) 2, 0, (short)7 , 5);  
        anchoVivaBem.setAnchorType(3);  
        planilha.createDrawingPatriarch().createPicture(anchoVivaBem, pictureIdx);
        
        HSSFRow row = planilha.createRow(6);
        HSSFCell celula = row.createCell(0);
        
        celula.setCellValue("Setor Economico - " + new SimpleDateFormat("MM/yyyy").format(data));
        celula.setCellStyle(estiloTitulo);
        
        Region r = new Region(6, (short)0, 6, (short)8);
        planilha.addMergedRegion(r);
		
        int linha = 8;
        
        row = planilha.createRow(linha);
        celula = row.createCell(0);
        
        celula.setCellValue("Cuentas Contables");
        celula.setCellStyle(estiloTituloTabela);
        r = new Region(linha, (short)0, linha, (short)5);
        planilha.addMergedRegion(r);
        
        celula = row.createCell(6);
        celula.setCellValue("Valores");
        celula.setCellStyle(estiloTituloTabelaC);
        r = new Region(linha, (short)6, linha, (short)8);
        planilha.addMergedRegion(r);
        
        linha++;
        
        for(Iterator i = contas.iterator() ; i.hasNext() ; )
        {
        	Entidade e = (Entidade) i.next();
        	
        	if(e instanceof ClassificacaoContas)
        	{
        		ClassificacaoContas cContas = (ClassificacaoContas) e;
        		
    	        row = planilha.createRow(linha);
    	        celula = row.createCell(0);
    	        celula.setCellValue(cContas.obterNome() + " - " + cContas.obterApelido());
    	        celula.setCellStyle(estiloTextoE);
    	        r = new Region(linha, (short)0, linha, (short)5);
    	        planilha.addMergedRegion(r);
    	        
    	        celula = row.createCell(6);
    	        celula.setCellValue(formataValor.format(cContas.obterTotalSaldoAtualMensal(data, aseguradorasMenor80)));
    	        celula.setCellStyle(estiloTextoD);
    	        r = new Region(linha, (short)6, linha, (short)8);
    	        planilha.addMergedRegion(r);
        	}
        	else
        	{
        		Conta conta = (Conta) e;
        		
    	        row = planilha.createRow(linha);
    	        celula = row.createCell(0);
    	        celula.setCellValue(conta.obterNome() + " - " + conta.obterApelido());
    	        celula.setCellStyle(estiloTextoE);
    	        r = new Region(linha, (short)0, linha, (short)5);
    	        planilha.addMergedRegion(r);
    	        
    	        celula = row.createCell(6);
    	        celula.setCellValue(formataValor.format(conta.obterTotalSaldoAtualMensal(data, aseguradorasMenor80)));
    	        celula.setCellStyle(estiloTextoD);
    	        r = new Region(linha, (short)6, linha, (short)8);
    	        planilha.addMergedRegion(r);
        	}
        	
        	linha++;
        }
        
		wb.write(stream);

        stream.flush();

        stream.close();
	}
}
