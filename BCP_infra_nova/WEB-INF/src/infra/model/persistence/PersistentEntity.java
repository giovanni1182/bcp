package infra.model.persistence;

import java.util.Iterator;
import java.util.Properties;

import infra.model.Entity;
import infra.sql.SQLQuery;
import infra.sql.SQLRow;
import infra.sql.SQLTransaction;
import infra.sql.SQLUpdate;

public abstract class PersistentEntity extends Entity {
	private long id;

	private Properties properties;

	private String getDatabaseResourceName() {
		return ((PersistentModelManager) this.getModelManager())
				.getDatabaseResourceName();
	}

	protected long getId() {
		return id;
	}

	void setId(long id) {
		this.id = id;
	}

	public String getProperty(String name) throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery(
				this.getDatabaseResourceName(),
				"select content from property where entity=? and name=?");
		query.addLong(this.id);
		query.addString(name);
		SQLRow[] rows = query.execute();
		if (rows.length == 1)
			return rows[0].getString("content");
		else
			return "";
	}

	public void setProperty(String name, String value) throws Exception {
		if (this.properties == null)
			this.properties = new Properties();
		this.properties.put(name, value);
	}

	public void addLink(String name, PersistentEntity target) throws Exception {
		if (target != null) {
			SQLUpdate update = this.getModelManager().createSQLUpdate(
					this.getDatabaseResourceName(),
					"insert into link (entity, name, target) values (?, ?, ?)");
			update.addLong(this.id);
			update.addString(name);
			update.addLong(target.getId());
			update.execute();
		}
	}

	public void setLink(String name, PersistentEntity target) throws Exception {
		SQLTransaction transaction = this.getModelManager()
				.createSQLTransaction(this.getDatabaseResourceName());
		SQLUpdate update1 = this.getModelManager().createSQLUpdate(
				this.getDatabaseResourceName(),
				"delete from link where entity=? and name=?");
		update1.addLong(this.id);
		update1.addString(name);
		update1.execute();
		if (target != null) {
			SQLUpdate update2 = this.getModelManager().createSQLUpdate(
					this.getDatabaseResourceName(),
					"insert into link (entity, name, target) values (?, ?, ?)");
			update2.addLong(this.id);
			update2.addString(name);
			update2.addLong(target.getId());
			update2.execute();
		}
		transaction.commit();
	}

	public void removeLink(String name, PersistentEntity target)
			throws Exception {
		if (target != null) {
			SQLUpdate update = this.getModelManager().createSQLUpdate(
					this.getDatabaseResourceName(),
					"delete from link where entity=? and name=? and target=?");
			update.addLong(this.id);
			update.addString(name);
			update.addLong(target.getId());
			update.execute();
		}
	}

	public void insert() throws Exception {
		SQLTransaction transaction = this.getModelManager()
				.createSQLTransaction(this.getDatabaseResourceName());
		SQLQuery query = transaction
				.createSQLQuery("select max(id) as lastid from entity");
		SQLRow[] rows = query.execute();
		if (rows.length != 1)
			this.id = 1;
		else
			this.id = rows[0].getLong("lastid") + 1;
		SQLUpdate update = transaction
				.createSQLUpdate("insert into entity (id, class) values (?, ?)");
		update.addLong(this.id);
		update.addString(this.getClassAlias());
		update.execute();
		if (this.properties != null)
			for (Iterator i = this.properties.keySet().iterator(); i.hasNext();) {
				String name = (String) i.next();
				String content = this.properties.getProperty(name);
				if (content != null) {
					SQLUpdate update1 = transaction
							.createSQLUpdate("insert into property (entity, name, content) values (?, ?, ?)");
					update1.addLong(this.id);
					update1.addString(name);
					update1.addString(content);
					update1.execute();
				}
			}
		transaction.commit();
	}

	public void update() throws Exception {
		SQLTransaction transaction = this.getModelManager()
				.createSQLTransaction(this.getDatabaseResourceName());
		if (this.properties != null)
			for (Iterator i = this.properties.keySet().iterator(); i.hasNext();) {
				String name = (String) i.next();
				String content = this.properties.getProperty(name);
				SQLQuery query = transaction
						.createSQLQuery("select content from property where entity=? and name=?");
				query.addLong(this.id);
				query.addString(name);
				SQLRow[] rows = query.execute();
				if (rows.length == 0 && content != null) {
					SQLUpdate update1 = transaction
							.createSQLUpdate("insert into property (entity, name, content) values (?, ?, ?)");
					update1.addLong(this.id);
					update1.addString(name);
					update1.addString(content);
					update1.execute();
				} else if (content == null) {
					SQLUpdate update1 = transaction
							.createSQLUpdate("delete from property where entity=? and name=?");
					update1.addLong(this.id);
					update1.addString(name);
					update1.execute();
				} else {
					SQLUpdate update1 = transaction
							.createSQLUpdate("update property set content=? where entity=? and name=?");
					update1.addString(content);
					update1.addLong(this.id);
					update1.addString(name);
					update1.execute();
				}
			}
		transaction.commit();
	}

	public void delete() throws Exception {
		SQLTransaction transaction = this.getModelManager()
				.createSQLTransaction(this.getDatabaseResourceName());
		SQLUpdate update1 = transaction
				.createSQLUpdate("delete from link where entity=?");
		update1.addLong(this.id);
		update1.execute();
		SQLUpdate update2 = transaction
				.createSQLUpdate("delete from link where target=?");
		update2.addLong(this.id);
		update2.execute();
		SQLUpdate update3 = transaction
				.createSQLUpdate("delete from property where entity=?");
		update3.addLong(this.id);
		update3.execute();
		SQLUpdate update4 = transaction
				.createSQLUpdate("delete from entity where id=?");
		update4.addLong(this.id);
		update4.execute();
		transaction.commit();
	}
}