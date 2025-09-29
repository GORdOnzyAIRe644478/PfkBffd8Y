// 代码生成时间: 2025-09-30 01:36:20
import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.QuarkusApplicationLifecycle;
import java.util.concurrent.CompletableFuture;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.MigrationInfo;
import org.flywaydb.core.api.MigrationState;

@ApplicationScoped
public class DatabaseMigrationTool {

    @Inject
    QuarkusApplicationLifecycle lifecycle;

    // Method to perform database migration
    public void performMigration() {
        try {
            // Initialize Flyway instance
            Flyway flyway = Flyway.configure()
                .dataSource("jdbc:postgresql://localhost:5432/mydatabase", "user", "password")
                .load();

            // Migrate the database
            flyway.migrate();

            // Check if all migrations were successful
            if (flyway.info().all().isEmpty()) {
                throw new RuntimeException("Migration failed");
            }
        } catch (Exception e) {
            throw new RuntimeException("Database migration failed", e);
        }
    }

    // Observe the application start event and perform migration
    public void onStart(@Observes QuarkusApplicationLifecycle.Startup startup) {
        CompletableFuture.runAsync(this::performMigration);
    }
}
