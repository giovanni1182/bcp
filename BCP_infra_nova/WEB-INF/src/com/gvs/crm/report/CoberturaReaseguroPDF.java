package com.gvs.crm.report;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import com.gvs.crm.model.Apolice;
import com.gvs.crm.model.Aseguradora;
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

public class CoberturaReaseguroPDF extends PDF
{
	public CoberturaReaseguroPDF(Aseguradora aseguradora,Date dataInicio,Date dataFim, String situacao, Collection<Apolice> apolices, String tipoContrato, String textoUsuario, String titulo) throws Exception
	{
		Document documento = new Document(PageSize.A4,5,5,5,5);
		
		try 
	    {
			String caminho = "C:/tmp/" + new Date().getTime() + ".pdf";
			
			DecimalFormat formataValor = new DecimalFormat("#,##0.00");
	        
	        this.setCaminho(caminho);
	        
	        Font fontTituloNegrito = new Font(Font.FontFamily.TIMES_ROMAN, 9,Font.BOLD);
	        Font fontTextoNormal = new Font(Font.FontFamily.TIMES_ROMAN, 8,Font.NORMAL);
	        Font fontTextoNegrito = new Font(Font.FontFamily.TIMES_ROMAN, 8,Font.BOLD);
	        
	        PdfWriter.getInstance(documento, new FileOutputStream(caminho));
	        documento.open();
	        
	        String dirImages = InfraProperties.getInstance().getProperty("report.images.url");
	        
	        Image img = Image.getInstance(dirImages + "/bcp.jpg");
	        img.scalePercent(50.0f);
	        img.setAlignment(Element.ALIGN_CENTER);
	        
	        PdfPTable table = new PdfPTable(new float[]{0.30f, 0.75f});
	        
	        PdfPCell cell = new PdfPCell(img);
	        cell.setBorder(0);
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        table.addCell(cell);
	        
	        Paragraph p = new Paragraph("SUPERINTENDENCIA DE SEGUROS\n"+titulo.toUpperCase(),fontTituloNegrito);
	        cell = new PdfPCell(p);
	        cell.setBorder(0);
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        table.addCell(cell);
	        
	        table.setSpacingAfter(10);
	        
	        documento.add(table);
	        
	        if(aseguradora!=null)
	        	p = new Paragraph("Aseguradora: " + aseguradora.obterNome(),fontTituloNegrito);
	        else
	        	p = new Paragraph("Aseguradora: Todas",fontTituloNegrito);
	        
	        documento.add(p);
	        
	        p = new Paragraph("Pólizas desde: " + new SimpleDateFormat("dd/MM/yyyy").format(dataInicio) + " hasta " + new SimpleDateFormat("dd/MM/yyyy").format(dataFim),fontTituloNegrito);
	        documento.add(p);
	        
	        if(!situacao.equals("0"))
	        	p = new Paragraph("Situacion: " + situacao,fontTituloNegrito);
	        else
	        	p = new Paragraph("Situacion: Todas",fontTituloNegrito);
	        documento.add(p);
	        
	        if(!tipoContrato.equals(""))
	        {
		        p = new Paragraph("Tipo Contrato: " + tipoContrato ,fontTituloNegrito);
		        documento.add(p);
	        }
	        
	        p = new Paragraph(apolices.size() + " Póliza(s)",fontTituloNegrito);
	        p.setSpacingAfter(10);
	        documento.add(p);
	        
	        table = new PdfPTable(new float[]{0.08f, 0.08f, 0.08f, 0.30f, 0.08f, 0.12f, 0.09f, 0.09f, 0.08f});
	        table.setWidthPercentage(100f);
	        
	        p = new Paragraph("Nº",fontTextoNegrito);
	        cell = new PdfPCell(p);
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
	        table.addCell(cell);
	        
	        p = new Paragraph("Vig. Inicio",fontTextoNegrito);
	        cell = new PdfPCell(p);
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
	        table.addCell(cell);
	        
	        p = new Paragraph("Vig. Final",fontTextoNegrito);
	        cell = new PdfPCell(p);
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
	        table.addCell(cell);
	        
	        p = new Paragraph("Assegurado",fontTextoNegrito);
	        cell = new PdfPCell(p);
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
	        table.addCell(cell);
	        
	        p = new Paragraph("Situacion",fontTextoNegrito);
	        cell = new PdfPCell(p);
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
	        table.addCell(cell);
	        
	        p = new Paragraph("Capital Gs",fontTextoNegrito);
	        cell = new PdfPCell(p);
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
	        table.addCell(cell);
	        
	        p = new Paragraph("Prima Gs",fontTextoNegrito);
	        cell = new PdfPCell(p);
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
	        table.addCell(cell);
	        
	        p = new Paragraph("Comisión Gs",fontTextoNegrito);
	        cell = new PdfPCell(p);
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
	        table.addCell(cell);
	        
	        p = new Paragraph("Cap. Dólar USA",fontTextoNegrito);
	        cell = new PdfPCell(p);
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
	        table.addCell(cell);
	        
	        for(Iterator<Apolice> i = apolices.iterator() ; i.hasNext() ; )
			{
				Apolice apolice = i.next();
				
				p = new Paragraph(apolice.obterNumeroApolice(),fontTextoNormal);
		        cell = new PdfPCell(p);
		        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		        table.addCell(cell);
		        
		        p = new Paragraph(new SimpleDateFormat("dd/MM/yyyy").format(apolice.obterDataPrevistaInicio()),fontTextoNormal);
		        cell = new PdfPCell(p);
		        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		        table.addCell(cell);
		        
		        p = new Paragraph(new SimpleDateFormat("dd/MM/yyyy").format(apolice.obterDataPrevistaConclusao()),fontTextoNormal);
		        cell = new PdfPCell(p);
		        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		        table.addCell(cell);
		        
		        p = new Paragraph(apolice.obterNomeAsegurado(),fontTextoNormal);
		        cell = new PdfPCell(p);
		        table.addCell(cell);
		        
		        p = new Paragraph(apolice.obterSituacaoSeguro(),fontTextoNormal);
		        cell = new PdfPCell(p);
		        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		        table.addCell(cell);
		        
		        p = new Paragraph(formataValor.format(apolice.obterCapitalGs()),fontTextoNormal);
		        cell = new PdfPCell(p);
		        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		        table.addCell(cell);
		        
		        p = new Paragraph(formataValor.format(apolice.obterPrimaGs()),fontTextoNormal);
		        cell = new PdfPCell(p);
		        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		        table.addCell(cell);
		        
		        p = new Paragraph(formataValor.format(apolice.obterComissaoGs()),fontTextoNormal);
		        cell = new PdfPCell(p);
		        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		        table.addCell(cell);
		        
		        String tipoMoeda = apolice.obterTipoMoedaCapitalGuarani();
				double capitalMe = 0;
				if(tipoMoeda.equals("Dólar USA"))
					capitalMe = apolice.obterCapitalMe();
				
				p = new Paragraph(formataValor.format(capitalMe),fontTextoNormal);
			    cell = new PdfPCell(p);
			    cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			    table.addCell(cell);
			}
	        
	        table.setSpacingAfter(10);
	        documento.add(table);
	        
	        p = new Paragraph(textoUsuario,fontTextoNegrito);
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
