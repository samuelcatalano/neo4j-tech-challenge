package co.uk.neo4j.estimate.dto;

public record DeltaMessage(long timestamp, float deltaValue) implements Message {
  // No additional methods needed, record provides timestamp() and deltaValue()
}
