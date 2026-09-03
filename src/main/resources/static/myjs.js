
const statusE1 = document.querySelector(".status")

const statusMessage = document.querySelector("#statusMess")

const btn = document.querySelector("#recordButton")



async function startRecording(){

    try{ 



    const stream = await navigator.mediaDevices.getUserMedia({ audio: true});

    const recorder = new MediaRecorder(stream);

    recorder.onstop = () => {

        console.log("Recording stopped")
    }

    recorder.start();

    console.log("Recording started...")

    setTimeout(() => {
        recorder.stop();
    },3000)

    }
    
    catch(error){
        statusMessage.textContent = "Idle";
        statusE1.classList.remove('recording');
        console.log("Mic access failed")
        console.log(error)
    }
}


btn.addEventListener("click", () => {
    
    statusMessage.textContent = 'Recording';
    statusE1.classList.add('recording');
    startRecording();

})




