package com.gvs.crm.report;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import com.gvs.crm.model.Reaseguradora;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import infra.config.InfraProperties;

public class ReaseguradoraQualificacaoPDF extends PDF 
{
	public ReaseguradoraQualificacaoPDF(Collection<Reaseguradora> reaseguradoras, Date data, String textoUsuario) throws Exception
	{
		Document documento = new Document(PageSize.A4,25,25,25,25);
		try 
	    {
	        Font fontTitulo = new Font(Font.FontFamily.TIMES_ROMAN, 9,Font.NORMAL);
	        Font fontTexto = new Font(Font.FontFamily.TIMES_ROMAN, 8,Font.NORMAL);
	        Font fontTituloNegrito = new Font(Font.FontFamily.TIMES_ROMAN, 9,Font.BOLD);
	        
	        String caminho = "C:/tmp/" + new Date().getTime() + ".pdf";
	        
	        this.setCaminho(caminho);
	        
	        PdfWriter.getInstance(documento, new FileOutputStream(caminho));
	        documento.open();
	        
	        String dirImages = InfraProperties.getInstance().getProperty("report.images.url");
	        
	        Image img = Image.getInstance(dirImages + "/bcp.jpg");
	        img.scalePercent(50.0f);
	        img.setAlignment(Element.ALIGN_CENTER);
	        
	        PdfPCell cellTitulo = new PdfPCell();
	        
	        Paragraph texto = new Paragraph("SUPERINTENDENCIA DE SEGUROS",fontTituloNegrito);
	        texto.setAlignment(Element.ALIGN_CENTER);
	        Paragraph texto2 = new Paragraph("INTENDENCIA DE CONTROL FINANCIERO",fontTitulo);
	        texto2.setAlignment(Element.ALIGN_CENTER);
	        Paragraph texto3 = new Paragraph("REGISTRO DE REASEGURADORAS",fontTitulo);
	        texto3.setAlignment(Element.ALIGN_CENTER);
	        Paragraph texto4 = new Paragraph("FECHA " + new SimpleDateFormat("dd/MM/yyyy").format(data),fontTitulo);
	        texto4.setAlignment(Element.ALIGN_CENTER);
	        
	        cellTitulo.addElement(texto);
	        cellTitulo.addElement(texto2);
	        cellTitulo.addElement(texto3);
	        cellTitulo.addElement(texto4);
	        cellTitulo.setHorizontalAlignment(Element.ALIGN_CENTER);
	        cellTitulo.setBorder(0);
	        
	        PdfPTable tableTitulo = new PdfPTable(new float[] { 0.20f, 0.80f});
	        PdfPCell cellTitulo2 = new PdfPCell(img);
	        cellTitulo2.setBorder(0);
	        tableTitulo.addCell(cellTitulo2);
	        tableTitulo.addCell(cellTitulo);
	        tableTitulo.setSpacingAfter(10);
	        
	        documento.add(tableTitulo);
	        
	        PdfPTable table = new PdfPTable(5);
	        table.setWidthPercentage(100);
	        
	        for(Iterator<Reaseguradora> i = reaseguradoras.iterator() ; i.hasNext() ; )
	        {
	        	Reaseguradora reaseguradora = (Reaseguradora) i.next();
	        	
	        	Paragraph p = new Paragraph(reaseguradora.obterNome(),fontTituloNegrito);
	 	        PdfPCell cell = new PdfPCell(p);
	 	        cell.setColspan(5);
	 	        cell.setBorder(0);
	 	        //cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	 	        table.addCell(cell);
	 	        
	 	        p = new Paragraph("Calificación",fontTituloNegrito);
	 	        cell = new PdfPCell(p);
	 	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	 	        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
	 	        table.addCell(cell);
	 	        
	 	        p = new Paragraph("Nivel",fontTituloNegrito);
	 	        cell = new PdfPCell(p);
	 	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	 	        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
	 	        table.addCell(cell);
	 	        
	 	        p = new Paragraph("Cod. Calificadora",fontTituloNegrito);
	 	        cell = new PdfPCell(p);
	 	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	 	        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
	 	        table.addCell(cell);
	 	        
	 	        p = new Paragraph("Calificadora",fontTituloNegrito);
	 	        cell = new PdfPCell(p);
	 	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	 	        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
	 	        table.addCell(cell);
	 	        
	 	        p = new Paragraph("Fecha de la Calificación",fontTituloNegrito);
	 	        cell = new PdfPCell(p);
	 	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	 	        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
	 	        table.addCell(cell);
	 	        
	 	        for(Iterator j = reaseguradora.obterClassificacoes().iterator() ; j.hasNext() ; )
	 	        {
	 	        	Reaseguradora.Classificacao classificacao = (Reaseguradora.Classificacao) j.next();
	 	        	
	 	        	p = new Paragraph(classificacao.obterClassificacao(),fontTexto);
	 	 	        cell = new PdfPCell(p);
	 	 	        table.addCell(cell);
	 	 	        
	 	 	        p = new Paragraph(classificacao.obterNivel(),fontTexto);
	 	 	        cell = new PdfPCell(p);
	 	 	        table.addCell(cell);
	 	 	        
	 	 	        p = new Paragraph(classificacao.obterCodigo(),fontTexto);
	 	 	        cell = new PdfPCell(p);
	 	 	        table.addCell(cell);
	 	 	        
	 	 	        p = new Paragraph(classificacao.obterQualificacao(),fontTexto);
	 	 	        cell = new PdfPCell(p);
	 	 	        table.addCell(cell);
	 	 	        
	 	 	        p = new Paragraph(new SimpleDateFormat("dd/MM/yyyy").format(classificacao.obterData()), fontTexto);
	 	 	        cell = new PdfPCell(p);
	 	 	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	 	 	        table.addCell(cell);
	 	        }
	        }
	        
	        documento.add(table);
	        
	        Paragraph p = new Paragraph("Elaborado por: ICORAS - Intendencia de Control de Operaciones de Reaseguros y Auxiliares del Seguro.",fontTituloNegrito);
       	 	p.setSpacingBefore(7);
	        documento.add(p);
	        
	        p = new Paragraph(textoUsuario,fontTituloNegrito);
       	 	p.setSpacingBefore(7);
	        documento.add(p);
	    }
		catch(DocumentException de) 
	    {
	        System.err.println(de.getMessage());
	        throw new Exception(de.getMessage());
	    }
	    catch(IOException ioe) 
	    {
	        System.err.println(ioe.getMessage());
	        throw new Exception(ioe.getMessage());
	    }
	    
	    documento.close();
	}
}
