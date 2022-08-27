import axios from "axios";

const Upload = () => {

    const handleFileInputChange = (event: React.FormEvent<HTMLInputElement>) => {
        const files = event.currentTarget.files || [];
        Array.from(files).forEach(async (file) => {
            const text = await file.text();
            const json = JSON.parse(text);
            console.log("File content: " + json);
            try {
                const data = await axios.put("/time-series", json);
                console.log(data);
            } catch (error) {
                console.log(error);
            }
        });
    }

    return (
        <div>
            <label className="upload-label" htmlFor="file">Upload TimeSeries</label>
            <input id="file" type="file" onChange={handleFileInputChange} />
        </div>
    )
}

export default Upload