import exception.InvalidCommandException;
import exception.NoFreeSlotAvailableException;
import model.Car;
import model.Command;
import model.ParkingLot;
import model.Slot;
import service.ParkingLotService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Main {



  public static void main(String[] args) throws IOException {
    OutputPrinter outputPrinter = new OutputPrinter();
    ParkingLotService parkingLotService = new ParkingLotService();
    outputPrinter.welcome();
    input(parkingLotService, outputPrinter);
  }

  private static void input(ParkingLotService parkingLotService, OutputPrinter outputPrinter) throws IOException {
    final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    while (true) {
      final String input = reader.readLine();
        final Command command = new Command(input);
      switch (command.getCommandName()){
        case "":
          outputPrinter.printWithNewLine("invalid input! try again.....");
          break;
        case "create_parking_lot":
          parkingLotService.createParkingLot(new ParkingLot(Integer.parseInt(command.getParams().get(0))));
          break;
        case "park":
          park(parkingLotService, outputPrinter, command);
          break;
        case "leave":
          int slotNum = Integer.parseInt(command.getParams().get(0));
          parkingLotService.makeSlotFree(slotNum);
          outputPrinter.printWithNewLine("Slot number " + slotNum + " is free");
          break;
        case "status":
          status(parkingLotService, outputPrinter);
          break;
        case "registration_numbers_for_cars_with_colour":
          registrationNumberForCarsWithColours(parkingLotService, outputPrinter, command);
          break;
        case "slot_numbers_for_cars_with_colour":
          slotNumbersForCarsWithColour(parkingLotService, outputPrinter, command);
          break;
        case "slot_number_for_registration_number":
          slotNumberForRegistrationNumber(parkingLotService, outputPrinter, command);
          break;
        case "exit":
          outputPrinter.end();
          break;
      }
      if (command.getCommandName().equals("exit")) {
        break;
      }
    }
    }


  private static void park(ParkingLotService parkingLotService, OutputPrinter outputPrinter, Command command) {
    try{
    Car car = new Car(command.getParams().get(0), command.getParams().get(1));
    Integer slotNumber=  parkingLotService.park(car);
    outputPrinter.printWithNewLine(" Allocated slot number: "+ slotNumber);
    }catch (NoFreeSlotAvailableException ex){
      outputPrinter.parkingLotFull();
    }
  }

  private static void slotNumberForRegistrationNumber(ParkingLotService parkingLotService, OutputPrinter outputPrinter, Command command) {
    final List<Slot> occupiedSlots = parkingLotService.getOccupiedSlots();
    final String regNumberToFind = command.getParams().get(0);
    final Optional<Slot> foundSlot = occupiedSlots.stream()
            .filter(slot -> slot.getParkedCar().getRegistrationNo().equals(regNumberToFind))
            .findFirst();
    if (foundSlot.isPresent()) {
      outputPrinter.printWithNewLine(foundSlot.get().getSlotNumber().toString());
    } else {
      outputPrinter.notFound();
    }
  }

  private static void status(ParkingLotService parkingLotService, OutputPrinter outputPrinter) {
    final List<Slot> occupiedSlots = parkingLotService.getOccupiedSlots();
    if (occupiedSlots.isEmpty()) {
      outputPrinter.parkingLotEmpty();
    }
    outputPrinter.statusHeader();
    for (Slot slot : occupiedSlots) {
      final Car parkedCar = slot.getParkedCar();
      final String slotNumber = slot.getSlotNumber().toString();
      outputPrinter.printWithNewLine(padString(slotNumber, 12)
              + padString(parkedCar.getRegistrationNo(), 19) + parkedCar.getColor());
    }
  }

  private static void registrationNumberForCarsWithColours(ParkingLotService parkingLotService, OutputPrinter outputPrinter, Command command) {
    final List<Slot> slotsForColor = parkingLotService.getSlotsForColor(command.getParams().get(0));
    if (slotsForColor.isEmpty()) {
      outputPrinter.notFound();
    } else {
      final String result =
              slotsForColor.stream()
                      .map(slot -> slot.getParkedCar().getRegistrationNo())
                      .collect(Collectors.joining(", "));
      outputPrinter.printWithNewLine(result);
    }
  }

  private static void slotNumbersForCarsWithColour(ParkingLotService parkingLotService, OutputPrinter outputPrinter, Command command) {
    List<Slot> slotsColor = parkingLotService.getSlotsForColor(command.getParams().get(0));
    if (slotsColor.isEmpty()) {
      outputPrinter.notFound();
    }else{
      final String result =
              slotsColor.stream()
                      .map(slot -> slot.getSlotNumber().toString())
                      .collect(Collectors.joining(", "));
      outputPrinter.printWithNewLine(result);
    }
  }

  private static String padString(final String word, final int length) {
    String newWord = word;
    for(int count = word.length(); count < length; count++) {
      newWord = newWord + " ";
    }
    return newWord;
  }

}
