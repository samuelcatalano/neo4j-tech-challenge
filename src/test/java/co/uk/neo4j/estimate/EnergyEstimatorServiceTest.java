package co.uk.neo4j.estimate;

import co.uk.neo4j.estimate.dto.DeltaMessage;
import co.uk.neo4j.estimate.dto.Message;
import co.uk.neo4j.estimate.dto.TurnOffMessage;
import co.uk.neo4j.estimate.service.EnergyEstimatorService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests the functionality of the EnergyEstimatorService class.
 */
class EnergyEstimatorServiceTest {

  /**
   * Tests the processMessage() method with a TurnOffMessage object.
   */
  @Test
  void testProcessMessage_TurnOffMessage() {
    final EnergyEstimatorService service = new EnergyEstimatorService();
    final TurnOffMessage turnOffMessage = new TurnOffMessage(123456789L);

    service.processMessage(turnOffMessage);

    assertEquals(0.0f, service.getCurrentDimmerValue(), 0.0f);
    assertEquals(1, service.getDimmerValueHistory().size());
    assertEquals(0.0f, service.getDimmerValueHistory().get(123456789L), 0.0f);
  }

  /**
   * Tests the processMessage() method with a DeltaMessage object.
   */
  @Test
  void testProcessMessage_DeltaMessage() {
    final EnergyEstimatorService service = new EnergyEstimatorService();
    final DeltaMessage deltaMessage = new DeltaMessage(123456789L, 0.5f);

    service.processMessage(deltaMessage);

    assertEquals(0.5f, service.getCurrentDimmerValue(), 0.0f);
    assertEquals(1, service.getDimmerValueHistory().size());
    assertEquals(0.5f, service.getDimmerValueHistory().get(123456789L), 0.0f);
  }

  /**
   * Tests the parseMessage() method with a TurnOff message.
   */
  @Test
  void testParseMessage_TurnOff() {
    final EnergyEstimatorService service = new EnergyEstimatorService();
    final Message message = service.parseMessage("123456789 TurnOff");

    assertInstanceOf(TurnOffMessage.class, message);
    assertEquals(123456789L, message.timestamp());
  }

  /**
   * Tests the calculateEnergyUsage() method.
   */
  @Test
  void testCalculateEnergyUsage() {
    final EnergyEstimatorService service = new EnergyEstimatorService();

    service.processMessage(new TurnOffMessage(1544206562L));
    service.processMessage(new DeltaMessage(1544206563L, 0.5f));
    service.processMessage(new DeltaMessage(1544210163L, -0.25f));
    service.processMessage(new DeltaMessage(1544211963L, 0.75f));
    service.processMessage(new DeltaMessage(1544211963L, 0.75f));
    service.processMessage(new TurnOffMessage(1544213763L));

    final float energyUsage = service.calculateEnergyUsage(1544206562L, 1544213763L);

    assertEquals(5.625f, energyUsage, 0.0f);
  }

  /**
   * Tests the parseMessage() method with an invalid input.
   */
  @Test
  void testParseException_InvalidInput() {
    final EnergyEstimatorService service = new EnergyEstimatorService();
    assertThrows(IllegalArgumentException.class, () -> {
      service.parseMessage("Invalid Input");
    });
  }
}
