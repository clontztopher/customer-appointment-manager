package clontz.clientschedulingapp.Models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Appointment {
    private int id;
    private String title;
    private String description;
    private String location;
    private String type;
    private LocalDateTime start;
    private LocalDateTime end;

    private Customer customer;
    private int user_id;
    private Contact contact;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
    }

    public Customer getCustomer() {
        return customer;
    }

    public int getCustomerId() {
        return customer.getId();
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public int getUserId() {
        return user_id;
    }

    public void setUserId(int user_id) {
        this.user_id = user_id;
    }

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    public String getStartFormatted() {
        return getStart().format(DateTimeFormatter.ofPattern("MM/dd/YYYY hh:mm a"));
    }

    public String getEndFormatted() {
        return getEnd().format(DateTimeFormatter.ofPattern("MM/dd/YYYY hh:mm a"));
    }
}
