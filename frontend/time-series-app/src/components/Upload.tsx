import axios from "axios";

const backendUrl = "http://localhost:8080/api";
axios.defaults.baseURL = backendUrl;

const Upload = () => {

    const handleFileInputChange =  (event: React.FormEvent<HTMLInputElement>) => {
        const files = event.currentTarget.files || [];
        Array.from(files).forEach(file => {
            console.log("Upload form file: " + file.name);
            file.text().then(async text => {
                const json = JSON.parse(text);
                console.log("File content: " + json);
                try {                
                    const data = await axios.put("/time-series", json);
                    console.log(data);
                } catch (error) {
                    console.log(error);
                }
            });                    
        });
    }

    return (
        <form>
            <div>
                <label>Upload a TimeSeries</label>
                <input type="file" multiple={true} onChange={handleFileInputChange}/>
            </div>
        </form>
        
    )
}

export default Upload