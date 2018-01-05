package net.nighthawkempires.guilds.data.datasection;

public interface Model<P> {
    Type getType();

    // -- ENUMS -- //
    enum Type {
        TRANSIENT, PERSISTENT
    }
}
