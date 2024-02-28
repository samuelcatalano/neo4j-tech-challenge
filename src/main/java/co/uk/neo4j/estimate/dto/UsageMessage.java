package co.uk.neo4j.estimate.dto;

public class UsageMessage implements Message {

  private final long startTimestamp;
  private final long endTimestamp;

  public UsageMessage(final long startTimestamp, final long endTimestamp) {
    this.startTimestamp = startTimestamp;
    this.endTimestamp = endTimestamp;
  }

  @Override
  public long timestamp() {
    return startTimestamp;
  }

  public long getEndTimestamp() {
    return endTimestamp;
  }
}