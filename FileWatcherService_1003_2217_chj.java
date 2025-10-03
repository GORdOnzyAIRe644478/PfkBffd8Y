// 代码生成时间: 2025-10-03 22:17:47
import java.io.IOException;
import java.nio.file.ClosedWatchServiceException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.nio.file.attribute.BasicFileAttributes;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.inject.Named;

@ApplicationScoped
public class FileWatcherService {

    @ConfigProperty(name = "watchedDirectory")
    String watchedDirectoryPath;

    @Inject
    @Named("fileChangeEvent")
    Event<FileChangeEvent> fileChangeEvent;

    private WatchService watcher;
    private final Path dir;

    public FileWatcherService() {
        // Initialize the directory path
        this.dir = Paths.get(System.getProperty("user.dir"));
    }

    public void startWatching() throws IOException {
        this.watcher = FileSystems.getDefault().newWatchService();
        registerDirectory(this.dir, this.watcher);
        startWatchingDirectory(this.watcher);
    }

    private void registerDirectory(final Path dir, WatchService watcher) throws IOException {
        dir.register(watcher, StandardWatchEventKinds.ENTRY_CREATE,
                StandardWatchEventKinds.ENTRY_DELETE,
                StandardWatchEventKinds.ENTRY_MODIFY);
    }

    private void startWatchingDirectory(WatchService watcher) {
        while (true) {
            WatchKey key;
            try {
                key = watcher.take();
                for (WatchEvent<?> event : key.pollEvents()) {
                    WatchEvent.Kind<?> kind = event.kind();
                    @SuppressWarnings("unchecked")
                    WatchEvent<Path> ev = (WatchEvent<Path>) event;
                    Path filename = ev.context();

                    switch (kind.name()) {
                        case "ENTRY_CREATE":
                            System.out.println("New file created: " + filename);
                            fileChangeEvent.fire(new FileChangeEvent(FileChangeEvent.Type.CREATED, filename));
                            break;
                        case "ENTRY_DELETE":
                            System.out.println("File deleted: " + filename);
                            fileChangeEvent.fire(new FileChangeEvent(FileChangeEvent.Type.DELETED, filename));
                            break;
                        case "ENTRY_MODIFY":
                            System.out.println("File modified: " + filename);
                            fileChangeEvent.fire(new FileChangeEvent(FileChangeEvent.Type.MODIFIED, filename));
                            break;
                    }
                }
                key.reset();
            } catch (InterruptedException | ClosedWatchServiceException ex) {
                Thread.currentThread().interrupt();
                System.err.println("Error occurred during file watching: " + ex.getMessage());
            }
        }
    }

    private static class FileChangeEvent {
        private Type type;
        private Path file;

        public FileChangeEvent(Type type, Path file) {
            this.type = type;
            this.file = file;
        }

        public enum Type {
            CREATED, DELETED, MODIFIED
        }

        public Type getType() {
            return type;
        }

        public Path getFile() {
            return file;
        }
    }
}
