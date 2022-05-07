package model;

import java.util.HashMap;
import java.util.Map;

public class ParkingLot {
  private final Map<Integer, Slot> slots;
  private final int capacity;

  public ParkingLot( int capacity) {
    this.slots = new HashMap<>();
    this.capacity = capacity;
  }

  public Map<Integer, Slot> getSlots() {
    return slots;
  }

  public int getCapacity() {
    return capacity;
  }

  public Slot park(Car car, Integer slotNumber) {
    Slot slot = getSlot(slotNumber);
    if (!slot.isSlotFree()) {
      throw new RuntimeException("SlotAlreadyOccupiedException");
    }
    slot.assignCar(car);
    return slot;
  }

  private Slot getSlot(final Integer slotNumber) {
    if (slotNumber > getCapacity() || slotNumber <= 0) {
      throw new RuntimeException("InvalidSlotException");
    }
    final Map<Integer, Slot> allSlots = getSlots();
    if (!allSlots.containsKey(slotNumber)) {
      allSlots.put(slotNumber, new Slot(slotNumber));
    }
    return allSlots.get(slotNumber);
  }

  public Slot makeSlotFree(final Integer slotNumber) {
    final Slot slot = getSlot(slotNumber);
    slot.unassignCar();
    return slot;
  }
}
