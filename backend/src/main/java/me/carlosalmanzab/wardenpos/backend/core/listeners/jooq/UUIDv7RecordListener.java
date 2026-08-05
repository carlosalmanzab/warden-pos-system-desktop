package me.carlosalmanzab.wardenpos.backend.core.listeners.jooq;

import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.impl.TimeBasedEpochGenerator;
import java.util.UUID;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.RecordContext;
import org.jooq.RecordListener;

public class UUIDv7RecordListener implements RecordListener {

  private static final TimeBasedEpochGenerator UUID_GENERATOR =
      Generators.timeBasedEpochGenerator();

  @Override
  public void insertStart(RecordContext ctx) {

    Record record = ctx.record();

    for (Field<?> field : record.fields()) {

      if (field.getType() != UUID.class) {
        continue;
      }

      if (!field.getName().equalsIgnoreCase("id")) {
        continue;
      }

      @SuppressWarnings("unchecked")
      Field<UUID> uuidField = (Field<UUID>) field;

      if (record.get(field) == null) {
        record.set(uuidField, UUID_GENERATOR.generate());
      }
    }
  }
}
