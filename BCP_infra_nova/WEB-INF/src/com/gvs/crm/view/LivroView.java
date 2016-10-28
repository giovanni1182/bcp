package com.gvs.crm.view;

import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;
import java.util.Properties;

import com.gvs.crm.component.AseguradorasSelect;
import com.gvs.crm.component.TipoLivroSelect;
import com.gvs.crm.model.Aseguradora;
import com.gvs.crm.model.CRMModelManager;
import com.gvs.crm.model.Livro;
import com.gvs.crm.model.LivroHome;
import com.gvs.crm.model.SampleModelManager;
import com.gvs.crm.model.UploadedFile;
import com.gvs.crm.model.UploadedFileHome;

import infra.control.Action;
import infra.security.User;
import infra.view.Block;
import infra.view.Button;
import infra.view.Image;
import infra.view.InputFile;
import infra.view.InputInteger;
import infra.view.Label;
import infra.view.Link;
import infra.view.Space;
import infra.view.Table;
import infra.view.View;

public class LivroView extends EventoAbstratoView
{
	public View execute(User user, Locale arg1, Properties arg2) throws Exception
	{
		Livro livro = (Livro) this.obterEvento();
		
		boolean novo = livro.obterId() == 0;
		
		Table mainTable = new Table(1);
		mainTable.setWidth("100%");
		
		Table table = new Table(2);
		
		table.addHeader("Aseguradora:");
		if(novo)
			table.add(new AseguradorasSelect("aseguradoraId", null, false));
		else
			table.add(new AseguradorasSelect("aseguradoraId", (Aseguradora)livro.obterOrigem(), false));
		table.addHeader("Tipo:");
		String tipo = livro.obterTipo();
		if(tipo == null)
			tipo = "";
		
		table.add(new TipoLivroSelect("tipo", tipo, false));
		table.addHeader("Mes:");
		
		Block block = new Block(Block.HORIZONTAL);
		block.add(new InputInteger("mes", livro.obterMes(), 2));
		block.add(new Space(2));
		
		Label l = new Label("Año:");
		l.setBold(true);
		block.add(l);
		block.add(new Space(2));
		block.add(new InputInteger("ano", livro.obterAno(), 4));
		l = new Label("(Ex.: 1980)");
		block.add(new Space(2));
		block.add(l);
		table.add(block);
		
		if(novo)
		{
			Button incluirButton = new Button("Incluir", new Action("novoLivro"));
			table.addFooter(incluirButton);
		}
		else
		{
			Button incluirButton = new Button("Atualizar", new Action("atualizarLivro"));
			incluirButton.getAction().add("id", livro.obterId());
			table.addFooter(incluirButton);
			
			incluirButton = new Button("Eliminar", new Action("excluirEvento"));
			incluirButton.getAction().add("id", livro.obterId());
			incluirButton.getAction().setConfirmation("Confirme la eliminación ?");
			table.addFooter(incluirButton);
		}
		
		mainTable.add(table);
		
		if(!novo)
		{
			table = new Table(2);
			table.addSubtitle("Nuevo Archivo");
			
			table.addHeader("Archivo:");
			table.add(new InputFile("file", null));
			
			Button arquivoButton = new Button("Adicionar", new Action("adicionarLivro"));
			arquivoButton.getAction().add("id", livro.obterId());
			table.addFooter(arquivoButton);
			
			mainTable.add(table);
			
			SampleModelManager mm2 = new SampleModelManager();
	        UploadedFileHome home = (UploadedFileHome) mm2.getHome("UploadedFileHome");
			Collection arquivos = home.getAllUploadedFiles(livro);
			
			if(arquivos.size() > 0)
			{
				table = new Table(2);
				table.addStyle(Table.STYLE_ALTERNATE);
				table.addSubtitle("Archivos");
				
				table.addHeader("");
				table.setNextHAlign(Table.HALIGN_CENTER);
				table.addHeader("Nombre");
				
		        for(Iterator i = arquivos.iterator() ; i.hasNext() ; )
		        {
		        	UploadedFile uploadedFile = (UploadedFile) i.next();
		        	
		        	Link link = new Link(new Image("delete.gif"), new Action("remove"));
		        	link.getAction().add("id", uploadedFile.getId());
		        	link.getAction().add("idEvento", livro.obterId());
		        	
		        	table.add(link);
		        	
		        	Link link2 = new Link(uploadedFile.getName(), new Action("download"));
		        	link2.getAction().add("id", uploadedFile.getId());
		        	
		        	table.add(link2);
		        	
		        }
		        
		        mainTable.add(table);
		        mainTable.addSubtitle("");
			}
			
			CRMModelManager mm = new CRMModelManager(user);
			LivroHome livroHome = (LivroHome) mm.getHome("LivroHome");
			Collection<Livro> livros = livroHome.obterLivrosMesAno((Aseguradora)livro.obterOrigem(), "", livro.obterMes(), livro.obterAno());
			livros.remove(livro);
			
			table = new Table(1);
			table.addSubtitle("Libros Cargados en " + livro.obterMes() + "/" + livro.obterAno());
			
			for(Iterator<Livro> i = livros.iterator() ; i.hasNext() ; )
			{
				Livro livro2 = i.next();
				
				table.addSubtitle(livro2.obterOrigem().obterNome());
				
				Link link = new Link("Tipo: " + livro2.obterTipo(), new Action("visualizarEvento"));
				link.getAction().add("id", livro2.obterId());
				
				table.add(link);
				
				arquivos = home.getAllUploadedFiles(livro2);
				
				for(Iterator j = arquivos.iterator() ; j.hasNext() ; )
		        {
		        	UploadedFile uploadedFile = (UploadedFile) j.next();
		        	
		        	Link link2 = new Link(uploadedFile.getName(), new Action("download"));
		        	link2.getAction().add("id", uploadedFile.getId());
		        	
		        	table.add(link2);
		        }
			}
			
			mainTable.add(table);
		}
		
		return mainTable;
	}
}