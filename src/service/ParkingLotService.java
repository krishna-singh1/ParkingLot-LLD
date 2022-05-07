package service;

import exception.NoFreeSlotAvailableException;
import model.Car;
import model.ParkingLot;
import model.Slot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class ParkingLotService {
    private ParkingLot parkingLot;
    TreeSet<Integer> freeSlotTreeSet;

    public ParkingLotService() {
        this.freeSlotTreeSet = new TreeSet<>();
    }

    public void createParkingLot(ParkingLot parkingLot){
        if(this.parkingLot !=null){
            throw new RuntimeException("parking lot already Exist");
        }
        this.parkingLot = parkingLot;
        for(int i=1; i<=parkingLot.getCapacity(); i++){
            this.freeSlotTreeSet.add(i);
        }
    System.out.println("Created a parking lot with "+  parkingLot.getCapacity() +" slots");
    }

    public Integer park(Car car){
        validateParkingLotExists();
        Integer nextFreeSlot = getNextSlot();
      parkingLot.park(car, nextFreeSlot);
      removeFreeSlot(nextFreeSlot);
      return nextFreeSlot;
    }
    public void validateParkingLotExists(){
        if(parkingLot == null){
            throw new RuntimeException("Parking Lot does not exist");
        }
    }


    public void removeFreeSlot(Integer slotNumber) {
        this.freeSlotTreeSet.remove(slotNumber);
    }

    public Integer getNextSlot(){
        if (freeSlotTreeSet.isEmpty()) {
            throw new NoFreeSlotAvailableException();
        }
       return this.freeSlotTreeSet.first();
    }

    public void addSlot(Integer slotNumber) {
        this.freeSlotTreeSet.add(slotNumber);
    }

    public void makeSlotFree(final Integer slotNumber) {
        validateParkingLotExists();
        parkingLot.makeSlotFree(slotNumber);
        addSlot(slotNumber);
    }


    public List<Slot> getOccupiedSlots() {
        validateParkingLotExists();
        final List<Slot> occupiedSlotsList = new ArrayList<>();
        final Map<Integer, Slot> allSlots = parkingLot.getSlots();

        for (int i = 1; i <= parkingLot.getCapacity(); i++) {
            if (allSlots.containsKey(i)) {
                final Slot slot = allSlots.get(i);
                if (!slot.isSlotFree()) {
                    occupiedSlotsList.add(slot);
                }
            }
        }
        return occupiedSlotsList;
    }

    public List<Slot> getSlotsForColor(final String color) {
        final List<Slot> occupiedSlots = getOccupiedSlots();
        return occupiedSlots.stream()
                .filter(slot -> slot.getParkedCar().getColor().equals(color))
                .collect(Collectors.toList());
    }

}
