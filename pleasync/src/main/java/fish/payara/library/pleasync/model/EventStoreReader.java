package fish.payara.library.pleasync.model;

public interface EventStoreReader {
    EventChunk read(String cursor, int pageSize);
}
