(() => {
  const timer = document.getElementById("quiz-timer");
  const quizForm = document.getElementById("quiz-form");

  if (timer && quizForm) {
    let remaining = Number.parseInt(timer.dataset.remaining || "0", 10);
    const value = document.getElementById("timer-value");
    const draw = () => {
      const minutes = Math.floor(Math.max(remaining, 0) / 60);
      const seconds = Math.max(remaining, 0) % 60;
      value.textContent = `${String(minutes).padStart(2, "0")}:${String(seconds).padStart(2, "0")}`;
      timer.classList.toggle("timer-urgent", remaining <= 60);
    };
    const submitExpired = () => {
      if (remaining <= 0 && !quizForm.dataset.submitted) {
        quizForm.dataset.submitted = "true";
        quizForm.requestSubmit();
      }
    };
    draw();
    submitExpired();
    const interval = window.setInterval(() => {
      remaining -= 1;
      draw();
      if (remaining <= 0) {
        window.clearInterval(interval);
        submitExpired();
      }
    }, 1000);
    quizForm.addEventListener("submit", () => {
      quizForm.dataset.submitted = "true";
      quizForm.querySelectorAll("button[type=submit]").forEach((button) => {
        button.disabled = true;
        button.textContent = "Submitting…";
      });
    });
  }

  document.querySelectorAll("form[data-confirm]").forEach((form) => {
    form.addEventListener("submit", (event) => {
      if (!window.confirm(form.dataset.confirm)) event.preventDefault();
    });
  });

  document.querySelectorAll(".option input[type=radio]").forEach((input) => {
    input.addEventListener("change", () => {
      document.querySelectorAll(`input[name="${CSS.escape(input.name)}"]`).forEach((radio) => {
        radio.closest(".option")?.classList.toggle("option-selected", radio.checked);
      });
    });
  });
})();
