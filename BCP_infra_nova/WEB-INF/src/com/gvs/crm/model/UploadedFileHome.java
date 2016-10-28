package com.gvs.crm.model;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import infra.model.Home;
import infra.sql.SQLQuery;
import infra.sql.SQLRow;
import infra.sql.SQLUpdate;

/**
 * Classe que implementa todos as ações que podem ser executadas com os arquivos
 * gravados (Uploaded files).
 * 
 * @author Gustavo Schmal
 */

public class UploadedFileHome extends Home {
	private static int CHUNK_BUFFER_SIZE = 524288; // 512 kbytes

	public void addUploadedFile(InputStream content, String fileName,
			String contentType, long contentSize, long lido) throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select max(id) as maximo from uploaded_file");
		long id = query.executeAndGetFirstRow().getLong("maximo") + 1;
		SQLUpdate insert1 = this
				.getModelManager()
				.createSQLUpdate("crm",
						"insert into uploaded_file (id, name, type, size) values (?, ?, ?, ?)");
		insert1.addLong(id);
		insert1.addString(fileName);
		insert1.addString(contentType);
		insert1.addLong(contentSize);
		//insert1.addLong(lido);
		insert1.execute();

		// cria um array de 512K que irá armazenar os fragmentos do arquivos ser
		// gravado no banco de dados. Por performance e agilização no banco de
		// dados, qualquer arquivo será gravado no banco de dados em segmentos
		// de 512K. O campo 'seq' indica a sequência de gravação dos segmentos
		// do arquivo.
		byte[] buffer = new byte[CHUNK_BUFFER_SIZE];
		int size = content.read(buffer);
		long contentId = 1;
		while (size > 0) {
			SQLUpdate insert2 = this
					.getModelManager()
					.createSQLUpdate("crm",
							"insert into uploaded_file_content (id, seq, content) values (?, ?, ?)");
			insert2.addLong(id);
			insert2.addLong(contentId++);
			insert2.addBytes(buffer, size);
			insert2.execute();
			size = content.read(buffer);
		}
	}

	public void addUploadedFile(Evento evento, InputStream content,	String fileName, String contentType, long contentSize, long lido) throws Exception
	{
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select max(id) as maximo from uploaded_file");
		long id = query.executeAndGetFirstRow().getLong("maximo") + 1;
		SQLUpdate insert1 = this.getModelManager().createSQLUpdate("crm",
						"insert into uploaded_file (id, name, type, size, id_evento,data_criacao) values (?, ?, ?, ?, ?,?)");
		insert1.addLong(id);
		insert1.addString(fileName);
		insert1.addString(contentType);
		insert1.addLong(contentSize);
		insert1.addLong(evento.obterId());
		insert1.addLong(new Date().getTime());
		insert1.execute();

		// cria um array de 512K que irá armazenar os fragmentos do arquivos ser
		// gravado no banco de dados. Por performance e agilização no banco de
		// dados, qualquer arquivo será gravado no banco de dados em segmentos
		// de 512K. O campo 'seq' indica a sequência de gravação dos segmentos
		// do arquivo.
		byte[] buffer = new byte[CHUNK_BUFFER_SIZE];
		int size = content.read(buffer);
		long contentId = 1;
		while (size > 0) {
			SQLUpdate insert2 = this
					.getModelManager()
					.createSQLUpdate(
							"crm",
							"insert into uploaded_file_content (id, seq, content, id_evento) values (?, ?, ?, ?)");
			insert2.addLong(id);
			insert2.addLong(contentId++);
			insert2.addBytes(buffer, size);
			insert2.addLong(evento.obterId());
			insert2.execute();
			size = content.read(buffer);
		}
	}

	// para remover um arquivo, basta remover os registros que armazenam o seu
	// cabeçalho (id, nome, tipo e tamanho) e os segumentos que formam o
	// arquivo.
	/*public void removeUploadedFile(long id) throws Exception {
		SQLUpdate delete1 = this.getModelManager().createSQLUpdate("crm",
				"delete from uploaded_file_content where id=?");
		delete1.addLong(id);
		delete1.execute();
		SQLUpdate delete2 = this.getModelManager().createSQLUpdate("crm",
				"delete from uploaded_file where id=?");
		delete2.addLong(id);
		delete2.execute();
	}*/
	
	public void addUploadedFile(Entidade entidade, InputStream content,	String fileName, String contentType, long contentSize, long lido) throws Exception
	{
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select max(id) as maximo from uploaded_file");
		long id = query.executeAndGetFirstRow().getLong("maximo") + 1;
		SQLUpdate insert1 = this
				.getModelManager()
				.createSQLUpdate(
						"crm",
						"insert into uploaded_file (id, name, type, size, id_entidade,data_criacao) values (?, ?, ?, ?, ?,?)");
		insert1.addLong(id);
		insert1.addString(fileName);
		insert1.addString(contentType);
		insert1.addLong(contentSize);
		insert1.addLong(entidade.obterId());
		insert1.addLong(new Date().getTime());
		insert1.execute();
		
		// cria um array de 512K que irá armazenar os fragmentos do arquivos ser
		// gravado no banco de dados. Por performance e agilização no banco de
		// dados, qualquer arquivo será gravado no banco de dados em segmentos
		// de 512K. O campo 'seq' indica a sequência de gravação dos segmentos
		// do arquivo.
		byte[] buffer = new byte[CHUNK_BUFFER_SIZE];
		int size = content.read(buffer);
		long contentId = 1;
		while (size > 0) 
		{
			SQLUpdate insert2 = this.getModelManager().createSQLUpdate("crm",
							"insert into uploaded_file_content (id, seq, content, id_entidade) values (?, ?, ?, ?)");
			insert2.addLong(id);
			insert2.addLong(contentId++);
			insert2.addBytes(buffer, size);
			insert2.addLong(entidade.obterId());
			insert2.execute();
			size = content.read(buffer);
		}
	}
		
		
		// para remover um arquivo, basta remover os registros que armazenam o seu
		// cabeçalho (id, nome, tipo e tamanho) e os segumentos que formam o
		// arquivo.
		public void removeUploadedFile(long id) throws Exception
		{
		SQLUpdate delete1 = this.getModelManager().createSQLUpdate("crm",
				"delete from uploaded_file_content where id=?");
		delete1.addLong(id);
		delete1.execute();
		SQLUpdate delete2 = this.getModelManager().createSQLUpdate("crm",
				"delete from uploaded_file where id=?");
		delete2.addLong(id);
		delete2.execute();
		}
		
			// obter o cabeçalho dos arquivos armazenados.
			public Collection getAllUploadedFiles() throws Exception {
				SQLQuery query = this.getModelManager().createSQLQuery("crm",
						"select * from uploaded_file order by id");
				SQLRow[] rows = query.execute();
				Collection uploadedFiles = new ArrayList();
				for (int i = 0; i < rows.length; i++) {
					UploadedFile uploadedFile = (UploadedFile) this.getModelManager()
							.getEntity("UploadedFile");
					uploadedFile.setId(rows[i].getLong("id"));
					uploadedFile.setName(rows[i].getString("name"));
					uploadedFile.setType(rows[i].getString("type"));
					uploadedFile.setSize(rows[i].getLong("size"));
		
					if (rows[i].getLong("lido") == 0)
						uploadedFile.setLido(false);
					else if (rows[i].getLong("lido") == 1)
						uploadedFile.setLido(true);
		
					uploadedFiles.add(uploadedFile);
				}
		return uploadedFiles;
	}

	// obter um arquivo armazenado pelo seu id.
	public UploadedFile getUploadedFileById(long id) throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select * from uploaded_file where id=?");
		query.addLong(id);
		SQLRow[] rows = query.execute();
		if (rows.length != 1) {
			return null;
		}
		UploadedFile uploadedFile = (UploadedFile) this.getModelManager()
				.getEntity("UploadedFile");
		uploadedFile.setId(rows[0].getLong("id"));
		uploadedFile.setName(rows[0].getString("name"));
		uploadedFile.setType(rows[0].getString("type"));
		uploadedFile.setSize(rows[0].getLong("size"));
		return uploadedFile;
	}

	// 	obter um arquivo armazenado pelo seu id do evento.
	public Collection<UploadedFile> getAllUploadedFiles(Evento evento) throws Exception
	{
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select * from uploaded_file where id_evento=? order by id");

		query.addLong(evento.obterId());
		SQLRow[] rows = query.execute();
		Collection<UploadedFile> uploadedFiles = new ArrayList<UploadedFile>();
		for (int i = 0; i < rows.length; i++) {
			UploadedFile uploadedFile = (UploadedFile) this.getModelManager()
					.getEntity("UploadedFile");
			uploadedFile.setId(rows[i].getLong("id"));
			uploadedFile.setName(rows[i].getString("name"));
			uploadedFile.setType(rows[i].getString("type"));
			uploadedFile.setSize(rows[i].getLong("size"));
			uploadedFile.setDate(new Date(rows[i].getLong("data_criacao")));

			if (rows[i].getLong("lido") == 0)
				uploadedFile.setLido(false);
			else if (rows[i].getLong("lido") == 1)
				uploadedFile.setLido(true);

			uploadedFiles.add(uploadedFile);
		}
		return uploadedFiles;
	}
	
	public void removeAllUploadedFiles(Evento evento) throws Exception
	{
		Collection anexos = this.getAllUploadedFiles(evento); 
		
		for(Iterator i = anexos.iterator() ; i.hasNext() ; )
		{
			UploadedFile up = (UploadedFile) i.next();
			
			this.removeUploadedFile(up.getId());
		}
	}
	
	public Collection getAllUploadedFiles(Entidade entidade) throws Exception
	{
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select * from uploaded_file where id_entidade=? order by id");

		query.addLong(entidade.obterId());
		SQLRow[] rows = query.execute();
		Collection uploadedFiles = new ArrayList();
		for (int i = 0; i < rows.length; i++)
		{
			UploadedFile uploadedFile = (UploadedFile) this.getModelManager().getEntity("UploadedFile");
			uploadedFile.setId(rows[i].getLong("id"));
			uploadedFile.setName(rows[i].getString("name"));
			uploadedFile.setType(rows[i].getString("type"));
			uploadedFile.setSize(rows[i].getLong("size"));
			uploadedFile.setDate(new Date(rows[i].getLong("data_criacao")));

			if (rows[i].getLong("lido") == 0)
				uploadedFile.setLido(false);
			else if (rows[i].getLong("lido") == 1)
				uploadedFile.setLido(true);

			uploadedFiles.add(uploadedFile);
		}
		return uploadedFiles;
	}

	// obter o arquivo armazenado no banco de dados através do seu cabeçalho.
	public InputStream getUploadedFileContent(UploadedFile uploadedFile)
			throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select * from uploaded_file_content where id=?");
		query.addLong(uploadedFile.getId());
		SQLRow[] rows = query.execute();
		return new UploadedFileInputStream(rows, "content");
	}

	public void atualizarLido(UploadedFile uploadedFile) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate(
				"update uploaded_file set lido=? where id=?");
		update.addLong(1);
		update.addLong(uploadedFile.getId());
		update.execute();
	}
}