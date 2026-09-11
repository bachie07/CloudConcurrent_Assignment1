
const statusE1 = document.querySelector(".status")

const statusMessage = document.querySelector("#statusMess")

const statusMessage2 = document.querySelector('#statusMess2')

const btn = document.querySelector("#recordButton")

let recording = false;

let recorder =  null;





async function startRecording(){ // start recording function 

    try{ 


    const stream = await navigator.mediaDevices.getUserMedia({ audio: true});

    recorder = new MediaRecorder(stream);
	
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

        statusMessage.textContent = "Transcription failed";
        console.error("Upload failed: ", error)

    } finally {
        statusE1.classList.remove(`recording`);
        btn.disabled = false;
    }
    
}



btn.addEventListener("click", () => {
	
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




