
const statusE1 = document.querySelector(".status")

const statusMessage = document.querySelector("#statusMess")

const statusMessage2 = document.querySelector('#statusMess2')

const btn = document.querySelector("#recordButton")

let recording = false;

let recorder =  null;





async function startRecording(){ // start recording function 

    try{ 


    const stream = await navigator.mediaDevices.getUserMedia({ audio: true});

	
	// lower audio bits since its speech, help optimize upload size / time 
	
	recorder = new MediaRecorder(stream, { audioBitsPerSecond: 32000 });	
	
	const chunks = [];
	
	recorder.ondataavailable = (event) => {
		chunks.push(event.data);
	}

    recorder.onstop = () => {
		
		const audioBlob = new Blob(chunks, { type: "audio/webm"})
		
		uploadAudio(audioBlob);
    }

    recorder.start();

    console.log("Recording started...")


    } 
    
    catch(error){
		
		// meesage for mic access failure, handling the permission problem instead of upload or networl
		
        statusMessage.textContent = "Idle";
        statusE1.classList.remove('recording');
        console.log("Mic access failed")
        console.log(error)
    }
}

async function uploadAudio(audioBlob){

    statusMessage.textContent = "Transcribing...";

    const formData = new FormData();

    formData.append("audio", audioBlob, "recording.webm");

    try {

        const response = await fetch("http://localhost:8080/api/transcribe" , {
            method: "POST",
            body: formData
        });

        if (!response.ok){
            throw new Error(`Server error: ${response.status}`);
        }

        const data = await response.json();

        statusMessage.textContent = data.text;
    }

    catch(error){
		
		//handling server error and network error 

		if (error.message.includes("Server error")) {
			
		    statusMessage.textContent = "Server error - please try again in a moment";
			
		} else {
			
		    statusMessage.textContent = "Network error - check your connection and try again";
		}
        console.error("Upload failed: ", error)

    } finally {
		// if user already get transcription 
		
        statusE1.classList.remove(`recording`);
		statusMessage2.textContent = 'Start again? Click the record button';
        btn.disabled = false;
    }
    
}



btn.addEventListener("click", () => {
	
	// handling click event 
	if(!recording){
		
		recording = true;
		statusMessage.textContent = 'Recording';
		statusMessage2.textContent = 'press the stop button to transcribe'
		statusE1.classList.add('recording');
		btn.classList.add('recording');
		btn.disabled = true;
		startRecording().then(() => {
			btn.disabled = false;
		});
		
	}
	
	else{
		
		btn.classList.remove('recording');
		recording = false;
		recorder.stop();
		
	}
    

})




