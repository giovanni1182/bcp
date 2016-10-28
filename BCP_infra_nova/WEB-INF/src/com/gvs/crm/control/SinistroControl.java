package com.gvs.crm.control;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import com.gvs.crm.model.Apolice;
import com.gvs.crm.model.Aseguradora;
import com.gvs.crm.model.CRMModelManager;
import com.gvs.crm.model.ClassificacaoContas;
import com.gvs.crm.model.Entidade;
import com.gvs.crm.model.EntidadeHome;
import com.gvs.crm.model.Sinistro;
import com.gvs.crm.model.SinistroHome;
import com.gvs.crm.model.Usuario;
import com.gvs.crm.model.UsuarioHome;
import com.gvs.crm.report.CentralRiscoSinistrosApolicesXLS;
import com.gvs.crm.report.CentralRiscoSinistrosXLS;
import com.gvs.crm.report.MaioresSinistrosXLS;
import com.gvs.crm.report.SinistrosXLS;
import com.gvs.crm.view.ApolicesPorSecao2View;
import com.gvs.crm.view.CentralRiscoSinistroView;
import com.gvs.crm.view.LocalizarSinistroView;
import com.gvs.crm.view.MaioresOcorrenciaSinistroView;

import infra.control.Action;
import infra.control.Control;

public class SinistroControl extends Control 
{
	public void visualizarCentralRiscoSinistro(Action action) throws Exception 
	{
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		SinistroHome sinistroHome = (SinistroHome) mm.getHome("SinistroHome");
		Usuario usuarioAtual = usuarioHome.obterUsuarioPorUser(this.getUser());
		Aseguradora aseguradora = null;
		String nomeAsegurado = null,documento = null;
		Collection<Entidade> aseguradoras = new ArrayList<>();
		Date dataInicio = null;
		Date dataFim = null;
		try 
		{
			nomeAsegurado = action.getString("nome").trim();
			documento = action.getString("documento").trim();
			
			if(!action.getBoolean("view"))
			{
				boolean erro = true;
				
				if(action.getLong("aseguradoraId") > 0)
					aseguradora = (Aseguradora) entidadeHome.obterEntidadePorId(action.getLong("aseguradoraId"));
				
				if(!action.getBoolean("naoValidar"))
				{
					if(nomeAsegurado.length() == 0 && documento.length() == 0)
						throw new Exception("Complete el Nombre o Documento");
					else if(nomeAsegurado.length() < 10 && documento.length() > 4)
						erro = false;
					else if(nomeAsegurado.length() > 9)
					{
						if(documento.length() > 0)
						{
							if(documento.length() < 4)
								erro = true;
							else
								erro = false;
						}
						else
							erro = false;
					}
					
					if(erro)
					{
						if(nomeAsegurado.length() < 10)
							throw new Exception("El nombre debe tener al menos 10 caracteres");
						else if(documento.length() < 4)
							throw new Exception("El documento debe tener al menos 5 dígitos");
					}
				}
				
				if(action.getDate("dataInicio")!=null)
				{
					String dataStr = new SimpleDateFormat("dd/MM/yyyy").format(action.getDate("dataInicio")) + " 00:00:00";
					dataInicio = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(dataStr);
				}
				if(action.getDate("dataFim")!=null)
				{
					String dataStr = new SimpleDateFormat("dd/MM/yyyy").format(action.getDate("dataFim")) + " 23:59:59";
					dataFim = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(dataStr);
				}
				
				aseguradoras = sinistroHome.obterAseguradorasCentralRisco(nomeAsegurado, documento);
				
			}
			
			this.setResponseView(new CentralRiscoSinistroView(usuarioAtual, aseguradoras, nomeAsegurado, aseguradora, dataInicio, dataFim, documento));
			
		} 
		catch (Exception exception) 
		{
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new CentralRiscoSinistroView(usuarioAtual, aseguradoras, nomeAsegurado, aseguradora, dataInicio, dataFim, documento));
		}
	}
	
	public void visualizarApolicesPorSecao(Action action) throws Exception 
	{
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		Aseguradora aseguradora = (Aseguradora) entidadeHome.obterEntidadePorId(action.getLong("aseguradoraId2"));
		Date dataInicio = action.getDate("dataInicio2");
		Date dataFim = action.getDate("dataFim2");
		ClassificacaoContas cContas = (ClassificacaoContas) entidadeHome.obterEntidadePorId(action.getLong("secaoId"));
		String situacao = action.getString("situacao");
		String situacao2 = action.getString("situacao2");
		
		Collection<Apolice> apolices = new ArrayList<>();
		try 
		{
			String dataInicioStr = new SimpleDateFormat("dd/MM/yyyy").format(dataInicio) + " 00:00:00";
			String dataFimStr = new SimpleDateFormat("dd/MM/yyyy").format(dataFim) + " 23:59:59";
			
			dataInicio = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(dataInicioStr);
			dataFim = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(dataFimStr);
			
			apolices = aseguradora.obterApolicesPorSecao(cContas,dataInicio,dataFim,situacao, situacao2);
					
			this.setResponseView(new ApolicesPorSecao2View(aseguradora, dataInicio,dataFim,cContas,apolices, situacao,situacao2));
		} 
		catch (Exception exception) 
		{
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new ApolicesPorSecao2View(aseguradora, dataInicio,dataFim,cContas,apolices, situacao, situacao2));
		}
	}
	
	public void excelCentralRiscoSinistro(Action action) throws Exception 
	{
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Usuario usuarioAtual = usuarioHome.obterUsuarioPorUser(this.getUser());
		Date dataInicio = null;
		Date dataFim = null;
		Aseguradora aseguradora = null;
		try 
		{
			if(action.getLong("aseguradoraId") > 0)
				aseguradora = (Aseguradora) entidadeHome.obterEntidadePorId(action.getLong("aseguradoraId"));
			else
				throw new Exception("Aseguradora en blanco");
			
			if(action.getDate("dataInicio")!=null)
			{
				String dataStr = new SimpleDateFormat("dd/MM/yyyy").format(action.getDate("dataInicio")) + " 00:00:00";
				dataInicio = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(dataStr);
			}
			else
				throw new Exception("Fecha inicio blanco");
			
			if(action.getDate("dataFim")!=null)
			{
				String dataStr = new SimpleDateFormat("dd/MM/yyyy").format(action.getDate("dataFim")) + " 23:59:59";
				dataFim = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(dataStr);
			}
			else
				throw new Exception("Fecha termino blanco");
			
			CentralRiscoSinistrosXLS xls = new CentralRiscoSinistrosXLS(aseguradora, dataInicio, dataFim, entidadeHome);
			
			InputStream arquivo = xls.obterArquivo();
			String nome = "Listado Siniestros_"+usuarioAtual.obterNome() + "_" + new SimpleDateFormat("dd/MM/yyyy").format(dataInicio) + "_hasta_" + new SimpleDateFormat("dd/MM/yyyy").format(dataFim) + ".xls"; 
			String mime = "application/vnd.ms-excel";
			
			this.setResponseInputStream(arquivo);
	        this.setResponseFileName(nome);
	        this.setResponseContentType(mime);
	        this.setResponseContentSize(arquivo.available());
			
			//this.setResponseView(new CentralRiscoSinistroView(usuarioAtual, aseguradoras, nomeAsegurado, aseguradora, dataInicio, dataFim));
			
		} 
		catch (Exception exception) 
		{
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new CentralRiscoSinistroView(usuarioAtual, new ArrayList(), "", aseguradora, dataInicio, dataFim,""));
		}
	}
	
	public void excelCentralRiscoSinistro2(Action action) throws Exception 
	{
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Usuario usuarioAtual = usuarioHome.obterUsuarioPorUser(this.getUser());
		Date dataInicio = null;
		Date dataFim = null;
		Aseguradora aseguradora = null;
		String situacao = action.getString("situacao");
		String situacao2 = action.getString("situacao2");
		try 
		{
			if(action.getLong("aseguradoraId") > 0)
				aseguradora = (Aseguradora) entidadeHome.obterEntidadePorId(action.getLong("aseguradoraId"));
			else
				throw new Exception("Aseguradora en blanco");
			
			if(action.getDate("dataInicio")!=null)
			{
				String dataStr = new SimpleDateFormat("dd/MM/yyyy").format(action.getDate("dataInicio")) + " 00:00:00";
				dataInicio = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(dataStr);
			}
			else
				throw new Exception("Fecha inicio blanco");
			
			if(action.getDate("dataFim")!=null)
			{
				String dataStr = new SimpleDateFormat("dd/MM/yyyy").format(action.getDate("dataFim")) + " 23:59:59";
				dataFim = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(dataStr);
			}
			else
				throw new Exception("Fecha termino blanco");
			
			ClassificacaoContas cContas = (ClassificacaoContas) entidadeHome.obterEntidadePorId(action.getLong("secaoId"));
			
			Collection apolices = aseguradora.obterApolicesPorSecao(cContas,dataInicio,dataFim,situacao, situacao2);
			
			CentralRiscoSinistrosApolicesXLS xls = new CentralRiscoSinistrosApolicesXLS(aseguradora, dataInicio, dataFim, cContas, apolices, situacao, situacao2);
			
			InputStream arquivo = xls.obterArquivo();
			String nome = "Listado Apolices de Siniestros_"+usuarioAtual.obterNome() + "_" + new SimpleDateFormat("dd/MM/yyyy").format(dataInicio) + "_hasta_" + new SimpleDateFormat("dd/MM/yyyy").format(dataFim) + ".xls"; 
			String mime = "application/vnd.ms-excel";
			
			this.setResponseInputStream(arquivo);
	        this.setResponseFileName(nome);
	        this.setResponseContentType(mime);
	        this.setResponseContentSize(arquivo.available());
			
		} 
		catch (Exception exception) 
		{
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new CentralRiscoSinistroView(usuarioAtual, new ArrayList(), "", aseguradora, dataInicio, dataFim,""));
		}
	}
	
	public void localizarSinistros(Action action) throws Exception 
	{
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		SinistroHome sinistroHome = (SinistroHome) mm.getHome("SinistroHome");
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Usuario usuarioAtual = usuarioHome.obterUsuarioPorUser(getUser());

		Aseguradora aseguradora = null;

		if (action.getLong("aseguradoraId") > 0)
			aseguradora = (Aseguradora) entidadeHome.obterEntidadePorId(action.getLong("aseguradoraId"));

		Collection<Sinistro> sinistros = new ArrayList<Sinistro>();

		String secao = action.getString("secao");
		String situacaoSinistro = action.getString("situacaoSinistro");

		String situacao = action.getString("situacao").trim();
		
		Date dataInicio = action.getDate("dataInicio");
		Date dataFim = action.getDate("dataFim");
		String nomeAsegurado = action.getString("nomeAsegurado").trim();
		try
		{
			if (action.getBoolean("listar"))
			{
				//if(aseguradora == null)
					//throw new Exception("Elija Aseguradora");
				if(dataInicio == null)
					throw new Exception("Fecha inicio em blanco");
				if(dataFim == null)
					throw new Exception("Fecha fin em blanco");
				if(nomeAsegurado.length()>0)
				{
					if(nomeAsegurado.length()<10)
						throw new Exception("Mínimo de 10 dígitos para el Nombre Asegurado");
				}
				if(secao.equals("") && situacao.equals("0") && situacaoSinistro.equals(""))
					throw new Exception("Es necesario un filtro adicional");
				
				String dataFimStr = new SimpleDateFormat("dd/MM/yyyy").format(dataFim) + " 23:59:59";
				dataFim = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(dataFimStr);
				
				sinistros = sinistroHome.obterSinistros(aseguradora, secao, situacao, dataInicio, dataFim, nomeAsegurado,situacaoSinistro);
			}
			
			if(!action.getBoolean("excel"))
				this.setResponseView(new LocalizarSinistroView(aseguradora, secao, situacao, dataInicio, dataFim, sinistros, action.getBoolean("listar"), nomeAsegurado,situacaoSinistro));
			else
			{
				SinistrosXLS xls = new SinistrosXLS(aseguradora, secao, situacao, dataInicio, dataFim, sinistros, nomeAsegurado);
				
				String hora = "_" + usuarioAtual.obterNome() + "_" + new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date());
				
				InputStream arquivo = xls.obterArquivo();
				
				this.setResponseInputStream(arquivo);
		        this.setResponseFileName("Buscar Siniestros"+hora+".xls");
		        this.setResponseContentType("application/vnd.ms-excel");
		        this.setResponseContentSize(arquivo.available());
			}
		} 
		catch (Exception exception) 
		{
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new LocalizarSinistroView(aseguradora, secao, situacao, dataInicio, dataFim, sinistros, action.getBoolean("listar"), nomeAsegurado,situacaoSinistro));
		}
	}
	
	public void maioresSinistros(Action action) throws Exception
	{
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Usuario usuario = usuarioHome.obterUsuarioPorUser(this.getUser());
		SinistroHome sinistroHome = (SinistroHome) mm.getHome("SinistroHome");
		Aseguradora aseguradora = null;
		String situacao = action.getString("situacao");
		Date dataInicio = action.getDate("dataInicio");
		Date dataFim = action.getDate("dataFim");
		Collection<Sinistro> colecao = new ArrayList<Sinistro>();
		if(action.getLong("aseguradora") > 0)
			aseguradora = (Aseguradora) entidadeHome.obterEntidadePorId(action.getLong("aseguradora"));
		
		String secao = action.getString("secao");
		double monto = action.getDouble("monto");
		String tipoPessoa = action.getString("tipoPessoa");
		int qtde = action.getInt("qtde");
		String modalidade = action.getString("modalidade");
		String tipoInstrumento = action.getString("tipoInstrumento");
		try
		{
			if(action.getBoolean("view"))
				this.setResponseView(new MaioresOcorrenciaSinistroView(aseguradora, action.getString("tipoPessoa"), dataInicio, dataFim, 0, situacao, new ArrayList<Sinistro>(), secao, monto, modalidade,tipoInstrumento));
			else
			{
				if(dataInicio == null)
					throw new Exception("Fecha Inicio en blanco");
				if(dataFim == null)
					throw new Exception("Fecha Fin en blanco");
				if(action.getInt("qtde")<=0)
					throw new Exception("Cantidad Solicitada és cero");
				
				if(action.getLong("aseguradora") > 0)
					aseguradora = (Aseguradora) entidadeHome.obterEntidadePorId(action.getLong("aseguradora"));
				
				String dataFimStr = new SimpleDateFormat("dd/MM/yyyy").format(dataFim) + " 23:59:59";
				
				dataFim = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(dataFimStr);
				
				colecao = sinistroHome.obterMaiores(aseguradora, tipoPessoa, dataInicio, dataFim, qtde, situacao, secao, monto, modalidade,tipoInstrumento);
				
				if(action.getBoolean("excel"))
				{
					String dataI = new SimpleDateFormat("dd/MM/yyyy").format(dataInicio);
					String dataF = new SimpleDateFormat("dd/MM/yyyy").format(dataFim);
					
					dataI = dataI.replace('/', '_');
					dataF = dataF.replace('/', '_');
					String hora = new SimpleDateFormat("HH:mm").format(new Date());
					
					String textoUsuario = "Generado : " + usuario.obterNome() + " " + new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date());
					
					MaioresSinistrosXLS xls = new MaioresSinistrosXLS(colecao, dataInicio, dataFim, situacao, tipoPessoa, secao, qtde,textoUsuario, modalidade);
					InputStream arquivo = xls.obterArquivo();
					String nome = "Mayores Ocurrencias Siniestros_" +usuario.obterNome()+"_"+ dataI + " hasta " +dataF+ "_"+hora+".xls";
					String mime = "application/vnd.ms-excel";
					
					this.setResponseInputStream(arquivo);
			        this.setResponseFileName(nome);
			        this.setResponseContentType(mime);
			        this.setResponseContentSize(arquivo.available());
				}
				else
					this.setResponseView(new MaioresOcorrenciaSinistroView(aseguradora, tipoPessoa, dataInicio, dataFim, qtde, situacao, colecao, secao, monto, modalidade,tipoInstrumento));
			}
		}
		catch (Exception e)
		{
			this.setAlert(Util.translateException(e));
			this.setResponseView(new MaioresOcorrenciaSinistroView(aseguradora, tipoPessoa, dataInicio, dataFim, qtde, situacao, new ArrayList<Sinistro>(), secao, monto, modalidade,tipoInstrumento));
		}
	}
}
