import * as React from "react";
import {getUrl} from "api/utils";
import {VelvetDawn} from "../../velvet-dawn/velvet-dawn";


export function ServerResourceLoadingScreen({ children }: { children: React.ReactElement }) {
    const [loading, setLoading] = React.useState(true);

    React.useEffect(() => {
        VelvetDawn.init()
            .then(() => {
                // TODO Load images and audio in. Once done, show the lobby screen
                console.log(VelvetDawn.datapacks.entities)
                console.log(VelvetDawn.datapacks.resources)
                console.log(VelvetDawn.datapacks.tiles)
                setLoading(false)
            })
            .catch((e) => {
                console.log(e)
                alert('Unable to load')
            })
    }, [])

    if (loading)
        return <div>Loading</div>

    return children
}
