package com.gvs.crm.report;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import com.gvs.crm.model.Aseguradora;
import com.gvs.crm.model.Inscricao;
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

public class AseguradoraValidezPDF extends PDF
{
	public AseguradoraValidezPDF(Collection<Aseguradora> aseguradoras, String textoUsuario) throws Exception
	{
		Document documento = new Document(PageSize.A4,25,25,25,25);
		try 
	    {
	        Font fontTexto = new Font(Font.FontFamily.TIMES_ROMAN, 8,Font.NORMAL);
	        Font fontTituloNegrito = new Font(Font.FontFamily.TIMES_ROMAN, 9,Font.BOLD);
	        Font fontPequenoCor = new Font(Font.FontFamily.TIMES_ROMAN, 9,Font.NORMAL,BaseColor.WHITE);
	        
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
	        
	        cellTitulo.addElement(texto);
	        cellTitulo.setHorizontalAlignment(Element.ALIGN_CENTER);
	        cellTitulo.setBorder(0);
	        
	        PdfPTable tableTitulo = new PdfPTable(new float[] { 0.20f, 0.80f});
	        PdfPCell cellTitulo2 = new PdfPCell(img);
	        cellTitulo2.setBorder(0);
	        tableTitulo.addCell(cellTitulo2);
	        tableTitulo.addCell(cellTitulo);
	        tableTitulo.setSpacingAfter(10);
	        
	        documento.add(tableTitulo);
	        
	        PdfPTable table = new PdfPTable(new float[]{0.10f, 0.70f, 0.20f});
	        table.setWidthPercentage(100);
	        
	        Paragraph p = new Paragraph("Inscripción",fontPequenoCor);
 	        PdfPCell cell = new PdfPCell(p);
 	        cell.setBackgroundColor(BaseColor.DARK_GRAY);
 	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
 	        table.addCell(cell);
 	        
 	        p = new Paragraph("Nombre",fontPequenoCor);
	        cell = new PdfPCell(p);
	        cell.setBackgroundColor(BaseColor.DARK_GRAY);
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        table.addCell(cell);
	        
	        p = new Paragraph("Fecha de Validez",fontPequenoCor);
	        cell = new PdfPCell(p);
	        cell.setBackgroundColor(BaseColor.DARK_GRAY);
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        table.addCell(cell);
	        
	        for(Iterator<Aseguradora> i = aseguradoras.iterator() ; i.hasNext() ; )
	        {
	        	Aseguradora aseguradora = i.next();
	        	
	        	Inscricao inscricao = aseguradora.obterInscricaoAtiva();
	        	
	        	p = new Paragraph(inscricao.obterInscricao(),fontTexto);
	 	        cell = new PdfPCell(p);
	 	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	 	        table.addCell(cell);
	 	        
	 	        p = new Paragraph(aseguradora.obterNome(),fontTexto);
	 	        cell = new PdfPCell(p);
	 	        table.addCell(cell);
	 	        
	 	        p = new Paragraph(new SimpleDateFormat("dd/MM/yyyy").format(inscricao.obterDataValidade()),fontTexto);
	 	        cell = new PdfPCell(p);
	 	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	 	        table.addCell(cell);
	        }
	        
	        documento.add(table);
	        
	        p = new Paragraph("Elaborado por: ICORAS - Intendencia de Control de Operaciones de Reaseguros y Auxiliares del Seguro.",fontTituloNegrito);
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
