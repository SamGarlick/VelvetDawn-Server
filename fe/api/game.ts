import {get} from "api/utils";
import {GameState} from "models/gameState";
import {VelvetDawn} from "../velvet-dawn/velvet-dawn";


export function getState(): Promise<GameState>  {
    return get(`/game-state/?username=${VelvetDawn.loginDetails.username}&password=${VelvetDawn.loginDetails.password}`)
}