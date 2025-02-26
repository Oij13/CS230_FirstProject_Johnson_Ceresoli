import java.io.*;
import java.util.HashMap;

    public class pepasm {
        public static void main(String[] args) {
            HashMap<String, String> instructionMap = new HashMap<>();
            HashMap<String, String> addressingModeMap = new HashMap<>();


            instructionMap.put("LDBA", "D0");
            instructionMap.put("STBA", "F1");
            instructionMap.put("STOP", "00");
            instructionMap.put("STWA", "F5");
            instructionMap.put("LDWA", "D5");
            instructionMap.put("ANDA", "C5");
            instructionMap.put("ASLA", "04");
            instructionMap.put("ASRA", "05");
            instructionMap.put("CPBA", "B0");
            instructionMap.put("BRNE", "28");
            instructionMap.put("ADDA", "60");

            addressingModeMap.put("i", "00");
            addressingModeMap.put("d", "16");
            String fileName = args[0];
            try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
                String line;
                StringBuilder machineCode = new StringBuilder();
                while ((line = br.readLine()) != null) {
                    line = line.trim();
                    if (line.isEmpty()) {
                        continue;
                    }
                    System.out.println("Reading line: " + line);
                    String[] parts = line.split("\\s+");

                    if (instructionMap.containsKey(parts[0])) {
                        String instruction = instructionMap.get(parts[0]);
                        if (parts[0].equals("STOP")) {
                            machineCode.append(instruction);
                            break;
                        }
                        if (parts.length >= 2) {
                            String operand = parts[1];
                            if (parts[1].contains("0x")) {
                                operand = parts[1].replace("0x", "").replace(",", "");
                            }
                            String address = addressingModeMap.get(parts[2]);
                            machineCode.append(instruction).append(operand).append(address);

                        }
                        else{
                            machineCode.append(instruction);
                        }
                    }
                }
                for (int i = 2; i < machineCode.length(); i += 3) {
                    machineCode.insert(i, ' ');
                }
                System.out.println(machineCode);
            } catch (IOException e) {
                System.out.println("File Error: " + e.getMessage());
            }
        }
    }

