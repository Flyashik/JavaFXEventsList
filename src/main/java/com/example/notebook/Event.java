package com.example.notebook;

public class Event {
    private final int id;
    private final String eventName;
    private final String date;
    private final String place;
    private final String description;
    private final String partCondition;
    private final boolean participation;
    private final String comment;

    public Event(int id, String eventName, String date, String place, String description, String partCondition, boolean participation, String comment) {
        this.id = id;
        this.eventName = eventName;
        this.date = date;
        this.place = place;
        this.description = description;
        this.partCondition = partCondition;
        this.participation = participation;
        this.comment = comment;
    }

    public int getId() {
        return this.id;
    }
    public String getEventName() {
        return this.eventName;
    }

    public String getDate() {
        return this.date;
    }

    public String getPlace() {
        return this.place;
    }

    public String getDescription() {
        return this.description;
    }

    public String getPartCondition() {
        return this.partCondition;
    }

    public boolean getParticipation() {
        return this.participation;
    }

    public String getComment() {
        return this.comment;
    }
}
