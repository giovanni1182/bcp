package com.gvs.crm.model;

import java.util.Date;

public interface EntidadeBCP 
{
	public void setNome(String nome);
	public void setSobreNome(String sobreNome);
	public void setPais(String pais);
	public void setTipoPessoa(String tipoPessoa);
	public void setTipoDocumento(String tipoDoc);
	public void setNumeroDoc(String numeroDoc);
	public void setDataNascimento(Date dataNascimento);
	
	public String getNome();
	public String getSobreNome();
	public String getPais();
	public String getTipoPessoa();
	public String getTipoDocumento();
	public String getNumeroDoc();
	public Date getDataNascimento();
}
