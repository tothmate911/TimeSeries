import axios from "axios";

interface Props {
    setAvailablePowerStations: React.Dispatch<React.SetStateAction<string[]>>;
}

const Upload: React.FC<Props> = ({ setAvailablePowerStations }) => {

    const handleFileInputChange = async (event: React.FormEvent<HTMLInputElement>) => {
        const files = event.currentTarget.files;
        if (!files) {
            return;
        }
        for (const file of Array.from(files)) {
            const text = await file.text();
            const json = JSON.parse(text);
            try {
                const data = axios.put("/time-series", json);
                console.log(data);
            } catch (error) {
                console.log(error);
            }
        }

        axios.get("/power-stations")
            .then((response) => {
                const powerStations: string[] = response.data;
                setAvailablePowerStations(powerStations);
            })
            .catch((error) => {
                console.log(error);
            });
    }

    return (
        <div>
            <label className="upload-label" htmlFor="file">Upload TimeSeries</label>
            <input id="file" multiple={true} type="file" onChange={handleFileInputChange} />
        </div>
    )
}

export default Upload