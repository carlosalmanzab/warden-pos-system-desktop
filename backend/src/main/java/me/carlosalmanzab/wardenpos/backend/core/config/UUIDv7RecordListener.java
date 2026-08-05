package me.carlosalmanzab.wardenpos.backend.core.config;

import com.fasterxml.uuid.Generators;
import java.util.UUID;
import org.jooq.*;
import org.jooq.Record;
import org.springframework.stereotype.Component;

@Component
public class UUIDv7RecordListener implements RecordListener {

  @Override
  public void insertStart(RecordContext ctx) {
    Record record = ctx.record();
    if (record instanceof org.jooq.TableRecord<?> tableRecord) {
      Table<?> table = tableRecord.getTable();

      UniqueKey<?> primaryKey = table.getPrimaryKey();
      if (primaryKey == null) {
        return;
      }

      // Auto-generate UUID only for single-column primary keys
      if (primaryKey.getFields().size() == 1) {
        Field<?> field = primaryKey.getFields().getFirst();
        if (field.getType().equals(UUID.class)) {
          Object currentValue = tableRecord.get(field);
          if (currentValue == null) {
            UUID uuid7 = Generators.timeBasedEpochGenerator().generate();

            @SuppressWarnings("unchecked")
            Field<UUID> uuidField = (Field<UUID>) field;

            tableRecord.set(uuidField, uuid7);
          }
        }
      }
    }
  }
}
