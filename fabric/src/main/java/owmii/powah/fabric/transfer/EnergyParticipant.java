package owmii.powah.fabric.transfer;

import net.fabricmc.fabric.api.transfer.v1.transaction.base.SnapshotParticipant;
import owmii.powah.lib.logistics.energy.Energy;

public class EnergyParticipant extends SnapshotParticipant<Long> {
	public static EnergyParticipant get(Energy energy) {
		if (energy.platformWrapper == null) {
			energy.platformWrapper = new EnergyParticipant(energy);
		}
		return (EnergyParticipant) energy.platformWrapper;
	}

	private final Energy energy;

	private EnergyParticipant(Energy energy) {
		this.energy = energy;
	}

	@Override
	protected Long createSnapshot() {
		return energy.getStored();
	}

	@Override
	protected void readSnapshot(Long snapshot) {
		energy.setStored(snapshot);
	}
}
