import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

public class pepasm {
    public static void main(String[] args) {
        HashMap<String, String> instructionMap = new HashMap<>();
        HashMap<String, String> addressingModeMap = new HashMap<>();

        // Stores mnemonics and addresses for calls later in program

        instructionMap.put("LDBA", "D");
        instructionMap.put("STBA", "F");
        instructionMap.put("STOP", "00");
        instructionMap.put("STWA", "E");
        instructionMap.put("LDWA", "C");
        instructionMap.put("ANDA", "C5");
        instructionMap.put("ASLA", "0A");
        instructionMap.put("ASRA", "0C");
        instructionMap.put("CPBA", "B");
        instructionMap.put("BRNE", "1A");
        instructionMap.put("ADDA", "6");

        addressingModeMap.put("i", "0");
        addressingModeMap.put("d", "1");

        String fileName = args[0];
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) { // BufferedReader parses file from parameters
            ArrayList<String> lines = new ArrayList<>(); // Stores viable lines for machine code
            HashMap<String, Integer> symbolAddress = new HashMap<>();
            String line;


            int currentAddress = 0;
            while ((line = br.readLine()) != null) {
                line = line.trim(); // Trim rids line of excess spaces
                if (line.isEmpty()) {
                    continue; // Ignores blank lines
                }
                lines.add(line);
                if (line.contains(":")) {
                    String symbol = line.substring(0, line.indexOf(":")).trim();
                    symbolAddress.put(symbol,currentAddress);
                    System.out.println(symbolAddress);
                }
                currentAddress = currentAddress+3;
            }

            StringBuilder machineCode = new StringBuilder();
            for (String readLine : lines) { // Machine code pass
                System.out.println("Reading line: " + readLine);
                if (readLine.equals(".END")) {
                    machineCode.append(" zz");
                    break;
                }

                String instructionLine = readLine;
                if (readLine.contains(":")) { // Ignores function call
                    instructionLine = readLine.substring(readLine.indexOf(":") + 1).trim();
                }


                String[] parts = instructionLine.split("[,\\s]+");// Splits line into parts when whitespace's present
                String instruction = parts[0].trim();


                if (!machineCode.isEmpty()) {
                    machineCode.append(" ");
                }

                if (parts.length == 1) { // For mnemonics like .END, STOP, ASLA, ASRA
                    machineCode.append(instructionMap.get(instruction));
                } else {
                    String mode = parts[2].trim();
                    if (!instruction.equals("BRNE")) {//BRNE has different hex conventions
                        machineCode.append(instructionMap.get(instruction))
                                .append(addressingModeMap.get(mode));
                    } else {
                        machineCode.append(instructionMap.get(instruction));
                    }
                    StringBuilder operand = new StringBuilder(parts[1].trim());
                    if (instruction.equals("BRNE")) {// Adds address when BRNE and a symbol is present

                        System.out.println(operand);

                        machineCode.append(" 00 0").append(symbolAddress.get(operand.toString()));
                    } else if (operand.toString().startsWith("0x")) {
                        operand = new StringBuilder(operand.toString().replace("0x", ""));
                        while (operand.length() < 4) { // Adds 0s for operands missing 4
                            operand.insert(0, "0");
                        }
                        machineCode.append(" ").append(operand, 0, 2) // Spacing for operand
                                .append(" ").append(operand.substring(2));
                    }
                }
            }

            System.out.println(machineCode);
        } catch (IOException e) {
            System.out.println("File Error: " + e.getMessage());
        }
    }
}