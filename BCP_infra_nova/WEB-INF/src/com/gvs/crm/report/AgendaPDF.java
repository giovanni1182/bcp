package com.gvs.crm.report;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.gvs.crm.model.AgendaMovimentacao;
import com.gvs.crm.model.Notificacao;
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

public class AgendaPDF extends PDF 
{
	public AgendaPDF(Notificacao notificacao) throws Exception
	{
		Document documento = new Document(PageSize.A4);
		
		try
		{
			AgendaMovimentacao agenda = (AgendaMovimentacao) notificacao.obterSuperior();
			
			Paragraph espaco = new Paragraph("");
			espaco.setSpacingAfter(10);
			PdfPCell espacoCell = new PdfPCell(espaco);
			espacoCell.setColspan(2);
			espacoCell.setBorder(0);
			 
	        Font fontTitulo = new Font(Font.FontFamily.TIMES_ROMAN, 9,Font.NORMAL);
	        Font fontTexto = new Font(Font.FontFamily.TIMES_ROMAN, 8,Font.NORMAL);
	        Font fontTituloNegrito = new Font(Font.FontFamily.TIMES_ROMAN, 10,Font.BOLD);
	        
	        
	        String caminho = "C:/tmp/" + new Date().getTime() + ".pdf";
	        
	        this.setCaminho(caminho);
	        
	        PdfWriter.getInstance(documento, new FileOutputStream(caminho));
	        documento.open();
	        
	        String dirImages = InfraProperties.getInstance().getProperty("report.images.url");
	        
	        Image img = Image.getInstance(dirImages + "/bcp.jpg");
	        img.scalePercent(50.0f);
	        img.setAlignment(Element.ALIGN_CENTER);
	        
	        PdfPTable table = new PdfPTable(new float[] { 0.25f, 0.75f});
	        PdfPCell cell = new PdfPCell(img);
	        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
	        cell.setBorder(0);
	        table.addCell(cell);
	        
	        String tipo = "";
	        
	        if(agenda.obterTipo().equals("Instrumento"))
	        	tipo = "Central de Información";
	        else
	        	tipo = "Contable";
	        
	        Paragraph para = new Paragraph("SUPERINTENDENCIA DE SEGUROS\n",fontTituloNegrito); 
	        para.add("División Informática\n");
	        para.add("Validación de Datos del Modulo: " + tipo + "\n");
	        int mes = agenda.obterMesMovimento();
	        String mesAno = "";
	        if(new Integer(mes).toString().length() == 1)
	        	mesAno = "0" + mes + "/" + agenda.obterAnoMovimento();
	        else
	        	mesAno = mes + "/" + agenda.obterAnoMovimento();
	        para.add("Datos al: " + mesAno);
	        
	        cell = new PdfPCell();
	        cell.addElement(para);
	        cell.setBorder(0);
	        table.addCell(cell);
	        
	        table.addCell(espacoCell);
	        table.addCell(espacoCell);
	        table.addCell(espacoCell);
	        
	        para = new Paragraph(agenda.obterOrigem().obterNome().toUpperCase());
	        para.setAlignment(Element.ALIGN_CENTER);
	        para.setFont(fontTituloNegrito);
	        
	        cell = new PdfPCell();
	        cell.addElement(para);
	        cell.setBorder(0);
	        cell.setColspan(2);
	        table.addCell(cell);
	        
	        table.addCell(espacoCell);
	        
	        para = new Paragraph("Fecha de Validación: " + new SimpleDateFormat("dd/MM/yyyy").format(notificacao.obterCriacao()),fontTexto); 
	        cell = new PdfPCell();
	        cell.addElement(para);
	        cell.setBorder(0);
	        cell.setColspan(2);
	        table.addCell(cell);
	        
	        table.addCell(espacoCell);
	        
	        
	        para = new Paragraph("El archivo entregado/enviado conforme el proceso de validación, presenta el siguiente resultado:",fontTexto); 
	        cell = new PdfPCell();
	        cell.addElement(para);
	        cell.setBorder(0);
	        cell.setColspan(2);
	        table.addCell(cell);
	        
	        String[] texto = notificacao.obterDescricao().split("\n");
	        
	        for(int i = 0 ; i < texto.length ; i++)
	        {
	        	String linha = texto[i];
	        	
	        	para = new Paragraph(linha,fontTexto); 
	 	        cell = new PdfPCell();
	 	        cell.addElement(para);
	 	        cell.setBorder(0);
	 	        cell.setColspan(2);
	 	        table.addCell(cell);
	        }
	        
	        table.setSpacingAfter(30);
	        
	        documento.add(table);
	        
	        PdfPTable table2 = new PdfPTable(new float[] { 0.50f, 0.50f});
	        para = new Paragraph("___________________________________________________\nFirma y nombre del funcionario",fontTexto);
	        para.setAlignment(Element.ALIGN_CENTER);
	        cell = new PdfPCell();
	        cell.addElement(para);
	        cell.setBorder(0);
	        table2.addCell(cell);
	        
	        para = new Paragraph("\nSello, nombre y firma de la DI/SIS",fontTexto);
	        para.setAlignment(Element.ALIGN_CENTER);
	        cell = new PdfPCell();
	        cell.addElement(para);
	        cell.setBorder(0);
	        table2.addCell(cell);
	        
	        documento.add(table2);
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
