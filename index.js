const quotes = [
  "The only limit to our realization of tomorrow is our doubts of today.",
  "The future belongs to those who believe in the beauty of their dreams.",
  "Success is not final, failure is not fatal: It is the courage to continue that counts.",
  "What lies behind us and what lies before us are tiny matters compared to what lies within us.",
  "The best way to predict the future is to create it.",
  "You miss 100% of the shots you don't take.",
  "It does not matter how slowly you go as long as you do not stop.",
  "Your time is limited, so don't waste it living someone else's life.",
  "The only way to do great work is to love what you do.",
];

let words = [];

let wordIndex = 0;

let startTime = Date.now();

const quoteElement = document.getElementById("quote");
const messageElement = document.getElementById("message");
const inputElement = document.getElementById("typed-value");
let accuracy = 100;

document.getElementById("start").addEventListener("click", function () {
  document.getElementById("start").innerText = "Start";
  const quoteIndex = Math.floor(Math.random() * quotes.length);
  const quote = quotes[quoteIndex];

  words = quote.split(" ");
  wordIndex = 0;

  const spanWords = words.map(function (word) {
    return `<span>${word} </span>`;
  });

  quoteElement.innerHTML = spanWords.join("");

  quoteElement.childNodes[0].className = "highlight";

  messageElement.innerText = "";
  inputElement.value = "";
  inputElement.focus();

  startTime = new Date().getTime();
});

document.getElementById("typed-value").addEventListener("input", (e) => {
  const currentWord = words[wordIndex];
  const typedValue = inputElement.value;
  if (typedValue === currentWord && wordIndex === words.length - 1) {
    inputElement.value = "";
    const elapsedTime = new Date().getTime() - startTime;
    const message = `Congratulation! You finished in ${
      elapsedTime / 1000
    } seconds.`;
    messageElement.innerText = message;
    document.getElementById("wpm").innerText = `${
      Math.round((words.length / (elapsedTime / 60000)) * 100) / 100
    }`;
    document.getElementById("start").innerText = "Restart";
  } else if (typedValue.endsWith(" ") && typedValue.trim() === currentWord) {
    inputElement.value = "";
    wordIndex++;
    for (const wordElement of quoteElement.childNodes) {
      wordElement.className = "";
    }
    quoteElement.childNodes[wordIndex].className = "highlight";
  } else if (currentWord.startsWith(typedValue)) {
    quoteElement.childNodes[wordIndex].className = "highlight";
    quoteElement.childNodes[wordIndex].classList.remove("error");
  } else {
    quoteElement.childNodes[wordIndex].className = "error";
    accuracy = Math.max(0, accuracy - 1);
    document.getElementById("accuracy").innerText = `${accuracy}`;
  }
});
