package co.uk.neo4j.estimate.dto;

public record TurnOffMessage(long timestamp) implements Message {
  // No additional methods needed, record provides timestamp()
}