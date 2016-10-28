package com.gvs.crm.report;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
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
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import infra.config.InfraProperties;

public class MaioresReaseguradorasPorSecaoPDF extends PDF
{
	public MaioresReaseguradorasPorSecaoPDF(String tipoValor,Date dataInicio,Date dataFim, String situacao, double dolar, double euro, double real, double pesoArg, double pesoUru, double yen, Collection<String> dados, String secao, String modalidade, String textoUsuario) throws Exception
	{
		Document documento = new Document(PageSize.A4);
		try 
	    {
			String caminho = "C:/tmp/" + new Date().getTime() + ".pdf";
			
			DecimalFormat formataValor = new DecimalFormat("#,##0.00");
	        
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
	        
	        PdfPTable table = new PdfPTable(new float[]{0.30f, 0.75f});
	        
	        PdfPCell cell = new PdfPCell(img);
	        cell.setBorder(0);
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        table.addCell(cell);
	        
	        Paragraph p = new Paragraph("SUPERINTENDENCIA DE SEGUROS\nMayores Reaseguradoras por Sección".toUpperCase(),fontTituloNegrito);
	        cell = new PdfPCell(p);
	        cell.setBorder(0);
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        table.addCell(cell);
	        
	        documento.add(table);
	        
	        if(tipoValor.equals("valorPrima"))
	     	   tipoValor = "Prima";
	        else if(tipoValor.equals("valorCapital"))
	     	   tipoValor = "Capital";
	        else if(tipoValor.equals("valorComissao"))
	     	   tipoValor = "Comisión";
	        
	        if(!secao.equals(""))
	        	p = new Paragraph("Sección: " + secao, fontTituloNegrito);
	        else
	        	p = new Paragraph("Sección: Todas", fontTituloNegrito);
	        p.setSpacingBefore(10);
	        documento.add(p);
	        
	        if(!modalidade.equals(""))
	        	p = new Paragraph("Modalidad: " + modalidade, fontTituloNegrito);
	        else
	        	p = new Paragraph("Modalidad: Todas", fontTituloNegrito);
	        documento.add(p);
	        
	        p = new Paragraph("Consulta por: " + tipoValor, fontTituloNegrito);
	        documento.add(p);
	        
	        p = new Paragraph("Pólizas Vigentes desde: " + new SimpleDateFormat("dd/MM/yyyy").format(dataInicio) + " hasta " + new SimpleDateFormat("dd/MM/yyyy").format(dataFim), fontTituloNegrito);
	        documento.add(p);
	        
	        if(situacao.equals("0"))
	        	p = new Paragraph("Situación: Todas", fontTituloNegrito);
	        else
	        	p = new Paragraph("Situación: " + situacao, fontTituloNegrito);
	        documento.add(p);
	        
	        table = new PdfPTable(new float[]{0.15f, 0.15f, 0.15f, 0.15f, 0.15f, 0.15f});
	        table.setWidthPercentage(100f);
	        table.setSpacingAfter(10);
	        
	        p = new Paragraph("Cotización del día en Guaraní", fontTituloNegrito);
	        cell = new PdfPCell(p);
	        cell.setBorder(0);
	        cell.setColspan(table.getNumberOfColumns());
	        table.addCell(cell);
	        
	        p = new Paragraph("Dólar USA: " + formataValor.format(dolar), fontTextoNormal);
	        cell = new PdfPCell(p);
	        cell.setBorder(0);
	        table.addCell(cell);
	        
	        p = new Paragraph("Euro: " + formataValor.format(euro), fontTextoNormal);
	        cell = new PdfPCell(p);
	        cell.setBorder(0);
	        table.addCell(cell);
	        
	        p = new Paragraph("Real: " + formataValor.format(real), fontTextoNormal);
	        cell = new PdfPCell(p);
	        cell.setBorder(0);
	        table.addCell(cell);
	        
	        p = new Paragraph("Peso Arg.: " + formataValor.format(pesoArg), fontTextoNormal);
	        cell = new PdfPCell(p);
	        cell.setBorder(0);
	        table.addCell(cell);
	        
	        p = new Paragraph("Peso Uru.: " + formataValor.format(pesoUru), fontTextoNormal);
	        cell = new PdfPCell(p);
	        cell.setBorder(0);
	        table.addCell(cell);
	        
	        p = new Paragraph("Yen: " + formataValor.format(yen), fontTextoNormal);
	        cell = new PdfPCell(p);
	        cell.setBorder(0);
	        table.addCell(cell);
	        
	        documento.add(table);
	        
	        table = new PdfPTable(new float[]{0.34f, 0.22f, 0.22f, 0.22f});
	        table.setWidthPercentage(100f);
	        table.setSpacingAfter(10);
	        
        	p = new Paragraph("Sección", fontTextoNegrito);
	        cell = new PdfPCell(p);
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
	        table.addCell(cell);
	        
	        double totalPrima = 0;
	   		double totalCapital = 0;
	   		double totalComissao = 0;
	        
	        if(tipoValor.equals("Prima"))
			{
	        	p = new Paragraph("Prima", fontTextoNegrito);
		        cell = new PdfPCell(p);
		        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
		        table.addCell(cell);
		        
		        p = new Paragraph("Capital", fontTextoNegrito);
		        cell = new PdfPCell(p);
		        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
		        table.addCell(cell);
		        
		        p = new Paragraph("Comisión", fontTextoNegrito);
		        cell = new PdfPCell(p);
		        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
		        table.addCell(cell);
			}
	        else if(tipoValor.equals("Capital"))
	        {
	        	p = new Paragraph("Capital", fontTextoNegrito);
			    cell = new PdfPCell(p);
			    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
			    table.addCell(cell);
			        
	        	p = new Paragraph("Prima", fontTextoNegrito);
		        cell = new PdfPCell(p);
		        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
		        table.addCell(cell);
		        
		        p = new Paragraph("Comisión", fontTextoNegrito);
		        cell = new PdfPCell(p);
		        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
		        table.addCell(cell);
	        }
	        else
	        {
	        	p = new Paragraph("Comisión", fontTextoNegrito);
		        cell = new PdfPCell(p);
		        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
		        table.addCell(cell);
		        
	        	p = new Paragraph("Capital", fontTextoNegrito);
			    cell = new PdfPCell(p);
			    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
			    table.addCell(cell);
			        
	        	p = new Paragraph("Prima", fontTextoNegrito);
		        cell = new PdfPCell(p);
		        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
		        table.addCell(cell);
	        }
	        
	        for(Iterator<String> i = dados.iterator() ; i.hasNext() ; )
			{
				String linhaSuja = i.next();
				
				String linha2[] = linhaSuja.split(";");
				
				String nome = linha2[0];
				double capital = Double.valueOf(linha2[1]);
				double prima = Double.valueOf(linha2[2]);
				double comissao = Double.valueOf(linha2[3]);
				
				totalCapital+=capital;
				totalPrima+=prima;
				totalComissao+=comissao;
				
				p = new Paragraph(nome, fontTextoNormal);
		        cell = new PdfPCell(p);
		        table.addCell(cell);
		        
		        if(tipoValor.equals("Prima"))
				{
		        	p = new Paragraph(formataValor.format(prima), fontTextoNormal);
			        cell = new PdfPCell(p);
			        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			        table.addCell(cell);
			        
			        p = new Paragraph(formataValor.format(capital), fontTextoNormal);
			        cell = new PdfPCell(p);
			        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			        table.addCell(cell);
			        
			        p = new Paragraph(formataValor.format(comissao), fontTextoNormal);
			        cell = new PdfPCell(p);
			        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			        table.addCell(cell);
				}
		        else if(tipoValor.equals("Capital"))
		        {
		        	p = new Paragraph(formataValor.format(capital), fontTextoNormal);
			        cell = new PdfPCell(p);
			        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			        table.addCell(cell);
			        
		        	p = new Paragraph(formataValor.format(prima), fontTextoNormal);
			        cell = new PdfPCell(p);
			        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			        table.addCell(cell);
			        
			        p = new Paragraph(formataValor.format(comissao), fontTextoNormal);
			        cell = new PdfPCell(p);
			        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			        table.addCell(cell);
		        }
		        else
		        {
		        	p = new Paragraph(formataValor.format(comissao), fontTextoNormal);
			        cell = new PdfPCell(p);
			        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			        table.addCell(cell);
			        
		        	p = new Paragraph(formataValor.format(capital), fontTextoNormal);
			        cell = new PdfPCell(p);
			        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			        table.addCell(cell);
			        
		        	p = new Paragraph(formataValor.format(prima), fontTextoNormal);
			        cell = new PdfPCell(p);
			        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			        table.addCell(cell);
		        }
			}
	        
	        p = new Paragraph("TOTAL", fontTextoNegrito);
	        cell = new PdfPCell(p);
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        table.addCell(cell);
	        
	        if(tipoValor.equals("Prima"))
			{
	        	p = new Paragraph(formataValor.format(totalPrima), fontTextoNegrito);
		        cell = new PdfPCell(p);
		        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		        table.addCell(cell);
		        
		        p = new Paragraph(formataValor.format(totalCapital), fontTextoNegrito);
		        cell = new PdfPCell(p);
		        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		        table.addCell(cell);
		        
		        p = new Paragraph(formataValor.format(totalComissao), fontTextoNegrito);
		        cell = new PdfPCell(p);
		        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		        table.addCell(cell);
			}
	        else if(tipoValor.equals("Capital"))
	        {
	        	p = new Paragraph(formataValor.format(totalCapital), fontTextoNegrito);
		        cell = new PdfPCell(p);
		        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		        table.addCell(cell);
		        
	        	p = new Paragraph(formataValor.format(totalPrima), fontTextoNegrito);
		        cell = new PdfPCell(p);
		        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		        table.addCell(cell);
		        
		        p = new Paragraph(formataValor.format(totalComissao), fontTextoNegrito);
		        cell = new PdfPCell(p);
		        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		        table.addCell(cell);
	        }
	        else
	        {
	        	p = new Paragraph(formataValor.format(totalComissao), fontTextoNegrito);
		        cell = new PdfPCell(p);
		        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		        table.addCell(cell);
		        
	        	p = new Paragraph(formataValor.format(totalCapital), fontTextoNegrito);
		        cell = new PdfPCell(p);
		        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		        table.addCell(cell);
		        
	        	p = new Paragraph(formataValor.format(totalPrima), fontTextoNegrito);
		        cell = new PdfPCell(p);
		        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		        table.addCell(cell);
	        }
	        
	        documento.add(table);
	        
	        p = new Paragraph(textoUsuario, fontTextoNegrito);
	        p.setAlignment(Element.ALIGN_CENTER);
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
