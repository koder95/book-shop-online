package pl.koder95.bso.model;

public enum Status {
    PENDING, // new order, before PREPARING or CANCELLED
    PREPARING, // order in preparing, before SHIPPED or CANCELLED
    CANCELLED, // order is cancelled, final status
    SHIPPED, // order is shipped, waits for DELIVERED
    DELIVERED, // order is delivered, waits for RETURNED or COMPLETED,
    RETURNED, // order is returned, final status
    COMPLETED; // order is completed, final status
}
