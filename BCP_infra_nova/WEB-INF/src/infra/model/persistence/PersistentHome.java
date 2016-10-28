package infra.model.persistence;

import infra.model.Home;
import infra.sql.SQLQuery;
import infra.sql.SQLRow;

public abstract class PersistentHome extends Home {
	private String getDatabaseResourceName() {
		return ((PersistentModelManager) this.getModelManager())
				.getDatabaseResourceName();
	}

	protected PersistentEntity getFirstEntity(PersistentEntity[] entities) {
		if (entities.length == 0)
			return null;
		else
			return entities[0];
	}

	protected PersistentEntity getEntityById(long id) throws Exception {
		PersistentEntity entity = null;
		SQLQuery query = this.getModelManager().createSQLQuery(
				this.getDatabaseResourceName(),
				"select class from entity where id=?");
		query.addLong(id);
		SQLRow[] row = query.execute();
		if (row.length > 0) {
			entity = (PersistentEntity) this.getModelManager().getEntity(
					row[0].getString("class"));
			entity.setId(id);
		}
		return entity;
	}

	protected PersistentEntity[] getEntities(String className) throws Exception {
		PersistentEntity[] entities = new PersistentEntity[0];
		SQLQuery query = this.getModelManager().createSQLQuery(
				this.getDatabaseResourceName(),
				"select id from entity where class=?");
		query.addString(className);
		SQLRow[] rows = query.execute();
		entities = new PersistentEntity[rows.length];
		for (int i = 0; i < rows.length; i++) {
			entities[i] = (PersistentEntity) this.getModelManager().getEntity(
					className);
			entities[i].setId(rows[i].getLong("id"));
		}
		return entities;
	}

	protected PersistentEntity[] getEntitiesByProperty(String className,
			String name, String content) throws Exception {
		PersistentEntity[] entities = new PersistentEntity[0];
		SQLQuery query = this
				.getModelManager()
				.createSQLQuery(
						this.getDatabaseResourceName(),
						"select entity.id as entityId from entity, property where entity.id=property.entity and entity.class=? and name=? and content=?");
		query.addString(className);
		query.addString(name);
		query.addString(content);
		SQLRow[] rows = query.execute();
		entities = new PersistentEntity[rows.length];
		for (int i = 0; i < rows.length; i++) {
			entities[i] = (PersistentEntity) this.getModelManager().getEntity(
					className);
			entities[i].setId(rows[i].getLong("entityId"));
		}
		return entities;
	}

	protected PersistentEntity[] getEntitiesByLink(PersistentEntity entity,
			String name) throws Exception {
		PersistentEntity[] targets = new PersistentEntity[0];
		SQLQuery query = this
				.getModelManager()
				.createSQLQuery(
						this.getDatabaseResourceName(),
						"select entity.id as entityId, entity.class as entityClass from entity, link where entity.id=link.target and link.entity=? and link.name=?");
		query.addLong(entity.getId());
		query.addString(name);
		SQLRow[] rows = query.execute();
		targets = new PersistentEntity[rows.length];
		for (int i = 0; i < rows.length; i++) {
			targets[i] = (PersistentEntity) this.getModelManager().getEntity(
					rows[i].getString("entityClass"));
			targets[i].setId(rows[i].getLong("entityId"));
		}
		return targets;
	}
}