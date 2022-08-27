import axios from "axios";

const Upload: React.FC = () => {

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

        window.location.reload();
    }

    return (
        <div>
            <label className="upload-label" htmlFor="file">Upload TimeSeries</label>
            <input id="file" multiple={true} type="file" onChange={handleFileInputChange} />
        </div>
    )
}

export default Upload