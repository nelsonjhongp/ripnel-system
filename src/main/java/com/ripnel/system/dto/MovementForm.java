// package com.ripnel.system.dto;  (o donde tengas tus DTOs)
package com.ripnel.system.dto;

public class MovementForm {

    private String movementType;    // IN, OUT, TRANSFER
    private Long variantId;         // por ahora solo productos terminados
    // Más adelante: private Long materialId;

    private Long fromLocationId;    // para OUT / TRANSFER
    private Long toLocationId;      // para IN / TRANSFER

    private Integer quantity;
    private String reason;          // COMPRA, VENTA, PRODUCCION, AJUSTE, etc.
    private String note;

    // getters y setters
    public String getMovementType() { return movementType; }
    public void setMovementType(String movementType) { this.movementType = movementType; }

    public Long getVariantId() { return variantId; }
    public void setVariantId(Long variantId) { this.variantId = variantId; }

    public Long getFromLocationId() { return fromLocationId; }
    public void setFromLocationId(Long fromLocationId) { this.fromLocationId = fromLocationId; }

    public Long getToLocationId() { return toLocationId; }
    public void setToLocationId(Long toLocationId) { this.toLocationId = toLocationId; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
}
