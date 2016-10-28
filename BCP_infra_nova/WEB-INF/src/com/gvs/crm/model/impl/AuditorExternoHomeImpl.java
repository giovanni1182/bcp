package com.gvs.crm.model.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import com.gvs.crm.model.AuditorExterno;
import com.gvs.crm.model.AuditorExternoHome;
import com.gvs.crm.model.Entidade;
import com.gvs.crm.model.EntidadeHome;
import com.gvs.crm.model.Inscricao;

import infra.model.Home;
import infra.sql.SQLQuery;
import infra.sql.SQLRow;

public class AuditorExternoHomeImpl extends Home implements AuditorExternoHome
{
	public Collection obterAuditores() throws Exception
	{
		Collection auditores = new ArrayList();

		SQLQuery query = this.getModelManager().createSQLQuery("crm","select entidade.id, nome from entidade, auditor_externo where entidade.id = auditor_externo.id order by nome ASC");

		SQLRow[] rows = query.execute();

		EntidadeHome home = (EntidadeHome) this.getModelManager().getHome("EntidadeHome");

		for (int i = 0; i < rows.length; i++)
		{
			long id = rows[i].getLong("id");

			AuditorExterno auditor = (AuditorExterno) home.obterEntidadePorId(id);

			auditores.add(auditor);
		}

		return auditores;
	}

	public Collection obterAuditoresPorDataResolucao(Date data)
			throws Exception {
		Map auditores = new TreeMap();

		for (Iterator i = this.obterAuditores().iterator(); i.hasNext();) 
		{
			AuditorExterno auditor = (AuditorExterno) i.next();

			for (Iterator j = auditor.obterInscricoes().iterator(); j.hasNext();) 
			{
				Inscricao inscricao = (Inscricao) j.next();

				if (inscricao.obterSituacao().equals("Vigente")) 
				{
					if (inscricao.obterDataResolucao() != null && inscricao.obterDataValidade() != null) 
					{

						String mesResolucao = new SimpleDateFormat("MM").format(inscricao.obterDataResolucao());
						String anoResolucao = new SimpleDateFormat("yyyy").format(inscricao.obterDataResolucao());
						
						String mesValidade = new SimpleDateFormat("MM").format(inscricao.obterDataValidade());
						String anoValidade = new SimpleDateFormat("yyyy").format(inscricao.obterDataValidade());
						
						Date dataResolucao = new SimpleDateFormat("MM/yyyy").parse(mesResolucao + "/" + anoResolucao);
						Date dataValidade = new SimpleDateFormat("MM/yyyy").parse(mesValidade + "/" + anoValidade);
						
						if((data.after(dataResolucao) || data.equals(dataResolucao)) && (data.before(dataValidade) || data.equals(dataValidade)))
						{
							if(auditor.obterSigla()!=null)
								auditores.put(auditor.obterSigla(), auditor);
							else
								auditores.put(auditor.obterNome(), auditor);
						}
							
					}
				}
			}
		}

		return auditores.values();
	}
	
	public Collection obterAuditoresVigentes(Date data,boolean ci) throws Exception
	{
		Collection lista = new ArrayList();
		
		for(Iterator i = this.obterAuditores().iterator() ; i.hasNext() ; )
		{
			AuditorExterno auditor = (AuditorExterno) i.next();
			
			if(auditor.obterUltimaInscricao()!=null)
			{
				Inscricao inscricao = auditor.obterUltimaInscricao();
				
				if(data.before(inscricao.obterDataValidade()))
				{
					String enderecoStr ="";
					String telefone = "";
					String email = "";
					String fax = "";
					
					for(Iterator j = auditor.obterEnderecos().iterator() ; j.hasNext() ; )
					{
						Entidade.Endereco endereco = (Entidade.Endereco) j.next();
						
						enderecoStr = endereco.obterRua() + " Nº " + endereco.obterNumero() + ", " + endereco.obterComplemento();
					}
					
					for(Iterator j = auditor.obterContatos().iterator() ; j.hasNext() ; )
					{
						Entidade.Contato contato = (Entidade.Contato) j.next();
						
						if(contato.obterNome().equals("Telefone"))
							telefone = contato.obterValor();
						else if(contato.obterNome().equals("Fax"))
							fax = contato.obterValor();
						else if(contato.obterNome().equals("Email"))
							email = contato.obterValor();
					}
					
					if(enderecoStr.equals(""))
						enderecoStr = "-";
					if(telefone.equals(""))
						telefone = "-";
					if(email.equals(""))
						email="-";
					if(fax.equals(""))
						fax = "-";
					
					String dataResolucao = new SimpleDateFormat("dd/MM/yyyy").format(inscricao.obterDataResolucao());
					String numeroResolucao = inscricao.obterNumeroResolucao();
					String dataValidade = new SimpleDateFormat("dd/MM/yyyy").format(inscricao.obterDataValidade());
					
					String ruc = auditor.obterRuc();
					if(ruc == null)
						ruc = "-";
					else
					{
						if(ruc.equals(""))
							ruc = "-";
					}
					
					if(ci)
					{
						if(!ruc.equals("-"))
							lista.add(numeroResolucao + ";" + dataResolucao + ";" + dataValidade + ";" + auditor.obterNome() + ";" + enderecoStr + ";" + telefone + ";" + fax + ";" + email + ";" + ruc);
					}
					else
						lista.add(numeroResolucao + ";" + dataResolucao + ";" + dataValidade + ";" + auditor.obterNome() + ";" + enderecoStr + ";" + telefone + ";" + fax + ";" + email+ ";" + ruc);
				}
			}
		}
		
		return lista;
	}
	
	public Collection obterAuditoresNoVigentes(Date data,boolean ci) throws Exception
	{
		Collection lista = new ArrayList();
		
		for(Iterator i = this.obterAuditores().iterator() ; i.hasNext() ; )
		{
			AuditorExterno auditor = (AuditorExterno) i.next();
			
			if(auditor.obterUltimaInscricao()!=null)
			{
				Inscricao inscricao = auditor.obterUltimaInscricao();
				
				if(data.after(inscricao.obterDataValidade()))
				{
					String enderecoStr ="";
					String telefone = "";
					String email = "";
					String fax = "";
					
					for(Iterator j = auditor.obterEnderecos().iterator() ; j.hasNext() ; )
					{
						Entidade.Endereco endereco = (Entidade.Endereco) j.next();
						
						enderecoStr = endereco.obterRua() + " Nº " + endereco.obterNumero() + ", " + endereco.obterComplemento();
					}
					
					for(Iterator j = auditor.obterContatos().iterator() ; j.hasNext() ; )
					{
						Entidade.Contato contato = (Entidade.Contato) j.next();
						
						if(contato.obterNome().equals("Telefone"))
							telefone = contato.obterValor();
						else if(contato.obterNome().equals("Fax"))
							fax = contato.obterValor();
						else if(contato.obterNome().equals("Email"))
							email = contato.obterValor();
					}
					
					if(enderecoStr.equals(""))
						enderecoStr = "-";
					if(telefone.equals(""))
						telefone = "-";
					if(email.equals(""))
						email="-";
					if(fax.equals(""))
						fax = "-";
					
					String dataResolucao = new SimpleDateFormat("dd/MM/yyyy").format(inscricao.obterDataResolucao());
					String numeroResolucao = inscricao.obterNumeroResolucao();
					String dataValidade = new SimpleDateFormat("dd/MM/yyyy").format(inscricao.obterDataValidade());
					
					String ruc = auditor.obterRuc();
					if(ruc == null)
						ruc = "-";
					else
					{
						if(ruc.equals(""))
							ruc = "-";
					}
					
					if(ci)
					{
						if(!ruc.equals("-"))
							lista.add(numeroResolucao + ";" + dataResolucao + ";" + dataValidade + ";" + auditor.obterNome() + ";" + enderecoStr + ";" + telefone + ";" + fax + ";" + email + ";" + ruc);
					}
					else
						lista.add(numeroResolucao + ";" + dataResolucao + ";" + dataValidade + ";" + auditor.obterNome() + ";" + enderecoStr + ";" + telefone + ";" + fax + ";" + email+ ";" + ruc);
				}
			}
		}
		
		return lista;
	}

}