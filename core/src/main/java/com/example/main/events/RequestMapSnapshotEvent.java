package com.example.main.events;

/**
 * An event published when the server requests the map from the host client.
 * The GDXGameScreen will listen for this to trigger the snapshot creation.
 */
public class RequestMapSnapshotEvent implements AppEvent {
    // This event needs no data, its existence is the message.
    public RequestMapSnapshotEvent() {}
}
