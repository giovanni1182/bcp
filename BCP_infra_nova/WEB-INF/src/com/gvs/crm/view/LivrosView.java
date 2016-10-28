package com.gvs.crm.view;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

import com.gvs.crm.component.AseguradorasSelect;
import com.gvs.crm.component.TipoLivroSelect;
import com.gvs.crm.model.Aseguradora;
import com.gvs.crm.model.AseguradoraHome;
import com.gvs.crm.model.CRMModelManager;
import com.gvs.crm.model.Entidade;
import com.gvs.crm.model.Livro;
import com.gvs.crm.model.LivroHome;
import com.gvs.crm.model.SampleModelManager;
import com.gvs.crm.model.UploadedFile;
import com.gvs.crm.model.UploadedFileHome;
import com.gvs.crm.model.Uteis;

import infra.control.Action;
import infra.security.User;
import infra.view.Block;
import infra.view.Button;
import infra.view.InputInteger;
import infra.view.Label;
import infra.view.Link;
import infra.view.Space;
import infra.view.Table;
import infra.view.View;

public class LivrosView extends PortalView
{
	private Aseguradora aseguradora;
	private String tipo;
	private int mes,ano;
	private boolean listar;
	
	public LivrosView(Aseguradora aseguradora, String tipo, int mes, int ano, boolean listar) throws Exception
	{
		this.aseguradora = aseguradora;
		this.tipo = tipo;
		this.mes = mes;
		this.ano = ano;
		this.listar = listar;
	}
	
	public View getBody(User user, Locale locale, Properties properties) throws Exception
	{
		CRMModelManager mm = new CRMModelManager(user);
		LivroHome livroHome = (LivroHome) mm.getHome("LivroHome");
		AseguradoraHome aseguradoraHome = (AseguradoraHome) mm.getHome("AseguradoraHome");
		Collection<Aseguradora> aseguradoras = new ArrayList<Aseguradora>();
		if(this.aseguradora == null)
			aseguradoras = aseguradoraHome.obterAseguradoras();
		else
			aseguradoras.add(aseguradora);
		
		Table mainTable = new Table(1);
		mainTable.setWidth("100%");
		
		Table table = new Table(2);
		table.addHeader("Aseguradora:");
		table.add(new AseguradorasSelect("aseguradoraId", aseguradora, true));
		table.addHeader("Tipo:");
		table.add(new TipoLivroSelect("tipo", tipo, true));
		table.addHeader("Mes:");
		
		Block block = new Block(Block.HORIZONTAL);
		block.add(new InputInteger("mes", mes, 2));
		block.add(new Space(2));
		
		Label l = new Label("Año:");
		l.setBold(true);
		block.add(l);
		block.add(new Space(2));
		block.add(new InputInteger("ano", ano, 4));
		l = new Label("(Ex.: 1980)");
		block.add(new Space(2));
		block.add(l);
		table.add(block);
		
		Button consultarButton = new Button("Listar", new Action("consultarLivros"));
		table.addFooter(consultarButton);
		
		Button excelButton = new Button("Generar Excel", new Action("consultarLivros"));
		excelButton.getAction().add("excel", true);
		
		table.addFooter(excelButton);
		
		mainTable.add(table);
		
		if(this.listar)
		{
			Uteis uteis = new Uteis();
			Collection<String> mimeTypesPDF = uteis.obterMimeTypesPDF();
			Collection<String> mimeTypesWord = uteis.obterMimeTypesWord();
			Collection<String> mimeTypesExcel = uteis.obterMimeTypesExcel();
			
			//Collection<String> nomeLivros = new ArrayList<String>();
			Map<String,Map<String,String>> grupoLivros = uteis.obterGruposELivros();
			SampleModelManager mm2 = new SampleModelManager();
	        UploadedFileHome home = (UploadedFileHome) mm2.getHome("UploadedFileHome");
			
			if(!tipo.equals(""))
			{
				for(String nomeGrupo : grupoLivros.keySet())
				{
					if(grupoLivros.get(nomeGrupo).containsKey(tipo))
					{
						String nomeLivro = grupoLivros.get(nomeGrupo).get(tipo);
						
						Map<String,String> livros = new TreeMap<String, String>();
						livros.put(nomeLivro, nomeLivro);
						
						grupoLivros.clear();
						grupoLivros.put(nomeGrupo, livros);
						break;
					}
				}
			}
			
			Link link, link2;
			Livro livro,ultimoLivro; 
			Collection arquivos;
			long idPdf;
    		long idWord;
    		long idExcel;
    		UploadedFile arquivo;
    		String mimeType;
    		
			for(Aseguradora aseg : aseguradoras)
			{
				table = new Table(6);
				table.setWidth("55%");
				table.addStyle(Table.STYLE_ALTERNATE);
				
				table.addSubtitle(aseg.obterNome());
				
				table.setNextHAlign(Table.HALIGN_CENTER);
				table.addHeader("LIBROS ELECTRÓNICOS");
				table.setNextHAlign(Table.HALIGN_CENTER);
				table.addHeader("FRECUENCIA");
				table.setNextHAlign(Table.HALIGN_CENTER);
				table.addHeader("WORD");
				table.setNextHAlign(Table.HALIGN_CENTER);
				table.addHeader("EXCEL");
				table.setNextHAlign(Table.HALIGN_CENTER);
				table.addHeader("PDF");
				table.setNextHAlign(Table.HALIGN_CENTER);
				table.addHeader("ÚLTIMO ENVÍO");
				
				for(String nomeGrupo : grupoLivros.keySet())
				{
					table.setNextColSpan(table.getColumns());
					table.addHeader(nomeGrupo);
					
					Map<String,String> nomeLivros = grupoLivros.get(nomeGrupo);
					
					for(String nomeLivro : nomeLivros.values())
					{
						block = new Block(Block.HORIZONTAL);
						block.add(new Space(5));
						block.add(new Label(nomeLivro));
						table.add(block);
						
						table.add(uteis.obterFrequenciaLivro(nomeLivro));
						
						livro = livroHome.obterLivro(aseg, nomeLivro, mes, ano);
						
						if(livro!=null)
						{
							arquivos = home.getAllUploadedFiles(livro);
							
				    		idPdf = 0;
				    		idWord = 0;
				    		idExcel = 0;
							
							for(Iterator k = arquivos.iterator() ; k.hasNext() ; )
							{
								arquivo = (UploadedFile) k.next();
								
								mimeType = arquivo.getType();
								
								if(mimeTypesPDF.contains(mimeType))
									idPdf = arquivo.getId();
								else if(mimeTypesWord.contains(mimeType))
									idWord = arquivo.getId();
								else if(mimeTypesExcel.contains(mimeType))
									idExcel = arquivo.getId();
							}
							
							table.setNextHAlign(Table.HALIGN_CENTER);
							if(idWord > 0)
							{
								link2 = new Link("Sí", new Action("download"));
								link2.setNote("Download");
								link2.getAction().add("id", idWord);
								
								table.add(link2);
							}
							else
								table.add("No");
							
							table.setNextHAlign(Table.HALIGN_CENTER);
							if(idExcel > 0)
							{
								link2 = new Link("Sí", new Action("download"));
								link2.setNote("Download");
								link2.getAction().add("id", idExcel);
								
								table.add(link2);
							}
							else
								table.add("No");
							
							table.setNextHAlign(Table.HALIGN_CENTER);
							if(idPdf > 0)
							{
								link2 = new Link("Sí", new Action("download"));
								link2.setNote("Download");
								link2.getAction().add("id", idPdf);
								
								table.add(link2);
							}
							else
								table.add("No");
						}
						else
						{
							table.setNextHAlign(Table.HALIGN_CENTER);
							table.add("No");
							table.setNextHAlign(Table.HALIGN_CENTER);
							table.add("No");
							table.setNextHAlign(Table.HALIGN_CENTER);
							table.add("No");
						}
						
						ultimoLivro = livroHome.obterUltimoLivro(aseg, nomeLivro);
						table.setNextHAlign(Table.HALIGN_CENTER);
						if(ultimoLivro!=null)
						{
							String mesStr = ultimoLivro.obterMes()+"";
							if(mesStr.length() == 1)
								mesStr = "0"+mesStr;
							
							link = new Link(mesStr + "/" + ultimoLivro.obterAno(), new Action("visualizarEvento"));
							link.getAction().add("id", ultimoLivro.obterId());
							link.setNovaJanela(true);
							table.add(link);
						}
						else
							table.add("");
					}
					
					table.setNextColSpan(table.getColumns());
					table.add(new Space());
				}
				mainTable.add(table);
			}
			
			
			
			/*table = new Table(2);
			if(tipo.equals(""))
			{
				nomeLivros = uteis.obterNomeLivros();
				table = new Table(nomeLivros.size() + 1);
				table.setWidth("100%");
			}
			else
				nomeLivros.add(tipo);
			
			table.addStyle(Table.STYLE_ALTERNATE);
			table.addSubtitle("");
			
			SampleModelManager mm2 = new SampleModelManager();
	        UploadedFileHome home = (UploadedFileHome) mm2.getHome("UploadedFileHome");
	        
	        table.add("");
	    	for(Iterator<String> i = nomeLivros.iterator() ; i.hasNext() ; )
	    	{
	    		String nomeLivro = i.next();
	    		
	    		table.setNextHAlign(Table.HALIGN_CENTER);
	    		table.addHeader(uteis.obterNomeLivroAreviado(nomeLivro));
	    	}
	    	
	    	table.add("");
	    	for(int i = 0 ; i < nomeLivros.size() ; i++)
	    	{
	    		Table tableAux = new Table(3);
	    		tableAux.setWidth("100%");
	    		
	    		tableAux.setNextHAlign(Table.HALIGN_CENTER);
	    		tableAux.addHeader("W");
	    		tableAux.setNextHAlign(Table.HALIGN_CENTER);
	    		tableAux.addHeader("E");
	    		tableAux.setNextHAlign(Table.HALIGN_CENTER);
	    		tableAux.addHeader("P");
	    		
	    		table.setNextHAlign(Table.HALIGN_CENTER);
	    		table.add(tableAux);
	    	}
			
			for(Iterator<Aseguradora> i = aseguradoras.iterator() ; i.hasNext() ; )
			{
				Aseguradora aseg = i.next();
				
				table.add(aseg.obterNome());
				
				for(Iterator<String> j = nomeLivros.iterator() ; j.hasNext() ; )
				{
					String nomeLivro = j.next();
					
					Livro livro = livroHome.obterLivro(aseg, nomeLivro, mes, ano);
					Table tableAux = new Table(3);
		    		tableAux.setWidth("100%");
					
					if(livro!=null)
					{
						Collection arquivos = home.getAllUploadedFiles(livro);
						
			    		long idPdf = 0;
			    		long idWord = 0;
			    		long idExcel = 0;
						
						for(Iterator k = arquivos.iterator() ; k.hasNext() ; )
						{
							UploadedFile arquivo = (UploadedFile) k.next();
							
							String mimeType = arquivo.getType();
							
							if(mimeTypesPDF.contains(mimeType))
								idPdf = arquivo.getId();
							else if(mimeTypesWord.contains(mimeType))
								idWord = arquivo.getId();
							else if(mimeTypesExcel.contains(mimeType))
								idExcel = arquivo.getId();
						}
						
						tableAux.setNextHAlign(Table.HALIGN_CENTER);
						if(idWord > 0)
						{
							Link link2 = new Link("S", new Action("download"));
							link2.setNote("Download");
							link2.getAction().add("id", idWord);
							
							tableAux.add(link2);
						}
						else
							tableAux.add("N");
						
						tableAux.setNextHAlign(Table.HALIGN_CENTER);
						if(idExcel > 0)
						{
							Link link2 = new Link("S", new Action("download"));
							link2.setNote("Download");
							link2.getAction().add("id", idExcel);
							
							tableAux.add(link2);
						}
						else
							tableAux.add("N");
						
						tableAux.setNextHAlign(Table.HALIGN_CENTER);
						if(idPdf > 0)
						{
							Link link2 = new Link("S", new Action("download"));
							link2.setNote("Download");
							link2.getAction().add("id", idPdf);
							
							tableAux.add(link2);
						}
						else
							tableAux.add("N");
						
						tableAux.setNextColSpan(tableAux.getColumns());
						tableAux.setNextHAlign(Table.HALIGN_CENTER);
						Link link = new Link("Actual: " + livro.obterMes() + "/" + livro.obterAno(), new Action("visualizarEvento"));
						link.getAction().add("id", livro.obterId());
						link.setNovaJanela(true);
						tableAux.add(link);
					}
					else
					{
						tableAux.setNextHAlign(Table.HALIGN_CENTER);
						tableAux.add("N");
						tableAux.setNextHAlign(Table.HALIGN_CENTER);
						tableAux.add("N");
						tableAux.setNextHAlign(Table.HALIGN_CENTER);
						tableAux.add("N");
					}
					
					Livro ultimoLivro = livroHome.obterUltimoLivro(aseg, nomeLivro);
					tableAux.setNextColSpan(tableAux.getColumns());
					tableAux.setNextHAlign(Table.HALIGN_CENTER);
					Link link = null;
					if(ultimoLivro!=null)
					{
						link = new Link("Ultimo: " + ultimoLivro.obterMes() + "/" + ultimoLivro.obterAno(), new Action("visualizarEvento"));
						link.getAction().add("id", ultimoLivro.obterId());
						link.setNovaJanela(true);
						tableAux.add(link);
					}
					else
						tableAux.add("Ultimo: xx/xxxx");
					
					table.setNextHAlign(Table.HALIGN_CENTER);
					table.add(tableAux);
				}
			}*/
			
			//mainTable.add(table);
		}
		
		return mainTable;
	}

	public String getSelectedGroup() throws Exception
	{
		return null;
	}

	public String getSelectedOption() throws Exception
	{
		return null;
	}

	public View getTitle() throws Exception
	{
		return new Label("Libros Electrónicos");
	}

	public Entidade getOrigemMenu() throws Exception
	{
		return null;
	}
}