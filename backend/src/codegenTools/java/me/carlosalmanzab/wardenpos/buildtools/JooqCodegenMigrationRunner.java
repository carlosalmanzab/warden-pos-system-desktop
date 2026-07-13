package me.carlosalmanzab.wardenpos.buildtools;

import org.flywaydb.core.Flyway;

import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Standalone Utility (not a Spring bean) used exclusively
 * during the Gradle build to generate a migrated a SQLite file
 * migrated as a source for JOOQ Codegen.
 * This tool is excluded from the final JAR/binary packaging
 */
public final class JooqCodegenMigrationRunner {
    public static void main(String[] args) throws Exception {
        if (args.length < 2) {
            System.err.println("Use JooqCodegenMigrationRunner <?> <?>");
            System.exit(1);
        }

        String dbPath = args[0];
        String migrationsPath = args[1];

        Path dbFile = Path.of(dbPath);
        Files.createDirectories(dbFile.getParent());

        Files.deleteIfExists(dbFile);
        Files.deleteIfExists(Path.of(dbPath + "-wal")); //sqlite aux file
        Files.deleteIfExists(Path.of(dbPath + "-shm")); //sqlite aux file

        String jdbcUrl = "jdbc:sqlite:" + dbFile.toAbsolutePath();

        Flyway flyway = Flyway.configure()
                .dataSource(jdbcUrl, null, null)
                .locations("filesystem:" + migrationsPath)
                .load();

        var result = flyway.migrate();

        System.out.printf(
                "Codegen DB migrated: %d applied migrations -> %s%n",
                result.migrationsExecuted, dbFile.toAbsolutePath()
        );

    }
}
