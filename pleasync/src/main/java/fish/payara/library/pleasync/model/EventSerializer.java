package fish.payara.library.pleasync.model;

public abstract class EventSerializer<T> {
    /**
     * Create storage and classpath-neutral representation of event
     * @param event
     * @return
     */
    public abstract EventEnvelope serialize(T event);

    protected abstract String determineBusinessKey(T event);

    protected String determineEventType(T event) {
        return event.getClass().getSimpleName(); // eventually dropping the Event suffix would be nice
    }

    protected int determinePayloadVersion(T event) {
        // TODO: Add payload override configuration straight into this class.
        return 0;
    }

}
