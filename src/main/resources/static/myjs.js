
const statusE1 = document.querySelector(".status")

const statusMessage = document.querySelector("#statusMess")

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
	
		console.log("Recording stopped, blob size:", audioBlob.size)
		
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


btn.addEventListener("click", () => {
	
	if(!recording){
		
		recording = true;
		statusMessage.textContent = 'Recording';
		statusE1.classList.add('recording');
		startRecording();
		
		
	}
	
	else{
		
		recording = false;
		recorder.stop();
		statusMessage.textContent = "Idle";
		statusE1.classList.remove('recording');
		
	}
    


})




