package velvetdawn.server.models;

import velvetdawn.server.VelvetDawnServerInstance;

import java.util.List;
import java.util.stream.Collectors;

public class MapResponse {

    public int width;
    public int height;
    public List<APITileInstance> tiles;

    public MapResponse() {
        var velvetDawn = VelvetDawnServerInstance.getInstance();

        this.width = velvetDawn.map.width;
        this.height = velvetDawn.map.height;

        this.tiles = velvetDawn.map.listTiles()
                .stream()
                .map(APITileInstance::from)
                .collect(Collectors.toList());
    }

}
