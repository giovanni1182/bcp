package com.gvs.crm.model;

import java.util.Date;

import infra.model.Entity;

/**
 * Classe que representa um arquivo gravado no banco de dados (uploaded). Cada
 * arquivo possui um id, o nome (nome do arquivo), o tamanho e o tipo (MIME).
 * 
 * @author Gustavo Schmal
 */

public class UploadedFile extends Entity {
	private long id;

	private String name;

	private long size;

	private String type;

	private String content;

	private boolean lido;
	
	private Date date;

	public String getContent() {
		return content;
	}

	/**
	 * @return Retorna o campo id.
	 */
	public long getId() {
		return id;
	}

	/**
	 * @return Retorna o campo name.
	 */
	public String getName() {
		return name;
	}

	public boolean getLido() {
		return lido;
	}

	/**
	 * @return Retorna o campo size.
	 */
	public long getSize() {
		return size;
	}

	/**
	 * @return Retorna o campo type.
	 */
	public String getType() {
		return type;
	}
	
	public Date getDate() {
		return date;
	}

	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * Atualiza o campo id
	 * 
	 * @param id
	 */
	public void setId(long id) {
		this.id = id;
	}

	public void setLido(boolean lido) {
		this.lido = lido;
	}

	/**
	 * Atualiza o campo name
	 * 
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Atualiza o campo size
	 * 
	 * @param size
	 */
	public void setSize(long size) {
		this.size = size;
	}

	/**
	 * Atualiza o campo type
	 * 
	 * @param type
	 */
	public void setType(String type) {
		this.type = type;
	}
	
	public void setDate(Date date) {
		this.date = date;
	}
}