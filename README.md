# Energy Estimator Application

This repository contains Java classes for an Energy Estimator application that calculates energy usage based on lighting control messages.

## Overview

The Energy Estimator application comprises two main classes: `EnergyEstimatorService` and `EnergyEstimatorApplication`.

### EnergyEstimatorService Class
- **Description:** This class is responsible for estimating the energy usage of a building based on lighting control messages. It utilizes dimmer value history to calculate energy usage for a given time range.
- **Attributes:**
  - `currentDimmerValue`: Represents the current dimmer value.
  - `dimmerValueHistory`: A TreeMap storing the history of dimmer values over time.
- **Methods:**
  - `processMessage(Message message)`: Processes lighting control messages to update dimmer values.
  - `calculateEnergyUsage(long startTimestamp, long endTimestamp)`: Calculates energy usage within a specified time range.
  - `parseMessage(String inputLine)`: Parses input lines to create specific message types.

### EnergyEstimatorApplication Class
- **Description:** Main class of the energy estimator application that interacts with users through the command line interface.
- **Functionality:**
  - Reads input from the command line to process messages and calculate energy usage using the `EnergyEstimatorService`.
  - Provides a user-friendly interface for input validation and energy usage calculation.

## Usage Instructions

To run the Energy Estimator application:
1. Compile the Java classes using a Java compiler.
2. Run the `EnergyEstimatorApplication` class in your IDE or terminal.
3. Input messages in the following format:
   - Turn off message: `<timestamp> TurnOff`
   - Delta message: `<timestamp> Delta <delta_value>`
   - Usage message: `<timestamp> Usage <start_timestamp> <end_timestamp>`

## Sample Input Format

- Turn off message: `<timestamp> TurnOff`
- Delta message: `<timestamp> Delta <delta_value>`
- Usage message: `<timestamp> Usage <start_timestamp> <end_timestamp>`

For detailed implementation and usage examples, refer to the source code in this repository.

