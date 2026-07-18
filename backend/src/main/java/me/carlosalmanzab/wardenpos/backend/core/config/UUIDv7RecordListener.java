package me.carlosalmanzab.wardenpos.backend.core.config;

import com.fasterxml.uuid.Generators;
import java.util.UUID;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.RecordContext;
import org.jooq.Table;
import org.jooq.UniqueKey;
import org.jooq.impl.DefaultRecordListener;

public class UUIDv7RecordListener extends DefaultRecordListener {

  @Override
  public void insertStart(RecordContext ctx) {
    Record record = ctx.record();
    if (record instanceof org.jooq.TableRecord<?> tableRecord) {
      Table<?> table = tableRecord.getTable();
      if (table == null) {
        return;
      }

      UniqueKey<?> primaryKey = table.getPrimaryKey();
      if (primaryKey == null) {
        return;
      }

      // Auto-generate UUID only for single-column primary keys
      if (primaryKey.getFields().size() == 1) {
        Field<?> field = primaryKey.getFields().get(0);
        if (field.getType().equals(UUID.class)) {
          Object currentValue = tableRecord.get(field);
          if (currentValue == null) {
            UUID uuidv7 = Generators.timeBasedEpochGenerator().generate();
            tableRecord.set((Field<UUID>) field, uuidv7);
          }
        }
      }
    }
  }
}
