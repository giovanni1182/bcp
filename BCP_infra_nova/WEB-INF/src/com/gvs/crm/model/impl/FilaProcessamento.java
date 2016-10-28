package com.gvs.crm.model.impl;

import java.util.Date;

public class FilaProcessamento 
{
	private int id,visivel;
	private String nome,status;
	private Date dataArquivo;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public int getVisivel() {
		return visivel;
	}
	public void setVisivel(int visivel) {
		this.visivel = visivel;
	}
	public Date getDataArquivo() {
		return dataArquivo;
	}
	public void setDataArquivo(Date dataArquivo) {
		this.dataArquivo = dataArquivo;
	}
	
	
}
