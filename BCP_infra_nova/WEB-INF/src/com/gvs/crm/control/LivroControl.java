package com.gvs.crm.control;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import com.gvs.crm.model.Aseguradora;
import com.gvs.crm.model.AseguradoraHome;
import com.gvs.crm.model.CRMModelManager;
import com.gvs.crm.model.EntidadeHome;
import com.gvs.crm.model.Evento;
import com.gvs.crm.model.EventoHome;
import com.gvs.crm.model.Livro;
import com.gvs.crm.model.LivroHome;
import com.gvs.crm.model.SampleModelManager;
import com.gvs.crm.model.UploadedFileHome;
import com.gvs.crm.model.Usuario;
import com.gvs.crm.model.UsuarioHome;
import com.gvs.crm.model.Uteis;
import com.gvs.crm.report.LivrosPendentesXLS;
import com.gvs.crm.report.LivrosXLS;
import com.gvs.crm.view.EventoView;
import com.gvs.crm.view.LivrosPendentesView;
import com.gvs.crm.view.LivrosView;
import com.gvs.crm.view.UltimosLivrosView;

import infra.control.Action;
import infra.control.Control;

public class LivroControl extends Control
{
	public void novoLivro(Action action) throws Exception
	{
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome home = (EntidadeHome) mm.getHome("EntidadeHome");
		Livro livro = (Livro) mm.getEntity("Livro");
		mm.beginTransaction();
		try 
		{
			if(action.getBoolean("view"))
				this.setResponseView(new EventoView(livro));
			else
			{
				int mes = action.getInt("mes");
				int ano = action.getInt("ano");
				
				livro.atribuirTipo(action.getString("tipo"));
				livro.atribuirMes(mes);
				livro.atribuirAno(ano);
				
				if(action.getLong("aseguradoraId") == 0)
					throw new Exception("Aseguradora en blanco");
				
				Aseguradora aseg = (Aseguradora) home.obterEntidadePorId(action.getLong("aseguradoraId"));
				livro.atribuirOrigem(aseg);
				
				if(action.getString("tipo").equals(""))
					throw new Exception("Tipo en blanco");
				if(mes == 0)
					throw new Exception("Mes en blanco");
				if(ano == 0)
					throw new Exception("Año en blanco");
				else if(new Integer(ano).toString().length()!=4)
					throw new Exception("Formato incorrecto del año");
				
				LivroHome livroHome = (LivroHome) mm.getHome("LivroHome");
				Collection<Livro> livros = livroHome.obterLivrosMesAno(aseg, action.getString("tipo"), mes, ano);
				if(livros.size() > 0)
					throw new Exception("Ya existe un libro con ese mismo mes y año");
				
				livro.atribuirTitulo(action.getString("tipo"));
				
				livro.incluir();
				
				livro.atualizarFase(Evento.EVENTO_CONCLUIDO);
				
				mm.commitTransaction();
				
				this.setResponseView(new EventoView(livro));
			}
		}
		catch (Exception exception)
		{
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new EventoView(livro));
			mm.rollbackTransaction();
		}
	}
	
	public void atualizarLivro(Action action) throws Exception
	{
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome home = (EntidadeHome) mm.getHome("EntidadeHome");
		EventoHome eventoHome = (EventoHome) mm.getHome("EventoHome");
		Livro livro = (Livro) eventoHome.obterEventoPorId(action.getLong("id"));
		mm.beginTransaction();
		try 
		{
			int mesAnterior = livro.obterMes();
			int anoAnterior = livro.obterAno();
			String tipoAnterior = livro.obterTipo();
			
			int mes = action.getInt("mes");
			int ano = action.getInt("ano");
				
			if(action.getLong("aseguradoraId") == 0)
				throw new Exception("Aseguradora en blanco");
			
			Aseguradora aseg = (Aseguradora) home.obterEntidadePorId(action.getLong("aseguradoraId"));
			
			if(action.getString("tipo").equals(""))
				throw new Exception("Tipo en blanco");
			if(mes == 0)
				throw new Exception("Mes blanco");
			if(ano == 0)
				throw new Exception("Año en blanco");
			else if(new Integer(ano).toString().length()!=4)
				throw new Exception("Formato incorrecto del año");
			
			if(mesAnterior!=mes || anoAnterior!=ano || !tipoAnterior.equals(action.getString("tipo")))
			{
				LivroHome livroHome = (LivroHome) mm.getHome("LivroHome");
				Collection<Livro> livros = livroHome.obterLivrosMesAno(aseg, action.getString("tipo"), mes, ano);
				if(livros.size() > 0)
					throw new Exception("Ya existe un libro mes "+mes+ " y año " + ano);
			}
			
			/*livro.atribuirTipo(action.getString("tipo"));
			livro.atribuirMes(mes);
			livro.atribuirAno(ano);
			
			livro.atribuirOrigem(aseg);*/
			
			livro.atualizarOrigem(aseg);
			livro.atualizarTipo(action.getString("tipo"));
			livro.atualizarMes(mes);
			livro.atualizarAno(ano);
			livro.atualizarTitulo(action.getString("tipo"));
			
			mm.commitTransaction();
			
			this.setResponseView(new EventoView(livro));
		}
		catch (Exception exception)
		{
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new EventoView(livro));
			mm.rollbackTransaction();
		}
	}
	
	public void adicionarLivro(Action action) throws Exception
	{
		CRMModelManager mm = new CRMModelManager(this.getUser());
		SampleModelManager mm2 = new SampleModelManager();
        UploadedFileHome home = (UploadedFileHome) mm2.getHome("UploadedFileHome");
		EventoHome eventoHome = (EventoHome) mm.getHome("EventoHome");
		Livro livro = (Livro) eventoHome.obterEventoPorId(action.getLong("id"));
		Uteis uteis = new Uteis();
		try 
		{
			Collection<String> mimeTypes = uteis.obterMimeTypes();
			
			if(action.getInputStream("file") == null)
				throw new Exception("Seleccione el archivo");
			
			String fileName = action.getString("file_name");
			String fileType = action.getString("file_content_type");
	        long fileSize = action.getLong("file_size");
	        
	        if(!mimeTypes.contains(fileType))
	        	throw new Exception("Formato de archivo no es válido");
	        
	        int mes = livro.obterMes();
	        int ano = livro.obterAno();
	        
	        String mesStr = new Integer(mes).toString();
	        if(mesStr.length() == 1)
	        	mesStr="0" + mesStr;
	        
	        String nomeArquivo = uteis.obterNomeArquivo(livro.obterTipo()) + ano + mesStr;
	        
	        if(!fileName.startsWith(nomeArquivo))
	        	throw new Exception("Nombre de archivo es diferente del código");
	        
	        
			home.addUploadedFile(livro, action.getInputStream("file"), fileName, fileType, fileSize, 0);
			
			this.setResponseView(new EventoView(livro));
		}
		catch (Exception exception)
		{
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new EventoView(livro));
		}
	}
	
	public void consultarLivros(Action action) throws Exception
	{
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome home = (EntidadeHome) mm.getHome("EntidadeHome");
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Usuario usuarioAtual = usuarioHome.obterUsuarioPorUser(getUser());
		int mes = action.getInt("mes");
		int ano = action.getInt("ano");
		String tipo = action.getString("tipo");
		Aseguradora aseg = null;
		try 
		{
			if(action.getBoolean("view"))
				this.setResponseView(new LivrosView(null, "", 0, 0, false));
			else
			{
				if(action.getLong("aseguradoraId") > 0)
					aseg = (Aseguradora) home.obterEntidadePorId(action.getLong("aseguradoraId"));
				
				if(mes == 0)
					throw new Exception("Mes blanco");
				if(ano == 0)
					throw new Exception("Año en blanco");
				else if(new Integer(ano).toString().length()!=4)
					throw new Exception("Formato incorrecto del año");
				
				if(action.getBoolean("excel"))
				{
					String hora = "_" + usuarioAtual.obterNome() + "_" + new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date());
					
					AseguradoraHome aseguradoraHome = (AseguradoraHome) mm.getHome("AseguradoraHome");
					LivroHome livroHome = (LivroHome) mm.getHome("LivroHome");
					SampleModelManager mm2 = new SampleModelManager();
					UploadedFileHome uploadedFileHome = (UploadedFileHome) mm2.getHome("UploadedFileHome");
					
					String textoUsuario = "Generado: " + usuarioAtual.obterNome() + " " + new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date());
					
					LivrosXLS xls = new LivrosXLS(aseg, tipo, mes, ano, aseguradoraHome, livroHome, uploadedFileHome,textoUsuario);
					
					this.setResponseInputStream(xls.obterArquivo());
			        this.setResponseFileName("Libros Electronicos-"+mes+"_"+ano+hora+".xls");
			        this.setResponseContentType("application/vnd.ms-excel");
			        this.setResponseContentSize(xls.obterArquivo().available());
				}
				else
					this.setResponseView(new LivrosView(aseg, tipo, mes, ano, true));
			}
		}
		catch (Exception exception)
		{
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new LivrosView(aseg, tipo, mes, ano, false));
		}
	}
	
	public void livrosPendentes(Action action) throws Exception
	{
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome home = (EntidadeHome) mm.getHome("EntidadeHome");
		AseguradoraHome aseguradoraHome = (AseguradoraHome) mm.getHome("AseguradoraHome");
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Usuario usuarioAtual = usuarioHome.obterUsuarioPorUser(getUser());
		Date dataInicio = action.getDate("dataInicio");
		Date dataFim = action.getDate("dataFim");
		String tipo = action.getString("tipo");
		Collection<Aseguradora> aseguradoras = new ArrayList<Aseguradora>();
		Aseguradora aseg = null;
		try 
		{
			if(action.getBoolean("view"))
				this.setResponseView(new LivrosPendentesView(aseg, tipo, null, null, aseguradoras));
			else
			{
				if(action.getLong("aseguradoraId") > 0)
				{
					aseg = (Aseguradora) home.obterEntidadePorId(action.getLong("aseguradoraId"));
					aseguradoras.add(aseg);
				}
				else
					aseguradoras = aseguradoraHome.obterAseguradoras();
				
				if(dataInicio == null)
					throw new Exception("Fecha Inicio en blanco");
				if(dataFim == null)
					throw new Exception("Fecha Fin en blanco");
				
				if(!action.getBoolean("excel"))
					this.setResponseView(new LivrosPendentesView(aseg, tipo, dataInicio, dataFim, aseguradoras));
				else
				{
					String hora = "_" + usuarioAtual.obterNome() + "_" + new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date());
					
					LivroHome livroHome = (LivroHome) mm.getHome("LivroHome");
					SampleModelManager mm2 = new SampleModelManager();
					UploadedFileHome uploadedFileHome = (UploadedFileHome) mm2.getHome("UploadedFileHome");
					
					String textoUsuario = "Generado: " + usuarioAtual.obterNome() + " " + new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date());
					
					LivrosPendentesXLS xls = new LivrosPendentesXLS(aseg, tipo, dataInicio, dataFim, aseguradoras, livroHome, uploadedFileHome,textoUsuario);
					
					String dataInicioStr = new SimpleDateFormat("MM/yyyy").format(dataInicio);
					String dataFimStr = new SimpleDateFormat("MM/yyyy").format(dataFim);
					
					this.setResponseInputStream(xls.obterArquivo());
			        this.setResponseFileName("Libros Electronicos Pendientes-"+dataInicioStr+"_"+dataFimStr+hora+".xls");
			        this.setResponseContentType("application/vnd.ms-excel");
			        this.setResponseContentSize(xls.obterArquivo().available());
				}
			}
		}
		catch (Exception exception)
		{
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new LivrosPendentesView(aseg, tipo, dataInicio, dataFim, aseguradoras));
		}
	}
	
	public void visualizarCentralRiscoLivros(Action action) throws Exception
	{
		CRMModelManager mm = new CRMModelManager(this.getUser());
		AseguradoraHome aseguradoraHome = (AseguradoraHome) mm.getHome("AseguradoraHome");
		LivroHome livroHome = (LivroHome) mm.getHome("LivroHome");
		Collection aseguradoras = new ArrayList();
		try
		{
			aseguradoras = aseguradoraHome.obterAseguradorasPorMenor80OrdenadoPorNome();
			
			this.setResponseView(new UltimosLivrosView(aseguradoras, livroHome));
		}
		catch (Exception exception)
		{
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new UltimosLivrosView(aseguradoras, livroHome));
		}
	}
}
