package it.unibo.homemanager.situatedness;

import alice.respect.situatedness.AbstractTransducer;
import alice.respect.situatedness.ISimpleProbe;
import alice.respect.situatedness.TransducerId;
import alice.tucson.api.ITucsonOperation;
import alice.tuplecentre.api.TupleCentreId;
import alice.tuplecentre.core.AbstractTupleCentreOperation;

public class LightTransducer extends AbstractTransducer {

	public LightTransducer(final TransducerId arg0, final TupleCentreId arg1) {
		super(arg0, arg1);
	}

	@Override
	public void operationCompleted(ITucsonOperation arg0) {
		// TODO
		
	}

	@Override
	public void operationCompleted(AbstractTupleCentreOperation arg0) {
		// TODO	
	}

	@Override
	public boolean getEnv(final String key) {
        this.speak("[" + this.id + "]: Reading...");
        boolean success = true;
        final Object[] keySet = this.probes.keySet().toArray();
        /*
         * for each probe this transducer models, stimulate it to sense its
         * environment
         */
        for (final Object element : keySet) {
            if (!((ISimpleProbe) this.probes.get(element)).readValue(key)) {
                this.speakErr("[" + this.id + "]: Read failure!");
                success = false;
                break;
            }
        }
        return success;
	}

	@Override
	public boolean setEnv(String key, int value) {
        this.speak("[" + this.id + "]: Writing...");
        boolean success = true;
        final Object[] keySet = this.probes.keySet().toArray();
        /*
         * for each probe this transducer models, stimulate it to act on its
         * environment
         */
        for (final Object element : keySet) {
            if (!((ISimpleProbe) this.probes.get(element)).writeValue(key, value)) {
                this.speakErr("[" + this.id + "]: Write failure!");
                success = false;
                break;
            }
        }
        return success;
	}

}
