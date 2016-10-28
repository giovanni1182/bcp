package com.gvs.crm.view;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

import com.gvs.crm.component.AseguradorasSelect;
import com.gvs.crm.component.TipoLivroSelect;
import com.gvs.crm.model.Aseguradora;
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
import infra.view.InputDate;
import infra.view.Label;
import infra.view.Link;
import infra.view.Space;
import infra.view.Table;
import infra.view.View;

public class LivrosPendentesView extends PortalView
{
	private Aseguradora aseguradora;
	private String tipo;
	private Date dataInicio, dataFim;
	private Collection<Aseguradora> aseguradoras;
	private Uteis uteis = new Uteis();
	
	public LivrosPendentesView(Aseguradora aseguradora, String tipo, Date dataInicio, Date dataFim, Collection<Aseguradora> aseguradoras)
	{
		this.aseguradora = aseguradora;
		this.tipo = tipo;
		this.dataInicio = dataInicio;
		this.dataFim = dataFim;
		this.aseguradoras = aseguradoras;
	}
	
	public View getBody(User user, Locale locale, Properties properties) throws Exception
	{
		CRMModelManager mm = new CRMModelManager(user);
		LivroHome livroHome = (LivroHome) mm.getHome("LivroHome");
		
		Table mainTable = new Table(1);
		mainTable.setWidth("100%");
		
		Table table = new Table(2);
		table.addHeader("Aseguradora:");
		table.add(new AseguradorasSelect("aseguradoraId", aseguradora, true));
		table.addHeader("Tipo:");
		table.add(new TipoLivroSelect("tipo", tipo, true));
		table.addHeader("Periodo:");
		
		Block block = new Block(Block.HORIZONTAL);
		block.add(new InputDate("dataInicio", this.dataInicio));
		block.add(new Space(2));
		block.add(new Label("hasta"));
		block.add(new Space(2));
		block.add(new InputDate("dataFim", this.dataFim));
		table.add(block);
		
		Button consultarButton = new Button("Listar", new Action("livrosPendentes"));
		table.addFooter(consultarButton);
		
		Button excelButton = new Button("Generar Excel", new Action("livrosPendentes"));
		excelButton.getAction().add("excel", true);
		table.addFooter(excelButton);
		
		mainTable.add(table);
		
		if(this.aseguradoras.size() > 0 && dataInicio!=null && dataFim!=null)
		{
			Collection<String> mimeTypesPDF = uteis.obterMimeTypesPDF();
			Collection<String> mimeTypesWord = uteis.obterMimeTypesWord();
			Collection<String> mimeTypesExcel = uteis.obterMimeTypesExcel();
			
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
		
			Calendar c = Calendar.getInstance();
			c.setTime(dataInicio);
			
			int contMes = 0;
			
			while(c.getTime().compareTo(dataFim) <=0)
			{
				contMes++;
				c.add(Calendar.MONTH, 1);
			}
			
			int tamanhoTabela = 3+(contMes*3);
			
			SimpleDateFormat formatoData = new SimpleDateFormat("MM/yyyy");
			Link link, link2;
			Livro livro,ultimoLivro; 
			Collection<UploadedFile> arquivos;
			long idPdf;
    		long idWord;
    		long idExcel;
    		String mimeType;
			
			for(Aseguradora aseg : aseguradoras)
			{
				c.setTime(dataInicio);
				
				table = new Table(tamanhoTabela);
				table.setWidth("100%");
				table.addStyle(Table.STYLE_ALTERNATE);
				table.addSubtitle(aseg.obterNome());
				
				table.setNextHAlign(Table.HALIGN_CENTER);
				table.addHeader("LIBROS ELECTRÓNICOS");
				table.setNextHAlign(Table.HALIGN_CENTER);
				table.addHeader("FRECUENCIA");
				
				c.setTime(dataInicio);
				
				while(c.getTime().compareTo(dataFim) <=0)
				{
					table.setNextHAlign(Table.HALIGN_CENTER);
					table.addHeader("WORD " + formatoData.format(c.getTime()));
					
					table.setNextHAlign(Table.HALIGN_CENTER);
					table.addHeader("EXCEL " + formatoData.format(c.getTime()));
					
					table.setNextHAlign(Table.HALIGN_CENTER);
					table.addHeader("PDF " + formatoData.format(c.getTime()));
					
					c.add(Calendar.MONTH, 1);
				}
				
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
						
						c.setTime(dataInicio);
						
						while(c.getTime().compareTo(dataFim) <=0)
						{
							int mes = Integer.valueOf(new SimpleDateFormat("MM").format(c.getTime()));
							int ano = Integer.valueOf(new SimpleDateFormat("yyyy").format(c.getTime()));
							
							livro = livroHome.obterLivro(aseg, nomeLivro, mes, ano);
							
							if(livro!=null)
							{
								arquivos = home.getAllUploadedFiles(livro);
								
					    		idPdf = 0;
					    		idWord = 0;
					    		idExcel = 0;
								
								for(UploadedFile arquivo : arquivos)
								{
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
							
							c.add(Calendar.MONTH, 1);
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
				}
				
				mainTable.add(table);
			}
			
			
			/*SampleModelManager mm2 = new SampleModelManager();
	        UploadedFileHome home = (UploadedFileHome) mm2.getHome("UploadedFileHome");
			
			Collection<String> mimeTypesPDF = uteis.obterMimeTypesPDF();
			Collection<String> mimeTypesWord = uteis.obterMimeTypesWord();
			Collection<String> mimeTypesExcel = uteis.obterMimeTypesExcel();
			
			Collection<String> nomeLivros = new ArrayList<String>();
			
			table = new Table(1);
			
			if(tipo.equals(""))
			{
				nomeLivros = uteis.obterNomeLivros();
				table = new Table(nomeLivros.size());
				table.setWidth("100%");
			}
			else
				nomeLivros.add(tipo);
			
			table.addStyle(Table.STYLE_ALTERNATE);
			
			for(Iterator<Aseguradora> i = aseguradoras.iterator() ; i.hasNext() ; )
			{
				Aseguradora aseg = i.next();
				
				table.addSubtitle(aseg.obterNome());
				
				Calendar c = Calendar.getInstance();
				c.setTime(dataInicio);
				
				while(c.getTime().compareTo(dataFim) <=0)
				{
					int mes = Integer.parseInt(new SimpleDateFormat("MM").format(c.getTime()));
					int ano = Integer.parseInt(new SimpleDateFormat("yyyy").format(c.getTime()));
					
					table.addSubtitle(new SimpleDateFormat("MM/yyyy").format(c.getTime()));
					
					this.montaCabecalho(table, nomeLivros);
					
					for(Iterator<String> j = nomeLivros.iterator() ; j.hasNext() ; )
					{
						String nomeLivro = j.next();
						
						Livro livro = livroHome.obterLivro(aseg, nomeLivro, mes, ano);
						Table tableAux = new Table(3);
			    		tableAux.setWidth("100%");
						
						if(livro == null)
						{
							tableAux.setNextHAlign(Table.HALIGN_CENTER);
							tableAux.add("N");
							tableAux.setNextHAlign(Table.HALIGN_CENTER);
							tableAux.add("N");
							tableAux.setNextHAlign(Table.HALIGN_CENTER);
							tableAux.add("N");
						}
						else
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
						}
						
						table.setNextHAlign(Table.HALIGN_CENTER);
						table.add(tableAux);
					}
					
					c.add(Calendar.MONTH, 1);
				}
			}
			
	    	mainTable.add(table);*/
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
		return new Label("Libros Pendientes");
	}

	public Entidade getOrigemMenu() throws Exception
	{
		return null;
	}
	
	private void montaCabecalho(Table table, Collection<String> nomeLivros)
	{
		for(Iterator<String> j = nomeLivros.iterator() ; j.hasNext() ; )
    	{
    		String nomeLivro = j.next();
    		
    		table.setNextHAlign(Table.HALIGN_CENTER);
    		table.addHeader(uteis.obterNomeLivroAreviado(nomeLivro));
    	}
		
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
	}
}