import axios from "axios";

const Upload: React.FC = () => {

    const handleFileInputChange = async (event: React.FormEvent<HTMLInputElement>) => {
        const files = event.currentTarget.files;
        if (!files) {
            return;
        }

        const fileArray = Array.from(files);
        fileArray.sort((file1, file2) => {
            return file1.name.localeCompare(file2.name);
        });

        for (const file of fileArray) {
            const text = await file.text();
            const json = JSON.parse(text);
            try {
                await axios.put("/time-series", json);
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