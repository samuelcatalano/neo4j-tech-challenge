package co.uk.neo4j.estimate;

import co.uk.neo4j.estimate.dto.Message;
import co.uk.neo4j.estimate.dto.UsageMessage;
import co.uk.neo4j.estimate.service.EnergyEstimatorService;

import java.text.DecimalFormat;
import java.util.Scanner;

/**
 * Main class of the energy estimator application.
 */
public class EnergyEstimatorApplication {

  /**
   * Main method of the application.
   * @param args command line arguments
   */
  public static void main(final String[] args) {
    final Scanner scanner = new Scanner(System.in);
    final EnergyEstimatorService service = new EnergyEstimatorService();

    while (scanner.hasNextLine()) {
      final String inputLine = scanner.nextLine();
      // Validate input and throw an Exception if it is null or empty
      validateInput(inputLine);

      final Message message = service.parseMessage(inputLine);

      if (message != null) {
        service.processMessage(message);
        if (message instanceof UsageMessage) {
          final float energyUsage = service.calculateEnergyUsage(message.timestamp(), ((UsageMessage) message).getEndTimestamp());
          final DecimalFormat formatter = new DecimalFormat("0.###");

          System.out.println(formatter.format(energyUsage) + " Wh");
          System.exit(0);
        }
      }
    }
  }

  private static void validateInput(String input) {
    if (input == null || input.trim().isEmpty()) {
      throw new IllegalArgumentException("Input cannot be null or empty.");
    }
  }
}
