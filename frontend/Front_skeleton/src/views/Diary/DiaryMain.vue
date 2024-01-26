<template>
  <div class="backg">
    <div class="greeting-box">
      <p>{{ talk }}</p>
      <button @click="startListening">일기 쓰기</button>
      <button @click="stopListening">일기 종료</button>
      <br>
      <PetComponent />
      <!-- <img src="@/assets/character/오리너구리.png" alt=""> -->
    </div>
  </div>

</template>

<script>
import OpenAI from 'openai';

export default {
    
data() {
    return {
    recognition: null,
    transcribedText: '',
    interimTranscript: '',
    talk:"안녕! 오늘의 일기를 쓸래?"
    };
},
methods: {
    startListening() {
    // 브라우저 지원 여부 확인
    if ('webkitSpeechRecognition' in window) {
        this.recognition = new webkitSpeechRecognition();
        this.recognition.continuous = true;
        this.recognition.interimResults = true;

        this.recognition.onstart = () => {
          this.talk = '오늘 하루는 어땠어?'
          console.log('음성 인식이 시작되었습니다.');
        };

        this.recognition.onresult = (event) => {
        this.interimTranscript = '';
        for (let i = event.resultIndex; i < event.results.length; i++) {
            const transcript = event.results[i][0].transcript;
            if (event.results[i].isFinal) {
            this.transcribedText += transcript + ' ';
            
            } else {
            this.interimTranscript += transcript;
            }
        }
        console.log('중간 결과:', this.interimTranscript);
        };

        this.recognition.onerror = (event) => {
        console.error('음성 인식 오류:', event.error);
        };

        this.recognition.onend = () => {
            this.sendToChatGPT(this.transcribedText); // 음성 인식 결과를 ChatGPT API로 보냄
        console.log('음성 인식이 종료되었습니다.');
        };

        this.recognition.start();
    } else {
        console.error('이 브라우저에서는 음성 인식이 지원되지 않습니다.');
    }
    },
    stopListening() {
    if (this.recognition) {
        this.recognition.stop();
    }
    },
    async sendToChatGPT(text) { // ChatGPT API 요청을 위한 메소드
        try {
        const openai = new OpenAI({
            apiKey: import.meta.env.VITE_API_URL,
            dangerouslyAllowBrowser: true
        });

        async function askQuestion(question) {
        return new Promise((resolve, reject) => {
            question(question, (answer) => {
            resolve(answer);
            });
        });
        }

        async function main() {
        try {
            const assistantId = "asst_OieWYU1AJco4iF1xXOnWBQg4";
            const assistantFilePath = "./assistant.json";

            // Check if the assistant.json file exists
            try {
            const assistantData = await fsPromises.readFile(
                assistantFilePath,
                "utf8"
            );
            
            console.log("\nExisting assistant detected.\n");
            } catch (error) {
            
            console.log("No existing assistant detected, creating new.\n");

            const assistant = await openai.beta.assistants.retrieve(assistantId);
            }

            console.log(
            `Hello there, I'm your personal assistant.Id:\n${assistantId}\n`
            );

            // Create a thread using the assistantId
            const thread = await openai.beta.threads.create();
            // Use keepAsking as state for keep asking questions

        let continueAskingQuestion = true;
    
          const userQuestion = text;

          // Pass in the user question into the existing thread
          await openai.beta.threads.messages.create(thread.id, {
            role: "user",
            content: userQuestion,
          });

          // Create a run
          const run = await openai.beta.threads.runs.create(thread.id, {
            assistant_id: assistantId,
          });

          // Imediately fetch run-status, which will be "in_progress"
          let runStatus = await openai.beta.threads.runs.retrieve(
            thread.id,
            run.id
          );

          // Polling mechanism to see if runStatus is completed
          while (runStatus.status !== "completed") {
            await new Promise((resolve) => setTimeout(resolve, 1000));
            runStatus = await openai.beta.threads.runs.retrieve(
              thread.id,
              run.id
            );

            if (runStatus.status === "requires_action") {
            //   console.log(
            //     runStatus.required_action.submit_tool_outputs.tool_calls
            //   );
              const toolCalls =
                runStatus.required_action.submit_tool_outputs.tool_calls;
              const toolOutputs = [];

              for (const toolCall of toolCalls) {
                const functionName = toolCall.function.name;

                console.log(
                  `This question requires us to call a function: ${functionName}`
                );

                const args = JSON.parse(toolCall.function.arguments);

                const argsArray = Object.keys(args).map((key) => args[key]);

                // Dynamically call the function with arguments
                const output = await global[functionName].apply(null, [args]);

                toolOutputs.push({
                  tool_call_id: toolCall.id,
                  output: output,
                });
              }
              // Submit tool outputs
              await openai.beta.threads.runs.submitToolOutputs(
                thread.id,
                run.id,
                { tool_outputs: toolOutputs }
              );
              continue; // Continue polling for the final response
            }

            // Check for failed, cancelled, or expired status
            if (["failed", "cancelled", "expired"].includes(runStatus.status)) {
              console.log(
                `Run status is '${runStatus.status}'. Unable to complete the request.`
              );
              break; // Exit the loop if the status indicates a failure or cancellation
            }
          }

          // Get the last assistant message from the messages array
          const messages = await openai.beta.threads.messages.list(thread.id);

          // Find the last message for the current run
          const lastMessageForRun = messages.data
            .filter(
              (message) =>
                message.run_id === run.id && message.role === "assistant"
            )
            .pop();
          console.log(messages)
          // If an assistant message is found, console.log() it
          if (lastMessageForRun) {
            console.log(`${lastMessageForRun.content[0].text.value} \n`);
          } else if (
            !["failed", "cancelled", "expired"].includes(runStatus.status)
          ) {
            console.log("No response received from the assistant.");
          }

          // Ask if the user wants to ask another question
          console.log(lastMessageForRun.content[0].text.value)
          const string = lastMessageForRun.content[0].text.value
          var fixedJsonString = string.replace(/,(\s*[}]])/g, '$1');
          const obj = JSON.parse(fixedJsonString)
          console.log(obj.bodyData.key_word)
    
            // close the readline
            //readline.close();
        } catch (error) {
            console.error(error);
        }
        }

        // Call the main function
        main();
        } catch(error) {
          console.error('ChatGPT 요청 오류:', error);
        }
      },
},
};
</script>
<style scoped>
.backg {
  position: relative;
  width: 100%;
  height: 100%;
  background: url('background-image-url.jpg') no-repeat center center;
  background-size: cover;
}

.greeting-box {
  position: absolute;
  top: 10%;
  left: 50%;
  transform: translateX(-50%);
  background-color: rgba(255, 255, 255, 0.5);
  padding: 20px;
  border-radius: 10px;
  text-align: center;
}

.greeting-box p {
  margin: 0;
  padding: 0;
  color: #333;
  font-size: 1.5rem;
}

.greeting-box button {
  margin-top: 10px;
  padding: 10px 20px;
  border: none;
  border-radius: 5px;
  background-color: #f9f9f9;
  cursor: pointer;
  transition: background-color 0.3s;
}

.greeting-box button:hover {
  background-color: #e0e0e0;
}

.bear-image {
  position: absolute;
  bottom: 0;
  left: 50%;
  transform: translateX(-50%);
  /* 이미지 경로를 설정하세요 */
}
</style>