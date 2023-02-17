package velvetdawn.mechanics.selectors.tiles;

import velvetdawn.VelvetDawn;
import velvetdawn.mechanics.selectors.Selector;
import velvetdawn.models.instances.Instance;
import java.util.Collection;

public class SelectorTiles extends Selector {

    /* Get a list of tiles */

    public SelectorTiles(VelvetDawn velvetDawn) {
        super(velvetDawn);
    }

    @Override
    protected Collection<Instance> getSelection(Instance instance) {
        return this.filters.filter(instance, velvetDawn.map.listTiles());
    }
}
