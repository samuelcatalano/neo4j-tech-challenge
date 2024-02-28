package co.uk.neo4j.estimate.service;

import co.uk.neo4j.estimate.dto.DeltaMessage;
import co.uk.neo4j.estimate.dto.Message;
import co.uk.neo4j.estimate.dto.TurnOffMessage;
import co.uk.neo4j.estimate.dto.UsageMessage;

import java.util.Map;
import java.util.TreeMap;

/**
 * This class is responsible for estimating the energy usage of a building based on the lighting control messages.
 * It uses the dimmer value history to calculate the energy usage for a given time range.
 */
public class EnergyEstimatorService {

  private float currentDimmerValue = 0.0f;
  private final TreeMap<Long, Float> dimmerValueHistory = new TreeMap<>();

  /**
   * Processes the lighting control message.
   * @param message the lighting control message
   */
  public void processMessage(final Message message) {
    if (message instanceof TurnOffMessage) { // If the message is a turn off message
      currentDimmerValue = 0.0f; // Set the current dimmer value to 0
      dimmerValueHistory.put(message.timestamp(), currentDimmerValue); // Update the dimmer value history
    } else if (message instanceof DeltaMessage) { // If the message is a delta message
      currentDimmerValue = Math.max(0.0f, Math.min(1.0f, currentDimmerValue + ((DeltaMessage) message).deltaValue())); // Adjust the current dimmer value based on the delta message
      dimmerValueHistory.put(message.timestamp(), currentDimmerValue); // Update the dimmer value history
    }
  }

  /**
   * Calculates the energy usage for the given time range.
   *
   * @param startTimestamp the start timestamp of the time range
   * @param endTimestamp   the end timestamp of the time range
   * @return the energy usage for the given time range
   */
  public float calculateEnergyUsage(final long startTimestamp, final long endTimestamp) {
    float energyUsage = 0.0f; // Initialize energy usage
    Map.Entry<Long, Float> previousEntry = dimmerValueHistory.floorEntry(startTimestamp); // Get the previous dimmer value entry

    for (final Map.Entry<Long, Float> entry : dimmerValueHistory.subMap(startTimestamp, true, endTimestamp, true).entrySet()) { // Iterate over the dimmer value history within the specified time range
      if (previousEntry != null) { // If there is a previous entry
        final long timeDiff = entry.getKey() - previousEntry.getKey(); // Calculate the time difference
        energyUsage += timeDiff * previousEntry.getValue() * 5 / 3600.0f; // Update energy usage based on time difference and previous dimmer value (converted to Wh)
      }
      previousEntry = entry; // Update the previous entry
    }

    if (previousEntry != null && previousEntry.getKey() < endTimestamp) { // If there is a previous entry within the specified time range
      final long timeDiff = endTimestamp - previousEntry.getKey(); // Calculate the time difference
      energyUsage += timeDiff * previousEntry.getValue() * 5 / 3600.0f; // Update energy usage based on time difference and previous dimmer value (converted to Wh)
    }
    return energyUsage; // Return the calculated energy usage
  }

  /**
   * Parses a message from the input line.
   *
   * @param inputLine the input line
   * @return the parsed message or null if the input line is invalid
   */
  public Message parseMessage(final String inputLine) {
    final String[] parts = inputLine.split(" "); // Split the input line by spaces to extract different parts
    final long timestamp = Long.parseLong(parts[0]); // Parse the first part as a long value (timestamp)

    switch (parts[1]) { // Check the type of message based on the second part of the input line
      case "TurnOff":
        return new TurnOffMessage(timestamp); // Create and return a TurnOffMessage with the parsed timestamp
      case "Delta":
        final float deltaValue = Float.parseFloat(parts[2]); // Parse the third part as a float value (delta)
        return new DeltaMessage(timestamp, deltaValue); // Create and return a DeltaMessage with the parsed timestamp and delta value
      case "Usage":
        final long startTimestamp = Long.parseLong(parts[2]); // Parse the third part as a long value (start timestamp)
        final long endTimestamp = Long.parseLong(parts[3]); // Parse the fourth part as a long value (end timestamp)
        return new UsageMessage(startTimestamp, endTimestamp); // Create and return an UsageMessage with the parsed start and end timestamps
      default:
        return null; // Return null for unknown message types or invalid input lines
    }
  }

  /**
   * Returns the current dimmer value.
   * @return the current dimmer value
   */
  public float getCurrentDimmerValue() {
    return currentDimmerValue;
  }

  /**
   * Returns the dimmer value history.
   * @return the dimmer value history
   */
  public Map<Long, Float> getDimmerValueHistory() {
    return dimmerValueHistory;
  }
}