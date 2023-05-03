package velvetdawn.server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.json.JsonMapper;
import org.jetbrains.annotations.NotNull;
import velvetdawn.core.constants.AttributeKeys;
import velvetdawn.core.models.Coordinate;
import velvetdawn.core.models.anytype.Any;
import velvetdawn.core.utils.Path;
import velvetdawn.server.auth.Authenticator;
import velvetdawn.server.models.GameState;
import velvetdawn.server.models.MapResponse;
import velvetdawn.server.routing.CombatRouting;
import velvetdawn.server.routing.DatapackRouting;
import velvetdawn.server.routing.EntitiesRouting;
import velvetdawn.server.routing.SetupRouting;
import velvetdawn.server.routing.TurnRouting;

import java.io.IOException;
import java.lang.reflect.Type;

public class VelvetDawnServer {

    public static void main(String[] args) throws Exception {
        VelvetDawnServerInstance.init();


        var vd = VelvetDawnServerInstance.getInstance();
        var p1 = vd.players.join("sam", "bananana");
        var p2 = vd.players.join("sam2", "bananana");
        vd.game.setup.updateSetup("civil-war:commander", 1);
        vd.game.setup.updateSetup("civil-war:cannons", 2);
        vd.game.setup.updateSetup("civil-war:pikemen", 2);
        vd.game.setup.updateSetup("civil-war:cavalry", 2);
        vd.game.setup.updateSetup("civil-war:musketeers", 2);
        vd.game.startSetupPhase();

        vd.game.setup.placeEntity(p1, "civil-war:commander", new Coordinate(14, 1));
        vd.game.setup.placeEntity(p1, "civil-war:cavalry", new Coordinate(13, 1));
        vd.game.setup.placeEntity(p1, "civil-war:cannons", new Coordinate(14, 0));

        vd.game.setup.placeEntity(p2, "civil-war:commander", new Coordinate(14, 18));
        vd.game.setup.placeEntity(p2, "civil-war:cavalry", new Coordinate(14, 17));
        vd.game.turns.ready(p1);
        vd.game.turns.ready(p2);
        vd.game.startGamePhase();

        Gson gson = new GsonBuilder().create();
        JsonMapper gsonMapper = new JsonMapper() {
            @Override
            public String toJsonString(@NotNull Object obj, @NotNull Type type) {
                return gson.toJson(obj, type);
            }

            @Override
            public <T> T fromJsonString(@NotNull String json, @NotNull Type targetType) {
                return gson.fromJson(json, targetType);
            }
        };
        var app = Javalin.create(config -> {
            config.plugins.enableCors(cors -> cors.add(it -> it.anyHost()));
            config.jsonMapper(gsonMapper);
        })
            .get("/ping/", VelvetDawnServer::ping)
            .get("/map/", VelvetDawnServer::getMap)
            .get("/game-state/", VelvetDawnServer::getGameState)
            .post("/join/", VelvetDawnServer::join)
            .get("/", VelvetDawnServer::loadWebpage)
            .get("/app.js", VelvetDawnServer::loadApp);

        CombatRouting.init(app);
        DatapackRouting.init(app);
        EntitiesRouting.init(app);
        SetupRouting.init(app);
        TurnRouting.init(app);

        app.start(1642);
    }

    private static void ping(Context ctx) {
        ctx.result("pong");
    }

    private static void getMap(Context ctx) {
        System.out.println("Returning map");
        ctx.json(new MapResponse());
    }

    private static void getGameState(Context ctx) throws Exception {
        var player = Authenticator.authenticate(ctx);

        VelvetDawnServerInstance.getInstance().game.turns.checkEndTurnCase();

        ctx.json(GameState.from(player, Boolean.parseBoolean(ctx.queryParam("full-state"))));
    }

    private static void join(Context ctx) throws Exception {
        var velvetDawn = VelvetDawnServerInstance.getInstance();

        var username = ctx.formParam("username");
        var password = ctx.formParam("password");

        if (username == null || password == null)
            throw new Exception("Missing username or password in request");

        var player = velvetDawn.players.join(username, password);

        ctx.json(player.toJson().toGson());
    }

    private static void loadWebpage(Context ctx) throws IOException {
        var path = new Path("./src/")
                .getChild("frontend")
                .getChild("dist")
                .getChild("index.html");

        ctx.html(path.readString());
    }

    private static void loadApp(Context ctx) throws IOException {
        var path = new Path("./src/")
                .getChild("frontend")
                .getChild("dist")
                .getChild("app.js");

        ctx.result(path.readString());
    }
}

//@app.after_request
//def add_cors_headers(response):
//    if request.referrer is not None:
//    response.headers.add('Access-Control-Allow-Origin', request.referrer[:-1])
//    else:
//    response.headers.add('Access-Control-Allow-Origin', "*")
//    response.headers.add('Access-Control-Allow-Credentials', 'true')
//    response.headers.add('Access-Control-Allow-Headers', 'Content-Type')
//    response.headers.add('Access-Control-Allow-Headers', 'Cache-Control')
//    response.headers.add('Access-Control-Allow-Headers', 'X-Requested-With')
//    response.headers.add('Access-Control-Allow-Headers', 'Authorization')
//    response.headers.add('Access-Control-Allow-Methods', 'GET, POST, OPTIONS, PUT, DELETE')
//
//    return response
//
//
//    if not os.environ.get("DEV") == "true":
//    import logging
//    log = logging.getLogger('werkzeug')
//    log.setLevel(logging.ERROR)

