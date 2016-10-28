package com.gvs.crm.report;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import com.gvs.crm.model.Usuario;
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

public class UsuariosPDF extends PDF
{
	public UsuariosPDF(Collection<Usuario> usuarios, String textoUsuario) throws Exception
	{
		Document documento = new Document(PageSize.A4);
		try 
	    {
			String caminho = "C:/tmp/" + new Date().getTime() + ".pdf";
	        
	        this.setCaminho(caminho);
	        
	        Font fontTituloNegrito = new Font(Font.FontFamily.TIMES_ROMAN, 11,Font.BOLD);
	        Font fontTextoNormal = new Font(Font.FontFamily.TIMES_ROMAN, 9,Font.NORMAL);
	        Font fontTextoNegrito = new Font(Font.FontFamily.TIMES_ROMAN, 9,Font.BOLD);
	        
	        PdfWriter.getInstance(documento, new FileOutputStream(caminho));
	        documento.open();
	        
	        String dirImages = InfraProperties.getInstance().getProperty("report.images.url");
	        
	        Image img = Image.getInstance(dirImages + "/bcp.jpg");
	        img.scalePercent(50.0f);
	        img.setAlignment(Element.ALIGN_CENTER);
	        
	        PdfPCell cellTitulo = new PdfPCell();
	        cellTitulo.setBorder(0);
	        
	        Paragraph texto = new Paragraph("SUPERINTENDENCIA DE SEGUROS",fontTituloNegrito);
	        texto.setAlignment(Element.ALIGN_CENTER);
	        cellTitulo.addElement(texto);
	        
	        PdfPTable tableTitulo = new PdfPTable(new float[] { 0.20f, 0.80f});
	        tableTitulo.setWidthPercentage(100.0f);
	        PdfPCell cellTitulo2 = new PdfPCell(img);
	        cellTitulo2.setBorder(0);
	        tableTitulo.addCell(cellTitulo2);
	        tableTitulo.addCell(cellTitulo);
	        tableTitulo.setSpacingAfter(10);
	        
	        documento.add(tableTitulo);
	        
	        PdfPTable table = new PdfPTable(new float[] { 0.25f, 0.25f, 0.25f, 0.25f});
	        table.setWidthPercentage(100.0f);
	        
	        cellTitulo = new PdfPCell();
	        texto = new Paragraph("Fecha Catastro",fontTextoNegrito);
	        texto.setAlignment(Element.ALIGN_CENTER);
	        cellTitulo.addElement(texto);
	        table.addCell(cellTitulo);
	        
	        cellTitulo = new PdfPCell();
	        texto = new Paragraph("Nombre",fontTextoNegrito);
	        texto.setAlignment(Element.ALIGN_CENTER);
	        cellTitulo.addElement(texto);
	        table.addCell(cellTitulo);
	        
	        cellTitulo = new PdfPCell();
	        texto = new Paragraph("Login",fontTextoNegrito);
	        texto.setAlignment(Element.ALIGN_CENTER);
	        cellTitulo.addElement(texto);
	        table.addCell(cellTitulo);
	        
	        cellTitulo = new PdfPCell();
	        texto = new Paragraph("Nivel",fontTextoNegrito);
	        texto.setAlignment(Element.ALIGN_CENTER);
	        cellTitulo.addElement(texto);
	        table.addCell(cellTitulo);
	        
	        for(Iterator<Usuario> i = usuarios.iterator() ; i.hasNext() ; )
	        {
	        	Usuario usuario = i.next();
	        	
	        	String data = new SimpleDateFormat("dd/MM/yyyy").format(usuario.obterCriacao());
	        	
	        	cellTitulo = new PdfPCell();
	 	        texto = new Paragraph(data,fontTextoNormal);
	 	        texto.setAlignment(Element.ALIGN_CENTER);
	 	        cellTitulo.addElement(texto);
	 	        table.addCell(cellTitulo);
	 	        
	 	        cellTitulo = new PdfPCell();
	 	        texto = new Paragraph(usuario.obterNome(),fontTextoNormal);
	 	        cellTitulo.addElement(texto);
	 	        table.addCell(cellTitulo);
	 	        
	 	        cellTitulo = new PdfPCell();
	 	        texto = new Paragraph(usuario.obterChave(),fontTextoNormal);
	 	        cellTitulo.addElement(texto);
	 	        table.addCell(cellTitulo);
	 	        
	 	        cellTitulo = new PdfPCell();
	 	        texto = new Paragraph(usuario.obterNivel(),fontTextoNormal);
	 	        texto.setAlignment(Element.ALIGN_CENTER);
	 	        cellTitulo.addElement(texto);
	 	        table.addCell(cellTitulo);
	        }
	        
	        documento.add(table);
	        
	        texto = new Paragraph(textoUsuario,fontTextoNegrito);
	        documento.add(texto);
	        
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
