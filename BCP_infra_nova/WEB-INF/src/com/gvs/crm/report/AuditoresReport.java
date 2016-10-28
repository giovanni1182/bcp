package com.gvs.crm.report;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import infra.config.InfraProperties;

public class AuditoresReport extends PDF 
{
	public AuditoresReport(Collection lista, Collection lista2, Date data, String obs, String textoUsuario,boolean ci) throws Exception
	{
		//Document documento = new Document(PageSize.LETTER.rotate(),40,40,40,40);
		Document documento = new Document(new Rectangle(1020, 620),25,25,25,5);
		try 
	    {
			//Rectangle rec = new Rectangle(150, 50);
			/*Font fontPequenoCor = new Font(Font.FontFamily.TIMES_ROMAN, 7,Font.NORMAL,BaseColor.WHITE);
			Font fontTextoCor = new Font(Font.FontFamily.TIMES_ROMAN, 9,Font.NORMAL,BaseColor.WHITE);
			Font fontTexto = new Font(Font.FontFamily.TIMES_ROMAN, 9);*/
			
	        Font fontTituloBranco = new Font(Font.FontFamily.TIMES_ROMAN, 8,Font.BOLD,BaseColor.WHITE);
	        Font fontTitulo = new Font(Font.FontFamily.TIMES_ROMAN, 9,Font.NORMAL);
	        Font fontTexto = new Font(Font.FontFamily.TIMES_ROMAN, 8,Font.NORMAL);
	        Font fontTituloNegrito = new Font(Font.FontFamily.TIMES_ROMAN, 10,Font.BOLD);
	        Font fontTituloPNegrito = new Font(Font.FontFamily.TIMES_ROMAN, 8,Font.BOLD);
	        
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
	        Paragraph texto3 = new Paragraph("REGISTRO DE AUDITORES EXTERNOS PARA LAS COMPAÑÍAS DE SEGUROS",fontTitulo);
	        texto3.setAlignment(Element.ALIGN_CENTER);
	        Paragraph texto4 = new Paragraph("FIRMAS DE AUDITORAS EXTERNAS INSCRIPTAS CONFORME LA RESOLUCION SS.SG. Nº 241/04 DEL 30.06.2004",fontTitulo);
	        texto4.setAlignment(Element.ALIGN_CENTER);
	        
	        cellTitulo.addElement(texto);
	        cellTitulo.addElement(texto2);
	        cellTitulo.addElement(new Paragraph(" "));
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
	        
	        PdfPTable table = null;
	        if(ci)
	        	table = new PdfPTable(new float[] { 0.03f, 0.04f, 0.05f, 0.07f, 0.20f, 0.22f, 0.07f, 0.07f, 0.19f, 0.06f});
	        else
	        	table = new PdfPTable(new float[] { 0.03f, 0.04f, 0.05f, 0.07f, 0.23f, 0.25f, 0.07f, 0.07f, 0.19f});
	        table.setWidthPercentage(100);
	        
	        Paragraph texto14 = new Paragraph("REGISTRO DE AUDITORES EXTERNOS HABILITADOS",fontTituloBranco);
	        PdfPCell cell10 = new PdfPCell(texto14);
	        cell10.setColspan(table.getNumberOfColumns());
	        cell10.setBorder(0);
	        cell10.setHorizontalAlignment(Element.ALIGN_CENTER);
	        cell10.setBackgroundColor(BaseColor.BLACK);
	        
	        table.addCell(cell10);
	        
	        Paragraph texto5 = new Paragraph("Nº de Orden",fontTituloPNegrito);
	        PdfPCell cell = new PdfPCell(texto5);
	        cell.setBorder(0);
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
	        
	        Paragraph texto6 = new Paragraph("Resolución SS.SG. Nº",fontTituloPNegrito);
	        PdfPCell cell2 = new PdfPCell(texto6);
	        cell2.setBorder(0);
	        cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
	        cell2.setBackgroundColor(BaseColor.LIGHT_GRAY);
	        
	        Paragraph texto7 = new Paragraph("Fecha de Registro",fontTituloPNegrito);
	        PdfPCell cell3 = new PdfPCell(texto7);
	        cell3.setBorder(0);
	        cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
	        cell3.setBackgroundColor(BaseColor.LIGHT_GRAY);
	        
	        Paragraph texto8 = new Paragraph("Vigencia del Registro",fontTituloPNegrito);
	        PdfPCell cell4 = new PdfPCell(texto8);
	        cell4.setBorder(0);
	        cell4.setHorizontalAlignment(Element.ALIGN_CENTER);
	        cell4.setBackgroundColor(BaseColor.LIGHT_GRAY);
	        
	        Paragraph texto9 = new Paragraph("Firma Auditora",fontTituloPNegrito);
	        PdfPCell cell5 = new PdfPCell(texto9);
	        cell5.setBorder(0);
	        cell5.setHorizontalAlignment(Element.ALIGN_CENTER);
	        cell5.setBackgroundColor(BaseColor.LIGHT_GRAY);
	        
	        Paragraph texto10 = new Paragraph("Dirección",fontTituloPNegrito);
	        PdfPCell cell6 = new PdfPCell(texto10);
	        cell6.setBorder(0);
	        cell6.setHorizontalAlignment(Element.ALIGN_CENTER);
	        cell6.setBackgroundColor(BaseColor.LIGHT_GRAY);
	        
	        Paragraph texto11 = new Paragraph("Teléfono",fontTituloPNegrito);
	        PdfPCell cell7 = new PdfPCell(texto11);
	        cell7.setBorder(0);
	        cell7.setHorizontalAlignment(Element.ALIGN_CENTER);
	        cell7.setBackgroundColor(BaseColor.LIGHT_GRAY);
	        
	        Paragraph texto12 = new Paragraph("Fax",fontTituloPNegrito);
	        PdfPCell cell8 = new PdfPCell(texto12);
	        cell8.setBorder(0);
	        cell8.setHorizontalAlignment(Element.ALIGN_CENTER);
	        cell8.setBackgroundColor(BaseColor.LIGHT_GRAY);
	        
	        Paragraph texto13 = new Paragraph("Email",fontTituloPNegrito);
	        PdfPCell cell9 = new PdfPCell(texto13);
	        cell9.setBorder(0);
	        cell9.setHorizontalAlignment(Element.ALIGN_CENTER);
	        cell9.setBackgroundColor(BaseColor.LIGHT_GRAY);
	        
	        Paragraph texto77 = new Paragraph("CI o RUC",fontTituloPNegrito);
	        PdfPCell cell77 = new PdfPCell(texto77);
	        cell77.setBorder(0);
	        cell77.setHorizontalAlignment(Element.ALIGN_CENTER);
	        cell77.setBackgroundColor(BaseColor.LIGHT_GRAY);
	        
	        table.addCell(cell);
	        table.addCell(cell2);
	        table.addCell(cell3);
	        table.addCell(cell4);
	        table.addCell(cell5);
	        table.addCell(cell6);
	        table.addCell(cell7);
	        table.addCell(cell8);
	        table.addCell(cell9);
	        
	        if(ci)
		        table.addCell(cell77);
	        
	        int cont = 1;
	        
	        for(Iterator i = lista.iterator() ; i.hasNext() ; )
	        {
	        	String str = (String) i.next();
	        	
	        	String[] dados = str.split(";");
	        	
	        	String resolucao = dados[0];
	        	String dataR = dados[1];
	        	String dataV = dados[2];
	        	String nome = dados[3];
	        	String endereco = dados[4];
	        	String tel = dados[5];
	        	String fax = dados[6];
	        	String email = dados[7];
	        	String ruc = dados[8];
	        	
	        	Paragraph texto15 = new Paragraph(new Integer(cont).toString(),fontTexto);
		        PdfPCell cell11 = new PdfPCell(texto15);
		        cell11.setHorizontalAlignment(Element.ALIGN_CENTER);
		        cell11.setBorder(0);
		        
		        table.addCell(cell11);
		        
		        texto15 = new Paragraph(resolucao,fontTexto);
		        cell11 = new PdfPCell(texto15);
		        cell11.setHorizontalAlignment(Element.ALIGN_CENTER);
		        cell11.setBorder(0);
		        
		        table.addCell(cell11);
		        
		        texto15 = new Paragraph(dataR,fontTexto);
		        cell11 = new PdfPCell(texto15);
		        cell11.setHorizontalAlignment(Element.ALIGN_CENTER);
		        cell11.setBorder(0);
		        
		        table.addCell(cell11);
		        
		        texto15 = new Paragraph(dataV,fontTexto);
		        cell11 = new PdfPCell(texto15);
		        cell11.setHorizontalAlignment(Element.ALIGN_CENTER);
		        cell11.setBorder(0);
		        
		        table.addCell(cell11);
		        
		        texto15 = new Paragraph(nome,fontTexto);
		        cell11 = new PdfPCell(texto15);
		        cell11.setBorder(0);
		        
		        table.addCell(cell11);
		        
		        texto15 = new Paragraph(endereco,fontTexto);
		        cell11 = new PdfPCell(texto15);
		        cell11.setBorder(0);
		        
		        table.addCell(cell11);
		        
		        texto15 = new Paragraph(tel,fontTexto);
		        cell11 = new PdfPCell(texto15);
		        cell11.setHorizontalAlignment(Element.ALIGN_CENTER);
		        cell11.setBorder(0);
		        
		        table.addCell(cell11);
		        
		        texto15 = new Paragraph(fax,fontTexto);
		        cell11 = new PdfPCell(texto15);
		        cell11.setHorizontalAlignment(Element.ALIGN_CENTER);
		        cell11.setBorder(0);
		        
		        table.addCell(cell11);
		        
		        texto15 = new Paragraph(email,fontTexto);
		        cell11 = new PdfPCell(texto15);
		        cell11.setBorder(0);
		        
		        table.addCell(cell11);
		        
		        if(ci)
		        {
		        	texto15 = new Paragraph(ruc,fontTexto);
				    cell11 = new PdfPCell(texto15);
				    cell11.setBorder(0);
				    cell11.setHorizontalAlignment(Element.ALIGN_CENTER);
			        table.addCell(cell11);
		        }
		        
		        cont++;
	        }
	        
	        texto14 = new Paragraph("FECHA DE ACTUALIZACION " + new SimpleDateFormat("dd/MM/yyyy").format(data),fontTituloBranco);
	        cell10 = new PdfPCell(texto14);
	        cell10.setColspan(9);
	        cell10.setBorder(0);
	        cell10.setHorizontalAlignment(Element.ALIGN_CENTER);
	        cell10.setBackgroundColor(BaseColor.BLACK);
	        
	        table.addCell(cell10);
	        
	        table.setSpacingAfter(20);
	        
	        documento.add(table);
	        
	        if(ci)
	        	table = new PdfPTable(new float[] { 0.03f, 0.04f, 0.05f, 0.07f, 0.20f, 0.22f, 0.07f, 0.07f, 0.19f, 0.06f});
	        else
	        	table = new PdfPTable(new float[] { 0.03f, 0.04f, 0.05f, 0.07f, 0.23f, 0.25f, 0.07f, 0.07f, 0.19f});
	        table.setWidthPercentage(100);
	        
	        texto14 = new Paragraph("REGISTRO DE AUDITORES EXTERNOS PENDIENTES DE REINSCRIPCIÓN",fontTituloBranco);
	        cell10 = new PdfPCell(texto14);
	        cell10.setColspan(table.getNumberOfColumns());
	        cell10.setBorder(0);
	        cell10.setHorizontalAlignment(Element.ALIGN_CENTER);
	        cell10.setBackgroundColor(BaseColor.BLACK);
	        
	        table.addCell(cell10);
	        
	        table.addCell(cell);
	        table.addCell(cell2);
	        table.addCell(cell3);
	        table.addCell(cell4);
	        table.addCell(cell5);
	        table.addCell(cell6);
	        table.addCell(cell7);
	        table.addCell(cell8);
	        table.addCell(cell9);
	        
	        if(ci)
		        table.addCell(cell77);
	        
	        cont = 1;
	        
	        for(Iterator i = lista2.iterator() ; i.hasNext() ; )
	        {
	        	String str = (String) i.next();
	        	
	        	String[] dados = str.split(";");
	        	
	        	String resolucao = dados[0];
	        	String dataR = dados[1];
	        	String dataV = dados[2];
	        	String nome = dados[3];
	        	String endereco = dados[4];
	        	String tel = dados[5];
	        	String fax = dados[6];
	        	String email = dados[7];
	        	String ruc = dados[8];
	        	
	        	Paragraph texto15 = new Paragraph(new Integer(cont).toString(),fontTexto);
		        PdfPCell cell11 = new PdfPCell(texto15);
		        cell11.setHorizontalAlignment(Element.ALIGN_CENTER);
		        cell11.setBorder(0);
		        
		        table.addCell(cell11);
		        
		        texto15 = new Paragraph(resolucao,fontTexto);
		        cell11 = new PdfPCell(texto15);
		        cell11.setHorizontalAlignment(Element.ALIGN_CENTER);
		        cell11.setBorder(0);
		        
		        table.addCell(cell11);
		        
		        texto15 = new Paragraph(dataR,fontTexto);
		        cell11 = new PdfPCell(texto15);
		        cell11.setHorizontalAlignment(Element.ALIGN_CENTER);
		        cell11.setBorder(0);
		        
		        table.addCell(cell11);
		        
		        texto15 = new Paragraph(dataV,fontTexto);
		        cell11 = new PdfPCell(texto15);
		        cell11.setHorizontalAlignment(Element.ALIGN_CENTER);
		        cell11.setBorder(0);
		        
		        table.addCell(cell11);
		        
		        texto15 = new Paragraph(nome,fontTexto);
		        cell11 = new PdfPCell(texto15);
		        cell11.setBorder(0);
		        
		        table.addCell(cell11);
		        
		        texto15 = new Paragraph(endereco,fontTexto);
		        cell11 = new PdfPCell(texto15);
		        cell11.setBorder(0);
		        
		        table.addCell(cell11);
		        
		        texto15 = new Paragraph(tel,fontTexto);
		        cell11 = new PdfPCell(texto15);
		        cell11.setHorizontalAlignment(Element.ALIGN_CENTER);
		        cell11.setBorder(0);
		        
		        table.addCell(cell11);
		        
		        texto15 = new Paragraph(fax,fontTexto);
		        cell11 = new PdfPCell(texto15);
		        cell11.setHorizontalAlignment(Element.ALIGN_CENTER);
		        cell11.setBorder(0);
		        
		        table.addCell(cell11);
		        
		        texto15 = new Paragraph(email,fontTexto);
		        cell11 = new PdfPCell(texto15);
		        cell11.setBorder(0);
		        
		        table.addCell(cell11);
		        
		        if(ci)
		        {
		        	texto15 = new Paragraph(ruc,fontTexto);
				    cell11 = new PdfPCell(texto15);
				    cell11.setBorder(0);
				    cell11.setHorizontalAlignment(Element.ALIGN_CENTER);
			        table.addCell(cell11);
		        }
		        
		        cont++;
	        }
	        
	        texto14 = new Paragraph("FECHA DE ACTUALIZACION " + new SimpleDateFormat("dd/MM/yyyy").format(data),fontTituloBranco);
	        cell10 = new PdfPCell(texto14);
	        cell10.setColspan(9);
	        cell10.setBorder(0);
	        cell10.setHorizontalAlignment(Element.ALIGN_CENTER);
	        cell10.setBackgroundColor(BaseColor.BLACK);
	        
	        table.addCell(cell10);
	        
	        table.setSpacingAfter(10);
	        
	        documento.add(table);
	        
	        texto14 = new Paragraph("AVISO IMPORTANTE: Las firmas que están con reinscripcion pendiente no podrán firmar contrato de prestacion de servicios con las compañías aseguradoras conforme la Resolución SS.SG. Nº 242/04 del 30.06.04.",fontTitulo);
	        texto14.setAlignment(Element.ALIGN_JUSTIFIED);
	        texto14.setSpacingAfter(15);
	        
	        documento.add(texto14);
	        
	        texto14 = new Paragraph("AVISO IMPORTANTE: Considerando la normativa vigente en la cual se expresa que Ninguna Firma de Auditoria Independiente podrá auditar a una misma Entidad de Seguros y/o Reaseguros por más de 5 (cinco) ejercicios consecutivos , debiendo transcurrir por lo menos 1 (un) año para que proceda a una nueva contratación. Se deben considerar los ejercicios cerrados siguientes: Ejercicio 2005/2006, Ejercicio 2006/2007, Ejercicio 2007/2008, Ejercicio 2008/2009 y Ejercicio 2009/2010, para la contratación de los servicios de auditoria independiente.",fontTitulo);
	        texto14.setAlignment(Element.ALIGN_JUSTIFIED);
	        
	        documento.add(texto14);
	        
	        Paragraph p = new Paragraph("Elaborado por: ICF - Intendencia de Control Financiero.",fontTituloPNegrito);
       	 	p.setSpacingBefore(7);
	        documento.add(p);
	        
	        p = new Paragraph(textoUsuario,fontTituloPNegrito);
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
