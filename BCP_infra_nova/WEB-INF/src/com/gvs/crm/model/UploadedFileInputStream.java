package com.gvs.crm.model;

import java.io.IOException;
import java.io.InputStream;

import infra.sql.SQLRow;

/**
 * Classe que implementa um InputStream especial que obter o seu conteúdo dos
 * campos "content" de uma sequência de registros (row).
 * 
 * @author Gustavo Schamal
 */

public class UploadedFileInputStream extends InputStream {
	private byte[] buffer;

	private String fieldName;

	private int position = 0;

	private int row = 0;

	private SQLRow[] rows;

	public UploadedFileInputStream(SQLRow[] rows, String fieldName) {
		this.rows = rows;
		this.fieldName = fieldName;
	}

	public int read() throws IOException {
		if (this.buffer == null || this.position >= this.buffer.length) {
			if (this.row >= this.rows.length) {
				return -1;
			} else {
				this.buffer = this.rows[this.row++].getBytes(this.fieldName);
				this.position = 0;
			}
		}
		int c = this.buffer[this.position++] & 0xff;
		return c;
	}
}